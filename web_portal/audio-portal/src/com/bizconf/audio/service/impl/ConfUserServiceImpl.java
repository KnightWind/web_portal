package com.bizconf.audio.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.exception.ConfUserException;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.util.DateUtil;

@Service
public class ConfUserServiceImpl extends BaseService implements ConfUserService {
	
	@Autowired
	ConfUserLogic confUserLogic;
	
	@Autowired
	ConfService confService;
	
	@Override
	public boolean fillConfUserForCancel(int confId, UserBase hostUser) {
		return confUserLogic.deleteConfUsersByConfId(confId);
	}

	@Override
	public boolean fillConfUserForCancelCycle(int cycleId, UserBase hostUser) {
		return confUserLogic.deleteConfUsersByCycleId(cycleId);
	}

	@Override
	public boolean fillConfUserForConfStatusUpdate(int confId, int confStatus) {
		try {
			logger.info("fillConfUserForConfStatusUpdate confId:" + confId +", confStatus:" + confStatus);
			return libernate.executeSql("update t_conf_user set conf_status=? where conf_id=?", 
					new Object[]{confStatus, confId});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean fillConfUserForCreate(ConfBase conf, UserBase hostUser) {
		return confUserLogic.addConfUser(conf, hostUser);
	}

	@Override
	public boolean fillConfUserForCreateCycle(List<ConfBase> confList,
			UserBase hostUser) {
		if (confList == null || confList.size() == 0) {
			throw new ConfUserException("confList is empty.");
		}
		for (ConfBase conf : confList) {
			confUserLogic.addConfUser(conf, hostUser);
		}
		return true;
	}

	@Override
	public boolean fillConfUserForInvite(ConfBase conf,
			List<UserBase> inviteUsers) {
		if (inviteUsers == null || inviteUsers.size() == 0) {
			throw new ConfUserException("inviteUsers is empty.");
		}
		//非周期会议
		if (conf.getCycleId().intValue() == 0) {
			for (UserBase user : inviteUsers) {
				confUserLogic.addConfUser(conf, user);
			}
		}
		//周期会议
		else {
			List<ConfBase> confList = confService.getCycConfBases(conf.getCycleId());
			if (confList == null) {
				return false;
			}
			
			for (ConfBase conf2 : confList) {
				for (UserBase user : inviteUsers) {
					confUserLogic.addConfUser(conf2, user);
				}
			}
		}
		return true;
	}

	@Override
	public boolean fillConfUserForModify(ConfBase conf, UserBase hostUser) {
		conf.setCycleId(0);
		return confUserLogic.updateConfUser(conf, hostUser);
	}

	@Override
	public boolean fillConfUserForModifyCycle(List<ConfBase> confList,
			UserBase hostUser) {
		if (confList == null || confList.size() == 0) {
			throw new ConfUserException("confList is empty.");
		}
		for (ConfBase conf : confList) {
			confUserLogic.updateConfUser(conf, hostUser);
		}
		return true;
	}

	@Override
	public List<ConfUser> getConfInviteUserList(int confId) {
		List<ConfUser> confUserList = null;
		try {
			confUserList = libernate.getEntityListWithCondition("conf_id=? and del_flag=? " +
					"and host_flag != ? ",
					 new Object[]{confId, ConstantUtil.DELFLAG_UNDELETE, ConfConstant.CONF_USER_HOST}, 
					 ConfUser.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return confUserList;
	}
	
	
	@Override
	public List<ConfUser> getAllConfUserList(int confId) {
		List<ConfUser> confUserList = null;
		try {
			confUserList = libernate.getEntityListWithCondition("conf_id=? and del_flag=? ",
					new Object[]{confId, ConstantUtil.DELFLAG_UNDELETE}, 
					 ConfUser.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return confUserList;
	}
	
	@Override
	public boolean recvInvite(int confUserId) {
		return handleInvite(confUserId, ConfConstant.CONF_USER_ACCEPT);
	}

	@Override
	public boolean refuseInvite(int confUserId) {
		//todo:调用SOAP，更新会议参与人列表
		//
		//
		
		return handleInvite(confUserId, ConfConstant.CONF_USER_REFUSE);
	}
	
	private boolean handleInvite(int confUserId, int handleStatus) {
		ConfUser confUser = confUserLogic.getConfUser(confUserId);
		if (confUser == null) {
			return false;
		}
		confUser.setAcceptFlag(handleStatus);
		try {
			libernate.updateEntity(confUser, "accept_flag");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public String retrieveAcceptURI(int confId, int userId, String userName, String userEmail) {
		ConfUser confUser = null;
		if (userId <= 0) {
			confUser = confUserLogic.getConfUserByEmailAndName(confId, userName, userEmail);
			if (confUser == null) {
				return "";
			}
		}
		else {
			confUser = confUserLogic.getConfUser(confId, userId);
		}
		return String.format("/user/inviteUser/recv?cuid=%d&confId=%d", 
				confUser == null ? 0 : confUser.getId(), confId);
	}
	
	@Override
	public String retrieveRefuseURI(int confId, int userId, String userName, String userEmail) {
		ConfUser confUser = null;
		if (userId <= 0) {
			confUser = confUserLogic.getConfUserByEmailAndName(confId, userName, userEmail);
			if (confUser == null) {
				return "";
			}
		}
		else {
			confUser = confUserLogic.getConfUser(confId, userId);
		}
		return String.format("/user/inviteUser/refuse?cuid=%d&confId=%d", 
				confUser == null ? 0 : confUser.getId(), confId);
	}

	@Override
	public List<ConfUser> getConfUserListForRemind(Integer aheadMinute, Integer scopeMinute) {
		List<ConfUser> confUserList=null;
		if(aheadMinute==null || aheadMinute.intValue() <=0){
			aheadMinute=ConfConstant.CONF_REMIND_AHEAD_MINUTES_DEFAULT;
		}
		if(scopeMinute==null || scopeMinute.intValue() <=0){
			scopeMinute=ConfConstant.CONF_REMIND_MINUTE_SCOPE_DEFAULT;
		}
		if(aheadMinute < scopeMinute){
			aheadMinute = ConfConstant.CONF_REMIND_AHEAD_MINUTES_DEFAULT;
		}
		Date fromDate=DateUtil.getGmtDateByBeforeMinute(aheadMinute);
		Date toDate=DateUtil.getGmtDateByBeforeMinute(aheadMinute-scopeMinute);
		StringBuffer  sqlBuffer = new StringBuffer();
		sqlBuffer.append("select * from t_conf_user ");
		sqlBuffer.append("	where del_flag=?");
		sqlBuffer.append("		and remind_flag = ?");
		sqlBuffer.append("		and start_time >= ?");
		sqlBuffer.append("		and start_time <= ?");
		Object[] values=new Object[]{
				ConstantUtil.DELFLAG_UNDELETE,
				ConfConstant.CONF_REMIND_FLAG_UNREMIND,
				fromDate,
				toDate
			};
		try {
			confUserList=libernate.getEntityListBase(ConfUser.class, sqlBuffer.toString(), values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sqlBuffer = null;
		values = null;
		fromDate = null;
		toDate = null;
		
		return confUserList;
	}

	@Override
	public boolean updateRemindFlag(ConfUser confUser) {
		boolean updateStatus=false;
		try {
			confUser=libernate.updateEntity(confUser, "id","remindFlag");
			updateStatus=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updateStatus;
	}

	@Override
	public boolean updateStartTime(Integer confId, Date startTime) {
		if(confId!=null && confId.intValue() > 0 ){
			List<ConfUser> confUserList=null;
			try {
				confUserList=libernate.getEntityListWithCondition("conf_id=? and del_flag=? " ,
						 new Object[]{confId, ConstantUtil.DELFLAG_UNDELETE }, 
						 ConfUser.class);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			if(confUserList!=null && confUserList.size() >0){
				for(ConfUser confUser :confUserList){
					if(confUser!=null){
						confUser.setStartTime(startTime);
						try {
							libernate.updateEntity(confUser, "id","startTime");
						} catch (Exception e) {
							e.printStackTrace();
							
							return false;
						}
					}
				}
			}
			confUserList=null;
			
		}
		// TODO Auto-generated method stub
		return true;
	}

}
