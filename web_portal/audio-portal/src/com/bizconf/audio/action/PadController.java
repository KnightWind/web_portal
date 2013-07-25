package com.bizconf.audio.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.component.language.LanguageHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ConfServiceForPad;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.NumberUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;


@ReqPath("Portal/conference")
public class PadController extends BaseController{
	
	@Autowired
	SiteService siteService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ConfServiceForPad confServiceForPad;
	
	
	/**
	 * PAD获取会议列表+登录接口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "getConfListByIpad.jsp")
	public Object getConfListForPad(HttpServletRequest request, HttpServletResponse response)throws Exception{
		SiteBase siteBase = null;
		String siteSign = SiteIdentifyUtil.getCurrentBrand();
		siteBase = siteService.getSiteBaseBySiteSign(siteSign);
		StringBuffer confBuffer = new StringBuffer();
		//confBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		confBuffer.append("<Root>");
		confBuffer.append("");
		UserBase userBase=null;
		boolean loginStatus=false;
		if (siteBase != null) {
			confBuffer.append("<public>");
			String loginName=request.getParameter("boxUserName");
			String loginPass=request.getParameter("boxUserPwd");
			String startDateStr=request.getParameter("startDate");
			Date startDate=StringUtil.isEmpty(startDateStr)?DateUtil.addDate(new Date(),-7):DateUtil.parseDate(startDateStr);
			String endDateStr=request.getParameter("endDate");
			Date endDate=StringUtil.isEmpty(endDateStr)?new Date():DateUtil.parseDate(endDateStr);
		
			String queryType=request.getParameter("queryType");
			String pageNumStr=StringUtil.isEmpty(request.getParameter("pageNum"))?"":request.getParameter("pageNum");
			int pageNum=(NumberUtil.isInteger(pageNumStr) && NumberUtil.parseInteger(pageNumStr) > 0) ? 1 : NumberUtil.parseInteger(pageNumStr);
			String pageCountStr=StringUtil.isEmpty(request.getParameter("pageCount"))?String.valueOf(ConstantUtil.PAGESIZE_DEFAULT):request.getParameter("pageCount");
			int pageCount=(NumberUtil.isInteger(pageCountStr) && NumberUtil.parseInteger(pageCountStr) > 0) ? ConstantUtil.PAGESIZE_DEFAULT : NumberUtil.parseInteger(pageCountStr);
			String sort=request.getParameter("sort");
			String confType=request.getParameter("confType");
			loginStatus=loginService.loginForPad(loginName, loginPass, siteBase, request);
			confBuffer.append("<error>" + (loginStatus?"0":"1") + "</error>");
			confBuffer.append("<site_info>");
			if(loginStatus){
				userBase=userService.getSiteUserByLoginName(siteBase.getId(), loginName);
				confBuffer.append("<user_id>" + userBase.getId() + "</user_id>");
				confBuffer.append("<username>" + userBase.getTrueName() + "</username>");
				confBuffer.append("");
				confBuffer.append("");
				startDate=DateUtil.getGmtDateByTimeZone(startDate,userBase.getTimeZone());
				endDate=DateUtil.getGmtDateByTimeZone(endDate,userBase.getTimeZone());
			}
			confBuffer.append("<site_id>" + ConfConstant.CLUSTER_ID + "</site_id>");
			confBuffer.append("<help_url>" + "http://"+SiteIdentifyUtil.getCurrentDomine()+"/help" + "</help_url>");
			confBuffer.append("<site_url>" + "http://"+SiteIdentifyUtil.getCurrentDomine() + "</site_url>");
			confBuffer.append("<conf_tag>2</conf_tag>");//写固定值2，必须写
			confBuffer.append("<unique_id>1</unique_id>");//写固定值1，必须写
			confBuffer.append("</site_info>");
			confBuffer.append("</public>");
			List<ConfBase> confList=null;
			confList=confServiceForPad.getConfListForPad(userBase, startDate, endDate, queryType, pageNum, pageCount, sort, confType);
			confBuffer.append("<mtg>");
			if(confList != null && confList.size() > 0){
				for(ConfBase eachConf:confList){
					if(eachConf!=null){
						confBuffer.append("<mtg_info>");
						String confHwId=StringUtil.isEmpty(eachConf.getConfHwid())?"per99999999":eachConf.getConfHwid();
						confBuffer.append("<mtg_id>"+confHwId+"</mtg_id> ");//必须有值
						confBuffer.append("<mtg_key>"+confHwId+"</mtg_key> ");//必须有值
						confBuffer.append("<mtg_title>"+eachConf.getConfName()+"</mtg_title> ");
						confBuffer.append("<mtg_type>1</mtg_type> ");
						confBuffer.append("<chairmanpasswd>"+eachConf.getCompereSecure()+"</chairmanpasswd>");
						confBuffer.append("<participantpasswd>"+eachConf.getUserSecure()+"</participantpasswd> ");
						confBuffer.append("<access_number>"+(eachConf.hasPhoneFunc()?"01058214900":"")+"</access_number> ");
						confBuffer.append("<chairmansecureconfid>"+eachConf.getCompereSecure()+"</chairmansecureconfid>");
						confBuffer.append("<participantsecureconfid>"+eachConf.getUserSecure()+"</participantsecureconfid> ");
						confBuffer.append("<begintime>"+eachConf.getStartTime()+"</begintime> ");
						confBuffer.append("<endtime>"+eachConf.getEndTime()+"</endtime> ");
						confBuffer.append("<chairmanName>"+eachConf.getCompereName()+"</chairmanName> ");
						confBuffer.append("<confstatus>"+eachConf.getConfStatus()+"</confstatus>");
						confBuffer.append("<user_role>"+(userBase.getId().equals(eachConf.getCompereUser())?"3":"8")+"</user_role>");
						confBuffer.append("");
						confBuffer.append("</mtg_info>");
					}
				}
			}
			confBuffer.append("</mtg>");
		

		}

		confBuffer.append("</Root>");
//	  
		return new ActionForward.TextXML(confBuffer.toString());
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@AsController(path = "getClientParameter.jsp")
	public Object getClientParameterForPad(HttpServletRequest request, HttpServletResponse response)throws Exception{
		SiteBase siteBase = null;
		String siteSign = SiteIdentifyUtil.getCurrentBrand();
		siteBase = siteService.getSiteBaseBySiteSign(siteSign);
		String siteId=request.getParameter("site_id");
		String confId=request.getParameter("conf_id");
		String confSecureId=request.getParameter("conf_secure_id");
		String userId=request.getParameter("user_id");
		String userName=request.getParameter("user_name");
		String userRole=request.getParameter("user_role");
		String langId=request.getParameter("lang_id");
		String confTag=request.getParameter("conf_tag");
		String random=request.getParameter("random");
		String uniqueId=request.getParameter("unique_id");
		String otherParams=request.getParameter("other_params");
		ConfBase confBase=null;
		
		//根据会议ID号获取会议信息
		if(!StringUtil.isEmpty(confId) && confId.indexOf("per")<0){
			confBase=confServiceForPad.getConfBaseByHwId(confId);
		}
		
		//根据会议的安全码获取会议信息
		if(confBase==null && !StringUtil.isEmpty(confSecureId)){
			//根据主持会议安全码
			confBase=confServiceForPad.getConfBaseByCompereSecure(confSecureId);
			if(confBase==null){
				//根据与会者会议安全码
				confBase=confServiceForPad.getConfBaseByUserSecure(confSecureId);
			}
		}
		
		//验证用户权限
		if(StringUtil.isEmpty(userRole)){
			if(confBase!=null){
				if(confSecureId.equals(confBase.getCompereSecure())){
					userRole="3";
				}
				if(confSecureId.equals(confBase.getUserSecure())){
					userRole="8";
				}
				if(StringUtil.isEmpty(userRole) && userId.equals(confBase.getCreateUser()+"")){
					userRole="3";
				}
			}
		}
		
		String clientParam=makeClientParamForPad(confBase,userId,userRole);
		
		confBase=null;
		siteBase=null;
		return new ActionForward.TextXML(clientParam);
	}

	
	@AsController(path = "queryConfMemberList.jsp")
	public Object queryConfMemberListForPad(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String hwId=request.getParameter("confId");
		List<ConfUser> userList=confServiceForPad.getUserListByHwId(hwId);
		int userCount=0;
		if(userList!=null){
			userCount=userList.size();
		}
		
		StringBuffer userBuffer=new StringBuffer();
		userBuffer.append("<?xml version=\"1.0\" encoding=\"UTF8\" ?> ");
		userBuffer.append("<Root>");
		userBuffer.append("<errMsg>0</errMsg> ");
		userBuffer.append("<confid>"+hwId+"</confid> ");
		userBuffer.append("<Users>");
		userBuffer.append("		<totalCount>"+userCount+"</totalCount> ");
		
		for(ConfUser confUser:userList){
			if(confUser!=null){
				userBuffer.append("		<User>");
				userBuffer.append("			<enterpriseId/> ");
				userBuffer.append("			<account>"+confUser.getUserName()+"</account> ");
				userBuffer.append("			<name>"+confUser.getUserName()+"</name> ");
				userBuffer.append("			<phone>"+confUser.getTelephone()+"</phone> ");
				userBuffer.append("			<email>"+confUser.getUserEmail()+"</email> ");
				userBuffer.append("			<role>"+(ConfConstant.CONF_HIDE_FLAG_TRUE.equals(confUser.getHostFlag())?3:8)+"</role> ");
				userBuffer.append("		</User>");
			}
		}
		
		userBuffer.append("</Users>");
		userBuffer.append("</Root>");
		
		userList=null;
		
		return new ActionForward.TextXML(userBuffer.toString());
		
	}
	
	
	
	

 
	/**
	 * 生成Pad入会的大参数
	 * @param confBase
	 * @param userId
	 * @param userRole
	 * @return
	 */
	private String makeClientParamForPad(ConfBase confBase,String userId,String userRole){
		if(confBase==null){
			return "";
		}
		StringBuffer paramBuffer=new StringBuffer();
		 
		paramBuffer.append("<ConfParam>");
		paramBuffer.append("<errCode>0</errCode>");
		paramBuffer.append("<enterpriseId>21</enterpriseId>");
		paramBuffer.append("<conf_id>"+confBase.getConfHwid()+"</conf_id>");
		paramBuffer.append("<user_id>"+userId+"</user_id>");
		paramBuffer.append("<lang_id>" + (LanguageHolder.getCurrentLanguage().equalsIgnoreCase(LanguageHolder.DEFAULT_LANGUAGE) ? 2052 : 1033) + "</lang_id>");
		paramBuffer.append("<conf_start_time>"+confBase.getStartTime()+"</conf_start_time>");
		paramBuffer.append("<conf_end_time>"+confBase.getEndTime()+"</conf_end_time>");
		paramBuffer.append("<mtg_mode>" + getConfigByBitIndex(confBase.getFuncBits(), 8) + "</mtg_mode>");
		paramBuffer.append("<conf_name>"+StringUtil.string2Unicodes(confBase.getConfName()) +"</conf_name>");
		paramBuffer.append("<user_show>000</user_show>");
		paramBuffer.append("<lock_status>" + (confBase.getConfStatus().intValue() == ConfConstant.CONF_STATUS_LOCKED.intValue() ? 1 : 0)  + "</lock_status>");
		paramBuffer.append("<user_role>"+userRole+"</user_role>");
		paramBuffer.append("<conf_pass>" + confBase.getUserSecure()+ "</conf_pass>");
		paramBuffer.append("<server_ip />");
		paramBuffer.append("<site_id />");
		paramBuffer.append("<conf_desp>"+StringUtil.string2Unicodes(confBase.getConfDesc())+"</conf_desp>");
		paramBuffer.append("<conf_key>"+confBase.getConfHwid()+"</conf_key>");
		paramBuffer.append("<host_key>"+confBase.getCompereSecure()+"</host_key>");
		paramBuffer.append("<conf_tag>2</conf_tag>");
		paramBuffer.append("<layout_model>02</layout_model>");
		paramBuffer.append("<video_maxnum>"+confBase.getMaxVideo()+"</video_maxnum>");
		paramBuffer.append("<audio_maxnum>"+confBase.getMaxAudio()+"</audio_maxnum>");
		paramBuffer.append("<video_width>"+confBase.getDefaultDpi()+"</video_width>");
		paramBuffer.append("<video_max_width>"+confBase.getMaxDpi()+"</video_max_width>");
		paramBuffer.append("<mcu_mode>0</mcu_mode>");
		paramBuffer.append("<ipad_video_enable>1</ipad_video_enable>");
		paramBuffer.append("<conf_status>"+confBase.getConfStatus()+"</conf_status>");
		paramBuffer.append("<max_time>24</max_time>");
		paramBuffer.append("<crypt_key>"+confBase.getCryptKey()+"</crypt_key>");
		paramBuffer.append("<mix_method>" + (ConfConstant.AUDIO_SERVER_MIX ? 2 : 1) + "</mix_method>");
		paramBuffer.append("<frame_len>30</frame_len>");
		paramBuffer.append("<sample_rate>8</sample_rate>");
		paramBuffer.append("<codec_name>iLBC</codec_name>");
		paramBuffer.append("<default_privilege>"+makePrivilege(confBase)+"</default_privilege>");
		paramBuffer.append("<web_config>"+makeWebConfig(confBase)+"</web_config>");
		paramBuffer.append("<roll_call>0</roll_call>");
		paramBuffer.append("<h323_enable>0</h323_enable>");
		paramBuffer.append("<invite_type>"+ (ConfConstant.SYS_ENABLE_PHONE ? "11" : "01") +"</invite_type>");
		paramBuffer.append("<phone_conf>");
		paramBuffer.append("<conf_type>"+ (confBase.hasPhoneFunc() ? 2 : 0) +"</conf_type>");
		paramBuffer.append("<max_phone_count>"+confBase.getMaxUser()+"</max_phone_count>");
		paramBuffer.append("<pin>"+confBase.getConfHwid()+"</pin>");
		paramBuffer.append("<nums>");
		paramBuffer.append("<num name=\"\"/>");
		paramBuffer.append("</nums>");
		paramBuffer.append("<phone_conf_id />");
		paramBuffer.append("</phone_conf>");
		paramBuffer.append("<support_info />");
		paramBuffer.append("<other_param></other_param>");
		paramBuffer.append("<time_now>"+DateUtil.getGmtDate(null)+"</time_now>");
		paramBuffer.append("<max_user_count>"+confBase.getMaxUser()+"</max_user_count>");
		paramBuffer.append("<microphoneStatus>"+getConfigByBitIndex(confBase.getFuncBits(),11)+"</microphoneStatus>");
		paramBuffer.append("<host_open_video>1</host_open_video>");
		paramBuffer.append("<large_venue>0</large_venue>");
		paramBuffer.append("<mirage_driver>0</mirage_driver>");
		paramBuffer.append("<speak_interval>5</speak_interval>");
		paramBuffer.append("</ConfParam>");
		
		return paramBuffer.toString();
	}



