package com.bizconf.audio.action.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.interceptors.UserInterceptor;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.LicService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.Interceptors;
import com.libernate.liberc.annotation.ReqPath;

/**
 * 企业用户会议缺省设置controller
 * @author wangyong
 * 2013/3/5
 */
@ReqPath("confConfig")
@Interceptors({UserInterceptor.class})
public class ConfConfigController extends BaseController {
	private final Logger logger = Logger.getLogger(ConfController.class);
	
	@Autowired
	UserService userService;
	@Autowired
	ConfService confService;
	@Autowired
	SiteService siteService;
	@Autowired
	EmpowerConfigService empowerConfigService;
	@Autowired
	LicService licService;
	
	/**
	 * 跳转到修改会议设置页面
	 * wangyong
	 * 2013-3-20
	 */
	@AsController(path = "getConfConfig")
	public Object getConfConfig(HttpServletRequest request) {
		UserBase currentUser = userService.getCurrentUser(request);
		if(!currentUser.isConfHost()){
			setErrMessage(request, "该企业用户无权限修改会议设置！");
			return new ActionForward.Forward("/jsp/user/conf_default_setup.jsp");
		}
		DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
		if(confConfig == null || confConfig.getId() == null){    //若当前用户无默认会议设置，则新建一条会议设置
			confConfig = confService.saveDefaultConfig(currentUser);
		}
//		//根据站点ID号取站点的全局变量设置
//		EmpowerConfig sitePower  = empowerConfigService.getSiteEmpowerGlobalBySiteId(currentUser.getSiteId());
//		//根据用户的ID号取用户的授权配置(站点管理员创建企业用户时的授权配置)
//		EmpowerConfig userPower  = empowerConfigService.getUserEmpowerConfigByUserId(currentUser.getId());
//		if(sitePower != null && userPower != null){
//			//站点全局变量“媒体共享”启用并且用户“媒体共享”授权启用后，个人用户才可以设置会议设置中“媒体共享”项
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
		request = confService.confConfigAttr(confConfig, request);
//		int effeLicense = licService.getSiteLicenseNum(currentSite.getId()) + currentSite.getLicense().intValue();
//		if(effeLicense < 5){
//			confConfig.setMaxUser(effeLicense);
//		}
		request.setAttribute("userEmpower", userEmpower);
		request.setAttribute("confConfig", confConfig);
//		request.setAttribute("defaultLicence", effeLicense);    //最大参会人数
		return new ActionForward.Forward("/jsp/user/conf_default_setup.jsp");
	} 
	
	/**
	 * 修改会议设置
	 * wangyong
	 * 2013-3-20
	 */
	@AsController(path = "updateConfConfig")
	public Object updateConfConfig(DefaultConfig newConfig, HttpServletRequest request){
		boolean updateFlag = false;
		UserBase currentUser = userService.getCurrentUser(request);
		DefaultConfig confConfig = confService.getDefaultConfig(currentUser);
		if(newConfig != null){
//			if(newConfig.getMaxUser() != null && newConfig.getMaxUser().intValue() > 1){
//				if(newConfig.getMaxUser().intValue() > currentSite.getLicense().intValue()){
//					setErrMessage(request, "最大参会人数不可大于站点license");
//					logger.info("最大参会人数不可大于站点license");
//					return new ActionForward.Forward("/jsp/user/conf_default_setup.jsp");
//				}
//			}
			confConfig.setMaxUser(2);    //页面不再设置最大参会方数，默认值为2
			confConfig.setMaxAudio(newConfig.getMaxAudio());
			confConfig.setMaxVideo(newConfig.getMaxVideo());
			confConfig.setVideoType(newConfig.getVideoType());
			confConfig.setMaxDpi(confConfig.getVideoType().substring(0, 1));
			confConfig.setDefaultDpi(confConfig.getVideoType().substring(1, 2));
			confConfig.setAheadTimes(newConfig.getAheadTimes());
			confConfig.setPriviBits(confService.getPriviBits(request, confConfig));
			confConfig.setClientConfig(confService.getClientConfig(request, confConfig));
			confConfig.setFuncBits(confService.getFuncBits(null, request, confConfig));
			confConfig.setConfType(newConfig.getConfType());
			updateFlag = confService.updateConfConfig(confConfig);
			if(updateFlag){
				setInfoMessage(request,"修改会议设置成功！");
			}else{
				setErrMessage(request, "修改会议设置失败！");
				return new ActionForward.Forward("/jsp/user/conf_default_setup.jsp");
			}
		}else{
			setErrMessage(request, "修改会议设置失败！");
		}
		return new ActionForward.Forward("/user/confConfig/getConfConfig");
	}

	
}
