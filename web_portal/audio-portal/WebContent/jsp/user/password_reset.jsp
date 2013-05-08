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
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom.js?ver=${version}"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js?ver=${version}"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js?ver=${version}"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></SCRIPT>
<script type="text/javascript">
	var passwordResetForm = null;
	$(document).ready(function(){
		$('#passwordResetForm :input').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 0.65 });
		var ruleString = {
				required: {
					"lp": "${LANG['valid.input.loginPass']}",
					"clp": "${LANG['valid.input.passConfirm']}",
					"authCode": "${LANG['valid.input.passAuthCode']}"
				},
				rangelength: {
					"lp": "${LANG['valid.rangelength616']}",
					"authCode": "${LANG['valid.rangelength4']}"
				},
				custom: {
					"equalTo": "${LANG['valid.password.confirm']}"
				}
		};
		passwordResetForm = $("#passwordResetForm").validate({
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
	function checkForm() {
		if(passwordResetForm.form()) {
			resetPass();
		}
	}
	</script>
<title>修改密码</title>

</head>

<body>
<!--页面头部开始-->
<jsp:include page="header.jsp" />

<div id="head_bar">
  <div class="nav_profile"><img src="/static/images/password_bg.png" width="13" height="16" align="absmiddle" /><a href="#"><span>修改密码？</span></a> </div>
  <!--<ul class="nav_help">
    <li class="bar01" align="absmiddle"><a  title="下载中心" href="#">下载中心</a> </li>
    <li class="bar02"><a  title="帮助" href="#">帮助</a> </li>
    <li class="bar03"><a  title="退出" href="#">退出</a> </li>
  </ul>-->
</div>
<!--忘记密码-->
<div class="Forget_password ">

<!--页面头部结束-->     
<div class="main">
  
<form action="/user/password/save" name="passwordResetForm" id="passwordResetForm" method="post">
	<input type="hidden" name="type" id="type" value="resetpass"/>
	<input type="hidden" name="random" id="random" value=""/>
	<input type="hidden" name="sid" id="sid" value="${sid}"/>
	<input type="hidden" name="auth" id="auth" value="${auth}"/>
	<input type="hidden" name="ts" id="ts" value="${ts}"/>
	<input type="hidden" name="ln" id="ln" value="${ln}"/>
<ul>
    	<li><span>密码：</span><input id="lp" name="lp" class="text01" type="password" /></li>
    	<li><span>确认密码：</span><input id="clp" name="clp" class="text01" type="password" /></li>
    	<li><span>验证码：</span><input id="authCode" name="authCode" type="text" class="text04"/>
                    <img id="authCodeImg" class="yanzhengma" alt="刷新" src=""  width="0" height="0" style="border: 1px solid #bbbbbb;" onclick="randomImg()"/>
                    &nbsp;&nbsp;<a class="change" href="javascript:;" onclick="randomImg()">看不清？换一张</a>
   	    </li>
	</ul>
	<div class="btnn">
    	<a href="javascript:" class="bt01" onclick="javascript:checkForm();">提交</a>
        <a href="javascript:" class="bt02">取消</a>
     
    </div>
<%--
	<input name="button01" class="button01" type="button" value="提交"  onclick="javascript:resetPass();" onmouseover="this.className='Btn_Hover02'" onmouseout="this.className='Btn02'"/>
    <input name="button02" class="button02" type="button" value="取消" onmouseover="this.className='Btn_Hover'" onmouseout="this.className='Btn'">
    --%>
</form>  
</div>        
</div>

<!--页面下部-->
<div id="copy_close">
<jsp:include page="footer.jsp" />
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
		randomImg();
		if (result && result.status==2) {
			$("#lp").val("");
			$("#clp").val("");
			$("#authCode").val("");
			successDialog("密码修改成功");
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
	$("#authCodeImg").attr("src", "/valid/image?type=resetpass&random="+random);
}

$(function() {
	randomImg();
	$("#authCodeImg").width(66);
	$("#authCodeImg").height(22);
	
});
</script>