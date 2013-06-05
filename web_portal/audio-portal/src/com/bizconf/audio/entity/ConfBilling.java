package com.bizconf.audio.entity;

import java.util.Iterator;
import java.util.List;

/**
 * @desc 某个会议的通信费用总和
 * @author martin
 * @date 2013-5-23
 */
public class ConfBilling {
	
	//会议号
	private String confHwid = "";
	
	//总金额
	private float sum = 0.00f;
	
	//CDR账单明细
	private List<BizBilling> billings;

	public String getConfHwid() {
		return confHwid;
	}

	public void setConfHwid(String confHwid) {
		this.confHwid = confHwid;
	}

	public float getSum() {
		return sum;
	}

	public void setSum(float sum) {
		this.sum = sum;
	}

	public List<BizBilling> getBillings() {
		return billings;
	}
	
	//设置会议账单的CDR账单  并统计会议账单总额
	public void setBillings(List<BizBilling> billings) {
		this.billings = billings;
		if(billings!=null && !billings.isEmpty()){
			for (Iterator<BizBilling> iterator = billings.iterator(); iterator.hasNext();) {
				BizBilling bill =  iterator.next();
				
				this.sum += bill.getTotalFee();
			}
			
		}
	}
	
	
	
}
