package com.bizconf.audio.service;

import java.util.Date;
import java.util.List;

import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.UserBase;

/**
 * 用户会议关系维护服务接口
 * <p>
 * 
 * @author Chris Gao
 *
 */
public interface ConfUserService {
	/**
	 * 创建会议完成后调用
	 * 
	 * @param conf
	 * @param hostUser
	 * @return
	 */
	public boolean fillConfUserForCreate(ConfBase conf, UserBase hostUser);
	
	/**
	 * 创建周期会议完成后调用
	 * <p>
	 * 
	 * @param confList
	 * @param hostUser
	 * @return
	 */
	public boolean fillConfUserForCreateCycle(List<ConfBase> confList, UserBase hostUser);
	
	/**
	 * 修改单个会议完成后调用
	 * <p>
	 * @param conf
	 * @param hostUser
	 * @return
	 */
	public boolean fillConfUserForModify(ConfBase conf, UserBase hostUser);
	
	/**
	 * 修改周期会议完成后调用
	 * <p>
	 * @param conf
	 * @param hostUser
	 * @return
	 */
	public boolean fillConfUserForModifyCycle(List<ConfBase> confList, UserBase hostUser);
	
	/**
	 * 取消单个会议调用
	 * <p>
	 * 
	 * @param conf
	 * @param confId
	 * @return
	 */
	public boolean fillConfUserForCancel(int confId, UserBase hostUser);
	
	/**
	 * 取消周期会议调用
	 * <p>
	 * 
	 * @param conf
	 * @param cycleId
	 * @return
	 */
	public boolean fillConfUserForCancelCycle(int cycleId, UserBase hostUser);
	
	/**
	 * 邀请用户调用
	 * 
	 * @param conf
	 * @param inviteUsers 被邀请的用户列表，如果是非注册用户，请构造：userId=0
	 * @return
	 */
	public boolean fillConfUserForInvite(ConfBase conf, List<UserBase> inviteUsers);
	
	/**
	 * 会议状态更新后调用 [confTask]
	 * 
	 * @param confId
	 * @param confStatus
	 * @return
	 */
	public boolean fillConfUserForConfStatusUpdate(int confId, int confStatus);
	
	/**
	 * 获取某会议的邀请用户列表
	 * 
	 * @param confId
	 * @return
	 */
	public List<ConfUser> getConfInviteUserList(int confId);
	
	
	/**
	 * 获取某会议的所有参会用户
	 * @param confId
	 * @return
	 */
	public List<ConfUser> getAllConfUserList(int confId);
	/**
	 * 接受邀请
	 * 
	 * @param confUserId
	 * @return
	 */
	public boolean recvInvite(int confUserId);
	
	/**
	 * 谢绝邀请
	 * 
	 * @param confUserId
	 * @return
	 */
	public boolean refuseInvite(int confUserId);
	
	/**
	 * 获取处理接受邀请的链接地址
	 * 
	 * @param confId
	 * @param userId
	 * @param userName
	 * @param userEmail
	 * @return
	 */
	public String retrieveAcceptURI(int confId, int userId, String userName, String userEmail);
	
	/**
	 * 获取处理拒绝邀请的链接地址
	 * 
	 * @param confId
	 * @param userId
	 * @param userName
	 * @param userEmail
	 * @return
	 */
	public String retrieveRefuseURI(int confId, int userId, String userName, String userEmail);
	
	
	/**
	 * 取出会议开始前
	 * @param aheadMinute
	 * @param scopeMinute
	 * @return
	 */
	public List<ConfUser> getConfUserListForRemind(Integer aheadMinute,Integer scopeMinute);
	
	
	/**
	 * 更新会议用户中的邮件提醒标志
	 * @param confUser
	 * @return
	 */
	public boolean updateRemindFlag(ConfUser confUser);
	

	/**
	 * 更新会议的开始时间
	 * @param confId
	 * @param date
	 * @return
	 */
	public boolean updateStartTime(Integer confId,Date startTime);
	
	
}
