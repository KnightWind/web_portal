package com.bizconf.audio.action.system;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.LicenseConstant;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.License;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.ConfLogic;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.EnterpriseAdminService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.LicService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 系统管理员操作站点controller
 * @author Martin
 * @date 2013.3.26
 */
@ReqPath("lic")
//@Interceptors({SystemUserInterceptor.class})
public class LicenseController extends BaseController{
	private final  Logger logger = Logger.getLogger(LicenseController.class);
	
	@Autowired
	SiteService siteService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	EmailService emailService;
	@Autowired
	UserService userService;

	@Autowired
	LicService licService;
	
	@Autowired 
	EnterpriseAdminService enterpriseAdminService;
	
	@Autowired
	EmpowerConfigService empowerConfigService;
	
	@Autowired
	ConfService confService;
	
	@Autowired
	ConfLogic confLogic;
	
	@Autowired
	ConfUserLogic confUserLogic;
	
	
	@AsController(path = "list")
	public Object listBySite(@CParam("siteId") int siteId,@CParam("userId") int userId,@CParam("pageNo") int pageNo, HttpServletRequest request) throws Exception{
		
		PageBean<License> pageModel = null;
		pageModel = licService.getLicensePage(siteId, userId, pageNo);
		if(pageModel!=null && pageModel.getDatas()!=null){
			for(License lic:pageModel.getDatas()){
				lic.setEffeDate(DateUtil.getBejingDateByGmtDate(lic.getEffeDate()));
				lic.setExpireDate(DateUtil.getBejingDateByGmtDate(lic.getExpireDate()));
			}
		}
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("siteId", siteId);
		if(userId > 0){
			request.setAttribute("userId", userId);
		}
		SiteBase site = siteService.getSiteBaseById(siteId);
		request.setAttribute("site", site);
		
		return new ActionForward.Forward("/jsp/system/license_list.jsp");
	}
	
	@AsController(path = "save")
	public Object saveLicenseEdit(License license, HttpServletRequest request) throws Exception{
		SystemUser currentUser = userService.getCurrentSysAdmin(request);
		license.init();
		license.setCreateUser(currentUser.getId());
		SiteBase site = siteService.getSiteBaseById(license.getSiteId());
		boolean addflag = false;
		//site.getChargeMode().intValue()!=1 &&
		if(license.getEffeDate().before(DateUtil.getGmtDate(null))){
			SiteBase asSite= siteService.queryASSiteInfo(site.getSiteSign());
			if(license.getLicNum().intValue()>0){
				addflag = true;
			}
			int licNum = asSite.getLicense() + license.getLicNum();
			site.setLicense(licNum);
			//添加license到as
			String updateInfo = siteService.soapUpdateSite(site,addflag);
			if(updateInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
				license.setEffeFlag(LicenseConstant.HAS_EFFED);
			}else{
				logger.error("add license fialed");
			}
		}
		licService.saveOrUpdate(license);
		return new ActionForward.Forward("/system/lic/list?siteId"+license.getSiteId());
	}
	
