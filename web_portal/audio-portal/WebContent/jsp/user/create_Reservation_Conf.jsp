<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>schedule meeting</title>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css"/>	
	<style>
	.cycleEnable, .extraEnable{
		display: none;
	}
	.extra_panel {
		display: none;
	}
	.warning {
	    display: block;
	    margin-left: 1em;
	    width: auto;
	    color: red;
	}
	</style>
	<!-- Javascript -->
	<script type="text/javascript">
		var confID = "${conf.id}";
		var confHwid = "${conf.confHwid}";
		var timeZone = "${conf.timeZone}";
		var cycleID = "${conf.cycleId}";
		var validString  = {
			pageRequired:{
				confName: "请输入会议主题",
				confNameLength: "主题长度为1~32位字符",
				confDescLength: "描述长度最大为256位字符",
				confPass: "请输入会议密码",
				confPass2: "请输入会议确认密码",
				confirmPass: "确认密码不一致",
				passLength: "密码长度为6~16位",
				startDate: "请选择开始时间",
				aheadTime: "会议提前时间为0~60分钟",
				aheadTimeInt: "会议提前时间必须为整数",
				maxUser: "请输入参会方数",
				maxLicense: "参会方数最少为2人, 最大不能超过",
				maxUserNumber: "参会方数格式不正确",
				dayInterval: "请输入正确的间隔天数",
				maxInterval: "循环周期不能大于31次",
				weekInterval: "请输入正确的间隔周数",
				weekFlag: "请至少选择一个星期日",
				weekStartDate: "请选择开始日期",
				weekEndDate: "请选择结束日期",
				cycleError: "循环日期不正确",
				timeError: "开始时间不能小于当前时间",
				hour: "小时",
				minute: "分钟",
				firConfType: "数据会议功能",
				secConfType: "电话功能 ",
				thirdConfType: "视频功能 ",
				fourConfType: "视频功能、电话功能",
				firCycleType: "日循环会议",
				secCycleType: "周循环会议",
				thirdCycleType: "月循环会议",
				fourCycleType: "非周期循环会议"
			},
			maxlength: {
				confName: "会议主题不超过32字符",
				confDesc: "会议描述不超过256字符",
				duration: "会议时长不能为0"
			},
			digits: {
				masterPass: "主持人密码必须为数字"			
			}
		};
		function setDefaultTime(defaultDate) {
			var tempDate = new Date();
			tempDate.setTime(defaultDate.getTime()+10*60*1000);
			var tempYear = tempDate.getFullYear();
			var tempMonth = tempDate.getMonth()+1;
			tempMonth = tempMonth<10?"0"+tempMonth:tempMonth;
			var tempDay = tempDate.getDate();
			tempDay = tempDay<10?"0"+tempDay:tempDay;
			var tempHours = tempDate.getHours();
			var tempMins = tempDate.getMinutes();
			if(tempMins>55){
				tempDate.setTime(tempDate.getTime()+60*60*1000);
				tempYear = tempDate.getFullYear();
				tempMonth = tempDate.getMonth()+1;
				tempMonth = tempMonth<10?"0"+tempMonth:tempMonth;
				tempDay = tempDate.getDate();
				tempDay = tempDay<10?"0"+tempDay:tempDay;
				tempHours = tempDate.getHours();
				tempMins = 0;
			}
			var tempTime = tempYear+"-"+tempMonth+"-"+tempDay;
			//设置默认的分钟
			$("#startTimeM option").each(function() {
 				var cValue = $(this).val();
 				if(tempMins<=cValue) {
 					$(this).attr("selected", true);
 					return false;
 				}
			});
			//设置默认的小时
			$("#startTimeH option").each(function() {
 				var cValue = $(this).val();
 				if(cValue==tempHours) {
 					$(this).attr("selected", true);
 					return false;
 				}
			});
			
			$("#startDate").val(tempTime);
			$("#startTime").val(tempTime);
		}
	</script>
	<%@ include file="/jsp/common/cookie_util.jsp"%>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-ui-1.9.2.custom.min.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-ui-i18n.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.uniform.min.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/date.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/json2.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/util.js"></script>
	<fmt:formatDate var="serverDate" value="${defaultDate}" type="date" pattern="yyyy/MM/dd HH:mm:ss"/>
	<fmt:formatDate var="beijingDate" value="${defaultDate}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
	<fmt:formatDate var="serverTime" value="${defaultDate}" type="date" pattern="yyyy-MM-dd"/>
	<c:set var="cycleConfType" value="${cycleConfType}"></c:set>
	<cc:confList var="CONF_VIDEO_TYPE_FLUENCY"/>
    <cc:confList var="CONF_VIDEO_TYPE_DISTINCT"/>
    <cc:confList var="CONF_VIDEO_TYPE_WEBBAND"/>
    <c:set var="CONF_VIDEO_TYPE_FLUENCY" value="${CONF_VIDEO_TYPE_FLUENCY }"></c:set>
    <c:set var="CONF_VIDEO_TYPE_DISTINCT" value="${CONF_VIDEO_TYPE_DISTINCT }"></c:set>
    <c:set var="CONF_VIDEO_TYPE_WEBBAND" value="${CONF_VIDEO_TYPE_WEBBAND }"></c:set>
	<script type="text/javascript">   //该脚本为创建会议时，加载用户默认会议设置信息
		var cycleConfType = "${cycleConfType}";
		var isAutoFlag = "${isAutoFlag}";
		var defaultDate = new Date("${serverDate}"); //2013/04/07 11:57:06 
		$(function() {
			var maxLicense = "${defaultLicence}";
			$("#maxLicense").text(maxLicense);
			if(defaultDate){
				//单次的开始时间
				var weekday = defaultDate.getDay()+1;
				//设置默认的星期几
				$("#weekDay").attr("value",weekday);
				$("input[name=cycleWeekFlag]").each(function(n){
					var typeValue = $(this).val();
					if(typeValue==weekday){
						$(this).attr("checked",'checked');
					} else {
						$(this).removeAttr("checked");
					}
				});
				//设置默认的日期、小时、分钟
				setDefaultTime(defaultDate);
			}
			
			var videoType = "${defaultConfig.videoType}";
			if(videoType=="${CONF_VIDEO_TYPE_FLUENCY}"){
				$("input:radio[name=videoType]:eq(0)").attr("checked",'checked');
			} else if(videoType=="${CONF_VIDEO_TYPE_DISTINCT}"){
				$("input:radio[name=videoType]:eq(1)").attr("checked",'checked');
			} else if(videoType=="${CONF_VIDEO_TYPE_WEBBAND}"){
				$("input:radio[name=videoType]:eq(2)").attr("checked",'checked');
			}
			
			//是否有视频权限
			var isVideoFlag = "${isVideoFlag}"; 
			if(!isVideoFlag){
				$("#videoFunc").hide();
				$("#videoFuncM").hide();
				$("#videoTR").hide;
				$("input[name=confType]:eq(0)").removeAttr("checked");
			}
			
			//是否开启电话功能
			var isPhoneFlag = "${isPhoneFlag}";
			if(!isPhoneFlag) {
				$("#phoneFunc").hide();
				$("#phoneFuncM").hide();
				$("input[name=confType]:eq(1)").attr("disabled",'disabled');
				$("input[name=confType]:eq(1)").removeAttr("checked");
			} else {
				$("input[name=confType]:eq(1)").removeAttr("disabled");
			}
			if(!isVideoFlag && !isPhoneFlag){
				$(".confFuncTR").hide();
				$("#videoTR").hide();
			}
			$("#maxUser").attr("value","${defaultConfig.maxUser}");
			$("#maxAudio").attr("value","${defaultConfig.maxAudio}");
			$("#maxVideo").attr("value","${defaultConfig.maxVideo}");
			$("#aheadTime").attr("value","${defaultConfig.aheadTimes}");
			//permission
			if("${changePage}"==1){
				$("input:checkbox[name=changePage]:eq(0)").attr("checked",'checked');
			}
			if("${annotate}"==1){
				$("input:checkbox[name=annotate]:eq(0)").attr("checked",'checked');
			}
			if("${chatAnyOne}"==1){
				$("input:checkbox[name=chatAnyOne]:eq(0)").attr("checked",'checked');
			}
			if("${chatCompere}"==1){
				$("input:checkbox[name=chatCompere]:eq(0)").attr("checked",'checked');
			}
			if("${chatParticipants}"==1){
				$("input:checkbox[name=chatParticipants]:eq(0)").attr("checked",'checked');
			}
			
			//会议功能
			if("${shareDocs}"==1){
				$("input:checkbox[name=shareDocs]:eq(0)").attr("checked",'checked');
			}
			if("${shareScreen}"==1){
				$("input:checkbox[name=shareScreen]:eq(0)").attr("checked",'checked');
			}
			if("${shareMedia}"==1){
				$("input:checkbox[name=shareMedia]:eq(0)").attr("checked",'checked');
			}
			var isShareMediaFlag = "${isShareMediaFlag}";
			if(!isShareMediaFlag) {
				$("#shareMedia").hide();
				$("#shareMediaM").hide();
				$("input:checkbox[name=shareMedia]:eq(0)").attr("disabled",'disabled');
				$("input:checkbox[name=shareMedia]:eq(0)").removeAttr("checked");
			}
			
			if("${whiteBoard}"==1){
				$("input:checkbox[name=whiteBoard]:eq(0)").attr("checked",'checked');
			}
			if("${fileTrans}"==1){
				$("input:checkbox[name=fileTrans]:eq(0)").attr("checked",'checked');
			}
			if("${record}"==1){
				$("input:checkbox[name=record]:eq(0)").attr("checked",'checked');
			}
			var isRecordFlag = "${isRecordFlag}";
			if(!isRecordFlag) {
				$("#record").hide();
				$("#recordM").hide();
				$("input:checkbox[name=record]:eq(0)").attr("disabled",'disabled');
				$("input:checkbox[name=record]:eq(0)").removeAttr("checked");
			}			
			$("#endTime").watermark('结束日期');			
		});
	</script>
