package com.bizconf.audio.entity;

import java.util.Date;

import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConfStatus;
import com.bizconf.audio.util.DateUtil;

public class ConfBaseSimple implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -556421752454369136L;
	
	private Integer id;
	/*
	 * 对应华为会议ID号
	 * */
	private String confHwid;// = String.valueOf(System.currentTimeMillis());
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
	 * 会议描述
	 * */
	private String confDesc;
	
	/*
	 * 是否公开会议，初始化默认为非公开会议
	 */
	private Integer publicFlag = ConfConstant.CONF_PUBLIC_FLAG_FALSE;
	
	/*
	 * 公开会议密码
	 * 注:公开会议需要有公开会议密码
	 */
	private String publicConfPass = "";
	
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
	 * 会议时间长度，分钟 为单位
	 * */
	private Integer duration;

	
	/*
	 * 会议结束时间
	 * */
	private Date endTime;
	/*
	 * 主持人ID号
	 * */
	private Integer compereUser;
	/*
	 * 主持人姓名
	 * */
	private String compereName;
	/*
	 * 主持人会议安全号
	 * */
	private String compereSecure = "";
	/*
	 * 与会者会议安全号
	 * */
	private String userSecure = "";
	
	/*
	 * 启动会议使用,AS返回的cryptkey
	 */
	private String 	cryptKey="";
	/*
	 * 电话会议号
	 * */
	private String callPhone = "";
	/*
	 * 电话会议密码
	 * */
	private String phonePass = "";
	/*
	 * 会议最大人数
	 * */
	private Integer maxUser = 0;
	/*
	 * 最大音频数
	 * */
	private Integer maxAudio = 0;
	/*
	 * 最大视频数
	 * */
	private Integer maxVideo = 0;
	/*
	 * 视频设置类型
//	 * 1.优先保证视频流畅
//	 * 2.优先保证画质清晰
//	 * 3.优先保证网络带宽
	 * 45.优先保证视频流畅(默认分辨率是4，最大分辨率是5)
	 * 48.优先保证画质清晰(默认分辨率是4，最大分辨率是8)
	 * 02.优先保证网络带宽(默认分辨率是0，最大分辨率是2)
	 * */
	private String videoType = "45";
	/*
	 * 最大分辨率
	 * */
	private String maxDpi = "";
	/*
	 * 默认分辨率
	 * */
	private String defaultDpi = "";
	/*
	 * 提前入会时间
	 * */
	private Integer aheadTime = 0;
	/*
	 * 是否开启Ipad功能
	 * */
	private Integer openIpad = 0;
	
	/*
	 * 会议的功能位由01 字符串组成, 32 位, 每一位表示一个特定的功能
		• 第1 位表示该会议会控时, 是否需要对操作者鉴权
		• 第2 位表示服务器混音
		• 第3 位表示自动外邀
		• 第4 位表示是否录制
		• 第5 位表示标清或高清会议
		• 第6 位表示是否智真会议
		• 第7 位表示是否使用安全会议号
		• 第8 位表示会议模式(0 : 自由模式; 1 : 主持人模式)
		• 第9 位表示是否为安全会议
		• 第10 位表示该会议是否通过用户pin 码入会
		• 第11 位表示麦克风状态是否关闭(0 : 关闭; 1 : 打开)
		• 第12 位表示是否是UC 会议(0: 否; 1: 是)
		• 第13 位表示是否启用会场放音(0: 否; 1: 是)
		• 第14 位表示是否启用录制会场录音(0: 否; 1: 是)
		• 第15 位表示创建语音会议时是否需要预留多媒体资源(0: 否;1: 是)
		• 第16 位表示创建会议时IVR 的放音类型(0: 标识播放预配置的语音; 1: 标识静默方式，即不放音)
		• 第17 位表示是否启用入会过滤功能(0：否，1：是)
		• 第18 位表示是否支持超大会场(0：否，1：是)
		• 第19 位表示预约会议时是否屏蔽创建会场(0：不屏蔽；1：屏蔽)
		• 第20 位表示该预约会议是否加密会议(0：非加密会议；1：加密会议)
		• 第21 位表示是否勾选Internet Lock-Out Controls(0 表示禁用涉外会议,Internet 用户可接入
			 */
	private String funcBits = "";
	
	/*
	 * 权限配置管理：共32位，（1：使用，0：不使用）
	 *   第1位：换页 
		第2位：批注 
		第3位：与所有人聊天 
		第4位：与主持人聊天 
		第5位：与参会人聊天 
	 *   其它全为0
	 */
	private String priviBits = "";
	
	
	
	/*
	 * Client功能配置
	 * 共34位，0是不启用，1是启用  
		第2位	文档共享
		第3位	屏幕共享
		第4位	白板
		第5位	笔记
		第6位         投票
		第8位	视频
		第9位	音频
		第10位	聊天
		第11位	公告
		第12位	文件传输
		第13位	录制
		第15位	会议助理
		第16位	私聊
		第17位	组聊
		第22位	媒体共享
	 * */
	private String clientConfig = "";
	
	/*
	 * 是否已经调用SOAP创建
	 * 		0:无效数据
	 * 		1：未创建
	 * 		2：已创建
	 * */
