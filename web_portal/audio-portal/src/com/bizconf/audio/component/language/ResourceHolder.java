package com.bizconf.audio.component.language;

import java.util.Map;

/**
 * 
 * @author Chris
 *
 */
public class ResourceHolder {
	private static ResourceLoader resourceLoader = ResourceLoader.getInstance();
	
	private static ResourceHolder instance = new ResourceHolder();
	
	private ResourceHolder() {
		
	}
	
	public static ResourceHolder getInstance() {
		return instance;
	}
	
	public Map<String, String> getPackage() {
		return resourceLoader.getPackage(LanguageHolder.getCurrentLanguage());
	}
	
	/**
	 * 根据指定key获取当前语言资源
	 * 
	 * @param key
	 * @return
	 */
	public String getResource(String key) {
		return getPackage().get(key);
	}
}
