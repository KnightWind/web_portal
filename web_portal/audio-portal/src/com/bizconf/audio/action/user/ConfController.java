package com.bizconf.audio.action.user;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfCycle;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.ContactGroup;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.logic.ConfLogic;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.service.ContactGroupService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.LicService;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.PublicConfService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.JsonUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Get;
import com.libernate.liberc.annotation.httpmethod.Post;

/**
 * 企业用户会议controller
 * @author wangyong
 * 2013/3/5
 */
@ReqPath("conf")
public class ConfController extends BaseController {
	private final Logger logger = Logger.getLogger(ConfController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	ConfService confService;
	@Autowired
	SiteService siteService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	private ContactGroupService groupService;
	@Autowired
	ConfLogic confLogic;
	@Autowired
	EmailService emailService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	PublicConfService publicConfService;
	@Autowired
	ConfUserService confUserService;
	@Autowired
	EmpowerConfigService empowerConfigService;
	@Autowired
	LicService licService;
	
	/**
	 * 查询站点下的所有公开会议
	 * 2013-3-5
	 */
	@AsController(path = "listPubConf")
	public Object listPubConf(PageModel pageModel, HttpServletRequest request){
		
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		
		List<ConfBase> dringConfList = publicConfService.getOnGoingConf(currentSite.getId());
		List<ConfBase> upcomingConfList = publicConfService.getUpComingConf(currentSite.getId());
		//keep as empty
		List<ConfBase> missConfList = new ArrayList<ConfBase>();
		//keep as empty
		List<ConfBase> attendedConfList = new ArrayList<ConfBase>();
		
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(dringConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(upcomingConfList));
		
		//当前站点时区的时间
		Date curDate=currentSite.getSiteLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("cycs", cycs);
		request.setAttribute("dringConfList", dringConfList);
		request.setAttribute("dringConfRows", publicConfService.countOnGoingConf(currentSite.getId()));
		request.setAttribute("upcomingConfList", upcomingConfList);
		request.setAttribute("UpcomingConfRows", publicConfService.countUpComingConf(currentSite.getId()));
		request.setAttribute("missConfList", missConfList);
		request.setAttribute("attendedConfList", attendedConfList);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/user/conf_list_index.jsp");
	}
	
	/**
	 * 查询与自己有关的会议，包括4种状态：
	 * 1.与自己有关的正在进行中的会议
	 * 2.与自己有关的即将开始的会议
	 * 3.自己错过的会议
	 * 4.自己参加过的的会议
	 * wangyong
	 * 2013-3-8
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "listConf")
	public Object listConf(PageModel pageModel, HttpServletRequest request){
		if (!loginService.isLogined(request)) {
			return new ActionForward.Redirect("/user/conf/listPubConf");
		}
		List<ConfBase> dringConfList = new ArrayList<ConfBase>();
		List<ConfBase> upcomingConfList = new ArrayList<ConfBase>();
		List<ConfBase> missConfList = new ArrayList<ConfBase>();
		List<ConfBase> attendedConfList = new ArrayList<ConfBase>();
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		Integer rows = 0;
		rows = confService.countDuringConfList(null, currentUser);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows.intValue());
			request.setAttribute("dringConfRows", rows.intValue());
			pageModel.setPageSize(ConfConstant.CONF_LIST_DEFALT_ROWS);
			dringConfList = confService.listDuringConfList(null, pageModel, null, null, currentUser, currentSite);
			dringConfList = ObjectUtil.parseHtmlWithList(dringConfList, "confName");     //数据库的数据转为实际字符
		}
		rows = confService.countUpcomingConfList(null, currentUser, ConfConstant.CONF_LIST_DASHBOARD_DAYS);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows.intValue());
			request.setAttribute("UpcomingConfRows", rows.intValue());
			pageModel.setPageSize(ConfConstant.CONF_LIST_DEFALT_ROWS);
			upcomingConfList = confService.listUpcomingConfList(null, pageModel, null, null, currentUser, currentSite, ConfConstant.CONF_LIST_DASHBOARD_DAYS);
			upcomingConfList = ObjectUtil.parseHtmlWithList(upcomingConfList, "confName");     //数据库的数据转为实际字符
		}
		rows = confService.countMissConfList(null, currentUser, ConfConstant.CONF_LIST_DASHBOARD_DAYS, ConfConstant.CONF_HIDE_FLAG_FALSE);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows.intValue());
			request.setAttribute("MissConfRows", rows.intValue());
			pageModel.setPageSize(ConfConstant.CONF_LIST_DEFALT_ROWS);
			missConfList = confService.listMissConfList(null, pageModel, null, null, currentUser, currentSite, ConfConstant.CONF_LIST_DASHBOARD_DAYS, ConfConstant.CONF_HIDE_FLAG_FALSE);
			missConfList = ObjectUtil.parseHtmlWithList(missConfList, "confName");     //数据库的数据转为实际字符
		}
		rows = confService.countAttendedConfList(null, currentUser, ConfConstant.CONF_LIST_DASHBOARD_DAYS);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows.intValue());
			request.setAttribute("AttendedConfRows", rows.intValue());
			pageModel.setPageSize(ConfConstant.CONF_LIST_DEFALT_ROWS);
			attendedConfList = confService.listAttendedConfList(null, pageModel, null, null, currentUser, currentSite, ConfConstant.CONF_LIST_DASHBOARD_DAYS);
			attendedConfList = ObjectUtil.parseHtmlWithList(attendedConfList, "confName");     //数据库的数据转为实际字符
		}
		//获取会议周期
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(dringConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(upcomingConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(missConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(attendedConfList));
		request.setAttribute("cycs", cycs);
		//当前用户喜好设置时区的时间
		Date curDate=currentUser.getUserLocalTime();
//		Date curDate=currentSite.getSiteLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("user", currentUser); 
		request.setAttribute("dringConfList", dringConfList);
		request.setAttribute("upcomingConfList", upcomingConfList);
		request.setAttribute("missConfList", missConfList);
		request.setAttribute("attendedConfList", attendedConfList);
		request.setAttribute("pageModel", pageModel);
		return new ActionForward.Forward("/jsp/user/conf_list_index.jsp");
	}
	
	/**
	 * 进入我的会议页面
	 * @param request
	 * @return
	 */
	@AsController(path = "myMeeting")
	public Object myMeeting(HttpServletRequest request){
		return new ActionForward.Forward("/jsp/user/conf_list_main.jsp");
	}
	
	/**
	 * 查询与自己有关的正在进行中的会议
	 * @param titleOrHostName 会议主题或主持人
	 * wangyong
	 * 2013-3-5
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "listWithDuringConf")
	public Object listWithDuringConf(@CParam("titleOrHostName")  String titleOrHostName, PageModel pageModel, HttpServletRequest request){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		Integer rows = 0;
		rows = confService.countDuringConfList(titleOrHostName, currentUser);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows);
			confList = confService.listDuringConfList(titleOrHostName, pageModel, sortField, sortord, currentUser, currentSite);
			confList = ObjectUtil.parseHtmlWithList(confList, "confName");     //数据库的数据转为实际字符
		}else{
			pageModel.setRowsCount(0);
		}
		Map<Integer,Integer> participantSizeList = getParticipants(confList);   //获取每个会议的参会人个数
		request.setAttribute("participantSizeList", participantSizeList);
		request.setAttribute("user", currentUser); 
		request.setAttribute("confList", confList);
		request.setAttribute("titleOrHostName", titleOrHostName);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortField);   //传排序字段的编号
		request.setAttribute("sortord", sortord);       //传排序方式的编号
		return new ActionForward.Forward("/jsp/user/during_conf_list.jsp");
	}
	
	/**
	 * 查询与自己有关的即将开始的会议
	 * @param titleOrHostName 会议主题或主持人
	 * wangyong
	 * 2013-3-5
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "listWithUpcomingConf")
	public Object listWithUpcomingConf(@CParam("titleOrHostName")  String titleOrHostName, PageModel pageModel, HttpServletRequest request){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		Integer rows = 0;
		rows = confService.countUpcomingConfList(titleOrHostName, currentUser, null);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows);
			confList = confService.listUpcomingConfList(titleOrHostName, pageModel, sortField, sortord, currentUser, currentSite, null);
			confList = ObjectUtil.parseHtmlWithList(confList, "confName");     //数据库的数据转为实际字符
		}else{
			pageModel.setRowsCount(0);
		}
		Map<Integer,Integer> participantSizeList = getParticipants(confList);   //获取每个会议的参会人个数
		request.setAttribute("participantSizeList", participantSizeList);
		request.setAttribute("user", currentUser); 
		request.setAttribute("confList", confList);
		request.setAttribute("titleOrHostName", titleOrHostName);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortField);   //传排序字段的编号
		request.setAttribute("sortord", sortord);       //传排序方式的编号
		return new ActionForward.Forward("/jsp/user/upcoming_conf_list.jsp");
	}
	/**
	 * 查询自己错过的会议
	 * @param titleOrHostName 会议主题或主持人
	 * wangyong
	 * 2013-3-5
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "listWithMissConf")
	public Object listWithMissConf(@CParam("titleOrHostName")  String titleOrHostName, PageModel pageModel, HttpServletRequest request){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		Integer rows = 0;
		rows = confService.countMissConfList(titleOrHostName, currentUser, null, null);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows);
			confList = confService.listMissConfList(titleOrHostName, pageModel, sortField, sortord, currentUser, currentSite, null, null);
			confList = ObjectUtil.parseHtmlWithList(confList, "confName");     //数据库的数据转为实际字符
		}else{
			pageModel.setRowsCount(0);
		}
		Map<Integer,Integer> participantSizeList = getParticipants(confList);   //获取每个会议的参会人个数
		request.setAttribute("participantSizeList", participantSizeList);
		request.setAttribute("user", currentUser); 
		request.setAttribute("confList", confList);
		request.setAttribute("titleOrHostName", titleOrHostName);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortField);   //传排序字段的编号
		request.setAttribute("sortord", sortord);       //传排序方式的编号
		return new ActionForward.Forward("/jsp/user/miss_conf_list.jsp");
	}
	/**
	 * 查询自己参加过的的会议
	 * @param titleOrHostName 会议主题或主持人
	 * wangyong
	 * 2013-3-5
	 */
	@SuppressWarnings("unchecked")
	@AsController(path = "listWithAttendedConf")
	public Object listWithAttendedConf(@CParam("titleOrHostName")  String titleOrHostName, PageModel pageModel, HttpServletRequest request){
		List<ConfBase> confList = new ArrayList<ConfBase>();
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		Integer rows = 0;
		rows = confService.countAttendedConfList(titleOrHostName, currentUser, null);
		if(rows != null && rows.intValue() > 0){
			pageModel.setRowsCount(rows);
			confList = confService.listAttendedConfList(titleOrHostName, pageModel, sortField, sortord, currentUser, currentSite, null);
			confList = ObjectUtil.parseHtmlWithList(confList, "confName");     //数据库的数据转为实际字符
		}else{
			pageModel.setRowsCount(0);
		}
		Map<Integer,Integer> participantSizeList = getParticipants(confList);   //获取每个会议的参会人个数
		request.setAttribute("participantSizeList", participantSizeList);
		request.setAttribute("user", currentUser); 
		request.setAttribute("confList", confList);
		request.setAttribute("titleOrHostName", titleOrHostName);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortField);   //传排序字段的编号
		request.setAttribute("sortord", sortord);       //传排序方式的编号
		return new ActionForward.Forward("/jsp/user/attended_conf_list.jsp");
	}
	
