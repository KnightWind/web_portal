<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/page.css"/>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>
<title>index</title>
<style>
.tr_center {
	border:#D2D8DB 1px solid;
	border-right:none;
}
.tr_main {
	border-bottom:#D2D8DB 1px solid;
	color:#666666;
}
.tr_top {
	border:#D2D8DB 1px solid;
	border-bottom:none;
	margin-top:26px;
}
.tr_bottom {
	border:#D2D8DB 1px solid;
	border-top:none
}
</style>
<script type="text/javascript">

function viewBillDetail(){
		parent.parent.showTelDetail("${user.id}","${year}","${month}");
}

function submitQuery(){
	$("#query").submit();
}
</script>
</head>
<body>
<form id="query" name="query" action="/common/billing/userBillListIndex" method="post">
    <table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" id="t_box" >
      <tr>
        <td height="40" colspan="4" bgcolor="#EAF4FC" class="tr_top">
          <select class="zd01" name="year" id="yearSelect" onchange="submitQuery();">
<%--            <option value="2013" <c:if test="${year eq '2013'}">selected="selected"</c:if>>2013${LANG['bizconf.jsp.admin.mybilling_list.res1']}</option>--%>
<%--            <option value="2012" <c:if test="${year eq '2012'}">selected="selected"</c:if>>2012${LANG['bizconf.jsp.admin.mybilling_list.res1']}</option>--%>
<%--            <option value="2011" <c:if test="${year eq '2011'}">selected="selected"</c:if>>2011${LANG['bizconf.jsp.admin.mybilling_list.res1']}</option>--%>
<%--            <option value="2010" <c:if test="${year eq '2010'}">selected="selected"</c:if>>2010${LANG['bizconf.jsp.admin.mybilling_list.res1']}</option>--%>
            <option value="${curryear}" <c:if test="${year eq curryear}">selected="selected"</c:if>>${curryear} ${LANG['bizconf.jsp.admin.mybilling_list.res1']}</option>
            <option value="${curryear-1}" <c:if test="${year eq (curryear-1)}">selected="selected"</c:if>>${curryear-1} ${LANG['bizconf.jsp.admin.mybilling_list.res1']}</option>
          </select>
          <select class="zd02" name="month" id="monthSelect" onchange="submitQuery();">
            <option value="1" <c:if test="${month eq '1'}">selected="selected"</c:if>>1${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="2" <c:if test="${month eq '2'}">selected="selected"</c:if>>2${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="3" <c:if test="${month eq '3'}">selected="selected"</c:if>>3${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="4" <c:if test="${month eq '4'}">selected="selected"</c:if>>4${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="5" <c:if test="${month eq '5'}">selected="selected"</c:if>>5${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="6" <c:if test="${month eq '6'}">selected="selected"</c:if>>6${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="7" <c:if test="${month eq '7'}">selected="selected"</c:if>>7${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="8" <c:if test="${month eq '8'}">selected="selected"</c:if>>8${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="9" <c:if test="${month eq '9'}">selected="selected"</c:if>>9${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="10" <c:if test="${month eq '10'}">selected="selected"</c:if> >10${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="11" <c:if test="${month eq '11'}">selected="selected"</c:if> >11${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
            <option value="12" <c:if test="${month eq '12'}">selected="selected"</c:if>>12${LANG['bizconf.jsp.admin.mybilling_list.res2']}</option>
          </select>
        </td>
      </tr>
      <tr align="right" bgcolor="#FFFFFF" height="32">
        <td class="tr_main" colspan="4" style=" border-left:#D2D8DB 1px solid; border-right:#D2D8DB 1px solid;padding-right:10px; border-bottom:none"> 
        	${LANG['bizconf.jsp.admin.mybilling_list.res3']}<fmt:formatDate value="${startDate}" type="date" pattern="yyyy-MM-dd"/> &nbsp; ${LANG['bizconf.jsp.admin.mybilling_list.res4']} &nbsp; <fmt:formatDate value="${endDate}" type="date" pattern="yyyy-MM-dd"/>
        </td>
      </tr>
      <tr align="right" bgcolor="#FFFFFF" height="32">
        <td width="50%" colspan="2" class="tr_main" style=" border-left:#D2D8DB 1px solid; padding-left:10px; " align="left"><strong>${LANG['bizconf.jsp.system.viewSite.res10']}${user.trueName}</strong> </td>
        <td width="50%" colspan="2" class="tr_main" style="  border-right:#D2D8DB 1px solid;padding-right:10px;">${LANG['bizconf.jsp.admin.mybilling_list.res6']}
        <c:choose>
	  	 	<c:when test="${site.chargeMode eq 1 }">
	  	 		name_host
	  	 	</c:when>
	  	 	<c:when test="${site.chargeMode eq 2 }">
	  	 		active users
	  	 	</c:when>
	  	 	<c:when test="${site.chargeMode eq 3 }">
	  	 		seats
	  	 	</c:when>
	  	 	<c:otherwise>
	  	 		time
	  	 	</c:otherwise>
	  	 </c:choose>
	  </td>
      </tr>
        <tr align="center" height="35" class="tr_center" bgcolor="#000066" >
         <td class="tr_main" colspan="2" style=" border-left:#D2D8DB 1px solid; padding-right:10px; padding-left:55px;" align="left"><strong>${LANG['bizconf.jsp.admin.mybilling_list.res7']}</strong></td>
        <td class="tr_center" colspan="2" style="border-right:#D2D8DB 1px solid; padding-right:10px; color:#996600; border-left:none;border-top:none" align="right"><strong>${LANG['bizconf.jsp.admin.mybilling_list.res8']}<fmt:formatNumber value="${conFee+fee}" pattern="#.00" type="number"/>${LANG['bizconf.jsp.admin.mybilling_list.res9']}</strong></td>
	
	<c:if test="${site.chargeMode != 3}">
      </tr>
        <tr align="left" bgcolor="#FFFFFF" height="32" class="tongxin">
        <td class="tr_main" colspan="2" style=" border-left:#D2D8DB 1px solid; cursor:pointer; padding-left:100px; border-bottom:dashed 1px #D2D8DB;" onclick="parent.parent.showDataFeeDetail(${user.id});">${LANG['bizconf.jsp.admin.mybilling_list.res10']}</td>
        <td class="tr_main" colspan="2" style=" border-left:#D2D8DB 1px solid; cursor:pointer; border-right:#D2D8DB 1px solid;padding-right:50px;border-left:none;border-bottom:dashed 1px #D2D8DB; " align="right"><span><fmt:formatNumber value="${conFee}" pattern="#.00" type="number"/></span></td>
      </tr>
     </c:if>
       
       <tr align="left" bgcolor="#FFFFFF" height="32" class="tongxin">
        <td class="tr_main" colspan="2" style=" border-left:#D2D8DB 1px solid; cursor:pointer; padding-left:100px;" onclick="viewBillDetail();">${LANG['bizconf.jsp.admin.mybilling_list.res11']}</td>
        <td class="tr_main" colspan="2" style=" border-left:#D2D8DB 1px solid; cursor:pointer; border-right:#D2D8DB 1px solid;padding-right:50px;border-left:none; " align="right"><span><fmt:formatNumber value="${fee}" pattern="#.00" type="number"/>${LANG['bizconf.jsp.admin.mybilling_list.res9']}</span></td>
      </tr>
     </tr>
    </table>
    </form>
</body>
</html>

