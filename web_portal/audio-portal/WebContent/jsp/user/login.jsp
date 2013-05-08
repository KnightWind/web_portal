<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>

<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom.js?ver=${version}"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js?ver=${version}"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js?ver=${version}"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></SCRIPT>
<script type="text/javascript">
$(function() {
	var sessionFlag = "${userSessionFlag}";
	if(sessionFlag && sessionFlag=="true"){
		reloadPage();
	}
	$('.placeValid').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 0.65 });
	fillInUserName();
	$("#loginName").watermark('登录名');
	$("#loginPass").watermark('密码');
	$("#authCode").watermark('验证码');
	
	$("#authCodeImg").width(66);
	$("#authCodeImg").height(22);
	randomImg();
	
    $('#userLoginForm').validate({
    	onkeyup: false,
    	errorClass: "warning",
        rules: {
            'loginName' : {required:true},
            'loginPass' : {required:true},
            'authCode'  : {required:true}
        },
		submitHandler: function(form) {
			userlogin();
		},
        messages: {
            'loginName' : {required:'用户名必须填'},
            'loginPass' : {required:'密码必须填'},
            'authCode'  : {required:'验证码必须填'}
        },

        success: function (label) {
            $(label).each(function () {
                $('#' + this.htmlFor).tipsy('hide').removeAttr('title');
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

function reloadPage() {
    parent.reloadPage();
}

function randomImg() {
	var random = new Date().getTime();
	$("#random").val(random);
	$("#authCodeImg").attr("src", "/valid/image?type=userlogin&random="+random);
}

function errorDialog(message) {
	$("<div/>").errorDialog({
		"title": "${LANG['system.tip']}",
		"dialogClass" : "ui-dialog-smile",
		"message" : message,
		"actions": ["${LANG['system.close']}"]
	});
}
function successDialog(message) {
	$("<div/>").successDialog({
		"title": "${LANG['system.tip']}",
		"dialogClass" : "ui-dialog-smile",
		"message" : message,
		"actions": ["${LANG['system.close']}"]
	});
}

function userlogin() {
	var userBase = {};
	userBase.loginName = $("#loginName").val();
	userBase.loginPass = $("#loginPass").val();
	userBase.authCode = $("#authCode").val();
	userBase.type = $("#type").val();
	userBase.random = $("#random").val();
	app.userLogin(userBase, function(result) {
		if(result) {
			if(result.status==10){
				rememberMe();
				parent.location.href = "/";
				closeDialog();
			} else {
				randomImg();
				parent.errorDialog(result.message);
			}
		}
	});
}
function loaded() {
	var frame = parent.$("#loginDialog");
	frame.trigger("loaded");
}

function closeDialog(result) {
	var dialog = parent.$("#loginDialog");
	if(result){
		dialog.trigger("closeDialog", [result]);
	} else {
		dialog.trigger("closeDialog");	
	}
}

function rememberMe() {
	var checkRemember = $("#remember_me").attr("checked");
	if(checkRemember){
		var userName = $("#loginName").val();
		if(userName){
			$.cookie("userName", userName, { expires: 7});
		}	
	} else {
		$.cookie("userName", null);
	}
}
function fillInUserName() {
	var userName = $.cookie("userName");
	if(userName){
		$("#loginName").val(userName);
		$("#remember_me").attr("checked", "checked");
	}
}
function checkRemember() {
	var checkRemember = $("#remember_me").attr("checked");
	if(!checkRemember){
		$("#remember_me").attr("checked", "checked");
	} else {
		$("#remember_me").removeAttr("checked");
	}
}
</script>
<title>登录</title>
</head>

<body onload="loaded()">
<form name="userLoginForm" id="userLoginForm" action="/user/login/check" method="post">
	<input type="hidden" name="random" id="random" value=""/>
	<input type="hidden" name="type" id="type" value="userlogin"/>
<div class="add19_main">
  <div class="add19_top">
    <div class="add19_top_left"></div>
    <div class="add19_top_center"></div>
    <div class="add19_top_right"></div>
  </div>
  <div class="add19_center">
    <div class="add19_center_left"></div>
    <div class="add19_center_center"> 
      <!--login-->
      <div class="logo_main">
        <div class="login_head"> <img class="login_heade_left" src="${baseUrlStatic}/images/user_login.png" width="36" height="29" align="absmiddle" />
          <h2>用户登录</h2>
        	<a class="dialog-close-button" style="float: right;margin-top:6px;margin-right:8px;" href="javascript:closeDialog();">&nbsp;</a>  
		</div>
        <div class="login_main">
          <div class="login_main_left">
          	<c:if test="${!empty siteBase.siteLogo}">
          		<img class="login_main_left_logo" src="${siteBase.siteLogo}" height="48" />
          	</c:if>
          	<c:if test="${empty siteBase.siteLogo}">
          		<img class="login_main_left_logo" src="${baseUrlStatic}/images/logo.png" height="48" />
          	</c:if>
          	<c:if test="${!empty siteBase}">
            	<h3 class="login_main_left_name">${siteBase.siteName}</h3>
          	</c:if>
          	<c:if test="${empty siteBase}">
            	<h3 class="login_main_left_name"><img src="${baseUrlStatic}/images/user_name.png" width="138" height="25" /></h3>
          	</c:if> 
          </div>
          <div class="login_main_right">
            <table>
            
              <tr>
                <td colspan="3">
                <div class="text_boder">
                	<b class="text_boder_img"><img src="${baseUrlStatic}/images/admin_bg.png" width="13" height="16" /></b>
                	<input class="placeholder placeValid" name="loginName" id="loginName" type="text" watermark="用户名" tabindex="1"/>
<!--                 	<input class="placeholder" name="" type="text" value="登录名" onfocus="this.value=' '; this.onfocus=null;" /> -->
                    <!--<label>登录名</label>-->
                </div>
                </td>
                </tr>
                
                <tr>
                <td colspan="3">
                <div class="text_boder01">
                	<b class="text_boder_img"><img src="${baseUrlStatic}/images/password_bg.png" width="13" height="16" /></b>
<!--                 	<input class="placeholder01" name="" type="text" value="密码" onfocus="this.value=' '; this.onfocus=null;" /> -->
                	<input class="placeholder01 placeValid" name="loginPass" id="loginPass" type="password" watermark="密码" tabindex="2"/>
                    <!--<label>登录名</label>-->
                </div>
                </td>
                </tr>
                
                <tr>
                <td>
                <div class="text_boder02">
                	<b class="text_boder_img"><img src="${baseUrlStatic}/images/identifying_code.png" width="15" height="16" /></b>
<!--                 	<input class="placeholder02" name="" type="text" value="验证码" onfocus="this.value=' '; this.onfocus=null;" /> -->
						<input class="placeholder02 placeValid" name="authCode" type="text" id="authCode" maxlength="4" watermark="验证码" tabindex="3"/>
                    <!--<label>登录名</label>-->
                  </div>
                </td>
                <td align="left">
<!--                 	<img class="text_boder02_code" src="${baseUrlStatic}/images/identifying_code_img.png" width="67" height="22" align="absmiddle" /> -->
                	<img  class="text_boder02_code" align="absmiddle" id="authCodeImg" class="yanzheng" alt="刷新" src=""  width="0" height="0" onclick="randomImg()"/>
                </td>
                </tr>
                
                <tr>
                  <td colspan="3">
                  	<ul class="text_boder03">
                  		
                      <li><input id="remember_me" name="remember_me" type="checkbox" value="" /><span onclick="checkRemember()" style="cursor: pointer;">记住用户名</span></li>
                      
<!--                       <li><input class="text_boder03_checkbox" name="" type="checkbox" value="" />SSL安全登录</li> -->
                      
                      <li><a href="/user/password/forget" target="_blank">忘记密码？</a></li>
                      
                      </ul>
                  </td>
                </tr>
                
                <tr>
                	<td colspan="3">
<!--                 		<a class="login_type" href="#">登&nbsp;&nbsp;录</a> -->
                		<input class="login_type"  type="submit" class="denglu" name="loginBtn" value="登录"/>
                	</td>
                </tr>
                
              
            </table>
          </div>
        </div>
      </div>
      <!--login--> 
    </div>
    <div class="add19_center_right"></div>
  </div>
  <div class="add19_bottom">
    <div class="add19_bottom_left"></div>
    <div class="add19_bottom_center"></div>
    <div class="add19_bottom_right"></div>
  </div>
</div>	
</form>
</body>
</html>