	/**
	 * 查询会议详情
	 * wangyong
	 * @param id 会议id号
	 * 2013-3-5
	 */
	@AsController(path = "view/{confId:([0-9]+)}")
	@Get
	public Object viewConf(@CParam("confId") Integer confId,HttpServletRequest request){
		logger.info("confId=="+confId);
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		UserBase currentUser = userService.getCurrentUser(request);
		ConfBase conf = null;
		if(currentUser != null){
			conf = confService.getConfBasebyConfId(confId, currentUser);
		}else{
			conf = confService.getConfBasebyConfId(confId, currentSite);
		}
		confResourceAppend(conf, currentUser, currentSite, request);    //查询会议详情时拼接会议的资源
		request.setAttribute("conf", conf);
		request.setAttribute("site", currentSite);
		request.setAttribute("user", currentUser);
		return new ActionForward.Forward("/jsp/user/viewConf.jsp");
	}
	
	@AsController(path = "view4sys/{confId:([0-9]+)}")
	@Get
	public Object sysviewConf(@CParam("confId") Integer confId,HttpServletRequest request){
		logger.info("confId=="+confId);
		ConfBase conf = confService.getConfBasebyConfId(confId);
		SiteBase realSite = siteService.getSiteBaseById(conf.getSiteId());
		UserBase realUser = userService.getUserBaseById(conf.getCreateUser());
		String[] fields = new String[]{"startTime","endTime"};
		long offset = 0 ;
		if(realSite != null){
			offset = realSite.getTimeZone();
			realUser.setTimeZone(new Long(offset).intValue());
		}else{
			offset = DateUtil.getDateOffset();
		}
		conf = (ConfBase)ObjectUtil.offsetDate(conf, offset, fields);
		confResourceAppend(conf, realUser, realSite, request);    //查询会议详情时拼接会议的资源
		request.setAttribute("conf", conf);
		request.setAttribute("site", realSite);
		request.setAttribute("user", realUser);
		return new ActionForward.Forward("/jsp/system/viewConf.jsp");
	}
	
