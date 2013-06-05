package com.bizconf.audio.action.admin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SiteOrg;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.logic.ConfLogic;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.EnterpriseAdminService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.OrgService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.ExcelUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.JsonUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberCFile;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.utils.LiberContainer;

/**
 * @desc 企业用户管理controller
 * @author martin
 * @date 2013-2-17
 */
@ReqPath("entUser")
@Interceptors({SiteAdminInterceptor.class})
public class EnterpriseAdminsController extends BaseController{
	private final Logger logger = Logger.getLogger(EnterpriseAdminsController.class);
	
	@Autowired
	private EnterpriseAdminService enterpriseAdminsService;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	EventLogService eventLogService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	SiteService siteService;
	
	@Autowired
	EmpowerConfigService empowerConfigService;
	
	@Autowired
	ConfService confService;
	
	@Autowired
	ConfLogic confLogic;
	
	@Autowired
	ConfUserLogic confUserLogic;
	
	@Autowired
	OrgService orgService;
	
	@AsController(path = "listAll")
	public Object list(@CParam("keyword") String keyword,@CParam("sortField") String sortField,@CParam("sortRule") String sortRule,@CParam("pageNo") String pageNo, HttpServletRequest request) throws Exception{
		PageBean<UserBase> pageModel = null;
		UserBase userBase = userService.getCurrentSiteAdmin(request);
		if(userBase.isSuperSiteAdmin()){
			pageModel = enterpriseAdminsService.getUserBases(keyword, sortField, sortRule, userBase.getSiteId(),null, pageNo);
		}else{
			pageModel = enterpriseAdminsService.getUserBases(keyword, sortField, sortRule, userBase.getSiteId(),userBase.getId(), pageNo);
		}
		if(pageModel != null && pageModel.getDatas() != null){
			Map<Integer, String> orgNamesMap = getOrgNames(pageModel.getDatas());   //获取每个会议的参会人个数
			request.setAttribute("orgNamesMap", orgNamesMap);
		}
		request.setAttribute("keyword", keyword);
		request.setAttribute("sortField", sortField);
		request.setAttribute("sortRule", sortRule);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/admin/site_user_list.jsp");
	}
	
	/**
	 * 获取每个用户的组织机构名称
	 * wangyong
	 * 2013-3-25
	 */
	private Map<Integer,String> getOrgNames(List<UserBase> userList){
		Map<Integer,String> orgNamesMap = new HashMap<Integer, String>();
		if(userList != null && userList.size() > 0){
			for(UserBase user:userList){
				SiteOrg org = orgService.getSiteOrgById(user.getOrgId());
				orgNamesMap.put(user.getId(), org == null ? "- -" : org.getOrgName());
			}
		}
		return orgNamesMap;
	}
	
	/**
	 * 批量删除站点用户
	 * @param request
	 * @return
	 */
	@AsController(path = "delSiteUsers")
	public Object delEnterpriseUsers(HttpServletRequest request){
		boolean deleteFlag = false;
		UserBase user = userService.getCurrentSiteAdmin(request);
		String[] ids = request.getParameterValues("id");
		logger.info("批量删除站点用户ID：" + ids);
		deleteFlag = enterpriseAdminsService.deleteUserBases(ids, user.getId());
//		if(deleteFlag){
			//======删除该用户创建的会议======
//			for (int i = 0; i < ids.length; i++) {
//				Integer creatorId = Integer.parseInt(ids[i]);
//				List<ConfBase> confs = confLogic.getConfBasesByCreator(creatorId);
//				for (Iterator<ConfBase> it = confs.iterator(); it.hasNext();) {
//					ConfBase conf = it.next();
//					if(conf.getConfStatus().intValue()==1){
//						if(conf.getCycleId()!=0){
//							confService.cancleCycleConfInfo(conf.getId(), site, user);
//						}else{
//							confService.cancleSingleReservationConf(conf.getId(), site, user);
//						}
//						List<ConfUser> confUsers = confUserLogic.getConfUserList(conf.getId());
//						emailService.confCancelEmail(confUsers, conf);
//					}
//				}
//			}
//			eventLogService.saveAdminEventLogBatch(user, EventLogConstants.SITE_ADMIN_USER_DELETE_BATCH, 
//					ResourceHolder.getInstance().getResource("siteAdmin.siteUser.delete.batch.1"), 
//					EventLogConstants.EVENTLOG_SECCEED, ids, request);   //删除成功后写EventLog
//		}
		sysHelpAdminEventLog(deleteFlag, userService.getCurrentSysAdmin(request), user, 
				EventLogConstants.SYSTEM_ADMIN_USER_DELETE_BATCH, EventLogConstants.SITE_ADMIN_USER_DELETE_BATCH, "批量删除站点用户", null, request);
		return new ActionForward.Forward("/admin/entUser/listAll");
	}
	
