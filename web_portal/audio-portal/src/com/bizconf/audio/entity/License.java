package com.bizconf.audio.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.util.DateUtil;

/**
 * 站点授权记录
 * @author shhc
 *
 */
public class License implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 8230405720717412487L;
	
	/**
	 * 主键ID号
	 */
	private Integer id;

	/**
	 * 站点ID号
	 */
	private Integer siteId;
	/**
	 * 用户ID号
	 */
	private Integer userId;
	
	/**
	 * 授权数
	 */
	private Integer licNum;
	
	/**
	 * 生效开始日期
	 */
	private Date effeDate;
	
	/**
	 * 生效结束日期
	 */
	private Date expireDate;
	
	
	private Integer effeFlag = 0;//生效标志 0：未生效 1:生效
	
	private Integer expireFlag = 0;//失效标志0:未失效 1:已失效
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建者ID号
	 */
	private Integer createUser;
	
	/**
	 * 删除标志
	 */
	private Integer delFlag;
	
	/**
	 * 删除时间
	 */
	private Date delTime;
	
	/**
	 * 删除者ID号
	 */
	private Integer delUser;

	// Constructors
	
	
	/** default constructor */
	public License() {
	}

	public Integer getEffeFlag() {
		return effeFlag;
	}

	public void setEffeFlag(Integer effeFlag) {
		this.effeFlag = effeFlag;
	}

	public Integer getExpireFlag() {
		return expireFlag;
	}

	public void setExpireFlag(Integer expireFlag) {
		this.expireFlag = expireFlag;
	}

	/** full constructor */
	public License(Integer siteId,Integer userId, Integer licNum, Date effeDate,
			Date expireDate, Date createTime, Integer createUser,
			Integer delFlag, Date delTime, Integer delUser) {
		this.siteId = siteId;
		this.userId=userId;

		this.licNum = licNum;
		this.effeDate = effeDate;
		this.expireDate = expireDate;
		this.createTime = createTime;
		this.createUser = createUser;
		this.delFlag = delFlag;
		this.delTime = delTime;
		this.delUser = delUser;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getLicNum() {
		return licNum;
	}

	public void setLicNum(Integer licNum) {
		this.licNum = licNum;
	}

	public Date getEffeDate() {
		return this.effeDate;
	}

	public void setEffeDate(Date effeDate) {
		this.effeDate = effeDate;
	}

	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Integer getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Date getDelTime() {
		return this.delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}

	public Integer getDelUser() {
		return this.delUser;
	}

	public void setDelUser(Integer delUser) {
		this.delUser = delUser;
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
		License other = (License) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "License [id=" + id + ", siteId=" + siteId + ", userId="
				+ userId + ", licNum=" + licNum + ", effeDate=" + effeDate
				+ ", expireDate=" + expireDate + ", createTime=" + createTime
				+ ", createUser=" + createUser + ", delFlag=" + delFlag
				+ ", delTime=" + delTime + ", delUser=" + delUser + "]";
	} 
	
	/**
	 * 初始化默认值
	 */
	public void init(){
//		this.siteId = 0;
		if(userId==null){
			this.userId=0;
		}
//		this.licNum = 0;
		if(this.effeDate!=null){
			this.effeDate = DateUtil.getGmtDate(this.effeDate);
		}
		if(this.expireDate!=null){
			this.expireDate = DateUtil.getGmtDate(this.expireDate);
		}
		this.createTime = DateUtil.getGmtDate(null);
		this.createUser = 0;
		this.delFlag = ConstantUtil.DELFLAG_UNDELETE;
		try {
//			DateUtil.getGmtDateByTimeZone(date, offset);
			setDelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.delUser = 0;
	}
	
}