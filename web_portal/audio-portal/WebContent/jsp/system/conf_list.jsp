<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css">
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/common.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/style.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/huiyixinxi.css"/>

<%@ include file="/jsp/common/cookie_util.jsp"%>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.uniform/jquery.uniform.js"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/development-bundle/ui/minified/i18n/jquery-ui-i18n.min.js"></SCRIPT>
<script type="text/javascript" src="/static/js/util.js"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/tipsy-master/src/javascripts/jquery.tipsy.js"></script>

<script type="text/javascript"> 
$(function() {
	
	$("div[name=attend_user]").tipsy({ html: true, trigger: 'hover', fade: false, gravity: 'e', opacity: 1 });
	//for search input style
	$("#confForm").find("input, select").not(".skipThese").uniform();
	
	//table tr background highlight
	$('#site-list tr').hover(function() {
			$(this).addClass('tr-hover');
		}, function() {
			$(this).removeClass('tr-hover');
	});
	
	//show or hide search input
	$(".gaoji").toggle(function () {
	    $("#search-condition").slideDown(function() {
		    parent.resizeHeight();//${LANG['bizconf.jsp.admin.conf_list.res1']}
	    });
	}, function () {
		$("#search-condition").slideUp(function() {
			parent.resizeHeight();//${LANG['bizconf.jsp.admin.conf_list.res2']}
		});
	});
	var lang = getBrowserLang();
	if (lang=="zh-cn") {
		$.datepicker.setDefaults( $.datepicker.regional[ "zh-CN" ] );
	} else {
		$.datepicker.setDefaults( $.datepicker.regional[ "en-GB" ] );
	}
	$( ".expireDate" ).datepicker({
		changeMonth: true,
		changeYear: true,
		dateFormat: "yy-mm-dd",
		showOn: "both",
		buttonImage: "/static/images/calendar.jpg",
		buttonImageOnly: true
	});
	
	$("#advanceSearch").click(function(){
		resetPageNo();
    	confForm.action="/system/conf/listWithCondition";
    	confForm.submit();	
	});

	$("#toSearch").click(function(){
		resetPageNo();
		confForm.action="/system/conf/list";
    	confForm.submit();	
	});
});

function checkForm(){
	$("#titleOrSiteSign").val("${titleOrSiteSign}");
	var titleOrSiteSign = $("#titleOrSiteSign").val();
	if(titleOrSiteSign != null && titleOrSiteSign != ""){
		confForm.action="/system/conf/list";
	}
}

function enterSumbit(url){  
    var event=arguments.callee.caller.arguments[0]||window.event;//${LANG['bizconf.jsp.admin.conf_list.res3']}   
    if (event.keyCode == 13){       //${LANG['bizconf.jsp.admin.conf_list.res4']}
    	resetPageNo();
    	confForm.action=url;
    	confForm.submit();	
    }   
} 


$(document).ready(function(){
	$("#titleOrSiteSign").watermark('${LANG['bizconf.jsp.system.conf_list.res1']}');
	$("#search-condition").find("input[type=text]").each(function(){
		if($(this).val()){
			$("#search-condition").show();
			return false;
		}
	});
	$("#search-condition").find("select").each(function(){
		if($(this).val()>0 && $(this).val()<99){
			$("#search-condition").show();
			return false;
		}		
	});
});
</script>



<title>site info</title>
</head>
<body>
<form id="confForm" name="confForm" action="/system/conf/listWithCondition" method="post" onsubmit="checkForm();">
	<input class="skipThese"  type="hidden" name="sortField" id="sortField" value="${sortField}"/>
	<input class="skipThese"  type="hidden" name="sortord" id="sortord" value="${sortord}"/>
 <div class="m_top">
	 <input name="titleOrSiteSign" id="titleOrSiteSign" class="search_input" type="text" value="${titleOrSiteSign }" onkeydown='enterSumbit("/system/conf/list")'/>
	 <input name="" class="searchs_button skipThese" type="button" id="toSearch" />
	  <a class="gaoji" title="${LANG['system.advancedSearch']}" href="javascript:;">${LANG['system.advancedSearch']}</a>
 </div>   	
