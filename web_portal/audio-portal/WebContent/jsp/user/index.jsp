<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<cc:confList var="JOIN_TYPE_CONFID"></cc:confList>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${siteBase.siteName}--会议系统</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/index.css?ver=${version}"/>

<fmt:formatDate var="serverDate" value="${defaultDate}" type="date" pattern="yyyy/MM/dd HH:mm:ss"/>
<script language="javascript">
<!--
/*屏蔽所有的js错误*/
function killerrors() {
return true;
}
window.onerror = killerrors;
//-->
</script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-1.8.3.min.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-ui-1.9.2.custom.min.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-ui-i18n.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.uniform.min.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.tipsy.min.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/json2.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></script>
<%@ include file="/jsp/common/cookie_util.jsp"%>
<c:if test="${!empty errorMessage}">
<script type="text/javascript">
$(function() {
	errorDialog("${errorMessage}");
});
</script>
</c:if>
<c:if test="${!empty infoMessage}">
<script type="text/javascript">
$(function() {
	successDialog("${infoMessage}");	
});
</script>
</c:if>
<cc:confList var="JOIN_TYPE_CONFID"/>
<script type="text/javascript">

/*
 *页面上动态调整Iframe的宽高
 *	
 */

function changeIframeSize(frameId,widthNum,heightNum){
	var frameObj=document.getElementById(frameId);
	if (frameObj){
		if (widthNum>0){
			frameObj.style.posWidth=widthNum;
			//frameObj.setAttribute("width",widthNum)
		}
		if (heightNum>0){
			//style.height
			if ($.browser.msie && $.browser.version<7) {
				frameObj.style.posHeight=heightNum;	
			} else {
				frameObj.style.height=heightNum+"px";	
			}
			//frameObj.setAttribute("height",heightNum)
		}
	} 
	$("#joinMeeting").trigger("resize");
}

jQuery(function($) {
	if ($.browser.msie && $.browser.version<7) {//for ie6 png
		$(".png").each(function() {
			$(this).attr('width',$(this).width());
			$(this).attr('height',$(this).height());
			var imgSrc = $(this).attr('src');
			$(this).get(0).runtimeStyle.filter = 'progid:DXImageTransform.Microsoft.AlphaImageLoader' + '(src=\'' + imgSrc + '\', sizingMethod=\'scale\');';
			$(this).attr('src', '/static/images/blank.gif');
		});		
	}
	$('body').bind(EVENT_CREATE, function(event, result, type) {
		if(type==VIEW_TYPE.bookMeeting) {
			jumpToMeetingPanel(result);
		} else if(type==VIEW_TYPE.group){
			if(result && result.status==1){
				successDialog(result.message);
				showChildURL("/user/group/list");	
			} else {
				errorDialog(result.message);
			}
		} else if (type==VIEW_TYPE.contact) {
			if(result && result.status==1){
				successDialog(result.message);
				showURL("/user/contact/goContacts");	
			} else {
				errorDialog(result.message);
			}
		} else if(type==VIEW_TYPE.attendee){
			if(result && result.status==1){
				successDialog(result.message);
				refreshChildIframe();
			} else {
				errorDialog(result.message);
			}
		} else if(type==VIEW_TYPE.tempMeeting) {
			joinImmediatelyConf(result);
		}
	});
	$('body').bind(EVENT_UPDATE, function(event, result, type) {
		if(type==VIEW_TYPE.bookMeeting) {
			refreshChildIframe();
		}
	});

	//left menu selected
	$('.nav_li').click(function() {
		var href = $(this).find("a").attr("link");
		if(href!="/jsp/user/help.jsp"){
			var loginUser = "${user}";
			if(loginUser) {
				$('.nav_menu').find("li").removeClass("nav_li_active");
				$(this).addClass("nav_li_active");
				$("#mainFrame").attr("src", href);	
			} else {
				if(href=="/jsp/user/download_center.jsp" || href=="/user/conf/getPublicControlPadIndex"){
					$('.nav_menu').find("li").removeClass("nav_li_active");
					$(this).addClass("nav_li_active");
					$("#mainFrame").attr("src", href);	
				} else {
					login();	
				}	
			}
		}
	});
	$('.nav_ul').click(function() {
		var ul = $(this).next();
		var link = $(this).find("a");
		if (ul.is(":visible")) {
			link.attr("style", "background:url('/static/images/new02.png') no-repeat scroll right center transparent");
			ul.slideUp();
		} else {
			link.attr("style", "background:url('/static/images/new01.png') no-repeat scroll right center transparent");
			ul.slideDown();
		}
	});
	//left menu hover
// 	$('.nav_menu li').hover(function() {
// 		if(!$(this).hasClass("nav_li_active")){
// 			$(this).addClass("nav_li_hover");	
// 		}
// 	}, function() {
// 		$(this).removeClass("nav_li_hover");
// 	});
	//resize iframe height
	$("#mainFrame").load(function(){
		resizeHeight();  
		joinMtgFromEmail();
    });
	//change lang
	$("#langForm").find("select").not(".skipThese").uniform();
	$("#langForm select").change(function () {
		var lang = $(this).val();
		changeLang(lang);
	});
	//
// 	$(window).bind("scroll", function(){
// 		var scrollTop = $(window).scrollTop();
// 		var width = $("body").width()-220;
// 		var navTop = 122;
// 		$("#mainFrame").width(width+20);
// 		if(scrollTop>navTop){
// 			$("#tool_bar").width(width).css({"position":"fixed"});	
// 		} else {
// 			$("#tool_bar").width(width).css({"position":"relative"});
// 		}
// 	});
	$(window).bind("resize", function() {
		resizeHeight();
		//resizeFloatBar();
	});
	popupNotice();
	showRemindBar();
	var needResetPass = "${needResetPass}";
	if(needResetPass != null && needResetPass =="true"){
		resetPass();
	}
	
});
window.setInterval(resizeHeight, 1000);
function resizeHeight() {
	var height = $("#mainFrame").contents().find("body").height()+50;
	var screenHeight = $(window).height()-144;
	if(screenHeight>0){
		if(height<screenHeight) {
			height = screenHeight;
		}
		if(height<550){
			height = 550;
		}
		$("#mainFrame").height(height);
		$(".main_right").height(height);
		$(".main_left").height(height);	
	}
}
function resizeFloatBar() {
	if($(document).scrollTop()==0){
		$("#tool_bar").css({"position":"relative"});
	}
	var width = $("body").width()-210;
	if($.browser.msie && $.browser.version<7){
		width-=20;//fixed ie6 iframe float down
	}
	$("#tool_bar").width(width);
	$("#mainFrame").width(width+20);
}
function reloadPage() {
	window.location.href="/";
}
//refresh Main Iframe Content
function showURL(url) {
	url+="?t="+ Math.random();
	$('#mainFrame').attr("src", url);
}
//refresh Child Iframe
function showChildURL(url) {
	url+="?t="+ Math.random();
	$("#mainFrame")[0].contentWindow.$("#contentFrame").attr("src", url);
}

