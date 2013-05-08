package com.bizconf.audio.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.ConfTelConfigService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;


@ReqPath("config")
@Interceptors({SiteAdminInterceptor.class})
public class ConfTelConfigController extends BaseController {
	
	private final Logger logger=Logger.getLogger(ConfTelConfigController.class);
	
	@Autowired
	EventLogService eventLogService;
	@Autowired
	UserService userService;
	@Autowired
	ConfTelConfigService confTelConfigService;
	@Autowired
	EmpowerConfigService empowerConfigService;
	
	@AsController(path = "configinfo")
	public Object configPageForAdmin(HttpServletRequest request){
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		//根据站点ID号取站点的授权配置
		EmpowerConfig sitePower = empowerConfigService.getSiteEmpowerConfigBySiteId(currentSiteAdmin.getSiteId());
//		Integer phoneFlag = sitePower.getPhoneFlag();
//		Integer autoFlag = sitePower.getAutoFlag();
//		Integer shareMediaFlag = sitePower.getShareMediaFlag();
//		Integer recordFlag = sitePower.getRecordFlag();
		
		//根据站点ID号取站点的全局变量设置
		EmpowerConfig globalConfig  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentSiteAdmin.getSiteId());
		boolean isNewGlobal=false;
		if(globalConfig == null){       //若该站点无全局变量设置,则新增全局变量设置
			globalConfig = new EmpowerConfig();
			globalConfig.initEmpower(currentSiteAdmin);
			isNewGlobal=true;
		}
		int powerCount=0;
		String[][] fieldArray=SiteConstant.EMPOWER_CODE_FIELD_ARRAY;
		if(fieldArray!=null && fieldArray.length > 0){
			Integer eachValue=null;
			for(String[] field:fieldArray){
				if(field!=null && field.length > 0 ){
					eachValue=(Integer)ObjectUtil.getFieldValue(sitePower, field[1]);
					globalConfig=(EmpowerConfig)ObjectUtil.setFieldValue(globalConfig, field[1], eachValue);
					if("1".equals(field[2])){
						if(SiteConstant.EMPOWER_ENABLED.equals(eachValue)){
							powerCount++;
							request.setAttribute("is"+StringUtil.firstUpper(field[1]), eachValue);
							if(isNewGlobal){
								globalConfig.setAutoFlag(SiteConstant.EMPOWER_ENABLED);
							}
						}
					}
				}
			}
		}
//		if(SiteConstant.EMPOWER_ENABLED.equals(phoneFlag)){//phoneFlag != null && phoneFlag.intValue() == SiteConstant.EMPOWER_ENABLED){    //电话功能已授权
//			request.setAttribute("isPhoneFlag", phoneFlag);
//		}
//		if(SiteConstant.EMPOWER_ENABLED.equals(autoFlag)){// != null && autoFlag.intValue() == SiteConstant.EMPOWER_ENABLED){
//			request.setAttribute("isAutoFlag", autoFlag);
//		}
//		if(SiteConstant.EMPOWER_ENABLED.equals(shareMediaFlag)){// != null && shareMediaFlag.intValue() == SiteConstant.EMPOWER_ENABLED){
//			request.setAttribute("isShareMediaFlag", shareMediaFlag);
//		}
//		if(SiteConstant.EMPOWER_ENABLED.equals(recordFlag)){// != null && recordFlag.intValue() == SiteConstant.EMPOWER_ENABLED){
//			request.setAttribute("isRecordFlag", recordFlag);
//		}
		//根据站点ID号取站点的全局变量设置
//		EmpowerConfig empowerConfig  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentSiteAdmin.getSiteId());
//		if(empowerConfig == null){       //若该站点无全局变量设置,则新增全局变量设置
//			empowerConfig = new EmpowerConfig();
//			empowerConfig.initEmpower(currentSiteAdmin);
//			if(SiteConstant.EMPOWER_ENABLED.equals(phoneFlag)){    //电话功能已授权
//				empowerConfig.setPhoneFlag(SiteConstant.EMPOWER_ENABLED);
//			}else{
//				empowerConfig.setPhoneFlag(SiteConstant.EMPOWER_DISABLED);
//			}
//			if(SiteConstant.EMPOWER_ENABLED.equals(autoFlag)){
//				empowerConfig.setAutoFlag(SiteConstant.EMPOWER_ENABLED);
//			}else{
//				empowerConfig.setAutoFlag(SiteConstant.EMPOWER_DISABLED);
//			}
//			if(SiteConstant.EMPOWER_ENABLED.equals(shareMediaFlag)){
//				empowerConfig.setShareMediaFlag(SiteConstant.EMPOWER_ENABLED);
//			}else{
//				empowerConfig.setShareMediaFlag(SiteConstant.EMPOWER_DISABLED);
//			}
//			if(SiteConstant.EMPOWER_ENABLED.equals(recordFlag)){
//				empowerConfig.setRecordFlag(SiteConstant.EMPOWER_ENABLED);
//			}else{
//				empowerConfig.setRecordFlag(SiteConstant.EMPOWER_DISABLED);
//			}
//			empowerConfigService.saveSiteEmpowerGlobal(empowerConfig);
//		}
		request.setAttribute("powerCount", powerCount);
		request.setAttribute("sitePower", sitePower);
		request.setAttribute("globalConfig", globalConfig);
		return new ActionForward.Forward("/jsp/admin/conf_tel_config.jsp");
	}
	
	/**
	 * 修改会议功能全局设置
	 * wangyong
	 * 2013-3-28
	 */
	@AsController(path = "telconfig/update")
	public Object updateTelConfig(EmpowerConfig newEmpowerConfig, HttpServletRequest request){
		logger.info("newEmpowerConfig:" + newEmpowerConfig);
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		//根据站点ID号取站点的授权配置
		EmpowerConfig sitePower = empowerConfigService.getSiteEmpowerConfigBySiteId(currentSiteAdmin.getSiteId());
		//根据站点ID号取站点的全局变量设置
		EmpowerConfig empowerConfig  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentSiteAdmin.getSiteId());
		
		if(empowerConfig==null){
			empowerConfig=new EmpowerConfig();
			empowerConfig.initEmpower(currentSiteAdmin);
		}
		
		String[][] fieldArray=SiteConstant.EMPOWER_CODE_FIELD_ARRAY;
		if(fieldArray!=null && fieldArray.length > 0){
			Integer eachPowerValue=null;
			Integer eachGolbalValue=null;
			for(String[] field:fieldArray){
				if(field!=null && field.length > 0 && "1".equals(field[2])){
					eachPowerValue=(Integer)ObjectUtil.getFieldValue(sitePower, field[1]);
					if(SiteConstant.EMPOWER_ENABLED.equals(eachPowerValue)){
						eachGolbalValue=(Integer)ObjectUtil.getFieldValue(newEmpowerConfig, field[1]);
						empowerConfig=(EmpowerConfig)ObjectUtil.setFieldValue(empowerConfig, field[1], eachGolbalValue);
					}
					if(SiteConstant.EMPOWER_DISABLED.equals(eachPowerValue)){
						empowerConfig=(EmpowerConfig)ObjectUtil.setFieldValue(empowerConfig, field[1], SiteConstant.EMPOWER_DISABLED);
					}
					
				}
			}
		}
		
		
		
