package com.bizconf.audio.action.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.service.EnterpriseAdminService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
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
		if(newUser != null){
			String flag = loginNameValidate(request);
			if("false".equals(flag)){
				this.setErrMessage(request, "登录名已存在，请重新输入！");
				return new ActionForward.Forward("/user/profile");
			}
			if(StringUtil.isNotBlank(oldpassword)&&!MD5.encodePassword(oldpassword, "MD5").equals(currentUser.getLoginPass())){
				this.setErrMessage(request, "原密码输入错误！");
				return new ActionForward.Forward("/user/profile");
			}else{
				if(StringUtil.isNotBlank(newUser.getLoginPass())){
					currentUser.setLoginPass(MD5.encodePassword(newUser.getLoginPass(), "MD5"));
				}
				currentUser.setLoginName(newUser.getLoginName());
				currentUser.setTrueName(newUser.getTrueName());
				currentUser.setEnName(newUser.getEnName());
				currentUser.setUserEmail(newUser.getUserEmail());
				currentUser.setMobile(newUser.getMobile());
				updateFlag = enterpriseAdminService.updateUserBase(currentUser);
				if(updateFlag){
					setInfoMessage(request,"个人信息设置成功！");
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_PROFILE_SETUP, "个人信息设置成功！",
							EventLogConstants.EVENTLOG_SECCEED, currentUser, request);
				}else{
					setErrMessage(request, "个人信息设置失败！");
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_PROFILE_SETUP, "个人信息设置失败！",
							EventLogConstants.EVENTLOG_FAIL, currentUser, request);
					return new ActionForward.Forward("/user/profile");
				}
			}
		}else{
			setErrMessage(request, "个人信息设置失败！");
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_PROFILE_SETUP, "个人信息设置失败！",
					EventLogConstants.EVENTLOG_FAIL, currentUser, request);
		}
//		setInfoMessage(request, ResourceHolder.getInstance().getResource("system.site.lock." + lockStatus));
		return new ActionForward.Forward("/user/profile");
	}
	
	/**
	 * 修改个人信息时验证登录名是否已存在
	 * return "false":已存在    "true":不存在
	 * wangyong
	 * 2013-3-20
	 */
	@AsController(path = "loginNameValidate")
	public String loginNameValidate(HttpServletRequest request){
		String loginName = request.getParameter("loginName");
		UserBase currentUser = userService.getCurrentUser(request);
		if(currentUser!=null){//说明是新增的
			if(StringUtil.isNotBlank(currentUser.getLoginName())){
				if(!currentUser.getLoginName().equals(loginName)){ //若登录名更改则查询登录名是否已存在
					UserBase otherUser = userService.getSiteUserByLoginName(currentUser.getSiteId(), loginName);
					if(otherUser != null && otherUser.getId() != null && otherUser.getId().intValue() > 0){
						return "false";
					}else{
						return "ture";
					}
				}
			}
		}
		return "ture";
	}
}
