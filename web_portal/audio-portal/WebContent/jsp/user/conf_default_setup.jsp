<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会议缺省设置</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css?ver=${version}">

<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery-validation-1.10.0/dist/jquery.validate.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.uniform.min.js?ver=${version}"></script>
<cc:confList var="CONF_VIDEO_TYPE_FLUENCY"/>
<cc:confList var="CONF_VIDEO_TYPE_DISTINCT"/>
<cc:confList var="CONF_VIDEO_TYPE_WEBBAND"/>
<c:set var="CONF_VIDEO_TYPE_FLUENCY" value="${CONF_VIDEO_TYPE_FLUENCY }"></c:set>
<c:set var="CONF_VIDEO_TYPE_DISTINCT" value="${CONF_VIDEO_TYPE_DISTINCT }"></c:set>
<c:set var="CONF_VIDEO_TYPE_WEBBAND" value="${CONF_VIDEO_TYPE_WEBBAND }"></c:set>
<script type="text/javascript">
$(function() {
	$("#maxAudio").attr("value","${confConfig.maxAudio}");
	var maxVideoNum = "${confConfig.maxVideo}";
	var empowerVideoNum = "${userEmpower.videoNumber}";
	if(maxVideoNum>empowerVideoNum){
		maxVideoNum = empowerVideoNum;
	}
	$("#maxVideo").attr("value",maxVideoNum);
	var videoType = "${confConfig.videoType}";
	if(videoType=="${CONF_VIDEO_TYPE_FLUENCY}"){
		$("input:radio[name=videoType]:eq(0)").attr("checked",'checked');
	} else if(videoType=="${CONF_VIDEO_TYPE_DISTINCT}"){
		$("input:radio[name=videoType]:eq(1)").attr("checked",'checked');
	} else if(videoType=="${CONF_VIDEO_TYPE_WEBBAND}"){
		$("input:radio[name=videoType]:eq(2)").attr("checked",'checked');
	}
	$("#aheadTimes").val("${confConfig.aheadTimes}");
	//设置视频
	var isVideoFlag = "${isVideoFlag}"; 
	if(isVideoFlag){
		$(".videoTR").show();
	} else {
		$(".videoTR").hide();
	}
	
	//会议模式
	if("${confModel}" == 1){
		$("input:radio[name=confModel]:eq(0)").attr("checked",'checked');
	}else if("${confModel}" == 0){
		$("input:radio[name=confModel]:eq(1)").attr("checked",'checked');
	}
	
	//默认开启麦克风
	if("${micStatus}" == 1){
		$("input:radio[name=micStatus]:eq(0)").attr("checked",'checked');
	}

	//电话功能
	var isPhoneFlag = "${isPhoneFlag}";
	if(isPhoneFlag){
		var phoheFuc = "${confConfig.confType}"; 
		if( phoheFuc == "1"){
			$("input:radio[name=confType]:eq(0)").attr("checked",'checked');
		} else {
			$(".callFunc").hide();
		}
	}else{
		$(".phoneFuc").hide();
		$(".callFunc").hide();
	}

	//设置自动外呼功能
	var isAutoFlag = "${isAutoFlag}";
	if(isAutoFlag){
		$('input:radio[name="confType"]').change(function() {
			var confTypeValue = $(this).val();
			if (confTypeValue=="1") {
				$(".callFunc").show();
			} else {
				$(".callFunc").hide();
				$("input:radio[name=allowCall]:eq(1)").attr("checked",'checked');
			}
		});
		//自动外呼
		var callFunc = "${allowCall}"; 
		if(callFunc=="1"){
			$("input:radio[name=allowCall]:eq(0)").attr("checked",'checked');
		}
	}	
	
	//权限
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
	var isShareMediaFlag = "${isShareMediaFlag}";
	if(isShareMediaFlag){
		if("${shareMedia}"==1){
			$("input:checkbox[name=shareMedia]:eq(0)").attr("checked",'checked');
		}	
	} else {
		$("#shareMedia").hide();
		$("#shareMediaM").hide();
		$("input:checkbox[name=shareMedia]:eq(0)").attr("disabled",'disabled');
	}
	if("${whiteBoard}"==1){
		$("input:checkbox[name=whiteBoard]:eq(0)").attr("checked",'checked');
	}
	if("${fileTrans}"==1){
		$("input:checkbox[name=fileTrans]:eq(0)").attr("checked",'checked');
	}
	var isRecordFlag = "${isRecordFlag}";
	if(isRecordFlag) {
		if("${record}"==1){
			$("input:checkbox[name=record]:eq(0)").attr("checked",'checked');
		}	
	} else {
		$("#record").hide();
		$("#recordM").hide();
		$("input:checkbox[name=record]:eq(0)").attr("disabled",'disabled');
	}
});
</script>
<script type="text/javascript">
var confConfigForm = null;
var maxUser = parseInt("${defaultLicence}", 10);
$(function() {
	var ruleString = {
		required: {
			"maxUser": "请输入参会方数"
		},
		rangelength: {
			"maxUser": "参会方数至少2位, 最大不超过"+maxUser+"位",
			"aheadTimes": "${LANG['valid.input.aheadTimes']}"
		}
	};
	$("#confConfigForm").find("input, select").not(".skipThese").uniform();
	$("#confConfigForm :input").tipsy({ trigger: 'manual', fade: false, gravity: 'sw', opacity: 0.65 });
	
	confConfigForm = $("#confConfigForm").validate({
		onkeyup: false,
		onblur: true,
		errorClass: "warning",
		rules: {
            'maxUser' : {required: true, min:2, max:maxUser},
            'aheadTimes': {min:0, max:60}
        },
        messages: {
            'maxUser' : {required: ruleString.required.maxUser, min: ruleString.rangelength.maxUser, max: ruleString.rangelength.maxUser},
            'aheadTimes': {min: ruleString.rangelength.aheadTimes, max: ruleString.rangelength.aheadTimes}
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
});

function saveInfo() {
	if(confConfigForm.form()) {
		document.getElementsByName("confConfigForm")[0].submit();	
	}	
}

</script>
</head>
<body>
<div class="main_content">
<form name="confConfigForm" id="confConfigForm" action="/user/confConfig/updateConfConfig" method="post">
  <div class="intercalate_main_top_01">
	<h3>会议缺省设置</h3>
    <p>设置您常用的会议参数, 每次安排会议无需重复选择。</p>
  </div>
  <table class="Personal_settings_main_01" cellpadding="0" cellspacing="0" border="0" >
<!--   	<tr height="40"> -->
<!--     	<td align="right" width="160">最大参会方数</td> -->
<!--         <td align="left" class="confsetTD"> -->
<%--         	<input class="Personal_settings_01 skipThese" id="maxUser" name="maxUser" type="text"  value="${confConfig.maxUser}"/> --%>
<%--         	&nbsp;<span style="color: red">参会方数不可超过: ${defaultLicence }</span> --%>
<!--         </td> -->
<!--     </tr> -->  
    <tr height="40">
    	<td align="right" width="160">允许最大音频路数</td>
        <td align="left" class="confsetTD">
        	<select name="maxAudio" id="maxAudio" class="video_channels">
				<c:forEach var="h" begin="0" end="16" step="1">
					<option value="${h}">${h} 路</option>
				</c:forEach>
		 	</select>
	 	</td>
    </tr>
    
    <tr height="40" class="videoTR">
    	<td align="right" width="160">允许最大视频路数</td>
        <td align="left" class="confsetTD">
	        <select name="maxVideo" id="maxVideo" class="video_channels">
				<c:forEach var="h" begin="0" end="${videoNumber }" step="1">
					<option value="${h}">${h} 路</option>
				</c:forEach>
		    </select>
        </td>
    </tr>
    <tr height="40">
    	<td align="right" width="160">视频画质设置</td>
        <td align="left" class="confsetTD">
        	<ul class="video_set">
        	    <cc:confList var="CONF_VIDEO_TYPE_FLUENCY"/>
	            <cc:confList var="CONF_VIDEO_TYPE_DISTINCT"/>
	            <cc:confList var="CONF_VIDEO_TYPE_WEBBAND"/>
        		<li><input name="videoType" type="radio" value="${CONF_VIDEO_TYPE_FLUENCY }" checked="checked"/>优先保证视频流畅</li>
                <li><input name="videoType" type="radio" value="${CONF_VIDEO_TYPE_DISTINCT }" />优先保证画质清晰</li>
                <li><input name="videoType" type="radio" value="${CONF_VIDEO_TYPE_WEBBAND }" />优先保证网络带宽</li>
           </ul>
       </td>
    </tr>
    
    <tr height="40">
    	<td align="right" width="160">会议提前时间</td>
        <td align="left" class="confsetTD">
        	<input class="Personal_settings_01 skipThese" id="aheadTimes" name="aheadTimes" type="text"  value="${confConfig.aheadTimes}" />分钟</td>
    </tr>
    
     	<tr height="40">
    	<td align="right" width="160">会议模式</td>
        <td align="left" class="confsetTD">
        	<input class="Personal_settings_01" name="confModel" type="radio" value="1" checked="checked"/>主持人模式
          	<input class="Personal_settings_01" name="confModel" type="radio" value="0" />自由模式</td>
        </td>
    </tr>        
 	<tr height="40">
    	<td align="right" width="160">默认开启麦克风</td>   <!-- 麦克风状态 -->
        <td align="left" class="confsetTD">
          	<input class="Personal_settings_01" name="micStatus" type="radio" value="1" />是
          	<input class="Personal_settings_01" name="micStatus" type="radio" value="0" checked="checked"/>否
        </td>
    </tr>        
 	<tr height="40" class="phoneFuc">
    	<td align="right" width="160">电话功能</td>
        <td align="left" class="confsetTD">
          	<input class="Personal_settings_01" name="confType" type="radio" value="1" />开启
          	<input class="Personal_settings_01" name="confType" type="radio" value="0" checked="checked"/>关闭
        </td>
    </tr>        
 	<tr height="40" class="callFunc">
    	<td align="right" width="160">启动外呼</td>
        <td align="left" class="confsetTD">
          	<input class="Personal_settings_01" name="allowCall" type="radio" value="1" />开启
          	<input class="Personal_settings_01" name="allowCall" type="radio" value="0" checked="checked"/>关闭
        </td>
    </tr>  
    
    <tr height="40">
    	<td align="right" width="160">参会人权限</td>
        <td align="left" class="authority_set confsetTD">
        	<input name="changePage" type="checkbox" value="1" />换页
        	<input name="annotate" type="checkbox" value="1" />批注
            <input name="chatAnyOne" type="checkbox" value="1" />与所有人聊天
            <input name="chatCompere" type="checkbox" value="1" />与主持人聊天
            <input name="chatParticipants" type="checkbox" value="1" />与参会人聊天</td>
    </tr>
    <tr height="40">
    	<td align="right" width="160">会议功能</td>
        <td align="left"  class="authority_set01 confsetTD">
	        <input name="shareDocs" type="checkbox" value="1" />文档共享
	        <input name="shareScreen" type="checkbox" value="1" />屏幕共享
	        <input name="shareMedia" id="shareMedia" type="checkbox" value="1" /><label id="shareMediaM">媒体共享</label>
	        <input name="whiteBoard" type="checkbox" value="1" />白板
	        <input name="fileTrans" type="checkbox" value="1" />文件传输
	        <input name="record" id="record" type="checkbox" value="1" /><label id="recordM">录制</label>
        </td>
    </tr>
    <tr>
    	<td></td>
        <td>
        <div style="margin-top: 20px;">
        <a class="Personal_settings_main_hold" style="float:left;margin-top:0px;" href="javascript:;" onclick="saveInfo()">
        	<img src="/static/images/ys_r_bg.png" width="16" height="14" align="absmiddle" style=" margin-right:5px;" />保存</a>
	        	<c:if test="${!empty infoMessage}">
		 				<img src="/static/images/ys_r_bg.jpg" width="16" height="14" style="margin-left:15px;margin-top:5px;margin-right: 5px"/><label style='color:#258021'>${infoMessage}</label>
		 			</c:if>
					<c:if test="${!empty errorMessage}">
		           		<img src="/static/images/ys_w_bg.jpg" width="16" height="14" style="margin-left:15px;margin-top:5px;margin-right: 5px;"/><label style='color:red'>${errorMessage}</label>
	           	</c:if>
        </div>
        
        	</td>
        <td></td>
    </tr>
    <tr height="40">
    	<td></td>
        <td>
			&nbsp;
        </td>
        <td></td>
    </tr>
  </table>
</form>
</body>
</html>
