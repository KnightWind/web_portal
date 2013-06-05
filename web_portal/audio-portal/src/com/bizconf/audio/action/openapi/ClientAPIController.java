package com.bizconf.audio.action.openapi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ClientAPIService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ContactService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.StringUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath(value = {"ClientAPI", "conference"})
public class ClientAPIController {
	
	private Logger logger=Logger.getLogger(ClientAPIController.class);

	@Autowired
	ClientAPIService clientAPIService;
	
	@Autowired
	ConfService confService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SiteService siteService;
	
	@Autowired
	ContactService contactService;
	
	/**
	 * 获取小参数，这个是供setup程序来调用的
	 * 
	 * @param random
	 * @return
	 */
	@AsController(path = "getPreParameter.jsp")
	public Object getPreParameter(@CParam("random") int random,HttpServletRequest request) {
		String clientIp=StringUtil.getIpAddr(request);
		String preParam = clientAPIService.getPreParam(random,clientIp);
		return new ActionForward.Text("<parameter>" + preParam + "</parameter>");
	}

	/**
	 * 获取client需要的大参数
	 * 
	 * @param random
	 * @return
	 */
	@AsController(path = "getClientParam.jsp")
	public Object getClientParameter(@CParam("random") long random) {
		String clientParams = clientAPIService.getClientParam(random);
		return new ActionForward.TextXML(clientParams == null ? "<result>random:"+ random +" invalid</result>" : clientParams);
	}

	/**
	 * 结束一个会议时被调用
	 * 
	 * @param confId
	 * @return
	 */
	@AsController(path = "terminateConf.jsp")
	public Object terminateConf(@CParam("confid") int confId) {
		int code = 0;
		logger.info(confId+"=stop");
		System.out.println(confId+"=stop");
		
		return new ActionForward.TextXML("<result>" + code + "</result>");
	}

	/**
	 * 邀请用户
	 * 
	 * @param confId
	 * @param email
	 * @param confPasswd
	 * @return
	 */
	@AsController(path = "getsiconfinvite.jsp")
	public Object invite(@CParam("confId") String confId, @CParam("email") String email,
			@CParam("confPasswd") String confPasswd, @CParam("userName") String userName) {
		
		//retrieve conf info
		ConfBase confBase =confService.getConfBaseByHwId(confId);
		
		//check conf password
		/*if (!confPasswd.equalsIgnoreCase(confBase.getUserSecure())) {
			return new ActionForward.TextXML("<ret>" + 1 + "</ret>");
		}*/
		
		//make user base
		UserBase user = new UserBase();
		user.setUserEmail(email);
		user.setClientName(userName);
		user.setLoginName(userName);
		user.setTrueName(email);
		List<UserBase> userList = new ArrayList<UserBase>();
		userList.add(user);
		 
		//send invite
		int code = emailService.sendConfInvite(userList, confBase) ? 0 : 1;
		
		return new ActionForward.TextXML("<ret>" + code + "</ret>");
	}
	
	/**
	 * 提供给客户端的查询企业通讯录的功能
	 * @param enterpriseId
	 * @param condition
	 * @param pageCount
	 * @param pageNum
	 * @param isExactQuery
	 * @return
	 */
	@AsController(path = "queryEntList.jsp")
	public Object queryEntUsers(@CParam("enterpriseId") String enterpriseId, @CParam("condition") String condition,
			@CParam("pageCount") int pageCount, @CParam("pageNum") int pageNum, @CParam("isExactQuery") String isExactQuery) {
		
		String xml = "";
		String retCode = ConstantUtil.AS_SUCCESS_CODE;
		String retContext = ConstantUtil.SUCCESS_FLAG;
		List<UserBase> datas = null;
		try{
			SiteBase site = siteService.getSiteBaseBySiteSign(enterpriseId);
			boolean isExact = false;
			if("1".equals(isExactQuery)){
				isExact = true;
			}
			PageBean<UserBase> page = contactService.getEnterpriseUsers(site.getId(), condition, pageNum, pageCount, isExact);
			if(page!=null){
				datas = page.getDatas();
			}
		}catch (Exception e) {
			retCode = String.valueOf(ConstantUtil.GLOBAL_FAIL_FLAG);
			retContext = ConstantUtil.ERROR_FLAG;
			e.printStackTrace();
		}
		xml = clientAPIService.transUserToXml(datas,retCode,retContext);
		return new ActionForward.TextXML(xml);
	}
}