	private String getConfigByBitIndex(String config, int index) {
		if (StringUtils.isEmpty(config)) {
			return "";
		}

		if (config.length() < index || index <= 0) {
			throw new IllegalArgumentException("'index' is not correct.");
		}

		return String.valueOf(config.charAt(index - 1));
	}

	private String makeWebConfig(ConfBase confBase) {
		if (true) {
			//return "1111110111111111111001101011110001";
			return confBase.getClientConfig();
		}
		char[] webConfigs = "0000000000000000000000000000000000".toCharArray();
		webConfigs[1] = this.getConfigByBitIndex(confBase.getClientConfig(), 1)
				.charAt(0);// 文档共享
		webConfigs[2] = this.getConfigByBitIndex(confBase.getClientConfig(), 2)
				.charAt(0);// 屏幕共享
		webConfigs[3] = this.getConfigByBitIndex(confBase.getClientConfig(), 3)
				.charAt(0);// 白板
		webConfigs[21] = this
				.getConfigByBitIndex(confBase.getClientConfig(), 5).charAt(0);// 媒体共享
		StringBuilder configs = new StringBuilder();
		for (char c : webConfigs) {
			configs.append(c);
		}
		return configs.toString();
	}

	private long makePrivilege(ConfBase confBase){
		if(confBase==null){
			return 0;
		}
		String priviBits=confBase.getPriviBits();
		if(StringUtil.isEmpty(priviBits)){
			return 0;
		}
		long privilege=0;
		char[] priviChars=priviBits.toCharArray();
		if(priviChars.length<ConfConstant.PRIVIBITS_CONFIG_CHAT_PARTICIPANTS){
			return 0;
		}
		
//
//数值	说明
//0x00000040==64	白板翻页
//0x00000004==4	文档翻页
//0x00000010==16	白板标注
//0x00000008==8	文档标注
//0x00000080==128	与所有人聊天
//0x00000100==256	与主持人聊天
//0x00000200==512	与主讲人聊天
//0x00000400==1024	与与会者聊天
		
		String eachPriviStr="";
		//翻页；包括   白板翻页、文档翻页
		eachPriviStr=String.valueOf(priviChars[ConfConstant.PRIVIBITS_CONFIG_CHANGEPAGE]);
		if("1".equals(eachPriviStr)){
			privilege+=64;//白板翻页
			privilege+=4;//文档翻页
		}

		//批注；包括   白板批注、文档批注
		eachPriviStr=String.valueOf(priviChars[ConfConstant.PRIVIBITS_CONFIG_ANNOTATE]);
		if("1".equals(eachPriviStr)){
			privilege+=16;//白板标注
			privilege+=8;//文档标注
		}

		//与所有人
		eachPriviStr=String.valueOf(priviChars[ConfConstant.PRIVIBITS_CONFIG_CHAT_ANYONE]);
		if("1".equals(eachPriviStr)){
			privilege+=128;//与所有人聊天
		}

		//与主持人；包括   主持人、主讲人
		eachPriviStr=String.valueOf(priviChars[ConfConstant.PRIVIBITS_CONFIG_CHAT_COMPERE]);
		if("1".equals(eachPriviStr)){
			privilege+=256;//与主持人聊天
			privilege+=512;//与主讲人聊天
		}

		//与参会者
		eachPriviStr=String.valueOf(priviChars[ConfConstant.PRIVIBITS_CONFIG_CHAT_PARTICIPANTS]);
		if("1".equals(eachPriviStr)){
			privilege+=1024;//与与会者聊天
		}
		return privilege;
	}
	

}
