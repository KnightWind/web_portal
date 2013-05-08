package com.bizconf.audio.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bizconf.audio.entity.Contacts;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.UserBase;

public interface ContactService {
	
	/**
	 * 根据姓名、手机或邮箱地址模糊查询联系人列表统计条数
	 * wangyong
	 * 2013-3-11
	 */
	public Integer countContactsList(String nameOrEmailOrMobile, UserBase currentUser);
	
	/**
	 * 根据姓名、手机或邮箱地址模糊查询联系人列表
	 * wangyong
	 * 2013-3-11
	 */
	public List<Contacts> listContactsList(String nameOrEmailOrMobile, PageModel pageModel, String sortField, String sortord, UserBase currentUser);
	
	/**
	 * 获取企业通讯录条数
	 * 站点下的企业用户：
	 * 1.如果该企业用户为超级企业管理员创建，则可以看到站点下的所有企业用户；
	 * 2.如果该企业用户为企业管理员创建，则只能看到企业管理员创建的企业用户名单。
	 * wangyong
	 * 2013-3-11
	 */
	public Integer countSiteContactsList(String nameOrEmailOrMobile, UserBase currentUser);
	
	/**
	 * 获取企业通讯录
	 * 站点下的企业用户：
	 * 1.如果该企业用户为超级企业管理员创建，则可以看到站点下的所有企业用户；
	 * 2.如果该企业用户为企业管理员创建，则只能看到企业管理员创建的企业用户名单。
	 * wangyong
	 * 2013-3-11
	 */
	public List<Contacts> listSiteContactsList(String nameOrEmailOrMobile, PageModel pageModel, String sortField, String sortord, UserBase currentUser);
	
	/**
	 * 新建联系人（单个）
	 * wangyong
	 * 2013-3-11
	 */
	public Contacts createContactSingle(Contacts contact, UserBase currentUser);
	
	/**
	 * 根据联系人ID获取联系人详细信息
	 * wangyong
	 * 2013-3-11
	 */
	public Contacts getContactById(Integer contactId);
	
	/**
	 * 修改联系人
	 * wangyong
	 * 2013-3-11
	 */
	public Contacts updateContactInfo(Contacts contact, UserBase currentUser);
	
	/**
	 * 根据联系人ID删除联系人
	 * wangyong
	 * 2013-3-11
	 */
	public boolean deleteContactInfo(Integer contactId, UserBase currentUser);
	
	/**
	 * 批量删除联系人
	 * wangyong
	 * 2013-3-11
	 */
	public boolean deleteContactInfoBatch(String[] ids, UserBase currentUser);
	
	/**
	 * 根据邮箱地址获取联系人
	 * wangyong
	 * 2013-3-11
	 */
	public Contacts getContactByEmail(String email,UserBase currentUser);
	
	/**
	 * 从Excel文件批量导入联系人
	 * wangyong
	 * 2013-3-12
	 */
	public boolean importBatchByExcel(List<Contacts> importusers, UserBase currentUser);
	
	/**
	 * 从企业通讯录批量导入联系人
	 * wangyong
	 * 2013-3-12
	 */
	public boolean importBatchByContacts(Integer[] ids, UserBase currentUser);
	
	
	/**
	 * 获取企业通信录联系人
	 * @param currUser
	 * @return
	 */
	PageBean<UserBase> getEnterpriseContacts(String keyword,Integer pageNo,boolean isSupper,boolean showAll,UserBase currUser);
	
	
	/**
	 * 查询联系人
	 * @param keyword
	 * @param siteId
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	PageBean<Contacts> getContactsPage(String keyword, Integer siteId,Integer userId,
			Integer pageNo, Integer pageSize);
	
	
	/**
	 * 从excel导入
	 * @param importusers
	 * @param currentUser
	 * @return
	 */
	boolean importContactBatch(List<Contacts> importusers);
	
	/**
	 * 判断企业用户是否已经被添加到联系人中
	 * is enterprise user exist in contacts  
	 * @param enterUserId   将要添加到通讯录的企业用户ID
	 * @param currentUserId 当前登录的用户ID
	 * @return
	 */
	 boolean enterUserIsExist(int enterUserId,int currentUserId);
	 
	 /**
	  * 对导入通信录得企业用户ID进行校验，返回可以导入的企业用户IDs
	  * @param ids
	  * @param currentUserId
	  * @return
	  */
	 Map<String, Integer[]> getCanSaveableIds(String[] ids,int currentUserId);
	 
	 
	 /**
	  * 对外提供的企业联系人查询
	  * @param siteId
	  * @param keyword
	  * @param pageNo
	  * @param pageSize
	  * @param isExactQuery
	  * @return
	  */
	 PageBean<UserBase> getEnterpriseUsers(Integer siteId,String keyword,int pageNo,int pageSize,boolean isExactQuery);
	 
}
