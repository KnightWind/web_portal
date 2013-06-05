package com.bizconf.audio.logic;

import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;

public interface UserLogic {
	
 
	public UserBase getUserBaseByLoginName(String loginName,Integer siteId);
}
