package com.bizconf.audio.action.admin;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.EmailConfig;
import com.bizconf.audio.entity.EmailInfo;
import com.bizconf.audio.entity.EmailTemplate;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.EmailConfigService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.EmailTemplateService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.JsonUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath("email")
@Interceptors({SiteAdminInterceptor.class})
public class EmailController extends BaseController{
	
	private final  Logger logger = Logger.getLogger(EmailController.class);
	
	@Autowired
	EmailConfigService emailConfigService;
	@Autowired
	EmailTemplateService emailTemplateService;
	@Autowired
	EmailService emailService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	UserService userService;
	
	/**
	 * 获取系统默认HOST配置。并返回页面
	 * @return
	 */
	@AsController(path = "showhost")
	public Object showHost(HttpServletRequest request){
		EmailConfig emailConfig=null;
		try {
			UserBase userBase = userService.getCurrentSiteAdmin(request);
			emailConfig=emailConfigService.getConfigBySiteId(userBase.getSiteId());
			if(emailConfig!=null){
				request.setAttribute("emailConfig", emailConfig);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ActionForward.Forward("/jsp/admin/email_config.jsp");
	}
	

	/**
	 * 保存系统默认HOST配置。，并返回页面
	 * @return
	 */
	@AsController(path = "savehost")
	public Object saveHost(EmailConfig emailConfig,HttpServletRequest request,HttpServletResponse response){
		logger.info(emailConfig);
		boolean saveFlag = false;
		Integer msgCode=0;
		UserBase currUser = userService.getCurrentSiteAdmin(request);
		if(emailConfig!=null){
			try {
				emailConfig.setCreateUser(currUser.getId());
				emailConfig=emailConfigService.saveSiteConfig(emailConfig);
				if(emailConfig != null && emailConfig.getId().intValue() > 0){
					saveFlag = true;
				}
				request.setAttribute("emailConfig", emailConfig);
				msgCode=1;
				
			} catch (Exception e) {
				e.printStackTrace();
				msgCode=2;
			}
			request.setAttribute("msgCode", msgCode);
		}
		sysHelpAdminEventLog(saveFlag, userService.getCurrentSysAdmin(request), currUser, 
				EventLogConstants.SYSTEM_ADMIN_EMAILSERVER_SETUP, EventLogConstants.SITE_ADMIN_EMAILSERVER_SETUP, "保存系统默认HOST配置", null, request);
		return new ActionForward.Forward("/jsp/admin/email_config.jsp");
	}
	
	
	/**
	 * 测试发送邮件
	 * @param toEmail
	 * @param emailBody
	 * @return
	 */
	@AsController(path = "checkHost")
	public Object checkHost(@CParam("toEmail") String toEmail,@CParam("emailBody") String emailBody,HttpServletRequest request){
		EmailInfo emailInfo=new EmailInfo();
		emailInfo.setEmailAccepter(toEmail);
		emailInfo.setEmailContent(emailBody);
		EmailConfig emailConfig=null;
		boolean saveFlag = false;
		Integer msgCode=0;
		UserBase userBase = null;
		try {
			userBase = userService.getCurrentSiteAdmin(request);
			emailInfo.setSiteId(userBase.getSiteId());
			emailConfig=emailConfigService.getConfigBySiteId(userBase.getSiteId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(emailConfig!=null){
			
			emailInfo.setCreateTime(new Date());
			emailInfo.setEmailSubject("测试发送邮件信息!/Test send email");
			emailInfo.setContentType("html");
			emailInfo.setFromEmail(emailConfig.getEmailSender());
			emailInfo.setFromName(emailConfig.getEmailName());
			
			emailInfo.setValidate(true);
			emailInfo.setServerHost(emailConfig.getEmailHost());
			emailInfo.setServerPort("25");
			emailInfo.setUserName(emailConfig.getEmailAccount());
			emailInfo.setUserPass(emailConfig.getEmailPassword());
			emailInfo.setRetryCount(10); 
			saveFlag = emailService.saveEmailInfo(emailInfo);
			sysHelpAdminEventLog(saveFlag, userService.getCurrentSysAdmin(request), userBase, 
					EventLogConstants.SYSTEM_ADMIN_EMAILTEST, EventLogConstants.SITE_ADMIN_EMAILTEST, "测试发送邮件", null, request);
			request.setAttribute("emailConfig", emailConfig);
			msgCode=3;
		}else{
			msgCode=4;
		}
		request.setAttribute("msgCode", msgCode);
		return new ActionForward.Forward("/jsp/admin/email_config.jsp");
	}
	 
	
	@AsController(path = "goTemplateEdit")
	public Object toEmailTemp(){
		return new ActionForward.Forward("/jsp/admin/emailTemplate.jsp");
	}
	/**
	 * 系统管理员获取系统默认模板内容
	 * @return
	 */
	@AsController(path = "viewSiteTemplate")
	public void switchTemplateByType(@CParam("tempType")Integer tempType,HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("pragma","no-cache");
		response.setHeader("cache-control","no-cache");
		
		EmailTemplate template = null;
		PrintWriter out = null;
		String json = null;
		try {
			UserBase userBase = userService.getCurrentSiteAdmin(request);
			out = response.getWriter();
			template = emailTemplateService.getTemplateBySiteId(userBase.getSiteId(),tempType);
			template=(EmailTemplate)ObjectUtil.parseChar(template, "emailContent");
//			template.setEmailContent(template.getEmailContent().replaceAll("\\$\\{siteaddress\\}", SiteIdentifyUtil.getCurrentDomine()));
			json = JsonUtil.parseJsonWithObject(template).toString();
			//logger.info("the json object is :: "+json);
		} catch (Exception e) {
			//TODO Auto-generated catch block
			json = ConstantUtil.ERROR_FLAG;
			e.printStackTrace();
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 修改系统默认模板内容
	 * @param request
	 * @param response
	 */
	@AsController(path = "updateSiteTemplate")
	public void updateSiteTemplateContent(EmailTemplate template,HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("pragma","no-cache");
		response.setHeader("cache-control","no-cache");
		PrintWriter out = null;
		String json = null;
		try {
			out = response.getWriter();
			UserBase user= userService.getCurrentSiteAdmin(request);
			if(user==null){
				throw new RuntimeException("获取当前登录用户失败！");
			}
			template.setSiteId(user.getSiteId());
			template.setCreateUser(user.getId());
			//template=(EmailTemplate)ObjectUtil.parseHtml(template, "emailContent");
			boolean saveFlag = emailTemplateService.saveOrUpdateSiteTemplate(template);
			if(saveFlag){
				json = ConstantUtil.SUCCESS_FLAG;
			}else{
				json = ConstantUtil.ERROR_FLAG;
			}
			sysHelpAdminEventLog(saveFlag, userService.getCurrentSysAdmin(request), user, 
					EventLogConstants.SYSTEM_ADMIN_EMAILMODEL_SETUP, EventLogConstants.SITE_ADMIN_EMAILMODEL_SETUP, "修改邮件模板", null, request);
		} catch (Exception e) {
			e.printStackTrace();
			json = ConstantUtil.ERROR_FLAG;
		}finally{
			JSONObject data = new JSONObject();
			data.put("message", json);
			out.print(data.toString());
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 恢复系统模板内容
	 * @param template
	 * @param request
	 * @param response
	 */
	@AsController(path = "recoverSiteTemplate")
	public void recoverSiteTemplateContent(@CParam("type")int type,HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("pragma","no-cache");
		response.setHeader("cache-control","no-cache");
		
		EmailTemplate template = null;
		PrintWriter out = null;
		String json = "";
		try {
			out = response.getWriter();
			template = emailTemplateService.getSysTemplateByType(type);
			
			template=(EmailTemplate)ObjectUtil.parseChar(template, "emailContent");
//			template.setEmailContent(template.getEmailContent().replaceAll("\\$\\{siteaddress\\}", SiteIdentifyUtil.getCurrentDomine()));
			if(template!=null){
				json = JsonUtil.parseJsonWithObject(template).toString();
			}
			//logger.info("the json object is :: "+json);
		} catch (Exception e) {
			json = ConstantUtil.ERROR_FLAG;
			e.printStackTrace();
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}
}
