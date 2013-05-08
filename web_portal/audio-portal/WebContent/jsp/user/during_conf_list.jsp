<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>正在开始的会议</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/jquery.plugin.js?ver=${version}"></script>
<script type="text/javascript">
/*
 * 加入会议
 */
function joinMeeting(joinType,cId){//cId,cPass,code){
	parent.joinMeeting(joinType,cId);
}
/*
 * 查看会议详情
 */
function viewConf(id){
	parent.viewConf(id);
}

function enterSumbit(url){  
    var event=arguments.callee.caller.arguments[0]||window.event;//消除浏览器差异   
    if (event.keyCode == 13){       //监听回车键
    	resetPageNo();
    	queryConf.action = "/user/conf/listWithDuringConf";
    	queryConf.submit();	
    }   
} 

$(function() {
	$("#toSearch").click(function(){
		resetPageNo();
		queryConf.action = "/user/conf/listWithDuringConf";
		queryConf.submit();	
	});
	$(".during_conf_search").watermark('主题、主持人');
});
</script>

</head>
<body>
<form id="queryConf" name="queryConf" action="" method="post">
  <div class="meeting_main" >
  <table width="100%" align="center" cellpadding="0" cellspacing="0" border="0" id="t_box" >
  <tr>
     <td height="40" colspan="6" bgcolor="#EAF4FC" class="tr_top">
     <input name="titleOrHostName" id="titleOrHostName" class="meeting_ss during_conf_search" type="text" value="${titleOrHostName }" onkeydown='enterSumbit("/admin/conf/list")'/>
     <input class="meeting_but" type="button" id="toSearch"/></td>
  </tr>
   <tr align="center" height="35" class="tr_center" bgcolor="#000066">
        <td width="25%" class="tr_center">会议主题</td>
        <td width="15%" class="tr_center">主持人</td>
        <td width="15%" class="tr_center">会议时间</td>
        
       
        <td width="10%" class="tr_center">邀请人数</td>
        <td width="10%" class="tr_center">在线人数</td>
        <td width="25%" class="tr_center" style=" border-right:#D2D8DB 1px solid">操作</td>
    </tr>
  
  <c:if test="${fn:length(confList)<=0 }">
      <tr>
        <td height="32" class="STYLE19" colspan="6" align="center">
     		${LANG['website.common.msg.list.nodata']}
        </td>
      </tr>
   </c:if>
   
   
   <c:if test="${fn:length(confList)>0 }">
    <c:forEach var= "conf" items = "${confList}"  varStatus="status">	
    <tr align="center" bgcolor="#FFFFFF" height="32">
	    <td class="tr_main" style=" border-left:#D2D8DB 1px solid">
	    <a href="javascript:;" onclick="viewConf(${conf.id})" style="text-decoration:underline"><span style="color:#73798E">${conf.confName }</span></a>
	    </td>
	    <td class="tr_main">${conf.compereName}</td>
	    <td class="tr_main"><fmt:formatDate value="${conf.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
	    <td class="tr_main">${participantSizeList[conf.id]}</td>
	    <td class="tr_main">${conf.pcNum+conf.phoneNum}</td>
	    <td class="tr_main" style=" border-right:1px solid #D2D8DB" align="center">
     		<table>
		     <tr>
		       <td class="k_a">
		       	 <a href="javascript:;" onclick="javascript:joinMeeting(1,'${conf.id}');"><img src="/static/images/jiaru.png" width="16" height="15" align="absmiddle" style=" margin-right:5px;" />
	              	 <c:if test="${user.id != conf.compereUser}"><span style="color:#73798E">加入会议 </span></c:if>
	           		 <c:if test="${user.id eq conf.compereUser}"><span style="color:#73798E">开始会议</span> </c:if>
              	 </a>
		       </td>
		     </tr>
     		</table>
   		</td>
     </tr> 
      
    </c:forEach>
    </c:if>
    
	  <tr>
	    <td class="table_bottom" height="38" colspan="6">
	     <jsp:include page="/jsp/common/page_info.jsp" />
		</td>
	  </tr>
	  
	  
    </table>
  </div>
<!-- </div> -->
</form>
</body>
</html>