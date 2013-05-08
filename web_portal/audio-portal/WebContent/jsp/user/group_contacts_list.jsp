<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>
<title>联系人列表</title>
<style>
</style>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></script>	
<script type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></script>	
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.plugin.js?ver=${version}"></script>	
<script type="text/javascript">
	function delUser(id){
// 		if(confirm("确认从该群组删除联系人？")){
// 			//query.action="/user/group/delContactsFormGroup";
// 			//$("input[name=id]").val(id);
// 			//query.submit();
// 			window.location = "/user/group/delContactsFormGroup?id="+id+"&group_id=${group_id}";
// 		}
		parent.confirmDialog("确认从该群组删除联系人？", function() {
			var groupId = "${group_id}";
			app.delContactFromGroup(id, groupId, function() {
				query.submit();
				parent.refreshGroupList();
			});
		});
	}

	function toAddContacts(id){
// 		window.location = "/user/group/importContacts?group_id=${group_id}";
		parent.addContactFromGroup(id);
	}
	
	function refreshData(){
		query.submit();
		parent.refreshGroupList();
	}
	
	$(document).ready(function(){
	$(".group_search").watermark('用户名、英文名、邮箱、联系电话、手机号码');
	});
	
	function submitForm(){
		$('input').trigger('submitForm');
		query.submit();
	}
</script>
</head>
<body onload="loaded();">
  <div class="add06_main">
    <div class="add06_top">
    <div class="add06_top_left"></div>
    <div class="add06_top_center"></div>
    <div class="add06_top_right"></div>
  </div>
  <div class="add06_center">
    <div class="add06_center_left"></div>
    <div class="add06_center_center">
<div class="First_Steps_invite" style=" background:#FFF; border-radius:3px;">
  <div class="First_Steps_title_invite"> <a href="javascript:closeDialog();"></a>
    <h3 class="tit_a_invite">${group.groupName } 群组成员</h3>
    <p class="tit_b_invite"></p>
  </div>
  <div style=" background:#fff"><img class="toa_quick_invite" src="/static/images/min.jpg" width="730" height="1" /></div>
  <div class="jianju"></div>
 
  <!--联系人开始-->
  <div class="First_Steps_main_invite" style=" background:#FFF;">
  <form id="query" name="query" action="/user/group/showContacts" method="post">
  	<input type="hidden" name="group_id" value="${group_id}"/>
    <table width="730" align="center" cellpadding="0" cellspacing="0" border="0" id="t_box" >
      <tr>
        <td height="40" colspan="6" bgcolor="#333" class="tr_top"><input name="keyword" type="text" class="meeting_ss group_search" value="${keyword}"/>
          <input class="meeting_but" type="button" onclick="submitForm();" />
          <div class="add_group_btn">
          	<span class="button_common">
          		<a href="javascript:toAddContacts('${group_id}');"><img width="16" height="16" align="absmiddle" style=" margin-left:5px; margin-right:5px;" src="/static/images/add_16.png" />添加组成员</a>
          	</span>
          </div>
          </td>
      </tr>
      <tr align="center" height="35" class="tr_center" bgcolor="#000066">
				<td width="15%" class="tr_center">用户名</td>
		        <td width="15%" class="tr_center">英文名</td>
		        <td width="20%" class="tr_center">邮箱</td>
		        <td width="15%" class="tr_center">联系电话</td>
		        <td width="15%" class="tr_center">手机号码</td>
		        <td width="20%" class="tr_center" style=" border-right:#D2D8DB 1px solid">操作</td>
      </tr>
      <c:if test="${fn:length(pageModel.datas)<=0}">
				<tr class="table001" height="32"  >
			            <td class="STYLE19"  height="32" colspan="11" align="center">
			            	<c:choose>
			            		<c:when test="${keyword!=null}">
			            			抱歉,没有找到符合条件的联系人。
			            		</c:when>
			            		<c:otherwise>
			            			您目前还没有添加组成员。
			            		</c:otherwise>
			            	</c:choose>
			            </td>
			     </tr>
	</c:if>
	<c:forEach var="contact" items="${pageModel.datas}" varStatus="status">
	      <tr align="center" bgcolor="#FFFFFF" height="30">
	        <td class="tr_main" style=" border-left:#D2D8DB 1px solid">${contact.contactName}</td>
	        <td class="tr_main">${contact.contactNameEn}</td>
	        <td class="tr_main">${contact.contactEmail}</td>
	        <td class="tr_main">${contact.contactPhone}</td>
	        <td class="tr_main">${contact.contactMobile}</td>
	        <td class="tr_main" style=" border-right:1px solid #D2D8DB" align="center"><a href="javascript:delUser('${contact.id }')">删除</a></td>
	      </tr>
      </c:forEach>
      <tr>
        <td height="35" colspan="6" class="tr_bottom table_bottom"> 
        	 <jsp:include page="/jsp/common/page_info.jsp" />
        </td>
      </tr>
    </table>
    </form>
  </div>
  <!--联系人结束--> 
  <div class="First_Steps_bottom_b">
    <div class="but99"><span class="button_common"><a href="javascript:closeDialog();"><img src="/static/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:8px;"/>返回</a></span></div>
  </div>
</div>
</div>
<div class="add06_center_right"></div>
</div>
<div class="add06_bottom">
<div class="add06_bottom_left"></div>
<div class="add06_bottom_center"></div>
<div class="add06_bottom_right"></div>
</div>
</div>
</body>
</html>
<script type="text/javascript">

function loaded() {
	var frame = parent.$("#viewGroup");
	frame.trigger("loaded");
}
function closeDialog(result) {
	var dialog = parent.$("#viewGroup");
	if(result){
		dialog.trigger("closeDialog", [result]);
	} else {
		dialog.trigger("closeDialog");	
	}
}
</script>
