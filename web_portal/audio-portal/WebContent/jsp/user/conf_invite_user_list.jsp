<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>
<title>无标题文档</title>
</head>

<body onload="loaded()">
<div class="add21_main">
  <div class="add21_top">
    <div class="add21_top_left"></div>
    <div class="add21_top_center"></div>
    <div class="add21_top_right"></div>
  </div>
  <div class="add21_center">
    <div class="add21_center_left"></div>
    <div class="add21_center_center">
      <div class="First_Steps_invite_first01" >
        <div class="First_Steps_title_invite01"> <a href="javascript:closeDialog();"></a>
          <h3 class="tit_a_invite">邀请人列表</h3>
          <p class="tit_b_invite">您可以查看邀请用户参会情况或再次提醒用户。</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick_invite" src="${baseUrlStatic}/images/min.jpg" width="550" height="1" /></div>
        <div class="join_list">
        	<ul class="boc">
            	<li class="boc01">用户名</li>
            	<li class="boc02">邮箱</li>
                <li class="boc03">电话</li>
                <li class="boc04">详情</li>
            </ul>
            <div class="boc_main">
            <c:forEach var="confUser" items="${confUserList}">
             <ul class="boc_a">
            	<li class="boc01">${confUser.userName}&nbsp;</li>
            	<li class="boc02">${confUser.userEmail}&nbsp;</li>
                <li class="boc03">${confUser.telephone}&nbsp;</li>
                <li class="boc04">${confUser.acceptFlag == 1 ? '已接受' : confUser.acceptFlag == 0 ? '未回应' : '已谢绝'}</li>
            </ul>
           </c:forEach>
             
            </div>
        
        </div>
       
       
       
       
        <div class="First_Steps_bottom_b">
          <div class="but101">
          <span class="button_common"><a href="javascript:closeDialog();"><img src="${baseUrlStatic}/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:10px"/>返&nbsp;&nbsp;回</a>
          </span></div>
          <div class="but111"></div>
        </div>
      </div>
    </div>
    <div class="add21_center_right"></div>
  </div>
  <div class="add21_bottom">
    <div class="add21_bottom_left"></div>
    <div class="add21_bottom_center"></div>
    <div class="add21_bottom_right"></div>
  </div>
</div>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#eidtInventContact");
	frame.trigger("loaded");
}
function closeDialog(){
	var frame = parent.$("#eidtInventContact");
	frame.trigger("closeDialog");
}
</script>