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
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
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
	
	$("#createOrg").click(function() {
		parent.createOrUpdateOrg();
	});
});
function updateOrg(id) {
	parent.createOrUpdateOrg(id);
}
function arrangeUser(id) {
	parent.getOrgUserList(id);
}
function del(id){
	parent.confirmDialog("确认删除该机构吗？", function() {
		query.action="/admin/org/delete/"+id;
		query.submit();
	});
}
</script>
<title>机构管理</title>
</head>
<body>
<form id="query" name="query" action="/admin/org/orgList" method="post">
<div class="main_content">
 <div class="m_top"> 
 	<input name="button_01" class="button_01" type="button" id="createOrg" value="添加机构" onmouseover="this.className='Btn_Hover_01'" onmouseout="this.className='Btn_01'"/>
  </div>
 <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" id="table_box" >
    <tr class="table002" height="32" >
    <td><table width="100%" border="0" cellpadding="0" cellspacing="0" id="site-list">
      <tr class="table003" height="38" >
          <td width="25%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>机构名</span></div></td>
	      <td width="35%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>描述</span></div></td>
	      <td width="15%"  height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>人数</span></div></td>
	      <td width="25%"  height="38" bgcolor="d3eaef" class="STYLE10" style=" border-right:none;"><div align="center" ><span>操作</span></div></td>
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
      <tr class="table001" height="32" >
        <td height="32"><div align="center"><span style="color:#73798E">${org.orgName}</span></div></td>
        <td height="32"><div align="center">${org.orgDesc}</div></td>
        <td height="32"><div align="center">${participantSizeList[org.id]}</div></td>
        <td height="32"><div align="center" >
			<a href="javascript:;" onclick='updateOrg("${org.id}")' class="edit">${LANG['system.change']}</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:;" onclick='del("${org.id}")' class="edit">${LANG['system.delete']}</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:;" onclick='arrangeUser("${org.id}")' class="edit">分配用户</a>
        </td>
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
