package com.bizconf.audio.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.UserService;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.interceptor.SysInterceptorExt;

/**
 * 
 * @author Chris Gao
 *
 */
@Service
public class SiteAdminInterceptor implements SysInterceptorExt {
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	UserService userService;
	
	@Override
	public Object doAfter(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws IOException {
		return null;
	}

	@Override
	public Object doBefore(HttpServletRequest arg0, HttpServletResponse arg1)
			throws IOException {
		if (!loginService.isSiteAdminLogined(arg0)) {
			return new ActionForward.Forward("/admin/login");
		}
		
		UserBase siteAdmin = userService.getCurrentSiteAdmin(arg0);
		arg0.setAttribute("currentSiteAdmin", siteAdmin);
		if (siteAdmin != null) {
			arg0.setAttribute("isSuperSiteAdmin", siteAdmin.getUserType().intValue() == 
				ConstantUtil.USERTYPE_ADMIN_SUPPER.intValue());
		}
		
		return null;
	}

	@Override
	public int getPriority() {
		return 50;
	}

}
