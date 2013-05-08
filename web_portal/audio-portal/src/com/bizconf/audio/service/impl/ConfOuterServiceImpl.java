package com.bizconf.audio.service.impl;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.bizconf.audio.entity.ConfOuter;
import com.bizconf.audio.service.ConfOuterService;
import com.bizconf.audio.util.StringUtil;


@Service
public class ConfOuterServiceImpl extends BaseService implements ConfOuterService  {



	@Override
	public ConfOuter getConfBaseByMeyKey(String mtgKey) {
		ConfOuter outer=null;
		if(!StringUtil.isEmpty(mtgKey)){
			StringBuffer sqlBuffer=new StringBuffer();
			sqlBuffer.append("select * from t_conf_outer tco where tco.mtg_key = ? ");
			Object[] values=new Object[]{mtgKey};
			try {
				outer=libernate.getEntityCustomized(ConfOuter.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sqlBuffer=null;
			values=null;
		}
		return outer;
	}

	


	@Override
	public ConfOuter getConfBaseByMeyKeyAndSiteId(String mtgKey,String  siteSign) {
		ConfOuter outer=null;
		if(!StringUtil.isEmpty(mtgKey)){
			StringBuffer sqlBuffer=new StringBuffer();
			sqlBuffer.append("select * from t_conf_outer tco where tco.mtg_key = ? and tco.site_sign=?");
			Object[] values=new Object[]{mtgKey,siteSign};
			try {
				outer=libernate.getEntityCustomized(ConfOuter.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sqlBuffer=null;
			values=null;
		}
		return outer;
	}
	@Override
	public ConfOuter saveConfOuter(ConfOuter confOuter) {
		if(confOuter!=null && confOuter.getMtgTitle() !=null && !"".equals(confOuter.getMtgKey())){
			try {
				confOuter=libernate.saveEntity(confOuter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return confOuter;
		
	}
 
	
	
	
	

}
