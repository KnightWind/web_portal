package com.bizconf.audio.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.Notice;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.NoticeService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
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
 * 公告controller
 * @author wangyong
 * @date 2013.2.22
 */
@ReqPath("notice")
@Interceptors({SiteAdminInterceptor.class})
public class NoticeController extends BaseController{
	private final  Logger logger = Logger.getLogger(NoticeController.class);
	
	@Autowired
	NoticeService noticeService;
	@Autowired
	UserService userService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	SiteService siteService;
	
	/**
	 * 获取全部站点公告信息列表
	 * wangyong
	 * 2013-2-22
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "list")
	//@CParam("input.filename") LiberCFile file
	public Object list(PageModel pageModel, HttpServletRequest request){
		List<Notice> noticeList = null;
		List<String> publishUserList = new ArrayList<String>();
		Integer rows = 0;
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		try {
			if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
				rows = noticeService.countNoticeBySiteId(currentSiteAdmin.getSiteId(), null);
			}else{
				rows = noticeService.countNoticeBySiteId(currentSiteAdmin.getSiteId(), currentSiteAdmin.getId());
			}
		} catch (Exception e) {
			logger.error("获取页数出错!"+e);
		}
		pageModel.setRowsCount(rows);
		try {
			if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
				noticeList = noticeService.getNoticeListBySiteId(currentSiteAdmin.getSiteId(), pageModel, null);
			}else{
				noticeList = noticeService.getNoticeListBySiteId(currentSiteAdmin.getSiteId(), pageModel, currentSiteAdmin.getId());
			}
			noticeList = ObjectUtil.parseHtmlWithList(noticeList, "title", "content");      //数据库的数据转为实际字符
		} catch (Exception e) {
			logger.error("获取全部站点公告信息列表出错!"+e);
		}
		int noticeSize = 0;
		if(noticeList != null){
			noticeSize = noticeList.size();
		}
		Integer[] createIds = null;
		if(noticeSize > 0){
			createIds = new Integer[noticeSize];
			int i = 0;
			for(Notice notice:noticeList){
				createIds[i] = notice.getCreateUser();
				i++;
			}
		}
		if(createIds != null){
			publishUserList = getNameListByid(createIds);
		}
		String[] fields = new String[2];
		fields[0] =  "startTime";
		fields[1] = "stopTime";
		long offset = getSiteOffset(request);    //根据当前访问的站点标识获取站点所在时区
		noticeList = ObjectUtil.offsetDateWithList(noticeList, offset, fields);
		request.setAttribute("noticeList", noticeList);
		request.setAttribute("publishUserList", publishUserList);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/admin/noticeList.jsp");
	}
	
	/**
	 * 预览公告
	 * wangyong
	 * 2013-2-22
	 */
	@AsController(path = "view/{id:([0-9]+)}")
	@Get
	public Object viewNotice(@CParam("id") Integer id,HttpServletRequest request) throws Exception{
		logger.info("id=="+id);
		Notice notice = new Notice();
		if (id>0) {
			notice = noticeService.getNoticeById(id);
			notice = (Notice) ObjectUtil.parseChar(notice, "title", "content");	//字符转义	
		} else {
			notice.setTitle(request.getParameter("title"));
			notice.setContent(request.getParameter("content"));
		}
		String[] fields = new String[2];
		fields[0] =  "startTime";
		fields[1] = "stopTime";
		long offset = getSiteOffset(request);    //根据当前访问的站点标识获取站点所在时区
		notice = (Notice) ObjectUtil.offsetDate(notice, offset, fields);
		request.setAttribute("notice", notice);
		return new ActionForward.Forward("/jsp/admin/viewNotice.jsp");
	}
	
