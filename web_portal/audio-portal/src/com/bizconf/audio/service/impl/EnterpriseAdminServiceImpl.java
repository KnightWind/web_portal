package com.bizconf.audio.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SortConstant;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.EmpowerLogic;
import com.bizconf.audio.service.EnterpriseAdminService;
import com.bizconf.audio.util.BeanUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;

/**
 * @desc 
 * @author martin
 * @date 2013-2-17
 */
@Service
public class EnterpriseAdminServiceImpl extends BaseService implements EnterpriseAdminService{
	
	@Autowired
	EmpowerLogic empowerLogic;
	@Override
	public List<UserBase> getSiteUserBases(String keyword, String sortField,String sortRule,Integer siteId,Integer creator) {
		
		List<UserBase> admins = null;
		List<Object> values = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(" from t_user_base where del_flag = ? and user_type = ? ");
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		values.add(ConstantUtil.USERTYPE_USERS);
		if(siteId!=null && siteId>0){
			sqlBuilder.append(" and site_id=? ");
			values.add(siteId);
		}
		if(keyword!=null && !keyword.trim().equals("")){
			sqlBuilder.append("and (login_name like ? or true_name like ? or user_email like ?)");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
		}
		if(creator!=null){
			sqlBuilder.append(" and create_user = ? ");
			values.add(creator);
		}
		sqlBuilder.append(" order by id desc ");
		try{
			 
			admins = libernate.getEntityListBase(UserBase.class,"select *"+sqlBuilder.toString(),values.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admins;
	}
	
	@Override
	public PageBean<UserBase> getUserBases(String keyword, String sortField,
			String sortRule, Integer siteId,Integer creator, String pageNo) {
		if(pageNo==null || pageNo.equals("")){
			pageNo = "1";
		}
		List<Object> values = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(" select * from t_user_base where del_flag = ? and user_type = ? ");
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		values.add(ConstantUtil.USERTYPE_USERS);
		if (siteId!=null && siteId>0) {
			sqlBuilder.append(" and site_id =? ");
			values.add(siteId);
		}
		
		if(creator!=null && creator.intValue()>0){
			sqlBuilder.append(" and create_user =? ");
			values.add(creator);
		}
		
		if(keyword!=null && !keyword.trim().equals("")){
			sqlBuilder.append("and (login_name like ? or true_name like ? or user_email like ?)");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
		}
		if(sortField!=null && !sortField.trim().equals("")){
			sqlBuilder.append(" order by "+initSort(sortField));
			if(SortConstant.SORT_ASC.equals(sortRule)){
				sqlBuilder.append(" asc ");
			}else{
				sqlBuilder.append(" desc ");
			}
		}else{
			sqlBuilder.append(" order by id desc ");
		}
		PageBean<UserBase> pageModel = getPageBeans(UserBase.class, sqlBuilder.toString(), Integer.parseInt(pageNo), values.toArray(new Object[values.size()]));
		return pageModel;
	}
	
	@Override
	public PageBean<UserBase> getSiteManagers(String keyword, String sortField,
			String sortRule, Integer siteId,Integer creator, String pageNo) {
		if(pageNo==null || pageNo.equals("")){
			pageNo = "1";
		}
		List<Object> values = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(" select * from t_user_base where del_flag = ? and user_type=? ");
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		values.add(ConstantUtil.USERTYPE_ADMIN);
		if (siteId!=null && siteId>0) {
			sqlBuilder.append(" and site_id =? ");
			values.add(siteId);
		}
		if (creator!=null && creator>0) {
			sqlBuilder.append(" and create_user =? ");
			values.add(creator);
		}
		if(keyword!=null && keyword.trim().equals("")){
			sqlBuilder.append("and (login_name like ? or true_name like ? or user_email like ?)");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
		}
		if(sortField!=null && !sortField.trim().equals("")){
			sqlBuilder.append(" order by "+initSort(sortField));
			if(SortConstant.SORT_ASC.equals(sortRule)){
				sqlBuilder.append(" asc ");
			}else{
				sqlBuilder.append(" desc ");
			}
		}else{
			sqlBuilder.append(" order by id desc ");
		}
		PageBean<UserBase> pageModel = getPageBeans(UserBase.class, sqlBuilder.toString(), Integer.parseInt(pageNo), values.toArray(new Object[values.size()]));
		
		return pageModel;
	}
	
	/**
	 * 获取分页列表
	 */
	@Override
	public PageBean<UserBase> getUserBasesBySite(String keyword,
			Integer siteId, String pageNo) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(" select * from t_user_base where del_flag = ? and site_id =? ");
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		values.add(siteId);
		if(keyword!=null && keyword.trim().equals("")){
			sqlBuilder.append("and (login_name like ? or true_name like ? or user_email like ?)");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
			values.add("%"+ keyword +"%");
		}
		
		sqlBuilder.append(" order by id desc ");
		
		PageBean<UserBase> pageModel = getPageBeans(UserBase.class, sqlBuilder.toString(), Integer.parseInt(pageNo), values.toArray(new Object[values.size()]));
		return pageModel;
	}
	
	/**
	 * 删除用户操作
	 */
	@Override
	public boolean deleteUserBases(String[] ids,int delUserId) {
		boolean deleteFlag = true;
		StringBuilder sqlBuilder = new StringBuilder("update t_user_base t set t.del_flag=?,t.del_time=?,t.del_user=? where t.id in (0");
		for(String id:ids){
			sqlBuilder.append(",");
			sqlBuilder.append(id);
		}
		sqlBuilder.append(")");
		Object[] values = new Object[3];
		values[0] = ConstantUtil.DELFLAG_DELETED;
		values[1] = DateUtil.getDateStrCompact(DateUtil.getGmtDate(null), "yyyy-MM-dd HH:mm:ss");
		values[2] = delUserId;
		
		
		try {
			deleteFlag = libernate.executeSql(sqlBuilder.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
			deleteFlag = false;
		}
		return deleteFlag;
	}
	
	@Override
	public boolean deleteUserBase(Integer id,int delUserId) {
		boolean deleteFlag = true;
		StringBuilder sqlBuilder = new StringBuilder("update t_user_base t set t.del_flag=?,t.del_time=?,t.del_user=? where t.id = ?");
		Object[] values = new Object[4];
		values[0] = ConstantUtil.DELFLAG_DELETED;
		values[1] = DateUtil.getDateStrCompact(DateUtil.getGmtDate(null), "yyyy-MM-dd HH:mm:ss");
		values[2] = delUserId;
		values[3] = id;
		try {
			deleteFlag = libernate.executeSql(sqlBuilder.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
			deleteFlag = false;
		}
		return deleteFlag;
	}
	
	@Override
	public boolean changeLockState(String[] ids, Integer userStatus) throws Exception {
		boolean lockFlag = true;
		StringBuilder sqlBuilder = new StringBuilder("update t_user_base t set t.user_status=? where t.del_flag=? and t.id in (0");
		for(String id:ids){
			sqlBuilder.append(",");
			sqlBuilder.append(id);
		}
		sqlBuilder.append(")");
		Object[] values = new Object[2];
		values[0] = userStatus;
		values[1] = ConstantUtil.DELFLAG_UNDELETE;
		lockFlag = libernate.executeSql(sqlBuilder.toString(), values);
		return lockFlag;
	}

	@Override
	public boolean updateUserBase(UserBase userBase) {
		try {
			libernate.updateEntity(userBase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	 
	/**
	 * 验证该对象是否信息完整合符规范
	 * @param userBase
	 * @return
	 */
	public boolean validUserBase(UserBase user){
		//登录名
		if(user.getLoginName()==null ||user.getLoginName().equals("")){
			return false;
		}else{
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{4,16}$");
			Matcher matcher = pattern.matcher(user.getLoginName());
			if(!matcher.matches()){
				return false;
			}
		}
		if(user.getLoginPass()==null || user.getLoginPass().equals("")){
			return false;
		}
		if(user.getSiteId()==null || user.getSiteId()<=0){
			return false;
		}
		if(user.getTrueName()==null || user.getTrueName().equals("")){
			return false;
		}
		//验证邮箱
		if(user.getUserEmail()==null || user.getUserEmail().equals("")){
			return false;
		}else{
			Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
			Matcher matcher = pattern.matcher(user.getUserEmail());
			if(!matcher.matches()){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 批量验证是否存在重复账号
	 * @param users
	 * @return
	 */
	public boolean validUserBaseBatch(List<UserBase> users,Integer siteId){
		if(users.size()<=0){
			return false;
		}
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" SELECT count(*) FROM t_user_base where id > 0 ");
		sqlBuffer.append(" and del_flag = ?");
		sqlBuffer.append(" and site_id = ? ");
		sqlBuffer.append(" and ( login_name='' ");
		List<Object> values =  new ArrayList<Object>();
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		values.add(siteId);
		for (Iterator<UserBase> it = users.iterator(); it.hasNext();) {
			sqlBuffer.append("or");
			UserBase user =  it.next();
			sqlBuffer.append("login_name=? ");
			values.add(user.getLoginName());
		}
		sqlBuffer.append(")");
		int count = libernate.queryForInt(sqlBuffer.toString(), values.toArray(new Object[values.size()]));
		if(count>0){
			return false;
		}
		return true;
	}

	/**
	 * 保存企业用户信息
	 */
	@Override
	public UserBase saveUserBase(UserBase userBase) {
		UserBase user = null;
		try {
			if(userBase.getLoginPass()!=null){
				userBase.setLoginPass(MD5.encodePassword(userBase.getLoginPass(), "MD5"));
			}
			user = libernate.saveEntity(userBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * 获取排序字段名
	 * @param field
	 * @return
	 */
	private String initSort(String field){
		String sortField = field;
		for(int i=0;i<SortConstant.USERBASE_FIELDS.length;i++){
			if(StringUtil.isNotBlank(sortField) && sortField.trim().equals(SortConstant.USERBASE_FIELDS[i][0])){
				sortField = SortConstant.USERBASE_FIELDS[i][1];
				sortField = BeanUtil.att2Field(sortField);
			}
		}
		return sortField;
	}
	
	/**
	 * 批量添加站点用户  用于导入
	 */
	@Override
	public boolean batchAddUsers(List<UserBase> users,EmpowerConfig config) {
		try{
			for (Iterator<UserBase> it = users.iterator(); it.hasNext();) {
				UserBase userBase = (UserBase) it.next();
				UserBase newuser = libernate.saveEntity(userBase);
				if(config!=null){
					config.setUserId(newuser.getId());
					empowerLogic.saveEmpowerConfig(config);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