	/**
	 * 批量锁定 
	 * @param request
	 * @param response
	 * @return
	 */
	@AsController(path = "lockSiteUsers")
	public Object lockUserBaseStatus(HttpServletRequest request,HttpServletResponse response){
		boolean lockFlag = false;
		UserBase user = userService.getCurrentSiteAdmin(request);
		String[] ids = request.getParameterValues("id");
		try {
			lockFlag = enterpriseAdminsService.changeLockState(ids, ConstantUtil.USER_STATU_LOCK);
		} catch (Exception e) {
			logger.error("批量锁定用户出错",e);
		}finally{
			sysHelpAdminEventLog(lockFlag, userService.getCurrentSysAdmin(request), user, 
					EventLogConstants.SYSTEM_ADMIN_USER_LOCK_BATCH, EventLogConstants.SITE_ADMIN_USER_LOCK_BATCH, "批量锁定 站点用户", null, request);
		}
		return new ActionForward.Forward("/admin/entUser/listAll");
	}
	
	/**
	 * 批量解锁
	 * @param request
	 * @param response
	 * @return
	 */
	@AsController(path = "unlockSiteUsers")
	public Object unlockUserBaseStatus(HttpServletRequest request,HttpServletResponse response){
		boolean lockFlag = false;
		UserBase user = userService.getCurrentSiteAdmin(request);
		String[] ids = request.getParameterValues("id");
		try {
			lockFlag = enterpriseAdminsService.changeLockState(ids, ConstantUtil.USER_STATU_UNLOCK);
		} catch (Exception e) {
			logger.error("批量解锁用户出错",e);
		} finally{
			sysHelpAdminEventLog(lockFlag, userService.getCurrentSysAdmin(request), user, 
					EventLogConstants.SYSTEM_ADMIN_USER_ACTIVE_BATCH, EventLogConstants.SITE_ADMIN_USER_ACTIVE_BATCH, "批量解锁 站点用户", null, request);
		}
		return new ActionForward.Forward("/admin/entUser/listAll");
	}
	
	/**
	 * 站点管理员管理该站点下的普通管理员
	 * @param keyword
	 * @param siteId
	 * @param pageNo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "list")
	public Object listBySite(@CParam("keyword") String keyword,@CParam("pageNo") String pageNo, HttpServletRequest request) throws Exception{
		UserBase userBase = userService.getCurrentSiteAdmin(request);
		int siteId = userBase.getSiteId();//当前登录站点
		PageBean<UserBase> pageModel = null;
		if(userBase.isSuperSiteAdmin()){
			pageModel = enterpriseAdminsService.getSiteManagers(keyword, null, null, siteId,null, pageNo);;
		}else{
			pageModel = enterpriseAdminsService.getSiteManagers(keyword, null, null, userBase.getSiteId(),userBase.getId(), pageNo);
		}
		request.setAttribute("keyword", keyword);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/admin/site_manager_list.jsp");
	}
	
	/**
	 * 删除某个普通管理员
	 * @param request
	 * @return
	 */
	@AsController(path = "delUsers")
	public Object delEnterpriseAdmins(HttpServletRequest request){
		boolean deleteFlag = false;
		UserBase user = userService.getCurrentSiteAdmin(request);
		String[] ids = request.getParameterValues("id");
		deleteFlag = enterpriseAdminsService.deleteUserBases(ids, user.getId());
		sysHelpAdminEventLog(deleteFlag, userService.getCurrentSysAdmin(request), user, 
				EventLogConstants.SYSTEM_ADMIN_SITEADMIN_DELETE, EventLogConstants.SITE_ADMIN_SITEADMIN_DELETE, "删除某个普通管理员", null, request);
		return new ActionForward.Forward("/admin/entUser/list");
	}
	
	/**
	 * 修改或者新建普通管理员
	 * @param id
	 * @param request
	 * @return
	 */
	@AsController(path = "toEditUser")
	public Object toEdit(@CParam("id") Integer id,HttpServletRequest request){
		if(id!=null&& id>0){
			UserBase user = userService.getUserBaseById(id);
			request.setAttribute("user", user);
		}
		return new ActionForward.Forward("/jsp/admin/add_site_user.jsp");
	}
	