function hideTabPanel(elem, callback) {
	var tabContent = window.frames["mainFrame"].document.getElementById(elem);
	if(tabContent && $(tabContent).is(":visible")){
		var icon = $(tabContent).prev().find(".panel_icon");
		hidePanel(tabContent, icon, function(){
			callback(tabContent);
		});
	} else {
		callback(tabContent);
	}	
}
function showTabPanel(elem, callback) {
	var tabContent = window.frames["mainFrame"].document.getElementById(elem);
	if(tabContent && $(tabContent).is(":hidden")){
		var icon = $(tabContent).prev().find(".panel_icon");
		showPanel(tabContent, icon, function(){
			callback(tabContent);
		});
	} else {
		callback(tabContent);
	}
}
function jumpToMeetingPanel(result) {
	$(".nav_li").removeClass("nav_li_active");
	$(".preside_li").addClass("nav_li_active");
	showURL("/user/conf/getControlPadIndex?userRole=1");
}

function jumpToFavorLi(url) {
	url+="?t="+ Math.random();
	$(".nav_li").removeClass("nav_li_active");
	$("#favorSetUp").addClass("nav_li_active");
	$("#mainFrame").attr("src", url); 
}
function showMeetingPanel(result) {
	hideTabPanel("running_list", function(runElem) {
		showTabPanel("coming_list", function() {
			var elem = null;
			if(runElem){
				elem = runElem;
			}
			insertMeeting(elem, result);
		});
	});		
}
function insertMeeting(elem, result) {
	var comingList = window.frames["mainFrame"].document.getElementById("coming_list");
	if(comingList) {
		insertMeetTR(comingList, result);
	} else {
		insertFirstMeet(elem, result);
	}
}

