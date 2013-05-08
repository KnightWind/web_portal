package com.bizconf.audio.action.system;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.constant.SortConstant;
import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SystemUserInterceptor;
import com.bizconf.audio.logic.SiteAdminLogic;
import com.bizconf.audio.service.EmailConfigService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.EmailTemplateService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.LicService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.BeanUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.JsonUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Get;
import com.libernate.liberc.annotation.httpmethod.Post;

/**
 * 系统管理员操作站点controller
 * @author wangyong
 * @date 2013.1.15
 */
@ReqPath("site")
@Interceptors({SystemUserInterceptor.class})
public class SiteController extends BaseController{
	private final  Logger logger = Logger.getLogger(SiteController.class);
	
	@Autowired
	SiteService siteService;
	@Autowired
	EmailConfigService emailConfigService;
	@Autowired
	EmailTemplateService emailTemplateService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	EmailService emailService;
	@Autowired
	UserService userService;
	@Autowired
	SiteAdminLogic siteAdminLogic;
	@Autowired
	EmpowerConfigService empowerConfigService;
	@Autowired
	LicService licService;
	
	/**
	 * 获取全部站点信息列表
	 * wangyong
	 * 2013-1-16
	 * 
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "list")
	public Object list(PageModel pageModel, HttpServletRequest request) throws Exception{
		SystemUser  currentSysUser = userService.getCurrentSysAdmin(request);
		List<SiteBase> siteList = null;
		Integer rows = 0;
		Condition condition = null;
		if(!currentSysUser.isSuperSystemAdmin() && !currentSysUser.isSystemClientServer()){
			condition = new Condition();
			condition.equal("create_user", currentSysUser.getId().intValue());       //普通系统管理员只能查看自己创建的站点
		}
		try {
			rows = siteService.countSiteByCondition(condition);
			logger.info("rows=="+rows);
		} catch (Exception e) {
			logger.error("获取页数出错!"+e);
		}
		if(rows != null ){
			pageModel.setRowsCount(rows);
		}else{
			pageModel.setRowsCount(0);
		}
		try {
			siteList = siteService.getSiteListByCondition(condition, null, null, pageModel);
			siteList = ObjectUtil.parseHtmlWithList(siteList, "siteName", "siteSign");      //数据库的数据转为实际字符
		} catch (Exception e) {
			logger.error("获取全部站点信息列表出错!"+e);
		}
		List<UserBase> supperAdminList=getUserListBySiteList(siteList);
		siteList = getEffeLicense(siteList);
		request.setAttribute("siteList", siteList);
		request.setAttribute("supperAdminList", supperAdminList);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("user", currentSysUser);
		return new ActionForward.Forward("/jsp/system/site_list.jsp");
	}

	/**
	 * 根据站点名称或标识获取站点信息列表
	 * wangyong
	 * 2013-1-16
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "listWithSignOrName")
	public Object listWithSignOrName(@CParam("nameOrSign")  String nameOrSign, PageModel pageModel, HttpServletRequest request) throws Exception{
		logger.info("nameOrSign=="+nameOrSign);
		SystemUser  currentSysUser = userService.getCurrentSysAdmin(request);
		List<SiteBase> siteList = null;
		String sortFieldValue = request.getParameter("sortField");
		String sortField = initSort(sortFieldValue);     //获取页面传递的排序参数
		String sortordValue = request.getParameter("sortord");
		String sortord = "desc";
		if(SortConstant.SORT_ASC.equals(sortordValue)){
			sortord = "asc";
		}
		Integer rows = 0;
		try {
			if(currentSysUser.isSuperSystemAdmin() || currentSysUser.isSystemClientServer()){
				rows = siteService.countSiteListBySiteNameOrSign(nameOrSign);
			}else{
				rows = siteService.countSiteListBySiteNameOrSign(nameOrSign, currentSysUser.getId());
			}
		} catch (Exception e) {
			logger.error("获取页数出错!"+e);
		}
		if(rows != null ){
			pageModel.setRowsCount(rows);
		}else{
			pageModel.setRowsCount(0);
		}
		try {
			if(currentSysUser.isSuperSystemAdmin()||currentSysUser.isSystemClientServer()){
				siteList = siteService.getSiteListBySiteNameOrSign(nameOrSign, sortField, sortord, pageModel);
			}else{
				siteList = siteService.getSiteListBySiteNameOrSign(nameOrSign, sortField, sortord, pageModel, currentSysUser.getId());
			}
			siteList = ObjectUtil.parseHtmlWithList(siteList, "siteName", "siteSign");      //数据库的数据转为实际字符
		} catch (Exception e) {
			logger.error("根据站点名称或标识获取站点信息列表出错!"+e);
		}
		List<UserBase> supperAdminList=getUserListBySiteList(siteList);
		siteList = getEffeLicense(siteList);
		request.setAttribute("siteList", siteList);
		request.setAttribute("supperAdminList", supperAdminList);
		request.setAttribute("nameOrSign", nameOrSign);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortFieldValue);   //传排序字段的编号
		request.setAttribute("sortord", sortordValue);       //传排序方式的编号
		request.setAttribute("user", currentSysUser);
		return new ActionForward.Forward("/jsp/system/site_list.jsp");
	}
 	
	/**
	 * 站点高级搜索获取站点信息列表
	 * wangyong
	 * 2013-1-16
	 */
	@SuppressWarnings("unchecked")
	@Post
	@Get
	@AsController(path = "listWithCondition")
	public Object listWithCondition(SiteBase siteBase,PageModel pageModel, HttpServletRequest request) throws Exception{
		List<SiteBase> siteList = null;
		SystemUser  currentSysUser = userService.getCurrentSysAdmin(request);
		Condition condition = initCondition(siteBase, request);
		String sortFieldValue = request.getParameter("sortField");
		String sortField = initSort(sortFieldValue);     //获取页面传递的排序参数
		String sortordValue = request.getParameter("sortord");
		String sortord = "desc";
		if(!currentSysUser.isSuperSystemAdmin() && !currentSysUser.isSystemClientServer()){
			condition.equal("create_user", currentSysUser.getId().intValue());   //普通系统管理员只能查看自己创建的站点
		}
		if(SortConstant.SORT_ASC.equals(sortordValue)){
			sortord = "asc";
		}
		Integer rows = 0;
		try {
			rows = siteService.countSiteByCondition(condition);
		} catch (Exception e) {
			logger.error("获取页数出错!"+e);
		}
		if(rows != null ){
			pageModel.setRowsCount(rows);
		}else{
			pageModel.setRowsCount(0);
		}
		try {
			siteList = siteService.getSiteListByCondition(condition, sortField, sortord, pageModel);
			siteList = ObjectUtil.parseHtmlWithList(siteList, "siteName", "siteSign");      //数据库的数据转为实际字符
		} catch (Exception e) {
			logger.error("站点高级搜索获取站点信息列表出错!"+e);
		}
		List<UserBase> supperAdminList=getUserListBySiteList(siteList);
		request.setAttribute("siteList", siteList);
		request.setAttribute("supperAdminList", supperAdminList);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortFieldValue);   //传排序字段的编号
		request.setAttribute("sortord", sortordValue);       //传排序方式的编号
		sortAttribute(siteBase, request);                    //向前台传递高级搜索表单值
		request.setAttribute("user", currentSysUser);
		return new ActionForward.Forward("/jsp/system/site_list.jsp");
	}
	
	/**
	 * 创建站点
	 * 说明：
	 * 1. @Post   禁止浏览器通过http://localhost:8080/system/site/create直接访问该controller
	 * 2. 新建站点，站点标识不可重复
	 * 3. 新建站点，要给站点管理员发封邮件
	 * 4. 校验数据放到service的logic中
	 * wangyong
	 * 2013-1-15
	 */
	@Post
	@AsController(path = "create")
	public Object saveSite(@CParam("data") String data, EmpowerConfig empowerConfig, HttpServletRequest request) throws Exception{
		Object[] siteAndUser = JsonUtil.parseObjectArrWithJsonString(data);
		SiteBase siteBase = (SiteBase) JsonUtil.parseObjectWithJsonString(siteAndUser[0].toString(), SiteBase.class);
		siteBase.setTimeZone(getTimeZone(siteBase.getTimeZoneId()));
		UserBase siteAdmin = (UserBase) JsonUtil.parseObjectWithJsonString(siteAndUser[1].toString(), UserBase.class);
		logger.info("SiteBase-----"+siteBase+";siteAdmin-----"+siteAdmin);
		SystemUser systemUser = userService.getCurrentSysAdmin(request);
		JSONObject json = new JSONObject();
		JSONArray jsonArrSite = new JSONArray();
		JSONArray jsonArrUser = new JSONArray();
		SiteBase site = null;
		
		String loginPass = ""; //获取密码发送邮件时用
		if(siteBase != null){
			if(StringUtil.isNotBlank(siteBase.getSiteSign())){
				siteBase = (SiteBase)ObjectUtil.parseHtml(siteBase, "siteName", "siteSign");	//字符转义
				if("false".equals(siteSignValidate(siteBase.getSiteSign(), 0))){
					return returnJsonStr(ConstantUtil.CREATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.list.siteSign.remote"));
				}
			}
			if(StringUtil.isNotBlank(siteAdmin.getUserEmail())){
				if("false".equals(emailValidate(siteAdmin.getUserEmail(), 0, 0))){
					return returnJsonStr(ConstantUtil.CREATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.list.userEmail.remote"));
				}
			}
			siteBase = initSiteObject(siteBase, systemUser);	//初始化siteBase对象
			siteAdmin = initSiteAdmin(siteAdmin, systemUser);    //初始化UserBase对象
			empowerConfig.initEmpower(systemUser);
			if(StringUtil.isNotBlank(siteAdmin.getLoginPass())){
				loginPass = siteAdmin.getLoginPass();
				if(loginPass.equals(siteAdmin.getLoginName())){
					return returnJsonStr(ConstantUtil.CREATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.list.loginNmaePasswd.remote"));
				}
				siteAdmin.setLoginPass(MD5.encodePassword(loginPass, "MD5"));
			}
			try{
				site = siteService.createSite(siteBase, siteAdmin, empowerConfig);
			}catch(Exception e){
				return returnJsonStr(ConstantUtil.CREATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.meaasge.create.failed"));
			}
			try{
				if(site != null && site.getId() != null && site.getId().intValue() > 0){
					emailConfigService.copyConfigFromDefault(site.getId());    //创建成功后COPY 邮件HOST设置
					emailTemplateService.copySiteTemplateFromDefault(site.getId());   //创建成功后COPY 邮件模板设置
					
					siteAdmin.setLoginPass(loginPass);//设置加密前的密码以便发送邮件
					emailService.createSiteEmail(site,siteAdmin);//创建成功后发送邮件
					
					eventLogService.saveSystemEventLog(systemUser, 
							EventLogConstants.SYSTEM_SITE_CREATE, ResourceHolder.getInstance().getResource("system.site.meaasge.create.succeed"), 
							EventLogConstants.EVENTLOG_SECCEED, site, request);   //创建成功后写EventLog
					logger.info("创建站点成功");
				}else{
					String errorMessage = getSiteErrorMessage(site);//取出返回Error信息内容
					eventLogService.saveSystemEventLog(systemUser, 
							EventLogConstants.SYSTEM_SITE_CREATE, errorMessage, 
							EventLogConstants.EVENTLOG_FAIL, siteBase, request);   //创建失败后写EventLog
					return returnJsonStr(ConstantUtil.CREATESITE_FAIL, errorMessage);
				}
			}catch(Exception e){
				eventLogService.saveSystemEventLog(systemUser, 
						EventLogConstants.SYSTEM_SITE_CREATE, ResourceHolder.getInstance().getResource("system.site.meaasge.create.failed"), 
						EventLogConstants.EVENTLOG_FAIL, site, request);   //创建失败后写EventLog
			}
			site.setEffeDate(DateUtil.getBejingDateByGmtDate(site.getEffeDate()));   //将数据库中的GMT时间转换为北京时间
			site.setExpireDate(DateUtil.getBejingDateByGmtDate(site.getExpireDate()));
			json.put("status", ConstantUtil.CREATESITE_SUCCEED);
			jsonArrSite.add(JsonUtil.parseJsonWithFullFieldByObject(site));
			jsonArrUser.add(JsonUtil.parseJsonWithFullFieldByObject(siteAdmin));
			json.put("siteBase", jsonArrSite);
			json.put("siteAdmin", jsonArrUser);
		}
		return json.toString();
	}
	
	/**
	 * 创建站点
	 * wangyong
	 * 2013-1-15
	 */
	@AsController(path = "new")
	public Object createSite() throws Exception{
		return new ActionForward.Forward("/jsp/system/createSite.jsp");
	}
	
	/**
	 * 系统管理员修改站点
	 * 说明：
	 * 1. @Post   禁止浏览器通过http://localhost:8080/system/site/update直接访问该controller
	 * 2. 修改站点，要给站点管理员发封邮件(判断页面的选择条件，是否发送邮件)
	 * 3. 目前为测试版本
	 * 4. 校验数据放到service的logic中
	 * 5. 校验修改的站点管理员loginname是否已存在（同一站点下的其他站点管理员是否重复）
	 * wangyong
	 * 2013-1-15
	 */
	@AsController(path = "update")
	@Post
	public Object updateSite(@CParam("data") String data, EmpowerConfig empowerConfig, HttpServletRequest request){
		Object[] siteAndUser = JsonUtil.parseObjectArrWithJsonString(data);
		SiteBase siteBase = (SiteBase) JsonUtil.parseObjectWithJsonString(siteAndUser[0].toString(), SiteBase.class);
		siteBase.setTimeZone(getTimeZone(siteBase.getTimeZoneId()));
		UserBase siteAdmin = (UserBase) JsonUtil.parseObjectWithJsonString(siteAndUser[1].toString(), UserBase.class);
		JSONObject json = new JSONObject();
		JSONArray jsonArrSite = new JSONArray();
		JSONArray jsonArrUser = new JSONArray();
		SiteBase site = null;
		if(siteBase != null){
			SystemUser systemUser = userService.getCurrentSysAdmin(request);
			if(StringUtil.isNotBlank(siteBase.getSiteSign())){
				siteBase = (SiteBase)ObjectUtil.parseHtml(siteBase, "siteName", "siteSign");	//字符转义
				if("false".equals(siteSignValidate(siteBase.getSiteSign(), siteBase.getId()))){
					return returnJsonStr(ConstantUtil.UPDATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.list.siteSign.remote"));
				}
			}
			if(StringUtil.isNotBlank(siteAdmin.getUserEmail())){
				if("false".equals(loginNameValidate(siteAdmin.getLoginName(), siteAdmin.getId(), siteBase.getId()))){
					return returnJsonStr(ConstantUtil.CREATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.list.loginName.remote"));
				}
			}
			if(StringUtil.isNotBlank(siteAdmin.getUserEmail())){
				if("false".equals(emailValidate(siteAdmin.getUserEmail(), siteAdmin.getId(), siteBase.getId()))){
					return returnJsonStr(ConstantUtil.CREATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.list.userEmail.remote"));
				}
			}
			if(StringUtil.isNotBlank(siteAdmin.getLoginPass())){
				String loginPass = siteAdmin.getLoginPass();
				if(loginPass.equals(siteAdmin.getLoginName())){
					return returnJsonStr(ConstantUtil.CREATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.list.loginNmaePasswd.remote"));
				}
				siteAdmin.setLoginPass(MD5.encodePassword(siteAdmin.getLoginPass(), "MD5"));
			}
			try {
				empowerConfig.initEmpower(systemUser);
				if(siteBase.getChargeMode() == SiteConstant.SITE_CHARGEMODE_NAMEHOST){
					siteBase.setLicense(0);
				}
				//boolean updatetoAs = siteService.soapUpdateSite(site);
				site = siteService.updateSiteBaseForSystem(siteBase, siteAdmin, empowerConfig);
			}catch (Exception e){
				return returnJsonStr(ConstantUtil.UPDATESITE_FAIL, ResourceHolder.getInstance().getResource("system.site.meaasge.update.failed"));
			}
			try{
				if(site != null && site.getId() != null && site.getId().intValue() > 0){
					eventLogService.saveSystemEventLog(systemUser, 
							EventLogConstants.SYSTEM_SITE_UPDATE, ResourceHolder.getInstance().getResource("system.site.meaasge.update.succeed"), 
							EventLogConstants.EVENTLOG_SECCEED, siteBase, request);   //修改成功后写EventLog
				}else{
					String errorMessage = getSiteErrorMessage(site);
					eventLogService.saveSystemEventLog(systemUser, 
							EventLogConstants.SYSTEM_SITE_UPDATE, errorMessage, 
							EventLogConstants.EVENTLOG_FAIL, siteBase, request);   //修改失败后写EventLog
					return returnJsonStr(ConstantUtil.UPDATESITE_FAIL, errorMessage);
				}
			} catch (Exception e) {
				eventLogService.saveSystemEventLog(systemUser, 
						EventLogConstants.SYSTEM_SITE_UPDATE, ResourceHolder.getInstance().getResource("system.site.meaasge.update.failed"), 
						EventLogConstants.EVENTLOG_FAIL, siteBase, request);   //修改失败后写EventLog
			}
			site.setEffeDate(DateUtil.getBejingDateByGmtDate(site.getEffeDate()));   //将数据库中的GMT时间转换为北京时间
			site.setExpireDate(DateUtil.getBejingDateByGmtDate(site.getExpireDate()));
			json.put("status", ConstantUtil.UPDATESITE_SUCCEED);
			jsonArrSite.add(JsonUtil.parseJsonWithFullFieldByObject(site));
			jsonArrUser.add(JsonUtil.parseJsonWithFullFieldByObject(siteAdmin));
			json.put("siteBase", jsonArrSite);
			json.put("siteAdmin", jsonArrUser);
		}
		return json.toString();
	}
	
	/**
	 * 系统管理员修改站点
	 * wangyong
	 * 2013-1-15
	 */
	@AsController(path = "update/{id:([0-9]+)}")
	@Get
	public Object updatePage(@CParam("id") Integer id,HttpServletRequest request) throws Exception{
		logger.info("id=="+id);
		SiteBase siteBase = siteService.getSiteBaseById(id);
		Integer[] siteIds = new Integer[1];
		siteIds[0] = siteBase.getId();
		List<UserBase> userList = userService.getSiteSupperMasterBySiteIdArray(siteIds);
		siteBase.setLicense(licService.getSiteLicenseNum(id) + siteBase.getLicense().intValue());
		request.setAttribute("siteBase", siteBase);
		if(userList != null && userList.size() > 0){
			request.setAttribute("siteAdmin", userList.get(0));
		}
		request.setAttribute("empowerConfig", empowerConfigService.getSiteEmpowerConfigBySiteId(siteBase.getId()));
		return new ActionForward.Forward("/jsp/system/createSite.jsp");
	}
	
	/**
	 * 预览站点
	 * wangyong
	 * 2013-1-15
	 */
	@AsController(path = "view/{id:([0-9]+)}")
	@Get
	public Object viewSite(@CParam("id") Integer id,HttpServletRequest request) throws Exception{
		logger.info("id=="+id);
		SiteBase siteBase = siteService.getSiteBaseById(id);
		Integer[] siteIds = new Integer[1];
		siteIds[0] = siteBase.getId();
		List<UserBase> userList = userService.getSiteSupperMasterBySiteIdArray(siteIds);
		UserBase admin = null;
		if(userList!=null && userList.size()>0){
			admin = userList.get(0);
		}
		siteBase.setLicense(licService.getSiteLicenseNum(id) + siteBase.getLicense().intValue());
		request.setAttribute("siteBase", siteBase);
		request.setAttribute("siteAdmin", admin);
		return new ActionForward.Forward("/jsp/system/viewSite.jsp");
	}
	
	@AsController(path = "siteCreateSuccess/{id:([0-9]+)}")
	@Get
	public Object siteCreateSuccess(@CParam("id") Integer id,HttpServletRequest request) throws Exception{
		logger.info("id=="+id);
		SiteBase siteBase = siteService.getSiteBaseById(id);
		Integer[] siteIds = new Integer[1];
		siteIds[0] = siteBase.getId();
		List<UserBase> userList = userService.getSiteSupperMasterBySiteIdArray(siteIds);
		request.setAttribute("siteBase", siteBase);
		request.setAttribute("siteAdmin", userList.get(0));
		return new ActionForward.Forward("/jsp/system/createSiteSuccess.jsp");
	}	
	
	@AsController(path = "siteUpdateSuccess/{id:([0-9]+)}")
	@Get
	public Object siteUpdateSuccess(@CParam("id") Integer id,HttpServletRequest request) throws Exception{
		logger.info("id=="+id);
		SiteBase siteBase = siteService.getSiteBaseById(id);
		Integer[] siteIds = new Integer[1];
		siteIds[0] = siteBase.getId();
		List<UserBase> userList = userService.getSiteSupperMasterBySiteIdArray(siteIds);
		request.setAttribute("siteBase", siteBase);
		request.setAttribute("siteAdmin", userList.get(0));
		return new ActionForward.Forward("/jsp/system/updateSiteSuccess.jsp");
	}	

	/**
	 * 锁定站点
	 * wangyong
	 * 2013-1-15
	 */
	@AsController(path = "lock/{id:([0-9]+)}")
	public Object lockSite(@CParam("id") Integer id, @CParam("nameOrSign") String nameOrSign, SiteBase siteBase,  HttpServletRequest request) throws Exception{
		logger.info("id=="+id+"nameOrSign=="+nameOrSign);
		SystemUser systemUser = userService.getCurrentSysAdmin(request);
		boolean lockFlag = siteService.lockSiteById(id);
		int lockStatus = ConstantUtil.LOCKSITE_SUCCEED;
		if(lockFlag){
			eventLogService.saveSystemEventLog(systemUser, 
					EventLogConstants.SYSTEM_SITE_LOCK, ResourceHolder.getInstance().getResource("system.site.lock." + lockStatus), 
					EventLogConstants.EVENTLOG_SECCEED, id, request);   //锁定站点成功后写EventLog
			setInfoMessage(request, ResourceHolder.getInstance().getResource("system.site.lock." + lockStatus));
		}else{
			lockStatus = ConstantUtil.LOCKSITE_FAIL;
			eventLogService.saveSystemEventLog(systemUser, 
					EventLogConstants.SYSTEM_SITE_LOCK, ResourceHolder.getInstance().getResource("system.site.lock." + lockStatus), 
					EventLogConstants.EVENTLOG_FAIL, id, request);   //锁定站点失败后写EventLog
			setErrMessage(request, ResourceHolder.getInstance().getResource("system.site.lock." + lockStatus));
		}
		if(StringUtil.isNotBlank(nameOrSign)){
			request.setAttribute("nameOrSign", nameOrSign);
			return new ActionForward.Forward("/system/site/listWithSignOrName");
		}else if(conditionSearch(siteBase, request)){
			return new ActionForward.Forward("/system/site/listWithCondition");
		}
		return new ActionForward.Forward("/system/site/list");
	}
	
	/**
	 * 解锁站点
	 * wangyong
	 * 2013-1-15
	 */
	@AsController(path = "unlock/{id:([0-9]+)}")
	public Object unLockSite(@CParam("id") Integer id, @CParam("nameOrSign") String nameOrSign, SiteBase siteBase,  HttpServletRequest request) throws Exception{
		logger.info("id=="+id+"nameOrSign=="+nameOrSign);
		SystemUser systemUser = userService.getCurrentSysAdmin(request);
		boolean unlockFlag = siteService.unLockSiteById(id);
		int unlockStatus = ConstantUtil.UNLOCKSITE_SUCCEED;
		if(unlockFlag){
			eventLogService.saveSystemEventLog(systemUser, 
					EventLogConstants.SYSTEM_SITE_UNLOCK, ResourceHolder.getInstance().getResource("system.site.unlock." + unlockStatus), 
					EventLogConstants.EVENTLOG_SECCEED, id, request);   //解锁站点成功后写EventLog
			setInfoMessage(request, ResourceHolder.getInstance().getResource("system.site.unlock." + unlockStatus));
		}else{
			unlockStatus = ConstantUtil.UNLOCKSITE_FAIL;
			eventLogService.saveSystemEventLog(systemUser, 
					EventLogConstants.SYSTEM_SITE_UNLOCK, ResourceHolder.getInstance().getResource("system.site.unlock." + unlockStatus), 
					EventLogConstants.EVENTLOG_FAIL, id, request);   //解锁站点失败后写EventLog
			setErrMessage(request, ResourceHolder.getInstance().getResource("system.site.unlock." + unlockStatus));
		}
		if(StringUtil.isNotBlank(nameOrSign)){
			request.setAttribute("nameOrSign", nameOrSign);
			return new ActionForward.Forward("/system/site/listWithSignOrName");
		}else if(conditionSearch(siteBase, request)){
			return new ActionForward.Forward("/system/site/listWithCondition");
		}
		return new ActionForward.Forward("/system/site/list");
	}
	
	/**
	 * 删除站点
	 * wangyong
	 * 2013-1-16
	 */
	@AsController(path = "delete/{id:([0-9]+)}")
	public Object delSite(@CParam("id") Integer id, @CParam("nameOrSign") String nameOrSign, SiteBase siteBase,  HttpServletRequest request) throws Exception{
		logger.info("id=="+id);
		SystemUser systemUser = userService.getCurrentSysAdmin(request);
		boolean delFlag = siteService.deleteSiteById(id, systemUser);
		int delStatus = ConstantUtil.DELSITE_SUCCEED;
		if(delFlag){
			//emailService.delSiteEmail(id);//站点删除成功发送邮件通知
			eventLogService.saveSystemEventLog(systemUser, 
					EventLogConstants.SYSTEM_SITE_DELETE, ResourceHolder.getInstance().getResource("system.site.delete." + delStatus), 
					EventLogConstants.EVENTLOG_SECCEED, id, request);   //删除站点成功后写EventLog
			setInfoMessage(request, ResourceHolder.getInstance().getResource("system.site.delete." + delStatus));
		}else{
			delStatus = ConstantUtil.DELSITE_FAIL;
			eventLogService.saveSystemEventLog(systemUser, 
					EventLogConstants.SYSTEM_SITE_DELETE, ResourceHolder.getInstance().getResource("system.site.delete." + delStatus), 
					EventLogConstants.EVENTLOG_FAIL, id, request);   //删除站点失败后写EventLog
			setErrMessage(request, ResourceHolder.getInstance().getResource("system.site.delete." + delStatus));
		}
		if(StringUtil.isNotBlank(nameOrSign)){
			request.setAttribute("nameOrSign", nameOrSign);
			return new ActionForward.Forward("/system/site/listWithSignOrName");
		}else if(conditionSearch(siteBase,request)){
			return new ActionForward.Forward("/system/site/listWithCondition");
		}
		return new ActionForward.Forward("/system/site/list");
	}
	
	
	/**
	 * 创建(修改)站点时验证站点标识是否已存在
	 * return true(不存在) false(已存在)
	 * wangyong
	 * 2013-1-31
	 */
	@Post
	@AsController(path = "siteSignValidate")
	public String siteSignValidate(@CParam("siteSign") String siteSign, @CParam("siteId") int siteId){
		String flag = "true";
		if(StringUtil.isNotBlank(siteSign)){
			SiteBase site = siteService.getSiteBaseBySiteSign(siteSign);
			if(site != null && siteId == 0){    //创建站点
				logger.info("该站点标识"+siteSign+"已存在!");
				flag = "false";
			}else if(site != null && siteId != 0 && site.getId().intValue() != siteId){    //修改站点
				logger.info("该站点标识"+siteSign+"已存在!");
				flag = "false";
			}
		}
		return flag;
	}
	
	/**
	 * 修改站点时验证站点管理员登录名是否已存在
	 * 注：站点管理员登录名只针对该站点下的管理员验证，所以创建站点无需验证登录名是否已存在
	 * return true(不存在) false(已存在)
	 * wangyong
	 * 2013-1-31
	 */
	@Post
	@AsController(path = "loginNameValidate")
	public String loginNameValidate(@CParam("loginName") String loginName, @CParam("userId") int userId, @CParam("siteId") int siteId){
		String flag = "true";
		if(StringUtil.isNotBlank(loginName)){
			UserBase admin = siteAdminLogic.getSiteAdmin(siteId, loginName);
			if(admin != null && userId != 0 && admin.getId().intValue() != userId){    //修改站点
				//验证同一站点下的站点管理员（包括超级站点管理员与普通站点管理员）是否登录名已存在
				int userType = admin.getUserType().intValue();
				if(userType == ConstantUtil.USERTYPE_ADMIN.intValue() || userType == ConstantUtil.USERTYPE_ADMIN_SUPPER.intValue()){
					logger.info("该站点下的管理员登录名'" + loginName + "'已存在!");
					flag = "false";
				}
			}
		}
		return flag;
	}
	
	/**
	 * 创建(修改)站点时验证站点管理员邮箱是否已存在
	 * return true(不存在) false(已存在)
	 * wangyong
	 * 2013-1-31
	 */
	@Post
	@AsController(path = "emailValidate")
	public String emailValidate(@CParam("userEmail") String userEmail, @CParam("userId") int userId,  @CParam("siteId") int siteId){
		String flag = "true";
		if(StringUtil.isNotBlank(userEmail)){
			UserBase admin = siteAdminLogic.getSiteAdminByEmail(siteId, userEmail.trim());
			if(admin != null && userId == 0){    //创建站点
				//验证同一站点下的站点管理员（包括超级站点管理员与普通站点管理员）是否邮箱已存在
				int userType = admin.getUserType().intValue();
				if(userType == ConstantUtil.USERTYPE_ADMIN.intValue() || userType == ConstantUtil.USERTYPE_ADMIN_SUPPER.intValue()){
					logger.info("邮箱名"+userEmail+"已存在!");
					flag = "false";
				}
			}else if(admin != null && userId != 0 && admin.getId().intValue() != userId){    //修改站点
				//验证同一站点下的站点管理员（包括超级站点管理员与普通站点管理员）是否邮箱已存在
				int userType = admin.getUserType().intValue();
				if(userType == ConstantUtil.USERTYPE_ADMIN.intValue() || userType == ConstantUtil.USERTYPE_ADMIN_SUPPER.intValue()){
					logger.info("邮箱名"+userEmail+"已存在!");
					flag = "false";
				}
			}
		}
		return flag;
	}
	
	/**
	 * 修改站点成功后，点选"通过邮件通知管理员"，发送邮件给管理员
	 * wangyong
	 * 2013-2-5
	 */
	@AsController(path = "sendEmail")
	@Post
	public boolean updateSiteSendEmail(@CParam("data") String data){
		boolean sendFlag = false;
		Object[] siteAndUser = JsonUtil.parseObjectArrWithJsonString(data);
		SiteBase siteBase = (SiteBase) JsonUtil.parseObjectWithJsonString(siteAndUser[0].toString(), SiteBase.class);
		UserBase siteAdmin = (UserBase) JsonUtil.parseObjectWithJsonString(siteAndUser[1].toString(), UserBase.class);
		int siteId = siteBase.getId();
		String loginPass = siteAdmin.getLoginPass();
		if(siteId > 0){
			SiteBase site = siteService.getSiteBaseById(siteId);
			UserBase user = userService.getSiteSupperMasterBySiteId(siteId);
			if(StringUtil.isNotBlank(loginPass)){
				user.setLoginPass(loginPass);
			}else{
				user.setLoginPass("");
			}
			sendFlag = emailService.updateSiteEmail(site,user); //修改成功后发送邮件
		}
		return sendFlag;
	}

	/**
	 * 查询站点列表，展示站点实际生效点数
	 * wangyong
	 * 2013-4-1
	 */
	private List<SiteBase> getEffeLicense(List<SiteBase> siteList){
		List<SiteBase> newSiteList = null;
		if(siteList != null && siteList.size() > 0){
			newSiteList = new ArrayList<SiteBase>();
			for(SiteBase site : siteList){
				site.setLicense(licService.getSiteLicenseNum(site.getId()) + site.getLicense().intValue());
				newSiteList.add(site);
			}
		}
		return newSiteList;
	}
	
	/**
	 * 是否有高级搜索条件
	 * wangyong
	 * 2013-1-23
	 */
	public boolean conditionSearch(SiteBase siteBase, HttpServletRequest request){
		if(StringUtil.isNotBlank(request.getParameter("expireDateStart"))){
			return true;
		}
		if(StringUtil.isNotBlank(request.getParameter("expireDateEnd"))){
			return true;
		}
		if(siteBase!=null){
			if(StringUtil.isNotBlank(siteBase.getSiteName())){
				return true;
			}
			if(StringUtil.isNotBlank(siteBase.getSiteSign())){
				return true;
			}
			if(siteBase.getSiteFlag() != null && siteBase.getLockFlag() != null && siteBase.getSiteFlag().intValue() == 1 && siteBase.getLockFlag().intValue() == 1){
				return false;
			}else if(siteBase.getSiteFlag() != null && siteBase.getLockFlag() != null){
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 获取页面传递来的排序参数
	 * wangyong
	 * 2013-1-22
	 */
	private String initSort(String field){
		String sortField = SortConstant.SITEBASE_FIELDS[0][1];
		for (String[] eachField : SortConstant.SITEBASE_FIELDS) {
			if (eachField != null && eachField[0].equals(field)) {
				sortField = BeanUtil.att2Field(eachField[1]);
				break;
			}
		}
		return sortField;
	}
	
	/**
	 * 向前台传递高级搜索表单值
	 * wangyong
	 * 2013-1-23
	 */
	private void sortAttribute(SiteBase siteBase, HttpServletRequest request){
		if(siteBase!=null){
			logger.info("SiteBase-----" + siteBase);
			if(StringUtil.isNotBlank(siteBase.getSiteName())){
				request.setAttribute("siteName", siteBase.getSiteName());
			}
			if(StringUtil.isNotBlank(siteBase.getSiteSign())){
				request.setAttribute("siteSign", siteBase.getSiteSign());
			}
			if(siteBase.getSiteFlag() != null){
				request.setAttribute("siteType", siteBase.getSiteFlag());
			}
			if(siteBase.getLockFlag() != null){
				request.setAttribute("siteStatus", siteBase.getLockFlag());
			}
		}
		if(StringUtil.isNotBlank(request.getParameter("expireDateStart"))){
			request.setAttribute("expireDateStart", request.getParameter("expireDateStart"));
		}
		if(StringUtil.isNotBlank(request.getParameter("expireDateEnd"))){
			request.setAttribute("expireDateEnd", request.getParameter("expireDateEnd"));
		}
	}
	
	/**
	 * 初始化高级搜索Condition条件
	 * wangyong
	 * 2013-1-23
	 */
	private Condition initCondition(SiteBase siteBase, HttpServletRequest request){
		Condition condition = new Condition();
		if(siteBase!=null){
			logger.info("SiteBase-----" + siteBase);
			if(StringUtil.isNotBlank(siteBase.getSiteName())){
				condition.like("site_name", siteBase.getSiteName());
			}
			if(StringUtil.isNotBlank(siteBase.getSiteSign())){
				condition.like("site_sign", siteBase.getSiteSign());
			}
			if(siteBase.getSiteFlag() != null && siteBase.getSiteFlag().intValue() > 0){
				condition.equal("site_flag", siteBase.getSiteFlag());    //站点类型：正式1、试用2
			}
			if(siteBase.getLockFlag() != null && siteBase.getLockFlag().intValue() > 0){
				condition.equal("lock_flag", siteBase.getLockFlag());    //站点状态：解锁1、锁定2
			}
		}
		String expireDateStart = request.getParameter("expireDateStart");
		String expireDateEnd = request.getParameter("expireDateEnd");
		if(StringUtil.isNotBlank(expireDateStart)){
			Date beginTime = DateUtil.StringToDate(expireDateStart, "yyyy-MM-dd");
			beginTime = DateUtil.getGmtDate(beginTime);
			condition.greaterEqual("expire_date",  DateUtil.getDateStrCompact(beginTime, "yyyy-MM-dd HH:mm:ss"));
		}
		if(StringUtil.isNotBlank(expireDateEnd)){
			Date endTime = DateUtil.StringToDate(expireDateEnd, "yyyy-MM-dd");
			endTime = DateUtil.getGmtDate(DateUtil.addDate(endTime, 1));
			condition.lessEqual("expire_date", DateUtil.getDateStrCompact(endTime, "yyyy-MM-dd HH:mm:ss"));
		}
		return condition;
	}
	
	
	/**
	 * 初始化siteBase对象
	 * wangyong
	 * 2013-1-21
	 */
	private SiteBase initSiteObject(SiteBase siteBase, SystemUser systemUser){
		siteBase.setMtgType(0);
		siteBase.setClientName("");
		siteBase.setCreateTime(DateUtil.getGmtDate(null));      //创建时间初始化为GMT时间
		siteBase.setEffeDate(DateUtil.getGmtDate(siteBase.getEffeDate()));      //开始时间初始化为GMT时间
//		siteBase.setExpireDate(DateUtil.getGmtDate(siteBase.getExpireDate()));    //到期时间修改为GMT时间
		siteBase.setExpireDate(DateUtil.addDate(DateUtil.getGmtDate(siteBase.getExpireDate()), 1));    //到期时间修改为GMT时间
		siteBase.setSiteDesc("站点");
		siteBase.setLockFlag((int)ConstantUtil.LOCKFLAG_UNLOCK);
		siteBase.setCreateUser(systemUser.getId());
		siteBase.setDelFlag(ConstantUtil.DELFLAG_UNDELETE);
		if(siteBase.getLicense() != null && siteBase.getLicense().intValue() > 0){
			siteBase.setLicense(siteBase.getLicense().intValue());
		}else{
			siteBase.setLicense(0);
		}
		if(siteBase.getChargeMode() == SiteConstant.SITE_CHARGEMODE_NAMEHOST){
			siteBase.setLicense(0);
		}
		try {
			siteBase.setDelTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00")));
		} catch (ParseException e) {
			Log.error("初始化site对象的删除时间字段错误！");
		}
		siteBase.setDelUser(0);
		return siteBase;
	}

	/**
	 * 初始化UserBase对象
	 * wangyong
	 * 2013-1-30
	 */
	private UserBase initSiteAdmin(UserBase siteAdmin, SystemUser systemUser){
		siteAdmin.setCreateTime(DateUtil.getGmtDate(null));   //创建时间初始化为GMT时间
		siteAdmin.setCreateUser(systemUser.getId());
		siteAdmin.setDelFlag(ConstantUtil.DELFLAG_UNDELETE);
		siteAdmin.setErrorCount(0);
		siteAdmin.setOrgCode("");
		siteAdmin.setOrgId(0);
		siteAdmin.setTrueName(siteAdmin.getTrueName());
		siteAdmin.setUserLogo("");
		siteAdmin.setUserSort(0);
		siteAdmin.setUserStatus(ConstantUtil.LOCKFLAG_UNLOCK);
		siteAdmin.setUserType(ConstantUtil.USERTYPE_ADMIN_SUPPER);
		siteAdmin.setLoginCount(0);
		try {
			siteAdmin.setLastErrorTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00")));
		} catch (ParseException e) {
			Log.error("初始化user对象的LastErrorTime字段错误！");
		}
		try {
			siteAdmin.setDelTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00")));
		} catch (ParseException e) {
			Log.error("初始化user对象的删除时间字段错误！");
		}
		siteAdmin.setDelUser(0);
		return siteAdmin;
	}
	
	/**
	 * 返回json字符串对象(创建，修改站点出错时)
	 * status 失败
	 * object 失败原因
	 * wangyong
	 * 2013-1-30
	 */
	private String returnJsonStr(int status, Object object){
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("message", object.toString());
		return json.toString();
	}
	
	private List<UserBase> getUserListBySiteList(List<SiteBase> siteList){
		List<UserBase> list=null;
		if(siteList!=null && siteList.size()>0){
			Integer[] siteIds=new Integer[siteList.size()];
			for(int ii=0;ii<siteList.size();ii++){
				siteIds[ii]=siteList.get(ii).getId();
			}
			list=userService.getSiteSupperMasterBySiteIdArray(siteIds);
			siteIds=null;
		}
		return list;
	}
	
	/**
	 * 调用service保存站点失败后，获取失败错误信息
	 * wangyong
	 * 2013-3-15
	 */
	private String getSiteErrorMessage(SiteBase site){
		String errorMessage = "";
		if(site.getId() != null && site.getId().intValue() < 0){
			errorMessage = ResourceHolder.getInstance().getResource("system.site.update.error." + site.getId().intValue());
		}
		return errorMessage;
	}
	
	private int getTimeZone(int id){
		if(id>0){
			return SiteConstant.TIMEZONE_WITH_CITY_MAP.get(id).getOffset();
		}
//		if(id>0 && id<=SiteConstant.TIMEZONE_WITH_CITY.length){
//			int index = (Integer)SiteConstant.TIMEZONE_WITH_CITY[id-1][0];
//			if(index==id){
//				return (Integer)SiteConstant.TIMEZONE_WITH_CITY[id-1][2];
//			}else{
//				for (int i = 0; i < SiteConstant.TIMEZONE_WITH_CITY.length; i++) {
//					index = (Integer)SiteConstant.TIMEZONE_WITH_CITY[i][0];
//					if(index==id){
//						return (Integer)SiteConstant.TIMEZONE_WITH_CITY[i][2];
//					}
//				}
//			}
//		}
		return 28800000;//默认为北京时间
	}
}
