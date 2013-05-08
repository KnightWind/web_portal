package com.bizconf.audio.service;

import java.util.List;

import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteOrg;
import com.bizconf.audio.entity.UserBase;

public interface OrgService {
	
	/**
	 * 查询站点下的组织机构
	 * 1.可根据组织机构名称模糊查询
	 * @param siteId 站点ID
	 * @param orgName 组织机构名称
	 * @param pageNo 页数
	 * @param sortField 排序字段
	 * @param sortord 排序方式
	 */
	public PageBean<SiteOrg> getSiteOrgList(int siteId, String orgName, String pageNo, String sortField, String sortord);
	
	/**
	 * 查询站点下的所有组织机构
	 */ 
	public PageBean<SiteOrg> getSiteOrgList(int siteId);
	
	/**
	 * 获取未分配组织机构的用户列表
	 * @param 
	 */
	public PageBean<UserBase> getOrgUserList(String orgName, String sortField,
			String sortord, Integer siteId,Integer creator, String pageNo, List<SiteOrg> orgList);
	
	/**
	 * 为用户批量分配机构
	 */
	public boolean updateOrgUserBatch(String orgId, String[] ids, UserBase currentUser);
	
	/**
	 * 新增站点下的组织机构
	 *  
	 */
	public SiteOrg addSiteOrg(SiteOrg siteOrg, UserBase currentSiteAdmin);
	
	/**
	 * 修改站点下的组织机构 
	 */
	public SiteOrg updateSiteOrg(SiteOrg siteOrg);
	
	/**
	 * 删除站点下的组织机构 
	 */
	public boolean deleteSiteOrg(int orgId, UserBase currentSiteAdmin);
	
	/**
	 * 组织机构详细信息 
	 */
	public SiteOrg getSiteOrgById(int orgId);
	
	/**
	 * 根据机构ID获取机构人数
	 * shhc
	 * 2013-5-7
	 */
	public List<UserBase> getOrgUserList(int orgId, int siteId);
	
}
