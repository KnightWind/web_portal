<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<TITLE>${siteBase.siteName}--${LANG['bizconf.jsp.admin.index.res1']}</TITLE>

<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/style.css"/>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.uniform/jquery.uniform.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js"></SCRIPT>

<script type="text/javascript">

	jQuery(function($) {
		var needResetPass = "${needResetPass}";
		if(needResetPass != null && needResetPass =="true"){
			resetPass();
		}
	});

	$(document).ready(function() {
		$("#langForm").find("select").not(".skipThese").uniform();
		$("#langForm select").change(function () {
			var lang = $(this).val();
		//	console.log("lang="+lang);
			changeLang(lang);
		});
		//left menu 
		$('.nav span').click(function() {
			var selectText = $(this).text();
			var ul = $(this).next();
			if (ul.is(":visible")) {
				$(this).attr("style", "background:url('/static/images/nav_bg_change.jpg') no-repeat scroll 0 0 transparent");
				ul.slideUp();
			} else {
				$(this).attr("style", "background:url('/static/images/nav_li_bg.jpg') no-repeat scroll 0 0 transparent");
				ul.slideDown();
			}
		});
		//left menu selected
		$('.sub_nav a').click(function() {
			$('.sub_nav').find("a").removeClass("li_actived");
			$(this).addClass("li_actived");
		});
		//left menu hover
		$('.sub_nav a').hover(function() {
			$(this).addClass("li_ahoverd");
		}, function() {
			$(this).removeClass("li_ahoverd");
		});
		
		$("#mainFrame").load(function(){
			resizeHeight();  
	    });
		$("body").bind(EVENT_CREATE, function(event, result, type) {
			if(type==VIEW_TYPE.siteAdminUser) {
				successDialog(result.userBase[0]);
				showURL("/admin/entUser/list");
			}   else if (type==VIEW_TYPE.siteUser) {
				successDialog(result.userBase[0]);
				showURL("/admin/entUser/listAll");
			}  else if(type==VIEW_TYPE.site){
				siteCreateSuccess(result);
				showURL("/admin/site/list");
			} else if(type==VIEW_TYPE.notice) {
				successDialog(result.message);
				showURL("/admin/notice/list");
			} else if(type==VIEW_TYPE.organiz) {
				successDialog("${LANG['bizconf.jsp.admin.index.res2']}");
// 				showURL("/admin/org/orgListIndex");
// 				$("#mainFrame")[0].contentWindow.addOrg(result);
				$("#mainFrame")[0].contentWindow.refreshIframe();
			} else if(type==VIEW_TYPE.assignUser) {
				successDialog("${LANG['bizconf.jsp.admin.index.res3']}");
// 				showURL("/admin/org/orgList");
				$("#mainFrame")[0].contentWindow.assignUser();
			} else if(type==VIEW_TYPE.relateOrg) {
				//console.log(result);
			}
		});
		$("body").bind(EVENT_UPDATE, function(event, result, type) {
			if (type==VIEW_TYPE.siteAdminUser) {
				successDialog(result.userBase[0]);
				showURL("/admin/entUser/list");
			}  else if (type==VIEW_TYPE.siteUser) {
				successDialog(result.userBase[0]);
				showURL("/admin/entUser/listAll");
			}  else if(type==VIEW_TYPE.site){
				siteUpdateSuccess(result);
				showURL("/admin/site/list");
			} else if (type==VIEW_TYPE.notice) {
				successDialog(result.message);
				showURL("/admin/notice/list");
			} else if(type==VIEW_TYPE.organiz) {
				successDialog("${LANG['bizconf.jsp.admin.index.res4']}");
// 				showURL("/admin/org/orgList");
// 				$("#mainFrame")[0].contentWindow.updateOrg(result);
				$("#mainFrame")[0].contentWindow.refreshIframe();
			}
		});
		$("body").bind(EVENT_DELETE, function(event, result) {
			//delete success
		});

		$("body").bind(EVENT_ERROR, function(event, result, type) {
			var message = result.message;
			errorDialog(message);
		});
		$("body").bind(EVENT_SEND_EMAIL, function(event, siteInfo, userInfo) {
			app.sendNoticeEmail(siteInfo, userInfo);
		});		
	});
	
	//create or update notice
	function createOrUpdateNotice(id) {
		if (id) {
			$("<div id=\"noticeDiv\"/>").showDialog({
				"title" : "${LANG['system.notice.list.Update']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/admin/notice/update/" + id,
				"type" : VIEW_TYPE.notice,
				"action" : ACTION.update,
				"width" : 680,
				"height" : 400
			});
		} else {
			$("<div id=\"noticeDiv\"/>").showDialog({
				"title" : "${LANG['system.notice.list.Create']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/admin/notice/create",
				"type" : VIEW_TYPE.notice,
				"action" : ACTION.create,
				"width" : 680,
				"height" : 400
			});
		}
	}
	
	function relateOrg(){
		$("<div id=\"relateOrgDiv\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res5']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/admin/org/relateOrg",
			"type" : VIEW_TYPE.relateOrg,
			"action" : ACTION.create,
			"width" : 350,
			"height" : 300
		});
	}
	
	/*
	 * ${LANG['bizconf.jsp.admin.index.res6']}
	 */
	function resetPass(){
		$("<div id=\"resetPass\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res7']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/admin/resetPass",
			"type" : VIEW_TYPE.notice,
			"action" : ACTION.view,
			"width" : 474,
			"height" : 220,
			"hideClose": true
		});
		
	}
	
	//create or update org
	function updateOrg(id) {
		$("<div id=\"organizDiv\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res8']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/admin/org/update/" + id,
			"type" : VIEW_TYPE.organiz,
			"action" : ACTION.update,
			"width" : 407,
			"height" : 200
		});
	}
	
	function createOrg(pid) {
		$("<div id=\"organizDiv\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res9']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/admin/org/add?pId="+pid,
			"type" : VIEW_TYPE.organiz,
			"action" : ACTION.create,
			"width" : 407,
			"height" : 200
		});
	}
	//${LANG['bizconf.jsp.admin.index.res10']}
	function getOrgUserList(id) {
		$("<div id=\"assignUserDiv\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res11']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/admin/org/getOrgUserList/" + id,
			"type" : VIEW_TYPE.assignUser,
			"action" : ACTION.create,
			"width" : 750,
			"height" : 480
		});
	}
	//view notice info
	function viewNotice(id) {
		$("<div id=\"previewNoticeDiv\"/>").showDialog({
			"url" : "/admin/notice/view/" + id,
			"dialogClass" : "ui-dialog-preview",
			"actions" : ["${LANG['system.close']}"],
			"width" : 677,
			"height" : 564
		});
	}
	//preview notice info
	function previewNotice(data) {
		$("<div id=\"previewNoticeDiv\"/>").showDialog({
			"url" : "/admin/notice/preview",
			"dialogClass" : "ui-dialog-preview",
			"data" : data,
			"actions" : ["${LANG['system.close']}"],
			"width" : 677,
			"height" : 564
		});
	}
	//create or update admin user
	function createOrUpdateAdminUser(id) {
		if(id) {
			$("<div id=\"userDiv\"/>").showDialog({
				"title": "${LANG['system.admin.index.updateadmin']}",
				"url" : "/admin/entUser/toEditUser?id=" + id,
				"dialogClass" : "ui-dialog-smile",
				"type" : VIEW_TYPE.siteAdminUser,
				"action" : ACTION.update,
				"width" : 450,
				"height" : 410
			});
		} else {
			$("<div id=\"userDiv\"/>").showDialog({
				"title": "${LANG['system.admin.index.createadmin']}",
				"url" : "/admin/entUser/toEditUser",
				"dialogClass" : "ui-dialog-smile",
				"type" : VIEW_TYPE.siteAdminUser,
				"action" : ACTION.create,
				"width" : 450,
				"height" : 410
			});
		}
	}	
	//create or update site user
	function createOrUpdateSiteUser(id) {
		if(id) {
			$("<div id=\"userDiv\"/>").showDialog({
				"title": "${LANG['system.admin.index.updateuser']}",
				"url" : "/admin/entUser/toEditUserBase?id=" + id,
				"dialogClass" : "ui-dialog-smile",
				"type" : VIEW_TYPE.siteUser,
				"action" : ACTION.update,
				"width" : 500,
				"height" : 545
			});
		} else {
			$("<div id=\"userDiv\"/>").showDialog({
				"title": "${LANG['system.admin.index.createuser']}",
				"url" : "/admin/entUser/toEditUserBase",
				"dialogClass" : "ui-dialog-smile",
				"type" : VIEW_TYPE.siteUser,
				"action" : ACTION.create,
				"width" : 500,
				"height" : 545
			});
		}
	}
	function siteCreateSuccess(data) {
		$("<div id=\"siteSuccessDiv\"/>").showDialog({
			"url" : "/system/site/siteCreateSuccess/" + data.siteBase[0].id,
			"dialogClass" : "ui-dialog-smile",
			"width" : 480,
			"height" : 350			
		});
	}
	function siteUpdateSuccess(data) {
		$("<div id=\"siteSuccessDiv\"/>").showDialog({
			"url" : "/system/site/siteUpdateSuccess/" + data.siteBase[0].id,
			"dialogClass" : "ui-dialog-smile",
			"width" : 480,
			"height" : 350
		});
	}

	
	/*import users*/
	function importUser() {
		$("<div id=\"importDiv\"/>").showDialog({
			"title":"${LANG['site.admin.title.importuser']}",
			"url" : "/admin/entUser/goImport",
			"dialogClass" : "ui-dialog-smile",
			"width" : 420,
			"height" : 120
		});
	}
	
	function viewUser(id) {
		$("<div id=\"viewUserDiv\"/>").showDialog({
			"title":"${LANG['bizconf.jsp.admin.index.res12']}",
			"url" : "/admin/entUser/toViewUserBase?id="+id,
			"dialogClass" : "ui-dialog-smile",
			"width" : 370,
			"height" : 360
		});
	}

	function warningDialog(message) {
		$("<div/>").warningDialog({
			"title": "${LANG['system.tip']}",
			"dialogClass" : "ui-dialog-smile",
			"message" : message,
			"actions": ["${LANG['system.close']}"]
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
	function successDialog(message) {
		$("<div/>").successDialog({
			"title": "${LANG['system.tip']}",
			"dialogClass" : "ui-dialog-smile",
			"message" : message,
			"actions": ["${LANG['system.close']}"]
		});
	}
	function confirmDialog(message, callback) {
		$("<div/>").confirmDialog({
			"title": "${LANG['system.tip']}",
			"dialogClass" : "ui-dialog-smile",
			"message" : message,
			"actions": ["${LANG['system.ok']}", "${LANG['system.cancel']}"],
			"callback" : callback
		});
	}
	
	function logout() {
		$("<div/>").confirmDialog({
			"title": "${LANG['bizconf.jsp.admin.index.res13']}",
			"dialogClass" : "ui-dialog-smile",
			"message" : "${LANG['bizconf.jsp.admin.index.res14']}?",
			"type": "confirm",
			"actions": ["${LANG['system.ok']}", "${LANG['system.cancel']}"],
			"callback" : function() {
				window.location.href="/admin/logout";
			}
		});
	}
	//refresh Main Iframe Content
	function showURL(url) {
		url+="?t="+ Math.random();
		$('#mainFrame').attr("src", url);
	}
	//resize index body height when search div Collapsing 
	function resizeHeight() {
		$("#mainFrame").height($("#mainFrame").contents().find("html").height()+50);  
	}	
	
	function showImportInfo(info,statu){
		showURL("/admin/entUser/listAll");
		if(statu==1){
			successDialog(info);
		}else{
			errorDialog(info);
		}
	}
	
	function showConflogs(id) {
		$("<div id=\"logview\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res15']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "<%=request.getContextPath()%>/user/conflog/loglist?confId="+id,
			"type" : VIEW_TYPE.group,
			"action" : ACTION.create,
			"width" : 804,
			"height" : 534
		});
	}
	
	function showTelDetail(id) {
		$("<div id=\"billingView\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.index.res8']}",
			"dialogClass" : "ui-dialog-user",
			"url" : "/common/billing/showTelDetail?id="+id,
			"type" : VIEW_TYPE.billing,
			"action" : ACTION.create,
			"width" : 624,
			"height" : 587
		});
	}
	
	function showDataFeeDetail(id) {
		$("<div id=\"dataFeeView\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.index.res8']}",
			"dialogClass" : "ui-dialog-user",
			"url" : "/common/billing/showDataDetail?id="+id,
			"type" : VIEW_TYPE.billing,
			"action" : ACTION.create,
			"width" : 624,
			"height" : 587
		});
	}
	
	function showAttendConfs(id) {
		$("<div id=\"attendconfs\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res16']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "<%=request.getContextPath()%>/user/conflog/attendConflist?userId="+id,
			"type" : VIEW_TYPE.group,
			"action" : ACTION.create,
			"width" : 804,
			"height" : 534
		});
	}
	//${LANG['bizconf.jsp.admin.index.res17']}
	function showOrgUsers(id) {
		$("<div id=\"orgUsersDiv\"/>").showDialog({
			"title" : "${LANG['bizconf.jsp.admin.index.res18']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/admin/org/getOrgSubUserList/" +id,
			"type" : VIEW_TYPE.group,
			"action" : ACTION.create,
			"width" : 804,
			"height" : 420
		});
	}
