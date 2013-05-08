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

<%@ include file="/jsp/common/cookie_util.jsp"%>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/development-bundle/ui/minified/i18n/jquery-ui-i18n.min.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.uniform/jquery.uniform.js"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js"></SCRIPT>

<c:if test="${!empty errorMessage}">
	<script type="text/javascript">
		$(function() {
			parent.errorDialog("${errorMessage}");
		});
	</script>
</c:if>
<c:if test="${!empty infoMessage}">
	<script type="text/javascript">
	$(function() {
		parent.successDialog("${infoMessage}");
	});	
	</script>
</c:if>

<script type="text/javascript"> 
$(function() {
	//for search input style
	$("#query").find("input, select").not(".skipThese").uniform();
	
	//table tr background highlight
	$('#site-list tr').hover(function() {
			$(this).addClass('tr-hover');
		}, function() {
			$(this).removeClass('tr-hover');
	});
	
	//show or hide search input
	$(".gaoji").toggle(function () {
	    $("#search-condition").slideDown(function() {
		    parent.resizeHeight();//增加高度
	    });
	}, function () {
		$("#search-condition").slideUp(function() {
			parent.resizeHeight();//减少高度
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
	
	$("#createSite").click(function() {
		parent.createOrUpdateSite();
	});
	
	$("#advanceSearch").click(function(){
		querySite("/system/site/listWithCondition");
	});
});

function countManager(id) {
	parent.countManager(id);
}
function hostManager(id) {
	parent.hostManger(id);
}
function updateSite(id) {
	parent.createOrUpdateSite(id);
}
function viewSite(id) {
	parent.viewSite(id);
}
function checkForm(){
	$("#textfield01").val("${nameOrSign}");
	var nameOrSign = $("#textfield01").val();
	if(nameOrSign != null && nameOrSign != ""){
		query.action="/system/site/listWithSignOrName";
	}else{
		formBind();
		query.action="/system/site/listWithCondition";
	}
}

function querySite(url){
	var searchResource= "${LANG['system.site.search.info']}";
	var searchValue = $("#textfield01").val();
	if (searchValue==searchResource) {
		$("#textfield01").val("");
	}
	resetPageNo();
	query.action=url;
	query.submit();	
}

function unlock(id){
	parent.confirmDialog("${LANG['system.site.unlock']}",function(){
		formBind();
		query.action="/system/site/unlock/"+id;
		query.submit();
	});
	
}

function lock(id){
	parent.confirmDialog("${LANG['system.site.lock']}",function(){
		formBind();
		query.action="/system/site/lock/"+id;
		query.submit();
	});
}

function del(id){
	parent.confirmDialog("${LANG['system.site.delete']}", function() {
		formBind();
		query.action="/system/site/delete/"+id;
		query.submit();
	});
}

function sortQuery(sortField,sortord){
	formBind();
	var nameOrSign = $("#textfield01").val();
	var url = "/system/site/listWithCondition";
	if(nameOrSign != null && nameOrSign != ""){
		url = "/system/site/listWithSignOrName";
	}
	$("#sortField").val(sortField);
	$("#sortord").val(sortord);
	querySite(url);
}

function formBind(){
	$("#textfield01").val("${nameOrSign}");
	$("#sortField").val("${sortField}");
	$("#sortord").val("${sortord}");
	$("#siteName").val("${siteName}");
	$("#siteSign").val("${siteSign}");
	$("#siteFlag").val("${site_flag}");
	$("#lockFlag").val("${lock_flag}");
	$("#expireDateStart").val("${expireDateStart}");
	$("#expireDateEnd").val("${expireDateEnd}");
}

function enterSumbit(url){   
    var event=arguments.callee.caller.arguments[0]||window.event;//消除浏览器差异   
    if (event.keyCode == 13){       //监听回车键
    	querySite(url);   
    }   
}  

$(document).ready(function(){
	$("#textfield01").watermark('企业名称、企业标识');
	$("#search-condition").find("input[type=text]").each(function(){
		if($(this).val()){
			$("#search-condition").show();
			return false;
		}
	});
	$("#search-condition").find("select").each(function(){
		if($(this).val()!=0){
			$("#search-condition").show();
			return false;
		}		
	});
});
</script>
<title>${LANG['system.menu.site.list']}</title>
</head>
<body>
<form id="query" name="query" action="/system/site/list/" method="post" onsubmit="checkForm();">
<div class="">
 <div class="m_top">
    <div class="text_box">
    	<input class="search_input" type="text" name="nameOrSign" id="textfield01" value="${nameOrSign}" onkeydown='enterSumbit("/system/site/listWithSignOrName")'/>
    	<input type="button" name="search" id="search" class="sousuo searchs_button skipThese" onclick='querySite("/system/site/listWithSignOrName")' />
    	<a style="" class="gaoji" title="${LANG['system.advancedSearch']}" href="javascript:;">${LANG['system.advancedSearch']}</a>
    </div>
  </div>
    	<div id="search-condition" style="display: none; width:100%; height: auto;margin-left: 20px;">
    		<div style="height:30px;">
	    		<label>${LANG['system.site.list.CompanyName']} </label>
	    		<input type="text" name="siteName" id="siteName" value="${siteName}" style="width:262px;" onkeydown='enterSumbit("/system/site/listWithCondition")'/>
    		</div>
    		<div style="height:30px;clear: left">
	    		<label>${LANG['system.site.list.SiteSign']} </label>
	    		<input type="text" name="siteSign" id="siteSign" value="${siteSign}" style="width:262px;" onkeydown='enterSumbit("/system/site/listWithCondition")'/>
    		</div>
    		<div style="height:30px;clear: left">
	    		<label>${LANG['system.site.list.SiteFlag']} </label>
	    		<select name="siteFlag" id="siteFlag">
					<cc:siteList var="SITE_TYPES"/>
			   		<c:forEach var="eachType" items="${SITE_TYPES}"  varStatus="itemStatus">
			   			<c:set var ="typeName" value="site.type.${eachType }"/>
			   			<option value="${eachType}" <c:if test="${siteType==eachType}">selected</c:if>>${LANG[typeName]}</option>
			   		</c:forEach>
    			</select>
	    		<label style="margin:4px 11px">${LANG['system.site.list.Status']} </label>
	    		<select name="lockFlag" id="lockFlag">
					<cc:siteList var="SITE_STATUS"/>
			   		<c:forEach var="eachType" items="${SITE_STATUS}"  varStatus="itemStatus">
			   			<c:set var ="typeName" value="site.status.${eachType }"/>
			   			<option value="${eachType}" <c:if test="${siteStatus==eachType}">selected</c:if>>${LANG[typeName]}</option>
			   		</c:forEach>
    			</select>
    		</div>
    		<div style="height:30px;clear: left">
	    		<label>${LANG['system.site.list.ExpireDate']} </label>
	    		<input type="text" name="expireDateStart" id="expireDateStart"  value="${expireDateStart}" style="width:100px;" class="expireDate"/>
	    		<label style="margin-top:4px;margin-left: 15px;margin-right: 18px;">---</label>
	    		<input type="text" name="expireDateEnd" id="expireDateEnd" value="${expireDateEnd}"  style="width:100px;" class="expireDate"/>
    		</div>
    		<div style="height:30px;clear: left">
    			<input type="button" id="advanceSearch" class='button-small' onclick='querySite("/system/site/listWithCondition")' value="${LANG['system.search']}"/>
    		</div>
    	</div>

<table width="98.5%" border="0" align="center" cellpadding="0" cellspacing="0" id="table_box" style=" margin-left:10px; margin-right:10px; border:#B5D7E6 1px solid; border-top:none; border-bottom:none;">
  <tr>
  	<td colspan="8">
  		<c:if test="${user.sysType gt 6 }">
	  		<div style="position: absolute; right: 20px; margin-top: -40px;">
	  			<div class="make_new"><a href="javascript:;" id="createSite">${LANG['system.site.list.create']}</a></div>
	  		</div>
  		</c:if>
  	</td>
  </tr>
  <tr class="table002" height="32" >
    <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" id="site-list">
    	<tr>
	      	<td colspan="8">
		      	<input type="hidden" name="sortField" id="sortField"/>
		      	<input type="hidden" name="sortord" id="sortord"/>
	      	</td>
    	</tr>
      <tr class="table003" height="38" >
        <td width="16%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.site.list.CompanyName']}</span></div></td>
        <td width="10%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center">
	        <c:if test="${1!=sortField}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('1','0')"><span>${LANG['system.site.list.SiteSign']}&nbsp;</span><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${1==sortField && 0==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('1','1')"><span>${LANG['system.site.list.SiteSign']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${1==sortField  && 1==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('1','0')"><span>${LANG['system.site.list.SiteSign']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a>
	        </c:if>
        </div></td>
       <td width="10%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center">
        	<c:if test="${2!=sortField}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('2','0')"><span>${LANG['system.site.list.SiteFlag']}&nbsp;</span><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${2==sortField && 0==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('2','1')"><span>${LANG['system.site.list.SiteFlag']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${2==sortField  && 1==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('2','0')"><span>${LANG['system.site.list.SiteFlag']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a>
	        </c:if>
        </div></td>
        <td width="8%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.site.list.hireMode']}</span></div></td>
        <td width="12%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center">
        	<c:if test="${5!=sortField}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('5','0')"><span>${LANG['system.site.list.effedate']}&nbsp;</span><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${5==sortField && 0==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('5','1')"><span>${LANG['system.site.list.effedate']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${5==sortField  && 1==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('5','0')"><span>${LANG['system.site.list.effedate']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a>
	        </c:if>
       		</div>
       	</td>
        <td width="12%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center">
        	<c:if test="${3!=sortField}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('3','0')"><span>${LANG['system.site.list.ExpireDate']}&nbsp;</span><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${3==sortField && 0==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('3','1')"><span>${LANG['system.site.list.ExpireDate']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${3==sortField  && 1==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('3','0')"><span>${LANG['system.site.list.ExpireDate']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a>
	        </c:if>
       		</div>
       	</td>
        <td width="9%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.site.list.UserName']}</span></div></td>
<%--         <td width="10%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['system.site.list.Status']}</span></div></td> --%>
        <td width="7%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center">
        	<c:if test="${4!=sortField}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('4','0')"><span>${LANG['system.site.list.Status']}&nbsp;</span><img src="${baseUrlStatic}/images/paixuzong.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${4==sortField && 0==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('4','1')"><span>${LANG['system.site.list.Status']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu02.png" width="6" height="13" /></a>
	        </c:if>
	        <c:if test="${4==sortField  && 1==sortord}">
	        	<a class="paixu01" href="javascript:;" onclick="sortQuery('4','0')"><span>${LANG['system.site.list.Status']}&nbsp;</span><img src="${baseUrlStatic}/images/paixu01.png" width="6" height="13" /></a>
	        </c:if>
       		</div>
       	</td>
        <td width="16%" height="38" bgcolor="d3eaef" class="STYLE10" class="STYLE_none" style="border-right:none"><div align="center"><span>${LANG['system.Operate']}</span></div></td>
      </tr>
        <c:if test="${fn:length(siteList)<=0 }">
         <tr>
        <td height="32" class="STYLE19" colspan="9"  align="center">
        	${LANG['website.common.msg.list.nodata']}
        </td>
      </tr>
        </c:if>
      <c:if test="${fn:length(siteList)>0 }">
     <c:forEach var= "siteBase" items = "${siteList}"  varStatus="status">
      <tr>
        <td height="32" class="STYLE19" align="left">
        	<c:choose>
	      			<c:when test="${myfn:expiredInWe(siteBase.expireDate) }">
	      				<div style="margin-left: 10px;"  title="站点即将过期" >
	      					<img src="/static/images/caution.png"/>
			        		<a onclick="viewSite(${siteBase.id})" href="javascript:;"><span>${siteBase.siteName}</span></a>
			        	</div>
	      			</c:when>
	      			<c:when test="${myfn:siteExpired(siteBase.expireDate) }">
	      				<div style="margin-left: 10px;" title="站点已经过期" >
	      					<img src="/static/images/overdue.png" />
	      					<a onclick="viewSite(${siteBase.id})" href="javascript:;"><span>${siteBase.siteName}</span></a>
	      				</div>	
	      			</c:when>
	      			<c:otherwise>
	      				<div style="margin-left: 10px;">
	      					<a onclick="viewSite(${siteBase.id})" href="javascript:;"><span>${siteBase.siteName}</span></a>
	      				</div>
	      			</c:otherwise>
		      	</c:choose>
        </td>
        <td height="32" class="STYLE19" b><div align="center">${siteBase.siteSign }</div></td>
        <c:if test="${siteBase.siteFlag == 1 }">
			<td height="32" class="STYLE19"><div align="center">${LANG['system.site.list.SiteFlag.official']}</div></td>
		</c:if>
		<c:if test="${siteBase.siteFlag == 2 }">
			<td height="32" class="STYLE19"><div align="center">${LANG['system.site.list.SiteFlag.trial']}</div></td>
		</c:if>
		<c:if test="${siteBase.hireMode == 1 }">
			<td height="32" class="STYLE19"><div align="center">${LANG['system.site.list.hireMode.month']}</div></td>
		</c:if>
		<c:if test="${siteBase.hireMode == 2 }">
			<td height="32" class="STYLE19"><div align="center">${LANG['system.site.list.hireMode.minutes']}</div></td>
		</c:if>
		<c:if test="${siteBase.hireMode == null || siteBase.hireMode == 0}">
			<td height="32" class="STYLE19"><div align="center">--</div></td>
		</c:if>
        <td height="32" class="STYLE19"><div align="center"><fmt:formatDate  value="${siteBase.effeDate}" type="date" pattern="yyyy-MM-dd"/></div></td>
        <td height="32" class="STYLE19"><div align="center"><fmt:formatDate  value="${siteBase.expireDate}" type="date" pattern="yyyy-MM-dd"/></div></td>
        <td height="32" class="STYLE19"><div align="center" id="siteIdDiv_${siteBase.id}">--</div></td>
	    <c:if test="${siteBase.lockFlag == 1}"> 
			<td height="32" class="STYLE19"><div align="center" class="STYLE21">${LANG['system.site.list.Status.normal']}</a></span></div></td>
		</c:if>
		<c:if test="${siteBase.lockFlag == 2}"> 
			<td height="32" class="STYLE19"><div align="center" class="STYLE21">${LANG['system.site.list.Status.lock']}</a></span></div></td>
		</c:if>
        <td height="32" class="STYLE19">
        	<div align="center" class="STYLE21">
<%--        <a href="javascript:hostManager(${siteBase.id});">host=${siteBase.chargeMode}</a>--%>
				<c:choose>
					<c:when test="${user.sysType gt 6 }">
						<c:if test="${siteBase.chargeMode == 1}">
		        			<a href="javascript:hostManager(${siteBase.id});">点数</a>&nbsp;&nbsp;
			        	</c:if>
			        	<c:if test="${siteBase.chargeMode != 1}">
		        			<a href="javascript:countManager(${siteBase.id});">点数</a>&nbsp;&nbsp;
			        	</c:if>
			     		<c:if test="${siteBase.lockFlag == 2}"> 
							<a href="javascript:;" onclick='unlock("${siteBase.id}")'>${LANG['system.site.list.Status.unlock']}</a>&nbsp;&nbsp;
						</c:if>
						<c:if test="${siteBase.lockFlag == 1}"> 
							<a href="javascript:;" onclick='lock("${siteBase.id}")'>${LANG['system.site.list.Status.lock']}</a>&nbsp;&nbsp;
						</c:if>
						<a onclick="updateSite(${siteBase.id})" href="javascript:;">${LANG['system.change']}</a>&nbsp;&nbsp;
						<a href="javascript:;" onclick='del("${siteBase.id}")'>${LANG['system.delete']}</a>&nbsp;&nbsp;
					</c:when>
					<c:otherwise>
						<a  href="javascript:;">申请修改</a>
					</c:otherwise>
				</c:choose>
<%-- 				<a href="javascript:;"  >${LANG['system.manager']}</a> --%>
			</div>
        </td>
      </tr>
     </c:forEach>
     </c:if>
     
    </table>
  </tr>
  <tr>
    <td class="table_bottom" height="38">
    <jsp:include page="/jsp/common/page_info.jsp" />
<!--     <table width="100%" border="0" cellspacing="0" cellpadding="0"> -->
<!--       <tr> -->
<%--         <td><div><span width="33%" class="STYLE22" align="left">&nbsp;&nbsp;&nbsp;&nbsp;共 <strong>${pageModel.pageCount}</strong> 页,共有<strong>${pageModel.rowsCount}</strong> 条记录</span></div></td> --%>
<!--         <td width="67%"><table width="420" border="0" align="right" cellpadding="0" cellspacing="0" > -->
<!--           <tr> -->
<!--             <td><div align="center" class="page_shouye"><a href="#">首页</a></div></td> -->
<!--             <td><div align="center" class="page_next"><a href="#">下一页</a></div></td> -->
<!--             <td><div align="center" class="page_shuzi"><a href="#">1</a>&nbsp;&nbsp;<a href="#">2</a>&nbsp;&nbsp;<a href="#">3</a>&nbsp;&nbsp;<a href="#">4</a>&nbsp;&nbsp;<a href="#">5</a></div></td> -->
            
<!--             <td><div class="page_next"><a href="#">下一页</a></div></td> -->
<!--             <td><div class="page_shouye"><a href="#">尾页</a></div></td> -->
<!--             <td class="STYLE22"><div class="page_shouye"><a href="#">转到</a></div></td> -->
<!--             <td><div align="center"> -->
<!--             <input type="text" name="textfield" id="textfield"  style="width:22px; height:16px; font-size:12px; border:solid 1px #CACACA; line-height:16px;"/> -->
<!--             </div></td> -->
<!--             <td class="STYLE22">页</div></td> -->
<!--             <td class="STYLE22"><div class="page_shouye01">跳转</div></td> -->
<!--            </tr>   -->
<!--            </table>   -->
<!--         </td>  -->
<!--        </td> -->
<!--       </tr> -->
<!--     </table> -->
    
    </td>
  </tr>
</table>

</div>
</form>
</body>
</html>


<script type="text/javascript"> 
function initPage(){
	var supperAdminArray=new Array();
	var eachSupperAdmin=null;
	<c:forEach var="supAdmin" items="${supperAdminList}" varStatus="status">
	eachSupperAdmin=new Array();
	eachSupperAdmin.push("${supAdmin.id}");
	eachSupperAdmin.push("${supAdmin.siteId}");
	eachSupperAdmin.push("${supAdmin.trueName}");
	supperAdminArray.push(eachSupperAdmin);
	</c:forEach>
	if(supperAdminArray!=null && supperAdminArray.length>0){
		for(var ii=0;ii<supperAdminArray.length;ii++){
			$("#siteIdDiv_"+supperAdminArray[ii][1]).html(supperAdminArray[ii][2]);
		}
		
	}
	//var bodyWidth=document.body.scrollWidth;
	//alert(bodyWidth);
}
initPage();
</script>

