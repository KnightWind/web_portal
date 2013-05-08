package com.bizconf.audio.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath("/")
public class IndexController {
	@Autowired
	SiteService siteService;
	
	@Autowired
	LoginService loginService;
	
	public Object doRequest(LiberInvocation inv) {
		String siteBrand = SiteIdentifyUtil.getCurrentBrand();
		if ("www".equalsIgnoreCase(siteBrand)) {
			return new ActionForward.Redirect("http://" + SiteIdentifyUtil.DEFAULT_SITE_BRAND + "." + SiteIdentifyUtil.MEETING_CENTER_DOMAIN);
		}
		SiteBase siteBase = siteService.getSiteBaseBySiteSign(siteBrand);
		if (siteBase == null) {
			return new ActionForward.Error(404);
		}
		inv.getRequest().setAttribute("siteBase", siteBase);
		inv.getRequest().setAttribute("isLogined", loginService.isLogined(inv.getRequest()));
		return new ActionForward.Forward("/user");
		//return new ActionForward.Forward("/jsp/index.jsp");
	}
}
