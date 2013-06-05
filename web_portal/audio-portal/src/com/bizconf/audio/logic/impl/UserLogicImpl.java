package com.bizconf.audio.logic.impl;

import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.UserLogic;

/**
 * @desc 
 * @author Administrator
 * @date 2013-5-22
 */
@Component
public class UserLogicImpl extends BaseLogic implements UserLogic {

	@Override
	public UserBase getUserBaseByLoginName(String loginName,Integer siteId) {
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

}
