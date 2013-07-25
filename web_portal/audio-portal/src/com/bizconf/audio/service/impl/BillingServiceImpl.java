package com.bizconf.audio.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.bizconf.audio.component.BaseConfig;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.BizBilling;
import com.bizconf.audio.entity.ConfBilling;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.BillingService;
import com.bizconf.audio.task.AppContextFactory;
import com.bizconf.audio.util.DateUtil;

/**
 * @desc 
 * @author Administrator
 * @date 2013-5-21
 */
@Service
public class BillingServiceImpl extends BaseService implements BillingService{
	
	public static Map<String,Integer> EVENT_MAP = new HashMap<String, Integer>();
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat rentsdf = new SimpleDateFormat("yyyyMMdd");
	static{
		EVENT_MAP.put("Shrine_BC_Dail Out", 1);
		EVENT_MAP.put("Shrine_BC_Toll Free dail in", 2);
		EVENT_MAP.put("Shrine_BC_Local conect Dail In", 3);
		EVENT_MAP.put("Shrine_BC_Playback", 4);
		EVENT_MAP.put("Web Connection", 5);
	}
	
	@Override
	public PageBean<BizBilling> getBillingPage(int pageNo, int pageSize,
			Map<String, Object> conditionMap) {
		//TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PageBean<BizBilling> getSysBillingPage(int pageNo, int pageSize,
			SystemUser sysAdmin, Date startDate, Date endDate, String keyword,
			String sortFiled) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder baseSql = new StringBuilder("select * from T_BIZ_BILLING tb where 1=1 and tb.bill_type = ?");
		values.add(2);
		
		PageBean<BizBilling> page = null;
		if(!sysAdmin.isSuperSystemAdmin()){
			baseSql.append(" and tb.site_sign in (select ts.site_sign from t_site_base ts where ts.create_user = ?)");
			values.add(sysAdmin.getId());
		}
		
		if(startDate!=null){
			baseSql.append(" and tb.bill_date > ?");
			values.add(startDate);
		}
		
		if(endDate!=null){
			baseSql.append(" and tb.bill_date < ?");
			values.add(endDate);
		}
		
		if(keyword!=null &&!keyword.equals("")){
			baseSql.append(" and tb.site_sign like ? ");
			values.add("%"+ keyword +"%");
		}
		
		baseSql.append(" order by ");
		if(sortFiled!=null && !sortFiled.equals("")){
			baseSql.append(sortFiled);
		}else{
			baseSql.append(" site_sign ");
		}
		
		page = getPageBeans(BizBilling.class, baseSql.toString(), pageNo, pageSize, values.toArray());
		
