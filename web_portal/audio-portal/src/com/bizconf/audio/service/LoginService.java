package com.bizconf.audio.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

	/**
	 * 站点管理员与普通用户的登录
	 * <p>
	 * 登录成功后，写入session<br/>
	 * uid=userId <br/>
	 * sessionid=md5(uid+"-"+siteSign+"-"+key) <br/>
	 * cookie domain:xx.yyy.zz <br/>
	 * */
	public int login(String loginName, String loginPass, String authCode, 
			HttpServletResponse response, HttpServletRequest request)
			throws Exception;
	
	/**
	 * 站点管理员登录
	 * <p>
	 * 登录成功后，写入session<br/>
	 * suid=userId <br/>
	 * sessionid=md5(uid+"-"+siteSign+"-"+key) <br/>
	 * cookie domain:xx.yyy.zz <br/>
	 * */
	public int loginSiteAdmin(String loginName, String loginPass, String authCode, 
			HttpServletResponse response, HttpServletRequest request)
			throws Exception;

	/**
	 * 系统管理员登录
	 * <p>
	 * 登录成功后，写入session<br/>
	 * said=sysadmin userId <br/>
	 * sessionid=md5(uid+"-"+"sysadmin"+"-"+key) <br/>
	 * cookie domain:yyy.zz <br/>
	 * */
	public int loginSysAdmin(String loginName, String loginPass, String authCode, 
			HttpServletResponse response, HttpServletRequest request)
			throws Exception;
	
	/**
	 * 用户对象退出登录
	 * <p>
	 * 删除uid和sessionid的cookie
	 * */
	public boolean logout(HttpServletResponse response,
			HttpServletRequest request) throws Exception;

	/**
	 * 站點管理员退出
	 * <p>
	 * 删除suid和sessionid的cookie
	 * */
	public boolean logoutSiteAdmin(HttpServletResponse response,
			HttpServletRequest request) throws Exception;
	
	/**
	 * 系统管理员退出
	 * <p>
	 * 删除said和sessionid的cookie
	 * */
	public boolean logoutSysAdmin(HttpServletResponse response,
			HttpServletRequest request) throws Exception;
	
	/**
	 * 
	 * 用户是否登录
	 * <p>
	 * 验证uid,sessionid算法
	 * 
	 * @param request
	 * @return
	 */
	public boolean isLogined(HttpServletRequest request);
	
	/**
	 * 站点管理员是否登录
	 * <p>
	 * 验证suid,sessionid算法
	 * @param request
	 * @return
	 */
	public boolean isSiteAdminLogined(HttpServletRequest request);
	
	/**
	 * 验证系统管理员是否登录
	 * <p>
	 * 验证said,sessionid算法
	 * @param request
	 * @return
	 */
	public boolean isSysAdminLogined(HttpServletRequest request);
}
