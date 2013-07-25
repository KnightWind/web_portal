package com.bizconf.audio.task.billing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.bizconf.audio.component.BaseConfig;
import com.bizconf.audio.service.BillingService;
import com.bizconf.audio.task.AppContextFactory;
import com.bizconf.audio.util.DateUtil;

/**
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
			// 这里需要在配置文件中设置账单文件的转存路径
			File bakupDir = new File(BaseConfig.getInstance().getString("billstorepath",""));
			if(!bakupDir.exists() || !bakupDir.isDirectory()){
				bakupDir.mkdir();
			}
			
			if(this.cdrFile!=null){
				String storePath = BaseConfig.getInstance().getString("billstorepath","")+File.separator+cdrFile.getName();
				storeFile(cdrFile, storePath);
				if(!billService.readCdrBillFile(cdrFile)){
					System.out.println("reading cdr data have exception happen, It's will store the cdr data file to:"+storePath);
				}
			}
			
			if(this.rentFile!=null){
				// 这里需要在配置文件中设置账单文件的转存路径
				String rentstorePath = BaseConfig.getInstance().getString("billstorepath","")+File.separator+rentFile.getName();
				storeFile(rentFile, rentstorePath);
				if(!billService.readRentBillFile(rentFile)){
					System.out.print("reading monthly rent fee data may be have some exception happen, It's will store the rent data file to:"+rentstorePath);
				}
			}
			
//			Calendar calendar = Calendar.getInstance();
//			String year = String.valueOf(calendar.get(Calendar.YEAR));
//			String month = String.valueOf(calendar.get(Calendar.MONTH));
			Date startDate = DateUtil.getMonthStartDate(28800000);
			String fileName = rentFile==null?cdrFile==null?"":cdrFile.getName():rentFile.getName();
			if(fileName!=null && !fileName.equals("")){
				if(fileName.lastIndexOf(".")>-1){
					fileName = fileName.substring(0,fileName.lastIndexOf("."));
				}
				//防止文件名错误
				try{
					//获取账单日期
					int year = Integer.parseInt(fileName.substring(fileName.length()-6,fileName.length()-2));
					int month = Integer.parseInt(fileName.substring(fileName.length()-2));
					startDate = DateUtil.getMonthStratDate(year+"", month+"", 28800000);
				}catch (Exception e) {
				}
			}
			Date endDate = DateUtil.getMonthEndDate(startDate);
			billService.genMonthyTotalFee(startDate, endDate);
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