function insertFirstMeet(prevElem, result) {
	var noData = window.frames["mainFrame"].document.getElementById("no_data");
	$(noData).remove();
	var tabTitle = null;
	if(prevElem){
		tabTitle = $("<div class='tab_title'></div>").insertAfter(prevElem);	
	} else {
		prevElem = window.frames["mainFrame"].document.getElementById("main_container");
		tabTitle = $("<div class='tab_title'></div>").appendTo(prevElem);
	}
	var left = $("<div align='left' style='float: left;width:45%'></div>").appendTo(tabTitle);
		$("<span onclick='toggleIcon(this)' class='panel_icon panel_icon_off'>&nbsp;</span>").appendTo(left);
		$("<span onclick='toggleIcon(this)' class='m_title'>即将开始</span>").appendTo(left);
		$("<div class='number_taxt'><div class='no_left'></div><div class='no_center'><span class='meeting-count'>0</span></div><div class='no_right'></div></div>").appendTo(left);
	var right = $("<div align='right' style='float: right;width:50%'></div>").appendTo(tabTitle);
	$("<input type='text' name='' class='m_search' onkeyup='quickSearch(this)'>").appendTo(right);
	var comingList = $("<div style='display: block;' class='tab_content' id='coming_list'>").insertAfter(tabTitle);
	insertMeetTR(comingList, result);
}
function insertMeetTR(comingList, result){
// 	var containers = $(comingList).find(".extras-container");
// 	$.each(containers, function(i, domEle) {
// 		var sortTime = $(domEle).find(".date-holder").attr("sorttime");
// 		var startTime = result.confBase[0].startTime;
// 		console.log("sortTime="+sortTime+" startTime="+startTime);
// 		if(compareDate(startTime, sortTime)){
// 			tabTitle = $(domEle).prev();
// 			return false;
// 		}
// 	});
	var newDiv = $("<div class='extras-container' style='display:none;text-align:center;'></div>");
	var loading = $("<img src='/static/images/loading.gif' width='32px' height='32px' style='margin-top: 10px;'/>").appendTo(newDiv);
	newDiv.prependTo(comingList);
	newDiv.slideDown(function() {
		if(result){
			//update count
			var countSpan = $(comingList).prev().find(".meeting-count");
			var count = parseInt(countSpan.text(), 10)+1;
			countSpan.text(count);
			//load new meeting div
			var id = result.confBase[0].id;
			app.bookMeetingSuccess(id, function(data) {
				if(data){
					newDiv.empty();
					$(newDiv).append(data);
					mainFrame.window.refreshTips();
					resizeHeight();	
				} else {
					alert("插入会议失败, 没有返回任何数据");
				}
			});	
		} else {
			alert("插入会议失败");
		}
	});
}

//refresh group list
function refreshGroupList() {
	showChildURL("/user/group/list");	
}
//refresh child iframe
function refreshChildIframe() {
	var iframeSrc = $("#mainFrame")[0].contentWindow.$("#contentFrame").attr("src");
	showChildURL(iframeSrc);
}

function showConflogs(id) {
	$("<div id=\"logview\"/>").showDialog({
		"title" : "入会详情",
		"dialogClass" : "ui-dialog-user",
		"url" : "http://meeting.confcloud.cn/user/conflog/loglist?confId="+id,
		"type" : VIEW_TYPE.group,
		"action" : ACTION.create,
		"width" : 804,
		"height" : 534
	});
}
//add group
function addGroup(id) {
	$("<div id=\"addGroup\"/>").showDialog({
		"title" : "添加群组",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/group/editGroup?id="+id,
		"type" : VIEW_TYPE.group,
		"action" : ACTION.create,
		"width" : 474,
		"height" : 304
	});
}
//view contact
function viewContact(id) {
	$("<div id=\"viewContact\"/>").showDialog({
		"title" : "查看联系人",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/group/showContacts?viewOnly=1&group_id="+id,
		"type" : VIEW_TYPE.contact,
		"action" : ACTION.view,
		"width" : 804,
		"height" : 584
	});	
}
//add contact
function addContact(id) {
	$("<div id=\"addContact\"/>").showDialog({
		"title" : "添加联系人",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/contact/editContact?id="+id,
		"type" : VIEW_TYPE.contact,
		"action" : ACTION.create,
		"width" : 474,
		"height" : 488
	});	
}
//import contact
function importContact() {
	$("<div id=\"importContact\"/>").showDialog({
		"title" : "导入联系人",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/contact/goContactsSelect",
		"type" : VIEW_TYPE.contact,
		"action" : ACTION.create,
		"width" : 804,
		"height" : 631
	});	
}
//add contact from group
function addContactFromGroup(id) {
	$("<div id=\"importContact\"/>").showDialog({
		"title" : "添加用户",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/group/importContacts?group_id="+id,
		"type" : VIEW_TYPE.contact,
		"action" : ACTION.create,
		"width" : 804,
		"height" : 584
	});	
}
//bulk import contact
function bulkImportContact() {
	$("<div id=\"importContact\"/>").showDialog({
		"title" : "批量导入联系人",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/contact/importPop",
		"type" : VIEW_TYPE.contact,
		"action" : ACTION.create,
		"width" : 804,
		"height" : 631
	});	
}
//invent contact
function inventContact(confId) {
	$("<div id=\"inventContact\"/>").showDialog({
		"title" : "邀请参会人",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/contact/goInviteContacts?confId="+confId,
		"type" : VIEW_TYPE.attendee,
		"action" : ACTION.create,
		"width" : 804,
		"height" : 475
	});	
}
//invent contact
function editInventContact(confId) {
	$("<div id=\"eidtInventContact\"/>").showDialog({
		"title" : "邀请参会人",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/inviteUser?confId="+confId,
		"type" : VIEW_TYPE.attendee,
		"action" : ACTION.create,
		"width" : 624,
		"height" : 504
	});	
}
function contactImport() {
	$("<div id=\"importContact\"/>").showDialog({
		"title" : "邀请联系人",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/contact/importPop",
		"type" : VIEW_TYPE.attendee,
		"action" : ACTION.create,
		"width" : 804,
		"height" : 625
	});	
}
//conf info
function viewConf(id) {
	$("<div id=\"viewMeeting\"/>").showDialog({
		"title" : "会议详情",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/conf/view/" + id,
		"type" : VIEW_TYPE.bookMeeting,
		"action" : ACTION.view,
		"width" : 684,
		"height" : 409
	});
}

