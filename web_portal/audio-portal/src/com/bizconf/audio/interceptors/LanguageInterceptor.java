package com.bizconf.audio.interceptors;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bizconf.audio.component.language.LanguageComponent;
import com.bizconf.audio.component.language.LanguageHolder;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.LoginConstants;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.libernate.liberc.annotation.CommonsInterceptor;
import com.libernate.liberc.interceptor.SysInterceptorExt;

@Component
@CommonsInterceptor
public class LanguageInterceptor implements SysInterceptorExt {
	
	@Autowired
	LanguageComponent languageComponent;
	
	Log logger = LogFactory.getLog(LanguageInterceptor.class);
	
	static final long version = System.currentTimeMillis();
	

	@Override
	public Object doAfter(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws IOException {
		LanguageHolder.remove();
		LanguageHolder.removeTSessionID();
		return null;
	}

	@Override
	public Object doBefore(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//set static version
		request.setAttribute("version", version);
		
		String language = request.getHeader("ACCEPT-LANGUAGE");
		
		String defaultLanguage = languageComponent.getDefaultLanguage(request);
		if (!StringUtils.isEmpty(defaultLanguage)) {
			LanguageHolder.setCurrentLanguage(defaultLanguage.toLowerCase());
		}
		else if (language != null && language.length() > 0) {
			language = language.split(";")[0].split(",")[0];
			LanguageHolder.setCurrentLanguage(language.toLowerCase());
		}
		
		request.setAttribute("currentLanguage", LanguageHolder.getCurrentLanguage());
		request.setAttribute("LANG", ResourceHolder.getInstance().getPackage());

		String domain = SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		
		// tsessionid cookie
		String tsessionId = CookieUtil.getCookieByDomain(request, LoginConstants.UN_LOGIN_SESSION_ID_NAME,domain);
	
//		if(tsessionId == null || tsessionId.length() == 0){
//			domain =  SiteIdentifyUtil.getCurrentBrand()+"."+SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
//			tsessionId = CookieUtil.getCookieByDomain(request, LoginConstants.UN_LOGIN_SESSION_ID_NAME,domain);
//		}
		if (tsessionId == null || tsessionId.length() == 0) {
			UUID uuid = UUID.randomUUID();
			try {
				CookieUtil.setPageCookie(response, LoginConstants.UN_LOGIN_SESSION_ID_NAME, 
						uuid.toString(),SiteIdentifyUtil.MEETING_CENTER_DOMAIN);
			} catch (Exception e) {
				e.printStackTrace();
			}
			tsessionId = uuid.toString();
		}
		LanguageHolder.setTSessionId(tsessionId);
		return null;
	}

	@Override
	public int getPriority() {
		return 90;
	}

}
