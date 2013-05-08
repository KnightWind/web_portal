<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/style.css"/>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<script type="text/javascript"> 
$(function() {
	$('#site-list tr').hover(function() {
			$(this).addClass('tr-hover');
		}, function() {
			$(this).removeClass('tr-hover');
	});
	
	$(".gaoji").toggle(function () {
	    $("#search-condition").show();
	}, function () {
		$("#search-condition").hide();
	});
});

function del(id){
	formBind();
	query.action="/system/notice/delete/"+id;
	query.submit();
}

</script>
<title>公告信息</title>
</head>
<body>
<form name="query" action="/system/notice/list/" method="post">
<div class="main_right">
 <div class="m_top">
<!--     <div class="text_box"> -->
<!--     	<input type="text" name="nameOrSign" id="textfield01" value="请输入企业名称，企业标识进行查询..." onfocus="this.value=''; this.onfocus=null;" /> -->
<%--     	<a class="sousuo" title="搜索" href="javascript:;" onclick='querySite("/system/site/listWithSignOrName")'><img src="${baseUrlStatic}/images/sousuo.jpg" width="29" height="24" /></a> --%>
<!--     	<a class="gaoji" title="高级搜索" href="#">高级搜索</a> -->
<!--     </div> -->
    <div class="make_new"><a href="#">添加公告</a></div>
  </div>
<!--     	<div id="search-condition" style="display: none; width:100%; height: 300px;"> -->
<!--     		搜索内容 -->
<!--     	</div> -->

<table width="98.5%" border="0" align="center" cellpadding="0" cellspacing="0" id="table_box" style=" margin-left:10px; margin-right:10px; border:#D6D6D6 1px solid; border-top:none; border-bottom:none;">
  
  <tr class="table002" height="32" >
    <td>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" id="site-list">
      <tr class="table003" height="38" >
<!--       	<input type="hidden" name="sortField" id="sortField"/> -->
<!--       	<input type="hidden" name="sortord" id="sortord"/> -->
        <td width="45%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>主题</span></div></td>
        <td width="20%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>发布时间</span></div></td>
        <td width="20%" height="38" bgcolor="d3eaef" class="STYLE10"><div align="center"><span>到期时间</span></div></td>
        <td width="15%" height="38" bgcolor="d3eaef" class="STYLE10" class="STYLE_none" style="border-right:none"><div align="center"><span>操作</span></div></td>
      </tr>
     <c:forEach var= "notice" items = "${noticeList}"  varStatus="status">
      <tr>
        <td height="32"><div align="center"><span class="STYLE19">${notice.title}</span></div></td>
<%--         <td height="32" class="STYLE19"><div align="center">${notice }</div></td> --%>
        <td height="32" class="STYLE19"><div align="center">${fn:substring(notice.startTime, 0, 16)}</div></td>
        <td height="32" class="STYLE19"><div align="center">${fn:substring(notice.stopTime, 0, 16)}</div></td>
        <td height="32"><div align="center" class="STYLE21">
			<a href="/system/site/update/${notice.id}">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:;" onclick='del("${notice.id}")'>删除</a>
        </td>
      </tr>
     </c:forEach>
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
