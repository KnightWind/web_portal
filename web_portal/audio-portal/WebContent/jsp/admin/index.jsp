<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<TITLE>${siteBase.siteName}--站点管理系统</TITLE>

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
				successDialog("新增站点下的组织机构成功");
				showURL("/admin/org/orgList");
			} else if(type==VIEW_TYPE.assignUser) {
				successDialog("分配成功");
				showURL("/admin/org/orgList");
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
				successDialog("修改站点下的组织机构成功");
				showURL("/admin/org/orgList");
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
	//create or update org
	function createOrUpdateOrg(id) {
		if (id) {
			$("<div id=\"organizDiv\"/>").showDialog({
				"title" : "修改机构",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/admin/org/update/" + id,
				"type" : VIEW_TYPE.organiz,
				"action" : ACTION.update,
				"width" : 407,
				"height" : 180
			});
		} else {
			$("<div id=\"organizDiv\"/>").showDialog({
				"title" : "添加机构",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/admin/org/add",
				"type" : VIEW_TYPE.organiz,
				"action" : ACTION.create,
				"width" : 407,
				"height" : 180
			});
		}
	}
	//获取未分配组织机构的用户列表
	function getOrgUserList(id) {
		$("<div id=\"assignUserDiv\"/>").showDialog({
			"title" : "获取未分配组织机构的用户列表",
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
				"width" : 450,
				"height" : 520
			});
		} else {
			$("<div id=\"userDiv\"/>").showDialog({
				"title": "${LANG['system.admin.index.createuser']}",
				"url" : "/admin/entUser/toEditUserBase",
				"dialogClass" : "ui-dialog-smile",
				"type" : VIEW_TYPE.siteUser,
				"action" : ACTION.create,
				"width" : 450,
				"height" : 520
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
			"title":"用户信息",
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
			"title": "提示",
			"dialogClass" : "ui-dialog-smile",
			"message" : "确定要退出吗?",
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
			"title" : "入会详情",
			"dialogClass" : "ui-dialog-smile",
			"url" : "<%=request.getContextPath()%>/user/conflog/loglist?confId="+id,
			"type" : VIEW_TYPE.group,
			"action" : ACTION.create,
			"width" : 804,
			"height" : 534
		});
	}
	
	function showAttendConfs(id) {
		$("<div id=\"attendconfs\"/>").showDialog({
			"title" : "参会详情",
			"dialogClass" : "ui-dialog-smile",
			"url" : "<%=request.getContextPath()%>/user/conflog/attendConflist?userId="+id,
			"type" : VIEW_TYPE.group,
			"action" : ACTION.create,
			"width" : 804,
			"height" : 534
		});
	}
</script>
</HEAD>


<body style="min-width:1002px;">
<!--页面头部开始-->
<jsp:include page="header.jsp" />
<!--页面左部-->
<div class="main_left" >
 <ul class="nav">
 		<li><span class="nav_top05"><a href="#">${LANG['system.menu.config.user']}</a></span>
          <ul class="sub_nav">
          <c:if test="${isSuperSiteAdmin}">
            <li class="b_line"><a class="li_alink" href="/admin/entUser/list" target="mainFrame">${LANG['company.menu.admin.manage']}</a></li>
          </c:if>
            <li class="b_line"><a class="li_alink li_actived" href="/admin/entUser/listAll" target="mainFrame">${LANG['company.menu.user.manage']}</a></li>
            <li><a class="li_alink" href="/admin/org/orgList" target="mainFrame">机构管理</a></li>
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
<!--页面下部-->
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

