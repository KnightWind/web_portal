<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<TITLE>商会云系统管理平台</TITLE>

<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="/static/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/common.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/style.css"/>

<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<SCRIPT type="text/javascript" src="/static/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.uniform/jquery.uniform.js"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/util.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js"></script>
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
			changeLang(lang);
		});
		$('body').bind(EVENT_CREATE, function(event, result, type) {
			if(type==VIEW_TYPE.sysAdminUser) {
				successDialog(result.siteBase[0]);
				showURL("/system/sysUser/list");
			} else if(type==VIEW_TYPE.site){
				siteCreateSuccess(result);
				showURL("/system/site/list");
			} else if(type==VIEW_TYPE.notice) {
				successDialog(result.message);
				showURL("/system/notice/list");
			}
		});
		$('body').bind(EVENT_UPDATE, function(event, result, type) {
			if (type==VIEW_TYPE.sysAdminUser) {
				successDialog(result.siteBase[0]);
				showURL("/system/sysUser/list");
			} else if(type==VIEW_TYPE.site){
				siteUpdateSuccess(result);
				showURL("/system/site/list");
			} else if (type==VIEW_TYPE.notice) {
				successDialog(result.message);
				showURL("/system/notice/list");
			}
		});
		$('body').bind(EVENT_DELETE, function(event, result) {
			//delete success
		});

		$('body').bind(EVENT_ERROR, function(event, result, type) {
			var message = result.message;
			errorDialog(message);
		});
		$('body').bind(EVENT_SEND_EMAIL, function(event, siteInfo, userInfo) {
    		app.sendSiteEmail(siteInfo, userInfo);
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
	});
	
	//refresh Main Iframe Content
	function showURL(url) {
		url+="?t="+ Math.random();
		$('#mainFrame').attr("src", url);
	}

	//resize index body height when search div Collapsing 
	function resizeHeight() {
		$("#mainFrame").height($("#mainFrame").contents().find("body").height());  
		
	}
	
	function createOrUpdateHost(siteId,userId) {
		if(userId) {
			$("<div id=\"hostDiv\"/>").showDialog({
				"title": "修改主持人",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/lic/goEditHost?siteId="+siteId+"&userId="+userId,
				"type" : VIEW_TYPE.hostUser,
				"action" : ACTION.update,
				"width" : 510,
				"height" : 450
			});
		} else {
			$("<div id=\"hostDiv\"/>").showDialog({
				"title" : "创建主持人",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/lic/goEditHost?siteId="+siteId+"&userId="+userId,
				"type" : VIEW_TYPE.hostUser,
				"action" : ACTION.create,
				"width" : 510,
				"height" : 450
			});
		}		
	}
	
	function countManager(id) {
		$("<div id=\"countManagerDiv\"/>").showDialog({
			"title" : "点数管理",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/system/lic/list?siteId="+id,
			"type" : VIEW_TYPE.hostUser,
			"action" : ACTION.create,
			"width" : 690,
			"height" : 380
		});			
	}
	//name_host点数设置
	function licenseManage(userId,siteId){
		$("<div id=\"countManagerDiv\"/>").showDialog({
			"title" : "点数管理",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/system/lic/list?userId="+userId+"&siteId="+siteId,
			"type" : VIEW_TYPE.hostUser,
			"action" : ACTION.create,
			"width" : 690,
			"height" : 380
		});			
	}
	function hostManger(id) {
		$("<div id=\"hostManagerDiv\"/>").showDialog({
			"title" : "host管理",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/system/lic/listHost?siteId="+id,
			"type" : VIEW_TYPE.hostUser,
			"action" : ACTION.create,
			"width" : 550,
			"height" : 380
		});			
	}
	
	//create or update system user
	function createOrUpdateUser(id) {
		if(id) {
			$("<div id=\"userDiv\"/>").showDialog({
				"title": "${LANG['system.sysUser.list.changeUser']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/sysUser/update/" + id,
				"type" : VIEW_TYPE.sysAdminUser,
				"action" : ACTION.update,
				"width" : 450,
				"height" : 380
			});
		} else {
			$("<div id=\"userDiv\"/>").showDialog({
				"title" : "${LANG['system.sysUser.list.addUser']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/sysUser/new",
				"type" : VIEW_TYPE.sysAdminUser,
				"action" : ACTION.create,
				"width" : 450,
				"height" : 380
			});
		}
	}
	//create or update notice
	function createOrUpdateNotice(id) {
		if (id) {
			$("<div id=\"noticeDiv\"/>").showDialog({
				"title" : "${LANG['system.notice.list.Update']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/notice/update/" + id,
				"type" : VIEW_TYPE.notice,
				"action" : ACTION.update,
				"width" : 680,
				"height" : 400
			});
		} else {
			$("<div id=\"noticeDiv\"/>").showDialog({
				"title" : "${LANG['system.notice.list.Create']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/notice/create",
				"type" : VIEW_TYPE.notice,
				"action" : ACTION.create,
				"width" : 680,
				"height" : 400
			});
		}
	}

	//create or update site
	function createOrUpdateSite(id) {
		if (id) {
			$("<div id=\"siteDiv\"/>").showDialog({
				"title" : "${LANG['system.site.list.update']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/site/update/" + id,
				"type" : VIEW_TYPE.site,
				"action" : ACTION.update,
				"width" : 500,
				"height" : 420
			});
		} else {
			$("<div id=\"siteDiv\"/>").showDialog({
				"title" : "${LANG['system.site.list.create']}",
				"dialogClass" : "ui-dialog-smile",
				"url" : "/system/site/new",
				"type" : VIEW_TYPE.site,
				"action" : ACTION.create,
				"width" : 500,
				"height" : 420
			});
		}
	}
	//view site info
	function viewSite(id) {
		$("<div id=\"siteDiv\"/>").showDialog({
			"title" : "${LANG['system.site.meaasge.lookup']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/system/site/view/" + id,
			"type" : VIEW_TYPE.site,
			"action" : ACTION.view,
			"width" : 420,
			"height" : 460
		});
	}
	//view notice info
	function viewNotice(id) {
		$("<div id=\"previewNoticeDiv\"/>").showDialog({
			"url" : "/system/notice/view/" + id,
			"dialogClass" : "ui-dialog-preview",
			"actions" : ["${LANG['system.close']}"],
			"width" : 677,
			"height" : 564
		});
	}
	//preview notice info
	function previewNotice(data) {
		$("<div id=\"previewNoticeDiv\"/>").showDialog({
			"url" : "/system/notice/preview",
			"dialogClass" : "ui-dialog-preview",
			"data" : data,
			"actions" : ["${LANG['system.close']}"],
			"width" : 677,
			"height" : 564
		});
	}
	function siteCreateSuccess(data) {
		$("<div id=\"siteSuccessDiv\"/>").showDialog({
			"title" : "${LANG['system.site.meaasge.create.succeed']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/system/site/siteCreateSuccess/" + data.siteBase[0].id,
			"width" : 480,
			"height" : 350			
		});
	}
	function siteUpdateSuccess(data) {
		$("<div id=\"siteSuccessDiv\"/>").showDialog({
			"title" : "${LANG['system.site.meaasge.update.succeed']}",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/system/site/siteUpdateSuccess/" + data.siteBase[0].id,
			"width" : 480,
			"height" : 350
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
				window.location.href="/system/logout";
			}
		});
	}	
	
	/*
	 * 超级系统管理员修改普通系统管理员密码后，普通系统管理员第一次登陆成功后需重置密码
	 */
	function resetPass(){
		$("<div id=\"resetPass\"/>").showDialog({
			"title" : "重置密码",
			"dialogClass" : "ui-dialog-smile",
			"url" : "/jsp/system/resetPass.jsp",
			"type" : VIEW_TYPE.notice,
			"action" : ACTION.view,
			"width" : 474,
			"height" : 220,
			"hideClose": true
		});
		
	}
	
	//查看入会详情
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
</script>

 
</HEAD>
<BODY style="min-width:1002px;">
<!--页面头部开始-->
<jsp:include page="header.jsp" />

