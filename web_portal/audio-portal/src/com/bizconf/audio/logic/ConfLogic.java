package com.bizconf.audio.logic;

import java.util.HashMap;
import java.util.List;

import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfCycle;
import com.bizconf.audio.entity.DefaultConfig;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;

public interface ConfLogic {
	
	/**
	 * 创建会议时验证站点所剩license（重新创建会议）
	 * wangyong
	 * 2013-3-14
	 */
	public boolean createConfLicenseVali(ConfBase conf, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser);
	
	/**
	 * 修改会议时验证站点所剩license
	 * wangyong
	 * 2013-3-14
	 */
	public boolean updateConfLicenseVali(ConfBase conf, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser);
	
	/**
	 * 保存会议时验证会议数据
	 * wangyong
	 * 2013-3-4
	 */
	public boolean saveConfValidate(ConfBase conf, ConfCycle confCycle, SiteBase siteBase);
	
//
//	/**
//	 * 根据时间范围取剩余的License数
//	 * @param request
//	 * @param beginTime
//	 * @param duration
//	 * @return  String 日期：格式 2013-01-01,Integer  剩余 的License数
//	 * 
//	 */
//	public HashMap<String,Integer> getSurplusLicense(ConfBase confBase,ConfCycle confCycle, SiteBase siteBase);
	
	/**
	 * 根据时间范围取剩余的License数
	 * @param confBase
	 * @param confCycle
	 * @param siteBase
	 * @param currentUser
	 * @return  String 日期：格式 2013-01-01,Integer  剩余的License数
	 * 
	 */
	public HashMap<String, Integer> getEffetLicense(ConfBase confBase, ConfCycle confCycle, SiteBase siteBase, UserBase currentUser);
	
	/**
	 * 获取当前会议的所属站点
	 * @return
	 */
	public SiteBase getConfSiteBase(int siteId);
	
	
	/**
	 * 获取当前周期会议的所属周期类型
	 * @param cycIds
	 * @return
	 */
	List<ConfCycle> getConfCycles(List<Integer> cycIds);
	
	/**
	 * 获取会议的周期ID（如果是周期会议的话）
	 * @param confs
	 * @return
	 */
	List<Integer> getCycIds(List<ConfBase> confs);
	
	/**
	 * 获取会议周期对象如果会议时周期会议的话
	 * @param confs
	 * @return
	 */
	List<ConfCycle> getConfCyclesByConf(List<ConfBase> confs);
	
	/**
	 * 获取会议周期
	 * @param conf
	 * @return
	 */
	ConfCycle getConfCycleByConf(ConfBase conf);
	
	
	/**
	 * 获取该用户创建的会议列表
	 * @param creatorId
	 * @return
	 */
	List<ConfBase> getConfBasesByCreator(Integer creatorId);
	
	
	/**
	 * 修改会议CryptKey
	 * @param conf
	 * @return
	 */
	boolean updateConfCryptKey(ConfBase conf);
	
	/**
	 * 后台配置客户端功能缺省设置，与前台配置无关
	 * 1.前台配置完成后，调用配置后台客户端默认功能
	 * 笔记、视频、音频、聊天、公告、私聊、组聊、投票（默认全部开启，但不在页面上配置）
	 * wangyong
	 * 2013-4-10
	 */
	public void setServerClientConfig(char[] clientConfig);
	
	
	/**
	 * 根据华为ID查询会议的创建者
	 * @param confHwId
	 * @return
	 */
	public UserBase getConfCreator(String confHwId);
	
	/**
	 * 创建即时会议的权限控制以及特殊变量赋值
	 * 1. 创建即时会议，initConf()初始化会议信息后调用
	 * wangyong
	 * 2013-7-4
	 */
	public void immediatelyConfAuthority(ConfBase conf, UserBase user);
}
