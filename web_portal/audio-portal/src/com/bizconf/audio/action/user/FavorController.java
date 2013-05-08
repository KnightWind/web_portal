package com.bizconf.audio.action.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.service.UserService;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Post;

/**
 * 对用户时区设置
 * @author wangyong
 * 2013.4.15
 */
@ReqPath("favor")
@Interceptors({UserInterceptor.class})
public class FavorController extends BaseController {

	private final Logger logger = Logger.getLogger(FavorController.class);
	
	@Autowired
	UserService userService;
	
	/**
	 * 跳转到用户修改时区设置
	 * wangyong
	 * 2013-4-15
	 */
	@AsController(path = "getTimeZone")
	public Object getTimeZone(HttpServletRequest request) throws Exception{
		UserBase currentUser = userService.getCurrentUser(request);
		request.setAttribute("user", currentUser);
		return new ActionForward.Forward("/jsp/user/favor_setup.jsp");
	}
	
	/**
	 * 用户修改时区设置
	 * wangyong
	 * 2013-4-15
	 */
	@AsController(path = "updateTimeZone")
	@Post
	public Object updateTimeZone(UserBase user, HttpServletRequest request){
		if(user == null){
			setErrMessage(request, "输入不正确,请重新输入！");
			return new ActionForward.Forward("/jsp/user/updateTimeZone.jsp");
		}
		UserBase currentUser = userService.getCurrentUser(request);
		currentUser.setTimeZoneId(user.getTimeZoneId());
		currentUser.setTimeZone(getTimeZone(user.getTimeZoneId()));
		
		boolean updateFlag = userService.saveUserBase(currentUser);
		if(updateFlag){
			setInfoMessage(request,"用户修改时区设置成功！");
		}else{
			setErrMessage(request, "用户修改时区设置失败！");
		}
		return new ActionForward.Forward("/user/favor/getTimeZone");
	}
	
	private int getTimeZone(int id){
		if(id>0){
			return SiteConstant.TIMEZONE_WITH_CITY_MAP.get(id).getOffset();
		}
		return 28800000;//默认为北京时间
	}
}
