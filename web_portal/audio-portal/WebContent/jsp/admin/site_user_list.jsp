<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${LANG['bizconf.jsp.admin.arrange_org_user.res1']}</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/rightbox.css" />
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.uniform/jquery.uniform.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.plugin.js"></script>
<script type="text/javascript">
	function delUser(id){
		if(!id){
			if($("input[name=id]:checked").length<=0){
				parent.warningDialog("${LANG['site.admin.userlist.info1']}");
				return false;
			}
			parent.confirmDialog("${LANG['site.admin.userlist.info2']}", function() {
					$("#query").attr("action","/admin/entUser/delSiteUsers");
					$("#query").submit();
			});
		}else{
			parent.confirmDialog("${LANG['site.admin.userlist.info2']}", function() {
				$("input[name=id]").each(function(){
					if($(this).val()==id){
						$(this).attr("checked",true);
					}else{
						$(this).attr("checked",false);
					}
				});
				$("#query").attr("action","/admin/entUser/delSiteUsers");
				$("#query").submit();
			});
		}
	}
	function lockUser(){
		if($("input[name=id]:checked").length<=0){
			parent.warningDialog("${LANG['site.admin.userlist.info3']}");
			return false;
		}
		parent.confirmDialog("${LANG['site.admin.userlist.info4']}", function(){
				$("#query").attr("action","/admin/entUser/lockSiteUsers");
				$("#query").submit();
				parent.successDialog("${LANG['bizconf.jsp.admin.site_user_list.res1']}");
		});
	}
	
	function unlockUser(){
		if($("input[name=id]:checked").length<=0){
			parent.warningDialog("${LANG['site.admin.userlist.info5']}");
			return false;
		}
		parent.confirmDialog("${LANG['site.admin.userlist.info6']}", function() {
				$("#query").attr("action","/admin/entUser/unlockSiteUsers");
				$("#query").submit();
				parent.successDialog("${LANG['bizconf.jsp.admin.site_user_list.res2']}");
		});
	}
	function doImport() {
		parent.importUser();
	}
	
	function exportExcel(){
		//var keyword = $("input[name=keyword]").val();
		$("#query").attr("action","/admin/entUser/exportUser");
		$("#query").submit();
		//window.location = "/admin/entUser/exportUser?keyword="+keyword;
	}
	
	function toEditUser(id){
		parent.createOrUpdateSiteUser(id);
	}
	
	function toViewUser(id){
		parent.viewUser(id);
	}
	
	function sortQuery(sortField,sortord){
		if(!sortord){
			if($("#sortRule").val()=='0'){
				sortord = "1";
			}else{
				sortord = "0";
			}
		}
		$("#sortField").val(sortField);
		$("#sortRule").val(sortord);
		$("#query").attr("action","/admin/entUser/listAll");
		$("#query").submit();
	}
	
	$(document).ready(function(){
		$(".search_user").uniform();
		$(".search_user").watermark('${LANG['bizconf.jsp.admin.site_org_user.res1']}');
		$("#btn_search").click(function(){
			$("#pageNo").val("");
			$("#query").attr("action","/admin/entUser/listAll");
			$("#query").submit();
		});
		
		//${LANG['bizconf.jsp.admin.arrange_org_user.res5']}and${LANG['bizconf.jsp.admin.arrange_org_user.res6']}
		$("#checkAll").click(function(){
			if($(this).attr("checked")){
				$("input[name=id]").attr("checked",true);				
			}else{
				$("input[name=id]").attr("checked",false);
			}
		});
		$("input[name=id]").click(function(){
			if($("input[name=id]").length == $("input[name=id]:checked").length){
				$("#checkAll").attr("checked",true);
			}else{
				$("#checkAll").attr("checked",false);
			}
		});
		//${LANG['bizconf.jsp.admin.site_org_user.res2']}
		$("input[name=keyword]").keyup(function(event){
			if(event.keyCode=='13'){
				$("#pageNo").val("");
				$("#query").attr("action","/admin/entUser/listAll");
				$("#query").submit();
			}
		});
		
	});
	
	//${LANG['bizconf.jsp.admin.site_org_user.res3']}
	function refreashPage(){
		$("#pageNo").val("");
		$("#sortField").val("");
		$("#sortRule").val("");
		$("input[name=keyword]").val("");
		$("#query").attr("action","/admin/entUser/listAll");
		$("#query").submit();
	}
	
	function showImportInfo(info,statu){
		refreashPage();
		if(statu==1){
			parent.successDialog(info);
		}else{
			parent.errorDialog(info);
		}
	}
	
	$(document).keydown(function(e){ 
		var doPrevent; 
		if (e.keyCode == 8) { 
			var d = e.srcElement || e.target; 
			if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') { 
				doPrevent = d.readOnly || d.disabled; 
			}else{
				doPrevent = true; 
			} 
		}else{
			doPrevent = false; 
		}
		if (doPrevent){
			e.preventDefault(); 
		} 
	}); 
