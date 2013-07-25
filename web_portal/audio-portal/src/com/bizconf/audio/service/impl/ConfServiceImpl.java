package com.bizconf.audio.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.component.SecureGenerator;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConfStatus;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.constant.SortConstant;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfBaseSimple;
import com.bizconf.audio.entity.ConfCycle;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.ConfUserCount;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.exception.ConfSyncNumException;
import com.bizconf.audio.logic.ConfLogic;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.service.ConfLogService;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapUserStatus;
import com.bizconf.audio.util.BeanUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.StringUtil;

import edu.emory.mathcs.backport.java.util.Collections;

@Service
public class ConfServiceImpl extends BaseService implements ConfService {
	
	@Autowired
	private ConfLogic confLogic;
	@Autowired
	private ConfManagementService confManagementService;
	@Autowired
	private EmailService emailService;
	
	@Autowired
	ConfUserService confUserService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SiteService siteService;
	
	@Autowired
	ConfLogService confLogService;
	
	@Autowired
	EmpowerConfigService empowerConfigService;
	
	@Autowired
	ConfUserLogic confUserLogic;
	
	/**
	 * 统计站点用户查询与自己相关正在进行中会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public Integer countDuringConfList(String titleOrHostName, UserBase currentUser){
		Integer rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		if(currentUser != null){
			StringBuffer sqlStr = new StringBuffer(); 
//			sqlStr.append(" SELECT count(1) FROM ( ");
//		    sqlStr.append(" SELECT confb.* FROM  t_conf_base confb,t_conf_user tcu ");		
//		    sqlStr.append(" WHERE confb.id=tcu.conf_id AND tcu.user_id=? AND confb.cycle_id = ? AND confb.conf_status= ? ");
//		    valueList.add(currentUser.getId());
//		    valueList.add(0);
//		    valueList.add(ConfConstant.CONF_STATUS_OPENING);
//		    sqlStr.append(" UNION ALL SELECT oconfb.* FROM t_conf_base oconfb, ( ");
//		    sqlStr.append(" SELECT MIN(confb.start_time) AS start_time,confb.cycle_id FROM t_conf_base confb,t_conf_user tcu ");
//		    sqlStr.append(" WHERE confb.cycle_id=tcu.cycle_id AND tcu.user_id=?   AND confb.cycle_id > ? AND confb.conf_status = ? ");
//		    valueList.add(currentUser.getId());
//		    valueList.add(0);
//		    valueList.add(ConfConstant.CONF_STATUS_OPENING);
//		    sqlStr.append(" GROUP BY confb.cycle_id ) tmpcc ");
//		    sqlStr.append(" WHERE oconfb.start_time=tmpcc.start_time AND oconfb.cycle_id=tmpcc.cycle_id ) tcb ");
//		    sqlStr.append(" WHERE tcb.start_time > ? ");
//		    Date now_time = DateUtil.getGmtDate(null);
//			valueList.add(now_time);
//			if(StringUtil.isNotBlank(titleOrHostName)){
//				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
//				valueList.add("%"+ titleOrHostName +"%");
//				valueList.add("%"+ titleOrHostName +"%");
//			}
			
			sqlStr.append(" SELECT count(1) FROM t_conf_base  tcb WHERE tcb.id IN( ");
			sqlStr.append(" SELECT tmp.conf_id FROM ( ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,tcu.`start_time` FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` =? AND (tcu.`user_id`=? OR tcu.`user_id`=? ) AND site_id=? AND tcu.`conf_status`=? AND del_flag = ?");
			sqlStr.append(" UNION ALL ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,MIN(tcu.`start_time`) AS start_time FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` >? AND (tcu.`user_id`=? OR tcu.`user_id`=?) AND site_id=? AND tcu.`conf_status`=? AND del_flag = ?");
			sqlStr.append(" GROUP BY tcu.`cycle_id`");
			sqlStr.append(" ) tmp ");
			sqlStr.append(" ) ");
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_OPENING);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_OPENING);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			sqlStr.append(" AND tcb.start_time < ? ");
		    Date now_time = DateUtil.getGmtDate(null);
			valueList.add(now_time);
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			Object[] values = valueList.toArray();
			try {
				rows = libernate.countEntityListWithSql(sqlStr.toString(), values);
			} catch (SQLException e) {
				logger.error("统计站点用户查询与自己相关正在进行中会议的条数出错！" + e);
			}
		}
		return rows;
	}
	
	/**
	 * 站点用户查询与自己相关正在进行中会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public List<ConfBase> listDuringConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		if(currentUser != null){
			List<Object> valueList = new ArrayList<Object>();
			StringBuffer sqlStr = new StringBuffer(); 
//			sqlStr.append(" SELECT * FROM ( ");
//		    sqlStr.append(" SELECT confb.* FROM  t_conf_base confb,t_conf_user tcu ");		
//		    sqlStr.append(" WHERE confb.id=tcu.conf_id AND tcu.user_id=? AND confb.cycle_id = ? AND confb.conf_status= ? ");
//		    valueList.add(currentUser.getId());
//		    valueList.add(0);
//		    valueList.add(ConfConstant.CONF_STATUS_OPENING);
//		    sqlStr.append(" UNION ALL SELECT oconfb.* FROM t_conf_base oconfb, ( ");
//		    sqlStr.append(" SELECT MIN(confb.start_time) AS start_time,confb.cycle_id FROM t_conf_base confb,t_conf_user tcu ");
//		    sqlStr.append(" WHERE confb.cycle_id=tcu.cycle_id AND tcu.user_id=?   AND confb.cycle_id > ? AND confb.conf_status = ? ");
//		    valueList.add(currentUser.getId());
//		    valueList.add(0);
//		    valueList.add(ConfConstant.CONF_STATUS_OPENING);
//		    sqlStr.append(" GROUP BY confb.cycle_id ) tmpcc ");
//		    sqlStr.append(" WHERE oconfb.start_time=tmpcc.start_time AND oconfb.cycle_id=tmpcc.cycle_id ) tcb ");
//		    sqlStr.append(" WHERE tcb.start_time > ? ");
//		    Date now_time = DateUtil.getGmtDate(null);
//			valueList.add(now_time);
//			if(StringUtil.isNotBlank(titleOrHostName)){
//				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
//				valueList.add("%"+ titleOrHostName +"%");
//				valueList.add("%"+ titleOrHostName +"%");
//			}
			
			sqlStr.append(" SELECT * FROM t_conf_base  tcb WHERE tcb.id IN( ");
			sqlStr.append(" SELECT tmp.conf_id FROM ( ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,tcu.`start_time` FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` =? AND (tcu.`user_id`=? OR tcu.`user_id`=? ) AND site_id=? AND tcu.`conf_status`=?  AND del_flag = ?");
			sqlStr.append(" UNION ALL ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,MIN(tcu.`start_time`) AS start_time FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` >? AND (tcu.`user_id`=? OR tcu.`user_id`=?) AND site_id=? AND tcu.`conf_status`=?  AND del_flag = ?");
			sqlStr.append(" GROUP BY tcu.`cycle_id`");
			sqlStr.append(" ) tmp ");
			sqlStr.append(" ) ");
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_OPENING);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_OPENING);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			sqlStr.append(" AND tcb.start_time < ? ");
		    Date now_time = DateUtil.getGmtDate(null);
			valueList.add(now_time);
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			
			if(StringUtil.isNotBlank(sortField)){
				String sortFieldValue = initSort(sortField);     //获取页面传递的排序参数
				String sortordValue = "desc";
				if(SortConstant.SORT_ASC.equals(sortord)){
					sortordValue = "asc";
				}
				if(StringUtil.isNotBlank(sortFieldValue)){
					sqlStr.append(" order by ").append("tcb.").append(sortFieldValue).append(sortordValue);
				}
			}else{
				sqlStr.append(" order by tcb.start_time DESC ");   //查出列表无排序条件则为默认逆序
			}
			if(pageModel != null){
				int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
				sqlStr.append(" limit ? , ?  ");
				valueList.add(recordNo);
				valueList.add(pageModel.getPageSize());
			}
			Object[] values = valueList.toArray();
			try {
				confList = libernate.getEntityListBase(ConfBase.class, sqlStr.toString(), values);
				if(confList != null && confList.size() > 0){
					confList = getOffsetConfList(currentUser, confList);
				}
			} catch (SQLException e) {
				logger.error("站点用户查询与自己相关正在进行中会议出错！" + e);
			}
		}
		return confList;
	}
	
	/**
	 * 统计站点用户查询与自己相关即将开始会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public Integer countUpcomingConfList(String titleOrHostName, UserBase currentUser, Integer days){
		Integer rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		if(currentUser != null){
			StringBuffer sqlStr = new StringBuffer(); 
			sqlStr.append(" SELECT count(1) FROM t_conf_base  tcb WHERE tcb.id IN( ");
			sqlStr.append(" SELECT tmp.conf_id FROM ( ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,tcu.`start_time` FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` =? AND (tcu.`user_id`=? OR tcu.`user_id`=? ) AND site_id=? AND tcu.`conf_status`=?  AND del_flag = ?");
			sqlStr.append(" UNION ALL ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,MIN(tcu.`start_time`) AS start_time FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` >? AND (tcu.`user_id`=? OR tcu.`user_id`=?) AND site_id=? AND tcu.`conf_status`=?  AND del_flag = ?");
			sqlStr.append(" GROUP BY tcu.`cycle_id`");
			sqlStr.append(" ) tmp ");
			sqlStr.append(" ) ");
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		    Date now_time = DateUtil.getGmtDate(null);
//		    sqlStr.append(" AND tcb.start_time > ? ");
//			valueList.add(now_time);
			if(days != null && days.intValue() != 0){
				sqlStr.append(" AND tcb.start_time < ? ");
				Date end_time = DateUtil.addDate(now_time, days.intValue());
				valueList.add(end_time);
			}
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			Object[] values = valueList.toArray();
			try {
				rows = libernate.countEntityListWithSql(sqlStr.toString(), values);
			} catch (SQLException e) {
				logger.error("统计站点用户查询与自己相关即将开始会议的条数出错！" + e);
			}
		}
		return rows;
	}
	
	/**
	 * 站点用户查询与自己相关即将开始会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public List<ConfBase> listUpcomingConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite, Integer days){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		if(currentUser != null){
			List<Object> valueList = new ArrayList<Object>();
			StringBuffer sqlStr = new StringBuffer(); 
			sqlStr.append(" SELECT * FROM t_conf_base  tcb WHERE tcb.id IN( ");
			sqlStr.append(" SELECT tmp.conf_id FROM ( ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,tcu.`start_time` FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` =? AND (tcu.`user_id`=? OR tcu.`user_id`=? ) AND site_id=? AND tcu.`conf_status`=?  AND del_flag = ?");
			sqlStr.append(" UNION ALL ");
			sqlStr.append(" SELECT tcu.`conf_id`,tcu.`cycle_id`,MIN(tcu.`start_time`) AS start_time FROM t_conf_user  tcu ");
			sqlStr.append(" WHERE tcu.`cycle_id` >? AND (tcu.`user_id`=? OR tcu.`user_id`=?) AND site_id=? AND tcu.`conf_status`=?  AND del_flag = ?");
			sqlStr.append(" GROUP BY tcu.`cycle_id`");
			sqlStr.append(" ) tmp ");
			sqlStr.append(" ) ");
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			valueList.add(0);
			valueList.add(currentUser.getId());
			valueList.add(-1);
			valueList.add(currentUser.getSiteId().intValue());
			valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		    Date now_time = DateUtil.getGmtDate(null);
//		    sqlStr.append(" AND tcb.start_time > ? ");
//			valueList.add(now_time);
			if(days != null && days.intValue() != 0){
			//	sqlStr.append(" AND tcb.start_time > ? ");
				sqlStr.append(" AND tcb.start_time < ? ");
				Date end_time = DateUtil.addDate(now_time, days.intValue());
			//	valueList.add(DateUtil.getGmtDate(null));
				valueList.add(end_time);
			}
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			if(StringUtil.isNotBlank(sortField)){
				String sortFieldValue = initSort(sortField);     //获取页面传递的排序参数
				String sortordValue = "desc";
				if(SortConstant.SORT_ASC.equals(sortord)){
					sortordValue = "asc";
				}
				if(StringUtil.isNotBlank(sortFieldValue)){
					sqlStr.append(" order by ").append("tcb.").append(sortFieldValue).append(sortordValue);
				}
			}else{
				sqlStr.append(" order by tcb.start_time ASC ");   //查出列表无排序条件则为默认逆序
			}
			if(pageModel != null){
				int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
				sqlStr.append(" limit ? , ?  ");
				valueList.add(recordNo);
				valueList.add(pageModel.getPageSize());
			}
			System.out.println("login upcoming conf sql--->>"+sqlStr.toString());
			Object[] values = valueList.toArray();
			try {
				confList = libernate.getEntityListBase(ConfBase.class, sqlStr.toString(), values);
				if(confList != null && confList.size() > 0){
					confList = getOffsetConfList(currentUser, confList);
				}
			} catch (SQLException e) {
				logger.error("站点用户查询与自己相关即将开始会议出错！" + e);
			}
		}
		return confList;
	}
	
	/**
	 * 统计站点用户查询与自己相关错过的会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public Integer countMissConfList(String titleOrHostName, UserBase currentUser, Integer days, Integer hideFlag){
		Integer rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		if(currentUser != null){
			StringBuffer sqlStr = new StringBuffer(); 
			sqlStr.append(" SELECT count(1) FROM t_conf_base  tcb ,t_conf_user tcup WHERE 1=1 ");
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			if(days != null && days.intValue() != 0){
				sqlStr.append(" AND tcb.start_time > ? ");
				Date start_time = DateUtil.addDate(DateUtil.getGmtDate(null), -days.intValue());
				valueList.add(start_time);
			}
			Integer userId = currentUser.getId();
			if(userId != null && userId.intValue() > 0){
				sqlStr.append(" AND tcb.conf_status= ? AND tcb.del_flag = ? AND tcb.id=tcup.conf_id AND tcup.user_id=? ");
				valueList.add(ConfConstant.CONF_STATUS_FINISHED);
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);
				valueList.add(userId);
				if(hideFlag != null){
					sqlStr.append(" AND tcup.hide_flag = ?");
					valueList.add(hideFlag);
				}
				sqlStr.append(" AND NOT EXISTS( SELECT DISTINCT tcu.conf_id FROM   t_conf_user tcu, t_conf_log tcl ");
				sqlStr.append(" WHERE 1=1 And tcu.user_id=? ");
				sqlStr.append(" AND tcu.conf_id=tcl.conf_id AND tcl.user_id=tcu.user_id AND tcb.id=tcl.conf_id ) ");
				valueList.add(userId);
			}
			Object[] values = valueList.toArray();
			try {
				rows = libernate.countEntityListWithSql(sqlStr.toString(), values);
			} catch (SQLException e) {
				logger.error("统计站点用户查询与自己相关错过会议的条数出错！" + e);
			}
		}
		return rows;
	}
	
	/**
	 * 站点用户查询与自己相关错过的会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public List<ConfBase> listMissConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite, Integer days, Integer hideFlag){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		List<Object> valueList = new ArrayList<Object>();
		if(currentUser != null){
			StringBuffer sqlStr = new StringBuffer(); 
			sqlStr.append(" SELECT tcb.* FROM t_conf_base  tcb ,t_conf_user tcup WHERE 1=1 ");
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			if(days != null && days.intValue() != 0){
				sqlStr.append(" AND tcb.start_time > ? ");
				Date start_time = DateUtil.addDate(DateUtil.getGmtDate(null), -days.intValue());
				valueList.add(start_time);
			}
			Integer userId = currentUser.getId();
			if(userId != null && userId.intValue() > 0){
				sqlStr.append(" AND tcb.conf_status= ? AND tcb.del_flag = ? AND tcb.id=tcup.conf_id AND tcup.user_id=? ");
				valueList.add(ConfConstant.CONF_STATUS_FINISHED);
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);
				valueList.add(userId);
				if(hideFlag != null){
					sqlStr.append(" AND tcup.hide_flag = ?");
					valueList.add(hideFlag);
				}
				sqlStr.append(" AND NOT EXISTS( SELECT DISTINCT tcu.conf_id FROM   t_conf_user tcu, t_conf_log tcl ");
				sqlStr.append(" WHERE 1=1 And tcu.user_id=? ");
				sqlStr.append(" AND tcu.conf_id=tcl.conf_id AND tcl.user_id=tcu.user_id AND tcb.id=tcl.conf_id ) ");
				valueList.add(userId);
			}
			if(StringUtil.isNotBlank(sortField)){
				String sortFieldValue = initSort(sortField);     //获取页面传递的排序参数
				String sortordValue = "desc";
				if(SortConstant.SORT_ASC.equals(sortord)){
					sortordValue = "asc";
				}
				if(StringUtil.isNotBlank(sortFieldValue)){
					sqlStr.append(" order by ").append("tcb.").append(sortFieldValue).append(sortordValue);
				}
			}else{
				sqlStr.append(" order by tcb.start_time DESC ");   //查出列表无排序条件则为默认逆序
			}
			if(pageModel != null){
				int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
				sqlStr.append(" limit ? , ?  ");
				valueList.add(recordNo);
				valueList.add(pageModel.getPageSize());
			}
			Object[] values = valueList.toArray();
			try {
				confList = libernate.getEntityListBase(ConfBase.class, sqlStr.toString(), values);
				if(confList != null && confList.size() > 0){
					confList = getOffsetConfList(currentUser, confList);
				}
			} catch (SQLException e) {
				logger.error("站点用户查询与自己相关错过会议出错！" + e);
			}
		}
		return confList;
	}
	
	
	/**
	 * 统计站点用户查询与自己相关已经加入过会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public Integer countAttendedConfList(String titleOrHostName, UserBase currentUser, Integer days){
		Integer rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		if(currentUser != null){
			StringBuffer sqlStr = new StringBuffer(); 
			sqlStr.append(" SELECT count(1) FROM t_conf_base  tcb ,t_conf_user tcup WHERE 1=1 ");
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			if(days != null && days.intValue() != 0){
				sqlStr.append(" AND tcb.start_time > ? ");
				Date start_time = DateUtil.addDate(DateUtil.getGmtDate(null), -days.intValue());
				valueList.add(start_time);
			}
			Integer userId = currentUser.getId();
			if(userId != null && userId.intValue() > 0){
				sqlStr.append(" AND tcb.conf_status= ? AND tcb.del_flag = ? AND tcb.id=tcup.conf_id AND tcup.user_id=? ");
				valueList.add(ConfConstant.CONF_STATUS_FINISHED);
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);
				valueList.add(userId);
				sqlStr.append(" AND EXISTS( SELECT DISTINCT tcu.conf_id FROM   t_conf_user tcu, t_conf_log tcl ");
				sqlStr.append(" WHERE 1=1 And tcu.user_id=? ");
				sqlStr.append(" AND tcu.conf_id=tcl.conf_id AND tcl.user_id=tcu.user_id AND tcb.id=tcl.conf_id ) ");
				valueList.add(userId);
			}
			Object[] values = valueList.toArray();
			try {
				rows = libernate.countEntityListWithSql(sqlStr.toString(), values);
			} catch (SQLException e) {
				logger.error("统计站点用户查询与自己相关已经加入过会议的条数出错！" + e);
			}
		}
		return rows;
	}
	
	/**
	 * 站点用户查询与自己相关已经加入过会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public List<ConfBase> listAttendedConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite, Integer days){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		List<Object> valueList = new ArrayList<Object>();
		if(currentUser != null){
			StringBuffer sqlStr = new StringBuffer(); 
			sqlStr.append(" SELECT tcb.* FROM t_conf_base  tcb ,t_conf_user tcup WHERE 1=1 ");
			if(StringUtil.isNotBlank(titleOrHostName)){
				sqlStr.append(" AND (tcb.conf_name like ? or tcb.compere_name like ? )");
				valueList.add("%"+ titleOrHostName +"%");
				valueList.add("%"+ titleOrHostName +"%");
			}
			if(days != null && days.intValue() != 0){
				sqlStr.append(" AND tcb.start_time > ? ");
				Date start_time = DateUtil.addDate(DateUtil.getGmtDate(null), -days.intValue());
				valueList.add(start_time);
			}
			Integer userId = currentUser.getId();
			if(userId != null && userId.intValue() > 0){
				sqlStr.append(" AND tcb.conf_status= ? AND tcb.del_flag = ? AND tcb.id=tcup.conf_id AND tcup.user_id=? ");
				valueList.add(ConfConstant.CONF_STATUS_FINISHED);
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);
				valueList.add(userId);
				sqlStr.append(" AND EXISTS( SELECT DISTINCT tcu.conf_id FROM   t_conf_user tcu, t_conf_log tcl ");
				sqlStr.append(" WHERE 1=1 And tcu.user_id=? ");
				sqlStr.append(" AND tcu.conf_id=tcl.conf_id AND tcl.user_id=tcu.user_id AND tcb.id=tcl.conf_id ) ");
				valueList.add(userId);
			}
			if(StringUtil.isNotBlank(sortField)){
				String sortFieldValue = initSort(sortField);     //获取页面传递的排序参数
				String sortordValue = "desc";
				if(SortConstant.SORT_ASC.equals(sortord)){
					sortordValue = "asc";
				}
				if(StringUtil.isNotBlank(sortFieldValue)){
					sqlStr.append(" order by ").append("tcb.").append(sortFieldValue).append(sortordValue);
				}
			}else{
				sqlStr.append(" order by tcb.start_time DESC ");   //查出列表无排序条件则为默认逆序
			}
			if(pageModel != null){
				int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
				sqlStr.append(" limit ? , ?  ");
				valueList.add(recordNo);
				valueList.add(pageModel.getPageSize());
			}
			Object[] values = valueList.toArray();
			try {
				confList = libernate.getEntityListBase(ConfBase.class, sqlStr.toString(), values);
				if(confList != null && confList.size() > 0){
					confList = getOffsetConfList(currentUser, confList);
				}
			} catch (SQLException e) {
				logger.error("站点用户查询与自己相关已经加入过会议出错！" + e);
			}
		}
		return confList;
	}
	
	/**
	 * 操作数据库，新建会议信息
	 * wangyong
	 * 2013-3-5
	 */
	private ConfBase saveConf(ConfBase confBase){
		ConfBase conf = new ConfBase();
		try {
			conf = libernate.saveEntity(confBase);
		} catch (Exception e) {
			logger.error("保存会议信息出错！",e);
		}
		return conf;
	}
	
