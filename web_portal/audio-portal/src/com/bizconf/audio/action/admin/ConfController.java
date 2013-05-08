package com.bizconf.audio.action.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.SortConstant;
import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
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
		int rows = 0;
		String sortFieldValue = request.getParameter("sortField");
		String sortField = initSort(sortFieldValue);     //获取页面传递的排序参数
		String sortordValue = request.getParameter("sortord");
		String sortord = "desc";
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		if(SortConstant.SORT_ASC.equals(sortordValue)){
			sortord = "asc";
		}
		if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
			rows = confService.countConfListBySubject(title, currentSiteAdmin.getSiteId(), null);
		}else{
			rows = confService.countConfListBySubject(title, currentSiteAdmin.getSiteId(), currentSiteAdmin.getId());
		}
		logger.info(rows);
		pageModel.setRowsCount(rows);
		if(currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
			confList = confService.getConfListBySubject(currentSite, title, currentSiteAdmin.getSiteId(), sortField, sortord, pageModel, null);
		}else{
			confList = confService.getConfListBySubject(currentSite, title, currentSiteAdmin.getSiteId(), sortField, sortord, pageModel, currentSiteAdmin.getId());
		}
		
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
		request.setAttribute("sortField", sortFieldValue);   //传排序字段的编号
		request.setAttribute("sortord", sortordValue);       //传排序方式的编号
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
		Condition condition = initCondition(confBase, currentSiteAdmin, request);
		String sortFieldValue = request.getParameter("sortField");
		String sortField = initSort(sortFieldValue);     //获取页面传递的排序参数
		String sortordValue = request.getParameter("sortord");
		String sortord = "desc";
		if(SortConstant.SORT_ASC.equals(sortordValue)){
			sortord = "asc";
		}
		Integer rows = 0;
		if(!currentSiteAdmin.isSuperSiteAdmin()){    //权限控制
			condition.equal("b.create_user", currentSiteAdmin.getId().intValue());  //普通站点管理员只能查看自己创建的站点
		}
		rows = confService.countConfListByCondition(condition);
		logger.info(rows);
		pageModel.setRowsCount(rows);
		confList = confService.getConfListByCondition(currentSite, condition, sortField, sortord, pageModel);
		
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
		request.setAttribute("sortField", sortFieldValue);   //传排序字段的编号
		request.setAttribute("sortord", sortordValue);       //传排序方式的编号
		sortAttribute(confBase, request);                    //向前台传递高级搜索表单值
		return new ActionForward.Forward("/jsp/admin/conf_list.jsp");
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
		String sortField = SortConstant.CONFBASE_FIELDS[0][1];
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
