package com.bizconf.audio.task.billing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.bizconf.audio.component.BaseConfig;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.service.BillingService;
import com.bizconf.audio.task.AppContextFactory;

/**
 * 发送站点到期提醒
 * 
 * @author
 *
 */
public class ReadBillingDateTask extends Thread implements Runnable {
	
	File cdrFile;
	
	File rentFile;
	
	static final BillingService billService = AppContextFactory.getAppContext().getBean(BillingService.class);
	
	public ReadBillingDateTask (File cdrFile, File rentFile) {
		this.cdrFile = cdrFile;
		this.rentFile = rentFile;
	}
	
	public void run() {
		try {
			//TODO 这里需要在配置文件中设置账单文件的转存路径
			String storePath = BaseConfig.getInstance().getString("billstorepath","")+File.separator+cdrFile.getName();
			if(!billService.readCdrBillFile(cdrFile)){
				System.out.println("reading cdr data have exception happen, It's will store the cdr data file to:");
			}
			storeFile(cdrFile, storePath);
			
			//TODO 这里需要在配置文件中设置账单文件的转存路径
			String rentstorePath = BaseConfig.getInstance().getString("billstorepath","")+File.separator+rentFile.getName();
			if(!billService.readCdrBillFile(rentFile)){
				System.out.print("reading monthly rent fee data may be have some exception happen, It's will store the rent data file to:");
			} 
			storeFile(rentFile, rentstorePath);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("read billing data exception happened! attention!");
		}finally{
			//删除原文件
			if(this.cdrFile!=null){
				this.cdrFile.delete();
			}
			
			if(this.rentFile!=null){
				this.rentFile.delete();
			}
		}
	}
	
	
	
	private void storeFile(File file, String storePath){
		BufferedInputStream in = null;
	    BufferedOutputStream out = null; 
		try{
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(new FileOutputStream(new File(storePath)));
			int temp=0;
			byte[] bts = new byte[1024*20];
        	while((temp = in.read(bts))!=-1){
        		out.write(bts,0,temp);
        	}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				if (out!=null) {
					out.close();
				}
				if (in!=null) {
					in.close();
				}
			}catch (Exception e) {
				System.out.println("close buffer failed! ");
			}
		}
	}
}
