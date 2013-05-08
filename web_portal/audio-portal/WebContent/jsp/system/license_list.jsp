<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/Popup.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="/static/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/common.css"/>
<title></title>
<SCRIPT type="text/javascript" src="/static/js/jquery-1.8.3.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/static/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/date.js"></SCRIPT>
<script type="text/javascript" src="/static/js/widgets.js"></script>
<style type="text/css">
	.showborder{
		border:#ccc 1px solid;
	}
	.fh_btn{ margin:20px 0px 20px 30px; padding-bottom:20px;display:inline-block;}
	.b_c{ display:inline-block; vertical-align:middle; margin-left:30px; margin-right:5px;}
	.b_p{ display:inline-block;}
</style>
</head>
<body onload="loaded();">
<div class="number_body">
<form id="save" name="save" action="/system/lic/save" method="post">
	<input name="siteId" type="hidden" value="${siteId}"/>
	<input name="userId" type="hidden" value="${userId}"/>
	<table border="0" cellpadding="0" cellspacing="0" class="site_top">
		<tr height="22">
	    	<td width="113" align="center">点数</td>
	        <td width="170" align="center">生效日期</td>
	        <td width="170" align="center">结束日期</td>
	        <td width="200"></td> 
	    </tr>
	    <tr height="36">
	    	<td align="center"><input id="licenseCount"  name="licNum" type="text" class="text01" /></td>
	        <td align="center"><input id="effeDate" name="effeDate" type="text" class="text02" /></td>
	        <td align="center"><input id="expireDate" name="expireDate" type="text" class="text03" /></td>
	        <td align="center"><a onclick="addLic();" href="#" class="tijiao_btn Public_button">提交</a></td>  
	    </tr>
	</table>
</form>

<form id="hidsave" name="hidsave" action="/system/lic/save" method="post">
	<input name="siteId" type="hidden" value="${siteId}"/>
	<input name="userId" type="hidden" value="${userId}"/>
	<input id="h_id" name="id" type="hidden" value=""/>
	<input id="h_licNum" name="licNum" type="hidden" />
	<input id="h_effeDate" name="effeDate" type="hidden" />
	<input id="h_expireDate" name="expireDate" type="hidden" />
</form>
<div class="si_m">                
<table border="0" cellpadding="0" cellspacing="0" class="site_main">
	<c:if test="${fn:length(pageModel.datas)<=0 }">
         <tr>
           <td colspan="4" width="653" align="center">
        	${LANG['website.common.msg.list.nodata']}
           </td>
         </tr>
      </c:if>
     <c:if test="${fn:length(pageModel.datas)>0 }">
     <c:forEach var="lic" items = "${pageModel.datas}"  varStatus="status">
     	<tr height="36">
	    	<td width="113" align="center">
	    		<input readonly="readonly" class="text_01" id="licNum${lic.id}" type="text" value="${lic.licNum}" bak="${lic.licNum}"/>
	    	</td>
	        <td width="190" align="center">
	        	<fmt:formatDate var="licEffeDate"  value="${lic.effeDate}" type="date" pattern="yyyy-MM-dd"/>
	        	<input readonly="readonly" class="text_02" id="effeDate${lic.id}" type="text" value="${licEffeDate }" bak="${licEffeDate}"/>
	        </td>
	        <td width="160" align="center">
	        	<fmt:formatDate var="licExpireDate"  value="${lic.expireDate}" pattern="yyyy-MM-dd"/>
	        	<input readonly="readonly" class="text_03" id="expireDate${lic.id}" type="text" value="${licExpireDate }" bak="${licExpireDate}"/>
	        </td>
	        <td width="210" align="center">
	         <c:choose>
					<c:when  test="${lic.effeFlag eq '0' and lic.expireFlag eq '0'}">
						<a id="modify${lic.id}" href="javascript:;" class="change_btn" onclick="toUpdate(${lic.id});" >修改</a> 
						<a id="del${lic.id}" href="javascript:;" class="cc_btn" onclick="del(${lic.id});" >删除</a>
						<a id="stroe${lic.id}" style="display: none;" href="javascript:;" class="change_btn" onclick="stroeData(${lic.id});" >保存</a>&nbsp;
						<a id="cancel${lic.id}" style="display: none;" href="javascript:;"  class="change_btn" onclick="cancelUpdate(${lic.id})">取消</a>						
					</c:when>
					<c:when test="${lic.effeFlag eq '1' and lic.expireFlag eq '0'}">
						&nbsp;<a href="javascript:;" class="cc_btn" style="color: #666" >(已生效)</a>
					</c:when>
					<c:when test="${lic.expireFlag != '1'}">
						&nbsp;<a href="javascript:;" class="cc_btn" style="color: #666" >(已过期)</a>
					</c:when>
					<c:otherwise>
						&nbsp;<a href="javascript:;" class="cc_btn" style="color: #666" >(已失效)</a>
					</c:otherwise>
				</c:choose>
<%--				<c:if test="${lic.effeFlag eq '1'}">--%>
<%--					&nbsp;<a href="javascript:;" class="cc_btn" style="color: #666" >(已生效)</a>--%>
<%--				</c:if>--%>
	        </td> 
   		 </tr>
    </c:forEach>
   </c:if>
