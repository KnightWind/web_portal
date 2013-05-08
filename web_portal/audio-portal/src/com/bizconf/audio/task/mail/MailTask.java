package com.bizconf.audio.task.mail;

import com.bizconf.audio.entity.EmailInfo;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.task.AppContextFactory;

/**
 * 发送邮件任务
 * 
 * @author Chris Gao
 *
 */
public class MailTask extends Thread implements Runnable {
	
	EmailInfo mail;
	
	static final EmailService emailService = AppContextFactory.getAppContext().getBean(EmailService.class);
	
	public MailTask (EmailInfo mail) {
		this.mail = mail;
	}
	
	public void run() {
		try {
			boolean result = emailService.send(mail);
			if (result) {
				emailService.updateSucceedEmailById(mail.getId());
				System.out.println("Mail task for :" + mail.getId() + " success.");
			}
			else{
				emailService.updateUnSucceedEmailById(mail.getId());
				System.out.println("Mail task for :" + mail.getId() + " failed, waiting for retry.");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
