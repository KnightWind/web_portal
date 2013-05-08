package com.bizconf.audio.logic.impl;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.EmailConfig;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.EmailLogic;
import com.bizconf.audio.service.impl.BaseService;
import com.bizconf.audio.util.DateUtil;
@Service
public class EmailLogicImpl extends BaseService implements EmailLogic{
	
	@Override
	public SiteBase getSiteBaseById(Integer id) {
		SiteBase siteBase = null;
		if(id != null && id.intValue()>0){
			StringBuffer strSql = new StringBuffer(" select * from t_site_base where id = ? ");
			Object[] values = new Object[1];
			try {
				values[0] = id;
				siteBase = libernate.getEntityCustomized(SiteBase.class, strSql.toString(), values);
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		return siteBase;
	}
	
	
	@Override
	public UserBase getSiteSupperMasterBySiteId(Integer siteId) {
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
				userBase= libernate.getEntityCustomized(UserBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				sqlBuffer=null;
				values=null;
			}
		}
		return userBase;
	}

	@Override
	public EmailConfig getSysEmailConfig(int siteId) throws Exception {
		EmailConfig emailConfig = null;
		try {
			emailConfig = libernate
					.getEntityCustomized(EmailConfig.class,
							"select * from t_email_config where del_flag=? and site_id=?",
							new Object[] { ConstantUtil.DELFLAG_UNDELETE, siteId });
			if(emailConfig==null){
				emailConfig = libernate
						.getEntityCustomized(EmailConfig.class,
								"select * from t_email_config where del_flag=? and site_id=?",
								new Object[] { ConstantUtil.DELFLAG_UNDELETE, 0 });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return emailConfig;
	}
	
	@Override
	public UserBase getUserBaseById(Integer userId) {
		try {
			return libernate.getEntity(UserBase.class, userId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Date getSiteLocalDate(Date date,Integer siteId){
		//默认为北京时间（GMT +8）
		//System.out.println("date: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
		int offset = 28800000;
		if(siteId!=null && siteId>0){
			SiteBase site = getSiteBaseById(siteId);
			offset = site.getTimeZone();
			if(offset == 0){
				offset = SiteConstant.TIMEZONE_WITH_CITY_MAP.get(site.getTimeZoneId()).getOffset();
			}
		}
		Date localDate = DateUtil.getOffsetDateByGmtDate(date, (long)offset);
		//System.out.println("localDate: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(localDate));
		return localDate;
	}
	
}
