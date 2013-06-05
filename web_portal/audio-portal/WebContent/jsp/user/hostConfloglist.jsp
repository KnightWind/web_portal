<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/page.css?ver=${version}"/>
<title>${LANG['bizconf.jsp.hostConfloglist.res1']}</title>
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
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-1.8.3.min.js?ver=${version}"></script>
<script type="text/javascript">
	function showlogs(id){
		parent.showConflogs(id);
	}
</script>
</head>
<body>
<div class="main_content">
<form id="query" name="query" action="/user/conflog/list" method="post">
<input type="hidden" name="isCreator" value="true" /> 
<div class="intercalate_main_top">
	<h3>${LANG['bizconf.jsp.hostConfloglist.res2']}</h3>
    <p>${LANG['bizconf.jsp.hostConfloglist.res3']}</p>
</div>
<div class="meeting_main" style=" margin-top:15px;" >
    <table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" id="t_box" >
      <tr align="center" height="35" class="tr_center" bgcolor="#000066">
        <td width="30%" class="tr_center">${LANG['bizconf.jsp.attendConfloglist.res3']}</td>
        <td width="10%" class="tr_center">${LANG['bizconf.jsp.attendConfloglist.res4']}</td>
        <td width="20%" class="tr_center">${LANG['bizconf.jsp.attendConfloglist.res6']}</td>
        <td width="20%" class="tr_center">${LANG['bizconf.jsp.attendConfloglist.res7']}</td>
        <td  width="10%" class="tr_center" style=" border-right:#D2D8DB 1px solid">${LANG['bizconf.jsp.attended_conf_list.res9']}</td>
      </tr>
    <c:if test="${fn:length(pageModel.datas)<=0}">
		<tr class="table001" height="32"  >
			<td class="STYLE19" height="32" colspan="11" align="center"> ${LANG['website.common.msg.list.nodata']} </td>
		</tr>
	</c:if>
	 
	<c:forEach var="conf" items="${pageModel.datas}" varStatus="status">
      <tr align="center" bgcolor="#FFFFFF" height="32">
        <td class="tr_main" style=" border-left:#D2D8DB 1px solid"><a href="#" onclick="javascript:parent.viewConf('${conf.id}');">${conf.confName}</td>
        <td class="tr_main" >${numMap[conf.id]}</td>
        <td class="tr_main">${myfn:fmtDate('yyyy-MM-dd HH:mm',conf.startTime,currUser.timeZone)}</td>
        <td class="tr_main">${myfn:fmtDate('yyyy-MM-dd HH:mm',conf.endTime,currUser.timeZone)}</td>
        <td class="tr_main" style=" border-right:1px solid #D2D8DB" align="center"><a  href="#" onclick="showlogs('${conf.id}');">${LANG['bizconf.jsp.hostConfloglist.res4']}</a>
        </td>
      </tr>
      </c:forEach>
      <tr>
        <td height="35" colspan="10" class="tr_bottom">
           <jsp:include page="/jsp/common/page_info.jsp" />
        </td>
      </tr>
    </table>
  </div>
</form>
</div>
</body>
</html>

