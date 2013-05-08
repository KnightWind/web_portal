<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>主会议列表</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/conf_list.css?ver=${version}"/>

<fmt:formatDate var="serverDate" value="${defaultDate}" type="date" pattern="yyyy/MM/dd HH:mm:ss"/>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-1.8.3.min.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-ui-1.9.2.custom.min.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js?ver=${version}"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js?ver=${version}"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></script>

<script type="text/javascript">

function initJoinConf(){
	parent.parent.joinMeetingReload();
}
initJoinConf();
/*
 * 查看会议详情
 */
function viewConf(id){
	parent.parent.viewConf(id);
}

function updateBookMeeting(id) {
	parent.parent.createReservationConf(id);
}
function updateAllBookMeeting(id) {
	parent.parent.updateReservationConf(id);
}
//删除单个周期会议
function delSignleConf(id) {
	parent.parent.delACycleConf(id);
}
function editInventContact(id) {
	parent.parent.editInventContact(id);
}
/*
 * 重新创建会议
 */
function reCreateReservationConf(id) {
	parent.parent.reCreateReservationConf(id);
}
/*
 * 修改周期会议中所有会议的信息
 * updateCycleBookMeetingInfo()
 */
function updateCycleBookMeetingInfo(id) {
	parent.parent.updateCycleMeetingInfo(id);
}

function sendNoticeEmail(id) {
	var userId = "${user.id}";
	if(userId){
		var data = {};
		data.confId = id;
		app.sendNoticeEmail(data, function(result) {
			if(result){
				if(result.status && result.status==1){
					parent.parent.successDialog(result.message);
				} else {
					parent.parent.errorDialog(result.message);
				}
			}
		},{message:"正在添加日历提醒...", ui:parent});		
	} else {
		parent.parent.addCalendar(id);
	}
}
function toggleExtra(elem) {
	$(".extras-action").slideUp("fast");
	$(".extras-default").removeClass("extras-actived");	
	var extrasTR = $(elem).closest(".extras-tr").next();
	var display = extrasTR.is(":visible"); 
	if(display==true){
		$(".fader").hide();
		extrasTR.slideUp("fast");
		$(elem).removeClass("extras-actived");	
	} else {
		$(".fader").show();
		extrasTR.slideDown("fast");
		$(elem).addClass("extras-actived");
	}
}

function toggleIcon(elem) {
	var icon = $(elem);
	if(!$(elem).hasClass("panel_icon")){
		icon = $(elem).prev();
	}
	var selector = $(elem).closest(".tab_title").next();
	togglePanel(selector, icon, function(){
		parent.refreshIHeight();	
	});	
}
function faderClick(elem) {
	$(".extras-action").slideUp();
	$(".extras-default").removeClass("extras-actived");
	$(".fader").hide();	
}

function showInfoView(info,id,event){
	var myEvent = event || window.event;
	var targetObj = myEvent.srcElement || myEvent.target;
	var offset = $(targetObj).offset();
	
	$("#"+id).find(".mySpan").html("主持人："+info);
	//$("#"+id).css("left", myEvent.clientX-120 + "px").css("top",myEvent.clientY-60 + "px");
	$("#"+id).css("z-index", "99").css("left", offset.left-60 + "px").css("top",offset.top-60 + "px");
	$("#"+id).css("visibility", "");
}
function hidenInfoView(id){
	$("#"+id).css("visibility", "hidden");
	showflag = false;
}
var showflag = true;
var confInfos = new Object();
function showInfos(confId,id,event){
	var myEvent = event || window.event;
	showflag = true;
	if(confInfos[confId]){
		var time = new Date().getTime()-confInfos[confId].time;
		if(time>30000){
			refreshData(confId,id,myEvent);
		}else{
			var data = confInfos[confId].datas;
			showDivOp(id,data,myEvent);
		}
	}else{
		refreshData(confId,id,myEvent);
	}
}

function showDivOp(id,data,myEvent){
	var targetObj = myEvent.srcElement || myEvent.target;
	var offset = $(targetObj).offset();
	$("#"+id).find(".bgview").empty();
	$("#"+id).find(".bgview").append("<div  style=\"padding-top: 6px; width:100%;\"></div>");
	if(data.length==0){
		$("#"+id).find(".bgview").append("<div><span class=\"mySpan\" style=\"line-height: 25px;\">"+'尚无参会者'+"</span></div>");
	}
	for(var i=0;i<data.length;i++){
		$("#"+id).find(".bgview").append("<div><span class=\"mySpan\" style=\"line-height: 25px;\">"+data[i].name+"</span></div>");				
	}
	if(showflag){
		$("#"+id).css("z-index", "999").css("visibility", "").css("left", offset.left+40 + "px").css("top",offset.top-90 + "px");
	}else{
		$("#"+id).css("visibility", "hidden");
	}
}