</table> </div>
	<a href="javascript:closeDialog();" style="margin:15px;display:inline-block;" class="Public_button" onclick="closeDialog();">关闭</a>
	<c:if test="${userId != '' && userId > 0}">
		<input class="b_c" name="sendEmail" type="checkbox" value=""/><span class="b_p">发送邮件</span>
	</c:if>	 
</div>
</body>
</html>
<script type="text/javascript">
$(document).ready(function(){
	
	$("input[name=effeDate]" ).datepicker({
		minDate: +0,
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy-mm-dd",
		showOn: "both",
		buttonImage: "/static/images/calendar.jpg",
		buttonImageOnly: true
	});
	
	$("input[name=expireDate]" ).datepicker({
		minDate: +0,
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy-mm-dd",
		showOn: "both",
		buttonImage: "/static/images/calendar.jpg",
		buttonImageOnly: true
	});
	
});

function cancelUpdate(licId) {
	$("#del"+licId).show();
	$("#modify"+licId).show();
	$("#stroe"+licId).hide();
	$("#cancel"+licId).hide();
	$("#licNum"+licId).removeClass("showborder").attr("readonly",true);
	$("#effeDate"+licId).datepicker("destroy").removeClass("showborder").attr("readonly",true);
	$("#expireDate"+licId).datepicker("destroy").removeClass("showborder").attr("readonly",true);
	var licNum = $("#licNum"+licId).attr("bak");
	$("#licNum"+licId).val(licNum);
	var effeDate = $("#effeDate"+licId).attr("bak");
	$("#effeDate"+licId).val(effeDate);
	var expireDate = $("#expireDate"+licId).attr("bak");
	$("#expireDate"+licId).val(expireDate);
}

function toUpdate(licId){
	$("#del"+licId).hide();
	$("#modify"+licId).hide();
	$("#stroe"+licId).show();
	$("#cancel"+licId).show();
	$("#licNum"+licId).addClass("showborder").attr("readonly",false);
	$("#effeDate"+licId).addClass("showborder").datepicker({
		minDate: +0,
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy-mm-dd",
		//showOn: "both",
		buttonImage: "/static/images/calendar.jpg",
		buttonImageOnly: true
	});
	
	$("#expireDate"+licId).addClass("showborder").datepicker({
		minDate: +0,
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy-mm-dd",
		//showOn: "both",
		buttonImage: "/static/images/calendar.jpg",
		buttonImageOnly: true
	});
}

function checkForm(licid){
	var count = $("#licenseCount").val();
	if(licid){
		count = $("#licNum"+licid).val();
	}
	if(!count){
		parent.errorDialog("请输入点数");
		return false;
	}
	if(count && !count.isInteger()){
		parent.errorDialog("点数必须是整数");
		return false;
	}
	var effeDate = $("#effeDate").val();
	if(licid){
		effeDate = $("#effeDate"+licid).val();
	}
	if(!effeDate){
		parent.errorDialog("请选择生效日期");
		return false;
	}
	var expireDate = $("#expireDate").val();
	if(licid){
		expireDate = $("#expireDate"+licid).val();
	}	
	if(!expireDate){
		parent.errorDialog("请选择结束日期");
		return false;
	}
	var effeDateTemp = effeDate.parseDate();
	var expireDateTemp = expireDate.parseDate();
	if(effeDateTemp.after(expireDateTemp)){
		parent.errorDialog("生效日期不能大于结束日期");
		return false;
	}
	return true;
}

function stroeData(licid){
	if(checkForm(licid)){
		$("#h_id").val(licid);
		$("#h_licNum").val($("#licNum"+licid).val());
		$("#h_effeDate").val($("#effeDate"+licid).val()+" 00:00:00");
		$("#h_expireDate").val($("#expireDate"+licid).val()+" 23:59:59");
	 	hidsave.submit();	
	}
}

function addLic(){
	if(checkForm()){
		$("input[name=effeDate]").val($("input[name=effeDate]").val()+" 00:00:00");
		$("input[name=expireDate]").val($("input[name=expireDate]").val()+" 23:59:59");
		save.submit();	
	}
}

function loaded() {
	
	var frame = parent.$("#countManagerDiv");
	frame.trigger("loaded");
}

function closeDialog() {
	//alert('hi');
	if($("input[name=sendEmail]:checked").length>0){
		$.ajax({
	      	type: "POST",
	      	url:"/system/lic/sendMail?userId=${userId}",
	      	dataType:"json",
	      	async:false,
	      	success:function(data){
				if(data.status == 1){
					parent.successDialog("邮件已发送！");
				}else{
					parent.errorDialog("邮件发送失败！");
				} 
	      	},
	        error:function(XMLHttpRequest, textStatus, errorThrown) {
	        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
	       		//html = "查询参会人失败！";
	       		//$(self).attr("title",html);
	        }
		}); 
		closePup();	
	}else{
		closePup();	
	}
}

function closePup(){
	if(parent.$("#hostManagerDiv").length>0){
		parent.$("#hostManagerDiv").find("iframe")[0].src = "/system/lic/listHost?siteId=${siteId}&userId=${userId}";	
	}
	var frame = parent.$("#countManagerDiv");
	frame.trigger("closeDialog");
}

function del(id){
	parent.confirmDialog("确认删除？",function(){
		window.location = "/system/lic/del?id="+id;	
	});
}


</script>