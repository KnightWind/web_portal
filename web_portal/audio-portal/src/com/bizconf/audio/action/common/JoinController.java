package com.bizconf.audio.action.common;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.LanguageHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.JoinRandom;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteStatusInterceptor;
import com.bizconf.audio.service.ClientAPIService;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.DownLoadUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.utils.LiberContainer;

@ReqPath("/join")
@Interceptors(SiteStatusInterceptor.class)
public class JoinController extends BaseController {
	private final Logger logger = Logger.getLogger(JoinController.class);
	
	

	@Autowired
	ConfService confService;
	@Autowired
	UserService userService;
	@Autowired
	SiteService siteService;
	@Autowired
	ClientAPIService clientAPIService;
	
	@Autowired
	ConfManagementService confManagementService;
	
	/**
	 * 
	 * @param joinType
	 * @param request
	 * @return
	 */
	@AsController(path = "joinpage")
	public Object joinPage(@CParam("joinType") String joinType,@CParam("cId") String cId,@CParam("uId") int uId,HttpServletRequest request){
		UserBase currentUser=null;
		Integer confStatus;
		currentUser=userService.getCurrentUser(request);
		request.setAttribute("currentUser",currentUser);
		request.setAttribute("joinType",joinType);
		request.setAttribute("domain",SiteIdentifyUtil.getCurrentDomine());
		if (StringUtil.isEmpty(joinType) ) {
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_1);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		Integer joinTypeNum=IntegerUtil.parseInteger(joinType);
		if(joinType==null || joinTypeNum.intValue()<ConfConstant.JOIN_TYPE_CONFID || joinTypeNum.intValue() >ConfConstant.JOIN_TYPE_OURURL){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_0);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}