<!-- 若会议信息不为空，则为修改会议，加载会议信息 -->
	<c:if test="${conf != null}">    
	<fmt:formatDate var="confStartTime" value="${conf.startTime}" type="date" pattern="yyyy/MM/dd HH:mm:ss"/>
	<fmt:formatDate var="confStartDate" value="${conf.startTime}" type="date" pattern="yyyy-MM-dd"/>
	<c:set var="timeZoneDesc" value="${conf.timeZoneDesc}"></c:set>
	<script type="text/javascript">
		$(function() {
			$("#confName").val("${conf.confName}");
			$("#confDesc").val("${conf.confDesc}");
			var isPublic = "${conf.publicFlag}";
			if(isPublic=="1"){
				$("input:radio[name=allowPublic]:eq(0)").attr("checked",'checked');
				$("#confPass").val("${conf.publicConfPass}");
				$("#confirmPass").val("${conf.publicConfPass}");
				$("#confPassTR").show();
				$("#confirmPassTR").show();
			}
			var startTime = new Date("${confStartTime}");
			if(startTime && cycleConfType != "2"){
				$("#startDate").val("${confStartDate}");
				var hour = startTime.getHours();
				$("#startTimeH").attr("value",hour);
				var mis = startTime.getMinutes(); 
				$("#startTimeM").attr("value",mis);
			}
			if(cycleConfType == "1"){        //修改周期会议中所有会议的cycleConfType字段值为1
				$(".startDateDiv").hide();	 //修改周期会议中所有会议，则不可修改开始时间的日期字段，只可修改小时与分钟!
				$("input[name=cycleOption]:eq(0)").attr("checked",'checked');
				$(".cycleTypeTR").show();
				$(".cycleType").attr("disabled",'disabled');
				var cycleType = "${cycleType}";//"${cycleType}";
				if(cycleType=="1"){
					$("input[name=cycleType]:eq(0)").attr("checked",'checked');
					$("#cycleDayFlag").attr("disabled",'disabled');
					$("#cycleDayFlag").val(1);//"${cycleDayValue}"
					$(".cycleDay").show();
				} else if (cycleType=="2") {
					$("input[name=cycleType]:eq(1)").attr("checked",'checked');
					$("input[name=cycleWeekFlag]").attr("disabled",'disabled');
					var cycleWeekValue = "${cycleWeekValue}";
					cycleWeekValue = cycleWeekValue.split(",");
					$("input[name=cycleWeekFlag]").removeAttr("checked");
					for ( var i = 0; i < cycleWeekValue.length; i++) {
						var indexWeek = cycleWeekValue[i]-1;
						$("input[name=cycleWeekFlag]:eq("+indexWeek+")").attr("checked",'checked');	
					}
// 					$("input[name=cycleWeekFlag]").each(function(n){
// 						var typeValue = $(this).val();//"${cycleWeekValue}"
// 						if(typeValue==1){
// 							$(this).attr("checked",'checked');
// 						} else {
// 							$(this).removeAttr("checked");
// 						}
// 					});
					$(".cycleWeek").show();
				} else if (cycleType=="3"){
					$("input[name=cycleMonthRadio]").attr("disabled",'disabled');
					$("#eachMonthDay").attr("disabled",'disabled');
					$("#weekFlag").attr("disabled",'disabled');
					$("#weekDay").attr("disabled",'disabled');
					var monthCycleOption = "${monthCycleOption}";//"${monthCycleOption}"
					if(monthCycleOption=="1"){
						$("input[name=cycleMonthRadio]:eq(0)").attr("checked",'checked');
						$("#eachMonthDay").val("${eachMonthDay}");//"${eachMonthDay}"
					} else if(monthCycleOption=="2"){
						$("input[name=cycleMonthRadio]:eq(1)").attr("checked",'checked');
						$("#weekFlag").attr("value","${weekFlag}");//"${weekFlag}"
						$("#weekDay").attr("value","${weekDay}");//"${weekDay}"
					}
					$("input[name=cycleType]:eq(2)").attr("checked",'checked');
					$(".cycleMonth").show();
				}
			}
			var duration = parseInt("${conf.duration}", 10);
			var duraHour = duration/60>>0;
			var duraMis = duration%60;
			$("#meetingTimeH").attr("value",duraHour);
			$("#meetingTimeM").attr("value",duraMis);
			if(duraMis%5!=0){
				$("#meetingTimeM").attr("value","10");
			}
			var confType = "${conf.confType}";
			if(confType) {
				if(confType==2 || confType==3){
					$("#videoTR").attr("style","");
					$("input[name=confType]:eq(0)").attr("checked",'checked');
				}
				
				if(confType==1 || confType==3){
					var isPhoneFlag = "${isPhoneFlag}";
					if(!isPhoneFlag) {
						$("#phoneFunc").hide();
						$("#phoneFuncM").hide();
						$("input[name=confType]:eq(1)").attr("disabled",'disabled');
						$("input[name=confType]:eq(1)").attr("checked",'');	
					} else {
						$("input[name=confType]:eq(1)").attr("checked",'checked');
					}
				}
			}
			//是否有视频权限
			var isVideoFlag = "${isVideoFlag}"; 
			if(!isVideoFlag){
				$("#videoFunc").hide();
				$("#videoFuncM").hide();
				$("#videoTR").hide;
				$("input[name=confType]:eq(0)").removeAttr("checked");
			}
			
			if(!isAutoFlag){
				$("#allowCallTr").attr("style","display:none");
			} else {
				if(confType==1 || confType==3){
					$("#allowCallTr").attr("style","");
				}
			} 
			$("#maxUser").val("${conf.maxUser}");
			//会议的功能位
			var confModel = "${confModel}";
			if(confModel==1){
				$("input:radio[name=confModel]:eq(0)").attr("checked",'checked');
			} else {
				$("input:radio[name=confModel]:eq(1)").attr("checked",'checked');
			}
			var micStatus = "${micStatus}";
			if(micStatus==1){
				$("input:radio[name=micStatus]:eq(0)").attr("checked",'checked');
			} else {
				$("input:radio[name=micStatus]:eq(1)").attr("checked",'checked');
			}
			var allowCall = "${allowCall}";
			if(allowCall==1){
				$("input:radio[name=allowCall]:eq(0)").attr("checked",'checked');
			} else {
				$("input:radio[name=allowCall]:eq(1)").attr("checked",'checked');
			}
			//config
			$("#maxAudio").attr("value","${conf.maxAudio}");
			$("#maxVideo").attr("value","${conf.maxVideo}");
			var videoType = "${conf.videoType}";
			if(videoType=="${CONF_VIDEO_TYPE_FLUENCY}"){
				$("input:radio[name=videoType]:eq(0)").attr("checked",'checked');
			} else if(videoType=="${CONF_VIDEO_TYPE_DISTINCT}"){
				$("input:radio[name=videoType]:eq(1)").attr("checked",'checked');
			} else if(videoType=="${CONF_VIDEO_TYPE_WEBBAND}"){
				$("input:radio[name=videoType]:eq(2)").attr("checked",'checked');
			}
// 			if(videoType==1){
// 				$("input:radio[name=videoType]:eq(0)").attr("checked",'checked');
// 			} else if(videoType==2){
// 				$("input:radio[name=videoType]:eq(1)").attr("checked",'checked');
// 			} else {
// 				$("input:radio[name=videoType]:eq(2)").attr("checked",'checked');
// 			}
			$("#aheadTime").val("${conf.aheadTime}");
			//permission
			if("${changePage}"==1){
				$("input:checkbox[name=changePage]:eq(0)").attr("checked",'checked');
			}
			if("${annotate}"==1){
				$("input:checkbox[name=annotate]:eq(0)").attr("checked",'checked');
			}
			if("${chatAnyOne}"==1){
				$("input:checkbox[name=chatAnyOne]:eq(0)").attr("checked",'checked');
			}
			if("${chatCompere}"==1){
				$("input:checkbox[name=chatCompere]:eq(0)").attr("checked",'checked');
			}
			if("${chatParticipants}"==1){
				$("input:checkbox[name=chatParticipants]:eq(0)").attr("checked",'checked');
			}
			
			//会议功能
			if("${shareDocs}"==1){
				$("input:checkbox[name=shareDocs]:eq(0)").attr("checked",'checked');
			}
			if("${shareScreen}"==1){
				$("input:checkbox[name=shareScreen]:eq(0)").attr("checked",'checked');
			}
			if("${shareMedia}"==1){
				$("input:checkbox[name=shareMedia]:eq(0)").attr("checked",'checked');
			}
			var isShareMediaFlag = "${isShareMediaFlag}";
			if(!isShareMediaFlag) {
				$("#shareMedia").hide();
				$("#shareMediaM").hide();
				$("input:checkbox[name=shareMedia]:eq(0)").attr("disabled",'disabled');
				$("input:checkbox[name=shareMedia]:eq(0)").removeAttr("checked");
			}
			if("${whiteBoard}"==1){
				$("input:checkbox[name=whiteBoard]:eq(0)").attr("checked",'checked');
			}
			if("${fileTrans}"==1){
				$("input:checkbox[name=fileTrans]:eq(0)").attr("checked",'checked');
			}
			if("${record}"==1){
				$("input:checkbox[name=record]:eq(0)").attr("checked",'checked');
			}
			var isRecordFlag = "${isRecordFlag}";
			if(!isRecordFlag) {
				$("#record").hide();
				$("#recordM").hide();
				$("input:checkbox[name=record]:eq(0)").attr("disabled",'disabled');
				$("input:checkbox[name=record]:eq(0)").removeAttr("checked");
			}	
		});
	</script>	
