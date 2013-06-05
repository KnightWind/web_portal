package com.bizconf.audio.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.Flags.Flag;

import com.bizconf.audio.entity.BizBilling;
import com.bizconf.audio.entity.ConfBilling;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;

/**
 * @desc 主要处理和账单相关的业务
 * @author martin
 * @date 2013-5-21
 */
public interface BillingService {

	public PageBean<BizBilling> getBillingPage(int pageNo,int pageSize,Map<String, Object> conditionMap);

	public PageBean<BizBilling> getSysBillingPage(int pageNo,int pageSize,SystemUser sysAdmin,Date startDate,Date endDate,String keyword,String sortFiled);
	
	public PageBean<BizBilling> getSiteBillingPage(int pageNo,int pageSize,SiteBase site,Date startDate,Date endDate,String sortFiled);
	
	public PageBean<BizBilling> getUserBillingPage(int pageNo,int pageSize,UserBase user,Date startDate,Date endDate,String sortFiled);
	/**
	 * 根据ID获取一个账单记录
	 * @param id
	 * @return
	 */
	public BizBilling getBillingById(int id);
	
	/**
	 * 查询站点下面namehost租用账单
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param site
	 * @return
	 */
	public PageBean<BizBilling> getNamehostDataSubBillingPage(int pageNo, int pageSize,
			Date startDate, Date endDate,SiteBase site);
	
	/**
	 * 按会议查询该会议产生的账单
	 * @param pageNo
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param site
	 * @return
	 */
	public PageBean<ConfBilling> getConfTelBillingPage(int pageNo,int pageSize,Date startDate, Date endDate,SiteBase site,Integer userId);
	
	/**
	 * 根据会议号查找该会议下面的CDR账单
	 * @param confHwid
	 * @return
	 */
	public List<BizBilling> getCdrBillByConfHwid(String confHwid);
	
	
	/**
	 * 根据站点查询该站点开的用户数 name_host 只查询主持人数量
	 * @param siteId
	 * @return
	 */
	public int getSiteUserNum(int siteId,boolean isNameHost);
	
	/**
	 * 读取CDR账单
	 * @param cdrFile
	 * @return
	 */
	public boolean readCdrBillFile(File cdrFile);
	
	/**
	 * 读取固定账单文件
	 * @param rentFile
	 * @return
	 */
	public boolean readRentBillFile(File rentFile);
	
	/**
	 * 生成站点月度总账单
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public boolean genMonthyTotalFee(Date startDate, Date endDate);
	
	
	/**
	 * 获取用户月通信费用
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public float getUserTotalFee(UserBase user,Date startDate, Date endDate);
	
	/**
	 * 
	 * @param site
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public float getConstantFee(SiteBase site, UserBase user,Date startDate, Date endDate);


	/**
	 * 获取账单所属站点集合
	 * @param billings
	 * @return
	 */
	Map<String, SiteBase> getBillBelongSite(List<BizBilling> billings);
	
	/**
	 * 查询数据费用
	 * @param site
	 * @param user
	 * @param pageNo
	 * @return
	 */
	public List<BizBilling> getDataBillings(SiteBase site, UserBase user,Date starDate, Date endDate);
}
