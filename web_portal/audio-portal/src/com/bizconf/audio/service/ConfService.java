package com.bizconf.audio.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bizconf.audio.entity.Condition;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfCycle;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.ConfUserCount;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;


public interface ConfService {

	/**
	 * 创建一个即时会议
	 * @param conf 会议基本信息
	 * @param siteBase 站点基本信息
	 * @param user 创建会议用户基本信息
	 */
	public ConfBase createImmediatelyConf(ConfBase conf,SiteBase siteBase, UserBase user);
	
	/**
	 * 创建一个单次预约会议
	 * @param conf 会议基本信息
	 * @param siteBase 站点基本信息
	 * @param user 创建会议用户基本信息
	 */
	public ConfBase createSingleReservationConf(ConfBase conf,SiteBase siteBase, UserBase user);
	
	/**
	 * 重新创建一个单次预约会议
	 * 1.只能创建单次预约会议
	 * 2.保留非周期会议信息
	 * 3.copy参会人，主持人与新会议的关联
	 * 4.不是修改，是新建
	 * 5.重新给参会人发送邮件
	 * @param conf 会议基本信息
	 * @param siteBase 站点基本信息
	 * @param user 创建会议用户基本信息
	 */
	public ConfBase reCreateconf(ConfBase conf,SiteBase siteBase, UserBase user);
	
	/**
	 * 修改单次预约会议
	 * @param conf 会议基本信息
	 * @param siteBase 站点基本信息
	 * @param user 创建会议用户基本信息
	 */
	public ConfBase updateSingleReservationConf(ConfBase conf,SiteBase siteBase, UserBase user);
	
	/**
	 * 当前登录用户是否有权限修改一个预约会议
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 */
	public boolean updateConfAuthority(Integer confId, UserBase currentUser);
	
	/**
	 * 当前登录用户是否有权限重新创建一个预约会议
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 */
	public boolean recreateConfAuthority(Integer confId, UserBase currentUser);
	
	/**
	 * 当前登录用户是否有权限取消一个预约会议
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 */
	public boolean cancleConfAuthority(Integer confId, UserBase currentUser);
	
	/**
	 * 当前登录用户是否有权限邀请参会人
	 * @param conf 会议基本信息
	 * @param currentUser 当前登录用户
	 */
	public boolean inviteConfAuthority(Integer confId, UserBase currentUser);
	
	/**
	 * 主持人删除(取消)整个周期预约会议
	 * @param cycleId 周期会议id
	 * @param currentSite 当前站点
	 * @param currentUser 当前登录用户
	 */
	public boolean cancleCycleConfInfo(Integer cycleId, SiteBase currentSite, UserBase currentUser);
	
	/**
	 * 主持人删除(取消)周期预约会议中的一条会议
	 * @param confId 会议id
	 * @param currentSite 当前站点
	 * @param currentUser 当前登录用户
	 */
	public boolean cancleSingleCycleConfInfo(Integer confId, SiteBase currentSite, UserBase currentUser);
	
	/**
	 * 取消一个预约会议
	 * @param confId 会议id
	 * @param currentSite 当前站点
	 * @param currentUser 当前登录用户
	 */
	public boolean cancleSingleReservationConf(Integer confId, SiteBase currentSite, UserBase currentUser);
	
	/**
	 * 隐藏一个错过的会议
	 * @param confId 会议id
	 * @param currentUser 当前登录用户
	 * wangyong
	 * 2013-4-12
	 */
	public boolean hideMissConf(Integer confId, UserBase currentUser);
	
	/**
	 * 创建周期预约会议
	 * @param conf 会议基本信息
	 * @param currentSite 当前站点
	 * @param currentUser 当前用户
	 */
	public ConfBase createCycleReservationConf(ConfBase conf, ConfCycle confCycle, SiteBase currentSite, UserBase currentUser);

	/**
	 * 修改周期预约会议中的一条会议
	 * 流程：
	 * 1.将该次周期会议改为单次预约会议，即cycleId改为0
	 * 2.copy一份参会用户（包括主持人）到参会用户表中，并且conf_id设置为本次会议的confId，cycle_id设置为0
	 * @param conf 会议基本信息
	 * @param currentSite 当前站点
	 * @param currentUser 当前用户
	 */
	public ConfBase updateSingleCycleConfInfo(ConfBase conf,SiteBase currentSite, UserBase currentUser);
	
