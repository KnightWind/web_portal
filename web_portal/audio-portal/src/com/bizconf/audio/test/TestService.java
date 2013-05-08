package com.bizconf.audio.test;

import com.bizconf.audio.entity.EmpowerConfig;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.EmpowerConfigService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.task.AppContextFactory;


public class TestService {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("0&1=="+('0'&'1'));
		System.out.println("1&1=="+('1'&'1'));
		System.out.println("0&0=="+('0'&'0'));
	//	System.out.println("0^1=="+(0^1));
		System.out.println("0|1=="+(0|1));
		System.out.println("0|0=="+(0|0));
		System.out.println("1|1=="+(1|1));
//
//		EmpowerConfigService empowerService=AppContextFactory.getAppContext().getBean(EmpowerConfigService.class);
//		EmpowerConfig userPower=empowerService.getUserEmpowerConfigByUserId(1000150);
//		System.out.println("userPower=="+userPower);


//		EmpowerConfigService empowerService=AppContextFactory.getAppContext().getBean(EmpowerConfigService.class);
//		UserService userService=AppContextFactory.getAppContext().getBean(UserService.class);
//		UserBase userBase=userService.getUserBaseById(1000150);
////		EmpowerConfig userPower=empowerService.getUserEmpowerConfigByUserId(1000150);
////
////		EmpowerConfig sitePower=empowerService.getSiteEmpowerConfigBySiteId(11);
//	//	EmpowerConfig globalPower=empowerService.getSiteEmpowerGlobalBySiteId(11);
//		
//		EmpowerConfig empowerConfig=empowerService.makeEmpowerForConf(userBase);
//		System.out.println("userPower=="+empowerConfig);
		
		
		

