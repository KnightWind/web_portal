<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/index.css?ver=${version}"/>

<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>
<SCRIPT type="text/javascript" src="/static/js/jquery-ui-1.9.2.custom.js?ver=${version}"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js?ver=${version}"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js?ver=${version}"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></SCRIPT>
<script type="text/javascript">
	var passwordForgetForm = null;
	$(document).ready(function(){
		$('#passwordForgetForm :input').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 0.65 });
		var ruleString = {
				required: {
					"email": "${LANG['valid.input.email']}",
					"authCode": "${LANG['valid.input.passAuthCode']}"
				},
				rangelength: {
					"email": "${LANG['valid.rangelength664']}",
					"authCode": "${LANG['valid.rangelength4']}"
				},
				custom: {
					"email": "${LANG['valid.checkEmail']}"
				}
		};
		passwordForgetForm = $("#passwordForgetForm").validate({
			onkeyup: false,
			errorClass: "warning",
			rules: {
	            'userEmail' : {required:true, rangelength: [6, 32], email: true},
	            'authCode' : {required:true, rangelength: [4, 4]}
	        },
	        messages: {
	            'userEmail' : {required: ruleString.required.email, rangelength: ruleString.rangelength.email, email:ruleString.custom.email},
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
	
	function checkForm() {
		if(passwordForgetForm.form()){
			sendEmail();
		}
	}
</script>
<title>忘记密码</title>
</head>
<body>
<!--页面头部开始-->
<jsp:include page="header.jsp" />

<div id="head_bar">
  <div class="nav_profile"><img src="/static/images/password_bg.png" width="13" height="16" align="absmiddle" /><a href="#"><span>忘记密码？</span></a> </div>
  <!--<ul class="nav_help">
    <li class="bar01" align="absmiddle"><a  title="下载中心" href="#">下载中心</a> </li>
    <li class="bar02"><a  title="帮助" href="#">帮助</a> </li>
    <li class="bar03"><a  title="退出" href="#">退出</a> </li>
  </ul>-->
</div>
<!--忘记密码-->
<div class="Forget_password ">
<form action="/user/password/sendEmail" name="passwordForgetForm" id="passwordForgetForm" method="post">
<div class="main">
  <input type="hidden" name="random" id="random" value=""/>
	<input type="hidden" name="type" id="type" value="forgetpass"/>
  <p>*  输入与您账户关联的电子邮件地址。我们将向您发送页面链接，通过该页面您可以轻松创建新密码。</p>
	<ul>
    	<li><span>邮箱地址：</span><input id="userEmail" name="userEmail" class="text01" type="text" /></li>
    	<li><span>验证码：</span> 
    	<input id="authCode" name="authCode" type="text" class="text04"/>
                    <img id="authCodeImg" class="yanzhengma" alt="刷新" src=""  width="0" height="0" style="border: 1px solid #bbbbbb;" onclick="randomImg()"/>
                    &nbsp;&nbsp;&nbsp;
                    <a class="change" href="javascript:;" onclick="randomImg()">看不清？换一张</a></li>
	</ul>
	<div style="clear: both;"></div>
	<a href="Javascript:" class="bt01"  onclick="javascript:checkForm()">提交</a>
    <a href="Javascript:" class="bt02"  onclick="javascript:back();">取消</a>
</div>
</form>
<!--页面下部-->
<div id="copy_close">
<jsp:include page="footer.jsp" />
</div>
</body>
</html>
<script type="text/javascript">
function randomImg() {
	var random = new Date().getTime();
	$("#random").val(random);
	$("#authCodeImg").attr("src", "/valid/image?type=forgetpass&random="+random);
}

$(function() {
	randomImg();
	$("#authCodeImg").width(66);
	$("#authCodeImg").height(22);
	
});

function sendEmail(){
	var authCode = $("#authCode").val();
	var type = $("#type").val();
	var random = $("#random").val();
	var userEmail = $("#userEmail").val();

	app.forgotUserPass(authCode, type, random, userEmail, function(result) {
		randomImg();
		if (result && result.status==2) {
			$("#authCode").val("");
			$("#userEmail").val("");
			successDialog("我们已经向"+userEmail+"发送了密码重置邮件, 请注意查收。 ");	
		} else {
			errorDialog(result.message);
		}
	});
}

function successDialog(message) {
	$("<div id=\"successDiv\"/>").alertDialog({
		"title" : "提示",
		"dialogClass" : "ui-dialog-user",
		"message": message,
		"type": "success",
		"actions": ["${LANG['system.ok']}"]
	});
}

function errorDialog(message) {
	$("<div id='errorDiv'/>").alertDialog({
		"title": "${LANG['system.tip']}",
		"dialogClass" : "ui-dialog-user",
		"message" : message,
		"type": "error",
		"actions": ["${LANG['system.close']}"]
	});
}

function back(){
	window.location.href = "/";
}
</script>


