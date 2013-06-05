package com.bizconf.audio.service;

import java.util.List;

import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetDataConfInfoResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapUserStatus;

/**
 * @desc 
 * @author Administrator
 * @date 2013-3-5
 */
public interface ConfManagementService {
	
	
	/**
	 * 创建会议
	 * @param confInfo
	 * @return
	 */
	String createConf(ConfBase confInfo,SiteBase currentsite,UserBase currUserBase,boolean isImmediatelyConf);
	
	/**
	 * 创建预约会议
	 * @param confInfo
	 * @param currentsite
	 * @param currUserBase
	 * @return
	 */
	String createConf(ConfBase confInfo,SiteBase currentsite,UserBase currUserBase);
	
	/**
	 * 修改会议信息
	 * @param confInfo
	 * @param currentsite
	 * @param currUserBase
	 * @return
	 */
	String updateConf(ConfBase confInfo,SiteBase currentsite,UserBase currUserBase);
	
	/**
	 * 取消一个会议
	 * @param confId
	 * @return
	 */
	boolean cancelConf(String confId,SiteBase currentsite,UserBase currUserBase);
	
	/**
	 * 查询会议信息
	 * @param confId
	 * @param currentsite
	 * @return
	 */
	ConfBase queryConfInfo(String confId,SiteBase currentsite,UserBase currUserBase);
	
	
	/**
	 * 邀请用户参加会议
	 * @param users
	 * @param currentsite
	 * @param currUserBase
	 * @return
	 */
	boolean inviteConfUser(List<ConfUser> users,String confId,SiteBase currentsite,UserBase currUserBase);
	
	/**
	 * 查询当前会议的用户状态
	 * @param confHwId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	ESpaceMeetingAsSoapUserStatus[] queryConfUserStatus(String confHwId,int pageNo,int pageSize,SiteBase currSite,UserBase currUser);

	
	/**
	 * 开始会议
	 * @param conf
	 * @param currentSite
	 * @param currUser
	 * @return
	 */
	boolean startConf(ConfBase conf,SiteBase currentSite,UserBase currUser);
	
	
	/**
	 * 修改会议信息
	 * @param confInfo
	 * @param currentsite
	 * @param currUserBase
	 * @return
	 */
	String updateConf(ConfBase confInfo,List<ConfUser> partciparts, SiteBase currentsite,UserBase currUserBase);

	
	/**
	 * 查询数据会议信息
	 * @param siteSign
	 * @param confId
	 * @return
	 */
	ESpaceMeetingAsSoapResponseGetDataConfInfoResponse queryDataConfInfo(String siteSign,String confId);

	
	/**
	 * 查询当前会议参会方的相关信息
	 * @param confId
	 * @return
	 */
	boolean queryConfUserTerminals(ConfBase conf);
	
	
	/**
	 * 更新所有正在进行会议的参会人数
	 * @return
	 */
	boolean setConfTerminalNum();
	
	
	/**
	 * 查询正在召开会议参会人状态
	 * @param confHwId
	 * @param pageNo
	 * @param pageSize
	 * @param currSite
	 * @param currUser
	 * @return
	 */
	PageBean<ConfLog> queryConfUserStatusForPage(String confHwId,int pageNo,int pageSize,SiteBase currSite,UserBase currUser);


	
	boolean setingOnlineUserNum(ConfBase conf);
}
