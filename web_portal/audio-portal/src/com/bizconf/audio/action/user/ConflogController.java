package com.bizconf.audio.action.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.MyfnTag;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ConfLogService;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.ExcelUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 会议日志controller
 * @author martin
 * 2013-04-25
 *
 */
@ReqPath("conflog")
public class ConflogController extends BaseController{
	private final Logger logger = Logger.getLogger(ConflogController.class);
	
	/**
	 * 获取企业联系人群组列表
	 * @param keyword
	 * @param pageNo
	 * @param request
	 * @return
	 */
	@Autowired
	private UserService userService; 
	@Autowired
	private ConfService confService;
	@Autowired
	private ConfLogService confLogService;
	@Autowired
	private ConfManagementService confManagementService;
	@Autowired
	private SiteService siteService;
	
	@AsController(path = "list")
	public Object logsList(@CParam("keyword") String keyword,@CParam("userId") Integer userId, 
			@CParam("pageNo")Integer pageNo,@CParam("isCreator")boolean isCreator, 
			@CParam("theme")String theme,
			HttpServletRequest request)throws Exception{
		
		UserBase currUser = userService.getCurrentUser(request);
		//获取时间区间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = null;
		Date endTime = null;
		String stime = request.getParameter("startTime");
		String etime = request.getParameter("endTime");
		if(stime!=null && !stime.equals("")){
			startTime = sdf.parse(stime);
		}
		if(etime!=null && !etime.equals("")){
			endTime = sdf.parse(etime);
		}
		if(startTime==null || endTime==null){
			startTime = DateUtil.getMonthStartDate(currUser.getTimeZone().longValue());
			endTime = DateUtil.getMonthEndDate(startTime);
		}
		//2013.6.24 因客户需求新加常量，部分每页展示用户偏好设置每页显示条数
		PageBean<ConfBase> page = confService.getConfBasePage(pageNo, currUser.getPageSize(), currUser, startTime,endTime,theme,isCreator);    
		String forward = "/jsp/user/hostConfloglist.jsp";
		if(!isCreator){
			//用户的入会，出会时间
			Map<Integer, ConfLog> clMap = confLogService.getConfLogDataMap(page==null?null:page.getDatas(), currUser.getId());
			//我参加的导出  需求变动不需添加
//			if(isExport){
//				exportForParticipant(response, page.getDatas(), clMap, currUser.getTimeZone());
//				return null;
//			}
			request.setAttribute("cls", clMap);
			forward = "/jsp/user/ptcConfloglist.jsp";
		}
		//我主持的导出 
//		else if(isExport){
//			int count  = confLogService.countConfLogsByConfs(page.getDatas());
//			if(count>10000){
//				request.setAttribute("errorinfo", "入会记录数量过大！");
//			}else{
//				
//				return null;
//			}
//		}
		//参会人次
		Map<Integer, Integer> numMap = confLogService.getConflogNumByConf(page==null?null:page.getDatas());
		request.setAttribute("currUser", currUser);
		request.setAttribute("numMap", numMap);
		request.setAttribute("pageModel", page);
		
		startTime = DateUtil.getOffsetDateByGmtDate(startTime, currUser.getTimeZone().longValue());
		endTime = DateUtil.getOffsetDateByGmtDate(endTime, currUser.getTimeZone().longValue());
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		
		return new ActionForward.Forward(forward);
	}
	