	/**
	 * 创建一个即时会议
	 * 即时会议只有会议名称，其他字段均为默认配置
	 * @param conf 会议基本信息
	 * @param siteBase 站点基本信息
	 * @param user 创建会议用户基本信息
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public ConfBase createImmediatelyConf(ConfBase conf,SiteBase siteBase, UserBase user){
		ConfBase confBase = new ConfBase();
		if(conf != null){
			//先检测参会人数是否大于站点当前所剩参会人数值
//			boolean licenceFlag = confLogic.createConfLicenseVali(conf, null, siteBase, user);
//			if(!licenceFlag){
//				confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//				return confBase;
//			}
			//再检测在正则表达式验证页面输入数据
			boolean flag = confLogic.saveConfValidate(conf, null, siteBase);
			if(flag){
				conf = initConf(conf, siteBase, user, null);
				confLogic.immediatelyConfAuthority(conf, user);     //2013.7.4 创建即时会议的权限控制
				if(!checkSiteMaxConfCount(conf, siteBase)){
					confBase.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
					logger.info("超过最大并发会议数，创建会议失败");
					return confBase;
				}
				
				String retInfo = confManagementService.createConf(conf, siteBase, user,true);
				Integer confId = null;
				if(retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
					conf.setSoapStatus(ConfConstant.CONF_SOAP_STATUS_TRUE);
					//即时会议，直接状态为进行状态，开始时间为当前时间
					conf.setStartTime(DateUtil.getGmtDate(null));
					conf.setConfStatus(ConfConstant.CONF_STATUS_OPENING);
					
					confBase = saveConf(conf);
					confId = confBase.getId();
				}else if(ConstantUtil.AS_FAILED_LICENSE_SET.contains(retInfo)){
					confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
					logger.info("参会人数大于站点当前所剩参会人数值");
					return confBase;
				}else{
					logger.info("华为返回错误码：" + retInfo);
					return confBase;
				}
				
				if(confBase != null && confId != null && confId.intValue() > 0){
//					boolean createCompere = saveCompere(confId, 0, user);
					boolean createCompere = confUserService.fillConfUserForCreate(confBase, user);
					if(createCompere){
						logger.info("创建会议后保存主持人信息成功");
					}else{
						logger.info("创建会议后保存主持人信息失败");
					}
					//发送周期会议信息到主持人邮箱
					boolean sendEmail = emailService.createConfEmail(confBase, null, user);
					if(sendEmail){
						logger.info("发送会议信息到主持人邮箱成功");
					}else{
						logger.info("发送会议信息到主持人邮箱失败");
					}
				}
			}
		}
		return confBase;
	}
	
	/**
	 * 创建一个单次预约会议
	 * @param conf 会议基本信息
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public ConfBase createSingleReservationConf(ConfBase conf, SiteBase siteBase, UserBase user){
		ConfBase confBase = new ConfBase();
		if(conf != null){
//			//先检测参会人数是否大于站点当前所剩参会人数值
//			boolean licenceFlag = confLogic.createConfLicenseVali(conf, null, siteBase, user);
//			if(!licenceFlag){
//				confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//				return confBase;
//			}
			//再检测在正则表达式验证页面输入数据
			boolean dataFlag = confLogic.saveConfValidate(conf, null, siteBase);
			if(dataFlag){
				conf = initConf(conf, siteBase, user, null);
				if(!checkSiteMaxConfCount(conf, siteBase)){
					conf.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
					logger.info("超过最大并发会议数，创建会议失败");
					return conf;
				}
				String  retInfo = confManagementService.createConf(conf, siteBase, user);
				if(ConstantUtil.AS_FAILED_LICENSE_SET.contains(retInfo)){
					confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
					logger.info("参会人数大于站点当前所剩参会人数值");
					return confBase;
				}else if(!retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
					//该错误不可以返回到页面，不可让客户知道该类错误
					logger.info("华为返回错误码：" + retInfo);
					return confBase;
				}
				confBase = saveConf(conf);
				Integer confId = confBase.getId();
				if(confBase != null && confId != null && confId.intValue() > 0){
					boolean createCompere = confUserService.fillConfUserForCreate(confBase, user);
					if(createCompere){
						logger.info("创建会议后保存主持人信息成功");
					}else{
						logger.info("创建会议后保存主持人信息失败");
					}
					//发送单次预约会议信息到主持人邮箱
					boolean sendEmail = emailService.createConfEmail(confBase, null, user);
					if(sendEmail){
						logger.info("发送会议信息到主持人邮箱成功");
					}else{
						logger.info("发送会议信息到主持人邮箱失败");
					}
					confBase = getOffsetConf(user, confBase);
				}
			}else{
				//针对不法方式绕过前台用户，该错误不可以返回到页面，不可让客户知道该类错误，只记录错误到logger文件
				logger.debug("正则表达式验证页面输入数据");
				return confBase;
			}
		}
		return confBase;
	}
	
	/**
	 * 重新创建一个单次预约会议
	 * 1.只能创建单次预约会议
	 * 2.保留非周期会议信息
	 * 3.copy参会人，主持人与新会议的关联
	 * 4.不是修改，是新建
	 * 5.重新给参会人发送邮件
	 * @param conf 会议基本信息
	 * @param siteBase 站点基本信息
	 * @param user 创建会议用户基本信息
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public ConfBase reCreateconf(ConfBase conf, SiteBase siteBase, UserBase user){
		ConfBase confBase = new ConfBase();
		if(conf != null){
//			//先检测参会人数是否大于站点当前所剩参会人数值
//			boolean licenceFlag = confLogic.createConfLicenseVali(conf, null, siteBase, user);
//			if(!licenceFlag){
//				confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//				return confBase;
//			}
			//再检测在正则表达式验证页面输入数据
			boolean dataFlag = confLogic.saveConfValidate(conf, null, siteBase);
			if(dataFlag){
				conf = initConf(conf, siteBase, user, null);
				String retInfo = confManagementService.createConf(conf, siteBase, user);
				if(retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE) || retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE_1)){
					confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
					logger.info("参会人数大于站点当前所剩参会人数值");
					return confBase;
				}else if(!retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
					//该错误不可以返回到页面，不可让客户知道该类错误
					logger.info("华为返回错误码：" + retInfo);
					return confBase;
				}
				confBase = saveConf(conf);
				Integer confId = confBase.getId();
				if(confBase != null && confId != null && confId.intValue() > 0){
					confUserService.fillConfUserForCreate(confBase, user);			   //创建会议完成后调用保存主持人信息
					boolean createCompere = copyConfUser(confBase, conf.getId());      //拷贝一份原会议的参会人用户到重新创建后的参会用户表
					if(createCompere){
						logger.info("重新创建会议后拷贝一份参会人用户到参会用户表成功");
					}else{
						logger.info("重新创建会议后拷贝一份参会人用户到参会用户表失败");
					}
					//发送单次预约会议信息到主持人邮箱
					boolean sendEmail = emailService.createConfEmail(confBase, null, user);
					if(sendEmail){
						logger.info("发送会议信息到主持人邮箱成功");
					}else{
						logger.info("发送会议信息到主持人邮箱失败");
					}
					List<ConfUser> confUsers = confUserService.getConfInviteUserList(confId);
					boolean sendConfUsers = emailService.sendInviteToConfUser(confUsers, confBase);
					if(sendConfUsers){
						logger.info("邀请联系人参会成功");
					}else{
						logger.info("邀请联系人参会失败");
					}
					confBase = getOffsetConf(user, confBase);
				}
			}else{
				//针对不法方式绕过前台用户，该错误不可以返回到页面，不可让客户知道该类错误，只记录错误到logger文件
				logger.debug("正则表达式验证页面输入数据");
				return confBase;
			}
		}
		return confBase;
	}
	
	/**
	 * 修改单次预约会议
	 * @param conf 会议基本信息
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public ConfBase updateSingleReservationConf(ConfBase conf, SiteBase siteBase, UserBase user){
		ConfBase confBase = new ConfBase();
		Integer timeZone = conf.getTimeZone();
		if (timeZone == null) {
			timeZone = 0;
		}
		//先检测参会人数是否大于站点当前所剩参会人数值
//		boolean licenceFlag = confLogic.updateConfLicenseVali(conf, null, siteBase, user);
//		if(!licenceFlag){
//			confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//			return confBase;
//		}
		//2013.7.17 修复bug ，修改会议需检查权限
		checkFuncEmpower(conf, user);
		//2013.7.17
		conf.setStartTime(DateUtil.getGmtDateByTimeZone(conf.getStartTime(),timeZone));
		conf.setEndTime(DateUtil.addDateMinutes(conf.getStartTime(), conf.getDuration()));
		String[] passArray = new String[]{
				"id", "confName", "confDesc","hostKey","confType", "startTime", "duration", "endTime", "maxAudio", "maxVideo", "videoType", "aheadTime", "funcBits", "clientConfig", "publicFlag", "publicConfPass", "privi_bits"
		};
		if(conf != null && conf.getId() > 0){
			//再检测在正则表达式验证页面输入数据
			boolean flag = confLogic.saveConfValidate(conf, null, siteBase);
			//2013.7.23  超过最大并发会议数
			if(!checkSiteMaxConfCount(conf, siteBase)){
				conf.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
				logger.info("超过最大并发会议数，创建会议失败");
				return conf;
			}
			//2013.7.23
			if(flag){
				try {
					String retInfo = confManagementService.updateConf(conf, siteBase, user);
					if(retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
						confBase = libernate.updateEntity(conf, passArray);
						confBase = libernate.getEntity(ConfBase.class, confBase.getId());
						if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
							boolean createCompere = confUserService.fillConfUserForModify(confBase, user);
							if(createCompere){
								logger.info("修改会议后保存主持人信息成功");
							}else{
								logger.info("修改会议后保存主持人信息失败");
							}
							List<ConfUser> confUsers = confUserService.getAllConfUserList(confBase.getId().intValue());
							boolean sendConfUsers = emailService.confModifyEmail(confUsers, confBase);
							if(sendConfUsers){
								logger.info("修改会议后通知联系人成功");
							}else{
								logger.info("修改会议后通知邀请联系人失败");
							}
						}
					}else if(retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE) || retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE_1)){
						confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
						logger.info("参会人数大于站点当前所剩参会人数值");
						return confBase;
					}else{
						//该错误不可以返回到页面，不可让客户知道该类错误
						logger.info("华为返回错误码：" + retInfo);
						return confBase;
					}
				} catch (Exception e) {
					logger.error("修改单次预约会议出错！",e);
				}
			}
		}
		return confBase;
	}
	
	/**
	 * 主持人删除(取消)周期预约会议
	 * @param cycleId 周期会议id
	 * @param currentSite 当前站点
	 * @param currentUser 当前登录用户
	 */
	@Override
	public boolean cancleCycleConfInfo(Integer cycleId, SiteBase currentSite, UserBase currentUser){
		boolean cancleHwFlag = true;
		boolean cancleFlag = false;
		List<Object> valueList = new ArrayList<Object>();
		List<ConfBase> confs = getConfWithHwIdByCycleId(cycleId);
		try {
			for (Iterator it = confs.iterator(); it.hasNext();) {
				ConfBase conf = (ConfBase) it.next();
				if(conf != null && conf.getId() != null && conf.getId().intValue() > 0){
					if(StringUtil.isNotBlank(conf.getConfHwid())){
						cancleHwFlag = confManagementService.cancelConf(conf.getConfHwid(), currentSite, currentUser);
					}
					if(!cancleHwFlag){
						logger.info("主持人删除(取消)周期预约会议:华为接口取消一个预约会议失败！");
						return false;
					}
				}
			}
		} catch (Exception e) {
			logger.error("主持人删除(取消)周期预约会议:取消一个预约会议出错！",e);
		}
		try {
			StringBuilder sqlBuilder = new StringBuilder("UPDATE t_conf_base SET ");
//			sqlBuilder.append(" conf_status=?, del_flag=?, del_time=?, del_user=? ");
			sqlBuilder.append(" conf_status=?,  del_time=?, del_user=? ");
			valueList.add(ConfConstant.CONF_STATUS_CANCELED);
//			valueList.add(ConstantUtil.DELFLAG_DELETED);
			valueList.add(DateUtil.getGmtDate(null));
			valueList.add(currentUser.getId());
			sqlBuilder.append(" WHERE cycle_id = ? and del_flag = ? and conf_status=? ");
			valueList.add(cycleId);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
			Object[] values = valueList.toArray();
			cancleFlag = libernate.executeSql(sqlBuilder.toString(), values);
			if(cancleFlag){
				boolean createCompere = confUserService.fillConfUserForCancelCycle(cycleId, currentUser);
				if(createCompere){
					logger.info("取消会议后保存主持人信息成功");
				}else{
					logger.info("取消会议后保存主持人信息失败");
				}
				
			}
		} catch (Exception e) {
			logger.error("主持人删除(取消)周期预约会议出错！",e);
		}
		if(cancleHwFlag && cancleFlag){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 主持人删除(取消)周期预约会议中的一条会议
	 * @param confId 会议id
	 * @param currentSite 当前站点
	 * @param currentUser 当前登录用户
	 */
	@Override
	public boolean cancleSingleCycleConfInfo(Integer confId, SiteBase currentSite, UserBase currentUser){
		boolean cancleHwFlag = true;
		boolean cancleFlag = false;
		ConfBase confBase = null;
		ConfBase conf = getConfBasebyConfId(confId);
		try {
			if(conf != null && conf.getId() != null && conf.getId().intValue() > 0){
				
				conf.setConfStatus(ConfConstant.CONF_STATUS_CANCELED);
//				conf.setDelFlag(ConstantUtil.DELFLAG_DELETED);
				conf.setDelTime(DateUtil.getGmtDate(null));
				conf.setDelUser(currentUser.getId());
				
				if(StringUtil.isNotBlank(conf.getConfHwid())){
					cancleHwFlag = confManagementService.cancelConf(conf.getConfHwid(), currentSite, currentUser);
				}
				if(!cancleHwFlag){
					logger.info("华为接口取消一个预约会议失败！");
					return false;
				}
//				confBase = libernate.updateEntity(conf, "id", "confStatus", "delFlag", "delTime", "delUser");
				confBase = libernate.updateEntity(conf, "id", "confStatus", "delTime", "delUser");
				
				List<ConfUser> confUsers = confUserLogic.getConfUserList(conf.getId());
				emailService.confCancelEmail(confUsers, conf);
			}
		} catch (Exception e) {
			logger.error("取消一个预约会议出错！",e);
		}
		if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
			cancleFlag = true;
			boolean createCompere = confUserService.fillConfUserForCancel(confBase.getId(), currentUser);
			if(createCompere){
				logger.info("取消会议后保存主持人信息成功");
			}else{
				logger.info("取消会议后保存主持人信息失败");
			}
		}
		if(cancleHwFlag && cancleFlag){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 取消(删除)一个预约会议
	 * 更新会议状态为取消，同时更新删除字段为已删除
	 * @param conf 会议基本信息
	 * @param user 当前登录用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public boolean cancleSingleReservationConf(Integer confId, SiteBase currentSite, UserBase currentUser){
		boolean cancleHwFlag = true;
		boolean cancleFlag = false;
		ConfBase confBase = null;
		ConfBase conf = getConfBasebyConfId(confId);
//		ConfBase childConf=null;
		boolean isPerConf=false;
		if(ConfConstant.CONF_PERMANENT_ENABLED_MAIN.equals(conf.getPermanentConf())){
			isPerConf=true;
//			childConf=getPermanentChildConf(conf.getId());
		}
		
		try {
			if(conf != null && conf.getId() != null && conf.getId().intValue() > 0){
				conf.setConfStatus(ConfConstant.CONF_STATUS_CANCELED);
//				conf.setDelFlag(ConstantUtil.DELFLAG_DELETED);
				conf.setDelTime(DateUtil.getGmtDate(null));
				conf.setDelUser(currentUser.getId());
				if(StringUtil.isNotBlank(conf.getConfHwid()) && !isPerConf){
					cancleHwFlag = confManagementService.cancelConf(conf.getConfHwid(), currentSite, currentUser);
				}
				if(!cancleHwFlag  && !isPerConf){
					logger.info("华为接口取消一个预约会议失败！");
					return false;
				}
//				if(isPerConf){
//					deleteChildConfByBelongConfId(conf.getId());
//				}
//				confBase = libernate.updateEntity(conf, "id", "confStatus", "delFlag", "delTime", "delUser");
				confBase = libernate.updateEntity(conf, "id", "confStatus", "delTime", "delUser");
				List<ConfUser> confUsers = confUserLogic.getConfUserList(conf.getId());
				emailService.confCancelEmail(confUsers, conf);
			}
		} catch (Exception e) {
			logger.error("取消一个预约会议出错！",e);
		}
		if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
			cancleFlag = true;
			boolean createCompere = confUserService.fillConfUserForCancel(confBase.getId(), currentUser);
			if(createCompere){
				logger.info("取消会议后保存主持人信息成功");
			}else{
				logger.info("取消会议后保存主持人信息失败");
			}
		}
		if(cancleHwFlag && cancleFlag){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 隐藏一个错过的会议
	 * @param confId 会议id
	 * @param currentUser 当前登录用户
	 * wangyong
	 * 2013-4-12
	 */
	@Override
	public boolean hideMissConf(Integer confId, UserBase currentUser){
		try {
			logger.info("隐藏一个错过的会议: confId:" + confId +", currentUser:" + currentUser);
			return libernate.executeSql("update t_conf_user set hide_flag = ? where conf_id = ? and user_id = ?", 
					new Object[]{ConfConstant.CONF_HIDE_FLAG_TRUE, confId, currentUser.getId().intValue()});
		} catch (Exception e) {
			logger.error("隐藏一个错过的会议出错！",e);
			return false;
		}
	}
	
	
	
	
	private ConfCycle createCycleConf(ConfCycle confCycle, SiteBase currentSite, UserBase currentUser, Integer timeZone){
		confCycle.setBeginDate(DateUtil.getGmtDateByTimeZone(confCycle.getBeginDate(), timeZone));
		confCycle.setCreateUser(currentUser.getId());
		confCycle.setEndDate(DateUtil.getGmtDateByTimeZone(confCycle.getEndDate(), timeZone));
		confCycle.setSiteId(currentSite.getId());
		try {
			confCycle = libernate.saveEntity(confCycle);
		} catch (Exception e) {
			logger.error("保存 会议信息--循环会议设置出错！" + confCycle , e);
		}
		return confCycle;
	}
	
	private ConfBase createCycleReservationConf(ConfBase conf, ConfCycle confCycleBase, SiteBase currentSite, UserBase currentUser, List<Date> confDateList){
		ConfBase confBase = new ConfBase();
		if(confDateList == null || confDateList.size() <= 0){
			logger.info("周期会议获取周期时间范围出错！" + confDateList);
			return confBase;
		}
		if(conf == null){
			logger.info("周期会议内容为空！");
			return confBase;
		}
		Integer cycleId = confCycleBase.getId();
		conf = initConf(conf, currentSite, currentUser, confCycleBase);
		conf.setCycleId(cycleId);
		conf.setSoapStatus(ConfConstant.CONF_SOAP_STATUS_TRUE);
		StringBuilder sqlBuilder = new StringBuilder("INSERT INTO t_conf_base ");
		sqlBuilder.append(" ( conf_hwid, site_id, cycle_id, conf_name, conf_desc, conf_type, start_time, duration, end_time,");
		sqlBuilder.append("  compere_user, compere_name, compere_secure, user_secure, call_phone, phone_pass, max_user, ");
		sqlBuilder.append("  max_audio, max_video, video_type, max_dpi, default_dpi, ahead_time, open_ipad, client_config, ");
		sqlBuilder.append("  conf_status, soap_status, conf_version, create_time, create_user, create_type, del_flag, ");
		sqlBuilder.append("  del_time, del_user, del_type, public_flag, func_bits, public_conf_pass, privi_bits, time_zone_id, time_zone, crypt_key , host_key) ");
		sqlBuilder.append(" VALUES ");
		for(int i=0;i<confDateList.size();i++){
			if(i>0){
				sqlBuilder.append(",");
			}
			sqlBuilder.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? )");
		}
		List<Object> valueList = null;
		try{
			valueList = getConfValues(conf, confDateList, currentSite, currentUser);
		}catch (ConfSyncNumException e){
			confBase.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
			logger.info("超过最大并发会议数" + confBase + e);
			return confBase;
		}catch (Exception e){
			confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
			logger.info("参会人数大于站点当前所剩参会人数值" + confBase + e);
			return confBase;
		}
		if(valueList == null || valueList.size() == 0){
			logger.info("创建周期会议获取创建周期会议属性值错误！");
			return confBase;
		}
		try {
			if(libernate.executeSql(sqlBuilder.toString(), valueList.toArray())){
				confBase = getConfBasebyCycleId(cycleId);
				//创建周期会议完成后调用保存主持人信息
				if(!confUserService.fillConfUserForCreateCycle(getCycleConfDate(cycleId, currentSite), currentUser)){
					logger.info("创建会议后保存主持人信息失败");
				}
				//发送周期会议信息到主持人邮箱,会议的时间应为gmt时间，发送邮件接口会转换gmt到站点时区时间
				if(!emailService.createConfEmail(confBase, confCycleBase, currentUser)){
					logger.info("发送周期会议信息到主持人邮箱失败");
				}
				getOffsetConf(currentUser, confBase);     //返回到controller的会议时间从gmt转换为偏好设置时区的时间
			}else{
				logger.info("创建周期预约会议失败！" + conf);
			}
		} catch (Exception e) {
			logger.error("创建周期预约会议失败！" + conf ,e);
		}	
		return confBase;
	}
	
	/**
	 * 补充创建无限期周期会议的下一条会议
	 * 1.创建无限期周期会议时只创建了30条会议，每当结束一条会议时，则调用该方法再补充创建一条会议，保证该无限期周期会议一直有30条
	 * @param conf 当前结束的会议
	 * wangyong
	 * 2013-5-9
	 */
	private ConfBase addInfiniteConf(ConfBase conf, ConfCycle confCycle, SiteBase currentSite, UserBase currentUser){
		if(conf == null){
			return null;
		}
		ConfBase firstCycleConf = new ConfBase();
		if(confCycle != null && currentUser != null && currentSite != null){
			Integer timeZone = conf.getTimeZone();
			DateUtil.getOffsetDateByGmtDate(confCycle.getBeginDate(), timeZone.longValue());
			Date localBeginDate = DateUtil.getOffsetDateByGmtDate(conf.getStartTime(), timeZone.longValue());    //转换会议开始时间为会议所属时区时间
			Calendar cal = Calendar.getInstance();
			Calendar calCycle = Calendar.getInstance();
			cal.setTime(localBeginDate);
			calCycle.setTime(DateUtil.getOffsetDateByGmtDate(confCycle.getBeginDate(), timeZone.longValue()));
			cal.set(Calendar.HOUR_OF_DAY, calCycle.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, calCycle.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, calCycle.get(Calendar.SECOND));
			localBeginDate = cal.getTime();
			Date localEndDate = DateUtil.getOffsetDateByGmtDate(confCycle.getEndDate(), timeZone.longValue());
			//从当前结束会议的开始时间的下一天起，获得30天循环周期日期
			List<Date> confDateList = DateUtil.getCycleDateFromScope(DateUtil.addDate(localBeginDate, 1), localEndDate, confCycle.getCycleType(), confCycle.getCycleValue(), ConstantUtil.CYCLE_CONF_DATE_LIMIT);
			if(confDateList == null || confDateList.size() == 0){
				logger.info("未获得有效循环周期日期");
				return null;
			}
			if(confDateList.size() == ConstantUtil.CYCLE_CONF_DATE_LIMIT){    //当周期范围为30天时，取最后一天
				List<Date> oneDateList = new ArrayList<Date>();
				oneDateList.add(confDateList.get(confDateList.size()-1));
				List<ConfBase> confList = getCycConfBases(confCycle.getId());
				for(ConfBase confBase : confList){
					if(confBase.getId().intValue() > conf.getId().intValue()){
						conf.setDuration(confBase.getDuration());
						conf.setConfStatus(ConfConstant.CONF_STATUS_SUCCESS);
						break;
					}
				}
				firstCycleConf = createCycleReservationConf(conf, confCycle, currentSite, currentUser, oneDateList);    //保存周期会议
			}
		}
		return firstCycleConf;
	}
	
	/**
	 * 创建周期预约会议
	 * @param conf 会议基本信息
	 */
	@Override
	public ConfBase createCycleReservationConf(ConfBase conf, ConfCycle confCycle, SiteBase currentSite, UserBase currentUser){
		ConfBase firstCycleConf = new ConfBase();

		ConfCycle confCycleBase = null;
		if(confCycle != null && currentUser != null && currentSite != null){
			//先检测参会人数是否大于站点当前所剩参会人数值
//			boolean licenceFlag = confLogic.createConfLicenseVali(conf, confCycle, currentSite, currentUser);
//			if(!licenceFlag){
//				firstCycleConf.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//				return firstCycleConf;
//			}
			//正则表达式验证页面输入数据
			boolean dataFlag = confLogic.saveConfValidate(conf, confCycle, currentSite);
			if(!dataFlag){
				//针对不法方式绕过前台用户，该错误不可以返回到页面，不可让客户知道该类错误，只记录错误到logger文件
				logger.info("正则表达式验证页面输入数据");
				return firstCycleConf;
			}
			Integer timeZone = 0;
			if(currentUser.getTimeZone() != null){
				timeZone = currentUser.getTimeZone();
			}else if(currentSite.getTimeZone() != null){
				timeZone = currentSite.getTimeZone();
			}
			Date localBeginDate = confCycle.getBeginDate();    //用户所设置时区的周期开始时间
			Date localEndDate = confCycle.getEndDate();
			confCycleBase = createCycleConf(confCycle, currentSite, currentUser, timeZone);      //保存周期会议属性到数据库
			if(confCycleBase != null && confCycleBase.getId() != null && confCycleBase.getId().intValue() > 0){
				int cycleLimit = confCycleBase.getRepeatCount().intValue() < 1000 && confCycleBase.getRepeatCount().intValue() != 0 ? confCycleBase.getRepeatCount().intValue() : ConstantUtil.CYCLE_CONF_DATE_LIMIT;
				List<Date> confDateList = DateUtil.getCycleDateFromScope(localBeginDate, localEndDate, confCycleBase.getCycleType(), confCycleBase.getCycleValue(), cycleLimit);
				if(confDateList == null || confDateList.size() == 0){
					logger.info("未获得有效循环周期日期");
					return firstCycleConf;
				}
				//若周期时间早于当前时间，则移除该时间
				for(int i=0;i<confDateList.size();i++){
					if(!DateUtil.getGmtDateByTimeZone(confDateList.get(i), timeZone).after(DateUtil.getGmtDate(null))){
						confDateList.remove(i);
					}
				}
				firstCycleConf = createCycleReservationConf(conf, confCycleBase, currentSite, currentUser, confDateList);    //保存周期会议
			}else{
				logger.info("创建周期预约会议出错！" + confCycle);
			}
		}
		return firstCycleConf;
	}
	
	/**
	 * 获取创建周期会议属性值
	 * wangyong
	 * 2013-3-7
	 */
	private List<Object> getConfValues(ConfBase conf, List<Date> dateList, SiteBase currentSite, UserBase currentUser) throws Exception{
		List<Object> valueList = new ArrayList<Object>();
		if(conf != null && dateList != null && currentSite != null){
			Integer timeZone = currentUser.getTimeZone();
			if (timeZone == null) {
				timeZone = 0;
			}
			int dateSize = dateList.size();
			for(int i=0; i<dateSize; i++){
				conf.setStartTime(DateUtil.getGmtDateByTimeZone(dateList.get(i), timeZone));
				conf.setEndTime(DateUtil.addDateMinutes(conf.getStartTime(), conf.getDuration()));
				//2013.7.23新增验证超过最大并发会议数，创建会议失败
				if(!checkSiteMaxConfCount(conf, currentSite)){
					logger.info("超过最大并发会议数，创建会议失败");
					throw new ConfSyncNumException("超过最大并发会议数，创建会议失败");
				}
				//2013.7.23
				
				String retInfo = confManagementService.createConf(conf, currentSite, currentUser);
				if(ConstantUtil.AS_FAILED_LICENSE_SET.contains(retInfo)){
					logger.info("License不足");
					throw new RuntimeException("License不足");
				}else if(!retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
					//该错误不可以返回到页面，不可让客户知道该类错误
					logger.info("华为返回错误码：" + retInfo);
					return null;
				}
				if(conf.getConfHwid() != null){
					valueList.add(conf.getConfHwid());
				}else{
					valueList.add("");
				}
				valueList.add(conf.getSiteId());
				valueList.add(conf.getCycleId());
				valueList.add(conf.getConfName());
				valueList.add(conf.getConfDesc());
				valueList.add(conf.getConfType());
				valueList.add(conf.getStartTime());
				valueList.add(conf.getDuration());
				valueList.add(conf.getEndTime());
				valueList.add(conf.getCompereUser());
				valueList.add(conf.getCompereName());
				valueList.add(conf.getCompereSecure());
				valueList.add(conf.getUserSecure());
				valueList.add(conf.getCallPhone());
				valueList.add(conf.getPhonePass());
				valueList.add(conf.getMaxUser());
				valueList.add(conf.getMaxAudio());
				valueList.add(conf.getMaxVideo());
				valueList.add(conf.getVideoType());
				valueList.add(conf.getMaxDpi());
				valueList.add(conf.getDefaultDpi());
				valueList.add(conf.getAheadTime());
				valueList.add(conf.getOpenIpad());
				valueList.add(conf.getClientConfig());
				valueList.add(conf.getConfStatus());
				valueList.add(ConfConstant.CONF_SOAP_STATUS_TRUE.intValue());  //SOAP默认值为2是已创建，但是周期会议只有第一个会议才是已创建，剩下的均为未创建
				valueList.add(conf.getConfVersion());
				valueList.add(conf.getCreateTime());
				valueList.add(conf.getCreateUser());
				valueList.add(conf.getCreateType());
				valueList.add(conf.getDelFlag());
				valueList.add(conf.getDelTime());
				valueList.add(conf.getDelUser());
				valueList.add(conf.getDelType());
				valueList.add(conf.getPublicFlag());
				valueList.add(conf.getFuncBits());
				valueList.add(conf.getPublicConfPass());
				valueList.add(conf.getPriviBits());
				valueList.add(conf.getTimeZoneId());
				valueList.add(conf.getTimeZone());
				valueList.add(conf.getCryptKey());
				valueList.add(conf.getHostKey());
			}
		}else{
			logger.debug("获取创建周期会议属性值出错！");
			return valueList;
		}
		return valueList;
	}

	/**
	 * 修改周期预约会议中的一条会议
	 * 流程：
	 * 1.将该次周期会议改为单次预约会议，即cycleId改为0
	 * 2.copy一份参会用户（包括主持人）到参会用户表中，并且conf_id设置为本次会议的confId，cycle_id设置为0
	 * @param conf 会议基本信息
	 * @param currentSite 当前站点
	 * @param currentUser 当前用户
	 */
	@Override
	public ConfBase updateSingleCycleConfInfo(ConfBase conf,SiteBase currentSite, UserBase currentUser){
		ConfBase confBase = new ConfBase();
		//ConfBase hwConf = null;
		Integer timeZone = conf.getTimeZone();
		if (timeZone == null) {
			timeZone = 0;
		}
		//先检测参会人数是否大于站点当前所剩参会人数值
//		boolean licenceFlag = confLogic.updateConfLicenseVali(conf, null, currentSite, currentUser);
//		if(!licenceFlag){
//			confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//			return confBase;
//		}
		//2013.7.17 修复bug ，修改会议需检查权限
		checkFuncEmpower(conf, currentUser);
		//2013.7.17
		conf.setStartTime(DateUtil.getGmtDateByTimeZone(conf.getStartTime(),timeZone));
		conf.setEndTime(DateUtil.addDateMinutes(conf.getStartTime(), conf.getDuration()));
		String[] passArray = new String[]{
				"id", "cycleId", "confName","hostKey","confDesc", "confType", "startTime", "duration", "endTime", "maxAudio", "maxVideo", "videoType", "aheadTime", "funcBits", "clientConfig", "publicFlag", "publicConfPass", "privi_bits"
		};
		if(conf != null && conf.getId() > 0){
			
			//再检测在正则表达式验证页面输入数据
			boolean flag = confLogic.saveConfValidate(conf, null, currentSite);
			//2013.7.23  超过最大并发会议数
			if(!checkSiteMaxConfCount(conf, currentSite)){
				conf.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
				logger.info("超过最大并发会议数，创建会议失败");
				return conf;
			}
			//2013.7.23
			if(flag){
				conf.setCycleId(0);
				try {
					String retInfo = confManagementService.updateConf(conf, currentSite, currentUser);
					if(retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE) || retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE_1)){
						confBase.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
						logger.info("参会人数大于站点当前所剩参会人数值");
						return confBase;
					}else if(!retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
						//该错误不可以返回到页面，不可让客户知道该类错误
						logger.info("华为返回错误码：" + retInfo);
						return confBase;
					}
					confBase = libernate.updateEntity(conf, passArray);
					if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
						boolean createCompere = confUserService.fillConfUserForModify(confBase, currentUser);
						if(createCompere){
							logger.info("修改会议后保存主持人信息成功");
						}else{
							logger.info("修改会议后保存主持人信息失败");
						}
						List<ConfUser> confUsers = confUserService.getAllConfUserList(confBase.getId().intValue());
						boolean sendConfUsers = emailService.confModifyEmail(confUsers, confBase);
						if(sendConfUsers){
							logger.info("修改会议后通知联系人成功");
						}else{
							logger.info("修改会议后通知联系人失败");
						}
					}
				} catch (Exception e) {
					logger.error("修改单次预约会议出错！",e);
				}
			}
		}
		return confBase;
	}
	
