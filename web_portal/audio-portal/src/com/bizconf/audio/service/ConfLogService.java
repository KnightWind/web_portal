package com.bizconf.audio.service;

import java.util.List;
import java.util.Map;

import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.PageBean;

public interface ConfLogService {
	
	/**
	 * 保存进出会日志
	 * @param confLog
	 * @return
	 */
	public boolean saveConfLog(ConfLog confLog);
	
	/**
	 * 批量保存进出会日志
	 * @param logList
	 * @return
	 */
	public boolean batchSaveConfLog(List<ConfLog> logList);
	
	/**
	 * 删除进会日志
	 * @param confLog
	 * @return
	 */
	public boolean delConfLog(ConfLog confLog);
	
	
	/**
	 * 根据会议ID号查询与会者进出会日志
	 * @param confId
	 * @return
	 */
	public List<ConfLog> getLogListByConfId(Integer confId);
	
	
	/**
	 * 根据用户的ID号查询用户的进出会日志
	 * @param userId
	 * @return
	 */
	public List<ConfLog> getLogListByUserId(Integer userId);
	
	/**
	 * 根据会议ID号统计参会人数
	 * @param confId
	 * @return
	 * 		Integer[0]  会议ID
	 * 		Integer[1]	参会人总人数
	 * 		Integer[2]	PC用户数
	 * 		Integer[3]  电话用户数
	 * 
	 */
	public Integer[] countUserByConfId(Integer confId);

	/**
	 * 根据会议ID数组,统计参会人数
	 * @param confIds
	 * @return
	 * 		Integer[0]  会议ID
	 * 		Integer[1]	参会人总人数
	 * 		Integer[2]	PC用户数
	 * 		Integer[3]  电话用户数
	 * 
	 */
	public List<Integer[]> countUserListByConfId(Integer[] confIds);
	
	
	
	/**
	 * 获取某个用户参加的会议记录
	 * @param confs
	 * @param creatorId
	 * @return
	 */
	Map<Integer, ConfLog> getConfLogDataMap(List<ConfBase> confs,Integer creatorId);
	
	
	/**
	 * 通过会议ID获取会议日志
	 * @param confId
	 * @return
	 */
	PageBean<ConfLog> getLogsByConf(Integer confId,Integer pageNo);
	
	/**
	 * 通过会议ID获取会议日志
	 * @param confId
	 * @return
	 */
	List<ConfLog> getAllLogsByConf(Integer confId);
	/**
	 * 获取会议-参会人次map
	 * @param confs
	 * @return
	 */
	Map<Integer, Integer> getConflogNumByConf(List<ConfBase> confs);
}
