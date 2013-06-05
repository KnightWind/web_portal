package com.bizconf.audio.action.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
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
import com.bizconf.audio.service.EnterpriseAdminService;
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
	public Object logsList(@CParam("keyword") String keyword,@CParam("userId") Integer userId, @CParam("pageNo")Integer pageNo,@CParam("isCreator")boolean isCreator, HttpServletRequest request){
		UserBase currUser = userService.getCurrentUser(request);
		
		PageBean<ConfBase> page = confService.getConfBasePage(pageNo, ConstantUtil.PAGESIZE_DEFAULT, currUser, isCreator);
		String forward = "/jsp/user/hostConfloglist.jsp";
		if(!isCreator){
			//用户的入会，出会时间
			Map<Integer, ConfLog> clMap = confLogService.getConfLogDataMap(page==null?null:page.getDatas(), currUser.getId());
			request.setAttribute("cls", clMap);
			forward = "/jsp/user/ptcConfloglist.jsp";
		}
		//参会人次
		Map<Integer, Integer> numMap = confLogService.getConflogNumByConf(page==null?null:page.getDatas());
		request.setAttribute("currUser", currUser);
		request.setAttribute("numMap", numMap);
		request.setAttribute("pageModel", page);
		
		return new ActionForward.Forward(forward);
	}
	
	@AsController(path = "attendConflist")
	public Object logsList(@CParam("keyword") String keyword,@CParam("userId") Integer userId, @CParam("pageNo")Integer pageNo, HttpServletRequest request){
		UserBase currUser = null;
		//企业管理员查看使用
		if(userId!=null && userId.intValue()>0){
			currUser = userService.getUserBaseById(userId);
		}
		PageBean<ConfBase> page = confService.getConfBasePage(pageNo, ConstantUtil.PAGESIZE_DEFAULT, currUser, false);
		//用户的入会，出会时间
		Map<Integer, ConfLog> clMap = confLogService.getConfLogDataMap(page==null?null:page.getDatas(), currUser.getId());
		request.setAttribute("cls", clMap);
		//参会人次
		Map<Integer, Integer> numMap = confLogService.getConflogNumByConf(page==null?null:page.getDatas());
		request.setAttribute("currUser", currUser);
		request.setAttribute("numMap", numMap);
		request.setAttribute("pageModel", page);
		return new ActionForward.Forward("/jsp/user/attendConfloglist.jsp");
	}
	
	@AsController(path = "loglist")
	public Object detilList(@CParam("userPage") boolean userPage,@CParam("confId") Integer confId, @CParam("pageNo")Integer pageNo, HttpServletRequest request){
		ConfBase conf = confService.getConfBasebyConfId(confId);
		SiteBase confSite = siteService.getSiteBaseById(conf.getSiteId());
		PageBean<ConfLog> page = null;
		if(ConfConstant.CONF_STATUS_FINISHED.equals(conf.getConfStatus())){
			page = confLogService.getLogsByConf(confId,pageNo);
		}else if(ConfConstant.CONF_STATUS_OPENING.equals(conf.getConfStatus())){
			page = confManagementService.queryConfUserStatusForPage(conf.getConfHwid(), pageNo, ConstantUtil.PAGESIZE_DEFAULT, confSite, null);
		}
		Integer timezone = conf.getTimeZone();
		if(timezone==null) timezone = confSite.getTimeZone();
		if(timezone==null) timezone = 28800000;
		
		request.setAttribute("pageModel", page);
		request.setAttribute("timezone", timezone);
		request.setAttribute("confId", confId);
		String forword = "/jsp/user/conflogs.jsp";
		if(userPage){
			forword = "/jsp/user/joinConfloglist.jsp";
		}
		return new ActionForward.Forward(forword);
	}
	
	@AsController(path = "exportLogs")
	public void export(@CParam("keyword") String keyword,@CParam("confId") Integer confId,HttpServletRequest request,HttpServletResponse response){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Object[]> objlist = new ArrayList<Object[]>();
		Object[] titles = new Object[4];
		titles[0] = "用户名";//
		titles[1] = "用户类型";//
		titles[2] = "加入时间";//
		titles[3] = "退出时间";
		objlist.add(titles);//添加头信息
		//添加数据信息
		List<ConfLog> logs = null;
		ConfBase conf = confService.getConfBasebyConfId(confId);
		
		Integer timezone = conf.getTimeZone();
		if(conf!=null){
			SiteBase confSite = siteService.getSiteBaseById(conf.getSiteId());
			if(timezone==null) timezone = confSite.getTimeZone();
			
			if(ConfConstant.CONF_STATUS_FINISHED.equals(conf.getConfStatus())){
				logs = confLogService.getAllLogsByConf(confId);
			}else if(ConfConstant.CONF_STATUS_OPENING.equals(conf.getConfStatus())){
				logs = confManagementService.queryConfUserStatusForPage(conf.getConfHwid(), 1, 3000, confSite, null).getDatas();
			}
		}
		if(timezone==null) timezone = (int)DateUtil.BJ_TIME_OFFSET;
		if(logs!=null && logs.size()>0){
			for (Iterator<ConfLog> tir = logs.iterator(); tir.hasNext();) {
				ConfLog log =  tir.next();
				Object[] logdata = new Object[4];
				logdata[0] = log.getUserName();//
				if(ConfConstant.CONF_USER_TERM_TYPE_MOBILE == log.getTermType().intValue()){
					logdata[1] = "电话";//
				}else if(ConfConstant.CONF_USER_TERM_TYPE_PC == log.getTermType().intValue()){
					logdata[1] = "电脑";//
				}else{
					logdata[1] = "未知";//
				}
				logdata[2] = sdf.format(DateUtil.getOffsetDateByGmtDate(log.getJoinTime(), timezone.longValue()));//
				logdata[3] = sdf.format(DateUtil.getOffsetDateByGmtDate(log.getExitTime(), timezone.longValue()));
				objlist.add(logdata);
			}
		}
		HSSFWorkbook wb = ExcelUtil.createExcelWorkbook("users", objlist);
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=conf_log.xls");
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
}