	/**
	 * 修改周期预约会议
	 * 注意：不可修改循环设置部分
	 * @param conf 会议基本信息
	 * @param currentSite 当前站点
	 * @param currentUser 当前用户
	 */
	public ConfBase updateCycleConfInfo(ConfBase conf,SiteBase currentSite, UserBase currentUser);
	
	//查询周期会议信息
	
	/**
	 * 主持人邀请参会人
	 * @param participantsList 参会人
	 * @param currentUser 当前登录用户
	 */
	public boolean inviteParticipants(Integer confId, Integer cycleId, List<UserBase> participantsList, UserBase currentUser);
	
	/**
	 * 站点管理员根据会议主题查询会议列表
	 * @param subject 会议主题
	 * @param siteId  站点ID
	 * @param sortField  排序字段
	 * @param sortord    排序方式
	 * @param pageModel  分页信息
	 * @param siteUserId  普通站点管理员id(当超级站点管理员查询时，传入null即可)
	 * 
	 */
	public List<ConfBase> getConfListBySubject(SiteBase currentSite, String subject, Integer siteId, String sortField, String sortord, PageModel pageModel, Integer siteUserId);
	
	/**
	 * 站点管理员根据会议主题统计会议总数
	 * @param subject 会议主题
	 * @param siteId  站点ID
	 * @param siteUserId  普通站点管理员id(当超级站点管理员查询时，传入null即可)
	 * 
	 */
	public int countConfListBySubject(String subject, Integer siteId, Integer siteUserId);
	
	/**
	 * 站点管理员根据高级搜索条件查询会议列表(权限控制条件可以放在condition中)
	 * @param condition  高级搜索条件
	 * @param sortField  排序字段
	 * @param sortord    排序方式
	 * @param pageModel  分页信息
	 * 
	 */
	public List<ConfBase> getConfListByCondition(SiteBase currentSite, Condition condition,String sortField, String sortord, PageModel pageModel);
	
	/**
	 * 站点管理员根据高级搜索条件统计会议总数(权限控制条件可以放在condition中)
	 * @param condition  高级搜索条件
	 * 
	 */
	public int countConfListByCondition(Condition condition);
	
	
	//加入会议
	

	
	/**
	 * 系统管理员对会议的基本查询
	 * @param titleOrSiteSign
	 * @param sortField
	 * @param sortord
	 * @param pageModel
	 * @param sysUserId  系统管理员id(当超级管理员查询时，传入null即可)
	 * @return
	 */
	public List<ConfBase> getConfListByBaseCondition(String titleOrSiteSign,String sortField,String sortord,PageModel pageModel, Integer sysUserId);
	
	/**
	 * 系统统计符合条件 的会议总数(基本搜索情况下)
	 * @param titleOrSiteSign
	 * @param sysUserId  系统管理员id(当超级管理员查询时，传入null即可)
	 * @return
	 */
	public Integer countConfListByBaseCondition(String titleOrSiteSign, Integer sysUserId);
	
	
	/**
	 * 系统管理员会议的高级搜索
	 * @param siteName
	 * @param siteSign
	 * @param beginTime  会议开始时间开始于
	 * @param endTime    会议开始时间结束于
	 * @param sortField
	 * @param sortord
	 * @param pageModel
	 * @param sysUserId  系统管理员id(当超级管理员查询时，传入null即可)
	 * @return
	 */
	public List<ConfBase> getConfListByAdvanceCondition(ConfBase confBase,String siteName,String siteSign,
			Date beginTime, Date endTime, String sortField, String sortord, PageModel pageModel, Integer sysUserId);
	
