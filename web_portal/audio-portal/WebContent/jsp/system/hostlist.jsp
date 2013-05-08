<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/Popup.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/reset.css"/>
<SCRIPT type="text/javascript" src="/static/js/jquery-1.8.3.js"></SCRIPT>
<title>主持人列表</title>
</head>
<body onload="loaded()">
<div class="n_title"><a href="#" onclick="popUpHost('${siteId}','');">添加主持人</a></div>
<table border="0" cellpadding="0" cellspacing="0" class="host_top">
	<tr height="30" class="host01">
    	<td width="100" align="center">登录名</td>
        <td width="150" align="center">邮箱</td>
        <td width="80" align="center">点数</td>
        <td width="170" align="center">操作</td>  
    </tr>
</table>
    <div class="h_main">
<table border="0" cellpadding="0" cellspacing="0" class="host_center">
	 <c:if test="${fn:length(pageModel.datas)<=0 }">
         <tr  height="36" class="host02">
           <td colspan="4" width="540" align="center">
        		${LANG['website.common.msg.list.nodata']}
           </td>
         </tr>
      </c:if>
    <c:forEach var="host" items = "${pageModel.datas}"  varStatus="status">
	    <tr height="36" class="host02">
	    	<td width="100" align="center" >${host.loginName}</td>
	        <td width="150" align="center">${host.userEmail}</td>
	        <td width="80" align="center" cla>${licnums[host.id]}</td>
	        <td width="230" align="center">
		        <a href="#" onclick="toLicenseManage(${host.id},${siteId})">点数管理</a>
		        <a href="#" onclick="popUpHost(${siteId},${host.id})">修改</a>
		        <a href="#" onclick="del(${host.id})">删除</a>
	        </td>  
	    </tr>
    </c:forEach>
</table>  
</div>
<a href="#" class="fh_btn Public_button" onclick="closeDialog();">取消</a>
</div>
</body>
</html>
<script type="text/javascript">
function refreshPage(){
	window.location.reload(true);
}

function popUpCount() {
	parent.countManager();
}

function popUpHost(siteId,userId) {
		parent.createOrUpdateHost(siteId,userId);
}

function popUpdateHost(elem) {
	var data = $(elem).closest("tr").data("data");
	parent.createOrUpdateHost(data);	
}

function del(id){
	parent.confirmDialog("确认删除主持人？",function(){
		window.location = "/system/lic/delHost?id="+id;
	});
}

function loaded() {
	var frame = parent.$("#hostManagerDiv");
	frame.trigger("loaded");
}
function closeDialog() {
	var frame = parent.$("#hostManagerDiv");
	frame.trigger("closeDialog");
}

//设置点数
function toLicenseManage(userId,siteId){
	parent.licenseManage(userId,siteId);
}
</script>