	/**
	 * 修改周期预约会议
	 * 注意：不可修改循环设置部分
	 * @param conf 会议基本信息
	 * @param currentSite 当前站点
	 * @param currentUser 当前用户
	 * wangyong
	 * 2013.3.5
	 */
	@Override
	public ConfBase updateCycleConfInfo(ConfBase conf, SiteBase siteBase, UserBase user){
		boolean updateFlag = false;
		ConfBase returnConf = new ConfBase();
		List<ConfBase> confList = getCycleConfDate(conf.getCycleId(), siteBase);
		if(confList != null && confList.size() > 0){
//			ConfCycle confCycle = confLogic.getConfCycleByConf(confList.get(0));
			//先检测参会人数是否大于站点当前所剩参会人数值
//			boolean licenceFlag = confLogic.createConfLicenseVali(conf, confCycle, siteBase, user);
//			if(!licenceFlag){
//				returnConf.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//				return returnConf;
//			}
			//2013.7.17 修复bug ，修改会议需检查权限
			checkFuncEmpower(conf, user);
			//2013.7.17
			//检测正则表达式验证页面输入数据
			boolean flag = confLogic.saveConfValidate(conf, null, siteBase);
			if(!flag){
				logger.info("正则表达式验证页面输入数据出错！");
				return returnConf;
			}
			Integer timeZone=conf.getTimeZone();
			if (timeZone == null) {
				timeZone = 0;
			}
			conf.setStartTime(DateUtil.getGmtDateByTimeZone(conf.getStartTime(),timeZone));
			Calendar calendarConf = Calendar.getInstance();
			Calendar calendarConfBase = Calendar.getInstance();
			calendarConf.setTime(conf.getStartTime());
			for(ConfBase base:confList){
				if(conf.getId().intValue() == base.getId().intValue()){
					calendarConfBase.setTime(DateUtil.getGmtDateByTimeZone(base.getStartTime(),timeZone)); 
					break;
				}
			}
			long confTime = calendarConf.getTimeInMillis();
			long confBaseTime = calendarConfBase.getTimeInMillis();
			long missMinutes =  (confTime - confBaseTime)/60000;   //相差多少分钟
			for(ConfBase confBase:confList){
				confBase.setStartTime(DateUtil.addDateMinutes(DateUtil.getGmtDateByTimeZone(confBase.getStartTime(),timeZone), (int)missMinutes));
				confBase.setEndTime(DateUtil.addDateMinutes(confBase.getStartTime(), conf.getDuration()));
				String updateFileds = "host_key=?, conf_name=?, conf_desc=?, conf_type=?, start_time=?, duration=?, end_time=?, max_audio=?, max_video=?, video_type=?, ahead_time=?, func_bits=?, client_config=?, privi_bits=? ";
				if(conf != null && conf.getId() > 0){
					//2013.7.23  超过最大并发会议数
					if(!checkSiteMaxConfCount(confBase, siteBase)){
						confBase.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
						logger.info("超过最大并发会议数，创建会议失败");
						return confBase;
					}
					//2013.7.23
					String retInfo = confManagementService.updateConf(confBase, siteBase, user);
					if(retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
						try {
							StringBuilder sqlBuilder = new StringBuilder("UPDATE t_conf_base SET ");
							sqlBuilder.append(updateFileds);
							List<Object> valueList = new ArrayList<Object>();
							valueList.add(conf.getHostKey());
							valueList.add(conf.getConfName());
							valueList.add(conf.getConfDesc());
							valueList.add(conf.getConfType());
							valueList.add(confBase.getStartTime());
							valueList.add(conf.getDuration());
							valueList.add(confBase.getEndTime());
							valueList.add(conf.getMaxAudio());
							valueList.add(conf.getMaxVideo());
							valueList.add(conf.getVideoType());
							valueList.add(conf.getAheadTime());
							valueList.add(conf.getFuncBits());
							valueList.add(conf.getClientConfig());
							valueList.add(conf.getPriviBits());
							sqlBuilder.append(" , public_flag=?, public_conf_pass=? ");
							valueList.add(conf.getPublicFlag());
							if(StringUtil.isNotBlank(conf.getPublicConfPass())){
								valueList.add(conf.getPublicConfPass());
							}else{
								valueList.add("");
							}
							sqlBuilder.append(" WHERE del_flag = ? and id = ?");
							valueList.add(ConstantUtil.DELFLAG_UNDELETE);
							valueList.add(confBase.getId());
							updateFlag = libernate.executeSql(sqlBuilder.toString(), valueList.toArray());
						} catch (Exception e) {
							logger.error("修改周期预约会议出错！",e);
						}
					}else if(retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE) || retInfo.equals(ConstantUtil.AS_FAILED_LICENSE_CODE_1)){
						returnConf.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
						logger.info("参会人数大于站点当前所剩参会人数值");
						return returnConf;
					}else{
						//该错误不可以返回到页面，不可让客户知道该类错误
						logger.info("华为返回错误码：" + retInfo);
						return returnConf;
					}
				}
				returnConf = confBase;
			}
			if(updateFlag){
				List<ConfBase> newConfList = getCycleConfDate(conf.getCycleId(), siteBase);
				boolean createCompere = confUserService.fillConfUserForModifyCycle(newConfList, user);
				if(createCompere){
					logger.info("修改会议后保存主持人信息成功");
				}else{
					logger.info("修改会议后保存主持人信息失败");
				}
			}
		}
		return returnConf;
	}
	
	/**
	 * 主持人邀请参会人
	 * @param participantsList 参会人
	 * @param currentUser 当前登录用户
	 * wangyong
	 * 2013-3-6
	 */
	@Override
	public boolean inviteParticipants(Integer confId, Integer cycleId, List<UserBase> participantsList, UserBase currentUser){
		boolean saveFlag = false;
		ConfBase conf = getConfBasebyConfId(confId);
		if(conf != null){
			//调用发邮件接口通知参会人
			//emailService.sendConfInvite(participantsList, conf);
			//保存参会人到数据库中
			StringBuilder sqlBuilder = new StringBuilder("INSERT INTO t_conf_user ");
			sqlBuilder.append(" ( conf_id, cycle_id, user_id, user_name, user_email, telephone, host_flag, accept_flag, ");
			sqlBuilder.append(" create_time, create_user, creater_user_type, del_flag, del_time, del_user, del_user_type ) ");
			sqlBuilder.append(" VALUES ");
			int userSize = participantsList.size();
			for(int i=1;i<userSize+1;i++){
				if(i == userSize){
					sqlBuilder.append("(?,?,?,?,?,?,?,?,?,?)");
				}else{
					sqlBuilder.append("(?,?,?,?,?,?,?,?,?,?),");
				}
			}
			List<Object> valueList = getParticipantsList(participantsList, confId, cycleId, currentUser);
			try {
				Object[] values = valueList.toArray();
				saveFlag = libernate.executeSql(sqlBuilder.toString(), values);
			} catch (Exception e) {
				logger.error("主持人邀请参会人失败！",e);
				saveFlag = false;
			}
		}
		return saveFlag;
	}
	
	/**
	 * 获得联系人list
	 * wangyong
	 * 2013-3-6
	 */
	private List<Object> getParticipantsList(List<UserBase> participantsList, Integer confId, Integer cycleId, UserBase currentUser){
		List<Object> valueList = new ArrayList<Object>();
		for(UserBase user:participantsList){
			if(cycleId != null && cycleId.intValue() > 0){
				valueList.add(0);                  //若为周期会议用户，则confid为0，cycleid不为0
				valueList.add(cycleId);
			}else{
				valueList.add(confId);             //若为单次会议用户，则confid不为0，cycleid为0
				valueList.add(0);
			}
			Integer userId = user.getId();
			if(userId != null){
				valueList.add(userId);
			}else{
				valueList.add(0);
			}
			if(StringUtil.isNotBlank(user.getTrueName())){
				valueList.add(user.getTrueName());
			}else{
				valueList.add("");
			}
			if(StringUtil.isNotBlank(user.getUserEmail())){
				valueList.add(user.getUserEmail());
			}else{
				valueList.add("");
			}
			if(StringUtil.isNotBlank(user.getMobile())){
				valueList.add(user.getMobile());
			}else{
				valueList.add("");
			}
			valueList.add(ConfConstant.CONF_USER_PARTICIPANT);
			valueList.add(0);
			valueList.add(DateUtil.getGmtDate(null));
			valueList.add(currentUser.getId());
			valueList.add(currentUser.getUserType());
			valueList.add(0);
			try {
				valueList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00"));
			} catch (ParseException e) {
				logger.error("主持人邀请参会人转换删除时间出错！" + e);
			}
			valueList.add(0);
			valueList.add(0);
		}
		return valueList;
	}
	
	/**
	 * 重新创建会议后拷贝一份参会人用户到参会用户表
	 * @param conf 重新创建后的会议
	 * @param oldConfId 原会议ID
	 * wangyong
	 * 2013-3-18
	 */
	private boolean copyConfUser(ConfBase conf, Integer oldConfId){
		boolean copyFlag = false;
		List<Object> valueList = new ArrayList<Object>();
		if(conf != null && conf.getId() > 0){
			try {
				StringBuilder sqlBuilder = new StringBuilder(" INSERT INTO t_conf_user ");
				sqlBuilder.append(" (conf_id, cycle_id, conf_status, user_id, site_id, user_name, user_email, telephone, host_flag, accept_flag, ");
				sqlBuilder.append("  create_time, create_user, creater_user_type, del_flag, del_time, del_user, del_user_type ) ");
				sqlBuilder.append(" SELECT ?, cycle_id, user_id, user_name, user_email, telephone, host_flag, accept_flag, create_time, ");
				sqlBuilder.append("  create_user, creater_user_type, del_flag, del_time, del_user, del_user_type ");
				sqlBuilder.append("  FROM t_conf_user where conf_id = ? ");
				valueList.add(1);
				valueList.add(conf.getSiteId().intValue());
				valueList.add(conf.getId().intValue());
				valueList.add(oldConfId.intValue());
				Object[] values = valueList.toArray();
				copyFlag = libernate.executeSql(sqlBuilder.toString(), values);
			} catch (Exception e) {
				logger.error("拷贝一份参会人用户到参会用户表出错！",e);
			}
		}
		return copyFlag;
	}
	
	/**
	 * 保存会议时，初始化会议信息
	 * wangyong
	 * 2013-3-4
	 */
	private ConfBase initConf(ConfBase confBase, SiteBase siteBase, UserBase user, ConfCycle confCycle){
		if(confBase != null && siteBase != null && user != null){
			DefaultConfig defaultConfig = getDefaultConfig(user);
			if(defaultConfig == null || defaultConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
				defaultConfig = saveDefaultConfig(user);
			}
			confBase.setSiteId(siteBase.getId());
			confBase.setCycleId(0);   //此处全部初始化为预约会议，周期会议在创建周期会议方法中初始化完毕后设置	
			if(!StringUtil.isNotBlank(confBase.getConfDesc())){
				confBase.setConfDesc("");
			}
			Integer timeZone = 0;
			Integer timeZoneId = 0;
			if(user.getTimeZone() != null){
				timeZone = user.getTimeZone();
				timeZoneId = user.getTimeZoneId();
			}else if(siteBase.getTimeZone() != null){
				timeZone = siteBase.getTimeZone();
				timeZoneId = siteBase.getTimeZoneId();
			}
			confBase.setTimeZone(timeZone);
			confBase.setTimeZoneId(timeZoneId);
			confBase.setStartTime(DateUtil.getGmtDateByTimeZone(confBase.getStartTime(), timeZone));
			if(confBase.getDuration() != null && confBase.getDuration().intValue() > 0){
				confBase.setDuration(confBase.getDuration());//转换为分钟为单位的值
			}else {
				confBase.setDuration(ConfConstant.CONF_DEFAULT_DURATION);
			}
			
			if(confBase.getEndTime()==null){
				confBase.setEndTime(DateUtil.addDateMinutes(confBase.getStartTime(), confBase.getDuration()));
			}else{
				confBase.setEndTime(DateUtil.getGmtDateByTimeZone(confBase.getEndTime(), timeZone));
			}
			confBase.setCompereUser(user.getId());
			confBase.setCompereName(user.getTrueName());
			confBase.setMaxUser(2);
			if(confBase.getMaxAudio() != null && confBase.getMaxAudio().intValue() >= 0){
				confBase.setMaxAudio(confBase.getMaxAudio());
			}else if(defaultConfig != null){
				confBase.setMaxAudio(defaultConfig.getMaxAudio());//最大音频数需从会议参数设置获得
			}
			if(confBase.getMaxVideo() != null && confBase.getMaxVideo().intValue() >= 0){
				confBase.setMaxVideo(confBase.getMaxVideo());
			}else if(defaultConfig != null){
				confBase.setMaxVideo(defaultConfig.getMaxVideo());
			}
			if(StringUtil.isNotBlank(confBase.getVideoType())){
				confBase.setVideoType(confBase.getVideoType());
			}else if(defaultConfig != null){
				confBase.setVideoType(defaultConfig.getVideoType());
			}
			confBase.setDefaultDpi(confBase.getVideoType().substring(0, 1));
			confBase.setMaxDpi(confBase.getVideoType().substring(1, 2));
			if(confBase.getAheadTime() != null && confBase.getAheadTime().intValue() > 0){
				confBase.setAheadTime(confBase.getAheadTime());
			}else if(defaultConfig != null){
				confBase.setAheadTime(defaultConfig.getAheadTimes());
			}
			if(StringUtil.isNotBlank(confBase.getFuncBits())){
				confBase.setFuncBits(confBase.getFuncBits());
			}else if(defaultConfig != null){
				confBase.setFuncBits(defaultConfig.getFuncBits());
			}
			if(StringUtil.isNotBlank(confBase.getClientConfig())){
				//TODO 验证站点以及用户是否有功能授权
				confBase.setClientConfig(confBase.getClientConfig());
			}else if(defaultConfig != null){
				//TODO 验证站点以及用户是否有功能授权
				confBase.setClientConfig(defaultConfig.getClientConfig());
			}
			if(StringUtil.isNotBlank(confBase.getPriviBits())){
				confBase.setPriviBits(confBase.getPriviBits());
			}else if(defaultConfig != null){
				confBase.setPriviBits(defaultConfig.getPriviBits());
			}
			confBase.setCreateTime(DateUtil.getGmtDate(null));   //创建时间初始化为GMT时间
			confBase.setCreateUser(user.getId());
			confBase.setCreateType(user.getUserType());
			try {
				confBase.setDelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00"));
			} catch (ParseException e) {
				logger.error("保存会议时，初始化会议信息,转换删除时间出错！"+e);
			}
			if(confBase.getPublicFlag()!= null && confBase.getPublicFlag().intValue() == ConfConstant.CONF_PUBLIC_FLAG_TRUE){
				confBase.setPublicConfPass(confBase.getPublicConfPass());
			}else{
				confBase.setPublicConfPass("");
			}
			checkFuncEmpower(confBase, user);
		}
		return confBase;
	}
	
	/**
	 * 站点管理员根据会议主题查询会议列表
	 * @param subject 会议主题
	 * @param siteId  站点ID
	 * @param sortField  排序字段
	 * @param sortord    排序方式
	 * @param pageModel  分页信息
	 * @param siteUserId  普通站点管理员id(当超级站点管理员查询时，传入null即可)
	 * 
	 */
	@Override
	public List<ConfBase> getConfListBySubject(SiteBase currentSite, String subject, Integer siteId,
			String sortField, String sortord, PageModel pageModel, Integer siteUserId) {
		List<ConfBase> confList = null;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" SELECT a.* FROM  t_conf_base a, t_user_base b WHERE 1=1 ");
		if(subject != null){
			strSql.append(" AND ( a.conf_name LIKE ? OR a.conf_hwid = ? ) ");
			valueList.add("%"+ subject +"%");
			valueList.add(subject);
		} 
		if(siteId != null && siteId.intValue() > 0){
			strSql.append(" AND a.site_id = ? ");
			valueList.add(siteId.intValue());
		}
		strSql.append(" AND a.del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		strSql.append(" AND b.del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		strSql.append(" AND a.create_user=b.id ");
		if(siteUserId != null && siteUserId.intValue() > 0){
			strSql.append(" AND b.create_user = ? ");
			valueList.add(siteUserId.intValue());
		}
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ").append("a.").append(sortField);
		}else{ 
			strSql.append(" order by a.id DESC ");   //查出列表无排序条件则为默认逆序
		}
		if(StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortord) && "desc".equals(sortord.trim().toLowerCase())){
			strSql.append(" DESC");
		}
		if(pageModel != null){
			int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
			strSql.append(" limit ? , ?  ");
			valueList.add(recordNo);
			valueList.add(pageModel.getPageSize());
		}
		try {
			Object[] values = valueList.toArray(); 
			confList = DAOProxy.getLibernate().getEntityListBase(ConfBase.class, strSql.toString(), values);
			if(confList != null && confList.size() > 0){
				confList = getOffsetConfList(currentSite, confList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return confList;
	}
	
	/**
	 * 站点管理员根据会议主题统计会议总数
	 * @param subject 会议主题
	 * @param siteId  站点ID
	 * @param siteUserId  普通站点管理员id(当超级站点管理员查询时，传入null即可)
	 * 
	 */
	@Override
	public int countConfListBySubject(String subject, Integer siteId, Integer siteUserId){
		int rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" SELECT count(1) FROM  t_conf_base a, t_user_base b WHERE 1=1 ");
		if(subject != null){
			strSql.append(" AND ( a.conf_name LIKE ? OR a.conf_hwid = ? )");
			valueList.add("%"+ subject +"%");
			valueList.add(subject);
		} 
		if(siteId != null && siteId.intValue() > 0){
			strSql.append(" AND a.site_id = ? ");
			valueList.add(siteId.intValue());
		}
		strSql.append(" AND a.del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		strSql.append(" AND b.del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		strSql.append(" AND a.create_user=b.id ");
		if(siteUserId != null && siteUserId.intValue() > 0){
			strSql.append(" AND b.create_user = ? ");
			valueList.add(siteUserId.intValue());
		}
		Object[] values = valueList.toArray();
		try {
			rows = DAOProxy.getLibernate().countEntityListWithSql(strSql.toString(), values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
	
	/**
	 * 站点管理员根据高级搜索条件查询会议列表(权限控制条件可以放在condition中)
	 * @param condition  高级搜索条件
	 * @param sortField  排序字段
	 * @param sortord    排序方式
	 * @param pageModel  分页信息
	 * 
	 */
	@Override
	public List<ConfBase> getConfListByCondition(SiteBase currentSite, Condition condition,String sortField, String sortord, PageModel pageModel){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		List<ConfBaseSimple> confBaseSimpleList = null;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" SELECT id, conf_name, create_user, compere_user,compere_name ,conf_hwid, conf_type, start_time, end_time, ");
		strSql.append(" (CASE WHEN  conf.conf_status =6 THEN 0 WHEN conf.conf_status =5 THEN 2 WHEN conf.conf_status =7 THEN 3 ELSE conf.conf_status END) conf_status ");
		strSql.append(" from ( ");
			strSql.append("select * from (");
				strSql.append(" SELECT id, conf_name, create_user, compere_user,compere_name ,conf_hwid, conf_type, start_time, end_time,");
				strSql.append(" (CASE WHEN  tmp.conf_status =20 THEN 6 WHEN tmp.conf_status =200 THEN 5 WHEN tmp.conf_status =2000 THEN 7 ELSE tmp.conf_status END) conf_status ");
				strSql.append(" FROM ( ");
					strSql.append(" SELECT a.id, a.conf_name, a.create_user, a.compere_user,a.compere_name ,a.conf_hwid, a.conf_type, a.start_time, a.end_time,");
					strSql.append(" (CASE WHEN  a.conf_status =0 THEN 20 WHEN a.conf_status =2 THEN 200 WHEN a.conf_status =3 THEN 2000 ELSE a.conf_status END) conf_status ");
					strSql.append(" FROM  t_conf_base a, t_user_base b WHERE 1=1 ");
					if(condition != null && StringUtil.isNotBlank(condition.getConditionSql())){
						strSql.append(" and ").append(condition.getConditionSql());
					} 
					strSql.append(" AND a.del_flag = ? ");
					valueList.add(ConstantUtil.DELFLAG_UNDELETE);
					strSql.append(" AND b.del_flag = ? ");
					valueList.add(ConstantUtil.DELFLAG_UNDELETE);
					strSql.append(" AND a.create_user=b.id ");
					strSql.append(" AND a.site_id = b.site_id ");
					strSql.append(" and a.site_id = ? ");
					valueList.add(currentSite.getId());
				strSql.append(" ) tmp ");
			strSql.append(" ) confBase ");
			strSql.append("ORDER BY confBase.conf_status ASC, confBase.start_time DESC, confBase.id ASC ");
		strSql.append(" ) conf ");
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ").append("conf.").append(sortField);
		}
		if(StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortord) && "desc".equals(sortord.trim().toLowerCase())){
			strSql.append(" DESC");
		}
		if(pageModel != null){
			int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
			strSql.append(" limit ? , ?  ");
			valueList.add(recordNo);
			valueList.add(pageModel.getPageSize());
		}
		
		
		try {
			confBaseSimpleList = libernate.getEntityListBase(ConfBaseSimple.class,
					strSql.toString(), valueList.toArray());
		} catch (SQLException e) {
			logger.error("站点管理员根据高级搜索条件查询会议列表(权限控制条件可以放在condition中)出错！" + e);
		}
		if(confBaseSimpleList != null && confBaseSimpleList.size() > 0){
			String[] filedArray = ObjectUtil.getFieldFromObject(confBaseSimpleList.get(0));
			String copyField = "";
			for(String filed : filedArray){
				if(!"confStatus".equals(filed)){
					copyField = copyField + "," + filed;
				}
			}
			logger.info("copyField:"+copyField);
			for(ConfBaseSimple conf : confBaseSimpleList){
				ConfBase confBase = new ConfBase();
				try {
					confBase = (ConfBase) ObjectUtil.copyObject(conf, confBase, copyField);
					confBase.setConfStatus(Integer.valueOf(conf.getConfStatus().toString()));
				} catch (Exception e) {
					logger.error("转换conf出错！" + e);
				}
				confList.add(confBase);
			}
			confList = getOffsetConfList(currentSite, confList);
		}
//		try{
//			Object[] values = valueList.toArray();
//			confList = DAOProxy.getLibernate().getEntityListBase(ConfBase.class, strSql.toString(), values);
//			if(confList != null && confList.size() > 0){
//				confList = getOffsetConfList(currentSite, confList);
//			}
//		}catch (Exception e){
//			e.printStackTrace(); 
//		}
		return confList;
	}
	
	/**
	 * 站点管理员根据高级搜索条件统计会议总数(权限控制条件可以放在condition中)
	 * @param condition  高级搜索条件
	 * 
	 */
	@Override
	public int countConfListByCondition(Condition condition){
		int rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" SELECT count(1) FROM  t_conf_base a, t_user_base b WHERE 1=1 ");
		if(condition != null && StringUtil.isNotBlank(condition.getConditionSql())){
			strSql.append(" and ").append(condition.getConditionSql());
		} 
		strSql.append(" AND a.del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		strSql.append(" AND b.del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		strSql.append(" AND a.create_user=b.id ");
		Object[] values = valueList.toArray();
		try {
			rows = DAOProxy.getLibernate().countEntityListWithSql(strSql.toString(), values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfBase> getListForStatusMonitor(long period) {
//		Date endDate = DateUtil.getGmtDateByAfterMs(period);
		Date currentDate = DateUtil.getGmtDate(null);
		List<ConfBase> confList = null;
		try {
			confList = DAOProxy.getLibernate().getEntityListWithCondition(
					"(conf_status = ?  or (start_time <= ? and conf_status = ?)) and del_flag = ? ",
					new Object[] { ConfStatus.LIVING.getStatus(), currentDate,
							ConfStatus.SCHEDULED.getStatus(),ConstantUtil.DELFLAG_UNDELETE}, ConfBase.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return confList == null ? Collections.emptyList() : confList;
	}

	@Override
	public boolean syncConfStatus(ConfBase conf) {
		logger.info("will sync conf status!");
		if (conf == null)
			throw new NullPointerException("conf is not allowed be null");
//		int status = 99; create_conf.ftl

		try {
			long time1=System.currentTimeMillis();
			SiteBase siteBase=siteService.getSiteBaseById(conf.getSiteId());
			UserBase userBase=userService.getUserBaseById(conf.getCreateUser());
			
			if(StringUtil.isEmpty(conf.getConfHwid())){//永久会议的HWID号为空
				if (conf.isBelongPermanentConf()) {
					 if (conf.getStartTime().before(DateUtil.getGmtDate(null)) 
						&& ConfConstant.CONF_STATUS_SUCCESS.equals(conf.getConfStatus())){
						 updateConfStatus(conf, ConfConstant.CONF_STATUS_OPENING);
					 }
					 else if (conf.getEndTime().before(DateUtil.getGmtDate(null)) && 
							 ConfConstant.CONF_STATUS_OPENING.equals(conf.getConfStatus())){
						 //TODO:
						 updateConfStatus(conf, ConfConstant.CONF_STATUS_FINISHED);
					 }
				}
				
				return false;
			}
			
			ConfBase confFromAs=confManagementService.queryConfInfo(conf.getConfHwid(), siteBase, userBase);
			if(confFromAs!=null){
				System.out.println("syncConfStatus confId:"+confFromAs.getConfHwid()+","+confFromAs.getId()+",status:" + confFromAs.getConfStatus());
				
				ESpaceMeetingAsSoapUserStatus[] soapUserArray=null;
				List<ConfLog> logList=null;
				
				if(!conf.getConfStatus().equals(confFromAs.getConfStatus())){
					//更新会议状态
					updateConfStatus(conf, confFromAs.getConfStatus());
					
					//会议结束 时更新会议状态 
					if(ConfConstant.CONF_STATUS_FINISHED.equals(confFromAs.getConfStatus())){
						logger.info("AS返回的会议相关信息:"+confFromAs);
						soapUserArray=confManagementService.queryConfUserStatus(confFromAs.getConfHwid(), 1,10000, siteBase, userBase) ;
//						System.out.println("soapUserArray .length :" + soapUserArray.length);
						if(soapUserArray != null && soapUserArray.length > 0){
							logList=getLogListFromSoap(conf.getId(),conf.getSiteId(),soapUserArray);
						}
						if(logList!=null && logList.size() > 0){
							boolean saveStatus=confLogService.batchSaveConfLog(logList);
						}
//						System.out.println("conf endtime :" + conf.getEndTime()+"  confFromAs  endtime="+confFromAs.getEndTime());
//						System.out.println("conf finished :" + conf.getConfHwid()+"  查的会议与会者列表");
						ConfCycle confCycle =  null;
						if(conf != null && conf.getCycleId().intValue() > 0){
							confCycle = getConfCyclebyConfId(conf.getCycleId());
						}
						if(confCycle != null && confCycle.getInfiniteFlag().intValue() == ConfConstant.CONF_CYCLE_INFINITE_TRUE){
							ConfBase addConf = addInfiniteConf(conf, confCycle, siteBase, userBase);
							if(addConf != null && addConf.getId() != null && addConf.getId().intValue() > 0){
								logger.info("补充创建无限期周期会议的下一条会议成功");
							}else{
								logger.error("补充创建无限期周期会议的下一条会议失败！" + conf);
							}
						}
						updateEndTime(conf, DateUtil.getGmtDate(confFromAs.getEndTime()));//DateUtil.getGmtDate(null));
						
					}
					
					if(conf.getBelongConfId()!=null && conf.getBelongConfId().intValue()>0){
						if(!confManagementService.setingOnlineUserNum(conf)){
							confManagementService.queryConfUserTerminals(conf);
						}
						logger.info("the conf:"+conf.getConfHwid()+" online number :"+conf.getPcNum());
					}
				}
				//更新在线人数
				if(ConfConstant.CONF_STATUS_OPENING.equals(confFromAs.getConfStatus())){
					if(!confManagementService.setingOnlineUserNum(conf)){
						
						confManagementService.queryConfUserTerminals(conf);
					}
					logger.info("the conf:"+conf.getConfHwid()+" online number :"+conf.getPcNum());
				}
			}
			else{
				System.out.println("confFromAs is null for:" + conf.getConfHwid());
			}
			long time2=System.currentTimeMillis();
			System.out.println("syncConfStatus use time :" + (time2-time1)+" ms");
				
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean updateConfStatus(ConfBase conf, int status) {
		conf.setConfStatus(status);
		try {
			DAOProxy.getLibernate().updateEntity(conf, "conf_status");
			confUserService.fillConfUserForConfStatusUpdate(conf.getId(), status);
		} catch (Exception e) {
			logger.error("update conf status error", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean updateConfCryptKey(ConfBase conf) {
		try {
			if(conf.getCryptKey()!=null && !conf.getCryptKey().equals("")){
				DAOProxy.getLibernate().updateEntity(conf, "id","crypt_key");
			}else{
				throw new RuntimeException("get crypt_key filed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("update crypt_key error", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 测试用
	 * shhc
	 * 2013-3-20
	 */
	public ConfBase createConf(ConfBase conf) {

		// check conf basic info here, including name,desc,start_time,duration
		// and etc.
		if (conf == null) {
			throw new IllegalArgumentException("conf can not be null.");
		}

		// check license
		// TODO:

		// call AS create conf first
		// TODO:

		try {
			conf = DAOProxy.getLibernate().saveEntity(conf);
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}

		return conf;
	}

	@Override
	public Integer countConfListByBaseCondition(String titleOrSiteSign, Integer sysUserId) {
		List<Object> valueList = new ArrayList<Object>();
		Integer confCount = 0;
		StringBuilder strSql = new StringBuilder();
		strSql.append(" SELECT count(1) ");
		strSql.append(" FROM ( ");
		strSql.append("     SELECT conf.id,conf.conf_hwid,conf.site_id,conf.cycle_id,conf.start_time,conf.end_time,conf.conf_type,conf.conf_name, ");
		strSql.append("     (CASE WHEN  conf.conf_status =0 THEN 20 WHEN conf.conf_status =2 THEN 10 WHEN conf.conf_status =3 THEN 30 ELSE conf.conf_status END) conf_status ");
		strSql.append("     FROM t_conf_base conf WHERE conf.id IN( ");
		strSql.append(" 	SELECT tmp.id FROM ( ");
		strSql.append(" 	SELECT tcb.`id`,tcb.start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` = ? AND del_flag = ? AND tcb.`conf_status` < ? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_CANCELED);
		strSql.append(" 	UNION ALL ");
		strSql.append(" 	SELECT tcb.`id`,MIN(tcb.`start_time`) AS start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND (tcb.`conf_status`=? OR tcb.`conf_status`=?) GROUP BY tcb.cycle_id ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
		valueList.add(ConfConstant.CONF_STATUS_OPENING);
		strSql.append(" 	UNION ALL     ");
		strSql.append(" 	SELECT tcb.`id`, start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND tcb.`conf_status`=? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_FINISHED);
		strSql.append("        ) tmp ");
		strSql.append("     )");
//		if(StringUtil.isNotBlank(titleOrSiteSign)){
//			strSql.append(" AND conf.conf_name LIKE ? ");
//			valueList.add("%" + titleOrSiteSign + "%");
//		}
		strSql.append("     ORDER BY conf.conf_status ASC,conf.start_time DESC,conf.id ASC ");
		strSql.append(" )confbase ,  t_site_base sitebase ");
		strSql.append(" WHERE confbase.site_id = sitebase.id AND sitebase.`del_flag` = ? AND (confbase.conf_name LIKE ? OR sitebase.`site_name` LIKE ? OR sitebase.site_sign LIKE ? )  ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add("%" + titleOrSiteSign + "%");
		valueList.add("%" + titleOrSiteSign + "%");
		valueList.add("%" + titleOrSiteSign + "%");
		if(sysUserId != null && sysUserId.intValue() > 0){
			strSql.append(" AND sitebase.create_user = ?");
			valueList.add(sysUserId.intValue());
		}
//		StringBuffer sqlBuffer = new StringBuffer();
//		Object[] values = null;
//		
//		//先查一下站点,获取站点信息
//		
//		sqlBuffer.append("select * from t_site_base where del_flag= ? and id >0 ");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		
//		if (titleOrSiteSign != null && !"".equals(titleOrSiteSign) && !"null".equals(titleOrSiteSign.trim().toLowerCase())) {
//			sqlBuffer.append(" and (");
//			sqlBuffer.append(" site_name  like ? ");
//			sqlBuffer.append(" or site_sign like ? ");
////			sqlBuffer.append(" or en_name like ? ");
//			sqlBuffer.append(" )");
//			valueList.add("%" + titleOrSiteSign + "%");
//			valueList.add("%" + titleOrSiteSign + "%");
////			valueList.add("%" + titleOrSiteSign + "%");
//		}
//		if(sysUserId != null && sysUserId.intValue() > 0){
//			sqlBuffer.append(" AND create_user = ?");
//			valueList.add(sysUserId.intValue());
//		}
//		values=valueList.toArray();
//		try {
//			siteList = libernate.getEntityListBase(SiteBase.class,
//					sqlBuffer.toString(), values);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//		valueList = new ArrayList<Object>();
//		sqlBuffer = new StringBuffer();
//		if(sysUserId != null && sysUserId.intValue() > 0){
//			sqlBuffer.append("SELECT count(1) FROM t_conf_base tc ,(SELECT id FROM t_site_base  ts WHERE  ts.`create_user`=? and ts.del_flag = ?) tmpts where tc.`site_id` =tmpts.id ");
//			valueList.add(sysUserId.intValue());
//			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		}else{
//			sqlBuffer.append("select count(1) from t_conf_base tc where 1=1 ");
//		}
//		sqlBuffer.append(" and tc.del_flag= ? and tc.id >0 ");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
////		sqlBuffer.append("select count(id) as confCount from t_conf_base where del_flag= ? and id >0 ");
////		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		if (titleOrSiteSign != null && !"".equals(titleOrSiteSign)
//				&& !"null".equals(titleOrSiteSign.trim().toLowerCase())) {
//			sqlBuffer.append(" and tc.conf_name like ? ");
//			valueList.add("%" + titleOrSiteSign + "%");
//		}
//		//如果站点列表中有值，则取站点的ID号对应关系
//		if (siteList != null && siteList.size() > 0) {
//			int ii = 0;
//			if(sysUserId != null && sysUserId.intValue() > 0){
//				sqlBuffer.append(" and (");
//			}else{
//				sqlBuffer.append(" or (");
//			}
////				sqlBuffer.append("  (");
//			for (SiteBase siteBase : siteList) {
//				if (siteBase != null) {
//					if (ii > 0) {
//						sqlBuffer.append(" or");
//					}
//					sqlBuffer.append(" tc.site_id = ?");
//					valueList.add(siteBase.getId());
//					ii++;
//				}
//			}
//			sqlBuffer.append(" )");
//		}
//		values = valueList.toArray();
		try {
			confCount = libernate.countEntityListWithSql(strSql.toString(), valueList.toArray());
		} catch (SQLException e) {
			logger.error("系统管理员根据站点标识、会议主题或站点名称查询会议出错！" + e);
		}
		return confCount;
	}
	
	@Override
	public List<ConfBase> getConfListByBaseCondition(String titleOrSiteSign,
			String sortField, String sortord, PageModel pageModel, Integer sysUserId) {
		List<Object> valueList = new ArrayList<Object>();
		List<ConfBase> confList = new ArrayList<ConfBase>();
		List<ConfBaseSimple> confBaseSimpleList = null;
		StringBuilder strSql = new StringBuilder();								//这里添加了pc_num 和phone_num字段
		strSql.append(" SELECT confbase.id,confbase.cycle_id,confbase.conf_name,confbase.pc_num,confbase.phone_num, confbase.site_id,confbase.conf_type,confbase.start_time,confbase.end_time, ");
		strSql.append(" (CASE WHEN  confbase.conf_status =20 THEN 2 WHEN confbase.conf_status =10 THEN 0 WHEN confbase.conf_status =30 THEN 3 ELSE confbase.conf_status END) as conf_status ");
		strSql.append(" FROM ( ");
		strSql.append("     SELECT conf.id,conf.conf_hwid,conf.site_id,conf.cycle_id,conf.start_time,conf.end_time,conf.conf_type,conf.conf_name,conf.pc_num,conf.phone_num, ");//martin 添加了pc_num and phone_num
		strSql.append("     (CASE WHEN  conf.conf_status =0 THEN 20 WHEN conf.conf_status =2 THEN 10 WHEN conf.conf_status =3 THEN 30 ELSE conf.conf_status END) conf_status ");
		strSql.append("     FROM t_conf_base conf WHERE conf.id IN( ");
		strSql.append(" 	SELECT tmp.id FROM ( ");
		strSql.append(" 	SELECT tcb.`id`,tcb.start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` = ? AND del_flag = ? AND tcb.`conf_status` < ? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_CANCELED);
		strSql.append(" 	UNION ALL ");
		strSql.append(" 	SELECT tcb.`id`,MIN(tcb.`start_time`) AS start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND (tcb.`conf_status`=? OR tcb.`conf_status`=?) GROUP BY tcb.cycle_id ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
		valueList.add(ConfConstant.CONF_STATUS_OPENING);
		strSql.append(" 	UNION ALL     ");
		strSql.append(" 	SELECT tcb.`id`, start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND tcb.`conf_status`=? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_FINISHED);
		strSql.append("        ) tmp ");
		strSql.append("     )");
//		if(StringUtil.isNotBlank(titleOrSiteSign)){
//			strSql.append(" AND conf.conf_name LIKE ? ");
//			valueList.add("%" + titleOrSiteSign + "%");
//		}
		strSql.append("     ORDER BY conf.conf_status ASC,conf.start_time DESC,conf.id ASC ");
		strSql.append(" )confbase ,  t_site_base sitebase ");
		strSql.append(" WHERE confbase.site_id = sitebase.id  AND sitebase.`del_flag` = ?  AND (confbase.conf_name LIKE ? OR sitebase.`site_name` LIKE ? OR sitebase.site_sign LIKE ? ) ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add("%" + titleOrSiteSign + "%");
		valueList.add("%" + titleOrSiteSign + "%");
		valueList.add("%" + titleOrSiteSign + "%");
		if(sysUserId != null && sysUserId.intValue() > 0){
			strSql.append(" AND sitebase.create_user = ?");
			valueList.add(sysUserId.intValue());
		}
		if (sortField != null && !"".equals(sortField.trim())
				&& !"null".equals(sortField.toLowerCase().trim())) {
			strSql.append(" order by ");
			for (String[] eachField : SortConstant.CONFBASE_FIELDS) {
				if (eachField != null && eachField[0].equals(sortField)) {
					strSql.append("confbase.");
					strSql.append(BeanUtil.att2Field(eachField[1]));
					break;
				}
			}
			if (SortConstant.SORT_ASC.equals(sortord)) {
				strSql.append(" asc ");
			}

			if (SortConstant.SORT_DESC.equals(sortord) || sortord == null
					|| "".equals(sortord.trim())
					|| "null".equals(sortord.trim().toLowerCase())) {
				strSql.append(" desc ");
			}
		} 
		if (pageModel != null) {
			strSql.append(" limit  ");
			strSql.append(" "
							+ ((IntegerUtil.parseInteger(pageModel.getPageNo()) - 1) * pageModel
									.getPageSize()));
			strSql.append(" , " + pageModel.getPageSize());
		}
//		StringBuffer sqlBuffer = new StringBuffer();
//		List<SiteBase> siteList = null;
//		
//		sqlBuffer.append("select * from t_site_base where del_flag= ? and id >0 ");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		if (titleOrSiteSign != null && !"".equals(titleOrSiteSign) && !"null".equals(titleOrSiteSign.trim().toLowerCase())) {
//			sqlBuffer.append(" and (");
//			sqlBuffer.append(" site_name  like ? ");
//			sqlBuffer.append(" or site_sign like ? ");
////			sqlBuffer.append(" or en_name like ? ");
//			sqlBuffer.append(" )");
//			valueList.add("%" + titleOrSiteSign + "%");
//			valueList.add("%" + titleOrSiteSign + "%");
////			valueList.add("%" + titleOrSiteSign + "%");
//		}
//		if(sysUserId != null && sysUserId.intValue() > 0){
//			sqlBuffer.append(" AND create_user = ?");
//			valueList.add(sysUserId.intValue());
//		}
//		values=valueList.toArray();
//		try {
//			siteList = libernate.getEntityListBase(SiteBase.class,
//					sqlBuffer.toString(), values);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		valueList = new ArrayList<Object>();
//		sqlBuffer = null;
//		sqlBuffer = new StringBuffer();
//		if(sysUserId != null && sysUserId.intValue() > 0){
//			sqlBuffer.append("SELECT tc.* FROM t_conf_base tc ,(SELECT id FROM t_site_base  ts WHERE  ts.`create_user`=? and ts.del_flag = ?) tmpts where tc.`site_id` =tmpts.id ");
//			valueList.add(sysUserId.intValue());
//			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		}else{
//			sqlBuffer.append("select tc.* from t_conf_base tc where 1=1 ");
//		}
//		sqlBuffer.append(" and tc.del_flag= ? and tc.id >0 ");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		if (titleOrSiteSign != null && !"".equals(titleOrSiteSign) && !"null".equals(titleOrSiteSign.trim().toLowerCase())) {
//			sqlBuffer.append(" and tc.conf_name like ? ");
//			valueList.add("%" + titleOrSiteSign + "%");
//		}
//		if (siteList != null && siteList.size() > 0) {
//			int ii = 0;
//			if(sysUserId != null && sysUserId.intValue() > 0){
//				sqlBuffer.append(" and (");
//			}else{
//				sqlBuffer.append(" or (");
//			}
////				sqlBuffer.append(" and (");
//			for (SiteBase siteBase : siteList) {
//				if (siteBase != null) {
//					if (ii > 0) {
//						sqlBuffer.append(" or");
//					}
//					sqlBuffer.append(" tc.site_id = ?");
//					valueList.add(siteBase.getId());
//					ii++;
//				}
//			}
//			sqlBuffer.append(" )");
//		}
//		sqlBuffer.append(" order by ");
//		if (sortField != null && !"".equals(sortField.trim())
//				&& !"null".equals(sortField.toLowerCase().trim())) {
//			for (String[] eachField : SortConstant.CONFBASE_FIELDS) {
//				if (eachField != null && eachField[0].equals(sortField)) {
//					sqlBuffer.append("tc.");
//					sqlBuffer.append(BeanUtil.att2Field(eachField[1]));
//					break;
//				}
//			}
//
//		} else {
//			sqlBuffer.append(BeanUtil
//					.att2Field(SortConstant.CONFBASE_FIELDS[0][1]));
//		}
//		if (SortConstant.SORT_ASC.equals(sortord)) {
//			sqlBuffer.append(" asc ");
//		}
//
//		if (SortConstant.SORT_DESC.equals(sortord) || sortord == null
//				|| "".equals(sortord.trim())
//				|| "null".equals(sortord.trim().toLowerCase())) {
//			sqlBuffer.append(" desc ");
//		}
//		if (pageModel != null) {
//			sqlBuffer.append(" limit  ");
//			sqlBuffer
//					.append(" "
//							+ ((IntegerUtil.parseInteger(pageModel.getPageNo()) - 1) * pageModel
//									.getPageSize()));
//			sqlBuffer.append(" , " + pageModel.getPageSize());
//		}
//		values = valueList.toArray();
		try {
			confBaseSimpleList = libernate.getEntityListBase(ConfBaseSimple.class,
					strSql.toString(), valueList.toArray());
		} catch (SQLException e) {
			logger.error("根据站点标识、会议主题或站点名称查询会议出错！" + e);
		}
		if(confBaseSimpleList != null && confBaseSimpleList.size() > 0){
			String[] filedArray = ObjectUtil.getFieldFromObject(confBaseSimpleList.get(0));
			String copyField = "";
			for(String filed : filedArray){
				if(!"confStatus".equals(filed)){
					copyField = copyField + "," + filed;
				}
			}
			logger.info("copyField:"+copyField);
			for(ConfBaseSimple conf : confBaseSimpleList){
				ConfBase confBase = new ConfBase();
				try {
					confBase = (ConfBase) ObjectUtil.copyObject(conf, confBase, copyField);
					confBase.setConfStatus(Integer.valueOf(conf.getConfStatus().toString()));
				} catch (Exception e) {
					logger.error("转换conf出错！" + e);
				}
				confList.add(confBase);
			}
		}
		return confList;
	}

	/**
	 * 系统管理员会议的高级搜索
	 * @param siteName
	 * @param siteSign
	 * @param beginTime  会议开始时间开始于
	 * @param endTime    会议开始时间结束于
	 * @param sortField
	 * @param sortord
	 * @param pageModel
	 * @param sysUserId  系统管理员id(当超级管理员查询时，传入null即可)
	 * @return
	 */
	@Override
	public List<ConfBase> getConfListByAdvanceCondition(ConfBase confBase,String siteName,String siteSign,
			Date beginTime, Date endTime, String sortField, String sortord, PageModel pageModel, Integer sysUserId) {
		List<ConfBase> confBaseList = new ArrayList<ConfBase>();
		List<ConfBaseSimple> confBaseSimpleList = null;
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder strSql = new StringBuilder();
		strSql.append(" SELECT * from ( ");										//Martin add pc_num and phone_num
		strSql.append(" SELECT confbase.id,confbase.cycle_id,confbase.conf_name,confbase.pc_num,confbase.phone_num,confbase.site_id,confbase.conf_type,confbase.start_time,confbase.end_time, ");
		strSql.append(" (CASE WHEN  confbase.conf_status =20 THEN 2 WHEN confbase.conf_status =10 THEN 0 WHEN confbase.conf_status =30 THEN 3 ELSE confbase.conf_status END) conf_status ");
		strSql.append(" FROM ( ");
		strSql.append("     SELECT conf.id,conf.conf_hwid,conf.site_id,conf.cycle_id,conf.start_time,conf.end_time,conf.conf_type,conf.conf_name, conf.pc_num,conf.phone_num,");
		strSql.append("     (CASE WHEN  conf.conf_status =0 THEN 20 WHEN conf.conf_status =2 THEN 10 WHEN conf.conf_status =3 THEN 30 ELSE conf.conf_status END) conf_status ");
		strSql.append("     FROM t_conf_base conf WHERE conf.id IN( ");
		strSql.append(" 	SELECT tmp.id FROM ( ");
		strSql.append(" 	SELECT tcb.`id`,tcb.start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` = ? AND del_flag = ? AND tcb.`conf_status` < ? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_CANCELED);
		strSql.append(" 	UNION ALL ");
		strSql.append(" 	SELECT tcb.`id`,MIN(tcb.`start_time`) AS start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND (tcb.`conf_status`=? OR tcb.`conf_status`=?) GROUP BY tcb.cycle_id ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
		valueList.add(ConfConstant.CONF_STATUS_OPENING);
		strSql.append(" 	UNION ALL     ");
		strSql.append(" 	SELECT tcb.`id`, start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND tcb.`conf_status`=? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_FINISHED);
		strSql.append("        ) tmp ");
		strSql.append("     )");
		strSql.append("     ORDER BY conf.conf_status ASC,conf.start_time DESC,conf.id ASC ");
		strSql.append(" )confbase ,  t_site_base sitebase ");
		strSql.append(" WHERE confbase.site_id = sitebase.id AND sitebase.`del_flag` = ? AND confbase.conf_name LIKE ? AND sitebase.`site_name` LIKE ? AND sitebase.site_sign LIKE ?   ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add("%"+ confBase.getConfName() +"%");
		valueList.add("%"+ siteName +"%");
		valueList.add("%"+ siteSign +"%");
		if(sysUserId != null && sysUserId.intValue() > 0){
			strSql.append(" AND sitebase.create_user = ?");
			valueList.add(sysUserId.intValue());
		}
		if(beginTime != null){
			beginTime = DateUtil.getGmtDate(beginTime);
			strSql.append(" AND confbase.start_time >= ? ");
			valueList.add(beginTime);
		}
		if(endTime != null){
			endTime = DateUtil.getGmtDate(DateUtil.addDate(endTime, 1));
			strSql.append(" AND confbase.start_time <= ? ");
			valueList.add(endTime);
		}
		Integer confType = confBase.getConfType();
		Integer confStatus = confBase.getConfStatus();
		if(confType != null && confType.intValue() > 0){
			strSql.append(" AND confbase.conf_type = ? ");
			valueList.add(confType);
		}
		strSql.append(" ) con ");
		if(confStatus != null && confStatus.intValue() >= 0 && confStatus.intValue() < 99){
			strSql.append(" where con.conf_status = ? ");
			valueList.add(confStatus);
		}
//		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 ");
//		strSql.append(" AND site_id IN ( SELECT id FROM t_site_base WHERE site_name LIKE ? AND site_sign LIKE ? ");
//		valueList.add("%"+ siteName +"%");
//		valueList.add("%"+ siteSign +"%");
//		if(sysUserId != null && sysUserId.intValue() > 0){
//			strSql.append(" AND create_user = ?");
//			valueList.add(sysUserId.intValue());
//		}
//		strSql.append(" AND del_flag = ? )");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		if(beginTime != null){
//			beginTime = DateUtil.getGmtDate(beginTime);
//			strSql.append(" AND start_time >= ? ");
//			valueList.add(beginTime);
//		}
//		if(endTime != null){
//			endTime = DateUtil.getGmtDate(DateUtil.addDate(endTime, 1));
//			strSql.append(" AND start_time <= ? ");
//			valueList.add(endTime);
//		}
//		Integer confType = confBase.getConfType();
//		Integer confStatus = confBase.getConfStatus();
//		strSql.append(" AND conf_name LIKE ? ");
//		valueList.add("%"+ confBase.getConfName() +"%");
//		if(confType != null && confType.intValue() > 0){
//			strSql.append(" AND conf_type = ? ");
//			valueList.add(confType);
//		}
//		if(confStatus != null && confStatus.intValue() > 0 && confStatus.intValue() < 99){
//			strSql.append(" AND conf_status = ? ");
//			valueList.add(confStatus);
//		}
//		strSql.append(" AND del_flag = ? AND id >0 ");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ").append(" con.").append(sortField);
			if(StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortord) && "desc".equals(sortord.trim().toLowerCase())){
				strSql.append(" DESC");
			}
		}
		if(pageModel != null){
			int recordNo = (Integer.parseInt(pageModel.getPageNo())-1) * pageModel.getPageSize();   //当前页第一条记录在数据库中位置
			strSql.append(" limit ? , ?  ");
			valueList.add(recordNo);
			valueList.add(pageModel.getPageSize());
		}
//		try{
//			Object[] values = valueList.toArray();
//			confBaseList = DAOProxy.getLibernate().getEntityListBase(ConfBase.class, strSql.toString(), values);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
		try {
			confBaseSimpleList = libernate.getEntityListBase(ConfBaseSimple.class,
					strSql.toString(), valueList.toArray());
		} catch (SQLException e) {
			logger.error("根据站点标识、会议主题或站点名称查询会议出错！" + e);
		}
		if(confBaseSimpleList != null && confBaseSimpleList.size() > 0){
			String[] filedArray = ObjectUtil.getFieldFromObject(confBaseSimpleList.get(0));
			String copyField = "";
			for(String filed : filedArray){
				if(!"confStatus".equals(filed)){
					copyField = copyField + "," + filed;
				}
			}
			logger.info("copyField:"+copyField);
			for(ConfBaseSimple conf : confBaseSimpleList){
				ConfBase confBaseObject = new ConfBase();
				try {
					confBaseObject = (ConfBase) ObjectUtil.copyObject(conf, confBaseObject, copyField);
					confBaseObject.setConfStatus(Integer.valueOf(conf.getConfStatus().toString()));
				} catch (Exception e) {
					logger.error("转换conf出错！" + e);
				}
				confBaseList.add(confBaseObject);
			}
		}
		return confBaseList;
	}

	/**
	 * 系统管理员下对会议高级搜索时统计会议总数
	 * @param siteName
	 * @param siteSign
	 * @param beginTime  会议开始时间开始于
	 * @param endTime    会议开始时间结束于
	 * @param sysUserId  系统管理员id(当超级管理员查询时，传入null即可)
	 * @return
	 */
	@Override
	public Integer countConfListByAdvanceCondition(ConfBase confBase,String siteName,String siteSign,Date beginTime, Date endTime, Integer sysUserId) {
		int rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder strSql = new StringBuilder();
		strSql.append(" SELECT count(1) from (");
		strSql.append(" SELECT confbase.id,confbase.cycle_id,confbase.conf_name,confbase.site_id,confbase.conf_type,confbase.start_time,confbase.end_time, ");
		strSql.append(" (CASE WHEN  confbase.conf_status =20 THEN 0 WHEN confbase.conf_status =10 THEN 2 WHEN confbase.conf_status =30 THEN 3 ELSE confbase.conf_status END) conf_status ");
		strSql.append(" FROM ( ");
		strSql.append("     SELECT conf.id,conf.conf_hwid,conf.site_id,conf.cycle_id,conf.start_time,conf.end_time,conf.conf_type,conf.conf_name, ");
		strSql.append("     (CASE WHEN  conf.conf_status =0 THEN 20 WHEN conf.conf_status =2 THEN 10 WHEN conf.conf_status =3 THEN 30 ELSE conf.conf_status END) conf_status ");
		strSql.append("     FROM t_conf_base conf WHERE conf.id IN( ");
		strSql.append(" 	SELECT tmp.id FROM ( ");
		strSql.append(" 	SELECT tcb.`id`,tcb.start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` = ? AND del_flag = ? AND tcb.`conf_status` < ? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_CANCELED);
		strSql.append(" 	UNION ALL ");
		strSql.append(" 	SELECT tcb.`id`,MIN(tcb.`start_time`) AS start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND (tcb.`conf_status`=? OR tcb.`conf_status`=?) GROUP BY tcb.cycle_id ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
		valueList.add(ConfConstant.CONF_STATUS_OPENING);
		strSql.append(" 	UNION ALL     ");
		strSql.append(" 	SELECT tcb.`id`, start_time FROM t_conf_base tcb WHERE tcb.`cycle_id` > ? AND tcb.del_flag = ? AND tcb.`conf_status`=? ");
		valueList.add(0);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_STATUS_FINISHED);
		strSql.append("        ) tmp ");
		strSql.append("     )");
//		if(StringUtil.isNotBlank(titleOrSiteSign)){
//			strSql.append(" AND conf.conf_name LIKE ? ");
//			valueList.add("%" + titleOrSiteSign + "%");
//		}
		strSql.append("     ORDER BY conf.conf_status ASC,conf.start_time DESC,conf.id ASC ");
		strSql.append(" )confbase ,  t_site_base sitebase ");
		strSql.append(" WHERE confbase.site_id = sitebase.id AND sitebase.`del_flag` = ? AND confbase.conf_name LIKE ? AND sitebase.`site_name` LIKE ? AND sitebase.site_sign LIKE ?   ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add("%"+ confBase.getConfName() +"%");
		valueList.add("%"+ siteName +"%");
		valueList.add("%"+ siteSign +"%");
		if(sysUserId != null && sysUserId.intValue() > 0){
			strSql.append(" AND sitebase.create_user = ?");
			valueList.add(sysUserId.intValue());
		}
		if(beginTime != null){
			beginTime = DateUtil.getGmtDate(beginTime);
			strSql.append(" AND confbase.start_time >= ? ");
			valueList.add(beginTime);
		}
		if(endTime != null){
			endTime = DateUtil.getGmtDate(DateUtil.addDate(endTime, 1));
			strSql.append(" AND confbase.start_time <= ? ");
			valueList.add(endTime);
		}
		Integer confType = confBase.getConfType();
		Integer confStatus = confBase.getConfStatus();
		if(confType != null && confType.intValue() > 0){
			strSql.append(" AND confbase.conf_type = ? ");
			valueList.add(confType);
		}
		strSql.append(" ) con ");
		if(confStatus != null && confStatus.intValue() >= 0 && confStatus.intValue() < 99){
			strSql.append(" where con.conf_status = ? ");
			valueList.add(confStatus);
		}
//		StringBuffer strSql = new StringBuffer(" SELECT COUNT(1) FROM t_conf_base WHERE 1=1 ");
//		strSql.append(" AND site_id IN ( SELECT id FROM t_site_base WHERE site_name LIKE ? and site_sign LIKE ? ");
//		valueList.add("%"+ siteName +"%");
//		valueList.add("%"+ siteSign +"%");
//		if(sysUserId != null && sysUserId.intValue() > 0){
//			strSql.append(" AND create_user = ? ");
//			valueList.add(sysUserId.intValue());
//		}
//		strSql.append(" AND del_flag = ? )");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
//		if(beginTime != null){
//			beginTime = DateUtil.getGmtDate(beginTime);
//			strSql.append(" AND start_time >= ? ");
//			valueList.add(beginTime);
//		}
//		if(endTime != null){
//			endTime = DateUtil.getGmtDate(DateUtil.addDate(endTime, 1));
//			strSql.append(" AND start_time <= ? ");
//			valueList.add(endTime);
//		}
//		Integer confType = confBase.getConfType();
//		Integer confStatus = confBase.getConfStatus();
//		strSql.append(" AND conf_name LIKE ? ");
//		valueList.add("%"+ confBase.getConfName() +"%");
//		if(confType != null && confType.intValue() > 0){
//			strSql.append(" AND conf_type = ? ");
//			valueList.add(confType);
//		}
//		if(confStatus != null && confStatus.intValue() > 0 && confStatus.intValue() < 99){
//			strSql.append(" AND conf_status = ? ");
//			valueList.add(confStatus);
//		}
//		strSql.append(" AND del_flag = ? AND id >0 ");
//		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		try {
			rows = DAOProxy.getLibernate().countEntityListWithSql(strSql.toString(), valueList.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public List<ConfUser> getConfUserListByConfId(Integer confId) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据会议cycleId号获取预约成功状态，并且通过华为接口获得了confHwid的会议
	 * @param cycleId  周期会议ID号
	 * 2013-2-21
	 */
	//List<
	private List<ConfBase> getConfWithHwIdByCycleId(int cycleId){
		List<ConfBase> confs = null;
//		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE cycle_id = ? AND conf_status = ?  AND del_flag = ? AND soap_status = ?");//
		Object[] values = new Object[4];
		values[0] = cycleId;
		values[1] = ConfConstant.CONF_STATUS_SUCCESS;
		values[2] = ConstantUtil.DELFLAG_UNDELETE;
		values[3] = ConfConstant.CONF_SOAP_STATUS_TRUE;
		try {
			//confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
			confs  = libernate.getEntityListBase(ConfBase.class, strSql.toString(), values);
			if(confs == null)confs = new ArrayList<ConfBase>();
		} catch (SQLException e) {
			logger.error("根据会议ID号获取会议信息出错！",e);
		}
		return confs;
	}
	

	/**
	 * 根据主持人会议安全号取到会议信息
	 * wangyong
	 * 2013-3-11
	 */
	public ConfBase getConfBaseByCompereSecure(String compereSecure){
		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND compere_secure = ? ");
		Object[] values = new Object[1];
		values[0] = compereSecure;
		try {
			confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据会议ID号获取会议信息出错！",e);
		}
		return confBase;
	}
	
	/**
	 * 根据与会者会议安全号取到会议信息
	 * wangyong
	 * 2013-3-11
	 */
	public ConfBase getConfBaseByUserSecure(String userSecure){
		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND user_secure = ? ");
		Object[] values = new Object[1];
		values[0] = userSecure;
		try {
			confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据会议ID号获取会议信息出错！",e);
		}
		return confBase;
	}

	/**
	 * 根据会议ID号获取会议信息
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	@Override
	public ConfBase getConfBasebyConfId(int confId){
		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND id = ? ");
		Object[] values = new Object[1];
		values[0] = confId;
		try {
			confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据会议ID号获取会议信息出错！",e);
		}
		return confBase;
	}
	
	/**
	 * 根据周期会议ID号获取周期会议周期信息
	 * @param cycleId  周期会议ID号
	 * 2013-2-21
	 */
	public ConfCycle getConfCyclebyConfId(int cycleId){
		ConfCycle confCycle = new ConfCycle();
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_cycle WHERE 1=1 AND id = ? ");
		Object[] values = new Object[]{
				cycleId
		};
		try {
			confCycle = libernate.getEntityCustomized(ConfCycle.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据会议ID号获取周期会议周期信息出错！",e);
		}
		return confCycle;
	}
	
	/**
	 * 根据会议ID号获取会议信息(可获得日期为站点所设时区的会议信息)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	@Override
	public ConfBase getConfBasebyConfId(int confId, SiteBase currentSite){
		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND id = ? ");
		Object[] values = new Object[1];
		values[0] = confId;
		try {
			confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
			if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
				confBase = getOffsetConf(currentSite, confBase);
			}
		} catch (SQLException e) {
			logger.error("根据会议ID号获取会议信息出错！",e);
		}
		return confBase;
	}
	
	/**
	 * 获取所有周期预约会议的日期
	 * @param cycleId  循环会议ID号
	 * 2013-2-21
	 */
	@Override
	public List<ConfBase> getCycleConfDate(int cycleId, SiteBase currentSite){
		List<ConfBase> confList = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND cycle_id = ? AND conf_status = ? AND del_flag = ? ");
		Object[] values = new Object[]{
				cycleId,
				ConfConstant.CONF_STATUS_SUCCESS,
				ConstantUtil.DELFLAG_UNDELETE
		};
		try {
			confList = libernate.getEntityListBase(ConfBase.class, strSql.toString(), values);
			if(confList != null && confList.size() > 0){
				confList = getOffsetConfList(currentSite, confList);
			}
		} catch (SQLException e) {
			logger.error("根据循环会议ID号获取所有周期预约会议的日期出错！",e);
		}
		return confList;
	}
	
	/**
	 * 根据会议ID号获取会议信息(可获得日期为用户喜好设置时区的会议信息)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	@Override
	public ConfBase getConfBasebyConfId(int confId, UserBase currentUser){
		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND id = ? ");
		Object[] values = new Object[1];
		values[0] = confId;
		try {
			confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
			if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
				confBase = getOffsetConf(currentUser, confBase);
			}
		} catch (SQLException e) {
			logger.error("根据会议ID号获取会议信息出错！",e);
		}
		return confBase;
	}
	
	/**
	 * 获取用户喜好设置时区所有周期预约会议的日期
	 * @param cycleId  循环会议ID号
	 * 2013-2-21
	 */
	@Override
	public List<ConfBase> getCycleConfDate(int cycleId, UserBase currentUser){
		List<ConfBase> confList = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND cycle_id = ? AND conf_status = ? AND del_flag = ? ");
		Object[] values = new Object[]{
				cycleId,
				ConfConstant.CONF_STATUS_SUCCESS,
				ConstantUtil.DELFLAG_UNDELETE
		};
		try {
			confList = libernate.getEntityListBase(ConfBase.class, strSql.toString(), values);
			if(confList != null && confList.size() > 0){
				confList = getOffsetConfList(currentUser, confList);
			}
		} catch (SQLException e) {
			logger.error("根据循环会议ID号获取所有周期预约会议的日期出错！",e);
		}
		return confList;
	}
	
	/**
	 * 根据cycleId号获取预约成功的会议信息(站点时区时间)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	@Override
	public ConfBase getConfBasebyCycleId(int cycleId, SiteBase currentSite){
		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND cycle_id = ? AND conf_status = ? ");
		Object[] values = new Object[]{
				cycleId,
				ConfConstant.CONF_STATUS_SUCCESS.intValue()
		};
		try {
			confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
			if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
				confBase = getOffsetConf(currentSite, confBase);
			}
		} catch (SQLException e) {
			logger.error("根据cycleId号获取会议信息出错！",e);
		}
		return confBase;
	}
	
	/**
	 * 根据cycleId号获取预约成功的会议信息(gmt时区时间)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	@Override
	public ConfBase getConfBasebyCycleId(int cycleId){
		ConfBase confBase = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND cycle_id = ? AND conf_status = ? ");
		Object[] values = new Object[]{
				cycleId,
				ConfConstant.CONF_STATUS_SUCCESS.intValue()
		};
		try {
			confBase = libernate.getEntityCustomized(ConfBase.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据cycleId号获取会议信息出错！",e);
		}
		return confBase;
	}
	
	
	/**
	 * 通过会议ID获取邀请人信息
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	public List<ConfUser> getConfInviteUser(int confId){
		List<ConfUser> userList = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_user WHERE conf_id = ? AND host_flag = ? AND accept_flag != ? ORDER BY id ASC ");
		Object[] values = new Object[]{
				confId,
				ConfConstant.CONF_USER_PARTICIPANT.intValue(),
				ConfConstant.CONF_USER_REFUSE.intValue()
		};
		try {
			userList = libernate.getEntityListBase(ConfUser.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("通过会议ID获取前几个邀请人信息出错！",e);
		}
		return userList;
	}
	
	@Override
	public Integer countConfUserByConfId(Integer confId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConfUser> getConfUserListByCycleId(Integer cycleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countConfUserByCycleId(Integer cycleId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConfUserCount> countConfUserByConfIds(Integer[] confIds) {
		List<ConfUserCount> confUserCount = null;
		if (confIds != null && confIds.length > 0) {
			StringBuffer sqlBuffer = new StringBuffer();
			List<Object> valueList = new ArrayList<Object>();
			sqlBuffer
					.append("select  conf_id,COUNT(id) as user_count from t_conf_user where conf_id > 0 and del_flag = ? ");
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			sqlBuffer.append(" and  (");
			int ii = 0;
			for (Integer confId : confIds) {
				if (ii > 0) {
					sqlBuffer.append(" or ");
				}
				sqlBuffer.append(" conf_id = ?");
				valueList.add(confId);
				ii++;
			}
			sqlBuffer.append(" )");
			sqlBuffer.append(" group by conf_id ");
			Object[] values = new Object[valueList.size()];
			for (ii = 0; ii < valueList.size(); ii++) {
				values[ii] = valueList.get(ii);
			}

			try {
				confUserCount = libernate.getEntityListBase(
						ConfUserCount.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return confUserCount;
	}

	@Override
	public List<ConfUserCount> countConfUserByCycleIds(Integer[] cycleIds) {
		List<ConfUserCount> confUserCount = null;
		if (cycleIds != null && cycleIds.length > 0) {
			StringBuffer sqlBuffer = new StringBuffer();
			List<Object> valueList = new ArrayList<Object>();
			sqlBuffer.append("select cycle_id,COUNT(id) as user_count from t_conf_user where cycle_id > 0 and del_flag = ? ");
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			sqlBuffer.append(" and  (");
			int ii = 0;
			for (Integer cycleId : cycleIds) {
				if (ii > 0) {
					sqlBuffer.append(" or ");
				}
				sqlBuffer.append(" cycle_id = ?");
				valueList.add(cycleId);
				ii++;
			}
			sqlBuffer.append(" )");//cycleId  cycle_id
			sqlBuffer.append(" group by cycle_id  ");
			Object[] values = new Object[valueList.size()];
			for (ii = 0; ii < valueList.size(); ii++) {
				values[ii] = valueList.get(ii);
			}
			logger.info("sqlBuffer sql ==" +sqlBuffer.toString());
			try {
				confUserCount = libernate.getEntityListBase(ConfUserCount.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return confUserCount;
	}
	
	/**
	 * 获取排序参数
	 * wangyong
	 * 2013-1-22
	 */
	private String initSort(String field){
		String sortField = SortConstant.CONFBASE_FIELDS[0][1];
		for (String[] eachField : SortConstant.CONFBASE_FIELDS) {
			if (eachField != null && eachField[0].equals(field)) {
				sortField = BeanUtil.att2Field(eachField[1]);
				break;
			}
		}
		return sortField;
	}
	
	/**
	 * 站点用户获取当前站点时区的会议
	 * wangyong
	 * 2013-3-8
	 */
	private ConfBase getOffsetConf(SiteBase currentSite, ConfBase conf){
		if(conf != null){
			String[] fields = new String[]{"startTime","endTime"};
			long offset = 0 ;
			if(currentSite != null){
				offset = currentSite.getTimeZone();
			}else{
				offset = DateUtil.getDateOffset();
			}
			logger.info("当前访问的站点时区" + offset);
			conf = (ConfBase)ObjectUtil.offsetDate(conf, offset, fields);
		}
		return conf; 
	}

	/**
	 * 站点用户获取当前站点时区的会议列表
	 * wangyong
	 * 2013-3-8
	 */
	@SuppressWarnings("unchecked")
	private List<ConfBase> getOffsetConfList(SiteBase currentSite, List<ConfBase> confList){
		List<ConfBase> confBaseList = new ArrayList<ConfBase>();
		String[] fields = new String[]{"startTime","endTime"};
		long offset = 0 ;
		if(currentSite != null){
			offset = currentSite.getTimeZone();
		}else{
			offset = DateUtil.getDateOffset();
		}
		logger.info("当前访问的站点时区" + offset);
		confBaseList = ObjectUtil.offsetDateWithList(confList, offset, fields);
		return confBaseList;
	}
	
	/**
	 * 站点用户获取用户喜好设置时区的会议
	 * wangyong
	 * 2013-3-8
	 */
	private ConfBase getOffsetConf(UserBase currentUser, ConfBase conf){
		if(conf != null){
			String[] fields = new String[]{"startTime","endTime"};
			long offset = 0 ;
			if(currentUser != null){
				offset = currentUser.getTimeZone();
			}else{
				offset = DateUtil.getDateOffset();
			}
			logger.info("当前访问的站点时区" + offset);
			conf = (ConfBase)ObjectUtil.offsetDate(conf, offset, fields);
		}
		return conf; 
	}

	/**
	 * 站点用户获取用户喜好设置时区的会议列表
	 * wangyong
	 * 2013-3-8
	 */
	@SuppressWarnings("unchecked")
	private List<ConfBase> getOffsetConfList(UserBase currentUser, List<ConfBase> confList){
		List<ConfBase> confBaseList = new ArrayList<ConfBase>();
		String[] fields = new String[]{"startTime","endTime"};
		long offset = 0 ;
		if(currentUser != null){
			offset = currentUser.getTimeZone();
		}else{
			offset = DateUtil.getDateOffset();
		}
		logger.info("当前访问的站点时区" + offset);
		confBaseList = ObjectUtil.offsetDateWithList(confList, offset, fields);
		return confBaseList;
	}
	
	@Override
	public boolean saveConfUser(ConfUser confUser) {
		try {
			libernate.saveEntity(confUser);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 更新企业用户会议设置
	 * wangyong
	 * 2013-3-20
	 */
	@Override
	public boolean updateConfConfig(DefaultConfig confConfig){
		try {
			libernate.updateEntity(confConfig);
		} catch (Exception e) {
			logger.error("更新企业用户会议设置" + e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 当前登录用户是否有权限修改一个预约会议
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 * wangyong
	 * 2013.3.6
	 */
	@Override
	public boolean updateConfAuthority(Integer confId, UserBase currentUser){
		boolean authorityFlag = false;
		Integer createConfUser = null;
		ConfBase conf = getConfBasebyConfId(confId);
		if(conf != null && conf.getCreateUser() != null){
			createConfUser = conf.getCreateUser();
		}
		Integer currentUserId = currentUser.getId();
		Integer confStatus = conf.getConfStatus();
		if(createConfUser != null && currentUserId != null && createConfUser.intValue() == currentUserId.intValue()){   //自己创建的会议可以修改
			if(confStatus != null && confStatus.intValue() != ConfConstant.CONF_STATUS_OPENING.intValue()){   //正在进行的会议不可修改
				authorityFlag = true;
			}
		}		
		return authorityFlag;
	}
	
	/**
	 * 当前登录用户是否有权限重新创建一个预约会议
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 * wangyong
	 * 2013.3.6
	 */
	@Override
	public boolean recreateConfAuthority(Integer confId, UserBase currentUser){
		boolean authorityFlag = false;
		Integer createConfUser = null;
		ConfBase conf = getConfBasebyConfId(confId);
		if(conf != null && conf.getCreateUser() != null){
			createConfUser = conf.getCreateUser();
		}
		Integer currentUserId = currentUser.getId();
		Integer confStatus = conf.getConfStatus();
		if(createConfUser != null && currentUserId != null && createConfUser.intValue() == currentUserId.intValue()){   //自己创建的会议可以修改
			if(confStatus != null && confStatus.intValue() != ConfConstant.CONF_STATUS_OPENING.intValue()){   //正在进行的会议不可修改
				authorityFlag = true;
			}
		}		
		return authorityFlag;
	}
	
	/**
	 * 当前登录用户是否有权限取消一个预约会议
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 * wangyong
	 * 2013.3.6
	 */
	@Override
	public boolean cancleConfAuthority(Integer confId, UserBase currentUser){
		boolean authorityFlag = false;
		Integer createConfUser = null;
		ConfBase conf = getConfBasebyConfId(confId);
		if(conf != null && conf.getCreateUser() != null){
			createConfUser = conf.getCreateUser();
		}
		Integer currentUserId = currentUser.getId();
		Integer confStatus = conf.getConfStatus();
		if(createConfUser != null && currentUserId != null && createConfUser.intValue() == currentUserId.intValue()){   //自己创建的会议可以删除
			if(ConfConstant.CONF_PERMANENT_UNABLE.equals(conf.getPermanentConf())){
				if(confStatus != null && confStatus.intValue() != ConfConstant.CONF_STATUS_OPENING.intValue() ){   //正在进行的会议不可删除
					authorityFlag = true;
				}
			}else if(ConfConstant.CONF_PERMANENT_ENABLED_MAIN.equals(conf.getPermanentConf())){
				authorityFlag = true;
			}
			
		}		
		return authorityFlag;
	}
	
	/**
	 * 当前登录用户是否有权限邀请参会人
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 * wangyong
	 * 2013.3.6
	 */
	@Override
	public boolean inviteConfAuthority(Integer confId, UserBase currentUser){
		boolean authorityFlag = false;
		Integer createConfUser = null;
		ConfBase conf = getConfBasebyConfId(confId);
		if(conf != null && conf.getCreateUser() != null){
			createConfUser = conf.getCreateUser();
		}
		Integer currentUserId = currentUser.getId();
		Integer confStatus = conf.getConfStatus();
		if(createConfUser != null && currentUserId != null && createConfUser.intValue() == currentUserId.intValue()){   //自己创建的会议可以邀请
			if(confStatus != null && confStatus.intValue() == ConfConstant.CONF_STATUS_SUCCESS.intValue()){   //预约成功的会议可以邀请
				authorityFlag = true;
			}
		}		
		return authorityFlag;
	}
	
	
	/**
	 * 获取企业用户默认会议设置
	 * wangyong
	 * 2013-3-20
	 */
	@Override
	public DefaultConfig getDefaultConfig(UserBase currentUser){
		DefaultConfig mtgParam = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_default_config WHERE 1=1 AND site_id = ? AND user_id = ? ");
		Object[] values = new Object[2];
		values[0] = currentUser.getSiteId();
		values[1] = currentUser.getId();
		try {
			mtgParam = libernate.getEntityCustomized(DefaultConfig.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("获取会议参数设置出错！",e);
		}
		return mtgParam;
	}
	
	/**
	 * 新增企业用户默认会议设置
	 * 1.参会人权限：换页，批注，与所有人聊天，与主持人聊天，与参会人聊天
		默认（与所有人，与主持人，与参会人勾选，其他不勾选）
	   2.会议功能：文档共享，屏幕共享，媒体共享，白板，文件传输，录制
		默认（文档，屏幕，白板，文件传输，录制勾选，媒体共享不勾选）
		笔记、视频、音频、聊天、公告、私聊、组聊、投票（默认全部开启，但不在页面上配置）,会议助理禁用
		记住：相应修改的页面有：会议缺省设置，创建会议的第三步高级设置
	 * wangyong
	 * 2013-3-20
	 */
	@Override
	public DefaultConfig saveDefaultConfig(UserBase currentUser){
		DefaultConfig confConfig = new DefaultConfig();
		if(currentUser != null && currentUser.getId() != null && currentUser.getId().intValue() > 0){
			EmpowerConfig userEmpower = empowerConfigService.makeEmpowerForConf(currentUser);   //获取用户创建会议，缺省会议设置的权限
			confConfig.setVideoType(ConfConstant.CONF_CONFIG_VIDEO_TYPE);
			confConfig.setDefaultDpi(String.valueOf(confConfig.getVideoType()).substring(0, 1));
			confConfig.setMaxDpi(String.valueOf(confConfig.getVideoType()).substring(1, 2));
			confConfig.setUserId(currentUser.getId());
			confConfig.setAheadTimes(ConfConstant.CONF_CONFIG_AHEADTIMES);
			confConfig.setClientConfig(setDefaultClientConfig(currentUser));
			confConfig.setFuncBits(setDefaultFuncBits(currentUser));
			confConfig.setMaxAudio(ConfConstant.CONF_CONFIG_MAX_AUDIO);
			confConfig.setMaxUser(ConfConstant.CONF_CONFIG_MAX_USER);
			if(userEmpower != null && userEmpower.getVideoFlag() == SiteConstant.EMPOWER_ENABLED && userEmpower.getVideoNumber() > 0){
				confConfig.setMaxVideo(userEmpower.getVideoNumber());
			}else{
				confConfig.setMaxVideo(0);
			}
			confConfig.setMtgType(0);
			confConfig.setOpenIpad(0);
			confConfig.setPriviBits(setDefaultPriviBits());
			confConfig.setSiteId(currentUser.getSiteId());
			confConfig.setConfType(0);     //会议缺省设置只配置电话功能，不配置视频功能，默认关闭
		}
		try {
			confConfig = libernate.saveEntity(confConfig);
		} catch (Exception e) {
			logger.error("新增企业用户默认会议设置出错！",e);
		}
		return confConfig;
	}
	
	/**
	 * 设置默认会议功能
	 * 大家知晓一下，昨天改的会议功能及参会人权限有改动。请大家以这次为准。改动如下：
	 *  1.参会人权限：换页，批注，与所有人聊天，与主持人聊天，与参会人聊天
		默认（与所有人，与主持人，与参会人勾选，其他不勾选）
		2.会议功能：文档共享，屏幕共享，媒体共享，白板，文件传输，录制
		默认（文档，屏幕，白板，文件传输，录制勾选，媒体共享不勾选）
		笔记、视频、音频、聊天、公告、私聊、组聊、投票（默认全部开启，但不在页面上配置）,会议助理禁用
		记住：相应修改的页面有：会议缺省设置，创建会议的第三步高级设置
	 * wangyong
	 * 2013-3-20
	 */
	private String setDefaultClientConfig(UserBase currentUser){
		char[] clientConfig = "0000000000000000000000000000000000".toCharArray();    //共34位
		clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_DOCS] = '1';   	//文档共享
		clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_SCREEN] = '1'; 	//屏幕共享
		clientConfig[ConfConstant.CLIENT_CONFIG_WHITE_BOARD] = '1';     //白板
		clientConfig[ConfConstant.CLIENT_CONFIG_FILE_TRANS] = '1';      //文件传输
		char record = '1';
		//根据站点ID号取站点的全局变量设置
		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentUser.getSiteId());
		//根据用户的ID号取用户的授权配置(站点管理员创建企业用户时的授权配置)
		EmpowerConfig userPower  = empowerConfigService.getUserEmpowerConfigByUserId(currentUser.getId());
		if(sitePower != null && userPower != null){
			//站点全局变量“媒体共享”启用并且用户“媒体共享”授权启用后，创建会议才可以选择“媒体共享”项
			if(sitePower.getRecordFlag().intValue() != SiteConstant.EMPOWER_ENABLED || userPower.getRecordFlag().intValue() != SiteConstant.EMPOWER_ENABLED){
				record = '0';
			}
		}
		clientConfig[ConfConstant.CLIENT_CONFIG_RECORD] = record;			//录制
		clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_MEDIA] = '0';		//媒体共享
		clientConfig[ConfConstant.CLIENT_CONFIG_ASSISTANT] = '0';     	//会议助理禁用
		confLogic.setServerClientConfig(clientConfig);
		return String.valueOf(clientConfig);
	}
	
	/**
	 * 设置默认参会人权限
	 * 大家知晓一下，昨天改的会议功能及参会人权限有改动。请大家以这次为准。改动如下：1.参会人权限：换页，批注，与所有人聊天，与主持人聊天，与参会人聊天
		默认（与所有人，与主持人，与参会人勾选，其他不勾选）
		2.会议功能：文档共享，屏幕共享，媒体共享，白板，文件传输，录制
		默认（文档，屏幕，白板，文件传输，录制勾选，媒体共享不勾选）
		笔记、视频、音频、聊天、公告、私聊、组聊、投票（默认全部开启，但不在页面上配置）,会议助理禁用
		记住：相应修改的页面有：会议缺省设置，创建会议的第三步高级设置
	 * wangyong
	 * 2013-3-20
	 */
	private String setDefaultPriviBits(){
		char[] priviBits = "00000000000000000000000000000000".toCharArray();    //共32位
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHANGEPAGE] = '0';
		priviBits[ConfConstant.PRIVIBITS_CONFIG_ANNOTATE] = '0';
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHAT_ANYONE] = '1';
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHAT_COMPERE] = '1';
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHAT_PARTICIPANTS] = '1';
		return String.valueOf(priviBits);
	}
	
	/**
	 * 设置会议功能
	 * 大家知晓一下，昨天改的会议功能及参会人权限有改动。请大家以这次为准。改动如下：1.参会人权限：换页，批注，与所有人聊天，与主持人聊天，与参会人聊天
		默认（与所有人，与主持人，与参会人勾选，其他不勾选）
		2.会议功能：文档共享，屏幕共享，媒体共享，白板，文件传输，录制
		默认（文档，屏幕，白板，文件传输，录制勾选，媒体共享不勾选）
		笔记、视频、音频、聊天、公告、私聊、组聊、投票（默认全部开启，但不在页面上配置）,会议助理禁用
		记住：相应修改的页面有：会议缺省设置，创建会议的第三步高级设置
	 * wangyong
	 * 2013-3-20
	 */
	private String setDefaultFuncBits(UserBase currentUser){
		char[] funcBits = "00000000000000000000000000000000".toCharArray();    //共32位
		funcBits[ConfConstant.FUNCBITS_CONFIG_AUDIO_MIX] = ConfConstant.AUDIO_SERVER_MIX ? '1' : '0';
		char autoInvite = '0';    //默认自动外呼关闭
		//根据站点ID号取站点的全局变量设置
		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentUser.getSiteId());
		//根据用户的ID号取用户的授权配置(站点管理员创建企业用户时的授权配置)
		EmpowerConfig userPower  = empowerConfigService.getUserEmpowerConfigByUserId(currentUser.getId());
		if(sitePower != null && userPower != null){
			//站点全局变量“媒体共享”启用并且用户“媒体共享”授权启用后，创建会议才可以选择“媒体共享”项
			if(sitePower.getAutoFlag().intValue() != SiteConstant.EMPOWER_ENABLED || userPower.getAutoFlag().intValue() != SiteConstant.EMPOWER_ENABLED){
				autoInvite = '0';
			}
		}
		funcBits[ConfConstant.FUNCBITS_CONFIG_AUTO_INVITE] = autoInvite;
		funcBits[ConfConstant.FUNCBITS_CONFIG_CONF_MODEL] = '1';    //默认主持人模式
		funcBits[ConfConstant.FUNCBITS_CONFIG_MIC_STATUS] = '0';    //默认麦克风状态关闭
		funcBits[ConfConstant.FUNCBITS_CONFIG_SUPER_MEETING] = ConfConstant.HUGE_MEETING_ENABLE ? '1' : '0'; //默认超大会场关闭
		return String.valueOf(funcBits);
	}
	
	
	
	
	public ConfBase saveConfBase(ConfBase confBase) {
		try {
			String retInfo = confManagementService.createConf(confBase,null,null);
			if(retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
				confBase=libernate.saveEntity(confBase);
			}else{
				throw new RuntimeException("invoking the soap interface error!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return confBase;
	}
	
	

	public ConfBase saveConfBaseForOuter(ConfBase confBase,SiteBase siteBase,UserBase userBase ) {
		try {
			String retInfo = confManagementService.createConf(confBase,siteBase,userBase);
			if(retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
				confBase=libernate.saveEntity(confBase);
			}else{
				throw new RuntimeException("invoking the soap interface error!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return confBase;
	}
 
	
	/**
	 * 获取周期会议的会议列表
	 */
	@Override
	public List<ConfBase> getCycConfBases(int cycId) {
		List<ConfBase> confs = new ArrayList<ConfBase>();
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE 1=1 AND cycle_id = ? AND del_flag = ? AND (conf_status = ? OR conf_status=?) ");
		Object[] values = new Object[]{
				cycId,
				ConstantUtil.DELFLAG_UNDELETE,
				ConfConstant.CONF_STATUS_SUCCESS,
				ConfConstant.CONF_STATUS_OPENING
		};
		
		try {
			confs = libernate.getEntityListBase(ConfBase.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据cycleId号获取会议信息出错！",e);
		}
		return confs;
	}

	@Override
	public List<ConfBase> getConfListForTask() {
		List<ConfBase> confList=null;
		Date nowGmtDate=DateUtil.getGmtDate(null);
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append("SELECT  *  FROM t_conf_base tcb ");
		sqlBuffer.append("	WHERE");
		sqlBuffer.append("		tcb.`del_flag` = 1 ");
		sqlBuffer.append("		and (");
		sqlBuffer.append("				tcb.`conf_status` = 2 ");
		sqlBuffer.append("				or ");
		sqlBuffer.append("				(");
		sqlBuffer.append("					tcb.`conf_status` = 1 ");
		sqlBuffer.append("				AND DATE_ADD( tcb.`start_time`, INTERVAL - 1 * tcb.`ahead_time` MINUTE) <= ?");
		sqlBuffer.append("				)");
		sqlBuffer.append("		)");
		sqlBuffer.append("");
		Object[] values=new Object[]{nowGmtDate};
		try {
			confList=libernate.getEntityListBase(ConfBase.class, sqlBuffer.toString(), values);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return confList;
	}

	@Override
	public boolean updateStartTime(ConfBase confBase, Date startTime) {
		if(confBase!=null){
			if(startTime==null){
				startTime=DateUtil.getGmtDate(null);
			}
			confBase.setStartTime(startTime);
			try {
				confBase=libernate.updateEntity(confBase, "id","startTime");
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			//更新 Conf user表记录
			confUserService.updateStartTime(confBase.getId(), startTime);
		}
		return true;
	}

	@Override
	public boolean updateEndTime(ConfBase confBase, Date endTime) {
		if(confBase!=null){
			if(endTime==null){
				endTime=DateUtil.getGmtDate(null);
			}
			Date startTime=confBase.getStartTime();
			if(startTime.before(endTime)){
				long duration=DateUtil.dateDiff(startTime, endTime);
				confBase.setEndTime(endTime);
				confBase.setDuration(IntegerUtil.parseInteger(""+duration));
				try {
					confBase=libernate.updateEntity(confBase, "id","endTime","duration");
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			startTime=null;
		}
		return true;
	}
	

	@Override
	public ConfBase getConfBaseByHwId(String hwId) {

		if (!StringUtil.isEmpty(hwId)) {
			try {
				return libernate.getEntity(ConfBase.class, "conf_hwid", hwId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 页面高级参数拼接字符串
	 * 1.创建预约会议时拼接高级参数（不包括即时会议）
	 * 2.修改用户会议缺省设置
	 * 
	 * Client功能配置
	 * 共34位，0是不启用，1是启用  
		第2位	文档共享
		第3位	屏幕共享
		第4位	白板
		第5位	笔记
		第6位         投票
		第8位	视频
		第9位	音频
		第10位	聊天
		第11位	公告
		第12位	文件传输
		第13位	录制
		第15位	会议助理
		第16位	私聊
		第17位	组聊
		第22位	媒体共享
	 * wangyong
	 * 2013-3-18
	 */
	@Override
	public String getClientConfig(HttpServletRequest request, DefaultConfig confConfig){
		UserBase currentUser = userService.getCurrentUser(request);
		char[] clientConfig = null;
		char[] defaultClientConfig = "0000000000000000000000000000000000".toCharArray();
		if(confConfig != null){
			clientConfig = confConfig.getClientConfig().toCharArray();    //共34位
			if(clientConfig.length != defaultClientConfig.length){
				clientConfig = defaultClientConfig;
			}
		}else{
			clientConfig = defaultClientConfig;
		}
		confLogic.setServerClientConfig(clientConfig);    //后台配置客户端功能缺省设置，与前台配置无关,设置默认功能开启
		String shareDocs = request.getParameter("shareDocs");    			//文档共享(0 : 关闭; 1 : 打开)
		String shareScreen = request.getParameter("shareScreen");    		//屏幕共享(0 : 关闭; 1 : 打开)
		String shareMedia = request.getParameter("shareMedia");    			//媒体共享(0 : 关闭; 1 : 打开)
		String whiteBoard = request.getParameter("whiteBoard");   			//白板(0 : 关闭; 1 : 打开)
		String fileTrans = request.getParameter("fileTrans");     			//文件传输 (0 : 关闭; 1 : 打开)
		String record = request.getParameter("record");    					//录制 (0 : 关闭; 1 : 打开)
		String confType = request.getParameter("confType");
		if(!StringUtil.isNotBlank(shareDocs)){
			shareDocs = "0";
		}
		if(!StringUtil.isNotBlank(shareScreen)){
			shareScreen = "0";
		}
		if(!StringUtil.isNotBlank(shareMedia)){
			shareMedia = "0";
		}
		if(!StringUtil.isNotBlank(whiteBoard)){
			whiteBoard = "0";
		}
		if(!StringUtil.isNotBlank(fileTrans)){
			fileTrans = "0";
		}
		if(!StringUtil.isNotBlank(record)){
			record = "0";
		}
		if(!StringUtil.isNotBlank(confType)){
			confType = "0";
		}
		//根据站点ID号取站点的授权配置
		EmpowerConfig sitePower = empowerConfigService.getSiteEmpowerConfigBySiteId(currentUser.getSiteId());
		if(sitePower == null || sitePower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //只要站点未授权，不管页面传值，全局变量必须设置为禁用
			shareMedia = "0";
		}
		if(sitePower == null || sitePower.getRecordFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //只要站点未授权，不管页面传值，全局变量必须设置为禁用
			record = "0";
		}
		clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_DOCS] = shareDocs.charAt(0);
		clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_SCREEN] = shareScreen.charAt(0);
		clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_MEDIA] = shareMedia.charAt(0);
		clientConfig[ConfConstant.CLIENT_CONFIG_WHITE_BOARD] = whiteBoard.charAt(0);
		clientConfig[ConfConstant.CLIENT_CONFIG_FILE_TRANS] = fileTrans.charAt(0);
		clientConfig[ConfConstant.CLIENT_CONFIG_RECORD] = record.charAt(0);
		clientConfig[ConfConstant.CLIENT_CONFIG_VIDEO] = confType.equalsIgnoreCase("2") || confType.equalsIgnoreCase("3") ? '1' : '0';//TODO:
		return String.valueOf(clientConfig);
	}
	
	/**
	 * 创建预约会议时：拼接会议功能字符串
	 * 会议的功能位由01 字符串组成, 32 位, 每一位表示一个特定的功能
		• 第1 位表示该会议会控时, 是否需要对操作者鉴权
		• 第2 位表示服务器混音
		• 第3 位表示自动外邀
		• 第4 位表示是否录制
		• 第5 位表示标清或高清会议
		• 第6 位表示是否智真会议
		• 第7 位表示是否使用安全会议号
		• 第8 位表示会议模式(0 : 自由模式; 1 : 主持人模式)
		• 第9 位表示是否为安全会议
		• 第10 位表示该会议是否通过用户pin 码入会
		• 第11 位表示麦克风状态是否关闭(0 : 关闭; 1 : 打开)
		• 第12 位表示是否是UC 会议(0: 否; 1: 是)
		• 第13 位表示是否启用会场放音(0: 否; 1: 是)
		• 第14 位表示是否启用录制会场录音(0: 否; 1: 是)
		• 第15 位表示创建语音会议时是否需要预留多媒体资源(0: 否;1: 是)
		• 第16 位表示创建会议时IVR 的放音类型(0: 标识播放预配置的语音; 1: 标识静默方式，即不放音)
		• 第17 位表示是否启用入会过滤功能(0：否，1：是)
		• 第18 位表示是否支持超大会场(0：否，1：是)
		• 第19 位表示预约会议时是否屏蔽创建会场(0：不屏蔽；1：屏蔽)
		• 第20 位表示该预约会议是否加密会议(0：非加密会议；1：加密会议)
		• 第21 位表示是否勾选Internet Lock-Out Controls(0 表示禁用涉外会议,Internet 用户可接入
		. 其他用0 补全
	 * wangyong
	 * 2013-3-18
	 */
	@Override
	public String getFuncBits(ConfBase conf, HttpServletRequest request, DefaultConfig confConfig){
		UserBase currentUser = userService.getCurrentUser(request);
		char[] funcBits = null;
		char[] defaultFuncBites = "00000000000000000000000000000000".toCharArray();
		if(confConfig != null){
			funcBits = confConfig.getFuncBits().toCharArray();
			if(funcBits.length != defaultFuncBites.length){
				funcBits = defaultFuncBites;
			}
		}else{
			funcBits = defaultFuncBites;
		}
		String allowCall = request.getParameter("allowCall");    //第3 位表示自动外邀(0 : 关闭; 1 : 打开)
		String confModel = request.getParameter("confModel");    //第8 位会议模式（0 : 自由模式; 1 : 主持人模式）
		String micStatus = request.getParameter("micStatus");    //第11 位表示麦克风状态是否关闭(0 : 关闭; 1 : 打开)
		if(!StringUtil.isNotBlank(allowCall)){
			allowCall = "0";
		}
		if(!StringUtil.isNotBlank(confModel)){
			confModel = "0";
		}
		if(!StringUtil.isNotBlank(micStatus)){
			micStatus = "0";
		}
		//根据站点ID号取站点的授权配置
		EmpowerConfig sitePower = empowerConfigService.getSiteEmpowerConfigBySiteId(currentUser.getSiteId());
		if(sitePower == null || sitePower.getAutoFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //只要站点未授权，不管页面传值，全局变量必须设置为禁用
			allowCall = "0";
		}else if(conf != null && conf.getConfType() != null && conf.getConfType().intValue() == ConfConstant.CONF_TYPE_VIDEO_FUNC){    //创建会议时只要未选择电话功能，不管页面传值，全局变量必须设置为禁用
			allowCall = "0";
		}
		funcBits[ConfConstant.FUNCBITS_CONFIG_AUDIO_MIX] = ConfConstant.AUDIO_SERVER_MIX ? '1' : '0';
		funcBits[ConfConstant.FUNCBITS_CONFIG_AUTO_INVITE] = allowCall.charAt(0);
		funcBits[ConfConstant.FUNCBITS_CONFIG_CONF_MODEL] = confModel.charAt(0);
		funcBits[ConfConstant.FUNCBITS_CONFIG_MIC_STATUS] = micStatus.charAt(0);
		funcBits[ConfConstant.FUNCBITS_CONFIG_SUPER_MEETING] = ConfConstant.HUGE_MEETING_ENABLE ? '1' : '0';//超大会场默认都关闭
		return String.valueOf(funcBits);
	}
	
	/**
	 * (创建会议或修改会议时)检查是否授权站点电话功能
	 * 若未授权站点电话功能，则去掉电话功能
	 * wangyong
	 * 2013-3-28
	 */
	@Override
	public ConfBase checkConfFunc(ConfBase conf, UserBase currentUser){
		EmpowerConfig sitePower = empowerConfigService.makeEmpowerForConf(currentUser);   //获取用户创建会议，缺省会议设置的权限
//		EmpowerConfig sitePower = empowerConfigService.getSiteEmpowerConfigBySiteId(currentUser.getSiteId());
		if(sitePower == null || sitePower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //检查是否授权站点电话功能
			if(conf.getConfType().intValue() == ConfConstant.CONF_TYPE_PHONE_FUNC){
				conf.setConfType(ConfConstant.CONF_TYPE_NULL_FUNC);
			}
			if(conf.getConfType().intValue() == ConfConstant.CONF_TYPE_PHONE_VIDEO_FUNC){
				conf.setConfType(ConfConstant.CONF_TYPE_VIDEO_FUNC);
			}
		}
		return conf;
	}
	
	/**
	 * 拼接参会人权限字符串
	 * 权限配置管理：共32位，（1：使用，0：不使用）
	 *  第1位：换页 
	 *  第2位：批注
	 *  第3位：与所有人聊天
	 *  第4位：与主持人聊天
	 *  第5位：与参会人聊天 
	 *  其它全为0
	 * wangyong
	 * 2013-3-18
	 */
	@Override
	public String getPriviBits(HttpServletRequest request, DefaultConfig confConfig){
		char[] priviBits = null;
		char[] defaultPriviBits = "00000000000000000000000000000000".toCharArray();
		if(confConfig != null){
			priviBits = confConfig.getPriviBits().toCharArray();    //共32位
			if(priviBits.length != defaultPriviBits.length){
				priviBits = defaultPriviBits;
			}
		}else{
			priviBits = defaultPriviBits;  
		}
		String changePage = request.getParameter("changePage");    				//换页(0 : 关闭; 1 : 打开)
		String annotate = request.getParameter("annotate");    					//批注(0 : 关闭; 1 : 打开)
		String chatAnyOne = request.getParameter("chatAnyOne");    				//与所有人聊天(0 : 关闭; 1 : 打开)
		String chatCompere = request.getParameter("chatCompere");   			//与主持人聊天(0 : 关闭; 1 : 打开)
		String chatParticipants = request.getParameter("chatParticipants");     //与参会人聊天 (0 : 关闭; 1 : 打开)
		if(!StringUtil.isNotBlank(changePage)){
			changePage = "0";
		}
		if(!StringUtil.isNotBlank(annotate)){
			annotate = "0";
		}
		if(!StringUtil.isNotBlank(chatAnyOne)){
			chatAnyOne = "0";
		}
		if(!StringUtil.isNotBlank(chatCompere)){
			chatCompere = "0";
		}
		if(!StringUtil.isNotBlank(chatParticipants)){
			chatParticipants = "0";
		}
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHANGEPAGE] = changePage.charAt(0);
		priviBits[ConfConstant.PRIVIBITS_CONFIG_ANNOTATE] = annotate.charAt(0);
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHAT_ANYONE] = chatAnyOne.charAt(0);
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHAT_COMPERE] = chatCompere.charAt(0);
		priviBits[ConfConstant.PRIVIBITS_CONFIG_CHAT_PARTICIPANTS] = chatParticipants.charAt(0);
		return String.valueOf(priviBits);
	}
	
	
	/**
	 * 封装会议权限状态到request对象返回到jsp
	 * wangyong
	 * 2013-4-17
	 */
	public HttpServletRequest getConfFuncFlag(UserBase currentUser, HttpServletRequest request){
		//检查用户站点是否有功能权限，站点全局设置的功能是否启用，用户的功能权限是否启用
		EmpowerConfig empowerConfig = empowerConfigService.makeEmpowerForConf(currentUser);
		String[][] fieldArray=SiteConstant.EMPOWER_CODE_FIELD_ARRAY;
		if(fieldArray!=null && fieldArray.length > 0){
			Integer eachValue=null;
			for(String[] field:fieldArray){
				if(field!=null && field.length > 0){
					eachValue=(Integer)ObjectUtil.getFieldValue(empowerConfig, field[1]);
					if(SiteConstant.EMPOWER_ENABLED.equals(eachValue)){
						request.setAttribute("is"+StringUtil.firstUpper(field[1]), eachValue);
					}
				}
			}
		}
		return request;
	}
	
	/**
	 * 封装会议参数设置到request对象返回到jsp
	 * wangyong
	 * 2013-3-21
	 */
	@Override
	public HttpServletRequest confConfigAttr(DefaultConfig confConfig, HttpServletRequest request){
		String funcBits = confConfig.getFuncBits();
		String allowCall = String.valueOf(funcBits.charAt(ConfConstant.FUNCBITS_CONFIG_AUTO_INVITE));    //第3 位表示自动外邀(0 : 关闭; 1 : 打开)
		String confModel = String.valueOf(funcBits.charAt(ConfConstant.FUNCBITS_CONFIG_CONF_MODEL));   //第8 位会议模式（0 : 自由模式; 1 : 主持人模式）
		String micStatus = String.valueOf(funcBits.charAt(ConfConstant.FUNCBITS_CONFIG_MIC_STATUS));    //第11 位表示麦克风状态是否关闭(0 : 关闭; 1 : 打开)
		request.setAttribute("allowCall", allowCall);
		request.setAttribute("confModel", confModel);
		request.setAttribute("micStatus", micStatus);
		
		String clientConfig = confConfig.getClientConfig();
		String shareDocs = String.valueOf(clientConfig.charAt(ConfConstant.CLIENT_CONFIG_SHARE_DOCS));    			//文档共享(0 : 关闭; 1 : 打开)
		String shareScreen = String.valueOf(clientConfig.charAt(ConfConstant.CLIENT_CONFIG_SHARE_SCREEN));   		//屏幕共享(0 : 关闭; 1 : 打开)
		String shareMedia = String.valueOf(clientConfig.charAt(ConfConstant.CLIENT_CONFIG_SHARE_MEDIA));   			//媒体共享(0 : 关闭; 1 : 打开)
		String whiteBoard = String.valueOf(clientConfig.charAt(ConfConstant.CLIENT_CONFIG_WHITE_BOARD));   			//白板(0 : 关闭; 1 : 打开)
		String fileTrans = String.valueOf(clientConfig.charAt(ConfConstant.CLIENT_CONFIG_FILE_TRANS));     			//文件传输 (0 : 关闭; 1 : 打开)
		String record = String.valueOf(clientConfig.charAt(ConfConstant.CLIENT_CONFIG_RECORD));    					//录制 (0 : 关闭; 1 : 打开)
		request.setAttribute("shareDocs", shareDocs);
		request.setAttribute("shareScreen", shareScreen);
		request.setAttribute("shareMedia", shareMedia);
		request.setAttribute("whiteBoard", whiteBoard);
		request.setAttribute("fileTrans", fileTrans);
		request.setAttribute("record", record);
		
		String priviBits = confConfig.getPriviBits();
		String changePage = String.valueOf(priviBits.charAt(ConfConstant.PRIVIBITS_CONFIG_CHANGEPAGE));    				//换页(0 : 关闭; 1 : 打开)
		String annotate = String.valueOf(priviBits.charAt(ConfConstant.PRIVIBITS_CONFIG_ANNOTATE));    					//批注(0 : 关闭; 1 : 打开)
		String chatAnyOne = String.valueOf(priviBits.charAt(ConfConstant.PRIVIBITS_CONFIG_CHAT_ANYONE));    			//与所有人聊天(0 : 关闭; 1 : 打开)
		String chatCompere = String.valueOf(priviBits.charAt(ConfConstant.PRIVIBITS_CONFIG_CHAT_COMPERE));   			//与主持人聊天(0 : 关闭; 1 : 打开)
		String chatParticipants = String.valueOf(priviBits.charAt(ConfConstant.PRIVIBITS_CONFIG_CHAT_PARTICIPANTS));    //与参会人聊天 (0 : 关闭; 1 : 打开)

		request.setAttribute("changePage", changePage);
		request.setAttribute("annotate", annotate);
		request.setAttribute("chatAnyOne", chatAnyOne);
		request.setAttribute("chatCompere", chatCompere);
		request.setAttribute("chatParticipants", chatParticipants);
		return request;
	}
	
	private List<ConfLog> getLogListFromSoap(Integer confId,Integer siteId,ESpaceMeetingAsSoapUserStatus[] soapUserArray){
		List<ConfLog> logList=null;
		if(soapUserArray != null && soapUserArray.length > 0){
			logList=new ArrayList<ConfLog>();
			ConfLog eachLog=null;
			String eachUri="";
			for(ESpaceMeetingAsSoapUserStatus eachSoapuser:soapUserArray){
				logger.info(" eachAS Soap Log Info:" + eachSoapuser);
				if(eachSoapuser!=null){
					eachLog=new ConfLog();
					eachLog.setSiteId(siteId);
					eachLog.setConfId(confId);
					eachLog.setUserId(IntegerUtil.parseInteger(eachSoapuser.getUserId()));
					eachLog.setUserName(eachSoapuser.getUserName());
					eachLog.setUserRole(eachSoapuser.getRole());
					eachLog.setTermType(eachSoapuser.getTermType());
					eachLog.setJoinType(eachSoapuser.getJoinType());
					eachLog.setLeaveType(eachSoapuser.getLeaveType());
					eachLog.setJoinTime(DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(eachSoapuser.getJoinDatetime(),"yyyy-MM-dd HH:mm:ss"),28800000));
					eachLog.setExitTime(DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(eachSoapuser.getLeaveDatetime(),"yyyy-MM-dd HH:mm:ss"),28800000));
					eachUri=eachSoapuser.getUri();
					eachLog.setPhone("");
					eachLog.setEmail("");
					if(!StringUtil.isEmpty(eachUri)){
						eachUri=eachUri.trim().toLowerCase();
						if(eachUri.indexOf("tel:")>-1){
							eachLog.setPhone(eachUri.substring(4));
						}
						if(eachUri.indexOf("sip:")>-1){
							eachLog.setEmail(eachUri.substring(4));
						}
					}
					logList.add(eachLog);
				}
				
			}
		}
		return logList;
		
		
	}

	/**
	 * 创建(修改)会议初始化会议对象时，检查用户功能权限，关闭没有权限的功能
	 * wangyong
	 * 2013-4-25
	 */
	private void checkFuncEmpower(ConfBase confBase, UserBase user){
		EmpowerConfig userEmpower = empowerConfigService.makeEmpowerForConf(user);   //获取用户创建会议，缺省会议设置的权限
		char[] clientConfig = confBase.getClientConfig().toCharArray();
		clientConfig[ConfConstant.CLIENT_CONFIG_VOTE] = '1';            //投票(问卷调查)默认开启
		char[] funcBits = confBase.getFuncBits().toCharArray();
		funcBits[ConfConstant.FUNCBITS_CONFIG_SUPER_MEETING] = ConfConstant.HUGE_MEETING_ENABLE ? '1' : '0';     //超大会场默认全部关闭，创建即时会议时，不以用户的会议缺省设置
		funcBits[ConfConstant.FUNCBITS_CONFIG_AUDIO_MIX] = ConfConstant.AUDIO_SERVER_MIX ? '1' : '0';
		if(userEmpower.getAutoFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
			funcBits[ConfConstant.FUNCBITS_CONFIG_AUTO_INVITE] = '0';
		}
		if(userEmpower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
			clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_MEDIA] = '0';
		}
		if(userEmpower.getRecordFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
			clientConfig[ConfConstant.CLIENT_CONFIG_RECORD] = '0';
		}
		if(userEmpower.getVideoFlag().intValue() == SiteConstant.EMPOWER_DISABLED || userEmpower.getVideoNumber().intValue() == 0 || confBase.getMaxVideo().intValue() == 0){
			clientConfig[ConfConstant.CLIENT_CONFIG_VIDEO] = '0';
			confBase.setMaxVideo(0);
		}else if(userEmpower.getVideoNumber().intValue() < confBase.getMaxVideo().intValue()){
			confBase.setMaxVideo(userEmpower.getVideoNumber());
		}
		
		if(userEmpower.getAudioFlag().intValue() == SiteConstant.EMPOWER_DISABLED || userEmpower.getAudioNumber().intValue() == 0 || confBase.getMaxAudio().intValue() == 0){
			clientConfig[ConfConstant.CLIENT_CONFIG_AUDIO] = '0';
			confBase.setMaxAudio(0);
		}else if(userEmpower.getAudioNumber().intValue() < confBase.getMaxAudio().intValue()){
			confBase.setMaxAudio(userEmpower.getAudioNumber());
		}
		confBase.setClientConfig(String.valueOf(clientConfig));
		confBase.setFuncBits(String.valueOf(funcBits));
	}
	
//	/**
//	 * 取需要发送会议提醒的所有会议,提前24小时
//	 * @return
//	 */
//	public List<ConfBase> getConfListForRemind(Integer minutes){
//		List<ConfBase>  confList=null;
//		if(minutes==null || minutes.intValue()<=0){
//			minutes=ConfConstant.CONF_REMIND_MINUTES;
//		}
////		Date nowGmtDate=DateUtil.getGmtDate(null);
//		Date fromDateTime=DateUtil.getGmtDateByBeforeMinute(minutes);
//		Date toDateTime=DateUtil.getGmtDateByBeforeMinute(minutes-60);
//		
//		StringBuffer sqlBuffer=new StringBuffer();
//		sqlBuffer.append("select * from t_conf_base ");
//		sqlBuffer.append("	where 1=1 and AND del_flag = ?  ");
//		sqlBuffer.append("		AND conf_status = ? ");
//		sqlBuffer.append("		AND start_time >=? ");
//		sqlBuffer.append("		AND start_time <=? ");
//		Object[] values=new Object[]{
//					ConstantUtil.DELFLAG_UNDELETE,
//					ConfConstant.CONF_STATUS_SUCCESS,
//					fromDateTime,
//					toDateTime
//				};
//		logger.error("sqlBuffer.toString()=="+sqlBuffer.toString());
//		try {
//			confList = libernate.getEntityListBase(ConfBase.class, sqlBuffer.toString(), values);
//		} catch (SQLException e) {
//			logger.error("根据cycleId号获取会议信息出错！",e);
//		}
//		sqlBuffer=null;
//		values=null;
//		fromDateTime=null;
//		toDateTime=null;
//		
//		return confList;
//	}

	public static void main(String[] args) throws Exception {
//		ConfServiceImpl service = new ConfServiceImpl();
//		for (int i = 0; i < 10; i++) {
//			ConfBase conf = new ConfBase();
//			conf.setConfName("a上海会畅BY测试程序" + System.currentTimeMillis());
//			conf.setConfDesc("");
//			conf.setStartTime(DateUtil.getGmtDateByAfterHour(-1));
//			conf.setDuration(120);
//			conf.setCompereUser(1);
//			conf.setCompereName("testuser");
//			conf.setCompereSecure("1234");
//			conf.setUserSecure("12345");
//			conf.setCreateUser(1);
//			conf.setDelTime(DateUtil.getGmtDate(null));
//			conf.setConfStatus(ConfStatus.SCHEDULED.getStatus());
//			conf.setConfHwid(System.currentTimeMillis() + "");
//
//		//	service.createConf(conf);
//			System.out.println("create ok");
//			Thread.sleep(2);
//		}
//
//		List<ConfBase> confList = service
//				.getListForStatusMonitor(3 * 3600 * 1000L);
//		System.out.println(confList.size());
	}
	
	/**
	 *  查询进行中的会议和已经结束会议参会人信息
	 */
	@Override
	public PageBean<ConfLog> getConflogsByConf(Integer confId,Integer pageNo,Integer pageSize) {
		String sql = "select * from t_conf_log where conf_id = ?";
		return getPageBeans(ConfLog.class, sql, pageNo, pageSize, new Object[]{confId});
	}
	
	@Override
	public int getTerminalNum(Integer confId,Integer terminalType) {
		
		List<Object> values = new ArrayList<Object>();
//		String sql = "select count(distinct user_name) from t_conf_log where conf_id = ? "; //去重
		String sql = "select count(*) from t_conf_log where conf_id = ? ";
		int count = 0;
		try{
			if(confId == null){
				throw new RuntimeException("the confId can not be null!");
			}
			values.add(confId);
			if(terminalType!=null && terminalType.intValue()>0){
				sql +=  " and term_type=? ";
				values.add(terminalType);
			}
			count = libernate.countEntityListWithSql(sql, values.toArray());
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public Map<Integer, Integer> getConfsTerminalNums(
			Collection<ConfBase> confs, Integer terminalType) {

		Map<Integer, Integer> datas = new HashMap<Integer, Integer>();
		if(confs!=null && confs.size()>0){
			for (Iterator<ConfBase> itr = confs.iterator(); itr.hasNext();) {
				ConfBase conf =  itr.next();
				datas.put(conf.getId(), getTerminalNum(conf.getId(),terminalType));
			}
		}
		return datas;
	}

	
	
	@Override
	public PageBean<ConfBase> getDailyOpeningConfListForHost(String confName, UserBase userBase,Integer pageNo) {
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_OPENING, null, null, pageNo, false);
	}

	@Override
	public PageBean<ConfBase> getDailyComingConfListForHost(String confName, UserBase userBase,Integer pageNo) {
		Date startDate = DateUtil.getTodayStartDate(userBase.getTimeZone());
		Date endDate = DateUtil.getTodayEndDate(startDate);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_SUCCESS, startDate, endDate, pageNo, false);
	}

	@Override
	public PageBean<ConfBase> getDailyJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo) {
		
		Date startDate = DateUtil.getTodayStartDate(userBase.getTimeZone());
		Date endDate = DateUtil.getTodayEndDate(startDate);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_FINISHED, startDate, endDate, pageNo, true);
	}

	@Override
	public PageBean<ConfBase> getWeeklyComingConfListForHost(String confName, UserBase userBase,Integer pageNo) {
		
		Date startDate = DateUtil.getWeekStartDate(userBase.getTimeZone());
		Date endDate = DateUtil.getWeekEndDate(startDate);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_SUCCESS, startDate, endDate, pageNo, false);
	}
	
	@Override
	public PageBean<ConfBase> getWeeklyJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo) {
		Date startDate = DateUtil.getWeekStartDate(userBase.getTimeZone());
		Date endDate = DateUtil.getWeekEndDate(startDate);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_FINISHED, startDate, endDate, pageNo, true);
	}
	
	
	@Override
	public PageBean<ConfBase> getMonthlyComingConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime) {
		Date monthBeginTime = DateUtil.getMonthStartDate(userBase.getTimeZone());
		Date monthEndTime = DateUtil.getMonthEndDate(monthBeginTime);
		//如果没有传入时间，或者传入时间范围超出本月
		if(beginTime==null || beginTime.before(monthBeginTime)){
			beginTime = monthBeginTime;
		}
		if(endTime==null || endTime.after(monthEndTime)){
			endTime = monthEndTime;
		}
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_SUCCESS, beginTime, endTime, pageNo, false);
	}
	
	@Override
	public PageBean<ConfBase> getMonthlyJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime) {
		Date monthBeginTime = DateUtil.getMonthStartDate(userBase.getTimeZone());
		Date monthEndTime = DateUtil.getMonthEndDate(monthBeginTime);
		//如果没有传入时间，或者传入时间范围超出本月
		if(beginTime==null || beginTime.before(monthBeginTime)){
			beginTime = monthBeginTime;
		}
		if(endTime==null || endTime.after(monthEndTime)){
			endTime = monthEndTime;
		}

		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_FINISHED, beginTime, endTime, pageNo, true);
	}
	
	@Override
	public PageBean<ConfBase> getFullOpeningConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime) {
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_OPENING, beginTime, endTime, pageNo, false);
	}
	
	@Override
	public PageBean<ConfBase> getFullComingConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime) {
		// TODO Auto-generated method stub
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_SUCCESS, beginTime, endTime, pageNo, false);
	}
	
	@Override
	public PageBean<ConfBase> getFullJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime) {
		// TODO Auto-generated method stub
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_HOST, ConfConstant.CONF_STATUS_FINISHED, beginTime, endTime, pageNo, true);
	}

	
	
	
	
	/**
	 * 我参与的会议
	 * 获取今天正在召开的会议列表
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getDailyOpeningConfListForActor(String confName, UserBase userBase, int pageNo) {
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_OPENING, null, null, pageNo, false);
	}

	/**
	 * 我参与的会议
	 * 获取今天即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getDailyComingConfListForActor(String confName, UserBase userBase, int pageNo) {
		Date beginTime = DateUtil.getTodayStartDate(userBase.getTimeZone());
		Date endTime = DateUtil.getTodayEndDate(beginTime);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_SUCCESS, beginTime, endTime, pageNo, false);
	}

	/**
	 * 我参与的会议
	 * 获取今天已经加入过的的会议列表
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getDailyJoinedConfListForActor(String confName, UserBase userBase, int pageNo) {
		Date beginTime = DateUtil.getTodayStartDate(userBase.getTimeZone());
		Date endTime = DateUtil.getTodayEndDate(beginTime);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_FINISHED, beginTime, endTime, pageNo, true);
	}

	/**
	 * 我参与的会议
	 * 获取本周的正在召开的会议
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getWeeklyOpeningConfListForActor(String confName, UserBase userBase, int pageNo) {
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_OPENING, null, null, pageNo, false);
	}

	/**
	 * 我参与的会议
	 * 获取本周的即将开始的会议
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getWeeklyComingConfListForActor(String confName, UserBase userBase, int pageNo) {
		Date beginTime = DateUtil.getWeekStartDate(userBase.getTimeZone());
		Date endTime = DateUtil.getWeekEndDate(beginTime);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_SUCCESS, beginTime, endTime, pageNo, false);
	}

	/**
	 * 我参与的会议
	 * 获取本周的已经加入过的会议
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getWeeklyJoinedConfListForActor(String confName, UserBase userBase, int pageNo) {
		Date beginTime = DateUtil.getWeekStartDate(userBase.getTimeZone());
		Date endTime = DateUtil.getWeekEndDate(beginTime);
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_FINISHED, beginTime, endTime, pageNo, true);
	}


	/**
	 * 我参与的会议
	 * 取本月的即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getMonthlyComingConfListForActor(String confName, UserBase userBase, int pageNo,Date beginTime, Date endTime) {
		
		Date monthBeginTime = DateUtil.getMonthStartDate(userBase.getTimeZone());
		Date monthEndTime = DateUtil.getMonthEndDate(monthBeginTime);
		//如果没有传入时间，或者传入时间范围超出本月
		if(beginTime==null || beginTime.before(monthBeginTime)){
			beginTime = monthBeginTime;
		}
		if(endTime==null || endTime.after(monthEndTime)){
			endTime = monthEndTime;
		}
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_SUCCESS, beginTime, endTime, pageNo, false);
	}

	/**
	 * 我参与的会议
	 * 取本月的已经加入过的会议列表
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getMonthlyJoinedConfListForActor(String confName, UserBase userBase, int pageNo,Date beginTime, Date endTime) {
		Date monthBeginTime = DateUtil.getMonthStartDate(userBase.getTimeZone());
		Date monthEndTime = DateUtil.getMonthEndDate(monthBeginTime);
		//如果没有传入时间，或者传入时间范围超出本月
		if(beginTime==null || beginTime.before(monthBeginTime)){
			beginTime = monthBeginTime;
		}
		if(endTime==null || endTime.after(monthEndTime)){
			endTime = monthEndTime;
		}
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_FINISHED, beginTime, endTime, pageNo, true);
	}
	
	
	
	/**
	 * 我参与的会议
	 * 所有的正在开始的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getFullOpeningConfListForActor(String confName, UserBase userBase, int pageNo, Date beginTime, Date endTime){
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_OPENING, beginTime, endTime, pageNo, false);
	}

	/**
	 * 我参与的会议
	 * 所有的即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getFullComingConfListForActor(String confName, UserBase userBase, int pageNo, Date beginTime, Date endTime) {
		
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_SUCCESS, beginTime, endTime, pageNo, false);
		
	}
	
	/**
	 * 我参与的会议
	 * 所有的加入过的会议列表
	 * @param userBase
	 * @return
	 */
	@Override
	public PageBean<ConfBase> getFullJoinedConfListForActor(String confName, UserBase userBase, int pageNo, Date beginTime, Date endTime) {
		
		return getConfListForCurrentUser(confName, userBase,ConfConstant.CONF_USER_PARTICIPANT, ConfConstant.CONF_STATUS_FINISHED, beginTime, endTime, pageNo, true);
		
	}

	private PageBean<ConfBase> getConfListForCurrentUserOld(String confName, UserBase userBase,int hostFlag,int confStatus,Date beginDate,Date endDate,int pageNo,boolean joinFlag){
		
		if(userBase == null){
			return null;
		}
		
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT * FROM t_conf_base tcb WHERE 1=1 AND tcb.conf_status = ? AND tcb.del_flag = ? and tcb.permanent_conf = ?");
		valueList.add(confStatus);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(0);

		sqlBuilder.append(" AND tcb.id in ( ");
		if(ConfConstant.CONF_USER_PARTICIPANT.intValue()==hostFlag){
			sqlBuilder.append("  select id from t_conf_base pub where pub.site_id=? and pub.public_flag=? AND pub.conf_status = ? AND pub.del_flag = ?  and (pub.compere_user>?  or pub.compere_user < ?) ");
			valueList.add(userBase.getSiteId());
			valueList.add(ConfConstant.CONF_PUBLIC_FLAG_TRUE);
			valueList.add(confStatus);
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			valueList.add(userBase.getId());
			valueList.add(userBase.getId());
			if(ConfConstant.CONF_STATUS_SUCCESS.intValue()==confStatus){
				if(beginDate != null){
					sqlBuilder.append(" AND pub.start_time >= ?");
					valueList.add(beginDate);
				}
				if(endDate != null){
						sqlBuilder.append(" AND pub.start_time < ?");
					valueList.add(endDate);
				}
			}
			if(ConfConstant.CONF_STATUS_FINISHED.intValue()==confStatus){
				if(beginDate != null){
					sqlBuilder.append(" AND pub.end_time >= ?");
					valueList.add(beginDate);
				}
				if(endDate != null){
						sqlBuilder.append(" AND pub.end_time < ?");
					valueList.add(endDate);
				}
			}
			sqlBuilder.append(" UNION ALL  ");
		}
		if(joinFlag){
			sqlBuilder.append(" 	SELECT tcu.conf_id FROM t_conf_user tcu, t_conf_log tcl  ");
			sqlBuilder.append("		WHERE tcu.conf_id = tcb.id AND tcu.host_flag = ?  ");
			sqlBuilder.append(" 	AND tcu.conf_id = tcl.conf_id ");
			sqlBuilder.append(" 	AND tcb.site_id = ? ");
			if(ConfConstant.CONF_USER_PARTICIPANT.intValue()==hostFlag){
				sqlBuilder.append(" 	AND  tcu.user_id = ? ");
				sqlBuilder.append(" 	AND tcu.user_id = tcl.user_id ");
			}
			if(ConfConstant.CONF_USER_HOST.intValue()==hostFlag){
				sqlBuilder.append(" 	and create_user=?   ");
				sqlBuilder.append(" 	and  tcu.create_user = tcl.user_id ");
			}
			
			valueList.add(hostFlag);    
			valueList.add(userBase.getSiteId());
			valueList.add(userBase.getId());
			
		}else{
			sqlBuilder.append(" 	SELECT tcu.conf_id FROM t_conf_user tcu WHERE tcu.conf_id = tcb.id AND tcu.host_flag = ?  ");
			sqlBuilder.append(" 	AND tcb.site_id = ? ");
			valueList.add(hostFlag);   
			valueList.add(userBase.getSiteId());
			if(ConfConstant.CONF_USER_PARTICIPANT.intValue()==hostFlag){
				sqlBuilder.append("   and  tcu.user_id = ?");
			}
			if(ConfConstant.CONF_USER_HOST.intValue()==hostFlag){
				sqlBuilder.append(" and  create_user=?");
			}
			
			valueList.add(userBase.getId());
			
		}
		
		if(ConfConstant.CONF_STATUS_SUCCESS.intValue()==confStatus){
			if(beginDate != null){
				sqlBuilder.append(" AND tcb.start_time >= ?");
				valueList.add(beginDate);
			}
			if(endDate != null){
					sqlBuilder.append(" AND tcb.start_time < ?");
				valueList.add(endDate);
			}
		}
		if(ConfConstant.CONF_STATUS_FINISHED.intValue()==confStatus){
			if(beginDate != null){
				sqlBuilder.append(" AND tcb.end_time >= ?");
				valueList.add(beginDate);
			}
			if(endDate != null){
					sqlBuilder.append(" AND tcb.end_time < ?");
				valueList.add(endDate);
			}
		}
		sqlBuilder.append(" )");
		if(StringUtil.isNotBlank(confName)){
			sqlBuilder.append(" AND tcb.conf_name LIKE ? ");
			valueList.add("%" + confName.trim() + "%");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(ConfConstant.CONF_USER_HOST.intValue()!=hostFlag){
			sqlBuilder.append(" or (tcb.permanent_conf = 1 and tcb.public_flag = ? and tcb.conf_status = ? and tcb.compere_user !=? and tcb.site_id = ? and tcb.end_time > ?)  or (tcb.permanent_conf = 1 and tcb.public_flag = ? and tcb.conf_status = ? and tcb.end_time > ? and EXISTS(SELECT tcu.conf_id FROM t_conf_user tcu where tcu.conf_id = tcb.id AND tcu.host_flag = ? AND  tcu.user_id = ?))");
			valueList.add(ConfConstant.CONF_PUBLIC_FLAG_TRUE);
			valueList.add(confStatus);
			valueList.add(userBase.getId());
			valueList.add(userBase.getSiteId());
			valueList.add(sdf.format(DateUtil.getGmtDate(null)));
			
			valueList.add(ConfConstant.CONF_PUBLIC_FLAG_FALSE);
			valueList.add(confStatus);
			valueList.add(sdf.format(DateUtil.getGmtDate(null)));
			valueList.add(ConstantUtil.USERROLE_PARTICIPANT);
			
			valueList.add(userBase.getId());
		}else{
			sqlBuilder.append(" or(tcb.permanent_conf = 1 and tcb.conf_status = ? and  tcb.compere_user = ? and tcb.end_time > ?)");
			valueList.add(confStatus);
			valueList.add(userBase.getId());
			valueList.add(sdf.format(DateUtil.getGmtDate(null)));
		}
		
		if(ConfConstant.CONF_STATUS_FINISHED.intValue()==confStatus){
			sqlBuilder.append("  ORDER BY tcb.start_time DESC ");
		}else{
			sqlBuilder.append("  ORDER BY tcb.start_time ASC ");
		}
		

		PageBean<ConfBase> page = getPageBeans(ConfBase.class, sqlBuilder.toString(), pageNo, valueList.toArray());
		if(page != null && page.getDatas() != null && page.getDatas().size() > 0){
			page.setDatas(getOffsetConfList(userBase, page.getDatas()));
		}
//		List<ConfBase> confList= page.getDatas();
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				System.out.println("--confBase->>"+confBase);
//			}
//		}
//		confList=null;
		return page;
	}
	
	
	private PageBean<ConfBase> getConfListForCurrentUser(String confName, UserBase userBase,int hostFlag,int confStatus,
					Date beginDate,Date endDate,int pageNo,boolean joinFlag){

		logger.info("getConfListForCurrentUser:  beginDate-->>"+beginDate + "; endDate-->>"+endDate);
		if(userBase == null){
			return null;
		}
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer sqlBuffer = new StringBuffer();
		if(joinFlag){//查找已经参加的

			if(ConfConstant.CONF_USER_HOST.equals(hostFlag)){//自己主持的会议
				sqlBuffer.append(" SELECT distinct tcb.* FROM t_conf_base tcb ,t_conf_log tcl ");
				sqlBuffer.append(" WHERE tcb.`del_flag`=? AND tcl.`conf_id`=tcb.`id` ");
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);
				sqlBuffer.append(" AND tcb.`conf_status`=? ");
				valueList.add(confStatus);
				sqlBuffer.append(" AND tcb.`compere_user`=tcl.`user_id` ");
				sqlBuffer.append(" AND tcb.`compere_user`=? ");
				valueList.add(userBase.getId());
				sqlBuffer.append(" AND (tcb.`permanent_conf`=? OR tcb.`permanent_conf`=?) ");
				valueList.add(ConfConstant.CONF_PERMANENT_UNABLE);
				valueList.add(ConfConstant.CONF_PERMANENT_ENABLED_CHILD);
				if(!StringUtil.isEmpty(confName)){
					sqlBuffer.append(" AND tcb.conf_name like ?");
					valueList.add("%"+confName.trim()+"%");
				}
				if(beginDate != null){
					sqlBuffer.append(" AND tcb.end_time >= ?");
					valueList.add(beginDate);
				}
				if(endDate != null){
					sqlBuffer.append(" AND tcb.end_time < ?");
					valueList.add(endDate);
				}
				sqlBuffer.append(" ORDER BY tcb.`end_time` DESC");
				
			}else  if (ConfConstant.CONF_USER_PARTICIPANT.equals(hostFlag)) {// 参与的会议的会议
				sqlBuffer.append(" SELECT distinct tcb.* FROM t_conf_base tcb ,t_conf_log tcl ");
				sqlBuffer.append(" WHERE tcb.`del_flag`=? AND tcl.`conf_id`=tcb.`id` ");
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);
				sqlBuffer.append(" AND tcb.`conf_status`=? ");
				valueList.add(confStatus);
				sqlBuffer.append(" AND tcb.`compere_user`!=? ");
				valueList.add(userBase.getId());
				sqlBuffer.append(" AND tcl.user_id=? ");
				valueList.add(userBase.getId());
				sqlBuffer.append(" AND (tcb.`permanent_conf`=? OR tcb.`permanent_conf`=?) ");
				valueList.add(ConfConstant.CONF_PERMANENT_UNABLE);
				valueList.add(ConfConstant.CONF_PERMANENT_ENABLED_CHILD);
				if(!StringUtil.isEmpty(confName)){
					sqlBuffer.append(" AND tcb.conf_name like ?");
					valueList.add("%"+confName.trim()+"%");
				}
				if(beginDate != null){
					sqlBuffer.append(" AND tcb.end_time >= ?");
					valueList.add(beginDate);
				}
				if(endDate != null){
					sqlBuffer.append(" AND tcb.end_time < ?");
					valueList.add(endDate);
				}
				sqlBuffer.append(" ORDER BY tcb.`end_time` DESC");
			}else{
				return null;
			}
			
		}else{//查找预约成功 + 正在召开的会议
			if(ConfConstant.CONF_USER_HOST.equals(hostFlag)){//自己主持的会议

				logger.info("我主持的的会议的confStatus=="+confStatus);
				logger.info("我主持的会议的beginDate=="+beginDate);
				logger.info("我主持的会议的endDate=="+endDate);
				
				sqlBuffer.append(" SELECT tcb.* FROM t_conf_base tcb ");
				sqlBuffer.append(" WHERE tcb.`del_flag`=? ");
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);
				sqlBuffer.append(" AND tcb.`compere_user`=?");
				valueList.add(userBase.getId());
				
				sqlBuffer.append(" AND tcb.`conf_status`=? ");
				valueList.add(confStatus);
				sqlBuffer.append(" AND tcb.`permanent_conf`<=? ");
				valueList.add(ConfConstant.CONF_PERMANENT_ENABLED_MAIN);
				
				
				if(!StringUtil.isEmpty(confName)){
					sqlBuffer.append(" AND tcb.conf_name like ?");
					valueList.add("%"+confName.trim()+"%");
				}
				if(ConfConstant.CONF_STATUS_SUCCESS.equals(confStatus)){//即将开始的会议
					if(beginDate != null){
						sqlBuffer.append(" AND tcb.start_time >= ?");
						valueList.add(beginDate);
					}
					if(endDate != null){
						sqlBuffer.append(" AND tcb.start_time < ?");
						valueList.add(endDate);
					}
				}
				
				sqlBuffer.append(" ORDER BY tcb.`permanent_conf` DESC,tcb.`start_time` ASC ");
				
			} else if (ConfConstant.CONF_USER_PARTICIPANT.equals(hostFlag)) {// 参与的会议的会议

				logger.info("我参与的会议的confStatus=="+confStatus);
				logger.info("我参与的会议的beginDate=="+beginDate);
				logger.info("我参与的会议的endDate=="+endDate);

				sqlBuffer.append(" SELECT DISTINCT tcb.* FROM t_conf_base tcb,t_conf_user tcu  ");
				sqlBuffer.append(" WHERE tcb.id=tcu.`conf_id` AND tcb.`create_user`!=? ");
				sqlBuffer.append(" 		AND tcb.site_id=?");
				sqlBuffer.append("		AND tcb.`del_flag`=?");
				valueList.add(userBase.getId());
				valueList.add(userBase.getSiteId());
				valueList.add(ConstantUtil.DELFLAG_UNDELETE);

				if(!StringUtil.isEmpty(confName)){
					sqlBuffer.append("		AND tcb.conf_name like ?");
					valueList.add("%"+confName.trim()+"%");
				}
				
				
				sqlBuffer.append("		AND ( ");
				sqlBuffer.append("				tcb.`public_flag`=?  ");//公开会议
				sqlBuffer.append("				OR tcb.`public_flag`=?");//所有 同站点注册用户可见
				sqlBuffer.append("				OR (tcb.`public_flag`=? AND tcu.`user_id`=?)");//非公开会议
				sqlBuffer.append("			 )");
				valueList.add(ConfConstant.CONF_PUBLIC_FLAG_TRUE);
				valueList.add(ConfConstant.CONF_PUBLIC_FLAG_USER);
				valueList.add(ConfConstant.CONF_PUBLIC_FLAG_FALSE);
				valueList.add(userBase.getId());
				
				sqlBuffer.append("		AND tcb.`conf_status`=?");
				valueList.add(confStatus);
				sqlBuffer.append("		AND tcb.`permanent_conf`<=?");
				valueList.add(ConfConstant.CONF_PERMANENT_ENABLED_MAIN);
				
				
				if(ConfConstant.CONF_STATUS_SUCCESS.equals(confStatus)){//即将开始
					
					if(beginDate!=null ){
						sqlBuffer.append("		AND tcb.start_time >= ? ");
						valueList.add(beginDate);
					}
					if(endDate!=null){
						sqlBuffer.append("		AND tcb.start_time < ?");
						valueList.add(endDate);
					}
				}
				sqlBuffer.append(" ORDER BY tcb.`permanent_conf` DESC,tcb.`start_time` ASC");
				sqlBuffer.append("");

			}else{
				return null;
			}
		}
		logger.info("sqlBuffer=="+sqlBuffer.toString());
		
//		PageBean<ConfBase> page = getPageBeans(ConfBase.class, sqlBuffer.toString(), pageNo, valueList.toArray());
		PageBean<ConfBase> page = getPageBeans(ConfBase.class, sqlBuffer.toString(), pageNo,  userBase.getPageSize(), valueList.toArray());    //按用户偏好设置每页显示条数
		if(page != null && page.getDatas() != null && page.getDatas().size() > 0){
			page.setDatas(getOffsetConfList(userBase, page.getDatas()));
		}
		return page;
	}
	
	
	@Override
	public PageBean<ConfBase> getConfBasePage(int pageNo, int pageSize,UserBase user,Date startTime,Date endTime,String theme,
			boolean isCreator) {
		
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT * FROM t_conf_base tcb WHERE 1=1 AND tcb.conf_status = ? AND tcb.del_flag = ? and tcb.permanent_conf != ? ");
		valueList.add(ConfConstant.CONF_STATUS_FINISHED);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(ConfConstant.CONF_PERMANENT_ENABLED_MAIN);//不显示主会议
		
		if(startTime!=null && endTime!=null){
			sqlBuilder.append(" and tcb.start_time between ? and ? ");
			valueList.add(startTime);
			valueList.add(endTime);
		}
		
		if(theme!=null){
			sqlBuilder.append(" and tcb.conf_name like ? ");
			valueList.add("%"+theme+"%");
		}
		
		if(!isCreator){
			sqlBuilder.append(" and tcb.compere_user != ? ");
			valueList.add(user.getId());
			sqlBuilder.append(" AND EXISTS ( ");
			sqlBuilder.append(" 	SELECT * FROM  t_conf_log tcl  ");
			sqlBuilder.append(" 	WHERE tcl.user_role = ? and tcl.user_id =? and  tcb.id = tcl.conf_id )");
			valueList.add(ConfConstant.CONF_USER_PARTICIPANT);   
			valueList.add(user.getId());
		}else{
//			@TODO  if table conf_log 's data  be maintained we can user query sql like that: 
//			sqlBuilder.append(" AND EXISTS ( ");
//			sqlBuilder.append(" 	SELECT * FROM  t_conf_log tcl ");
//			sqlBuilder.append(" 	WHERE tcl.user_id= ? AND tcl.user_role = ?  AND  tcl.conf_id = tcb.id )");
//			valueList.add(user.getId());
//			valueList.add(ConfConstant.CONF_USER_HOST);
			
			//现用
			sqlBuilder.append(" AND  tcb.compere_user = ? ");
			sqlBuilder.append(" AND EXISTS ( ");
			sqlBuilder.append(" 	SELECT * FROM  t_conf_log tcl ");
			sqlBuilder.append(" 	WHERE  tcl.conf_id = tcb.id )");
			valueList.add(user.getId());
		}
		sqlBuilder.append("  ORDER BY tcb.start_time desc ");
		return getPageBeans(ConfBase.class, sqlBuilder.toString(), pageNo, pageSize, valueList.toArray());
	}

	@Override
	public ConfBase createChildConf(ConfBase primaryConf) {
		ConfBase childConf=initPermanentConf(primaryConf);
		if(childConf!=null){
			SiteBase currentsite=siteService.getSiteBaseById(primaryConf.getSiteId());
			UserBase currUserBase=userService.getUserBaseById(primaryConf.getCreateUser());
			String retCode=confManagementService.createConf(childConf, currentsite, currUserBase, true);
			Integer confId=0;
			if(ConstantUtil.AS_SUCCESS_CODE.equals(retCode)){
				childConf.setSoapStatus(ConfConstant.CONF_SOAP_STATUS_TRUE);
				childConf = saveConf(childConf);
				confId = childConf.getId();
			}else{
				logger.info("华为返回错误码：" + retCode);
				return childConf;
			}
			
//			if(confId != null && confId.intValue() > 0){
//	
//			}else{
//				childConf=null;
//			}
		}
		
		return childConf;
	}

	//初始化永久会议子会议信息
	private ConfBase initPermanentConf(ConfBase primaryConf){
		ConfBase childConf=null;
		if(primaryConf!=null && primaryConf.getId()!=null && primaryConf.getId().intValue() > 0){
			childConf=new ConfBase();
			childConf.setSiteId(primaryConf.getSiteId());
			childConf.setCycleId(0);
			childConf.setConfName(primaryConf.getConfName());
			childConf.setConfDesc(primaryConf.getConfDesc());
			childConf.setPublicFlag(primaryConf.getPublicFlag());
			childConf.setPublicConfPass(primaryConf.getPublicConfPass());
			childConf.setConfType(primaryConf.getConfType());
			Date startGmtTime=DateUtil.getGmtDate(null);
			childConf.setStartTime(startGmtTime);
			childConf.setDuration(ConfConstant.CONF_DEFAULT_DURATION);
			Date endGmtTime=DateUtil.addDateMinutes(startGmtTime, ConfConstant.CONF_DEFAULT_DURATION);
			childConf.setEndTime(endGmtTime);
			childConf.setCompereUser(primaryConf.getCompereUser());
			childConf.setCompereName(primaryConf.getCompereName());
			childConf.setCallPhone(primaryConf.getCallPhone());
			childConf.setPhonePass(primaryConf.getPhonePass());
			childConf.setMaxUser(primaryConf.getMaxUser());
			childConf.setMaxAudio(primaryConf.getMaxAudio());
			childConf.setMaxVideo(primaryConf.getMaxVideo());
			childConf.setVideoType(primaryConf.getVideoType());
			childConf.setMaxDpi(primaryConf.getMaxDpi());
			childConf.setDefaultDpi(primaryConf.getDefaultDpi());
			childConf.setAheadTime(primaryConf.getAheadTime());
			childConf.setOpenIpad(primaryConf.getOpenIpad());
			childConf.setFuncBits(primaryConf.getFuncBits());
			childConf.setPriviBits(primaryConf.getPriviBits());
			childConf.setClientConfig(primaryConf.getClientConfig());
			childConf.setConfStatus(ConfConstant.CONF_STATUS_SUCCESS);
			childConf.setSoapStatus(ConfConstant.CONF_SOAP_STATUS_FASLE);
			childConf.setConfVersion(1);
			//childConf.setCreateTime(startGmtTime);
			
			childConf.setCreateUser(primaryConf.getCreateUser());
			childConf.setCreateType(primaryConf.getCreateType());
			childConf.setDelFlag(ConstantUtil.DELFLAG_UNDELETE);
			childConf.setDelTime(startGmtTime);
			childConf.setTimeZone(primaryConf.getTimeZone());
			childConf.setTimeZoneId(primaryConf.getTimeZoneId());
			childConf.setPcNum(0);
			childConf.setPhoneNum(0);
			childConf.setHostKey(primaryConf.getHostKey());
			childConf.setPermanentConf(ConfConstant.CONF_PERMANENT_ENABLED_CHILD);
			childConf.setBelongConfId(primaryConf.getId());
			//compereUser
			
		}
		return childConf;
	}
	@Override
	public ConfBase getPermanentChildConf(int belongConfId) {
		ConfBase childConf=null;
		if(belongConfId > 0){
			StringBuffer sqlBuffer=new StringBuffer();
			sqlBuffer.append("select tcb.* from t_conf_base tcb ");
			sqlBuffer.append(" where tcb.del_flag=? and tcb.permanent_conf=? ");
			sqlBuffer.append("  and belong_confId=? ");
			sqlBuffer.append("  and (tcb.conf_status=? or tcb.conf_status=?) ");
			sqlBuffer.append(" order by conf_status desc,tcb.id desc");
			Object[] values=new Object[]{ConstantUtil.DELFLAG_UNDELETE,
					ConfConstant.CONF_PERMANENT_ENABLED_CHILD,belongConfId,
					ConfConstant.CONF_STATUS_OPENING,
					ConfConstant.CONF_STATUS_SUCCESS};
			try {
				childConf=libernate.getEntityCustomized(ConfBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return childConf;
		
	}
	
	/**
	 * 查询永久会议（主会议）
	 */
	@Override
	public PageBean<ConfBase> getPermanentConfPage(int pageNo, int pageSize,
			Integer siteId, Integer userId,Integer confStatus,boolean publicOnly) {
		
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT * FROM t_conf_base tcb WHERE 1=1 AND tcb.del_flag = ? ");
		valueList.add(ConfConstant.CONF_STATUS_FINISHED);
		if(siteId!=null){
			sqlBuilder.append(" and site_id = ? ");
			valueList.add(siteId);
		}
		if(confStatus!=null){
			sqlBuilder.append(" and  conf_status = ? ");
			valueList.add(confStatus);
		}
		if(userId!= null && userId.intValue()>0){
			sqlBuilder.append(" and compere_user = ? ");
			valueList.add(userId);
		}
		if(publicOnly){
			sqlBuilder.append(" and public_flag = ?");
			valueList.add(ConfConstant.CONF_PUBLIC_FLAG_FALSE);
		}
		sqlBuilder.append("  ORDER BY tcb.start_time desc ");
		return getPageBeans(ConfBase.class, sqlBuilder.toString(), pageNo, valueList.toArray());
	}
	
	
	
	@Override
	public ConfBase createPermanentConf(ConfBase conf,SiteBase site,UserBase user) {
		conf = initConf(conf, site, user, null);
		conf.setCompereSecure(SecureGenerator.getSecureNum());
		conf.setUserSecure(SecureGenerator.getSecureNum());
		if(!checkSiteMaxConfCount(conf, site)){
			conf.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
			logger.info("超过最大并发会议数，创建会议失败");
			return conf;
		}
		try {
			conf = libernate.saveEntity(conf);
			boolean sendEmail = emailService.createConfEmail(conf, null, user);
			if(sendEmail){
				logger.info("create createPermanentConf send email success..");
			}else{
				logger.error("create PermanentConf send email failed! ");
			}
		} catch (Exception e) {
 
			e.printStackTrace();
		}
		return conf;
	}
	
	
	
	/**
	 * 修改永久会议
	 */
	@Override
	public ConfBase updatePermanentConf(ConfBase conf, SiteBase currentSite, UserBase currUser) {
		//2013.7.17 修复bug ，修改会议需检查权限
		checkFuncEmpower(conf, currUser);
		//2013.7.17
		String[] passArray = new String[]{
				"id","confName","confDesc","hostKey","confType", "startTime", "endTime", "maxAudio", "maxVideo", "videoType", "aheadTime", "funcBits", "clientConfig", "publicFlag", "publicConfPass", "privi_bits"
		};
		ConfBase updatedconf = null;
		try {
			if(conf.getEndTime()!=null){
				conf.setEndTime(DateUtil.getGmtDateByTimeZone(conf.getEndTime(), currUser.getTimeZone()));
			}
			if(conf.getStartTime()!=null){
				conf.setStartTime(DateUtil.getGmtDateByTimeZone(conf.getStartTime(), currUser.getTimeZone()));
			}
			//2013.7.23  超过最大并发会议数
			if(!checkSiteMaxConfCount(conf, currentSite)){
				conf.setId(ConfConstant.CONF_CREATE_ERROR_SYNC_LICENCE);    //超过最大并发会议数
				logger.info("超过最大并发会议数，创建会议失败");
				return conf;
			}
			//2013.7.23
			
			
			updatedconf = libernate.updateEntity(conf, passArray);
			
			if(updatedconf!=null){
				List<ConfUser> confUsers = confUserService.getAllConfUserList(updatedconf.getId().intValue());
				boolean sendConfUsers = emailService.confModifyEmail(confUsers, updatedconf);
				if(sendConfUsers){
					logger.info("send update PermanentConf email  ok!");
				}else{
					logger.info("send update PermanentConf email  failed!");
				}
				updatedconf = libernate.getEntity(ConfBase.class, updatedconf.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updatedconf;
	}
	
	//删除永久会议 
	public boolean delPermanentConf(ConfBase conf,SiteBase site,UserBase user){
		boolean flag = true;
		ConfBase subConf = getPermanentChildConf(conf.getId());
		try {
			emailService.confCancelEmail(confUserLogic.getConfUserList(conf.getId()), conf);
			if(subConf!=null){
					if(confManagementService.cancelConf(subConf.getConfHwid(), site, user)){
						libernate.deleteEntity(conf);
					}else{
						logger.debug("delPermanentConf fail because of the child conf is runing!");
						flag = false;
					}
			}else{
				libernate.deleteEntity(conf);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 根据永久会议的ID号，获取所有的子会议的列表
	 * @param confId
	 * @return
	 */
	private List<ConfBase> getChildConfListByBelongConfId(int confId){
		List<ConfBase> childConfList=null;
		if(confId > 0){
			StringBuffer sqlBuffer=new StringBuffer();
			Object[] values=null;
			sqlBuffer.append("	select tcb.* from t_conf_base tcb ");
			sqlBuffer.append("	where tcb.del_flag=? ");
			sqlBuffer.append("		and permanent_conf=?");
			sqlBuffer.append("		and belong_confId=?");
			values=new Object[]{ConstantUtil.DELFLAG_UNDELETE,ConfConstant.CONF_PERMANENT_ENABLED_CHILD,confId};
			logger.info("get child conf --->>>"+sqlBuffer.toString());
			try {
				childConfList=libernate.getEntityListBase(ConfBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return childConfList;
	}
	
	private boolean deleteChildConfByBelongConfId(int confId){
		boolean delStatus=true;
		if(confId > 0){
			StringBuffer sqlBuffer=new StringBuffer();
			Object[] values=null;
			sqlBuffer.append("	update t_conf_base tcb ");
			sqlBuffer.append("	set tcb.del_flag=? ");
			sqlBuffer.append("	where tcb.del_flag=? ");
			sqlBuffer.append("		and permanent_conf=?");
			sqlBuffer.append("		and belong_confId=?");
			values=new Object[]{ConstantUtil.DELFLAG_DELETED,ConstantUtil.DELFLAG_UNDELETE,ConfConstant.CONF_PERMANENT_ENABLED_CHILD,confId};
			logger.info("delete child conf --->>>"+sqlBuffer.toString());
			try {
				libernate.executeSql(sqlBuffer.toString(), values);
			} catch (Exception e) {
				delStatus=false;
				e.printStackTrace();
			}
		}
		return delStatus;
		
	}
	
	
	
	
	/**
	 * 系统管理员根据会议主题、企业名称、企业标识模糊查询所有站点的会议列表
	 * @param TitleOrNameOrSign  会议主题、企业名称、企业标识
	 * wangyong
	 * 2013-5-29
	 */
	@Override
	public PageBean<ConfBase> getSysConfByName(String titleOrNameOrSign, String sortField, String sortord, PageModel pageModel, Integer sysUserId){
		return getSysConf(null, titleOrNameOrSign, sortField, sortord, pageModel, sysUserId);
	}
	
	/**
	 * 系统管理员高级搜索查询会议列表
	 * @param conf 会议名称、会议功能、会议状态等信息
	 * @param siteName 站点名称
	 * @param siteSign 站点标识
	 * @param beginTime 开始时间
	 * @param endTime	结束时间
	 * @param sortField 排序字段
	 * @param sortord 排序方式，正序逆序
	 * @param pageModel 页数信息
	 * @param sysUserId 不为空，则只查出该普通系统管理员创建的站点下的所有会议；为空，则查询所有站点下的会议
	 * wangyong
	 * 2013-5-29
	 */
	@Override
	public PageBean<ConfBase> getSysConfByCondition(ConfBase conf, String siteName, String siteSign, 
			Date beginTime, Date endTime, String sortField, String sortord, PageModel pageModel, Integer sysUserId){
		Condition condition = initCondition(conf, siteName, siteSign, beginTime, endTime);
		return getSysConf(condition, null, sortField, sortord, pageModel, sysUserId);
	}
	
	/**
	 * 站点管理员根据会议主题或会议号模糊查询所有站点的会议列表
	 * @param subject  会议主题或会议号
	 * wangyong
	 * 2013-5-29
	 */
	@Override
	public PageBean<ConfBase> getAdminConfByName(String subject, SiteBase currentSite,  String sortField, String sortord, PageModel pageModel, Integer adminId){
		return getAdminConf(null, subject, currentSite, sortField, sortord, pageModel, adminId);
	}
	
	/**
	 * 站点管理员高级搜索查询会议列表
	 * @param conf 会议名称、会议功能、会议状态等信息
	 * @param siteId 站点id
	 * @param beginTime 开始时间
	 * @param endTime	结束时间
	 * @param sortField 排序字段
	 * @param sortord 排序方式，正序逆序
	 * @param pageModel 页数信息
	 * @param adminId 不为空，则只查出该普通系统管理员创建的站点下的所有会议；为空，则查询所有站点下的会议
	 * wangyong
	 * 2013-5-29
	 */
	@Override
	public PageBean<ConfBase> getAdminConfByCondition(ConfBase conf, SiteBase currentSite, 
			Date beginTime, Date endTime, String sortField, String sortord, PageModel pageModel, Integer adminId){
		Condition condition = initCondition(conf, null, null, beginTime, endTime);
		return getAdminConf(condition, null, currentSite, sortField, sortord, pageModel, adminId);
	}
	
	private PageBean<ConfBase> getSysConf(Condition condition, String titleOrNameOrSign, String sortField, String sortord, PageModel pageModel, Integer sysUserId){
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" SELECT id, site_id, conf_name, create_user, compere_user,compere_name ,conf_hwid, conf_type, start_time, end_time, pc_num, phone_num, ");
		strSql.append(" (CASE WHEN  conf.conf_status =6 THEN 0 WHEN conf.conf_status =5 THEN 2 WHEN conf.conf_status =7 THEN 3 ELSE conf.conf_status END) conf_status ");
		strSql.append(" from ( ");
			strSql.append("select * from (");
				strSql.append(" SELECT id, site_id, conf_name, create_user, compere_user,compere_name ,conf_hwid, conf_type, start_time, end_time, pc_num, phone_num,");
				strSql.append(" (CASE WHEN  tmp.conf_status =20 THEN 6 WHEN tmp.conf_status =200 THEN 5 WHEN tmp.conf_status =2000 THEN 7 ELSE tmp.conf_status END) conf_status ");
				strSql.append(" FROM ( ");
					strSql.append(" SELECT confBase.id, confBase.site_id, confBase.conf_name, confBase.create_user, confBase.compere_user, confBase.compere_name , confBase.conf_hwid, confBase.conf_type, confBase.start_time, confBase.end_time, confBase.pc_num, confBase.phone_num, ");
					strSql.append(" (CASE WHEN  confBase.conf_status =0 THEN 20 WHEN confBase.conf_status =2 THEN 200 WHEN confBase.conf_status =3 THEN 2000 ELSE confBase.conf_status END) conf_status ");
					strSql.append(" FROM  t_conf_base confBase, t_site_base siteBase WHERE 1=1 ");
					if(condition != null && StringUtil.isNotBlank(condition.getConditionSql())){
						strSql.append(" and ").append(condition.getConditionSql());
					} 
					strSql.append(" AND confBase.del_flag = ? ");
					valueList.add(ConstantUtil.DELFLAG_UNDELETE);
					strSql.append(" AND siteBase.del_flag = ? ");
					valueList.add(ConstantUtil.DELFLAG_UNDELETE);
					strSql.append(" AND confBase.site_id = siteBase.id ");
					if(StringUtil.isNotBlank(titleOrNameOrSign)){
						strSql.append(" AND (confBase.conf_name LIKE ? OR siteBase.`site_name` LIKE ? OR siteBase.site_sign LIKE ? ) ");
						valueList.add("%" + titleOrNameOrSign + "%");
						valueList.add("%" + titleOrNameOrSign + "%");
						valueList.add("%" + titleOrNameOrSign + "%");
					}
					if(sysUserId != null && sysUserId.intValue() > 0){
						strSql.append(" AND siteBase.create_user = ?");
						valueList.add(sysUserId.intValue());
					}
				strSql.append(" ) tmp ");
			strSql.append(" ) base ");
			strSql.append("ORDER BY base.conf_status ASC, base.start_time ASC, base.id ASC ");
		strSql.append(" ) conf ");
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ");
			for (String[] eachField : SortConstant.CONFBASE_FIELDS) {
				if (eachField != null && eachField[0].equals(sortField)) {
					strSql.append("conf.");
					strSql.append(BeanUtil.att2Field(eachField[1]));
					break;
				}
			}
			if (SortConstant.SORT_ASC.equals(sortord)) {
				strSql.append(" asc ");
			}

			if (SortConstant.SORT_DESC.equals(sortord) || sortord == null
					|| "".equals(sortord.trim())
					|| "null".equals(sortord.trim().toLowerCase())) {
				strSql.append(" desc ");
			}
		}
		
		
		return getConfPageModule(null, pageModel, strSql, valueList);
	}
	
	private PageBean<ConfBase> getAdminConf(Condition condition, String subject, SiteBase currentSite, String sortField, String sortord, PageModel pageModel, Integer adminId){
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" SELECT id, conf_name, create_user, compere_user,compere_name ,conf_hwid, conf_type, start_time, end_time, pc_num, phone_num, ");
		strSql.append(" (CASE WHEN  conf.conf_status =6 THEN 0 WHEN conf.conf_status =5 THEN 2 WHEN conf.conf_status =7 THEN 3 ELSE conf.conf_status END) conf_status ");
		strSql.append(" from ( ");
			strSql.append("select * from (");
				strSql.append(" SELECT id, conf_name, create_user, compere_user,compere_name ,conf_hwid, conf_type, start_time, end_time, pc_num, phone_num, ");
				strSql.append(" (CASE WHEN  tmp.conf_status =20 THEN 6 WHEN tmp.conf_status =200 THEN 5 WHEN tmp.conf_status =2000 THEN 7 ELSE tmp.conf_status END) conf_status ");
				strSql.append(" FROM ( ");
					strSql.append(" SELECT confBase.id, confBase.conf_name, confBase.create_user, confBase.compere_user, confBase.compere_name , confBase.conf_hwid, confBase.conf_type, confBase.start_time, confBase.end_time, confBase.pc_num, confBase.phone_num, ");
					strSql.append(" (CASE WHEN  confBase.conf_status =0 THEN 20 WHEN confBase.conf_status =2 THEN 200 WHEN confBase.conf_status =3 THEN 2000 ELSE confBase.conf_status END) conf_status ");
					strSql.append(" FROM  t_conf_base confBase, t_user_base b WHERE 1=1 ");
					if(condition != null && StringUtil.isNotBlank(condition.getConditionSql())){
						strSql.append(" and ").append(condition.getConditionSql());
					} 
					strSql.append(" AND confBase.del_flag = ? ");
					valueList.add(ConstantUtil.DELFLAG_UNDELETE);
					strSql.append(" AND b.del_flag = ? ");
					valueList.add(ConstantUtil.DELFLAG_UNDELETE);
					strSql.append(" AND confBase.create_user = b.id ");
					strSql.append(" AND confBase.site_id = b.site_id ");
					strSql.append(" and confBase.site_id = ? ");
					valueList.add(currentSite.getId());
					if(StringUtil.isNotBlank(subject)){
						strSql.append(" AND ( confBase.conf_name LIKE ? OR confBase.conf_hwid = ? ) ");
						valueList.add("%"+ subject +"%");
						valueList.add(subject);
					}
					if(adminId != null && adminId.intValue() > 0){
						strSql.append(" AND b.create_user = ? ");
						valueList.add(adminId);
					}
				strSql.append(" ) tmp ");
			strSql.append(" ) base ");
			strSql.append("ORDER BY base.conf_status ASC, base.start_time ASC, base.id ASC ");
		strSql.append(" ) conf ");
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ");
			for (String[] eachField : SortConstant.CONFBASE_FIELDS) {
				if (eachField != null && eachField[0].equals(sortField)) {
					strSql.append("conf.");
					strSql.append(BeanUtil.att2Field(eachField[1]));
					break;
				}
			}
			if (SortConstant.SORT_ASC.equals(sortord)) {
				strSql.append(" asc ");
			}

			if (SortConstant.SORT_DESC.equals(sortord) || sortord == null
					|| "".equals(sortord.trim())
					|| "null".equals(sortord.trim().toLowerCase())) {
				strSql.append(" desc ");
			}
		}
		return getConfPageModule(currentSite, pageModel, strSql, valueList);
	}
	
	private PageBean<ConfBase> getConfPageModule(SiteBase currentSite, PageModel pageModel, StringBuffer strSql, List<Object> valueList){
		List<ConfBase> confList = null;
		List<ConfBaseSimple> confBaseSimpleList = null;
		PageBean<ConfBase> page = new PageBean<ConfBase>();
	
		try {
			String sql = strSql.toString().toLowerCase();
			String countsql = " select count(*) " + sql.substring(sql.indexOf("from"));
			int count = libernate.countEntityListWithSql(countsql, valueList.toArray());
			page.setRowsCount(count);
			
			pageModel.setRowsCount(count);
		} catch (SQLException e) {
			logger.error("站点管理员根据高级搜索条件查询会议列表条数出错！" + e);
			return null;
		}
		try {
			String limitsql = strSql.toString() + " limit "+(Integer.parseInt(pageModel.getPageNo()) - 1) * pageModel.getPageSize() + "," + pageModel.getPageSize();
			confBaseSimpleList = libernate.getEntityListBase(ConfBaseSimple.class, limitsql, valueList.toArray());
		} catch (SQLException e) {
			logger.error("站点管理员根据高级搜索条件查询会议列表(权限控制条件可以放在condition中)出错！" + e);
		}
		if(confBaseSimpleList != null && confBaseSimpleList.size() > 0){
			confList = new ArrayList<ConfBase>();
			String[] filedArray = ObjectUtil.getFieldFromObject(confBaseSimpleList.get(0));
			String copyField = "";
			for(String filed : filedArray){
				if(!"confStatus".equals(filed)){
					copyField = copyField + "," + filed;
				}
			}
			logger.info("copyField:"+copyField);
			for(ConfBaseSimple conf : confBaseSimpleList){
				ConfBase confBase = new ConfBase();
				try {
					confBase = (ConfBase) ObjectUtil.copyObject(conf, confBase, copyField);
					confBase.setConfStatus(Integer.valueOf(conf.getConfStatus().toString()));
				} catch (Exception e) {
					logger.error("转换conf出错！" + e);
				}
				confList.add(confBase);
			}
			if(currentSite != null){
				confList = getOffsetConfList(currentSite, confList);
			}
		}
		page.setDatas(confList);
		page.setPageNo(pageModel.getPageNo());
		page.setPageSize(pageModel.getPageSize());
		return page;
	}
	
	/**
	 * 初始化高级搜索Condition条件
	 * wangyong
	 * 2013-1-23
	 */
	private Condition initCondition(ConfBase confBase, String siteName, String siteSign, Date beginTime, Date endTime){
		Condition condition = new Condition();
		if(confBase != null){
			logger.info("confBase-----" + confBase);
			if(StringUtil.isNotBlank(confBase.getConfName())){
				condition.like("confBase.conf_name", confBase.getConfName());
			}
			if(confBase.getConfType() != null && confBase.getConfType().intValue() > 0){
				condition.equal("confBase.conf_type", confBase.getConfType());    //	会议类型： 1 、电话会议 2、视频 3、视频+网络电话  4、视频+传统电话
			}
			if(confBase.getConfStatus() != null && confBase.getConfStatus().intValue() != -1){
				condition.equal("confBase.conf_status", confBase.getConfStatus());    //会议状态：-1：全部会议  0、预约成功  2、正在召开  3、已结束    9、取消的会议
				if(confBase.getConfStatus().intValue() != ConfConstant.CONF_STATUS_CANCELED){
					condition.equal("confBase.del_flag", ConstantUtil.DELFLAG_UNDELETE);
				}
			}
			if(StringUtil.isNotBlank(siteName)){
				condition.like("siteBase.site_name", siteName);
			}
			if(StringUtil.isNotBlank(siteSign)){
				condition.like("siteBase.site_sign", siteSign);
			}
		}
	    if(beginTime != null){
	      beginTime = DateUtil.StringToDate(DateUtil.dateToString(beginTime, null), "yyyy-MM-dd");
	      beginTime = DateUtil.getGmtDate(beginTime);
	      condition.greaterEqual("confBase.start_time", DateUtil.getDateStrCompact(beginTime, "yyyy-MM-dd HH:mm:ss"));
	    }
	    if(endTime != null){
	      endTime = DateUtil.StringToDate(DateUtil.dateToString(endTime, null), "yyyy-MM-dd");
	      endTime = DateUtil.getGmtDate(DateUtil.addDate(endTime, 1));
	      condition.lessEqual("confBase.start_time", DateUtil.getDateStrCompact(DateUtil.addDateSecond(endTime, -1), "yyyy-MM-dd HH:mm:ss"));
	    }
		return condition;
	}
	
	
	
	public boolean checkSiteMaxConfCount(ConfBase confBase,SiteBase siteBase){
		if (!ConfConstant.CHECK_CONCURRENT_CONF_SCHEDULE) {
			return true;
		}
		if(confBase==null || siteBase==null){
			return false;
		}
		List<Object> valueList = new ArrayList<Object>();
		int nowConfCount = 0;
		//站点最大并发量
		int siteMaxConf = siteBase.getSyncConfNum();
		//站点最大并发量不做限制的话，直接返回true
		if(siteMaxConf <= 0){
			return true;
		}
		
		//取当前 站点下时间段内的会议数量
		StringBuffer sqlBuffer=new StringBuffer();
		sqlBuffer.append(" SELECT COUNT(id) AS confCount FROM t_conf_base tcb ");
		
		sqlBuffer.append(" WHERE 	tcb.`del_flag`=? ");
		sqlBuffer.append("			AND tcb.`site_id`=?");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(siteBase.getId());

		if(confBase.getId()!=null && confBase.getId().intValue()>0){
			sqlBuffer.append("		AND tcb.id != ?");
			valueList.add(confBase.getId());
		}
		
		sqlBuffer.append("			AND (tcb.`conf_status`=? OR tcb.`conf_status`=?)");
		valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
		valueList.add(ConfConstant.CONF_STATUS_OPENING);
		sqlBuffer.append("			AND (tcb.`permanent_conf` <= ?)");
		valueList.add(ConfConstant.CONF_PERMANENT_ENABLED_MAIN);
		sqlBuffer.append("			AND ");
		sqlBuffer.append("			(");
		sqlBuffer.append("				(tcb.`start_time`<=? AND tcb.`end_time`>=?)");
		valueList.add(confBase.getStartTime());
		valueList.add(confBase.getStartTime());
		sqlBuffer.append("				OR");
		sqlBuffer.append("				( tcb.`start_time`>=? AND tcb.`end_time`<=?)");
		valueList.add(confBase.getStartTime());
		valueList.add(confBase.getEndTime());
		sqlBuffer.append("				OR");
		sqlBuffer.append("				( tcb.`start_time`<=? AND tcb.`end_time`>=?)");
		valueList.add(confBase.getEndTime());
		valueList.add(confBase.getEndTime());
		sqlBuffer.append("			)");
		
		try {
			nowConfCount = libernate.countEntityListWithSql(sqlBuffer.toString(), valueList.toArray());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return siteMaxConf > nowConfCount ?  true: false;
	}
	
	
	
	
}