	/**
	 * 修改或者新建普通站点用户
	 * @param id
	 * @param request
	 * @return
	 */
	@AsController(path = "toEditUserBase")
	public Object toEditUserBase(@CParam("id") Integer userId,HttpServletRequest request){
		UserBase currUser = userService.getCurrentSiteAdmin(request);
		if(currUser==null){
			return null;
		}
		Integer siteId=currUser.getSiteId();
		SiteBase siteBase = siteService.getSiteBaseById(siteId);
		EmpowerConfig sitePower= empowerConfigService.getSiteEmpowerGlobalBySiteId(siteId);
		EmpowerConfig userPower = null;
		UserBase userBase=null;
		if(userId!=null&& userId.intValue()>0){
//			UserBase user = userService.getUserBaseById(userId);
			userPower = empowerConfigService.getUserEmpowerConfigByUserId(userId);
//			request.setAttribute("user", user);
//			request.setAttribute("config", config);
			userBase = userService.getUserBaseById(userId);
			if(userBase != null && userBase.getId().intValue() > 0){
				getOrgParentId(userBase.getOrgId(), request);     //修改
			}
		}
		EmpowerConfig userConfig=empowerConfigService.makeEmpowerForUser(sitePower, userPower);
		List<SiteOrg> orgList = orgService.getSiteOrgList(currUser.getSiteId()).getDatas();
		request.setAttribute("orgList", orgList);
		request.setAttribute("userBase", userBase);
		request.setAttribute("siteBase", siteBase);
		request.setAttribute("sitePower", sitePower);
		request.setAttribute("userConfig", userConfig);
		
		return new ActionForward.Forward("/jsp/admin/edit_userbase.jsp");
	}
	
	/**
	 * 修改用户时，获得用户所在机构的级别
	 * 1.组织机构最多4级
	 * 2.返回前台页面每级所对应的orgId
	 * 3.以递归的形式获取orgId
	 * shhc
	 * 2013-5-21
	 */
	private boolean getOrgParentId(int orgId, HttpServletRequest request){
        SiteOrg org = orgService.getSiteOrgById(orgId);
        if(org == null || org.getId().intValue() <= 0){
        	return false;
        }
        int orgLevel = org.getOrgLevel().intValue();
        switch (orgLevel) {
		case 4:
			request.setAttribute("dep4", orgId);
			getOrgParentId(org.getParentId(), request);
			break;
		case 3:
			request.setAttribute("dep3", orgId);
			getOrgParentId(org.getParentId(), request);
			break;
		case 2:
			request.setAttribute("dep2", orgId);
			getOrgParentId(org.getParentId(), request);
			break;
		case 1:
			request.setAttribute("dep1", orgId);
			break;
		}
        return true;
    }
	
	/**
	 * 查看普通站点用户信息
	 * @param id
	 * @param request
	 * @return
	 */
	@AsController(path = "toViewUserBase")
	public Object toViewUserBase(@CParam("id") Integer id,HttpServletRequest request){
		UserBase currUser = userService.getCurrentSiteAdmin(request);
		SiteBase site = siteService.getSiteBaseById(currUser.getSiteId());
		EmpowerConfig siteConfig = empowerConfigService.getSiteApplyEmpowerBySiteId(site.getId());
		if(id!=null&& id>0){
			UserBase user = userService.getUserBaseById(id);
			EmpowerConfig config = empowerConfigService.getUserEmpowerConfigByUserId(user.getId());
			if(user != null && user.getId().intValue() > 0){
				List<SiteOrg> orgList = orgService.getSiteOrgList(user.getSiteId()).getDatas();
				if(orgList!=null && !orgList.isEmpty()){
					for(SiteOrg org : orgList){
						if(user.getOrgId().intValue() == org.getId().intValue()){
							request.setAttribute("orgName", StringUtil.isNotBlank(org.getOrgName()) ? org.getOrgName() : "无");
							break;
						}
					}
				}
			}
			request.setAttribute("user", user);
			request.setAttribute("config", config);
		}
		request.setAttribute("site", site);
		request.setAttribute("siteConfig", siteConfig);
		return new ActionForward.Forward("/jsp/admin/user_info.jsp");
	}
	