function refreshData(confId,id,myEvent){
	$.ajax({
      	type: "POST",
      	url:"/user/conf/getConfInviteUser",
      	data:{confId:confId},
      	dataType:"json",
      	success:function(data){
			if(data){
				var conf = new Object();
				conf["time"] = new Date().getTime();
				conf["datas"] = data;
				confInfos[confId] = conf;
				showDivOp(id,data,myEvent);
			}else{
				alert('Server inner error!');
			}
      	},
        error:function(XMLHttpRequest, textStatus, errorThrown) {
        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
        }
	});  
}

function joinMeeting(joinType,cId){//cId,cPass,code){
	parent.parent.joinMeeting(joinType,cId);
} 
function delConf(confId){
	parent.parent.confirmDialog("确认取消会议？",function(){
		$.ajax({
	      	type: "POST",
	      	url:"/user/conf/delete/"+confId,
	      	dataType:"json",
	      	success:function(data){
				if(data){
					 window.location.reload(true);
				}else{
					parent.parent.errorDialog('取消会议出现异常！');
				}
	      	},
	        error:function(XMLHttpRequest, textStatus, errorThrown) {
	        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
	        }
		}); 
	});	
}
/*
 * 所有企业用户都可以隐藏已错过的会议
 */
function hideConf(confId){
	parent.parent.confirmDialog("确认隐藏会议？",function(){
		$.ajax({
	      	type: "POST",
	      	url:"/user/conf/hide/"+confId,
	      	dataType:"json",
	      	success:function(data){
				if(data){
					 window.location.reload(true);
				}else{
					parent.parent.errorDialog('隐藏会议出现异常！');
				}
	      	},
	        error:function(XMLHttpRequest, textStatus, errorThrown) {
	        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
	        }
		}); 
	});	
}

function delAllConf(cycId,confId){
	parent.parent.confirmDialog("确认取消整个周期会议？",function(){
		$.ajax({
	      	type: "POST",
	      	url:"/user/conf/deleteCycleConfInfo/"+cycId+"?confId="+confId,
	      	dataType:"json",
	      	success:function(data){
				if(data){
					 window.location.reload(true);
				}else{
					parent.parent.errorDialog('取消周期会议失败！');
				}
	      	},
	        error:function(XMLHttpRequest, textStatus, errorThrown) {
	        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
	        }
		}); 
	});
}

function inventContact(confId){
	parent.parent.inventContact(confId);
// 	//alert("邀请");	inviteFirst.jsp
// 	var url = "/user/contact/goInviteContacts";
// 	var features = "height=670, width=990, top=100, left=150,toolbar=no, menubar=no, scrollbars=no, resizable=yes, location=yes, status=yes";
// 	window.open(url,"inviteMeeting",features);	
}

function refreshTips() {
	switchDuration();
	switchTime();
	$('.timeago').tipsy({ trigger: 'hover', fade: false, gravity: 'sw', opacity: 0.65 });
	$('.attendee').tipsy({ html: true, trigger: 'hover', fade: false, gravity: 'w', opacity: 0.65 });
	$('.viewtip').tipsy({ trigger: 'hover', fade: false, gravity: 's', opacity: 0.65 });
}

