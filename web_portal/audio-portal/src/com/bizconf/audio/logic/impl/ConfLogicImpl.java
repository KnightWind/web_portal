package com.bizconf.audio.logic.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfCycle;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.ConfLogic;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.impl.BaseService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.StringUtil;

@Service
public class ConfLogicImpl extends BaseService implements ConfLogic {
	
	@Autowired
	ConfService confService;
	@Autowired
	EmpowerConfigService empowerConfigService;
	
	/**
	 * 创建或修改会议时获取可用license
	 * wangyong
	 * 2013-3-26
	 */
	@Override
	public HashMap<String, Integer> getEffetLicense(ConfBase confBase, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser){
		HashMap<String, Integer> licenseMap = null;
		if(confBase == null){
			return licenseMap;
		}
		if(siteBase == null){
			return licenseMap;
		}
		if(siteBase.getLicense() == null || siteBase.getLicense().intValue() <= 0){
			return licenseMap;
		}
		Integer siteChargeMode=siteBase.getChargeMode();
		if(siteChargeMode == null || siteChargeMode.intValue() > SiteConstant.SITE_CHARGEMODE_SEATES || siteChargeMode.intValue() < SiteConstant.SITE_CHARGEMODE_NAMEHOST ){
			return licenseMap;
		}
		if (siteChargeMode.intValue() == SiteConstant.SITE_CHARGEMODE_NAMEHOST) { //NameHost模式
			licenseMap = getNameHostLicense(confBase, confCycle, siteBase, currentUser);
			return licenseMap;
		} else if (siteChargeMode.intValue() == SiteConstant.SITE_CHARGEMODE_ACTIVEUSER) { //ActiveUser模式
			licenseMap = getActiveUserLicense(confBase, confCycle, siteBase, currentUser);
			return licenseMap;
		} else if (siteChargeMode.intValue() == SiteConstant.SITE_CHARGEMODE_SEATES) { //seats模式
			licenseMap = getSeatsLicense(confBase, confCycle, siteBase);
			return licenseMap;
		} else if (siteChargeMode.intValue() == SiteConstant.SITE_CHARGEMODE_TIME){ //time模式
			licenseMap = getTimeModeLicense(confBase, confCycle, siteBase);
			return licenseMap;
		}
		return licenseMap;
	}
	
	/**
	 * NameHost模式下获取可用license数
	 * 1.只需要验证该主持人创建的所有会议license总数 < 创建站点时为该主持人分配的有效license
	 * 2.每个主持人的有效license不一定相同
	 * wangyong
	 * 2013-3-26
	 */
	private HashMap<String, Integer> getNameHostLicense(ConfBase confBase, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser){
		HashMap<String, Integer> licenseMap = new HashMap<String, Integer>();
		Date startGmtTime = DateUtil.getGmtDateByTimeZone(confBase.getStartTime(), siteBase.getTimeZone());
		Date endGmtTime = DateUtil.addDateMinutes(startGmtTime, confBase.getDuration());
		Integer license = 0;       //获取一段时间内的所有会议使用的总点数license
		Integer userLicense = 0;
		if(confCycle == null){       //非周期会议
			license = getLicenseByTime(siteBase, startGmtTime, endGmtTime, currentUser);
			userLicense =  getUserEffetLicense(currentUser, startGmtTime, endGmtTime);   //获取当前用户（主持人）有效license总数
			licenseMap.put(DateUtil.dateToString(startGmtTime,"yyyy-MM-dd"), (userLicense-license) <= 0? 0 : (userLicense-license));
		}else{   //周期会议
			List<Date> dateList = DateUtil.getCycleDateFromScope(confCycle.getBeginDate(), confCycle.getEndDate(), confCycle.getCycleType(), confCycle.getCycleValue(), ConstantUtil.CYCLE_CONF_DATE_LIMIT);
			if(dateList != null && dateList.size() > 0){
				for(Date eachDate:dateList){
					Date eachStartTime = null;
					if(eachDate != null){
						eachStartTime = eachDate;
						eachStartTime.setHours(confBase.getStartTime().getHours());
						eachStartTime.setMinutes(confBase.getStartTime().getMinutes());
						startGmtTime = DateUtil.getGmtDateByTimeZone(eachStartTime, siteBase.getTimeZone());
						endGmtTime = DateUtil.addDateMinutes(eachStartTime, confBase.getDuration());
						license = getLicenseByTime(siteBase, startGmtTime, endGmtTime, currentUser);
						userLicense =  getUserEffetLicense(currentUser, startGmtTime, endGmtTime);   //获取当前用户（主持人）有效license总数
						licenseMap.put(DateUtil.dateToString(startGmtTime,"yyyy-MM-dd"), (userLicense-license) <= 0? 0 : (userLicense-license));
					}
				}
			}
			dateList=null;
		}
		return licenseMap;
	}
	
