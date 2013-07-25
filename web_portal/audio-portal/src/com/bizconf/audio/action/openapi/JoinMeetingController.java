package com.bizconf.audio.action.openapi;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.LanguageHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfOuter;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.JoinRandom;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteStatusInterceptor;
import com.bizconf.audio.service.ClientAPIService;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.ConfOuterService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.LongUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.Base64;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;


@ReqPath({"/join_mtg.asp","join_mtg.asp"})
@Interceptors(SiteStatusInterceptor.class)
public class JoinMeetingController  extends BaseController {
	private final Logger logger=Logger.getLogger(JoinMeetingController.class);
	private final Long MAX_EXPIRE_MILLIS=  20 * 30 * 1000L;
	private final Integer CONF_DURATION_DEFAULT = 120;
	private final Integer ERROR_CODE_101=101;//站点ID号为空或者是站点ID号不存在 
	private final Integer ERROR_CODE_102=102;//连接地址过期 
	private final Integer ERROR_CODE_103=103;//MtgKey为空,不能进入会议 
	private final Integer ERROR_CODE_104=104;//AuthId不正确,不能进入会议 
	private final Integer ERROR_CODE_105=105;//主持人密码为空,不能进入会议
	private final Integer ERROR_CODE_106=106;//会议密码为空,不能进入会议
	private final String USER_TYPE_HOSTER="1";//主持人
	private final String USER_TYPE_SPEAKER="2";//主讲人
	private final String USER_TYPE_HOSTER_SPEAKER="3";//主持人+主讲人
	private final String USER_TYPE_PARTICIPANTS="8";//普通与会者

	@Autowired
	ConfService confService;
	@Autowired
	SiteService siteService;
	@Autowired
	ConfOuterService confOuterService;
	@Autowired
	ClientAPIService clientAPIService;
	@Autowired
	ConfManagementService confManagementService;
	
	@Autowired
	EmpowerConfigService empowerConfigService;


	
	