<div class="main_left" >
 <ul class="nav">
 		<c:if test="${isSuperSystemAdmin}">
	 		<li><span class="nav_top05"><a href="#">${LANG['system.menu.config.user']}</a></span>
	          <ul class="sub_nav">
	            <li><a class="li_alink" href="/system/sysUser/list" target="mainFrame">${LANG['system.menu.config.sysUser']}</a></li>
	          </ul>
	        </li>
        <li><span class="nav_top01"><a href="#">${LANG['system.menu.config']}</a></span>
          <ul class="sub_nav">
            <li class="b_line"><a class="li_alink" href="/system/email/showhost" target="mainFrame">${LANG['system.menu.config.host']}</a></li>
            <li><a class="li_alink" href="/system/email/goTemplate" target="mainFrame">${LANG['system.menu.config.template']}</a></li>
          </ul>
        </li>
        </c:if>
        <li><span class="nav_top02"><a class="" href="#">${LANG['system.menu.site.manage']}</a></span>
          <ul class="sub_nav">
            <li><a class="li_alink li_actived" href="/system/site/list" target="mainFrame">${LANG['system.menu.site.list']}</a></li>
          </ul>
        </li>
        <c:if test="${isSuperSystemAdmin}">
        <li><span class="nav_top03"><a class="" href="#">${LANG['system.menu.notice.manage']}</a></span>
          <ul class="sub_nav">
            <li><a class="li_alink" href="/system/notice/list" target="mainFrame">${LANG['system.menu.notice.list']}</a></li>
          </ul>
        </li>
        </c:if>
       
        <li><span class="nav_top04"><a class="" href="#">${LANG['system.menu.info.list']}</a></span>
          <ul class="sub_nav">
            <li class="b_line"><a class="li_alink" href="/system/conf/list" target="mainFrame">${LANG['system.menu.info.meeting']}</a></li>
            <c:if test="${user.sysType gt 6}">
	            <li class="b_line"><a class="li_alink" href="/system/logs/syslist" target="mainFrame">${LANG['system.menu.info.log.sysManager']}</a></li>
	            <li><a class="li_alink" href="/system/logs/siteloglist" target="mainFrame">${LANG['system.menu.info.log.userManager']}</a></li>
         	</c:if>
          </ul>
        </li>
         <li><span class="nav_top06"><a class="" href="#">${LANG['system.menu.info.user.manage']}</a></span>
          <ul class="sub_nav">
            <li class="b_line"><a class="li_alink" href="/system/profile" target="mainFrame">${LANG['system.menu.info.user.manage']}</a></li>
            
          </ul>
        </li>
    </ul>    
</div>
<div class="main_container">
<iframe frameborder="0" width="100%" height="100%" id="mainFrame" name="mainFrame" scrolling="no" src="/system/site/list"></iframe>
</div>

<!--左部菜单栏-->
<jsp:include page="footer.jsp" />
</BODY>
</HTML>
<script type="text/javascript">
	function changeLang(langVal){
		var jumpUrl="/changeLang?lang="+langVal+"&returnURL=/system/";
		window.location.href=jumpUrl;
	}
	function initPage(){
		var useLang="${lang}";
		//$("#lang option[value='"+useLang+"']").
		$("#lang").val(useLang); 
		
	}
	initPage();
//zh-cn,lang_zh.xml
//en-us,lang_en.xml
/////changeLang?lang=&returnURL=
</script>

