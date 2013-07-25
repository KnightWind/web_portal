package com.bizconf.audio.action.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.bizconf.audio.constant.SortConstant;
import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.BeanUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.ExcelUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Get;
import com.libernate.liberc.annotation.httpmethod.Post;

/**
 * 站点管理员下会议信息查询
 * @author wangyong
 */
@Interceptors({SiteAdminInterceptor.class})
@ReqPath("conf")
public class ConfController extends BaseController {
	private final  Logger logger = Logger.getLogger(ConfController.class);

	@Autowired
	ConfService confService;
	@Autowired
	UserService userService;
	@Autowired
	SiteService siteService;
	@Autowired
	ConfUserService confUserService;
	
	
	
	/**
	 * 根据会议主题模糊检索会议列表
	 * 1.也可以根据华为会议ID号精确检索
	 * wangyong
	 * 2013-2-21
	 */
	@AsController(path = "list")
	public Object List(@CParam("title") String title, PageModel pageModel, HttpServletRequest request){
		List<ConfBase> confList = null;
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		
		PageBean<ConfBase> page = new PageBean<ConfBase>();
		pageModel.setPageSize(currentSiteAdmin.getPageSize());     // 2013.6.24 因客户需求每页展示用户设置的条数
		if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
			page = confService.getAdminConfByName(title, currentSite, sortField, sortord, pageModel, null);
		}else{
			page = confService.getAdminConfByName(title, currentSite, sortField, sortord, pageModel, currentSiteAdmin.getId());
		}
		confList = page.getDatas();
		pageModel.setRowsCount(page.getRowsCount());
		pageModel.getPageCount();
		
		
		//这里分别获取PC终端数量和电话终端数量
		Map<Integer,Integer> terminalPcs = confService.getConfsTerminalNums(confList, ConfConstant.CONF_USER_TERM_TYPE_PC);
		request.setAttribute("terminalPcs", terminalPcs);
		Map<Integer,Integer> terminalPhones = confService.getConfsTerminalNums(confList, ConfConstant.CONF_USER_TERM_TYPE_MOBILE);
		request.setAttribute("terminalPhones", terminalPhones);
		Map<Integer,Integer> allTerminals = confService.getConfsTerminalNums(confList, null);//所有参会者总数
		request.setAttribute("allTerminals", allTerminals);
		