</c:if>
<script type="text/javascript" src="${baseUrlStatic}/js/valid.js"></script>

</head>
<body onload="loaded()">
<form id="meetingForm" action="">
<div id="step1" class="validation_group" style="">
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" >
    <tbody>
      <tr class="no-header">
        <td class="overlay-hdL"></td>
        <td class="overlay-hdC"></td>
        <td class="overlay-hdR"></td>
      </tr>
      <tr>
        <td class="overlay-bdL"></td>
        <td class="overlay-content">
        <!--弹出层主题内容区域开始========================================================================-->
			<div class="First_Steps">
			    <div class="First_Steps_title"> 
			    	<a href="javascript:;" class="closeButton"></a> 
			    	<img class="nar" src="/static/images/nar.png" width="261" height="18"/>
			    	<c:if test="${!empty conf }">
			      		<h3 class="tit">${LANG['user.menu.conf.update']}</h3>
			    	</c:if>
			    	<c:if test="${empty conf }">
			      		<h3 class="tit">${LANG['user.menu.conf.schedule']}</h3>
			    	</c:if>
			      <ul class="nar_text">
			        <li style=" margin-left:">${LANG['system.menu.info.meeting']}会议信息</li>
			        <li style=" margin-left:30px;">${LANG['user.menu.conf.time.info']}</li>
			        <li style=" margin-left:35px;">${LANG['user.menu.conf.parameter.info']}参数信息</li>
			        <li style=" margin-left:50px;">${LANG['system.complete']}</li>
			      </ul>
			    </div>
			    <div class="toa"></div>
			    <div class="First_Steps_top">
			      <h3>第一步：会议基本信息</h3>
			      <p>我们将带领您通过一些简单的步骤来安排您的会议。</p>
			    </div>
			    <div class="First_Steps_main" style="height: 270px;">
			      <table class="box_a">
			        <tr class="box01">
			          <td align="right" class="left_text"><span class="red_star">*</span>会议主题</td>
			          <td align="left" class="td_space"><input class="right_text skipThese"  id="confName" name="confName" type="text" value=""/></td>
			        </tr>
					<tr class="box01">
						<td align="right" class="left_text">公开会议</td>
						<td align="left"  class="td_space">
							<input id="allowPublicYes" class="cursor right_radio01 allowPublic" name="allowPublic" type="radio" value="1" /> 
							<label class="cursor" for="allowPublicYes">是</label> 
							<input id="allowPublicNo" class="cursor right_radio01 allowPublic" name="allowPublic" type="radio" value="2" checked="checked"/>
							<label class="cursor" for="allowPublicNo">否</label>
						</td>
					</tr>
					<tr class="box01 publicPassSet" style="display: none;">
						<td align="right" class="left_text">设置会议密码</td>
						<td align="left"  class="td_space">
							<input class="right_radio01 passSetRadio" name="passSetRadio" type="radio" value="1" /> 是 
							<input class="right_radio01 passSetRadio" name="passSetRadio" type="radio" value="2" checked="checked"/> 否
						</td>
					</tr>
					<tr class="box01" style="display: none;" id="confPassTR">
			          <td  align="right" class="left_text"><span class="red_star">*</span>会议密码</td>
			          <td align="left" class="td_space"><input class="right_text skipThese" id="confPass" name="confPass" type="password"  value=""/></td>
			        </tr>
					<tr class="box01" style="display: none;" id="confirmPassTR">
			          <td  align="right" class="left_text"><span class="red_star">*</span>确认会议密码</td>
			          <td align="left" class="td_space"><input class="right_text skipThese" id="confirmPass" name="confirmPass" type="password"  value=""/></td>
			        </tr>        
			        <tr class="box02">
			          <td  align="right" valign="top" style="padding-top:5px">会议描述</td>
			          <td align="left" class="td_space" style="padding-top:5px">
			          	<textarea class="textarea_step1 skipThese"  id="confDesc" name="confDesc" cols="" rows="" style="text-indent: 5px;"></textarea>
			          </td>
			        </tr>
			      </table>
			    </div>
			    <div class="First_Steps_bottom">
			      <div class="but01"><a href="javascript:;" class="closeButton"><img src="/static/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:10px"/>取&nbsp;&nbsp;消</a></div>
			      <div class="but02"><a href="javascript:;" class="next" pageIndex="1"><img src="/static/images/next_bg.png" width="9" height="11" align="absmiddle" style=" margin-right:5px; margin-left:10px;" />下一步</a></div>
			    </div>
			</div>
        <!--弹出层主题内容区域开始========================================================================-->
        </td>
        <td class="overlay-bdR"></td>
      </tr>
      <tr>
        <td class="overlay-ftL"></td>
        <td class="overlay-ftC"></td>
        <td class="overlay-ftR"></td>
      </tr>
    </tbody>
  </table>