var $l = {
	seconds: "不到 1 分钟",
	minute: "大约 1 分钟",
	minutes: "%d 分钟",
	hour: "大约 1 小时",
	hours: "大约 %d 小时",
	day: "1 天",
	days: "%d 天",
	month: "大约 1 个月",
	months: "%d 月",
	year: "大约 1 年",
	years: "%d 年"
};
function substitute(string, number) {
    return string.replace(/%d/i, number);
}
function getwords(distanceMillis) {
    var seconds = Math.abs(distanceMillis) / 1000;
    var minutes = seconds / 60;
    var hours = minutes / 60;
    var days = hours / 24;
    var years = days / 365;
    var words = seconds < 45 && substitute($l.seconds, Math.round(seconds)) ||
        seconds < 90 && substitute($l.minute, 1) ||
        minutes < 45 && substitute($l.minutes, Math.round(minutes)) ||
        minutes < 90 && substitute($l.hour, 1) ||
        hours < 24 && substitute($l.hours, Math.round(hours)) ||
        hours < 42 && substitute($l.day, 1) ||
        days < 30 && substitute($l.days, Math.round(days)) ||
        days < 45 && substitute($l.month, 1) ||
        days < 365 && substitute($l.months, Math.round(days / 30)) ||
        years < 1.5 && substitute($l.year, 1) ||
        substitute($l.years, Math.round(years));
        return words;
}
function switchDuration() {
	$(".durationTime").each(function(index, elem){
		var status = $(elem).attr("status");
		var duration = $(elem).attr("duration");
			duration = parseInt(duration, 10);
		var duraHour = duration/60>>0;
		var duraMis = duration%60;
		var duraText = "";
		if(duraHour>0){
			duraText += duraHour+" 小时";
		}
		if(duraMis>0){
			duraText += duraMis+" 分钟";
		} 
		if(!duraText){
			duraText = "0 分钟";
		}
		$(elem).find(".durationMin").text(duraText);
// 		if(status && status=="run"){
// 			var dateEndStr = $(elem).attr("endtime").replace(/-/g,"/");
// 			var endDate = new Date(dateEndStr);
// 			var currentDate = new Date("${serverDate}");
// 		    var distanceMillis = endDate.getTime()-currentDate.getTime();
// 		    var words = getwords(distanceMillis);
		    //过去的会议
// 		    var prefix = "";
// 		    var suffix = "后会议结束";  
// 		    var title = prefix+words+suffix;
// 		    var title = prefix+duraText+suffix;
// 		    $(elem).attr("title", title);			
// 		} else {
		    var prefix = "会议时长:";
		    var suffix = "";
		    var title = prefix+duraText+suffix;
			$(elem).attr("title", title);
// 		}
	});
}
function switchTime() {
	$(".timeago").each(function(index, elem) {
		var dateStartStr = $(elem).attr("starttime").replace(/-/g,"/");
		var dateEndStr = $(elem).attr("endtime").replace(/-/g,"/");
		var status = $(elem).attr("status");

		var currentDate = new Date("${serverDate}");
		var startDate = new Date(dateStartStr);
		var endDate = new Date(dateEndStr);
		switchWords(currentDate, startDate, endDate, elem);
		switchTitle(currentDate, startDate, endDate, elem, status);
	});
}

function switchWords(currentDate, startDate, endDate, elem) {
	console.log("currentDate="+currentDate);
	var currentYear = currentDate.getFullYear();
	var currentMonth = currentDate.getMonth();
	var startYear = startDate.getFullYear();
	var startMonth = startDate.getMonth();
	if(currentYear==startYear && currentMonth==startMonth){	
		var currentDay = currentDate.getDate();
		var startDay = startDate.getDate();
		if((startDay-currentDay)==0){
			$(elem).find(".date").text("今天");
		} else if((startDay-currentDay)==1) {
			$(elem).find(".date").text("明天");
		} else if((startDay-currentDay)==-1) {
			$(elem).find(".date").text("昨天");
		} else {
			$(elem).find(".date").text(formatDate(startDate));
		}
	} else {
		$(elem).find(".date").text(formatDate(startDate));
	}
}
function switchTitle(currentDate, startDate, endDate, elem, status) {
    //过去的会议
    var prefix = "";
    var suffix = "前加入的会议";    
    if(status=="come"){
    //即将开始的会议   
    	prefix = "";
        suffix = "后开始会议";
    } else if(status=="run"){
    //正在进行的会议
    	prefix = "会议已开始";
        suffix = "";    
    } else if(status=="miss"){
    //错过的会议
    	prefix = "";
        suffix = "前错过的会议";    
    }
    var distanceMillis = currentDate.getTime()-startDate.getTime();
    var words = getwords(distanceMillis);
    var title = prefix+words+suffix;
    $(elem).attr("title", title);
}

function quickSearch(elem) {
	var searchValue = $(elem).val();
	var listPanel = $(elem).closest(".tab_title").next();
	var noResult = listPanel.find(".empty_result"); 
	var container = listPanel.find(".extras-container");
	if(searchValue) {
		var findList = $(container).hide().filter(":contains('" + searchValue + "')");
		if(findList.length>0){
			noResult.hide();
			findList.show();
		} else {
			noResult.show();
		}
	} else {
		noResult.hide();
		$(container).show();
	}
}
//查询会议参会人
jQuery(function($) {
	refreshTips();	
	showPassConf();
	getParticipantInfo();
	$(".m_search").watermark('会议主题、参会人');
	$(".has_more").click(function() {
		var hasMoreBtn = $(this);
		hasMoreBtn.hide();
		var loading = $(this).prev();
		var hasContainer = $(this).closest(".has_more_container");
		loading.show();
		var data = {};
		data.userRole = parent.$("#conf_user_role").val();
		data.dateScopeFlag = parent.$("#date_scope_flag").val();
		data.confStatus = $(this).attr("status");
		data.pageNo = parseInt($(this).attr("pageNo"), 10);
		app.loadMoreConf(data,function(result) {
			if(result.status){
				loading.find("img").hide();
				loading.find("span").text(result.message);			
			} else {
				hasMoreBtn.attr("pageNo", data.pageNo+1);
				loading.hide();
				hasMoreBtn.show();
				hasContainer.before(result);
			}
			parent.refreshIHeight();
			refreshTips();
		});
	});
	parent.refreshIHeight();
});
function showPassConf() {
	var checked = parent.$("#conf_check_box").attr("checked");
	if(!checked){
		$("#pass_conf_container").hide();
	} else {
		$("#pass_conf_container").show();
	}	
}

