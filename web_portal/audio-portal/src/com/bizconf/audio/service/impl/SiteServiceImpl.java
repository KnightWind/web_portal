package com.bizconf.audio.service.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.LoginConstants;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.SiteLogic;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsEnterprise;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsEnterpriseAreaMap;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapBusinessService;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapBusinessServicebindingStub;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestAddEnterpriseAreaMapRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestAddEnterpriseRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryEnterpriseAreaMapListRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryEnterpriseListRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryEnterpriseRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestRemoveEnterpriseRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestUpdateEnterpriseRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryEnterpriseAreaMapListResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryEnterpriseListResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryEnterpriseResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapToken;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsStringKV;
import com.bizconf.audio.soap.conf.ESpaceMeetingCmuLocator;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryEnterpriseAreaMapListResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryEnterpriseListResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryEnterpriseResponseHolder;
import com.bizconf.audio.task.AppContextFactory;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
@Service
public class SiteServiceImpl extends BaseSoapService implements SiteService{
	private final Logger logger=Logger.getLogger(SiteServiceImpl.class);

	@Autowired
	SiteLogic siteLogic;

	@Autowired
	LoginService loginService;
	@Autowired
	EmpowerConfigService empowerConfigService;
	
	/**
	 * 创建新站点（系统管理员）
	 * action 中处理图片（上传，修改大小）
	 * action 中判断企业标识是否已存在，创建或修改成功要给站点管理员发封邮件
	 * @author wangyong
	 * @date 2013.1.11
	 */
	@Override
	public SiteBase createSite(SiteBase siteBase, UserBase siteAdmin, EmpowerConfig empowerConfig)
			throws Exception {
		boolean saveAdminFlag = false;
		SiteBase site = null;
		String retCode = null;
		if(siteBase != null && siteAdmin != null){
			siteBase.init();
			if(!siteLogic.createSiteValidate(siteBase, siteAdmin)){
				logger.error("创建站点验证前台表单数据出错！");
				return null;
			}
			try {
				retCode =soapAddSite(siteBase);
				if(retCode.equals(ConstantUtil.AS_SUCCESS_CODE)){
					site = DAOProxy.getLibernate().saveEntity(siteBase);    //首先保存站点的基本信息
					if(site!=null && site.getId().intValue() > 0){
						empowerConfig.setSiteId(site.getId());
						boolean empowerFlag = empowerConfigService.saveEmpowerConfig(empowerConfig);
						if(!empowerFlag){
							logger.error("创建站点时创建授权失败！");
						}
						//系统管理员创建站点后，初始化站点的全局变量设置
						updateSiteEmpowerGlobal(empowerConfig, site);
						siteAdmin.setSiteId(site.getId());
						saveAdminFlag = siteLogic.saveSiteSupperAdmin(siteAdmin);         //再保存站点管理员的基本信息
						if(!saveAdminFlag){
							DAOProxy.getLibernate().deleteEntity(site); //若保存站点管理员失败，则删除已经创建的站点
							site = null;
							logger.error("保存站点管理员失败！");
						}
					}
				}else if(retCode.equals(ConstantUtil.AS_FAILED_LICENSE_CODE)){
					siteBase.setId(SiteConstant.SITE_CREATE_ERROR_LICENCE);    //系统license不足
					logger.info("创建站点时license不足");
					return siteBase;
				}else{
					logger.info("华为返回错误码：" + retCode);
					return siteBase;
				}
			} catch (Exception e) {
				if(retCode.equals(ConstantUtil.AS_SUCCESS_CODE)){
					DAOProxy.getLibernate().deleteEntity(site); //若保存站点logo失败，则删除已经创建的站点
					siteLogic.deleteSiteSupperAdmin(site.getId(), siteAdmin); //然后删除已经创建的站点管理员
				}
				logger.error("创建站点失败", e);
				site = null;
			}
		}
		return site;
	}

	/**
	 * 保存会议设置参数项
	 * 
	 * 
	 */
	@Override
	public boolean saveDefaultConfig(DefaultConfig config) throws Exception {
		return false;
	}

	/**
	 * 系统管理员修改站点信息
	 * 
	 */
	@Override
	public SiteBase updateSiteBaseForSystem(SiteBase siteBase, UserBase siteAdmin, EmpowerConfig empowerConfig) throws Exception {
		SiteBase site = null;
		UserBase user = null;
		String retCode = "";
		if(siteBase != null && siteAdmin != null){
			if(!siteLogic.updateSiteValidate(siteBase)){
				return site;
			}
			try {
				retCode = soapUpdateSite(siteBase,true);
				if(retCode.equals(ConstantUtil.AS_SUCCESS_CODE)){
					site = DAOProxy.getLibernate().updateEntity(siteBase, "id", "enName", "siteName", "siteLogo", "siteSign", "siteFlag","timeZone","timeZoneId", "mtgType", "expireDate", "effeDate", "hireMode", "chargeMode");  //先更新站点信息
					if(site!=null && site.getId().intValue() > 0){
						empowerConfig.setSiteId(site.getId());
						boolean empowerFlag = empowerConfigService.updateEmpowerConfig(empowerConfig);
						if(!empowerFlag){
							logger.info("修改站点时创建授权失败！");
						}
						//系统管理员关闭站点功能权限后，要强制修改站点的全局变量设置
						updateSiteEmpowerGlobal(empowerConfig, siteBase);
						if(StringUtil.isNotBlank(siteAdmin.getLoginPass())){     //页面未修改用户密码
							user = DAOProxy.getLibernate().updateEntity(siteAdmin, "id", "trueName", "loginName", "loginPass", "userEmail", "mobile", "remark", "enName");     //再更新站点管理员的基本信息
						}else{
							user = DAOProxy.getLibernate().updateEntity(siteAdmin, "id", "trueName", "loginName", "userEmail", "mobile", "remark", "enName");     //再更新站点管理员的基本信息
						}
					}
					if(user == null){
						//若更新站点管理员失败，则恢复已经修改的站点
					}
				}else if(retCode.equals(ConstantUtil.AS_FAILED_LICENSE_CODE)){
					siteBase.setId(SiteConstant.SITE_CREATE_ERROR_LICENCE);    //参会人数大于站点当前所剩参会人数值
					logger.info("修改站点时license不足");
					return siteBase;
				}else{
					logger.info("华为返回错误码：" + retCode);
					return siteBase;
				}
			} catch (Exception e) {
				logger.error("修改站点失败", e);
			}finally{
				
			}
		}
		return site;
	}
	