</div>
<div id="step2" class="validation_group"  style="display: none;">
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" >
    <tbody>
      <tr class="no-header">
        <td class="overlay-hdL"></td>
        <td class="overlay-hdC"></td>
        <td class="overlay-hdR"></td>
      </tr>
      <tr>
        <td class="overlay-bdL"></td>
        <td class="overlay-content">
        <!--弹出层主题内容区域开始========================================================================-->
			<div class="First_Steps">
			   	  <div class="First_Steps_title"> <a href="javascript:;" class="closeButton"></a>
			       <img class="nar" src="/static/images/nar02.jpg" width="261" height="18" />
			    	<c:if test="${!empty conf }">
			      		<h3 class="tit">修改会议</h3>
			    	</c:if>
			    	<c:if test="${empty conf }">
			      		<h3 class="tit">安排会议</h3>
			    	</c:if>
			        <ul class="nar_text">
			        	<li style=" margin-left:">会议信息</li>
			            <li style=" margin-left:30px;">时间信息</li>
			            <li style=" margin-left:35px;">参数信息</li>
			            <li style=" margin-left:50px;">完成</li>
			        </ul>  
			        </div>
			        <div class="toa"></div>
			        <div class="First_Steps_top">
			        	<h3>第二步：会议时间信息</h3>
			            <p>设置您开会的日期与时间。</p>
			          
			        </div>
			        <div class="First_Steps_main" style="height: 270px;">
			          <table class="box_a" style="">
			            	<tr class="box01">
			                	<td align="right" class="left_text">重复周期</td>
			                    <td align="left" class="td_space">
			                    	<input class="cycleOption" name="cycleOption" type="radio" value="1" <c:if test="${!empty conf}">disabled</c:if>/><span>是</span>
			                    	<input class="cycleOption"  name="cycleOption" type="radio" value="2" checked="checked" <c:if test="${!empty conf}">disabled</c:if>/><span>否</span>
			                    </td>
			                </tr>
							<cc:confList var="WEEK_DAYS"/>
							<cc:confList var="CONF_CYCLE_DAILY"/>
							<cc:confList var="CONF_CYCLE_WEEKLY"/>
							<cc:confList var="CONF_CYCLE_MONTHLY"/>                
			                <tr class="box01 cycleEnable cycleTypeTR">
			                	<td align="right" class="left_text">定期模式</td>
			                    <td align="left" class="td_space">
				                    <input class="cycleType" name="cycleType" type="radio" value="${CONF_CYCLE_DAILY}" <c:if test="${confCycle.cycleType== CONF_CYCLE_DAILY}"> checked </c:if> /><span>按天</span>
				                    <input class="cycleType"  name="cycleType" type="radio" value="${CONF_CYCLE_WEEKLY}" <c:if test="${empty confCycle || confCycle.cycleType== CONF_CYCLE_WEEKLY}"> checked </c:if> /><span>按周</span>
				                    <input class="cycleType"  name="cycleType" type="radio" value="${CONF_CYCLE_MONTHLY}" <c:if test="${confCycle.cycleType== CONF_CYCLE_MONTHLY}"> checked </c:if> /><span>按月</span>
			                    </td>
			                </tr>
			                <!--按天周期开始-->
			                <tr class="box01 cycleDay" style=" margin-top:14px; display:none" >
			                	<td  align="right" class="left_text">填写天数</td>
			                    <td align="left" class="td_space">
			                    	每<input class="right_text06 skipThese" id="cycleDayFlag" name="cycleDayFlag" type="text" value="1"/>天
			                    </td>
			                </tr>
			                <!--按天周期结束-->
			                
			                <!--按周的周期开始-->
			                <tr class="box01 cycleEnable cycleWeek" style=" margin-top:14px; display:none" >
			                	<td  align="right" class="left_text">选择星期</td>
			                    <td align="left" class="wert td_space">
				                    <c:forEach var="eachDay" items="${WEEK_DAYS}">
										<c:set var="week_day_lang" value="conf.cycle.weekly.${eachDay}"/>
										<input name="cycleWeekFlag" type="checkbox" value="${eachDay}"/>
										<label class="radio-label">${LANG[week_day_lang]}</label>
						 			</c:forEach>
			                    </td>
			                </tr>
			                 <!--按周的周期结束-->
			                 
			                <!--按月的周期开始-->
			                <tr class="cycleMonth" style=" margin-top:14px;display: none;">
			                	<td  align="right" valign="top" class="left_text">选择日期</td>
			                    <td align="left" class="td_space">
			                    	<p style="margin-bottom: 5px;" class="monthCycleDayTR">
			                    		<input name="cycleMonthRadio" type="radio" value="1"/>
										每月第<input class="right_text03 skipThese" type="text" id="eachMonthDay" name="eachMonthDay" value="1"/>天
			                    	</p>
			                    	<p class="monthCycleWeekTR">
										<input name="cycleMonthRadio" type="radio" value="2"  checked="checked"/>
										每月第
										<select class="right_text03" id="weekFlag" name="weekFlag">
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
										</select>个
										<select name="weekDay" id="weekDay">
											<c:forEach var="eachDay" items="${WEEK_DAYS}">
												<c:set var="week_day_lang" value="conf.cycle.weekly.${eachDay}"/>
												<option value="${eachDay}">${LANG[week_day_lang]}</option>
											</c:forEach>
										</select>                    	
			                    	</p>
			                    </td>
			                </tr>
			                <!--按月的周期结束-->
			                <tr class="box01 cycleEnable cycleTimeTr" style=" margin-top:14px;" >
			                	<td align="right" class="left_text">重复范围</td>
			                    <td align="left" class="td_space">
			                    	<div style="position: relative;float: left;">
			                    		<input class="right_text05 skipThese" id="startTime" name="startTime" type="text" readonly="readonly" value=""/>
			                    	</div>
			                    	<span style="margin-left: 10px;float: left;margin-top: 5px;">——</span>
			                    	<div style="position: relative;float: left;">
			                    		<input class="right_text07 skipThese" id="endTime" name="endTime" type="text"  readonly="readonly" value="" />
			                    	</div>
			                    	<div style="clear:both"></div> 
			                    </td>
			                </tr>            
			                <tr class="startDateTr">
			                <td align="right" class="left_text"></td>
			                <td class="td_space">
			                <span style="color:#EB6D00"  class="li01" title="${beijingDate}">会议时区:
			                <c:if test="${empty timeZoneDesc }">
			                	${currentUser.timeZoneDesc }时间&nbsp;
			                </c:if>
			                <c:if test="${!empty timeZoneDesc }">
			                	${timeZoneDesc }时间&nbsp;
			                </c:if>
			                 <span id="site_time">${beijingDate}</span> 
			                </span></td>
			                </tr>
			               	<tr class="startDateTr">
								<td align="right" class="left_text"><span class="red_star">*</span>开始时间</td>
								<td class="td_space">
									<div class="startDateDiv" style="float: left;margin-right:10px;position: relative;">
										<c:if test="${!empty conf && !empty conf.startTime}">
											<fmt:formatDate var="defaultDate" value="${conf.startTime}" />
										</c:if>
										<input class="right_text05 skipThese" id="startDate" name="startDate" type="text" value="" style="width:100px;"/>
									</div>
									<select name="startTimeH" id="startTimeH">
										<c:forEach var="h" begin="00" end="23" step="1">
											<option value="${h}" >${h} 点</option>
										</c:forEach>
									</select>
									<span style="margin-left: 5px;"></span>
									<select name="startTimeM" id="startTimeM">
										<c:forEach var="m" begin="00" end="55" step="5">
											<option value="${m}">${m} 分</option>
										</c:forEach>
									</select>
									
								</td>
							</tr>
			                 <tr style=" margin-top:14px;">
			                	<td align="right" class="left_text"><span class="red_star">*</span>持续时间</td>
			                    <td align="left" class="td_space">
			  						<select name="meetingTimeH" id="meetingTimeH">
										<c:forEach var="h" begin="00" end="23" step="1">
											<option value="${h}" <c:if test="${h==1}">selected="selected"</c:if>>${h} 小时</option>
										</c:forEach>
									</select>                  
									<select name="meetingTimeM" id="meetingTimeM">
										<c:forEach var="m" begin="00" end="55" step="5">
											<option value="${m}">${m} 分钟</option>
										</c:forEach>
									</select>
			                    </td>
			                </tr>
			                 
			            </table>
			            </div>
						<div class="First_Steps_bottom">
			      <div class="but01"><a href="javascript:;" class="closeButton"><img src="/static/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:10px"/>取&nbsp;&nbsp;消</a></div>
			      <div class="but02"><a href="javascript:;" class="prev"  pageIndex="2"><img src="/static/images/prev_bg.png" width="9" height="11" align="absmiddle" style=" margin-right:5px; margin-left:10px;" />上一步</a></div>
			      <div class="but02"><a href="javascript:;" class="next"  pageIndex="2"><img src="/static/images/next_bg.png" width="9" height="11" align="absmiddle" style=" margin-right:5px; margin-left:10px;" />下一步</a></div>
			    </div>
			</div>			
        <!--弹出层主题内容区域开始========================================================================-->
        </td>
        <td class="overlay-bdR"></td>
      </tr>
      <tr>
        <td class="overlay-ftL"></td>
        <td class="overlay-ftC"></td>
        <td class="overlay-ftR"></td>
      </tr>
    </tbody>
  </table>
