package com.bizconf.audio.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.entity.EventLog;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 企业管理员操作日志
 * @author wangyong
 * 2013/2/25
 */
@ReqPath("siteAdminLogs")
@Interceptors({SiteAdminInterceptor.class})
public class SiteAdminEventLogController extends BaseController {
private final Logger logger = Logger.getLogger(SiteController.class);
	
	@Autowired
	SiteService siteService;
	@Autowired
	UserService userService;
	@Autowired
	EventLogService eventLogService;
	
	/**
	 * 企业管理员操作日志列表
	 * @param operator 操作员(可模糊查询)
	 * wangyong
	 * 2013-2-25
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "list")
	public Object eventLogList(@CParam("operator") String operator, PageModel pageModel, HttpServletRequest request){
		List<EventLog> logList = null;
		int rows = 0;
		String siteSign = "--";
		List<String> operatorList = new ArrayList<String>();      //操作员用户名列表，前台页面展示
		List<String> operatorObjectList = new ArrayList<String>();      //操作对象列表，前台页面展示
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		Integer logType = IntegerUtil.parseInteger(request.getParameter("logType"));
		if(!StringUtil.isNotBlank(operator)){
			operator = "";
		}
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		if(currentSite != null){
			siteSign = currentSite.getSiteSign();
		}
		if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
			rows = eventLogService.countSiteLogBySiteId(currentSiteAdmin.getSiteId(), operator, logType);
		}else{
			rows = eventLogService.countSiteLogBySiteId(currentSiteAdmin.getSiteId(), logType, currentSiteAdmin.getId());
		}
		logger.info(rows);
		pageModel.setRowsCount(rows);
		if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
			logList = eventLogService.getSiteLogListBySiteId(currentSiteAdmin.getSiteId(), operator, logType, sortField, sortord, pageModel);
		}else{
			logList = eventLogService.getSiteLogListBySiteId(currentSiteAdmin.getSiteId(), logType, sortField, sortord, pageModel, currentSiteAdmin.getId());
		}
		int logSize = 0;
		if(logList != null){
			logSize = logList.size();
		}
		Integer[] operatorIds = null;
		Integer[] operatorObjectIds = null;
		if(logSize > 0){
			operatorIds = new Integer[logSize];
			operatorObjectIds = new Integer[logSize];
			int i = 0;
			for(EventLog log:logList){
				operatorIds[i] = log.getCreateUser();
				operatorObjectIds[i] = log.getUserId();
				i++;
			}
		}
		if(operatorIds != null){
			operatorList = getNameListByid(operatorIds);
		}
		if(operatorObjectIds != null){
			operatorObjectList = getNameListByid(operatorObjectIds);
		}
		//需对会议列表时间展示时区设置，优先级最高的是会议表中的时区(查询某个会议信息时)，然后是站点表中的时区(查询站点下会议列表时)，最后是gmt时区
		String[] fields = new String[]{
			"createTime"
		};
		long offset = getSiteOffset(request);    //根据当前访问的站点标识获取站点所在时区
		logList = ObjectUtil.offsetDateWithList(logList, offset, fields);
		request.setAttribute("logList", logList);
		request.setAttribute("logType", logType);
		request.setAttribute("siteSign", siteSign);
		request.setAttribute("operatorList", operatorList);    
		request.setAttribute("operatorObjectList", operatorObjectList);    
		request.setAttribute("operator", operator);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortField);   //传排序字段的编号
		request.setAttribute("sortord", sortord);       //传排序方式的编号
		return new ActionForward.Forward("/jsp/admin/site_eventlog_list.jsp");
	}
	
	/**
	 * 用户表中通过id获取trueName
	 * wangyong
	 * 2013-2-26
	 */
	private List<String> getNameListByid(Integer[] userIds){
		List<String> nameList = new ArrayList<String>();
		if(userIds != null){
			List<UserBase> userList = userService.getUserListByUserIdArray(userIds);
			if(userList != null && userList.size() > 0){
				Map<Integer, String> userMap = new HashMap<Integer, String>();
				for(UserBase user:userList){
					userMap.put(user.getId(), user.getTrueName());
				}
				for(Integer userId:userIds){
					String name = userMap.get(userId);
					if(StringUtil.isNotBlank(name)){
						nameList.add(name);
					}else{
						nameList.add("--");
					}
				}
			}else{
				int userIdSize = 0;
				if(userIds != null){
					userIdSize = userIds.length;
				}
				for(int i=0; i<userIdSize; i++){
					nameList.add("--");
				}
			}
		}
		return nameList;
	}
	
	/**
	 * 根据当前访问的站点标识获取站点所在时区
	 * wangyong
	 * 2013-2-21
	 */
	private long getSiteOffset(HttpServletRequest request){
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		long offset = 0 ;
		if(currentSite != null){
			offset = currentSite.getTimeZone();
		}else{
			offset = DateUtil.getDateOffset();
		}
		logger.info("当前访问的站点时区" + offset);
		return offset;
	}
	
}
