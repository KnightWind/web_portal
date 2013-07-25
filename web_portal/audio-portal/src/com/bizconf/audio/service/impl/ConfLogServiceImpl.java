package com.bizconf.audio.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SortConstant;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.ConfUserLog;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.service.ConfLogService;
import com.bizconf.audio.util.DateUtil;
@Service
public class ConfLogServiceImpl extends BaseService implements ConfLogService{

	@Override
	public boolean saveConfLog(ConfLog confLog) {
		boolean saveStatus=false;
		if(confLog!=null){
			try {
				confLog.setCreateTime(DateUtil.getGmtDate(null));
				libernate.saveEntity(confLog);
				saveStatus=true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return saveStatus;
	}

	@Override
	public boolean batchSaveConfLog(List<ConfLog> logList) {
		boolean saveStatus=false;
		if(logList!=null && logList.size() > 0){
			for(ConfLog confLog:logList){
				try {
					confLog.setCreateTime(DateUtil.getGmtDate(null));
					libernate.saveEntity(confLog);
					saveStatus=true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			int ii=0;
//			StringBuffer sqlBuffer=new StringBuffer();
//			sqlBuffer.append("	INSERT INTO `t_conf_log`(`site_id`,`conf_id`,`user_id`,`user_name`,`user_role`,`email`,`phone`,`term_type`,`join_type`,`leave_type`,`join_time`,`exit_time`,`create_time`,`del_flag`,`join_ip`,`del_time`)");
//			sqlBuffer.append("	values");
//			sqlBuffer.append("");
//			Date nowDate=DateUtil.getGmtDate(null);
//			List<Object> valueList=new ArrayList<Object>();
//			Calendar calendar=Calendar.getInstance();
//			calendar.set(1970,0, 1, 0,0,0);
//			Date delDate=calendar.getTime();
//			for(ConfLog confLog:logList){
//				if(confLog!=null){
//					if(ii>0){
//						sqlBuffer.append(",");
//					}
//					sqlBuffer.append("(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//					if(confLog.getSiteId()==null){
//						valueList.add(0);
//					}else{
//						valueList.add(confLog.getSiteId());
//					}
//					valueList.add(confLog.getConfId());
//					valueList.add(confLog.getUserId());
//					valueList.add(confLog.getUserName());
//					valueList.add(confLog.getUserRole());
//					valueList.add(confLog.getEmail());
//					valueList.add(confLog.getPhone());
//					valueList.add(confLog.getTermType());
//					valueList.add(confLog.getJoinType());
//					valueList.add(confLog.getLeaveType());
//					valueList.add(confLog.getJoinTime());
//					valueList.add(confLog.getExitTime());
//					valueList.add(nowDate);
//					valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//					valueList.add("");
//					valueList.add(delDate);
//					ii++;
//				}
//			}
//			System.out.println("inser conflog sqlBuffer="+sqlBuffer.toString());
//			
//			try {
//				Object[] values= valueList.toArray();
//				saveStatus=libernate.executeSql(sqlBuffer.toString(),values);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		return saveStatus;
	}

	@Override
	public boolean delConfLog(ConfLog confLog) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ConfLog> getLogListByConfId(Integer confId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConfLog> getLogListByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer[] countUserByConfId(Integer confId) {
		Integer[] userCount = null;
		ConfUserLog confUserLog = null;
		if(confId != null && confId.intValue()>0){
			StringBuffer strSql = new StringBuffer(" SELECT tcb.id conf_id, ");
			strSql.append(" CASE  WHEN pcTbl.pcUser IS NULL THEN 0 ELSE pcTbl.pcUser END pc_user, ");
			strSql.append(" CASE  WHEN telTbl.telUser IS NULL THEN 0 ELSE telTbl.telUser END tel_user ");
			strSql.append(" FROM t_conf_base tcb ");
			strSql.append(" LEFT JOIN ( ");
			strSql.append(" SELECT conf_id,COUNT(id) AS pcUser FROM t_conf_log  ");
			strSql.append(" WHERE conf_id= ?  AND term_type= ? GROUP BY conf_id ");
			strSql.append(" ) pcTbl ON tcb.id=pcTbl.conf_id ");
			strSql.append(" LEFT JOIN ( ");
			strSql.append(" SELECT conf_id,COUNT(id) AS telUser FROM t_conf_log  ");
			strSql.append(" WHERE conf_id= ? AND term_type= ? GROUP BY conf_id ");
			strSql.append(" ) telTbl ON tcb.id=telTbl.conf_id ");
			strSql.append(" WHERE tcb.id= ? ");
			Object[] values = new Object[]{
				confId,
				ConfConstant.CONF_USER_TERM_TYPE_PC,
				confId,
				ConfConstant.CONF_USER_TERM_TYPE_MOBILE,
				confId
			};
			try {
				confUserLog  = DAOProxy.getLibernate().getEntityCustomized(ConfUserLog.class, strSql.toString(), values);
			} catch (SQLException e) {
				logger.error("根据会议ID号统计参会人数出错！" + e);
			}
			if(confUserLog != null && confUserLog.getConfId() != null){
				userCount = new Integer[4];
				userCount[0] = confUserLog.getConfId();
				userCount[1] = confUserLog.getTotalUser();
				userCount[2] = confUserLog.getPcUser();
				userCount[3] = confUserLog.getTelUser();
			}
		}
		return userCount;
	}

	@Override
	public List<Integer[]> countUserListByConfId(Integer[] confIds) {
		List<Integer[]> confUserLogCountList = new ArrayList<Integer[]>();
		Integer[] userCount = null;
		List<ConfUserLog> confUserLogList = null;
		if(confIds != null && confIds.length>0){
			StringBuffer strSql = new StringBuffer(" SELECT tcb.id conf_id, ");
			strSql.append(" CASE  WHEN pcTbl.pcUser IS NULL THEN 0 ELSE pcTbl.pcUser END pc_user, ");
			strSql.append(" CASE  WHEN telTbl.telUser IS NULL THEN 0 ELSE telTbl.telUser END tel_user ");
			strSql.append(" FROM t_conf_base tcb ");
			strSql.append(" LEFT JOIN ( ");
			strSql.append(" SELECT conf_id,COUNT(id) AS pcUser FROM t_conf_log  ");
			strSql.append(" WHERE 1=1 ");
			strSql.append(" AND conf_id in( ");
			for(int i=0;i<confIds.length;i++){
				if(i>0){
					strSql.append(",");
				}
				strSql.append(confIds[i]);
			}
			strSql.append(" )");
			strSql.append(" AND term_type=3 GROUP BY conf_id ");
			strSql.append(" ) pcTbl ON tcb.id=pcTbl.conf_id ");
			strSql.append(" LEFT JOIN ( ");
			strSql.append(" SELECT conf_id,COUNT(id) AS telUser FROM t_conf_log  ");
			strSql.append(" WHERE 1=1 ");
			strSql.append(" AND conf_id in( ");
			for(int i=0;i<confIds.length;i++){
				if(i>0){
					strSql.append(",");
				}
				strSql.append(confIds[i]);
			}
			strSql.append(" )");
			strSql.append(" AND term_type=1 GROUP BY conf_id ");
			strSql.append(" ) telTbl ON tcb.id=telTbl.conf_id ");
			strSql.append(" WHERE 1=1 ");
			strSql.append(" AND conf_id in( ");
			for(int i=0;i<confIds.length;i++){
				if(i>0){
					strSql.append(",");
				}
				strSql.append(confIds[i]);
			}
			strSql.append(" )");
			Object[] values = new Object[2];
			try {
				values[0] = ConstantUtil.DELFLAG_UNDELETE;
				confUserLogList = libernate.getEntityListCustomized(ConfUserLog.class, strSql.toString(), values);
			} catch (SQLException e) {
				logger.error("根据会议ID数组,统计参会人数出错！" + e);
			}		
			if(confUserLogList != null && confUserLogList.size() > 0){
				for(ConfUserLog confUserLog : confUserLogList){
					userCount = new Integer[]{
						confUserLog.getConfId(),
						confUserLog.getTotalUser(),
						confUserLog.getPcUser(),
						confUserLog.getTelUser()
					};
					confUserLogCountList.add(userCount);
				}
			}
		}
		return confUserLogCountList;
	}
	
	@Override
	public Map<Integer, ConfLog> getConfLogDataMap(List<ConfBase> confs,
			Integer userId) {
		Map<Integer, ConfLog> data = new HashMap<Integer, ConfLog>();
		if(confs!=null && confs.size()>0){
			for (Iterator<ConfBase> itr = confs.iterator(); itr.hasNext();) {
				ConfBase conf = itr.next();
				data.put(conf.getId(), getConfLogByConf(conf,userId));
			}
		}
		return data;
	}
	
	public Map<Integer, Integer> getConflogNumByConf(List<ConfBase> confs){
		
		Map<Integer, Integer> data = new HashMap<Integer, Integer>();
		if(confs!=null && confs.size()>0){
			for (Iterator<ConfBase> itr = confs.iterator(); itr.hasNext();) {
				ConfBase conf = itr.next();
				data.put(conf.getId(), getConfLogNumByConf(conf.getId()));
			}
		}
		
		return data;
	}
	
	
	
	public int getConfLogNumByConf(Integer confId){
		String sql = "select count(*) from t_conf_log where conf_id = ? ";
		int count = 0;
		try {
			count = libernate.countEntityListWithSql(sql, new Object[]{confId});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	
	public ConfLog getConfLogByConf(ConfBase conf,Integer userId){
		ConfLog cl = null;
		String sql = "select * from t_conf_log where conf_id = ? and user_id = ? order by exit_time desc ";
		List<Object> values = new ArrayList<Object>();
		values.add(conf.getId());
		values.add(userId);
		try{
			cl = libernate.getEntityCustomized(ConfLog.class, sql, values.toArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return cl;
	}

	@Override
	public PageBean<ConfLog> getLogsByConf(Integer confId, int pageSize, Integer pageNo,String sortField,String sortRule) {
		
		PageBean<ConfLog> page = null;
		String sql = "select * from t_conf_log where conf_id = ? order by ";
		List<Object> values = new ArrayList<Object>();
		values.add(confId);
		String field = SortConstant.CONFLOG_FIELDS.get(sortField);
		String rule = " desc ";
		if(field==null || field.equals("")){
			field = " exit_time ";
		}
		if(sortRule!=null && sortRule.equals("1")){
			rule = " asc ";
		}
		sql = sql+field+rule;
		try{
			page =  getPageBeans(ConfLog.class, sql, pageNo, pageSize, values.toArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return page;
	}

	@Override
	public List<ConfLog> getAllLogsByConf(Integer confId,String sortField,String sortRule) {
		
		String sql = "select * from t_conf_log where conf_id = ? order by  ";
		List<Object> values = new ArrayList<Object>();
		values.add(confId);
		
		String field = SortConstant.CONFLOG_FIELDS.get(sortField);
		String rule = " desc ";
		if(field==null || field.equals("")){
			field = " exit_time ";
		}
		if(sortRule!=null && sortRule.equals("1")){
			rule = " asc ";
		}
		
		sql = sql+field+rule;
		List<ConfLog> logs = null;
		try{
			 logs = libernate.getEntityListBase(ConfLog.class, sql, values.toArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return logs;
	}
	
	
	@Override
	public int countConfLogsByConfs(List<ConfBase> confs) {
		StringBuilder sqlbuiler = new StringBuilder("select count(*) from t_conf_log where conf_id in (0 ");
		if(confs!=null && !confs.isEmpty()){
			for (Iterator it = confs.iterator(); it.hasNext();) {
				ConfBase confBase = (ConfBase) it.next();
				sqlbuiler.append(",");
				sqlbuiler.append(confBase.getId());
			}
		}
		sqlbuiler.append(")");
		try{
			return libernate.countEntityListWithSql(sqlbuiler.toString(),new Object[]{});
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