	/**
	 * 查询会议详情时拼接会议周期信息的资源
	 */
	private void confResourceAppend(ConfBase conf, UserBase currentUser, SiteBase currentSite, HttpServletRequest request){
		if(conf.getCycleId() != null && conf.getCycleId().intValue() > 0){
			ConfCycle confCycle = confService.getConfCyclebyConfId(conf.getCycleId().intValue());
			long offset = 0 ;
			if(currentUser != null){
				offset = currentUser.getTimeZone();
			}else{
				offset = currentSite.getTimeZone();
			}
			//定期模式：按月（6号；第2个周一）
			StringBuilder cycleMode = new StringBuilder("");
			if(confCycle.getCycleType().intValue() == ConfConstant.CONF_CYCLE_MONTHLY.intValue()){      //按月循环的
				if(confCycle.getCycleValue().indexOf(ConfConstant.CONF_CYCLE_VALUE_MONTH_SPLIT)>0){     //有分号的
					String[] monthValueArray = confCycle.getCycleValue().split(ConfConstant.CONF_CYCLE_VALUE_MONTH_SPLIT);
					String week = ResourceHolder.getInstance().getResource("system.month." + monthValueArray[1]);
					cycleMode.append(String.format(ResourceHolder.getInstance().getResource("system.by.month.week"), monthValueArray[0], week));   // 格式化字符串，按月(每月第几个周几)
				}else{
					cycleMode.append(String.format(ResourceHolder.getInstance().getResource("system.by.month.day"), confCycle.getCycleValue()));   // 格式化字符串，按月(每月第几天)
				}
			}else if(confCycle.getCycleType().intValue() == ConfConstant.CONF_CYCLE_DAILY.intValue()){    //按日循环的
				cycleMode.append(String.format(ResourceHolder.getInstance().getResource("system.by.day"), confCycle.getCycleValue()));      // 格式化字符串，按日(每几天)
			}else if(confCycle.getCycleType().intValue() == ConfConstant.CONF_CYCLE_WEEKLY.intValue()){   //按周循环的
				StringBuilder week = new StringBuilder();
				for(String weekValue : confCycle.getCycleValue().split(ConfConstant.CONF_CYCLE_VALUE_DAYS_SPLIT)){
					week.append(ResourceHolder.getInstance().getResource("system.month." + weekValue)).append(",");
				}
				cycleMode.append(String.format(ResourceHolder.getInstance().getResource("system.by.week"), 
						week.deleteCharAt(week.lastIndexOf(",")).toString()));      // 格式化字符串，按周（每周几）
			}
			request.setAttribute("cycleMode", cycleMode.toString());
			request.setAttribute("confCycle", (ConfCycle)ObjectUtil.offsetDate(confCycle, offset, new String[]{"beginDate","endDate"}));
		}
		int duraH = conf.getDuration()/60;
		int duraM = conf.getDuration()%60;
		String duration = "";
		if(duraH >= 1){
			duration = String.valueOf(duraH) + ResourceHolder.getInstance().getResource("user.menu.conf.hour");
			if(duraH > 1){
				duration = String.valueOf(duraH) + ResourceHolder.getInstance().getResource("user.menu.conf.hours");
			}
		}
		if(duraM > 0){
			duration += " " + String.valueOf(duraM) + ResourceHolder.getInstance().getResource("user.menu.conf.minute");
		}
		duraH = conf.getAheadTime()/60;
		duraM = conf.getAheadTime()%60;
		String aheadTime = String.valueOf(duraM) + ResourceHolder.getInstance().getResource("user.menu.conf.minute");
		if(duraH >= 1){
			aheadTime = String.valueOf(duraH) + ResourceHolder.getInstance().getResource("user.menu.conf.hour") + aheadTime;
		}
//		int confUserNum = confService.getTerminalNum(confId, null);    //会议的参会人数
//		request.setAttribute("confUserNum", confUserNum);
		List<ConfUser> userBaseList = null;
		
		if(conf.getPermanentConf().intValue() == ConfConstant.CONF_PERMANENT_ENABLED_CHILD){
			userBaseList = confService.getConfInviteUser(conf.getBelongConfId());
		}else{
			userBaseList = confService.getConfInviteUser(conf.getId());
		}
		if(userBaseList != null && userBaseList.size() > 0){
			request.setAttribute("inviteUserCount", userBaseList.size());
		}else{
			request.setAttribute("inviteUserCount", 0);
		}
		if(currentUser == null || currentUser.getId() == null){
			conf.setCompereSecure("");
			conf.setPublicConfPass("");
			if (!conf.isPublic()) {
				conf.setUserSecure("");
			}
		}
		
		StringBuilder confType = new StringBuilder("");
		if(conf.getPublicFlag().intValue() == ConfConstant.CONF_PUBLIC_FLAG_TRUE.intValue()){
			confType.append(ResourceHolder.getInstance().getResource("user.menu.conf.public"));
		}
		String resourceConfType = ResourceHolder.getInstance().getResource("conf.confType." + conf.getConfType().intValue());
		if(StringUtil.isNotBlank(confType.toString()) && StringUtil.isNotBlank(resourceConfType)){
			confType.append("、");
		}
		if(StringUtil.isNotBlank(resourceConfType)){
			confType.append(resourceConfType);
		}
		request.setAttribute("confType", confType.toString());
//		文档共享 屏幕共享 
//		媒体共享 白板 
//		文件传输 录制
		StringBuilder clientFunc = new StringBuilder();
		char[] clientConfig = conf.getClientConfig().toCharArray();
		clientFunc.append(clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_DOCS] == '1' ? ResourceHolder.getInstance().getResource("system.site.empower.item.5") + ", " : "");
		clientFunc.append(clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_SCREEN] == '1' ? ResourceHolder.getInstance().getResource("system.site.empower.item.6") + ", " : "");
		clientFunc.append(clientConfig[ConfConstant.CLIENT_CONFIG_SHARE_MEDIA] == '1' ? ResourceHolder.getInstance().getResource("system.site.empower.item.3") + ", "  : "");
		clientFunc.append(clientConfig[ConfConstant.CLIENT_CONFIG_WHITE_BOARD] == '1' ? ResourceHolder.getInstance().getResource("system.site.empower.item.7") + ", "  : "");
		clientFunc.append(clientConfig[ConfConstant.CLIENT_CONFIG_FILE_TRANS] == '1' ? ResourceHolder.getInstance().getResource("system.site.empower.item.12") + ", "  : "");
		clientFunc.append(clientConfig[ConfConstant.CLIENT_CONFIG_RECORD] == '1' ? ResourceHolder.getInstance().getResource("system.site.empower.item.4") + ", "  : "");
		String client =  clientFunc.toString();
		request.setAttribute("hostUrl", emailService.getJoinUrlForHost(conf));
		request.setAttribute("userUrl", emailService.getJoinUrlForUser(conf));
		request.setAttribute("clientFunc", client.lastIndexOf(",") > 0 ? client.substring(0, client.lastIndexOf(",")) : client);
		request.setAttribute("duration", duration);
		request.setAttribute("aheadTime", aheadTime);
	}
	
	/**
	 * 加入会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "attendConf")
	public Object attendConf(@CParam("confId") Integer confId,HttpServletRequest request){
		return new ActionForward.Forward("/jsp/user/attendConf.jsp");
	}
	
	/**
	 * 主持人创建即时会议(跳转页面)
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "createImmediatelyConf")
	@Interceptors({UserInterceptor.class})
	@Get
	public Object createImmediatelyConf(HttpServletRequest request) throws Exception{
		UserBase currentUser = userService.getCurrentUser(request);
//		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		request.setAttribute("confNameDefault", new StringBuilder(currentUser.getTrueName()).append(ResourceHolder.getInstance().getResource("user.menu.conf.who.conf")).toString());
		return new ActionForward.Forward("/jsp/user/create_Immediately_Conf.jsp");
	}
	
	/**
	 * 主持人创建即时会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "createNewImmediatelyConf")
	@Post
	public Object createNewImmediatelyConf(ConfBase conf, HttpServletRequest request){
		ConfBase confBase = null;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		if(conf != null){
			//判断当前企业用户是否为主持人角色
			if(currentUser.isConfHost()){
				confBase = confService.createImmediatelyConf(conf, currentSite, currentUser);
				if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_IMMEDIATELY_CREATE, "当前企业用户创建即时会议成功",
							EventLogConstants.EVENTLOG_SECCEED, confBase, request);   //创建成功后写EventLog
					JSONObject json = new JSONObject();
					json.put("status", ConstantUtil.CREATE_CONF_SUCCEED);
					json.put("id", confBase.getId().intValue());
					return json.toString();
				}else{
					String errorMessage = getConfErrorMessage(confBase);
					 //ResourceHolder.getInstance().getResource("system.site.meaasge.create.succeed"), 
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_IMMEDIATELY_CREATE, "创建即时会议失败"+ ":" + errorMessage,
							EventLogConstants.EVENTLOG_FAIL, confBase, request);   //创建失败后写EventLog
					return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.create.confnow.failed")+ errorMessage);
				}
			}else{
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_IMMEDIATELY_CREATE, "当前企业用户无权限创建即时会议",
						EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
				return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.confnow.create.not"));
			}
		}else{
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_IMMEDIATELY_CREATE, "当前企业用户创建即时会议失败",
					EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
			return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.create.confnow.failed"));
		}
	}
	
	/**
	 * 主持人创建预约会议(跳转页面)
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "createReservationConf")
	@Interceptors({UserInterceptor.class})
	@Get
	public Object createReservationConf(HttpServletRequest request) throws Exception{
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		//当前用户喜好设置时区的时间
		Date curDate=currentUser.getUserLocalTime();
//		Date curDate=currentSite.getSiteLocalTime();
		DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
		if(confConfig == null || confConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
			confConfig = confService.saveDefaultConfig(currentUser);
		}
//		//根据站点ID号取站点的全局变量设置
//		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentUser.getSiteId());
//		//根据用户的ID号取用户的授权配置(站点管理员创建企业用户时的授权配置)
//		EmpowerConfig userPower  = empowerConfigService.getUserEmpowerConfigByUserId(currentUser.getId());
//		if(sitePower != null && userPower != null){
//			//站点全局变量“媒体共享”启用并且用户“媒体共享”授权启用后，创建会议才可以选择“媒体共享”项
//			if(sitePower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isShareMediaFlag", SiteConstant.EMPOWER_ENABLED);      
//			}
//			if(sitePower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isRecordFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isPhoneFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isAutoFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//		}
		EmpowerConfig userEmpower = empowerConfigService.makeEmpowerForConf(currentUser);   //获取用户创建会议，缺省会议设置的权限
		setEmpowerFlag(request, userEmpower);    //将用户权限flag传递到前台
		request.setAttribute("userEmpower", userEmpower);
		request = confService.confConfigAttr(confConfig, request);
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("currentUser", currentUser);
		request.setAttribute("siteBase", currentSite);
		request.setAttribute("defaultLicence", licService.getSiteLicenseNum(currentSite.getId()) + currentSite.getLicense().intValue());    //最大参会人数
		request.setAttribute("defaultConfig", confConfig);
		return new ActionForward.Forward("/jsp/user/create_Reservation_Conf.jsp");
	}

	
	/**
	 * 主持人创建预约会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "createNewReservationConf")
	@Post
	public Object createNewReservationConf(ConfBase conf, ConfCycle confCycle, HttpServletRequest request){
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		ConfBase confBase = null;
		JSONObject json = new JSONObject();
		JSONArray jsonArrConf = new JSONArray();
		JSONArray jsonArrConfCycle = new JSONArray();
		String errorMessage = "";
		if(conf != null){
			DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
			if(confConfig == null || confConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
				confConfig = confService.saveDefaultConfig(currentUser);
			}
			String funcBits = confService.getFuncBits(conf, request, confConfig);
			String priviBits = confService.getPriviBits(request, confConfig);
			String clientConfig = confService.getClientConfig(request, confConfig);
			conf.setFuncBits(funcBits);
			conf.setPriviBits(priviBits);
			conf.setClientConfig(clientConfig);
			conf = confService.checkConfFunc(conf, currentUser);
			String isCycleConf = request.getParameter("cycleOption");
			//判断当前企业用户是否为主持人角色
			if(currentUser.isConfHost()){
				if(StringUtil.isNotBlank(isCycleConf) && IntegerUtil.parseIntegerWithDefaultZero(isCycleConf).intValue() == ConfConstant.CONF_CYCLE_TRUE.intValue()){
					confBase = confService.createCycleReservationConf(conf, confCycle, currentSite, currentUser);
					if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
						eventLogService.saveUserEventLog(currentUser, 
								EventLogConstants.SITEUSER_CONF_CYCLE_CREATE, "创建周期预约会议成功",
								EventLogConstants.EVENTLOG_SECCEED, confBase, request);   //创建成功后写EventLog
					}else{
						errorMessage = getConfErrorMessage(confBase);
						eventLogService.saveUserEventLog(currentUser, 
								EventLogConstants.SITEUSER_CONF_CYCLE_CREATE, "创建周期预约会议失败" + errorMessage,
								EventLogConstants.EVENTLOG_FAIL, confBase, request);   //创建失败后写EventLog
						return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.create.confcycle.failed") + errorMessage);
					}
				}else{
					if(conf.isBelongPermanentConf()){
						confBase = confService.createPermanentConf(conf, currentSite, currentUser);
						confUserService.fillConfUserForCreate(confBase, currentUser);
					}else {
						conf.setDuration(conf.getDuration());
						confBase = confService.createSingleReservationConf(conf, currentSite, currentUser);
					}
					
					if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
						eventLogService.saveUserEventLog(currentUser, 
								EventLogConstants.SITEUSER_CONF_RESERVATION_CREATE, "创建预约会议成功",
								EventLogConstants.EVENTLOG_SECCEED, confBase, request);   //创建成功后写EventLog
					}else{
						errorMessage = getConfErrorMessage(confBase);
						eventLogService.saveUserEventLog(currentUser, 
								EventLogConstants.SITEUSER_CONF_RESERVATION_CREATE, "创建预约会议失败" + errorMessage,
								EventLogConstants.EVENTLOG_FAIL, confBase, request);   //创建失败后写EventLog
						return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.create.conf.failed") + errorMessage);
					}
				}
			}else{
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_RESERVATION_CREATE, "当前企业用户无权限创建预约会议",
						EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
				return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.create.conf.failed"));
			}
		}else{
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_RESERVATION_CREATE, "创建预约会议失败",
					EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
			return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.create.conf.failed"));
		}
		json.put("status", ConstantUtil.CREATE_CONF_SUCCEED);
		jsonArrConf.add(JsonUtil.parseJsonWithFullFieldByObject(confBase));
		jsonArrConfCycle.add(JsonUtil.parseJsonWithFullFieldByObject(confCycle));
		json.put("confBase", jsonArrConf);
		json.put("confCycle", jsonArrConfCycle);
		
		return json.toString();
	}
	
	/**
	 * 主持人修改预约会议(跳转页面)
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "update/{confId:([0-9]+)}")
	@Get
	public Object updateConf(@CParam("confId") Integer confId, HttpServletRequest request) throws Exception{
		logger.info("confId=="+confId);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		UserBase currentUser = userService.getCurrentUser(request);
		ConfBase conf = confService.getConfBasebyConfId(confId);
		if(conf != null){
			String[] fields = new String[]{"startTime","endTime"};
			long offset = 0 ;
			if(conf.getTimeZone() != null){
				offset = conf.getTimeZone();
			}else{
				offset = currentSite.getTimeZone();
			}
			logger.info("当前访问的站点时区" + offset);
			conf = (ConfBase)ObjectUtil.offsetDate(conf, offset, fields);
		}
		//当前站点时区的时间
		Date curDate=conf.getConfLocalTime();
//		Date curDate=currentSite.getSiteLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("siteBase", currentSite);
		request.setAttribute("defaultLicence", licService.getSiteLicenseNum(currentSite.getId()) + currentSite.getLicense().intValue());    //最大参会人数
		
		DefaultConfig confConfig = new DefaultConfig();
		confConfig.setPriviBits(conf.getPriviBits());
		confConfig.setClientConfig(conf.getClientConfig());
		confConfig.setFuncBits(conf.getFuncBits());
		conf = confService.checkConfFunc(conf, currentUser);
		request = confService.confConfigAttr(confConfig, request);
//		//根据站点ID号取站点的全局变量设置
//		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentUser.getSiteId());
//		//根据用户的ID号取用户的授权配置(站点管理员创建企业用户时的授权配置)
//		EmpowerConfig userPower  = empowerConfigService.getUserEmpowerConfigByUserId(currentUser.getId());
//		if(sitePower != null && userPower != null){
//			//站点全局变量“媒体共享”启用并且用户“媒体共享”授权启用后，创建会议才可以选择“媒体共享”项
//			if(sitePower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isShareMediaFlag", SiteConstant.EMPOWER_ENABLED);      
//			}
//			if(sitePower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isRecordFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isPhoneFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isAutoFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			
//		}
		EmpowerConfig userEmpower = empowerConfigService.makeEmpowerForConf(currentUser);   //获取用户创建会议，缺省会议设置的权限
		setEmpowerFlag(request, userEmpower);    //将用户权限flag传递到前台
		request.setAttribute("conf", conf);
		request.setAttribute("userEmpower", userEmpower);
		return new ActionForward.Forward("/jsp/user/create_Reservation_Conf.jsp");
	}
	
	/**
	 * 主持人修改周期预约会议中所有会议的信息(跳转页面)
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "updateCycleConfInfo/{confId:([0-9]+)}")
	@Get
	public Object updateCycleConfInfo(@CParam("confId") Integer confId, HttpServletRequest request) throws Exception{
		logger.info("confId=="+confId);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		UserBase currentUser = userService.getCurrentUser(request);
		ConfBase conf = confService.getConfBasebyConfId(confId);
		ConfCycle confCycle = confService.getConfCyclebyConfId(conf.getCycleId());
		if(conf != null){
			String[] fields = new String[]{"startTime","endTime"};
			long offset = 0 ;
			if(conf.getTimeZone() != null){
				offset = conf.getTimeZone();
			}else{
				offset = currentSite.getTimeZone();
			}
			logger.info("当前访问的站点时区" + offset);
			conf = (ConfBase)ObjectUtil.offsetDate(conf, offset, fields);
		}
		//当前站点时区的时间
		Date curDate = conf.getConfLocalTime();
//		Date curDate=currentSite.getSiteLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("currentUser", currentUser);
		request.setAttribute("siteBase", currentSite);
		request.setAttribute("cycleConfType", 1);
		request.setAttribute("defaultLicence", licService.getSiteLicenseNum(currentSite.getId()) + currentSite.getLicense().intValue());    //最大参会人数
		setConfCycleRequest(request, confCycle);
		DefaultConfig confConfig = new DefaultConfig();
		confConfig.setPriviBits(conf.getPriviBits());
		confConfig.setClientConfig(conf.getClientConfig());
		confConfig.setFuncBits(conf.getFuncBits());
		request = confService.confConfigAttr(confConfig, request);
//		//根据站点ID号取站点的全局变量设置
//		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentUser.getSiteId());
//		//根据用户的ID号取用户的授权配置(站点管理员创建企业用户时的授权配置)
//		EmpowerConfig userPower  = empowerConfigService.getUserEmpowerConfigByUserId(currentUser.getId());
//		if(sitePower != null && userPower != null){
//			//站点全局变量“媒体共享”启用并且用户“媒体共享”授权启用后，创建会议才可以选择“媒体共享”项
//			if(sitePower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isShareMediaFlag", SiteConstant.EMPOWER_ENABLED);      
//			}
//			if(sitePower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isRecordFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isPhoneFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isAutoFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//		}
		EmpowerConfig userEmpower = empowerConfigService.makeEmpowerForConf(currentUser);   //获取用户创建会议，缺省会议设置的权限
		setEmpowerFlag(request, userEmpower);    //将用户权限flag传递到前台
		request.setAttribute("conf", conf);
		request.setAttribute("userEmpower", userEmpower);
		return new ActionForward.Forward("/jsp/user/create_Reservation_Conf.jsp");
	}
	
	/**
	 * 传给前台周期循环规则参数
	 * shhc
	 * 2013-4-17
	 */
	private void setConfCycleRequest(HttpServletRequest request, ConfCycle confCycle){
		int cycleType = confCycle.getCycleType();
		String cycleValue = confCycle.getCycleValue();
		if(ConfConstant.CONF_CYCLE_DAILY.intValue() == cycleType){
			request.setAttribute("cycleDayValue", cycleValue);
		}
		if(ConfConstant.CONF_CYCLE_WEEKLY.intValue() == cycleType){
			request.setAttribute("cycleWeekValue", cycleValue);
		}
		if(ConfConstant.CONF_CYCLE_MONTHLY.intValue() == cycleType){
			if(cycleValue.indexOf(";") == -1){
				request.setAttribute("monthCycleOption", 1);
				request.setAttribute("eachMonthDay", cycleValue);
			}else{
				request.setAttribute("monthCycleOption", 2);
				String[] cycleValueArray = cycleValue.split(";"); 
				request.setAttribute("weekFlag", cycleValueArray[0]);
				request.setAttribute("weekDay", cycleValueArray[1]);
			}
		}
		request.setAttribute("cycleType", cycleType);
	}
	
	/**
	 * 获取所有周期预约会议的日期
	 * wangyong
	 * 2013-3-18
	 */
	@AsController(path = "getCycleConfDate/{cycleId:([0-9]+)}")
	public Object getCycleConfDate(@CParam("cycleId") Integer cycleId,@CParam("delCycle") String delCycle, HttpServletRequest request){
		UserBase currentUser = userService.getCurrentUser(request);
		List<ConfBase> confList = confService.getCycleConfDate(cycleId, currentUser);
		request.setAttribute("confList", confList);
		request.setAttribute("delCycle", delCycle);
		request.setAttribute("curYear", DateUtil.getDateStrCompact(currentUser.getUserLocalTime(), "yyyy-MM-dd HH:mm:ss").substring(0,4));
		return new ActionForward.Forward("/jsp/user/updateReservation_Conf.jsp");
	}
	
	/**
	 * 主持人修改周期预约会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "updateCycleConfInfo")
	public Object updateCycleConfInfo(ConfBase conf, HttpServletRequest request){
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		Integer confId = conf.getId();
		JSONObject json = new JSONObject();
		JSONArray jsonArrConf = new JSONArray();
		//先检测参会人数是否大于站点当前所剩参会人数值
//		boolean licenceFlag = confLogic.updateConfLicenseVali(conf, null, currentSite, currentUser);
//		if(!licenceFlag){
//			conf.setId(ConfConstant.CONF_CREATE_ERROR_LICENCE);
//			String errorMessage = getConfErrorMessage(conf);
//			eventLogService.saveUserEventLog(currentUser, 
//					EventLogConstants.SITEUSER_CONF_RESERVATION_CREATE, "修改预约会议失败" + errorMessage,
//					EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
//			return returnJsonStr(ConstantUtil.CREATE_CONF_FAIL, "修改预约会议失败" + errorMessage);
//		}
		if(conf != null && confId != null){
			//判断当前企业用户是否有权限修改即时会议
			boolean userRole = confService.updateConfAuthority(confId, currentUser);
			if(userRole){
				DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
				if(confConfig == null || confConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
					confConfig = confService.saveDefaultConfig(currentUser);
				}
				String funcBits = confService.getFuncBits(conf, request, confConfig);
				String priviBits = confService.getPriviBits(request, confConfig);
				String clientConfig = confService.getClientConfig(request, confConfig);
				conf.setFuncBits(funcBits);
				conf.setPriviBits(priviBits);
				conf.setClientConfig(clientConfig);
				conf = confService.checkConfFunc(conf, currentUser);
				ConfBase confBase = confService.updateCycleConfInfo(conf, currentSite, currentUser);
				if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_UPDATE, "修改预约会议成功",
							EventLogConstants.EVENTLOG_SECCEED, conf, request);   //创建成功后写EventLog
				}else{
					String errorMessage = getConfErrorMessage(confBase);
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_UPDATE, "修改预约会议失败" + errorMessage,
							EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
					return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed") + errorMessage);
				}
			}else{
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_UPDATE, "当前企业用户无权限修改预约会议",
						EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
				return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed"));
			}
		}else{
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_UPDATE, "当前企业用户修改预约会议失败",
					EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
			return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed"));
		}
		json.put("status", ConstantUtil.UPDATE_CONF_SUCCEED);
		jsonArrConf.add(JsonUtil.parseJsonWithFullFieldByObject(conf));
		json.put("confBase", jsonArrConf);
		return json.toString();
	}
	
	/**
	 * 主持人修改周期预约会议中的一条会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "updateSingleCycleConfInfo")
	public Object updateSingleCycleConfInfo(ConfBase conf, HttpServletRequest request){
		ConfBase confBase = null;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		Integer confId = conf.getId();
		JSONObject json = new JSONObject();
		JSONArray jsonArrConf = new JSONArray();
		if(conf != null && confId != null){
			//判断当前企业用户是否有权限修改即时会议
			boolean userRole = confService.updateConfAuthority(confId, currentUser);
			if(userRole){
				DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
				if(confConfig == null || confConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
					confConfig = confService.saveDefaultConfig(currentUser);
				}
				String funcBits = confService.getFuncBits(conf, request, confConfig);
				String priviBits = confService.getPriviBits(request, confConfig);
				String clientConfig = confService.getClientConfig(request, confConfig);
				conf.setFuncBits(funcBits);
				conf.setPriviBits(priviBits);
				conf.setClientConfig(clientConfig);
				conf = confService.checkConfFunc(conf, currentUser);
				confBase = confService.updateSingleCycleConfInfo(conf, currentSite, currentUser);
				if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_UPDATE, "修改预约会议成功",
							EventLogConstants.EVENTLOG_SECCEED, conf, request);   //创建成功后写EventLog
				}else{
					String errorMessage = getConfErrorMessage(confBase);
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_UPDATE, "修改预约会议失败" + errorMessage,
							EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
					return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed") + errorMessage);
				}
			}else{
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_UPDATE, "当前企业用户无权限修改预约会议",
						EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
				return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed"));
			}
		}else{
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_UPDATE, "当前企业用户修改预约会议失败",
					EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
			return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed"));
		}
		json.put("status", ConstantUtil.UPDATE_CONF_SUCCEED);
		jsonArrConf.add(JsonUtil.parseJsonWithFullFieldByObject(confBase));
		json.put("confBase", jsonArrConf);
		return json.toString();
	}
	
	/**
	 * 主持人修改单次预约会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "updateConfInfo")
	public Object updateConfInfo(ConfBase conf, HttpServletRequest request){
		ConfBase confBase = null;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		Integer confId = conf.getId();
		JSONObject json = new JSONObject();
		JSONArray jsonArrConf = new JSONArray();
		if(conf != null && confId != null){
			//判断当前企业用户是否有权限修改即时会议
			boolean userRole = confService.updateConfAuthority(confId, currentUser);
			if(userRole){
				DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
				if(confConfig == null || confConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
					confConfig = confService.saveDefaultConfig(currentUser);
				}
				String funcBits = confService.getFuncBits(conf, request, confConfig);
				String priviBits = confService.getPriviBits(request, confConfig);
				String clientConfig = confService.getClientConfig(request, confConfig);
				conf.setFuncBits(funcBits);
				conf.setPriviBits(priviBits);
				conf.setClientConfig(clientConfig);
				conf = confService.checkConfFunc(conf, currentUser);
				if(conf.isBelongPermanentConf()){
					confBase = confService.updatePermanentConf(conf, currentSite, currentUser);
				}else{
					confBase = confService.updateSingleReservationConf(conf, currentSite, currentUser);
				}
				if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_UPDATE, "修改预约会议成功",
							EventLogConstants.EVENTLOG_SECCEED, conf, request);   //创建成功后写EventLog
				}else{
					String errorMessage = getConfErrorMessage(confBase);
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_UPDATE, "修改预约会议失败" + errorMessage,
							EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
					return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed") + errorMessage);
				}
			}else{
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_UPDATE, "当前企业用户无权限修改预约会议",
						EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
				return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed"));
			}
		}else{
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_UPDATE, "当前企业用户修改预约会议失败",
					EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
			return returnJsonStr(ConstantUtil.UPDATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.update.confcycle.failed"));
		}
		json.put("status", ConstantUtil.UPDATE_CONF_SUCCEED);
		jsonArrConf.add(JsonUtil.parseJsonWithFullFieldByObject(confBase));
		json.put("confBase", jsonArrConf);
		return json.toString();
	}
	
	/**
	 * 重新创建会议(跳转页面)
	 * 1.如果已错过的会议为自己创建的，可以重新创建该会议
	 * 2.如果已参加过的会议为自己创建的，可以重新创建该会议
	 * 3.只能创建单次会议，不可创建周期会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "reCreateconf/{confId:([0-9]+)}")
	@Get
	public Object reCreateconf(@CParam("confId") Integer confId, HttpServletRequest request){
		logger.info("confId=="+confId);
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		ConfBase conf = confService.getConfBasebyConfId(confId, currentUser);
		if(conf != null && currentUser != null){
			if(conf.getCreateUser() != null && conf.getCreateUser().intValue() != currentUser.getId()){
				setErrMessage(request,  ResourceHolder.getInstance().getResource("bizconf.jsp.user.conf.recreate.error"));
				return false;
			}
		}
		//当前用户喜好设置时区的时间
		Date curDate=currentUser.getUserLocalTime();
//		Date curDate=currentSite.getSiteLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("currentUser", currentUser);
		request.setAttribute("siteBase", currentSite);
		/*
		 * cycleConfType:
		 * 值1：修改循环会议中的所有会议 ;
		 * 值2：重新创建会议
		 */
		request.setAttribute("cycleConfType", 2);
		request.setAttribute("defaultLicence", licService.getSiteLicenseNum(currentSite.getId()) + currentSite.getLicense().intValue());    //最大参会人数
		
		DefaultConfig confConfig = new DefaultConfig();
		confConfig.setPriviBits(conf.getPriviBits());
		confConfig.setClientConfig(conf.getClientConfig());
		confConfig.setFuncBits(conf.getFuncBits());
		request = confService.confConfigAttr(confConfig, request);
		
		request.setAttribute("conf", conf);
