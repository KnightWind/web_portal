<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/popupbox.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css"/>
<title>企业用户信息</title>
</head>
<body onload="loaded();">
<div class="popup_box">
	<div class="popup_box_main">
    <table>
  <tr>
    <td align="right" class="changes_left">登录名:</td>
    <td class="changes_right">${user.loginName}</td>
  </tr>
  <tr>
    <td align="right" class="changes_left">用户名:</td>
    <td class="changes_right">${user.trueName}</td>
  </tr>
  <tr>
    <td align="right" class="changes_left">英文名:</td>
    <td class="changes_right">${user.enName}</td>
  </tr>
<%--  <tr>--%>
<%--    <td align="right" class="changes_left">密码:</td>--%>
<%--    <td  class="changes_right">${user.loginPass}</td>--%>
<%--  </tr>--%>
	<tr>
		 <td align="right" class="changes_left">用户状态:</td>
		 <td  class="changes_right">
		 	<c:if test="${user.userStatus eq '0'}">${LANG['system.site.list.Status.lock']}</c:if>
			<c:if test="${user.userStatus eq '1'}">${LANG['site.admin.userlist.active']}</c:if>
		 </td>
	</tr>
	
	<tr>
	    <td align="right" class="changes_left">用户机构:</td>
	    <td class="changes_right">${orgName}</td>
	</tr>
	
  <tr>
    <td align="right" class="changes_left">用户角色:</td>
    <td class="changes_right">
    	<c:choose>
    		<c:when test="${user.userRole eq '1'}">主持人</c:when>
    		<c:otherwise>参会者</c:otherwise>
    	</c:choose>
    </td>
  </tr>

  <tr>
    <td align="right" class="changes_left">邮箱:</td>
    <td class="changes_right">${user.userEmail}</td>
  </tr>
  <tr>
    <td align="right" class="changes_left">电话:</td>
    <td class="changes_right">${user.mobile}</td>
  </tr>
  <c:if test="${user.userRole eq '1'}">
	  <tr>
	    <td align="right" class="changes_left">会议权限:</td>
	    <td class="changes_right"><p>
	    <c:if test="${config.phoneFlag eq 1 and siteConfig.phoneFlag eq 1}">电话&nbsp;</c:if> 
	    <c:if test="${config.autoFlag eq 1 and siteConfig.autoFlag eq 1}">自动外呼&nbsp;</c:if> 
	    <c:if test="${config.shareMediaFlag eq 1 and siteConfig.shareMediaFlag eq 1}">媒体共享&nbsp;</c:if> 
	    <c:if test="${config.recordFlag eq 1 and siteConfig.recordFlag eq 1}">录制</c:if> 
	    </p></td>
	  </tr>
  </c:if>
</table>
   <a class="user_messages_btn" href="#" onclick="closeDialog();" >确认</a> 
 </div>
</div>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#viewUserDiv");
	frame.trigger("loaded");
}

function closeDialog(result) {
	var dialog = parent.$("#viewUserDiv");
	if(result){
		dialog.trigger("closeDialog", [result]);
	} else {
		dialog.trigger("closeDialog");	
	}
}
</script>