	/**
	 * 保存普通站点管理员信息
	 * @param user
	 * @param request
	 * @return
	 */
	@AsController(path = "saveSiteAdmin")
	public Object saveSiteAdmin(@CParam("data") String data,HttpServletRequest request){
		Object[] infos = JsonUtil.parseObjectArrWithJsonString(data);
		UserBase user = (UserBase) JsonUtil.parseObjectWithJsonString(infos[0].toString(), UserBase.class);
		UserBase userAdmin = userService.getCurrentSiteAdmin(request);
		UserBase userBase = null;
		if(user.getId()!=null && user.getId()>0){
			UserBase orgUser = userService.getUserBaseById(user.getId());
			orgUser.setLoginName(user.getLoginName());
			orgUser.setTrueName(user.getTrueName());
			orgUser.setEnName(user.getEnName());
			orgUser.setRemark(user.getRemark());
			if(StringUtil.isNotBlank(user.getUserEmail())){
				userBase =	userService.getSiteAdminByEmail(userAdmin.getSiteId(), user.getUserEmail());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() != user.getId().intValue()){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.list.update.3"));
				}
				orgUser.setUserEmail(user.getUserEmail());
			}
			if(StringUtil.isNotBlank(user.getLoginName())){
				userBase = userService.getSiteAdminByLoginName(userAdmin.getSiteId(), user.getLoginName());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() != user.getId().intValue()){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.list.update.4"));
				}
			}
			orgUser.setMobile(user.getMobile());
			if(StringUtil.isNotBlank(user.getLoginPass())){
				orgUser.setPassEditor(userAdmin.getId());
				orgUser.setLoginPass(MD5.encodePassword(user.getLoginPass(), "MD5"));
			}
			if(userService.siteUserSaveable(orgUser)){
				
				boolean flag = enterpriseAdminsService.updateUserBase(orgUser);
				if(flag){
					orgUser.setLoginPass(user.getLoginPass());//邮件给用户时显示的密码
					emailService.updateSiteAdmin(orgUser);
				}
				sysHelpAdminEventLog(flag, userService.getCurrentSysAdmin(request), userAdmin, 
						EventLogConstants.SYSTEM_ADMIN_SITEADMIN_UPDATE, EventLogConstants.SITE_ADMIN_SITEADMIN_UPDATE, "修改普通站点管理员", user, request);
			}else{
				sysHelpAdminEventLog(false, userService.getCurrentSysAdmin(request), userAdmin, 
						EventLogConstants.SYSTEM_ADMIN_SITEADMIN_UPDATE, EventLogConstants.SITE_ADMIN_SITEADMIN_UPDATE, "修改普通站点管理员", user, request);
				return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.list.update.2"));
			}
			
