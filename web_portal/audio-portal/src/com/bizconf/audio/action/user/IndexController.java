package com.bizconf.audio.action.user;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.Notice;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.User;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.service.NoticeService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.CookieUtil;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.Base64;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberCFile;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.exception.LiberCFileException;

/**
 * 企业用户入口
 * 
 * @author wangyong
 * 2013/2/28
 */
@ReqPath("")
//@Interceptors(UserInterceptor.class)
public class IndexController extends BaseController{
	private final  Logger logger=Logger.getLogger(IndexController.class);
	@Autowired
	UserService userService;
	@Autowired
	NoticeService noticeService;
	@Autowired
	SiteService siteService;
	
	@AsController(path = "")
	public Object index(LiberInvocation inv) {
		UserBase user = userService.getCurrentUser(inv.getRequest());
		SiteBase currentSite = siteService.getSiteBaseBySiteSign(SiteIdentifyUtil.getCurrentBrand());
		String domain = SiteIdentifyUtil.MEETING_CENTER_DOMAIN;
		String lang = CookieUtil.getCookieByDomain(inv.getRequest(), ConstantUtil.COOKIE_LANG_KEY,domain);
		
		
		String joinUrl=inv.getRequest().getParameter("joinUrl");
		boolean joinFlag=false;
		if(joinUrl!=null && joinUrl.trim().length()>0){
			joinFlag=true;
			joinUrl=Base64.decode(joinUrl, "utf8");
			joinUrl=joinUrl.replaceAll("_", "/");
		}
		inv.getRequest().setAttribute("joinFlag",joinFlag);
		inv.getRequest().setAttribute("joinType",ConfConstant.JOIN_TYPE_EMAIL);
		inv.getRequest().setAttribute("joinUrl",joinUrl);
		Notice sysNotice = noticeService.getCurrentSystemNotice();
		if(sysNotice != null && sysNotice.getId() != null && sysNotice.getId().intValue() > 0){
			sysNotice=(Notice)ObjectUtil.parseChar(sysNotice, "content");
			inv.getRequest().setAttribute("sysNotice", sysNotice);
		}
		if(currentSite != null && currentSite.getId() != null && currentSite.getId().intValue() > 0){
			//当前站点时区的时间
			Date curDate=currentSite.getSiteLocalTime();
			inv.getRequest().setAttribute("defaultDate", curDate);
		}
		if(user != null && user.getId() != null && user.getId().intValue() > 0){
			inv.getRequest().setAttribute("isConfHost", user.isConfHost());
			if(user.getPassEditor() != null && user.getPassEditor().intValue() != 0 && user.getPassEditor().intValue() != user.getId().intValue()){
				inv.getRequest().setAttribute("needResetPass", "true");      //用户密码被管理员修改后，第一次登陆需重置密码
			}
			//当前用户时区的时间
			Date curDate = user.getUserLocalTime();
			inv.getRequest().setAttribute("defaultDate", curDate);
			currentSite.setTimeZoneId(user.getTimeZoneId());
		}
		inv.getRequest().setAttribute("siteBase", currentSite);
		inv.getRequest().setAttribute("user",user);
		inv.getRequest().setAttribute("lang", lang);
		return new ActionForward.Forward("/jsp/user/index.jsp");
	}
	
	
	@AsController(path = "{id:([0-9]+)}")
	@Interceptors({UserInterceptor.class})
	//@Post
	public Object index(@CParam("id") int id, HttpServletRequest request) throws Exception {
		
		logger.info("UserId="+id);
//		request.getInputStream();
		try {
			
			System.out.println("user" + new String(StringUtil.readInputStream(request.getInputStream())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "user:" + id + ", site: " + SiteIdentifyUtil.getCurrentBrand();
	}
	
	

	
	@AsController
	//@Get
	public Object addUser(User user, @CParam("name2") String name, @CParam("file1") LiberCFile file) {
		try {
//			Condition condition=new Condition();
			file.save("", "png", "jpg");
		} catch (LiberCFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		User newUser =new User();// userService.addUser(user);
		return newUser.getName() + "ok";
	}
	
	
	
}
