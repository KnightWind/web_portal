package com.bizconf.audio.action.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.service.ValidCodeService;
import com.bizconf.audio.util.IntegerUtil;
import com.bizconf.audio.util.JsonUtil;
import com.bizconf.audio.util.PasswordUrlUtil;
import com.bizconf.audio.util.SiteIdentifyUtil;
import com.bizconf.audio.util.StringUtil;
import com.bizconf.encrypt.MD5;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Get;
import com.libernate.liberc.annotation.httpmethod.Post;
@ReqPath("password")
public class ResetPasswordController  extends BaseController {
	private final  Logger logger = Logger.getLogger(ResetPasswordController.class);
	@Autowired
	UserService userService;
	@Autowired
	ValidCodeService validCodeService;
	@Autowired
	EmailService emailService;
	@Autowired
	SiteService siteService;
	
	@AsController(path = "forget")
	public Object forget(){
		return new ActionForward.Forward("/jsp/admin/password_forget.jsp");
	}
	

	
	
	@AsController(path = "sendEmail")
	@Post
	public Object sendUrlToEmail(@CParam("authCode") String authCode, @CParam("type") String type,
			@CParam("random") String random, HttpServletRequest request){
		Map<String, Object> results = new HashMap<String, Object>();
		String systemEmail=String.valueOf(request.getParameter("systemEmail"));
		if (!validCodeService.checkValidCode(random, type, authCode)) {
			results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
			results.put("message", "验证码错误");
			return JsonUtil.parseMapToJsonStr(results);
		}
		logger.info(" sendemail email= "+systemEmail);
		if (systemEmail==null || StringUtil.isEmpty(systemEmail)) {
			results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
			results.put("message", "请填写发送邮件地址");
			return JsonUtil.parseMapToJsonStr(results);			
		}

		String siteSign = SiteIdentifyUtil.getCurrentBrand();
		SiteBase siteBase = siteService.getSiteBaseBySiteSign(siteSign);
		UserBase adminUser = userService.getSiteAdminByEmail(siteBase.getId(), systemEmail);
		if(adminUser==null){
			results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
			results.put("message", "对不起, 没有这个用户");
			return JsonUtil.parseMapToJsonStr(results);
		}
		String domain = SiteIdentifyUtil.getCurrentDomine();
		String resetUrl = PasswordUrlUtil.getResetPasswordUrlForAdmin(adminUser,domain);
		domain=null;
		try {
			emailService.resetPasswordEmail(adminUser, resetUrl);
		} catch (Exception e) {
			e.printStackTrace();
			results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
			results.put("message", "邮件发送失败");
			return JsonUtil.parseMapToJsonStr(results);
		}
		results.put("status", ConstantUtil.EMAIL_FLAG_SECCEED);
		results.put("message", "邮件发送成功, 请即时查收!");
		return JsonUtil.parseMapToJsonStr(results);
	}
	
	
	@AsController(path = "reset")
	@Get
	public Object reset(HttpServletRequest request){
		//http://dick.bizconf.cn/system/password/reset?ts=1361177403425&sid=10&ln=zfzst1980&auth=b196678615282c92c9c34e7a0d978bd8
		String ts=String.valueOf(request.getParameter("ts"));
		String sid=String.valueOf(request.getParameter("sid"));
		String ln=String.valueOf(request.getParameter("ln"));
		String auth=String.valueOf(request.getParameter("auth"));
		if(sid==null || "".equals(sid.trim()) || "null".equals(sid.trim().toLowerCase())){
			//链接地址错误,
			return new ActionForward.Forward("/jsp/admin/password_reset.jsp");
		}
		Integer sysId=IntegerUtil.parseInteger(sid);
		if(sysId==null || sysId.intValue() <= 0){
			//链接地址错误,
			return new ActionForward.Forward("/jsp/admin/password_reset.jsp");
		}
		
		UserBase adminUser = null;
		adminUser = userService.getUserBaseById(sysId);
		if(adminUser==null){
			//链接地址错误,
			return new ActionForward.Forward("/jsp/admin/password_reset.jsp");
		}else{
			boolean tsStatus=PasswordUrlUtil.isExpireTimeStamp(ts);
			//已经过期
			if(tsStatus){
				return new ActionForward.Forward("/jsp/admin/password_reset.jsp");
			}
			boolean  autoStatus=PasswordUrlUtil.checkSiteAdminAuthCode(adminUser, auth, ts);
			//autoCode错误
			if(!autoStatus){
				return new ActionForward.Forward("/jsp/admin/password_reset.jsp");
			}
			
			
		}
		request.setAttribute("ts", ts);
		request.setAttribute("sid", sid);
		request.setAttribute("ln", ln);
		request.setAttribute("auth", auth);
		return new ActionForward.Forward("/jsp/admin/password_reset.jsp");
	}

	
	@AsController(path = "save")
	@Post
	public Object savePassword(HttpServletRequest request){
		String ts=String.valueOf(request.getParameter("ts"));
		String sid=String.valueOf(request.getParameter("sid"));
		String ln=String.valueOf(request.getParameter("ln"));
		String auth=String.valueOf(request.getParameter("auth"));
		String lp=String.valueOf(request.getParameter("lp"));
		String clp=String.valueOf(request.getParameter("clp"));

		request.setAttribute("ts", ts);
		request.setAttribute("sid", sid);
		request.setAttribute("ln", ln);
		request.setAttribute("auth", auth);
		
		Map<String, Object> results = new HashMap<String, Object>();
		if(sid==null || "".equals(sid.trim()) || "null".equals(sid.trim().toLowerCase())){
			//页面中传递来的SID是空
			results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
			results.put("message", "找回密码失败,请重新找回密码");
			return JsonUtil.parseMapToJsonStr(results);	
		}
		Integer sysId=IntegerUtil.parseInteger(sid);
		if(sysId==null || sysId.intValue() <= 0){
			//页面中传递来的SID的值小于0或者是空
			results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
			results.put("message", "找回密码失败,请重新找回密码");
			return JsonUtil.parseMapToJsonStr(results);	
		}
		
		UserBase adminUser = null;
		adminUser = userService.getUserBaseById(sysId);
		if(adminUser==null){//根据用户ID号获取不到用户对象
			//找回密码失败,请重新找回密码
			results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
			results.put("message", "找回密码失败,用户不存在");
			return JsonUtil.parseMapToJsonStr(results);	
		}else{
			if(lp==null || lp.length()<=0 ){//
				//新密码为空
				results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
				results.put("message", "找回密码失败,请输入新密码");
				return JsonUtil.parseMapToJsonStr(results);	
			}else{
				if(!clp.equals(lp)){//密码不相等
					results.put("status", ConstantUtil.EMAIL_FLAG_FAIL);
					results.put("message", "找回密码失败,两次密码不相等");
					return JsonUtil.parseMapToJsonStr(results);	
				}else{
					adminUser.setLoginPass(MD5.encodePassword(lp, "MD5"));
					userService.updateAdminPassWord(adminUser);//.updateSystemUser(systemUser);
				}
			}
		}
		results.put("status", ConstantUtil.EMAIL_FLAG_SECCEED);
		results.put("message", "密码修改成功");
		return JsonUtil.parseMapToJsonStr(results);
	}
	
	
	/**
	 * 超级站点管理员修改普通站点管理员密码后，普通站点管理员第一次登陆成功后需重置密码
	 * wangyong
	 * 2013-5-31
	 */
	@AsController(path = "resetPass")
	@Post
	public Object resetPass(HttpServletRequest request){
		Map<String, Object> results = new HashMap<String, Object>();
		UserBase currentSiteAdmin = userService.getCurrentSiteAdmin(request);
		String orgPass = request.getParameter("orgPass");
		String newPass = request.getParameter("loginPass");
		if(!StringUtil.isNotBlank(orgPass) || !StringUtil.isNotBlank(newPass)){
			results.put("status", ConstantUtil.RESET_PASS_FAILED);
			results.put("message", "请输入密码！");
			return JsonUtil.parseMapToJsonStr(results);	
		}
		if(StringUtil.isNotBlank(orgPass)){
			String inputOrgPass = MD5.encodePassword(orgPass, "MD5");
			if(!currentSiteAdmin.getLoginPass().equals(inputOrgPass)){
				results.put("status", ConstantUtil.RESET_PASS_FAILED);
				results.put("message", "原密码错误，请重新输入！");
				return JsonUtil.parseMapToJsonStr(results);	
			}
		}
		if (StringUtil.isNotBlank(newPass)) {
			currentSiteAdmin.setLoginPass(MD5.encodePassword(newPass, "MD5"));
			currentSiteAdmin.setPassEditor(currentSiteAdmin.getId());
			try {
				DAOProxy.getLibernate().updateEntity(currentSiteAdmin);
			} catch (Exception e) {
				logger.error("重置密码失败！"+e);
				results.put("status", ConstantUtil.RESET_PASS_FAILED);
				results.put("message", "重置密码失败！");
				return JsonUtil.parseMapToJsonStr(results);	
			}
		}
		results.put("status", ConstantUtil.RESET_PASS_SUCCEED);
		results.put("message", "重置密码成功！");
		return JsonUtil.parseMapToJsonStr(results);
	}
}