function getParticipantInfo(){
	$("div[name=attendeer]").each(function(){
		var confId = $(this).attr("confId");
		var self = $(this);
	    var html = "";
		$.ajax({
	      	type: "POST",
	      	url:"/user/conf/getConfInviteUser",
	      	data:{confId:confId},
	      	dataType:"json",
	      	success:function(data){
				html ="尚未邀请人";
				if(data){
					var renshu = "<img src=\"${baseUrlStatic}/images/ico24.png\" width=\"20\" height=\"20\" align=\"absmiddle\" />";
					if(data.length>0){
						html = "";
					}
					renshu += data.length;
					$(self).empty().html(renshu);
					for(var i=0;i<data.length&&i<5;i++){
						if(data[i]){
							html +=data[i].name+"<br/>";
						}
					}
				}else{
					alert('Server inner error!');
				}
				$(self).attr("original-title",html);
	      	},
	        error:function(XMLHttpRequest, textStatus, errorThrown) {
	        	//alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
	       		html = "查询参会人失败！";
	       		$(self).attr("original-title",html);
	        }
		});  
	});
}
window.setInterval(refreshTips, 60000);
</script>
</head>
<body>
<DIV id="showHostInfo" class="mycontarin" style="visibility:hidden;">
			<div class="bgview" align="center">
				<div style="padding-top: 26px;"><p><span class="mySpan"></span></p></div>
			</div>
</DIV>
<DIV id="showMenberList" class="mycontarin1" style="visibility: hidden;">
		<div class="bgview" style="background-image: url('/static/images/u227_normal.png'); height: 178px;" align="center">
			 
		</div>
</DIV>
<div class="main_content" id="main_container" style="margin: 0px;">
<fmt:setLocale value="en-us"/>

 <!--正在进行的的会议-->
<c:if test="${fn:length(dringConfList)>0 }">
<div class="conf_list_container">
	<div class="tab_title">
		<div align="left" style="float: left;width:45%">
	      	<span class="panel_icon panel_icon_off" onclick="toggleIcon(this)">&nbsp;</span>
	      	<span class="m_title" onclick="toggleIcon(this)">
	      	<c:choose>
	      		<c:when test="${user != null}">
	      			正在进行
	      		</c:when>
	      		<c:otherwise>
	      			正在进行
	      		</c:otherwise>
	      	</c:choose>
	      	</span>
	  		<div class="number_taxt">
	       		<div class="no_left"></div>
	            <div class="no_center"><span class="meeting-count">${dringConfRows}</span></div>
	            <div class="no_right"></div>
	       	</div>      		
		</div>
		<div align="right" style="float: right;width:50%;display: none;">
	      	<form action=""><input class="m_search" name="" type="text"  onblur="quickSearch(this)" onkeyup="quickSearch(this)"/></form>	
		</div>
	</div>
	<div id="running_list" class="tab_content" style="display: block;">
	<c:if test="${fn:length(dringConfList)<=0}">
		<div class="empty_result">抱歉，没有正在进行的会议</div>
	</c:if>
	<div class="empty_result" style="display: none;">抱歉，没有找到符合条件的会议</div>
	<c:forEach var="dringConf" items="${dringConfList}" varStatus="dringStatus">
	<div class="extras-container">
		<div class="extras-tr">
	      	<div class="date_part">
	      	  <fmt:formatDate var="runStartTime" value="${dringConf.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
	      	  <fmt:formatDate var="runEndTime" value="${dringConf.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
	          <div class="timeago date-holder" title="${runStartTime}" startTime="${runStartTime}" endTime="${runEndTime}" status="run"> 
	          	<span class="date">
	          		<fmt:formatDate value="${dringConf.startTime}" pattern="yyyy-MM-dd" />
	          	</span> 
	          	<span class="time">
	          		<fmt:formatDate value="${dringConf.startTime}" pattern="HH:mm"/></span> 
	          	</div>
	        </div>
	        <div class="text_part" align="left">
	        	<div class="date-holder_01"> 
	        		<span class="title">
	        		<a href="javascript:;" onclick="viewConf(${dringConf.id})">${dringConf.confName}
	        			<c:forEach var="confCyc" items="${cycs}">
			        			<c:if test="${confCyc.id eq dringConf.cycleId}">
			        				<c:choose>
			        					<c:when test="${confCyc.cycleType eq '1'}">
			        						<span style="color:#47ADE9;">(日会议)</span>
			        					</c:when>
			        					<c:when test="${confCyc.cycleType eq '2'}">
			        						<span style="color:#47ADE9;">(周会议)</span>
			        					</c:when>
			        					<c:when test="${confCyc.cycleType eq '3'}">
			        						<span style="color:#47ADE9;">(月会议)</span>
			        					</c:when>
			        				</c:choose>
			        			</c:if>
		        		</c:forEach>
	        		</a></span> 
	        		<span class="content"><a href="javascript:;" onclick="viewConf(${dringConf.id})">${dringConf.confDesc}</a></span>
	        	</div>
	        </div>
			<div class="action_part">
				<table  border="0" align="right" cellpadding="0" cellspacing="0">
          			<tr>
          				<td><div class="k02 viewtip durationTime" title="会议时长:${dringConf.duration}分钟" duration="${dringConf.duration}" endTime="${runEndTime}" status="run"><img src="${baseUrlStatic}/images/clockbtn.png" width="20" height="20" align="absmiddle" /><div class="k02_span durationMin">${dringConf.duration}分钟</div></div></td>
			            <td><div class="k02 k09 viewtip" title="主持人${dringConf.compereName}"><img src="${baseUrlStatic}/images/mac.png" width="18" height="21" align="absmiddle" /><div class="k02_span">${dringConf.compereName}</div></div></td>