<div id="search-condition" style="display: none; width:100%; height: auto;margin-left: 20px;">
    		<div style="height:30px;">
	    		<label>${LANG['system.site.list.CompanyName']}: </label>
	    		<input type="text" name="siteName" id="siteName" value="${siteName}" style="width:262px;" onkeydown='enterSumbit("/system/conf/listWithCondition")'/>
    		</div>
    		<div style="height:30px;clear: left">
	    		<label>${LANG['system.site.list.SiteSign']}: </label>
	    		<input type="text" name="siteSign" id="siteSign" value="${siteSign}" style="width:262px;" onkeydown='enterSumbit("/system/conf/listWithCondition")'/>
    		</div>
    		<div style="height:30px;clear: left">
	    		<label>${LANG['system.list.meeting.title']}: </label>
	    		<input type="text" name="confName" id="siteSign" value="${confName}" style="width:262px;" onkeydown='enterSumbit("/system/conf/listWithCondition")'/>
    		</div>
    		<div style="height:30px;clear: left">
	    		<label>${LANG['system.list.meeting.type']}: </label>
	    		<select name="confType" id="confType">
					<cc:confList var="CONF_TYPES"/>
			   		<c:forEach var="eachType" items="${CONF_TYPES}"  varStatus="itemStatus">
			   			<c:set var ="typeName" value="conf.type.${eachType }"/>
			   			<option value="${eachType}" <c:if test="${confType==eachType}">selected</c:if>>${LANG[typeName]}</option>
			   		</c:forEach>
	    		</select>
	    		<label style="margin:4px 11px">${LANG['system.list.meeting.status']}: </label>
	    		<select name="confStatus" id="confStatus">
					<cc:confList var="CONF_STATUS"/>
			   		<c:forEach var="eachType" items="${CONF_STATUS}"  varStatus="itemStatus">
			   			<c:set var ="typeName" value="conf.status.${eachType }"/>
			   			<option value="${eachType}" <c:if test="${confStatus==eachType}">selected</c:if>>${LANG[typeName]}</option>
			   		</c:forEach>
	    		</select>
    		</div>
    		<div style="height:30px;clear: left">
	    		<label>${LANG['system.list.meeting.start.time']}: </label>
	    		<input type="text" name="effeDateStart" id="effeDateStart" value="${effeDateStart}" style="width:100px;" class="expireDate"/>
	    		<label style="margin-top:4px;margin-left: 15px;margin-right: 18px;">---</label>
	    		<input type="text" name="effeDateEnd" id="effeDateEnd" value="${effeDateEnd}" style="width:100px;" class="expireDate"/>
    		</div>
    		<div style="height:30px;clear: left">
    			<input type="button" id="advanceSearch" class='button-small' value="${LANG['system.search']}"/>
    		</div>
    	</div>
