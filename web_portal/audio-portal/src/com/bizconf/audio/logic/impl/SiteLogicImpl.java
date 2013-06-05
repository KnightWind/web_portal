package com.bizconf.audio.logic.impl;

import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SiteConstant;
import com.bizconf.audio.dao.DAOProxy;
import com.bizconf.audio.entity.SiteBase;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.logic.SiteLogic;
import com.bizconf.audio.util.ObjectUtil;
import com.bizconf.audio.util.RegexUtil;
import com.bizconf.audio.util.StringUtil;
@Service
public class SiteLogicImpl extends BaseLogic implements SiteLogic {
	

	/**
	 * 创建站点验证前台表单数据
	 * wangyong
	 * 2013-1-22
	 */
	@Override
	public boolean createSiteValidate(SiteBase siteBase, UserBase siteAdmin) throws Exception {
		if(!siteValidate(siteBase)){
			return false;
		}
		if(!adminValidate(siteAdmin)){
			return false;
		}
		return true;
	}

	/**
	 * 修改站点验证前台表单数据
	 * wangyong
	 * 2013-1-22
	 */
	@Override
	public boolean updateSiteValidate(SiteBase siteBase) throws Exception {
		if(!siteValidate(siteBase)){
			return false;
		}
		return true;
	}
	
	/**
	 * 创建站点时，新增站点Supper管理员
	 * @param userBase
	 * @return
	 */
	@Override
	public boolean saveSiteSupperAdmin(UserBase userBase){
		boolean saveStatus = false;
		if(userBase != null){
			try {
				userBase.setUserType(ConstantUtil.USERTYPE_ADMIN_SUPPER);
				libernate.saveEntity(userBase);
				saveStatus = true;
			} catch (Exception e) {
				logger.error("创建站点新增supper管理员出错",e);
			}
		}
		return saveStatus;
	}
	
	/**
	 * 修改站点时，修改站点Supper管理员
	 * @param userBase
	 * @return
	 */
	@Override
	public boolean updateSiteSupperAdmin(UserBase userBase,String... updateFields ){
		boolean saveStatus=false;
		
		if(userBase!=null){
			try {
				libernate.updateEntity(userBase, updateFields);
				saveStatus=true;
			} catch (Exception e) {
				logger.error("修改站点时修改supper管理员出错",e);
			}
		}
		return saveStatus;
	}
	
	/**
	 * 验证站点对象数据格式
	 * wangyong
	 * 2013-1-22
	 */
	private boolean siteValidate(SiteBase siteBase){
		ObjectUtil.parseChar(siteBase, "siteName", "siteSign");
		String siteName = siteBase.getSiteName();
		String enName = siteBase.getEnName();
		String siteSign = siteBase.getSiteSign();
		int siteFlag = siteBase.getSiteFlag();
		if(siteBase.getLicense() != null && siteBase.getLicense().intValue() > 0){
			if(siteBase.getChargeMode() != SiteConstant.SITE_CHARGEMODE_NAMEHOST && siteBase.getLicense().intValue() < 1){
				logger.error("站点最大点数错误");
				return false;
			}
		}
		if(StringUtil.isNotBlank(siteName)){
			if(!RegexUtil.accordStringByPattern(siteName,"^[a-zA-Z0-9_\\-&\\u4e00-\\u9fa5]{4,32}$")){
				logger.error("站点名称错误");
				return false;
			}
		}else{
			return false;
		}
		if(StringUtil.isNotBlank(enName)){
			if(!RegexUtil.accordStringByPattern(enName,"^[a-zA-Z0-9_\\s\\-&]{4,64}$")){
				logger.error("站点英文名称错误");
				return false;
			}
		}else{
			return false;
		}
		if(StringUtil.isNotBlank(siteSign)){
			if(!RegexUtil.accordStringByPattern(siteSign,"^[a-zA-Z0-9\\-]{1,32}$")){
				logger.error("站点标识错误");
				return false;
			}
		}else{
			return false;
		}
//		if(!StringUtil.isNotBlank(siteBase.getSiteLogo())){
//			logger.error("站点logo错误");
//			return false;
//		}
		if(siteFlag != ConstantUtil.SITEFLAG_FORMAL && siteFlag != ConstantUtil.SITEFLAG_TRY){
			logger.error("站点类型错误");
			return false;
		}
		return true;
	}
	
	/**
	 * 验证站点管理员数据格式
	 * wangyong
	 * 2013-1-22
	 */
	private boolean adminValidate(UserBase siteAdmin){
		String trueName = siteAdmin.getTrueName();
		String loginName = siteAdmin.getLoginName();
		if(StringUtil.isNotBlank(trueName)){
			if(!RegexUtil.accordStringByPattern(trueName,"^[a-zA-Z0-9_\\-&\\u4e00-\\u9fa5]{1,32}$")){
				logger.error("站点管理员用户名错误");
				return false;
			}
		}else{
			return false;
		}
		if(StringUtil.isNotBlank(loginName)){
			if(!RegexUtil.accordStringByPattern(loginName,"^[a-zA-Z0-9]{4,16}$")){
				logger.error("站点管理员登录名错误");
				return false;
			}
		}else{
			return false;
		}
		return true;
	}

	/**
	 * 保存临时目录的站点logo到logo图片目录
	 * 无需删除临时目录图片，每日有定时任务删除临时图片
	 * wangyong
	 * 2013-2-4
	 */
	@Override
	public boolean saveSiteLogo(String siteLogoPath) {
		/*boolean flag = true;
		StringBuffer logoPath = new StringBuffer(ConstantUtil.TEMPSITELOGOPATH).append("/").append(siteLogoPath);
		StringBuffer desFilePath = new StringBuffer(ConstantUtil.SITELOGOPATH);
		desFilePath.append("/").append(SiteIdentifyUtil.getCurrentBrand()).append("/");
		FileUtil.createDirs(desFilePath.toString());
		desFilePath.append(siteLogoPath);
		flag = FileUtil.copyFile(logoPath.toString(), desFilePath.toString());
		return flag;*/
		return true;
	}

	/**
	 * 创建站点失败时，删除站点Supper管理员
	 * @param userBase
	 * @return
	 */
	@Override
	public boolean deleteSiteSupperAdmin(int siteId, UserBase userBase) {
		boolean delFlag = false;
		if(siteId > 0 && userBase != null){
			String delSql = "DELETE FROM t_user_base WHERE site_id = ? AND login_name = ? AND user_type = ?";
			Object[] values = new Object[3];
			values[0] = siteId;
			values[1] = userBase.getLoginName();
			values[2] = ConstantUtil.USERTYPE_ADMIN_SUPPER;
			try{
				delFlag = DAOProxy.getLibernate().executeSql(delSql, values);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return delFlag;
	}

}
