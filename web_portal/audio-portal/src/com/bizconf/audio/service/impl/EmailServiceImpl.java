package com.bizconf.audio.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.component.BaseConfig;
import com.bizconf.audio.component.email.EmailContentGenerator;
import com.bizconf.audio.component.email.EmailUtil;
import com.bizconf.audio.component.email.model.SendMail;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EmailConstant;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfCycle;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.EmailConfig;
import com.bizconf.audio.entity.EmailInfo;
import com.bizconf.audio.entity.EmailTemplate;
import com.bizconf.audio.entity.License;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.logic.EmailLogic;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.encrypt.Base64;
@Service
public class EmailServiceImpl extends BaseService implements EmailService{
	
	/**邮件服务获取业务数据接口*/
	@Autowired
	private EmailLogic emailLogic; 
	
	@Autowired
	private ConfUserLogic confUserLogic;
	@Autowired
	UserService userService;
	
	@Override
	public boolean createSiteEmail(SiteBase site,UserBase admin) {
		boolean createStatus=false;
		if(site!=null && admin!=null){
			try{
				
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(admin.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				if(site!=null && !site.getChargeMode().equals(2)){
					site.setLicense(emailLogic.getSiteLicenseNum(site.getId()));
				}
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("userBase", admin);
				datas.put("siteBase",site);
				datas.put("timezone", site.getTimeZoneDesc());
				
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.CRAETE_SITE_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(site.getId());
				emailInfo.setEmailSubject("企业站点管理平台 / Enterprise Site info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(admin.getUserEmail());
				saveEmailInfo(emailInfo);
				createStatus = true;
			}catch(Exception e){
				createStatus = false;
				e.printStackTrace();
			}
		}
		return createStatus;
	}
	
	
	/**
	 * 修改站点信息
	 */
	@Override
	public boolean updateSiteEmail(SiteBase site,UserBase admin) {
		boolean updateStatus=false;
		if(site!=null && admin!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(admin.getSiteId());
				if(site!=null && !site.getChargeMode().equals(2)){
					site.setLicense(emailLogic.getSiteLicenseNum(site.getId()));
				}
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("userBase", admin);
				datas.put("siteBase",site);
				datas.put("timezone", site.getTimeZoneDesc());
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.UPDATE_SITE_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(site.getId());
				emailInfo.setEmailSubject("企业站点管理平台 /Enterprise Site info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(admin.getUserEmail());
				saveEmailInfo(emailInfo);
				updateStatus = true;
			}catch(Exception e){
				updateStatus = false;
				e.printStackTrace();
			}
		}
		return updateStatus;
	}
	
	
	@Override
	public boolean saveEmailInfo(EmailInfo emailInfo){
		boolean saveStatus=false;
		try {
			libernate.saveEntity(emailInfo);
			saveStatus=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveStatus;
	}

	@Override
	public List<EmailInfo> getUnSendEmailListByStartId(Long startId, Integer limit) {
		
		List<EmailInfo> emailList=null;
		if (startId != null && startId.intValue() >= 0) {
			String condition = " id > ?  and (send_flag = ? or send_flag = ?) and send_count <= retry_count";
			Object[] values = new Object[3];
			try {
				if (limit == null || limit.intValue() <= 0) {
					limit = ConstantUtil.PAGESIZE_DEFAULT;
				}
				values[0] = startId;
				values[1] = ConstantUtil.EMAIL_FLAG_UNSEND;
				values[2] = ConstantUtil.EMAIL_FLAG_FAIL;
				emailList = DAOProxy.getLibernate().getEntityListWithCondition(condition, values, EmailInfo.class, 1, limit);
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				values = null;
				condition = null;
			}
		}
		return emailList;
	}

	@Override
	public boolean updateSucceedEmailById(Long id) {
		boolean updateFlag=false;
		if (id != null && id.intValue() > 0){
			String updateSql="update t_email_info set send_flag = ? , send_count = send_count + 1 ,send_time = ?  where id = ? ";
			Object[] values = new Object[3];
			try {
				values[0] = ConstantUtil.EMAIL_FLAG_SECCEED;
				values[1] = new Date();
				values[2] = id;
				DAOProxy.getLibernate().executeSql(updateSql, values);
				updateFlag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				values = null;
				updateSql = null;
			}
		}
		return updateFlag;
	}
	
	@Override
	public boolean updateUnSucceedEmailById(Long id) {
		boolean updateFlag=false;
		if (id != null && id.intValue() > 0){
			String updateSql="update t_email_info set send_flag = ? , send_count = send_count + 1 where id = ? ";
			Object[] values = new Object[2];
			try {
				values[0] = ConstantUtil.EMAIL_FLAG_FAIL;
				values[1] = id;
				DAOProxy.getLibernate().executeSql(updateSql, values);
				updateFlag = true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				values = null;
				updateSql = null;
			}
		}
		return updateFlag;
	}

	@Override
	public boolean send(Long id) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public boolean send(EmailInfo emailInfo) {
		boolean sendStatus = false;
		if (emailInfo != null) {
			SendMail mailInfo=new SendMail();
			try {
				mailInfo.setFromEmail(emailInfo.getFromEmail());
				mailInfo.setFromName(emailInfo.getFromName());
				mailInfo.setServerHost(emailInfo.getServerHost());
				mailInfo.setServerPort(emailInfo.getServerPort());
				mailInfo.setValidate(emailInfo.getValidate());
				mailInfo.setUserName(emailInfo.getUserName());
				mailInfo.setUserPass(emailInfo.getUserPass());

				mailInfo.setToEmail(emailInfo.getEmailAccepter());
				
				mailInfo.setSubject(emailInfo.getEmailSubject());
				mailInfo.setContent(emailInfo.getEmailContent());
				mailInfo.setContentType(emailInfo.getContentType());
				
				
				
				mailInfo.setCalFlag(emailInfo.getWarnFlag().intValue()==2?true:false);
				mailInfo.setWarnSubject(emailInfo.getWarnSubject());
				mailInfo.setWarnCount(emailInfo.getWarnCount());
				mailInfo.setBeforeMinute(emailInfo.getBeforeMinute());
				mailInfo.setGapMinute(emailInfo.getGapMinute());
				mailInfo.setStartTime(emailInfo.getStratTime());
				mailInfo.setStopTime(emailInfo.getEndTime());
				mailInfo.setTimeZone(emailInfo.getTimeZone());
				
				/*
				
				mailInfo.setAddress(emailInfo.getConfAddress());
				mailInfo.setCalFlag(false);
//				mailInfo.setCcEmailList(ccEmailList)
				
				mailInfo.setContent(mailInfo.getContent());
				mailInfo.setContentType(emailInfo.getContentType());
				mailInfo.setFromEmail(emailInfo.getFromEmail());
				mailInfo.setFromName(emailInfo.getFromName());
				mailInfo.setToEmail(emailInfo.getEmailAccepter());
				
				mailInfo.setGapMinute(emailInfo.getGapMinute());
				mailInfo.setBeforeMinute(emailInfo.getBeforeMinute());
				mailInfo.setServerHost(emailInfo.getServerHost());
				mailInfo.setServerPort(emailInfo.getServerPort());
				
				*/
				
//				mail
				
				sendStatus=EmailUtil.send(mailInfo);
				if(sendStatus){
//					sendStatus=true;
					
					//将邮件发送标志 改成发送成功标志、同时改一下发送时间 
					
				}else{
					
					//将邮件信息的未成功的发送次数据加 1
					
				}
				
				
			} catch (Exception e) {
				
				e.printStackTrace();
				
				
			}finally{
				
			}
		}
		return sendStatus;
	}
	
	/**
	 * 根据系统邮件设置设置emailInfo对象
	 * @param emailInfo
	 * @param sysConfig
	 */
	private void setSysDefEmailInfo(EmailInfo emailInfo,EmailConfig sysConfig){
	
		emailInfo.setServerHost(sysConfig.getEmailHost());
		emailInfo.setFromEmail(sysConfig.getEmailSender());
		emailInfo.setFromName(sysConfig.getEmailName());
		emailInfo.setUserName(sysConfig.getEmailAccount());
		emailInfo.setUserPass(sysConfig.getEmailPassword());
		emailInfo.setCreateTime(new Date());
		
		emailInfo.setServerPort(EmailConstant.DEF_EMAIL_HOST_PORT);
		emailInfo.setSendFlag(EmailConstant.NOT_SEND_FLAG);
		emailInfo.setValidate(true);
		emailInfo.setContentType(EmailConstant.EMAIL_TYPE_HTML);
		emailInfo.setSendCount(EmailConstant.DEF_SEND_COUNT);
		emailInfo.setRetryCount(EmailConstant.DEF_RETRY_COUNT); //0
		emailInfo.setSendTime(EmailConstant.DEF_SYS_DATE);
		emailInfo.setWarnFlag(EmailConstant.DEF_WARN_FLAG); //0 1 2
		emailInfo.setWarnType(EmailConstant.DEF_WARN_TYPE);
		//emailInfo.setConfAddress("");
		//emailInfo.setWarnSubject("Warn!");
		//emailInfo.setTimeZone("");
		emailInfo.setStratTime(EmailConstant.DEF_SYS_DATE);
		emailInfo.setEndTime(EmailConstant.DEF_SYS_DATE);
		emailInfo.setGapMinute(EmailConstant.DEF_GAP_MINUTE);
		emailInfo.setBeforeMinute(EmailConstant.DEF_BEFORE_MINUTE);
		emailInfo.setWarnCount(EmailConstant.DEF_WARN_COUNT);
	}
	
	/**
	 * 新建系统管理员成功后发送邮件
	 * wangyong
	 * 2013-2-5
	 */
	@Override
	public boolean createSystemUserEmail(SystemUser sysUser) {
		boolean createStatus=false;
		if(sysUser!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(0);
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("user", sysUser);
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.CREATE_SYSUSER_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setEmailSubject("系统管理员帐号信息/ System Administrator account info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(sysUser.getEmail());
				saveEmailInfo(emailInfo);
				createStatus = true;
			}catch(Exception e){
				createStatus = false;
				e.printStackTrace();
			}
		}
		return createStatus; 
	}
	
	/**
	 * 修改系统管理员成功后发送邮件
	 * wangyong
	 * 2013-2-5
	 */
	public boolean updateSystemUserEmail(SystemUser sysUser){
		boolean updateStatus=false;
		if(sysUser!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(0);
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("user", sysUser);
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine());
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.UPDATE_SYSUSER_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setEmailSubject("系统管理员帐号修改信息/ System Administrator account fixed info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(sysUser.getEmail());
				saveEmailInfo(emailInfo);
				updateStatus = true;
			}catch(Exception e){
				updateStatus = false;
				e.printStackTrace();
			}
		}
		return updateStatus; 
	}
	
	
	/**
	 * 重设密码
	 */
	@Override
	public void resetPasswordEmail(SystemUser user,String url) {
		try{
			EmailConfig sysConfig = emailLogic.getSysEmailConfig(0);
			EmailInfo emailInfo =new EmailInfo();
			//获取邮件内容
			Map<String, Object> datas = new HashMap<String,Object>();
			datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine());
			datas.put("user", user);
			datas.put("url", url);
			datas.put("hours", ConstantUtil.PASSWORD_URL_EXPIRE_HOURS);
			String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.SYSUSER_RESETPWD_TEMP, datas);
			logger.info("the email content:"+emailContent);
			setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
			
			emailInfo.setEmailSubject("ConfCloud-找回密码 /ConfCloud- get my password");
			emailInfo.setEmailContent(emailContent);//设置邮件内容
			emailInfo.setEmailAccepter(user.getEmail());
			saveEmailInfo(emailInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 站点管理员重置密码
	 */
	@Override
	public void resetPasswordEmail(UserBase user,String url) throws Exception{
		
		EmailConfig sysConfig = emailLogic.getSysEmailConfig(user.getSiteId());
		EmailInfo emailInfo =new EmailInfo();
		//获取邮件内容
		Map<String, Object> datas = new HashMap<String,Object>();
		datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine());
		datas.put("user", user);
		datas.put("url", url);
		datas.put("hours", ConstantUtil.PASSWORD_URL_EXPIRE_HOURS);
		String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.SYSUSER_RESETPWD_TEMP, datas);
		logger.info("the email content:"+emailContent);
		setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置

		emailInfo.setEmailSubject("找回密码 /Get back my password");
		emailInfo.setEmailContent(emailContent);//设置邮件内容
		emailInfo.setEmailAccepter(user.getUserEmail());
		saveEmailInfo(emailInfo);
	}
	
	@Override
	public boolean createSiteAdminEmail(UserBase user) {
		boolean createStatus=false;
		if(user!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(user.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("user", user);
				//datas.put("siteBase",site);
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.CREATE_SITEADMIN_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(user.getSiteId());
				emailInfo.setEmailSubject("企业管理员帐号信息/Enterprise Administrator account info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(user.getUserEmail());
				saveEmailInfo(emailInfo);
				createStatus = true;
			}catch(Exception e){
				createStatus = false;
				e.printStackTrace();
			}
		}
		return createStatus;
	}

	@Override
	public Boolean createSiteUser(UserBase user) {
		boolean createStatus=false;
		if(user!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(user.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("user", user);
				//datas.put("siteBase",site);
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.CREATE_SITEUSER_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(user.getSiteId());
				emailInfo.setEmailSubject("企业用户帐号信息/Enterprise user account info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(user.getUserEmail());
				saveEmailInfo(emailInfo);
				createStatus = true;
			}catch(Exception e){
				createStatus = false;
				e.printStackTrace();
			}
		}
		return createStatus;
	}

	@Override
	public boolean updateSiteAdmin(UserBase user) {
		boolean updateStatus=false;
		if(user!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(user.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("user", user);
				//datas.put("siteBase",site);
				String emailContent  =  EmailContentGenerator.getInstance().genContent(EmailConstant.UPDATE_SITEADMIN_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(user.getSiteId());
				emailInfo.setEmailSubject("企业管理员帐号修改信息/Enterprise Administrator account info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(user.getUserEmail());
				saveEmailInfo(emailInfo);
				updateStatus = true;
			}catch(Exception e){
				updateStatus = false;
				e.printStackTrace();
			}
		}
		return updateStatus;
	}

	@Override
	public Boolean updateSiteUserEmail(UserBase user) {
		boolean updateStatus=false;
		if(user!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(user.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("user", user);
				//datas.put("siteBase",site);
				String emailContent  =  EmailContentGenerator.getInstance().genContent(EmailConstant.UPDATE_SITEUSER_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(user.getSiteId());
				emailInfo.setEmailSubject("企业用户帐号信息/Enterprise user account info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(user.getUserEmail());
				saveEmailInfo(emailInfo);
				updateStatus = true;
			}catch(Exception e){
				updateStatus = false;
				e.printStackTrace();
			}
		}
		return updateStatus;
	}
	
	
	/**
	 * 发送会议邀请
	 */
	@Override
	public boolean sendConfInvite(List<UserBase> userBases, ConfBase confInfo) {
		 List<ConfUser> confUsers = new ArrayList<ConfUser>();
		 for (Iterator iterator = userBases.iterator(); iterator.hasNext();) {
			UserBase userBase  = (UserBase) iterator.next();
			ConfUser user = new ConfUser();
			user.setUserEmail(userBase.getUserEmail());
			user.setUserName(userBase.getTrueName());
			user.setHostFlag(ConfConstant.CONF_USER_PARTICIPANT);
			confUsers.add(user);
		}
		return sendInviteToConfUser(confUsers,confInfo);
	}
	
	//加入会议 joinUrl=Base64.encode("join/joinpage?joinType=3&cId=1196&scode=81317079","UTF-8");
	/**
	 * 发送会议邀请
	 */
	@Override
	public boolean sendInviteToConfUser(List<ConfUser> users, ConfBase confInfo) {
		boolean flag=false;
		if(users!=null && confInfo!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(confInfo.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				String templateContent = getTemplateContentByType(confInfo.getSiteId(),EmailConstant.TEMPLATE_INVITE);
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				
				//域名的获取在任务中是获取不到的故需要根据站点标识获取
				SiteBase confsite = libernate.getEntity(SiteBase.class, confInfo.getSiteId());
				String siteaddress = SiteIdentifyUtil.getCurrentDomine();
				if(confsite!=null){siteaddress = confsite.getSiteSign()+".confcloud.cn";}
				
				datas.put("siteaddress", siteaddress);
				datas.put("EnterNumber", ConfConstant.ACCESSCODE);
				datas.put("timezone", getTimezoneName(confInfo.getSiteId()));
				datas.put("ispublicconf", ConfConstant.CONF_PUBLIC_FLAG_TRUE.equals(confInfo.getPublicFlag())?"":"style='display:none;'");
				datas.put("istelconf", ConfConstant.CONF_TYPE_PHONE_FUNC.equals(confInfo.getConfType())||ConfConstant.CONF_TYPE_PHONE_VIDEO_FUNC.equals(confInfo.getConfType())?"":"style='display:none;'");
				//接入号
				datas.put("accessNumber1", BaseConfig.getInstance().getString("access_num1", ""));
				datas.put("accessNumber2", BaseConfig.getInstance().getString("access_num2", ""));
				datas.put("accessNumber3", BaseConfig.getInstance().getString("access_num3", ""));
				
				transferConfDate(confInfo);
				datas.put("conf", confInfo);
				for(ConfUser user:users){
					if(user.getUserEmail()==null || user.getUserEmail().equals("")) continue; //目前有可能只有电话
					if(user.getUserName()==null || user.getUserName().trim().equals("")){
						user.setUserName(user.getUserEmail());
					}
					datas.put("user", user);
					datas.put("securityCode", confInfo.getUserSecure());
					if(user.getHostFlag()!=null && user.getHostFlag().intValue() == ConfConstant.CONF_USER_HOST.intValue()){
						datas.put("securityCode", confInfo.getCompereSecure());
					}
					datas.put("joinUrl", getJionUrl(confInfo, user));
					if(user.getId()==null) user.setId(0);
					if(user.getUserId()==null) user.setUserId(0);
					datas.put("acceptUrl", confUserLogic.retrieveAcceptURI(confInfo.getId(), user.getUserId(), user.getUserName(), user.getUserEmail()));
					datas.put("refuseURI", confUserLogic.retrieveRefuseURI(confInfo.getId(), user.getUserId(), user.getUserName(), user.getUserEmail()));
					
					//"yaoqing........";//
					String emailContent  = EmailContentGenerator.getInstance().parseTempToContent(templateContent, datas);
					logger.info("the email content:"+emailContent);
					setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
					
					emailInfo.setSiteId(confInfo.getSiteId());
					emailInfo.setEmailSubject(confInfo.getConfName()+"－会议通知/"+confInfo.getConfName()+"－Conference Notice");
					emailInfo.setEmailContent(emailContent);//设置邮件内容
					emailInfo.setEmailAccepter(user.getUserEmail());
					
					emailInfo.setWarnFlag(2);
					emailInfo.setWarnSubject(emailInfo.getEmailSubject());
					emailInfo.setWarnCount(3);
					emailInfo.setBeforeMinute(30);
					emailInfo.setGapMinute(5);
					emailInfo.setStratTime(new Date(confInfo.getStartTime().getTime()-1800000l));
					emailInfo.setEndTime(confInfo.getStartTime());
					emailInfo.setTimeZone("Asia/Shanghai");
					send(emailInfo);
//					saveEmailInfo(emailInfo);
				}
				flag = true;
			}catch(Exception e){
				flag = false;
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 获取邮件模板内容
	 * @param siteId
	 * @param siteId
	 * @param tempType
	 * @return
	 */
	private String getTemplateContentByType(int siteId,int tempType){
		String tempContent = null;
		EmailTemplate emailTemplate=null;
		StringBuffer condBuffer=new StringBuffer();
		Object[] values=new Object[3];
		try {
			if(siteId>0){
					condBuffer.append(" del_flag = ?  ");
					condBuffer.append(" and site_id = ?");
					condBuffer.append(" and email_type = ?");
					values[0] = ConstantUtil.DELFLAG_UNDELETE;
					values[1] = siteId;
					values[2] = tempType;
					emailTemplate = libernate.getEntityWithCondition(EmailTemplate.class, condBuffer.toString(), values);
			}
			//如果该站点还没有相关模板，取系统模板
			if(emailTemplate==null){
				condBuffer = new StringBuffer();
				condBuffer.append("temp_version = ?  ");
				condBuffer.append(" and sys_reserve_flag = ?  ");
				condBuffer.append(" and email_type = ?");
				values[0] = ConstantUtil.SYS_VERSION; //系统模板 （区别于站点模板）
				values[1] = ConstantUtil.SYS_RESERVE_FLAG_N;//非系统保留版本
				values[2] = tempType;
				List<EmailTemplate> templates =  libernate.getEntityListWithCondition(condBuffer.toString(), values, EmailTemplate.class);
				if(templates.size()>0){
					emailTemplate = templates.get(0);
				}
			}
			tempContent = emailTemplate.getEmailContent();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			condBuffer=null;
			values=null;
		}
		return tempContent;
	}
	
	/**
	 * 创建会议成功后发送会议信息到主持人邮箱
	 * @param user
	 * @param url
	 */
	public boolean createConfEmail(ConfBase conf, ConfCycle confCycle, UserBase currentUser){
		boolean flag=false;
		if(currentUser!=null && conf!=null){
			UserBase user = emailLogic.getUserBaseById(conf.getCompereUser());
			try{
				//域名的获取在任务中是获取不到的故需要根据站点标识获取
				SiteBase confsite = libernate.getEntity(SiteBase.class, conf.getSiteId());
				String siteaddress = SiteIdentifyUtil.getCurrentDomine();
				if(confsite!=null){siteaddress = confsite.getSiteSign()+".confcloud.cn";}
				
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(currentUser.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				//String templateContent = getTemplateContentByType(confInfo.getSiteId(),EmailConstant.TEMPLATE_INVITE);
				//获取邮件内容
				transferConfDate(conf);
				Map<String, Object> datas = new HashMap<String,Object>();
				
				datas.put("siteaddress", siteaddress); //域名的获取
				datas.put("EnterNumber", ConfConstant.ACCESSCODE);
				datas.put("user", user);
				datas.put("conf", conf);
				datas.put("timezone", getTimezoneName(conf.getSiteId()));
				datas.put("accessNumber1", BaseConfig.getInstance().getString("access_num1", ""));
				datas.put("accessNumber2", BaseConfig.getInstance().getString("access_num2", ""));
				datas.put("accessNumber3", BaseConfig.getInstance().getString("access_num3", ""));
				
				
				String joinUrl = "join/joinpage?joinType=3&cId="+conf.getId()+"&scode="+conf.getCompereSecure()+"&uId="+user.getId();
				datas.put("joinUrl", Base64.encode(joinUrl, "UTF-8").replaceAll("/", "_"));
				
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.CREATE_CONF_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(user.getSiteId());
				emailInfo.setEmailSubject(conf.getConfName()+"－会议通知/"+conf.getConfName()+"－Conference Notice");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(user.getUserEmail());
				
				emailInfo.setWarnFlag(2);
				emailInfo.setWarnSubject(conf.getConfName()+"－会议通知/"+conf.getConfName()+"－Conference Notice");
				emailInfo.setWarnCount(3);
				emailInfo.setBeforeMinute(30);
				emailInfo.setGapMinute(10);
				emailInfo.setStratTime(conf.getStartTime());
				emailInfo.setEndTime(conf.getEndTime());
				
				saveEmailInfo(emailInfo);
				flag = true;
			}catch(Exception e){
				flag = false;
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 设置会议提醒
	 * 添加会议到日历提醒for UserBase
	 */
	@Override
	public boolean sendConfRemindEmail(UserBase user, ConfBase conf) {
		ConfUser confUser = new ConfUser();
		confUser.setUserEmail(user.getUserEmail());
		confUser.setUserName(user.getTrueName());
		confUser.setHostFlag(2);
		if(user.getId().equals(conf.getCompereUser())){
			confUser.setHostFlag(1);
		}
		return confRemind(confUser, conf);
	}
	
	
	/**
	 * 
	 * 添加会议到日历提醒for everyBody
	 */
	public boolean remindEmailForAnyOne(String userName,String email,ConfBase conf) {
		ConfUser confUser = new ConfUser();
		confUser.setUserName(userName);
		confUser.setUserEmail(email);
		confUser.setHostFlag(2);
		
		return confRemind(confUser,conf);
	}
	
	/**
	 * 会议提醒
	 */
	@Override
	public boolean confRemind(ConfUser confUser, ConfBase conf) {
		boolean flag=false;
		if(confUser!=null && conf!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(conf.getSiteId());
				//System.out.println("systemconfig === "+ sysConfig);
				String templateContent = getTemplateContentByType(conf.getSiteId(),EmailConstant.TEMPLATE_REMIND);
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				transferConfDate(conf);//将GMT时间转换为当地时间
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("EnterNumber", ConfConstant.ACCESSCODE);
				datas.put("user", confUser);
				datas.put("conf", conf);
				datas.put("timezone", getTimezoneName(conf.getSiteId()));
				
				datas.put("accessNumber1", BaseConfig.getInstance().getString("access_num1", ""));
				datas.put("accessNumber2", BaseConfig.getInstance().getString("access_num2", ""));
				datas.put("accessNumber3", BaseConfig.getInstance().getString("access_num3", ""));
				//控制公开会议密码显示
				datas.put("ispublicconf", ConfConstant.CONF_PUBLIC_FLAG_TRUE.equals(conf.getPublicFlag())?"":"style='display:none;'");
				//控制接入号的显示
				datas.put("istelconf", ConfConstant.CONF_TYPE_PHONE_FUNC.equals(conf.getConfType())||ConfConstant.CONF_TYPE_PHONE_VIDEO_FUNC.equals(conf.getConfType())?"":"style='display:none;'");
				
				
				datas.put("securityCode", conf.getUserSecure());
				if(confUser.getHostFlag()!=null && confUser.getHostFlag().intValue() == ConfConstant.CONF_USER_HOST.intValue()){
					datas.put("securityCode", conf.getCompereSecure());
				}
				datas.put("joinUrl",getJionUrl(conf, confUser));
				
				if(confUser.getId()==null) confUser.setId(0);
				if(confUser.getUserId()==null) confUser.setUserId(0);
				datas.put("acceptUrl", confUserLogic.retrieveAcceptURI(conf.getId(), confUser.getUserId(), confUser.getUserName(), confUser.getUserEmail()));
				datas.put("refuseURI", confUserLogic.retrieveRefuseURI(conf.getId(), confUser.getUserId(), confUser.getUserName(), confUser.getUserEmail()));
				
				String emailContent = "";
				emailContent  = EmailContentGenerator.getInstance().parseTempToContent(templateContent, datas);
				if(emailContent==null){
					emailContent = "这是会议提醒！";
				}	
				logger.info("the email content:"+emailContent);
				
				SendMail mailInfo=new SendMail();
				mailInfo.setFromEmail(sysConfig.getEmailSender());
				mailInfo.setFromName(sysConfig.getEmailName());
				mailInfo.setServerHost(sysConfig.getEmailHost());
				mailInfo.setServerPort("25");
				mailInfo.setUserName(sysConfig.getEmailSender());
				mailInfo.setUserPass(sysConfig.getEmailPassword());
				String mailSubject=conf.getConfName()+"－会议提醒通知/"+conf.getConfName()+"－Conference Remind Notice";
				mailInfo.setSubject(mailSubject);
				mailInfo.setContentType("html");
				mailInfo.setContent(emailContent);
				mailInfo.setValidate(true);
				mailInfo.setToEmail(confUser.getUserEmail());
				mailInfo.setCalFlag(true);
				
				mailInfo.setTimeZone("Asia/Shanghai");
				mailInfo.setStartTime(conf.getStartTime());
				mailInfo.setStopTime(conf.getEndTime());
				mailInfo.setAddress("");
				mailInfo.setWarnSubject(mailSubject+"Asia/Shanghai");
				mailInfo.setBeforeMinute(30);
				mailInfo.setWarnCount(3);
				mailInfo.setGapMinute(15);
				EmailUtil.send(mailInfo);
				flag = true;
			}catch(Exception e){
				flag = false;
				e.printStackTrace();
			}
		}
		return flag;
	}
	 
	
	/**
	 * 取消会议时给参会者的邮件
	 */
	@Override
	public boolean confCancelEmail(List<ConfUser> confUsers,ConfBase conf) {
		boolean flag=false;
		if(confUsers!=null && conf!=null){
			try{
				ConfBase orgConf = libernate.getEntity(ConfBase.class, conf.getId());
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(orgConf.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				String templateContent = getTemplateContentByType(orgConf.getSiteId(),EmailConstant.TEMPLATE_CANCEL);
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("EnterNumber", ConfConstant.ACCESSCODE);
				datas.put("timezone", getTimezoneName(orgConf.getSiteId()));
				transferConfDate(orgConf);
				datas.put("conf", orgConf);
				
				datas.put("accessNumber1", BaseConfig.getInstance().getString("access_num1", ""));
				datas.put("accessNumber2", BaseConfig.getInstance().getString("access_num2", ""));
				datas.put("accessNumber3", BaseConfig.getInstance().getString("access_num3", ""));
				//控制公开会议密码显示
				datas.put("ispublicconf", ConfConstant.CONF_PUBLIC_FLAG_TRUE.equals(conf.getPublicFlag())?"":"style='display:none;'");
				//控制接入号的显示
				datas.put("istelconf", ConfConstant.CONF_TYPE_PHONE_FUNC.equals(conf.getConfType())||ConfConstant.CONF_TYPE_PHONE_VIDEO_FUNC.equals(conf.getConfType())?"":"style='display:none;'");
				for(ConfUser user:confUsers){
					datas.put("user", user);
					datas.put("securityCode", orgConf.getUserSecure());
					if(user.getHostFlag()!=null && user.getHostFlag().intValue() == ConfConstant.CONF_USER_HOST.intValue()){
						datas.put("securityCode", orgConf.getCompereSecure());
					}
					String emailContent  = EmailContentGenerator.getInstance().parseTempToContent(templateContent, datas);
					logger.info("the email content:"+emailContent);
					setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
					
					emailInfo.setSiteId(orgConf.getSiteId());
					emailInfo.setEmailSubject(orgConf.getConfName()+"－会议取消通知/"+orgConf.getConfName()+"－Conference Cancel Notice");
					emailInfo.setEmailContent(emailContent);//设置邮件内容
					emailInfo.setEmailAccepter(user.getUserEmail());
					saveEmailInfo(emailInfo);
				}
				flag = true;
			}catch(Exception e){
				flag = false;
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 会议修改提醒
	 */
	@Override
	public boolean confModifyEmail(List<ConfUser> confUsers, ConfBase conf) {
		boolean flag=false;
		if(confUsers!=null && conf!=null){
			try{
				ConfBase orgConf = libernate.getEntity(ConfBase.class, conf.getId());
				
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(orgConf.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				String templateContent = getTemplateContentByType(orgConf.getSiteId(),EmailConstant.TEMPLATE_MODIFY);
				//获取邮件内容
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", SiteIdentifyUtil.getCurrentDomine()); //域名的获取
				datas.put("EnterNumber", ConfConstant.ACCESSCODE);
				datas.put("timezone", getTimezoneName(orgConf.getSiteId()));
				datas.put("accessNumber1", BaseConfig.getInstance().getString("access_num1", ""));
				datas.put("accessNumber2", BaseConfig.getInstance().getString("access_num2", ""));
				datas.put("accessNumber3", BaseConfig.getInstance().getString("access_num3", ""));
				//控制公开会议密码显示
				datas.put("ispublicconf", ConfConstant.CONF_PUBLIC_FLAG_TRUE.equals(conf.getPublicFlag())?"":"style='display:none;'");
				//控制接入号的显示
				datas.put("istelconf", ConfConstant.CONF_TYPE_PHONE_FUNC.equals(conf.getConfType())||ConfConstant.CONF_TYPE_PHONE_VIDEO_FUNC.equals(conf.getConfType())?"":"style='display:none;'");
				
				String emailSubject = orgConf.getConfName()+"－会议修改通知/"+orgConf.getConfName()+"－Conference Modify Notice";
				//获取数据库中会议信息传入的会议信息不可用
				transferConfDate(orgConf);
				datas.put("conf", orgConf);
				for(ConfUser user:confUsers){
					datas.put("user", user);
					datas.put("securityCode", orgConf.getUserSecure());
					//如果是会议主持人则添加日历提醒
					if(user.getHostFlag()!=null && user.getHostFlag().intValue() == ConfConstant.CONF_USER_HOST.intValue()){
						datas.put("securityCode", orgConf.getCompereSecure());
						emailInfo.setWarnFlag(2);
						emailInfo.setWarnSubject(emailSubject);
						emailInfo.setWarnCount(3);
						emailInfo.setBeforeMinute(30);
						emailInfo.setGapMinute(10);
						emailInfo.setStratTime(conf.getStartTime());
						emailInfo.setEndTime(conf.getEndTime());
					}
					datas.put("joinUrl", getJionUrl(orgConf, user));
					if(user.getId()==null) user.setId(0);
					if(user.getUserId()==null) user.setUserId(0);
					datas.put("acceptUrl", confUserLogic.retrieveAcceptURI(orgConf.getId(), user.getUserId(), user.getUserName(), user.getUserEmail()));
					datas.put("refuseURI", confUserLogic.retrieveRefuseURI(orgConf.getId(), user.getUserId(), user.getUserName(), user.getUserEmail()));
				
					String emailContent  = EmailContentGenerator.getInstance().parseTempToContent(templateContent, datas);
					logger.info("the email content:"+emailContent);
					setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
					
					emailInfo.setSiteId(orgConf.getSiteId());
					emailInfo.setEmailSubject(emailSubject);
					emailInfo.setEmailContent(emailContent);//设置邮件内容
					emailInfo.setEmailAccepter(user.getUserEmail());
					saveEmailInfo(emailInfo);
				}
				flag = true;
			}catch(Exception e){
				flag = false;
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	
	
	/**
	 * 
	 * @param siteId
	 * @return
	 */
	private String getTimezoneName(int siteId){
		String name = "";
		SiteBase site = emailLogic.getSiteBaseById(siteId);
		//String keyName = "website.timezone.city."+site.getTimeZoneId();
		//ResourceHolder.getInstance().getResource(keyName);
		if(site!=null){
			name = site.getTimeZoneDesc();
		}else{
			name="北京";
		}
		return name;
		
	}
	
	
	/**
	 * 转化会议时间为站点的当地时间
	 * @param conf
	 */
	private void transferConfDate(ConfBase conf){
		if(conf!=null){
			if(conf.getStartTime()!=null){
				conf.setStartTime(emailLogic.getSiteLocalDate(conf.getStartTime(), conf.getSiteId()));
			}
			if(conf.getEndTime()!=null){
				conf.setEndTime(emailLogic.getSiteLocalDate(conf.getEndTime(), conf.getSiteId()));
			}
		}
	}


	@Override
	public boolean createNameHost(UserBase host, List<License> licenses) {
		boolean createStatus=false;
		if(host!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(host.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				SiteBase site = libernate.getEntity(SiteBase.class, host.getSiteId());
				if(licenses!=null && !licenses.isEmpty()){
					for (Iterator it = licenses.iterator(); it
							.hasNext();) {
						License lic = (License) it.next();
						if(site!=null && site.getTimeZone()!=null){
							lic.transforLocalDate(site.getTimeZone());
						}else{
							lic.transforLocalDate(28800000);
						}
					}
				}
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", site.getSiteSign()+".confcloud.cn"); //域名的获取
				datas.put("user", host);
				datas.put("lics", licenses);
				//datas.put("siteBase",site);
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.CREATE_HOST_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(host.getSiteId());
				emailInfo.setEmailSubject("主持人帐号信息/Enterprise host account info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(host.getUserEmail());
				saveEmailInfo(emailInfo);
				createStatus = true;
			}catch(Exception e){
				createStatus = false;
				e.printStackTrace();
			}
		}
		return createStatus;
	}


	@Override
	public boolean updateNameHost(UserBase host, List<License> licenses) {
		boolean createStatus=false;
		if(host!=null){
			try{
				EmailConfig sysConfig = emailLogic.getSysEmailConfig(host.getSiteId());
				EmailInfo emailInfo =new EmailInfo();
				//获取邮件内容
				SiteBase site = libernate.getEntity(SiteBase.class, host.getSiteId());
				if(licenses!=null && !licenses.isEmpty()){
					for (Iterator it = licenses.iterator(); it
							.hasNext();) {
						License lic = (License) it.next();
						if(site!=null && site.getTimeZone()!=null){
							lic.transforLocalDate(site.getTimeZone());
						}else{
							lic.transforLocalDate(28800000);
						}
					}
				}
				Map<String, Object> datas = new HashMap<String,Object>();
				datas.put("siteaddress", site.getSiteSign()+".confcloud.cn"); //域名的获取
				datas.put("user", host);
				datas.put("lics", licenses);
				//datas.put("siteBase",site);
				String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.UPDATE_HOST_TEMP, datas);
				logger.info("the email content:"+emailContent);
				setSysDefEmailInfo(emailInfo,sysConfig);//邮件的基本设置
				
				emailInfo.setSiteId(host.getSiteId());
				emailInfo.setEmailSubject("主持人帐号信息/Enterprise host account info");
				emailInfo.setEmailContent(emailContent);//设置邮件内容
				emailInfo.setEmailAccepter(host.getUserEmail());
				saveEmailInfo(emailInfo);
				createStatus = true;
			}catch(Exception e){
				createStatus = false;
				e.printStackTrace();
			}
		}
		return createStatus;
	}
	
	private String getJionUrl(ConfBase conf,ConfUser user){
		String scode = conf.getUserSecure();
		if(user.getHostFlag()!=null && user.getHostFlag().equals(ConstantUtil.USERROLE_HOST)){
			scode = conf.getCompereSecure();
		}
		String uid = user.getUserId()==null?"0":user.getUserId().toString();
		String joinUrl = "join/joinpage?joinType=3&cId="+conf.getId()+"&scode="+scode+"&uId="+uid;
		String ecodeUrl = Base64.encode(joinUrl, "UTF-8").replaceAll("/", "_");
		
		return ecodeUrl;
	}
	
	@Override
	public String getJoinUrlForHost(ConfBase conf){
		String scode = conf.getCompereSecure();
//		String uid = user.getUserId()==null?"0":user.getUserId().toString();
		String domain=SiteIdentifyUtil.getCurrentDomine();
		String joinUrl = "join/joinpage?joinType="+ConfConstant.JOIN_TYPE_EMAIL+"&cId="+conf.getId()+"&scode="+scode+"&uId=0";
		String ecodeUrl = Base64.encode(joinUrl, "UTF-8").replaceAll("/", "_");
		//http://${siteaddress}/?joinUrl=${joinUrl}
		ecodeUrl="http://"+domain+"/?joinUrl="+ecodeUrl;
		return ecodeUrl;
	}
	

	@Override
	public String getJoinUrlForUser(ConfBase conf){
		String scode = conf.getUserSecure();
//		String uid = user.getUserId()==null?"0":user.getUserId().toString();
		String domain=SiteIdentifyUtil.getCurrentDomine();
		String joinUrl = "join/joinpage?joinType="+ConfConstant.JOIN_TYPE_EMAIL+"&cId="+conf.getId()+"&scode="+scode+"&uId=0";
		String ecodeUrl = Base64.encode(joinUrl, "UTF-8").replaceAll("/", "_");
		ecodeUrl="http://"+domain+"/?joinUrl="+ecodeUrl;
		return ecodeUrl;
	}

//	
//	@Override
//	public List<SiteBase> getExpireRemindSites() {
//		String sql = "select * from t_site_base where del_flag = ? and expire_date < ? and send_remind_flag < ? ";
//		List<SiteBase> sites = null;
//		try {
//			Date expDate = DateUtil.getGmtDate(new Date(new Date().getTime() + SiteConstant.BEFORE_SITE_EXP_REMIND_DATES*3600*24*1000l));
//			sites = libernate.getEntityListBase(SiteBase.class, sql, new Object[]{
//				ConstantUtil.DELFLAG_UNDELETE, expDate, SiteConstant.SEND_SITE_EXP_REMIND});
//			 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sites;
//	}


	@Override
	public List<SiteBase> getExpireRemindSites() {
		StringBuffer sqlBuffer = new StringBuffer();//"select * from t_site_base where del_flag = ? and send_remind_flag < ?  ";// and expire_date < ?
		List<SiteBase> sites = null;
		try {
			List<Object> valueList=new ArrayList<Object>(); 
			Date nowGmtDate = DateUtil.getGmtDate(null);//new Date(new Date().getTime() - SiteConstant.BEFORE_SITE_EXP_REMIND_DATES*3600*24*1000l));
			sqlBuffer.append(" select * from t_site_base where del_flag = ? and send_remind_flag < ?  ");
			valueList.add(ConstantUtil.DELFLAG_UNDELETE);
			valueList.add(SiteConstant.SEND_SITE_EXP_REMIND);
			Date eachExpStartDate=null;
			Date eachExpEndDate=null;
//			sqlBuffer.append(" and (");
//			int ii=0;
//			for(Integer[] eachRemindDays:SiteConstant.SITE_EXPIRE_DAYS){
//				if(ii > 0){
//					sqlBuffer.append(" or ");
//				}
//				eachExpStartDate=DateUtil.addDateMinutes(nowGmtDate, eachRemindDays[1]* 24 * 60);
//				eachExpEndDate=DateUtil.addDateMinutes(nowGmtDate, eachRemindDays[0]* 24 * 60);
//				sqlBuffer.append(" (");
//				sqlBuffer.append(" expire_date >? and expire_date<=?");
//				valueList.add(eachExpStartDate);
//				valueList.add(eachExpEndDate);
//				sqlBuffer.append(")");
//				ii++;
//			}
//			sqlBuffer.append(" )");
			sites = libernate.getEntityListBase(SiteBase.class, sqlBuffer.toString(), valueList.toArray());
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sites;
	}


	@Override
	public boolean sendSiteRmindEmail(SiteBase site) {
		boolean flag = false;
		try{
			EmailConfig sysConfig = emailLogic.getSysEmailConfig(site.getId());
			
			UserBase admin = emailLogic.getSiteSupperMasterBySiteId(site.getId());
			//获取邮件内容.
			
			
			
			
		
		Map<String, Object> datas = new HashMap<String,Object>();
			datas.put("user",admin);
//			datas.put("exp_date", SiteConstant.BEFORE_SITE_EXP_REMIND_DATES);
			datas.put("exp_date", site.getExpireDateNumber());
			String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.SITE_EXPIRED_REMIND, datas);
			logger.info("the email content:"+emailContent);
			SendMail mailInfo=new SendMail();
			mailInfo.setFromEmail(sysConfig.getEmailSender());
			mailInfo.setFromName(sysConfig.getEmailName());
			mailInfo.setServerHost(sysConfig.getEmailHost());
			mailInfo.setServerPort("25");
			mailInfo.setUserName(sysConfig.getEmailSender());
			mailInfo.setUserPass(sysConfig.getEmailPassword());
			mailInfo.setSubject("企业站点即将过期/Enterprise will expired");
			mailInfo.setContentType("html");
			mailInfo.setContent(emailContent);
			mailInfo.setValidate(true);
			mailInfo.setToEmail(admin.getUserEmail());
			EmailUtil.send(mailInfo);
			
			site.setSendRemindFlag(site.getSendRemindFlag()+SiteConstant.SEND_SITE_EXP_REMIND);
			libernate.updateEntity(site, "sendRemindFlag");
			flag = true;
			
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
		
	}

	
	public boolean sendEmailForSiteRemind(SiteBase siteBase) throws Exception{
		if(siteBase!=null){
			Date expireDate=siteBase.getExpireDate();
			if(expireDate==null){
				expireDate=DateUtil.getGmtDate(null);
			}
			Integer sendFlag=siteBase.getSendRemindFlag();
			Date nowGmtDate=DateUtil.getGmtDate(null);
			Date localDate=DateUtil.getOffsetDateByGmtDate(nowGmtDate, (long)siteBase.getTimeZone());
			Date localExpireDate=DateUtil.getOffsetDateByGmtDate(expireDate, (long)siteBase.getTimeZone());
			int diffDays=DateUtil.getDateDiff(localDate,localExpireDate);
			
//			System.out.println("site task for : siteId=" + siteBase.getId() + " , siteName=" + siteBase.getSiteName()+ ",diffDays= "+diffDays);
			int ii=0;
			int thisFlag=0;
			for(Integer reminDay:SiteConstant.SITE_REMIND_DAYS){
				ii++;
				thisFlag=Integer.valueOf(Math.round(Math.pow(2d,(ii-1)*1d))+"");
				System.out.println("site task for : siteId=" + siteBase.getId() + " , siteName=" + siteBase.getSiteName()+ ",thisFlag= "+thisFlag+";sendFlag="+sendFlag+ ",diffDays= "+diffDays + ";(thisFlag&sendFlag)="+(thisFlag&sendFlag));
				if(reminDay.intValue()==diffDays && sendFlag < thisFlag && (thisFlag&sendFlag) == 0 ){//?????????
					EmailConfig sysConfig = emailLogic.getSysEmailConfig(siteBase.getId());
					UserBase admin = emailLogic.getSiteSupperMasterBySiteId(siteBase.getId());
					Map<String, Object> datas = new HashMap<String,Object>();
					siteBase.setEffeDate(DateUtil.getOffsetDateByGmtDate(siteBase.getEffeDate(),(long)siteBase.getTimeZone()));
					
					datas.put("user",admin);
					datas.put("siteBase",siteBase);
					datas.put("timezone",siteBase.getTimeZoneDesc());
					
//					datas.put("exp_date", SiteConstant.BEFORE_SITE_EXP_REMIND_DATES);
					datas.put("exp_date", siteBase.getExpireDateNumber());
					String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.SITE_EXPIRED_REMIND, datas);
					logger.info("the email content:"+emailContent);
					EmailInfo emailInfo=new EmailInfo();
					emailInfo.setSiteId(siteBase.getId());
					emailInfo.setServerHost(sysConfig.getEmailHost());
					emailInfo.setServerPort("25");
					emailInfo.setFromEmail(sysConfig.getEmailSender());
					emailInfo.setFromName(sysConfig.getEmailName());
					emailInfo.setUserName(sysConfig.getEmailSender());
					emailInfo.setUserPass(sysConfig.getEmailPassword());
					emailInfo.setEmailSubject("企业站点即将过期/Enterprise will expired");
					emailInfo.setContentType("html");
					emailInfo.setValidate(true);
					emailInfo.setEmailContent(emailContent);
					emailInfo.setEmailAccepter(admin.getUserEmail());
					//libernate.saveEntity(emailInfo);
					
					//发送给站点创建者
					SystemUser systemUser=userService.getSystemUserById(siteBase.getCreateUser());
					systemUser.setEmail("frank_song@bizconf.cn");
					emailInfo.setEmailAccepter(systemUser.getEmail());
					libernate.saveEntity(emailInfo);
					
//					SendMail mailInfo=new SendMail();
//					mailInfo.setFromEmail(sysConfig.getEmailSender());
//					mailInfo.setFromName(sysConfig.getEmailName());
//					mailInfo.setServerHost(sysConfig.getEmailHost());
//					mailInfo.setServerPort("25");
//					mailInfo.setUserName(sysConfig.getEmailSender());
//					mailInfo.setUserPass(sysConfig.getEmailPassword());
//					mailInfo.setSubject("企业站点即将过期/Enterprise will expired");
//					mailInfo.setContentType("html");
//					mailInfo.setContent(emailContent);
//					mailInfo.setValidate(true);
//					mailInfo.setToEmail(admin.getUserEmail());
//					EmailUtil.send(mailInfo);
		
					
					siteBase.setSendRemindFlag(Integer.valueOf(Math.round(Math.pow(2d,ii*1d)-1)+""));
					libernate.updateEntity(siteBase, "sendRemindFlag");
					return true;
				}
			}
		
			
		//	siteBase.setSendRemindFlag(siteBase.getSendRemindFlag()+SiteConstant.SEND_SITE_EXP_REMIND);
		//	libernate.updateEntity(siteBase, "sendRemindFlag");
			
			return false;
			
			
			
		}
		return false;
	}

	@Override
	public List<SiteBase> getExpiredSites() {
		String sql = "select * from t_site_base where del_flag = ? and expire_date < ? and send_remind_flag < ?";
		List<SiteBase> sites = null;
		try {
			//站点已经过期
			Date expDate = DateUtil.getGmtDate(null);
			sites = libernate.getEntityListBase(SiteBase.class, sql, new Object[]{
				ConstantUtil.DELFLAG_UNDELETE, expDate,  SiteConstant.SEND_SITE_EXP});
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sites;
	}


	@Override
	public boolean sendSiteExpiredEmail(SiteBase site) {
		boolean flag = false;
		try{
			EmailConfig sysConfig = emailLogic.getSysEmailConfig(site.getId());
			//获取邮件内容
			UserBase admin = emailLogic.getSiteSupperMasterBySiteId(site.getId());
			Map<String, Object> datas = new HashMap<String,Object>();
			datas.put("user",admin);
			
			String emailContent  = EmailContentGenerator.getInstance().genContent(EmailConstant.SITE_EXPIRED, datas);
			logger.info("the email content:"+emailContent);
			SendMail mailInfo=new SendMail();
			mailInfo.setFromEmail(sysConfig.getEmailSender());
			mailInfo.setFromName(sysConfig.getEmailName());
			mailInfo.setServerHost(sysConfig.getEmailHost());
			mailInfo.setServerPort("25");
			mailInfo.setUserName(sysConfig.getEmailSender());
			mailInfo.setUserPass(sysConfig.getEmailPassword());
			mailInfo.setSubject("企业站点已经过期/Enterprise expired");
			mailInfo.setContentType("html");
			mailInfo.setContent(emailContent);
			mailInfo.setValidate(true);
			mailInfo.setToEmail(admin.getUserEmail());
			EmailUtil.send(mailInfo);
			
			site.setSendRemindFlag(site.getSendRemindFlag()+SiteConstant.SEND_SITE_EXP);
			libernate.updateEntity(site, "sendRemindFlag");
			flag = true;
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	
}


