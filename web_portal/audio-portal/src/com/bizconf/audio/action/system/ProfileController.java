package com.bizconf.audio.action.system;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.interceptors.SystemUserInterceptor;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

@Interceptors({SystemUserInterceptor.class})
@ReqPath("profile")
public class ProfileController extends BaseController {
	
	@Autowired
	UserService userService;
	
	public Object doRequest(LiberInvocation inv) {
		return new ActionForward.Forward("/jsp/system/profile.jsp");
	}
	
	@AsController
	public Object submit(SystemUser newUser,@CParam("orgPass") String orgPass, LiberInvocation inv) {
		SystemUser oldUser = userService.getCurrentSysAdmin(inv.getRequest());
		if(StringUtil.isNotBlank(orgPass)){
			String inputOrgPass = MD5.encodePassword(orgPass, "MD5");
			if(!oldUser.getLoginPass().equals(inputOrgPass)){
				this.setErrMessage(inv.getRequest(), "set profile failed.the original password incorrect");
				return new ActionForward.Forward("/system/profile");
			}
		}
		oldUser.setEnName(newUser.getEnName());
		oldUser.setTrueName(newUser.getTrueName());
		oldUser.setEmail(newUser.getEmail());
		oldUser.setTelephone(newUser.getTelephone());
		oldUser.setMobile(newUser.getMobile());
		oldUser.setPassEditor(oldUser.getId());
		if (newUser.getLoginPass() != null && newUser.getLoginPass().length() > 0) {
			oldUser.setLoginPass(MD5.encodePassword(newUser.getLoginPass(), "MD5"));
		}
		
		try {
			DAOProxy.getLibernate().updateEntity(oldUser);
		} catch (Exception e) {
			e.printStackTrace();
			this.setErrMessage(inv.getRequest(), "set profile failed.");
		}
		
		this.setInfoMessage(inv.getRequest(), "set profile ok");
		
		return new ActionForward.Forward("/system/profile");
	}
}
