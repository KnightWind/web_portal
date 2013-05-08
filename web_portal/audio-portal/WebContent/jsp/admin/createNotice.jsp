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
	$(document).ready(function(){
		$('#noticeForm :input').tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 1 });
		var ruleString = {
				required: {
					"title": "${LANG['system.notice.title.input']}",
					"contents": "${LANG['system.notice.contents.input']}",
					"stopTime": "${LANG['system.notice.stoptime.input']}"
				},
				rangelength: {
					"title": "${LANG['system.notice.title.rangelength']}"
				},
				custom: {
					"dateISO": "${LANG['system.notice.date.dateISO']}"
				}
		};
		var v = $("#noticeForm").validate({
			onkeyup: false,
			errorClass: "warning",
			submitHandler: function(form) {
				var frame = parent.$("#noticeDiv");
				var noticeID = "${notice.id}";
				var notice = {};
				notice.title = $("#title").val();
				notice.content = $("#content").val();
				if (noticeID && noticeID.length>0) {
					notice.id = noticeID;
					app.updateSiteNotice(notice, function(result) {
						frame.trigger("closeDialog", [result]);
					},{message:"${LANG['system.site.change']}", ui:parent});
				} else {
					app.createSiteNotice(notice, function(result) {
						frame.trigger("closeDialog", [result]);
					},{message:"${LANG['system.site.save']}", ui:parent});	
				}
			},
			rules: {
	            'title' : {required:true, rangelength: [2, 32]},
	            'content' : {required:true}
	        },
	        messages: {
	            'title' : {required: ruleString.required.title, rangelength: ruleString.rangelength.title},
	            'content' : {required: ruleString.required.contents}
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
			var frame = parent.$("#noticeDiv");
			frame.trigger("closeDialog");
		});
	});	
	</script>
</head>
<body onload="loaded()" style="font-size: 12px;padding: 10px 15px; margin: 0px;">
	<form id="noticeForm" name="loginform" action="">
		<table align="center" cellpadding="4" cellspacing="0" border="0px">
			<tr>
				<td colspan="2">
						&nbsp;
				</td>
			</tr>		
			<tr>
				<td align="right" width="60px"><span class='red_star'>*</span>${LANG['system.notice.list.Title']}</td>
				<td><input id="title" name="title" type="text" class="form-input" value="${notice.title }"/></td>
			</tr>
			<tr>
				<td align="right" width="60px" valign="top"><span class='red_star'>*</span>${LANG['system.notice.list.Contents']}</td>
				<td>
					<textarea id="content" name="content" style="width:520px;height:250px;resize:none"><c:out value="${notice.content }" /></textarea>
				</td>
			</tr>
			<td colspan="2" align="center">
				<input name="submit" type="submit"  value="${LANG['system.ok']}" class="form-button" id="submitForm"/>
				<input name="submit" type="button"  value="${LANG['system.cancel']}" class="form-button closeButton"/>
			</td>
		</table>
	</form>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#noticeDiv");
	frame.trigger("loaded");
}
</script>