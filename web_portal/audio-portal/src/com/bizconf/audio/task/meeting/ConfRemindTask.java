package com.bizconf.audio.task.meeting;

import java.util.List;

import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfUser;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.ConfUserService;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.task.AppContextFactory;

/**
 * 
 * @author zhaost
 *
 */
public class ConfRemindTask extends Thread implements Runnable {
	private List<ConfUser> confUserList;
//	private SiteBase siteBase;
	
	public ConfRemindTask(List<ConfUser> confUserList){
		this.confUserList=confUserList;
	}
	
	public void run(){
		ConfService confService=AppContextFactory.getAppContext().getBean(ConfService.class);
		ConfUserService confUserService=AppContextFactory.getAppContext().getBean(ConfUserService.class);
		EmailService emailService=AppContextFactory.getAppContext().getBean(EmailService.class);
		ConfBase confBase=null;
		boolean remindFlag=false;
		for(ConfUser confUser:this.confUserList){
			if(confUser != null){
				confBase=null;
				if(confUser.getConfId()!=null && confUser.getConfId().intValue() > 0){
					confBase=confService.getConfBasebyConfId(confUser.getConfId());
				}
				if(confBase == null){
					continue;
				}
				remindFlag=emailService.confRemind(confUser, confBase);
				System.out.println("confUser.getId()==="+confUser.getId()+";;;;remindFlag=="+remindFlag);
				if(remindFlag){
					confUser.setRemindFlag(ConfConstant.CONF_REMIND_FLAG_REMINDED);
					confUserService.updateRemindFlag(confUser);
				}
			}
		}
		
		this.confUserList=null;
		confBase=null;
		confService=null;
		confUserService=null;
		emailService=null;
	}

}
