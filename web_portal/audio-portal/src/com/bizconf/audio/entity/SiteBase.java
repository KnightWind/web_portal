package com.bizconf.audio.entity;

import java.util.Date;

import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.util.DateUtil;


/**
 * 站点基本信息表
 * @author shhc
 */
public class SiteBase implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -548700708862480087L;
	
	/*
	 * 主键ID
	 */
	private Integer id;

	/*
	 * 站点名称
	 */
	private String siteName;
	/*
	 * 站点名称英文名称
	 */
	private String enName;
	
	/*
	 * Client显示名称
	 */
	private String clientName;
	
	/*
	 * 站点LOGO
	 * 各种不同规格的图片用逗号分隔
	 */
	private String siteLogo;
	
	/*
	 * 站点标识
	 * 删除标志为1时，不能重复
	 */
	private String siteSign;
	
	/*
	 * 站点描述
	 */
	private String siteDesc;
	
	/*
	 * 站点时区
	 */
	private Integer timeZone;
	
	/**
	 * 站点时区ID
	 */
	private Integer timeZoneId;
	/*
	 * 站点状态
	 * 试用、正式
	 */
	private Integer siteFlag = 0;
	
	/*
	 * 租用模式
	 * 包月，计时两选项，单选按钮，一个站点只能择其一
	 * 1.包月 2.计时
	 */
	private Integer hireMode;
	
	/*
	 * 计费模式
	 * 选中包月：出现三种计费模式：name host, active user, seats选项，为单选按钮
	 * 1.name host 2.active user 3.seats 4.time
	 */
	private Integer chargeMode;
	
	/*
	 * 授权数
	 * 最大并发人数
	 */
	private Integer license;
	
	/*
	 * 会议类型
	 * 1、纯数据会议、
	 *    数据+HW电话
	 *    数据+会畅电话
	 */
	private Integer mtgType;
	
	/*
	 * 锁定状态：
	 * 0、无效数据
	 * 1、解锁状态
	 * 2、锁定状态
	 */
	private Integer lockFlag = 1;
	
	/*
	 * 生效时间
	 */
	private Date effeDate;
	
	/*
	 * 过期时间
	 */
	private Date expireDate;
	
	/*
	 * 创建时间
	 */
	private Date createTime;
	
	/*
	 * 创建者ID号
	 */
	private Integer createUser;
	
	/*
	 * 删除标志 ：  0：无效数据  1、正常使用  2、已删除
	 */
	private Integer delFlag;
	
	/*
	 * 删除时间
	 */
	private Date delTime;
	
	/*
	 * 删除者ID号
	 */
	private Integer delUser;
	
	
	private String priseId;
	
	/**
	 * 是否发送到期提醒
	 */
	private Integer sendRemindFlag = 0;
	// Constructors

	/** default constructor */
	public SiteBase() {
	}

	/** minimal constructor */
	public SiteBase(Date createTime) {
		this.createTime = createTime;
	}

	/** full constructor */
	public SiteBase(String siteName,String enName, String clientName, String siteLogo,
			String siteSign, String siteDesc, Integer timeZone,Integer timeZoneId, Integer siteFlag,Integer license,
			Integer mtgType, Integer lockFlag, Date effeDate, Date expireDate, Date createTime,
			Integer createUser, Integer delFlag, Date delTime, Integer delUser,String priseId) {
		this.siteName = siteName;
		this.enName=enName;
		this.clientName = clientName;
		this.siteLogo = siteLogo;
		this.siteSign = siteSign;
		this.siteDesc = siteDesc;
		this.timeZone = timeZone;
		this.timeZoneId=timeZoneId;
		this.siteFlag = siteFlag;
		this.license = license;
		this.mtgType = mtgType;
		this.lockFlag = lockFlag;
		this.effeDate = effeDate;
		this.expireDate = expireDate;
		this.createTime = createTime;
		this.createUser = createUser;
		this.delFlag = delFlag;
		this.delTime = delTime;
		this.delUser = delUser;
		this.priseId=priseId;
	}

	// Property accessors


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	
	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getClientName() {
		return this.clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getSiteLogo() {
		return this.siteLogo;
	}

	public void setSiteLogo(String siteLogo) {
		this.siteLogo = siteLogo;
	}

	public String getSiteSign() {
		return this.siteSign;
	}

	public void setSiteSign(String siteSign) {
		this.siteSign = siteSign;
	}

	public String getSiteDesc() {
		return this.siteDesc;
	}

	public void setSiteDesc(String siteDesc) {
		this.siteDesc = siteDesc;
	}
	
	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getSiteFlag() {
		return this.siteFlag;
	}

	public void setSiteFlag(Integer siteFlag) {
		this.siteFlag = siteFlag;
	}

	public Integer getLicense() {
		return this.license;
	}

	public void setLicense(Integer license) {
		this.license = license;
	}

	public Integer getMtgType() {
		return this.mtgType;
	}

	public void setMtgType(Integer mtgType) {
		this.mtgType = mtgType;
	}

	public Integer getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(Integer lockFlag) {
		this.lockFlag = lockFlag;
	}
	
	public Integer getHireMode() {
		return hireMode;
	}

	public void setHireMode(Integer hireMode) {
		this.hireMode = hireMode;
	}

	public Integer getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(Integer chargeMode) {
		this.chargeMode = chargeMode;
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
	
	
	public Integer getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(Integer timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	
	public String getPriseId() {
		return priseId;
	}

	public void setPriseId(String priseId) {
		this.priseId = priseId;
	}
	
	//added by Chris Gao
	public String getTimeZoneDesc() {
		return ResourceHolder.getInstance().getResource("website.timezone.city." + this.timeZoneId);
	}
	
	//added by jack wang
	public String getFullTimeZoneDesc() {
		return ResourceHolder.getInstance().getResource("website.timezone.city.zone." + this.timeZoneId);
	}
	
	
	public Date getSiteLocalTime() {
		return DateUtil.getOffsetDateByGmtDate(DateUtil.getGmtDate(null), 
				(long)this.getTimeZone().intValue());
	}
	
	public Integer getSendRemindFlag() {
		return sendRemindFlag;
	}

	public void setSendRemindFlag(Integer sendRemindFlag) {
		this.sendRemindFlag = sendRemindFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	public void init(){
		if(this.siteLogo==null){
			this.siteLogo = "";
		}
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SiteBase other = (SiteBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SiteBase [id=" + id + ", siteName=" + siteName + ", enName="
				+ enName + ", clientName=" + clientName + ", siteLogo="
				+ siteLogo + ", siteSign=" + siteSign + ", siteDesc="
				+ siteDesc + ", timeZone=" + timeZone + ", timeZoneId="
				+ timeZoneId + ", siteFlag=" + siteFlag + ", hireMode="
				+ hireMode + ", chargeMode=" + chargeMode + ", license="
				+ license + ", mtgType=" + mtgType + ", lockFlag=" + lockFlag
				+ ", effeDate=" + effeDate + ", expireDate=" + expireDate
				+ ", createTime=" + createTime + ", createUser=" + createUser
				+ ", delFlag=" + delFlag + ", delTime=" + delTime
				+ ", delUser=" + delUser + ", priseId=" + priseId + "]";
	}

 

}