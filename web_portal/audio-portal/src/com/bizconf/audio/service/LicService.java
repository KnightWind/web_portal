package com.bizconf.audio.service;

import java.util.List;
import java.util.Map;

import com.bizconf.audio.entity.License;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.UserBase;

/**
 * @desc  license管理service
 * @author martin
 * @date 2013-3-26
 */
public interface LicService {
	
	/**
	 * 查询licnese列表
	 * @return
	 */
	PageBean<License> getLicensePage(int siteId,int userId,int pageNo);
	
	/**
	 * 保存或者修改license
	 * @param license
	 * @return
	 */
	boolean saveOrUpdate(License license);
	
	/**
	 * 根据ID删除license
	 * @param licId
	 * @param deleter 删除者
	 * @return
	 */
	boolean delLicense(int licId,int deleter);
	
	/**
	 * 获取name host 模式下的主持人列表
	 * @param keyword
	 * @param siteId
	 * @param pageNo
	 * @return
	 */
	PageBean<UserBase> getHostsBySite(int siteId,int pageNo);
	
	/**
	 * 根据ID获取license对象
	 * @param id
	 * @return
	 */
	License getLicenseById(int id);
	
	/**
	 * 获取namehost模式下某个主持人的license点数
	 * @param userId
	 * @return
	 */
	int getHostLicenseNum(Integer userId);
	
	/**
	 * 获取nameHost模式下主持人对应点数的map
	 * @param hosts
	 * @return
	 */
	Map<Integer, Integer> getHostsLienseDatas(List<UserBase> hosts);
	
	/**
	 * 获取站点下有效license数
	 * @param siteId
	 * @return
	 */
	public int getSiteLicenseNum(Integer siteId);
	
	
	/**
	 *  获取某主持人的license记录
	 * @param hostId
	 * @return
	 */
	public List<License> getHostLicenses(Integer hostId);
	
	
	/**
	 * 激活一个license
	 * @param lic
	 * @return
	 */
	public boolean effeLicense(License lic);
	
	
	/**
	 * 废除一个license
	 * @param lic
	 * @return
	 */
	public boolean expiredLicense(License lic);
	
	/**
	 * 获取指定站点的所有点数管理记录
	 * wangyong
	 * 2013-5-3
	 */
	public List<License> getSiteLicenseList(int siteId);
}