</script>
</HEAD>


<body style="min-width:1002px;">
<!--${LANG['bizconf.jsp.admin.CopyOfadminIndex.res2']}-->
<jsp:include page="header.jsp" />
<!--${LANG['bizconf.jsp.admin.CopyOfadminIndex.res8']}-->
<div class="main_left" >
 <ul class="nav">
 		<li><span class="nav_top05"><a href="#">${LANG['system.menu.config.user']}</a></span>
          <ul class="sub_nav">
          <c:if test="${isSuperSiteAdmin}">
            <li class="b_line"><a class="li_alink" href="/admin/entUser/list" target="mainFrame">${LANG['company.menu.admin.manage']}</a></li>
          </c:if>
            <li class="b_line"><a class="li_alink li_actived" href="/admin/entUser/listAll" target="mainFrame">${LANG['company.menu.user.manage']}</a></li>
          	<li><a class="li_alink" href="/admin/org/orgListIndex" target="mainFrame">${LANG['bizconf.jsp.admin.index.res19']}</a></li>
          </ul>
        </li>
        <c:if test="${isSuperSiteAdmin}">
        <li><span class="nav_top01"><a href="#">${LANG['system.menu.config']}</a></span>
          <ul class="sub_nav">
            <li class="b_line"><a class="li_alink" href="/admin/email/showhost" target="mainFrame">${LANG['system.menu.config.host']}</a></li>
            <li class="b_line"><a class="li_alink" href="/admin/email/goTemplateEdit" target="mainFrame">${LANG['system.menu.config.template']}</a></li>
            <li><a class="li_alink" href="/admin/config/configinfo" target="mainFrame">${LANG['company.menu.meeting.permissions']}</a></li>
          </ul>
        </li>
        <li><span class="nav_top02"><a  href="#">${LANG['system.menu.site.infoManage']}</a></span>
          <ul class="sub_nav">
            <li><a class="li_alink" href="/admin/site/info" target="mainFrame">${LANG['system.menu.site.infoManage']}</a></li>
          </ul>
        </li>
        <li><span class="nav_top03"><a href="#">${LANG['system.menu.notice.manage']}</a></span>
          <ul class="sub_nav">
            <li><a class="li_alink" href="/admin/notice/list" target="mainFrame">${LANG['system.menu.notice.list']}</a></li>
          </ul>
        </li>
       </c:if>
        <li><span class="nav_top04"><a href="#">${LANG['system.menu.info.list']}</a></span>
          <ul class="sub_nav">
            <li class="b_line"><a class="li_alink" href="/admin/conf/list" target="mainFrame">${LANG['system.menu.info.meeting']}</a></li>
            <li class="b_line"><a class="li_alink" href="/admin/siteAdminLogs/list" target="mainFrame">${LANG['system.menu.info.log.userManager']}</a></li>
            <li><a class="li_alink" href="/admin/siteUserLogs/list" target="mainFrame">${LANG['system.menu.info.log.userOperator']}</a></li>
          </ul>
        </li>
