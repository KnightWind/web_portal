package com.bizconf.audio.entity;

import java.util.Date;

import com.bizconf.audio.util.DateUtil;

/**
 * 会议信息--循环会议设置
 * @author shhc
 *
 */
public class ConfCycle implements java.io.Serializable {
	// Fields
	private static final long serialVersionUID = -8834082789962143901L;
	
	private Integer id;
	
	/*
	 * 站点ID号
	 * */
	private Integer siteId = 0;
	
	/*
	 * 循环类型
	 * 0：无效类型
	 * 1：日循环会议
	 * 2：周循环会议
	 * 3：月循环会议
	 * */
	private Integer cycleType = 0;
	
	/*
	 * 根据周期范围、周期类型、周期值取到整个周期中的日期列表
	 * 
	 * @param {Object} beginDate  开始日期，字符串格式：2013-1-1(或者 2013-01-01)
	 * @param {Object} endDate  结束日期，字符串格式：2013-1-1(或者 2013-01-01)
	 * @param {Object} cycleType  
	 *  			1：按日循环；		2：按周循环；		3：按月循环；	
	 * @param {Object} cycleValue
	 * 			按日循环时：数字
	 *      	按周循环时：由周日、到周六的编码组成的字符串,中间用英文的逗号分开，如：2,3,4,5(表示每周周一、周二、周三、周四)
	 *      				对应关系：  1==周日；2==周一；3==周二；4==周三；5==周四；6==周五；7==周六
	 *      
	 *      	按月循环时：
	 *      			如果是每月第几天时，Value是一个数字（数字>=1并且<=31）
	 *      
	 *      			由周数+周对应的编码组成的字符串,第几周对应的数字与周几用英文的分号分开，如：2;2（表示每月第2周的周一的会议）
	 * 
	 * 
	 * @param {Object} isWorkDay   如果是按天、按月（每月第几天）循环时，调用 是否为工作日 。true ：范围中的工作日； false ：工作日+周末
	 * 			工作日：周一到周五按工作日算
	 * 			周末按非工作日算
	 */
	private String cycleValue;
	/*
	 * 循环开始日期
	 * */
	private Date beginDate;

	/*
	 *循环结束时间
	 * */
	private Date endDate;

	/*
	 * 创建时间
	 * */
	private Date createTime = DateUtil.getGmtDate(null);
	/*
	 * 周期会议的创建者id
	 * */
	private Integer createUser;

	// Constructors

	/** default constructor */
	public ConfCycle() {
	}

	public ConfCycle(Integer id, Integer siteId, Integer cycleType,
			String cycleValue, Date beginDate, Date endDate, Date createTime,
			Integer createUser) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.cycleType = cycleType;
		this.cycleValue = cycleValue;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.createTime = createTime;
		this.createUser = createUser;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getCycleType() {
		return cycleType;
	}

	public void setCycleType(Integer cycleType) {
		this.cycleType = cycleType;
	}

	public String getCycleValue() {
		return cycleValue;
	}

	public void setCycleValue(String cycleValue) {
		this.cycleValue = cycleValue;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
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
		ConfCycle other = (ConfCycle) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConfCycle [id=" + id + ", siteId=" + siteId + ", cycleType="
				+ cycleType + ", cycleValue=" + cycleValue + ", beginDate="
				+ beginDate + ", endDate=" + endDate + ", createTime="
				+ createTime + ", createUser=" + createUser + "]";
	}
 
}