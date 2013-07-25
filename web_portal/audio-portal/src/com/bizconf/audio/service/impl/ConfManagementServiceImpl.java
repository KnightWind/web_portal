package com.bizconf.audio.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.ConfLogic;
import com.bizconf.audio.logic.ConfUserLogic;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsArea;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapBusinessService;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfInfo;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfManagementService;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapConfManagementServicebindingStub;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapInvitedUser;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapQueryPage;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCancelConfRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestCreateConfRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetConfRealtimeStatRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestGetDataConfInfoRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestInviteUsersRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestModifyConfRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryAreaListRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfInfoRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryConfListRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestQueryUsersStatusRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequestStartConfRequest;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapRequester;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseCreateConfResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseGetDataConfInfoResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseInviteUsersResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseModifyConfResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryAreaListResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfInfoResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryConfListResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResponseQueryUsersStatusResponse;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapResult;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapScheduledUser;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapToken;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsSoapUserStatus;
import com.bizconf.audio.soap.conf.ESpaceMeetingAsStringKV;
import com.bizconf.audio.soap.conf.ESpaceMeetingCmuLocator;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseCreateConfResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseGetDataConfInfoResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseInviteUsersResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseModifyConfResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryAreaListResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfInfoResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryConfListResponseHolder;
import com.bizconf.audio.soap.conf.holders.ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder;
import com.bizconf.audio.task.AppContextFactory;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.StringUtil;

/**
 * @desc
 * @author martin
 * @date 2013-3-6
 */
@Service
public class ConfManagementServiceImpl extends BaseSoapService implements ConfManagementService{
	
	@Autowired
	ConfLogic confLogic;
	@Autowired
	ConfUserLogic confUserLogic;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 创建一个会议
	 */
	@Override
	public String createConf(ConfBase confInfo,SiteBase currentsite,UserBase currUserBase,boolean isImmediatelyConf) {
		String retStr= ConstantUtil.AS_COMMON_SUCCESS_CODE+"";
		try{
			if(currentsite==null){
				currentsite = confLogic.getConfSiteBase(confInfo.getSiteId());
			}
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestCreateConfRequest request = new ESpaceMeetingAsSoapRequestCreateConfRequest();
			ESpaceMeetingAsSoapToken token = genToken();
			if(confInfo.getHostKey()!=null && !"".equals(confInfo.getHostKey().trim())){
				token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("EXT_CHAIRMAN_PWD",confInfo.getHostKey())});
			}
			request.setToken(token);
			request.setRequester(getRequester(currentsite.getSiteSign(), currUserBase,null));
			request.setConfId(null);
			request.setAreaId(getDefAreaId());
			request.setConfType(1);
			if(isImmediatelyConf){
				request.setConfType(0);
			}
//			else if(confInfo.getPermanentConf().intValue() == 1){
//				request.setConfType(3);
//			}
			request.setExtConfType(1001);
			request.setAccessCode(confInfo.getCallPhone());
			request.setAgenda(confInfo.getConfDesc());//会议描叙
			request.setMaxMemberAmount(confInfo.getMaxUser());
			request.setUsers(getHostUser(currUserBase, currentsite.getSiteSign()));
			//TEST VALUE
			request.setMaxSpokesmanAmount(2);
			request.setMinReservedAmount(2);
			
			request.setSubject(confInfo.getConfName());//会议主题
			request.setConfCreateType(confInfo.getCreateType());//会议创建方式
			request.setFuncBits(confInfo.getFuncBits());
			request.setMediaBits(confInfo.getMediaBits());
			request.setDuration(confInfo.getDuration());
			request.setBeginDatetime(sdf.format(DateUtil.getBejingDateByGmtDate(confInfo.getStartTime())));
			
			ESpaceMeetingAsSoapResponseCreateConfResponse resp = new ESpaceMeetingAsSoapResponseCreateConfResponse();
			ESpaceMeetingAsSoapResponseCreateConfResponseHolder response = new ESpaceMeetingAsSoapResponseCreateConfResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.createConf(request, response);
			