</div>
<div id="step3" class="validation_group" style="display: none;">
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" >
    <tbody>
      <tr class="no-header">
        <td class="overlay-hdL"></td>
        <td class="overlay-hdC"></td>
        <td class="overlay-hdR"></td>
      </tr>
      <tr>
        <td class="overlay-bdL"></td>
        <td class="overlay-content">
        <!--弹出层主题内容区域开始========================================================================-->
			<div class="First_Steps_gg">
			    <div class="First_Steps_title" style=" background:#FFF"> <a href="javascript:;" class="closeButton"></a> <img class="nar" src="/static/images/nar03.jpg" width="261" height="18" />
			    	<c:if test="${!empty conf }">
			      		<h3 class="tit">修改会议</h3>
			    	</c:if>
			    	<c:if test="${empty conf }">
			      		<h3 class="tit">安排会议</h3>
			    	</c:if>
			      <ul class="nar_text">
			        <li style=" margin-left:">会议信息</li>
			        <li style=" margin-left:30px;">时间信息</li>
			        <li style=" margin-left:35px;">参数信息</li>
			        <li style=" margin-left:50px;">完成</li>
			      </ul>
			    </div>
			    <div style=" background:#fff"><img class="toa" src="/static/images/min.jpg" width="621" height="1" /></div>
			    <div class="First_Steps_top" style=" background:#FFF">
			      <h3>第三步：会议参数信息</h3>
			      <p>设置您会议的功能参数。</p>
			    </div>
			    <div class="step3_content" style="height: 280px;overflow: hidden;">
				    <div class="First_Steps_main extend_option">
				      <table class="box_a">
			<!-- 	        <tr class="box01" > -->
			<!-- 	          <td  align="right" class="left_text"><span class="red_star">*</span>最大参会方数</td> -->
			<!-- 	          <td align="left" class="td_space"> -->
			<!-- 	          	<input class="right_text skipThese" id="maxUser" name="maxUser" type="text" value="5" style="width: 150px"/> &nbsp;&nbsp;<span style="color: red">参会方数不可超过: <span id="maxLicense"></span></span> -->
			<!-- 	          </td> -->
			<!-- 	        </tr>	       -->
				        <tr class="box01 confFuncTR" >
				          <td align="right" class="left_text">会议类型</td>
				          <td align="left" class="td_space">
				          	<input class="confType" name="confType" type="checkbox" id="videoFunc" value="2" checked="checked"/><span id="videoFuncM">视频功能</span>
				          	<input class="confType" name="confType" type="checkbox" id="phoneFunc" value="1"/><span id="phoneFuncM">电话功能</span>
			          	  </td>
				        </tr>
				        <tr class="box01" id="allowCallTr" style="display: none;">
				          <td  align="right" class="left_text">启动外呼</td>
				          <td align="left" class="td_space">
				          	<input class=""  name="allowCall" type="radio" value="1" />开启
				          	<input class=""  name="allowCall" type="radio" value="0" checked="checked"/>关闭
				          </td>
				        </tr>	        
				        <tr class="box01">
				          <td  align="right" class="left_text">会议模式</td>
				          <td align="left" class="td_space">
				          	<input class=""  name="confModel" type="radio" value="1" checked="checked"/>主持人模式
				          	<input class=""  name="confModel" type="radio" value="0" />自由模式</td>
				        </tr>
				         <tr class="box01">
				          <td  align="right" class="left_text">默认开启麦克风</td>
				          <td align="left" class="td_space">
				          	<input class=""  name="micStatus" type="radio" value="1" />是
				          	<input class=""  name="micStatus" type="radio" value="0" checked="checked"/>否
				          </td>
				        </tr>
				      </table>
				    </div>
				    <div style="padding-top: 10px;">
				          <span class="gaoji" style="font-size: 14px;font-weight: bold;cursor: pointer;padding-right: 5px;">
				          	高级参数<img src="/static/images/y_gaoji.jpg" style="width:7px;height:11px;margin-left:5px" alt="" id="gaojiicon"/></span>
				          
				    </div>
				    <div class="extra_panel">
				       <table class="box_a">
				        <tr class="box01">
				          <td align="right" class="left_text">最大音频路数</td>
				          <td align="left" class="td_space">
					          <select name="maxAudio" id="maxAudio">
								<c:forEach var="h" begin="0" end="16" step="1">
									<option value="${h}">${h} 路</option>
								</c:forEach>
							  </select>
							</td>
				        </tr>
				        <tr class="box01" id="videoTR">
				          <td  align="right" class="left_text">最大视频路数</td>
				          <td align="left" class="td_space">
				          	<select name="maxVideo" id="maxVideo">
								<c:forEach var="h" begin="0" end="${videoNumber }" step="1">
									<option value="${h}">${h} 路</option>
								</c:forEach>
							</select>
				         </td>
				        </tr>
				        <tr class="box01">
				          <td  align="right" class="left_text">视频画质设置</td>
				          <td align="left" class="td_space">
				          <cc:confList var="CONF_VIDEO_TYPE_FLUENCY"/>
				          <cc:confList var="CONF_VIDEO_TYPE_DISTINCT"/>
				          <cc:confList var="CONF_VIDEO_TYPE_WEBBAND"/>
				          	<input class="leixing"  name="videoType" type="radio" value="${CONF_VIDEO_TYPE_FLUENCY }" checked="checked"/>优先保证视频流畅
				          	<input class="leixing"  name="videoType" type="radio" value="${CONF_VIDEO_TYPE_DISTINCT }" />优先保证画质清晰
				          	<input class="leixing"  name="videoType" type="radio" value="${CONF_VIDEO_TYPE_WEBBAND }" />优先保证网络带宽</td>
				        </tr>
				         <tr class="box01" >
				          <td  align="right" class="left_text">会议提前时间</td>
				          <td align="left" class="td_space">
				          	<input class="right_text skipThese" id="aheadTime" name="aheadTime" type="text" value="10" style="width: 100px"/> 分钟
				          </td>
				        </tr>
				         <tr class="box01">
				          <td  align="right" class="left_text">参会人权限</td>
				          <td align="left" class="td_space">
				          	<input class="extraPermission" name="changePage" type="checkbox" value="1" />换页
				        	<input class="extraPermission" name="annotate" type="checkbox" value="1" />批注
				            <input class="extraPermission" name="chatAnyOne" type="checkbox" value="1" />与所有人聊天
				            <input class="extraPermission" name="chatCompere" type="checkbox" value="1" />与主持人聊天
				            <input class="extraPermission" name="chatParticipants" type="checkbox" value="1" />与参会人聊天
				          </td>
				        </tr>
				         <tr class="box01">
				          <td  align="right" class="left_text">会议功能</td>
				          <td align="left" class="td_space">
					        <input class="clientOption" name="shareDocs" type="checkbox" value="1" />文档共享
					        <input class="clientOption" name="shareScreen" type="checkbox" value="1" />屏幕共享
					        <input class="clientOption" name="shareMedia" type="checkbox" id="shareMedia" value="1" /><span id="shareMediaM">媒体共享</span>
					        <input class="clientOption" name="whiteBoard" type="checkbox" value="1" />白板
					        <input class="clientOption" name="fileTrans" type="checkbox" value="1" />文件传输
					        <input class="clientOption" name="record" type="checkbox" id="record" value="1" /><span id="recordM">录制</span>
				          </td>
				        </tr>
				        
				      </table>    
				    </div>    
			    </div>
			    <div class="First_Steps_bottom">
			      <div class="but01"><a href="javascript:;" class="closeButton"><img src="/static/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:10px"/>取&nbsp;&nbsp;消</a></div>
				  <div class="but02"><a href="javascript:;" class="prev"  pageIndex="2"><img src="/static/images/prev_bg.png" width="9" height="11" align="absmiddle" style=" margin-right:5px; margin-left:10px;" />上一步</a></div>
			      <div class="but02"><a href="javascript:;" class="next"  pageIndex="3"><img src="/static/images/next_bg.png" width="9" height="11" align="absmiddle" style=" margin-right:5px; margin-left:10px;" />完&nbsp;&nbsp;成</a></div>
			    </div>
			</div>			
        <!--弹出层主题内容区域开始========================================================================-->
        </td>
        <td class="overlay-bdR"></td>
      </tr>
      <tr>
        <td class="overlay-ftL"></td>
        <td class="overlay-ftC"></td>
        <td class="overlay-ftR"></td>
      </tr>
    </tbody>
  </table>
