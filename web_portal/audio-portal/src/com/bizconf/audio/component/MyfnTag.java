package com.bizconf.audio.component;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bizconf.audio.util.DateUtil;

/**
 * @desc 
 * @author martin
 * @date 2013-3-27
 */
public class MyfnTag {
	
	/**
	 * 判断license是否可删除 （是否已生效）
	 * @param date
	 * @return
	 */
	public static boolean licenseDeleteable(Date date){
		Date now = DateUtil.getGmtDate(null);
		if(date.after(now)){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 判断站点7天内是否到期
	 * @param date
	 * @return
	 */
	public static boolean siteExpiredInWeek(Date date){
		Date now = DateUtil.getGmtDate(null);
		long expireTime = date.getTime();
		long nowTime = now.getTime();
		long gapTime = expireTime-nowTime;
		//System.out.println("expiredate"+date+"expireTime: =="+expireTime+"nowTime"+nowTime+"gapTime =="+gapTime);
		//gapTime<0 说明已过期
		if(gapTime<(7*24*3600000l) && gapTime>-24*3600000l){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断站点是否过期
	 * @param date
	 * @return
	 */
	public static boolean siteExpired(Date date){
		Date now = DateUtil.getGmtDate(null);
		long expireTime = date.getTime()+24*3600000l;
		long nowTime = now.getTime();
		long gapTime = expireTime-nowTime;
		if(gapTime<=0){
			return true;
		}
		return false;
	}
	
	
	public static String fmtDate(String parten,Date date,Integer timezone){
		SimpleDateFormat sdf = new SimpleDateFormat(parten);
		Date localDate = DateUtil.getOffsetDateByGmtDate(date, timezone.longValue());
		return sdf.format(localDate);
	}
}