	private boolean updateSiteEmpowerGlobal(EmpowerConfig empowerConfig, SiteBase siteBase){
		boolean savedConfig = false;
		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(siteBase.getId());
		if(sitePower != null){     //当站点设置了全局变量
			if(empowerConfig.getPhoneFlag() != null && empowerConfig.getPhoneFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
				sitePower.setPhoneFlag(SiteConstant.EMPOWER_DISABLED);
			}
			if(empowerConfig.getAutoFlag() != null && empowerConfig.getAutoFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
				sitePower.setAutoFlag(SiteConstant.EMPOWER_DISABLED);
			}
			if(empowerConfig.getShareMediaFlag() != null && empowerConfig.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
				sitePower.setShareMediaFlag(SiteConstant.EMPOWER_DISABLED);
			}
			if(empowerConfig.getRecordFlag() != null && empowerConfig.getRecordFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
				sitePower.setRecordFlag(SiteConstant.EMPOWER_DISABLED);
			}
			savedConfig = empowerConfigService.updateSiteEmpowerGlobal(empowerConfig);
			if(!savedConfig){
				logger.info("系统管理员关闭站点功能权限后，强制修改站点的全局变量设置出错！");
			}
		}else{     //当站点未设置全局变量，则新建站点全局变量,站点授权的功能默认都开启
			empowerConfig.setSiteId(siteBase.getId());
			empowerConfig.setEmTime(DateUtil.getGmtDate(null));
			empowerConfig.setEmUser(1);    //默认为系统管理员
			empowerConfig.setEmUserType(ConstantUtil.USERTYPE_SYSTEM_SUPPER);
			empowerConfig.setUserId(-1);
			if(empowerConfig.getPhoneFlag() != null && empowerConfig.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED){    //电话功能已授权
				empowerConfig.setPhoneFlag(SiteConstant.EMPOWER_ENABLED);
			}else{
				empowerConfig.setPhoneFlag(SiteConstant.EMPOWER_DISABLED);
			}
			if(empowerConfig.getAutoFlag() != null && empowerConfig.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				empowerConfig.setAutoFlag(SiteConstant.EMPOWER_ENABLED);
			}else{
				empowerConfig.setAutoFlag(SiteConstant.EMPOWER_DISABLED);
			}
			if(empowerConfig.getShareMediaFlag() != null && empowerConfig.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				empowerConfig.setShareMediaFlag(SiteConstant.EMPOWER_ENABLED);
			}else{
				empowerConfig.setShareMediaFlag(SiteConstant.EMPOWER_DISABLED);
			}
			if(empowerConfig.getRecordFlag() != null && empowerConfig.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
				empowerConfig.setRecordFlag(SiteConstant.EMPOWER_ENABLED);
			}else{
				empowerConfig.setRecordFlag(SiteConstant.EMPOWER_DISABLED);
			}
			savedConfig = empowerConfigService.saveSiteEmpowerGlobal(empowerConfig);
			if(!savedConfig){
				logger.info("当站点未设置全局变量，则新建站点全局变量出错！");
			}
		}
		return savedConfig;
	}

	/**
	 * 站点管理员修改站点名称与LOGO
	 * 
	 */
	@Override
	public boolean updateSiteBaseForSiteAdmin(Integer siteId, String siteName, String enName, File logoFile) {
		boolean saveFlag=false;
		if (siteId != null && siteId.intValue() > 0) {
			
			
//			StringBuffer sqlBuffer=new StringBuffer();
//			List<Object> valueList=new ArrayList<Object>();
//			if((siteName != null && !"".equals(siteName)) || (logoFile!=null && logoFile.length() > 0)){
//				
////				sqlBuffer.append("update t_site_base t set t.site_name = ? where t.id = ?");
//				
//			}
		}
		return saveFlag;
	}
	
