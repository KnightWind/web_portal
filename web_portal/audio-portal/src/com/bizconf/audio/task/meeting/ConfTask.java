package com.bizconf.audio.task.meeting;

import java.util.List;

import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.task.AppContextFactory;

/**
 * 同步会议状态的任务
 * 
 * @author Chris Gao
 *
 */
public class ConfTask extends Thread implements Runnable {

	// ConfBase conf;
	private List<ConfBase> confList;
	ConfBase conf;

	public ConfTask (ConfBase conf) {
		this.conf = conf;
	}
	
	public ConfTask(List<ConfBase> confList) {
		this.confList = confList;
	}

	public void run() {
		ConfService confService = AppContextFactory.getAppContext().getBean(ConfService.class);
		try {
			boolean result = confService.syncConfStatus(conf);
			if (result) {
				System.out.println("Conf status task for :" + conf.getId() + " success.");
			}
			else{
				System.out.println("Conf status task for :" + conf.getId() + " failed, waiting for next sync.");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}



}
























/*

ConfBase conf;

public ConfTask (ConfBase conf) {
	this.conf = conf;
}

public void run() {
	ConfService confService = AppContextFactory.getAppContext().getBean(ConfService.class);
	try {
		boolean result = confService.syncConfStatus(conf);
		if (result) {
			System.out.println("Conf status task for :" + conf.getId() + " success.");
		}
		else{
			System.out.println("Conf status task for :" + conf.getId() + " failed, waiting for next sync.");
		}
		
	}
	catch (Exception e) {
		e.printStackTrace();
	}
}
*/