<%--			            <td><div name="attendeer" confId="${dringConf.id}" class="k04 attendee" title=""><img src="${baseUrlStatic}/images/renshu.png" width="20" height="20" align="absmiddle" />${dringConf.maxUser}</div></td>--%>
			            <td><div class="k04 attendee" title="在线人数：${dringConf.pcNum+dringConf.phoneNum}"><img src="${baseUrlStatic}/images/renshu.png" width="20" height="20" align="absmiddle" />${dringConf.pcNum+dringConf.phoneNum}</div></td>
			            <td><div class="extras-default viewtip"  title="查看会议选项" onclick="toggleExtra(this)"><a href="javascript:;"><img src="${baseUrlStatic}/images/caozuo.png" width="20" height="20" /></a></div></td>
			            <td><div class="k06" onclick="javascript:joinMeeting(1,'${dringConf.id}');">
			            		<c:if test="${user.id != dringConf.compereUser}">
			            			<a href="javascript:;">加入会议</a>
			            		</c:if>
			            		<c:if test="${user.id eq dringConf.compereUser}">
			            			<a href="javascript:;">进入会议</a>
			            		</c:if>
			            </div></td>
          			</tr>
        		</table>
			</div>
			<span class="fader" onclick="faderClick(this)"></span>		
		</div>
   		<div class="extras-action">
   			<div align="right" style="padding-top:10px">
	   			<a class="email05" href="javascript:;" onclick="viewConf(${dringConf.id});"><img src="${baseUrlStatic}/images/xiangq.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;" />查看详情</a>
   			</div>
   		</div>		
	</div>	
	</c:forEach>
	<c:if test="${fn:length(dringConfList)==10 }">
	<div  class="has_more_container">
		<div style="display: none;width: 180px;margin: 0 auto;position: relative;text-align: left;">
			<img src="/static/images/loading.gif" alt="" />
			<span style="position: absolute;left: 35px;">正在加载中...</span>
		</div>
		<a class="has_more" href="javascript:;" status="1" pageNo="2">点击显示更多信息</a>
	</div>
	</c:if>
</div> 	
</div>   
</c:if>
 <!--即将开始的的会议-->
<%-- <c:if test="${fn:length(upcomingConfList)>0 }">  --%>
<div class="conf_list_container">
<div class="tab_title">
	<div align="left" style="float: left;width:45%">
      	<span class="panel_icon panel_icon_off" onclick="toggleIcon(this)">&nbsp;</span>
      	<span class="m_title" onclick="toggleIcon(this)">
	      	<c:choose>
	      		<c:when test="${user != null}">
			      	即将开始
	      		</c:when>
	      		<c:otherwise>
	      			即将开始
	      		</c:otherwise>
	      	</c:choose>
      	</span>
  		<div class="number_taxt">
       		<div class="no_left"></div>
            <div class="no_center"><span class="meeting-count">${UpcomingConfRows}</span></div>
            <div class="no_right"></div>
       	</div>	
	</div>
	<div align="right" style="float: right;width:50%;display: none;">
      	<form action=""><input class="m_search" name="" type="text"  onblur="quickSearch(this)" onkeyup="quickSearch(this)"/></form>	
	</div>