		return page;
	}
	
	@Override
	public PageBean<BizBilling> getSiteBillingPage(int pageNo, int pageSize,
			SiteBase site, Date startDate, Date endDate, String sortFiled) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder baseSql = new StringBuilder("select * from T_BIZ_BILLING tb where 1=1 and tb.bill_type = ?");
		values.add(2);
		
		PageBean<BizBilling> page = null;
		if(site!=null){
			baseSql.append(" and tb.site_sign = ?");
			values.add(site.getSiteSign());
		}
		
		if(startDate!=null){
			baseSql.append(" and tb.bill_date > ?");
			values.add(startDate);
		}
		
		if(endDate!=null){
			baseSql.append(" and tb.bill_date < ?");
			values.add(endDate);
		}
		baseSql.append(" order by ");
		if(sortFiled!=null && !sortFiled.equals("")){
			baseSql.append(sortFiled);
		}else{
			baseSql.append(" bill_date desc ");
		}
		page = getPageBeans(BizBilling.class, baseSql.toString(), pageNo, pageSize, values.toArray());
		
		return page;
	}
	
	@Override
	public PageBean<BizBilling> getUserBillingPage(int pageNo, int pageSize,
			UserBase user, Date startDate, Date endDate, String sortFiled) {
		StringBuilder sqlBuilder = new StringBuilder("select * from t_biz_billing tb where 1=1 ");
		PageBean<BizBilling> page = null;
		List<Object> values = new ArrayList<Object>();
		try {
			SiteBase site = libernate.getEntity(SiteBase.class,user.getSiteId());
			sqlBuilder.append(" and tb.site_sign =? ");
			values.add(site.getSiteSign());
			//name_host
			if (site.getChargeMode().intValue() == 1) {
				sqlBuilder.append(" and tb.bill_type = ?  and tb.user_id = ? ");
				values.add(2);
				values.add(user.getLoginName());
			
			}else if(site.getChargeMode().intValue() == 2 || site.getChargeMode().intValue() == 3){
				
				sqlBuilder.append(" and tb.bill_type = ? ");
				values.add(1);
				
			}else if(site.getChargeMode().intValue() == 4){
				
				sqlBuilder.append(" and tb.bill_type = ?  and EXISTS(select tc.id from t_conf_base where tc.conf_hwid = tb.conf_hwid and tc.compere_user=?)");
				values.add(0);
				values.add(user.getId());
			}
			
			if(startDate==null || endDate==null){
				startDate = DateUtil.getMonthStartDate(DateUtil.BJ_TIME_OFFSET);
				endDate = DateUtil.getMonthEndDate(startDate);
			}
			sqlBuilder.append(" and tc.bill_date between ? and ? ");
			values.add(startDate);
			values.add(endDate);
			
			page = getPageBeans(BizBilling.class, sqlBuilder.toString(), pageNo, values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
	
	public PageBean<BizBilling> getNamehostDataSubBillingPage(int pageNo, int pageSize,
			Date startDate, Date endDate,SiteBase site) {
		StringBuilder sqlBuilder = new StringBuilder("select * from t_biz_billing tb where 1=1 ");
		PageBean<BizBilling> page = null;
		List<Object> values = new ArrayList<Object>();
		try {
			sqlBuilder.append(" and tb.site_sign =? ");
			values.add(site.getSiteSign());
			//name_host
			sqlBuilder.append(" and tb.bill_type = ?  and tb.user_id != '' ");
			values.add(1);
			
			if(startDate==null || endDate==null){
				startDate = DateUtil.getMonthStartDate(DateUtil.BJ_TIME_OFFSET);
				endDate = DateUtil.getMonthEndDate(startDate);
			}
			sqlBuilder.append(" and tc.bill_date between ? and ? ");
			values.add(startDate);
			values.add(endDate);
			
			page = getPageBeans(BizBilling.class, sqlBuilder.toString(), pageNo, values.toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}
	
	@Override
	public BizBilling getBillingById(int id) {
		try {
			return libernate.getEntity(BizBilling.class, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int getSiteUserNum(int siteId,boolean isNameHost){
		String sql = "select count(*) from t_user_base where site_id = ? and del_flag = ? and user_type = ?";
		List<Object> values = new ArrayList<Object>();
		values.add(siteId);
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		values.add(ConstantUtil.USERTYPE_USERS);
		if(isNameHost){
			sql += " and user_role = ?";
			values.add(ConstantUtil.USERROLE_HOST);
		}
		
		try {
			return libernate.countEntityListWithSql(sql, values.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public PageBean<ConfBilling> getConfTelBillingPage(int pageNo,
			int pageSize, Date startDate, Date endDate, SiteBase site,Integer userId) {
		
		if(pageNo<1) pageNo = 1;
		if(pageSize<1) pageSize = ConstantUtil.PAGESIZE_DEFAULT;
		List<Object> values = new ArrayList<Object>();
		values.add(site.getSiteSign());
		values.add(0);
		StringBuilder basesql = new StringBuilder(" from t_biz_billing tb where tb.site_sign = ? and tb.bill_type = ? ");
		if(userId!=null && userId>0){
			basesql.append("and EXISTS(select tc.id from t_conf_base tc where tc.conf_hwid = tb.conf_hwid and tc.compere_user=?)");
			values.add(userId);
		}
		basesql.append(" and tb.bill_date between ? and ? ");
		values.add(startDate);
		values.add(endDate);
		
		String sql = "select distinct tb.conf_hwid "+basesql.toString()+" limit ?,? ";
		String sqlforcount = "select count(distinct tb.conf_hwid) "+basesql.toString();
		
		List<ConfBilling> confBillings = new ArrayList<ConfBilling>();
		PageBean<ConfBilling> page = new PageBean<ConfBilling>();
		page.setPageNo(String.valueOf(pageNo));
		page.setPageSize(pageSize);
		try{
			int count = libernate.countEntityListWithSql(sqlforcount,values.toArray());
			page.setRowsCount(count);
			//可否这样查询？
			values.add((pageNo-1)*pageSize);
			values.add(pageSize);
			List<Object> hwIds = libernate.getObjectList(sql,values.toArray());
			if(hwIds!=null && !hwIds.isEmpty()){
				for (Iterator<Object> iterator = hwIds.iterator(); iterator.hasNext();) {
					Object object = iterator.next();
					List<BizBilling> bills = getCdrBillByConfHwid(object.toString());
					ConfBilling cb = new ConfBilling();
					cb.setConfHwid(object.toString());
					cb.setBillings(bills);
					confBillings.add(cb);
				}
			}
			page.setDatas(confBillings);
		}catch(Exception e){
			e.printStackTrace();
		}
		return page;
	}
	
	@Override
	public List<BizBilling> getCdrBillByConfHwid(String confHwid){
		String sql = "select * from t_biz_billing where conf_hwid = ? and bill_type = ?";
		List<BizBilling> bills = null;
		try {
			bills =  libernate.getEntityListBase(BizBilling.class, sql, new Object[]{confHwid,0});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bills;
	}
	
	
	/**
	 * 读取cdr账单文件 入库
	 * @param cdrFile
	 * @return
	 */
	@Override
	public boolean readCdrBillFile(File cdrFile){
		boolean flag = true;
		BufferedReader reader = null;
		BufferedWriter errorWriter = null;
		SimpleDateFormat nsdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			String logDir = BaseConfig.getInstance().getString("exceptionlog", "");
			File logDirFile = new File(logDir);
			if(!logDirFile.exists() || !logDirFile.isDirectory()){
				logDirFile.mkdir();
			}
			String errorFilePath = logDir+File.separator+nsdf.format(new Date())+"_CDR_exception.log";
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(cdrFile),"utf-8"));
			String temp = null;
			while((temp=reader.readLine())!=null){
				logger.info("got CDR billing massge: "+temp);
				String[] values = temp.split("\\|");
				BizBilling billing = new BizBilling();
				try{
					billing.setBillType(0);
					billing.setSiteSign(values[0]);
					if(values[0]!=null && values[0].indexOf("[")>-1){
						billing.setSiteSign(getParentSiteSign(values[0]));
					}
					billing.setConfHwid(values[1]);
					billing.setUserName(values[2]);
					if(values[3].equals("PC")){
						billing.setEnterType(1);
					}else if(values[3].equals("PHONE")){
						billing.setEnterType(2);
					}
					billing.setEnterNumber(values[4]);
					billing.setCallNumber(values[5]);
					billing.setEventType(getCallType(values[6]));
					
					billing.setStartDate(sdf.parse(values[7]));
					//账单日期
					billing.setBillDate(sdf.parse(values[7]));
					if(values[7]!=null && values[7].endsWith("00")){
						billing.setBillDate(new Date(sdf.parse(values[7]).getTime()+1000l));
					}
					//账单生成日期
					billing.setCreateDate(DateUtil.getGmtDate(new Date())); 
					billing.setDuration(getDurationSecByFormatStr(values[8]));
					billing.setTelFee(Float.parseFloat(values[9]));
					
					libernate.saveEntity(billing);
				}catch(Exception e){
					//TODO  这里要记录发生异常的信息
					flag = false;
					if(errorWriter==null){
						errorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(errorFilePath)),"UTF-8"));
					}
					errorWriter.write(temp);//记录错误行数内容
					errorWriter.newLine(); 
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}finally{
			try{
				if(reader!=null){
					reader.close();
				}
				if(errorWriter!=null){
					errorWriter.flush();
					errorWriter.close();
				}
			}catch (Exception e) {
				logger.error(" when reader CDR billing:close reader and writer failed! ");
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	private int getDurationSecByFormatStr(String durationstr){
		int count = 0;
		String[] times = durationstr.split(":");
		count += Integer.parseInt(times[0])*3600;
		count += Integer.parseInt(times[1])*60;
		count += Integer.parseInt(times[2]);
		
		return count;
	}
	
	
	/**
	 * 读取月固定账单文件入库
	 * @param cdrFile
	 * @return
	 */
	@Override
	public boolean readRentBillFile(File rentFile){
		boolean flag = true;
		BufferedReader reader = null;
		BufferedWriter errorWriter = null;
		SimpleDateFormat nsdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			String logDir = BaseConfig.getInstance().getString("exceptionlog", "");
			File logDirFile = new File(logDir);
			if(!logDirFile.exists() || !logDirFile.isDirectory()){
				logDirFile.mkdir();
			}
			String errorFilePath = logDir+File.separator+nsdf.format(new Date())+"_RENT_exception.log";
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(rentFile),"utf-8"));
			String temp = null;
			while((temp=reader.readLine())!=null){
				logger.info("got RENT billing massge: "+temp);
				String[] values = temp.split("\\|");
				BizBilling billing = new BizBilling();
				try{
					billing.setBillType(1);
					billing.setSiteSign(values[0]);
					if(values[0]!=null && values[0].indexOf("[")>-1){
						billing.setSiteSign(getParentSiteSign(values[0]));
					}
					billing.setDataFee(Float.parseFloat(values[1]));
					//账单日期
					billing.setBillDate(rentsdf.parse(values[2]));
					billing.setStartDate(rentsdf.parse(values[2]));
					if(values.length>3){
						billing.setUserId(values[3]);
						if(values[3]!=null && values[3].indexOf("[")>-1 && values[3].indexOf("]")>-1){
							billing.setUserId(getUserSign(values[3]));
							billing.setSiteSign(getParentSiteSign(values[3]));
						}
					}else{
						billing.setUserId(getUserSign(values[0]));
					}
					billing.setCreateDate(DateUtil.getGmtDate(new Date()));
					libernate.saveEntity(billing);
				}catch(Exception e){
					//TODO  这里要记录发生异常的信息
					flag = false;
					if(errorWriter==null){
						errorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(errorFilePath)),"UTF-8"));
					}
					errorWriter.write("exception RENT billing line is: "+temp);//记录错误行数内容
					errorWriter.newLine(); 
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}finally{
			try{
				if(reader!=null){
					reader.close();
				}
				if(errorWriter!=null){
					errorWriter.flush();
					errorWriter.close();
				}
			}catch (Exception e) {
				logger.error(" when reader RENT billing:close reader and writer failed! ");
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	//生成这个月各个站点的总账单
	@Override
	public boolean genMonthyTotalFee(Date startDate, Date endDate){
		boolean flag = true;
		String sql = "SELECT tb.site_sign, SUM(tb.tel_fee) AS tel_fee, SUM(tb.data_fee) AS data_fee FROM t_biz_billing tb WHERE tb.bill_type !=? and tb.bill_date "
					+" BETWEEN ? AND ? GROUP BY tb.site_sign";
		try{
			List<BizBilling> billings = libernate.getEntityListBase(BizBilling.class, sql, new Object[]{2,startDate,endDate});
			if(billings!=null && !billings.isEmpty()){
				for (Iterator ite = billings.iterator(); ite.hasNext();) {
					BizBilling bizBilling = (BizBilling) ite.next();
					bizBilling.setBillType(2);
					bizBilling.setBillDate(new Date(startDate.getTime()+2*36000000l));
					bizBilling.setCreateDate(new Date());
					bizBilling.setStartDate(new Date(startDate.getTime()+1000l));
					BizBilling oldBill =  getMonthlyBill(bizBilling.getSiteSign(), startDate, endDate);
					if(oldBill!=null){
						oldBill.setDataFee(bizBilling.getDataFee());
						oldBill.setTelFee(bizBilling.getTelFee());
						libernate.updateEntity(oldBill);
					}else{
						libernate.saveEntity(bizBilling);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	// 获取月总账单
	public BizBilling getMonthlyBill(String site_sign,Date startDate, Date endDate){
		String sql = "select * from t_biz_billing where site_sign = ? and bill_type = ? and bill_date "
					+" BETWEEN ? AND ? ";
		BizBilling bill = null;
		try {
			bill = libernate.getEntityCustomized(BizBilling.class,sql, new Object[]{site_sign,2,startDate,endDate});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bill;
	}
	
	/**
	 * 获取用户每月通信费总额
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public float getUserTotalFee(UserBase user,Date startDate, Date endDate){
		float total = 0.00f;
		List<Object> values = new ArrayList<Object>();
		values.add(0);
		values.add(user.getId());
		values.add(startDate);
		values.add(endDate);
		StringBuilder basesql = new StringBuilder(" select sum(tb.tel_fee) as total  from t_biz_billing tb where tb.bill_type = ? ");
		basesql.append("and EXISTS(select tc.id from t_conf_base tc where tc.conf_hwid = tb.conf_hwid and tc.compere_user=?)");
		basesql.append(" and tb.bill_date between ? and ? ");
		try{
			List<Object> objs = libernate.getObjectList(basesql.toString(), values.toArray());
			if(objs!=null && !objs.isEmpty()){
				Double value = (Double)objs.get(0);
				if(value!=null){
					total = value.floatValue();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
	
	
	public void test(){
		String sql = "select sum(totalfee) as fee from t_test t ";
		try {
			List<Object> objs = libernate.getObjectList(sql, new Object[]{});
			for (Iterator ite = objs.iterator(); ite.hasNext();) {
				float l = 1.00f;
				Double f = (Double) ite.next();
				f.floatValue();
				System.out.println(" obj "+f.floatValue());
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String ... strs){
		
		BillingService billingService = AppContextFactory.getAppContext().getBean(BillingService.class);
	}
	
	
	@Override
	public float getConstantFee(SiteBase site, UserBase user, Date startDate,
			Date endDate) {
		
		float total = 0.00f;
		List<Object> values = new ArrayList<Object>();
		values.add(1);
		StringBuilder basesql = new StringBuilder(" select sum(tb.data_fee) as total  from t_biz_billing tb where tb.bill_type = ? ");
		if(site!=null){
			basesql.append(" and tb.site_sign = ? ");
			values.add(site.getSiteSign());
		}
		if(user!=null){
			basesql.append(" and tb.user_id = ?");
			values.add(user.getLoginName());
		}
		basesql.append(" and tb.bill_date between ? and ? ");
		values.add(startDate);
		values.add(endDate);
		try{
			List<Object> objs = libernate.getObjectList(basesql.toString(), values.toArray());
			if(objs!=null && !objs.isEmpty()){
				Double value = (Double)objs.get(0);
				if(value!=null){
					total = value.floatValue();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}
 
	@Override
	public Map<String, SiteBase> getBillBelongSite(List<BizBilling> billings) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<BizBilling> getDataBillings(SiteBase site, UserBase user,
			Date startDate, Date endDate) {
		StringBuilder sqlBuilder = new StringBuilder("select * from t_biz_billing tb where 1=1 and tb.bill_type = ? ");
		List<Object> values = new ArrayList<Object>();
		values.add(1);
		try {
			if(site!=null){
				sqlBuilder.append(" and tb.site_sign =? ");
				values.add(site.getSiteSign());
				if(site.getChargeMode().intValue() == 1 || site.getChargeMode().intValue() == 4){
					sqlBuilder.append(" and tb.user_id != '' ");
				}
			}
			if(user!=null){
				sqlBuilder.append(" and tb.user_id = ? ");
				values.add(user.getLoginName());
			} 
			sqlBuilder.append(" and tb.bill_date between ? and ? ");
			values.add(startDate);
			values.add(endDate);
			return libernate.getEntityListBase(BizBilling.class, sqlBuilder.toString(), values.toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//获取父站点标识
	public String getParentSiteSign(String value){
		if(value!=null && !value.equals("") && value.indexOf("[")>-1){
			return value.substring(0,value.indexOf("["));
		}
		return "";
	}
	
	//获取用户ID
	public String getUserSign(String value){
		if(value!=null && !value.equals("") && value.indexOf("[")>-1 && value.indexOf("]")>-1){
			return value.substring(value.lastIndexOf("[")+1, value.lastIndexOf("]"));
		}
		return "";
	}
	
	
	public int getCallType(String type){
		if(type==null) return 0;
		
		Set<String> keys = EVENT_MAP.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String str = (String) it.next();
			if (type.equalsIgnoreCase(str)) {
				return EVENT_MAP.get(str);
			}
		}
		return 0;
	}
}
