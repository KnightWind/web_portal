<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.bizconf.audio.entity.SiteBase"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>修改站点</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="${ctx}/script/jquery-1.8.2.js"></script> 
</head>
<body>
	<form name="loginform" action="/system/site/update" method="post">
		<table align = "center" style="width:500px">
			<tr>
				<td colspan="2" align="center">修改站点</td>
			</tr>
		</table>
		<table align="center" cellpadding="4" cellspacing="0" border="1" style="width:500px">
			<tr>
				<td>企业名称</td>
				<td><input name="siteName" type="text" value="${siteBase.siteName}"></td>
				<input name="id" type="hidden" value = "${siteBase.id}">
			</tr>
			
			<tr>
				<td>企业标识</td>
				<td><input name="siteSign" type="text" value="${siteBase.siteSign}"></td>
			</tr>
<%-- 			<tr>
				<td>企业LOGO</td>
				<td><input name="siteLogo" type="text" value="${siteBase.siteLogo}"></td>
			</tr> --%>
			<tr>
				<td>站点类型</td>
				<td>
					<input name="siteFlag" type="radio" value="1" checked="checked">正式
					<input name="siteFlag" type="radio" value="2">试用
				</td>
			</tr>
			<tr>
				<td>最大参会方数</td>
				<td><input name="license" type="text" value="${siteBase.license}"></td>
			</tr>
			<tr>
				<td>会议类型</td>
				<td>
					<input name="mtgType" type="radio" value="1" checked="checked">视频
					<input name="mtgType" type="radio" value="2">视频+网络电话
					<input name="mtgType" type="radio" value="3">视频+传统电话
				
				</td>
			</tr>
			<tr>
				<td>到期时间</td>
				<td><input name="expireDate" type="text" value="${siteBase.expireDate}"></td>
			</tr>
			
			
			<tr>
				<td>用户名</td>
				<td><input name="clientName" type="text"></td>
			</tr>
			<tr>
				<td>登陆名</td>
				<td><input name="loginName" type="text"></td>
			</tr>
			<tr>
				<td>密码</td>
				<td><input name="loginPass" type="password"></td>
			</tr>
			<tr>
				<td>邮箱</td>
				<td><input name="userEmail" type="text"></td>
			</tr>
			<tr>
				<td>手机</td>
				<td><input name="mobile" type="text"></td>
			</tr>
			<tr>
				<td>备注</td>
				<td><input name="remark" type="text"></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input name="submit" type="submit"  value="确定"></td>
			</tr>
		</table>
	</form>
	
	<script type="text/javascript">
		function saveSiteInfo(){
			
			document.getElementsByName("loginform")[0].submit();
		}
	</script>
</body>
</html>