<table width="98.5%" border="0" align="center" cellpadding="0" cellspacing="0" id="table_box" style="  margin-left:10px; margin-right:10px; border:#D6D6D6 1px solid; border-top:none; border-bottom:none;">
  
  <tr class="table002" height="32" >
    <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" id="site-list">
     <tr class="table003" height="38" >
        <td width="20%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.site.list.CompanyName']}</span></div></td>
        <td width="10%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.site.list.SiteSign']}</span></div></td>
        <cc:sort var="SORT_ASC"/><cc:sort var="SORT_DESC"/><cc:sort var="CONFBASE_SORT_ENDTIME"/><cc:sort var="CONFBASE_SORT_CONFTYPE"/><cc:sort var="CONFBASE_SORT_STATUS"/><cc:sort var="CONFBASE_SORT_STARTTIME"/>
        <td width="15%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.list.meeting.title']}</span>
		</div></td>
        <td width="12%" height="38" bgcolor="d3eaef" class="STYLE10" onclick="javascript:sort('${CONFBASE_SORT_CONFTYPE}');" style="cursor: pointer;"><div align="center"><span>${LANG['system.list.meeting.type']}&nbsp;</span>
	        <c:if test="${CONFBASE_SORT_CONFTYPE!=sortField}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a></c:if>
	        <c:if test="${CONFBASE_SORT_CONFTYPE==sortField && SORT_ASC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a></c:if>
	        <c:if test="${CONFBASE_SORT_CONFTYPE==sortField  && SORT_DESC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a></c:if>
        </div></td>
        <td width="8%" height="38" bgcolor="d3eaef" class="STYLE10" onclick="javascript:sort('${CONFBASE_SORT_STATUS}');" style="cursor: pointer;"><div align="center"><span>${LANG['system.list.meeting.status']}&nbsp;</span>
           <c:if test="${CONFBASE_SORT_STATUS!=sortField}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a></c:if>
	       <c:if test="${CONFBASE_SORT_STATUS==sortField && SORT_ASC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a></c:if>
	       <c:if test="${CONFBASE_SORT_STATUS==sortField  && SORT_DESC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a></c:if>
        </div></td>
        <td width="14%" height="38" bgcolor="d3eaef" class="STYLE10" onclick="javascript:sort('${CONFBASE_SORT_STARTTIME}');" style="cursor: pointer;"><div align="center"><span>${LANG['system.list.meeting.start.time']}&nbsp;</span>
         	<c:if test="${CONFBASE_SORT_STARTTIME!=sortField}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a></c:if>
	        <c:if test="${CONFBASE_SORT_STARTTIME==sortField && SORT_ASC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a></c:if>
	        <c:if test="${CONFBASE_SORT_STARTTIME==sortField  && SORT_DESC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a></c:if>
       </div></td>
       <!--   <td width="14%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.list.meeting.stop.time']}&nbsp;</span></div></td>-->
       	<td width="14%" height="38" bgcolor="d3eaef" class="STYLE10" onclick="javascript:sort('${CONFBASE_SORT_ENDTIME}');" style="cursor: pointer;"><div align="center"><span>${LANG['system.list.meeting.stop.time']}&nbsp;</span>
		 	<c:if test="${CONFBASE_SORT_ENDTIME!=sortField}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a></c:if>
	        <c:if test="${CONFBASE_SORT_ENDTIME==sortField && SORT_ASC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a></c:if>
	        <c:if test="${CONFBASE_SORT_ENDTIME==sortField  && SORT_DESC==sortord}"><a class="paixu01" href="#"><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a></c:if>
       
		</div></td>
        <td width="7%" height="38" bgcolor="d3eaef" class="STYLE10" style="border-right:none"><div class="STYLE_none" style="border-right:none"><div align="center"><span>${LANG['system.list.meeting.license']}</span></div></td>
      </tr>
      <c:if test="${fn:length(confList)<=0 }">
         <tr>
           <td height="32" class="STYLE19" colspan="8" align="center">
        	${LANG['website.common.msg.list.nodata']}
           </td>
         </tr>
      </c:if>
      <c:if test="${fn:length(confList)>0 }">
      <c:forEach var="confInfo" items="${confList}" varStatus="status">
      <tr class="table001" height="32" >
        <td height="32"  class="STYLE19"><div align="center"  class="siteNameDiv_${confInfo.siteId }">--</div></td> 
        <td height="32" class="STYLE19"><div align="center" class="siteSignDiv_${confInfo.siteId }">-- </div></td>
        <td height="32" class="STYLE19"><div align="center">${confInfo.confName }
        <c:if test="${confInfo.cycleId!=null && confInfo.cycleId>0 }"><span id="cycleId_${confInfo.cycleId}">${LANG['bizconf.jsp.admin.conf_list.res7']}</span></c:if>
        </div></td>
         <c:set var="typeLang" value="conf.type.list.${confInfo.confType}"/>
        <td height="32" class="STYLE19"><div align="center">${LANG[typeLang]}</div></td>
        <c:set var="statusLang" value="conf.status.${confInfo.confStatus}"/>
        <td height="32" class="STYLE19"><div align="center">${LANG[statusLang]}</div></td>
        <td height="32" class="STYLE19"><div align="center"><fmt:formatDate value="${confInfo.startTime}" pattern="yyyy-MM-dd HH:mm"/>
        </div></td>
       	<c:set var="divIdStr" value="id='confUserConfId_${confInfo.id}'"/>
        <c:if test="${confInfo.cycleId != null && confInfo.cycleId > 0 }">
        	<c:set var="divIdStr" value="class='confUserCycleId_${confInfo.cycleId}'"/>
        </c:if>
        <cc:confList var="CONF_STATUS_OPENING"/>
        <cc:confList var="CONF_STATUS_FINISHED"/>
         <c:choose>
         <c:when test="${CONF_STATUS_OPENING == confInfo.confStatus}">
			<td height="32"><div align="center" class="STYLE21">--</div></td>
	       	<td height="32" class="STYLE21" ><div style="cursor: pointer;" onclick="parent.showConflogs('${confInfo.id}');"  name="attend_user" align="center"  title="${LANG['bizconf.jsp.admin.conf_list.res8']}:${confInfo.pcNum}<br/>${LANG['bizconf.jsp.admin.conf_list.res9']}:${confInfo.phoneNum}" >${confInfo.pcNum+confInfo.phoneNum}</div></td>
      	 </c:when>
      	 <c:when test="${CONF_STATUS_FINISHED == confInfo.confStatus}">
            <td height="32"><div align="center" class="STYLE21"><fmt:formatDate value="${confInfo.endTime}" pattern="yyyy-MM-dd HH:mm"/></div></td>
	       	<td height="32" class="STYLE21" ><div style="cursor: pointer;" onclick="parent.showConflogs('${confInfo.id}');" name="attend_user" align="center"  title="${LANG['bizconf.jsp.admin.conf_list.res8']}:${terminalPcs[confInfo.id]}<br/>${LANG['bizconf.jsp.admin.conf_list.res9']}:${terminalPhones[confInfo.id]}" >${allTerminals[confInfo.id]}</div></td>
      	 </c:when>
       	 <c:otherwise>
            <td height="32"><div align="center" class="STYLE21"><fmt:formatDate value="${confInfo.endTime}" pattern="yyyy-MM-dd HH:mm"/></div></td>
	       	<td height="32" class="STYLE21" ><div  name="attend_user" align="center"  title="${LANG['bizconf.jsp.admin.conf_list.res8']}:${terminalPcs[confInfo.id]}<br/>${LANG['bizconf.jsp.admin.conf_list.res9']}:${terminalPhones[confInfo.id]}" >${allTerminals[confInfo.id]}</div></td>
       	 </c:otherwise>
		</c:choose>