			if(result.getErrCode()==ConstantUtil.AS_COMMON_SUCCESS_CODE){
				confInfo.setCompereSecure(response.value.getChairmanPwd());
				confInfo.setUserSecure(response.value.getMemberPwd());
				confInfo.setConfHwid(response.value.getConfId());
				
				ConfBase newconfBase = queryConfInfo(response.value.getConfId(),currentsite,currUserBase);
				confInfo.setCryptKey(newconfBase.getCryptKey());
			}else{
				// 添加日志记录AS返回错误码
				retStr = result.getErrCode()+"";
				logger.info("创建会议AS返回错误码：" + retStr);
			}
		}catch(Exception e){
			retStr = "error";
			e.printStackTrace();
			logger.error("调用AS创建会议失败！" + e);
		}
		return retStr;
	}
	
	@Override
	public String createConf(ConfBase confInfo, SiteBase currentsite,
			UserBase currUserBase) {
		return createConf(confInfo,currentsite,currUserBase,false);
	}
	/**
	 * 取消会议
	 */
	@Override
	public boolean cancelConf(String hwconId,SiteBase currentsite,UserBase currUserBase) {
		boolean flag = false;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestCancelConfRequest request = new ESpaceMeetingAsSoapRequestCancelConfRequest();
			request.setConfId(hwconId);
			request.setToken(genToken());
			request.setRequester(getRequester(currentsite.getSiteSign(), currUserBase,hwconId));
			ESpaceMeetingAsSoapResult result = stub.cancelConf(request);
			if(result.getErrCode() == ConstantUtil.AS_COMMON_SUCCESS_CODE || result.getErrCode() == ConstantUtil.AS_CANCLE_SUCCESS_CODE){
				flag = true;
			}
		}catch(Exception e){
			logger.error("调用华为接口取消会议异常！" + e);
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 查询会议信息
	 */
	@Override
	public ConfBase queryConfInfo(String confId, SiteBase currentsite,UserBase currUserBase) {
		ConfBase confBase = null;
		try{
			logger.info("queryConfInfo confId:"+confId);
			logger.info("queryConfInfo currentsite:"+currentsite);
			logger.info("queryConfInfo curruser:"+currUserBase);
			
			if(confId==null || "".equals(confId)){
				return null;
			}
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestQueryConfInfoRequest request = new ESpaceMeetingAsSoapRequestQueryConfInfoRequest();
			request.setRequester(getRequester(currentsite.getSiteSign(), currUserBase,confId));
			request.setToken(genToken());
			request.setConfId(confId);
			
			ESpaceMeetingAsSoapResponseQueryConfInfoResponse response = new ESpaceMeetingAsSoapResponseQueryConfInfoResponse();
			ESpaceMeetingAsSoapResponseQueryConfInfoResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryConfInfoResponseHolder(response);
			ESpaceMeetingAsSoapResult result = stub.queryConfInfo(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				//TODO 添加日志信息
				logger.error("queryConfInfo soap massge "+result.getErrCode());
			}else{
				confBase = tansfroToConfBase(holder.value.getConfInfo());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return confBase;
	}
	
	/**
	 * 开始会议
	 */
	@Override
	public boolean startConf(ConfBase conf,SiteBase currentSite,UserBase currUser) {
		boolean flag = true;
		try{
			if(currentSite==null){
				currentSite = confLogic.getConfSiteBase(conf.getSiteId());
			}
			logger.info("start conf with huawei conf ID:"+conf.getConfHwid());
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestStartConfRequest request = new ESpaceMeetingAsSoapRequestStartConfRequest();
			request.setConfId(conf.getConfHwid());
			request.setToken(genToken());
			request.setRequester(getRequester(currentSite.getSiteSign(), currUser,conf.getConfHwid()));
			ESpaceMeetingAsSoapResult result = stub.startConf(request);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				flag = false;
				//TODO 日志记录AS错误代码
				logger.info("start conf as return code:"+result.getErrCode());
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
			logger.info("start conf exception happen "+e.getLocalizedMessage());
		}
		return flag;
	}
	
	/**
	 * 
	 * @param confInfo
	 * @return
	 */
	private ConfBase tansfroToConfBase(ESpaceMeetingAsSoapConfInfo confInfo){
		if(confInfo==null){
			return null;
		}
		ConfBase conf = new ConfBase();
		
		conf.setConfHwid(confInfo.getConfId());
		//conf.setSiteId(confInfo.getEnterpriseId());
		if(confInfo.getCycleId()!=null){
			conf.setCycleId(Integer.parseInt(confInfo.getCycleId()));
		}
		
		if(confInfo.getAuthKey()!=null){
			conf.setCryptKey(confInfo.getAuthKey());
		}
		
		conf.setConfName(confInfo.getSubject());
		conf.setConfDesc(confInfo.getAgenda());
		//conf.setConfType(confInfo.getConfType());
		//会议开始时间
		if(confInfo.getBeginDatetime()!=null){
			try {
				conf.setStartTime(sdf.parse(confInfo.getBeginDatetime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(confInfo.getEndDatetime()!=null){
			try {
				conf.setEndTime(sdf.parse(confInfo.getEndDatetime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(confInfo.getChairmanUserId()!=null){
			conf.setCompereUser(Integer.parseInt(confInfo.getChairmanUserId()));
		}
		conf.setCompereName(confInfo.getChairmanName());
		conf.setCompereSecure(confInfo.getChairmanPwd()); //主持人安全号
		conf.setUserSecure(confInfo.getMemberPwd()); //与会者安全号
		//need to check
		conf.setPhonePass(confInfo.getAuthKey()); //电话会议密码
		conf.setMaxUser(confInfo.getMaxMemberAmount());
		conf.setMaxAudio(confInfo.getMaxSpokesmanAmount());//最大音频数量 maybe not for this
		conf.setConfStatus(confInfo.getConfStatus());//会议状态
		conf.setSoapStatus(2);
		conf.setCreateUser(Integer.parseInt(confInfo.getCreatorUserId()));
		conf.setCreateType(confInfo.getConfCreateType());
		
		return conf;
	}
	
	/**
	 * 更改会议信息
	 */
	@Override
	public String updateConf(ConfBase confInfo, SiteBase currentsite,
			UserBase currUserBase) {
		return updateConf(confInfo,null,currentsite,currUserBase);
	}
	
	@Override
	public String updateConf(ConfBase confInfo, List<ConfUser> partciparts,
			SiteBase currentsite, UserBase currUserBase) {
		String retInfo = ConstantUtil.AS_SUCCESS_CODE;
		try{
			confInfo.setMaxUser(2);
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestModifyConfRequest request = new ESpaceMeetingAsSoapRequestModifyConfRequest();
			ESpaceMeetingAsSoapToken token = genToken();
			if(confInfo.getHostKey()!=null && !"".equals(confInfo.getHostKey().trim())){
				token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("EXT_CHAIRMAN_PWD",confInfo.getHostKey())});
			}
			request.setToken(token);
			request.setRequester(getRequester(currentsite.getSiteSign(), currUserBase,confInfo.getConfHwid()));
			request.setConfId(confInfo.getConfHwid());
			request.setConfType(1);//预约会议
			request.setAccessCode(ConfConstant.ACCESSCODE);
			request.setAgenda(confInfo.getConfDesc());//会议描叙
			request.setMaxMemberAmount(confInfo.getMaxUser());
			//TEST VALUE
			request.setMaxSpokesmanAmount(2);
			request.setMinReservedAmount(2);
			
			request.setSubject(confInfo.getConfName());//会议主题
			request.setFuncBits(confInfo.getFuncBits());//暂时测试数据
			request.setMediaBits(confInfo.getMediaBits());
			request.setDuration(confInfo.getDuration());//会议时长
			request.setBeginDatetime(sdf.format(DateUtil.getBejingDateByGmtDate(confInfo.getStartTime())));
			//设置参会者
//			if(partciparts!=null){
//				request.setUsers(getScheduledUsers(partciparts, currentsite.getSiteSign()));
//			}else{
//				List<ConfUser> confUsers = confUserLogic.getConfUserList(confInfo.getId());
//				request.setUsers(getScheduledUsers(confUsers, currentsite.getSiteSign()));
//			}
			//华为AS调整后使用上面注释代码
			ConfBase libConf = libernate.getEntity(ConfBase.class, confInfo.getId());
			ConfUser host = confUserLogic.getConfUser(confInfo.getId(),libConf.getCompereUser());
			if(host==null){
				host = confUserLogic.getConfUserHosterForPublicConf(confInfo.getId());
			}
			List<ConfUser> confUsers = new ArrayList<ConfUser>();
			confUsers.add(host);
			request.setUsers(getScheduledUsers(confUsers, currentsite.getSiteSign(),libConf));
			
			ESpaceMeetingAsSoapResponseModifyConfResponse resp = new ESpaceMeetingAsSoapResponseModifyConfResponse();
			ESpaceMeetingAsSoapResponseModifyConfResponseHolder response = new ESpaceMeetingAsSoapResponseModifyConfResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.modifyConf(request, response);
			if(result.getErrCode()==ConstantUtil.AS_COMMON_SUCCESS_CODE){
				confInfo.setCompereSecure(response.value.getChairmanPwd());
				confInfo.setUserSecure(response.value.getMemberPwd());
				confInfo.setConfHwid(response.value.getConfId());
				
				ConfBase newconfBase = queryConfInfo(response.value.getConfId(),currentsite,currUserBase);
				confInfo.setCryptKey(newconfBase.getCryptKey());
				confLogic.updateConfCryptKey(confInfo);
			}else{
				//TODO 添加日志记录AS返回错误码
				retInfo =  result.getErrCode()+"";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		return retInfo;
	}
	
	/**
	 * 邀请参会者参加会议 
	 */
	@Override
	public boolean inviteConfUser(List<ConfUser> users, String confId,SiteBase currentsite,
			UserBase currUserBase) {
		boolean flag = true;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestInviteUsersRequest request = new ESpaceMeetingAsSoapRequestInviteUsersRequest();
			request.setConfId(confId);
			request.setToken(genToken());
			request.setRequester(getRequester(currentsite.getSiteSign(), currUserBase,confId));
			List<ESpaceMeetingAsSoapInvitedUser> eSpaceUsers = new ArrayList<ESpaceMeetingAsSoapInvitedUser>();
			for(ConfUser user:users){
				ESpaceMeetingAsSoapInvitedUser eUser = new ESpaceMeetingAsSoapInvitedUser();
				eUser.setRole(user.getHostFlag());
				eUser.setEnterpriseId(currentsite.getSiteSign());
				eUser.setUserId(user.getId().toString());
				eUser.setName(user.getUserName());
				eUser.setUri("tel:"+user.getTelephone());//sip  
				eUser.setPin(""); //not sure yet
				eUser.setAllowCallNum("");// not sure yet
				eUser.setNotifyTypes(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("1",user.getUserEmail()),new ESpaceMeetingAsStringKV("2",user.getTelephone())});
				eSpaceUsers.add(eUser);
			}
			request.setUsers(eSpaceUsers.toArray(new ESpaceMeetingAsSoapInvitedUser[eSpaceUsers.size()]));
			ESpaceMeetingAsSoapResponseInviteUsersResponse resp = new ESpaceMeetingAsSoapResponseInviteUsersResponse();
			ESpaceMeetingAsSoapResponseInviteUsersResponseHolder holder = new ESpaceMeetingAsSoapResponseInviteUsersResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.inviteUsers(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				flag = false;
			}else{
				
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@Override
	public ESpaceMeetingAsSoapUserStatus[] queryConfUserStatus(String confHwId, int pageNo,
			int pageSize,SiteBase currSite,UserBase currUser) {
		ESpaceMeetingAsSoapUserStatus[] userStatus = null;
		try{
			if(pageNo<1)pageNo=1;
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestQueryUsersStatusRequest request = new ESpaceMeetingAsSoapRequestQueryUsersStatusRequest();
			
			ESpaceMeetingAsSoapQueryPage page = new ESpaceMeetingAsSoapQueryPage();
			page.setCurrentPage(pageNo);
			page.setPageSize(pageSize);
			request.setConfId(confHwId);
			request.setPage(page);
			request.setToken(genToken());
			request.setRequester(getRequester(currSite.getSiteSign(),currUser,confHwId));
			
			ESpaceMeetingAsSoapResponseQueryUsersStatusResponse resp = new ESpaceMeetingAsSoapResponseQueryUsersStatusResponse();
			ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder(resp);
			
			ESpaceMeetingAsSoapResult result = stub.queryUsersStatus(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				 logger.info("soap get user status error errorCode:"+result.getErrCode());
				 throw new RuntimeException("soap get user status error errorCode:"+result.getErrCode());
			}else{
				String[] ids = result.getArgs();
				userStatus = holder.value.getStatuses();
				for (int i = 0; i < userStatus.length; i++) {
					userStatus[i].setUserId(ids[i]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return userStatus;
	}
	
	
	@Override
	public PageBean<ConfLog> queryConfUserStatusForPage(String confHwId,
			int pageNo, int pageSize, SiteBase currSite, UserBase currUser) {
		PageBean<ConfLog> logs = new PageBean<ConfLog>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ESpaceMeetingAsSoapUserStatus[] userStatus = null;
		try{
			if(pageNo<1)pageNo=1;
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			ESpaceMeetingAsSoapRequestQueryUsersStatusRequest request = new ESpaceMeetingAsSoapRequestQueryUsersStatusRequest();
			
			ESpaceMeetingAsSoapQueryPage page = new ESpaceMeetingAsSoapQueryPage();
			page.setCurrentPage(pageNo);
			page.setPageSize(pageSize);
			logs.setPageNo(pageNo+"");
			logs.setPageSize(pageSize);
			request.setConfId(confHwId);
			request.setPage(page);
			request.setToken(genToken());
			request.setRequester(getRequester(currSite.getSiteSign(),currUser,confHwId));
			
			ESpaceMeetingAsSoapResponseQueryUsersStatusResponse resp = new ESpaceMeetingAsSoapResponseQueryUsersStatusResponse();
			ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder(resp);
			
			ESpaceMeetingAsSoapResult result = stub.queryUsersStatus(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				 logger.info("soap get user status error errorCode:"+result.getErrCode());
				 throw new RuntimeException("soap get user status error errorCode:"+result.getErrCode());
			}else{
				userStatus = holder.value.getStatuses();
				logs.setRowsCount(holder.value.getTotalCount());
				List<ConfLog> confLogs = new ArrayList<ConfLog>();
				
				if(userStatus!=null && userStatus.length>0){
					for (int i = 0; i < userStatus.length; i++) {
						ConfLog log = new ConfLog();
						log.setTermType(userStatus[i].getTermType());
						log.setJoinTime(DateUtil.getGmtDateByTimeZone(sdf.parse(userStatus[i].getJoinDatetime()), 28800000));
						if(userStatus[i].getLeaveDatetime()!=null && !userStatus[i].getLeaveDatetime().equals("")){
							log.setExitTime(DateUtil.getGmtDateByTimeZone(sdf.parse(userStatus[i].getLeaveDatetime()),28800000));
						}
						log.setUserName(userStatus[i].getUserName());
						confLogs.add(log);
					}
					logs.setDatas(confLogs);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return logs;
	}
/**
 * 查询数据会议ips
 * @param siteSign
 * @param confId
 */
@Override
public ESpaceMeetingAsSoapResponseGetDataConfInfoResponse queryDataConfInfo(String siteSign,String confId){
		try{
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL(CONF_URL),null);
			ESpaceMeetingAsSoapRequestGetDataConfInfoRequest request = new ESpaceMeetingAsSoapRequestGetDataConfInfoRequest();
			request.setToken(genToken());
			request.setRequester(getRequester(siteSign, null,confId));
			request.setTermType(2);
			request.setConfId(confId);
			 
			ESpaceMeetingAsSoapResponseGetDataConfInfoResponse resp = new ESpaceMeetingAsSoapResponseGetDataConfInfoResponse();
			ESpaceMeetingAsSoapResponseGetDataConfInfoResponseHolder holder = new ESpaceMeetingAsSoapResponseGetDataConfInfoResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.getDataConfInfo(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				logger.error("call queryDataConfInfo interface failed! errorcode:"+result.getErrCode());
			}else{
				return resp;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 更新参会人数信息
	 */
	@Override
	public boolean queryConfUserTerminals(ConfBase conf) {
		boolean flag = false;
		try{
			SiteBase currSite = confLogic.getConfSiteBase(conf.getSiteId());
			ESpaceMeetingAsSoapUserStatus[] userStatus = queryConfUserStatus(conf.getConfHwid(), 1, 6000, currSite, null);
			int pcNum = 0;
			int phoneNum = 0;
			if(userStatus!=null && userStatus.length>0){
				for (int i = 0; i < userStatus.length; i++) {
					if(userStatus[i].getTermType()== ConfConstant.CONF_USER_TERM_TYPE_PC && "1".equals(userStatus[i].getUserOnlineStatus())){
						pcNum++;
					}else if(userStatus[i].getTermType()== ConfConstant.CONF_USER_TERM_TYPE_MOBILE && "1".equals(userStatus[i].getUserOnlineStatus())){
						phoneNum++;
					}
				}
				
				if(conf.getBelongConfId()!=null && conf.getBelongConfId().intValue()>0){
					ConfBase base = libernate.getEntity(ConfBase.class, conf.getBelongConfId());
					base.setPcNum(pcNum);
					base.setPhoneNum(phoneNum);
					libernate.updateEntity(base,"id","pc_num","phone_num");
				}
				conf.setPcNum(pcNum);
				conf.setPhoneNum(phoneNum);
				libernate.updateEntity(conf,"id","pc_num","phone_num");
			}
			flag = true;
		}catch(Exception e){
			
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 设置所有正在进行中的会议的终端数量和终端类型
	 */
	@Override
	public boolean setConfTerminalNum() {
		boolean flag = false;
		String sql = "select * from t_conf_base where conf_status = ?";
		try{
			List<ConfBase> confs = libernate.getEntityListCustomized(ConfBase.class, sql, new Object[]{ConfConstant.CONF_STATUS_OPENING});
			for (Iterator<ConfBase> it = confs.iterator(); it.hasNext();) {
				ConfBase conf =  it.next();
				queryConfUserTerminals(conf);
			}
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 获取默认区域ID
	 * @return
	 */
	public String getDefAreaId(){
		if(DEFAULT_AREA_ID!=null&&!DEFAULT_AREA_ID.equals("")){
			return DEFAULT_AREA_ID;
		}else{
			try{
					ESpaceMeetingCmuLocator locator = getLocator();
					ESpaceMeetingAsSoapBusinessService stab = locator.getESpaceMeetingAsSoapBusinessService(new java.net.URL(BUSINESS_URL));
					ESpaceMeetingAsSoapRequestQueryAreaListRequest request = new ESpaceMeetingAsSoapRequestQueryAreaListRequest();
					request.setToken(genToken());
					ESpaceMeetingAsSoapResponseQueryAreaListResponse resp = new ESpaceMeetingAsSoapResponseQueryAreaListResponse();
					ESpaceMeetingAsSoapResponseQueryAreaListResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryAreaListResponseHolder(resp);
					ESpaceMeetingAsSoapResult result = stab.queryAreaList(request, holder);
					
					if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
						logger.error("call getDefAreaId interface failed! errorcode:"+result.getErrCode());
					}else{
						ESpaceMeetingAsArea[] areas = holder.value.getAreaList();
						if(areas!=null && areas.length>0){
							DEFAULT_AREA_ID = areas[0].getAreaId();
							return areas[0].getAreaId();
						}
					}
			}catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}
//	public void 
	
	public  static void queryConf(String siteSign, String confHwId){
		try{
			ESpaceMeetingAsSoapRequester requester = new ESpaceMeetingAsSoapRequester();
			if(StringUtil.isNotBlank(siteSign)){
				requester.setEnterpriseId(siteSign);
			}else{
				requester.setEnterpriseId("meeting");
			}
			requester.setUserId("920");
			requester.setUserName("martin");
			
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId(null);
			token.setTimezone("Aisa/shanghai");
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")}); 
			
//			EngineConfiguration defaultConfig = EngineConfigurationFactoryFinder.newFactory()
//	                .getClientEngineConfig();
			
			EngineConfiguration conf = new FileProvider(ConfManagementServiceImpl.class.getResourceAsStream("/client-config.wsdd"));
//			ESpaceMeetingCmuLocator locator = new ESpaceMeetingCmuLocator(conf);
			//			defaultConfig.
//			SimpleProvider config = new SimpleProvider(defaultConfig);//Commons  defaultConfig
//			config.deployTransport(HTTPTransport.DEFAULT_TRANSPORT_NAME, new CommonsHTTPSender());
			//在构造Service对象或Service对象的子类时，请使用如下的构造函数：
			ESpaceMeetingCmuLocator locator = new ESpaceMeetingCmuLocator(conf);
			ESpaceMeetingAsSoapConfManagementService stuba =  locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL(CONF_URL),locator);
			ESpaceMeetingAsSoapRequestQueryConfInfoRequest request = new ESpaceMeetingAsSoapRequestQueryConfInfoRequest();
			request.setRequester(requester);
			request.setToken(token);
			if(StringUtil.isNotBlank(confHwId)){
				request.setConfId(confHwId);
			}else{
				request.setConfId("00100000712");
			}
			
			ESpaceMeetingAsSoapResponseQueryConfInfoResponse response = new ESpaceMeetingAsSoapResponseQueryConfInfoResponse();
			ESpaceMeetingAsSoapResponseQueryConfInfoResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryConfInfoResponseHolder(response);
//			ESpaceMeetingAsSoapResult result = stub.queryConfInfo(request, holder);
			
			ESpaceMeetingAsSoapResult result = stub.queryConfInfo(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				System.out.println(result.getErrCode());
			}else{
				System.out.println("will show conf Info");
				//confBase = tansfroToConfBase(holder.value.getConfInfo());
				System.out.println(holder.value.getConfInfo().getAgenda());
				System.out.println(holder.value.getConfInfo().getAreaId());
				System.out.println("会议状态："+holder.value.getConfInfo().getConfStatus());
				System.out.println(holder.value.getConfInfo().getAccessCode());
				System.out.println("AuthKey: "+holder.value.getConfInfo().getAuthKey());
				System.out.println(holder.value.getConfInfo().getCreatorName());
				System.out.println(holder.value.getConfInfo().getBeginDatetime());
				System.out.println(holder.value.getConfInfo().getEndDatetime());
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static void testcreateConf(){
		try {
			
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL("http://10.184.130.16:8996/eSpaceMeeting/ConfManagementService"),null);
			
			ESpaceMeetingAsSoapRequester requester = new ESpaceMeetingAsSoapRequester();
			requester.setEnterpriseId("Putin_OklahomaCity");
			requester.setUserId("386");
			requester.setUserName("Putin");
			
//			if(currentsite!=null){
//				token = genSiteToken(currentsite);
//			}else{
//				token = genSiteToken(confLogic.getConfSiteBase(confInfo.getSiteId()));
//			}
			//token 信息
			
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone("Aisa/shanghai");
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			 
			ESpaceMeetingAsSoapRequestCreateConfRequest request = new ESpaceMeetingAsSoapRequestCreateConfRequest();
			request.setToken(token);
			request.setRequester(requester);
			request.setAreaId(null);
			request.setConfId(null);
			request.setExtConfType(1001);
			request.setConfType(1);
			request.setAccessCode("555555");
			request.setAgenda("wadde测试会议");//会议描叙
			request.setMaxMemberAmount(6);
			//TEST VALUE
			request.setMaxSpokesmanAmount(2);
			request.setMinReservedAmount(2);
			
			request.setSubject("测试license");//会议主题
			request.setConfCreateType(0);//会议创建方式
			request.setFuncBits("00000011001011001100000000000000");//暂时测试数据
			request.setMediaBits("101");
			request.setDuration(120);
			request.setRecordURL("www.confcloud.com");
			System.out.println("开始时间："+sdf.format(new Date(new Date().getTime()+960000000)));
			request.setBeginDatetime(sdf.format(new Date(new Date().getTime()+960000000)));
			
			//ESpaceMeetingAsSoapScheduledUser user = new ESpaceMeetingAsSoapScheduledUser("102","teste","teste_test","tel:15652292572","15652292572","123456",1,1,new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("a","b")});
			ESpaceMeetingAsSoapScheduledUser eUser = new ESpaceMeetingAsSoapScheduledUser();
			eUser.setRole(1);
			eUser.setEnterpriseId("Putin_OklahomaCity");
			eUser.setUserId("387");
			eUser.setName("MARTIN_teles");
			eUser.setUri("tel:13700012151");//sip  
			eUser.setPin("123456"); //not sure yet
			eUser.setAllowCallNum("15652292572");// not sure yet
			eUser.setNotifyTypes(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("email","true")});
			//eSpaceUsers.add(eUser);
			request.setUsers(new ESpaceMeetingAsSoapScheduledUser[]{eUser});
			
			ESpaceMeetingAsSoapResponseCreateConfResponse resp = new ESpaceMeetingAsSoapResponseCreateConfResponse();
			ESpaceMeetingAsSoapResponseCreateConfResponseHolder response = new ESpaceMeetingAsSoapResponseCreateConfResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.createConf(request, response);
				if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
					System.out.println(result.getErrCode());
				}else{
					System.out.println("主持人密码：  "+response.value.getChairmanPwd());
					System.out.println("与会者密码："+response.value.getMemberPwd());
					System.out.println("华为ID：" +response.value.getConfId());
					//response.value.getEndDatetime(); 结束时间
					//confInfo.set
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public static void testupdateConf(){
		
		try {
			
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL("http://10.184.130.16:8996/eSpaceMeeting/ConfManagementService"),null);
			
			ESpaceMeetingAsSoapRequester requester = new ESpaceMeetingAsSoapRequester();
			requester.setEnterpriseId("jack");
			requester.setUserId("385");
			requester.setUserName("kanuo");
			
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone("Aisa/shanghai");
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			
			ESpaceMeetingAsSoapRequestModifyConfRequest request = new ESpaceMeetingAsSoapRequestModifyConfRequest();
			request.setToken(token);
			request.setRequester(requester);
			request.setAreaId("0");
			request.setConfId("00100000186");
			request.setExtConfType(1001);
			request.setConfType(1);
			request.setAccessCode("123456");
			request.setAgenda("修改会议测试");//会议描叙
			request.setMaxMemberAmount(3);
			//TEST VALUE
			request.setMaxSpokesmanAmount(1);
			request.setMinReservedAmount(2);
			
			request.setSubject("华为会议修改");//会议主题
			request.setFuncBits("00000011001011001100000000000000");//暂时测试数据
			request.setMediaBits("101");
			request.setDuration(60);
			request.setRecordURL("www.confcloud.com");
			request.setBeginDatetime(sdf.format(new Date(new Date().getTime()+910000000)));
			
			//ESpaceMeetingAsSoapScheduledUser user = new ESpaceMeetingAsSoapScheduledUser("102","teste","teste_test","tel:15652292572","15652292572","123456",1,1,new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("a","b")});
			ESpaceMeetingAsSoapScheduledUser eUser = new ESpaceMeetingAsSoapScheduledUser();
			eUser.setRole(2);
			eUser.setEnterpriseId("jack");
			eUser.setUserId("912");
			eUser.setName("martinwang");
			eUser.setUri("tel:13700012151");//sip  
			eUser.setPin("123456"); //not sure yet
			eUser.setAllowCallNum("15652292572");// not sure yet
			eUser.setNotifyTypes(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("email","true")});
			
			ESpaceMeetingAsSoapScheduledUser userPing = new ESpaceMeetingAsSoapScheduledUser();
			userPing.setRole(2);
			userPing.setEnterpriseId("jack");
			userPing.setUserId("982");
			userPing.setName("ping");
			userPing.setUri("tel:13710012151");//sip  
			userPing.setPin("123456"); //not sure yet
			userPing.setAllowCallNum("15652292572");// not sure yet
			userPing.setNotifyTypes(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("email","true")});
			//eSpaceUsers.add(eUser);
			request.setUsers(new ESpaceMeetingAsSoapScheduledUser[]{eUser,userPing});
			
			ESpaceMeetingAsSoapResponseModifyConfResponse resp = new ESpaceMeetingAsSoapResponseModifyConfResponse();
			ESpaceMeetingAsSoapResponseModifyConfResponseHolder response = new ESpaceMeetingAsSoapResponseModifyConfResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.modifyConf(request, response);
				if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
					System.out.println(result.getErrCode());
				}else{
					System.out.println("主持人密码：  "+response.value.getChairmanPwd());
					System.out.println("与会者密码："+response.value.getMemberPwd());
					System.out.println("华为ID：" +response.value.getConfId());
					System.out.println("开始时间：" +response.value.getBeginDatetime());
					//response.value.getEndDatetime(); 结束时间
					//confInfo.set
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	
	public static void testqueryConfUserStatus(String confId){
		try{
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL("http://10.184.130.16:8996/eSpaceMeeting/ConfManagementService"),null);
			ESpaceMeetingAsSoapRequestQueryUsersStatusRequest request = new ESpaceMeetingAsSoapRequestQueryUsersStatusRequest();
			
			//request.setToken(token);
			//request.setRequester(requester);
			
			ESpaceMeetingAsSoapRequester requester = new ESpaceMeetingAsSoapRequester();
			requester.setEnterpriseId("meeting");
			requester.setUserId("1130");
			requester.setUserName("holik");
			
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone(null);
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			ESpaceMeetingAsSoapQueryPage page = new ESpaceMeetingAsSoapQueryPage();
			page.setCurrentPage(1);
			page.setPageSize(2000);
			request.setConfId(confId);
			request.setPage(page);
			request.setToken(token);
			request.setRequester(requester);
			
			ESpaceMeetingAsSoapResponseQueryUsersStatusResponse resp = new ESpaceMeetingAsSoapResponseQueryUsersStatusResponse();
			ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryUsersStatusResponseHolder(resp);
			
			ESpaceMeetingAsSoapResult result = stub.queryUsersStatus(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				//TODO add log
				System.out.println(result.getErrCode());
			}else{
				System.out.println("will show the status!");
				System.out.println("total count = "+holder.value.getTotalCount());
				ESpaceMeetingAsSoapUserStatus[] espaceUsers = holder.value.getStatuses();
				
				String[] ids = result.getArgs();
				for (int i = 0; i < espaceUsers.length; i++){
					espaceUsers[i].setUserId(ids[i]);
					 System.out.println("name:"+espaceUsers[i].getUserName());
					 System.out.println("userId:"+espaceUsers[i].getUserId());
					 System.out.println("online "+espaceUsers[i].getUserOnlineStatus());
					 System.out.println("JoinDatetime:"+espaceUsers[i].getJoinDatetime());
					 System.out.println("LeaveDatetime:"+espaceUsers[i].getLeaveDatetime());
					 System.out.println("userRole:"+espaceUsers[i].getRole());
					 System.out.println("--------");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	
	public static void cancelMeeting(String confId){
		try{
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone(null);
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			ESpaceMeetingAsSoapRequester requester = new ESpaceMeetingAsSoapRequester();
			requester.setEnterpriseId("shrine");
			requester.setUserId("1130");
			requester.setUserName("holik");
			
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL(CONF_URL),null);
			ESpaceMeetingAsSoapRequestCancelConfRequest request = new ESpaceMeetingAsSoapRequestCancelConfRequest();
			request.setConfId(confId);
			request.setToken(token);
			request.setRequester(requester);
			ESpaceMeetingAsSoapResult result = stub.cancelConf(request);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				System.out.println(result.getErrCode());
			}else{
				System.out.println("conf has been cancel");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void testqueryConfList(String siteSign){
		try{
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone(null);
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			ESpaceMeetingAsSoapRequester requester = new ESpaceMeetingAsSoapRequester();
			if(StringUtil.isNotBlank(siteSign)){
				requester.setEnterpriseId(siteSign);      //站点标识
			}else{
				requester.setEnterpriseId("meeting");      //站点标识
			}
			requester.setUserId("1130");
			requester.setUserName("holik");
			
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL(CONF_URL),null);
			
			ESpaceMeetingAsSoapRequestQueryConfListRequest request = new ESpaceMeetingAsSoapRequestQueryConfListRequest();
			request.setToken(token);
			request.setRequester(requester);
			
			request.setConfStatusList(new int[]{0,2});
			
			ESpaceMeetingAsSoapQueryPage page = new ESpaceMeetingAsSoapQueryPage();
			page.setCurrentPage(1);
			page.setPageSize(100);
			request.setPage(page);
			
			
			ESpaceMeetingAsSoapResponseQueryConfListResponse resp = new ESpaceMeetingAsSoapResponseQueryConfListResponse();
			ESpaceMeetingAsSoapResponseQueryConfListResponseHolder holder = new ESpaceMeetingAsSoapResponseQueryConfListResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.queryConfList(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				System.out.println(result.getErrCode());
			}else{
				ESpaceMeetingAsSoapConfInfo[] confInfos = holder.value.getConfInfos();
//				System.out.println("当前总会议数："+confInfos.length);
				int temp = 0;
				for (int i = 0; i < confInfos.length; i++) {
					ESpaceMeetingAsSoapConfInfo conf = confInfos[i];
					if(requester.getEnterpriseId().equals(conf.getEnterpriseId())){
						System.out.println("enterpriseId = "+conf.getEnterpriseId());
						System.out.println("confName "+conf.getSubject());
						System.out.println("huiyiID  "+conf.getConfId());
						System.out.println("会议点数 "+conf.getMaxMemberAmount());
						System.out.println("会议状态："+conf.getConfStatus());
						System.out.println("会议开始时间："+conf.getBeginDatetime());
						System.out.println("会议结束时间："+conf.getEndDatetime());
						if(conf.getConfStatus()==0){
							//cancelMeeting(conf.getConfId());
						}
						temp+= conf.getMaxMemberAmount();
					}
				}
				System.out.println("license 使用总数"+temp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void queryDataConfInfos(String siteSign,String confId){
		
		try{
			ESpaceMeetingAsSoapToken token = new ESpaceMeetingAsSoapToken();
			token.setAppkey("INTERNAL");
			token.setTimestamp(new Date().getTime());
			token.setArgs(new String[]{"test1"});
			token.setRequestId("bizconf");
			token.setTimezone(null);
			token.setKwargs(new ESpaceMeetingAsStringKV[]{new ESpaceMeetingAsStringKV("c","d")});
			
			ESpaceMeetingAsSoapRequester requester = new ESpaceMeetingAsSoapRequester();
			if(StringUtil.isNotBlank(siteSign)){
				requester.setEnterpriseId(siteSign);      //站点标识
			}else{
				requester.setEnterpriseId("meeting");      //站点标识
			}
			requester.setUserId("1130");
			requester.setUserName("holik");
			
			ESpaceMeetingAsSoapConfManagementServicebindingStub stub = new ESpaceMeetingAsSoapConfManagementServicebindingStub(new java.net.URL(CONF_URL),null);
			
			ESpaceMeetingAsSoapRequestGetDataConfInfoRequest request = new ESpaceMeetingAsSoapRequestGetDataConfInfoRequest();
			request.setToken(token);
			request.setRequester(requester);
			request.setTermType(2);
			request.setConfId(confId);
			 
			
			ESpaceMeetingAsSoapResponseGetDataConfInfoResponse resp = new ESpaceMeetingAsSoapResponseGetDataConfInfoResponse();
			ESpaceMeetingAsSoapResponseGetDataConfInfoResponseHolder holder = new ESpaceMeetingAsSoapResponseGetDataConfInfoResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.getDataConfInfo(request, holder);
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				System.out.println(result.getErrCode());
			}else{
				 String ips = holder.value.getServerIP();
				 System.out.println(ips);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean setingOnlineUserNum(ConfBase conf) {
		boolean flag = false;
		try{
			ESpaceMeetingCmuLocator locator = getLocator();
			ESpaceMeetingAsSoapConfManagementService stub = locator.getESpaceMeetingAsSoapConfManagementService(new java.net.URL(CONF_URL));
			
			ESpaceMeetingAsSoapRequestGetConfRealtimeStatRequest request = new ESpaceMeetingAsSoapRequestGetConfRealtimeStatRequest();
			
			request.setConfId(conf.getConfHwid());
			request.setStatItemList(new String[]{"online_user_number"});
			request.setToken(genToken());
			ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse resp = new ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponse();
			ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponseHolder holder = new ESpaceMeetingAsSoapResponseGetConfRealtimeStatResponseHolder(resp);
			ESpaceMeetingAsSoapResult result = stub.getConfRealtimeStat(request, holder);
			int onlineNum = 0;
			if(result.getErrCode()!=ConstantUtil.AS_COMMON_SUCCESS_CODE){
				logger.error("call get online user soap inteface failed! error code:"+result.getErrCode());
			}else{
				ESpaceMeetingAsStringKV[] datas = holder.value.getStatData();
				for (int i = 0; i < datas.length; i++) {
					if(datas[i].getK().equals("online_user_number")){
						String num = datas[i].getV();
						try{
							onlineNum = Integer.parseInt(num);
							logger.info("conf: "+conf.getConfHwid()+"  getConfRealtimeStat soap massge: "+num);
							//获取成功
							flag = true;
						}catch(Exception e){
							
						}
						break;
					}
				}
			}
			conf.setPcNum(onlineNum);
			libernate.updateEntity(conf,"id","pc_num");
			if(conf.getBelongConfId()!=null && conf.getBelongConfId()>0){
				conf.setId(conf.getBelongConfId());
				libernate.updateEntity(conf,"id","pc_num");
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}
	
	public static void main(String[] args)throws Exception{
		//testupdateConf();
//		queryConf("meeting", "00100014467");
		
//		ConfManagementServiceImpl impl = new ConfManagementServiceImpl();
//		ConfBase conf = new ConfBase();
//		conf.setConfHwid("00100010832");
//		impl.setingOnlineUserNum(conf);
//		String id = impl.getDefAreaId();
		//testcreateConf();
//		testqueryConfUserStatus("00100014467");
		//cancelMeeting("00100000142");
		
//		queryConf("meeting", "00100016056");
		
		//testcreateConf();
//		testqueryConfUserStatus("00100008104");
		//cancelMeeting("00100000839");
//		testqueryConfList("meeting");      //查询所有AS的会议
//		testqueryConfList();      //查询所有AS的会议
		
//		queryDataConfInfos("meeting","00100016718");
		
		
		//连续调用StartConf
//		ConfManagementService cs = AppContextFactory.getAppContext().getBean(ConfManagementService.class);
//		ConfBase conf = new ConfBase();
//		conf.setConfHwid("00100016711");
//		SiteBase site = new SiteBase();
//		site.setSiteSign("meeting");
//		System.out.println(cs.startConf(conf, site, null));
//		Thread.sleep(80l);
//		cs = AppContextFactory.getAppContext().getBean(ConfManagementService.class);
//		System.out.println(cs.startConf(conf, site, null));
		queryConf("meeting", "00100016718");
	}

	
}