</div>

<div id="step4" class="" style="display:none;">
  <table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" >
    <tbody>
      <tr class="no-header">
        <td class="overlay-hdL"></td>
        <td class="overlay-hdC"></td>
        <td class="overlay-hdR"></td>
      </tr>
      <tr>
        <td class="overlay-bdL"></td>
        
        <!--弹出层主题内容区域开始========================================================================-->
        
        <td class="overlay-content" style="width:660px; height: 440px;background:#FFF;" > 
          <div class="First_Steps_title" style=" background:#FFF"> 
          		<a href="javascript:createSuccess();"></a>
          		<img class="nar" src="/static/images/nar04.jpg" width="261" height="18" />
              <h3 class="tit">安排会议</h3>
              <ul class="nar_text">
                <li style=" margin-left:">会议信息</li>
                <li style=" margin-left:30px;">时间信息</li>
                <li style=" margin-left:35px;">参数信息</li>
                <li style=" margin-left:50px;">完成</li>
              </ul>
          </div>
          <div style=" background:#fff"><img class="toa" src="/static/images/min.jpg" width="621" height="1" /></div>
          <div class="overlay-top">
          	<img src="/static/images/agree.png" width="52" height="41" />
          	<h3>预约会议成功！</h3>
          </div>
          <div class="overlay-main">
          	<div class="overlay-main_left">
            	<table border="0" cellpadding="0" cellspacing="0" id="overlay-main_txt">
