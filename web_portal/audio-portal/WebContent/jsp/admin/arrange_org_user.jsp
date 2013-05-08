<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>企业用户管理</title>

<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/popupbox.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css"/>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></script>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js"></SCRIPT>
<c:if test="${!empty errorMessage}">
	<script type="text/javascript">
		$(function() {
			parent.errorDialog("${errorMessage}");
		});
	</script>
</c:if>
<c:if test="${!empty infoMessage}">
	<script type="text/javascript">
	$(function() {
		parent.successDialog("${infoMessage}");
	});	
	</script>
</c:if>
<script type="text/javascript">
	
	//批量提交
	function submitBatch(){
		var data = {};
		data.orgId = $("#orgId").val();
		data.id = [];
		$("input[name=id]:checked").each(function(){
			var id = $(this).val();
			data.id.push(id);
		});
		if(data.id.length==0){	 
			parent.errorDialog("请至少选择一个");
			return;
		} else {
			data.id = data.id.join(",");
			app.assignUser(data, function(result) {
				if(result) {
					if(result.status==1){
						closeDialog(result);		
					} else {
						parent.errorDialog(result.message);
					}
				}
			}, {message:"保存数据...", ui:parent});
		}
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
<body onload="loaded()">
<form id="query" name="query" action="" method="post">
		<div style="margin:10px auto;width: 710px;height: 405px;">
		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" id="table_box" style="border:#A3C5DE 1px solid; border-top:none; border-bottom:none;">
        <tr class="table002" height="32" >
          <td>
            <table width="100%" border="0" cellpadding="0" cellspacing="0" id="site-list">

              <tr class="table003" height="38" >
                <td width="5%" height="38" bgcolor="d3eaef" class="STYLE10">
                  <div align="center"><span><input type="checkbox" id="checkAll"/></span></div>
                </td>
                <td width="30%" height="38" bgcolor="d3eaef" class="STYLE10">
                  <div align="center"><span>登录名</span></div>
                </td>
                <td width="30%"  height="38" bgcolor="d3eaef" class="STYLE10" >
                  <div align="center" ><span>用户名</span></div>
                </td>
                <td width="35%"  height="38" bgcolor="d3eaef" class="STYLE10" style="border-right:none;">
                  <div align="center" ><span>邮箱</span></div>
                </td>
              </tr>

              <c:if test="${fn:length(pageModel.datas)<=0 }">
		        <tr>
		          <td height="32" class="STYLE19" colspan="4"  align="center">
		       		${LANG['website.common.msg.list.nodata']}
		          </td>
		        </tr>
		      </c:if>
              
              <c:if test="${fn:length(pageModel.datas)>0 }">
              	 <input id="orgId" name="orgId" type="hidden" value="${orgId}"
			     <c:forEach var= "user" items = "${pageModel.datas}"  varStatus="status">
			      <tr class="table001" height="32" >
			        <td height="32">
	                  <div align="center"><span><input name="id" type="checkbox" value="${user.id }" /></span></div>
	                </td>
			        <td height="32"><div align="center">${user.loginName }</div></td>
			        <td height="32"><div align="center">${user.trueName }</div></td>
			        <td height="32"><div align="center">${user.userEmail }</div></td>
			      </tr>
			     </c:forEach>
		      </c:if>
            </table>
        </tr>
        <tr>
          <td class="table_bottom" height="38">
			<jsp:include page="/jsp/common/page_info.jsp" />
          </td>
        </tr>
      </table>
      </td>
      </tr>
      </table>
		</div>
      <input name="emile_btn01" class="emile_btn01_PP" type="button"  value="提交" onclick="submitBatch()" style=" margin-right:30px;margin-left: 20px;"/>
      <input name="emile_btn01" class="emile_btn01_PP" type="button"  value="返回" onclick="closeDialog()"/> 
</form>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#assignUserDiv");
	frame.trigger("loaded");
}

function closeDialog(result) {
	var dialog = parent.$("#assignUserDiv");
	if(result){
		dialog.trigger("closeDialog", [result]);
	} else {
		dialog.trigger("closeDialog");	
	}
}
</script>
