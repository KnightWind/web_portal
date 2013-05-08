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
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<title>index</title>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></script>	
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.plugin.js?ver=${version}"></script>	
<script type="text/javascript">
	function delUser(id){
		parent.parent.confirmDialog("确定删除联系人？", function() {
			window.location = "/user/contact/deleteContact?id="+id;
		});
	}
	
	//批量删除联系人
	function deleteContactBatch(){
		parent.parent.confirmDialog("确定删除联系人？", function() {
			$("#query").attr("action","/user/contact/deleteContactBatch");
			$("#query").submit();
		});
	}

	function toEditContact(id){
		parent.parent.addContact(id);
	}
	
	function doImport(){
		parent.parent.bulkImportContact();
	}
	
	$(document).ready(function(){
		$(".meeting_search").watermark('用户名、邮箱、联系电话、手机号码');
	});
	
	function submitForm(){
		$('input').trigger('submitForm');
		query.submit();
	}
	
	$(document).ready(function(){
		
		//全选and全不选
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
			
	});
</script>
</head>
<body>
  <form id="query" name="query" action="/user/contact/list" method="post">
  	<input type="hidden"  name="group_id" value="${group_id}"/>
  <div class="meeting_main">
      <table id="t_box" align="center" border="0" cellpadding="0" cellspacing="0" width="100%"><tbody><tr>
        <td height="40" colspan="6" bgcolor="#EAF4FC" class="tr_top">
        	<input class="meeting_ss meeting_search" name="keyword"  value="${keyword}" type="text">
          <input class="meeting_but" type="button" onclick="submitForm();">
          <div class="top_right">
          	<span class="button_common" style="margin-right: 5px;"><a href="javascript:;" onclick="toEditContact('');"><img width="16" height="16" align="absmiddle" style=" margin-left:5px; margin-right:5px;" src="/static/images/add_16.png">添加联系人</a></span>
          	<span class="button_common" style="margin-right: 5px;"><a href="javascript:;" onclick="doImport();"><img width="16" height="16" align="absmiddle" style=" margin-left:5px; margin-right:5px;" src="/static/images/add_16.png">批量添加联系人</a></span>
          	<span class="button_common"><a href="javascript:;" onclick="deleteContactBatch();"><img width="16" height="16" align="absmiddle" style=" margin-left:5px; margin-right:5px;" src="/static/images/email02.png">批量删除联系人</a></span>
          </div>
          </td>
      </tr>
      <tr align="center" height="35" class="tr_center" bgcolor="#000066">
        <td width="5%" class="tr_center"><input type="checkbox" id="checkAll"/></td>
        <td width="20%" class="tr_center">用户名</td>
        <td width="20%" class="tr_center">邮箱</td>
        <td width="20%" class="tr_center">联系电话</td>
        <td width="15%" class="tr_center">手机号码</td>
        <td width="15%" class="tr_center" style=" border-right:#D2D8DB 1px solid">操作</td>
      </tr>
     <c:if test="${fn:length(pageModel.datas)<=0}">
				<tr class="table001" height="32"  >
			            <td class="STYLE19"  height="32" colspan="11" align="center"> ${LANG['website.common.msg.list.nodata']} </td>
			     </tr>
			</c:if>
	<c:forEach var="contact" items="${pageModel.datas}" varStatus="status">
	      <tr align="center" bgcolor="#FFFFFF" height="32">
	      	<td class="tr_main"  style=" border-left:#D2D8DB 1px solid"><input name="id" type="checkbox" value="${contact.id }" /></td>
	        <td class="tr_main">${contact.contactName}</td>
	        <td class="tr_main">${contact.contactEmail}</td>
	        <td class="tr_main">${contact.contactPhone}</td>
	        <td class="tr_main">${contact.contactMobile}</td>
	        <td class="tr_main" style=" border-right:1px solid #D2D8DB" align="center">
	        <table>
	            <tbody><tr>
	              <td class="k_a"><a href="javascript:toEditContact('${contact.id }')"><img style=" margin-right:5px;" src="/static/images/email01.png" align="absmiddle" height="16" width="16">修改</a></td>
	              <td class="k_a"><a href="javascript:delUser('${contact.id }')"><img style=" margin-right:5px;" src="/static/images/email02.png" align="absmiddle" height="17" width="12">删除</a></td>
	            </tr>
	          </tbody>
	          </table>
	         </td>
	      </tr>
     </c:forEach>
     <tr>
        <td class="table_bottom" height="38" colspan="6"> 
        	 <jsp:include page="/jsp/common/page_info.jsp" />
        </td>
      </tr>
    </tbody>
    </table>
  </div>
  </form>
</body>
</html>