//view group
function viewGroup(id) {
	$("<div id=\"viewGroup\"/>").showDialog({
		"title" : "查看群组成员",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/group/showContacts?group_id=" + id,
		"type" : VIEW_TYPE.group,
		"action" : ACTION.view,
		"width" : 804,
		"height" : 584
	});
}
//create or update createNewReservationConf
function createReservationConf(id) {
	var loginUser = "${user}";
	if(loginUser){
		if (id) {
			$("<div id=\"bookMeeting\"/>").showDialog({
				"title" : "修改预约会议",
				"dialogClass" : "ui-dialog-user",
				"url" : "/user/conf/update/" + id,
				"type" : VIEW_TYPE.bookMeeting,
				"action" : ACTION.update,
				"width" : 684,
				"height" : 464
			});
		} else {
			$("<div id=\"bookMeeting\"/>").showDialog({
				"title" : "创建预约会议",
				"dialogClass" : "ui-dialog-user",
				"url" : "/user/conf/createReservationConf",
				"type" : VIEW_TYPE.bookMeeting,
				"action" : ACTION.create,
				"width" : 684,
				"height" : 464
			});
		}
		
	} else {
		login();	
	}
}
//重新创建会议
function reCreateReservationConf(id) {
	$("<div id=\"bookMeeting\"/>").showDialog({
		"title" : "重新创建会议",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/conf/reCreateconf/" + id,
		"type" : VIEW_TYPE.bookMeeting,
		"action" : ACTION.update,
		"width" : 684,
		"height" : 464
	});	
}

//update all ReservationConf
function updateReservationConf(id) {
	$("<div id=\"updateOneMeeting\"/>").showDialog({
		"title" : "修改周期会议其中一条会议",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/conf/getCycleConfDate/" + id,
		"type" : VIEW_TYPE.bookMeeting,
		"action" : ACTION.update,
		"width" : 474,
		"height" : 434
	});	
}

function delACycleConf(id) {
	$("<div id=\"updateOneMeeting\"/>").showDialog({
		"title" : "修改周期会议其中一条会议",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/conf/getCycleConfDate/" + id+"?delCycle=1",
		"type" : VIEW_TYPE.bookMeeting,
		"action" : ACTION.update,
		"width" : 474,
		"height" : 434
	});	
}

//修改周期会议中所有会议的信息
function updateCycleMeetingInfo(id) {
	$("<div id=\"bookMeeting\"/>").showDialog({
		"title" : "修改周期会议中所有会议的信息",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/conf/updateCycleConfInfo/" + id,
		"type" : VIEW_TYPE.bookMeeting,
		"action" : ACTION.update,
		"width" : 680,
		"height" : 460
	});	
}

//createImmediatelyConf
function createImmediatelyConf() {
	var loginUser = "${user}";
	if(loginUser){
		$("<div id=\"joinMeeting\"/>").showDialog({
			"title" : "创建即时会议",
			"dialogClass" : "ui-dialog-user",
			"url" : "/user/conf/createImmediatelyConf",
			"type" : VIEW_TYPE.tempMeeting,
			"action" : ACTION.create,
			"width" : 474,
			"height" : 184
		});	
	} else {
		login();
	}
}
function joinImmediatelyConf(result) {
	var url = "/join?joinType=${JOIN_TYPE_CONFID}&cId="+result.id;
	$("<div id=\"joinMeeting\"/>").showDialog({
		"title" : "创建即时会议",
		"dialogClass" : "ui-dialog-user",
		"url" : url,
		"type" : VIEW_TYPE.linkTempMeeting,
		"action" : ACTION.create,
		"width" : 474,
		"height" : 184
	});	
	
}
//add calendar
function addCalendar(id) {
	$("<div id=\"addCalendar\"/>").showDialog({
		"title" : "添加日历提醒",
		"dialogClass" : "ui-dialog-user",
		"url" : "/jsp/user/add_calendar_notice.jsp",
		"data": id,
		"type" : VIEW_TYPE.calendar,
		"action" : ACTION.create,
		"width" : 474,
		"height" : 204
	});	
}
//quick attendConf
function attendConf() {
	$("<div id=\"quickMeeting\"/>").showDialog({
		"title" : "快速入会",
		"dialogClass" : "ui-dialog-smile",
		"url" : "/user/conf/attendConf",
		"type" : VIEW_TYPE.quickMeeting,
		"action" : ACTION.create,
		"width" : 400,
		"height" : 150
	});
}


function joinMeeting(joinType,cId){
	if(joinType==null || joinType==""){
		return false;
	}
	var titleName="快速入会";
	if(joinType=="${JOIN_TYPE_CONFID}"){
		titleName="加入会议";
	}
	var divHeight="auto";
	if ($.browser.msie && $.browser.version<7) {
		divHeight=230;
	}
	$("<div id=\"joinMeeting\"/>").showDialog({
		"title" : titleName,
		"dialogClass" : "ui-dialog-user",
		"url" : "/join/joinpage?joinType="+joinType+"&cId="+cId,
		"type" : VIEW_TYPE.joinMeeting,
		"action" : ACTION.join,
		"width" : 474,
		"height" : divHeight
	});
}