	@AsController(path = "attendConflist")
	public Object logsList(@CParam("keyword") String keyword,@CParam("userId") Integer userId, @CParam("pageNo")Integer pageNo, HttpServletRequest request){
		UserBase currUser = null;
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		SiteBase site = siteService.getSiteBaseById(currentSiteAdmin.getSiteId());
		int pageSize = ConstantUtil.PAGESIZE_DEFAULT;
		if(currentSiteAdmin != null && currentSiteAdmin.getId() != null){
			pageSize = currentSiteAdmin.getPageSize();
		}
		//企业管理员查看使用
		if(userId!=null && userId.intValue()>0){
			currUser = userService.getUserBaseById(userId);
		}
		
		long timezone = DateUtil.BJ_TIME_OFFSET;
		if(site!=null){
			timezone = site.getTimeZone();
		}
//		PageBean<ConfBase> page = confService.getConfBasePage(pageNo, ConstantUtil.PAGESIZE_DEFAULT, currUser, false);
		//2013.6.24 因客户需求新加常量，部分每页展示用户偏好设置每页显示条数
		PageBean<ConfBase> page = confService.getConfBasePage(pageNo, pageSize, currUser,null,null,null,false);
		//用户的入会，出会时间
		Map<Integer, ConfLog> clMap = confLogService.getConfLogDataMap(page==null?null:page.getDatas(), currUser.getId());
		request.setAttribute("cls", clMap);
		//参会人次
		Map<Integer, Integer> numMap = confLogService.getConflogNumByConf(page==null?null:page.getDatas());
		request.setAttribute("currUser", currUser);
		request.setAttribute("timezone", timezone);
		request.setAttribute("numMap", numMap);
		request.setAttribute("pageModel", page);
		return new ActionForward.Forward("/jsp/user/attendConfloglist.jsp");
	}
	
	@AsController(path = "loglist")
	public Object detilList(@CParam("userPage") boolean userPage,@CParam("issys") boolean issys,@CParam("confId") Integer confId,
			@CParam("sortField") String sortField,@CParam("sortRule") String sortRule,
			@CParam("pageNo")Integer pageNo, HttpServletRequest request){
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		UserBase currUser = userService.getCurrentUser(request);
		//2013.6.24 因客户需求新加常量，部分每页展示用户偏好设置每页显示条数
		int pageSize = ConstantUtil.PAGESIZE_DEFAULT;
		if(currUser != null && currUser.getId() != null){
			pageSize = currUser.getPageSize();
		}else if(currentSiteAdmin != null && currentSiteAdmin.getId() != null){
			pageSize = currentSiteAdmin.getPageSize();
		}
		ConfBase conf = confService.getConfBasebyConfId(confId);
		SiteBase confSite = siteService.getSiteBaseById(conf.getSiteId());
		//获取会议时区
		Integer timezone = conf.getTimeZone();
		if(timezone==null) timezone = confSite.getTimeZone();
		if(timezone==null) timezone = 28800000;
		
		PageBean<ConfLog> page = null;
		if(ConfConstant.CONF_STATUS_FINISHED.equals(conf.getConfStatus())){
			page = confLogService.getLogsByConf(confId, pageSize, pageNo,sortField,sortRule);
		}else if(ConfConstant.CONF_STATUS_OPENING.equals(conf.getConfStatus())){
			if(!conf.getPermanentConf().equals(1)){
				page = confManagementService.queryConfUserStatusForPage(conf.getConfHwid(), pageNo, pageSize, confSite, null);
			}else{
				conf = confService.getPermanentChildConf(confId);
				if(conf!=null){
					page = confManagementService.queryConfUserStatusForPage(conf.getConfHwid(), pageNo, pageSize, confSite, null);
				}
			}
		}
		
		request.setAttribute("conf", conf);
		request.setAttribute("sortField", sortField);
		request.setAttribute("sortRule", sortRule);
		request.setAttribute("pageModel", page);
		request.setAttribute("timezone", timezone);
		request.setAttribute("confId", confId);
		String forword = "/jsp/user/conflogs.jsp";
		if(userPage){
			if(issys){
				timezone = 28800000;
			}else{
				timezone = confSite.getTimeZone();
			}
			forword = "/jsp/user/joinConfloglist.jsp";
		}
		return new ActionForward.Forward(forword);
	}
	
