<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>邮件模板列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="${ctx}/script/jquery-1.8.2.js"></script> 
	</head>
	<body>
	<table align="center" cellpadding="4" cellspacing="0" border="1" style="width:500px">
	<form name="configForm" id="configForm" action="${ctx}/system/email/savehost" method="post">
	<input type="hidden" name="id" id="id" value="${emailConfig.id}"/>
	<input type="hidden" name="siteId" id="siteId" value="${emailConfig.siteId}"/>
		<tr>
			<td colspan="5" align="center"  >邮件模板列表 <input type="button" onclick="javascript:window.open('${ctx}/system/email/template/0-0')" value="新建模板"/></td>
		</tr>
		 
		<tr>
			<td align="center" style="width:100px;">&nbsp;模板名称</td>
			<td align="center" style="width:100px;">&nbsp;模板类别</td>
			<td align="center" style="width:100px;">&nbsp;站点标识</td>
			<td align="center" style="width:100px;">&nbsp;版本</td>  
			<td align="center" style="width:100px;">&nbsp;操作</td>  
		 </tr> 
		<tr>
			<td >&nbsp;</td>
			<td >&nbsp;</td>
			<td >&nbsp;</td>
			<td >&nbsp;</td>
			<td >[修改][删除]</td>
			
		 </tr> 
		 <tr>
			<td colspan="5" align="center"  >&nbsp;</td>
		</tr>
		</form>
	</table>
</body>
</html>
<script type="text/javascript">

 
	function saveHost(){
		$("#configForm").submit();
	}
</script>