	/**
	 * 系统管理员下对会议高级搜索时统计会议总数
	 * @param siteName
	 * @param siteSign
	 * @param beginTime  会议开始时间开始于
	 * @param endTime    会议开始时间结束于
	 * @param sysUserId  系统管理员id(当超级管理员查询时，传入null即可)
	 * @return
	 */
	public Integer countConfListByAdvanceCondition(ConfBase confBase,String siteName,String siteSign,Date beginTime, Date endTime, Integer sysUserId);

	/**
	 * 根据主持人会议安全号取到会议信息
	 * wangyong
	 * 2013-3-11
	 */
	public ConfBase getConfBaseByCompereSecure(String compereSecure);
	
	/**
	 * 根据与会者会议安全号取到会议信息
	 * wangyong
	 * 2013-3-11
	 */
	public ConfBase getConfBaseByUserSecure(String userSecure);
	
	/**
	 * 根据cycleId号获取预约成功的会议信息(站点时区时间)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	public ConfBase getConfBasebyCycleId(int cycleId, SiteBase currentSite);
	
	/**
	 * 根据cycleId号获取预约成功的会议信息(gmt时区时间)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	public ConfBase getConfBasebyCycleId(int cycleId);
	
	/**
	 * 根据会议ID号获取会议信息
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	public ConfBase getConfBasebyConfId(int confId);
	
	/**
	 * 根据会议ID号获取周期会议周期信息
	 * @param cycleId  周期会议ID号
	 * 2013-2-21
	 */
	public ConfCycle getConfCyclebyConfId(int cycleId);
	
	/**
	 * 通过会议ID获取邀请人信息
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	public List<ConfUser> getConfInviteUser(int confId);
	
	/**
	 * 根据会议ID号获取会议信息(可获得日期为站点所设时区的会议信息)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	public ConfBase getConfBasebyConfId(int confId, SiteBase currentSite);
	
	/**
	 * 获取所有周期预约会议的日期
	 * @param cycleId  循环会议ID号
	 * 2013-2-21
	 */
	public List<ConfBase> getCycleConfDate(int cycleId, SiteBase currentSite);
	
	/**
	 * 根据会议ID号获取会议信息(可获得日期为用户喜好设置时区的会议信息)
	 * @param confId  会议ID号
	 * 2013-2-21
	 */
	public ConfBase getConfBasebyConfId(int confId, UserBase currentUser);
	
	/**
	 * 获取用户喜好设置时区所有周期预约会议的日期
	 * @param cycleId  循环会议ID号
	 * 2013-2-21
	 */
	public List<ConfBase> getCycleConfDate(int cycleId, UserBase currentUser);
	
	/**
	 * 根据会议ID号获取参加会议人数列表
	 * @param confId
	 * @return
	 */
	public List<ConfUser> getConfUserListByConfId(Integer confId);
	
	/**
	 * 根据会议的ID号获取参加会议的总人数
	 * @param confId
	 * @return
	 */
	public Integer countConfUserByConfId(Integer confId);
	

	/**
	 * 根据会议的周期ID号获取参加会议人数列表
	 * @param confId
	 * @return
	 */
	public List<ConfUser> getConfUserListByCycleId(Integer cycleId);
	
	/**
	 * 根据周期会议的周期ID号获取会议的总人数
	 * @param cycleId
	 * @return
	 */
	public Integer countConfUserByCycleId(Integer cycleId);
	
	
	
	
	/**
	 * 根据会议的ID号统计周期会议的参会人数
	 * @param ids
	 * @return  Object[]  0：会议的ID号     1:会议人数
	 */
	
	public List<ConfUserCount> countConfUserByConfIds(Integer[] confIds);
	
	/**
	 * 根据周期会议的周期ID号统计周期会议的参会人数
	 * @param cycleIds
	 * @return  Object[]  0：Cycle ID号     1:会议人数
	 */
	public List<ConfUserCount> countConfUserByCycleIds(Integer[] cycleIds);
	
	
	
	//站点管理员查询会议列表
	
	
	//站点用户查询自己创建的与被邀请的所有 会议
	
	/**
	 * 统计站点用户查询与自己相关正在进行中会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 */
	public Integer countDuringConfList(String titleOrHostName, UserBase currentUser);
	