function joinMeetingReload(){
	
	
	var domain=getDomain(); 
	var reloadValue=getCookieByDomain("reload",domain);
	if(reloadValue==null || reloadValue==""){
		return null;
	}
	var reloadArr=reloadValue.split(",");
	if(reloadArr.length!=7){
		return null;
	}

	var reload=reloadArr[0];
	var cId=reloadArr[1];
	var userName=reloadArr[2];
	var joinType=reloadArr[3];
	var code=reloadArr[4];
	var cpass=reloadArr[5];
	var rId=reloadArr[6];
	
	//alert("reload=="+reload);
	$("#reload").val(reload);
	$("#cId").val(cId);
	$("#rId").val(rId);
	$("#userName").val(userName);
	$("#joinType").val(joinType);
	$("#code").val(code);
	$("#cPass").val(cpass);
	
	
	if(joinType==null || joinType==""){
		return false;
	}
	var titleName="快速入会";
	if(joinType=="${JOIN_TYPE_CONFID}"){
		titleName="加入会议";
	}
	
	$("<div id=\"joinMeeting\"/>").showDialog({
		"title" : titleName,
		"dialogClass" : "ui-dialog-user",
		"url" : "about:blank",
		//"data":{"cId":cId,"cPass":cPass,"code":code,"reLoad":reLoad,"joinType":joinType,"userName":userName},
		"type" : VIEW_TYPE.joinMeeting,
		"action" : ACTION.join,
		"width" : 474,
		"height" : "auto"
	});
	
	var frameObj=document.getElementById("dialogFrame");
	//alert("frameObj=="+frameObj);
	if(frameObj){
		document.getElementById("reLoadJoinConf").submit();
	}
	
}
function joinMeetingFromEmail(joinMtgUrl){
	var  titleName="加入会议";
	$("<div id=\"joinMeeting\"/>").showDialog({
		"title" : titleName,
		"dialogClass" : "ui-dialog-user",
		"url" : joinMtgUrl,//"/join/joinpage?joinType="+joinType+"&cId="+cId,
		"type" : VIEW_TYPE.joinMeeting,
		"action" : ACTION.join,
		"width" : 474,
		"height" :"auto"
	});
}
function errorDialog(message, callback) {
	$("<div id=\"errorDiv\"/>").alertDialog({
		"title" : "提示",
		"dialogClass" : "ui-dialog-user",
		"type": "error",
		"message": message,
		"actions": ["${LANG['system.ok']}"],
		"callback": callback
	});
}