	/**
	 * ActiveUser模式下获取可用license数
	 * 1.创建站点时，设置的站点最大点数即为每个主持人的最大license（包括动态分配的有效license）
	 * 2.只需要验证该主持人创建的所有会议license总数 < 创建站点为该站点分配的有效license
	 * wangyong
	 * 2013-3-26
	 */
	private HashMap<String, Integer> getActiveUserLicense(ConfBase confBase, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser){
		HashMap<String, Integer> licenseMap = new HashMap<String, Integer>();
		Date startGmtTime=DateUtil.getGmtDateByTimeZone(confBase.getStartTime(), siteBase.getTimeZone());
		Date endGmtTime=DateUtil.addDateMinutes(startGmtTime, confBase.getDuration());
		Integer license = 0;
		Integer siteLicense = 0;
		if(confCycle==null){    //非周期会议
			license = getLicenseByTime(siteBase, startGmtTime, endGmtTime, currentUser);
			siteLicense =  getSiteEffetLicense(siteBase, startGmtTime, endGmtTime);   //获取当前用户（主持人）有效license总数
			licenseMap.put(DateUtil.dateToString(startGmtTime,"yyyy-MM-dd"), (siteLicense-license) <= 0? 0 : (siteLicense-license));
		}else{   //周期会议
			List<Date> dateList=DateUtil.getCycleDateFromScope(confCycle.getBeginDate(), confCycle.getEndDate(), confCycle.getCycleType(), confCycle.getCycleValue(), ConstantUtil.CYCLE_CONF_DATE_LIMIT);
			if(dateList!=null && dateList.size() > 0){
				for(Date eachDate:dateList){
					Date eachStartTime = null;
					if(eachDate != null){
						eachStartTime = eachDate;
						eachStartTime.setHours(confBase.getStartTime().getHours());
						eachStartTime.setMinutes(confBase.getStartTime().getMinutes());
						startGmtTime = DateUtil.getGmtDateByTimeZone(eachStartTime, siteBase.getTimeZone());
						endGmtTime = DateUtil.addDateMinutes(eachStartTime, confBase.getDuration());
						license = getLicenseByTime(siteBase, startGmtTime, endGmtTime, currentUser);
						siteLicense =  getSiteEffetLicense(siteBase, startGmtTime, endGmtTime);   //获取当前用户（主持人）有效license总数
						licenseMap.put(DateUtil.dateToString(startGmtTime,"yyyy-MM-dd"), (siteLicense-license) <= 0? 0 : (siteLicense-license));
					}
				}
			}
			dateList=null;
		}
		return licenseMap;
	}
	