			return returnJsonStr(ConstantUtil.CREATESITEUSER_SUCCEED, ResourceHolder.getInstance().getResource("siteAdmin.list.update.1"));
		}else{
			UserBase creator = userService.getCurrentSiteAdmin(request);
			if(StringUtil.isNotBlank(user.getUserEmail())){
				userBase =	userService.getSiteAdminByEmail(userAdmin.getSiteId(), user.getUserEmail());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() > 0){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.list.update.3"));
				}
				user.setUserEmail(user.getUserEmail());
			}
			if(StringUtil.isNotBlank(user.getLoginName())){
				userBase = userService.getSiteAdminByLoginName(userAdmin.getSiteId(), user.getLoginName());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() > 0){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.list.update.4"));
				}
			}
			initSiteAdmin(user,creator);
			String orgPass = user.getLoginPass();
			userBase = null;
			boolean createFlag = false;
			if(userService.siteUserSaveable(user)){
				user.setPassEditor(userAdmin.getId());
				userBase = enterpriseAdminsService.saveUserBase(user);
				if(userBase != null && userBase.getId() > 0){
					createFlag = true;
					user.setLoginPass(orgPass);//明文密码 发送给用户
					emailService.createSiteAdminEmail(user);
				}else{
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.list.create.2"));
				}
			}else{
				sysHelpAdminEventLog(false, userService.getCurrentSysAdmin(request), userAdmin, 
						EventLogConstants.SYSTEM_ADMIN_SITEADMIN_CREATE, EventLogConstants.SITE_ADMIN_SITEADMIN_CREATE, "新建普通站点管理员", user, request);
				return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.list.create.2"));
			}
			sysHelpAdminEventLog(true, userService.getCurrentSysAdmin(request), userAdmin, 
					EventLogConstants.SYSTEM_ADMIN_SITEADMIN_CREATE, EventLogConstants.SITE_ADMIN_SITEADMIN_CREATE, "新建普通站点管理员", user, request);
			return returnJsonStr(ConstantUtil.CREATESITEUSER_SUCCEED, ResourceHolder.getInstance().getResource("siteAdmin.list.create.1"));
		}
	}
	
	/**
	 * 保存普通站点用户信息
	 * @param user
	 * @param request
	 * @return
	 */
	@AsController(path = "saveSiteUser")
	public Object saveSiteUser(@CParam("data") String data,HttpServletRequest request){
		Object[] infos = JsonUtil.parseObjectArrWithJsonString(data);
		UserBase user = (UserBase) JsonUtil.parseObjectWithJsonString(infos[0].toString(), UserBase.class);
		EmpowerConfig config = (EmpowerConfig) JsonUtil.parseObjectWithJsonString(infos[1].toString(), EmpowerConfig.class);
		UserBase userBase = null;
		UserBase userAdmin = userService.getCurrentSiteAdmin(request);
		if (userAdmin==null){
			return null;
		}
		List<String[]> empowerFieldList=SiteConstant.EMPOWER_CODE_FIELD_LIST;
		EmpowerConfig globalPower = empowerConfigService.getSiteEmpowerGlobalBySiteId(userAdmin.getSiteId());	
		EmpowerConfig userPower=null;
		String eachFieldName="";
		Integer eachValue;
		
		//保存用户时，保存组织机构orgCode
		if(user != null && user.getOrgId() != null && user.getOrgId().intValue() > 0){
			SiteOrg org = orgService.getSiteOrgById(user.getOrgId());
			if(org != null){
				user.setOrgCode(org.getOrgCode());
			}else{
				user.setOrgCode("");
			}
		}else{
			user.setOrgId(0);
			user.setOrgCode("");
		}
		
		if(user.getId()!=null && user.getId()>0){
			UserBase orgUser = userService.getUserBaseById(user.getId());
			orgUser.setLoginName(user.getLoginName());
			orgUser.setTrueName(user.getTrueName());
			orgUser.setEnName(user.getEnName());
			orgUser.setUserEmail(user.getUserEmail());
			orgUser.setMobile(user.getMobile());
			orgUser.setUserRole(user.getUserRole());
			orgUser.setOrgId(user.getOrgId());
			orgUser.setOrgCode(user.getOrgCode());
			orgUser.setExprieDate(user.getExprieDate());
			if(StringUtil.isNotBlank(user.getUserEmail())){
				userBase =	userService.getSiteUserByEmail(userAdmin.getSiteId(), user.getUserEmail());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() != user.getId().intValue()){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.3"));
				}
				orgUser.setUserEmail(user.getUserEmail());
			}
			if(StringUtil.isNotBlank(user.getLoginName())){
				userBase = userService.getSiteUserByLoginName(userAdmin.getSiteId(), user.getLoginName());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() != user.getId().intValue()){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.4"));
				}
			}
			if(StringUtil.isNotBlank(user.getLoginPass())){
				orgUser.setLoginPass(MD5.encodePassword(user.getLoginPass(), "MD5"));
				orgUser.setPassEditor(userAdmin.getId());   //若管理员修改用户密码，则记录操作人ID
			}
			boolean flag = false;
			if(userService.siteUserSaveable(orgUser)){
				flag = enterpriseAdminsService.updateUserBase(orgUser);
				if(flag){
					userPower= empowerConfigService.getUserEmpowerConfigByUserId(user.getId());
					
					if(userPower==null){
						userPower=globalPower;
						//config.setSiteId(userAdmin.getSiteId());
						userPower.setUserId(user.getId());
						userPower.setEmTime(DateUtil.getGmtDate(null));
						userPower.setEmUser(userAdmin.getId());
						userPower.setEmUserType(userAdmin.getUserType());
						
						for(String[] eachField:empowerFieldList){
							if(eachField!=null){
								eachFieldName=eachField[1];
								if( "1".equals(eachField[3])){
									eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(config, eachFieldName));
									userPower=(EmpowerConfig)ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
								}else{
									eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(globalPower, eachFieldName));
									userPower=(EmpowerConfig)ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
								}
								
							}
						}
						empowerConfigService.saveEmpowerConfig(userPower);
					}else{
//						orgConfig.setAutoFlag(config.getAutoFlag());
//						orgConfig.setPhoneFlag(config.getPhoneFlag());
//						orgConfig.setShareMediaFlag(config.getShareMediaFlag());
//						orgConfig.setRecordFlag(config.getRecordFlag());
						for(String[] eachField:empowerFieldList){
							if(eachField!=null ){
								eachFieldName=eachField[1];
								if( "1".equals(eachField[3])){
									eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(config, eachFieldName));
									userPower=(EmpowerConfig)ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
								}else{
									eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(globalPower, eachFieldName));
									userPower=(EmpowerConfig)ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
								}
								
//								eachFieldName=eachField[1];
//								eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(config, eachFieldName));
//								ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
							}
						}
						empowerConfigService.updateEmpowerConfig(userPower);
					}
					orgUser.setLoginPass(user.getLoginPass());//明文密码发送给用户
					emailService.updateSiteUserEmail(orgUser);
				}
			}else{
				sysHelpAdminEventLog(false, userService.getCurrentSysAdmin(request), userAdmin, 
						EventLogConstants.SYSTEM_ADMIN_USER_UPDATE, EventLogConstants.SITE_ADMIN_USER_UPDATE, "修改普通站点用户", user, request);
				return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.2")+":该用户名已存在");
			}
			sysHelpAdminEventLog(flag, userService.getCurrentSysAdmin(request), userAdmin, 
					EventLogConstants.SYSTEM_ADMIN_USER_UPDATE, EventLogConstants.SITE_ADMIN_USER_UPDATE, "修改普通站点用户", user, request);
			return returnJsonStr(ConstantUtil.CREATESITEUSER_SUCCEED, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.1"));
		}else{
			initSiteAdmin(user,userAdmin);
			user.setUserType(ConstantUtil.USERTYPE_USERS);
			user.setPassEditor(userAdmin.getId());
			String orgPass=user.getLoginPass();
			if(StringUtil.isNotBlank(user.getUserEmail())){
				userBase =	userService.getSiteUserByEmail(userAdmin.getSiteId(), user.getUserEmail());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() > 0){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.3"));
				}
			}
			if(StringUtil.isNotBlank(user.getLoginName())){
				userBase = userService.getSiteUserByLoginName(userAdmin.getSiteId(), user.getLoginName());
				if(userBase != null && userBase.getId() != null && userBase.getId().intValue() > 0){
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.update.4"));
				}
			}
			
			
			boolean flag = false;
			if(userService.siteUserSaveable(user)){
				userBase = enterpriseAdminsService.saveUserBase(user);
				
				if(userBase != null && userBase.getId() > 0){
					flag = true;
					//保存权限设置
					userPower=globalPower;
					//config.setSiteId(userAdmin.getSiteId());
					userPower.setUserId(userBase.getId());
					userPower.setEmTime(DateUtil.getGmtDate(null));
					userPower.setEmUser(userAdmin.getId());
					userPower.setEmUserType(userAdmin.getUserType());
					for(String[] eachField:empowerFieldList){
						if(eachField!=null ){
							eachFieldName=eachField[1];
							if( "1".equals(eachField[3])){
								eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(config, eachFieldName));
								userPower=(EmpowerConfig)ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
							}else{
								eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(globalPower, eachFieldName));
								userPower=(EmpowerConfig)ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
							}
//							eachValue=IntegerUtil.parseInteger(ObjectUtil.getFieldValue(config, eachFieldName));
//							ObjectUtil.setFieldValue(userPower, eachFieldName, eachValue);
						}
					}
					empowerConfigService.saveEmpowerConfig(userPower);
					user.setLoginPass(orgPass);
					emailService.createSiteUser(user);
				}else{
					return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.create.2"));
				}
			}else{
				sysHelpAdminEventLog(false, userService.getCurrentSysAdmin(request), userAdmin, 
						EventLogConstants.SYSTEM_ADMIN_USER_CREATE, EventLogConstants.SITE_ADMIN_USER_CREATE, "新建普通站点用户", user, request);
				return returnJsonStr(ConstantUtil.CREATESITEUSER_FAIL, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.create.2")+":该用户名已存在");
			}
			sysHelpAdminEventLog(flag, userService.getCurrentSysAdmin(request), userAdmin, 
					EventLogConstants.SYSTEM_ADMIN_USER_CREATE, EventLogConstants.SITE_ADMIN_USER_CREATE, "新建普通站点用户", user, request);
			return returnJsonStr(ConstantUtil.CREATESITEUSER_SUCCEED, ResourceHolder.getInstance().getResource("siteAdmin.siteUser.create.1"));
		}
	}
	
	@AsController(path = "downTemp")
	public void downloadTemplate(HttpServletRequest request,HttpServletResponse response){
		String lang = getLang(request);
		String dirPath = LiberContainer.getContainer().getServletContext().getRealPath("excel_template")+File.separator;
		String tempPath = "";
		if(lang.startsWith(ConstantUtil.LANG_CN)){
			tempPath = dirPath+"template_user_zh.xlsx";
		}else{
			tempPath = dirPath+"template_user_en.xlsx";
		}
		File file = new File(tempPath);
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=template_user.xlsx");
        BufferedInputStream in = null;
        BufferedOutputStream out = null; 
        try {
        	in = new BufferedInputStream(new FileInputStream(file));
        	out = new BufferedOutputStream(response.getOutputStream());
        	byte[] bts = new byte[1024*20];
        	int temp = 0;
        	while((temp = in.read(bts))!=-1){
        		out.write(bts,0,temp);
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				in.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 导出
	 * @param request
	 * @param response
	 */
	@AsController(path = "exportUser")
	public void export(@CParam("keyword") String keyword,HttpServletRequest request,HttpServletResponse response){
		UserBase currUserBase = userService.getCurrentSiteAdmin(request);
		Integer creator = null;
		if(!currUserBase.isSuperSiteAdmin()){
			creator = currUserBase.getId();
		}
		List<UserBase>	users = enterpriseAdminsService.getSiteUserBases(keyword, null, null, currUserBase.getSiteId(), creator);
		List<Object[]> objlist = new ArrayList<Object[]>();
//		登录名，密码，用户名，英文名，用户角色，邮箱，电话，电话会议，自动外呼，
//		集成VoIP，密码字段的值为空，导出文件名为：export_user.xls
		Object[] titles = new Object[9];
		titles[0] = ResourceHolder.getInstance().getResource("system.sysUser.list.loginName");// "登录名";
		titles[1] = ResourceHolder.getInstance().getResource("system.sysUser.list.loginPass");//"密码";
		titles[2] = ResourceHolder.getInstance().getResource("system.sysUser.list.userName");//"用户名";
		titles[3] = ResourceHolder.getInstance().getResource("system.sysUser.list.enName");//"英文名";
		titles[4] = ResourceHolder.getInstance().getResource("site.admin.edituser.userrole");//"用户角色";
		titles[5] = ResourceHolder.getInstance().getResource("system.sysUser.list.email");//"邮箱";
		titles[6] = ResourceHolder.getInstance().getResource("system.sysUser.list.telephone");//"电话";
		objlist.add(titles);//添加头信息
		//添加数据信息
		if(users!=null && users.size()>0){
			for (Iterator<UserBase> it = users.iterator(); it.hasNext();) {
				UserBase user =  it.next();
				Object[] userObjs = new Object[9];
				userObjs[0] = user.getLoginName();
				userObjs[1] = "";
				userObjs[2] = user.getTrueName()==null?"":user.getTrueName();
				userObjs[3] = user.getEnName()==null?"":user.getEnName();
				if(user.getUserRole().equals(ConstantUtil.USERROLE_HOST)){
					userObjs[4] = ResourceHolder.getInstance().getResource("site.admin.edituser.role1");//"主持人";
				}else{
					userObjs[4] = ResourceHolder.getInstance().getResource("site.admin.edituser.role2");//"参会者";
				}
				userObjs[5] = user.getUserEmail()==null?"":user.getUserEmail();
				userObjs[6] = user.getMobile()==null?"":user.getMobile();
				objlist.add(userObjs);
			}
		}
		HSSFWorkbook wb = ExcelUtil.createExcelWorkbook("users", objlist);
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=export_user.xls");
        try {
        	wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			users = null;
			objlist = null;
			wb = null;
		}
	}
	
	/**
	 * 导入
	 * @param request
	 * @param response
	 */
	@AsController(path = "importUser")
	public Object importUserBases(@CParam("excelfile") LiberCFile file,HttpServletRequest request,HttpServletResponse response){
		UserBase currUser = userService.getCurrentSiteAdmin(request);
		SiteBase site = siteService.getSiteBaseById(currUser.getSiteId());
//		List<SiteOrg> siteOrgList = orgService.getSiteOrgList(site.getId()).getDatas();
//		SiteOrg siteOrg = null;
//		if(siteOrgList != null){
//			for(SiteOrg org:siteOrgList){
//				siteOrg = org;
//				break;
//			}
//		}
		try {
			List<UserBase> importusers = new ArrayList<UserBase>();//可成功导入的用户
			List<UserBase> repeatusers = new ArrayList<UserBase>();//数据库中已存在的用户
			List<UserBase> unSaveableusers = new ArrayList<UserBase>();//数据格式不对或者不全的
			Set<String> loginNames = new HashSet<String>();
			Set<String> userEmails = new HashSet<String>();
			if(file!=null){
				List<Object[]> datas = ExcelUtil.getDataByInputStream(file.getInputStream(), file.getOriginalFilename(), 2, 0);
				for (Iterator<Object[]> it = datas.iterator(); it.hasNext();) {
					Object[] objs = it.next();
					UserBase user = new UserBase();
					initSiteAdmin(user, currUser);
					user.setSiteId(currUser.getSiteId());
					user.setUserType(ConstantUtil.USERTYPE_USERS);
					user.setLoginName(objs[0]==null?null:objs[0].toString());
					user.setLoginPass(objs[1]==null?"":MD5.encodePassword(objs[1].toString(), "MD5"));
					user.setTrueName(objs[2]==null?"":objs[2].toString());
					user.setEnName(objs[3]==null?"":objs[3].toString());
					user.setPassEditor(currUser.getId());
//					if(siteOrg != null){
//						user.setOrgId(siteOrg.getId());
//						user.setOrgCode(siteOrg.getOrgCode());
//					}
					//userRole
					if(site.getChargeMode()!=null && site.getChargeMode().intValue()==1){
						user.setUserRole(ConstantUtil.USERROLE_PARTICIPANT);
					}else if(objs[4]!=null){
						if(objs[4].toString().equalsIgnoreCase("主持人")|| objs[4].toString().equalsIgnoreCase("host")){
							user.setUserRole(ConstantUtil.USERROLE_HOST);
						}else{
							user.setUserRole(ConstantUtil.USERROLE_PARTICIPANT);
						}
					}else{
						user.setUserRole(ConstantUtil.USERROLE_PARTICIPANT);
					}
					//name host 模式全是参会者
					user.setUserEmail(objs[5]==null?"":objs[5].toString());
					user.setMobile(objs[6]==null?"":objs[6].toString());
					//去除excel文件中重复的记录
					if(!enterpriseAdminsService.validUserBase(user)){
						unSaveableusers.add(user);
					}else if(!userService.siteUserSaveable(user)||loginNames.contains(user.getLoginName()) || userEmails.contains(user.getUserEmail())){
						repeatusers.add(user);
					}else{
						importusers.add(user);
					}
					loginNames.add(user.getLoginName());
					userEmails.add(user.getUserEmail());
				}
				//用于显示总共多少数据
				request.setAttribute("itemnum",datas.size());	
			}
			request.setAttribute("statu", 2);
			//成功导入的
			if(importusers.size()>0){
				//生成默认权限设置
				EmpowerConfig siteConfig = empowerConfigService.getSiteEmpowerConfigBySiteId(site.getId());
				EmpowerConfig userConfig = empowerConfigService.makeEmpowerForUser(siteConfig, null);
				boolean flag = enterpriseAdminsService.batchAddUsers(importusers,userConfig);
				if(flag){
					request.setAttribute("statu", 1);
				}
			}
			request.setAttribute("iptitemnum",importusers.size());
			//重复账号的
			if(repeatusers.size()>0){
				request.setAttribute("repeated",repeatusers);
			}
			if(unSaveableusers.size()>0){
				request.setAttribute("dissaveable",unSaveableusers);
			}
		}catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("statu", 2);
		}
		return new ActionForward.Forward("/jsp/admin/importCallback.jsp");
	}
	
	
	
	/**
	 * 导入文弹出层JSP
	 * @param request
	 * @param response
	 * @return
	 */
	@AsController(path = "goImport")
	public Object importUserBases(HttpServletRequest request,HttpServletResponse response){
		return new ActionForward.Forward("/jsp/admin/import.jsp");
	}
	/**
	 * 前台的ajax验证
	 * @param loginName
	 * @param siteId
	 * @return
	 */ 
	@AsController(path = "loginNameValidate")
	public String loginNameValidate(@CParam("loginName") String loginName, @CParam("adminId") Integer adminId,@CParam("siteId") Integer siteId){
		//说明是修改
		if(adminId!=null && adminId>0){
			UserBase user = userService.getUserBaseById(adminId);
			if(StringUtil.isNotBlank(loginName) && user.getLoginName().equals(loginName)){
				return "true";
			}
		}
		UserBase otherUser = userService.getSiteUserByLoginName(siteId, loginName);
		if(otherUser==null){ //存在有相同登录名的账号
			return "true";
		}
		return "false";
	}
	
	private void initSiteAdmin(UserBase siteUser, UserBase creator){
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		siteUser.setCreateTime(DateUtil.getGmtDate(null));   //创建时间初始化为GMT时间
		siteUser.setCreateUser(creator.getId());
		siteUser.setSiteId(creator.getSiteId());
		siteUser.setDelFlag(ConstantUtil.DELFLAG_UNDELETE);
		siteUser.setErrorCount(0);
		siteUser.setUserLogo("");
		siteUser.setUserSort(0);
		siteUser.setUserStatus(ConstantUtil.LOCKFLAG_UNLOCK);
		siteUser.setUserType(ConstantUtil.USERTYPE_ADMIN);
		siteUser.setLoginCount(0);
		siteUser.setTimeZone(currentSite.getTimeZone());
		siteUser.setTimeZoneId(currentSite.getTimeZoneId());
		try {
			siteUser.setLastErrorTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00")));
			siteUser.setDelTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00")));
		} catch (ParseException e) {
			 e.printStackTrace();
		}
		siteUser.setDelUser(0);
	}
	
	//返回信息
	private String returnJsonStr(int status, Object object){
		JSONObject json = new JSONObject();
		JSONArray jsonArrSite = new JSONArray();
		json.put("status", status);
		jsonArrSite.add(object.toString());
		json.put("userBase", jsonArrSite);
		json.put("message",object.toString());
		return json.toString();
	}
	
	
//	private Map<Integer, EmpowerConfig> getUsersPermissions(List<UserBase> users){
//		
//	}
}