<!--             	 <tr class="cycleTypeTR"> -->
<!--             	<td height="30">周期类型：</td> -->
<!--             	<td><span id="cycleTypeSpan"></span></td> -->
<!--             </tr> -->
<!--             <tr class="cycleTimeTRD"> -->
<!--             	<td height="30">周期时间：</td> -->
<!--             	<td><span id="cycleTimeSpan"></span></td> -->
<!--             </tr> -->
                	<tr>
                    	<td class="overlay_title">会议主题:</td>
                    	<td class="overlay_content"><span id="confNameSpan"></span></td>
                    </tr>
                   
                    <tr class="cycleTypeTR">
                    	<td class="overlay_title">定期模式:</td>
                    	<td class="overlay_content"><span id="cycleTypeSpan"></span></td>
                    </tr>
                    <tr class="cycleTimeTRD">
                    	<td class="overlay_title">重复范围:</td>
                    	<td class="overlay_content"><span id="cycleTimeSpan"></span></td>
                    </tr>
                    <tr>
                    	<td class="overlay_title">持续时间:</td>
                    	<td class="overlay_content"><span id="durationSpan"></span></td>
                    </tr>
                     <tr class="confTypeTR">
                    	<td class="overlay_title">会议类型:</td>
                    	<td class="overlay_content"><span id="confTypeSpan"></span></td>
                    </tr>