	@AsController(path = "")
	public Object join(HttpServletRequest request,HttpServletResponse response){
//		long logRandom=Math.round(Math.random()*99999999);
		
		String siteSign=request.getParameter("siteId");
		String language=request.getParameter("language");
		String duration=request.getParameter("duration");
		Integer dura=0;
		if(!StringUtil.isEmpty(duration)){
			dura=IntegerUtil.parseInteger(duration);
		}
		String mtgTitle=unicode2String(request.getParameter("mtgTitle"));
//		try {
//			mtgTitle=new String(request.getParameter("mtgTitle").getBytes("ISO-8859-1"), "GB2312");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		String mtgKey=request.getParameter("mtgKey");
		String userName=unicode2String(request.getParameter("userName"));
//		try {
//			userName=new String(request.getParameter("userName").getBytes("ISO-8859-1"), "GB2312");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		String userId=request.getParameter("userId");
		String userType=request.getParameter("userType");
		String timestamp=request.getParameter("timestamp");
		String hostPwd=request.getParameter("hostPwd");
		
		String mtgPwd=request.getParameter("mtgPwd");
		String authId=request.getParameter("authId");
		String debug=request.getParameter("debug");
		
		logger.info("siteSign=" + siteSign + ",language=" + language
				+ ",duration=" + duration + ",mtgTitle=" + mtgTitle + ",mtgKey="
				+ mtgKey + ",userName=" + userName + ",userId=" + userId
				+ ",userType=" + userType + ",timestamp=" + timestamp
				+ ",hostPwd=" + hostPwd + ",mtgPwd=" + mtgPwd + ",authId=" + authId);

		if(!StringUtil.isEmpty(debug) && ("yes".equals(debug.trim().toLowerCase()) || "1".equals(debug.trim().toLowerCase()))){
			request.setAttribute("siteSign", siteSign);
			request.setAttribute("language", language);
			request.setAttribute("duration", duration);
			request.setAttribute("mtgTitle", mtgTitle);
			request.setAttribute("mtgKey", mtgKey);
			request.setAttribute("userName", userName);
			request.setAttribute("userId", userId);
			request.setAttribute("userType", userType);
			request.setAttribute("timestamp", timestamp);
			request.setAttribute("hostPwd", hostPwd);
			request.setAttribute("mtgPwd", mtgPwd);
			request.setAttribute("authId", authId);
			return  new ActionForward.Forward("/jsp/common/join_msg_debug.jsp");
		}
		
		
		//设置语言
		if(language==null || "".equals(language.trim())){
			language="0";
		}
		String langRes="zh-cn";
		for(String[] eachLang: ConstantUtil.CLIENT_LANGUAGE){
			if(eachLang!=null && eachLang.length==2){
				if(eachLang[0].equals(language)){
					langRes=eachLang[1];
				}
			}
		}
		String mainDomain=SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		String domain = SiteIdentifyUtil.getCurrentDomine();
		request.setAttribute("domain", domain);
		try {
			CookieUtil.setPageCookie(response, ConstantUtil.COOKIE_LANG_KEY, langRes, mainDomain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//验证站点是否存在 
		
		if(StringUtil.isEmpty(siteSign)){
			request.setAttribute("errorCode", ERROR_CODE_101);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}

		//根据站点标识取到站点信息
		
		SiteBase siteBase=null;
		siteBase=siteService.getSiteBaseBySiteSign(siteSign);
		if(siteBase==null){
			request.setAttribute("errorCode", ERROR_CODE_101);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
		
		
		
		//验证URL地址中TimeStamp是否过期
		Calendar calendar=Calendar.getInstance();
		Long nowTimeStamp=calendar.getTimeInMillis();
		Long urlTimeStamp=LongUtil.parseLong(timestamp);
		if( (nowTimeStamp-urlTimeStamp) > MAX_EXPIRE_MILLIS ){
			request.setAttribute("errorCode", ERROR_CODE_102);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
		
		
		nowTimeStamp=null;
		urlTimeStamp=null;
		calendar=null;
		
		
		//验证AuthId是否正确 
		
		if (authId==null || "".equals(authId.trim())){
			request.setAttribute("errorCode", ERROR_CODE_104);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
		StringBuffer  authBuffer=new StringBuffer();
		authBuffer.append(""+ConstantUtil.JOIN_MTG_PUBLIC_KEY);
		authBuffer.append(""+siteSign);
		authBuffer.append(""+mtgKey);
		authBuffer.append(""+userId);
		authBuffer.append(""+userType);
		authBuffer.append(""+timestamp);
		
		
		String tmpAuthId=MD5.encodePassword(authBuffer.toString(), "MD5");
		logger.info("tmpAuthId=" + tmpAuthId);
		if(!tmpAuthId.equalsIgnoreCase(authId)){
			request.setAttribute("errorCode", ERROR_CODE_104);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
		
		
		
		//验证MtgKey是否为空
		if(StringUtil.isEmpty(mtgKey)){
			request.setAttribute("errorCode", ERROR_CODE_103);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
	
		if(StringUtil.isEmpty(hostPwd)){
			request.setAttribute("errorCode", ERROR_CODE_105);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
		
		if(StringUtil.isEmpty(mtgPwd)){
			request.setAttribute("errorCode", ERROR_CODE_106);
			return  new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
		hostPwd=Base64.decode(hostPwd.replaceAll("_","/"),"utf8");
		if(!StringUtil.isEmpty(hostPwd)){
			hostPwd=getPassWordByBase64(hostPwd);
		}
		
		mtgPwd=Base64.decode(mtgPwd.replaceAll("_","/"),"utf8");
		if(!StringUtil.isEmpty(hostPwd)){
			mtgPwd=getPassWordByBase64(mtgPwd);
		}

		logger.info("hostPwd=" + hostPwd);
		logger.info("mtgPwd=" + mtgPwd);
		//验证mtgKey是否已经创建会议
		
		ConfOuter confOuter=null;
		
		confOuter=confOuterService.getConfBaseByMeyKeyAndSiteId(mtgKey,siteSign);
		Integer confId=0;
		ConfBase confBase=null;
		
		if(confOuter==null){//不存在 则创建一个新的会议 
			confOuter=new ConfOuter();
			confOuter.setSiteSign(siteSign);
			confOuter.setMtgKey(mtgKey);
			confOuter.setHostPwd(hostPwd);
			confOuter.setMtgPwd(mtgPwd);
			confOuter.setMtgTitle(mtgTitle);
			confOuter.setCreateTime(DateUtil.getGmtDate(null));
			confOuter.setUserName(userName);
			confOuter.setUserId(userId);
			confOuter.setUserType(IntegerUtil.parseIntegerWithDefaultZero(userType));
			confBase=initConfBase(confOuter,siteBase);
			confBase.setDuration(dura);
			UserBase userBase=new UserBase();
			userBase.setId(IntegerUtil.parseInteger(userId));
			userBase.setTrueName(userName);
			userBase.setMobile("");
			userBase.setUserEmail("");
			confBase=confService.saveConfBaseForOuter(confBase, siteBase, userBase);//.saveConfBase(confBase);
			if(confBase!=null && confBase.getId()!=null && confBase.getId().intValue() >0 ){
				confId=confBase.getId();
			}
			confOuter.setConfId(confId);
			confOuter=confOuterService.saveConfOuter(confOuter);
			
		}
		
		if(confOuter!=null && confOuter.getId() !=null && confOuter.getId().intValue() >0  && confId.intValue() <= 0){
//			confBase=
			confId=confOuter.getConfId();
			confBase=confService.getConfBasebyConfId(confId);
		}
		logger.info("confOuter=" + confOuter);
		logger.info("confBase=" + confBase);
		
//		if(confBase==null || confBase.getConfStatus().intValue() >ConfConstant.CONF_STATUS_OPENING.intValue()){
//			confOuter=new ConfOuter();
//			confOuter.setSiteSign(siteSign);
//			confOuter.setMtgKey(mtgKey);
//			confOuter.setMtgTitle(mtgTitle);
//			confOuter.setCreateTime(DateUtil.getGmtDate(null));
//			confOuter.setUserName(userName);
//			confOuter.setUserId(userId);
//			confOuter.setUserType(IntegerUtil.parseIntegerWithDefaultZero(userType));
////			confOuter.set
//			confBase=initConfBase(confOuter,siteBase);
//			confBase.setDuration(dura);
//			UserBase userBase=new UserBase();
//			userBase.setId(IntegerUtil.parseInteger(userId));
//			userBase.setTrueName(userName);
//			userBase.setMobile("");
//			userBase.setUserEmail("");
//			confBase=confService.saveConfBaseForOuter(confBase, siteBase, userBase);//.saveConfBase(confBase);
//			if(confBase!=null && confBase.getId()!=null && confBase.getId().intValue() >0 ){
//				confId=confBase.getId();
//			}
//			confOuter.setConfId(confId);
//			confOuter=confOuterService.saveConfOuter(confOuter);
//		}
//		if(confBase!=null && confBase.getId().intValue() > 0 
//				&& (ConfConstant.CONF_STATUS_SUCCESS.equals(confBase.getConfStatus()) 
//						|| ConfConstant.CONF_STATUS_OPENING.equals(confBase.getConfStatus()))){
//			
//			
//			
//		}
		
		


		//验证在线人数与站点的License数
//		int siteLicCount = 1601;
//		siteLicCount=siteService.queryASSiteInfo(siteBase.getSiteSign()).getLicense();
//		confManagementService.setingOnlineUserNum(confBase);
//		System.out.println("confBase.pcNum="+confBase.getPcNum());
//		System.out.println("siteLicCount="+siteLicCount);
//		if (true/*confBase.getPcNum() >= (siteLicCount-1)*/) {
//			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_11);
//			return new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
//		}
//	
		
		
		//进入会议
		
		
		UserBase currentUser=null;
//		if(ConfConstant.JOIN_TYPE_OURURL.equals(joinType)){
		currentUser=new UserBase();
//		userName=request.getParameter("userName");
//			userName=request
//			String userType=request.getParameter("userType");
//			String userId=request.getParameter("userId");
		
		int uId=IntegerUtil.parseIntegerWithDefaultZero(userId);
		if(uId<=0){
			uId=Integer.parseInt(Math.round(Math.random()*99999999)+"");
		}
		currentUser.setId(uId+200000000);
		currentUser.setTrueName(userName);
		currentUser.setClientName(userName);
		
		logger.info("currentUser=" + currentUser);
//		}
		
		
		JoinRandom joinRandom=new JoinRandom();
		joinRandom.setConfId(confBase.getId());//,currentUser,userRole);
		joinRandom.setUserId(currentUser.getId());
		joinRandom.setUserEmail(currentUser.getUserEmail());
		joinRandom.setUserName(userName);
		joinRandom.setUserRole(IntegerUtil.parseIntegerWithDefaultZero(userType));
		joinRandom.setCreateTime(DateUtil.getGmtDate(null));
		joinRandom.setLanguage(LanguageHolder.getCurrentLanguage());
		joinRandom.setClientIp(StringUtil.getIpAddr(request));
		joinRandom=clientAPIService.saveRandom(joinRandom);
		if(joinRandom==null || joinRandom.getId()<= 0){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
			return new ActionForward.Forward("/jsp/common/join_msg_outer.jsp");
		}
		logger.info("joinRandom=" + joinRandom);
		boolean startStatus=true;
		if(confBase.getStartTime().after(DateUtil.getGmtDate(null)) && ConfConstant.CONF_STATUS_SUCCESS.equals(confBase.getConfStatus())){
				startStatus=confManagementService.startConf(confBase, null, currentUser);
				if(startStatus){
					confService.updateStartTime(confBase, DateUtil.getGmtDate(null));
					confService.updateConfStatus(confBase,ConfConstant.CONF_STATUS_OPENING);
					
				}
		}
//		startStatus=confManagementService.startConf(confBase, null, currentUser);

		String userIp=StringUtil.getIpAddr(request);
		String preParam=clientAPIService.makePreParam(joinRandom,userIp);
		logger.info("preParam=" + preParam);
		logger.info("cId=" + confBase.getId());
		logger.info("rId=" + joinRandom.getId());
		logger.info("userType=" + userType);
		request.setAttribute("preParam", preParam);
		request.setAttribute("cId", confBase.getId());
		request.setAttribute("rId", joinRandom.getId());
		request.setAttribute("userType", userType);
		
		return new ActionForward.Forward("/jsp/common/join_plug_outer.jsp");
		
		
//		StringBuffer joinBuffer=new StringBuffer();
//		joinBuffer.append("/join?joinType"+ConfConstant.JOIN_TYPE_OURURL);
//		joinBuffer.append("&cId="+confId);
//		joinBuffer.append("&userName="+userName);
//		joinBuffer.append("&userId="+(IntegerUtil.parseIntegerWithDefaultZero(confOuter.getUserId())+200000000));
//		joinBuffer.append("&userType="+userType);
//		
//		return  new ActionForward.Redirect(joinBuffer.toString());
		
	}
	
	private ConfBase initConfBase(ConfOuter confOuter,SiteBase siteBase){
		ConfBase confBase =null;//new ConfBase();
		if(confOuter!=null && siteBase!=null){
			confBase=new ConfBase();
			confBase.setConfName(confOuter.getMtgTitle());
			confBase.setConfDesc(confOuter.getMtgTitle());
			confBase.setConfStatus(ConfConstant.CONF_STATUS_SUCCESS);
			confBase.setDelFlag(ConstantUtil.DELFLAG_UNDELETE);
			confBase.setPublicFlag(ConfConstant.CONF_PUBLIC_FLAG_FALSE);
			confBase.setCreateTime(DateUtil.getGmtDate(null));
			confBase.setSiteId(siteBase.getId());
			confBase.setCreateUser(0);
			if("1".equals(confOuter.getUserType())){
				confBase.setCreateUser((IntegerUtil.parseIntegerWithDefaultZero(confOuter.getUserId())+200000000));
			}
			confBase.setCycleId(0);
			confBase.setPublicConfPass("");
			confBase.setConfType(3);
			confBase.setHostKey(confOuter.getHostPwd());
			Date startTime=DateUtil.addDateMinutes(DateUtil.getGmtDate(null),10);
			Date endTime=DateUtil.addDateMinutes(startTime,CONF_DURATION_DEFAULT);
			confBase.setStartTime(startTime);
			confBase.setDuration(CONF_DURATION_DEFAULT);
			confBase.setEndTime(endTime);
			confBase.setCompereUser(0);
			confBase.setCompereName("");
			confBase.setMaxUser(2);
			Calendar calendar=Calendar.getInstance();
			calendar.clear();
			calendar.set(1970, 0, 1);
			confBase.setDelTime(calendar.getTime());
			
			
			/*
			 *
			 * */
			EmpowerConfig siteGlobalConfig=empowerConfigService.getSiteEmpowerGlobalBySiteId(siteBase.getId());
			EmpowerConfig siteEmpowerConfig=empowerConfigService.getSiteEmpowerConfigBySiteId(siteBase.getId());
			EmpowerConfig empowerConfig=empowerConfigService.makeEmpowerForUser(siteEmpowerConfig, siteGlobalConfig);
			
			UserBase tempUserBase=new UserBase();
			tempUserBase.setId(0);
			tempUserBase.setSiteId(0);
			DefaultConfig systemDefaultConfig=confService.getDefaultConfig(tempUserBase);
			tempUserBase.setSiteId(siteBase.getId());
			DefaultConfig siteDefaultConfig=confService.getDefaultConfig(tempUserBase);
			if(siteDefaultConfig==null){
				siteDefaultConfig=systemDefaultConfig;
				if(siteDefaultConfig.getAheadTimes()==null || siteDefaultConfig.getAheadTimes()<=0){
					siteDefaultConfig.setAheadTimes(10);
				}
			}
			int videoNum=0;
			int audioNum=1;
			if(empowerConfig != null){
				String clientConfig=siteDefaultConfig.getClientConfig();
				char[] clientConfigChars=clientConfig.toCharArray();
				clientConfigChars[7]='0';
				clientConfigChars[8]='0';
				clientConfigChars[12]='0';
				clientConfigChars[21]='0';
				if(SiteConstant.EMPOWER_ENABLED.equals(empowerConfig.getVideoFlag())){
					clientConfigChars[7]='1';
					videoNum=empowerConfig.getVideoNumber();
				}
				if(SiteConstant.EMPOWER_ENABLED.equals(empowerConfig.getAudioFlag())){
					clientConfigChars[8]='1';
					audioNum=empowerConfig.getAudioNumber();
				}
				if(SiteConstant.EMPOWER_ENABLED.equals(empowerConfig.getShareMediaFlag())){
					clientConfigChars[21]='1';
				}
				if(SiteConstant.EMPOWER_ENABLED.equals(empowerConfig.getRecordFlag())){
					clientConfigChars[12]='1';
				}
				siteDefaultConfig.setClientConfig(new String(clientConfigChars));
			}
			
//			DefaultConfig defaultConfig=ConfConstant.DEFAULT_CONFIG_FOR_OUTER;
			confBase.setMaxVideo(videoNum);
			confBase.setMaxAudio(audioNum);
			confBase.setMaxDpi(siteDefaultConfig.getMaxDpi());
			confBase.setDefaultDpi(siteDefaultConfig.getDefaultDpi());
			confBase.setOpenIpad(siteDefaultConfig.getOpenIpad());
			confBase.setAheadTime(siteDefaultConfig.getAheadTimes());
			confBase.setPriviBits(siteDefaultConfig.getPriviBits());
			confBase.setClientConfig(siteDefaultConfig.getClientConfig());
			confBase.setFuncBits(siteDefaultConfig.getFuncBits());
			confBase.setTimeZone(siteBase.getTimeZone());
			confBase.setTimeZoneId(siteBase.getTimeZoneId());
			confBase.setPermanentConf(ConfConstant.CONF_PERMANENT_UNABLE);
			confBase.setBelongConfId(0);
			confBase.setPcNum(0);
			confBase.setPhoneNum(0);
			
		}
		return confBase;
		
	}
	
	
	private String unicode2String(String unicode){
		StringBuffer buffer=new StringBuffer();;
		if(!StringUtil.isEmpty(unicode)){
			String[]  unicodeArr=unicode.split(",");
			if(unicodeArr!=null && unicodeArr.length > 0){
				for(String eachCode:unicodeArr){
					buffer.append((char)IntegerUtil.parseInteger(eachCode).intValue());
				}
			}
			unicodeArr=null;
		}
		return buffer.toString();
	}
	
	private String getPassWordByBase64(String pwd){
		if(StringUtil.isEmpty(pwd)){
			return "";
		}
		StringBuffer password=new StringBuffer();
		int pwdLen=pwd.length();
		char[] pwdChars=pwd.toCharArray();
		for(int ii=0;ii < pwdLen;ii++){
			if(ii%2==0){
				password.append(String.valueOf(pwdChars[ii]));
			}
			
		}
		pwdChars=null;
		return password.toString();
		
	}
	
	
}
