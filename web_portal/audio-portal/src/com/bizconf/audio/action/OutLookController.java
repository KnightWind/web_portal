package com.bizconf.audio.action;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.LoginService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.IntegerUtil;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;


@ReqPath("Portal")
public class OutLookController  extends BaseController {

	private Logger logger=Logger.getLogger(OutLookController.class);
	private final int OPTTYPE_LOGIN = 1001;// 登录
	private final int OPTTYPE_CREATE_IMM_CONF = 1002;// 创建即时会议
	private final int OPTTYPE_CREATE_RES_CONF = 1003;// 创建预约会议
	private final int OPTTYPE_MODIFY_RES_CONF = 1004;// 修改预约会议
	private final int OPTTYPE_CANCLE_RES_CONF = 1005;// 取消预约会议
	
	@Autowired
	SiteService siteService;
	
	@Autowired 
	UserService userService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	ConfService confService;
	
	@Autowired
	EmailService emailService;
	
	

	@AsController(path = "ProtocolServlet")
	public Object protocol(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		logger.info("-->>>>OutLook-->>ProtocolServlet");
		PrintWriter printWriter = response.getWriter();
		StringBuffer printBuffer = new StringBuffer();
		printBuffer.append("<https>false</https>");
		printBuffer.append("<httpsPort>8080</httpsPort>");
		printWriter.print(printBuffer.toString());
		printWriter.flush();
		printWriter.close();
		printWriter = null;
		return null;
	}
	
	/**
	 * OutLook控制
	 * @param request
	 * @param response
	 * @return
	 */
	@AsController(path = "OutLookServlet")
	public Object outlook(HttpServletRequest request,HttpServletResponse response) throws Exception{
		int optType = IntegerUtil.parseInteger(request.getParameter("operatortype"));
		logger.info("-->>>>OutLook-->>OutLookServlet  " + optType);
		StringBuffer retBuffer=new StringBuffer();
		if (optType >= 1001 && optType <= 1005) {
			switch (optType) {
			case OPTTYPE_LOGIN:
				int retCode = -1;
				String userName = request.getParameter("username");
				String userPass = request.getParameter("password");
				retCode=loginService.login(userName, userPass, "", response, request);
				if(retCode==0){
					retCode=7000;
				}
				retBuffer.append("<result>"+retCode+"</result>");
				break;
			case OPTTYPE_CREATE_IMM_CONF:{
					UserBase  userBase=userService.getCurrentUser(request);
					logger.info("-->>>>OutLook-->>OutLookServlet 创建即时会议  " + userBase);
					SiteBase siteBase = siteService.getCurrentSiteBaseByUserLogin(request);
					int userId=userBase.getId();
					String username=userBase.getTrueName();
					String confName=request.getParameter("confsubject");
					int hours=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("hourofduration"));
					int minutes=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("minuteofduration"));
					int duraMinutes=hours * 60 + minutes;
					String maxUser=request.getParameter("participantcount");
					ConfBase confBase=new ConfBase();
					confBase.setConfName(confName);
					confBase.setCreateUser(userId);
					confBase.setCompereUser(userId);
					confBase.setCompereName(username);
					confBase.setDuration(duraMinutes);
					confBase.setMaxUser(IntegerUtil.parseIntegerWithDefaultZero(maxUser));
					confBase=confService.createImmediatelyConf(confBase, siteBase, userBase);
					if(confBase!=null && confBase.getId() !=null && confBase.getId().intValue()>0){
						retBuffer=new StringBuffer();
						retBuffer.append("<conferenceid>"+confBase.getConfHwid()+"</conferenceid>");
						retBuffer.append("<chairmanlink>"+emailService.getJoinUrlForHost(confBase)+"</chairmanlink>");
						retBuffer.append("<participantlink>"+emailService.getJoinUrlForUser(confBase)+"</participantlink>");
					}else{
						retBuffer.append("<result>0</result>");
					}
				}
				break;
				
			case OPTTYPE_CREATE_RES_CONF:{
					UserBase  userBase=userService.getCurrentUser(request);
					logger.info("-->>>>OutLook-->>OutLookServlet  创建预约会议" + userBase);
					SiteBase siteBase = siteService.getCurrentSiteBaseByUserLogin(request);
					int userId=userBase.getId();
					String username=userBase.getTrueName();
					String confName=request.getParameter("confsubject");
					int hours=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("hourofduration"));
					int minutes=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("minuteofduration"));
					int duraMinutes=hours * 60 + minutes;
					String maxUser=request.getParameter("participantcount");
					String beginDate=request.getParameter("begindate");
					Date startDate=DateUtil.StringToDate(beginDate, "yyyy-MM-dd HH:mm");
					startDate=DateUtil.addDateSecond(startDate, 28800);
					Date endDate=DateUtil.addDateMinutes(startDate, duraMinutes);//DateUtil.StringToDate(beginDate, "yyyy-MM-dd HH:mm");
					logger.info("-->>>>OutLook-->>OutLookServlet  创建预约会议startDate==" + startDate);
					logger.info("-->>>>OutLook-->>OutLookServlet  创建预约会议endDate==" + endDate);
					ConfBase confBase=new ConfBase();
					confBase.setConfName(confName);
					confBase.setCreateUser(userId);
					confBase.setCompereUser(userId);
					confBase.setCompereName(username);
					confBase.setStartTime(startDate);
					confBase.setDuration(duraMinutes);
					confBase.setEndTime(endDate);
					confBase.setMaxUser(IntegerUtil.parseIntegerWithDefaultZero(maxUser));

