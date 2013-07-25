package com.bizconf.audio.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.LoginConstants;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.LicenseManageInterceptor;
import com.bizconf.audio.interceptors.SiteStatusInterceptor;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.ValidCodeService;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 站点管理员登录
 * 
 * @author Chris
 * 
 */

@ReqPath("login")
@Interceptors(SiteStatusInterceptor.class)
public class LoginController extends BaseController {
	private final Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	LoginService loginService;

	@Autowired
	ValidCodeService validCodeService;
	
	@Autowired
	SiteService siteService;

	@AsController(path = "")
	public Object login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (loginService.isSiteAdminLogined(request)) {
			return new ActionForward.Redirect("/admin/");
		}
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		request.setAttribute("siteBase", currentSite);
		return new ActionForward.Forward("/jsp/admin/login.jsp");
	}

	/**
	 * 站点管理员登录验证
	 * 
	 * @param sysUser
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "check")
	@Interceptors({LicenseManageInterceptor.class})
	public Object checkLogin(UserBase userBase,
			@CParam("authCode") String authCode, @CParam("type") String type,
			@CParam("random") String random, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (loginService.isSiteAdminLogined(request)) {
			return new ActionForward.Redirect("/admin/");
		}

		int loginStatus = LoginConstants.LOGIN_ERROR_SUCCESS;
		if (!validCodeService.checkValidCode(random, type, authCode)) {
			loginStatus = LoginConstants.LOGIN_ERROR_AUTHCODE;
			setErrMessage(request, ResourceHolder.getInstance().getResource(
					"system.login.error." + loginStatus));
			return new ActionForward.Forward("/admin/login");
		}
		if (userBase != null) {
			String loginName = userBase.getLoginName();
			String loginPass = userBase.getLoginPass();

			if (StringUtil.isNotBlank(loginName)
					&& StringUtil.isNotBlank(loginPass)) {
				loginStatus = loginService.loginSiteAdmin(loginName, loginPass,
						authCode, response, request);
			}

			if (loginStatus != LoginConstants.LOGIN_ERROR_SUCCESS) {
				setErrMessage(request, ResourceHolder.getInstance()
						.getResource("system.login.error."+ loginStatus));
				return new ActionForward.Forward("/admin/login");
			}
		}
		return new ActionForward.Redirect("/admin/");
	}
}