//		//根据站点ID号取站点的全局变量设置
//		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentUser.getSiteId());
//		//根据用户的ID号取用户的授权配置(站点管理员创建企业用户时的授权配置)
//		EmpowerConfig userPower  = empowerConfigService.getUserEmpowerConfigByUserId(currentUser.getId());
//		if(sitePower != null && userPower != null){
//			//站点全局变量“媒体共享”启用并且用户“媒体共享”授权启用后，创建会议才可以选择“媒体共享”项
//			if(sitePower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getShareMediaFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isShareMediaFlag", SiteConstant.EMPOWER_ENABLED);      
//			}
//			if(sitePower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getRecordFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isRecordFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getPhoneFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isPhoneFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//			if(sitePower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue() && userPower.getAutoFlag().intValue() == SiteConstant.EMPOWER_ENABLED.intValue()){
//				request.setAttribute("isAutoFlag", SiteConstant.EMPOWER_ENABLED);
//			}
//		}
		EmpowerConfig userEmpower = empowerConfigService.makeEmpowerForConf(currentUser);   //获取用户创建会议，缺省会议设置的权限
		setEmpowerFlag(request, userEmpower);    //将用户权限flag传递到前台
		request.setAttribute("userEmpower", userEmpower);
		return new ActionForward.Forward("/jsp/user/create_Reservation_Conf.jsp");
	}
	
	/**
	 * 重新创建会议
	 * 1.如果已错过的会议为自己创建的，可以重新创建该会议
	 * 2.如果已参加过的会议为自己创建的，可以重新创建该会议
	 * 3.只能创建单次会议，不可创建周期会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "reCreateconfInfo")
	@Post
	public Object reCreateconfInfo(ConfBase conf, HttpServletRequest request){
		ConfBase confBase = null;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		Integer confId = conf.getId();
		JSONObject json = new JSONObject();
		JSONArray jsonArrConf = new JSONArray();
		if(conf != null && confId != null){
			//判断当前企业用户是否有权限修改即时会议
			boolean userRole = confService.recreateConfAuthority(confId, currentUser);
			if(userRole){
				DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
				if(confConfig == null || confConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
					confConfig = confService.saveDefaultConfig(currentUser);
				}
				String funcBits = confService.getFuncBits(conf, request, confConfig);
				String priviBits = confService.getPriviBits(request, confConfig);
				String clientConfig = confService.getClientConfig(request, confConfig);
				conf.setFuncBits(funcBits);
				conf.setPriviBits(priviBits);
				conf.setClientConfig(clientConfig);
				conf = confService.checkConfFunc(conf, currentUser);
				confBase = confService.reCreateconf(conf, currentSite, currentUser);
				if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_RECREATE, "重新预约会议成功",
							EventLogConstants.EVENTLOG_SECCEED, conf, request);   //创建成功后写EventLog
				}else{
					String errorMessage = getConfErrorMessage(confBase);
					eventLogService.saveUserEventLog(currentUser, 
							EventLogConstants.SITEUSER_CONF_RECREATE, "重新创建会议失败" + errorMessage,
							EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
					return returnJsonStr(ConstantUtil.RECREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.recreate.conf.failed") + errorMessage);
				}
			}else{
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_RECREATE, "当前企业用户无权限重新创建会议",
						EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
				return returnJsonStr(ConstantUtil.RECREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.recreate.conf.failed"));
			}
		}else{
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_RECREATE, "当前企业用户重新创建会议失败",
					EventLogConstants.EVENTLOG_FAIL, conf, request);   //创建失败后写EventLog
			return returnJsonStr(ConstantUtil.RECREATE_CONF_FAIL, ResourceHolder.getInstance().getResource("bizconf.jsp.user.recreate.conf.failed"));
		}
		json.put("status", ConstantUtil.RECREATE_CONF_SUCCEED);
		jsonArrConf.add(JsonUtil.parseJsonWithFullFieldByObject(confBase));
		json.put("confBase", jsonArrConf);
		return json.toString();
	}
	
	/**
	 * 主持人删除(取消)周期预约会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "deleteCycleConfInfo/{cycleId:([0-9]+)}")
	public boolean deleteCycleConfInfo(@CParam("confId") Integer confId, @CParam("cycleId") Integer cycleId, HttpServletRequest request){
		int delStatus = ConstantUtil.DELETE_CONF_FAIL; 
		boolean delFlag = false;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		//判断当前企业用户是否有权限删除即时会议
		boolean userRole = confService.cancleConfAuthority(confId, currentUser);
		if(userRole){
			delFlag = confService.cancleCycleConfInfo(cycleId, currentSite, currentUser);
			if(delFlag){
				delStatus = ConstantUtil.DELETE_CONF_SUCCEED;
				setInfoMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.del.confcycle.success"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_DELETE, "删除周期预约会议成功",
						EventLogConstants.EVENTLOG_SECCEED, null, request);   //删除成功后写EventLog
			}else{
				setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.del.confcycle.failed"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_DELETE, "删除周期预约会议失败",
						EventLogConstants.EVENTLOG_FAIL, null, request);   //删除失败后写EventLog
			}
		}else{
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_DELETE, "删除周期预约会议成功",
					EventLogConstants.EVENTLOG_FAIL, null, request);   //删除失败后写EventLog
			setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.confcycle.delete.not"));
		}
		return delFlag;
	}
	
	/**
	 * 主持人删除(取消)周期预约会议中的一条会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "deleteSingleCycleConfInfo/{confId:([0-9]+)}")
	public boolean deleteSingleCycleConfInfo(@CParam("confId") Integer confId, HttpServletRequest request){
		int delStatus = ConstantUtil.DELETE_CONF_FAIL; 
		boolean delFlag = false;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		//判断当前企业用户是否有权限删除即时会议
		boolean userRole = confService.cancleConfAuthority(confId, currentUser);
		if(userRole){
			delFlag = confService.cancleSingleCycleConfInfo(confId, currentSite, currentUser);
			if(delFlag){
				delStatus = ConstantUtil.DELETE_CONF_SUCCEED;
				setInfoMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.del.conforder.success"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_DELETE, "删除预约会议成功",
						EventLogConstants.EVENTLOG_SECCEED, null, request);   //删除成功后写EventLog
			}else{
				setErrMessage(request,  ResourceHolder.getInstance().getResource("bizconf.jsp.user.del.conforder.failed"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_DELETE, "删除预约会议失败",
						EventLogConstants.EVENTLOG_FAIL, null, request);   //删除失败后写EventLog
			}
		}else{
			setErrMessage(request,  ResourceHolder.getInstance().getResource("bizconf.jsp.user.conforder.delete.not"));
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_DELETE, "删除预约会议失败",
					EventLogConstants.EVENTLOG_FAIL, null, request);   //删除失败后写EventLog
		}
		return delFlag;
	}
	
	/**
	 * 主持人删除(取消)单次预约会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "delete/{confId:([0-9]+)}")
	public boolean deleteConf(@CParam("confId") Integer confId, HttpServletRequest request){
		int delStatus = ConstantUtil.DELETE_CONF_FAIL; 
		boolean delFlag = false;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		//判断当前企业用户是否有权限删除即时会议
		boolean userRole = confService.cancleConfAuthority(confId, currentUser);
		if(userRole){
			delFlag = confService.cancleSingleReservationConf(confId, currentSite, currentUser);
			if(delFlag){
				delStatus = ConstantUtil.DELETE_CONF_SUCCEED;
				setInfoMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.del.conforder.success"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_DELETE, "删除预约会议成功",
						EventLogConstants.EVENTLOG_SECCEED, null, request);   //删除失败后写EventLog
			}else{
				setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.del.conforder.failed"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_DELETE, "删除预约会议失败",
						EventLogConstants.EVENTLOG_FAIL, null, request);   //删除失败后写EventLog
			}
		}else{
			setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.conforder.delete.not"));
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_DELETE, "当前企业用户无权限删除预约会议",
					EventLogConstants.EVENTLOG_FAIL, null, request);   //删除失败后写EventLog
		}
		return delFlag;
	}
	
	/**
	 * 企业用户隐藏已错过的会议
	 * 1.所有企业用户都可以隐藏已错过的会议
	 * 2.只是隐藏会议主控面板的已错过的会议
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "hide/{confId:([0-9]+)}")
	public boolean hideMissConf(@CParam("confId") Integer confId, HttpServletRequest request){
		boolean hideFlag = false;
		UserBase currentUser = userService.getCurrentUser(request);
		hideFlag = confService.hideMissConf(confId, currentUser);
		if(hideFlag){
			setInfoMessage(request, "隐藏已错过的会议成功");
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_DELETE, "隐藏已错过的会议成功",
					EventLogConstants.EVENTLOG_SECCEED, null, request);   
		}else{
			setErrMessage(request,  "隐藏已错过的会议失败");
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_DELETE, "隐藏已错过的会议失败",
					EventLogConstants.EVENTLOG_FAIL, null, request);
		}
		return hideFlag;
	}
	
	/**
	 * 主持人邀请参会人(跳转页面)
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "invite")
	public Object invite(){
		return new ActionForward.Forward("/jsp/user/invite_Conf.jsp");
	}
	
	/**
	 * 主持人邀请参会人，导入联系人(跳转页面默认显示群组列表)
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "inviteImportContacts")
	public Object inviteImportContacts(@CParam("nameOrEmailOrMobile") String nameOrEmailOrMobile, PageModel pageModel, HttpServletRequest request){
		UserBase currentUser = userService.getCurrentUser(request);
		PageBean<ContactGroup> page = new PageBean<ContactGroup>();
		if(currentUser != null){
			page = groupService.getGroupPageModel(nameOrEmailOrMobile,currentUser.getSiteId(),currentUser.getId(),IntegerUtil.parseIntegerWithDefaultZero(pageModel.getPageNo()),0);
		}
		request.setAttribute("pageModel", page);
		request.setAttribute("nameOrEmailOrMobile", nameOrEmailOrMobile);
		return new ActionForward.Forward("/jsp/user/invite_import_contact.jsp");
	}
	
	/**
	 * 主持人邀请参会人
	 * @param data 被邀请人信息(用户id，姓名，邮箱，电话)列表，分两种情况：
	 * 1.(用户id，姓名，邮箱，电话)，用户id为空，非本站用户
	 * 2.(用户id，姓名，邮箱，电话)，用户id不为空，本站用户
	 * @param confId
	 * wangyong
	 * 2013-3-5
	 */
	@AsController(path = "inviteParticipants")
	public boolean inviteParticipants(@CParam("data") String data, @CParam("confId") Integer confId, @CParam("cycleId") Integer cycleId, HttpServletRequest request){
		Object[] inviteUser = JsonUtil.parseObjectArrWithJsonString(data);
		List<UserBase> participantsList = new ArrayList<UserBase>();
		for(Object user:inviteUser){
			UserBase userBase = (UserBase) JsonUtil.parseObjectWithJsonString(user.toString(), UserBase.class);
			participantsList.add(userBase);
		}
		boolean participantsFlag = false;
		int participantsStatus = ConstantUtil.INVITE_CONF_USER_FAIL;
		UserBase currentUser = userService.getCurrentUser(request);
		//判断当前企业用户是否有权限邀请参会人
		boolean userRole = confService.inviteConfAuthority(confId, currentUser);
		if(userRole){
			participantsFlag = confService.inviteParticipants(confId, cycleId, participantsList, currentUser);
			if(participantsFlag){
				participantsStatus = ConstantUtil.INVITE_CONF_USER_SUCCEED;
				setInfoMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.conf.invite.success"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_INVITE, "主持人邀请参会人成功",
						EventLogConstants.EVENTLOG_SECCEED, null, request);   //邀请成功后写EventLog
			}else{
				setErrMessage(request,  ResourceHolder.getInstance().getResource("bizconf.jsp.user.conf.invite.failed"));
				eventLogService.saveUserEventLog(currentUser, 
						EventLogConstants.SITEUSER_CONF_INVITE, "主持人邀请参会人失败",
						EventLogConstants.EVENTLOG_FAIL, null, request);   //邀请失败后写EventLog
			}
		}else{
			setErrMessage(request,  ResourceHolder.getInstance().getResource("bizconf.jsp.user.conf.invite.not"));
			eventLogService.saveUserEventLog(currentUser, 
					EventLogConstants.SITEUSER_CONF_INVITE, "当前企业用户无权限邀请参会人",
					EventLogConstants.EVENTLOG_FAIL, null, request);   //邀请失败后写EventLog
		}
		return participantsFlag;
	}
	
	/**
	 * 通过会议ID获取前几个邀请人信息
	 * wangyong
	 * 2013-3-14
	 */
	@AsController(path = "getConfInviteUser")
	public Object getConfInviteUser(@CParam("confId") Integer confId, HttpServletRequest request){
		List<ConfUser> userBaseList = null;
		userBaseList = confService.getConfInviteUser(confId);
		userBaseList = (userBaseList==null)?new ArrayList<ConfUser>():userBaseList;
		JSONArray jsonArray = new JSONArray();
		String name = "";
		for (Iterator<ConfUser> it = userBaseList.iterator(); it.hasNext();) {
			ConfUser user = (ConfUser) it.next();
			JSONObject obj = new JSONObject();
			//先取用户名 如果没有输入则依次取邮箱，电话
			if(user.getUserName()!=null && !user.getUserName().trim().equals("")){
				name = user.getUserName();
			}else if(user.getUserEmail()!=null && !user.getUserEmail().trim().equals("")){
				name = user.getUserEmail();
			}else if(user.getTelephone()!=null && !user.getTelephone().trim().equals("")){
				name = user.getTelephone();
			}else{
				name = "unknow";
			}
			obj.put("name", name);
			jsonArray.add(obj);
		}
		return jsonArray.toString();
	}
	
	/**
	 * 创建会议成功后
	 * wangyong
	 * 2013-3-14
	 */
	@AsController(path = "returnNewConf/{confId:([0-9]+)}")
	@Get
	public Object returnNewConf(@CParam("confId") Integer confId, HttpServletRequest request){
		SiteBase currentSite = siteService.getCurrentSiteBaseByUserLogin(request);
		UserBase currentUser = userService.getCurrentUser(request);
//		long offset = 0 ;
//		if(currentSite != null){
//			offset = currentSite.getTimeZone();
//		}else{
//			offset = DateUtil.getDateOffset();
//		}
		ConfBase confBase = confService.getConfBasebyConfId(confId, currentUser);
//		Date dashboardDays = DateUtil.addDate(DateUtil.getGmtDateByTimeZone(null, (int)offset), ConfConstant.CONF_LIST_DASHBOARD_DAYS.intValue());
//		Date confStartTime = DateUtil.getGmtDateByTimeZone(confBase.getStartTime(), (int)offset);
//		if(confStartTime.before(dashboardDays) && confStartTime.after(DateUtil.getGmtDateByTimeZone(null, (int)offset))){
			int rows = confService.countUpcomingConfList(null, currentUser, ConfConstant.CONF_LIST_DASHBOARD_DAYS);
			ConfCycle cycle = confLogic.getConfCycleByConf(confBase);
			
			request.setAttribute("cycle", cycle);
			request.setAttribute("UpcomingConfRows", rows);
			request.setAttribute("newConfBase", confBase);
			
			return new ActionForward.Forward("/jsp/user/conf_list_index_newdata.jsp");
//		}
//		return null;
	}
	
	@AsController(path = "addReminds")
	public Object addReminds(@CParam("confId") Integer confId,@CParam("userName") String userName,
			@CParam("email") String email, HttpServletRequest request){
		boolean flag = false;
		Integer status = ConstantUtil.GLOBAL_FAIL_FLAG;
		String msg = ResourceHolder.getInstance().getResource("bizconf.jsp.user.reminder.email.failed");
		UserBase currUser = userService.getCurrentUser(request);
		ConfBase currConf = confService.getConfBasebyConfId(confId);
		if(currUser!=null && currConf!=null){
			flag = emailService.sendConfRemindEmail(currUser, currConf);
		}else{
			if(email==null) throw new RuntimeException("email can not be null!");
			if(userName==null || userName.equals("")) userName = email.substring(0,email.indexOf("@"));
			flag = emailService.remindEmailForAnyOne(userName,email,currConf);
		}
		
		if(flag){
			status = ConstantUtil.GLOBAL_SUCCEED_FLAG;
			msg = ResourceHolder.getInstance().getResource("bizconf.jsp.user.reminder.email.success");
		}
		return StringUtil.returnJsonStr(status, msg);
	}
	
	/**
	 * 显示参会人列表预留
	 * @param confId
	 * @param keyword
	 * @param pageNo
	 * @param request
	 * @return
	 */
	@AsController(path = "showUsers")
	public Object showConfUsers(@CParam("confId") Integer confId,@CParam("keyword") String keyword,
			@CParam("pageNo") Integer pageNo,HttpServletRequest request){
		 
		PageBean<ConfLog> page = confService.getConflogsByConf(confId, pageNo, 10);
		request.setAttribute("pageModel", page);
		return new ActionForward.Forward("/jsp/user/conf_log_list.jsp");
	}
	
	/**
	 * 调用service保存会议完成失败后，获取失败错误信息
	 * wangyong
	 * 2013-3-15
	 */
	private String getConfErrorMessage(ConfBase confBase){
		String errorMessage = "";
		if(confBase.getId() != null && confBase.getId().intValue() < 0){
			errorMessage = ResourceHolder.getInstance().getResource("conf.create.error." + confBase.getId().intValue());
		}
		return errorMessage;
	}
	
	/**
	 * 返回json字符串对象(创建，修改会议出错时)
	 * status 失败
	 * object 失败原因
	 * wangyong
	 * 2013-1-30
	 */
	private String returnJsonStr(int status, Object object){
		JSONObject json = new JSONObject();
		json.put("status", status);
		json.put("message", object.toString());
		return json.toString();
	}
	
	/**
	 * 获取每个会议的参会人个数
	 * wangyong
	 * 2013-3-25
	 */
	private Map<Integer,Integer> getParticipants(List<ConfBase> confList){
		Map<Integer,Integer> ParticipantsMap = new HashMap<Integer, Integer>();
		if(confList != null && confList.size() > 0){
			for(ConfBase conf:confList){
				List<ConfUser> Participants = confUserService.getConfInviteUserList(conf.getId());
				ParticipantsMap.put(conf.getId(), Participants == null ? 0 : Participants.size());
			}
		}
		return ParticipantsMap;
	}
	
	/**
	 * 此方法需要
	 * @param map
	 * @param confs
	 */
	private void setPartcipents(Map<String, JSONArray> map,List<ConfBase> confs){
		
		for (Iterator<ConfBase> it = confs.iterator(); it.hasNext();) {
			ConfBase conf = it.next();
			List<ConfUser> userBaseList = confService.getConfInviteUser(conf.getId());
			userBaseList = (userBaseList==null)?new ArrayList<ConfUser>():userBaseList;
			JSONArray jsonArray = new JSONArray();
			for (Iterator<ConfUser> itr = userBaseList.iterator(); it.hasNext();) {
				ConfUser user =  itr.next();
				JSONObject obj = new JSONObject();
				obj.put("name", user.getUserName()==null?"unknow":user.getUserName());
				jsonArray.add(obj);
			}
			map.put(conf.getId().toString(), jsonArray);
		}
		
	}
	
	/**
	 * 未登录，转到公开会议
	 * @param request
	 * @return
	 */
	@AsController(path = "getPublicControlPadIndex")
	public Object getPublicControlPadIndex(HttpServletRequest request){
		boolean isLogined = loginService.isLogined(request);
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		Date curDate=currentSite.getSiteLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("isLogined", isLogined); 
		return new ActionForward.Forward("/jsp/user/conf_list.jsp");
	}	
	
	/**
	 * 登录后，转到我的会议
	 * @param request
	 * @return
	 */
	@AsController(path = "getControlPadIndex")
	@Interceptors({UserInterceptor.class})
	public Object getControlPadIndex(HttpServletRequest request){
		boolean isLogined = loginService.isLogined(request);
		if(isLogined){
			UserBase currentUser = userService.getCurrentUser(request);
			if(currentUser != null){
				Date curDate = currentUser.getUserLocalTime();			
				request.setAttribute("defaultDate", curDate);		//当前用户喜好设置时区的时间
				request.setAttribute("user", currentUser); 
			}
			int userRole = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("userRole")).intValue();              //角色：1.我主持的；2.我参与的（邀请我参加的）
			request.setAttribute("userRole", userRole);
			//int dateScopeFlag = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("dateScopeFlag")).intValue();    //时间范围：1.今天；2.本周；3.本月；4.所有
			//request.setAttribute("dateScopeFlag", dateScopeFlag);
		} 	
		request.setAttribute("isLogined", isLogined); 
		return new ActionForward.Forward("/jsp/user/conf_list.jsp");
	}	
	
	/**
	 * 新控制面板查询会议
	 * 一、查询我主持的会议，包括以下8种：
	 *  1、今天正在进行的
		2、今天即将开始的
		3、今天我参加过的 
		4、本周即将开始的
		5、本周我参加过的
		6、本月即将开始的
		7、本月我参加过的
		8、所有即将开始的
	 * 二、查询我参与的会议（邀请我参加的，我作为参会人的会议），包括以下8种：
	 * 	1、今天正在进行的
		2、今天即将开始的
		3、今天我参加过的 
		4、本周即将开始的
		5、本周我参加过的
		6、本月即将开始的
		7、本月我参加过的
		8、所有即将开始的
	 * wangyong
	 * 2013-3-8
	 */
	@AsController(path = "getControlPadConf")
	@Interceptors({UserInterceptor.class})
	public Object getControlPadConf(HttpServletRequest request){
//		if (!loginService.isLogined(request)) {
//			return new ActionForward.Redirect("/user/conf/getControlPadPublicConf");
//		}
		UserBase currentUser = userService.getCurrentUser(request);
		int userRole = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("userRole")).intValue();              //角色：1.我主持的；2.我参与的（邀请我参加的）
		int dateScopeFlag = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("dateScopeFlag")).intValue();    //时间范围：1.今天；2.本周；3.本月；4.所有
		int pageNo = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("pageNo")).intValue();
		String confName = request.getParameter("confName");
		if(StringUtil.isNotBlank(confName)) {
			try {
				confName = URLDecoder.decode(request.getParameter("confName"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("我的会议搜索转码错误"+e);
			}			
		}
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		Date beginDate = null;
		Date endDate = null;
		if(StringUtil.isNotBlank(beginTime)){
			beginDate = DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(beginTime, null), currentUser.getTimeZone());
		}
		if(StringUtil.isNotBlank(endTime)){
			endDate = DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(endTime, null), currentUser.getTimeZone());
		}
		//永久会议
		PageBean<ConfBase> permanentConfs = null;
		if(userRole == ConfConstant.CONF_USER_HOST || userRole == 0){
			getHostConf(confName, dateScopeFlag, beginDate, endDate, pageNo, request, currentUser);
			
		}else if(userRole == ConfConstant.CONF_USER_PARTICIPANT){
			getParticipantConf(confName, dateScopeFlag, beginDate, endDate, pageNo, request, currentUser);
		}
		
		request.setAttribute("permanentConfs", permanentConfs);
		//当前用户喜好设置时区的时间
		Date curDate = currentUser.getUserLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("user", currentUser); 
		request.setAttribute("isLogined", loginService.isLogined(request));
		return new ActionForward.Forward("/jsp/user/conf_list_pad.jsp");
	}
	
	/**
	 * 新控制面板查询会议:查询我主持的会议
	 * wangyong
	 * 2013-4-22
	 */
	private HttpServletRequest getHostConf(String confName, int dateScopeFlag, Date beginTime, Date endTime, int pageNo, HttpServletRequest request, UserBase currentUser){
		List<ConfBase> dringConfList = new ArrayList<ConfBase>();
		List<ConfBase> upcomingConfList = new ArrayList<ConfBase>();
		List<ConfBase> attendedConfList = new ArrayList<ConfBase>();
//		dringConfList = confService.getDailyOpeningConfListForHost(confName, currentUser, pageNo).getDatas();
		dringConfList = confService.getFullOpeningConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
		if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_TODAY || dateScopeFlag == 0){      //今天
			upcomingConfList = confService.getDailyComingConfListForHost(confName, currentUser, pageNo).getDatas();
			attendedConfList = confService.getDailyJoinedConfListForHost(confName, currentUser, pageNo).getDatas();
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_WEEK){   //本周
			upcomingConfList = confService.getWeeklyComingConfListForHost(confName, currentUser, pageNo).getDatas();
			attendedConfList = confService.getWeeklyJoinedConfListForHost(confName, currentUser, pageNo).getDatas();
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_MONTH){   //本月
			upcomingConfList = confService.getMonthlyComingConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			attendedConfList = confService.getMonthlyJoinedConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_ANY){     //所有即将开始的
			upcomingConfList = confService.getFullComingConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			attendedConfList = confService.getFullJoinedConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
		}
		request.setAttribute("dringConfList", dringConfList);
		request.setAttribute("upcomingConfList", upcomingConfList);
		request.setAttribute("attendedConfList", attendedConfList);
		//获取会议周期
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(dringConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(upcomingConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(attendedConfList));
		request.setAttribute("cycs", cycs);
		return request;
	}
	
	/**
	 * 新控制面板查询会议:查询我参与的会议（邀请我参加的，我作为参会人的会议）
	 * wangyong
	 * 2013-4-22
	 */
	private HttpServletRequest getParticipantConf(String confName, int dateScopeFlag, Date beginTime, Date endTime, int pageNo, HttpServletRequest request, UserBase currentUser){
		List<ConfBase> dringConfList = new ArrayList<ConfBase>();
		List<ConfBase> upcomingConfList = new ArrayList<ConfBase>();
		List<ConfBase> attendedConfList = new ArrayList<ConfBase>();
//		dringConfList = confService.getDailyOpeningConfListForActor(confName, currentUser, pageNo).getDatas();
		dringConfList = confService.getFullOpeningConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
		if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_TODAY || dateScopeFlag == 0){      //今天
			upcomingConfList = confService.getDailyComingConfListForActor(confName, currentUser, pageNo).getDatas();
			attendedConfList = confService.getDailyJoinedConfListForActor(confName, currentUser, pageNo).getDatas();
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_WEEK){   //本周
			upcomingConfList = confService.getWeeklyComingConfListForActor(confName, currentUser, pageNo).getDatas();
			attendedConfList = confService.getWeeklyJoinedConfListForActor(confName, currentUser, pageNo).getDatas();
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_MONTH){   //本月
			upcomingConfList = confService.getMonthlyComingConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			attendedConfList = confService.getMonthlyJoinedConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_ANY){     //所有即将开始的
			upcomingConfList = confService.getFullComingConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			attendedConfList = confService.getFullJoinedConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
		}
		request.setAttribute("dringConfList", dringConfList);
		request.setAttribute("upcomingConfList", upcomingConfList);
		request.setAttribute("attendedConfList", attendedConfList);
		//获取会议周期
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(dringConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(upcomingConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(attendedConfList));
		request.setAttribute("cycs", cycs);
		return request;
	}
	
	/**
	 * 新控制面板查询会议，获取更多信息
	 * 1.有更多会议信息时，返回一个jsp
	 * 2.无更多会议信息时，返回json（status,message）
	 * wangyong
	 * 2013-4-22
	 */
	@AsController(path = "getMoreControlPadConf")
	public Object getMoreControlPadConf(HttpServletRequest request){
		List<ConfBase> confList = null;
		int userRole = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("userRole")).intValue();              //角色：1.我主持的；2.我参与的（邀请我参加的）
		int dateScopeFlag = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("dateScopeFlag")).intValue();    //时间范围：1.今天；2.本周；3.本月；4.所有
		int confStatus = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("confStatus")).intValue();    	  //会议状态：0.所有状态；1.正在进行的；2.即将开始的；3.参加过的
		int pageNo = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("pageNo")).intValue();
		String confName = request.getParameter("confName");
		if(StringUtil.isNotBlank(confName)) {
			try {
				confName = URLDecoder.decode(request.getParameter("confName"),"utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("我的会议搜索转码错误"+e);
			}			
		}
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		Date beginDate = null;
		Date endDate = null;
		UserBase currentUser = userService.getCurrentUser(request);
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		Integer timeZone = currentSite.getTimeZone();
		if(currentUser != null){
			timeZone = currentUser.getTimeZone();
		}
		if(StringUtil.isNotBlank(beginTime)){
			beginDate = DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(beginTime, null), timeZone);
		}
		if(StringUtil.isNotBlank(endTime)){
			endDate = DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(endTime, null), timeZone);
		}
		if (loginService.isLogined(request)) {
			if(userRole == ConfConstant.CONF_USER_HOST || userRole == 0){
				confList = getMoreHostConf(confName, dateScopeFlag, confStatus, beginDate, endDate, pageNo, request, currentUser);
			}else if(userRole == ConfConstant.CONF_USER_PARTICIPANT){
				confList = getMoreParticipantConf(confName, dateScopeFlag, confStatus, beginDate, endDate, pageNo, request, currentUser);
			}
		}else{
			confList = getPublicMoreConf(confName, confStatus, beginDate, endDate, pageNo, request, currentSite);
		}
		
