package com.bizconf.audio.entity;

import java.util.Date;

import com.bizconf.audio.constant.ConfStatus;

public class ConfBaseSimple implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -556421752454369136L;
	
	private Integer id;
	/*
	 * 站点ID号
	 * */
	private Integer siteId = 0;
	/*
	 * 周期会议设置ID
	 * */
	private Integer cycleId = 0;
	/*
	 * 会议名称
	 * */
	private String confName;
	
	
	/*
	 * 会议功能：
	 * 		0、数据会议功能（页面什么都不勾选）
	 * 		1 、电话功能
	 * 		2、视频功能 
	 * 		3、视频功能 + 电话功能
	 * */
	private Integer confType = 0;
	/*
	 * 会议开始时间
	 * */
	private Date startTime;

	
	/*
	 * 会议结束时间
	 * */
	private Date endTime;

	
	/*
	 * 会议状态
	 * 		0：无效状态
	 * 		1、预约成功
	 * 		2、正在召开
	 * 		3、已结束
	 * 		4、已过期
	 * 		5、取消的会议
	 * 		6、锁定
	 * */
	private Object confStatus = ConfStatus.SCHEDULED.getStatus();
	
	// Constructors

	/** default constructor */
	public ConfBaseSimple() {
	}

	public ConfBaseSimple(Integer id, Integer siteId, Integer cycleId,
			String confName, Integer confType, Date startTime, Date endTime,
			Object confStatus) {
		super();
		this.id = id;
		this.siteId = siteId;
		this.cycleId = cycleId;
		this.confName = confName;
		this.confType = confType;
		this.startTime = startTime;
		this.endTime = endTime;
		this.confStatus = confStatus;
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

	public Integer getCycleId() {
		return cycleId;
	}

	public void setCycleId(Integer cycleId) {
		this.cycleId = cycleId;
	}

	public String getConfName() {
		return confName;
	}

	public void setConfName(String confName) {
		this.confName = confName;
	}

	public Integer getConfType() {
		return confType;
	}

	public void setConfType(Integer confType) {
		this.confType = confType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Object getConfStatus() {
		return confStatus;
	}

	public void setConfStatus(Object confStatus) {
		this.confStatus = confStatus;
	}

	@Override
	public String toString() {
		return "ConfBaseSimple [id=" + id + ", siteId=" + siteId + ", cycleId="
				+ cycleId + ", confName=" + confName + ", confType=" + confType
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", confStatus=" + confStatus + "]";
	}

}