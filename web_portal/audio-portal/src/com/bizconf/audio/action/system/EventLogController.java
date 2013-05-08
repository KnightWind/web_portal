package com.bizconf.audio.action.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.entity.EventLog;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SystemUserInterceptor;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath("logs")
@Interceptors({SystemUserInterceptor.class })
public class EventLogController {
	
	@Autowired
	EventLogService eventLogService;

	@Autowired
	SiteService siteService;
	
	@Autowired
	UserService userService;

	/**
	 * 取系统管理员操作日志
	 * 
	 * @param pageModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "syslist")
	public Object showSysList(HttpServletRequest request) {
		SystemUser  currentSysUser = userService.getCurrentSysAdmin(request);
		Integer logType=IntegerUtil.parseInteger(request.getParameter("logType"));
		String pageNo=String.valueOf(request.getParameter("pageNo"));
		String sortField=String.valueOf(request.getParameter("sortField"));
		String userName=String.valueOf(request.getParameter("userName"));    //页面对操作员的模糊查询
		if(userName==null || "null".equals(userName.trim().toLowerCase())){
			userName="";		
		}
		String sortord=String.valueOf(request.getParameter("sortord"));
		List<EventLog> logList=null;
		PageModel pageModel=new PageModel();
		if(pageNo==null || "".equals(pageNo.trim()) || "null".equals(pageNo.trim().toLowerCase())){
			pageNo="1";
		}
		if(sortField==null || "".equals(sortField.trim()) || "null".equals(sortField.trim().toLowerCase())){
			sortField="";
		}
		pageModel.setPageNo(pageNo);
		int rowsCount = 0;
		if(currentSysUser.isSuperSystemAdmin()){
			rowsCount=eventLogService.countSystemLogByLogType(logType,userName);    //超级管理员对页面上的操作员模糊查询
		}else{
			rowsCount=eventLogService.countSystemLogByLogType(logType, currentSysUser.getId());     //普通管理员不对操作员模糊查询，所以传值null
		}
		pageModel.setRowsCount(rowsCount);
		if(currentSysUser.isSuperSystemAdmin()){
			logList=eventLogService.getSystemLogListByLogType(logType,userName,sortField,sortord, pageModel);  //超级管理员对页面上的操作员模糊查询
		}else{
			logList=eventLogService.getSystemLogListByLogType(logType,sortField,sortord, pageModel, currentSysUser.getId()); //普通管理员不对操作员模糊查询，所以传值null
		}
		Integer[] ids=null;
		HashMap<String, Integer> sysUserIdMap=new HashMap<String, Integer>();
		List<UserBase> operatorList=null;
		if(logList!=null && logList.size()>0){
			Integer[] operatorIds=new Integer[logList.size()];
			int i = 0;
			for(EventLog eventLog:logList){
				if(eventLog!=null){
					sysUserIdMap.put("uid_"+eventLog.getCreateUser(), eventLog.getCreateUser());
					if(eventLog.getUserId() != null && eventLog.getUserId().intValue() > 0){
						operatorIds[i] = eventLog.getUserId();
					}else{
						operatorIds[i] = 0;
					}
					i++;
				}
			}
			operatorList = userService.getUserListByUserIdArray(operatorIds);
		}
		if(sysUserIdMap!=null && sysUserIdMap.size()>0){
			ids=new Integer[sysUserIdMap.size()];
			int ii=0;
			for(String key:sysUserIdMap.keySet()){
				ids[ii]=sysUserIdMap.get(key);
				ii++;
			}
		}
		List<SystemUser> sysUserList=null;
		sysUserList=userService.getSysUserListByIds(ids);
		String[] fields = new String[1];
		fields[0] =  "createTime";
		long offset = DateUtil.getDateOffset();    //获取所在时区的Offset
		logList = ObjectUtil.offsetDateWithList(logList, offset, fields);
		Map<Integer, String> operatorObjectList = getOperatorObject(operatorList);   //获取每个会议的参会人个数
		request.setAttribute("operatorObjectList", operatorObjectList);
		request.setAttribute("sysUserList", sysUserList);
		request.setAttribute("userName", userName);
		request.setAttribute("logList", logList);
		request.setAttribute("logType", logType);
		request.setAttribute("sortField", sortField);
		request.setAttribute("sortord", sortord);
		request.setAttribute("pageModel", pageModel);
		
		return new ActionForward.Forward("/jsp/system/eventlog_list.jsp");
	}
	
	/**
	 * 取企业管理员的操作日志
	 * @param pageModel
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "siteloglist")
	public Object showAdminList(PageModel pageModel, HttpServletRequest request) {
		String sortField=request.getParameter("sortField");
		String sortord=request.getParameter("sortord");
		String siteSign=String.valueOf(request.getParameter("siteSign"));
		String userName=String.valueOf(request.getParameter("userName"));
		Integer logType=IntegerUtil.parseInteger(request.getParameter("logType"));
		SystemUser  currentSysUser = userService.getCurrentSysAdmin(request);
		if(userName==null || "".equals(userName.trim()) || "null".equals(userName.trim().toLowerCase())){
			userName="";
		}
		if(siteSign!=null && !"".equals(siteSign)){
			
		}
		List<EventLog> logList=null;
		int rowsCount = 0;
		if(currentSysUser.isSuperSystemAdmin()){
			rowsCount = eventLogService.countSiteAdminLog(logType, userName);
		}else{
			rowsCount = eventLogService.countSiteAdminLog(logType, currentSysUser.getId());
		}
		pageModel.setRowsCount(rowsCount);
		if(currentSysUser.isSuperSystemAdmin()){
			logList = eventLogService.getSiteAdminLogList(logType, userName, sortField, sortord, pageModel);
		}else{
			logList = eventLogService.getSiteAdminLogList(logType, sortField, sortord, pageModel, currentSysUser.getId());
		}
		List<SiteBase> siteList=null;
		List<UserBase> userList=null;
		List<UserBase> operatorList=null;
		if(logList != null && logList.size() >0){
			Integer[] siteAdminIds=new Integer[logList.size()];
			Integer[] siteIds=new Integer[logList.size()];
			Integer[] operatorIds=new Integer[logList.size()];
			int ii=0;
			for(EventLog evnetLog:logList){
				siteAdminIds[ii]=evnetLog.getCreateUser();
				siteIds[ii]=evnetLog.getSiteId();
				if(evnetLog.getUserId() != null && evnetLog.getUserId().intValue() > 0){
					operatorIds[ii] = evnetLog.getUserId();
				}else{
					operatorIds[ii] = 0;
				}
				ii++;
			}
			userList=userService.getUserListByUserIdArray(siteAdminIds);
			siteList=siteService.getAllSiteListBySiteIds(siteIds);
			operatorList = userService.getUserListByUserIdArray(operatorIds);
		}
		Map<Integer, String> operatorObjectList = getOperatorObject(operatorList);   //获取每个会议的参会人个数
		String[] fields = new String[1];
		fields[0] =  "createTime";
		long offset = DateUtil.getDateOffset();    //获取所在时区的Offset
		logList = ObjectUtil.offsetDateWithList(logList, offset, fields);
		request.setAttribute("operatorObjectList", operatorObjectList);
		request.setAttribute("logList", logList);
		request.setAttribute("userName", userName);
		request.setAttribute("userList", userList);
		request.setAttribute("siteList", siteList);
		request.setAttribute("logType", logType);
		request.setAttribute("sortField", sortField);
		request.setAttribute("sortord", sortord);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/system/site_eventlog_list.jsp");
	}
	
	/**
	 * 获取每个会议的参会人个数
	 * wangyong
	 * 2013-3-25
	 */
	private Map<Integer,String> getOperatorObject(List<UserBase> userList){
		Map<Integer,String> operatorObjectList = new HashMap<Integer, String>();
		if(userList != null && userList.size() > 0){
			for(UserBase user:userList){
				operatorObjectList.put(user.getId(), user.getTrueName());
			}
		}
		return operatorObjectList;
	}
}
