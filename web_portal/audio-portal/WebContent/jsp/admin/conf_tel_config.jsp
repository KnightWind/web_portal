<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会议全局设置</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/rightbox.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">

<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.uniform/jquery.uniform.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js"></script>

<%-- <cc:confList var="CONF_CONFIG_ALLOW_PHONE_ENABLED"/><cc:confList var="CONF_CONFIG_ALLOW_PHONE_DISABLED"/><cc:confList var="CONF_CONFIG_ALLOW_PHONE_AUTOABLED"/> --%>
<%-- <cc:confList var="CONF_CONFIG_AUTO_CALL_ENABLED"/><cc:confList var="CONF_CONFIG_AUTO_CALL_DISABLED"/><cc:confList var="CONF_CONFIG_AUTO_CALL_AUTOABLED"/> --%>
<%-- <cc:confList var="CONF_CONFIG_VOIP_ENABLED"/><cc:confList var="CONF_CONFIG_VOIP_DISABLED"/><cc:confList var="CONF_CONFIG_VOIP_AUTOABLED"/> --%>
<cc:siteList var="EMPOWER_DISABLED"/>
<cc:siteList var="EMPOWER_ENABLED"/>
<cc:siteList var="EMPOWER_CODE_FIELD_ARRAY"/>
	<script type="text/javascript">
		$(function() {

			$("input:radio[name=phoneFlag]").change(function() {
				var value = $(this).val();
				if(value==1){
					$("input:radio[name=autoFlag]").removeAttr("disabled");
				} else {
					$("input:radio[name=autoFlag]").attr("disabled",'disabled');
					$("input:radio[name=autoFlag]:eq(1)").attr("checked",'checked');
				}
			});
		});
	</script>	  
<!-- <script type="text/javascript"> -->
<!-- 	$(document).ready(function() { -->
<!-- 		$("input").not(".skipThese").uniform(); -->
<!-- 		$(".allowTelFlag").change(function() { -->
<!-- 			var value = $(this).val(); -->
<%-- 			if (value==${CONF_CONFIG_ALLOW_PHONE_DISABLED}) { --%>
<!-- 				$(".needEnable").rules("remove"); -->
<!-- 				$(".needDisable").rules("remove"); -->
<!-- 				$(".error-container").empty(); -->
<!-- 				$(".needEnable").attr("disabled", true); -->
<!-- 				$(".needDisable").attr("disabled", true); -->
<!-- 			} else { -->
<!-- 				$(".needEnable").rules("add", {required:true}); -->
<!-- 				$(".needDisable").rules("add", {required:true}); -->
				
<!-- 				$(".needEnable").attr("disabled", false); -->
<!-- 				$(".needDisable").attr("disabled", false); -->
<!-- 			} -->
<!-- 			$("input").not(".skipThese").uniform(); -->
<!-- 		}); -->
<!-- 		var v = $("#configForm").validate({ -->
<!-- 			errorClass: "warning", -->
<!-- 			rules: { -->
<!-- 				'allowTelFlag' : {required:true}, -->
<!-- 	            'autoCallFlag' : {required:true}, -->
<!-- 	            'voipFlag' : {required:true} -->
<!-- 	        }, -->
<!-- 	        messages: { -->
<!-- 	        	'allowTelFlag' : {required:"请选择是否开启创建电话会议"}, -->
<!-- 	            'autoCallFlag' : {required:"请选择是否开启自动外呼"}, -->
<!-- 	            'voipFlag' : {required:"请选择是否开启集成VOIP"} -->
<!-- 	        }, -->
<!-- 	        success: function (label) { -->

<!-- 	        }, -->
<!-- 	        errorPlacement: function (error, element) { -->
<!-- 	        	  if ( element.is(":radio") ) { -->
<!-- 	        	        error.appendTo( element.closest("td").next().next() );   -->
<!-- 	        	  } -->
<!-- 	        } -->
<!-- 		});	 -->
<!-- 	}); -->
<!-- </script> -->
</head>
<body>
<div class="s_emile">
<div class="s_emile_01">
  <div class="s_emile_01_top"><span>${LANG['site.admin.conf.config.phone.title'] }</span></div>
<%--   <c:if test="${config.id==null ||  config.id<=0 }"> --%>
<!--   <form name="configForm" id="configForm" action="/admin/config/telconfig/save" method="post"> -->
<%--   </c:if> --%>
<%--   <c:if test="${config.id>0}"> --%>
<!--   <form name="configForm" id="configForm" action="/admin/config/telconfig/update" method="post"> -->
<%--   </c:if> --%>
  <form name="configForm" id="configForm" action="/admin/config/telconfig/update" method="post">
<%--   <input name="id" id="id" type="hidden" value="${config.id }" checked="checked"/> --%>
<%--   <input name="siteId" id="siteId" type="hidden" value="${config.siteId }"/> --%>

  <c:set var="flagCount" value="0"/>
  <c:forEach var="eachField" items="${EMPOWER_CODE_FIELD_ARRAY}" varStatus="fieldSatus">
   <c:if test="${fn:indexOf(eachField[1],'Flag') > -1 && eachField[2]==1}"><c:set var="flagCount" value="${flagCount+1 }"/></c:if>
     </c:forEach>