//	private Integer soapStatus = 2;
	private Integer soapStatus = ConfConstant.CONF_SOAP_STATUS_TRUE;
	/*
	 * 版本号：记录会议的修改次数
	 * */
	private Integer confVersion = 0;
	/*
	 * 创建时间
	 * */
	private Date createTime = DateUtil.getGmtDate(null);
	/*
	 * 创建者ID号
	 * */
	private Integer createUser;
	/*
	 * 创建会议者类别
	 * 		0：无效数据
	 * 		1、站点管理员
	 * 		2、普通（企业）用户
	 * 		999、系统管理员
	 * */
	private Integer createType = 2;
	/*
	 * 删除标志 ： 
	 * 		0：无效数据  
	 * 		1、正常使用  
	 * 		2、已删除
	 * */
	private Integer delFlag = 1;
	/*
	 * 删除时间
	 * */
	private Date delTime;
	/*
	 * 删除用户的ID号
	 * */
	private Integer delUser = 0;
	/*
	 * 删除用户的类别
	 * 		0：无效数据
	 * 		1、站点管理员
	 * 		2、普通（企业）用户
	 * 		999、系统管理员
	 * */
	private Integer delType = 2;
	
	
	/*
	 * 站点时区
	 */
	private Integer timeZone;
	
	/**
	 * 站点时区ID
	 */
	private Integer timeZoneId;
	
	/**
	 * 当前会议的PC端
	 */
	private Integer pcNum = 0;
	
	/**
	 * 电话会议终端
	 */
	private Integer phoneNum = 0;
	
	/**
	 * 主持人密码
	 */
	private String hostKey = "";
	/**
	 * 是否永久会议
	 */
	private Integer permanentConf = 0; //0：非永久会议    1:永久主会议 2:永久会议子会议
	
	
	private Integer belongConfId = 0;//所属永久会议ID 如果为永久会议时使用   默认：0  

	
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

	public ConfBaseSimple(Integer id, String confHwid, Integer siteId,
			Integer cycleId, String confName, String confDesc,
			Integer publicFlag, String publicConfPass, Integer confType,
			Date startTime, Integer duration, Date endTime,
			Integer compereUser, String compereName, String compereSecure,
			String userSecure, String cryptKey, String callPhone,
			String phonePass, Integer maxUser, Integer maxAudio,
			Integer maxVideo, String videoType, String maxDpi,
			String defaultDpi, Integer aheadTime, Integer openIpad,
			String funcBits, String priviBits, String clientConfig,
			Integer soapStatus, Integer confVersion, Date createTime,
			Integer createUser, Integer createType, Integer delFlag,
			Date delTime, Integer delUser, Integer delType, Integer timeZone,
			Integer timeZoneId, Integer pcNum, Integer phoneNum,
			String hostKey, Integer permanentConf, Integer belongConfId,
			Object confStatus) {
		super();
		this.id = id;
		this.confHwid = confHwid;
		this.siteId = siteId;
		this.cycleId = cycleId;
		this.confName = confName;
		this.confDesc = confDesc;
		this.publicFlag = publicFlag;
		this.publicConfPass = publicConfPass;
		this.confType = confType;
		this.startTime = startTime;
		this.duration = duration;
		this.endTime = endTime;
		this.compereUser = compereUser;
		this.compereName = compereName;
		this.compereSecure = compereSecure;
		this.userSecure = userSecure;
		this.cryptKey = cryptKey;
		this.callPhone = callPhone;
		this.phonePass = phonePass;
		this.maxUser = maxUser;
		this.maxAudio = maxAudio;
		this.maxVideo = maxVideo;
		this.videoType = videoType;
		this.maxDpi = maxDpi;
		this.defaultDpi = defaultDpi;
		this.aheadTime = aheadTime;
		this.openIpad = openIpad;
		this.funcBits = funcBits;
		this.priviBits = priviBits;
		this.clientConfig = clientConfig;
		this.soapStatus = soapStatus;
		this.confVersion = confVersion;
		this.createTime = createTime;
		this.createUser = createUser;
		this.createType = createType;
		this.delFlag = delFlag;
		this.delTime = delTime;
		this.delUser = delUser;
		this.delType = delType;
		this.timeZone = timeZone;
		this.timeZoneId = timeZoneId;
		this.pcNum = pcNum;
		this.phoneNum = phoneNum;
		this.hostKey = hostKey;
		this.permanentConf = permanentConf;
		this.belongConfId = belongConfId;
		this.confStatus = confStatus;
	}






	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfHwid() {
		return confHwid;
	}

	public void setConfHwid(String confHwid) {
		this.confHwid = confHwid;
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

	public String getConfDesc() {
		return confDesc;
	}

	public void setConfDesc(String confDesc) {
		this.confDesc = confDesc;
	}

	public Integer getPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(Integer publicFlag) {
		this.publicFlag = publicFlag;
	}

	public String getPublicConfPass() {
		return publicConfPass;
	}

	public void setPublicConfPass(String publicConfPass) {
		this.publicConfPass = publicConfPass;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCompereUser() {
		return compereUser;
	}

	public void setCompereUser(Integer compereUser) {
		this.compereUser = compereUser;
	}

	public String getCompereName() {
		return compereName;
	}

	public void setCompereName(String compereName) {
		this.compereName = compereName;
	}

	public String getCompereSecure() {
		return compereSecure;
	}

	public void setCompereSecure(String compereSecure) {
		this.compereSecure = compereSecure;
	}

	public String getUserSecure() {
		return userSecure;
	}

	public void setUserSecure(String userSecure) {
		this.userSecure = userSecure;
	}

	public String getCryptKey() {
		return cryptKey;
	}

	public void setCryptKey(String cryptKey) {
		this.cryptKey = cryptKey;
	}

	public String getCallPhone() {
		return callPhone;
	}

	public void setCallPhone(String callPhone) {
		this.callPhone = callPhone;
	}

	public String getPhonePass() {
		return phonePass;
	}

	public void setPhonePass(String phonePass) {
		this.phonePass = phonePass;
	}

	public Integer getMaxUser() {
		return maxUser;
	}

	public void setMaxUser(Integer maxUser) {
		this.maxUser = maxUser;
	}

	public Integer getMaxAudio() {
		return maxAudio;
	}

	public void setMaxAudio(Integer maxAudio) {
		this.maxAudio = maxAudio;
	}

	public Integer getMaxVideo() {
		return maxVideo;
	}

	public void setMaxVideo(Integer maxVideo) {
		this.maxVideo = maxVideo;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

	public String getMaxDpi() {
		return maxDpi;
	}

	public void setMaxDpi(String maxDpi) {
		this.maxDpi = maxDpi;
	}

	public String getDefaultDpi() {
		return defaultDpi;
	}

	public void setDefaultDpi(String defaultDpi) {
		this.defaultDpi = defaultDpi;
	}

	public Integer getAheadTime() {
		return aheadTime;
	}

	public void setAheadTime(Integer aheadTime) {
		this.aheadTime = aheadTime;
	}

	public Integer getOpenIpad() {
		return openIpad;
	}

	public void setOpenIpad(Integer openIpad) {
		this.openIpad = openIpad;
	}

	public String getFuncBits() {
		return funcBits;
	}

	public void setFuncBits(String funcBits) {
		this.funcBits = funcBits;
	}

	public String getPriviBits() {
		return priviBits;
	}

	public void setPriviBits(String priviBits) {
		this.priviBits = priviBits;
	}

	public String getClientConfig() {
		return clientConfig;
	}

	public void setClientConfig(String clientConfig) {
		this.clientConfig = clientConfig;
	}

	public Integer getSoapStatus() {
		return soapStatus;
	}

	public void setSoapStatus(Integer soapStatus) {
		this.soapStatus = soapStatus;
	}

	public Integer getConfVersion() {
		return confVersion;
	}

	public void setConfVersion(Integer confVersion) {
		this.confVersion = confVersion;
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

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Date getDelTime() {
		return delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}

	public Integer getDelUser() {
		return delUser;
	}

	public void setDelUser(Integer delUser) {
		this.delUser = delUser;
	}

	public Integer getDelType() {
		return delType;
	}

	public void setDelType(Integer delType) {
		this.delType = delType;
	}

	public Integer getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(Integer timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getTimeZoneId() {
		return timeZoneId;
	}

	public void setTimeZoneId(Integer timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	public Integer getPcNum() {
		return pcNum;
	}

	public void setPcNum(Integer pcNum) {
		this.pcNum = pcNum;
	}

	public Integer getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(Integer phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getHostKey() {
		return hostKey;
	}

	public void setHostKey(String hostKey) {
		this.hostKey = hostKey;
	}

	public Integer getPermanentConf() {
		return permanentConf;
	}

	public void setPermanentConf(Integer permanentConf) {
		this.permanentConf = permanentConf;
	}

	public Integer getBelongConfId() {
		return belongConfId;
	}

	public void setBelongConfId(Integer belongConfId) {
		this.belongConfId = belongConfId;
	}

	public Object getConfStatus() {
		return confStatus;
	}

	public void setConfStatus(Object confStatus) {
		this.confStatus = confStatus;
	}

	@Override
	public String toString() {
		return "ConfBaseSimple [id=" + id + ", confHwid=" + confHwid
				+ ", siteId=" + siteId + ", cycleId=" + cycleId + ", confName="
				+ confName + ", confDesc=" + confDesc + ", publicFlag="
				+ publicFlag + ", publicConfPass=" + publicConfPass
				+ ", confType=" + confType + ", startTime=" + startTime
				+ ", duration=" + duration + ", endTime=" + endTime
				+ ", compereUser=" + compereUser + ", compereName="
				+ compereName + ", compereSecure=" + compereSecure
				+ ", userSecure=" + userSecure + ", cryptKey=" + cryptKey
				+ ", callPhone=" + callPhone + ", phonePass=" + phonePass
				+ ", maxUser=" + maxUser + ", maxAudio=" + maxAudio
				+ ", maxVideo=" + maxVideo + ", videoType=" + videoType
				+ ", maxDpi=" + maxDpi + ", defaultDpi=" + defaultDpi
				+ ", aheadTime=" + aheadTime + ", openIpad=" + openIpad
				+ ", funcBits=" + funcBits + ", priviBits=" + priviBits
				+ ", clientConfig=" + clientConfig + ", soapStatus="
				+ soapStatus + ", confVersion=" + confVersion + ", createTime="
				+ createTime + ", createUser=" + createUser + ", createType="
				+ createType + ", delFlag=" + delFlag + ", delTime=" + delTime
				+ ", delUser=" + delUser + ", delType=" + delType
				+ ", timeZone=" + timeZone + ", timeZoneId=" + timeZoneId
				+ ", pcNum=" + pcNum + ", phoneNum=" + phoneNum + ", hostKey="
				+ hostKey + ", permanentConf=" + permanentConf
				+ ", belongConfId=" + belongConfId + ", confStatus="
				+ confStatus + "]";
	}



}