		if(ConfConstant.JOIN_TYPE_CONFID.equals(joinTypeNum)){
			if(StringUtil.isEmpty(cId)){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_2);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			Integer confId=IntegerUtil.parseInteger(cId);
			ConfBase confBase = null;
			if(cId!=null && confId!=null && confId.intValue()>0){
				confBase=confService.getConfBasebyConfId(confId);
			}
			if(confBase==null){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_2);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
		
//			confStatus=confBase.getConfStatus();
//			if(ConfConstant.confStatus==null ){
//				
//			}
			if(checkTooEarly(confBase)){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_7);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			
			Integer publicFlag=confBase.getPublicFlag();
			if(ConfConstant.CONF_PUBLIC_FLAG_TRUE.equals(publicFlag)){//如果是公开会议，则必须输入密码
				String confPass=confBase.getPublicConfPass();
				if(!StringUtil.isEmpty(confPass)){
					request.setAttribute("passCheck", 1);
					return new ActionForward.Forward("/jsp/common/join_page.jsp");
				}
				if(currentUser==null){
					request.setAttribute("passCheck", 0);
					return new ActionForward.Forward("/jsp/common/join_page.jsp");
				}
				return new ActionForward.Redirect("/join?cId="+cId+"&joinType="+joinType);//.Forward("/jsp/common/join_public.jsp");
			}else{
				//如果不是公开 会议验证用户是否登录，如果登录，则直接进入会议,未登录时，去登录
				if(currentUser!=null && currentUser.getId()!=null && currentUser.getId().intValue() >0){
					return new ActionForward.Redirect("/join?cId="+cId+"&joinType="+joinType);//.Forward("/jsp/common/join_public.jsp");
				}
				return new ActionForward.Forward("/jsp/user/login.jsp");
			}
		}
		if(ConfConstant.JOIN_TYPE_SECURE_CODE.equals(joinTypeNum)){
			return new ActionForward.Forward("/jsp/common/join_page.jsp");
		}
		if(ConfConstant.JOIN_TYPE_EMAIL.equals(joinTypeNum)){
			if(uId>0){
				currentUser=userService.getUserBaseById(uId);
			}else{
				currentUser=new UserBase();
				currentUser.setId(0);
			}
			
			Integer confId=IntegerUtil.parseInteger(cId);
			ConfBase confBase = null;
			if(cId!=null && confId!=null && confId.intValue()>0){
				confBase=confService.getConfBasebyConfId(confId);
			}
			if(confBase==null){
				return  ConfConstant.JOIN_ERROR_CODE_2;
			}
			if(checkTooEarly(confBase)){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_7);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			String scode=request.getParameter("scode");
			if(scode==null || "".equals(scode.trim())){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_6);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			request.setAttribute("cId",cId);
			request.setAttribute("uId",uId);
			request.setAttribute("scode",scode);
			request.setAttribute("currentUser", null);
			if(uId<=0){
				return new ActionForward.Forward("/jsp/common/join_page.jsp");
			}else{
				return new ActionForward.Redirect("/join?joinType="+ConfConstant.JOIN_TYPE_EMAIL+"&scode="+scode+"&cId="+cId+"&uId="+uId);
			}
		}
		if(ConfConstant.JOIN_TYPE_OURURL.equals(joinTypeNum)){
			
		}
		return new ActionForward.Forward("/jsp/common/join_page.jsp");
		/*
		

		
		boolean isLogin=false;
		if(currentUser != null && currentUser.getId() != null && currentUser.getId().intValue() > 0 ){
			isLogin=true;
			request.setAttribute("currentUser", currentUser);
		}
		request.setAttribute("isLogin", isLogin);
		
		if (StringUtil.isEmpty(joinType) ) {
			return ConfConstant.JOIN_ERROR_CODE_1;
		}
		Integer joinTypeNum=IntegerUtil.parseInteger(joinType);
		
		if(joinType==null || joinTypeNum.intValue()<ConfConstant.JOIN_TYPE_CONFID || joinTypeNum.intValue() >ConfConstant.JOIN_TYPE_OURURL){
			return ConfConstant.JOIN_ERROR_CODE_1;
		}
		
		if(joinTypeNum==ConfConstant.JOIN_TYPE_CONFID){//通过会议ID号进入会议
			Integer confId=IntegerUtil.parseInteger(cId);
			ConfBase confBase = null;
			if(cId!=null && confId!=null && confId.intValue()>0){
				confBase=confService.getConfBasebyConfId(confId);
			}
			if(confBase==null){
				return  ConfConstant.JOIN_ERROR_CODE_2;
			}
			
			if(checkTooEarly(confBase)){
				request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_7);
				return new ActionForward.Forward("/jsp/common/join_early.jsp");
			}
			
			Integer publicFlag=confBase.getPublicFlag();
			
			if(publicFlag.equals(ConfConstant.CONF_PUBLIC_FLAG_TRUE)){//如果是公开会议，则必须输入密码
				
				
				
				String confPass=confBase.getPublicConfPass();
				if(confPass!=null && !"".equals(confPass.trim())){
					return new ActionForward.Forward("/jsp/common/join_public.jsp");
				}
				return new ActionForward.Redirect("/join?cId="+cId+"&joinType="+joinType);//.Forward("/jsp/common/join_public.jsp");
			}else{
				//如果不是公开 会议验证用户是否登录，如果登录，则直接进入会议,未登录时，去登录
				if(currentUser!=null && currentUser.getId() >0){
					return new ActionForward.Redirect("/join?cId="+cId+"&joinType="+joinType);//.Forward("/jsp/common/join_public.jsp");
				}
				return new ActionForward.Forward("/jsp/user/login.jsp");
			}
		}
		if(ConfConstant.JOIN_TYPE_SECURE_CODE.equals(joinTypeNum)){
			return new ActionForward.Forward("/jsp/common/join_secure.jsp");
		}
		
		if(ConfConstant.JOIN_TYPE_EMAIL.equals(joinTypeNum)){
			Integer confId=IntegerUtil.parseInteger(cId);
			ConfBase confBase = null;
			if(cId!=null && confId!=null && confId.intValue()>0){
				confBase=confService.getConfBasebyConfId(confId);
			}
			if(confBase==null){
				return  ConfConstant.JOIN_ERROR_CODE_2;
			}
			if(checkTooEarly(confBase)){
				request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_7);
				return new ActionForward.Forward("/jsp/common/join_early.jsp");
			}
			String scode=request.getParameter("scode");
			if(scode==null || "".equals(scode.trim())){
//				secureCode=
				request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_6);
				return new ActionForward.Forward("/jsp/common/join_email.jsp");
			}
			
			request.setAttribute("cId",cId);
			request.setAttribute("scode",scode);
			request.setAttribute("currentUser", null);
			return new ActionForward.Forward("/jsp/common/join_email.jsp");
			
		}
		if(ConfConstant.JOIN_TYPE_OURURL.equals(joinTypeNum)){
			
			
		}
		
		
		
//		ConfBase confBase = null;
//		Integer confId = 0;
//		if (!StringUtil.isEmpty(cId)) {
//			confId = IntegerUtil.parseInteger(cId);
//			if (confId != null && confId.intValue() > 0) {
//				confBase = confService.getConfBasebyConfId(confId);
//			}
//			if(confBase == null){
//				return JOIN_ERROR_CODE_2;
//			}
//		}
		return "Join Error : JoinType Error";
		
		*/
	}
	
	/**
	 * 通过ID号或者是安全会议码加入会议
	 * @param cId
	 * @param code
	 * @param cPass
	 * @param request
	 * @return
	 */
	@AsController(path = "")
	public Object joinMeeting(@CParam("cId") String cId,@CParam("cPass") String cPass,@CParam("code") String code,@CParam("userName") String userName,@CParam("rId") int rId,@CParam("uId") int uId,HttpServletRequest request){
		
		UserBase currentUser=null;
		currentUser=userService.getCurrentUser(request);
		boolean isLogin=false;
		request.setAttribute("cId", cId);
		request.setAttribute("userName", userName);
		request.setAttribute("domain",SiteIdentifyUtil.getCurrentDomine());
		if(currentUser!=null && currentUser.getId() !=null && currentUser.getId().intValue() > 0 ){
			isLogin=true;
			request.setAttribute("currentUser", currentUser);
		}

		ConfBase perConf=null;
		String userIp=StringUtil.getIpAddr(request);
		request.setAttribute("isLogin", isLogin);
		String reload=request.getParameter("reload");
		logger.info("reload=="+reload);
		if(!StringUtil.isEmpty(reload)){
			request.setAttribute("reload", reload);	
			request.setAttribute("code", code);	
			request.setAttribute("cPass", cPass);
			request.setAttribute("scode", request.getParameter("scode"));
			request.setAttribute("userId", request.getParameter("userId"));
			request.setAttribute("userType", request.getParameter("userType"));
			if(rId>0){
				JoinRandom joinRandom=clientAPIService.getJoinRandomById(rId);
				if(joinRandom==null || joinRandom.getId()<= 0){
					request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
					return new ActionForward.Forward("/jsp/common/join_msg.jsp");
				}
				
				String preParam=clientAPIService.makePreParam(joinRandom,userIp);
				
				request.setAttribute("preParam", preParam);
			}
			return new ActionForward.Forward("/jsp/common/join_plug.jsp");
			
		}
		
		if (StringUtil.isEmpty(cId) && StringUtil.isEmpty(code)) {
//			request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_1);
//			return "会议方式 错误 ";
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_1);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		Integer joinType=0;
		joinType=IntegerUtil.parseInteger(request.getParameter("joinType"));
		request.setAttribute("joinType",joinType);
		
		
		ConfBase confBase = null;
		SiteBase siteBase=null;
		String siteSign=SiteIdentifyUtil.getCurrentBrand();
		siteBase=siteService.getSiteBaseBySiteSign(siteSign);
		
		if(siteBase==null){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_9);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		int userRole=8;
		
		if(ConfConstant.JOIN_TYPE_CONFID.equals(joinType)){
			Integer confId = 0;
			if (!StringUtil.isEmpty(cId)) {
				confId = IntegerUtil.parseInteger(cId);
				if (confId != null && confId.intValue() > 0) {
					confBase = confService.getConfBasebyConfId(confId);
				}
			}
			if(confBase == null ){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_2);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			
			
			Integer permanentConf = confBase.getPermanentConf();
			if(ConfConstant.CONF_PERMANENT_ENABLED_MAIN.equals(permanentConf)){
				perConf=confBase;
				ConfBase childConf=confService.getPermanentChildConf(confBase.getId());
				if(childConf==null){
					childConf=confService.createChildConf(confBase);
				}
				if(childConf==null){
					request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_10);
					return new ActionForward.Forward("/jsp/common/join_msg.jsp");
				}
				confBase=childConf;
			}
			
			
			
//			if(checkTooEarly(confBase)){
//				request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_7);
//				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
//			}
			
			//验证会议是否公开会议,同时验证会议密码
			Integer publicFlag=confBase.getPublicFlag();
			String publicPass=confBase.getPublicConfPass();
			if(ConfConstant.CONF_PUBLIC_FLAG_TRUE .equals(publicFlag)){
				if(!StringUtil.isEmpty(publicPass)){
					if((cPass==null || "".equals(cPass.trim()))){
						return new ActionForward.Forward("/jsp/common/join_page.jsp");
					}
					if(!cPass.equals(publicPass)){
						request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_5);
						return new ActionForward.Forward("/jsp/common/join_msg.jsp");
					}
				}
			}
			
			Integer confCreator=confBase.getCreateUser();
			if(isLogin && currentUser.getId()!=null && confCreator!=null && confCreator.intValue() > 0 && currentUser.getId().equals(confCreator)){
				userRole = 3;
			}
		}

		if(ConfConstant.JOIN_TYPE_SECURE_CODE.equals(joinType)){
			if(StringUtil.isEmpty(code)){
				return new ActionForward.Forward("/jsp/common/join_page.jsp");
			}
			//参会者安全会议号
			confBase = confService.getConfBaseByUserSecure(code);
			if (confBase == null) {
				//主持人安全会议号
				confBase = confService.getConfBaseByCompereSecure(code);
			}
			
			if(confBase==null || (siteBase.getId()!=null && !siteBase.getId().equals(confBase.getSiteId())) ){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_3);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			
			perConf=confBase;
			
			Integer permanentConf = confBase.getPermanentConf();
			if(ConfConstant.CONF_PERMANENT_ENABLED_MAIN.equals(permanentConf)){
				ConfBase childConf=confService.getPermanentChildConf(confBase.getId());
				if(childConf==null){
					childConf=confService.createChildConf(confBase);
				}
				if(childConf==null){
					request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_10);
					return new ActionForward.Forward("/jsp/common/join_msg.jsp");
				}
				confBase=childConf;
			}
			
			
		}

		if(ConfConstant.JOIN_TYPE_EMAIL.equals(joinType)){
			if(uId>0){
				currentUser=userService.getUserBaseById(uId);
			}else{
				currentUser=new UserBase();
				currentUser.setId(0);
				currentUser.setTrueName(userName);
				currentUser.setClientName(userName);
			}
			code=request.getParameter("scode");
			if(StringUtil.isEmpty(code)){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			//参会者安全会议号
			confBase = confService.getConfBaseByUserSecure(code);
			if (confBase == null) {
				//主持人安全会议号
				confBase = confService.getConfBaseByCompereSecure(code);
			}

			if(confBase==null){
				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_6);
				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
			}
			
			perConf=confBase;
			//验证永久会议信息
			Integer permanentConf = confBase.getPermanentConf();
			if(ConfConstant.CONF_PERMANENT_ENABLED_MAIN.equals(permanentConf)){
				
				ConfBase childConf=confService.getPermanentChildConf(confBase.getId());
				if(childConf==null){
					childConf=confService.createChildConf(confBase);
				}
				if(childConf==null){
					request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_10);
					return new ActionForward.Forward("/jsp/common/join_msg.jsp");
				}
				confBase=childConf;
			}
			
			
		}
		
		if(ConfConstant.JOIN_TYPE_SECURE_CODE.equals(joinType)|| ConfConstant.JOIN_TYPE_EMAIL.equals(joinType)){
			
			String hostCode=confBase.getCompereSecure();
			String userCode=confBase.getUserSecure();
			if(ConfConstant.CONF_PERMANENT_ENABLED_MAIN.equals(perConf.getPermanentConf())){
				hostCode=perConf.getCompereSecure();
				userCode=perConf.getUserSecure();
			}
			if(!StringUtil.isEmpty(code)){
				if(code.equals(userCode)){
					userRole = 8;
				}
				if(code.equals(hostCode)){
					userRole = 3;
				}
			}
		
		}
		if(ConfConstant.JOIN_TYPE_OURURL.equals(joinType)){
			currentUser=new UserBase();
			String userType=request.getParameter("userType");
			String userId=request.getParameter("userId");
			currentUser.setId(IntegerUtil.parseInteger(userId));
			currentUser.setTrueName(userName);
			currentUser.setClientName(userName);
		}
		if(confBase==null){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		if(!(ConfConstant.CONF_STATUS_SUCCESS.equals(confBase.getConfStatus()) || ConfConstant.CONF_STATUS_OPENING.equals(confBase.getConfStatus()))){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_4);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		//验证在线人数与站点的License数
//		int siteLicCount = siteService.queryASSiteInfo(siteBase.getSiteSign()).getLicense();
//		confManagementService.setingOnlineUserNum(confBase);
//		logger.info("confBase.pcNum="+confBase.getPcNum());
//		logger.info("siteLicCount="+siteLicCount);
//		if (confBase.getPcNum() >= (siteLicCount-1)) {
//			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_11);
//			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
//		}
		
		if(checkTooEarly(confBase)){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_7);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		if(currentUser==null){
			currentUser=new UserBase();
			currentUser.setId(0);
			currentUser.setTrueName(userName);
			currentUser.setClientName(userName);
		}
		
		boolean startStatus=true;
		if(confBase.getStartTime().after(DateUtil.getGmtDate(null)) && ConfConstant.CONF_STATUS_SUCCESS.equals(confBase.getConfStatus())){
				startStatus=confManagementService.startConf(confBase, null, currentUser);
				if(startStatus){
					confService.updateStartTime(confBase, DateUtil.getGmtDate(null));
					confService.updateConfStatus(confBase,ConfConstant.CONF_STATUS_OPENING);
					
				}
		}
		if(perConf!=null && perConf.getId().equals(confBase.getBelongConfId()) 
				&& ConfConstant.CONF_STATUS_SUCCESS.equals(perConf.getConfStatus())){
			confService.updateConfStatus(perConf,ConfConstant.CONF_STATUS_OPENING);
		}
		logger.info("Join Conf startStatus="+startStatus);
		if(!startStatus){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		JoinRandom joinRandom=null;
		
		if(rId>0){
			joinRandom=clientAPIService.getJoinRandomById(rId);
		}
		
		if(joinRandom==null){
			joinRandom=new JoinRandom();
			joinRandom.setConfId(confBase.getId());
			joinRandom.setUserId(currentUser.getId());
			joinRandom.setUserName(currentUser.getTrueName());
			joinRandom.setUserEmail(currentUser.getUserEmail());
			joinRandom.setUserRole(userRole);
			joinRandom.setCreateTime(DateUtil.getGmtDate(null));
			joinRandom.setLanguage(LanguageHolder.getCurrentLanguage());
			joinRandom.setClientIp(StringUtil.getIpAddr(request));
			joinRandom=clientAPIService.saveRandom(joinRandom);
		}
		if(joinRandom==null || joinRandom.getId()<= 0){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		String preParam=clientAPIService.makePreParam(joinRandom,userIp);
		
		request.setAttribute("preParam", preParam);
		request.setAttribute("cId", confBase.getId());
		request.setAttribute("rId", joinRandom.getId());
		request.setAttribute("code", code);
		request.setAttribute("cPass", cPass);
		return new ActionForward.Forward("/jsp/common/join_plug.jsp");
		
	}
		
		
//		
//	private Object checkPermanentConf(ConfBase confBase,HttpServletRequest request){
//		Integer permanentConf = confBase.getPermanentConf();
//		if(ConfConstant.CONF_PERMANENT_ENABLED_MAIN.equals(permanentConf)){
//			ConfBase childConf=confService.getPermanentChildConf(confBase.getId());
//			if(childConf==null){
//				childConf=confService.createChildConf(confBase);
//			}
//			if(childConf==null){
//				request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_10);
//				return new ActionForward.Forward("/jsp/common/join_msg.jsp");
//			}
//			confBase=childConf;
//		}
//		return null;
//	}
//		
		
		
		
		/*
		
//		Integer confId = 0;
//		if (!StringUtil.isEmpty(cId)) {
//			confId = IntegerUtil.parseInteger(cId);
//			if (confId != null && confId.intValue() > 0) {
//				confBase = confService.getConfBasebyConfId(confId);
//			}
//		}
//		if(joinType==ConfConstant.JOIN_TYPE_CONFID && confBase==null ){
//			
//		}
		if(confBase == null && StringUtil.isEmpty(code)){
			request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_2);
			if(ConfConstant.JOIN_TYPE_EMAIL.equals(joinType)){
				return new ActionForward.Forward("/jsp/common/join_email.jsp");
			}
			return new ActionForward.Forward("/jsp/common/join_secure.jsp");
		}
		if (confBase == null && !StringUtil.isEmpty(code)) {
			//参会者安全会议号
			confBase = confService.getConfBaseByUserSecure(code);
			if (confBase == null) {
				//主持人安全会议号
				confBase = confService.getConfBaseByCompereSecure(code);
			}
		}
		if (confBase == null) {
			request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_3);
			if(ConfConstant.JOIN_TYPE_EMAIL.equals(joinType)){
				return new ActionForward.Forward("/jsp/common/join_email.jsp");
			}
			return new ActionForward.Forward("/jsp/common/join_secure.jsp");
		}
	
//		if("www".equals(siteSign.toLowerCase().trim())){
//			siteSign="meeting";
//		}
		if(siteBase==null){
			String forWardUrl=  errorForward(joinType, ConfConstant.JOIN_ERROR_CODE_9,request);
			return new ActionForward.Forward(forWardUrl);
		}
		if(!confBase.getSiteId().equals(siteBase.getId())){

			String forWardUrl=  errorForward(joinType, ConfConstant.JOIN_ERROR_CODE_3,request);
			return new ActionForward.Forward(forWardUrl);
		}
		
//		Date startTime=confBase.getStartTime();
//		Date nowGmtDate=DateUtil.getGmtDate(null);
//		Integer aheadMinute=confBase.getAheadTime();
//		long startTimeStamp=startTime.getTime();
//		long nowTimeStamp=nowGmtDate.getTime();
		
		if(checkTooEarly(confBase)){
			request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_7);
			return new ActionForward.Forward("/jsp/common/join_early.jsp");
		}

		//验证会议状态 是否正在召开中或者是预约成功的
		Integer confStatus=confBase.getConfStatus();
		if(confStatus==null || !(confStatus.intValue()==ConfConstant.CONF_STATUS_SUCCESS.intValue()  || confStatus.intValue()==ConfConstant.CONF_STATUS_OPENING.intValue())){
//			request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_4);
//			if(ConfConstant.JOIN_TYPE_SECURE_CODE.equals(joinType)){
//				return new ActionForward.Forward("/jsp/common/join_secure.jsp");
//			}
//			if(ConfConstant.JOIN_TYPE_CONFID.equals(joinType)){
//				return new ActionForward.Forward("/jsp/common/join_public.jsp");
//			}
//			if(ConfConstant.JOIN_TYPE_EMAIL.equals(joinType)){
//				return new ActionForward.Forward("/jsp/common/join_email.jsp");
//			}
			String forWardUrl=  errorForward(joinType, ConfConstant.JOIN_ERROR_CODE_4,request);
			return new ActionForward.Forward(forWardUrl);
			
			
		}
		
		//验证会议是否公开会议,同时验证会议密码
		Integer publicFlag=confBase.getPublicFlag();
		String publicPass=confBase.getPublicConfPass();
		//joinType=ConfConstant.JOIN_TYPE_SECURE_CODE;
		if(ConfConstant.JOIN_TYPE_CONFID.equals(joinType) && ConfConstant.CONF_PUBLIC_FLAG_TRUE .equals(publicFlag)){
			if(publicPass!=null && !"".equals(publicPass.trim())){
				if((cPass==null || "".equals(cPass.trim()))){
					return new ActionForward.Forward("/jsp/common/join_public.jsp");
				}
				if(!cPass.equals(publicPass)){
					request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_5);
					return new ActionForward.Forward("/jsp/common/join_public.jsp");
				}
			}
		}
		
		//验证是否为周期会议
		boolean isLoop=false;
		Integer cycleId=confBase.getCycleId();
		if(cycleId!=null && cycleId.intValue()>0){
			isLoop=false;
		}
		
		//验证当前用户是哪种角色
		
		String chairCode=confBase.getCompereSecure();
//		String userCode=confBase.getUserSecure();

//		boolean isChair=false;//是否为主持人标志
//		if(code!=null && code.equals(chairCode)){
//			isChair=true;
//		}
//		
//		if(isChair){
//			
//		}
//		logger.info("request.getContextPath()=="+request.getSession().getServletContext().getRealPath("/"));//.getContextPath());
		
		//拼成Param参数
		
		if(currentUser==null){
			currentUser=new UserBase();
			currentUser.setId(0);
			currentUser.setTrueName(userName);
			currentUser.setClientName(userName);
		}
		if(ConfConstant.JOIN_TYPE_OURURL.equals(joinType)){
			currentUser=new UserBase();
//			userName=request.getParameter("userName");
//			userName=request
			String userType=request.getParameter("userType");
			String userId=request.getParameter("userId");
			currentUser.setId(0);
			currentUser.setTrueName(userName);
			currentUser.setClientName(userName);
			
		}
//		confBase
		boolean startStatus=true;
		if(confBase.getStartTime().after(DateUtil.getGmtDate(null)) && ConfConstant.CONF_STATUS_SUCCESS.equals(confBase.getConfStatus())){
				startStatus=confManagementService.startConf(confBase, null, currentUser);
				if(startStatus){
					confService.updateStartTime(confBase, DateUtil.getGmtDate(null));
					confService.updateConfStatus(confBase,ConfConstant.CONF_STATUS_OPENING);
				}
		}
		logger.info("Join Conf startStatus="+startStatus);
		if(!startStatus){
			String forWardUrl=  errorForward(joinType, ConfConstant.JOIN_ERROR_CODE_8,request);
			return new ActionForward.Forward(forWardUrl);
		}
		
		String preParam=clientAPIService.makePreParam(confBase, currentUser);
		request.setAttribute("preParam", preParam);
		request.setAttribute("cId", confBase.getId());
		return new ActionForward.Forward("/jsp/common/join_plug.jsp");
		
		
	}
	
	*/
//	
//	private String errorForward(Integer joinType,String errorCode, HttpServletRequest request){
//		request.setAttribute("msgFlag", errorCode);
//		String forWardFile="";
//		if(ConfConstant.JOIN_TYPE_EMAIL.equals(joinType)){
//			forWardFile="/jsp/common/join_email.jsp";
//		}
//		if(ConfConstant.JOIN_TYPE_CONFID.equals(joinType)){
//			forWardFile="/jsp/common/join_public.jsp";
//		}
//		if(ConfConstant.JOIN_TYPE_SECURE_CODE.equals(joinType)){
//			forWardFile="/jsp/common/join_secure.jsp";
//		}
//		return forWardFile;
//	}
	
	
	/**
	 * 下载Client
	 * @param request
	 * @return
	 */
	@AsController(path = "download")
	public Object download(@CParam("cId") String cId,@CParam("userName") String userName,@CParam("rId") int rId,HttpServletRequest request,HttpServletResponse response){
		
		ConfBase confBase = null;
		Integer confId = 0;
		if (!StringUtil.isEmpty(cId)) {
			confId = IntegerUtil.parseInteger(cId);
			if (confId != null && confId.intValue() > 0) {
				confBase = confService.getConfBasebyConfId(confId);
			}
		}
		if(confBase==null ){
			return "会议号 error";
		}
		JoinRandom joinRandom=null;
		if(rId>0){
			joinRandom=clientAPIService.getJoinRandomById(rId);
		}
		
		String webRootPath=LiberContainer.getContainer().getServletContext().getRealPath("/");
		String filePath=webRootPath+"download/mcStartUp.exe";
		String fileBaseName="mcStartUp";
		UserBase currentUser=null;
		currentUser=userService.getCurrentUser(request);
		
		if(currentUser==null){
			currentUser=new UserBase();
			currentUser.setId(0);
			currentUser.setTrueName(userName);
			currentUser.setClientName(userName);
		}
		if(joinRandom==null){
			joinRandom=new JoinRandom();
			joinRandom.setConfId(confBase.getId());
			joinRandom.setUserId(currentUser.getId());
			joinRandom.setUserEmail(currentUser.getUserEmail());
			joinRandom.setUserName(currentUser.getTrueName());
			joinRandom.setUserRole(8);
			joinRandom.setCreateTime(DateUtil.getGmtDate(null));
			joinRandom.setLanguage(LanguageHolder.getCurrentLanguage());
			joinRandom.setClientIp(StringUtil.getIpAddr(request));
			joinRandom=clientAPIService.saveRandom(joinRandom);
		}
		if(joinRandom==null || joinRandom.getId()<= 0){
			request.setAttribute("errorCode", ConfConstant.JOIN_ERROR_CODE_8);
			return new ActionForward.Forward("/jsp/common/join_msg.jsp");
		}
		
		String confparam=clientAPIService.makeSuffixForSetup(joinRandom);
		String downLoadName=fileBaseName+"_"+confparam+".exe";
		try {
			DownLoadUtil.downloadFileNewName(response, filePath, downLoadName, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private boolean checkTooEarly(ConfBase confBase){
		boolean timeStatus=false;
		if(confBase!=null){
			Date startTime=confBase.getStartTime();
			Date nowGmtDate=DateUtil.getGmtDate(null);
			Integer aheadMinute=confBase.getAheadTime();
			long startTimeStamp=startTime.getTime();
			long nowTimeStamp=nowGmtDate.getTime();
			timeStatus=(nowTimeStamp+aheadMinute*60000) <= startTimeStamp;
//		if((nowTimeStamp+aheadMinute*60000) <= startTimeStamp){
//			request.setAttribute("msgFlag", ConfConstant.JOIN_ERROR_CODE_7);
//			return new ActionForward.Forward("/jsp/common/join_early.jsp");
//		}
		}
		return timeStatus;
	}
	

}
