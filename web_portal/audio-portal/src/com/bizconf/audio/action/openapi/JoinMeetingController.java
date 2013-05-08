package com.bizconf.audio.action.openapi;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.LanguageHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfOuter;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.JoinRandom;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ClientAPIService;
import com.bizconf.audio.service.ConfOuterService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.LongUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;


@ReqPath("join_mtg.asp")
public class JoinMeetingController  extends BaseController {
	private final Long MAX_EXPIRE_MILLIS= 10 * 60 * 1000L;
	private final Integer CONF_DURATION_DEFAULT = 120;
	private final Integer ERROR_CODE_1=1;//站点ID号为空或者是站点ID号不存在 
	private final Integer ERROR_CODE_2=2;//连接地址过期 
	private final Integer ERROR_CODE_3=3;//MtgKey为空,不能进入会议 
	private final Integer ERROR_CODE_4=4;//AuthId不正确,不能进入会议 
	@Autowired
	ConfService confService;
	@Autowired
	SiteService siteService;
	@Autowired
	ConfOuterService confOuterService;
	@Autowired
	ClientAPIService clientAPIService;
	
	
	@AsController(path = "")
	public Object join(HttpServletRequest request,HttpServletResponse response){
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
		String authId=request.getParameter("authId");
		
		
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
		String domain =   SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		try {
			CookieUtil.setPageCookie(response, ConstantUtil.COOKIE_LANG_KEY, langRes, domain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//验证站点是否存在 
		
		if(StringUtil.isEmpty(siteSign)){
			request.setAttribute("errorCode", ERROR_CODE_1);
			return  new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}

		//根据站点标识取到站点信息
		
		SiteBase siteBase=null;
		siteBase=siteService.getSiteBaseBySiteSign(siteSign);
		if(siteBase==null){
			request.setAttribute("errorCode", ERROR_CODE_1);
			return  new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		
		
		//验证URL地址中TimeStamp是否过期
		Calendar calendar=Calendar.getInstance();
		Long nowTimeStamp=calendar.getTimeInMillis();
		Long urlTimeStamp=LongUtil.parseLong(timestamp);
		if( (nowTimeStamp-urlTimeStamp) > MAX_EXPIRE_MILLIS ){
			request.setAttribute("errorCode", ERROR_CODE_2);
			return  new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		
		nowTimeStamp=null;
		urlTimeStamp=null;
		calendar=null;
		
		
		//验证AuthId是否正确 
		
		if (authId==null || "".equals(authId.trim())){
			request.setAttribute("errorCode", ERROR_CODE_4);
			return  new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		StringBuffer  authBuffer=new StringBuffer();
		authBuffer.append(""+ConstantUtil.JOIN_MTG_PUBLIC_KEY);
		authBuffer.append(""+siteSign);
		authBuffer.append(""+mtgKey);
		authBuffer.append(""+userId);
		authBuffer.append(""+userType);
		authBuffer.append(""+timestamp);
		String tmpAuthId=MD5.encodePassword(authBuffer.toString(), "MD5");
		if(!tmpAuthId.equalsIgnoreCase(authId)){
			request.setAttribute("errorCode", ERROR_CODE_4);
			return  new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		
		
		//验证MtgKey是否为空
		if(StringUtil.isEmpty(mtgKey)){
			request.setAttribute("errorCode", ERROR_CODE_3);
			return  new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		//验证mtgKey是否已经创建会议
		
		ConfOuter confOuter=null;
		
		confOuter=confOuterService.getConfBaseByMeyKeyAndSiteId(mtgKey,siteSign);
		Integer confId=0;
		ConfBase confBase=null;
		
		if(confOuter==null){//不存在 则创建一个新的会议 
			confOuter=new ConfOuter();
			confOuter.setSiteSign(siteSign);
			confOuter.setMtgKey(mtgKey);
			confOuter.setMtgTitle(mtgTitle);
			confOuter.setCreateTime(DateUtil.getGmtDate(null));
			confOuter.setUserName(userName);
			confOuter.setUserId(userId);
			confOuter.setUserType(IntegerUtil.parseIntegerWithDefaultZero(userType));
//			confOuter.set
			confBase=initConfBase(confOuter,siteBase);
			confBase.setDuration(dura);
			confBase=confService.saveConfBase(confBase);
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
		
		
		//进入会议
		
		
		UserBase currentUser=null;
//		if(ConfConstant.JOIN_TYPE_OURURL.equals(joinType)){
		currentUser=new UserBase();
//		userName=request.getParameter("userName");
//			userName=request
//			String userType=request.getParameter("userType");
//			String userId=request.getParameter("userId");
		currentUser.setId(IntegerUtil.parseIntegerWithDefaultZero(confOuter.getUserId())+200000000);
		currentUser.setTrueName(userName);
		currentUser.setClientName(userName);
			
//		}
		
		
		JoinRandom joinRandom=new JoinRandom();
		joinRandom.setConfId(confBase.getId());//,currentUser,userRole);
		joinRandom.setUserId(currentUser.getId());
		joinRandom.setUserEmail(currentUser.getUserEmail());
		joinRandom.setUserRole(8);
		joinRandom.setCreateTime(DateUtil.getGmtDate(null));
		joinRandom.setLanguage(LanguageHolder.getCurrentLanguage());
		joinRandom=clientAPIService.saveRandom(joinRandom);
		if(joinRandom==null || joinRandom.getId()<= 0){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		
		String preParam=clientAPIService.makePreParam(joinRandom);
		request.setAttribute("preParam", preParam);
		request.setAttribute("cId", confBase.getId());
		request.setAttribute("rId", joinRandom.getId());
		request.setAttribute("userType", userType);
		
		return new ActionForward.Forward("/jsp/common/join_plug.jsp");
		
		
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
			Date startTime=DateUtil.getGmtDate(null);
			Date endTime=DateUtil.addDateMinutes(startTime,CONF_DURATION_DEFAULT);
			confBase.setStartTime(startTime);
			confBase.setDuration(CONF_DURATION_DEFAULT);
			confBase.setEndTime(endTime);
			confBase.setCompereUser(0);
			confBase.setCompereName("");
			confBase.setMaxUser(siteBase.getLicense());
			Calendar calendar=Calendar.getInstance();
			calendar.clear();
			calendar.set(1970, 0, 1);
			confBase.setDelTime(calendar.getTime());
			DefaultConfig defaultConfig=ConfConstant.DEFAULT_CONFIG_FOR_OUTER;
			confBase.setMaxVideo(defaultConfig.getMaxVideo());
			confBase.setMaxAudio(defaultConfig.getMaxAudio());
			confBase.setMaxDpi(defaultConfig.getMaxDpi());
			confBase.setDefaultDpi(defaultConfig.getDefaultDpi());
			confBase.setOpenIpad(defaultConfig.getOpenIpad());
			confBase.setAheadTime(defaultConfig.getAheadTimes());
			confBase.setPriviBits(defaultConfig.getPriviBits());
			confBase.setClientConfig(defaultConfig.getClientConfig());
			confBase.setFuncBits(defaultConfig.getFuncBits());
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
	
	
	
	
}
