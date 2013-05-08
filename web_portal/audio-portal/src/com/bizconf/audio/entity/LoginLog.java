package com.bizconf.audio.entity;

import java.util.Date;

/**
 * 用户登录日志
 * @author shhc
 *
 */
public class LoginLog implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -5043399579810413351L;
	
	/**
	 * 主键ID
	 */
	private Long id;
	
	/**
	 * 站点ID号
	 */
	private Integer siteId;
	
	/**
	 * 用户ID号
	 */
	private Integer userId;

	/*
	 * 用户类别  
	 * 对应 ConstantUtil.USERTYPE_**  对应
	 */
	
	private Integer userType;
	/**
	 * 登入时间
	 */
	private Date loginTime;
	
	/**
	 * 退出时间
	 */
	private Date logoutTime;
	
	/**
	 * 登录的IP地址
	 */
	private String loginIp;

	// Constructors

	/** default constructor */
	public LoginLog() {
	}

	 
	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginLog other = (LoginLog) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoginLog [id=" + id + ", siteId=" + siteId + ", userId="
				+ userId + ", userType=" + userType + ", loginTime="
				+ loginTime + ", logoutTime=" + logoutTime + ", loginIp="
				+ loginIp + "]";
	}

}