package com.bizconf.audio.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;

public interface SiteService {

	/**
	 * 
	 * 创建新站点
	 * 
	 * @param siteBase
	 * @param siteAdmin
	 * @return
	 * @throws Exception
	 */
	public SiteBase createSite(SiteBase siteBase, UserBase siteAdmin, EmpowerConfig empowerConfig)
			throws Exception;

	/**
	 * 保存会议设置参数项
	 * 
	 * @param mtgParam
	 * @return
	 * @throws Exception
	 */
	public boolean saveDefaultConfig(DefaultConfig config) throws Exception;

	/**
	 * 系统管理员修改站点信息
	 * 
	 * @param siteBase
	 * @return
	 * @throws Exception
	 */
	public SiteBase updateSiteBaseForSystem(SiteBase siteBase, UserBase siteAdmin, EmpowerConfig empowerConfig) throws Exception;

	/**
	 * 站点管理员修改站点名称、英文名称与LOGO
	 * 
	 * @param siteName
	 * @param logoFile
	 * @return
	 */
	public boolean updateSiteBaseForSiteAdmin(Integer siteId, String siteName, String enName, File logoFile);
	
	/**
	 * 站点管理员修改站点名称与LOGO
	 * 
	 * @param siteName
	 * @param logoFile
	 * @return
	 */
	public SiteBase updateSiteBaseForSiteAdmin(Integer siteId, String siteName, String enName, String logoFile,Integer timeZoneId,Integer timeZone);

	/**
	 * 根据站点ID号删除站点
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSiteById(Integer id,SystemUser sysUser) throws Exception;

	/**
	 * 根据站点ID号锁定站点
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */

	public boolean lockSiteById(Integer id) throws Exception;

	/**
	 * 根据站点ID号解锁站点
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean unLockSiteById(Integer id) throws Exception;

	/**
	 * 根据站点名称或标识获取站点信息列表（超级系统管理员）
	 * 
	 * @param siteNameOrSign
	 * @param sortField:排序字段名
	 * @param sortord  : asc  desc
	 * @param pageModel:分页
	 * @return
	 */
	public List<SiteBase> getSiteListBySiteNameOrSign(String siteNameOrSign,
			String sortField, String sortord, PageModel pageModel);
	
	/**
	 * 根据站点名称或标识获取站点信息列表（普通系统管理员）
	 * 
	 * @param siteNameOrSign
	 * @param sortField:排序字段名
	 * @param sortord  : asc  desc
	 * @param pageModel:分页
	 * @param sysUserId:普通系统管理员ID
	 * @return
	 */
	public List<SiteBase> getSiteListBySiteNameOrSign(String siteNameOrSign,
			String sortField, String sortord, PageModel pageModel, int sysUserId);

	/**
	 * 统计根据站点名称或者是标识的站点记录数(超级系统管理员)
	 * 
	 * @param siteNameOrSign
	 * @param pageModel
	 * @return
	 */
	public Integer countSiteListBySiteNameOrSign(String siteNameOrSign);
	
	/**
	 * 统计根据站点名称或者是标识的站点记录数(普通系统管理员)
	 * 
	 * @param siteNameOrSign
	 * @param pageModel
	 * @param sysUserId:普通系统管理员ID
	 * @return
	 */
	public Integer countSiteListBySiteNameOrSign(String siteNameOrSign, int sysUserId);

	/**
	 * 站点高级搜索列表
	 * 
	 * @param condition
	 * @param sortField
	 * @param sortord
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	public List<SiteBase> getSiteListByCondition(Condition condition,
			String sortField, String sortord, PageModel pageModel)
			throws Exception;

	/**
	 * 统计高级搜索总记录数
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Integer countSiteByCondition(Condition condition) throws Exception;

	/**
	 * 根据站点标识获取站点信息
	 * 
	 * @param siteSign
	 * @return
	 */
	public SiteBase getSiteBaseBySiteSign(String siteSign);

	/**
	 * 根据站点ID号获取站点基本信息
	 * 
	 * @param id
	 * @return
	 */
	public SiteBase getSiteBaseById(Integer id);
	
	/**
	 * 根据站点ID号集合获取站点列表
	 * @param ids
	 * @return
	 */
	public List<SiteBase> getSiteListBySiteIds(Integer[] ids);
	
	/**
	 * 根据站点ID号集合获取所有站点列表（包括已删除站点）
	 * @param ids
	 * @return
	 */
	public List<SiteBase> getAllSiteListBySiteIds(Integer[] ids);
	

	/**
	 * 取出当前登录用户所在的站点信息
	 * @param request
	 * @return
	 */
	public SiteBase getCurrentSiteBaseByUserLogin(HttpServletRequest request);
	
	

	/**
	 * 取出当前登录站点管理员所在的站点信息
	 * @param request
	 * @return
	 */
	public SiteBase getCurrentSiteBaseByAdminLogin(HttpServletRequest request);
	
	
	/**
	 * 查询一周内服务到期的站点
	 */
//	public List<SiteBase> getSite
	
	/**
	 * 查询一月内服务到期的站点
	 */
	
	
	
	/**
	 * soap注册新增站点到AS
	 * @param site
	 * @return
	 */
	public String soapAddSite(SiteBase site);
	
	
	/**
	 * soap 注册修改站点信息到AS
	 * @param site
	 * @param flag
	 * @return
	 */
	public String soapUpdateSite(SiteBase site,boolean flag);
	

	/**
	 * soap 注册删除站点到AS 
	 * @param siteId
	 * @return
	 */
	public String soapDelSite(String siteId);
	
	/**
	 * 通过siteSign获取企业信息
	 * @param siteSign
	 * @return
	 */
	public SiteBase queryASSiteInfo(String siteSign);
	
	
	/**
	 * 查询当前所有站点使用license数量
	 * @return
	 */
	public int queryLicenseUseNum();
	
	
	/**
	 * 查询AS中所有的站点信息
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageBean<SiteBase> queryASAllSites(int start, int end);
}
