package com.bizconf.audio.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.LoginConstants;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.ValidCodeService;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 站点用户登录
 * 
 * @author Chris
 * 
 */

@ReqPath("login")
public class LoginController extends BaseController {
	@Autowired
	LoginService loginService;
	
	@Autowired
	SiteService siteService;

	@Autowired
	ValidCodeService validCodeService;

	@AsController(path = "")
	public Object login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (loginService.isSiteAdminLogined(request)) {
			return new ActionForward.Redirect("/");
		}
		
		String siteBrand = SiteIdentifyUtil.getCurrentBrand();
		SiteBase siteBase = siteService.getSiteBaseBySiteSign(siteBrand);
		request.setAttribute("siteBase", siteBase);
		
		return new ActionForward.Forward("/jsp/login.jsp");
	}

	/**
	 * 站点用户登录
	 * 
	 * @param sysUser
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "check")
	public Object checkLogin(UserBase userBase,
			@CParam("authCode") String authCode, @CParam("type") String type,
			@CParam("random") String random, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (loginService.isLogined(request)) {
			return new ActionForward.Redirect("/");
		}

		int loginStatus = LoginConstants.LOGIN_ERROR_SUCCESS;
		if (!validCodeService.checkValidCode(random, type, authCode)) {
			loginStatus = LoginConstants.LOGIN_ERROR_AUTHCODE;
			setErrMessage(request, ResourceHolder.getInstance().getResource(
					"system.login.error." + loginStatus));
			return new ActionForward.Forward("/login");
		}
		if (userBase != null) {
			String loginName = userBase.getLoginName();
			String loginPass = userBase.getLoginPass();

			if (StringUtil.isNotBlank(loginName)
					&& StringUtil.isNotBlank(loginPass)) {
				loginStatus = loginService.login(loginName, loginPass,
						authCode, response, request);
			}

			if (loginStatus != LoginConstants.LOGIN_ERROR_SUCCESS) {
				setErrMessage(request, ResourceHolder.getInstance()
						.getResource("system.login.error."+ loginStatus));
				return new ActionForward.Forward("/login");
			}
		}
		return new ActionForward.Redirect("/");
	}
}
