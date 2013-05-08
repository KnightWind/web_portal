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
			frameObj.style.height=heightNum+"px";
			//frameObj.setAttribute("height",heightNum)
		};
	}
	$("#joinMeeting").trigger("resize");
}

jQuery(function($) {
	$('body').bind(EVENT_CREATE, function(event, result, type) {
		if(type==VIEW_TYPE.bookMeeting) {
			if(result && result.status==1){
				if ($.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0") && !$.support.style) {
					showURL("/user/conf/listConf");	
		    	} else {
					jumpToMeetingPanel(result);	
		    	}
			} else {
				//show error
			}
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
				var href = $("#mainFrame").attr("src");
				if(href=="/user/conf/listConf") {
					showURL(href);	
				} else {
					href = $("#mainFrame")[0].contentWindow.$("#contentFrame").attr("src");
					showChildURL(href);	
				}
			} else {
				errorDialog(result.message);
			}
		}
	});
	$('body').bind(EVENT_UPDATE, function(event, result, type) {
		if(type==VIEW_TYPE.bookMeeting) {
			updateMeetingTR();
		}
	});

	//left menu selected
	$('.nav_menu li').click(function() {
		var loginUser = "${user}";
		if(loginUser) {
			$('.nav_menu').find("li").removeClass("nav_li_active");
			$(this).addClass("nav_li_active");
			var href = $(this).find("a").attr("link");
			$("#mainFrame").attr("src", href);	
		} else {
			login();	
		}
	});
	//left menu hover
	$('.nav_menu li').hover(function() {
		if(!$(this).hasClass("nav_li_active")){
			$(this).addClass("nav_li_hover");	
		}
	}, function() {
		$(this).removeClass("nav_li_hover");
	});
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
	$(window).bind("scroll", function(){
		var scrollTop = $(window).scrollTop();
		var width = $("body").width()-220;
		var navTop = 122;
		$("#mainFrame").width(width+20);
		if(scrollTop>navTop){
			$("#tool_bar").width(width).css({"position":"fixed"});	
		} else {
			$("#tool_bar").width(width).css({"position":"relative"});
		}
	});
	$(window).bind("resize", function() {
		resizeHeight();
		resizeFloatBar();
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
	$("#tool_bar").width(width);
	$("#mainFrame").width(width+20);
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
	var href = $("#mainFrame").attr("src");
	if(href!="/user/conf/listConf"){
		$('.nav_menu').find("li:eq(0)").addClass("nav_li_active").siblings().removeClass("nav_li_active");
		$("#mainFrame").attr("src", "/user/conf/listConf");
	} else {
		showMeetingPanel(result);	
	}
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
		$("<span onclick='toggleIcon(this)' class='m_title'>即将召开的会议</span>").appendTo(left);
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

function updateMeetingTR() {
	var href = $("#mainFrame").attr("src");
	if(href=="/user/conf/listConf") {
		showURL(href);	
	} else {
		href = $("#mainFrame")[0].contentWindow.$("#contentFrame").attr("src");
		showChildURL(href);	
	}
	
}


//refresh group list
function refreshGroupList() {
	showChildURL("/user/group/list");	
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
		"height" : 625
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
		"height" : 625
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
		"height" : 625
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
		"height" : 625
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
		"width" : 474,
		"height" : 408
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
		"height" : 207
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
	$("<div id=\"joinMeeting\"/>").showDialog({
		"title" : titleName,
		"dialogClass" : "ui-dialog-user",
		"url" : "/join/joinpage?joinType="+joinType+"&cId="+cId,
		"type" : VIEW_TYPE.joinMeeting,
		"action" : ACTION.join,
		"width" : 474,
		"height" : "auto"
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
</script>

<script type="text/javascript">

function joinMtgFromEmail(){

	<c:if test="${joinFlag==true && joinType==JOIN_TYPE_EMAIL}">

	var joinUrl="${joinUrl}";
	joinMeetingFromEmail(joinUrl);
	</c:if>
	
}
//function 
</script>
<script type="text/javascript">
	function changeLang(langVal){
		var jumpUrl="/changeLang?lang="+langVal+"&returnURL=/";
		window.location.href=jumpUrl;
	}
	function initPage(){
		var useLang="${lang}";
		//$("#lang option[value='"+useLang+"']").
		$("#lang").val(useLang); 
		
	}
	initPage();
</script>