	@AsController(path = "exportLogs")
	public void export(@CParam("keyword") String keyword,@CParam("confId") Integer confId,
			@CParam("sortField") String sortField,@CParam("sortRule") String sortRule,
			HttpServletRequest request,HttpServletResponse response){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ConfBase conf = confService.getConfBasebyConfId(confId);
		//添加数据信息
		List<ConfLog> logs = null;
		
		String timezonedesc = "";
		Integer timezone = conf.getTimeZone();
		timezonedesc = conf.getTimeZoneDesc();
		if(conf!=null){
			SiteBase confSite = siteService.getSiteBaseById(conf.getSiteId());
			if(timezone==null){
				timezone = confSite.getTimeZone();
				timezonedesc = confSite.getTimeZoneDesc();
			} 
			if(ConfConstant.CONF_STATUS_FINISHED.equals(conf.getConfStatus())){
				logs = confLogService.getAllLogsByConf(confId,sortField,sortRule);
			}else if(ConfConstant.CONF_STATUS_OPENING.equals(conf.getConfStatus())){
				logs = confManagementService.queryConfUserStatusForPage(conf.getConfHwid(), 1, 3000, confSite, null).getDatas();
			}
		}
		if(timezone==null){ 
			timezone = (int)DateUtil.BJ_TIME_OFFSET; 
			timezonedesc = ResourceHolder.getInstance().getResource("website.timezone.city.44");
		}
		List<Object[]> objlist = new ArrayList<Object[]>();
		
		Object[] title_timezone = new Object[1];
		title_timezone[0] = ResourceHolder.getInstance().getResource("bizconf.jsp.site.confexport.timzzoneinfo")+timezonedesc
					+ResourceHolder.getInstance().getResource("website.message.time");
		objlist.add(title_timezone);
		Object[] title_subject = new Object[1];
		
		title_subject[0] = "\""+conf.getConfName()+"\""+ResourceHolder.getInstance().getResource("admin.site.userimport.accdetail");
		objlist.add(title_subject);
		
		Object[] titles = new Object[5];
		titles[0] =  ResourceHolder.getInstance().getResource("site.admin.edituser.role2");//"参加者";//
		titles[1] = ResourceHolder.getInstance().getResource("bizconf.jsp.add_contacts.res7"); //"用户名";//
		titles[2] = ResourceHolder.getInstance().getResource("bizconf.jsp.conflogs.res2");//"用户类型";//
		titles[3] = ResourceHolder.getInstance().getResource("bizconf.jsp.attendConfloglist.res8");//"加入时间";//
		titles[4] = ResourceHolder.getInstance().getResource("bizconf.jsp.attendConfloglist.res9");//"退出时间";
		objlist.add(titles);//添加头信息
		
		int index = 1;
		if(logs!=null && logs.size()>0){
			for (Iterator<ConfLog> tir = logs.iterator(); tir.hasNext();) {
				ConfLog log =  tir.next();
				Object[] logdata = new Object[5];
				logdata[0] = index++;
				logdata[1] = log.getUserName();//
				if(ConfConstant.CONF_USER_TERM_TYPE_MOBILE == log.getTermType().intValue()){
					logdata[2] = ResourceHolder.getInstance().getResource("bizconf.jsp.conflogs.res4");//"退出时间";
				}else if(ConfConstant.CONF_USER_TERM_TYPE_PC == log.getTermType().intValue()){
					logdata[2] = ResourceHolder.getInstance().getResource("bizconf.jsp.conflogs.res3");//"退出时间";
				}else{
					logdata[2] = "未知";//
				}
				logdata[3] = sdf.format(DateUtil.getOffsetDateByGmtDate(log.getJoinTime(), timezone.longValue()));//
				logdata[4] = "--";
				if(log.getExitTime()!=null){
					logdata[4] = sdf.format(DateUtil.getOffsetDateByGmtDate(log.getExitTime(), timezone.longValue()));
				}
				objlist.add(logdata);
			}
		}
		HSSFWorkbook wb = ExcelUtil.createExcelWorkbook("users", objlist);
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=join_detail.xls");
        try {
        	wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			objlist = null;
			wb = null;
		}
	}
	
//	private void setConfPageDate(PageBean<ConfBase> page,Integer timezone){
//		
//		if(page!=null && page.getDatas()!=null){
//			List<ConfBase> confs = page.getDatas();
//			for (Iterator<ConfBase> itr = confs.iterator(); itr.hasNext();) {
//				ConfBase conf = itr.next();
//				if(timezone==null)timezone = conf.getTimeZone();
//				if(timezone==null)timezone = 2;
//				conf.setStartTime(DateUtil.getOffsetDateByGmtDate(conf.getStartTime(),timezone.longValue()));
//			}
//		}
//	}
	
	
	private void exportForParticipant(HttpServletResponse response,List<ConfBase> confs,
			Map<Integer, ConfLog> clMap,Integer timezone){
		if(timezone==null){
			timezone = 28800000;
		}
		response.reset();
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=join_detail.xls");
        List<Object[]> objlist = new ArrayList<Object[]>();
		Object[] titles = new Object[6];
		titles[0] = "会议主题";//
		titles[1] = "主持人";//
		titles[2] = "开始时间";//
		titles[3] = "结束时间";
		titles[4] = "加入时间";
		titles[5] = "结束时间";
		objlist.add(titles);//
		if(confs!=null && !confs.isEmpty()){
			for (Iterator it = confs.iterator(); it.hasNext();) {
				ConfBase conf = (ConfBase) it.next();
				Object[] dataline = new Object[6];
				dataline[0] = conf.getConfName();
				dataline[1] = conf.getCompereName();
				dataline[2] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", conf.getStartTime(), timezone);
				dataline[3] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", conf.getEndTime(), timezone);
				if(clMap.get(conf.getId())!=null){
					dataline[4] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", clMap.get(conf.getId()).getJoinTime(), timezone);
					dataline[5] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", clMap.get(conf.getId()).getExitTime(), timezone);
				}else{
					dataline[4]= "--";
					dataline[5]= "--";
				}
				objlist.add(dataline);
			}
		}
		HSSFWorkbook wb = ExcelUtil.createExcelWorkbook("users", objlist);
        try {
        	wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			objlist = null;
			wb = null;
		}
	}
	
