package com.bizconf.audio.service.impl;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.LoginConstants;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;

@Service
public class UserServiceImpl extends BaseService implements UserService {

	@Autowired
	LoginService loginService;

	@Override
	public boolean deleteUserBaseByIds(Integer... ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserBase getCurrentSiteAdmin(HttpServletRequest request) {
		if (!loginService.isSiteAdminLogined(request)) {
			return null;
		}
		String domain = SiteIdentifyUtil.getCurrentBrand()+ "." + SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
//		String suid = CookieUtil.getCookie(request, LoginConstants.SITE_ADMIN_USER_SESSION_ID_NAME);
		String suid = CookieUtil.getCookieByDomain(request, LoginConstants.SITE_ADMIN_USER_SESSION_ID_NAME,domain);
		int userId = Integer.parseInt(suid);
		try {
			UserBase user = libernate.getEntity(UserBase.class, userId);
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SystemUser getCurrentSysAdmin(HttpServletRequest request) {
		if (!loginService.isSysAdminLogined(request)) {
			return null;
		}

		String domain = SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
//		String said = CookieUtil.getCookie(request, LoginConstants.SYSTEM_USER_SESSION_ID_NAME);
		String said = CookieUtil.getCookieByDomain(request, LoginConstants.SYSTEM_USER_SESSION_ID_NAME,domain);
		int userId = Integer.parseInt(said);
		try {
//			request.getServletContext().getRealPath("");
			SystemUser user = libernate.getEntity(SystemUser.class, userId);
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public SystemUser getSystemUserById(Integer sysUserId){
		SystemUser sysUser=null;
		if(sysUserId!=null && sysUserId.intValue()>0){
			try {
				sysUser= libernate.getEntity(SystemUser.class, sysUserId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return sysUser;
	}
	@Override
	public List<SystemUser> getSysUserListByIds(Integer[] ids){
		List<SystemUser> list=null;
		if(ids!=null && ids.length > 0){
			StringBuffer sqlBuffer=new StringBuffer();
			List<Object> valueList=new ArrayList<Object>();
			sqlBuffer.append("SELECT * FROM  t_system_user where id > 0 ");
			sqlBuffer.append(" and ( ");
			int ii=0;
			for(Integer id:ids){
				if(id!=null && id.intValue() >0){
					if(ii>0){
						sqlBuffer.append("  or ");
					}
					sqlBuffer.append("  id = ?");
					valueList.add(id);
					ii++;
				}
			}
			sqlBuffer.append(" )");
			try {
				logger.info("sqlBuffer--->>"+sqlBuffer.toString());
				list=libernate.getEntityListBase(SystemUser.class, sqlBuffer.toString(), valueList.toArray());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				sqlBuffer=null;
				valueList=null;
			}
		}
		
		return list;
		
	}
	
	@Override
	public UserBase getSiteSupperMasterBySiteId(Integer siteId){
		UserBase userBase=null;
		if(siteId!=null && siteId.intValue()>0){
			StringBuffer sqlBuffer=new StringBuffer();
			sqlBuffer.append(" SELECT * FROM t_user_base where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and user_type = ?");
			sqlBuffer.append(" and site_id = ? ");
			Object[] values=new Object[3];
			values[0]=ConstantUtil.DELFLAG_UNDELETE;
			values[1]=ConstantUtil.USERTYPE_ADMIN_SUPPER;
			values[2]=siteId;
			try {
				userBase=libernate.getEntityCustomized(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				sqlBuffer=null;
				values=null;
			}
		}
		return userBase;
	}
	
	@Override
	public List<UserBase> getSiteSupperMasterBySiteIdArray(Integer[] siteIds){
		List<UserBase> userList=null;
		if(siteIds!=null && siteIds.length>0){
			StringBuffer sqlBuffer=new StringBuffer();
			Object[] values=new Object[siteIds.length+2];
			sqlBuffer.append(" SELECT * FROM t_user_base where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and user_type = ?");
			values[0]=ConstantUtil.DELFLAG_UNDELETE;
			values[1]=ConstantUtil.USERTYPE_ADMIN_SUPPER;
			int ii=2;
			sqlBuffer.append("  and (");
			for(Integer siteId:siteIds){
				if(siteId!=null && siteId.intValue()>0){
					if(ii>2){
						sqlBuffer.append(" or ");
					}
					sqlBuffer.append("  site_id = ? ");
					values[ii]=siteId;
					ii++;
				}
				
			}
			sqlBuffer.append("  )");
			try {
				userList=libernate.getEntityListBase(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				sqlBuffer=null;
				values=null;
			}
		}
		
		return userList;
	}
	
	public List<UserBase> getUserListByUserIdArray(Integer[] userIds){
		List<UserBase>  userList=null;
		if(userIds !=null && userIds.length>0){
			StringBuffer sqlBuffer=new StringBuffer();
			Object[] values=new Object[userIds.length];
//			Object[] values=new Object[3+userIds.length];
			sqlBuffer.append("select * from t_user_base where id >0 ");
//			sqlBuffer.append(" and del_flag = ?");
//			sqlBuffer.append(" and (");
//			sqlBuffer.append(" user_type =?");
//			sqlBuffer.append(" or  user_type =?");
//			sqlBuffer.append(")");
//			values[0]=ConstantUtil.DELFLAG_UNDELETE;
//			values[0]=ConstantUtil.USERTYPE_ADMIN;
//			values[1]=ConstantUtil.USERTYPE_ADMIN_SUPPER;
//			int ii=3;
//			sqlBuffer.append(" and ( ");
//			for(Integer userId:userIds){
//				if(ii>3){
//					sqlBuffer.append(" or");
//				}
//				sqlBuffer.append(" id = ?");
//				values[ii]=userId;
//				ii++;
//			}
			int ii=0;
			sqlBuffer.append(" and ( ");
			for(Integer userId:userIds){
				if(ii>0){
					sqlBuffer.append(" or");
				}
				sqlBuffer.append(" id = ?");
				values[ii]=userId;
				ii++;
			}
			sqlBuffer.append(" ) ");
			logger.info("---->>>SqlBuffer>"+sqlBuffer.toString());
			try {
				userList=libernate.getEntityListBase(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return userList;
	}
	
	@Override
	public UserBase getCurrentUser(HttpServletRequest request) {
		
		if (!loginService.isLogined(request)) {
			return null;
		}
		String domain = SiteIdentifyUtil.getCurrentBrand()+ "." + SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
//		String suid = CookieUtil.getCookie(request, LoginConstants.SITE_ADMIN_USER_SESSION_ID_NAME);
		String uid = CookieUtil.getCookieByDomain(request, LoginConstants.USER_SESSION_ID_NAME,domain);
		int userId = Integer.parseInt(uid);
		try {
			UserBase user = libernate.getEntity(UserBase.class, userId);
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UserBase getUserBaseById(Integer userId) {
		try {
			if(userId!=null && userId.intValue() >0 ){
				return libernate.getEntity(UserBase.class, userId);
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public boolean deleteUserBaseById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public List<UserBase> getUserListByNameOrEmail(String nameOrEmail,
			String sortField, String sortord, PageModel pageModel) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<UserBase> getUserListByNameOrEmail(String nameOrEmail) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean importUserByExcelFileName(String excelFileName) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean lockUserBaseById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean lockUserBaseByIds(Integer... ids) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean saveUserBase(UserBase userBase) {
		 try {
			 if(userBase!=null){
				 if(userBase.getId()!=null && userBase.getId().intValue() > 0 ){
					 String[] fields=ObjectUtil.getFieldFromObject(userBase);
					 libernate.updateEntity(userBase,fields);
					 fields=null;
				 }else{
					 libernate.saveEntity(userBase);
				 }
			 }
			//libernate.saveEntity(userBase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		 return true;
	}
	
	@Override
	public boolean unlockUserBaseById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlockUserBaseByIds(Integer... ids) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 根据登录名获取指定站点下的站点管理员
	 * wangyong
	 * 2013-1-31
	 */
	@Override
	public UserBase getSiteAdminByLoginName(Integer siteId, String loginName) {
		UserBase userBase = null;
		if(siteId != null && siteId.intValue() > 0){
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(" SELECT * FROM t_user_base where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and login_name = ?");
			sqlBuffer.append(" and site_id = ? ");
			sqlBuffer.append(" and (user_type = ? or user_type = ?)");
			Object[] values=new Object[]{
				ConstantUtil.DELFLAG_UNDELETE,
				loginName,
				siteId,
				ConstantUtil.USERTYPE_ADMIN,
				ConstantUtil.USERTYPE_ADMIN_SUPPER
			};
			try {
				userBase = libernate.getEntityCustomized(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				logger.error("根据登录名获取指定站点下的站点管理员出错",e);
			}finally{
				sqlBuffer = null;
				values = null;
			}
		}
		return userBase;
	}
	
	/**
	 * 根据登录名获取指定站点下的用户
	 * wangyong
	 * 2013-1-31
	 */
	@Override
	public UserBase getSiteUserByLoginName(Integer siteId, String loginName) {
		UserBase userBase = null;
		if(siteId != null && siteId.intValue() > 0){
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(" SELECT * FROM t_user_base where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and login_name = ?");
			sqlBuffer.append(" and site_id = ? ");
			sqlBuffer.append(" and user_type = ?");
			Object[] values=new Object[4];
			values[0] = ConstantUtil.DELFLAG_UNDELETE;
			values[1] = loginName;
			values[2] = siteId;
			values[3] = ConstantUtil.USERTYPE_USERS;
			try {
				userBase = libernate.getEntityCustomized(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				logger.error("根据登录名获取指定站点下的用户出错",e);
			}finally{
				sqlBuffer = null;
				values = null;
			}
		}
		return userBase;
	}

	/**
	 * 根据邮箱名获取指定站点下的用户
	 * wangyong
	 * 2013-1-31
	 */
	@Override
	public UserBase getSiteUserByEmail(Integer siteId, String email) {
		UserBase userBase = null;
		if(StringUtil.isNotBlank(email)){
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(" SELECT * FROM t_user_base where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and user_type = ?");
			sqlBuffer.append(" and user_email = ?");
			sqlBuffer.append(" and site_id = ? ");
			Object[] values=new Object[4];
			values[0] = ConstantUtil.DELFLAG_UNDELETE;
			values[1] = ConstantUtil.USERTYPE_USERS;
			values[2] = email.trim();
			values[3] = siteId;
			try {
				userBase = libernate.getEntityCustomized(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				logger.error("根据邮箱名获取指定站点下的用户出错",e);
			}finally{
				sqlBuffer = null;
				values = null;
			}
		}
		return userBase;
	}
	

	/**
	 * 根据邮箱获取站点下的管理员或超级管理员信息
	 * alan
	 * 2013-3-7
	 */
	@Override
	public UserBase getSiteAdminByEmail(Integer siteId, String email) {
		UserBase userBase = null;
		if(StringUtil.isNotBlank(email)){
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(" SELECT * FROM t_user_base where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and (user_type = ? or user_type = ?)");
			sqlBuffer.append(" and user_email = ?");
			sqlBuffer.append(" and site_id = ? ");
			Object[] values=new Object[5];
			values[0] = ConstantUtil.DELFLAG_UNDELETE;
			values[1] = ConstantUtil.USERTYPE_ADMIN;
			values[2] = ConstantUtil.USERTYPE_ADMIN_SUPPER;
			values[3] = email.trim();
			values[4] = siteId;
			try {
				userBase = libernate.getEntityCustomized(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				logger.error("根据邮箱名获取指定站点下的用户出错",e);
			}finally{
				sqlBuffer = null;
				values = null;
			}
		}
		return userBase;
	}
	
	/**
	 * 后台校验是否可以保存
	 * @param user
	 * @return
	 */
	public boolean siteUserSaveable(UserBase user){
		if(user==null){
			return false;
		}
		if(user.getId()!=null && user.getId()>0){
			UserBase orgUser = getUserBaseById(user.getId());
			if(user.getLoginName().equals(orgUser.getLoginName())){
				return true;
			}
//			if(user.getUserEmail().equals(orgUser.getUserEmail())){
//				return true;
//			}
		}
		
		if(null!=getSiteUserByLoginName(user.getSiteId(),user.getLoginName())){
				return false;
		}
//		if(null!=getSiteUserByEmail(user.getSiteId(),user.getUserEmail())){
//				return false;
//		}
		return true;
	}
	
	
	/**
	 * 统计系统管理员总记录数
	 * wangyong
	 * 2013-2-5
	 */
	@Override
	public int CountSystemUser(Condition condition) {
		int rows = 0;
		Object[] values = new Object[2];
		StringBuffer strSql = new StringBuffer(" select count(1) from t_system_user where del_flag = ? and sys_type = ? ");
		values[0] = ConstantUtil.DELFLAG_UNDELETE;
		values[1] = ConstantUtil.USERTYPE_SYSTEM;
		if(condition != null){
			strSql.append(" and ").append(condition.getConditionSql());
		} 
		try {
			rows = DAOProxy.getLibernate().countEntityListWithSql(strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("统计系统管理员总记录数出错"+e);
		}
		return rows;
	}

	/**
	 * 根据条件获取系统管理员列表
	 * wangyong
	 * 2013-2-5
	 */
	@Override
	public List<SystemUser> getSystemUserList(Condition condition,
			PageModel pageModel) {
		List<SystemUser> systemUserList = null;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" select * from t_system_user where del_flag = ? and (sys_type = ? or sys_type = ?)  ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConstantUtil.USERTYPE_SYSTEM);
		valueList.add(ConstantUtil.USERTYPE_SYS_SERVER);
		if(condition != null){
			strSql.append(" and ").append(condition.getConditionSql());
		} 
		strSql.append(" order by id DESC ");   //查出列表无排序条件则为默认逆序
		if(pageModel != null){
			int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
			strSql.append(" limit ? , ?  ");
			valueList.add(recordNo);
			valueList.add(pageModel.getPageSize());
		}
		try{
			Object[] values = valueList.toArray();
			systemUserList = DAOProxy.getLibernate().getEntityListBase(SystemUser.class, strSql.toString(), values);
		}catch (Exception e){
			logger.error("根据条件获取系统管理员列表出错"+e);
		}
		return systemUserList;
	}
	
	/**
	 * 新建系统管理员
	 * wangyong
	 * 2013-2-5
	 */
	@Override
	public SystemUser createSystemUser(SystemUser systemUser){
		SystemUser sysUser = null;
		if(systemUser != null){
//			if(!userLogic.createSysUserValidate(systemUser)){
//				return null;
//			}
			try {
				sysUser = DAOProxy.getLibernate().saveEntity(systemUser);    //首先保存站点的基本信息
			} catch (Exception e) {
				logger.error("创建系统管理员失败", e);
				sysUser = null;
			}
		}
		return sysUser;
	}
	
	/**
	 * 删除系统管理员
	 * wangyong
	 * 2013-2-5
	 */
	@Override
	public boolean delSystemUser(int id, SystemUser currentSystemUser){
		boolean updateFlag = false;
		if(id > 0 && currentSystemUser != null){
			String updateSql = "update t_system_user set del_flag = ?,del_time = ?,del_user = ? where id = ? ";
			Object[] values = new Object[4];
			values[0] = ConstantUtil.DELFLAG_DELETED;
			values[1] = DateUtil.getDateStrCompact(DateUtil.getGmtDate(null), "yyyy-MM-dd HH:mm:ss");
			values[2] = currentSystemUser.getId();
			values[3] = id;
			try{
				updateFlag = DAOProxy.getLibernate().executeSql(updateSql, values);
			}catch (Exception e){
				logger.error("删除系统管理员出错"+e);
			}
		}
		return updateFlag;
	}
	
	/**
	 * 修改系统管理员
	 * wangyong
	 * 2013-2-5
	 */
	@Override
	public SystemUser updateSystemUser(SystemUser systemUser){
		SystemUser sysUser = null;
		if(systemUser != null){
//			if(!userLogic.updateSysUserValidate(systemUser)){
//				return null;
//			}
			try {
				if(StringUtil.isNotBlank(systemUser.getLoginPass())){
					sysUser = DAOProxy.getLibernate().updateEntity(systemUser, "id", "loginName", "loginPass", "trueName", "enName", "email", "mobile", "remark", "pass_editor"); 
				}else{
					sysUser = DAOProxy.getLibernate().updateEntity(systemUser, "id", "loginName", "trueName", "enName", "email", "mobile", "remark"); 
				}
				if(sysUser!=null && sysUser.getId().intValue() > 0){
					logger.info("修改系统管理员成功");
				}else{
					logger.error("修改系统管理员失败");
				}
			} catch (Exception e) {
				sysUser = null;
				logger.error("修改系统管理员失败", e);
			}
		}
		return sysUser;
	}
	
	public boolean updatePassWord(SystemUser systemUser){
		if(systemUser==null){
			return false; 
		}
		boolean saveStatus=false;
		if(StringUtil.isNotBlank(systemUser.getLoginPass())){
			try {
				systemUser = DAOProxy.getLibernate().updateEntity(systemUser, "id","loginPass");
				saveStatus = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return saveStatus;
		
	}

	@Override
	public boolean updateAdminPassWord(UserBase userBase){
		if(userBase==null){
			return false; 
		}
		boolean saveStatus=false;
		if(StringUtil.isNotBlank(userBase.getLoginPass())){
			try {
				userBase = DAOProxy.getLibernate().updateEntity(userBase, "id","loginPass");
				saveStatus = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return saveStatus;
		
	}
	
	/**
	 * 根据系统管理员登录名获取系统管理员信息
	 * wangyong
	 * 2013-2-5
	 */
	public SystemUser getSystemUserByLoginName(String loginName){
		SystemUser sysUser = null;
		if(StringUtil.isNotBlank(loginName)){
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(" SELECT * FROM t_system_user where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and login_name = ?");
			Object[] values=new Object[3];
			values[0] = ConstantUtil.DELFLAG_UNDELETE;
			values[1] = loginName;
			try {
				sysUser = libernate.getEntityCustomized(SystemUser.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				logger.error("根据登录名获取系统管理员出错",e);
			}finally{
				sqlBuffer = null;
				values = null;
			}
		}
		return sysUser;
	}
	
	/**
	 * 根据系统管理员邮箱获取系统管理员信息
	 * wangyong
	 * 2013-2-5
	 */
	@Override
	public SystemUser getSystemUserByEmail(String userEmail) {
		SystemUser sysUser = null;
		if(StringUtil.isNotBlank(userEmail)){
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append(" SELECT * FROM t_system_user where id > 0 ");
			sqlBuffer.append(" and del_flag = ?");
			sqlBuffer.append(" and email = ?");
			Object[] values=new Object[3];
			values[0] = ConstantUtil.DELFLAG_UNDELETE;
			values[1] = userEmail.trim();
			try {
				sysUser = libernate.getEntityCustomized(SystemUser.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				logger.error("根据邮箱名获取系统管理员出错",e);
			}finally{
				sqlBuffer = null;
				values = null;
			}
		}
		return sysUser;
	}
}
