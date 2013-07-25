package com.bizconf.audio.logic;

import java.util.Date;

import com.bizconf.audio.entity.EmailConfig;
import com.bizconf.audio.entity.EmailInfo;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;


/**
 * @desc 
 * @author Administrator
 * @date 2013-1-31
 */
public interface EmailLogic {
	
	/**
	 * 通过siteBaseId获取站点信息
	 * @param id
	 * @return
	 */
	SiteBase getSiteBaseById(Integer id);
	
	/**
	 * 通过站点ID获取站点管理员信息
	 * @param siteId
	 * @return
	 */
	UserBase getSiteSupperMasterBySiteId(Integer siteId);
	
	/**
	 * 获取系统邮件服务配置
	 * @return
	 */
	EmailConfig getSysEmailConfig(int siteId)throws Exception;
	
	/**
	 * 获取一个用户对象
	 * @param userId
	 * @return
	 */
	UserBase getUserBaseById(Integer userId);
	
	
	/**
	 * 获取站点的当地时间
	 * @param date
	 * @param siteId
	 * @return
	 */
	Date getSiteLocalDate(Date date,Integer siteId);
	
	/**
	 * 获取站点可用的license总数
	 * @param siteId
	 * @return
	 */
	public int getSiteLicenseNum(Integer siteId);
//	EmailInfo get
}