<!--                      <tr> -->
<!--                     	<td class="overlay_title">最大参会方数:</td> -->
<!--                     	<td class="overlay_content"><span id="maxUserSpan"></span></td> -->
<!--                     </tr> -->
                     <tr>
                    	<td class="overlay_title">与会者安全会议号:</td>
                    	<td class="overlay_content"><span id="userSecureSpan"></span></td>
                    </tr>
                </table>
            </div>
            <img class="add_li" src="/static/images/add_li_bg.png" width="5" height="265" />
            <div class="overlay-main_right">
           	 	<input type="hidden" id="confIdForEmail" value=""/>
            	<h3>我的日历提醒:</h3>
                <p>您可以通过点击下面的按钮, 将此会议添加到邮件日历中。</p>
                <a class="overlay_email01" href="javascript:sendNoticeEmail();" style="margin-left: 180px;"><img src="/static/images/email05.png" width="16" height="12" align="absmiddle" style=" padding-right:5px;"/>Outlook</a>
                <a class="overlay_email02" href="javascript:sendNoticeEmail();" style="display: none;"> <img src="/static/images/ico002.png" width="16" height="11" align="absmiddle" style=" padding-right:5px;" />Gmail</a>
                <a class="overlay_email03" href="javascript:sendNoticeEmail();" style="display: none;"><img src="/static/images/ico003.png" width="14" height="17" align="absmiddle" style=" padding-right:5px;" />Foxmail</a>
	            <div style="clear: left;"></div>
				<h3 style="clear: left;">邀请与会者:</h3>
				<p>您可以立即通知用户参与此会议或可以稍后在会议列表中点击“邮件图标”来通知用户</p>
				<a class="overlay_email01" href="javascript:inventContact();" style="margin-left: 180px;"><img src="/static/images/yaoqing.png" width="16" height="12" align="absmiddle" style=" padding-right:5px;"/>邀请</a>
                <a class="overlay_email04" href="javascript:createSuccess();"><img src="/static/images/ys_r_bg.png" width="16" height="14" align="absmiddle" style=" padding-right:5px;"/>确定</a>
<!--                 <a class="overlay_email05" href="#"><img src="/static/images/ico004.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;" />修改</a> -->

            </div>
          </div>
          <div class="overlay_bottom" style=" height:40px;"></div>
          
        </td>
        
        <!--弹出层主题内容区域开始========================================================================-->
        
        <td class="overlay-bdR"></td>
      </tr>
      <tr>
        <td class="overlay-ftL"></td>
        <td class="overlay-ftC"></td>
        <td class="overlay-ftR"></td>
      </tr>
    </tbody>
  </table>
</div>
</form>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#bookMeeting");
	frame.trigger("loaded");
}
</script>
