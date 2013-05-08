package com.bizconf.audio.entity;

import java.util.Date;

/**
 * 加入会议的随机数
 * 
 * @author Chris Gao
 * 
 */
public class JoinRandom {
	private long id;
	private int confId;
	private int userId;
	private String userName;
	private int userRole;
	private Date createTime;
	private String language;
	private String userEmail;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

}
