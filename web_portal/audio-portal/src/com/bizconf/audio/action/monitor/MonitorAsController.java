package com.bizconf.audio.action.monitor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Get;
import com.libernate.liberd.Libernate;

@ReqPath("")
public class MonitorAsController extends BaseController{
	private final Logger logger=Logger.getLogger(MonitorAsController.class);

	private static Libernate libernate = DAOProxy.getLibernate();

	@Autowired
	UserService userService;
	@Autowired
	ConfService confService;
	@Autowired
	SiteService siteService;
	@Autowired
	ConfManagementService confManagementService;
	
	
	
	@Autowired
	ConfUserService confUserService;
	
	
	@AsController(path = "checkas")
	@Get
	public Object monitor( HttpServletRequest request){
		int userId = 1003676;     //go 的dick用户
		int siteId = 26;		  //go站点
//		int userId = 1000003;     //240 的jack用户id
//		int siteId = 1;			  //240 的meet站点
		UserBase userBase=userService.getUserBaseById(userId);
		SiteBase siteBase=siteService.getSiteBaseById(siteId);
		String debug=request.getParameter("debug");
		if("debug".equalsIgnoreCase(debug)){
			String code=request.getParameter("code");
			return code;
		}
		if(userBase==null){
			logger.info("userBase is null ");
			return 3;
		}
		if(siteBase==null){
			logger.info("siteBase is null ");
			return 3;
		}
		ConfBase confBase = new ConfBase();
		initConf(confBase, siteBase, userBase);
		logger.info(siteBase);
		logger.info(userBase);
		//Create Conf
		String retInfo = confManagementService.createConf(confBase, siteBase, userBase, false);
		
		if(retInfo.equals(ConstantUtil.AS_SUCCESS_CODE)){
			confBase = saveConf(confBase);
			if(confBase != null && confBase.getId() != null && confBase.getId().intValue() > 0){
				confUserService.fillConfUserForCreate(confBase, userBase);
			}
			logger.info("创建会议成功！");
		}else{
			logger.error("创建会议失败，as错误码：" + retInfo);
			return 3;
		}
		logger.info(confBase);
		// Start Conf
		if(!confManagementService.startConf(confBase, siteBase, userBase)){
			logger.error("启动会议失败");
			return 3;
		}
		logger.info("启动会议成功");
		//Calcel Conf
//		if(!confManagementService.cancelConf(confBase.getConfHwid(), siteBase, userBase)){
//			logger.error("结束会议失败");
//			return 3;
//		}
//		logger.info("结束会议成功");
		return 0;
	}
	
	/**
	 * 保存会议时，初始化会议信息
	 * wangyong
	 * 2013-3-4
	 */
	private void initConf(ConfBase confBase, SiteBase siteBase, UserBase user){
		if(confBase != null && siteBase != null && user != null){
			DefaultConfig defaultConfig = confService.getDefaultConfig(user);
			confBase.setConfName("验证AS的会议-" + DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
			confBase.setSiteId(siteBase.getId());
			confBase.setCycleId(0);   //此处全部初始化为预约会议，周期会议在创建周期会议方法中初始化完毕后设置	
			if(!StringUtil.isNotBlank(confBase.getConfDesc())){
				confBase.setConfDesc("");
			}
			Integer timeZone = 0;
			Integer timeZoneId = 0;
			if(user.getTimeZone() != null){
				timeZone = user.getTimeZone();
				timeZoneId = user.getTimeZoneId();
			}else if(siteBase.getTimeZone() != null){
				timeZone = siteBase.getTimeZone();
				timeZoneId = siteBase.getTimeZoneId();
			}
			confBase.setStartTime(DateUtil.addDateMinutes(DateUtil.getGmtDate(null),5));
			confBase.setDuration(5);
			confBase.setEndTime(DateUtil.addDateMinutes(confBase.getStartTime(),5));
			confBase.setTimeZone(timeZone);
			confBase.setTimeZoneId(timeZoneId);
			confBase.setCompereUser(user.getId());
			confBase.setCompereName(user.getTrueName());
			confBase.setMaxUser(2);
			confBase.setMaxAudio(defaultConfig.getMaxAudio());//最大音频数需从会议参数设置获得
			confBase.setMaxVideo(defaultConfig.getMaxVideo());
			confBase.setVideoType(defaultConfig.getVideoType());
			confBase.setDefaultDpi(confBase.getVideoType().substring(0, 1));
			confBase.setMaxDpi(confBase.getVideoType().substring(1, 2));
			confBase.setAheadTime(defaultConfig.getAheadTimes());
			confBase.setFuncBits(defaultConfig.getFuncBits());
			confBase.setClientConfig(defaultConfig.getClientConfig());
			confBase.setPriviBits(defaultConfig.getPriviBits());
			confBase.setCreateTime(DateUtil.getGmtDate(null));   //创建时间初始化为GMT时间
			confBase.setCreateUser(user.getId());
			confBase.setCreateType(user.getUserType());
			try {
				confBase.setDelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00"));
			} catch (ParseException e) {
				logger.error("保存会议时，初始化会议信息,转换删除时间出错！"+e);
			}
			if(confBase.getPublicFlag()!= null && confBase.getPublicFlag().intValue() == ConfConstant.CONF_PUBLIC_FLAG_TRUE){
				confBase.setPublicConfPass(confBase.getPublicConfPass());
			}else{
				confBase.setPublicConfPass("");
			}
		}
	}
	
	
	/**
	 * 操作数据库，新建会议信息
	 * wangyong
	 * 2013-3-5
	 */
	private ConfBase saveConf(ConfBase confBase){
		ConfBase conf = new ConfBase();
		try {
			conf = libernate.saveEntity(confBase);
		} catch (Exception e) {
			logger.error("保存会议信息出错！",e);
		}
		return conf;
	}
	
}