	/**
	 * seats模式下获取可用license数
	 * 1.只需要验证该站点下所有会议总人数 < 该站点分配到的有效license
	 * wangyong
	 * 2013-3-26
	 */
	@SuppressWarnings("deprecation")
	private HashMap<String, Integer> getSeatsLicense(ConfBase confBase, ConfCycle confCycle, SiteBase siteBase){
		HashMap<String, Integer> licenseMap = new HashMap<String, Integer>();
		Date startGmtTime=DateUtil.getGmtDateByTimeZone(confBase.getStartTime(), siteBase.getTimeZone());
		Date endGmtTime=DateUtil.addDateMinutes(startGmtTime, confBase.getDuration());
		Integer license = 0;
		Integer siteLicense = 0;
		if(confCycle==null){    //非周期会议
			license = getLicenseByTime(siteBase, startGmtTime, endGmtTime, null);
			siteLicense =  getSiteEffetLicense(siteBase, startGmtTime, endGmtTime);   //获取当前用户（主持人）有效license总数
			licenseMap.put(DateUtil.dateToString(startGmtTime,"yyyy-MM-dd"), (siteLicense-license) <= 0? 0 : (siteLicense-license));
		}else{   //周期会议
			List<Date> dateList=DateUtil.getCycleDateFromScope(confCycle.getBeginDate(), confCycle.getEndDate(), confCycle.getCycleType(), confCycle.getCycleValue(), ConstantUtil.CYCLE_CONF_DATE_LIMIT);
			if(dateList!=null && dateList.size() > 0){
				for(Date eachDate:dateList){
					Date eachStartTime = null;
					if(eachDate != null){
						eachStartTime = eachDate;
						eachStartTime.setHours(confBase.getStartTime().getHours());
						eachStartTime.setMinutes(confBase.getStartTime().getMinutes());
						startGmtTime = DateUtil.getGmtDateByTimeZone(eachStartTime, siteBase.getTimeZone());
						endGmtTime = DateUtil.addDateMinutes(eachStartTime, confBase.getDuration());
						license = getLicenseByTime(siteBase, startGmtTime, endGmtTime, null);
						siteLicense =  getSiteEffetLicense(siteBase, startGmtTime, endGmtTime);   //获取当前用户（主持人）有效license总数
						licenseMap.put(DateUtil.dateToString(startGmtTime,"yyyy-MM-dd"), (siteLicense-license) <= 0? 0 : (siteLicense-license));
					}
				}
			}
			dateList=null;
		}
		return licenseMap;
	}
	
	/**
	 * time模式下获取可用license数
	 * 1.只需要验证该站点下所有会议总人数 < 该站点分配到的有效license
	 * 2.与seats模式相同，直接引用即可
	 * wangyong
	 * 2013-3-26
	 */
	private HashMap<String, Integer> getTimeModeLicense(ConfBase confBase, ConfCycle confCycle, SiteBase siteBase){
		return getSeatsLicense(confBase, confCycle, siteBase);
	}
	
	/**
	 * 获取一段时间内的所有会议使用的总点数license
	 * 1.若为seats模式，currentUser传null
	 * 2.若为NameHost模式，ActiveUser模式，currentUser传当前用户（主持人）
	 * wangyong
	 * 2013-3-26
	 */
	private Integer getLicenseByTime(SiteBase siteBase, Date startGmtTime, Date endGmtTime, UserBase currentUser){
		Integer license = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT SUM(tcb.`max_user`) AS licCount FROM t_conf_base tcb ");
		sqlBuffer.append(" WHERE 1=1 ");
		sqlBuffer.append("      and   (tcb.conf_status = ? or tcb.conf_status = ?) ");
		sqlBuffer.append("		and tcb.site_id = ? ");
		sqlBuffer.append("		and ( ");
		sqlBuffer.append("			(tcb.`start_time`<=? AND tcb.`end_time`>=?) ");
		sqlBuffer.append("			OR  (tcb.`start_time`<=? AND tcb.`end_time`>=?) ");
		sqlBuffer.append("			OR(tcb.`start_time`>=? AND tcb.`end_time`<=?) ");
		sqlBuffer.append("		)");
		valueList.add(ConfConstant.CONF_STATUS_SUCCESS);
		valueList.add(ConfConstant.CONF_STATUS_OPENING);
		valueList.add(siteBase.getId());
		valueList.add(endGmtTime);
		valueList.add(endGmtTime);
		valueList.add(startGmtTime);
		valueList.add(startGmtTime);
		valueList.add(startGmtTime);
		valueList.add(endGmtTime);
		if(currentUser != null && currentUser.getId() != null && currentUser.getId().intValue() > 0){
			sqlBuffer.append("   and create_user = ? ");
			valueList.add(currentUser.getId());
		}
		try {
			license = libernate.countEntityListWithSql(sqlBuffer.toString(), valueList.toArray());
		} catch (SQLException e) {
			logger.error("获取一段时间内的所有会议使用的总点数license错误！" + e);
		}
		return license;
	}
	
