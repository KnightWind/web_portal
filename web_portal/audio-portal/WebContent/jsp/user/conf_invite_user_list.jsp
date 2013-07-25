<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>
<title>${LANG['bizconf.jsp.conf_invite_user_list.res1']}</title>
</head>

<body onload="loaded()">

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
        <!--${LANG['bizconf.jsp.add_contacts.res2']}========================================================================-->
      	<div class="First_Steps_invite_first01" >
        <div class="First_Steps_title_invite01"> <a href="javascript:closeDialog();"></a>
          <h3 class="tit_a_invite">${LANG['bizconf.jsp.conf_invite_user_list.res2']}</h3>
          <p class="tit_b_invite">${LANG['bizconf.jsp.conf_invite_user_list.res3']}</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick_invite" src="${baseUrlStatic}/images/min.jpg" width="550" height="1" /></div>
        <table style="width: 100%;">
        	<tr>
        		<td align="left"><a class="invent_user_default" href="#">${LANG['bizconf.jsp.inviteContactsSelect.before']}
        		<c:set var="inviteCount" value="${fn:length(confUserList)}" />
        		<c:if test="${inviteCount>1}">
        		<c:out value="${inviteCount}"/> ${LANG['bizconf.jsp.inviteContactsSelect.after']}
        		</c:if>
        		<c:if test="${inviteCount<2}">
        		<c:out value="${inviteCount}"/>${LANG['bizconf.jsp.inviteContactsSelect.after2']}
        		</c:if>
        		</a></td>
        		<td align="right">
        			<c:if test="${user.id eq conf.createUser and conf.confStatus lt 3}">
	        			<span class="button_common" style="float: right;margin-right: 20px;margin-top: 5px;">
		              		<a href="javascript:parent.inventContact('${conf.id}');">
		              			<img width="16" height="16" align="absmiddle" src="/static/images/add_16.png" style=" margin-left:5px; margin-right:5px;">${LANG['bizconf.jsp.inviteFirst.res14']}
		              		</a>
	              		</span>
              		</c:if>
        		</td>
        	</tr>
        </table>
        <div class="join_list">
        	<ul class="boc">
            	<li class="boc01">${LANG['bizconf.jsp.add_contacts.res7']}</li>
            	<li class="boc02">${LANG['bizconf.jsp.add_contacts.res9']}</li>
                <li class="boc03"><!--  ${LANG['bizconf.jsp.conflogs.res4']}-->&nbsp;</li>
                <li class="boc04">${LANG['bizconf.jsp.conf_invite_user_list.res4']}</li>
            </ul>
            <div class="boc_main">
            <c:forEach var="confUser" items="${confUserList}">
             <ul class="boc_a">
            	<li class="boc01">${confUser.userName}&nbsp;</li>
            	<li class="boc02">${confUser.userEmail}&nbsp;</li>
                <li class="boc03"><!-- ${confUser.telephone}-->&nbsp; </li>
                <li class="boc04">
                	<c:choose>
                		<c:when test="${confUser.acceptFlag eq 1}">
                			${LANG['bizconf.jsp.conf_invite_user_list.res5']}
                		</c:when>
                		<c:when test="${confUser.acceptFlag eq 0}">
                			${LANG['bizconf.jsp.conf_invite_user_list.res6']}
                		</c:when>
                		<c:otherwise>
                			${LANG['bizconf.jsp.conf_invite_user_list.res7']}
                		</c:otherwise>
                	</c:choose>
                </li>
            </ul>
           </c:forEach>
             
            </div>
        
        </div>
       
       
       
       
        <div class="First_Steps_bottom_b" style="padding-bottom: 0px;height: 40px;">
          <div class="but101">
          <span class="button_common"><a href="javascript:closeDialog();"><img src="${baseUrlStatic}/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px; margin-left:10px"/>${LANG['bizconf.jsp.conf_invite_user_list.res8000']}</a>
          </span></div>
          <div class="but111"></div>
        </div>
      </div>
        <!--${LANG['bizconf.jsp.add_contacts.res2']}========================================================================-->
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
