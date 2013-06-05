package com.bizconf.audio.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 帮助
 * @author wangyong
 *
 */
@ReqPath("help")
public class HelpController {
	
	@Autowired
	SiteService siteService;
	/**
	 * 帮助
	 * wangyong
	 * 2013-4-26
	 */
	@AsController(path = "")
	public Object help(HttpServletRequest request,HttpServletResponse response){
		System.out.println("---------------->");
		SiteBase siteBase = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		request.setAttribute("siteBase", siteBase);
		return new ActionForward.Forward("/jsp/user/help.jsp");
	}
}
