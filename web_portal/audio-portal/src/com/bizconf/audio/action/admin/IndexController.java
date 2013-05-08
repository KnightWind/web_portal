package com.bizconf.audio.action.admin;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 
 * 企业管理员入口
 * 
 * @author Chris Gao [gaohl81@gmail.com]
 *
 */
@ReqPath("")
@Interceptors({SiteAdminInterceptor.class})
public class IndexController {
	
	@Autowired
	SiteService siteService;
	
	@Autowired
	UserService userService;
	
	@AsController(path = "")
	public Object index(LiberInvocation inv) {
		String siteBrand = SiteIdentifyUtil.getCurrentBrand();
		SiteBase siteBase = siteService.getSiteBaseBySiteSign(siteBrand);
		UserBase user = userService.getCurrentSiteAdmin(inv.getRequest());
		String domain = SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		String lang=CookieUtil.getCookieByDomain(inv.getRequest(), ConstantUtil.COOKIE_LANG_KEY,domain);
		inv.getRequest().setAttribute("user",user);
		inv.getRequest().setAttribute("siteBase", siteBase);
		inv.getRequest().setAttribute("lang", lang);
		return new ActionForward.Forward("/jsp/admin/index.jsp");
	}
}
