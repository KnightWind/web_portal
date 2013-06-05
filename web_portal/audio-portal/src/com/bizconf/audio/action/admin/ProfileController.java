package com.bizconf.audio.action.admin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

@Interceptors({SiteAdminInterceptor.class})
@ReqPath("profile")
public class ProfileController extends BaseController {
	
	private final Logger logger = Logger.getLogger(ProfileController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	EventLogService eventLogService;
	
	public Object doRequest(LiberInvocation inv) {
		//获取当前登录用户
		return new ActionForward.Forward("/jsp/admin/profile.jsp");
	}
	
	@AsController
	public Object submit(@CParam("orgloginPass") String orgPass,UserBase newUser, LiberInvocation inv) {
		boolean flag = false;
		String oldPass = inv.getRequest().getParameter("orgPass");
		UserBase oldUser = userService.getCurrentSiteAdmin(inv.getRequest());
		oldUser.setEnName(newUser.getEnName());
		oldUser.setLoginName(newUser.getLoginName());
		oldUser.setTrueName(newUser.getTrueName());
		oldUser.setUserEmail(newUser.getUserEmail());
		oldUser.setMobile(newUser.getMobile());
		if(newUser != null && StringUtil.isNotBlank(newUser.getLoginPass())){
			if(StringUtil.isEmpty(oldPass)){
				this.setErrMessage(inv.getRequest(), "若修改密码请输入原始密码");
				return new ActionForward.Forward("/admin/profile");
			}
		}
//		if(StringUtil.isNotBlank(orgPass)&&!MD5.encodePassword(orgPass, "MD5").equals(oldUser.getLoginPass())){
		if(StringUtil.isNotBlank(oldPass)&&!MD5.encodePassword(oldPass, "MD5").equals(oldUser.getLoginPass())){
			//原始密码输入错误！
			this.setErrMessage(inv.getRequest(), "原始密码输入错误");
			return new ActionForward.Forward("/admin/profile");
			
		}else {
			if (newUser.getLoginPass() != null && newUser.getLoginPass().length() > 0) {
				oldUser.setLoginPass(MD5.encodePassword(newUser.getLoginPass(), "MD5"));
			}
			try {
				oldUser = DAOProxy.getLibernate().updateEntity(oldUser);
				if(oldUser != null && oldUser.getId() != null && oldUser.getId().intValue() > 0){
					flag = true;
				}
			} catch (Exception e) {
				logger.error("企业管理员个人设置异常" + oldUser + e);
			} finally {
				sysHelpAdminEventLog(flag, userService.getCurrentSysAdmin(inv.getRequest()), oldUser, 
						EventLogConstants.SYSTEM_ADMIN_INFO_SETUP, EventLogConstants.SITE_ADMIN_INFO_SETUP, "企业管理员个人设置", null, inv.getRequest());
			}
			if(flag){
				this.setInfoMessage(inv.getRequest(), "个人设置修改成功");
			}else{
				this.setErrMessage(inv.getRequest(), "个人设置修改失败");
			}
		}
		return new ActionForward.Forward("/admin/profile");
	}
	
	@AsController(path = "loginNameValidate")
	public String loginNameValidate(@CParam("loginName") String loginName, @CParam("adminId") int adminId){
		UserBase user = userService.getUserBaseById(adminId);
		if(user==null){//说明是新增的
			return "true";
		}
		if(StringUtil.isNotBlank(loginName)){
			if(user.getLoginName().equals(loginName)){ //说明没有更改
				return "true";
			}
			UserBase otherUser = userService.getSiteUserByLoginName(user.getSiteId(), loginName);
			if(otherUser==null){ //存在有相同登录名的账号
				return "true";
			}
		}
		return "false";
	}
}