</script>
</head>
<body>
<form id="query" name="query" action="/admin/entUser/listAll" method="post">
	<input type="hidden" value="${sortField}" id="sortField" name="sortField" />
	<input type="hidden" value="${sortRule}" id="sortRule" name="sortRule" />
	<div class="main_content">
		<div class="m_top">
		    <input class="search_user" name="keyword" type="text" maxlenght="32" value="${keyword}" />
		    <input name="" class="searchs_button" type="button" id="btn_search" />
	 	 </div>
		 <div class="batch">
		  	<ul>
		    	<li><a href="#" onclick="toEditUser();" class="zengjia">${LANG['site.admin.userlist.add']} </a></li>
		        <li><a href="#" onclick="delUser();" class="shanchu">${LANG['system.delete']}</a></li>
		        <li><a href="#" onclick="unlockUser();" class="jihuo">${LANG['site.admin.userlist.active']}</a></li>
		        <li><a href="#" onclick="lockUser();" class="suoding">${LANG['system.site.list.Status.lock']}</a></li>
		        <li><a href="#" onclick="doImport();" class="daoru">${LANG['site.admin.userlist.batchimport']}</a></li>
		        <li><a href="#" onclick="exportExcel();" class="daochu">${LANG['site.admin.userlist.batchexport']}</a></li>
		    </ul>
		</div>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0" id="table_box">
			<tr class="table003" height="38">
			<td width="5%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><input type="checkbox" id="checkAll"/></div></td>
            <td width="10%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.login.name']}</span></div></td>
            <td width="10%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.sysUser.list.userName']}</span></div></td>
            <td width="8%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['site.admin.edituser.userrole']}</span></div></td>
            <td width="20%"  height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.sysUser.list.email']}</span></div></td>
            <td width="16%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.sysUser.list.telephone']}</span></div></td>
            <td width="7%" height="38" bgcolor="d3eaef" class="STYLE10" onclick="sortQuery('1');" style="cursor: pointer;">
            <div align="center">
	            <span>${LANG['site.admin.userlist.userstatu']}</span>
	             <c:if test="${1!=sortField}">
	            	<a class="paixu01" href="javascript:;" onclick="sortQuery('1','1')"><img src="/static/images/paixu_button.png" width="6" height="13" /></a>
	       		 </c:if>
	       		 <c:if test="${1==sortField && 0==sortRule}">
	        		<a class="paixu01" href="javascript:;" onclick="sortQuery('1','1')"><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a>
	        	</c:if>
		        <c:if test="${1==sortField  && 1==sortRule}">
		        	<a class="paixu01" href="javascript:;" onclick="sortQuery('1','0')"><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a>
		        </c:if>
            </div>
            </td>
            <td width="10%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.sysUser.list.org']}</span></div></td>
			<td width="18%"  height="38" bgcolor="d3eaef" class="STYLE10" style=" border-right:none;"><div align="center" ><span>${LANG['system.sysUser.list.operate']}</span></div></td>
			</tr>
			<c:if test="${fn:length(pageModel.datas)<=0}">
				<tr class="table001" height="32"  >
			            <td height="32" colspan="11" align="center"> ${LANG['website.common.msg.list.nodata']} </td>
			     </tr>
			</c:if>
				<c:forEach var="user" items="${pageModel.datas}" varStatus="status">
					<tr class="table001" height="32" >
			            <td height="32"><div align="center"><input name="id" type="checkbox" value="${user.id }" /></div></td>
			            <td height="32"><div align="center"><a href="#" onclick="toViewUser('${user.id }');"><span>${user.loginName }</span></a></div></td>
			            <td height="32"><div align="center"><span>${user.trueName }</span></div></td>
			            <td height="32">
			            <div align="center">
				            <c:if test="${user.userRole eq '1'}">
					            ${LANG['site.admin.edituser.role1']}
				            </c:if>
				            <c:if test="${user.userRole eq '2'}">
					            ${LANG['site.admin.edituser.role2']}
				            </c:if>
			            </div></td>
			            <td height="32"><div align="center">${user.userEmail }</div></td>
			            <td height="32"><div align="center">${user.mobile }</div></td>
			            <td height="32">
				            <div align="center">
				            	<c:if test="${user.userStatus eq '0'}">${LANG['system.site.list.Status.lock']}</c:if>
				            	<c:if test="${user.userStatus eq '1'}">${LANG['site.admin.userlist.active']}</c:if>
				            </div>
			            </td>
			            <td height="32"><div align="center">${orgNamesMap[user.id]}</div></td>
			            <td height="32">
			            <div align="center" class="STYLE21">
			            	<a href="#" onclick="toEditUser('${user.id }');">${LANG['system.change']}</a>&nbsp;
			            	<a href="#" onclick="delUser('${user.id }');">${LANG['system.delete']}</a>&nbsp;
			            	<a href="#" onclick="parent.showAttendConfs('${user.id }');">${LANG['bizconf.jsp.admin.index.res16']}</a>
			            </div></td>
			         </tr>
				</c:forEach>
			<tr class="table001" height="32" >
				<td class="table_bottom" height="38" colspan="11"> 
					 <jsp:include page="/jsp/common/page_info.jsp" />
				</td>
			</tr>
		</table>
	</div>
	</form>
</body>
</html>