	/**
	 * 站点用户查询与自己相关正在进行中会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 */
	public List<ConfBase> listDuringConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite);
	
	/**
	 * 统计站点用户查询与自己相关即将开始会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 * @param days 几天内即将开始会议,null代表所有会议
	 */
	public Integer countUpcomingConfList(String titleOrHostName, UserBase currentUser, Integer days);
	
	/**
	 * 站点用户查询与自己相关即将开始会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 * @param days 几天内即将开始会议,null代表所有会议
	 */
	public List<ConfBase> listUpcomingConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite, Integer days);
	
	
	/**
	 * 统计站点用户查询与自己相关错过的会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 * @param days 几天内错过的会议,null代表所有会议
	 * @param hideFlag 查询隐藏的会议（主控面板查询未隐藏的会议，不关心隐藏则传null）
	 */
	public Integer countMissConfList(String titleOrHostName, UserBase currentUser, Integer days, Integer hideFlag);
	
	/**
	 * 站点用户查询与自己相关错过的会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 * @param days 几天内错过的会议,null代表所有会议
	 * @param hideFlag 查询隐藏的会议（主控面板查询未隐藏的会议，不关心隐藏则传null）
	 */
	public List<ConfBase> listMissConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite, Integer days, Integer hideFlag);
	
	
	/**
	 * 统计站点用户查询与自己相关已经加入过会议的条数
	 * @param titleOrHostName 会议主题或主持人
	 * @param currentUser 当前站点用户
	 * @param days 几天内已经加入过的会议,null代表所有会议
	 */
	public Integer countAttendedConfList(String titleOrHostName, UserBase currentUser, Integer days);
	
	/**
	 * 站点用户查询与自己相关已经加入过会议
	 * @param titleOrHostName 会议主题或主持人
	 * @param sortField   排序字段
	 * @param sortord     排序方式
	 * @param pageModel   分页对象
	 * @param currentUser 当前站点用户
	 * @param days 几天内已经加入过的会议,null代表所有会议
	 */
	public List<ConfBase> listAttendedConfList(String titleOrHostName, PageModel pageModel, String sortField, String sortord, UserBase currentUser, SiteBase currentSite, Integer days);
	
	//站点用户根据会议ID号重新创建相同配置的会议
	
	/**
	 * 这里获取一个会议列表去构造会议状态同步的任务，包括：<br>
	 * 1、正在进行的会议<br>
	 * 2、开始时间在当前时间点之前，但会议状态是未开始的<br>
	 * 3、开始时间在当前时间点之后一段时间内的会议<br>
	 * 
	 * @param period 获取的时间段，单位：ms
	 * 
	 * @author Chris Gao [gaohl81@gmail.com]
	 * */
	public List<ConfBase> getListForStatusMonitor(long period);
	
	/**
	 * 根据查询sSpace Meeting AS接口所返回的会议状态，同步到本地存储
	 * <p>
	 * 1、根据AS查询会议状态 <br>
	 * 2、如果会议状态与conf的状态不一致，则更新到本地，否则忽略 <br>
	 * 
	 * @TODO:调用AS接口，得到会议真实状态。
	 * 目前利用开始时间逻辑来模拟得到了真实的会议状态，并同步到本地
	 * 
	 * @param conf 
	 * @author Chris Gao [gaohl81@gmail.com]
	 * 
	 * @return
	 */
	public boolean syncConfStatus(ConfBase conf);
	
	/**
	 * 更新会议状态为指定状态
	 * 
	 * @param conf
	 * @param status
	 * @return
	 */
	public boolean updateConfStatus(ConfBase conf, int status);
	
	/**
	 * 更新会议的crypt_key字段（当邀请参会人后，需更新crypt_key值）
	 * wangyong
	 * 2013-4-4
	 */
	public boolean updateConfCryptKey(ConfBase conf);
	
	/**
	 * 根据会议安全码取会议信息
	 * @param secureCode
	 * @return
	 */