	//主持人的导出功能
	private void exportForHost(HttpServletResponse response,List<ConfBase> confs,
			Integer timezone){
		//会议主题，主持人，开始时间，结束时间，用户名，用户类型，加入时间，退出时间
		if(timezone==null){
			timezone = 28800000;
		}
		response.reset();
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=conf_detail.xls");
        List<Object[]> objlist = new ArrayList<Object[]>();
		Object[] titles = new Object[8];
		titles[0] = "会议主题";//
		titles[1] = "主持人";//
		titles[2] = "开始时间";//
		titles[3] = "结束时间";
		titles[4] = "用户名";
		titles[5] = "用户类型";
		titles[6] = "加入时间";
		titles[7] = "退出时间";
		objlist.add(titles);//
		if(confs!=null && !confs.isEmpty()){
			for (Iterator it = confs.iterator(); it.hasNext();) {
				ConfBase conf = (ConfBase) it.next();
				List<ConfLog> logs = confLogService.getAllLogsByConf(conf.getId(),null,null);
				if(logs!=null && !logs.isEmpty()){
					Object[] dataline = new Object[8];
					dataline[0] = conf.getConfName();
					dataline[1] = conf.getCompereName();
					dataline[2] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", conf.getStartTime(), timezone);
					dataline[3] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", conf.getEndTime(), timezone);
				}
				
			}
		}
		HSSFWorkbook wb = ExcelUtil.createExcelWorkbook("users", objlist);
        try {
        	wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			objlist = null;
			wb = null;
		}
	}
	
//	private Object[] genObjects(){
//		
//	}
}
