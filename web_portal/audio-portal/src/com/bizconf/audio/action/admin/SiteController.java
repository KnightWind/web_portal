package com.bizconf.audio.action.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.EventLogConstants;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.SiteAdminInterceptor;
import com.bizconf.audio.service.EventLogService;
import com.bizconf.audio.service.LicService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 企业站点信息
 * @author wangyong
 * 2013/2/23
 */
@ReqPath("site")
@Interceptors({SiteAdminInterceptor.class})
public class SiteController extends BaseController {
	private final Logger logger = Logger.getLogger(SiteController.class);
	
	@Autowired
	SiteService siteService;
	@Autowired
	UserService userService;
	@Autowired
	EventLogService eventLogService;
	@Autowired
	LicService licService;
	
	/**
	 * 获取当前站点时区的站点信息
	 * shhc
	 * 2013-6-3
	 */
	private void getOffsetSiteBase(SiteBase siteBase){
		String[] fields = new String[]{"effeDate", "expireDate"};
		long offset = 0 ;
		if(siteBase != null){
			offset = siteBase.getTimeZone();
		}else{
			offset = DateUtil.getDateOffset();
		}
		siteBase = (SiteBase) ObjectUtil.offsetDate(siteBase, offset, fields);
	}
	
	/**
	 * 获取企业站点信息（超级企业管理员）
	 * wangyong
	 * 2013-2-23
	 */
	@AsController(path = "info")
	public Object siteInfo(HttpServletRequest request){
		UserBase siteAdmin = userService.getCurrentSiteAdmin(request);
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		currentSite.setLicense(licService.getSiteLicenseNum(currentSite.getId()) + currentSite.getLicense().intValue());
		getOffsetSiteBase(currentSite);
		request.setAttribute("siteBase", currentSite);
		request.setAttribute("siteAdmin", siteAdmin);
		PageBean<UserBase> pageModel = licService.getHostsBySite(currentSite.getId(), 1);
		request.setAttribute("pageModel", pageModel);
		request.setAttribute("siteId", currentSite.getId());
		request.setAttribute("licnums", licService.getHostsLienseDatas(pageModel.getDatas()));
		request.setAttribute("effeDates", licService.getHostsLienseEffeDates(pageModel.getDatas(), currentSite.getTimeZone()));
		return new ActionForward.Forward("/jsp/admin/siteBase_info.jsp");
	}
	
	/**
	 * 修改企业站点信息（超级企业管理员）
	 * wangyong
	 * return msgCode 1,成功  2,失败
	 * 2013-2-23
	 */
	@AsController(path = "update")
	public Object updateSiteInfo(SiteBase siteBase, HttpServletRequest request){
		boolean flag = false;
		SiteBase site = null;
		Integer msgCode = 0;
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		if(siteBase != null){
			siteBase = (SiteBase)ObjectUtil.parseHtml(siteBase, "siteName", "enName");	//字符转义
			try {
				Integer timeZone=SiteConstant.TIMEZONE_WITH_CITY_MAP.get(siteBase.getTimeZoneId()).getOffset();
				site = siteService.updateSiteBaseForSiteAdmin(siteBase.getId(), siteBase.getSiteName(), siteBase.getEnName(), siteBase.getSiteLogo(),siteBase.getTimeZoneId(),timeZone);
			}catch (Exception e){
				logger.error("修改站点信息失败");
			}
			try{
				if(site != null && site.getId() > 0){
					flag = true;
//					eventLogService.saveAdminEventLog(currentSiteAdmin, EventLogConstants.SITE_INFO_UPDATE, ResourceHolder.getInstance().getResource("system.notice.list.Create.1"), EventLogConstants.EVENTLOG_SECCEED, siteBase, request);   //创建成功后写EventLog
					msgCode = 1;
				}else{
					msgCode = 2;
					logger.error("记录修改站点信息失败");
				}
			} catch (Exception e) {
				logger.error("记录修改站点信息失败" + e);
			} finally{
				sysHelpAdminEventLog(flag, userService.getCurrentSysAdmin(request), currentSiteAdmin, 
						EventLogConstants.SYSTEM_INFO_UPDATE, EventLogConstants.SITE_INFO_UPDATE, "修改站点信息", null, request);
			}
		}
		request.setAttribute("msgCode", msgCode);
		SiteBase currentSite = siteService.getCurrentSiteBaseByAdminLogin(request);
		request.setAttribute("msgCode", msgCode);
		currentSite.setLicense(licService.getSiteLicenseNum(currentSite.getId()) + currentSite.getLicense().intValue());
		request.setAttribute("siteBase", currentSite);
		request.setAttribute("siteAdmin", currentSiteAdmin);
		return new ActionForward.Forward("/jsp/admin/siteBase_info.jsp");
	}
}
