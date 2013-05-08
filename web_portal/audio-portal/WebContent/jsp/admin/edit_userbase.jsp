<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>

<cc:siteList var="EMPOWER_CODE_FIELD_LIST"/>
<cc:siteList var="EMPOWER_ENABLED"/>
<cc:siteList var="EMPOWER_CODE_VIDEO"/>
<cc:siteList var="EMPOWER_CODE_AUDIO"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加企业用户</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/popupbox.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/tipsy-master/src/stylesheets/tipsy.css" />
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">	
	<script type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></script>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
	<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/jquery.uniform/jquery.uniform.js"></script>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
	<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js"></SCRIPT>
	<script type="text/javascript">
	$(document).ready(function(){
		$("#saveUserForm").find("input, select, textarea").not(".skipThese").uniform();
		$('#saveUserForm :input').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 1 });
		var ruleString = {
				required: {
					"loginName": "${LANG['system.sysUser.loginName.input']}",
					"trueName": "${LANG['system.sysUser.trueName.input']}",
					"oldPass": "${LANG['system.user.oldPass.input']}",
					"loginPass": "${LANG['system.sysUser.loginPass.input']}",
					"confirmPass": "${LANG['system.user.confirmPass.input']}",
					"enName": "${LANG['system.sysUser.enName.input']}",
					"email": "${LANG['system.sysUser.email.input']}"
				},
				maxlength: {
					"remark": "${LANG['system.sysUser.remark.maxlength']}"
				},
				rangelength: {
					"loginName": "${LANG['system.sysUser.loginName.rangelength']}",
					"trueName": "${LANG['system.sysUser.trueName.rangelength']}",
					"loginPass": "${LANG['system.sysUser.loginPass.rangelength']}",
					"enname": "${LANG['system.sysUser.enname.rangelength']}",
					"email": "${LANG['system.sysUser.email.rangelength']}",
					"mobile": "${LANG['system.sysUser.mobile.rangelength']}"
				},
				custom: {
					"equalTo": "${LANG['system.user.confirmpass.custom']}",
					"email": "${LANG['system.sysUser.email.custom']}",
					"mobile": "${LANG['system.sysUser.mobile.custom']}",
					"checkLoginName": "${LANG['system.sysUser.checkLoginName.custom']}",
					"checkUserName": "${LANG['system.sysUser.checkUserName.custom']}",
					"checkEnName":"${LANG['system.sysUser.checkEnName.custom']}"
				}
		};
		
		$.validator.addMethod("notRequired", function(value, element) {
			if(value==null || value=="" || value.length==0){
				$(element).tipsy('hide').removeAttr('original-title');
			}
	    	return true;
	 	}, "");
		$.validator.addMethod("noSpace", function(value, element) {
			return value=="" || (value != ""&&value.indexOf(" ") < 0 );
	 	}, "密码中不能含有空格");
		$.validator.addMethod("checkLoginName", function(value, element) {       
	    	return this.optional(element) || /^[a-zA-Z0-9]{4,16}$/.test(value);
	 	}, ruleString.custom.checkLoginName);
		$.validator.addMethod("checkUserName", function(value, element) {       
	    	return this.optional(element) || /^[a-zA-Z0-9_\-&\s\u4e00-\u9fa5]{1,32}$/.test(value);
	 	}, ruleString.custom.checkUserName);	
		$.validator.addMethod("checkEnName", function(value, element) {       
	    	return this.optional(element) || /^[a-zA-Z0-9_\-&\s]{1,32}$/.test(value);
	 	}, ruleString.custom.checkEnName);
		$.validator.addMethod("checkMobile", function(value, element) {       
	    	return this.optional(element) || /(^((\+86)?|\(\+86\)|\+86\s|\+86-)0?1[358]\d{9}$)|(^((\+86)?|\(\+86\)|\+86\s|\+86-)0?([1-9]\d-?\d{6,8}|[3-9][13579]\d-?\d{6,7}|[3-9][24680]\d{2}-?\d{6})(-\d{4})?$)/.test(value);
	 	}, ruleString.custom.checkMobile);
		var v = $("#saveUserForm").validate({
			onkeyup: false,
			errorClass: "warning",
			submitHandler: function(form) {
				saveSysUser();
			},
			rules: {
	            'loginName' : {required:true, rangelength: [4, 16], checkLoginName:true},
	            'trueName' : {required:true, rangelength: [1, 32], checkUserName:true},
	            'enName' : {notRequired:true, rangelength:[1, 32], checkEnName:true},
	            'userEmail' : {required:true, rangelength:[6, 64], email: true},
	            'mobile' : {notRequired:true, rangelength:[4, 32], checkMobile: true},
	            'remark' : {notRequired:true, maxlength: 256}
	        },
	        messages: {
	            'loginName' : {required:ruleString.required.loginName, rangelength: ruleString.rangelength.loginName},
	            'trueName' : {required:ruleString.required.trueName, rangelength: ruleString.rangelength.trueName},
	            'loginPass' : {required:ruleString.required.loginPass, rangelength: ruleString.rangelength.loginPass},
	            'confirmPass' : {required:ruleString.required.confirmPass, rangelength: ruleString.rangelength.loginPass, equalTo:ruleString.custom.equalTo},
	            'oldPass' : {required:ruleString.required.oldPass, rangelength: ruleString.rangelength.loginPass},
	            'enName' : {required:ruleString.required.enName, rangelength: ruleString.rangelength.enName},
	            'userEmail' : {required:ruleString.required.email, rangelength: ruleString.rangelength.email, email: ruleString.custom.email},
	            'mobile': {rangelength: ruleString.rangelength.mobile, checkMobile: ruleString.custom.mobile},
	            'remark': {maxlength: ruleString.maxlength.remark}
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
		
		$(".closeButton").click(function() {
			var frame = parent.$("#userDiv");
			frame.trigger("closeDialog");
		});

		//update passworld required to verify or disable
		var siteId = "${userBase.id}";
		if (siteId && siteId.length>0) {
			$("#loginPass").rules("add", {noSpace: true, notRequired:true, rangelength:[6, 16]});
			$("#confirmPass").rules("add", {noSpace: true, notRequired:true, rangelength:[6, 16], equalTo: '#loginPass'});
		} else {
			$("#loginPass").rules("add", {noSpace: true, required:true, rangelength:[6, 16]});
			$("#confirmPass").rules("add", {noSpace: true, required:true, rangelength:[6, 16], equalTo: '#loginPass'});
		}
	});	
	
	function saveSysUser() {
		var userId = "${userBase.id}";
		var user = {};
		user.loginName = $("#loginName").val();
		user.loginPass = $("#loginPass").val();
		user.trueName = $("#trueName").val();
		user.enName = $("#enName").val();
		user.userEmail = $("#userEmail").val();
		user.mobile = $("#mobile").val();
		user.remark = $("#remark").val();
		user.userRole = $("select[name=userRole]").val();
		user.orgId = $("#orgId").val();
		var func = {};
		<c:forEach var="eachField" items="${EMPOWER_CODE_FIELD_LIST}" varStatus="status"><c:set var="eachFieldName" value="${eachField[1]}"/><c:if test='${sitePower[eachFieldName]==EMPOWER_ENABLED && fn:indexOf(eachFieldName,"Flag")>-1 && eachField[2]==1}'>
		func.${eachFieldName} = $("input[name=${eachFieldName}]:checked").length>0?1:0;
		</c:if></c:forEach>
		//func.phoneFlag = $("input[name=phoneFlag]:checked").length>0?1:0;
		//func.autoFlag = $("input[name=autoFlag]:checked").length>0?1:0;
		
		//func.shareMediaFlag = $("input[name=shareMediaFlag]:checked").length>0?1:0;
		//func.recordFlag = $("input[name=recordFlag]:checked").length>0?1:0;
		//var orgPass = $("#oldPass").val();
		if (userId && userId.length>0) {
			user.id = userId;
			app.updateSiteUser(user,func, function(result) {
				var frame = parent.$("#userDiv");
				frame.trigger("closeDialog", [result]);
			}, {message:"${LANG['system.site.change']}", ui:parent});
		} else {
			app.createSiteUser(user,func, function(result) {
				var frame = parent.$("#userDiv");
				frame.trigger("closeDialog", [result]);
			}, {message:"${LANG['system.site.save']}", ui:parent});	
		}
	}
	
	function showFunc(obj){
		if(obj.value==1){
			$("#conffuc").show();
		}else{
			$("#conffuc").hide();
		}
	}
	</script>	
</head> 
<body onload="loaded()">
<form id="saveUserForm"  action="" method="post">
     		<input type="hidden" name="id" value="${userBase.id}"/>
			<table class="table_create_system_user">
				  <tr>
				    <td align="right">
				      <label class='red_star'>*</label>${LANG['system.sysUser.list.loginName']}
				    </td>
				    <td class="table-td">
				      <input id="loginName" name="loginName" class="create_system_user_input" type="text" value="${userBase.loginName}"/>
				    </td>
				  </tr>
				  <tr>
				    <td align="right">
				      <label class='red_star'>*</label>${LANG['system.sysUser.list.loginPass']}
				    </td>
				    <td class="table-td">
				      <input id="loginPass" name="loginPass" class="create_system_user_input" type="password"/>
				    </td>
				  </tr>
				  <tr>
				    <td align="right">
				      <label class='red_star'>*</label>${LANG['system.user.confirmPass']}
				    </td>
				    <td class="table-td">
				      <input id="confirmPass" name="confirmPass" class="create_system_user_input" type="password"/>
				    </td>
				  </tr>
				  <tr>
				    <td align="right">
				      <label class='red_star'>*</label>${LANG['system.sysUser.list.userName']}
				    </td>
				    <td class="table-td">
				      <input id="trueName" name="trueName" class="create_system_user_input" type="text" value="${userBase.trueName}"/>
				    </td>
				  </tr>
				  <tr>
				    <td align="right">
				      ${LANG['system.sysUser.list.enName']}
				    </td>
				    <td class="table-td">
				      <input id="enName" name="enName" class="create_system_user_input" type="text" value="${userBase.enName}"/>
				    </td>
				  </tr>
				  
				  <tr>
				    <td align="right">
				      	用户机构
				    </td>
				    <td class="table-td">
				       <select name="orgId" id="orgId">
                           <option <c:if test="${!empty userBase && userBase.orgId eq '0'}">selected</c:if> value="0" >无</option>
                           <c:forEach var="org" items="${orgList}" >
                              <option <c:if test="${!empty userBase && userBase.orgId eq org.id}">selected</c:if> value="${org.id}" >${org.orgName}</option>
                           </c:forEach>
                       </select>
				    </td>
				  </tr>
				  
				  <c:choose>
					<c:when test="${site.chargeMode eq 1}">
						<input type="hidden" name="userRole" value="2"/>
					</c:when>
					<c:otherwise>
						<tr>
						    <td align="right">
						      ${LANG['site.admin.edituser.userrole']}
						    </td>
						    <td class="table-td">
						      <select name="userRole" onchange="showFunc(this);">
				               	<option value="1" <c:if test="${userBase.userRole eq '1'}">selected="selected"</c:if>>${LANG['site.admin.edituser.role1']}</option>
						      	<option value="2" <c:if test="${empty userBase || userBase.userRole eq '2'}">selected="selected"</c:if>>${LANG['site.admin.edituser.role2']}</option>
				              </select>
						    </td>
						</tr>
						<c:if test="${sitePower != null}"> 
						<tr id="conffuc" <c:if test="${empty userBase || userBase.userRole eq '2'}">style="display:none"</c:if> >
						    <td align="right">
						      会议功能
						    </td>
						    <td class="table-td">
						    <%--
						    <c:forEach var ="eachField" items="${EMPOWER_CODE_FIELD_LIST}" varStatus="status">
						    <c:set var="eachFieldName" value="${eachField[1]}"/>
						    </c:forEach>
						    <c:if test="${eachField[0]==EMPOWER_CODE_VIDEO || eachField[0]==EMPOWER_CODE_AUDIO }"><c:set var="tmpNum" value="${tmpNum+1 }"/></c:if>
						    --%>
						    <table width="100%" border="0" >
								<tr><c:set var="modNum" value="0"/><c:set var="modMax" value="4"/><c:set var="tmpNum" value="0"/>
								<c:forEach var="eachField" items="${EMPOWER_CODE_FIELD_LIST}" varStatus="status">
									<c:set var="eachFieldName" value="${eachField[1]}"/><c:set var="modNum" value="${tmpNum % modMax }"/>
									<c:if test="${modNum ==0  && tmpNum > 0}"></tr><tr><c:set var="tmpNum" value="0"/></c:if>
									<c:if test='${sitePower[eachFieldName]==EMPOWER_ENABLED && fn:indexOf(eachFieldName,"Flag")>-1 && eachField[3]==1}'>
									<td>
									<input class="extraConfig" name="${eachFieldName}" id="${eachFieldName}" type="checkbox" value="${EMPOWER_ENABLED}"  <c:if test="${userConfig[eachFieldName]==EMPOWER_ENABLED && userConfig.userId > 0 }"> checked </c:if> />
									<label><c:set var="langName" value="system.site.empower.item.${eachField[0]}"/>${LANG[langName]}</label>
									</td>
									<%--<c:if test="${eachField[0]==EMPOWER_CODE_VIDEO }"><td id="videoNumTd"></td> </c:if>--%>
									<c:set var="tmpNum" value="${tmpNum+1}"/>
									</c:if>
								</c:forEach>
								<c:if test="${modNum>0}">
								<c:forEach begin="${modNum+1 }" end="${modMax}" >
									<td>&nbsp;</td>
								</c:forEach>
								</c:if>
								</tr>
							</table>
<%-- 						      <input type="checkbox" <c:if test="${config.phoneFlag eq 1 and siteConfig.phoneFlag eq 1}">checked</c:if> <c:if test="${siteConfig.phoneFlag eq 0}">disabled="disabled"</c:if> value="1" name="phoneFlag"/>电话&nbsp; 
 						      <input type="checkbox" value="1" name="autoFlag" <c:if test="${config.autoFlag eq 1 and siteConfig.autoFlag eq 1}">checked</c:if> <c:if test="${siteConfig.autoFlag eq 0}">disabled="disabled"</c:if> />自动外呼&nbsp; 
 						      <input type="checkbox" value="1" name="shareMediaFlag" <c:if test="${config.shareMediaFlag eq 1 and siteConfig.shareMediaFlag eq 1}">checked</c:if> <c:if test="${siteConfig.shareMediaFlag eq 0}">disabled="disabled"</c:if> />媒体共享&nbsp; 
 						      <input type="checkbox" value="1" name="recordFlag"<c:if test="${config.recordFlag eq 1 and siteConfig.recordFlag eq 1}">checked</c:if> <c:if test="${siteConfig.recordFlag eq 0}">disabled="disabled"</c:if>/>录制 
 						    
							  <c:if test="${siteConfig.phoneFlag eq 1}"> 
						      <input type="checkbox" <c:if test="${config.phoneFlag eq 1 and siteConfig.phoneFlag eq 1}">checked</c:if> value="1" name="phoneFlag"/>电话&nbsp;
						      </c:if>
						      <c:if test="${siteConfig.autoFlag eq 1}">
						      <input type="checkbox" value="1" name="autoFlag" <c:if test="${config.autoFlag eq 1 and siteConfig.autoFlag eq 1}">checked</c:if>  />自动外呼&nbsp;
						      </c:if>
						      <c:if test="${siteConfig.shareMediaFlag eq 1}">
						      <input type="checkbox" value="1" name="shareMediaFlag" <c:if test="${config.shareMediaFlag eq 1 and siteConfig.shareMediaFlag eq 1}">checked</c:if>  />媒体共享&nbsp;
						      </c:if>
						      <c:if test="${siteConfig.recordFlag eq 1}">
						      <input type="checkbox" value="1" name="recordFlag"<c:if test="${config.recordFlag eq 1 and siteConfig.recordFlag eq 1}">checked</c:if> />录制
						      </c:if>
 						      --%>
						    </td>
						</tr>
						</c:if>
					</c:otherwise>
				  </c:choose>
				  <tr>
				    <td align="right">
				      <label class='red_star'>*</label>${LANG['system.sysUser.list.email']}
				    </td>
				    <td class="table-td">
				      <input id="userEmail" name="userEmail" class="create_system_user_input" type="text" value="${userBase.userEmail}"/>
				    </td>
				  </tr>
				  <tr>
				    <td align="right">
				      ${LANG['system.sysUser.list.telephone']}
				    </td>
				    <td class="table-td">
				      <input id="mobile" name="mobile" class="create_system_user_input" type="text" value="${userBase.mobile}"/>
				    </td>
				  </tr>
				  <tr>
				    <td align="right" valign="top">
				      <div style="margin-top: 5px">${LANG['system.sysUser.list.remark']}</div>
				    </td>
				    <td class="table-td" style="height: 65px;">
				      <textarea id="remark" name="remark" style="width: 261px; height:50px;resize:none;">${userBase.remark}</textarea>
				    </td>
				  </tr>
				</table>
				<div style="width: 260px; margin: 0px auto">
					<input name="emile_button" class="create_system_user_button skipThese" type="submit"  value="${LANG['system.submit']}" />
					<input name="emile_button02"  class="create_system_user_button closeButton skipThese" type="button"  value="${LANG['system.cancel']}" style="margin-left: 100px" />
				</div>     		
     	</form>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#userDiv");
	frame.trigger("loaded");
}
</script>
