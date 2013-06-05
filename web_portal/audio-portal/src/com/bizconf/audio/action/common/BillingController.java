package com.bizconf.audio.action.common;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.entity.BizBilling;
import com.bizconf.audio.entity.ConfBilling;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.SystemUser;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.BillingService;
import com.bizconf.audio.service.LicService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.DateUtil;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;

/**
 * @desc 
 * @author Administrator
 * @date 2013-5-21
 */
@ReqPath("billing")
public class BillingController extends BaseController {
	
	@Autowired
	BillingService billingService;
	@Autowired
	UserService userService;
	@Autowired
	SiteService siteService;
	@Autowired
	LicService licService;
	
	
	//企业
	@AsController(path = "goSysBilling")
	public Object goSysBilling(HttpServletRequest request){
			return new ActionForward.Forward("/jsp/system/sysBillMain.jsp");
	}
	
	//系统
	@AsController(path = "sysBillList")
	public Object sysBillList(@CParam("keyword") String keyword, 
			@CParam("pageNo")Integer pageNo,@CParam("year")String year,
			@CParam("month")String month, HttpServletRequest request){
		
		Date startDate = DateUtil.getMonthStratDate(year, month, 0);
		Date endDate = new Date(DateUtil.getMonthEndDate(startDate).getTime()-1000l);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(startDate.getTime()+36000000l));
		request.setAttribute("year", c.get(Calendar.YEAR));
		request.setAttribute("month", c.get(Calendar.MONTH)+1);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		
		SystemUser sysAdmin = userService.getCurrentSysAdmin(request);
		PageBean<BizBilling> page = billingService.getSysBillingPage(pageNo, ConstantUtil.PAGESIZE_DEFAULT, sysAdmin, startDate, endDate, keyword, null);
		if(page!=null && page.getDatas()!=null){
			Map<String, SiteBase> siteMap = getBillingSitesMap(page.getDatas());
			request.setAttribute("siteMap", siteMap);
		}
		request.setAttribute("pageModel", page);
		return new ActionForward.Forward("/jsp/system/mybilling_list.jsp");
	}
	
	//企业
	@AsController(path = "goSiteBilling")
	public Object goSiteBilling(HttpServletRequest request){
		return new ActionForward.Forward("/jsp/admin/siteBillMain.jsp");
	}
		
	//企业
	@AsController(path = "siteBillList")
	public Object siteBillList(@CParam("pageNo")Integer pageNo,@CParam("year")String year,
				@CParam("month")String month, HttpServletRequest request){
			
			Date startDate = DateUtil.getMonthStratDate(year, month, 0);
			Date endDate = new Date(DateUtil.getMonthEndDate(startDate).getTime()-1000l);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(startDate.getTime()+36000000l));
			request.setAttribute("year", c.get(Calendar.YEAR));
			request.setAttribute("month", c.get(Calendar.MONTH)+1);
			
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			//billingService.genMonthyTotalFee(startDate,endDate);
			UserBase user = userService.getCurrentSiteAdmin(request);
			SiteBase site = siteService.getSiteBaseById(user.getSiteId());
			
			request.setAttribute("site", site);
			PageBean<BizBilling> page = billingService.getSiteBillingPage(pageNo, ConstantUtil.PAGESIZE_DEFAULT, site, startDate, endDate, null);
			request.setAttribute("pageModel", page);
			return new ActionForward.Forward("/jsp/admin/mybilling_list.jsp");
	}
	
	
	//企业
	@AsController(path = "userBillList")
	public Object userBillList(HttpServletRequest request){
			return new ActionForward.Forward("/jsp/user/mybilling.jsp");
	}
	
	//企业
	@AsController(path = "userBillListIndex")
	public Object userBillListIndex(@CParam("keyword") String keyword, 
					@CParam("pageNo")Integer pageNo,@CParam("year")String year,
					@CParam("month")String month, HttpServletRequest request){
			Date startDate = DateUtil.getMonthStratDate(year, month, 0);
			Date endDate = new Date(DateUtil.getMonthEndDate(startDate).getTime()-1000l);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(startDate.getTime()+36000000l));
			UserBase user = userService.getCurrentUser(request);
			SiteBase site = siteService.getSiteBaseById(user.getSiteId());
			