		//需对会议列表时间展示时区设置，优先级最高的是会议表中的时区(查询某个会议信息时)，然后是站点表中的时区(查询站点下会议列表时)，最后是gmt时区
		Map<Integer,Integer> participantSizeList = getParticipants(confList);   //获取每个会议的参会人个数
		request.setAttribute("participantSizeList", participantSizeList);
		request.setAttribute("confList", confList);
		request.setAttribute("title", title);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortField);   //传排序字段的编号
		request.setAttribute("sortord", sortord);       //传排序方式的编号
		return new ActionForward.Forward("/jsp/admin/conf_list.jsp");
	}
	
	/**
	 * 会议列表高级搜索
	 * wangyong
	 * 2013-2-21
	 */
	@AsController(path = "listWithCondition")
	@Post
	public Object listWithCondition(ConfBase confBase, PageModel pageModel, HttpServletRequest request){
		List<ConfBase> confList = null;
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		String effeDateStart = request.getParameter("effeDateStart");
	    String effeDateEnd = request.getParameter("effeDateEnd");
	    Date beginTime = null;
	    Date endTime = null;
	    if(StringUtil.isNotBlank(effeDateStart)){
	      beginTime = DateUtil.StringToDate(effeDateStart, "yyyy-MM-dd");
	    }
	    if(StringUtil.isNotBlank(effeDateEnd)){
	      endTime = DateUtil.StringToDate(effeDateEnd, "yyyy-MM-dd");
	    }
		
		PageBean<ConfBase> page = new PageBean<ConfBase>();
		pageModel.setPageSize(currentSiteAdmin.getPageSize());     // 2013.6.24 因客户需求每页展示用户设置的条数
		if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
			page = confService.getAdminConfByCondition(confBase, currentSite, beginTime, endTime, sortField, sortord, pageModel, null);
		}else{
			page = confService.getAdminConfByCondition(confBase, currentSite, beginTime, endTime, sortField, sortord, pageModel, currentSiteAdmin.getId());
		}
		confList = page.getDatas();
		pageModel.setRowsCount(page.getRowsCount());
		pageModel.getPageCount();
		
		
		//这里分别获取PC终端数量和电话终端数量
		Map<Integer,Integer> terminalPcs = confService.getConfsTerminalNums(confList, ConfConstant.CONF_USER_TERM_TYPE_PC);
		request.setAttribute("terminalPcs", terminalPcs);
		Map<Integer,Integer> terminalPhones = confService.getConfsTerminalNums(confList, ConfConstant.CONF_USER_TERM_TYPE_MOBILE);
		request.setAttribute("terminalPhones", terminalPhones);
		Map<Integer,Integer> allTerminals = confService.getConfsTerminalNums(confList, null);//所有参会者总数
		request.setAttribute("allTerminals", allTerminals);
		
		//需对会议列表时间展示时区设置，优先级最高的是会议表中的时区(查询某个会议信息时)，然后是站点表中的时区(查询站点下会议列表时)，最后是gmt时区
		Map<Integer,Integer> participantSizeList = getParticipants(confList);   //获取每个会议的参会人个数
		request.setAttribute("participantSizeList", participantSizeList);
		request.setAttribute("confList", confList);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("sortField", sortField);   //传排序字段的编号
		request.setAttribute("sortord", sortord);       //传排序方式的编号
		sortAttribute(confBase, request);                    //向前台传递高级搜索表单值
		return new ActionForward.Forward("/jsp/admin/conf_list.jsp");
	}
	
	/**
	 * 导出会议
	 * martin
	 * 2013-2-21
	 */
	@AsController(path = "exportConfdetails")
	@Post
	public void exportConfLogs(ConfBase confBase, PageModel pageModel, 
			@CParam("title") String title,
			HttpServletRequest request,HttpServletResponse response){
		List<ConfBase> confList = null;
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		String sortField = request.getParameter("sortField");
		String sortord = request.getParameter("sortord");
		String effeDateStart = request.getParameter("effeDateStart");
		String effeDateEnd = request.getParameter("effeDateEnd");
		Date beginTime = null;
		Date endTime = null;
		if(StringUtil.isNotBlank(effeDateStart)){
			beginTime = DateUtil.StringToDate(effeDateStart, "yyyy-MM-dd");
		}
		if(StringUtil.isNotBlank(effeDateEnd)){
			endTime = DateUtil.StringToDate(effeDateEnd, "yyyy-MM-dd");
		}
		// 为了查询出所有的合符条件的数据，但不想添加新的逻辑
		PageBean<ConfBase> page = new PageBean<ConfBase>();
		pageModel.setPageSize(5000);
		pageModel.setPageNo("1");
		if(title!=null && !title.equals("")){
			if(currentSiteAdmin.isSuperSiteAdmin()){
				page = confService.getAdminConfByName(title, currentSite, sortField, sortord, pageModel, null);
			}else{
				page = confService.getAdminConfByName(title, currentSite, sortField, sortord, pageModel, currentSiteAdmin.getId());
			}
		}else{
			if(currentSiteAdmin.isSuperSiteAdmin()){
				page = confService.getAdminConfByCondition(confBase, currentSite, beginTime, endTime, sortField, sortord, pageModel, null);
			}else{
				page = confService.getAdminConfByCondition(confBase, currentSite, beginTime, endTime, sortField, sortord, pageModel, currentSiteAdmin.getId());
			}
		}
		confList = page.getDatas();
		
		Map<Integer,Integer> terminalPcs = confService.getConfsTerminalNums(confList, ConfConstant.CONF_USER_TERM_TYPE_PC);
		Map<Integer,Integer> terminalPhones = confService.getConfsTerminalNums(confList, ConfConstant.CONF_USER_TERM_TYPE_MOBILE);
		
		//时间已经转换成站点时间故不需要再转
//		Integer timezone = currentSite.getTimeZone();
//		if(timezone==null){
//			timezone = 28800000;
//		}
		response.reset();
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=conf_list.xls");
        List<Object[]> objlist = new ArrayList<Object[]>();
        Object[] title_timezone = new Object[1];
        title_timezone[0] = ResourceHolder.getInstance().getResource("bizconf.jsp.site.confexport.timzzoneinfo")+currentSite.getTimeZoneDesc()
				+ResourceHolder.getInstance().getResource("website.message.time");
        objlist.add(title_timezone);
        Object[] titles = new Object[8];
		titles[0] = ResourceHolder.getInstance().getResource("system.list.meeting.title");//会议主题
		titles[1] =	ResourceHolder.getInstance().getResource("system.list.meeting.host");// "主持人";
		titles[2] = ResourceHolder.getInstance().getResource("user.menu.conf.func");//"会议功能";//
		titles[3] = ResourceHolder.getInstance().getResource("system.list.meeting.status");//"会议状态";
		titles[4] = ResourceHolder.getInstance().getResource("system.list.meeting.start.time");//"开始时间";
		titles[5] = ResourceHolder.getInstance().getResource("system.list.meeting.stop.time");//"结束时间";
		titles[6] = ResourceHolder.getInstance().getResource("bizconf.jsp.site.confexport.phonenum");//"电话用户数";
		titles[7] = ResourceHolder.getInstance().getResource("bizconf.jsp.site.confexport.pcnum");//"电脑用户数";
		objlist.add(titles);//
		if(confList!=null && !confList.isEmpty()){
			for (Iterator it = confList.iterator(); it.hasNext();) {
				ConfBase conf = (ConfBase) it.next();
				Object[] dataline = new Object[8];
				dataline[0] = conf.getConfName();
				dataline[1] = conf.getCompereName();
				String typelang = "conf.type.list."+conf.getConfType();
				String confFun = ResourceHolder.getInstance().getResource(typelang);
				if(confFun==null || confFun.equals("")){
					confFun = "其他";
				}
 				String statulang = "conf.status."+conf.getConfStatus();
 				String confStatu = ResourceHolder.getInstance().getResource(statulang);
 				if(confStatu==null || confStatu.equals("")){
 					confStatu = "其他";
				}
 				dataline[2] = confFun;
 				dataline[3] = confStatu;
 				
 				dataline[4] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", conf.getStartTime(), 0);
				dataline[5] = MyfnTag.fmtDate("yyyy-MM-dd HH:mm", conf.getEndTime(), 0);
				if(conf.getConfStatus().equals(ConfConstant.CONF_STATUS_OPENING)){
					dataline[5] = "--";
					dataline[6] = conf.getPhoneNum();
					dataline[7] = conf.getPcNum();
				}else if(conf.getConfStatus().equals(ConfConstant.CONF_STATUS_FINISHED)){
					if(terminalPhones.get(conf.getId())!=null){
						dataline[6] = terminalPhones.get(conf.getId());
					}else{
						dataline[6] = "0";
					}
					if(terminalPcs.get(conf.getId())!=null){
						dataline[7] = terminalPcs.get(conf.getId());
					}else{
						dataline[7] = "0";
					}
				}else{
					dataline[6] = "--";
					dataline[7] = "--";
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
	
	/**
	 * 预览会议
	 * wangyong
	 * 2013-1-15
	 */
	@AsController(path = "view/{id:([0-9]+)}")
	@Get
	public Object viewConf(@CParam("id") Integer id,HttpServletRequest request) throws Exception{
		logger.info("id=="+id);
		ConfBase confBase = confService.getConfBasebyConfId(id);
		request.setAttribute("confBase", confBase);
		return new ActionForward.Forward("/jsp/system/viewSite.jsp");
	}
//	
//	/**
//	 * 根据当前访问的站点标识获取站点所在时区
//	 * wangyong
//	 * 2013-2-21
//	 */
//	private long getSiteOffset(HttpServletRequest request){
//		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
//		long offset = 0 ;
//		if(currentSite != null){
//			offset = currentSite.getTime_zone();
//		}else{
//			offset = DateUtil.getDateOffset();
//		}
//		logger.info("当前访问的站点时区" + offset);
//		return offset;
//	}
	
	/**
	 * 向前台传递高级搜索表单值
	 * wangyong
	 * 2013-1-23
	 */
	private void sortAttribute(ConfBase confBase, HttpServletRequest request){
		if(confBase!=null){
			logger.info("confBase-----" + confBase);
			if(StringUtil.isNotBlank(confBase.getConfName())){
				request.setAttribute("confName", confBase.getConfName());
			}
			if(confBase.getConfType() != null){
				request.setAttribute("confType", confBase.getConfType());
			}
			if(confBase.getConfStatus() != null){
				request.setAttribute("confStatus", confBase.getConfStatus());
			}
		}
		if(StringUtil.isNotBlank(request.getParameter("effeDateStart"))){
			request.setAttribute("effeDateStart", request.getParameter("effeDateStart"));
		}
		if(StringUtil.isNotBlank(request.getParameter("effeDateEnd"))){
			request.setAttribute("effeDateEnd", request.getParameter("effeDateEnd"));
		}
	}
	
	/**
	 * 初始化高级搜索Condition条件
	 * wangyong
	 * 2013-1-23
	 */
	private Condition initCondition(ConfBase confBase, UserBase user, HttpServletRequest request){
		Condition condition = new Condition();
		if(confBase != null){
			logger.info("confBase-----" + confBase);
			if(StringUtil.isNotBlank(confBase.getConfName())){
				condition.like("a.conf_name", confBase.getConfName());
			}
			if(confBase.getConfType() != null && confBase.getConfType().intValue() > 0){
				condition.equal("a.conf_type", confBase.getConfType());    //	会议类型： 1 、电话会议 2、视频 3、视频+网络电话  4、视频+传统电话
			}
			if(confBase.getConfStatus() != null && confBase.getConfStatus().intValue() != -1){
				condition.equal("a.conf_status", confBase.getConfStatus());    //会议状态：-1：全部会议  0、预约成功  2、正在召开  3、已结束    9、取消的会议
			}
		}
		if(user != null){
			if(user.getSiteId() != null && user.getSiteId().intValue() > 0){
				condition.equal("a.site_id", user.getSiteId());
			}
		}
		String effeDateStart = request.getParameter("effeDateStart");
	    String effeDateEnd = request.getParameter("effeDateEnd");
	    if(StringUtil.isNotBlank(effeDateStart)){
	      Date beginTime = DateUtil.StringToDate(effeDateStart, "yyyy-MM-dd");
	      beginTime = DateUtil.getGmtDate(beginTime);
	      condition.greaterEqual("a.start_time", DateUtil.getDateStrCompact(beginTime, "yyyy-MM-dd HH:mm:ss"));
	    }
	    if(StringUtil.isNotBlank(effeDateEnd)){
	      Date endTime = DateUtil.StringToDate(effeDateEnd, "yyyy-MM-dd");
	      endTime = DateUtil.getGmtDate(DateUtil.addDate(endTime, 1));
	      condition.lessEqual("a.start_time", DateUtil.getDateStrCompact(endTime, "yyyy-MM-dd HH:mm:ss"));
	    }
		return condition;
	}
	
	/**
	 * 获取页面传递来的排序参数
	 * wangyong
	 * 2013-1-22
	 */
	private String initSort(String field){
		String sortField = "";
		for (String[] eachField : SortConstant.CONFBASE_FIELDS) {
			if (eachField != null && eachField[0].equals(field)) {
				sortField = BeanUtil.att2Field(eachField[1]);
				break;
			}
		}
		return sortField;
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
	
}