<%--     martin modify here  <td height="32" class="STYLE21" ><div  name="attend_user" align="center"  ${divIdStr} title="PC:${terminalPcs[confInfo.id]}<br/>phonecall:${terminalPhones[confInfo.id]}" >0</div></td>--%>
     </tr>
     </c:forEach>
     </c:if>
    </table>
  </tr>
  <tr>
    <td class="table_bottom" height="38">
     <jsp:include page="/jsp/common/page_info.jsp" />
	</td>
  </tr>
</table>
</div>
</form>
</body>
</html>
<script type="text/javascript">


	function initPage(){
		initSiteInfo();
		initConfUser();
	}
	initPage();
	
	//${LANG['bizconf.jsp.system.conf_list.res2']}
	function initSiteInfo(){
		<c:if test="${siteList!=null && fn:length(siteList) > 0}">
		var siteArray=new Array();
		var siteInfo=new Array();
		<c:forEach var="siteInfo" items="${siteList}" varStatus="status">
		siteInfo=new Array();
		siteInfo.push("${siteInfo.id}");
		siteInfo.push("${siteInfo.siteName}");
		siteInfo.push("${siteInfo.siteSign}");
		siteArray.push(siteInfo);
		</c:forEach>
		if(siteArray!=null && siteArray.length>0){
			for(var ii=0;ii<siteArray.length;ii++){
				$(".siteNameDiv_"+siteArray[ii][0]).html(siteArray[ii][1]);
				$(".siteSignDiv_"+siteArray[ii][0]).html(siteArray[ii][2]);
			}
		}
		</c:if>
	}
	//${LANG['bizconf.jsp.system.conf_list.res3']}
	function initConfUser(){
		<c:if test="${userCountList!=null && fn:length(userCountList) > 0}">
		var userCountArray=new Array();
		var userCountInfo=new Array();
		<c:forEach var="userCount" items="${userCountList}" varStatus="status">
		userCountInfo=new Array();
		userCountInfo.push("${userCount.confId}");
		userCountInfo.push("${userCount.cycleId}");
		userCountInfo.push("${userCount.userCount}");
		userCountArray.push(userCountInfo);
		</c:forEach>
		if(userCountArray != null && userCountArray.length > 0 ){
			for(var ii=0;ii<userCountArray.length;ii++){
				$("#confUserConfId_"+userCountArray[ii][0]).html(userCountArray[ii][2]);
				$(".confUserCycleId_"+userCountArray[ii][1]).html(userCountArray[ii][2]);
			}
		}
		</c:if>
	}
	
	function sort(sortField){
		var formId=($("#sortField").closest("form").attr("id"));
		var oldSortField=$("#sortField").val();
		var oldSortType=$("#sortord").val();
		if(oldSortField==sortField){
			if(oldSortType=="${SORT_DESC}"){
				$("#sortord").val("${SORT_ASC}");
			}else{
				if(oldSortType=="${SORT_ASC}"){
					$("#sortord").val("${SORT_DESC}");
				}
			}
		}else{
			$("#sortField").val(sortField);
			$("#sortord").val("${SORT_ASC}");
		}
		resetPageNo();
		$("#"+formId).submit();
		
	}
</script>
