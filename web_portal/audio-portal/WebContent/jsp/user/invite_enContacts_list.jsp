<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>企业联系人列表</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></script>	
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.plugin.js?ver=${version}"></script>
<script type="text/javascript">

	function getContactsData(){
		var datas = new Array();
		$("input[name=id]:checked").each(function(){
			var item = new Object();
			var id = $(this).val();
			var name= $("td[name=contactName"+id+"]").html();
			var email= $("td[name=contactEmail"+id+"]").html();
			var phone= $("td[name=contactPhone"+id+"]").html();
			var mobile= $("td[name=contactMobile"+id+"]").html();
			item.name = name;
			item.email = email;
			item.phone = phone||mobile;
			item.userId = id;
			datas.push(item);
		});
		return datas;
	}
	//导入联系人用
	function importContacts(){
		if($("input[name=id]:checked").length>0){
			$("#query").attr("action","/user/contact/importBatchByContacts");
			$("#query").submit();
		}else{
			alert("请选择导入项！");
		}
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
		$(".contact_search").watermark('用户名、英文名、邮箱、联系电话、手机号码');
	});
	
	function submitForm(){
		$('input').trigger('submitForm');
		query.submit();
	}
</script>
</head>
<body style="height: 550px;">
<form id="query" name="query" action="/user/contact/showEnterpriseContacts" method="post" onsubmit="javascript:$('input').trigger('submitForm');">
	  <input type="hidden"  name="showAll" value="${showAll}"/>
	  <!--企业通讯录开始-->
        <div class="First_Steps_main_invite" style=" background:#FFF;">
          <table width="730" align="center" cellpadding="0" cellspacing="0" border="0" id="t_box" >
            <tr>
              <td height="40" colspan="6" bgcolor="#EAF4FC" class="tr_top">
              	<input name="keyword" type="text" value="${keyword}" class="meeting_ss contact_search" />
                <input class="meeting_but" type="button" onclick="javscript:submitForm();" /></td>
            </tr>
            <tr align="center" height="35" class="tr_center" bgcolor="#000066">
              <td width="5%" class="tr_center"><input name="" id="checkAll" type="checkbox" value="" /></td>
              <td width="15%" class="tr_center">用户名</td>
              <td width="15%" class="tr_center">英文名</td>
              <td width="25%" class="tr_center">邮箱</td>
              <td width="20%" class="tr_center">联系电话</td>
              <td width="20%" class="tr_center" style=" border-right:#D2D8DB 1px solid">手机号码</td>
            </tr>
            <c:if test="${fn:length(pageModel.datas)<=0}">
				<tr class="table001" height="32"  >
			            <td class="STYLE19" height="32" colspan="11" align="center">
			            	<c:if test="${keyword !=null}">
			            		抱歉！没找到符合条件的用户。
			            	</c:if>
			            	<c:if test="${empty keyword}">
			            		您的企业站点中没有其他用户。
			            	</c:if>
			            </td>
			     </tr>
			</c:if>
			<c:forEach var="contact" items="${pageModel.datas}" varStatus="status">
	            <tr name="contactsItem" align="center" bgcolor="#FFFFFF" height="30">
	              <td class="tr_main" style=" border-left:#D2D8DB 1px solid"><input name="id" type="checkbox" value="${contact.id}" /></td>
	              <td class="tr_main" style=" border-left:#D2D8DB 1px solid" name="contactName${contact.id}">${contact.trueName}</td>
	              <td class="tr_main" >${contact.enName}</td>
	              <td class="tr_main" name="contactEmail${contact.id}">${contact.userEmail}</td>
	              <td class="tr_main" name="contactPhone${contact.id}">${contact.phone}</td>
	              <td class="tr_main" style=" border-right:1px solid #D2D8DB" align="center" name="contactMobile${contact.id}">${contact.mobile}</td>
	            </tr>
            </c:forEach>
			<tr>
				<td height="35" colspan="6" class="tr_bottom table_bottom">
					 <jsp:include page="/jsp/common/page_info.jsp" />
				</td>
			</tr>            
          </table>
        </div>
	</form>
</body>
</html>

