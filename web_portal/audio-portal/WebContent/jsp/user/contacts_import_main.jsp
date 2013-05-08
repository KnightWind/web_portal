<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>
<title>选择联系人</title>
<style></style>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	 
$(document).ready(function(){
	$(".cluster_link").click(function() {
		$(".cluster_link").removeClass("cluster_active");
		$(this).addClass("cluster_active");
	});
});
	function importByTemplate(){
		$("#contactFrame").attr("src","/user/contact/importContactsByExcel");
	}
	
	function importByEnContacts(){
		$("#contactFrame").attr("src","/user/contact/showEnterpriseContacts");	
	}
	
	function showInfo(info,status){
		if(status==1){
			parent.successDialog(info);
		}else{
			parent.errorDialog(info);
		}
		//刷新页面
		parent.showURL("/user/contact/goContacts");
		closeDialog();
	}
</script>
</head>

<body onload="loaded()">
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="joinPublicDiv">
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      <td class="overlay-content">
      <!--弹出层主题内容区域开始========================================================================-->  
		<div class="First_Steps_invite_m" style=" background:#FFF; border-radius:3px;">
	        <div class="First_Steps_title_invite"> <a href="javascript:;" onclick="closeDialog()"></a>
	          <h3 class="tit_a_invite">批量添加联系人</h3>
	          <p class="tit_b_invite">您可以通过查看企业通信录来批量添加联系人，还可以上传联系人名单批量添加</p>
	        </div>
	        <div style=" background:#fff"><img class="toa_quick_invite" src="/static/images/min.jpg" width="730" height="1" /></div>
	        <div class="First_Steps_top_invite" style=" background:#FFF;">
	          <div class="cluster"><a class="cluster_link cluster_active" href="javascript:;" style="margin-left:10px;" onclick="importByEnContacts();"><img src="/static/images/tongxl.png" width="18" height="20" align="absmiddle" style=" margin-right:5px;" />添加企业用户&nbsp;&nbsp;</a></div>
	          <div class="cluster"><a class="cluster_link" href="javascript:;" style="margin-left:10px;" onclick="importByTemplate();"><img src="/static/images/lianxiren.png" width="18" height="20" align="absmiddle" style=" margin-right:5px;" />上传联系人名单&nbsp;&nbsp;</a></div>
	        </div>
	        <div class="jianju"></div>
	        <!--群组开始-->
	        <div class="First_Steps_main_invite">
	        	<iframe frameborder="0" width="100%" height="500px;" id="contactFrame" name="contactFrame" scrolling="no" src="/user/contact/showEnterpriseContacts"></iframe>
	        </div>
	        <!--企业通讯录结束-->
	        <div align="center" class="First_Steps_bottom_s">
	          <div class="but44"><span class="button_common"><a href="javascript:;" onclick="closeDialog()"><img src="/static/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:10px;"/>取消</a></span></div>
	          <div class="but09"><span class="button_common"><a href="javascript:;" onclick="doImportContacts()"><img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-right:8px; margin-left:10px;" />添加</a></span></div>
	        </div>
      	</div>		
      <!--弹出层主题内容区域开始========================================================================-->      
      </td>
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>

</body>
</html>
<script type="text/javascript">
function doImportContacts() {
	//var data = {"name": "alan", "email": "asd@qq.com", "phone": "12312311"};
	window.frames["contactFrame"].importContacts();
	//closeDialog();
} 
function loaded() {
	var dialog = parent.$("#importContact");
	dialog.trigger("loaded");
}
function closeDialog() {
	var dialog = parent.$("#importContact");
	dialog.trigger("closeDialog");
}
</script>