//			billingService.genMonthyTotalFee(startDate,endDate);	
			//显示站点当月账单
			
			float conFee = billingService.getConstantFee(site, user, startDate, endDate);
			request.setAttribute("conFee", conFee);
			
			float fee = billingService.getUserTotalFee(user, startDate, endDate);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("user", user);
			request.setAttribute("fee", fee);
			request.setAttribute("site", site);
			request.setAttribute("year", c.get(Calendar.YEAR));
			request.setAttribute("month", c.get(Calendar.MONTH)+1);
			return new ActionForward.Forward("/jsp/user/mybilling_list.jsp");
	}
	
	//查看数据费用详细 （企业  系统）
	@AsController(path = "showDataDetail")
	public Object showDataDetail(@CParam("id") int id,@CParam("userId") int userId,
					@CParam("pageNo")Integer pageNo,@CParam("year")String year,
					@CParam("month")String month, HttpServletRequest request){
			Date startDate = DateUtil.getMonthStratDate(year, month, null);
			Date endDate = DateUtil.getMonthEndDate(startDate);
			SiteBase site = null;
			UserBase user = null;
			List<BizBilling> billings = null;
			if(id>0){
				BizBilling bill = billingService.getBillingById(id);
				request.setAttribute("bill", bill);
				site = siteService.getSiteBaseBySiteSign(bill.getSiteSign());
			}else if(userId>0){
				user= userService.getUserBaseById(userId);
				site = siteService.getSiteBaseById(user.getSiteId());
			}
			billings = billingService.getDataBillings(site, user, startDate, endDate);
			
			Map<String, Integer> licMap = licService.getHostsLienseDatasMap(billings, site);
			request.setAttribute("licMap", licMap);
			
			request.setAttribute("sitelic", site.getLicense()+licService.getSiteLicenseNum(site.getId()));
			request.setAttribute("total", getTotalFee(billings));
			request.setAttribute("billings", billings);
			request.setAttribute("site", site);
			
//			if(site.getChargeMode().intValue()==1){
//				PageBean<BizBilling> page = billingService.getNamehostDataSubBillingPage(pageNo, 10, startDate, endDate, site);
//				if(page!=null&&page.getDatas()!=null){
//					Map<String, Integer> licMap = licService.getHostsLienseDatasMap(page.getDatas(), site);
//					request.setAttribute("licMap", licMap);
//				}
//				request.setAttribute("page", page);
//			}else if(site.getChargeMode().intValue()==2|| site.getChargeMode().intValue()==3){
//				//active user  seat
//				int usernum = billingService.getSiteUserNum(site.getId(), false);
//				request.setAttribute("usernum", usernum);
//			}
			return new ActionForward.Forward("/jsp/common/dataFee_list.jsp");
	}
	
	
	private float getTotalFee(List<BizBilling> billings){
		float totalFee = 0f;
		if(billings!=null && !billings.isEmpty()){
			for (Iterator it = billings.iterator(); it.hasNext();) {
				BizBilling bizBilling = (BizBilling) it.next();
				totalFee += bizBilling.getTotalFee();
			}
		}
		return totalFee;
	}
	
	//查看通信费用详情
	@AsController(path = "showTelDetail")
	public Object showTelDetail(@CParam("id") int id,@CParam("userId") Integer userId, 
					@CParam("pageNo")Integer pageNo,@CParam("year")String year,
					@CParam("month")String month, HttpServletRequest request){
			
			Date startDate = DateUtil.getMonthStratDate(year, month, null);
			Date endDate = DateUtil.getMonthEndDate(startDate);
			SiteBase site = null;
			if(id>0){
				BizBilling bill = billingService.getBillingById(id);
				site = siteService.getSiteBaseBySiteSign(bill.getSiteSign());
				request.setAttribute("bill", bill);
			}else{
				site = siteService.getCurrentSiteBaseByUserLogin(request);
			}
			
			if(userId!=null && userId.intValue()>0){
				UserBase user = userService.getUserBaseById(userId);
				float fee = billingService.getUserTotalFee(user, startDate, endDate);
				request.setAttribute("fee", fee);
			}
			PageBean<ConfBilling> page = billingService.getConfTelBillingPage(pageNo, ConstantUtil.PAGESIZE_DEFAULT, startDate, endDate, site, userId);
			
			request.setAttribute("pageModel", page);
			request.setAttribute("site", site);
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			return new ActionForward.Forward("/jsp/common/bill_detaillist.jsp");
	}
	
	
	private Map<String, SiteBase> getBillingSitesMap(List<BizBilling> billings){
		if(billings!=null && !billings.isEmpty()){
			Map<String, SiteBase> siteMap = new HashMap<String, SiteBase>(16);

			for (Iterator it = billings.iterator(); it.hasNext();) {
				BizBilling bizBilling = (BizBilling) it.next();
				siteMap.put(bizBilling.getSiteSign(), siteService.getSiteBaseBySiteSign(bizBilling.getSiteSign()));
			}
			return siteMap;
		}
		return null;
	}
}
