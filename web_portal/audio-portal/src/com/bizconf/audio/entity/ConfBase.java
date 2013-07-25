package com.bizconf.audio.entity;

import java.util.Date;

import com.bizconf.audio.component.language.ResourceHolder;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConfStatus;
import com.bizconf.audio.util.DateUtil;

public class ConfBase implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -8834082789962143901L;
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
	 * 会议开始时间备份（从数据库中取出的GMT时间备份）
	 * */
	private Date startTimeGmt = null;
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
	private Integer maxAudio = -1;
	/*
	 * 最大视频数
	 * */
	private Integer maxVideo = -1;
	/*
	 * 视频设置类型
//	 * 1.优先保证视频流畅
//	 * 2.优先保证画质清晰
//	 * 3.优先保证网络带宽
	 * 45.优先保证视频流畅(默认分辨率是4，最大分辨率是5)
	 * 48.优先保证画质清晰(默认分辨率是4，最大分辨率是8)
	 * 03.优先保证网络带宽(默认分辨率是0，最大分辨率是3)
	 * */
	private String videoType = "";
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
	 * 会议状态
	 *  0,//预约成功                
		2,//正在召开              
		3,//已结束 
		9,//取消的会议
	 * */
	private Integer confStatus = ConfStatus.SCHEDULED.getStatus();
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
	
	
	
	/** default constructor */
	public ConfBase() {
	}

	// Constructors
	public ConfBase(Integer id, String confHwid, Integer siteId,
			Integer cycleId, String confName, String confDesc,
			Integer publicFlag, String publicConfPass, Integer confType,
			Date startTime, Integer duration, Date endTime,
			Integer compereUser, String compereName, String compereSecure,
			String userSecure, String callPhone, String phonePass,
			Integer maxUser, Integer maxAudio, Integer maxVideo,
			String videoType, String maxDpi, String defaultDpi,
			Integer aheadTime, Integer openIpad, String funcBits,
			String priviBits, String clientConfig, Integer confStatus,
			Integer soapStatus, Integer confVersion, Date createTime,
			Integer createUser, Integer createType, Integer delFlag,
			Date delTime, Integer delUser, Integer delType, Integer timeZone,
			Integer timeZoneId) {
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
		this.confStatus = confStatus;
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
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfHwid() {
		return this.confHwid;
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
		return this.cycleId;
	}

	public void setCycleId(Integer cycleId) {
		this.cycleId = cycleId;
	}
	
	public Integer getPublicFlag() {
		return publicFlag;
	}

	public void setPublicFlag(Integer publicFlag) {
		if(publicFlag != null){
			this.publicFlag = publicFlag;
		}else{
			this.publicFlag = ConfConstant.CONF_PUBLIC_FLAG_FALSE;
		}
	}

	public String getPublicConfPass() {
		return publicConfPass;
	}

	public void setPublicConfPass(String publicConfPass) {
		this.publicConfPass = publicConfPass;
	}

	public String getConfName() {
		return this.confName;
	}

	public void setConfName(String confName) {
		this.confName = confName;
	}

	public String getConfDesc() {
		return this.confDesc;
	}

	public void setConfDesc(String confDesc) {
		this.confDesc = confDesc;
	}

	public Integer getConfType() {
		return this.confType;
	}

	public void setConfType(Integer confType) {
		this.confType = confType;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		if(this.startTimeGmt == null){
			this.startTimeGmt = startTime;
		}
	}

	public Date getStartTimeGmt() {
		return startTimeGmt;
	}

	public void setStartTimeGmt(Date startTimeGmt) {
		this.startTimeGmt = startTimeGmt;
	}

	public Integer getDuration() {
		if(this.duration!=null && this.duration.intValue()>0){
			return this.duration;
		}else if(this.endTime!=null){
			Long l = new Long((this.endTime.getTime()-this.startTime.getTime())/60000);
			return l.intValue();
		}else{
			return 0;
		}
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getEndTime() {
//		if(this.startTime==null){
//			this.endTime=null;
//		}else{
//			Calendar calendar=Calendar.getInstance();
//			calendar.setTime(this.startTime);
//			if(duration!=null && duration.intValue() != 0){
//				calendar.add(Calendar.MINUTE, duration);
//			}
//			endTime=calendar.getTime();
//			calendar=null;
//		}
		
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCompereUser() {
		return this.compereUser;
	}

	public void setCompereUser(Integer compereUser) {
		this.compereUser = compereUser;
	}

	public String getCompereName() {
		return this.compereName;
	}

	public void setCompereName(String compereName) {
		this.compereName = compereName;
	}

	public String getCompereSecure() {
		return this.compereSecure;
	}

	public void setCompereSecure(String compereSecure) {
		this.compereSecure = compereSecure;
	}

	public String getUserSecure() {
		return this.userSecure;
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
		return this.callPhone;
	}

	public void setCallPhone(String callPhone) {
		this.callPhone = callPhone;
	}

	public String getPhonePass() {
		return this.phonePass;
	}

	public void setPhonePass(String phonePass) {
		this.phonePass = phonePass;
	}

	public Integer getMaxUser() {
		return this.maxUser;
	}

	public void setMaxUser(Integer maxUser) {
		this.maxUser = maxUser;
	}

	public Integer getMaxAudio() {
		return this.maxAudio;
	}

	public String getFuncBits() {
		return funcBits;
	}

	public void setFuncBits(String funcBits) {
		this.funcBits = funcBits;
	}
	
	public boolean isAudioServerMixed() {
		if (funcBits == null || funcBits.length() < 2) {
			return false;
		}
		if (funcBits.charAt(1) == '1') {
			return true;
		}
		return false;
	}

	public String getPriviBits() {
		return priviBits;
	}

	public void setPriviBits(String priviBits) {
		this.priviBits = priviBits;
	}

	public void setMaxAudio(Integer maxAudio) {
		this.maxAudio = maxAudio;
	}

	public Integer getMaxVideo() {
		return this.maxVideo;
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
		return this.maxDpi;
	}

	public void setMaxDpi(String maxDpi) {
		this.maxDpi = maxDpi;
	}

	public String getDefaultDpi() {
		return this.defaultDpi;
	}

	public void setDefaultDpi(String defaultDpi) {
		this.defaultDpi = defaultDpi;
	}

	public Integer getAheadTime() {
		return this.aheadTime;
	}

	public void setAheadTime(Integer aheadTime) {
		this.aheadTime = aheadTime;
	}

	public Integer getOpenIpad() {
		return this.openIpad;
	}

	public void setOpenIpad(Integer openIpad) {
		this.openIpad = openIpad;
	}

	public String getClientConfig() {
		return this.clientConfig;
	}

	public void setClientConfig(String clientConfig) {
		this.clientConfig = clientConfig;
	}

	public Integer getConfStatus() {
		return this.confStatus;
	}

	public void setConfStatus(Integer confStatus) {
		this.confStatus = confStatus;
	}

	public Integer getSoapStatus() {
		return this.soapStatus;
	}

	public void setSoapStatus(Integer soapStatus) {
		this.soapStatus = soapStatus;
	}

	public Integer getConfVersion() {
		return this.confVersion;
	}

	public void setConfVersion(Integer confVersion) {
		this.confVersion = confVersion;
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

	public Integer getCreateType() {
		return this.createType;
	}
	
	public void setCreateType(Integer createType) {
		this.createType = createType;
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

	public Integer getDelType() {
		return this.delType;
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

	public void setPhoneNum(Integer phoneNum) {
		this.phoneNum = phoneNum;
	}

	public boolean isPublic() {
		return this.publicFlag.intValue() == ConfConstant.CONF_PUBLIC_FLAG_TRUE.intValue();
	}
	
	//added by Chris Gao
	public String getTimeZoneDesc() {
		return ResourceHolder.getInstance().getResource("website.timezone.city." + this.timeZoneId);
	}
	
	/**
	 * 当前时间是否允许进会
	 * @return true 允许进会   false  不允许进会
	 * shhc
	 * 2013-7-2
	 */
	public boolean getJoinTimeFlag() {
		Date nowGmtDate = DateUtil.getGmtDate(null);
		Date aheadGmtDate = DateUtil.addDateMinutes(nowGmtDate, this.aheadTime);
//		long startTimeStamp = DateUtil.getGmtDateByTimeZone(this.startTime, this.timeZone).getTime(); 
		long startTimeStamp = this.startTimeGmt.getTime(); 
		long gmtTimeStamp = aheadGmtDate.getTime();
		nowGmtDate=null;
		aheadGmtDate=null;
		return gmtTimeStamp >= startTimeStamp;
	}
	
	/**
	 * 获取当前会议时区的时间
	 * wangyong
	 * 2013-3-28
	 */
	public Date getConfLocalTime() {
		return DateUtil.getOffsetDateByGmtDate(DateUtil.getGmtDate(null), 
				(long)this.getTimeZone().intValue());
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	/**
	 * 掉用华为AS接口创建会议时调用
	 * mediabits媒体类型。01字符串，0表示选取，1不选取
		第1位：音频
		第2位：视频
		第3位：数据如001标识只有数据媒体，111表示多媒体媒体都有
	 */
	public String getMediaBits() {
		if (this.confType.intValue() == 2 || this.confType.intValue() == 3) {
			 return "111";
		}
		return "101";
	}
	
	/**
	 * 是否有电话功能
	 */
	public boolean hasPhoneFunc() {
		if (this.confType.intValue() == 1 || this.confType.intValue() == 3) {
			 return true;
		}
		return false;
	}
	
	/**
	 * 是否有视频功能
	 */
	public boolean hasVideo() {
		if (this.confType.intValue() == 2 || this.confType.intValue() == 3) {
			 return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfBase other = (ConfBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean isBelongPermanentConf(){
		if(this.getPermanentConf()!=null && this.getPermanentConf().intValue() ==1){
			return true;
		}
		else if(getEndTime()!=null && (getEndTime().getTime() - getStartTime().getTime())>24*3600000l){
			setPermanentConf(1);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "ConfBase [id=" + id + ", confHwid=" + confHwid + ", siteId="
				+ siteId + ", cycleId=" + cycleId + ", confName=" + confName
				+ ", confDesc=" + confDesc + ", publicFlag=" + publicFlag
				+ ", publicConfPass=" + publicConfPass + ", confType="
				+ confType + ", startTime=" + startTime + ", duration="
				+ duration + ", endTime=" + endTime + ", compereUser="
				+ compereUser + ", compereName=" + compereName
				+ ", compereSecure=" + compereSecure + ", userSecure="
				+ userSecure + ", cryptKey=" + cryptKey + ", callPhone="
				+ callPhone + ", phonePass=" + phonePass + ", maxUser="
				+ maxUser + ", maxAudio=" + maxAudio + ", maxVideo=" + maxVideo
				+ ", videoType=" + videoType + ", maxDpi=" + maxDpi
				+ ", defaultDpi=" + defaultDpi + ", aheadTime=" + aheadTime
				+ ", openIpad=" + openIpad + ", funcBits=" + funcBits
				+ ", priviBits=" + priviBits + ", clientConfig=" + clientConfig
				+ ", confStatus=" + confStatus + ", soapStatus=" + soapStatus
				+ ", confVersion=" + confVersion + ", createTime=" + createTime
				+ ", createUser=" + createUser + ", createType=" + createType
				+ ", delFlag=" + delFlag + ", delTime=" + delTime
				+ ", delUser=" + delUser + ", delType=" + delType
				+ ", timeZone=" + timeZone + ", timeZoneId=" + timeZoneId
				+ ", pcNum=" + pcNum + ", phoneNum=" + phoneNum + ", hostKey="
				+ hostKey + ", permanentConf=" + permanentConf
				+ ", belongConfId=" + belongConfId + "]";
	}

}