</div>
<div id="coming_list" class="tab_content" style="display: block;">
	<c:if test="${fn:length(upcomingConfList)<=0}">
		<div id="no_data" class="no_meeting">
		  	<div class="no_meeting_box">
		    	<p>您好，您当前没有即将开始的会议</p>
		    	<c:if test="${empty user}">
		    		<a href="javascript:;" onclick="parent.parent.createReservationConf()">预约会议</a>
		    	</c:if>
		    	<c:if test="${!empty user && user.userRole==1 }">
		    		<a href="javascript:;" onclick="parent.parent.createReservationConf()">预约会议</a>
		    	</c:if>
		    </div>
		</div>
	</c:if>
	<div class="empty_result" style="display: none;">抱歉，没有找到符合条件的会议</div>
  	<c:forEach var="upcomingConf" items="${upcomingConfList}" varStatus="upcomingStatus">
  	<div class="extras-container">
		<div class="extras-tr">
			<div class="date_part">
				<fmt:formatDate var="comingStartTime" value="${upcomingConf.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
				<fmt:formatDate var="comingEndTime" value="${upcomingConf.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
	          <div class="timeago date-holder" title="${comingStartTime}" startTime="${comingStartTime}" endTime="${comingEndTime}" status="come"> 
		        <span class="date">
   		      		<fmt:formatDate value="${upcomingConf.startTime}" pattern="yyyy-MM-dd" />
	          	</span>
	          	<span class="time">
	          		<fmt:formatDate value="${upcomingConf.startTime}" pattern="HH:mm"/>
	          	</span> 
	          </div>
	        </div>
	        <div class="text_part" align="left">
	        	<div class="date-holder_01"> 
	        		<span class="title">
	        		<a href="javascript:;" onclick="viewConf(${upcomingConf.id})">${upcomingConf.confName}
	        			<c:forEach var="confCyc" items="${cycs}">
			        			<c:if test="${confCyc.id eq upcomingConf.cycleId}">
			        				<c:choose>
			        					<c:when test="${confCyc.cycleType eq '1'}">
			        						<span style="color:#47ADE9;">(日会议)</span>
			        					</c:when>
			        					<c:when test="${confCyc.cycleType eq '2'}">
			        						<span style="color:#47ADE9;">(周会议)</span>
			        					</c:when>
			        					<c:when test="${confCyc.cycleType eq '3'}">
			        						<span style="color:#47ADE9;">(月会议)</span>
			        					</c:when>
			        				</c:choose>
			        			</c:if>
		        		</c:forEach>		
	        		</a>
	        		</span> 
	        		<span class="content"><a href="javascript:;" onclick="viewConf(${upcomingConf.id})">${upcomingConf.confDesc}</a></span>
	        	</div>
	        </div>
			<div class="action_part">
				<table  border="0" align="right" cellpadding="0" cellspacing="0">
	          		<tr>
	          			<td class="" ><!-- 邀请（主持人专用） -->
	          				<c:if test="${user.id eq upcomingConf.compereUser}">
	          					<div class="k_f" onclick="inventContact(${upcomingConf.id})"><img src="/static/images/yaoqing.png" width="21" height="16" align="absmiddle" style=" padding-right:5px;" />邀请</div>
	          				</c:if>
	          			</td>
	          			<td><div class="k02 viewtip durationTime" title="会议时长:${upcomingConf.duration}分钟" duration="${upcomingConf.duration}"><img src="${baseUrlStatic}/images/clockbtn.png" width="20" height="20" align="absmiddle" /><div class="k02_span durationMin">${upcomingConf.duration}分钟</div></div></td>
	            		<td><div class="k02 k09 viewtip" title="主持人${upcomingConf.compereName}"><img src="${baseUrlStatic}/images/zhuchuren.png" width="20" height="20" align="absmiddle" /><div class="k02_span">${upcomingConf.compereName}</div></div></td>
	            		<td><div onclick="<c:if test="${!empty user}">editInventContact('${upcomingConf.id}')</c:if>" name="attendeer" confId="${upcomingConf.id}" class="k04 attendee" title=""><img src="${baseUrlStatic}/images/ico24.png" width="20" height="20" align="absmiddle" /></div></td>
	            		<td><div title="查看会议选项" class="viewtip extras-default"  onclick="toggleExtra(this)"><a href="javascript:;"><img src="${baseUrlStatic}/images/caozuo.png" width="20" height="20" /></a></div></td>
	            		<td>
		            		<div class="k06" onclick="javascript:joinMeeting(1,'${upcomingConf.id}');">
			            		<c:if test="${user.id != upcomingConf.compereUser}">
			            			<a href="javascript:;">加入会议</a>
			            		</c:if>
			            		<c:if test="${user.id eq upcomingConf.compereUser}">
			            			<a href="javascript:;">开始会议</a>
			            		</c:if>
		            		</div>
	            		</td>
	          		</tr>
        		</table>
			</div>
			<span class="fader" onclick="faderClick(this)"></span>		
		</div>
   		<div class="extras-action">
				<div align="left" style="padding-top:10px">
					<a class="email01" href="javascript:sendNoticeEmail(${upcomingConf.id});"><img src="${baseUrlStatic}/images/email05.png" width="16" height="12" align="absmiddle" style=" padding-right:5px;"/>Outlook</a>
					<a class="email02" href="javascript:sendNoticeEmail(${upcomingConf.id});" style="display: none;"> <img src="${baseUrlStatic}/images/ico002.png" width="16" height="11" align="absmiddle" style=" padding-right:5px;" />Gmail</a>
					<a class="email03" href="javascript:sendNoticeEmail(${upcomingConf.id});" style="display: none;"><img src="${baseUrlStatic}/images/ico003.png" width="14" height="17" align="absmiddle" style=" padding-right:5px;" />Foxmail</a>
				</div>
				<div align="right">
					<c:if test="${user.id eq upcomingConf.createUser}">
						 <c:choose>
						 	<c:when test="${upcomingConf.cycleId!=0}">
								<a class="email04" href="javascript:;" onclick="delAllConf(${upcomingConf.cycleId},${upcomingConf.id});">
									<img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;"/>取消全部</a>
								<a class="email04" href="javascript:;" onclick="delSignleConf(${upcomingConf.cycleId});">
									<img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;"/>取消</a>
							 	<a class="email05" href="javascript:;" onclick="updateCycleBookMeetingInfo(${upcomingConf.id})">
									<img src="${baseUrlStatic}/images/ico004.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;"/>修改全部</a>	
								<a class="email05" href="javascript:;" onclick="updateAllBookMeeting(${upcomingConf.cycleId})">
									<img src="${baseUrlStatic}/images/ico004.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;"/>修改</a>
						 	</c:when>
						 	<c:otherwise>
						 		<a class="email04" href="javascript:;" onclick="delConf(${upcomingConf.id});">
									<img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;"/>取消</a>
						 		<a class="email05" href="javascript:;" onclick="updateBookMeeting(${upcomingConf.id})">
								<img src="${baseUrlStatic}/images/ico004.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;"/>修改</a>	
						 	</c:otherwise>
						 </c:choose>
					 </c:if>
				</div>
		</div>	  	
  	</div>	
	</c:forEach>
	<c:if test="${fn:length(upcomingConfList)==10 }">
	<div  class="has_more_container">
		<div style="display: none;width: 180px;margin: 0 auto;position: relative;text-align: left;">
			<img src="/static/images/loading.gif" alt="" />
			<span style="position: absolute;left: 35px;">正在加载中...</span>
		</div>
		<a class="has_more" href="javascript:;" status="2" pageNo="2">点击显示更多信息</a>
	</div>
	</c:if>
