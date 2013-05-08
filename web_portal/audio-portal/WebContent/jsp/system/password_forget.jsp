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
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/m_wangjimima.css"/>

<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/static/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js"></SCRIPT>
<script type="text/javascript">
	$(document).ready(function(){
		$('#passwordForgetForm :input').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 1 });
		var ruleString = {
				required: {
					"email": "请输入邮箱地址",
					"authCode": "请输入验证码"
				},
				rangelength: {
					"email": "字符长度为6~32位",
					"authCode": "字符长度为4位"
				},
				custom: {
					"email": "请输入正确的email格式"
				}
		};
		var v = $("#passwordForgetForm").validate({
			onkeyup: false,
			errorClass: "warning",
			submitHandler: function(form) {
				sendEmail();
			},
			rules: {
	            'systemEmail' : {required:true, rangelength: [6, 32], email: true},
	            'authCode' : {required:true, rangelength: [4, 4]}
	        },
	        messages: {
	            'systemEmail' : {required: ruleString.required.email, rangelength: ruleString.rangelength.email, email:ruleString.custom.email},
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
<title>系统管理员-忘记密码</title>
</head>

<body>

<!--页面头部开始-->
<jsp:include page="header.jsp" />   
<!--页面头部结束-->     
<form action="/system/password/sendEmail" name="passwordForgetForm" id="passwordForgetForm" method="post">
<div class="main">
  <h3>忘记密码？</h3>
  <p>*  输入与eCon账户关联的电子邮件地址。我们将向您发送页面链接，通过该页面您可以轻松创建新密码。</p>
	<input type="hidden" name="random" id="random" value=""/>
	<input type="hidden" name="type" id="type" value="forgetpass"/>
	<ul>
    	<li><span>邮箱地址：</span><input id="systemEmail" name="systemEmail" class="text01" type="text" /></li>
    	<li><span>验证码：</span><input id="authCode" name="authCode" type="text" id="authCode" maxlength="4" class="text02"/>
                    <img id="authCodeImg" class="yanzhengma" alt="刷新" src=""  width="0" height="0" style="border: 1px solid #bbbbbb;" onclick="randomImg()"/>
                    &nbsp;&nbsp;&nbsp;
                    <a class="change" href="javascript:;" onclick="randomImg()">看不清？换一张</a>
   	    </li>
	</ul>
	<div style="clear: both;"></div>
	<input name="button01" class="button01" type="submit" value="提交"  />
    <input name="button02" class="button02" type="button" value="返回"  onclick="javascript:back();" >

</div>        
    </form>   
    <!--页脚版权-->       
<jsp:include page="footer.jsp" />

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
	var systemEmail = $("#systemEmail").val();
	app.forgotPass(authCode, type, random, systemEmail, function(result) {
		randomImg();
		if (result && result.status==2) {
			successDialog("邮件发送成功", function() {
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
function back(){
	window.history.back(-1);
//	$("#passwordForgetForm").submit();
}
</script>