	/**
	 * 站点管理员修改站点名称,英文名称与LOGO
	 * 
	 */
	@Override
	public SiteBase updateSiteBaseForSiteAdmin(Integer siteId, String siteName, String enName, String logoPath,Integer timeZoneId,Integer timeZone){
		SiteBase siteBase = new SiteBase();
		siteBase.setId(siteId);
		siteBase.setSiteName(siteName);
		siteBase.setEnName(enName);
		siteBase.setSiteLogo(logoPath);
		siteBase.setTimeZoneId(timeZoneId);
		siteBase.setTimeZone(timeZone);
		SiteBase site = null;
		if(siteId != null && siteId.intValue() > 0){
			try {
				site = DAOProxy.getLibernate().updateEntity(siteBase, "id", "enName", "siteName", "siteLogo","timeZone","timeZoneId");  //更新站点信息
			} catch (Exception e) {
				logger.error("修改站点失败", e);
			}
		}
		return site;
	}

	/**
	 * 根据站点ID号删除站点
	 * wangyong
	 * 2013-1-8
	 */
	@Override
	public boolean deleteSiteById(Integer id, SystemUser sysUser) throws Exception {
		boolean updateFlag = false;
		SiteBase site = getSiteBaseById(id);
		String soapCode="";
		if(site != null && id != null && id.intValue()>0 && sysUser != null){
			soapCode = soapDelSite(site.getSiteSign());
			if(soapCode.equals(ConstantUtil.AS_SUCCESS_CODE)){
				String updateSql = "update t_site_base set del_flag = ?,del_time = ?,del_user = ? where id = ? ";
				Object[] values = new Object[4];
				values[0] = ConstantUtil.DELFLAG_DELETED;
				values[1] = DateUtil.getDateStrCompact(DateUtil.getGmtDate(null), "yyyy-MM-dd HH:mm:ss");
				values[2] = sysUser.getId();
				values[3] = id;
				try{
					updateFlag = DAOProxy.getLibernate().executeSql(updateSql, values);
				}catch (Exception e){
					logger.error("根据站点ID号删除站点出错！" + e);
				}
			}else{
				logger.info("调用soap接口删除站点出错！");
			}
		}
		return updateFlag;
	}

