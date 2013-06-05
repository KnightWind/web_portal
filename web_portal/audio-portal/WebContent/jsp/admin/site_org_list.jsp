<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<jsp:useBean id="nowDate" class="java.util.Date"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/reset.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/enterprise/rightbox.css"/>
<style type="text/css">
	.sub-icon {
		width: 16px;height: 16px;background-image: url('/static/css/user/zTreeStyle/img/zTreeCustom.gif');background-position: -385px -90px;
		position: relative;bottom: 3px;float:left;
		margin-right: 5px;
	}
	.home-icon {
		width: 16px;height: 16px;background-image: url('/static/css/user/zTreeStyle/img/diy/1_close.png');
		position: relative;margin-right: 5px;float:left;
	}
</style>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js"></SCRIPT>
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
	$('#site-list tr').hover(function() {
			$(this).addClass('tr-hover');
		}, function() {
			$(this).removeClass('tr-hover');
	});
});
function createSubOrg(id, level) {
	parent.createOrg(id, level);
}
function updateOrg(id) {
	parent.parent.updateOrg(id);
}
function arrangeUser(id) {
	parent.parent.getOrgUserList(id);
}
function del(id){
	parent.parent.confirmDialog("${LANG['bizconf.jsp.admin.site_org_list.res1']}", function() {
		app.delSiteOrg(id, function(result) {
			if(result){
				if(result.status=="1"){
					parent.refreshIframe();
					parent.parent.successDialog(result.message);
				} else {
					parent.parent.errorDialog(result.message);
				}
			}
		});
	});
}
jQuery(function($) {
	parent.refreshIHeight();
});
// $(document).ready(function() {
// 	$(".home-icon").click(function() {
// 		var pTr = $(this).closest("tr").next();
// 		if(pTr){
// 			if(pTr.is(":visible")){
// 				pTr.hide();
// 			} else {
// 				pTr.show();
// 			}
// 		}
// 	});
// });
</script>
<title>${LANG['bizconf.jsp.admin.index.res19']}</title>
</head>
<body>
<form id="query" name="query" action="/admin/org/orgList/${orgId }" method="post">
<div class="main_content" style="overflow-y: auto;height: 600px;">
<!--  <div class="m_top">  -->
<!--  	<input name="button_01" class="button_01" type="button" id="createOrg" value="${LANG['bizconf.jsp.admin.site_org_list.res2']}" onmouseover="this.className='Btn_Hover_01'" onmouseout="this.className='Btn_01'"/> -->
<!--   </div> -->
 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" id="table_box" >
    <tr class="table002" height="32" >
    <td style="border-bottom:1px solid  #A3C5DE"><table width="100%" border="0" cellpadding="0" cellspacing="0" id="site-list">
      <tr class="table003" height="38" >
          <td width="25%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['bizconf.jsp.admin.site_org_list.res3']}</span></div></td>
	      <td width="35%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['bizconf.jsp.admin.site_org_list.res4']}</span></div></td>
	      <td width="15%"  height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>${LANG['bizconf.jsp.admin.site_org_list.res5']}</span></div></td>
	      <td width="25%"  height="38" bgcolor="d3eaef" class="STYLE10" style=" border-right:none;"><div align="center" style="width: 300px;"><span>${LANG['bizconf.jsp.admin.site_org_list.res6']}</span></div></td>
	  </tr>
      <c:if test="${fn:length(pageModel.datas)<=0 }">
         <tr>
           <td height="32" class="STYLE19" colspan="4"  align="center">
        	${LANG['website.common.msg.list.nodata']}
           </td>
         </tr>
      </c:if>
      <c:if test="${fn:length(pageModel.datas)>0 }">
     <c:forEach var= "org" items = "${pageModel.datas}"  varStatus="status">
      <tr class="table001" height="32">
        <td height="32" valign="middle">
	        <div style="padding-left: 10px;position: relative;">
		        <span style="color:#73798E">
			        <c:if test="${org.orgLevel == 1 }">
			        	<div class="home-icon"></div>
			        </c:if>
			        <c:if test="${org.orgLevel == 2 }">
			            <div class="sub-icon" style="margin-left: 15px"></div>
			        </c:if>
			        <c:if test="${org.orgLevel == 3 }">
			            <div class="sub-icon" style="margin-left: 35px"></div>
			        </c:if>
			        <c:if test="${org.orgLevel == 4 }">
			        	<div class="sub-icon" style="margin-left: 55px"></div>
			        </c:if>
		        	${org.orgName}
		        </span>
	        </div>
        </td>
        <td height="32"><div title="${org.orgDesc}" align="center" style="width: 450px;white-space:nowrap;overflow: hidden;">${org.orgDesc}</div></td>
        <td height="32"><div onclick="parent.parent.showOrgUsers('${org.id}');" align="center"  style="cursor: pointer;" >${participantSizeList[org.id]}</div></td>
        <td height="32" align="center" style=""><div>
        	<c:if test="${org.orgLevel<4}">
        		<a href="javascript:;" onclick='createSubOrg("${org.id}", "${org.orgLevel}")' class="edit">${LANG['bizconf.jsp.admin.site_org_list.res7']}</a>&nbsp;&nbsp;&nbsp;&nbsp;
        	</c:if>
			<a href="javascript:;" onclick='updateOrg("${org.id}")' class="edit">${LANG['system.change']}</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:;" onclick='del("${org.id}")' class="edit">${LANG['system.delete']}</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:;" onclick='arrangeUser("${org.id}")' class="edit">${LANG['bizconf.jsp.admin.index.res11']}</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:;" onclick="parent.parent.showOrgUsers('${org.id}');" class="edit">${LANG['bizconf.jsp.admin.site_org_list.res8']}</a>
        </td>
      </tr>
     </c:forEach>
     </c:if>
    </table>
  </tr>
<!--   <tr> -->
<!--     <td class="table_bottom" height="38"> -->
<%--     <jsp:include page="/jsp/common/page_info.jsp" /> --%>
<!--     </td> -->
<!--   </tr> -->
</table>
<table width="100%" height="50px;">
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
</div>
</form>
</body>
</html>
