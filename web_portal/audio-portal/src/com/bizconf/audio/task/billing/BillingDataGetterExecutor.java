package com.bizconf.audio.task.billing;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bizconf.audio.component.BaseConfig;
import com.bizconf.audio.task.support.TaskExecutorFactory;

/**
 * 
 * 
 * 
 * @Author 
 */
public class BillingDataGetterExecutor {

	private static final Log logger = LogFactory.getLog(BillingDataGetterExecutor.class);

	private static final int THREAD_POOL_SIZE = 2;

	private static final long REST_INTERVAL = 3*24*3600*1000L;// 两天来一次？？

	private static final Executor executor = TaskExecutorFactory
			.newExecutor(THREAD_POOL_SIZE);

	public static void main(String[] args) throws IOException {
		while (true) {
			try {
				File cdrFile = null;
				File rentFile = null;
				String dirPath = BaseConfig.getInstance().getString("billingpath", "");//TODO 这里需要设置账单文件的路径
				File dir = new File(dirPath);
				logger.info("the billing dirPath is " +dirPath);
				if(dir.exists()){
					File[] files = dir.listFiles();
					for (int i = 0; i < files.length; i++) {
						if(files[i].getName().startsWith("bill_detail")){ //TODO 这里需要把CDR账单命名加上去
							cdrFile = files[i];
						}
						if(files[i].getName().startsWith("bill_fix")){ //TODO 这里需要把站点月租用费账单命名加上去
							rentFile = files[i];
						}
					}
				}
				Runnable task = new ReadBillingDateTask(cdrFile, rentFile);
				executor.execute(task);
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