//		//当前用户喜好设置时区的时间
		if(currentUser!=null){
			Date curDate = currentUser.getUserLocalTime();
			request.setAttribute("defaultDate", curDate);
			request.setAttribute("user", currentUser); 
		}
		if(confList != null && confList.size() > 0){
			request.setAttribute("confList", confList);
			return new ActionForward.Forward("/jsp/user/conf_list_index_more.jsp");
		}
		return returnJsonStr(ConstantUtil.GLOBAL_FAIL_FLAG, ResourceHolder.getInstance().getResource("bizconf.jsp.user.conf.nomore"));
	}
	
	/**
	 * 新控制面板查询会议,获取更多信息:查询我主持的会议
	 * wangyong
	 * 2013-4-22
	 */
	private List<ConfBase> getMoreHostConf(String confName, int dateScopeFlag, int confStatus, Date beginTime, Date endTime, int pageNo, HttpServletRequest request, UserBase currentUser){
		List<ConfBase> confList = null;
		if(confStatus == ConfConstant.CONF_PAD_STATUS_OPENING){
			confList = confService.getDailyOpeningConfListForHost(confName, currentUser, pageNo).getDatas();
		}
		if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_TODAY || dateScopeFlag == 0){      //今天
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getDailyComingConfListForHost(confName, currentUser, pageNo).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getDailyJoinedConfListForHost(confName, currentUser, pageNo).getDatas();
			}
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_WEEK){   //本周
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getWeeklyComingConfListForHost(confName, currentUser, pageNo).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getWeeklyJoinedConfListForHost(confName, currentUser, pageNo).getDatas();
			}
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_MONTH){   //本月
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getMonthlyComingConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getMonthlyJoinedConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_ANY){     //所有
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getFullComingConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getFullJoinedConfListForHost(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
		}
		//获取会议周期
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(confList));
		request.setAttribute("cycs", cycs);
		return confList;
	}
	
	/**
	 * 新控制面板查询会议,获取更多信息:查询我 参与的会议（邀请我参加的）
	 * wangyong
	 * 2013-4-22
	 */
	private List<ConfBase> getMoreParticipantConf(String confName, int dateScopeFlag, int confStatus, Date beginTime, Date endTime, int pageNo, HttpServletRequest request, UserBase currentUser){
		List<ConfBase> confList = null;
		if(confStatus == ConfConstant.CONF_PAD_STATUS_OPENING){
			confList = confService.getDailyOpeningConfListForActor(confName, currentUser, pageNo).getDatas();
		}
		if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_TODAY || dateScopeFlag == 0){      //今天
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getDailyComingConfListForActor(confName, currentUser, pageNo).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getDailyJoinedConfListForActor(confName, currentUser, pageNo).getDatas();
			}
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_WEEK){   //本周
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getWeeklyComingConfListForActor(confName, currentUser, pageNo).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getWeeklyJoinedConfListForActor(confName, currentUser, pageNo).getDatas();
			}
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_MONTH){   //本月
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getMonthlyComingConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getMonthlyJoinedConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
		}else if(dateScopeFlag == ConfConstant.CONF_DATE_SCOPE_FLAG_ANY){     //所有即将开始的
			if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
				confList = confService.getFullComingConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
			if(confStatus == ConfConstant.CONF_PAD_STATUS_ATTENDED){
				confList = confService.getFullJoinedConfListForActor(confName, currentUser, pageNo, beginTime, endTime).getDatas();
			}
		}
		//获取会议周期
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(confList));
		request.setAttribute("cycs", cycs);
		return confList;
	}
	
	/**
	 * 未登录情况下查询公开会议
	 * wangyong
	 * 2013-4-23
	 * @throws Exception 
	 */
	@AsController(path = "getControlPadPublicConf")
	public Object getControlPadPublicConf(HttpServletRequest request) throws Exception{
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		int pageNo = IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("pageNo")).intValue();
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		Date beginDate = null;
		Date endDate = null;
		String confName = request.getParameter("confName");
		if(StringUtil.isNotBlank(beginTime)){
			beginDate = DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(beginTime, null), currentSite.getTimeZone());
		}
		if(StringUtil.isNotBlank(endTime)){
			endDate = DateUtil.getGmtDateByTimeZone(DateUtil.StringToDate(endTime, null), currentSite.getTimeZone());
		}
		if(confName!=null && !confName.equals("")){
			confName = URLDecoder.decode(confName, "UTF-8").trim();
		}
		getPublicConf(confName, beginDate, endDate, pageNo, request, currentSite);
		//当前站点时区的时间
		Date curDate = currentSite.getSiteLocalTime();
		request.setAttribute("defaultDate", curDate);
		request.setAttribute("isLogined", loginService.isLogined(request));
		return new ActionForward.Forward("/jsp/user/conf_list_pad.jsp");
	}
	
	private HttpServletRequest getPublicConf(String confName, Date beginTime, Date endTime, int pageNo, HttpServletRequest request, SiteBase currentSite){
		List<ConfBase> dringConfList = new ArrayList<ConfBase>();
		List<ConfBase> upcomingConfList = new ArrayList<ConfBase>();
		dringConfList = publicConfService.getOpeningConfList(confName, currentSite, pageNo, beginTime, endTime).getDatas();
		upcomingConfList = publicConfService.getUpComingConfList(confName, currentSite, pageNo, beginTime, endTime).getDatas();
		request.setAttribute("dringConfList", dringConfList);
		request.setAttribute("upcomingConfList", upcomingConfList);
		//获取会议周期
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(dringConfList));
		cycs.addAll(confLogic.getConfCyclesByConf(upcomingConfList));
		request.setAttribute("cycs", cycs);
		return request;
	}
	
	private List<ConfBase> getPublicMoreConf(String confName, int confStatus, Date beginTime, Date endTime, int pageNo, HttpServletRequest request, SiteBase currentSite){
		List<ConfBase> confList = null;
		if(confStatus == ConfConstant.CONF_PAD_STATUS_OPENING){
			confList = publicConfService.getOpeningConfList(confName, currentSite, pageNo, beginTime, endTime).getDatas();
		}
		if(confStatus == ConfConstant.CONF_PAD_STATUS_COMING){
			confList = publicConfService.getUpComingConfList(confName, currentSite, pageNo, beginTime, endTime).getDatas();
		}
		//获取会议周期
		Set<ConfCycle> cycs = new HashSet<ConfCycle>();
		cycs.addAll(confLogic.getConfCyclesByConf(confList));
		request.setAttribute("cycs", cycs);
		return confList;
	}

}
