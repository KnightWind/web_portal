package com.bizconf.audio.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.component.language.LanguageComponent;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath("changeLang")
public class ChangeLanguageController {
	
	@Autowired
	LanguageComponent languageComponent;
	
	public Object doRequest(@CParam("lang") String lang, @CParam("returnURL") String retUrl, 
			LiberInvocation invocation) {
		//String referURL = invocation.getRequest().getHeader("referer");
		if (lang != null && lang.length() > 0) {
			languageComponent.setDefaultLanguage(invocation.getRequest(), invocation.getResponse(), lang);
		}
		return new ActionForward.Redirect(retUrl);
	}
}
