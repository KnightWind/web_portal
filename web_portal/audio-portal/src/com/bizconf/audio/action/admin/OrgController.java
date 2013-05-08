package com.bizconf.audio.action.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteOrg;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.OrgService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.JsonUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Get;
import com.libernate.liberc.annotation.httpmethod.Post;

/**
 * 组织机构controller
 * @author wangyong
 * 2013.5.6
 */
@ReqPath("org")
@Interceptors({SiteAdminInterceptor.class})
public class OrgController extends BaseController{

	private final  Logger logger = Logger.getLogger(NoticeController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	SiteService siteService;
	@Autowired
	OrgService orgService;
	
	/**
	 * 查询站点下的组织机构
	 * wangyong
	 * 2013-5-6
	 */
	@AsController(path = "orgList")
	public Object orgList(@CParam("orgName") String orgName,@CParam("sortField") String sortField,@CParam("sortord") String sortord,@CParam("pageNo") String pageNo, HttpServletRequest request){
		UserBase userBase = userService.getCurrentSiteAdmin(request);
		PageBean<SiteOrg> pageModel = orgService.getSiteOrgList(userBase.getSiteId(), orgName, pageNo, sortField, sortord);
		//需对会议列表时间展示时区设置，优先级最高的是会议表中的时区(查询某个会议信息时)，然后是站点表中的时区(查询站点下会议列表时)，最后是gmt时区
		Map<Integer,Integer> participantSizeList = getParticipants(pageModel.getDatas());   //获取每个会议的参会人个数
		request.setAttribute("participantSizeList", participantSizeList);
		request.setAttribute("orgName", orgName);
		request.setAttribute("sortField", sortField);
		request.setAttribute("sortord", sortord);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/admin/site_org_list.jsp");
	}
	

	/**
	 * 获取未分配组织机构的用户列表
	 * wangyong
	 * 2013-5-7
	 */
	@AsController(path = "getOrgUserList/{id:([0-9]+)}")
	public Object getOrgUserList(@CParam("id") String id,@CParam("orgName") String orgName,@CParam("sortField") String sortField,@CParam("sortord") String sortord,@CParam("pageNo") String pageNo, HttpServletRequest request){
		UserBase userBase = userService.getCurrentSiteAdmin(request);
		PageBean<UserBase> pageModel = null;
		List<SiteOrg> orgList = orgService.getSiteOrgList(userBase.getSiteId(), orgName, pageNo, sortField, sortord).getDatas();
		if(userBase.isSuperSiteAdmin()){
			pageModel = orgService.getOrgUserList(orgName, sortField, sortord, userBase.getSiteId(), null, pageNo, orgList);
		}else{
			pageModel = orgService.getOrgUserList(orgName, sortField, sortord, userBase.getSiteId(), userBase.getId(), pageNo, orgList);
		}
		request.setAttribute("orgId", id);
		request.setAttribute("orgName", orgName);
		request.setAttribute("sortField", sortField);
		request.setAttribute("sortord", sortord);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/admin/arrange_org_user.jsp");
	}
	
	/**
	 * 为用户批量分配机构
	 * wangyong
	 * 2013-5-7
	 */
	@AsController(path = "updateOrgUserBatch")
	public Object updateOrgUserBatch(HttpServletRequest request){
		boolean updateFlag = false;
		UserBase currentUser = userService.getCurrentSiteAdmin(request);
		String orgId = request.getParameter("orgId");
		String[] ids = request.getParameter("id").split(",");
		if(ids != null && ids.length > 0){
			updateFlag = orgService.updateOrgUserBatch(orgId, ids, currentUser);
		}
		if(updateFlag){
			return returnJsonStr(EventLogConstants.EVENTLOG_SECCEED, "为用户批量分配机构成功");
//			eventLogService.saveUserEventLog(currentUser, 
//					EventLogConstants.SITEUSER_CONF_CONTACT_DELETE, "为用户批量分配机构成功",
//					EventLogConstants.EVENTLOG_SECCEED, null, request);
		}else{
			return returnJsonStr(EventLogConstants.EVENTLOG_FAIL, "为用户批量分配机构失败");
//			eventLogService.saveUserEventLog(currentUser, 
//					EventLogConstants.SITEUSER_CONF_CONTACT_DELETE, "为用户批量分配机构失败",
//					EventLogConstants.EVENTLOG_FAIL, null, request);
		}
	}
	
	/**
	 * 新增站点下的组织机构(跳转页面)
	 * wangyong
	 * 2013-5-6
	 */
	@AsController(path = "add")
	@Get
	public Object addOrg(){
		return new ActionForward.Forward("/jsp/admin/createOrg.jsp");
	}
	
	/**
	 * 新增站点下的组织机构
	 * wangyong
	 * 2013-5-6
	 */
	@AsController(path = "create")
	@Post
	public Object createOrg(SiteOrg siteOrg, HttpServletRequest request){
		if(siteOrg != null && StringUtil.isNotBlank(siteOrg.getOrgName())){
			UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
			siteOrg = (SiteOrg)ObjectUtil.parseHtml(siteOrg, "orgName", "orgDesc");
			JSONObject json = new JSONObject();
			JSONArray jsonArrOrg = new JSONArray();
			SiteOrg org = orgService.addSiteOrg(siteOrg, currentSiteAdmin);
			if(org != null && org.getId() > 0){
				eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ORG_CREATE, 
						"新增站点下的组织机构成功", 
						EventLogConstants.EVENTLOG_SECCEED, null, request);   //创建成功后写EventLog
				json.put("status", ConstantUtil.CREATESITE_SUCCEED);
				jsonArrOrg.add(JsonUtil.parseJsonWithFullFieldByObject(org));
				json.put("siteOrg", jsonArrOrg);
				return json.toString();
			}else{
				eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ORG_CREATE, 
						"新增站点下的组织机构失败", 
						EventLogConstants.EVENTLOG_FAIL, null, request);   //创建成功后写EventLog
				return returnJsonStr(EventLogConstants.EVENTLOG_FAIL, "新增站点下的组织机构失败");
			}
		}
		return new ActionForward.Forward("/admin/org/orgList");
	}
	
	/**
	 * 修改站点下的组织机构(跳转页面)
	 * wangyong
	 * 2013-5-6
	 */
	@AsController(path = "update/{id:([0-9]+)}")
	@Get
	public Object updateOrg(@CParam("id") Integer id,HttpServletRequest request){
		SiteOrg siteOrg = orgService.getSiteOrgById(id);
		siteOrg = (SiteOrg)ObjectUtil.parseChar(siteOrg, "orgName", "orgDesc");
		request.setAttribute("siteOrg", siteOrg);
		return new ActionForward.Forward("/jsp/admin/createOrg.jsp");
	}
	
	/**
	 * 修改站点下的组织机构
	 * wangyong
	 * 2013-5-6
	 */
	@AsController(path = "update")
	public Object updateOrgInfo(SiteOrg siteOrg, HttpServletRequest request){
		if(siteOrg != null && StringUtil.isNotBlank(siteOrg.getOrgName())){
			UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
			siteOrg = (SiteOrg)ObjectUtil.parseHtml(siteOrg, "orgName", "orgDesc");
			JSONObject json = new JSONObject();
			JSONArray jsonArrOrg = new JSONArray();
			SiteOrg org = orgService.updateSiteOrg(siteOrg);
			if(org != null && org.getId() > 0){
				eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ORG_UPDATE, 
						"新增站点下的组织机构成功", 
						EventLogConstants.EVENTLOG_SECCEED, null, request);   //创建成功后写EventLog
				json.put("status", ConstantUtil.CREATESITE_SUCCEED);
				jsonArrOrg.add(JsonUtil.parseJsonWithFullFieldByObject(org));
				json.put("siteOrg", jsonArrOrg);
				return json.toString();
			}else{
				eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ORG_UPDATE, 
						"新增站点下的组织机构失败", 
						EventLogConstants.EVENTLOG_FAIL, null, request);   //创建成功后写EventLog
				return returnJsonStr(EventLogConstants.EVENTLOG_FAIL, "修改站点下的组织机构失败");
			}
		}
		return new ActionForward.Forward("/admin/org/orgList");
	}
	
	/**
	 * 删除站点下的组织机构
	 * wangyong
	 * 2013-5-6 
	 */
	@AsController(path = "delete/{id:([0-9]+)}")
	public Object deleteNotice(@CParam("id") Integer id, HttpServletRequest request){
		logger.info("id=="+id);
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		if(orgService.deleteSiteOrg(id, currentSiteAdmin)){
			eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ORG_DELETE, 
					"删除站点下的组织机构成功", 
					EventLogConstants.EVENTLOG_SECCEED, null, request);   //创建成功后写EventLog
			setInfoMessage(request, "删除站点下的组织机构成功");
		}else{
			eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_ORG_DELETE, 
					"删除站点下的组织机构失败", 
					EventLogConstants.EVENTLOG_FAIL, null, request);   //创建成功后写EventLog
			setErrMessage(request, "删除站点下的组织机构失败");
		}
		return new ActionForward.Forward("/admin/org/orgList");
	}
	
	/**
	 * 预览站点下的组织机构
	 * wangyong
	 * 2013-5-6 
	 */
	@AsController(path = "view/{id:([0-9]+)}")
	@Get
	public Object viewSiteOrg(@CParam("id") Integer id,HttpServletRequest request){
		SiteOrg siteOrg = orgService.getSiteOrgById(id);
		siteOrg = (SiteOrg)ObjectUtil.parseChar(siteOrg, "orgName", "orgDesc");
		request.setAttribute("siteOrg", siteOrg);
		return new ActionForward.Forward("/jsp/admin/site_org_view.jsp");
	}
	
	/**
	 * 返回json字符串对象(创建，修改公告出错时)
	 * status 失败
	 * object 失败原因
	 * wangyong
	 * 2013-2-22
	 */
	private String returnJsonStr(int status, Object object){
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("message", object.toString());
		return json.toString();
	}
	
	/**
	 * 获取每个组织机构的人数
	 * wangyong
	 * 2013-3-25
	 */
	private Map<Integer,Integer> getParticipants(List<SiteOrg> orgList){
		Map<Integer,Integer> ParticipantsMap = new HashMap<Integer, Integer>();
		if(orgList != null && orgList.size() > 0){
			for(SiteOrg org:orgList){
				List<UserBase> Participants = orgService.getOrgUserList(org.getId(), org.getSiteId());
				ParticipantsMap.put(org.getId(), Participants == null ? 0 : Participants.size());
			}
		}
		return ParticipantsMap;
	}
	
}
