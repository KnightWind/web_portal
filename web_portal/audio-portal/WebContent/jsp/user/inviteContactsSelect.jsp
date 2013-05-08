<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-1.8.3.min.js?ver=${version}"></script>
<script type="text/javascript">
	function selectGroup(obj){
			$("#contactFrame").attr("src","/user/group/invitelist");	
			$(".cluster").find("a").css("color","#333333");
			$(obj).css("color","#ED6F00");
	}
	
	function selectContact(obj){
			$("#contactFrame").attr("src","/user/contact/invitelist");
			$(".cluster").find("a").css("color","#333333");
			$(obj).css("color","#ED6F00");
	}
	
	function selectEnContacts(obj){
			$("#contactFrame").attr("src","/user/contact/showEnterpriseContacts?showAll=1");	
			$(".cluster").find("a").css("color","#333333");
			$(obj).css("color","#ED6F00");
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
          <h3 class="tit_a_invite">从联系人导入</h3>
          <p class="tit_b_invite">您可以在自己的联系人名单或企业通讯录中添加参会成员。</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick_invite" src="/static/images/min.jpg" width="730" height="1" /></div>
        <div class="First_Steps_top_invite" style=" background:#FFF;">
          <div class="cluster"><a href="#" style="margin-left:10px; color: #ED6F00;" onclick="selectContact(this);"><img src="/static/images/lianxiren.png" width="18" height="20" align="absmiddle" style=" margin-right:5px;" />联系人&nbsp;&nbsp;</a></div>
          <div class="cluster"><a href="#" style="margin-left:10px; " onclick="selectGroup(this);"><img src="/static/images/qunzu.png" width="20" height="18" align="absmiddle" style=" margin-right:5px;" />群组&nbsp;&nbsp;</a></div>
          <div class="cluster"><a href="#" style="margin-left:10px;" onclick="selectEnContacts(this);"><img src="/static/images/tongxl.png" width="18" height="20" align="absmiddle" style=" margin-right:5px;" />企业通讯录&nbsp;&nbsp;</a></div>
        </div>
        <div class="jianju"></div>
        <!--群组开始-->
        <div class="First_Steps_main_invite">
        	<iframe frameborder="0" width="100%" height="500px;" id="contactFrame" name="contactFrame" scrolling="no" src="/user/contact/invitelist"></iframe>
        </div>
        <!--企业通讯录结束-->
        <div class="First_Steps_bottom_s">
          <div class="but44"><span class="button_common"><a href="javascript:;" onclick="closeDialog()"><img src="/static/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:10px;"/>取消</a></span></div>
          <div class="but09"><span class="button_common"><a href="javascript:;" onclick="selectContacts()"><img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-right:8px; margin-left:10px;" />选定</a></span></div>
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
function selectContacts() {
	//var data = {"name": "alan", "email": "asd@qq.com", "phone": "12312311"};
	var datas = window.frames["contactFrame"].getContactsData();
	//var dialog = parent.$("#inventContact").find("iframe")[0].contentWindow.appendRow(data);
	if(datas && datas.ids){
		$.ajax({
	      	type: "POST",
	      	url:"/user/group/getContactsByGroups",
	      	data:{ids:datas.ids},
	      	dataType:"json",
	      	async:false,
	      	success:function(data){
	      		var contacts = new Array();
				if(data){
					for(var i=0;i<data.length;i++){
						var item = new Object();
						item.name = data[i].contactName;
						item.email = data[i].contactEmail;
						item.phone = data[i].contactPhone || data[i].contactMobile;
						contacts.push(item);
					}
					parent.$("#inventContact").find("iframe")[0].contentWindow.addByExtral(contacts);
				}else{
					alert('Server inner error!');
				}
	      	},
	        error:function(XMLHttpRequest, textStatus, errorThrown) {
	        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
	        }
		});  
	}else if(datas){
		parent.$("#inventContact").find("iframe")[0].contentWindow.addByExtral(datas);
		//alert(datas);
	}
	closeDialog();
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