//	public ConfBase geConfBaseBySecureCode(String secureCode);
	
	boolean saveConfUser(ConfUser confUser);
	
	/**
	 * 更新企业用户会议设置
	 * wangyong
	 * 2013-3-20
	 */
	public boolean updateConfConfig(DefaultConfig confConfig);
	
	/**
	 * 获取企业用户默认会议设置
	 * wangyong
	 * 2013-3-20
	 */
	public DefaultConfig getDefaultConfig(UserBase currentUser);
	
	/**
	 * 新增企业用户默认会议设置
	 * 1.参会人权限：换页，批注，与所有人聊天，与主持人聊天，与参会人聊天
		默认（与所有人，与主持人，与参会人勾选，其他不勾选）
	   2.会议功能：文档共享，屏幕共享，媒体共享，白板，文件传输，录制
		默认（文档，屏幕，白板，录制勾选，媒体共享不勾选）
		笔记、视频、音频、聊天、公告、会议助理、私聊、组聊（默认全部开启，但不在页面上配置）
		记住：相应修改的页面有：会议缺省设置，创建会议的第三步高级设置
	 * wangyong
	 * 2013-3-20
	 */
	public DefaultConfig saveDefaultConfig(UserBase currentUser);
	
	
	
	
	
	
	List<ConfBase> getCycConfBases(int cycId);
	
	
	public ConfBase saveConfBase(ConfBase confBase);
	
