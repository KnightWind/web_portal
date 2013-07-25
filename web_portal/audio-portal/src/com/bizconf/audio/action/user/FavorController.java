package com.bizconf.audio.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.component.language.LanguageComponent;
import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.StringUtil;
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
	@Autowired
	LanguageComponent languageComponent;
	
	
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
	 * 用户修改偏好设置
	 * 1.    2013.6.26 新增修改偏好设置中的每页显示条数
	 * 2.    2013.7.03 新增修改偏好设置中的语言版本，包括cookie
	 * wangyong
	 * 2013-4-15
	 */
	@AsController(path = "updateFavor")
	@Post
	public Object updateFavor(UserBase user, HttpServletRequest request, HttpServletResponse response){
		if(user == null){
			setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.favor.input.error"));
			return new ActionForward.Forward("/jsp/user/favor_setup.jsp");
		}
		UserBase currentUser = userService.getCurrentUser(request);
		currentUser.setTimeZoneId(user.getTimeZoneId());
		currentUser.setTimeZone(getTimeZone(user.getTimeZoneId()));
		currentUser.setPageSize(user.getPageSize());
		currentUser.setFavorLanguage(user.getFavorLanguage());    //设置用户偏好语言版本
		if (StringUtil.isNotBlank(user.getFavorLanguage())) {
			languageComponent.setDefaultLanguage(request, response, user.getFavorLanguage());    //设置用户偏好语言版本（cookie）
		}
		boolean updateFlag = userService.saveUserBase(currentUser);
		if(updateFlag){
			setInfoMessage(request,ResourceHolder.getInstance().getResource("bizconf.jsp.user.favor.update.success"));
			logger.info("用户修改偏好设置成功！" + currentUser);
		}else{
			setErrMessage(request, ResourceHolder.getInstance().getResource("bizconf.jsp.user.favor.update.failed"));
			logger.info("用户修改偏好设置失败！" + currentUser);
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
