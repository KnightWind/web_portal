package com.bizconf.audio.action;

import javax.servlet.http.HttpServletRequest;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.libernate.liberc.annotation.Disabled;

@Disabled
public abstract class BaseController {
	
	/**
	 * 设置提示信息
	 * 
	 * @param request
	 * @param message
	 */
	protected void setInfoMessage(HttpServletRequest request, String message) {
		if (request.getAttribute("errorMessage") != null) {
			throw new IllegalStateException("errorMessage is set before.");
		}
		request.setAttribute("infoMessage", message);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param request
	 * @param message
	 */
	protected void setErrMessage(HttpServletRequest request, String message) {
		if (request.getAttribute("infoMessage") != null) {
			throw new IllegalStateException("infoMessage is set before.");
		}
		request.setAttribute("errorMessage", message);
	}
	
	protected String getLang(HttpServletRequest request){
		String domain=SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		String lang=CookieUtil.getCookieByDomain(request,ConstantUtil.COOKIE_LANG_KEY, domain);
		if(lang!=null && !lang.equals("")){
			return lang;
		}else{
			return request.getLocale().getLanguage();
		}

	}
	
	protected void setEmpowerFlag(HttpServletRequest request, EmpowerConfig userEmpower){
		if(userEmpower != null){
			if(userEmpower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				request.setAttribute("isShareMediaFlag", SiteConstant.EMPOWER_ENABLED);
			}
			if(userEmpower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				request.setAttribute("isRecordFlag", SiteConstant.EMPOWER_ENABLED);
			}
			if(userEmpower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				request.setAttribute("isPhoneFlag", SiteConstant.EMPOWER_ENABLED);
			}
			if( userEmpower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				request.setAttribute("isAutoFlag", SiteConstant.EMPOWER_ENABLED);
			}
			if(userEmpower.getVideoFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				request.setAttribute("isVideoFlag", SiteConstant.EMPOWER_ENABLED);
			}
			request.setAttribute("videoNumber", userEmpower.getVideoNumber());
		}
	}
	
	
}