					logger.info("-->>>>OutLook-->>OutLookServlet  创建预约会议confBase==" + confBase);
					confBase=confService.createSingleReservationConf(confBase, siteBase, userBase);
//					if(confBase!=null && confBase.getId() !=null && confBase.getId().intValue()>0){
//						retBuffer=new StringBuffer();
//						retBuffer.append("<result>0</result>");
//					}
					if(confBase!=null && confBase.getId() !=null && confBase.getId().intValue()>0){
						retBuffer=new StringBuffer();
						retBuffer.append("<conferenceid>"+confBase.getConfHwid()+"</conferenceid>");
						retBuffer.append("<chairmanlink>"+emailService.getJoinUrlForHost(confBase)+"</chairmanlink>");
						retBuffer.append("<participantlink>"+emailService.getJoinUrlForUser(confBase)+"</participantlink>");
					}else{
						retBuffer.append("<result>0</result>");
					}
				}
				break;
			case OPTTYPE_MODIFY_RES_CONF:{
					UserBase  userBase=userService.getCurrentUser(request);
					logger.info("-->>>>OutLook-->>OutLookServlet  修改预约会议" + userBase);
					SiteBase siteBase = siteService.getCurrentSiteBaseByUserLogin(request);
					int userId=userBase.getId();
					String username=userBase.getTrueName();
					String confName=request.getParameter("confsubject");
					String hwId=request.getParameter("conferenceteluri");
					logger.info("-->>>>OutLook-->>OutLookServlet  hwId" + hwId);
					ConfBase confBase=confService.getConfBaseByHwId(hwId);//new ConfBase();
					
					
					int hours=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("hourofduration"));
					int minutes=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("minuteofduration"));
					int duraMinutes=hours * 60 + minutes;
					String maxUser=request.getParameter("participantcount");
					String beginDate=request.getParameter("begindate");
					Date startDate=DateUtil.StringToDate(beginDate, "yyyy-MM-dd HH:mm");
					startDate=DateUtil.addDateSecond(startDate, 28800);
					Date endDate=DateUtil.addDateMinutes(startDate, duraMinutes);//DateUtil.StringToDate(beginDate, "yyyy-MM-dd HH:mm");
					logger.info("-->>>>OutLook-->>OutLookServlet  修改预约会startDate==" + startDate);
					logger.info("-->>>>OutLook-->>OutLookServlet  修改预约会endDate==" + endDate);
					
//					int hours=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("hourofduration"));
//					int minutes=IntegerUtil.parseIntegerWithDefaultZero(request.getParameter("minuteofduration"));
//					int duraMinutes=hours * 60 + minutes;
//					String maxUser=request.getParameter("participantcount");
//					String beginDate=request.getParameter("begindate");
//					String endDate=request.getParameter("enddate");
					confBase.setConfName(confName);
					confBase.setCreateUser(userId);
					confBase.setCompereUser(userId);
					confBase.setCompereName(username);
					confBase.setMaxUser(IntegerUtil.parseIntegerWithDefaultZero(maxUser));
					confBase.setStartTime(startDate);
					confBase.setDuration(duraMinutes);
					confBase.setEndTime(endDate);
					logger.info("-->>>>OutLook-->>OutLookServlet  修改预约会议confBase==" + confBase);
					confBase=confService.updateSingleReservationConf(confBase, siteBase, userBase);
//					if(confBase!=null && confBase.getId() !=null && confBase.getId().intValue()>0){
//						retBuffer=new StringBuffer();
//						retBuffer.append("<result>0</result>");
//					}
					if(confBase!=null && confBase.getId() !=null && confBase.getId().intValue()>0){
						retBuffer=new StringBuffer();
						retBuffer.append("<conferenceid>"+confBase.getConfHwid()+"</conferenceid>");
						retBuffer.append("<chairmanlink>"+emailService.getJoinUrlForHost(confBase)+"</chairmanlink>");
						retBuffer.append("<participantlink>"+emailService.getJoinUrlForUser(confBase)+"</participantlink>");
					}else{
						retBuffer.append("<result>0</result>");
					}
				}
				break;
			case OPTTYPE_CANCLE_RES_CONF:
		
				break;
			}
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.print(retBuffer.toString());
		printWriter.flush();
		printWriter.close();
		return null;
	}
	
	

	/**
	 * 创建即时会议
	 * @param request
	 * @param response
	 * @return
	 */
	public Object createImmConf(HttpServletRequest request,HttpServletResponse response) throws Exception{
		UserBase  userBase=userService.getCurrentUser(request);
		SiteBase siteBase = siteService.getCurrentSiteBaseByUserLogin(request);
		String hwId=request.getParameter("conferenceteluri");
		logger.info("-->>>>OutLook-->>OutLookServlet  hwId" + hwId);
		ConfBase confBase=confService.getConfBaseByHwId(hwId);//new ConfBase();
		confService.cancleSingleCycleConfInfo(confBase.getId(), siteBase, userBase);
		PrintWriter printWriter = response.getWriter();
		printWriter.print("<result>0</result>");
		printWriter.flush();
		printWriter.close();
		return null;
		
	}
	 
	
}
