package com.bizconf.audio.service;

import com.bizconf.audio.entity.ConfOuter;

public interface ConfOuterService {
	

	/**
	 * 根据MtgKey获取会议基本信息
	 * @param mtgKey
	 * @return
	 */
	public ConfOuter getConfBaseByMeyKey(String mtgKey);
	
	/**
	 * 根据 MtgKey与站点Id号获取会议信息
	 * @param mtgKey
	 * @param siteId
	 * @return
	 */
	public ConfOuter getConfBaseByMeyKeyAndSiteId(String mtgKey,String  siteSign);
	
	/**
	 * 保存外部Conf信息
	 * @param confOuter
	 */
	public ConfOuter saveConfOuter(ConfOuter confOuter);
	

}