	/**
	 * 获取当前时间站点生效的license数
	 * wangyong
	 * 2013-3-26
	 */
	private Integer getSiteEffetLicense(SiteBase currentSite, Date startGmtTime, Date endGmtTime ){
		Integer license = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT SUM(lic.lic_num) AS licCount FROM t_license AS lic ");
		sqlBuffer.append(" WHERE 1=1 ");
		sqlBuffer.append(" AND lic.site_id = ? ");
		sqlBuffer.append(" AND lic.effe_date < ? ");
		sqlBuffer.append(" AND lic.expire_date > ? ");
		sqlBuffer.append(" AND lic.del_flag = ? ");
		valueList.add(currentSite.getId());
		valueList.add(startGmtTime);
		valueList.add(endGmtTime);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		try {
			license = libernate.countEntityListWithSql(sqlBuffer.toString(), valueList.toArray());
		} catch (SQLException e) {
			logger.error("获取当前时间站点生效的license数错误！" + e);
		}
		return license + currentSite.getLicense();
	}
	
	/**
	 * 获取当前时间主持人用户生效的license数
	 * wangyong
	 * 2013-3-26
	 */
	private Integer getUserEffetLicense(UserBase currentUser, Date startGmtTime, Date endGmtTime ){
		Integer license = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT SUM(lic.lic_num) AS licCount FROM t_license AS lic ");
		sqlBuffer.append(" WHERE 1=1 ");
		sqlBuffer.append(" AND lic.user_id = ? ");
		sqlBuffer.append(" AND lic.effe_date < ? ");
		sqlBuffer.append(" AND lic.expire_date > ? ");
		sqlBuffer.append(" AND lic.del_flag = ? ");
		valueList.add(currentUser.getId());
		valueList.add(startGmtTime);
		valueList.add(endGmtTime);
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		try {
			license = libernate.countEntityListWithSql(sqlBuffer.toString(), valueList.toArray());
		} catch (SQLException e) {
			logger.error("获取当前时间主持人用户生效的license数错误！" + e);
		}
		return license;
	}
	
