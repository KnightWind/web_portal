package com.bizconf.audio.action.admin;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
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
		UserBase oldUser = userService.getCurrentSiteAdmin(inv.getRequest());
		oldUser.setEnName(newUser.getEnName());
		oldUser.setLoginName(newUser.getLoginName());
		oldUser.setTrueName(newUser.getTrueName());
		oldUser.setUserEmail(newUser.getUserEmail());
		oldUser.setMobile(newUser.getMobile());
		if(StringUtil.isNotBlank(orgPass)&&!MD5.encodePassword(orgPass, "MD5").equals(oldUser.getLoginPass())){
			//原始密码输入错误！
			this.setErrMessage(inv.getRequest(), "The original password error.");
			
		}else {
			if (newUser.getLoginPass() != null && newUser.getLoginPass().length() > 0) {
				oldUser.setLoginPass(MD5.encodePassword(newUser.getLoginPass(), "MD5"));
			}
			try {
				DAOProxy.getLibernate().updateEntity(oldUser);
			} catch (Exception e) {
				e.printStackTrace();
				this.setErrMessage(inv.getRequest(), "set profile failed.");
				eventLogService.saveAdminEventLog(oldUser, EventLogConstants.SITE_ADMIN_INFO_SETUP, 
						"企业管理员个人设置失败",
						EventLogConstants.EVENTLOG_FAIL, null, inv.getRequest());   //设置成功后写EventLog
			}
			this.setInfoMessage(inv.getRequest(), "set profile ok");
			eventLogService.saveAdminEventLog(oldUser, EventLogConstants.SITE_ADMIN_INFO_SETUP, 
					"企业管理员个人设置成功", 
					EventLogConstants.EVENTLOG_SECCEED, null, inv.getRequest());   //设置成功后写EventLog
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