<%--        <li><span class="nav_top04"><a href="#">${LANG['bizconf.jsp.admin.index.res20']}</a></span>--%>
<%--          <ul class="sub_nav">--%>
<%--            <li class="b_line"><a class="li_alink" href="/common/billing/goSiteBilling" target="mainFrame">${LANG['bizconf.jsp.admin.CopyOfadminIndex.res22']}</a></li>--%>
<%--          </ul>--%>
<%--        </li>--%>
        <li><span class="nav_top06"><a href="#">${LANG['system.menu.info.user.manage']}</a></span>
          <ul class="sub_nav">
            <li class="b_line"><a class="li_alink" class="li_alink" href="/admin/profile" target="mainFrame">${LANG['system.menu.info.user.manage']}</a></li>
          </ul>
        </li>
       </ul>    
</div> 
<div class="main_right">
	<iframe frameborder="0" width="100%" height="100%" id="mainFrame" name="mainFrame" scrolling="no" src="/admin/entUser/listAll"></iframe>
</div>
<!--${LANG['bizconf.jsp.admin.CopyOfadminIndex.res24']}-->
 <div id="copy">
<span class="copy_text" >Copyright © 2003-2012 Shanghai Shrine Telecom Co., Ltd. 2012. All rights reserved.Version:eMeeting V5.0</span>
</div>
</body>
</HTML>
<script type="text/javascript">
	function changeLang(langVal){
		var jumpUrl="/changeLang?lang="+langVal+"&returnURL=/admin/";
		window.location.href=jumpUrl;
	}
	function initPage(){
		var useLang="${lang}";
		$("#jumpMenu").val(useLang); 
	}
	initPage();
</script>

