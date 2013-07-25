package com.bizconf.audio.action.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.service.EnterpriseAdminService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath("profile")
@Interceptors(UserInterceptor.class)
public class ProfileController extends BaseController {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	UserService userService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	SiteService siteService;
	@Autowired
	EnterpriseAdminService enterpriseAdminService;
	
	/**
	 * 跳转到修改个人信息设置页面
	 * wangyong
	 * 2013-3-20
	 */
	@AsController(path = "")
	public Object getCurrentUserInfo(HttpServletRequest request) {
		UserBase currentUser = userService.getCurrentUser(request);
		request.setAttribute("currentUser", currentUser);
		request.setAttribute("siteBase", siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand()));
		return new ActionForward.Forward("/jsp/user/profile.jsp");
	}
	
	/**
	 * 修改个人信息设置
	 * wangyong
	 * 2013-3-20
	 */
	@AsController(path = "setupInfo")
	public Object setupInfo(@CParam("oldpassword") String oldpassword, UserBase newUser, HttpServletRequest request){
		boolean updateFlag = false;
		UserBase currentUser = userService.getCurrentUser(request);
		if(newUser == null){
			setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.profile.update.failed"));
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_PROFILE_SETUP, "个人信息设置失败！",
					EventLogConstants.EVENTLOG_FAIL, currentUser, request);
		}
			
		if(StringUtil.isNotBlank(oldpassword) && !MD5.encodePassword(oldpassword, "MD5").equals(currentUser.getLoginPass())){
			this.setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.old.pass.error"));
			return new ActionForward.Forward("/user/profile");
		}
		if(StringUtil.isNotBlank(newUser.getLoginPass())){
			currentUser.setLoginPass(MD5.encodePassword(newUser.getLoginPass(), "MD5"));
		}
		currentUser.setLoginName(newUser.getLoginName());
		currentUser.setTrueName(newUser.getTrueName());
		currentUser.setEnName(newUser.getEnName());
		currentUser.setUserEmail(newUser.getUserEmail());
		currentUser.setMobile(newUser.getMobile());
		
		if("false".equals(loginNameValidate(request))){   //验证登录名是否重复
			this.setErrMessage(request, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.4"));
			request.setAttribute("currentUser", currentUser);
			return new ActionForward.Forward("/jsp/user/profile.jsp");
		}
		
		if(StringUtil.isNotBlank(currentUser.getUserEmail())){  //验证邮箱是否重复
			if("false".equals(emailValidate(currentUser.getUserEmail(), currentUser.getId(), currentUser.getSiteId()))){
				this.setErrMessage(request, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.3"));
				request.setAttribute("currentUser", currentUser);
				return new ActionForward.Forward("/jsp/user/profile.jsp");
			}
		}
		
		updateFlag = enterpriseAdminService.updateUserBase(currentUser);
		if(updateFlag){
			setInfoMessage(request,ResourceHolder.getInstance().getResource("bizconf.jsp.user.profile.update.success"));
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_PROFILE_SETUP, "个人信息设置成功！",
					EventLogConstants.EVENTLOG_SECCEED, currentUser, request);
		}else{
			setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.profile.update.failed"));
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_PROFILE_SETUP, "个人信息设置失败！",
					EventLogConstants.EVENTLOG_FAIL, currentUser, request);
			return new ActionForward.Forward("/user/profile");
		}
		return new ActionForward.Forward("/user/profile");
	}
	
	/**
	 * 修改个人信息时验证登录名是否已存在
	 * return "false":已存在    "true":不存在
	 * wangyong
	 * 2013-3-20
	 */
	private String loginNameValidate(HttpServletRequest request){
		String loginName = request.getParameter("loginName");
		UserBase currentUser = userService.getCurrentUser(request);
		if(currentUser != null && StringUtil.isNotBlank(currentUser.getLoginName())){
			if(!currentUser.getLoginName().equals(loginName)){ //若登录名更改则查询登录名是否已存在
				UserBase otherUser = userService.getSiteUserByLoginName(currentUser.getSiteId(), loginName);
				if(otherUser != null && otherUser.getId() != null && otherUser.getId().intValue() > 0){
					return "false";
				}else{
					return "ture";
				}
			}
		}
		return "ture";
	}
	
	/**
	 * 修改个人信息时验证邮箱是否已存在
	 * return true(不存在) false(已存在)
	 * wangyong
	 * 2013-6-17
	 */
	private String emailValidate(String userEmail, int userId, int siteId){
		String flag = "true";
		if(StringUtil.isNotBlank(userEmail)){
			UserBase user = userService.getSiteUserByEmail(siteId, userEmail.trim());
			if(user != null && userId != 0 && user.getId().intValue() != userId){    //修改用户
				logger.info("邮箱名"+userEmail+"已存在!");
				flag = "false";
			}
		}
		return flag;
	}
}
