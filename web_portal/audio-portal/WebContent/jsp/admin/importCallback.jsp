<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<script type="text/javascript">
	var statu = "${statu}"
	var showinfo = "${LANG['site.user.import.cbinfo1']} ${itemnum} ${LANG['site.user.import.cbinfo2']} ${iptitemnum} ${LANG['site.user.import.cbinfo3']}"
	//${LANG['bizconf.jsp.admin.importCallback.res1']}";
	<c:if test="${repeated != null}">
		showinfo += " ${LANG['site.user.import.cbinfo4']} ";
		<c:forEach var="rpt" items="${repeated}" varStatus="status">
		showinfo += "  ${rpt.loginName} ";
		</c:forEach>
	</c:if>
	<c:if test="${dissaveable != null}">
		showinfo += " ${LANG['site.user.import.cbinfo5']} ";
		<c:forEach var="unsave" items="${dissaveable}" varStatus="status">
		showinfo += "  ${unsave.loginName} ";
		</c:forEach>
	</c:if>
	var info = "";
	if(statu=="1"){
		info = showinfo;
	}else{
		info = "${LANG['site.user.import.cbinfo6']}${LANG['bizconf.jsp.admin.importCallback.res2']}";
	}
	window.parent.showInfo(info,statu);
	//alert("yes!");
</script>