	/**
	 * 进入编辑页面
	 * @param licId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "goEdit")
	public Object toEdit(@CParam("id") int licId, HttpServletRequest request) throws Exception{
		
		return new ActionForward.Forward("/user/lic/list");
	}
	
	/**
	 * 删除license
	 * @param licId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "del")
	public Object delLic(@CParam("id") int licId, HttpServletRequest request) throws Exception{
		
		SystemUser currentUser = userService.getCurrentSysAdmin(request);
		License license  = licService.getLicenseById(licId);
		if(license.getEffeFlag().intValue()!=1){
			licService.delLicense(licId, currentUser.getId());
		}
		return new ActionForward.Forward("/system/lic/list?siteId="+license.getSiteId()+"&userId="+license.getUserId());
	}
	
	
	
	/**
	 * nameHost 模式下显示主持人列表
	 * @param siteId
	 * @param pageNo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "listHost")
	public Object listHost(@CParam("siteId") int siteId,@CParam("pageNo") int pageNo, HttpServletRequest request) throws Exception{
		PageBean<UserBase> pageModel = licService.getHostsBySite(siteId, pageNo);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("siteId", siteId);
		request.setAttribute("licnums", licService.getHostsLienseDatas(pageModel.getDatas()));
		return new ActionForward.Forward("/jsp/system/hostlist.jsp");
	}
	
	@AsController(path = "delHost")
	public Object delHostUser(@CParam("id") int id,@CParam("pageNo") int pageNo, HttpServletRequest request) throws Exception{
		UserBase user = userService.getUserBaseById(id);
		SystemUser currentUser = userService.getCurrentSysAdmin(request);
		SiteBase site = siteService.getSiteBaseById(user.getSiteId());
		
		//删除namehost用户的同时 删除该namehost 的 license
		SiteBase asSite= siteService.queryASSiteInfo(site.getSiteSign());
		int licNum = asSite.getLicense() + licService.getHostLicenseNum(user.getId());
		site.setLicense(licNum);
		//添加license到as
		String updateInfo = siteService.soapUpdateSite(site,true);
		if(updateInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
			if(licService.delHostLicenses(user.getId())){
				enterpriseAdminService.deleteUserBase(id, currentUser.getId());
			}
		}else{
			logger.error("update site  license num  fialed");
		}
		//======删除该用户创建的会议======
//		List<ConfBase> confs = confLogic.getConfBasesByCreator(id);
//		for (Iterator<ConfBase> it = confs.iterator(); it.hasNext();) {
//			ConfBase conf = it.next();
//			SiteBase site = siteService.getSiteBaseById(conf.getSiteId());
//			if(conf.getConfStatus().intValue()==1){
//				if(conf.getCycleId()!=0){
//					confService.cancleCycleConfInfo(conf.getId(), site, user);
//				}else{
//					confService.cancleSingleReservationConf(conf.getId(), site, user);
//				}
//				List<ConfUser> confUsers = confUserLogic.getConfUserList(conf.getId());
//				emailService.confCancelEmail(confUsers, conf);
//			}
//		}
		return new ActionForward.Redirect("/system/lic/listHost?siteId="+user.getSiteId());
	}
	
	@AsController(path = "goEditHost")
	public Object goEditHost(@CParam("userId") int userId,@CParam("siteId") int siteId, HttpServletRequest request) throws Exception{
		UserBase host = userService.getUserBaseById(userId);
		request.setAttribute("user", host);
		request.setAttribute("siteId", siteId);
		
		SiteBase site = siteService.getSiteBaseById(siteId);
		EmpowerConfig siteConfig = empowerConfigService.getSiteEmpowerConfigBySiteId(site.getId());
		EmpowerConfig globalConfig = empowerConfigService.getSiteEmpowerGlobalBySiteId(site.getId());
		empowerConfigService.setConfigValue(siteConfig,globalConfig);
		
		EmpowerConfig config = empowerConfigService.getUserEmpowerConfigByUserId(userId);
		request.setAttribute("config", config);
		request.setAttribute("siteConfig", siteConfig);
		return new ActionForward.Forward("/jsp/system/add_site_user.jsp");
	}
	
	/**
	 * 新增或者修改namehost下的主持人信息
	 * @param user
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "saveHost")
	public Object saveHost(UserBase user,License license,EmpowerConfig config, HttpServletRequest request) throws Exception{
		SystemUser userAdmin = userService.getCurrentSysAdmin(request);
		user.setUserRole(ConstantUtil.USERROLE_HOST);
		String orgPass = user.getLoginPass();
		if(user.getId()!=null && user.getId()>0){
			UserBase orgUser = userService.getUserBaseById(user.getId()); //原始的用户信息
			orgUser.setLoginName(user.getLoginName());
			orgUser.setTrueName(user.getTrueName());
			orgUser.setEnName(user.getEnName());
			orgUser.setUserEmail(user.getUserEmail());
			orgUser.setMobile(user.getMobile());
			if(StringUtil.isNotBlank(user.getLoginPass())){
				orgUser.setLoginPass(MD5.encodePassword(user.getLoginPass(), "MD5"));
			}
			if(userService.siteUserSaveable(orgUser)){
				boolean flag = enterpriseAdminService.updateUserBase(orgUser);
				if(flag){
					EmpowerConfig orgConfig = empowerConfigService.getUserEmpowerConfigByUserId(user.getId());
					if(orgConfig==null){
						config.setSiteId(user.getSiteId());
						config.setUserId(user.getId());
						config.setEmTime(DateUtil.getGmtDate(null));
						config.setEmUser(userAdmin.getId());
						config.setEmUserType(user.getUserType());
						empowerConfigService.saveEmpowerConfig(config);
					}else{
						orgConfig.setAutoFlag(config.getAutoFlag());
						orgConfig.setPhoneFlag(config.getPhoneFlag());
						orgConfig.setShareMediaFlag(config.getShareMediaFlag());
						orgConfig.setRecordFlag(config.getRecordFlag());
						empowerConfigService.updateEmpowerConfig(orgConfig);
					}
//					eventLogService.saveAdminEventLog(userAdmin, EventLogConstants.SITE_ADMIN_USER_UPDATE, 
//							ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.1"), 
//							EventLogConstants.EVENTLOG_SECCEED, user, request);   //修改成功后写EventLog
					orgUser.setLoginPass(orgPass);
					List<License> lics = licService.getHostLicenses(orgUser.getId());
					emailService.updateNameHost(orgUser, lics);
				}
			}else{
//				eventLogService.saveAdminEventLog(userAdmin, EventLogConstants.SITE_ADMIN_USER_UPDATE, 
//						ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.2"), 
//						EventLogConstants.EVENTLOG_FAIL, user, request);   //修改失败后写EventLog
				return StringUtil.returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.2"));
			}
			return StringUtil.returnJsonStr(ConstantUtil.CREATESITEUSER_SUCCEED, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.1"));
		}else{
			user.init();
			user.setCreateUser(0);//
			user.setUserType(ConstantUtil.USERTYPE_USERS);
			UserBase userBase = null;
			if(userService.siteUserSaveable(user)){
				userBase = enterpriseAdminService.saveUserBase(user);
				if(userBase != null && userBase.getId() > 0){
					//保存权限设置
					config.setSiteId(user.getSiteId());
					config.setUserId(userBase.getId());
					config.setEmTime(DateUtil.getGmtDate(null));
					config.setEmUser(userAdmin.getId());
					config.setEmUserType(user.getUserType());
					empowerConfigService.saveEmpowerConfig(config);
					
					if(license!=null && license.getLicNum()!=null && license.getLicNum()>0){
						license.init();
						license.setCreateUser(userAdmin.getId());
						SiteBase site = siteService.getSiteBaseById(license.getSiteId());
						license.setEffeDate(DateUtil.getGmtDate(null));
						license.setExpireDate(site.getExpireDate());
						license.setUserId(userBase.getId());
						if(DateUtil.getGmtDate(license.getEffeDate()).before(DateUtil.getGmtDate(null))){
							SiteBase asSite= siteService.queryASSiteInfo(site.getSiteSign());
							boolean flag = false;
							if(license.getLicNum().intValue()>0){
								flag = true;
							}else{
								throw new RuntimeException("init license num can not less then 0!");
							}
							int licNum = asSite.getLicense() + license.getLicNum();
							site.setLicense(licNum);
							//添加license到as
							String updateInfo = siteService.soapUpdateSite(site,flag);
							if(updateInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
								license.setEffeFlag(LicenseConstant.HAS_EFFED);
							}else{
								logger.error("add license fialed");
							}
						}
						licService.saveOrUpdate(license);
					}
					userBase.setLoginPass(orgPass);
					
					List<License> lics = licService.getHostLicenses(userBase.getId());
					emailService.createNameHost(userBase, lics);
				}else{
					return StringUtil.returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.create.2"));
				}
			}else{
				return StringUtil.returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.create.2"));
			}
			return StringUtil.returnJsonStr(ConstantUtil.CREATESITEUSER_SUCCEED, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.create.1"));
		}
	}
	
	@AsController(path = "sendMail")
	public Object sendRemindMail(@CParam("userId")Integer userId, HttpServletRequest request) throws Exception{
		boolean flag = true;
		int status = ConstantUtil.GLOBAL_SUCCEED_FLAG;
		String msg = "success";
		try{
			UserBase user = userService.getUserBaseById(userId);
			user.setLoginPass("");
			List<License> licenses = licService.getHostLicenses(userId);
			flag = emailService.updateNameHost(user, licenses);
		
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		if(!flag){
			status = ConstantUtil.GLOBAL_FAIL_FLAG;
			msg = "error";
		}
		return StringUtil.returnJsonStr(status, msg);
	}
}