</div>
</div> 
<%-- </c:if>  --%>
<!--参加过的会议-->
	<div class="conf_list_container" id="pass_conf_container" style="display: none;">
		<div class="tab_title">
			<div align="left" style="float: left;width:45%">
		      	<span class="panel_icon panel_icon_off" onclick="toggleIcon(this)">&nbsp;</span>
		      	<span class="m_title" onclick="toggleIcon(this)">已参加</span>
		       	<div class="number_taxt">
		       		<div class="no_left"></div>
		            <div class="no_center"><span class="meeting-count">${AttendedConfRows}</span></div>
		            <div class="no_right"></div>
		       	</div>     		
			</div>
			<div align="right" style="float: right;width:50%;display: none;">
		      	<form action=""><input class="m_search" name="" type="text"  onkeyup="quickSearch(this)" style="display: none;"/></form>	
			</div>
		</div>
		<div id="passing_list" class="tab_content" style="display: block;">
			<c:if test="${fn:length(attendedConfList)<=0}">
				<div id="no_data" class="no_meeting">
				  	<div class="no_meeting_box">
				    	<p>您好，您当前没有已参加的会议</p>
				    	<c:if test="${empty user}">
				    		<a href="javascript:;" onclick="parent.parent.createReservationConf()">预约会议</a>
				    	</c:if>
				    	<c:if test="${!empty user && user.userRole==1 }">
				    		<a href="javascript:;" onclick="parent.parent.createReservationConf()">预约会议</a>
				    	</c:if>
				    </div>
				</div>
			</c:if>
		    <c:forEach var="attendedConf" items="${attendedConfList}" varStatus="attendedStatus">
		    <div class="extras-container">	
				<div class="extras-tr">
					<div class="date_part">
						<fmt:formatDate var="passStartTime" value="${attendedConf.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
						<fmt:formatDate var="passEndTime" value="${attendedConf.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
			            <div class="timeago date-holder" title="${passStartTime}" startTime="${passStartTime}" endTime="${passEndTime}" status="pass"> 
			            	<span class="date">
			            		<fmt:formatDate value="${attendedConf.startTime}" pattern="yyyy-MM-dd" />
			            	</span> 
			            	<span class="time">
			            		<fmt:formatDate value="${attendedConf.startTime}" pattern="HH:mm"/>
			            	</span> 
			            </div>
			        </div>
			        
			        <div class="text_part" align="left">
			        	<div class="date-holder_01"> 
				        	<span class="title">
					        	<a href="javascript:;" onclick="viewConf(${attendedConf.id})">
					        		${attendedConf.confName}
					        		<c:forEach var="confCyc" items="${cycs}">
					        			<c:if test="${confCyc.id eq attendedConf.cycleId}">
					        				<c:choose>
					        					<c:when test="${confCyc.cycleType eq '1'}">
					        						<span style="color:#47ADE9;">(日会议)</span>
					        					</c:when>
					        					<c:when test="${confCyc.cycleType eq '2'}">
					        						<span style="color:#47ADE9;">(周会议)</span>
					        					</c:when>
					        					<c:when test="${confCyc.cycleType eq '3'}">
					        						<span style="color:#47ADE9;">(月会议)</span>
					        					</c:when>
					        				</c:choose>
					        			</c:if>
					        		</c:forEach>
					        	</a>
				        	</span>
			        	 	<span class="content">
			        	 		<a href="javascript:;" onclick="viewConf(${attendedConf.id})">${attendedConf.confDesc}</a>
			        	 	</span>
			        	 </div>
			        </div>
					<div class="action_part" style="float: right;">
						<table  border="0" align="right" cellpadding="0" cellspacing="0">
				          <tr>
				           	<td><div class="k02 viewtip durationTime" title="会议时长:${attendedConf.duration}分钟"  duration="${attendedConf.duration}"><img src="${baseUrlStatic}/images/clockbtn.png" width="20" height="20" align="absmiddle" /><div class="k02_span durationMin">${attendedConf.duration}分钟</div></div></td>
				            <td><div class="k02 k09 viewtip" title="主持人${attendedConf.compereName}"><img src="${baseUrlStatic}/images/zhuchuren.png" width="20" height="20" align="absmiddle" /><div class="k02_span">${attendedConf.compereName}</div></div></td>
				            <td><div  name="attendeer" confId="${attendedConf.id}" class="k04 attendee" title=""><img src="${baseUrlStatic}/images/renshu.png" width="20" height="20" align="absmiddle" />${attendedConf.maxUser}</div></td>
				            <td><div class="extras-default viewtip" title="查看会议选项"  onclick="toggleExtra(this)"><a href="javascript:;"><img src="${baseUrlStatic}/images/caozuo.png" width="20" height="20" /></a></div></td>
				              <td>
				            	<div class="k06">
				            		<c:if test="${user.id eq attendedConf.createUser}">
				            			<a href="javascript:;" onclick="reCreateReservationConf(${attendedConf.id})">重新创建</a>
			              			</c:if>
				            	</div>
				           	  </td>
				          </tr>
				        </table>
					</div>
					<span class="fader" onclick="faderClick(this)"></span>		
				</div>
		   		<div class="extras-action">
					<div align="right" style="padding-top:10px">
						<c:if test="${user.id eq attendedConf.createUser}">
		   					<a class="email04" href="#" onclick="delConf(${attendedConf.id});"><img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;" />删除</a>
		   				</c:if>
		   				<a class="email05" href="javascript:;" onclick="viewConf(${attendedConf.id});"><img src="${baseUrlStatic}/images/xiangq.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;" />查看详情</a>
		   			</div>
				</div>
			</div>	
			</c:forEach>
			<c:if test="${fn:length(attendedConfList)==10 }">
				<div  class="has_more_container">
					<div style="display: none;width: 180px;margin: 0 auto;position: relative;text-align: left;">
						<img src="/static/images/loading.gif" alt="" />
						<span style="position: absolute;left: 35px;">正在加载中...</span>
					</div>
					<a class="has_more" href="javascript:;" status="3" pageNo="2">点击显示更多信息</a>
				</div>
			</c:if>
		</div>    
	</div>
<!--参加过的会议结束-->	   
</div>
<!--   space -->
<div style="height: 80px;width: 500px">&nbsp;</div>
  
</body>

</html>