		ConfManagementService confManagementService=AppContextFactory.getAppContext().getBean(ConfManagementService.class);
		
		

		
//		ConfService confService=AppContextFactory.getAppContext().getBean(ConfService.class);
//		UserService userService=AppContextFactory.getAppContext().getBean(UserService.class);
//		UserBase userBase=userService.getUserBaseById(1000068);
//		//
//		List<ConfBase> confList=null;
//		PageBean<ConfBase> comingDate=null;
//		int pageCount=5;
//		for(int ii=1;ii<pageCount;ii++){
//			System.out.println("------------Host------------------>>>>>>"+ii);
//			comingDate=confService.getMonthlyComingConfListForHost(null, userBase, ii,null,null);//(userBase, ii);
//			confList=comingDate.getDatas();
//			if(confList!=null && confList.size() > 0){
//				for(ConfBase confBase:confList){
//					if(confBase!=null){
//						System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"-->> hostId"+confBase.getCompereUser()+"-->> hostId"+confBase.getCreateUser()+"--->>StartTime="+confBase.getStartTime());
//					}
//				}
//			}
//		}
//		
//
//		for(int ii=1;ii<pageCount;ii++){
//			System.out.println("-------Actor----------------------->>>>>>"+ii);
//			comingDate=confService.getMonthlyComingConfListForActor(null, userBase, ii,null,null);//(userBase, ii);
//			confList=comingDate.getDatas();
//			if(confList!=null && confList.size() > 0){
//				for(ConfBase confBase:confList){
//					if(confBase!=null){
//						System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"-->> hostId"+confBase.getCompereUser()+"-->> hostId"+confBase.getCreateUser()+"--->>StartTime="+confBase.getStartTime());
//					}
//					
//				}
//			}
//		}
//		
//		comingDate=confService.getMonthlyComingConfListForHost(userBase, 2);
//		confList=comingDate.getDatas();
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				if(confBase!=null){
//					System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"--->>StartTime="+confBase.getStartTime());
//				}
//				
//			}
//		}
//		comingDate=confService.getMonthlyComingConfListForHost(userBase, 3);
//		confList=comingDate.getDatas();
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				if(confBase!=null){
//					System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"--->>StartTime="+confBase.getStartTime());
//				}
//				
//			}
//		}
//		comingDate=confService.getMonthlyComingConfListForHost(userBase, 4);
//		confList=comingDate.getDatas();
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				if(confBase!=null){
//					System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"--->>StartTime="+confBase.getStartTime());
//				}
//				
//			}
//		}
//		comingDate=confService.getMonthlyComingConfListForHost(userBase, 5);
//		confList=comingDate.getDatas();
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				if(confBase!=null){
//					System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"--->>StartTime="+confBase.getStartTime());
//				}
//				
//			}
//		}
//		comingDate=confService.getMonthlyComingConfListForHost(userBase, 6);
//		confList=comingDate.getDatas();
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				if(confBase!=null){
//					System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"--->>StartTime="+confBase.getStartTime());
//				}
//				
//			}
//		}
//		comingDate=confService.getMonthlyComingConfListForHost(userBase, 7);
//		confList=comingDate.getDatas();
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				if(confBase!=null){
//					System.out.println("confId="+confBase.getId()+"--->>confName="+confBase.getConfName()+"--->>StartTime="+confBase.getStartTime());
//				}
//				
//			}
//		}
//		System.out.println(comingDate.getDatas().size());
 
		
//		String base64="aHR0cDovL3d3dy5jb25mY2xvdWQuY24vb3BlbmFwaS9DbGllbnRBUEkjaHR0cDovL3d3dy5jb25mY2xvdWQuY24vZG93bmxvYWQjMjA1MiMxIzk5OQ==";
//		
//
//		System.out.println(Base64.decode(base64));
//		
//		
//				System.out.println(Base64.decode("aHR0cDovL3d3dy5jb25mY2xvdWQuY24vb3BlbmFwaS9DbGllbnRBUEkjaHR0cDovL3d3dy5jb25mY2xvdWQuY24vZG93bmxvYWQjMiMxIzQ="));
//		System.out.println(Base64.decode("am9pbi9qb2lucGFnZT9qb2luVHlwZT0zJmNJZD0yMCZzY29kZT02NDIwMzAzNDAmdUlkPTA="));
//		
//		ConfService confService = AppContextFactory.getAppContext().getBean(ConfService.class);
//		ConfManagementService confManageService=AppContextFactory.getAppContext().getBean(ConfManagementService.class);
//		ConfBase conf=confService.getConfBasebyConfId(744);
////		confManageService.queryConfInfo(conf.getConfHwid(), null, null);
//		confService.syncConfStatus(conf);
//		
//		//confManageService.
//		
////		System.out.println(empowerConfigService.getSiteEmpowerConfigBySiteId(27));
//		confService=null;
//		EmpowerConfigService empowerConfigService = AppContextFactory.getAppContext().getBean(EmpowerConfigService.class);
//		EmpowerConfig empowerConfig = new EmpowerConfig();
//		empowerConfig.setSiteId(48);
//		empowerConfig.setUserId(-1);
//		empowerConfig.setPhoneFlag(1);
//		empowerConfig.setAutoFlag(1);
//		empowerConfig.setRecordFlag(0);
//		empowerConfig.setShareMediaFlag(0);
//		empowerConfig.setEmTime(com.bizconf.audio.util.DateUtil.getGmtDate(null));
//		empowerConfig.setEmUser(1);
//		empowerConfig.setEmUserType(9999);
////		empowerConfigService.saveSiteEmpowerGlobal(empowerConfig);
//		ConfService confService=AppContextFactory.getAppContext().getBean(ConfService.class);
//		ConfManagementService confManageService=AppContextFactory.getAppContext().getBean(ConfManagementService.class);
//		SiteService siteService=AppContextFactory.getAppContext().getBean(SiteService.class);
//		UserService userService=AppContextFactory.getAppContext().getBean(UserService.class);
//		
//		//取即将召开、正在召开会议列表
//		List<ConfBase> confList=confService.getConfListForTask();
//		
//		List<ConfBase> openingList=new ArrayList<ConfBase>();//正在召开的会议列表
//		List<ConfBase> comingList=new ArrayList<ConfBase>();//即将开始的会议列表
//		
//		if(confList!=null && confList.size() > 0){
//			for(ConfBase confBase:confList){
//				if(confBase!=null){
//					if(ConfConstant.CONF_STATUS_OPENING.equals(confBase.getConfStatus())){
//						openingList.add(confBase);
//					}
//					if(ConfConstant.CONF_STATUS_SUCCESS.equals(confBase.getConfStatus())){
//						comingList.add(confBase);
//					}
//				}
//			}
//		}
//		confList=null;
//		
//		SiteBase siteBase=null;
//		UserBase userBase=null;
//		if(openingList != null && openingList.size() > 0){
//			for(ConfBase confBase:openingList){
//				if(confBase != null){
//					siteBase=siteService.getSiteBaseById(confBase.getSiteId());
//					userBase=userService.getUserBaseById(confBase.getCreateUser());
//					confBase=confManageService.queryConfInfo(confBase.getId()+"", siteBase, userBase);
//					if(ConfConstant.CONF_STATUS_FINISHED.equals(confBase.getConfStatus())){
//						//更新会议状态
//						confService.updateConfStatus(confBase, ConfConstant.CONF_STATUS_FINISHED);
//						//查找会议的与会人数
//						
//					}
//				}
//			}
//		}
//		if(comingList != null && comingList.size() > 0){
//			for(ConfBase confBase:openingList){
//				if(confBase != null){
//					siteBase=siteService.getSiteBaseById(confBase.getSiteId());
//					userBase=userService.getUserBaseById(confBase.getCreateUser());
//					confBase=confManageService.queryConfInfo(confBase.getId()+"", siteBase, userBase);
//					if(ConfConstant.CONF_STATUS_OPENING.equals(confBase.getConfStatus())){
//						//更新会议状态
//						confService.updateConfStatus(confBase, ConfConstant.CONF_STATUS_OPENING);
//					}
//				}
//			}
//		}
//
//		System.out.println(openingList);
//		System.out.println(comingList);
//		comingList=null;
//		openingList=null;
//		confService=null;
//		confManageService=null;
//		siteService=null;
//		userService=null;
//	}
	}

}