//		empowerConfig.setPhoneFlag(newEmpowerConfig.getPhoneFlag());
//		if(empowerConfig.getPhoneFlag().intValue() == SiteConstant.EMPOWER_DISABLED){
//			empowerConfig.setAutoFlag(SiteConstant.EMPOWER_DISABLED);
//		}else if(empowerConfig.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED){
//			empowerConfig.setAutoFlag(newEmpowerConfig.getAutoFlag());
//		}
//		empowerConfig.setShareMediaFlag(newEmpowerConfig.getShareMediaFlag());
//		empowerConfig.setRecordFlag(newEmpowerConfig.getRecordFlag());
//		if(sitePower == null || sitePower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //只要站点未授权，不管页面传值，全局变量必须设置为禁用
//			empowerConfig.setPhoneFlag(SiteConstant.EMPOWER_DISABLED);
//		}
//		if(sitePower == null || sitePower.getAutoFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //只要站点未授权，不管页面传值，全局变量必须设置为禁用
//			empowerConfig.setAutoFlag(SiteConstant.EMPOWER_DISABLED);
//		}
//		if(sitePower == null || sitePower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //只要站点未授权，不管页面传值，全局变量必须设置为禁用
//			empowerConfig.setShareMediaFlag(SiteConstant.EMPOWER_DISABLED);
//		}
//		if(sitePower == null || sitePower.getRecordFlag().intValue() == SiteConstant.EMPOWER_DISABLED){     //只要站点未授权，不管页面传值，全局变量必须设置为禁用
//			empowerConfig.setRecordFlag(SiteConstant.EMPOWER_DISABLED);
//		}
		boolean savedConfig = empowerConfigService.updateSiteEmpowerGlobal(empowerConfig);
		if(savedConfig){
			setInfoMessage(request,"修改会议功能全局设置成功！");
			eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ADMIN_CONFAUTHORITY_SETUP, 
					ResourceHolder.getInstance().getResource("siteAdmin.ConfAuthority.update.1"), 
					EventLogConstants.EVENTLOG_SECCEED, null, request);   //设置成功后写EventLog
		}else{
			setErrMessage(request, "修改会议功能全局设置失败！");
			eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ADMIN_CONFAUTHORITY_SETUP, 
					ResourceHolder.getInstance().getResource("siteAdmin.ConfAuthority.update.2"), 
					EventLogConstants.EVENTLOG_FAIL, null, request);   //设置失败后写EventLog
		}
		return new ActionForward.Forward("/admin/config/configinfo");
	}
	
