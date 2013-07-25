package com.bizconf.audio.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.component.language.LanguageComponent;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.UserService;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath("changeLang")
public class ChangeLanguageController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	LanguageComponent languageComponent;
	
	public Object doRequest(@CParam("lang") String lang, @CParam("returnURL") String retUrl, 
			LiberInvocation invocation) {
		//String referURL = invocation.getRequest().getHeader("referer");
		if (lang != null && lang.length() > 0) {
			languageComponent.setDefaultLanguage(invocation.getRequest(), invocation.getResponse(), lang);
			
			//如果登录用户，直接修改偏好
			UserBase userBase = userService.getCurrentUser(invocation.getRequest());
			if (userBase != null) {
				userBase.setFavorLanguage(lang);
				try {
					DAOProxy.getLibernate().updateEntity(userBase, "favor_language");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new ActionForward.Redirect(retUrl);
	}
}