function confirmDialog(message, callback) {
	$("<div id=\"confirmDiv\"/>").alertDialog({
		"title" : "提示",
		"dialogClass" : "ui-dialog-user",
		"type": "confirm",
		"message": message,
		"actions": ["${LANG['system.cancel']}", "${LANG['system.ok']}"],
		"callback" : callback
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

function logout() {
	$("<div/>").alertDialog({
		"title": "提示",
		"dialogClass" : "ui-dialog-user",
		"message" : "确定要退出吗?",
		"type": "confirm",
		"actions": ["${LANG['system.cancel']}", "${LANG['system.ok']}"],
		"callback" : function() {
			window.location.href="/user/logout";
			window.event.returnValue = false;
		}
	});
}
function login() {
	$("<div id=\"loginDialog\"/>").showDialog({
		"title" : "登录",
		"dialogClass" : "ui-dialog-user",
		"url" : "/user/login",
		"type" : VIEW_TYPE.login,
		"action" : ACTION.view,
		"width" : 604,
		"height" : 354
	});
}

function popupNotice() { 
	var noticeId = "${sysNotice.id}";
	if(noticeId){
		var data = {};
		data.id = "${sysNotice.id}";
		data.title = "${sysNotice.title}";
		data.content = $("#noticeText").html();
		//data.content = "${sysNotice.content}";
		var existId = $.cookie("noticeId");
		if( !existId || (existId && existId!=noticeId) ){
			$("<div id=\"popupNotice\"/>").showDialog({
				"title" : "公告",
				"dialogClass" : "ui-dialog-user",
				"data": data,
				"url" : "/jsp/user/viewNotice.jsp",
				"type" : VIEW_TYPE.notice,
				"action" : ACTION.view,
				"width" : 677,
				"height" : 564
			});
		}		
	}
}

/*
 * 企业管理员修改企业用户密码后，企业用户第一次登陆成功后需重置密码
 */
function resetPass(){
	$("<div id=\"resetPass\"/>").showDialog({
		"title" : "重置密码",
		"dialogClass" : "ui-dialog-user",
		"url" : "/jsp/user/resetPass.jsp",
		"type" : VIEW_TYPE.notice,
		"action" : ACTION.view,
		"width" : 474,
		"height" : 285
	});
}

function getMainDomain(){
	var domain=document.domain;
	if(domain!=null && domain.length>0){
		var firstDotIndex=domain.indexOf(".");
		domain=domain.substr(firstDotIndex+1);
	}
	return domain;
}

function getDomain(){
	return document.domain;
}
function setNoticeCookie(noticeId) {
	$.cookie("noticeId", noticeId, { expires: 365 ,domain:getDomain()});
}
function showRemindBar() {
	if(!$.browser.mozilla && !$.browser.msie) { 
		var remindBar = $.cookie("remindBar");
		if(!remindBar){
			$("#remindBar").show();
		}
	}
	
}
function closeRemindBar() {
	$.cookie("remindBar", true, { expires: 365 ,domain:getDomain()});
	$("#remindBar").hide();
}
function jumpToFavor() {
	var loginUser = "${user}";
	if(loginUser) {
		jumpToFavorLi("/user/favor/getTimeZone");
	} else {
		login();
	}
}
function getDateScopCode(date) {
	var serverDate = new Date("${serverDate}");
	var dateScopFlag = 0;
	var startTimeStamp = date.getTime();
	var dayEndTimeStamp = getDayEnd(serverDate).getTime();
	var weekEndTimeStamp = getWeekEnd(serverDate).getTime();
	var monthEndTimeStamp = getMonthEnd(serverDate).getTime();
	if(startTimeStamp<dayEndTimeStamp){
		dateScopFlag = 1;
	} else if(startTimeStamp<weekEndTimeStamp){
		dateScopFlag = 2;
	}else if(startTimeStamp<monthEndTimeStamp){
		dateScopFlag = 3;
	}else {
		dateScopFlag = 4;
	}
	return dateScopFlag;
}  
function getMonthDays(nowYear, nowMonth){  
	var monthStartDate = new Date(nowYear, nowMonth, 1);  
	var monthEndDate = new Date(nowYear, nowMonth + 1, 1);  
	var days = (monthEndDate - monthStartDate)/(1000 * 60 * 60 * 24);  
	return days;  
} 
function getDayEnd(date) {
	var tempDate = new Date();
	tempDate.setFullYear(date.getFullYear());
	tempDate.setMonth(date.getMonth());
	tempDate.setDate(date.getDate()+1);	
	tempDate.setHours(0);
	tempDate.setMinutes(0) ;
	tempDate.setSeconds(0) ;
	return tempDate;
}
function getWeekEnd(date) {
	var tempDate = new Date();
	tempDate.setFullYear(date.getFullYear());
	tempDate.setMonth(date.getMonth());
	tempDate.setDate(date.getDate()+(6-date.getDay())+1);	
	tempDate.setHours(0);
	tempDate.setMinutes(0) ;
	tempDate.setSeconds(0) ;
	return tempDate; 	
}
function getMonthEnd(date) {
	var monthDays = getMonthDays(date.getFullYear(), date.getMonth());
	var tempDate = new Date();
	tempDate.setFullYear(date.getFullYear());
	tempDate.setMonth(date.getMonth());
	tempDate.setDate(monthDays+1);	
	tempDate.setHours(0);
	tempDate.setMinutes(0) ;
	tempDate.setSeconds(0) ;	  
	return tempDate; 	
}
</script>
</head>

<body>
<c:if test="${empty user }">
	<div id="remindBar" style="display: none;">
		<table width="100%" height="40" bgcolor="#FFFFCC" align="center" style="">
			<tbody>
				<tr>
					<td align="center" style="font-size: 16px;">
						您的浏览器可能与本站点不兼容，建议您使用IE7或Firefox3.6及以上版本!&nbsp;&nbsp;&nbsp;
						<a class="remind_link" type="button" href="javascript:closeRemindBar();">&nbsp;&nbsp;不再提醒&nbsp;&nbsp;</a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</c:if>
<cc:confList var="JOIN_TYPE_EMAIL"/>
<!--页面头部开始-->
<jsp:include page="header.jsp" />

<div id="head_bar">
  <div class="nav_profile" >
  	<c:if test="${!empty user}">
  		<img style="float: left; margin-right: 10px;margin-top: 14px;_margin-top: 0px;" class="png" src="${baseUrlStatic}/images/header_bar_01.png" width="14" height="16" align="absmiddle" />
  		<span class="nav_name" title="${user.trueName }">${LANG['website.message.welcome']}，${user.trueName }&nbsp;&nbsp;</span>
  		<a style="display:inline-block;" href="javascript:void(0);" onclick="logout();return false;">${LANG['website.message.logout']}</a>
  	</c:if>
  	<c:if test="${empty user }">
  		<img class="png" src="${baseUrlStatic}/images/header_bar_01.png" width="14" height="16" align="absmiddle" />
  		<a href="javascript:login();">${LANG['website.message.login']}</a>
  	</c:if> 
  </div>
  <div class="subnav">
  <ul>
    <c:if test="${empty user}">
		<li class="sub01"><img src="${baseUrlStatic}/images/subnav01.png" width="20" height="20" onclick="createImmediatelyConf()"/><a class="subnav_01" href="javascript:createImmediatelyConf();">${LANG['user.menu.conf.immediate']}</a></li>
    	<li class="sub02"><img src="${baseUrlStatic}/images/subnav02.png" width="20" height="19" onclick="createReservationConf()"/><a class="subnav_02" href="javascript:createReservationConf();">${LANG['user.menu.conf.reservation']}</a></li>
    </c:if>
    <c:if test="${!empty user && user.userRole==1 }">
		<li class="sub01"><img src="${baseUrlStatic}/images/subnav01.png" width="20" height="20" onclick="createImmediatelyConf()"/><a class="subnav_01" href="javascript:createImmediatelyConf();">${LANG['user.menu.conf.immediate']}</a></li>    
    	<li class="sub02"><img src="${baseUrlStatic}/images/subnav02.png" width="20" height="19" onclick="createReservationConf()"/><a class="subnav_02" href="javascript:createReservationConf();">${LANG['user.menu.conf.reservation']}</a></li>
    </c:if>
    <li class="sub03"><img src="${baseUrlStatic}/images/subnav03.png" width="17" height="20" onclick="joinMeeting(2)"/><a class="subnav_03" href="javascript:joinMeeting(2);">${LANG['user.menu.conf.join.confid']}</a></li>
  </ul>  
  </div>
  <ul class="nav_right">
    <li class="time_bj" align="absmiddle" onclick="jumpToFavor();"><a style="font-weight: bold;	" title="${siteBase.fullTimeZoneDesc }${LANG['website.message.time']}" href="javascript:;">${siteBase.timeZoneDesc }${LANG['website.message.time']}</a></li>
    <li class="language_box">
      <form name="form" id="form">
        <select name="jumpMenu_language" id="jumpMenu_language" style="padding: 2px">
          <option selected="selected">中文版</option>
<!--           <option>English</option> -->
        </select>
      </form>
    </li>
  </ul>  
</div>
<!--页面左部-->
<div class="main_left">
  <ul class="nav_menu">
  	<c:if test="${!isLogined}">
  	<li class="nav_li nav_li_active" flag="publicConf">
    	<span class="m01_public">
    		<img class="m01_img" src="${baseUrlStatic}/images/yh_icon01.png" width="25" height="24" />
    		<a href="javascript:;" link="/user/conf/getPublicControlPadIndex" target="mainFrame">${LANG['user.menu.conf.public']}</a>
    	</span>
    </li>
    </c:if>
    <c:if test="${isLogined}">
    <li class="">
    	<span class="m01 nav_ul">
    	<img  class="m01_img" src="${baseUrlStatic}/images/yh_icon01.png" width="25" height="24" />
    	<a class="nav_a_on" href="javascript:;" link="/user/conf/listConf" target="mainFrame">${LANG['user.menu.conf.myconf']}</a>
    	</span>
    	<ul>
        	<li class="preside_li nav_li m09 nav_li_active" flag="myPresideMeet">
        		<img  class="m09_img" src="${baseUrlStatic}/images/icon_a.png" width="25" height="23" />
        		<a href="javascript:;" target="mainFrame" link="/user/conf/getControlPadIndex?userRole=1">${LANG['user.menu.conf.myhost']}</a>
        	</li>
        	<li class="nav_li m09" flag="myAttendMeet">
        		<img  class="m09_img" src="${baseUrlStatic}/images/icon_b.png" width="25" height="23" />
        		<a href="javascript:;" target="mainFrame" link="/user/conf/getControlPadIndex?userRole=2">${LANG['user.menu.conf.myact']}</a>
        	</li>
    	</ul>
    </li>
    </c:if>
    <li class="nav_li m03" flag="myContact">
    	<img  class="m01_img" src="${baseUrlStatic}/images/yh_icon03.png" width="25" height="25" />
    	<a href="javascript:;" link="/user/contact/goContacts" target="mainFrame">${LANG['siteAdmin.eventlog.type.4200']}</a>
    </li>
    <li class="nav_li m04" flag="noticeList">
    	<img  class="m01_img" src="${baseUrlStatic}/images/yh_icon04.png" width="25" height="22" />
    	<a href="javascript:;" link="/user/notice/list" target="mainFrame">${LANG['system.menu.notice.list']}</a>
    </li>

<%--    <c:if test="${isLogined}"> --%>
<%-- 	<li class=""> --%>
<%--    		<span class="m07 nav_ul">--%>
<%--    			<img  class="m01_img" src="${baseUrlStatic}/images/set_ico.png" width="25" height="22" /> --%>
<%--   			<a href="javascript:;" target="mainFrame" style="background:url('/static/images/new02.png') no-repeat scroll right center transparent">会议报告</a> --%>
<%--   		</span> --%>
<%--   		<ul style="display: none;"> --%>
<%-- 	    	<li class="nav_li m09"  flag="myPresideReport">--%>
<%-- 	    		<img  class="m09_img" src="${baseUrlStatic}/images/icon_a.png" width="22" height="22" /> --%>
<%-- 	    		<a href="javascript:;" link="/user/conflog/list?isCreator=true" target="mainFrame">我主持的</a>--%>
<%--	    	</li> --%>
<%-- 	    	<li class="nav_li m09"  flag="myAttendReport"> --%>
<%-- 	    		<img  class="m09_img" src="${baseUrlStatic}/images/icon_b.png" width="20" height="20" /> --%>
<%-- 	    		<a href="javascript:;" link="/user/conflog/list?isCreator=false"  target="mainFrame">我参加的</a>--%>
<%--	    	</li> --%>
<%-- 	    </ul> --%>
<%--   	</li>    --%>
<%--    </c:if> --%>

    <c:if test="${isConfHost}">
    	<li class="nav_li m06" flag="meetConfig">
    		<img  class="m01_img" src="${baseUrlStatic}/images/yh_icon06.png" width="25" height="25" />
    		<a href="javascript:;" link="/user/confConfig/getConfConfig" target="mainFrame">${LANG['user.menu.conf.default.setup']}</a>
    	</li>
    </c:if>
    <c:if test="${isLogined}">
    	<li id="favorSetUp" class="nav_li m06" flag="favorConfig">
    		<img  class="m01_img" src="${baseUrlStatic}/images/ico22.png" width="25" height="22" />
    		<a href="javascript:;" link="/user/favor/getTimeZone" target="mainFrame">${LANG['user.menu.conf.favor.setup']}</a>
    	</li>
    	<li class="nav_li m06" flag="personConfig">
    		<img  class="m01_img" src="${baseUrlStatic}/images/yh_icon07.png" width="25" height="22" />
    		<a href="javascript:;" link="/user/profile" target="mainFrame">${LANG['user.menu.conf.person.setup']}</a>
    	</li>
    </c:if>
    	<li class="m06">
    		<span class="m07 nav_ul">
    			<img  class="m01_img" src="${baseUrlStatic}/images/new05.png" width="25" height="22" />
    			<a href="javascript:;" target="mainFrame" style="background:url('/static/images/new02.png') no-repeat scroll right center transparent">${LANG['user.menu.conf.support']}</a>
    		</span>
    		<ul style="display: none;">
    			<li class="nav_li m09" flag="helpCenter">
		    		<img  class="m09_img" src="${baseUrlStatic}/images/hele_new.png" width="20" height="20" />
		    		<a href="/jsp/user/help.jsp" link="/jsp/user/help.jsp" target="_blank">帮助</a>
		    	</li>
		    	<li class="nav_li m09" flag="downloadCenter">
		    		<img  class="m09_img" src="${baseUrlStatic}/images/download_new.png" width="20" height="20" />
		    		<a href="javascript:;" link="/jsp/user/download_center.jsp" target="mainFrame">${LANG['user.menu.conf.down.center']}</a>
		    	</li>
		    </ul>
    	</li>    
  </ul>
</div>
<!--页面主体-->
<div class="main_right">
<c:if test="${isLogined}"> 
  <iframe frameborder="0" width="100%" height="100%" id="mainFrame" name="mainFrame" scrolling="no" src="/user/conf/getControlPadIndex?userRole=1#"></iframe>
</c:if>
<c:if test="${!isLogined}">
   <iframe frameborder="0" width="100%" height="100%" id="mainFrame" name="mainFrame" scrolling="no" src="/user/conf/getPublicControlPadIndex"></iframe>
</c:if>
</div>
<!--页面下部-->
<jsp:include page="footer.jsp" />


  <div style="display:none" id="reloadDiv">
  <form name="reLoadJoinConf" id="reLoadJoinConf" action="/join?" method="post" target="dialogFrame">
  	<input type="hidden" name="cId" id="cId" value=""/>
  	<input type="hidden" name="rId" id="rId" value=""/>
  	<input type="hidden" name="cPass" id="cPass" value=""/>
  	<input type="hidden" name="joinType" id="joinType" value=""/>
  	<input type="hidden" name="userName" id="userName" value=""/>
  	<input type="hidden" name="reload" id="reload" value=""/>
  	<input type="hidden" name="code" id="code" value=""/>
  		<!-- setCookie("reload","1,${cId},${userName},${joinType},${code},${cPass}",domain); -->
  </form>
  </div>
  <div id="noticeText" style="display: none;">${sysNotice.content}</div>
</body>
</html>


<script type="text/javascript">

function joinMtgFromEmail(){

	<c:if test="${joinFlag==true && joinType==JOIN_TYPE_EMAIL}">

	var joinUrl="${joinUrl}";
	joinMeetingFromEmail(joinUrl);
	</c:if>
	
}
</script>
<script type="text/javascript">
	function changeLang(langVal){
		var jumpUrl="/changeLang?lang="+langVal+"&returnURL=/";
		window.location.href=jumpUrl;
	}
	function initPage(){
		var useLang="${lang}";
		$("#lang").val(useLang); 
		
	}
	initPage();
</script>
