package com.bizconf.audio.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bizconf.audio.constant.ConstantUtil;
import com.bizconf.audio.constant.SortConstant;
import com.bizconf.audio.entity.PageBean;
import com.bizconf.audio.entity.SiteOrg;
import com.bizconf.audio.entity.UserBase;
import com.bizconf.audio.service.OrgService;
import com.bizconf.audio.util.BeanUtil;
import com.bizconf.audio.util.DateUtil;
import com.bizconf.audio.util.StringUtil;

@Service
public class OrgServiceImpl extends BaseService implements OrgService {

	@Override
	public PageBean<SiteOrg> getSiteOrgList(int siteId, String orgName,
			String pageNo, String sortField, String sortord) {
		if(pageNo==null || pageNo.equals("")){
			pageNo = "1";
		}
		List<Object> values = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(" select * from t_site_org where del_flag = ? ");
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		if (siteId > 0) {
			sqlBuilder.append(" and site_id =? ");
			values.add(siteId);
		}
		if(StringUtil.isNotBlank(orgName)){
			sqlBuilder.append("and org_name like ?");
			values.add("%"+ orgName +"%");
		}
		if(StringUtil.isNotBlank(sortField)){
			sqlBuilder.append(" order by " + initSort(sortField));
			if(SortConstant.SORT_ASC.equals(sortord)){
				sqlBuilder.append(" asc ");
			}else{
				sqlBuilder.append(" desc ");
			}
		}else{
			sqlBuilder.append(" order by id desc ");
		}
		return getPageBeans(SiteOrg.class, sqlBuilder.toString(), Integer.parseInt(pageNo), values.toArray(new Object[values.size()]));
	}
	
	@Override
	public PageBean<SiteOrg> getSiteOrgList(int siteId) {
		List<Object> values = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(" select * from t_site_org where del_flag = ? ");
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		if (siteId > 0) {
			sqlBuilder.append(" and site_id =? ");
			values.add(siteId);
		}
		return getPageBeans(SiteOrg.class, sqlBuilder.toString(), 1, 1000, values.toArray(new Object[values.size()]));
	}
	
	@Override
	public PageBean<UserBase> getOrgUserList(String orgName, String sortField,
			String sortord, Integer siteId,Integer creator, String pageNo, List<SiteOrg> orgList) {
		if(pageNo==null || pageNo.equals("")){
			pageNo = "1";
		}
		List<Object> values = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder(" select * from t_user_base where del_flag = ? and user_type = ? ");
		values.add(ConstantUtil.DELFLAG_UNDELETE);
		values.add(ConstantUtil.USERTYPE_USERS);
		if (siteId!=null && siteId>0) {
			sqlBuilder.append(" and site_id =? ");
			values.add(siteId);
		}
		
		if(creator!=null && creator.intValue()>0){
			sqlBuilder.append(" and create_user =? ");
			values.add(creator);
		}
		
		if(StringUtil.isNotBlank(orgName)){
			sqlBuilder.append("and (login_name like ? or true_name like ? or user_email like ?)");
			values.add("%"+ orgName +"%");
			values.add("%"+ orgName +"%");
			values.add("%"+ orgName +"%");
		}
		if(orgList != null && orgList.size() > 0){
			sqlBuilder.append(" and org_id not in ( -1 ");
			for(SiteOrg org:orgList){
				sqlBuilder.append(",");
				sqlBuilder.append(" ? ");
				values.add(org.getId());
			}
			sqlBuilder.append(" ) ");
		}
		
		if(StringUtil.isNotBlank(sortField)){
			sqlBuilder.append(" order by "+initSort(sortField));
			if(SortConstant.SORT_ASC.equals(sortord)){
				sqlBuilder.append(" asc ");
			}else{
				sqlBuilder.append(" desc ");
			}
		}else{
			sqlBuilder.append(" order by id desc ");
		}
		PageBean<UserBase> pageModel = getPageBeans(UserBase.class, sqlBuilder.toString(), Integer.parseInt(pageNo), values.toArray(new Object[values.size()]));
		return pageModel;
	}

