package com.bizconf.audio.action.user;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.ContactGroup;
import com.bizconf.audio.entity.Contacts;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ContactGroupService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 企业用户联系人群组controller
 * @author martin
 * 2013-03-01
 *
 */
@ReqPath("group")
public class GroupController extends BaseController{
	private final Logger logger = Logger.getLogger(GroupController.class);
	
	/**
	 * 获取企业联系人群组列表
	 * @param keyword
	 * @param pageNo
	 * @param request
	 * @return
	 */
	@Autowired
	private ContactGroupService groupService;
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	EventLogService eventLogService;
	
	
	@AsController(path = "list")
	public Object groupsList(@CParam("keyword") String keyword, @CParam("pageNo")Integer pageNo, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		
		PageBean<ContactGroup> page = groupService.getGroupPageModel(keyword,currUser.getSiteId(),currUser.getId(),pageNo,0);
		request.setAttribute("pageModel", page);
		request.setAttribute("keyword", keyword);
		if(page!=null && page.getDatas()!=null){
			Map<String, Integer> data = getCountDataMap(page.getDatas());
			String json = JSONObject.fromObject(data).toString();
			request.setAttribute("countJson", json);
		}
		return new ActionForward.Forward("/jsp/user/group_list.jsp");
		//return new ActionForward.Forward("/jsp/user/invite_group_list.jsp");
	}
	
	
	@AsController(path = "invitelist")
	public Object groupsInviteList(@CParam("keyword") String keyword, @CParam("pageNo")Integer pageNo, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		
		PageBean<ContactGroup> page = groupService.getGroupPageModel(keyword,currUser.getSiteId(),currUser.getId(),pageNo,0);
		request.setAttribute("pageModel", page);
		request.setAttribute("keyword", keyword);
		//return new ActionForward.Forward("/jsp/user/group_list.jsp");
		return new ActionForward.Forward("/jsp/user/invite_group_list.jsp");
	}
	/**
	 * 
	 * @param group
	 * @param request
	 * @return
	 */
	@AsController(path = "save")
	public Object saveOrModify(ContactGroup group, HttpServletRequest request){
		int id = group.getId();
		UserBase currUser = userService.getCurrentUser(request); 
		
		String successInfo = "";
		String errorInfo = "";
		if(id>0){//修改
			successInfo = "修改群组信息成功！";
			errorInfo = "修改群组信息失败！";
		}else{//添加
			successInfo = "创建群组成功！";
			errorInfo = "创建群组失败！";
			
			group.setSiteId(currUser.getSiteId());
			group.setCreateUser(currUser.getId());
			group.setCreateTime(DateUtil.getGmtDate(null));
			group.setUserId(currUser.getId());
			group.setDelFlag(ConstantUtil.DELFLAG_UNDELETE);
			group.setDelUser(0);
			try {
				group.setDelTime((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		boolean flag = groupService.saveOrUpdateGroup(group);
		if(flag){
			eventLogService.saveUserEventLog(currUser, 
					EventLogConstants.SITEUSER_CONF_GROUP_CREATE, successInfo,
					EventLogConstants.EVENTLOG_SECCEED, null, request);
			return StringUtil.returnJsonStr(ConstantUtil.GLOBAL_SUCCEED_FLAG,successInfo);
		}else{
			eventLogService.saveUserEventLog(currUser, 
					EventLogConstants.SITEUSER_CONF_GROUP_CREATE, errorInfo,
					EventLogConstants.EVENTLOG_FAIL, null, request);
			return StringUtil.returnJsonStr(ConstantUtil.GLOBAL_FAIL_FLAG,errorInfo);
		}
	}
	
	
	@AsController(path = "delGroup")
	public Object delGroup(@CParam("id") Integer id, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		if(groupService.deleteGroupById(id, currUser)){
			
		}else{
			
		}
		return new ActionForward.Forward("/user/group/list");
	}
	
	@AsController(path = "delContactsFormGroup")
	public Object delContactFromGroup(@CParam("id") Integer id,@CParam("group_id") Integer group_id, HttpServletRequest request){
		if(groupService.delContactFromGroup(id)){
		}else{
		}
		return new ActionForward.Forward("/user/group/showContacts?group_id="+group_id);
	}
	
	@AsController(path = "editGroup")
	public Object toEditGroup(@CParam("id") Integer id, HttpServletRequest request){
		ContactGroup group = groupService.getGroupById(id);
		request.setAttribute("group", group);
		return new ActionForward.Forward("/jsp/user/add_group.jsp");
	}
	
	/**
	 * 查看一个群组的列表
	 * @param id
	 * @param request
	 * @return
	 */
	@AsController(path = "showContacts")
	public Object showMyContacts(@CParam("viewOnly")String viewOnly,@CParam("group_id") Integer group_id,@CParam("pageNo") Integer pageNo, @CParam("keyword")String keyword, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		PageBean<Contacts> page = groupService.getContactsByGroup(currUser.getSiteId(), currUser.getId(), group_id, keyword, pageNo);
		
		ContactGroup group = groupService.getGroupById(group_id);
		request.setAttribute("group", group);
		
		request.setAttribute("pageModel", page);
		request.setAttribute("group_id", group_id);
		request.setAttribute("keyword", keyword);
		request.setAttribute("viewOnly", viewOnly);
		if(viewOnly!=null&&viewOnly.equals("1")){
			return new ActionForward.Forward("/jsp/user/view_group_contacts_list.jsp");
		}
		return new ActionForward.Forward("/jsp/user/group_contacts_list.jsp");
	}
	
	
	@AsController(path = "getContactsByGroups")
	public void getMyContacts(@CParam("ids") String group_ids,@CParam("pageNo") Integer pageNo, HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("pragma","no-cache");
		response.setHeader("cache-control","no-cache");
		
		PrintWriter out = null;
		String json = null;
		try {
			out = response.getWriter();
			UserBase currUser = userService.getCurrentUser(request);
			String[] ids = group_ids.split(",");
			List<Contacts> contacts = groupService.getContactsByGroups(currUser.getSiteId(), currUser.getId(), ids);
			if(contacts==null){
				contacts = new ArrayList<Contacts>();
			}
			json = JSONArray.fromObject(contacts).toString();
			logger.info("the json String  is :: "+json);
		} catch (Exception e) {
			json = ConstantUtil.ERROR_FLAG;
			e.printStackTrace();
		}finally{
			out.print(json);
			out.flush();
			out.close();
		}
	}	
	
	
	@AsController(path = "importContacts")
	public Object importContacts(@CParam("group_id") Integer group_id,@CParam("pageNo") Integer pageNo, @CParam("keyword")String keyword, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		PageBean<Contacts> page = groupService.getImorpContectsList(currUser.getSiteId(), currUser.getId(), group_id, keyword, pageNo);
		ContactGroup group = groupService.getGroupById(group_id);
		request.setAttribute("group", group);
		request.setAttribute("pageModel", page);
		request.setAttribute("group_id", group_id);
		request.setAttribute("keyword", keyword);
		return new ActionForward.Forward("/jsp/user/group_imports_list.jsp");
	}
	
	/**
	 * 导入联系人到群组 （暂时没用到）
	 * @param group_id
	 * @param request
	 * @return
	 */
	@AsController(path = "doImport")
	public Object importToGroup(@CParam("group_id") Integer group_id, HttpServletRequest request){
		String[] conIds = request.getParameterValues("id");
		boolean flag = groupService.addContactsToGroup(conIds, group_id);
		return new ActionForward.Forward("/user/group/showContacts?"+group_id);
	}
	
	/**
	 * 导入联系人到群组
	 * @param group_id 群组
	 * @param request
	 * @return
	 */
	@AsController(path = "doImportSyn")
	public Object importToGroupByAjax(@CParam("group_id") Integer group_id, HttpServletRequest request){
		String[] conIds = request.getParameterValues("id");
		String info = "导入联系人到群组失败！";
		Integer status = ConstantUtil.GLOBAL_FAIL_FLAG;
		boolean flag = groupService.addContactsToGroup(conIds, group_id);
		if(flag){
			status = ConstantUtil.GLOBAL_SUCCEED_FLAG;
			info = "已成功导入联系人到群组 ";
		}
		return StringUtil.returnJsonStr(status, info);
	}
	
	//返回信息
	private Map<String, Integer> getCountDataMap(List<ContactGroup> groups){
		Map<String, Integer> data = new HashMap<String, Integer>();
		for(ContactGroup group:groups){
			data.put(group.getId().toString(), groupService.getGroupMemberNum(group.getId()));
		}
		return data;
	}
}
