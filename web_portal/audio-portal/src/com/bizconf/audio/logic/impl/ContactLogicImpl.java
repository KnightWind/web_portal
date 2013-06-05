package com.bizconf.audio.logic.impl;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.Contacts;
import com.bizconf.audio.logic.ContactLogic;
import com.bizconf.audio.service.impl.BaseService;

@Service
public class ContactLogicImpl extends BaseService implements ContactLogic {
	
	/**
	 * 修改联系人（单个）验证前台表单数据
	 * wangyong
	 * 2013-3-11
	 */
	@Override
	public boolean updateContactSingleValidate(Contacts contact) {
		return true;
	}
	
	/**
	 * 新建联系人（单个）验证前台表单数据
	 * 2013-3-11
	 */
	@Override
	public boolean createContactSingleValidate(Contacts contact) {
		if(contact.getContactName()==null || contact.getContactName().equals("")){
			return false;
		}else if(contact.getContactName().length()<1 || contact.getContactName().length()>32){
			return false;
		}else{
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5_\\-&]{1,32}$");
			Matcher matcher = pattern.matcher(contact.getContactName());
			if(!matcher.matches()){
				return false;
			}
		}
		
		if(contact.getContactPhone()==null || contact.getContactPhone().equals("")){
			return false;
		}
		else {
			Pattern pattern = Pattern.compile("^((\\+?[0-9]{2,4}\\-[0-9]{3,4}\\-)|([0-9]{3,4}\\-))?([0-9]{7,8})(\\-[0-9]+)?$");
			Matcher matcher = pattern.matcher(contact.getContactPhone());
			Pattern patternMobile = Pattern.compile("^((\\+86)?|\\(\\+86\\)|\\+86\\s|\\+86-)0?1[358]\\d{9}$");
			String mobile = contact.getContactMobile();
			if(mobile==null){
				mobile = "";
			}
			Matcher matcherMobile = patternMobile.matcher(mobile);
			if(!matcher.matches() && !matcherMobile.matches()){
				return false;
			}
		}
		
		if(contact.getContactEmail()==null || contact.getContactEmail().equals("")){
			return false;
		}else{
			Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
			Matcher matcher = pattern.matcher(contact.getContactEmail());
			if(!matcher.matches()){
				return false;
			}
		}
		
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_contacts WHERE del_flag !=? and contact_email = ? and user_id = ? and id!=?");
		Object[] values = new Object[4];
		values[0] = ConstantUtil.DELFLAG_DELETED;
		values[1] = contact.getContactEmail();
		values[2] = contact.getUserId();
		values[3] = contact.getId()==null?0:contact.getId();
		try {
//			int count = libernate.queryForInt(strSql.toString(), values);
//			if(count>0){
//				return false;
//			}
			contact = libernate.getEntityCustomized(Contacts.class, strSql.toString(), values);
			if(contact!=null){
				return false;
			}
		} catch (SQLException e) {
			logger.error("根据邮箱地址获取联系人详细信息出错！",e);
			return false;
		}
		
		
		return true;
	}
	
	
}
