<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>帮助</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/index.css?ver=${version}"/>
<link rel="stylesheet" href="${baseUrlStatic}/css/user/zTreeStyle/zTreeStyle.css" type="text/css">
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.ztree.core-3.5.min.js"></script>
<SCRIPT type="text/javascript">
		var setting = {
			view: {
				dblClickExpand: false,
				showLine: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick
			}
		};

		var zNodes =[
			{ id:1, pId:0, name:"用户指南", open:true, iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png", url:"/static/html/help/help1.html", target:"mainFrame"},
			{ id:11, pId:1, name:"会议管理平台", open:true, iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:111, pId:11, name:"会议管理界面", icon:"/static/images/help_ico02.png", url:"/static/html/help/help2.html", target:"mainFrame"},
			{ id:112, pId:11, name:"会议管理", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1121, pId:112, name:"即时会议", icon:"/static/images/help_ico02.png", url:"/static/html/help/help4.html", target:"mainFrame"},
			{ id:1122, pId:112, name:"预约会议", icon:"/static/images/help_ico02.png", url:"/static/html/help/help5.html", target:"mainFrame"},
			{ id:1123, pId:112, name:"邀请参会者", icon:"/static/images/help_ico02.png", url:"/static/html/help/help6.html", target:"mainFrame"},
			{ id:1124, pId:112, name:"快速入会", icon:"/static/images/help_ico02.png", url:"/static/html/help/help7.html", target:"mainFrame"},
			{ id:113, pId:11, name:"个人信息管理", icon:"/static/images/help_ico02.png", url:"/static/html/help/help8.html", target:"mainFrame"},
			{ id:114, pId:11, name:"通讯录", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1141, pId:114, name:"联系人", icon:"/static/images/help_ico02.png", url:"/static/html/help/help9.html", target:"mainFrame"},
			{ id:1142, pId:114, name:"群组", icon:"/static/images/help_ico02.png", url:"/static/html/help/help10.html", target:"mainFrame"},
			{ id:115, pId:11, name:"公告信息", icon:"/static/images/help_ico02.png", url:"/static/html/help/help11.html", target:"mainFrame"},
			{ id:12, pId:1, name:"会议客户端", open:true, iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:121, pId:12, name:"加入会议", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1211, pId:121, name:"通过邮件链接加入会议", icon:"/static/images/help_ico02.png", url:"/static/html/help/help12.html", target:"mainFrame"},
			{ id:1212, pId:121, name:"通过会议管理界面加入", icon:"/static/images/help_ico02.png", url:"/static/html/help/help13.html", target:"mainFrame"},
			{ id:122, pId:12, name:"了解会议窗口", icon:"/static/images/help_ico02.png", url:"/static/html/help/help14.html", target:"mainFrame"},
			{ id:123, pId:12, name:"管理会议", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1231, pId:123, name:"会议开始后邀请其他用户加入会议", icon:"/static/images/help_ico02.png", url:"/static/html/help/help15.html", target:"mainFrame"},
			{ id:1232, pId:123, name:"会议控制", icon:"/static/images/help_ico02.png", url:"/static/html/help/help16.html", target:"mainFrame"},
			{ id:1233, pId:123, name:"录制", icon:"/static/images/help_ico02.png", url:"/static/html/help/help17.html", target:"mainFrame"},
			{ id:124, pId:12, name:"管理音频", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1241, pId:124, name:"会场静音和取消静音", icon:"/static/images/help_ico02.png", url:"/static/html/help/help18.html", target:"mainFrame"},
			{ id:125, pId:12, name:"管理视频", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1251, pId:125, name:"开启摄像头", icon:"/static/images/help_ico02.png", url:"/static/html/help/help19.html", target:"mainFrame"},
			{ id:126, pId:12, name:"管理数据", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1261, pId:126, name:"共享文档", icon:"/static/images/help_ico02.png", url:"/static/html/help/help20.html", target:"mainFrame"},
			{ id:1262, pId:126, name:"共享白板", icon:"/static/images/help_ico02.png", url:"/static/html/help/help21.html", target:"mainFrame"},
			{ id:1263, pId:126, name:"共享屏幕", icon:"/static/images/help_ico02.png", url:"/static/html/help/help22.html", target:"mainFrame"},
			{ id:1264, pId:126, name:"媒体共享", icon:"/static/images/help_ico02.png", url:"/static/html/help/help23.html", target:"mainFrame"},
			{ id:1265, pId:126, name:"参与投票", icon:"/static/images/help_ico02.png", url:"/static/html/help/help24.html", target:"mainFrame"},
			{ id:127, pId:12, name:"会议工具", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1271, pId:127, name:"会议中上传、下载文件", icon:"/static/images/help_ico02.png", url:"/static/html/help/help25.html", target:"mainFrame"},
			{ id:128, pId:12, name:"主持人退出会议和结束会议", iconOpen:"/static/images/help_ico01.png", iconClose:"/static/images/help_ico01.png"},
			{ id:1281, pId:128, name:"退出会议", icon:"/static/images/help_ico02.png", url:"/static/html/help/help26.html", target:"mainFrame"},
			{ id:1281, pId:128, name:"结束会议", icon:"/static/images/help_ico02.png", url:"/static/html/help/help27.html", target:"mainFrame"}
		];

		function onClick(e,treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			zTree.expandNode(treeNode);
		}

		$(document).ready(function(){
			$("#mainFrame").load(function(){
				resizeHeight();  
		    });
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
		
		function resizeHeight() {
			var height = $(window).height()-156;
			if(height<550){
				height = 550;
			}
			$("#mainFrame").height(height);
			$(".help_left").height(height);
		}

	</SCRIPT>
	<style type="text/css">
	html{
		overflow: hidden;
	}
		.ztree li a{
			overflow: hidden;
		    text-overflow: ellipsis;
		    white-space: nowrap;
		    width: 120px;
		    word-break: keep-all;
		}
	</style>	
</head>
<body>
<!--页面头部开始-->
<jsp:include page="header.jsp" />
<div id="head_bar">
  <div class="help_subnav"><img src="/static/images/help_ico07.png" width="18" height="19" align="absmiddle" /><span style="position: relative;top: 8px;left: 10px;">帮助文档</span></div>
</div>
<div class="help_left" style="min-height: 550px;overflow-y: auto;padding-top: 10px;">
	<ul id="treeDemo" class="ztree"></ul>
</div>
<div class="main_right">
	<iframe width="100%" height="500" frameborder="0" id="mainFrame" name="mainFrame" scrolling="auto" src="/static/html/help/help1.html#"></iframe>
</div>
<!--页面下部-->
<div id="copy_close_downhelp">
<jsp:include page="footer.jsp" />
</div>
</body>
</html>