<c:if test="${powerCount > 0}">
  <table class="all_install" width="auto" border="0" cellpadding="0" cellspacing="0">  
  	<tr>
  		<td>已授权的功能</td>
  	</tr>
  	<tr>
    	<td width="120px" height="38px"><div align="center"></div></td>
    	<td width="120px" height="38px"><div align="center">${LANG['site.admin.conf.config.option.enable']}</div></td>
        <td width="120px" height="38px"><div align="center">${LANG['site.admin.conf.config.option.disable']}</div></td>
        <td width="auto" height="38px" style="border:0px;background:#FFFFFF"></td>
        <%-- <td width="120px" height="38px" style="border-right:#CFE2EF 1px solid;"><div align="center">不修改</div></td>
         --%>
    </tr>
    <c:forEach var="eachField" items="${EMPOWER_CODE_FIELD_ARRAY}" varStatus="fieldSatus">
    <c:set var="eachFieldName" value="${eachField[1]}"/>
   
    <c:if test="${sitePower[eachFieldName]==EMPOWER_ENABLED  && eachField[2]==1}">
    
     <tr>
	    	<td><div align="center"><c:set var="langName" value="system.site.empower.item.${eachField[0]}"/>${LANG[langName]}</div></td>
	        <td><div align="center">
	        	<input type="radio" class="allowTelFlag" name="${eachFieldName}"  value="${EMPOWER_ENABLED}" 
	        	<c:if test="${globalConfig[eachFieldName]==EMPOWER_ENABLED }">checked</c:if>/>
	        	</div>
	       	</td>
	        <td><div align="center">
	        	<input type="radio" class="allowTelFlag" name="${eachFieldName }" value="${EMPOWER_DISABLED}" 
	        	<c:if test="${globalConfig[eachFieldName]==EMPOWER_DISABLED }">checked</c:if>/>
	        	</div>
	       	</td>
	        <td width="auto" height="38px" class="error-container" style="border:0px;background:#FFFFFF;color:red;padding-left:10px;"></td>
	    </tr>
	   
	    </c:if>
    </c:forEach>
 
  </table>
  </c:if>
  
  <c:if test="${flagCount > powerCount}">
  <table class="all_install" width="auto" border="0" cellpadding="0" cellspacing="0">  
  	<tr>
  		<td>未授权的功能</td>
  	</tr>
  	<tr>
    	<td width="120px" height="38px"><div align="center"></div></td>
    	<td width="120px" height="38px"><div align="center">${LANG['site.admin.conf.config.option.enable']}</div></td>
        <td width="120px" height="38px"><div align="center">${LANG['site.admin.conf.config.option.disable']}</div></td>
        <td width="auto" height="38px" style="border:0px;background:#FFFFFF"></td>
    </tr>
     <c:forEach var="eachField" items="${EMPOWER_CODE_FIELD_ARRAY}" varStatus="fieldSatus">
    <c:set var="eachFieldName" value="${eachField[1]}"/>
    <c:if test="${sitePower[eachFieldName]==EMPOWER_DISABLED && eachField[2]==1}">
     <tr>
	    	<td><div align="center"><c:set var="langName" value="system.site.empower.item.${eachField[0]}"/>${LANG[langName]}</div></td>
	        <td><div align="center">
	        	<input type="radio" class="allowTelFlag" name="${eachFieldName}"  value="${EMPOWER_ENABLED}" 
	        	<c:if test="${globalConfig[eachFieldName]==EMPOWER_ENABLED }">checked</c:if> disabled/>
	        	</div>
	       	</td>
	        <td><div align="center">
	        	<input type="radio" class="allowTelFlag" name="${eachFieldName }" value="${EMPOWER_DISABLED}" 
	        	<c:if test="${globalConfig[eachFieldName]==EMPOWER_DISABLED }">checked</c:if> disabled/>
	        	</div>
	       	</td>
	        <td width="auto" height="38px" class="error-container" style="border:0px;background:#FFFFFF;color:red;padding-left:10px;"></td>
	    </tr>
	    </c:if>
    </c:forEach>
    
   
    
  </table>
  </c:if>
  <div style="width:450px;text-align: center;margin-top: 15px;">
  	<input name="button_a" class="button_a skipThese" type="submit" value="${LANG['system.submit']}"/>
  	<c:if test="${!empty infoMessage}">
		<img src="${baseUrlStatic}/images/ys_r_bg.jpg" width="16" height="14" style="margin-left:15px;margin-top:5px;margin-right: 5px"/><label style='color:#258021'>${infoMessage}</label>
	</c:if>
	<c:if test="${!empty errorMessage}">
   		<img src="${baseUrlStatic}/images/ys_w_bg.jpg" width="16" height="14" style="margin-left:15px;margin-top:5px;margin-right: 5px;"/><label style='color:red'>${errorMessage}</label>
   	</c:if>
  </div>
 
    </form>
  </div>
</div>
</body>
</html>
