package com.bizconf.audio.task.site;

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
	
	SiteBase site;
	
	Integer email_type;
	
	static final EmailService emailService = AppContextFactory.getAppContext().getBean(EmailService.class);
	
	public SiteRemindTask (SiteBase site,Integer email_type) {
		this.site = site;
		this.email_type = email_type;
	}
	
	public void run() {
		try {
			boolean result = false;
			if (email_type.equals(SiteConstant.SEND_SITE_EXP_REMIND)) {
				result = emailService.sendSiteRmindEmail(site);
			}else if(email_type.equals(SiteConstant.SEND_SITE_EXP)){
				result = emailService.sendSiteExpiredEmail(site);
			}
			
			if (result) {
				System.out.println("site task for :" + site.getId() + " success.");
			}
			else{
				System.out.println("site task for :" + site.getId() + " failed, waiting for retry.");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