	/**
	 * 站点管理员发布站点公告
	 * wangyong
	 * 2013-2-22
	 */
	@Post
	@AsController(path = "go")
	//@CParam("name") @Xss String name,CSRF
	public Object createNotice(@CParam("data") String data, HttpServletRequest request){
		Object[] noticeData = JsonUtil.parseObjectArrWithJsonString(data);
		Notice notice = (Notice) JsonUtil.parseObjectWithJsonString(noticeData[0].toString(), Notice.class);
		boolean creatFlag = false;
		if(notice != null){
			UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
			if(!StringUtil.isNotBlank(notice.getTitle()) || !StringUtil.isNotBlank(notice.getContent())){
//				if(!StringUtil.isNotBlank(notice.getTitle()) || !StringUtil.isNotBlank(notice.getContent()) || notice.getStopTime() == null){
				return ResourceHolder.getInstance().getResource("system.notice.contents.input");
			}else{
				try {
//					if(DateUtil.getGmtDate(notice.getStopTime()).compareTo(DateUtil.getGmtDate(null)) <= 0){
//						return returnJsonStr(ConstantUtil.CREATENOTICE_FAIL, ResourceHolder.getInstance().getResource("system.notice.date.errorTime"));
//					}
					notice = (Notice) ObjectUtil.parseHtml(notice, "title", "content");	//字符转义
					creatFlag = noticeService.publishNotice(currentSiteAdmin, notice.getTitle(), notice.getContent());
					if(creatFlag){
						eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_NOTICE_CREATE, 
								ResourceHolder.getInstance().getResource("system.notice.list.Create.1"), 
								EventLogConstants.EVENTLOG_SECCEED, notice, request);   //创建成功后写EventLog
						return returnJsonStr(ConstantUtil.CREATENOTICE_SUCCEED, ResourceHolder.getInstance().getResource("system.notice.list.Create.1"));
					}else{
						eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_NOTICE_CREATE, 
								ResourceHolder.getInstance().getResource("system.notice.list.Create.2"), 
								EventLogConstants.EVENTLOG_FAIL, notice, request);   //创建失败后写EventLog
						return returnJsonStr(ConstantUtil.CREATENOTICE_FAIL, ResourceHolder.getInstance().getResource("system.notice.list.Create.2"));
					}
				} catch (Exception e) {
					eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_NOTICE_CREATE, ResourceHolder.getInstance().getResource("system.notice.list.Create.2"), EventLogConstants.EVENTLOG_FAIL, notice, request);   //创建失败后写EventLog
					logger.error("站点管理员发布站点公告出错!"+e);
					return returnJsonStr(ConstantUtil.CREATENOTICE_FAIL, ResourceHolder.getInstance().getResource("system.notice.list.Create.2"));
				}
			}
		}
		return creatFlag;
	}
	
	/**
	 * 站点管理员发布站点公告
	 * wangyong
	 * 2013-2-22
	 */
	@AsController(path = "create")
	@Get
	public Object publishNotice(){
		return new ActionForward.Forward("/jsp/admin/createNotice.jsp");
	}
	
	/**
	 * 站点管理员修改站点公告
	 * wangyong
	 * 2013-2-22
	 */
	@Post
	@AsController(path = "update")
	public Object updateNoticeInfo(@CParam("data") String data, HttpServletRequest request){
		Object[] noticeData = JsonUtil.parseObjectArrWithJsonString(data);
		Notice notice = (Notice) JsonUtil.parseObjectWithJsonString(noticeData[0].toString(), Notice.class);
		boolean updateFlag = false;
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		if(notice != null){
			int noticeId = notice.getId();
			String title = notice.getTitle();
			String content = notice.getContent();
//			Date expireTime = notice.getStopTime();
//			if(DateUtil.getGmtDate(notice.getStopTime()).compareTo(DateUtil.getGmtDate(null)) <= 0){
//				return returnJsonStr(ConstantUtil.CREATENOTICE_FAIL, ResourceHolder.getInstance().getResource("system.notice.date.errorTime"));
//			}
			if(noticeId > 0 && StringUtil.isNotBlank(title) && StringUtil.isNotBlank(content)){
//				if(noticeId > 0 && StringUtil.isNotBlank(title) && StringUtil.isNotBlank(content) && expireTime != null){
				updateFlag = noticeService.updateNotice(noticeId, title, content);
			}
			if(updateFlag){
				eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_NOTICE_UPDATE,  ResourceHolder.getInstance().getResource("system.notice.list.Update.1"), EventLogConstants.EVENTLOG_SECCEED, notice, request);   //修改成功后写EventLog
				return returnJsonStr(ConstantUtil.CREATENOTICE_SUCCEED,  ResourceHolder.getInstance().getResource("system.notice.list.Update.1"));
			}else{
				eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_NOTICE_UPDATE,  ResourceHolder.getInstance().getResource("system.notice.list.Update.2"), EventLogConstants.EVENTLOG_FAIL, notice, request);   //修改失败后写EventLog
				return returnJsonStr(ConstantUtil.CREATENOTICE_FAIL, ResourceHolder.getInstance().getResource("system.notice.list.Update.2"));
			}
		}
		return updateFlag;
	}
	
	/**
	 * 站点管理员修改站点公告
	 * wangyong
	 * 2013-2-22
	 */
	@AsController(path = "update/{id:([0-9]+)}")
	@Get
	public Object updateNotice(@CParam("id") Integer id,HttpServletRequest request){
		logger.info("id=="+id);
		Notice notice = noticeService.getNoticeById(id);
		notice = (Notice) ObjectUtil.parseChar(notice, "title", "content");	//字符转义
		String[] fields = new String[2];
		fields[0] =  "startTime";
		fields[1] = "stopTime";
		long offset = getSiteOffset(request);   //根据当前访问的站点标识获取站点所在时区
		notice = (Notice) ObjectUtil.offsetDate(notice, offset, fields);
		request.setAttribute("notice", notice);
		return new ActionForward.Forward("/jsp/admin/createNotice.jsp");
	}
	
	/**
	 * 站点管理员删除站点公告
	 * wangyong
	 * 2013-2-22
	 */
	@AsController(path = "delete/{id:([0-9]+)}")
	public Object deleteNotice(@CParam("id") Integer id, HttpServletRequest request){
		logger.info("id=="+id);
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		boolean delFlag = noticeService.deleteNoticeById(id, currentSiteAdmin);
		int delStatus = ConstantUtil.DELSITE_SUCCEED;
		if(delFlag){
			//传入参数需修改
			eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_NOTICE_DELETE, ResourceHolder.getInstance().getResource("system.notice.delete." + delStatus), EventLogConstants.EVENTLOG_SECCEED, id, request);   //删除成功后写EventLog
			setInfoMessage(request, ResourceHolder.getInstance().getResource("system.notice.delete." + delStatus));
		}else{
			delStatus = ConstantUtil.DELSITE_FAIL;
			//传入参数需修改
			eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_NOTICE_DELETE, ResourceHolder.getInstance().getResource("system.notice.delete." + delStatus), EventLogConstants.EVENTLOG_FAIL, id, request);   //删除失败后写EventLog
			setErrMessage(request, ResourceHolder.getInstance().getResource("system.notice.delete." + delStatus));
		}
		return new ActionForward.Forward("/admin/notice/list");
	}
	
	/**
	 * 只是简单的预览公告 不存入数据库
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "preview")
	@Get
	public Object previewNotice(HttpServletRequest request) throws Exception{
		return new ActionForward.Forward("/jsp/admin/viewNotice.jsp");
	}	
	
	/**
	 * 根据当前访问的站点标识获取站点所在时区
	 * wangyong
	 * 2013-2-22
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
	
}
