package com.bizconf.audio.action.system;

import org.apache.log4j.Logger;
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
	
	private final Logger logger = Logger.getLogger(this.getClass());
	
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
				this.setErrMessage(inv.getRequest(), "个人信息修改失败，原始密码输入错误");
				return new ActionForward.Forward("/system/profile");
			}
		}
		oldUser.setEnName(newUser.getEnName());
		oldUser.setTrueName(newUser.getTrueName());
		oldUser.setEmail(newUser.getEmail());
		oldUser.setTelephone(newUser.getTelephone());
		oldUser.setMobile(newUser.getMobile());
		oldUser.setPassEditor(oldUser.getId());
		
		if(StringUtil.isNotBlank(oldUser.getEmail())){
			if("false".equals(emailValidate(oldUser.getEmail(), oldUser.getId()))){
				this.setErrMessage(inv.getRequest(), "该邮箱已存在!");
				inv.getRequest().setAttribute("currentSystemUser", oldUser);
				return new ActionForward.Forward("/jsp/system/profile.jsp");
			}
		}
		
		if (newUser.getLoginPass() != null && newUser.getLoginPass().length() > 0) {
			if(StringUtil.isEmpty(orgPass)){
				this.setErrMessage(inv.getRequest(), "如需修改密码，请输入正确的原始密码");
				return new ActionForward.Forward("/system/profile");
			}
			oldUser.setLoginPass(MD5.encodePassword(newUser.getLoginPass(), "MD5"));
		}
		
		try {
			DAOProxy.getLibernate().updateEntity(oldUser);
		} catch (Exception e) {
			e.printStackTrace();
			this.setErrMessage(inv.getRequest(), "个人信息修改失败");
		}
		
		this.setInfoMessage(inv.getRequest(), "个人信息修改成功");
		
		return new ActionForward.Forward("/system/profile");
	}
	
	/**
	 * 创建(修改)系统管理员时验证邮箱是否已存在
	 * return true(不存在) false(已存在)
	 * wangyong
	 * 2013-6-17
	 */
	private String emailValidate(@CParam("userEmail") String userEmail, @CParam("userId") int userId){
		String flag = "true";
		if(StringUtil.isNotBlank(userEmail)){
			SystemUser systemUser = userService.getSystemUserByEmail(userEmail.trim());
			if(systemUser != null && userId != 0 && systemUser.getId().intValue() != userId){    //修改用户
				logger.info("邮箱名"+userEmail+"已存在!");
				flag = "false";
			}
		}
		return flag;
	}
	
}
