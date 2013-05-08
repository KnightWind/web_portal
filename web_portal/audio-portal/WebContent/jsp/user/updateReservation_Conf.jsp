<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>schedule meeting</title>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css?ver=${version}"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/js/jquery.uniform/themes/default/css/uniform.custom.css?ver=${version}">
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
	<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-1.8.3.min.js?ver=${version}"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-ui-1.9.2.custom.min.js?ver=${version}"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery-ui-i18n.js?ver=${version}"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.uniform.min.js?ver=${version}"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/date.js?ver=${version}"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/json2.js?ver=${version}"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/widgets.js?ver=${version}"></script>
	<script type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></script>

</head>
<body onload="loaded()">
	
	<div class="add16_main">
  <div class="add16_top">
    <div class="add16_top_left"></div>
    <div class="add16_top_center"></div>
    <div class="add16_top_right"></div>
  </div>
  <div class="add16_center">
    <div class="add16_center_left"></div>
    <div class="add16_center_center">
      <div class="First_Steps_quick_a" style=" background:#FFF">
        <div class="First_Steps_title_a"> <a href="javascript:;" onclick="closeDialog()"></a>
          <c:choose>
        		<c:when test="${delCycle eq '1'}">
        			<h3 class="tit_a">选择周期一个会议</h3>
		          	<p class="tit_b">删除成功后，默认将该会议移出周期会议。</p>
        		</c:when>
        		<c:otherwise>
			      <h3 class="tit_a">选定日期</h3>
		          <p class="tit_b">修改成功后，默认将该会议移出周期会议。</p>		 
        		</c:otherwise>
        	</c:choose>
        </div>
        <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
        <div class="Choose_date">
	        <a href="#" class="go_up">2013</a>
   	    <c:forEach var="conf" items="${confList}" varStatus="status">
<%--    	 <c:out value="${status.count%3}"></c:out> --%>
   	    	 <ul>
   	    	 	<c:choose>
   	    	 		<c:when test="${status.count == 1}">
	   	    			<li><input id = "cycleConfId" name="cycleConfId" type="radio" value="${conf.id}" checked="checked"/><fmt:formatDate value="${conf.startTime}" pattern="MM-dd" /></li>
   	    	 		</c:when>
   	    	 		<c:otherwise>
	   	    			<li><input id = "cycleConfId" name="cycleConfId" type="radio" value="${conf.id}" /><fmt:formatDate value="${conf.startTime}" pattern="MM-dd" /></li>
	   	    	 	</c:otherwise>
   	    	 	</c:choose>
		     </ul>
       	</c:forEach>
        </div>
      </div>
        <div class="Choose_date_bottom" style="position: absolute;bottom: 20px;height: 35px;">
        	<div class="Choose_date_btn02" style="margin-top: 0px;">
        		<span class="button_common">
	        	<a href="javascript:;" onclick = "closeDialog()">
	        		<img src="/static/images/quxiao.png" width="11" height="10" align="absmiddle" style=" margin-right:8px;"/>取消
	        	</a>
	        	</span>
        	</div>
        	<c:choose>
        		<c:when test="${delCycle eq '1'}">
        			<div class="Choose_date_btn01" style="padding-top: 0px;">
        				<span class="button_common">
		        		<a href="javascript:;" onclick = "delSignConf()">
		        			<img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-right:8px;"/>确定
		        		</a>
		        		</span>
	        		</div>
        		</c:when>
        		<c:otherwise>
	        		<div class="Choose_date_btn01" style="padding-top: 0px;">
	        			<span class="button_common">
		        		<a href="javascript:;" onclick = "updateBookMeeting()">
		        			<img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-right:8px;"/>选定
		        		</a>
		        		</span>
	        		</div>
        		</c:otherwise>
        	</c:choose>
        	
            
        </div>
    </div>
    <div class="add16_center_right"></div>
  </div>
  <div class="add16_bottom">
    <div class="add16_bottom_left"></div>
    <div class="add16_bottom_center"></div>
    <div class="add16_bottom_right"></div>
  </div>
</div>
	
	
	
	

</body>
</html>
<script type="text/javascript">
function updateBookMeeting() {
	var cycleConfId = $('input:radio[name="cycleConfId"]:checked').val();
	if(cycleConfId){
		parent.createReservationConf(cycleConfId);
		closeDialog();
	} else {
		parent.errorDialog("请至少选择其中的一天");
	}
}

function loaded() {
	var frame = parent.$("#updateOneMeeting");
	frame.trigger("loaded");
}
function closeDialog() {
	var dialog = parent.$("#updateOneMeeting");
	dialog.trigger("closeDialog");
}

function delSignConf(){
	if($('input:radio[name="cycleConfId"]:checked').length>0){
		parent.confirmDialog("确认取消选中会议？",function(){
			var cycleConfId = $('input:radio[name="cycleConfId"]:checked').val();
			$.ajax({
		      	type: "POST",
		      	url:"/user/conf/delete/"+cycleConfId,
		      	dataType:"json",
		      	success:function(data){
					if(data){
// 						parent.showURL("/user/conf/listConf");
						parent.refreshChildIframe();
						closeDialog();
					}else{
						parent.errorDialog('取消会议出现异常！');
					}
		      	},
		        error:function(XMLHttpRequest, textStatus, errorThrown) {
		        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
		        }
			}); 
		}); 
	}else{
		parent.errorDialog('请选择删除项！');
	}
}
</script>
