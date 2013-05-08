package com.bizconf.audio.task.site;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.service.EmailService;
import com.bizconf.audio.task.AppContextFactory;
import com.bizconf.audio.task.support.TaskExecutorFactory;

/**
 * 站点到期任务执行器
 * <p>
 * 
 * @Author  
 */
public class SiteRemindExecutor {

	private static final Log logger = LogFactory.getLog(SiteRemindExecutor.class);

	private static final int THREAD_POOL_SIZE = 2;

	private static final long REST_INTERVAL = 6*3600 * 1000L;


	private static final Executor executor = TaskExecutorFactory
			.newExecutor(THREAD_POOL_SIZE);

	public static void main(String[] args) throws IOException {
		long border = 0;
		
		EmailService emailService = AppContextFactory.getAppContext().getBean(EmailService.class);
		
		while (true) {
			try {
				//需要提醒的站点
				List<SiteBase> sites = emailService.getExpireRemindSites();
				
				//过期未提醒站点
				List<SiteBase> expsites = emailService.getExpiredSites();
				// 运行索引任务
				if (sites != null && !sites.isEmpty()) {
					for (SiteBase site : sites) {
						Runnable task = new SiteRemindTask(site, SiteConstant.SEND_SITE_EXP_REMIND);
						System.out.println("Now to do site expried remind email task for id:" + site.getId());
						executor.execute(task);
						border = site.getId();
					}
				}
				if (expsites != null && !expsites.isEmpty()) {
					for (SiteBase site : sites) {
						Runnable task = new SiteRemindTask(site, SiteConstant.SEND_SITE_EXP);
						System.out.println("Now to do site expried email task for id:" + site.getId());
						executor.execute(task);
						border = site.getId();
					}
				}
				if((expsites != null && !expsites.isEmpty()) || (sites != null && !sites.isEmpty())){
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("System run exception:" + e.getLocalizedMessage());
			}

			try {
				Thread.sleep(REST_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
