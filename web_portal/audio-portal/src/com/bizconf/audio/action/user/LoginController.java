package com.bizconf.audio.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.LanguageComponent;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.LoginConstants;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.LicenseManageInterceptor;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.service.ValidCodeService;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 站点用户登录
 * 
 * @author wangyong
 * 2013/2/28
 */
@ReqPath("login")
public class LoginController extends BaseController {
	private final Logger logger = Logger.getLogger(LoginController.class);
	@Autowired
	LoginService loginService;
	@Autowired
	ValidCodeService validCodeService;
	@Autowired
	SiteService siteService;
	@Autowired
	UserService userService;
	@Autowired
	LanguageComponent languageComponent;

	@AsController(path = "")
	public Object login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (loginService.isLogined(request)) {
			return new ActionForward.Redirect("/user/");
		}
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		request.setAttribute("siteBase", currentSite);
		return new ActionForward.Forward("/jsp/user/login.jsp");
	}

	/**
	 * 站点用户登录验证
	 * @param userBase 
	 * wangyong
	 * 2013-2-28
	 */
	@AsController(path = "check")
	@Interceptors({LicenseManageInterceptor.class})
	public Object checkLogin(UserBase userBase,
			@CParam("authCode") String authCode, @CParam("type") String type,
			@CParam("random") String random, HttpServletRequest request,
			HttpServletResponse response) {
		if (loginService.isLogined(request)) {
//			return new ActionForward.Redirect("/user/");
			return returnJsonStr(LoginConstants.USER_LOGIN_SUCCESS, ResourceHolder.getInstance().getResource("bizconf.jsp.user.login.success"));
		}

		int loginStatus = LoginConstants.LOGIN_ERROR_SUCCESS;
/*		if (!validCodeService.checkValidCode(random, type, authCode)) {
			loginStatus = LoginConstants.LOGIN_ERROR_AUTHCODE;
//			setErrMessage(request, ResourceHolder.getInstance().getResource(
//					"system.login.error." + loginStatus));
//			return new ActionForward.Forward("/user/login");
			String errorMessage = ResourceHolder.getInstance().getResource("system.login.error." + loginStatus);
			return returnJsonStr(LoginConstants.USER_LOGIN_FAILED, errorMessage);
		}  */
		if (userBase != null) {
			String loginName = userBase.getLoginName();
			String loginPass = userBase.getLoginPass();

			if (StringUtil.isNotBlank(loginName)
					&& StringUtil.isNotBlank(loginPass)) {
				try {
					loginStatus = loginService.login(loginName, loginPass,
							authCode, response, request);
				} catch (Exception e) {
					logger.error("站点用户登录错误！",e);
				}
			}
			if (loginStatus != LoginConstants.LOGIN_ERROR_SUCCESS) {
//				setErrMessage(request, ResourceHolder.getInstance()
//						.getResource("system.login.error."+ loginStatus));
//				return new ActionForward.Forward("/user/login");
				String errorMessage = ResourceHolder.getInstance().getResource("system.login.error." + loginStatus);
				return returnJsonStr(LoginConstants.USER_LOGIN_FAILED, errorMessage);
			}
		}
		
		//2013.7.4 登录成功后，用户缺省设置的语言设置到cookie中
		SiteBase site = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		UserBase user = userService.getSiteUserByLoginName(site.getId(), userBase.getLoginName());
		if(user != null && user.getId() != null && user.getId().intValue() > 0){
			languageComponent.setDefaultLanguage(request, response, user.getFavorLanguage());
		}
		//2013.7.4   ----结束
//		return new ActionForward.Redirect("/user/");
		return returnJsonStr(LoginConstants.USER_LOGIN_SUCCESS, ResourceHolder.getInstance().getResource("bizconf.jsp.user.login.success"));
	}
	
	private String returnJsonStr(int status, Object object){
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("message", object.toString());
		return json.toString();
	}
}