//	/**
//	 *
//	 * 取出提前多少分钟开始会议的会议列表，默认为24小时
//	 * 
//	 * 该方法不分站点，整个系统下的所有站点
//	 * @param minutes,可以为空，为空时默认为24小时
//	 * @return
//	 */
//	public List<ConfBase> getConfListForRemind(Integer minutes);

	/**
	 *
	 * 取出会议列表，为定时任务使用
	 * 
	 * @return
	 */
	public List<ConfBase> getConfListForTask();
	
	
	

	/**
	 * 更新会议的开始时间
	 * @param confBase
	 * @param startTime
	 * @return
	 */
	public boolean updateStartTime(ConfBase confBase,Date startTime);
	
	/**
	 * 更新会议的结束时间
	 * @param confBase
	 * @param startTime
	 * @return
	 */
	public boolean updateEndTime(ConfBase confBase,Date startTime);
	
	/**
	 * 根据华为ID号获取会议信息
	 * @param hwId
	 * @return
	 */
	public ConfBase getConfBaseByHwId(String hwId);
	
	/**
	 * 创建会议时拼接高级参数
	 * 页面高级参数拼接字符串
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
	 * wangyong
	 * 2013-3-18
	 */
	public String getClientConfig(HttpServletRequest request, DefaultConfig confConfig);
	
	/**
	 * 拼接会议功能字符串
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
		. 其他用0 补全
	 * wangyong
	 * 2013-3-18
	 */
	public String getFuncBits(ConfBase conf, HttpServletRequest request, DefaultConfig confConfig);
	
	/**
	 * (创建会议或修改会议时)检查是否授权站点电话功能
	 * 若未授权站点电话功能，则去掉电话功能
	 * wangyong
	 * 2013-3-28
	 */
	public ConfBase checkConfFunc(ConfBase conf, UserBase currentUser);
	
	/**
	 * 拼接参会人权限字符串
	 * 权限配置管理：共32位，（1：使用，0：不使用）
	 *  第1位：换页 
	 *  第2位：批注
	 *  第3位：与所有人聊天
	 *  第4位：与主持人聊天
	 *  第5位：与参会人聊天 
	 *  其它全为0
	 * wangyong
	 * 2013-3-18
	 */
	public String getPriviBits(HttpServletRequest request, DefaultConfig confConfig);
	
	/**
	 * 封装会议参数设置到request对象返回到jsp
	 * wangyong
	 * 2013-3-21
	 */
	public HttpServletRequest confConfigAttr(DefaultConfig confConfig, HttpServletRequest request);
	
	/**
	 * 封装会议权限状态到request对象返回到jsp
	 * 1.会议缺省设置页面显示已分配权限的功能
	 * 2.创建会议时页面显示已分配权限的功能
	 * wangyong
	 * 2013-4-17
	 */
	public HttpServletRequest getConfFuncFlag(UserBase currentUser, HttpServletRequest request);
	
	
	/**
	 * 获取conflogs (参会人信息)
	 * @param conf
	 * @return
	 */
	public PageBean<ConfLog> getConflogsByConf(Integer confId,Integer pageNo,Integer pageSize);
	
	
	/**
	 * 查询会议各种终端数量
	 * @param confId
	 * @param terminalType 终端类型：PC OR telephone 如果查询所有终端总数直接传null
	 * @return
	 */
	public int getTerminalNum(Integer confId,Integer terminalType);
	
	
	/**
	 * 获取会议的种终端数量
	 * @return
	 */
	public Map<Integer,Integer> getConfsTerminalNums(Collection<ConfBase> confs,Integer terminalType);
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 我主持的会议
	 * 获取今天正在召开的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getDailyOpeningConfListForHost(String confName, UserBase userBase,Integer pageNo);
	

	/**
	 * 我主持的会议
	 * 获取今天即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getDailyComingConfListForHost(String confName, UserBase userBase,Integer pageNo);
	
	/**
	 * 我主持的会议
	 * 获取今天已经加入过的的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getDailyJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo);
	
	

	/**
	 * 我主持的会议
	 * 获取本周的即将开始的会议
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getWeeklyComingConfListForHost(String confName, UserBase userBase,Integer pageNo);
	
	

	/**
	 * 我主持的会议
	 * 获取本周的已经加入过的会议
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getWeeklyJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo);


	/**
	 * 我主持的会议
	 * 取本月的即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getMonthlyComingConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime);
	


	/**
	 * 我主持的会议
	 * 取本月的已经加入过的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getMonthlyJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime);
	
	

	/**
	 * 我主持的会议
	 * 所有的即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getFullComingConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime);
	
	/**
	 * 我主持的会议
	 * 所有的加入过的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getFullJoinedConfListForHost(String confName, UserBase userBase,Integer pageNo,Date beginTime, Date endTime);
	
	
	/***************************************************************************/
	
	

	
	
	/**
	 * 我参与的会议
	 * 获取今天正在召开的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getDailyOpeningConfListForActor(String confName, UserBase userBase, int pageNo);
	

	/**
	 * 我参与的会议
	 * 获取今天即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getDailyComingConfListForActor(String confName, UserBase userBase, int pageNo);
	
	/**
	 * 我参与的会议
	 * 获取今天已经加入过的的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getDailyJoinedConfListForActor(String confName, UserBase userBase, int pageNo);
	
	

	/**
	 * 我参与的会议
	 * 获取本周的正在召开的会议
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getWeeklyOpeningConfListForActor(String confName, UserBase userBase, int pageNo);
	
	
	/**
	 * 我参与的会议
	 * 获取本周的即将开始的会议
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getWeeklyComingConfListForActor(String confName, UserBase userBase, int pageNo);
	
	

	/**
	 * 我参与的会议
	 * 获取本周的已经加入过的会议
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getWeeklyJoinedConfListForActor(String confName, UserBase userBase, int pageNo);


	/**
	 * 我参与的会议
	 * 取本月的即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getMonthlyComingConfListForActor(String confName, UserBase userBase, int pageNo,Date beginTime, Date endTime);
	


	/**
	 * 我参与的会议
	 * 取本月的已经加入过的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getMonthlyJoinedConfListForActor(String confName, UserBase userBase, int pageNo,Date beginTime, Date endTime);
	
	
	
	/**
	 * 我参与的会议
	 * 所有的即将开始的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getFullComingConfListForActor(String confName, UserBase userBase, int pageNo, Date beginTime, Date endTime);
	
	/**
	 * 我参与的会议
	 * 所有的已经加入过的会议列表
	 * @param userBase
	 * @return
	 */
	public PageBean<ConfBase> getFullJoinedConfListForActor(String confName, UserBase userBase, int pageNo, Date beginTime, Date endTime);
	
	/**
	 * 查询我创建或者参加的会议
	 * @param pageNo
	 * @param pageSize
	 * @param isHost
	 * @return
	 */
	public PageBean<ConfBase> getConfBasePage(int pageNo,int pageSize,UserBase user,boolean isCreator);
}