	/**
	 * 根据站点ID号锁定站点
	 * wangyong
	 * 2013-1-9
	 */
	@Override
	public boolean lockSiteById(Integer id) throws Exception {
		boolean updateFlag = false;
		if(id != null && id.intValue()>0){
			String updateSql = "update t_site_base set lock_flag = ? where id = ? ";
			Object[] values = new Object[2];
			values[0] = ConstantUtil.LOCKFLAG_LOCKED;
			values[1] = id;
			try{
				DAOProxy.getLibernate().executeSql(updateSql, values);
				updateFlag = true;
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return updateFlag;
	}

	/**
	 * 根据站点ID号解锁站点
	 * wangyong
	 * 2013-1-9
	 */
	@Override
	public boolean unLockSiteById(Integer id) throws Exception {
		boolean updateFlag = false;
		if(id != null && id.intValue()>0){
			String updateSql = "update t_site_base set lock_flag = ? where id = ? ";
			Object[] values = new Object[2];
			values[0] = ConstantUtil.LOCKFLAG_UNLOCK;
			values[1] = id;
			try{
				DAOProxy.getLibernate().executeSql(updateSql, values);
				updateFlag = true;
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return updateFlag;
	}

	/**
	 * 根据站点名称或标识获取站点信息列表,默认为逆序
	 * @param sortField:以该字段排序
	 * @param sortord:正序（ASC）or逆序（DESC）
	 * wangyong
	 * 2013-1-10
	 */
	@Override
	public List<SiteBase> getSiteListBySiteNameOrSign(String siteNameOrSign,
			String sortField, String sortord, PageModel pageModel) {
		List<SiteBase> siteBaseList = null;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" select * from t_site_base where del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		if(StringUtil.isNotBlank(siteNameOrSign)){
			strSql.append(" and (site_name like ? or site_sign like ? ) ");
			valueList.add("%"+ siteNameOrSign +"%");
			valueList.add("%"+ siteNameOrSign +"%");
		} 
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ").append(sortField);
		}else{ 
			strSql.append(" order by id DESC ");   //查出列表无排序条件则为默认逆序
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
		try{
			Object[] values = valueList.toArray(); 
			siteBaseList = DAOProxy.getLibernate().getEntityListBase(SiteBase.class, strSql.toString(), values);
		}catch (Exception e){
			e.printStackTrace();
		}
		return siteBaseList;
	}
	
	/**
	 * 根据站点名称或标识获取站点信息列表（普通系统管理员）
	 * 
	 * @param siteNameOrSign
	 * @param sortField:排序字段名
	 * @param sortord  : asc  desc
	 * @param pageModel:分页
	 * @param sysUserId:普通系统管理员ID
	 * @return
	 */
	public List<SiteBase> getSiteListBySiteNameOrSign(String siteNameOrSign,
			String sortField, String sortord, PageModel pageModel, int sysUserId){
		List<SiteBase> siteBaseList = null;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" select * from t_site_base where del_flag = ? and create_user = ?");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(sysUserId);
		if(StringUtil.isNotBlank(siteNameOrSign)){
			strSql.append(" and (site_name like ? or site_sign like ? ) ");
			valueList.add("%"+ siteNameOrSign +"%");
			valueList.add("%"+ siteNameOrSign +"%");
		} 
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ").append(sortField);
		}else{ 
			strSql.append(" order by id DESC ");   //查出列表无排序条件则为默认逆序
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
		try{
			Object[] values = valueList.toArray(); 
			siteBaseList = DAOProxy.getLibernate().getEntityListBase(SiteBase.class, strSql.toString(), values);
		}catch (Exception e){
			e.printStackTrace();
		}
		return siteBaseList;
	}

	/**
	 * 统计根据站点名称或者是标识的站点记录数（超级系统管理员）
	 * wangyong
	 * 2013-1-9
	 */
	@Override
	public Integer countSiteListBySiteNameOrSign(String siteNameOrSign) {
		int rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" select count(1) from t_site_base where del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		if(StringUtil.isNotBlank(siteNameOrSign)){
			strSql.append(" and (site_name like ? or site_sign like ? ) ");
			valueList.add("%"+ siteNameOrSign +"%");
			valueList.add("%"+ siteNameOrSign +"%");
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
	 * 统计根据站点名称或者是标识的站点记录数(普通系统管理员)
	 * 
	 * @param siteNameOrSign
	 * @param pageModel
	 * @param sysUserId:普通系统管理员ID
	 * @return
	 */
	public Integer countSiteListBySiteNameOrSign(String siteNameOrSign, int sysUserId){
		int rows = 0;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" select count(1) from t_site_base where del_flag = ? and create_user = ?");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		valueList.add(sysUserId);
		if(siteNameOrSign != null){
			strSql.append(" and (site_name like ? or site_sign like ? ) ");
			valueList.add("%"+ siteNameOrSign +"%");
			valueList.add("%"+ siteNameOrSign +"%");
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
	 * 站点高级搜索列表
	 * wangyong
	 * 2013-1-11
	 */
	@Override
	public List<SiteBase> getSiteListByCondition(Condition condition,
			String sortField, String sortord, PageModel pageModel)
			throws Exception {
		List<SiteBase> siteBaseList = null;
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer strSql = new StringBuffer(" select * from t_site_base where del_flag = ? ");
		valueList.add(ConstantUtil.DELFLAG_UNDELETE);
		if(condition != null && StringUtil.isNotBlank(condition.getConditionSql())){
			strSql.append(" and ").append(condition.getConditionSql());
		} 
		if(StringUtil.isNotBlank(sortField)){
			strSql.append(" order by ").append(sortField);
		}else{ 
			strSql.append(" order by id DESC ");   //查出列表无排序条件则为默认逆序
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
		try{
			Object[] values = valueList.toArray(); 
			siteBaseList = DAOProxy.getLibernate().getEntityListBase(SiteBase.class, strSql.toString(), values);
		}catch (Exception e){
			e.printStackTrace();
		}
		return siteBaseList;
	}

	/**
	 * 统计高级搜索总记录数
	 * wangyong
	 * 2013-1-11
	 */
	@Override
	public Integer countSiteByCondition(Condition condition) throws Exception {
		int rows = 0;
		Object[] values = new Object[1];
		StringBuffer strSql = new StringBuffer(" select count(1) from t_site_base where del_flag = ?");
		values[0] = ConstantUtil.DELFLAG_UNDELETE;
		if(condition != null && StringUtil.isNotBlank(condition.getConditionSql())){
			strSql.append(" and ").append(condition.getConditionSql());
		} 
		try {
			rows = DAOProxy.getLibernate().countEntityListWithSql(strSql.toString(), values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 * 根据站点标识获取站点信息
	 * wangyong
	 * 2013-1-11
	 */
	@Override
	public SiteBase getSiteBaseBySiteSign(String siteSign) {
		SiteBase siteBase = null;
		if(StringUtil.isNotBlank(siteSign)){
			StringBuffer strSql = new StringBuffer(" select * from t_site_base where del_flag = ? and site_sign = ? ");
			Object[] values = new Object[2];
			try {
				values[0] = ConstantUtil.DELFLAG_UNDELETE;
				values[1] = siteSign;
				siteBase = DAOProxy.getLibernate().getEntityCustomized(SiteBase.class, strSql.toString(), values);
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		return siteBase;
	}

	/**
	 * 根据站点ID号获取站点基本信息
	 * wangyong
	 * 2013-1-11
	 */
	@Override
	public SiteBase getSiteBaseById(Integer id) {
		SiteBase siteBase = null;
		if(id != null && id.intValue()>0){
			StringBuffer strSql = new StringBuffer(" select * from t_site_base where del_flag = ? and id = ? ");
			Object[] values = new Object[2];
			try {
				values[0] = ConstantUtil.DELFLAG_UNDELETE;
				values[1] = id;
				siteBase = DAOProxy.getLibernate().getEntityCustomized(SiteBase.class, strSql.toString(), values);
			} catch (SQLException e) {
				logger.error("根据站点ID号获取站点基本信息出错！" + e);
			}			
		}
		return siteBase;
	}

	
	@Override
	public List<SiteBase> getSiteListBySiteIds(Integer[] ids){
		List<SiteBase> list=null;
		if(ids!=null && ids.length>0){
			StringBuffer sqlBuffer=new StringBuffer();
			Object[] values=new Object[ids.length +1];
			sqlBuffer.append("select * from t_site_base where del_flag=? and id > 0 ");
			sqlBuffer.append(" and (");
			values[0]=ConstantUtil.DELFLAG_UNDELETE;
			for(int ii=0;ii<ids.length;ii++){
				if(ii>0){
					sqlBuffer.append(" or ");
				}
				sqlBuffer.append(" id =? ");
				values[ii+1]=ids[ii];
			}
			sqlBuffer.append(")");
			logger.info("sqlBuffer=----"+sqlBuffer.toString());
			try {
				list=libernate.getEntityListBase(SiteBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				sqlBuffer=null;
				values=null;
			}
		}
		return list;
	}
	
	@Override
	public List<SiteBase> getAllSiteListBySiteIds(Integer[] ids){
		List<SiteBase> list=null;
		if(ids!=null && ids.length>0){
			StringBuffer sqlBuffer=new StringBuffer();
			Object[] values=new Object[ids.length];
			sqlBuffer.append("select * from t_site_base where 1=1 and id > 0 ");
			sqlBuffer.append(" and (");
			for(int ii=0;ii<ids.length;ii++){
				if(ii>0){
					sqlBuffer.append(" or ");
				}
				sqlBuffer.append(" id =? ");
				values[ii]=ids[ii];
			}
			sqlBuffer.append(")");
			logger.info("sqlBuffer=----"+sqlBuffer.toString());
			try {
				list=libernate.getEntityListBase(SiteBase.class, sqlBuffer.toString(), values);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				sqlBuffer=null;
				values=null;
			}
		}
		return list;
	}
	
	
	
	/**
	 * 取出当前登录用户所在的站点信息
	 * Dick
	 * 
	 */
	@Override
	public SiteBase getCurrentSiteBaseByUserLogin(HttpServletRequest request) {
		SiteBase siteBase=null;
		if(!loginService.isLogined(request)){
			return siteBase;
		}
		
		UserBase userBase=null;

		String domain = SiteIdentifyUtil.getCurrentBrand()+ "." + SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		String uid = CookieUtil.getCookieByDomain(request, LoginConstants.USER_SESSION_ID_NAME,domain);
		Integer userId=IntegerUtil.parseInteger(uid);
		try {
			userBase = libernate.getEntity(UserBase.class, userId);
			if(userBase!=null && userBase.getSiteId()!=null){
				siteBase=libernate.getEntity(SiteBase.class, userBase.getSiteId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			userBase=null;
			
		}
		
	
		
		return siteBase;
	}
	
	
	
	/**
	 * 取出当前登录站点管理员所在的站点信息
	 * Dick
	 * 
	 */
	@Override
	public SiteBase getCurrentSiteBaseByAdminLogin(HttpServletRequest request) {
		SiteBase siteBase=null;
		if(!loginService.isSiteAdminLogined(request)){
			return siteBase;
		}
		UserBase userBase=null;
		String domain = SiteIdentifyUtil.getCurrentBrand()+ "." + SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		String uid = CookieUtil.getCookieByDomain(request, LoginConstants.SITE_ADMIN_USER_SESSION_ID_NAME,domain);
		Integer userId=IntegerUtil.parseInteger(uid);
		try {
			userBase = libernate.getEntity(UserBase.class, userId);
			if(userBase!=null && userBase.getSiteId()!=null){
				siteBase=libernate.getEntity(SiteBase.class, userBase.getSiteId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			userBase=null;
		}
		return siteBase;
	}
	
	
	@Override
	public String soapAddSite(SiteBase site) {
		String retInfo = ConstantUtil.AS_SUCCESS_CODE;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapBusinessService stub = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
			
			ESpaceMeetingAsEnterprise enterprise = new ESpaceMeetingAsEnterprise();
			enterprise.setEnterpriseId(site.getSiteSign());
			enterprise.setName(site.getSiteName());
			enterprise.setStatus(site.getLockFlag());//锁定/解锁状态
			enterprise.setAudioPort(site.getLicense());
			enterprise.setDataPort(site.getLicense());
			enterprise.setVideoPort(site.getLicense());
			enterprise.setForeverAudioPort(0);
			enterprise.setForeverDataPort(0);
			enterprise.setForeverVideoPort(0);
			enterprise.setIsSupportTP(0);//是否支持智真
			enterprise.setNeedDefaultArea(1);//是否使用默认区域
			
			ESpaceMeetingAsSoapRequestAddEnterpriseRequest request = new ESpaceMeetingAsSoapRequestAddEnterpriseRequest();
			request.setToken(genToken());
			request.setEnterprise(enterprise);
			ESpaceMeetingAsSoapResult result = stub.addEnterprise(request);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				retInfo = ""+result.getErrCode();
				logger.info("result.getErrCode():" + result.getErrCode());
			}
		}catch(Exception e){
			retInfo = "error";
			logger.error("调用soap接口创建站点失败！" + e);
		}
		return retInfo;
	}

	@Override
	public String soapUpdateSite(SiteBase site,boolean flag) {
		String retInfo = ConstantUtil.AS_SUCCESS_CODE;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapBusinessService stub = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
			ESpaceMeetingAsEnterprise enterprise = new ESpaceMeetingAsEnterprise();
			enterprise.setEnterpriseId(site.getSiteSign().toString());
			enterprise.setName(site.getSiteName());
			
			enterprise.setStatus(site.getLockFlag());//锁定/解锁状态
			enterprise.setAudioPort(site.getLicense());
			enterprise.setDataPort(site.getLicense());
			enterprise.setVideoPort(site.getLicense());
			enterprise.setForeverAudioPort(0);
			enterprise.setForeverDataPort(0);
			enterprise.setForeverVideoPort(0);
			enterprise.setIsSupportTP(0);
			enterprise.setNeedDefaultArea(1);
			
			ESpaceMeetingAsSoapRequestUpdateEnterpriseRequest request = new ESpaceMeetingAsSoapRequestUpdateEnterpriseRequest();
			request.setEnterprise(enterprise);
			request.setToken(genToken());
			
			ESpaceMeetingAsSoapRequestAddEnterpriseAreaMapRequest areaRequest = new ESpaceMeetingAsSoapRequestAddEnterpriseAreaMapRequest();
			areaRequest.setToken(genToken());
			
			ESpaceMeetingAsEnterpriseAreaMap enterpiseAreaMap = new ESpaceMeetingAsEnterpriseAreaMap();
			enterpiseAreaMap.setAudioPort(site.getLicense());
			enterpiseAreaMap.setDataPort(site.getLicense());
			enterpiseAreaMap.setVideoPort(site.getLicense());
			enterpiseAreaMap.setIsDefault(1);
			enterpiseAreaMap.setAreaId(getAreaMap(site.getSiteSign()).getAreaId());
			enterpiseAreaMap.setEnterpriseId(site.getSiteSign());
			areaRequest.setEnterpiseAreaMap(enterpiseAreaMap);
			if(flag){
				//先修改站点license
				ESpaceMeetingAsSoapResult result = stub.updateEnterprise(request);// 
				if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
					retInfo = ""+result.getErrCode();
					logger.info("调用华为接口soap修改站点区域设置ErrCode：" + retInfo);
				}else{
					//若修改站点license成功再修改（站点区域）
					result = stub.updateEnterpriseAreaMap(areaRequest);
					if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
						retInfo = ""+result.getErrCode();
						logger.info("调用华为接口soap修改站点返回ErrCode：" + retInfo);
					}
				}
			}else{
				//先修改企业区域license
				ESpaceMeetingAsSoapResult result = stub.updateEnterpriseAreaMap(areaRequest); //result = stub.updateEnterprise(request);// 
				if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
					retInfo = ""+result.getErrCode();
					logger.info("调用华为接口soap修改站点区域设置ErrCode：" + retInfo);
				}else{
					//若修改站点license成功再修改站点license
					result = stub.updateEnterprise(request);
					if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
						retInfo = ""+result.getErrCode();
						logger.info("调用华为接口soap修改站点返回ErrCode：" + retInfo);
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("调用华为接口soap修改站点出错！"+e);
		}
		return retInfo;
	}
	
	@Override
	public String soapDelSite(String siteSign) {
		String retInfo = ConstantUtil.AS_SUCCESS_CODE;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapBusinessService stub = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
			ESpaceMeetingAsSoapRequestRemoveEnterpriseRequest request = new ESpaceMeetingAsSoapRequestRemoveEnterpriseRequest();
			request.setEnterpriseId(siteSign);
			request.setToken(genToken());
			ESpaceMeetingAsSoapResult result = stub.removeEnterprise(request);
			if(result.getErrCode() != ConstantUtil.AS_COMMON_SUCCESS_CODE){
				retInfo = result.getErrCode()+"";
				logger.info("删除站点AS返回错误码：" + retInfo);
			}
		}catch(Exception e){
			retInfo = "error";
			logger.error("调用AS删除站点失败！" + e);
		}
		return retInfo;
	}
	
	public static void testupdateEnterprise(){
		try{
			ESpaceMeetingAsSoapBusinessServicebindingStub stub = new ESpaceMeetingAsSoapBusinessServicebindingStub(new java.net.URL(BUSINESS_URL),null);
			
			ESpaceMeetingAsEnterprise enterprise = new ESpaceMeetingAsEnterprise();
			enterprise.setEnterpriseId("1");
			enterprise.setName("D.wade");
			
			enterprise.setStatus(1);//锁定/解锁状态
			enterprise.setAudioPort(100);
			enterprise.setDataPort(100);
			enterprise.setVideoPort(100);
			enterprise.setForeverAudioPort(0);
			enterprise.setForeverDataPort(0);
			enterprise.setForeverVideoPort(0);
			enterprise.setIsSupportTP(0);
			enterprise.setNeedDefaultArea(1);
			
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone("Aisa/shanghai");
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			ESpaceMeetingAsSoapRequestUpdateEnterpriseRequest request = new ESpaceMeetingAsSoapRequestUpdateEnterpriseRequest();
			request.setEnterprise(enterprise);
			request.setToken(token);
			
			ESpaceMeetingAsSoapResult result = stub.updateEnterprise(request);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				System.out.println(result.getErrCode());
			}else{
				System.out.println("update enterprise success");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testAdd(){
		
		SiteBase site = new SiteBase();
		site.setSiteSign("bizconf");
		site.setSiteName("bizconf");

		site.setLockFlag(1);
		try {
		ESpaceMeetingAsSoapBusinessServicebindingStub stub = new ESpaceMeetingAsSoapBusinessServicebindingStub(new java.net.URL(BUSINESS_URL),null);
		ESpaceMeetingAsEnterprise enterprise = new ESpaceMeetingAsEnterprise();
		enterprise.setEnterpriseId(site.getSiteSign());
		enterprise.setName(site.getSiteName());
		enterprise.setStatus(site.getLockFlag());//锁定/解锁状态
		enterprise.setAudioPort(20);
		enterprise.setDataPort(20);
		enterprise.setVideoPort(20);
		enterprise.setForeverAudioPort(0);
		enterprise.setForeverDataPort(0);
		enterprise.setForeverVideoPort(0);
		enterprise.setIsSupportTP(0);//是否支持智真
		enterprise.setNeedDefaultArea(1);//是否使用默认区域
		
		ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
		token.setAppkey("INTERNAL");
		token.setTimestamp(new Date().getTime());
		token.setArgs(new String[]{"test1"});
		token.setRequestId("bizconf");
		token.setTimezone("Aisa/shanghai");
		token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
		
		ESpaceMeetingAsSoapRequestAddEnterpriseRequest request = new ESpaceMeetingAsSoapRequestAddEnterpriseRequest();
		request.setToken(token);
		request.setEnterprise(enterprise);
		ESpaceMeetingAsSoapResult result = stub.addEnterprise(request);
		//以下是删除
//		ESpaceMeetingAsSoapBusinessServicebindingStub stub = new ESpaceMeetingAsSoapBusinessServicebindingStub(new java.net.URL("http://10.184.130.16:8996/eSpaceMeeting/BusinessService"),null);
//		ESpaceMeetingAsSoapRequestRemoveEnterpriseRequest request = new ESpaceMeetingAsSoapRequestRemoveEnterpriseRequest();
//		request.setEnterpriseId("teste_test");
//		request.setToken(token);
//		ESpaceMeetingAsSoapResult result = stub.removeEnterprise(request);
		
		if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				System.out.println(result.getErrCode());
		}else{
			System.out.println("create enterprise success!");
		}
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void querySites(){
		try {
			
//		EngineConfiguration defaultConfig = EngineConfigurationFactoryFinder.newFactory()
//	                .getClientEngineConfig();
//			SimpleProvider config = new SimpleProvider(defaultConfig);
//			config.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME,
//			                new CommonsHTTPSender());
//			//在构造Service对象或Service对象的子类时，请使用如下的构造函数：
//		ESpaceMeetingCmuLocator locator = new ESpaceMeetingCmuLocator(config);
//		locator.setESpaceMeetingAsSoapConfManagementServiceWSDDServiceName(BUSINESS_URL);
		    
		EngineConfiguration conf = new FileProvider(ConfManagementServiceImpl.class.getResourceAsStream("/client-config.wsdd"));
		ESpaceMeetingCmuLocator locator = new ESpaceMeetingCmuLocator(conf);
		ESpaceMeetingAsSoapBusinessServicebindingStub stub = new ESpaceMeetingAsSoapBusinessServicebindingStub(new java.net.URL(BUSINESS_URL),locator);
		
		ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
		token.setAppkey("INTERNAL");
		token.setTimestamp(new Date().getTime());
		token.setArgs(new String[]{"test1"});
		token.setRequestId("bizconf");
		token.setTimezone("Aisa/shanghai");
		token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
		
		//查询企业
		ESpaceMeetingAsSoapRequestQueryEnterpriseListRequest req = new ESpaceMeetingAsSoapRequestQueryEnterpriseListRequest();
		req.setToken(token);
		ESpaceMeetingAsSoapResponseQueryEnterpriseListResponse resp = new ESpaceMeetingAsSoapResponseQueryEnterpriseListResponse();
		ESpaceMeetingAsSoapResponseQueryEnterpriseListResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryEnterpriseListResponseHolder(resp);
		ESpaceMeetingAsSoapResult result = stub.queryEnterpriseList(req, holder);
		ESpaceMeetingAsEnterprise[] sites = holder.value.getEnterpriseList();
			for (int i = 0; i < sites.length; i++) {
				System.out.println(sites[i].getEnterpriseId()); 
				//站点标识
			///	testDelEnterprise(sites[i].getEnterpriseId());
//				System.out.println(sites[i].getName());
//				System.out.println(sites[i].getAudioPort());    //license个数
//				System.out.println(sites[i].getDataPort());
//				System.out.println(sites[i].getVideoPort());
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testDelEnterprise(String eid){
		try{
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone("Aisa/shanghai");
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			//以下是删除
			ESpaceMeetingAsSoapBusinessServicebindingStub stub = new ESpaceMeetingAsSoapBusinessServicebindingStub(new java.net.URL(BUSINESS_URL),null);
			ESpaceMeetingAsSoapRequestRemoveEnterpriseRequest request = new ESpaceMeetingAsSoapRequestRemoveEnterpriseRequest();
			request.setEnterpriseId(eid);
			request.setToken(token);
			ESpaceMeetingAsSoapResult result = stub.removeEnterprise(request);
			
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				System.out.println(result.getErrCode());
			}else{
				System.out.println("del enterprise success");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		//testupdateEnterprise();           //修改站点

		//testDelEnterprise("1");     //测试通过站点标识删除as的站点
		
		//testupdateEnterprise();
//		SiteService ss = AppContextFactory.getAppContext().getBean(SiteService.class);
//		SiteBase siteBase = ss.queryASSiteInfo("meeting");
//		System.out.println(siteBase.getSiteName());
//		System.out.println(siteBase.getLicense());
		
		
		SiteServiceImpl ss = new SiteServiceImpl();
		System.out.println(ss.getAreaMap("meeting").getAreaId());
	}

	@Override
	public SiteBase queryASSiteInfo(String siteSign) {
		SiteBase site = null;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapBusinessService stub = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
			ESpaceMeetingAsSoapRequestQueryEnterpriseRequest request = new ESpaceMeetingAsSoapRequestQueryEnterpriseRequest();
			ESpaceMeetingAsSoapResponseQueryEnterpriseResponse resp = new ESpaceMeetingAsSoapResponseQueryEnterpriseResponse();
			ESpaceMeetingAsSoapResponseQueryEnterpriseResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryEnterpriseResponseHolder(resp);
			request.setEnterpriseId(siteSign);
			request.setToken(genToken());
			ESpaceMeetingAsSoapResult result = stub.queryEnterprise(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				logger.info("调用华为接口soap修改站点返回ErrCode：" + result.getErrCode());
			}else{
				ESpaceMeetingAsEnterprise enterprise = holder.value.getEnterprise();
				site = new SiteBase();
				site.setLicense(enterprise.getAudioPort());
				
				System.out.println("dataPort:  "+enterprise.getDataPort());
				site.setSiteName(enterprise.getName());
				site.setSiteSign(siteSign);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("调用华为接口soap查询站点异常！"+e);
		}
		return site;
	}
	
	@Override
	public int queryLicenseUseNum() {
		int licnum = 0; 
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapBusinessService stub = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
			ESpaceMeetingAsSoapRequestQueryEnterpriseListRequest req = new ESpaceMeetingAsSoapRequestQueryEnterpriseListRequest();
			req.setToken(genToken());
			ESpaceMeetingAsSoapResponseQueryEnterpriseListResponse resp = new ESpaceMeetingAsSoapResponseQueryEnterpriseListResponse();
			ESpaceMeetingAsSoapResponseQueryEnterpriseListResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryEnterpriseListResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.queryEnterpriseList(req, holder);
			
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				ESpaceMeetingAsEnterprise[] sites = holder.value.getEnterpriseList();
				for (int i = 0; i < sites.length; i++) {
						
					licnum += sites[i].getAudioPort();
				}
			}else{
				logger.error("query license sum number error, errorCode:"+result.getErrCode());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return licnum;
	}
	
	/**
	 * 获取企业区域
	 * @param siteSign
	 * @return
	 */
	private ESpaceMeetingAsEnterpriseAreaMap getAreaMap(String siteSign){
		
		ESpaceMeetingAsEnterpriseAreaMap areaMap = null;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapBusinessService stub = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
			
			ESpaceMeetingAsSoapRequestQueryEnterpriseAreaMapListRequest request = new ESpaceMeetingAsSoapRequestQueryEnterpriseAreaMapListRequest();
			request.setEnterpriseId(siteSign);
			request.setToken(genToken());
			
			ESpaceMeetingAsSoapResponseQueryEnterpriseAreaMapListResponse resp = new ESpaceMeetingAsSoapResponseQueryEnterpriseAreaMapListResponse();
			ESpaceMeetingAsSoapResponseQueryEnterpriseAreaMapListResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryEnterpriseAreaMapListResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.queryEnterpriseAreaMapList(request, holder);
		
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				logger.error("query license sum number error, errorCode:"+result.getErrCode());
			}else if(holder.value.getEnterpriseAreaMapList().length>0){
						areaMap = holder.value.getEnterpriseAreaMapList()[0];
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("调用华为接口soap查询站点异常！"+e);
		}
		
		return areaMap;
	}

	@Override
	public PageBean<SiteBase> queryASAllSites(int pageNo, int pageSize) {
		
		PageBean<SiteBase> page = new PageBean<SiteBase>();
		List<SiteBase> sitelists = new ArrayList<SiteBase>();
		if(pageNo<1) pageNo = 1;
		int start = (pageNo-1)*pageSize;
		int end = start+pageSize;
		try {
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapBusinessService stub = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
			//查询企业
			ESpaceMeetingAsSoapRequestQueryEnterpriseListRequest req = new ESpaceMeetingAsSoapRequestQueryEnterpriseListRequest();
			req.setToken(genToken());
			ESpaceMeetingAsSoapResponseQueryEnterpriseListResponse resp = new ESpaceMeetingAsSoapResponseQueryEnterpriseListResponse();
			ESpaceMeetingAsSoapResponseQueryEnterpriseListResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryEnterpriseListResponseHolder(resp);
			stub.queryEnterpriseList(req, holder);
			ESpaceMeetingAsEnterprise[] sites = holder.value.getEnterpriseList();
			if(sites.length>end){
				for (int i = start; i < end; i++) {
					SiteBase site = new SiteBase();
					site.setSiteSign(sites[i].getEnterpriseId());
					site.setSiteName(sites[i].getName());
					site.setLicense(sites[i].getDataPort());
					sitelists.add(site);
				}
			}else if(sites.length>=start && sites.length<=end){
				
				for (int i = start; i < sites.length; i++) {
					SiteBase site = new SiteBase();
					site.setSiteSign(sites[i].getEnterpriseId());
					site.setSiteName(sites[i].getName());
					site.setLicense(sites[i].getDataPort());
					sitelists.add(site);
				}
			}
			page.setDatas(sitelists);
			page.setPageNo(pageNo+"");
			page.setRowsCount(sites.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
}