	/**
	 * 创建会议时验证站点所剩license（重新创建会议）
	 * wangyong
	 * 2013-3-14
	 */
	@Override
	public boolean createConfLicenseVali(ConfBase conf, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser){
		HashMap<String, Integer> licenseMap = getEffetLicense(conf, confCycle, siteBase, currentUser);
		if(licenseMap != null){
			Iterator<Entry<String, Integer>>  iter = licenseMap.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<String, Integer> entry = iter.next();
				if(conf.getMaxUser() != null && entry.getValue() != null){
					if(conf.getMaxUser().intValue() > entry.getValue().intValue()){
						logger.debug("会议最大参会人数大于站点所剩license，无法保存会议");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 修改会议时验证站点所剩license
	 * wangyong
	 * 2013-3-14
	 */
	@Override
	public boolean updateConfLicenseVali(ConfBase conf, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser){
		HashMap<String, Integer> licenseMap = getEffetLicense(conf, confCycle, siteBase, currentUser);
		if(conf != null && conf.getId() != null && conf.getId().intValue() > 0){
			ConfBase confBase = confService.getConfBasebyConfId(conf.getId());
			if(licenseMap != null){
				Iterator<Entry<String, Integer>>  iter = licenseMap.entrySet().iterator();
				while(iter.hasNext()){
					Map.Entry<String, Integer> entry = iter.next();
					if(conf.getMaxUser() != null && entry.getValue() != null){
						//若为修改会议，则判断修改后的license是否大于当前可用的license+修改前的license
						if(conf.getMaxUser().intValue() > entry.getValue().intValue() + confBase.getMaxUser()){
							logger.debug("会议最大参会人数大于站点所剩license，无法保存会议");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 保存会议时验证会议数据
	 * wangyong
	 * 2013-3-4
	 */
	@Override
	public boolean saveConfValidate(ConfBase conf, ConfCycle confCycle, SiteBase siteBase) {
		//补充正则表达式验证页面输入数据
		return true;
	}
	
//	
//	@Override
//	public HashMap<String,Integer> getSurplusLicense(ConfBase confBase,ConfCycle confCycle, SiteBase siteBase){
//		Integer surpLicense=0;
//		HashMap<String,Integer> surpMap = null;
//		if(confBase==null){
//			return surpMap;
//		}
//		
//		if(siteBase == null){
//			return surpMap;
//		}
//		Integer siteLicense= siteBase.getLicense();
//		if(siteLicense==null || siteLicense.intValue()<=0){
//			return surpMap;
//		}
//		Integer siteChargeMode=siteBase.getChargeMode();
//		if(siteChargeMode==null || siteChargeMode.intValue()>SiteConstant.SITE_CHARGEMODE_SEATES || siteChargeMode.intValue()< SiteConstant.SITE_CHARGEMODE_NAMEHOST ){
//			return surpMap;
//		}
//		Integer timeZone=siteBase.getTimeZone();
//		if (siteChargeMode.intValue() == SiteConstant.SITE_CHARGEMODE_NAMEHOST) {
//			//NameHost模式
//			
//			
//
//		} else if (siteChargeMode.intValue() == SiteConstant.SITE_CHARGEMODE_ACTIVEUSER) {
//			//ActiveUser模式
//			
//
//		} else if (siteChargeMode.intValue() == SiteConstant.SITE_CHARGEMODE_SEATES) {
//			//Seat模式
//			//检测时间范围内所有会议的License总和
//			Date startTime=confBase.getStartTime();
//			Date startGmtTime=DateUtil.getGmtDateByTimeZone(startTime, timeZone);
//			Integer duration =confBase.getDuration();
//			Date endGmtTime=DateUtil.addDateMinutes(startGmtTime, duration);
//			StringBuffer sqlBuffer=new StringBuffer();
//			if(confCycle==null){//非周期会议
//				sqlBuffer.append("SELECT SUM(tcb.`max_user`) AS licCount FROM t_conf_base tcb ");
//				sqlBuffer.append(" WHERE  (tcb.conf_status = ? or tcb.conf_status = ?)");
//				sqlBuffer.append("		and tcb.site_id = ?");
//				sqlBuffer.append("		and ( ");
//				sqlBuffer.append("			(tcb.`start_time`<=? AND tcb.`end_time`>=?)");
//				sqlBuffer.append("			OR  (tcb.`start_time`<=? AND tcb.`end_time`>=?)");
//				sqlBuffer.append("			OR(tcb.`start_time`>=? AND tcb.`end_time`<=?) ");
//				sqlBuffer.append("		)");
//				Object[] values=new Object[9];
//				values[0]=ConfConstant.CONF_STATUS_SUCCESS;
//				values[1]=ConfConstant.CONF_STATUS_OPENING;
//				values[2]=siteBase.getId();
//				values[3]=endGmtTime;
//				values[4]=endGmtTime;
//				values[5]=startGmtTime;
//				values[6]=startGmtTime;
//				values[7]=startGmtTime;
//				values[8]=endGmtTime;
//				Integer license=null;
//				try {
//					System.out.println("sqlBuffer=="+sqlBuffer.toString());
//					license=libernate.countEntityListWithSql(sqlBuffer.toString(), values);
//					System.out.println("license=="+license);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				if(license==null){
//					license=0;
//				}
//				surpLicense=siteLicense-license;
//				if(surpLicense<=0){
//					surpLicense=0;
//				}
//				
////				Decimal d;
////				BigDecimal 
//				surpMap=new HashMap<String, Integer>();
//				surpMap.put(DateUtil.dateToString(startGmtTime,"yyyy-MM-dd"),surpLicense);
//			}else{//周期会议情况下
//				List<Date> dateList=DateUtil.getCycleDateFromScope(confCycle.getBeginDate(), confCycle.getEndDate(), confCycle.getCycleType(), confCycle.getCycleValue());
//				Object[] values=null;
//				if(dateList!=null && dateList.size() > 0){
//					surpMap=new HashMap<String, Integer>();
//					Date eachStartTime=null;
//					Date eachEndTime=null;
//					Integer eachCountLicense=null;
//					Integer eachSurpLicense=0;
//					String eachDateStr="";
//					for(Date eachDate:dateList){
//						if(eachDate!=null){
//							sqlBuffer=new StringBuffer();
//							eachStartTime=eachDate;
//							eachStartTime.setHours(confBase.getStartTime().getHours());
//							eachStartTime.setMinutes(confBase.getStartTime().getMinutes());
//							eachStartTime=DateUtil.getGmtDateByTimeZone(eachStartTime, timeZone);
//							eachEndTime=DateUtil.addDateMinutes(eachStartTime, duration);
//							eachDateStr=DateUtil.dateToString(eachDate,"yyyy-MM-dd");
//							sqlBuffer.append("SELECT SUM(tcb.`max_user`) AS licCount FROM t_conf_base tcb ");
//							sqlBuffer.append(" WHERE   (tcb.conf_status = ? or tcb.conf_status = ?)");
//							sqlBuffer.append("		and tcb.site_id = ?");
//							sqlBuffer.append("		and ( ");
//							sqlBuffer.append("			(tcb.`start_time`<=? AND tcb.`end_time`>=?)");
//							sqlBuffer.append("			OR  (tcb.`start_time`<=? AND tcb.`end_time`>=?)");
//							sqlBuffer.append("			OR(tcb.`start_time`>=? AND tcb.`end_time`<=?) ");
//							sqlBuffer.append("		)");
//							values=new Object[9];
////							paramNum=ii*9;
//							values[0]=ConfConstant.CONF_STATUS_SUCCESS;
//							values[1]=ConfConstant.CONF_STATUS_OPENING;
//							values[2]=siteBase.getId();
//							values[3]=eachEndTime;
//							values[4]=eachEndTime;
//							values[5]=eachStartTime;
//							values[6]=eachStartTime;
//							values[7]=eachStartTime;
//							values[8]=eachEndTime;
//							try {
//								System.out.println("sqlBuffer=="+sqlBuffer.toString());
//								eachCountLicense=libernate.countEntityListWithSql(sqlBuffer.toString(), values);
//								System.out.println("license=="+eachCountLicense);
//							} catch (SQLException e) {
//								e.printStackTrace();
//							}
//							if(eachCountLicense==null || eachCountLicense.intValue()<=0){
//								eachCountLicense=0;
//							}
//							eachSurpLicense=siteLicense-eachCountLicense;
//							surpMap.put(eachDateStr, eachSurpLicense);
//						}
//					}
//				}
//				dateList=null;
//			}
//		}
//		
//		return surpMap;
//	}

	@Override
	public SiteBase getConfSiteBase(int siteId) {
		SiteBase siteBase = null;
		try{
			siteBase = libernate.getEntity(SiteBase.class, siteId);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return siteBase;
	}
	
	/**
	 * 查询会议周期对象
	 */
	@Override
	public List<ConfCycle> getConfCycles(List<Integer> cycIds) {
		List<ConfCycle> confCycles = new ArrayList<ConfCycle>();
		try{
			for(Integer id:cycIds){
				ConfCycle confCycle = libernate.getEntity(ConfCycle.class, id);
				confCycles.add(confCycle);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return confCycles;
	}
	
	/**
	 * 获取会议的周期ID
	 */
	@Override
	public List<Integer> getCycIds(List<ConfBase> confs) {
		// TODO Auto-generated method stub
		List<Integer> cycIds = new ArrayList<Integer>();
		for(ConfBase conf:confs){
			 if(conf.getCycleId()!=null &&conf.getCycleId()>0){
				 cycIds.add(conf.getCycleId());
			 }
		}
		return cycIds;
	}
	
	@Override
	public List<ConfCycle> getConfCyclesByConf(List<ConfBase> confs) {
		List<ConfCycle> confCycles = new ArrayList<ConfCycle>();
		if(confs==null){
			return confCycles;
		}
		try{
			for(ConfBase conf:confs){
				 if(conf.getCycleId()!=null &&conf.getCycleId()>0){
					 ConfCycle confCycle = libernate.getEntity(ConfCycle.class, conf.getCycleId());
					confCycles.add(confCycle);
				 }
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return confCycles;
	}

	@Override
	public ConfCycle getConfCycleByConf(ConfBase conf) {
		ConfCycle confCycle = null;
		try{
			
			if(conf.getCycleId()!=null &&conf.getCycleId()>0){
				  confCycle = libernate.getEntity(ConfCycle.class, conf.getCycleId());
				 
			 }
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return confCycle;
	}
	
	@Override
	public List<ConfBase> getConfBasesByCreator(Integer creatorId) {
		List<ConfBase> confs = new ArrayList<ConfBase>();
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_conf_base WHERE del_flag = ?  and (create_user = ?  or compere_user=?)");
		Object[] values = new Object[]{
				ConstantUtil.DELFLAG_UNDELETE,
				creatorId,
				creatorId
		};
		
		try {
			confs = libernate.getEntityListBase(ConfBase.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据创建者id号获取会议信息出错！",e);
		}
		return confs;
	}
	
	/**
	 * 修改会议的Crypt_key
	 */
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
	 * 后台配置客户端功能缺省设置，与前台配置无关
	 * 笔记、视频、音频、聊天、公告、私聊、组聊、投票（默认全部开启，但不在页面上配置）
	 * 1.前台配置完成后，调用配置后台客户端默认功能
	 * wangyong
	 * 2013-4-10
	 */
	@Override
	public void setServerClientConfig(char[] clientConfig){
		clientConfig[ConfConstant.CLIENT_CONFIG_NOTE] = '1';            //笔记
		clientConfig[ConfConstant.CLIENT_CONFIG_VIDEO] = '1';           //视频
		clientConfig[ConfConstant.CLIENT_CONFIG_AUDIO] = '1'; 			//音频
		clientConfig[ConfConstant.CLIENT_CONFIG_CHAT] = '1';			//聊天
		clientConfig[ConfConstant.CLIENT_CONFIG_NOTICE] = '1';			//公告
		clientConfig[ConfConstant.CLIENT_CONFIG_PRIVATE_CHAT] = '1';    //私聊
		clientConfig[ConfConstant.CLIENT_CONFIG_GROUP_CHAT] = '1';		//组聊
		clientConfig[ConfConstant.CLIENT_CONFIG_VOTE] = '1';            //投票(问卷调查)
	}

	@Override
	public UserBase getConfCreator(String confHwId) {
		if (!StringUtil.isEmpty(confHwId)) {
			try {
				ConfBase conf =  libernate.getEntity(ConfBase.class, "conf_hwid", confHwId);
				if(conf!=null){
					return libernate.getEntity(UserBase.class, conf.getCreateUser());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public void immediatelyConfAuthority(ConfBase conf, UserBase user){
		EmpowerConfig userEmpower = empowerConfigService.makeEmpowerForConf(user);   //获取用户创建会议，缺省会议设置的权限
		DefaultConfig defaultConfig = confService.getDefaultConfig(user);
		char[] clientConfig = conf.getClientConfig().toCharArray();
		if(userEmpower.getVideoFlag().intValue() == SiteConstant.EMPOWER_ENABLED && defaultConfig.getMaxVideo().intValue() > 0){
			clientConfig[ConfConstant.CLIENT_CONFIG_VIDEO] = '1';      //由于个人缺省设置不设置视频是否开启，所以只要站点授权视频功能，则创建即时会议时，自动打开视频功能
			conf.setConfType(ConfConstant.CONF_TYPE_VIDEO_FUNC);
			conf.setClientConfig(String.valueOf(clientConfig));
		}
		if(userEmpower.getAudioFlag().intValue() == SiteConstant.EMPOWER_ENABLED && defaultConfig.getMaxAudio().intValue() > 0){
			clientConfig[ConfConstant.CLIENT_CONFIG_AUDIO] = '1';      //由于个人缺省设置不设置音频是否开启，所以只要站点授权音频功能，则创建即时会议时，自动打开音频功能
			conf.setClientConfig(String.valueOf(clientConfig));
		}
		//2013.7.2新增站点分辨率权限控制    ----开始
		String userDefaultMaxDpi = defaultConfig.getMaxDpi();
		String siteEmpowerMaxDpi = "";
		String siteEmpowerVideoType = "";
		String confVideoType = defaultConfig.getVideoType();
		if(userEmpower.getDpiNumber() != null && userEmpower.getDpiNumber().intValue() == ConfConstant.CONF_VIDEO_TYPE_WEBBAND_CODE){
			siteEmpowerMaxDpi = ConfConstant.CONF_VIDEO_TYPE_WEBBAND.substring(1,2);
			siteEmpowerVideoType = ConfConstant.CONF_VIDEO_TYPE_WEBBAND;
		}

		if(userEmpower.getDpiNumber() != null && userEmpower.getDpiNumber().intValue() == ConfConstant.CONF_VIDEO_TYPE_FLUENCY_CODE){
			siteEmpowerMaxDpi = ConfConstant.CONF_VIDEO_TYPE_FLUENCY.substring(1,2);
			siteEmpowerVideoType = ConfConstant.CONF_VIDEO_TYPE_FLUENCY;
		}

		if(userEmpower.getDpiNumber() != null && userEmpower.getDpiNumber().intValue() == ConfConstant.CONF_VIDEO_TYPE_DISTINCT_CODE){
			siteEmpowerMaxDpi = ConfConstant.CONF_VIDEO_TYPE_DISTINCT.substring(1,2);
			siteEmpowerVideoType = ConfConstant.CONF_VIDEO_TYPE_DISTINCT;
		}
		// 2013.7.8 新增视频质量 （最高）
		if(userEmpower.getDpiNumber() != null && userEmpower.getDpiNumber().intValue() == ConfConstant.CONF_VIDEO_TYPE_BEST_CODE){
			siteEmpowerMaxDpi = ConfConstant.CONF_VIDEO_TYPE_BEST.substring(1,2);
			siteEmpowerVideoType = ConfConstant.CONF_VIDEO_TYPE_BEST;
		}
		if(userDefaultMaxDpi != null && !userDefaultMaxDpi.equals(siteEmpowerMaxDpi)
				&& IntegerUtil.parseIntegerWithDefaultZero(userDefaultMaxDpi) > IntegerUtil.parseIntegerWithDefaultZero(siteEmpowerMaxDpi)){
				userDefaultMaxDpi = siteEmpowerMaxDpi;
				confVideoType = siteEmpowerVideoType;
			
		}
		conf.setVideoType(confVideoType);
		conf.setMaxDpi(userDefaultMaxDpi);
		conf.setDefaultDpi(confVideoType.substring(0,1));
		//2013.7.2新增站点分辨率权限控制   -----结束
		//2013.7.4创建即时会议根据权限与个人缺省设置，是否有电话功能、视频功能  ------开始
		if(userEmpower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED && defaultConfig.hasPhoneFunc()){
			conf.setConfType(ConfConstant.CONF_TYPE_PHONE_FUNC);
			if(userEmpower.getVideoFlag().intValue() == SiteConstant.EMPOWER_ENABLED && defaultConfig.getMaxVideo().intValue() > 0){
				conf.setConfType(ConfConstant.CONF_TYPE_PHONE_VIDEO_FUNC);
			}
		}
		//2013.7.4创建即时会议根据权限与个人缺省设置，是否有电话功能、视频功能   -----结束
		conf.setAheadTime(ConfConstant.CONF_CONFIG_AHEADTIMES+5);//即时会议的提前时间多加5分钟，以免在入会时提示用户会议未到开始时间
		conf.setStartTime(DateUtil.addDateMinutes(DateUtil.getGmtDate(null), ConfConstant.CONF_CONFIG_AHEADTIMES));      //即时会议为当前GMT时间+5分钟 
		conf.setEndTime(DateUtil.addDateMinutes(conf.getStartTime(), conf.getDuration()));
		conf.setConfDesc(ResourceHolder.getInstance().getResource("bizconf.jsp.user.immediaconf.desc"));
	}

}
