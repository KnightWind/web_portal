<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>${LANG['system.notice.list.Create']}</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/popupbox.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/tipsy-master/src/stylesheets/tipsy.css" />
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/common.css"/>	
	<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></script>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
	<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js"></script>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js"></SCRIPT>
	
	<script type="text/javascript">
	var organizForm = null;
	$(document).ready(function(){
		$('#organizForm :input').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 1 });
		var ruleString = {
				required: {
					"orgName": "${LANG['valid.input.orgName']}"
				},
				maxlength: {
					"orgDesc": "${LANG['valid.rangelength256']}"
				},
				rangelength: {
					"orgName": "${LANG['valid.rangelength132']}"
				}
		};
		$.validator.addMethod("notRequired", function(value, element) {
			if(value==null || value=="" || value.length==0){
				$(element).tipsy('hide').removeAttr('original-title');
			}
	    	return true;
	 	}, "");
		organizForm = $("#organizForm").validate({
			onkeyup: false,
			errorClass: "warning",
			submitHandler: function(form) {
				saveOrganiz();
			},
			rules: {
	            'orgName' : {required:true, rangelength: [1, 32]},
	            'orgDesc' : {notRequired:true, maxlength: 256}
	        },
	        messages: {
	            'orgName' : {required:ruleString.required.orgName, rangelength: ruleString.rangelength.orgName},
	            'orgDesc' : {maxlength: ruleString.maxlength.orgDesc}
	        },
	        success: function (label) {
	            $(label).each(function () {
	                $('#' + this.htmlFor).tipsy('hide').removeAttr('original-title');
	            });
	        },
	        errorPlacement: function (error, element) {
	        	var errorEl = $(".tipsy");
	        	var errorText = error.text();
	        	if (!errorEl || errorEl.length==0) {
		            element.attr('original-title', errorText);
	                element.tipsy('show');	
	        	} else {
	        		//for update first tip div title
		        	var elTitle = element.attr('original-title');
		        	if (elTitle && elTitle.length>0 && elTitle!=errorText) {
		        		element.attr('original-title', error.text());
		                element.tipsy('show');	
		        	}
	        	}
	        }
		});	
	});	
	
	function submitForm() {
		if(organizForm.form()){
			saveOrganiz();
		}
	}
	function saveOrganiz(){
		var frame = parent.$("#organizDiv");
		var organizID = "${siteOrg.id}";
		var organiz = {};
		organiz.orgName = $("#orgName").val();
		organiz.orgDesc = $("#orgDesc").val();
		if (organizID && organizID.length>0) {
			organiz.id = organizID;
			app.updateOrganiz(organiz, function(result) {
				frame.trigger("closeDialog", [result]);
			},{message:"修改数据...", ui:parent});
		} else {
			app.createOrganiz(organiz, function(result) {
				frame.trigger("closeDialog", [result]);
			},{message:"保存数据...", ui:parent});	
		}
	}
	</script>
</head>
<body onload="loaded()" style="font-size: 12px;padding: 10px 15px; margin: 0px;">
	<form id="organizForm" name="organizForm" action="">
		<ul class="emile_01_bottom_jg">
		        	<li><span>机构名</span><input id="orgName" name="orgName" class="text02" type="text" value="${siteOrg.orgName}"/></li>
		            
		            <li><span>备注</span><textarea id="orgDesc" name="orgDesc" class="text07" cols="" rows="" >${siteOrg.orgDesc}</textarea></li>
		            <li class="button_submit">
		            	<input name="emile_button" class="emile_button_jg" type="button"  value="提交" onclick="submitForm()"/>
		            	<input name="emile_button02" class="emile_button02_jg closeButton" type="button"  value="取消" onclick="closeDialog()"/>
		            </li>       
		            </ul>	
	</form>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#organizDiv");
	frame.trigger("loaded");
}

function closeDialog(result) {
	var dialog = parent.$("#organizDiv");
	if(result){
		dialog.trigger("closeDialog", [result]);
	} else {
		dialog.trigger("closeDialog");	
	}
}
</script>