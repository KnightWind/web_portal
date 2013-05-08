<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>我的会议（正在开始的、即将开始的、错过的、加入过的）</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>
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
	/*
	 * 邀请参会人
	 */
	function inventContact(confId){
		parent.inventContact(confId);
	}
	
	/*
	 * 修改普通单次预约会议
	 */
	function updateBookMeeting(id) {
		parent.createReservationConf(id);
	}
	/*
	 * 修改周期会议中的某一天会议
	 */
	function updateAllBookMeeting(id) {
		parent.updateReservationConf(id);
	}
	/*
	 * 修改周期会议中所有会议的信息
	 * updateCycleBookMeetingInfo()
	 */
	function updateCycleBookMeetingInfo(id) {
		parent.updateCycleMeetingInfo(id);
	}
	/*
	 * 重新创建会议
	 */
	function reCreateReservationConf(id) {
		parent.reCreateReservationConf(id);
	}
	function delConf(confId){
		parent.confirmDialog("确认取消会议？",function(){
			$.ajax({
		      	type: "POST",
		      	url:"/user/conf/delete/"+confId,
		      	dataType:"json",
		      	success:function(data){
					if(data){
						 window.location.reload(true);
					}else{
						parent.errorDialog('取消会议出现异常！');
					}
		      	},
		        error:function(XMLHttpRequest, textStatus, errorThrown) {
		        	alert(XMLHttpRequest+"\n"+textStatus+"\n"+errorThrown);
		        }
			});  
		});	
	}
	jQuery(function($) {
		var links = [
		             "/user/conf/listWithDuringConf",
		             "/user/conf/listWithUpcomingConf",
		             "/user/conf/listWithMissConf",
		             "/user/conf/listWithAttendedConf"];
		$(".switchConf").click(function() {
			var index = $(this).index();
			var href = links[index];
			$("#contentFrame").attr("src",href);
			$(".meau_btn a").removeClass("activeMeau-left activeMeau-right");
			$(this).find("a").addClass("activeMeau-left activeMeau-right");
		});
		
		$("#contentFrame").load(function(){         
	        $(this).height($(this).contents().find("body").height() + 40);  
	    });
	});
</script>

</head>
<body>
<div class="main_content">
	<div class="meeting_top">
		<ul>
		<li class="meau_01 switchConf">
			<span class="meau_btn"><a href="javascript:;" class="activeMeau-left activeMeau-right"><img src="/static/images/icon_a.png" width="25" height="23" align="absmiddle" style=" margin-right:10px; margin-left:20px;"  />进行中的会议</a></span></li>
	    <li class="meau_02 switchConf">
	    	<span class="meau_btn"><a href="javascript:;"><img src="/static/images/icon_b.png" width="25" height="23" align="absmiddle" style=" margin-right:10px; margin-left:20px;"  />即将召开的会议</a></span></li>
	    <li class="meau_03 switchConf">
	    	<span class="meau_btn"><a href="javascript:;"><img src="/static/images/icon_c.png" width="25" height="23" align="absmiddle" style=" margin-right:10px; margin-left:20px;"  />未参加的会议</a></span></li>
	    <li class="meau_04 switchConf">
	    	<span class="meau_btn"><a href="javascript:;"><img src="/static/images/icon_d.png" width="25" height="23" align="absmiddle" style=" margin-right:10px; margin-left:20px;"  />已参加的会议</a></span></li>
	    </ul>
	</div>
	<div style="clear: both;"></div>
	<iframe frameborder="0" width="100%" height="550px;" id="contentFrame" name="contentFrame" scrolling="no" src="/user/conf/listWithDuringConf"></iframe>
</div>
</body>
</html>