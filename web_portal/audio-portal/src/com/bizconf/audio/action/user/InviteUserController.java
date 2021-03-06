package com.bizconf.audio.action.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;


@ReqPath("inviteUser")
public class InviteUserController extends BaseController {

	@Autowired
	ConfUserService confUserService;
	
	@Autowired
	ConfUserLogic confUserLogic;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ConfService confService;
	
	@Autowired
	SiteService siteService;
	
	public Object doRequest(@CParam("confId") int confId, LiberInvocation inv) {
		UserBase user = userService.getCurrentUser(inv.getRequest());
		ConfBase conf = confService.getConfBasebyConfId(confId);
		if (conf == null) {
			return null;
		}
		
		//for data security
		if (user == null && conf.getPublicFlag().intValue() != ConfConstant.CONF_PUBLIC_FLAG_TRUE.intValue()) {
			return null;
		}
		if (user == null || user.getSiteId().intValue() != conf.getSiteId().intValue()) {
			return null;
		}
		inv.getRequest().setAttribute("user", user) ;
		inv.getRequest().setAttribute("conf", conf) ;
		inv.getRequest().setAttribute("confUserList", confUserService.getConfInviteUserList(confId)) ;
		return new ActionForward.Forward("/jsp/user/conf_invite_user_list.jsp");
	}
	
	@AsController
	public Object sysview(@CParam("confId") int confId, LiberInvocation inv) {
		ConfBase conf = confService.getConfBasebyConfId(confId);
		if (conf == null) {
			return null;
		}
		
		inv.getRequest().setAttribute("confUserList", confUserService.getConfInviteUserList(confId)) ;
		return new ActionForward.Forward("/jsp/system/conf_invite_user_list.jsp");
	}
	
	
	@AsController
	public Object recv(@CParam("cuid") int confUserId, @CParam("confId") int confId, LiberInvocation inv) {
		ConfUser confUser = confUserLogic.getConfUser(confUserId);
		int result = 0;
		
		if (confUser == null || confUser.getConfId().intValue() != confId) {
			result = 1;
		}
		
		result = confUserService.recvInvite(confUserId) ? 0 : 2;
		
		ConfBase confBase = confService.getConfBasebyConfId(confId);
		if (confBase != null) {
			//2013.6.19 验收报告修复 时间为会议时区时间（以前为站点时区时间）
//			SiteBase siteBase = siteService.getSiteBaseById(confBase.getSiteId());
//			this.convertConfTime2LocalTime(confBase, siteBase);
			this.convertConfTime2LocalTime(confBase);
			inv.getRequest().setAttribute("confBase", confBase);
			inv.getRequest().setAttribute("confUser", confUser);
//			inv.getRequest().setAttribute("timeZoneDesc", siteBase.getTimeZoneDesc());
			inv.getRequest().setAttribute("timeZoneDesc", confBase.getTimeZoneDesc());
		}
		inv.getRequest().setAttribute("user", userService.getCurrentUser(inv.getRequest()));
		if (result != 0) {
			this.setErrMessage(inv.getRequest(), ResourceHolder.getInstance().getResource("bizconf.jsp.user.accept.invite.error"));
		}
		return new ActionForward.Forward("/jsp/user/conf_invite_recv.jsp");
	}
	
	@AsController
	public Object refuse(@CParam("cuid") int confUserId, @CParam("confId") int confId, LiberInvocation inv) {
		ConfUser confUser = confUserLogic.getConfUser(confUserId);
		int result = 0;
		if (confUser == null || confUser.getConfId().intValue() != confId) {
			result = 1;
		}
		
		result = confUserService.refuseInvite(confUserId) ? 0 : 2;
		
		ConfBase confBase = confService.getConfBasebyConfId(confId);
		if (confBase != null) {
			//2013.6.19 验收报告修复 时间为会议时区时间（以前为站点时区时间）
//			SiteBase siteBase = siteService.getSiteBaseById(confBase.getSiteId());
//			this.convertConfTime2LocalTime(confBase, siteBase);
			this.convertConfTime2LocalTime(confBase);
			inv.getRequest().setAttribute("confBase", confBase);
			inv.getRequest().setAttribute("confUser", confUser);
//			inv.getRequest().setAttribute("timeZoneDesc", siteBase.getTimeZoneDesc());
			inv.getRequest().setAttribute("timeZoneDesc", confBase.getTimeZoneDesc());
		}
		
		if (result != 0) {
			this.setErrMessage(inv.getRequest(), ResourceHolder.getInstance().getResource("bizconf.jsp.user.refuse.invite.error"));
		}
		return new ActionForward.Forward("/jsp/user/conf_invite_refuse.jsp");
	}
	
//	private void convertConfTime2LocalTime(ConfBase confBase, SiteBase siteBase) {
//		Date localDate = DateUtil.getOffsetDateByGmtDate(confBase.getStartTime(), 
//				(long)siteBase.getTimeZone().intValue());
//		Date localDate2 = DateUtil.getOffsetDateByGmtDate(confBase.getEndTime(), 
//				(long)siteBase.getTimeZone().intValue());
//		confBase.setStartTime(localDate);
//		confBase.setEndTime(localDate2);
//	}
	
	private void convertConfTime2LocalTime(ConfBase confBase) {
		Date localDate = DateUtil.getOffsetDateByGmtDate(confBase.getStartTime(), 
				(long)confBase.getTimeZone().intValue());
		Date localDate2 = DateUtil.getOffsetDateByGmtDate(confBase.getEndTime(), 
				(long)confBase.getTimeZone().intValue());
		confBase.setStartTime(localDate);
		confBase.setEndTime(localDate2);
	}
}
