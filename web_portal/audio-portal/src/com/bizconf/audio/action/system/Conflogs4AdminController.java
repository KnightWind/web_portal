package com.bizconf.audio.action.system;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.constant.ConfConstant;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.ConfLog;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.service.ConfManagementService;
import com.bizconf.audio.service.ConfService;
import com.bizconf.audio.service.SiteService;
import com.bizconf.audio.service.UserService;
import com.bizconf.audio.util.ExcelUtil;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.CParam;
import com.libernate.liberc.annotation.ReqPath;
@ReqPath("confAttendees")
public class Conflogs4AdminController extends BaseController{
	private final  Logger logger = Logger.getLogger(Conflogs4AdminController.class);
	
	@Autowired
	SiteService siteService;
	@Autowired
	UserService userService;
	@Autowired
	ConfService confService;
	@Autowired
	ConfManagementService confManagementService;
	
	@AsController(path = "exp/{confHwId:([0-9]+)}")
	public void list(@CParam("confHwId") String confHwId,HttpServletRequest request,HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Object[]> objlist = new ArrayList<Object[]>();
		Object[] titles = new Object[4];
		titles[0] = "用户名";//
		titles[1] = "用户类型";//
		titles[2] = "加入时间";//
		titles[3] = "退出时间";
		objlist.add(titles);//添加头信息
		//添加数据信息
		List<ConfLog> logs = null;
		ConfBase conf = confService.getConfBaseByHwId(confHwId);
		if(conf!=null){
			SiteBase confSite = siteService.getSiteBaseById(conf.getSiteId());
			logs = confManagementService.queryConfUserStatusForPage(conf.getConfHwid(), 1, 3000, confSite, null).getDatas();
		}
		
		if(logs!=null && logs.size()>0){
			for (Iterator<ConfLog> tir = logs.iterator(); tir.hasNext();) {
				ConfLog log =  tir.next();
				Object[] logdata = new Object[4];
				logdata[0] = log.getUserName();//
				if(ConfConstant.CONF_USER_TERM_TYPE_MOBILE == log.getTermType().intValue()){
					logdata[1] = "电话";//
				}else if(ConfConstant.CONF_USER_TERM_TYPE_PC == log.getTermType().intValue()){
					logdata[1] = "电脑";//
				}else{
					logdata[1] = "未知";//
				}
				logdata[2] = sdf.format(log.getJoinTime());//
				logdata[3] = sdf.format(log.getExitTime());
				objlist.add(logdata);
			}
		}
		HSSFWorkbook wb = ExcelUtil.createExcelWorkbook("users", objlist);
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=conf_log.xls");
        try {
        	wb.write(response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			objlist = null;
			wb = null;
		}
	}
	
}
