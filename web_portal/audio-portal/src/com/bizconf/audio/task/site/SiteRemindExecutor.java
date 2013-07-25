package com.bizconf.audio.task.site;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.service.SiteService;
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

	private static final long REST_INTERVAL = 10 * 1000L;


	private static final Executor executor = TaskExecutorFactory
			.newExecutor(THREAD_POOL_SIZE);

	public static void main(String[] args) throws IOException {
		long border = 0;

	//	EmailService emailService = AppContextFactory.getAppContext().getBean(EmailService.class);
		SiteService siteService = AppContextFactory.getAppContext().getBean(SiteService.class);
		
		while (true) {
			try {
				//需要提醒的站点
				List<SiteBase> siteList = siteService.getSiteListForRemind();
				if(siteList!=null && siteList.size() > 0){
					Runnable task = new SiteRemindTask(siteList);
					executor.execute(task);
				}
				siteList=null;
				
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
