package com.bizconf.audio.task.site;

import java.util.List;

import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.task.AppContextFactory;

/**
 * 发送站点到期提醒
 * 
 * @author
 *
 */
public class SiteRemindTask extends Thread implements Runnable {
	
	List<SiteBase> siteList;
	 
	
	static final EmailService emailService = AppContextFactory.getAppContext().getBean(EmailService.class);
	
	public SiteRemindTask (List<SiteBase> siteList) {
		this.siteList = siteList; 
	}
	
	public void run() {
		try {
			boolean result = false;
			if(siteList!=null && siteList.size()>0){
				for(SiteBase siteBase:siteList){
					result = false;
					if(siteBase!=null){
						result = emailService.sendEmailForSiteRemind(siteBase);
					}
//					if (result) {
//						System.out.println("site task for :" + siteBase + " success.");
//					}
//					else{
//						System.out.println("site task for :" + siteBase + " failed, waiting for retry.");
//					}
				}
			}

			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
