<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>
<title>${LANG['bizconf.jsp.common.bill_detaillist.res1']}</title>
</head>
<body onload="loaded()">
<div class="First_Steps_quick_e" style=" background:#FFF;">
          <p style="margin: 15px 20px;color:#666666;">${LANG['bizconf.jsp.common.bill_detaillist.res2']}</p>
          <div style=" background:#fff"><img class="toa_quick" src="${baseUrlStatic}/images/min.jpg" width="560" height="1" /></div>
          <div class="First_Steps_top" style=" background:#FFF"> </div>
          <div class="First_Steps_main_quick" >
            <table cellpadding="0" cellspacing="0" border="0" class="chaxun_top">
              <tr class="cx01">
                <td style=" font-weight:bold;">
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
                <td align="right"><strong style=" color:#F00">${LANG['bizconf.jsp.admin.mybilling_list.res8']}<fmt:formatNumber value=" ${fee}" pattern="#.00" type="number"/>${LANG['bizconf.jsp.admin.mybilling_list.res9']}</strong></td>
              </tr>
            </table>
            <form action="/common/billing/sysShowTelDetail" method="post">
			<input type="hidden" name="userId" value="${userId}" />
			<input type="hidden" name="id" value="${id}" />
			<input type="hidden" name="year" value="${year}" />
			<input type="hidden" name="month" value="${month}" />
            <div class="div_overflow">
            <c:if test="${fn:length(pageModel.datas)<=0}">
			           <div align="center" style="height: 35px;width: 100%;"> ${LANG['website.common.msg.list.nodata']}</div>
			</c:if>
			<c:forEach var="confBill" items="${pageModel.datas}" varStatus="status">
              <table cellpadding="0" cellspacing="0" border="0" class="chaxun_top">
                <tr>
                  <td style=" font-weight:bold;"></td>
                  <td></td>
                </tr>
                <tr class="cx02">
                  <td>${LANG['bizconf.jsp.common.bill_detaillist.res3']}${confBill.confHwid}</td>
                  <td></td>
                </tr>
              </table>
              <table cellpadding="0" cellspacing="0" border="0" class="chaxun_main">
               	 <tr class="cx03">
                  <td>${LANG['bizconf.jsp.admin.arrange_org_user.res8']}</td>
                  <td>${LANG['bizconf.jsp.common.bill_detaillist.res4']}</td>
                  <td>${LANG['bizconf.jsp.common.bill_detaillist.res5']}</td>
                  <td>${LANG['bizconf.jsp.common.bill_detaillist.res6']}</td>
                  <td>${LANG['bizconf.jsp.common.bill_detaillist.res7']}</td>
                  <td >${LANG['bizconf.jsp.common.bill_detaillist.res8']}</td>
                </tr>
              <c:if test="${confBill.billings !=null}">
                <c:forEach  var="bill" items="${confBill.billings}" varStatus="status">
	                <tr class="cx04">
	                  <td>${bill.userName}</td>
	                  <td>${bill.enterNumber }</td>
	                  <td>${bill.callNumber }</td>
	                  <td><fmt:formatDate value="${bill.startDate}" type="date" pattern="yyyy-MM-dd HH:mm"/></td>
	                  <td>${bill.showDuration}</td>
	                  <td><fmt:formatNumber value=" ${bill.totalFee}" pattern="#.00" type="number"/>${LANG['bizconf.jsp.admin.mybilling_list.res9']}</td>
	                </tr>
				</c:forEach>
				</c:if>
                <tr class="cx04">
                  <td colspan="6" align="right" style=" padding-right:10px; color:#F00">${LANG['bizconf.jsp.common.bill_detaillist.res9']}<fmt:formatNumber value=" ${confBill.sum}" pattern="#.00" type="number"/></td>
                </tr>
              </table>
		    </c:forEach>
		    <div style="margin-top: 20px;">
		    	 <jsp:include page="page_info.jsp" />
		    </div>
            </div>
            </form>
            <div class="but160" style=" margin-bottom:70px;"><span class="button_common"><a href="javascript:closeDialog();"><img src="${baseUrlStatic}/images/right.png" width="16" height="14" align="absmiddle" style=" margin-right:5px; margin-left:5px"/>${LANG['bizconf.jsp.common.bill_detaillist.res10']}</a></span></div>
          </div>
        </div>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#billingView");
	frame.trigger("loaded");
}
function closeDialog(result) {
	var dialog = parent.$("#billingView");
	if(result){
		dialog.trigger("closeDialog", [result]);
	} else {
		dialog.trigger("closeDialog");	
	}
}
</script>
