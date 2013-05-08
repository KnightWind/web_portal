package com.bizconf.audio.action.user;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.Contacts;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.logic.ContactLogic;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.service.ContactService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.ExcelUtil;
import com.bizconf.audio.util.JsonUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberCFile;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Post;
import com.libernate.liberc.utils.LiberContainer;

/**
 * 企业用户联系人controller
 * @author 
 * 2013/2/28
 *
 */
@ReqPath("contact")
@Interceptors({UserInterceptor.class})
public class ContactController extends BaseController{
	private final Logger logger = Logger.getLogger(ContactController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	SiteService siteService;
	@Autowired
	ContactService contactService;
	@Autowired
	ContactLogic contactLogic;
	@Autowired
	ConfService confService;
	@Autowired
	EmailService emailService;
	@Autowired
	ConfUserService confUserService;
	@Autowired
	ConfManagementService confManagementService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	ConfUserLogic confUserLogic;
	@Autowired
	EmpowerConfigService empowerConfigService;
	 
	
	@AsController(path = "list")
	public Object groupsList(@CParam("keyword") String keyword, @CParam("pageNo")Integer pageNo, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		PageBean<Contacts> page = contactService.getContactsPage(keyword,currUser.getSiteId(),currUser.getId(),pageNo,0);
		request.setAttribute("pageModel", page);
		request.setAttribute("keyword", keyword);
		return new ActionForward.Forward("/jsp/user/contacts_list.jsp");
	}
	
	@AsController(path = "invitelist")
	public Object inviteContactslist(@CParam("keyword") String keyword, @CParam("pageNo")Integer pageNo, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		PageBean<Contacts> page = contactService.getContactsPage(keyword,currUser.getSiteId(),currUser.getId(),pageNo,0);
		request.setAttribute("pageModel", page);
		request.setAttribute("keyword", keyword);
		return new ActionForward.Forward("/jsp/user/invite_contacts_list.jsp");
	}
	
	/**
	 * 进入联系人的信息编辑页
	 * 2013-2-28
	 */
	@AsController(path = "editContact")
	public Object toEditContact(@CParam("id") Integer contactId,HttpServletRequest request){
		Contacts contact = contactService.getContactById(contactId);
		contact = (Contacts) ObjectUtil.parseHtml(contact, "contactName","contactDesc");
		request.setAttribute("contact", contact);
		return new ActionForward.Forward("/jsp/user/add_contacts.jsp");
	}

	/**
	 * 新建或者修改联系人
	 * 2013-2-28
	 */
	@AsController(path = "saveSingle")
	public Object saveContactSingle(Contacts contact, HttpServletRequest request){
		int createFlag = ConstantUtil.CREATE_CONF_FAIL;
		String retInfo = "";
		Contacts contactInfo = null;
		UserBase currentUser = userService.getCurrentUser(request);
		contact = (Contacts) ObjectUtil.parseHtml(contact, "contactName","contactDesc");
		contact.setUserId(currentUser.getId());
		if(contact != null){
			if(contact.getId()!=null&&contact.getId()>0){
				contactInfo = contactService.updateContactInfo(contact, currentUser);
				retInfo += "修改";
			}else{
				contactInfo = contactService.createContactSingle(contact, currentUser);
				retInfo += "新建";
			}
			if(contactInfo != null && contactInfo.getId() != null && contactInfo.getId() > 0){
				createFlag = ConstantUtil.CREATE_CONF_SUCCEED;
				retInfo += "联系人成功";
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_CONTACT_CREATE, retInfo,
						EventLogConstants.EVENTLOG_SECCEED, contactInfo, request);   //创建成功后写EventLog
			}else{
				retInfo +=  "联系人失败";
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_CONTACT_CREATE, retInfo,
						EventLogConstants.EVENTLOG_FAIL, contactInfo, request);   //创建成功后写EventLog
			}
		}
		//return new ActionForward.Forward("/jsp/user/contactsList.jsp");
		return StringUtil.returnJsonStr(createFlag, retInfo);
	}
	
	/**
	 * 新建（修改）联系人时验证邮箱是否已存在
	 * wangyong
	 * 2013-3-11
	 */
	@Post
	@AsController(path = "contactEmailValidate")
	public Object contactEmailValidate(@CParam("email") String email, @CParam("userId") int userId,HttpServletRequest request){
		String flag = "true";
		UserBase currentUser = userService.getCurrentUser(request);
		if(StringUtil.isNotBlank(email)){
			Contacts contact = contactService.getContactByEmail(email,currentUser);
			if(contact != null && userId == 0){    //新建联系人
				logger.info("该email已存在!");
				flag = "false";
			}else if(contact != null && userId != 0 && contact.getId().intValue() != userId){    //修改联系人
				logger.info("该email已存在!");
				flag = "false";
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * 批量导入联系人
	 * @param ids  
	 * 2013-3-1
	 */
	@AsController(path = "importBatchByContacts")
	public Object importBatchByContacts(@CParam("excelfile") LiberCFile file,HttpServletRequest request){
		boolean importFlag = false;
		int statu = ConstantUtil.GLOBAL_FAIL_FLAG; 
		String retInfo = "";
		UserBase currentUser = userService.getCurrentUser(request);
		if(file!=null){
			try {
				List<Contacts> importusers = new ArrayList<Contacts>();		//可成功导入的用户
				List<Contacts> repeatusers = new ArrayList<Contacts>();		//邮箱已存在的用户
				List<Contacts> unSaveableusers = new ArrayList<Contacts>(); //数据格式不对或者不全的
				Set<String> userEmails = new HashSet<String>();
				List<Object[]> datas = ExcelUtil.getDataByInputStream(file.getInputStream(), file.getOriginalFilename(), 2, 0);
					//用于显示总共多少数据
				request.setAttribute("itemnum",datas.size());
				for (Iterator<Object[]> it = datas.iterator(); it.hasNext();) {
						Object[] objs = it.next();
						Contacts contact = new Contacts();
						for(int i=0;i<objs.length;i++){
							if(i==0){
								contact.setContactName(objs[0]==null?"":objs[0].toString());
							}
							if(i==1){
								contact.setContactNameEn(objs[1]==null?"":objs[1].toString());
							}
							if(i==2){
								contact.setContactEmail(objs[2]==null?"":objs[2].toString());
							}
							if(i==3){
								contact.setContactPhone(objs[3]==null?"":objs[3].toString());
							}
							if(i==4){
								contact.setContactMobile(objs[4]==null?"":objs[4].toString());
							}
						}
						contact.setUserId(currentUser.getId());
						contact.setSiteId(currentUser.getSiteId());
						contact.setCreateUser(currentUser.getId());
						contact.setCreateTime(DateUtil.getGmtDate(null));
						contact.setContactDesc("");
						
						//去除excel文件中重复的记录
						if(userEmails.contains(contact.getContactEmail()) || contactService.getContactByEmail(contact.getContactEmail(), currentUser)!=null){
							repeatusers.add(contact);
						}else if(!contactLogic.createContactSingleValidate(contact)){
							unSaveableusers.add(contact);
						}else{
							contact.initContact();
							importusers.add(contact);
						}
						userEmails.add(contact.getContactEmail());
				}
					
				request.setAttribute("statu", ConstantUtil.GLOBAL_FAIL_FLAG);
				//可以成功导入的
				if(importusers.size()>0){
					boolean flag = contactService.importContactBatch(importusers);//contactService.importBatchByExcel(importusers, currentUser);
					if(flag){
						request.setAttribute("statu", ConstantUtil.GLOBAL_SUCCEED_FLAG);
					}else{
						throw new RuntimeException("import contacts failed!");
					}
					request.setAttribute("imported", importusers);
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
				logger.error("从Excel文件批量导入联系人出错！" + e);
				
				request.setAttribute("statu", ConstantUtil.GLOBAL_FAIL_FLAG);
				request.setAttribute("info", "导入联系人失败！");
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_CONTACTBATCH_CREATE, "从Excel文件批量导入联系人出错！",
						EventLogConstants.EVENTLOG_FAIL, null, request);
			}
		}else{
			String[] ids = request.getParameterValues("id");
			//String[] saveableIds = 
			Map<String,Integer[]> idsdata = contactService.getCanSaveableIds(ids, currentUser.getId());
			Integer[] saveables = idsdata.get("saveable");
			Integer[] repeats = idsdata.get("unsaveable");
			String repeatInfo = "";
			if(repeats!=null && repeats.length>0){
				repeatInfo += "重复联系人信息有：";
				for (int i = 0; i < repeats.length; i++) {
					UserBase repeatUser = userService.getUserBaseById(repeats[i]);
					repeatInfo += repeatUser.getTrueName()+"、";
				}
			}
			if(saveables!=null && saveables.length>0){
				importFlag = contactService.importBatchByContacts(saveables, currentUser);
			}
			if(importFlag){
				statu = ConstantUtil.GLOBAL_SUCCEED_FLAG;
				retInfo = "导入联系人成功, "+repeatInfo;
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_CONTACTBATCH_CREATE, "从Excel文件批量导入联系人成功！",
						EventLogConstants.EVENTLOG_SECCEED, null, request);
			}else{
				retInfo = "导入联系人失败。 ";
				if(!repeatInfo.equals("")){
					retInfo += "联系人信息重复！";
				}
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_CONTACTBATCH_CREATE, retInfo,
						EventLogConstants.EVENTLOG_FAIL, null, request);
			}
			request.setAttribute("statu", statu);
			request.setAttribute("info", retInfo);
		}
		return new ActionForward.Forward("/jsp/user/importCallback.jsp");
	}
	
	
	/**
	 * 从Excel文件批量导入联系人(跳转页面)
	 * wangyong
	 * 2013-3-1
	 */
	@AsController(path = "importContactsByExcel")
	public Object importContactsByExcel(){
		return new ActionForward.Forward("/jsp/user/import.jsp");
	}
	
	/**
	 * 转到导入页面
	 * @return
	 */
	@AsController(path = "importPop")
	public Object importPop(){
		return new ActionForward.Forward("/jsp/user/contacts_import_main.jsp");
	}
	
	/**
	 * 下载联系人模板
	 * wangyong
	 * 2013-3-1
	 */
	@AsController(path = "downloadContactsTemplate")
	public void downloadContactsTemplate(HttpServletRequest request,HttpServletResponse response){
		String lang = getLang(request);
		String dirPath = LiberContainer.getContainer().getServletContext().getRealPath("excel_template")+File.separator;
		String tempPath = "";
		if(lang.startsWith(ConstantUtil.LANG_CN)){
			tempPath = dirPath+"template_contact_zh.xlsx";
		}else{
			tempPath = dirPath+"template_contact_en.xlsx";
		}
		File file = new File(tempPath);
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=template_contact.xlsx");
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
	 * 删除联系人
	 * wangyong
	 * 2013-3-1
	 */
	@AsController(path = "deleteContact")
	public Object deleteContact(@CParam("id") String id, HttpServletRequest request){
		boolean delFlag = false;
		//int delStatus = ConstantUtil.DELETE_CONF_FAIL; 
		UserBase currentUser = userService.getCurrentUser(request);
		if(StringUtil.isNotBlank(id)){
			delFlag = contactService.deleteContactInfo(Integer.parseInt(id), currentUser);
		}
		if(delFlag){
			//delStatus = ConstantUtil.DELETE_CONF_SUCCEED;
			setInfoMessage(request, "删除联系人成功");
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_CONTACT_DELETE, "删除联系人成功",
					EventLogConstants.EVENTLOG_SECCEED, null, request);
		}else{
			setErrMessage(request,  "删除联系人失败");
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_CONTACT_DELETE, "删除联系人失败",
					EventLogConstants.EVENTLOG_FAIL, null, request);
		}
		return new ActionForward.Forward("/user/contact/list");
	}
	
	/**
	 * 批量删除联系人
	 * wangyong
	 * 2013-5-2
	 */
	@AsController(path = "deleteContactBatch")
	public Object deleteContactBatch(HttpServletRequest request){
		boolean delFlag = false;
		UserBase currentUser = userService.getCurrentUser(request);
		String[] ids = request.getParameterValues("id");
		if(ids != null && ids.length > 0){
			delFlag = contactService.deleteContactInfoBatch(ids, currentUser);
		}
		if(delFlag){
			setInfoMessage(request, "批量删除联系人成功");
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_CONTACT_DELETE, "批量删除联系人成功",
					EventLogConstants.EVENTLOG_SECCEED, null, request);
		}else{
			setErrMessage(request,  "批量删除联系人失败");
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_CONTACT_DELETE, "批量删除联系人失败",
					EventLogConstants.EVENTLOG_FAIL, null, request);
		}
		return new ActionForward.Forward("/user/contact/list");
	}
	
	
	/**
	 * 查询企业联系人
	 * @param request
	 * @return
	 */
	@AsController(path = "showEnterpriseContacts")
	public Object showEnterpriseContacts(@CParam("keyword") String keyword,@CParam("showAll")String showAll,@CParam("pageNo") Integer pageNo,HttpServletRequest request){
		UserBase currentUser = userService.getCurrentUser(request);
		UserBase creator = userService.getUserBaseById(currentUser.getCreateUser());
		boolean showAllFlag = true;
		boolean isSupper = true;// 默认全部显示
		//如果找不到登录用户的创建者。认为该用户为管理员
		if(currentUser.isSiteAdmin()||creator==null || creator.isSuperSiteAdmin()){
			isSupper = true;
		}
		if(showAll!=null&&showAll.trim().equals("1")){
			showAllFlag =false;
		}
		PageBean<UserBase> page = contactService.getEnterpriseContacts(keyword, pageNo, isSupper, showAllFlag,currentUser);
 		request.setAttribute("pageModel", page);
 		request.setAttribute("showAll", showAll);
 		request.setAttribute("keyword", keyword);
//		return new ActionForward.Forward("/jsp/user/enContacts_list.jsp");
		return new ActionForward.Forward("/jsp/user/invite_enContacts_list.jsp");
	}
	
	/**
	 * 进入邀请界面
	 * @param confId
	 * @param request
	 * @return
	 */
	@AsController(path = "goInviteContacts")
	public Object goInviteContacts(@CParam("confId") String confId,HttpServletRequest request){
		request.setAttribute("confId", confId); 
		
		UserBase user = userService.getCurrentUser(request);
		SiteBase site = siteService.getSiteBaseById(user.getSiteId());
		EmpowerConfig siteConfig = empowerConfigService.getSiteApplyEmpowerBySiteId(site.getId());
		EmpowerConfig config = empowerConfigService.getUserEmpowerConfigByUserId(user.getId());
		request.setAttribute("config", config);
		request.setAttribute("siteConfig", siteConfig);
		
		return new ActionForward.Forward("/jsp/user/inviteFirst.jsp");
	}
	
	/**
	 * 邀请选择主页面
	 * @param request
	 * @return
	 */
	@AsController(path = "goContactsSelect")
	public Object goContactsSelect(HttpServletRequest request){
		
		return new ActionForward.Forward("/jsp/user/inviteContactsSelect.jsp");
	}
	
	/**
	 * 进入联系人管理页面
	 * @param request
	 * @return
	 */
	@AsController(path = "goContacts")
	public Object goContacts(HttpServletRequest request){
		return new ActionForward.Forward("/jsp/user/contacts_main.jsp");
	}
	
	/**
	 * 邀请联系人参会
	 * @param data
	 * @return
	 */
	@AsController(path = "sendinvites")
	public Object inviteContacts(@CParam("data") String data,HttpServletRequest request){
		UserBase currUser =userService.getCurrentUser(request);
		SiteBase currSite = siteService.getSiteBaseById(currUser.getSiteId());
		Integer status = ConstantUtil.GLOBAL_SUCCEED_FLAG;
		String msg = "邀请参会者成功！";
		List<ConfUser> users = new ArrayList<ConfUser>();
		List<UserBase> ubs = new ArrayList<UserBase>();
		try{
			JSONObject jObject = JSONObject.fromObject(data);
			Integer confId = Integer.parseInt(jObject.get("confId").toString());
			ConfBase confInfo = confService.getConfBasebyConfId(confId);
			JSONArray jsonArray = jObject.getJSONArray("users");
			
			List<ConfUser> invertedUsers = confUserLogic.getConfUserList(confId);
			for (int i = 0; i < jsonArray.size(); i++) {
				ConfUser cu = (ConfUser) JsonUtil.parseObjectWithJsonString(jsonArray.get(i).toString(), ConfUser.class);
				users.add(cu);
				UserBase ub = new UserBase();
				ub.setId(0);
				if(cu.getUserId()!=null && cu.getUserId()>0){
					ub.setId(cu.getUserId());
				}
				ub.setTrueName(cu.getUserName());
				ub.setUserEmail(cu.getUserEmail());
				ub.setPhone(cu.getTelephone());
				ubs.add(ub);
			}
//			invertedUsers.addAll(users);
//			String retInfo = confManagementService.updateConf(confInfo, invertedUsers, currSite, currUser);
//			if(retInfo.equals(ConstantUtil.AS_MORE_THAN_MAXNUM)){
//				return StringUtil.returnJsonStr(ConstantUtil.GLOBAL_FAIL_FLAG, "到达会议人数上限");
//			}else if(ConstantUtil.AS_BEGIN_TIME_INVALID.equals(retInfo)){
//				return StringUtil.returnJsonStr(ConstantUtil.GLOBAL_FAIL_FLAG, "会议已开始,请从会议客户端邀请参会人");
//			}else if(!retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
//				throw new RuntimeException("invoking the soap invite interface error errorcode:"+retInfo);
//			}
			
			boolean flag = confUserService.fillConfUserForInvite(confInfo, ubs);
			if(flag){
				flag = emailService.sendInviteToConfUser(users, confInfo);
			}
			if(!flag){
				status =ConstantUtil.GLOBAL_FAIL_FLAG;
				msg = "邀请参会人失败！";
			}
		}catch (Exception e) {
			e.printStackTrace();
			status =ConstantUtil.GLOBAL_FAIL_FLAG;
			msg = "邀请参会人失败！";
		}
		return StringUtil.returnJsonStr(status, msg);
	}
}
