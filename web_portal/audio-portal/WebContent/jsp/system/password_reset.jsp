<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="/static/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/common.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/tipsy-master/src/stylesheets/tipsy.css" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/m_genggaimima.css"/>

<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/static/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js"></SCRIPT>
<script type="text/javascript">
	$(document).ready(function(){
		$('#passwordResetForm :input').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 1 });
		var ruleString = {
				required: {
					"lp": "请输入密码",
					"clp": "请输入确认密码",
					"authCode": "请输入验证码"
				},
				rangelength: {
					"lp": "字符长度为6~16位",
					"authCode": "字符长度为4位"
				},
				custom: {
					"equalTo": "确认密码和密码不一致"
				}
		};
		var v = $("#passwordResetForm").validate({
			onkeyup: false,
			errorClass: "warning",
			submitHandler: function(form) {
				resetPass();
			},
			rules: {
	            'lp' : {required:true, rangelength: [6, 16]},
	            'clp' : {required:true, rangelength: [6, 16], equalTo: '#lp'},
	            'authCode' : {required:true, rangelength: [4, 4]}
	        },
	        messages: {
	            'lp' : {required: ruleString.required.lp, rangelength: ruleString.rangelength.lp},
	            'clp' : {required: ruleString.required.clp, rangelength: ruleString.rangelength.lp, equalTo:ruleString.custom.equalTo},
	            'authCode' : {required: ruleString.required.authCode, rangelength: ruleString.rangelength.authCode}
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
	</script>
<title>系统管理员-重置密码</title>
</head>

<body>
<!--页面头部开始-->
<jsp:include page="header.jsp" />  
<!--页面头部结束-->     

<div class="main">
<form action="/system/password/save" name="passwordResetForm" id="passwordResetForm" method="post">
	<input type="hidden" name="type" id="type" value="resetpass"/>
	<input type="hidden" name="random" id="random" value=""/>
	<input type="hidden" name="sid" id="sid" value="${sid}"/>
	<input type="hidden" name="auth" id="auth" value="${auth}"/>
	<input type="hidden" name="ts" id="ts" value="${ts}"/>
	<input type="hidden" name="ln" id="ln" value="${ln}"/>
  <h3>重置密码</h3>
  
	<ul>
    	<li><span>密码：</span><input id="lp" name="lp" class="text01" type="password" /></li>
    	<li><span>确认密码：</span><input id="clp" name="clp" class="text01" type="password" /></li>
    	<li><span>验证码：</span><input id="authCode" name="authCode" type="text" class="text02 " />
                    <img id="authCodeImg" class="yanzhengma" alt="刷新" src=""  width="0" height="0" style="border: 1px solid #bbbbbb;" onclick="randomImg()"/>
                    &nbsp;&nbsp;<a class="change" href="javascript:;" onclick="randomImg()">看不清？换一张</a>
   	    </li>
	</ul>
	<div style="clear: both;"></div>
	<input name="button01" class="button01" type="submit" value="提交"  />
    <input name="button02" class="button02" type="button" value="取消"  onclick="javascript:back();"/>
</form>  
</div>      

 <!--页脚版权-->       
<div id="copy">
<span class="copy_text" >Copyright © 2003-2012 Shanghai Shrine Telecom Co., Ltd. 2012. All rights reserved.Version:eMeeting V5.0</span>
</div>   
</body>
</html>


<script type="text/javascript">

function resetPass() {
	var data = {};
	data.ts = $("#ts").val();
	data.sid = $("#sid").val();
	data.auth = $("#auth").val();
	data.authCode = $("#authCode").val();
	data.type = $("#type").val();
	data.random = $("#random").val();
	data.ln = $("#ln").val();	
	data.lp = $("#lp").val();
	data.clp = $("#clp").val();
	app.resetUserPass(data, function(result) {
		if (result && result.status==2) {
			successDialog("密码修改成功", function() {
				//window.location.href = "/system/login";
			});
		} else {
			errorDialog(result.message);
		}

	});
	
}
function successDialog(message, callback) {
	$("<div/>").successDialog({
		"title": "${LANG['system.tip']}",
		"dialogClass" : "ui-dialog-smile",
		"message" : message,
		"actions": ["${LANG['system.close']}"],
		"callback": callback
	});
}
function errorDialog(message) {
	$("<div/>").errorDialog({
		"title": "${LANG['system.tip']}",
		"dialogClass" : "ui-dialog-smile",
		"message" : message,
		"actions": ["${LANG['system.close']}"]
	});
}
function randomImg() {
	var random = new Date().getTime();
	$("#random").val(random);
	$("#authCodeImg").attr("src", "/valid/image?type=syslogin&random="+random);
}
function back(){
	window.history.back(-1);
}
$(function() {
	randomImg();
	$("#authCodeImg").width(66);
	$("#authCodeImg").height(22);
	
});
</script>