	@Override
	public SiteOrg addSiteOrg(SiteOrg siteOrg, UserBase currentSiteAdmin) {
		if(siteOrg == null){
			return null;
		}
		siteOrg.init(currentSiteAdmin);
		try {
			siteOrg = libernate.saveEntity(siteOrg);
			if(siteOrg != null && siteOrg.getId() > 0){
				return siteOrg;
			}
		} catch (Exception e) {
			logger.error("新增站点下的组织机构错误！" + e);
		}
		return null;
	}

	@Override
	public SiteOrg updateSiteOrg(SiteOrg siteOrg) {
		try {
			siteOrg = libernate.updateEntity(siteOrg, "orgName", "orgDesc");
			if(siteOrg != null && siteOrg.getId() > 0){
				return siteOrg;
			}
		} catch (Exception e) {
			logger.error("修改站点下的组织机构错误！" + e);
		}
		return null;
	}

	@Override
	public boolean deleteSiteOrg(int orgId, UserBase currentSiteAdmin) {
		SiteOrg siteOrg = getSiteOrgById(orgId);
		if(siteOrg == null){
			return false;
		}
		siteOrg.setDelFlag(ConstantUtil.DELFLAG_DELETED);
		siteOrg.setDelTime(DateUtil.getGmtDate(null));
		siteOrg.setDelUser(currentSiteAdmin.getId());
		try {
			siteOrg = libernate.updateEntity(siteOrg, "delFlag", "delTime", "delUser");
			if(siteOrg != null && siteOrg.getId() > 0){
				return true;
			}
		} catch (Exception e) {
			logger.error("删除站点下的组织机构错误！" + e);
		}
		return false;
	}

	@Override
	public SiteOrg getSiteOrgById(int orgId) {
		SiteOrg siteOrg = null;
		StringBuffer strSql = new StringBuffer(" SELECT * FROM t_site_org WHERE 1=1 AND id = ? ");
		Object[] values = new Object[1];
		values[0] = orgId;
		try {
			siteOrg = libernate.getEntityCustomized(SiteOrg.class, strSql.toString(), values);
		} catch (SQLException e) {
			logger.error("根据组织机构ID号获取组织机构出错！",e);
		}
		return siteOrg;
	}
	
	@Override
	public List<UserBase> getOrgUserList(int orgId, int siteId) {
		List<UserBase> orgUserList = null;
		try {
			orgUserList = libernate.getEntityListWithCondition(" org_id = ? and del_flag = ? and site_id = ? " ,
					 new Object[]{orgId, ConstantUtil.DELFLAG_UNDELETE, siteId}, 
					 UserBase.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return orgUserList;
	}
	
	/**
	 * 为用户批量分配机构
	 * wangyong
	 * 2013-5-2
	 */
	@Override
	public boolean updateOrgUserBatch(String orgId, String[] ids, UserBase currentUser){
		boolean updateFlag = true;
		List<Object> valueList = new ArrayList<Object>();
		StringBuilder sqlBuilder = new StringBuilder("UPDATE t_user_base SET org_id = ? WHERE site_id = ? AND id in (0");
		valueList.add(orgId);
		valueList.add(currentUser.getSiteId());
		for(String id:ids){
			sqlBuilder.append(",");
			sqlBuilder.append(" ? ");
			valueList.add(id);
		}
		sqlBuilder.append(")");
		try {
			updateFlag = libernate.executeSql(sqlBuilder.toString(), valueList.toArray());
		} catch (Exception e) {
			logger.error("为用户批量分配机构出错！" + e);
			updateFlag = false;
		}
		return updateFlag;
	}
	
	/**
	 * 获取页面传递来的排序参数
	 * wangyong
	 * 2013-5-6
	 */
	private String initSort(String field){
		String sortField = SortConstant.SITE_ORG_FIELDS[0][1];
		for (String[] eachField : SortConstant.SITE_ORG_FIELDS) {
			if (eachField != null && eachField[0].equals(field)) {
				sortField = BeanUtil.att2Field(eachField[1]);
				break;
			}
		}
		return sortField;
	}

}