//	@AsController(path = "telconfig/save")
//	public Object saveTelConfig(HttpServletRequest request){
//		Integer allowTelFlag =IntegerUtil.parseInteger(String.valueOf(request.getParameter("allowTelFlag")));
//		Integer autoCallFlag=IntegerUtil.parseInteger(String.valueOf(request.getParameter("autoCallFlag")));
//		Integer voipFlag=IntegerUtil.parseInteger(String.valueOf(request.getParameter("voipFlag")));
//		Integer id=IntegerUtil.parseInteger(String.valueOf(request.getParameter("id")));
//		Integer siteId=IntegerUtil.parseInteger(String.valueOf(request.getParameter("siteId")));
//
////		String domain = SiteIdentifyUtil.getCurrentBrand()+"."+SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
////		Integer cookieSiteId=IntegerUtil.parseInteger( CookieUtil.getCookieByDomain(request, LoginConstants.SITE_ADMIN_USER_SESSION_ID_NAME, domain));
//		UserBase user=userService.getCurrentSiteAdmin(request);
//		if(siteId==null || siteId.intValue()<=0){
//			return  new ActionForward.Redirect("/admin/config/configinfo");
//		}
//		if(user==null || !user.getSiteId().equals(siteId)){
//			return  new ActionForward.Redirect("/admin/config/configinfo");
//		}
//		if(allowTelFlag==null){
//			allowTelFlag=0;
//		}
//		if(autoCallFlag==null){
//			autoCallFlag=0;
//		}
//		if(voipFlag==null){
//			voipFlag=0;
//		}
//		ConfTelConfig config=new ConfTelConfig();
//
//		config.setAllowTelFlag(allowTelFlag);
//		config.setAutoCallFlag(autoCallFlag);
//		config.setVoipFlag(voipFlag);
//		config.setSiteId(siteId);
//		config.setUserId(0);
//		config.setLastModifyTime(DateUtil.getGmtDate(null));
//		config=confTelConfigService.save(config);
//
//		logger.info("config="+config);
//		return new ActionForward.Redirect("/admin/config/configinfo");
//	}

}
