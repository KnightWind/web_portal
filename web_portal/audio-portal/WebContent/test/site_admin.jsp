<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>site admin</title>
<style type="text/css" title="currentStyle">
@import "/static/css/reset.css";
@import "/static/css/style.css";
@import "/static/js/jquery.layout.all-1.2.0/jquery.layout.css";
@import "/static/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css";
</style>
<script type="text/javascript" language="javascript" src="/static/js/jquery-ui-1.9.2.custom/js/jquery-1.8.3.js"></script>
<script type="text/javascript" language="javascript" src="/static/js/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.js"></script>
<script type="text/javascript" language="javascript" src="/static/js/jquery.layout.all-1.2.0/jquery.layout-latest.js"></script>
<script type="text/javascript">
$(document).ready(function () {
	$('body').layout({ 
		north: {
		    enableCursorHotkey: false,
		    closable: false,
		    resizable: false,
		    spacing_open: 0,
		    spacing_closed: 0,
		    size: 80
		},
		south: {
		    enableCursorHotkey: false,
		    closable: false,
		    resizable: false,
		    spacing_open: 0,
		    spacing_closed: 0
		},
		west: {
		    enableCursorHotkey: false,
		    closable: true,
		    resizable: false,
		    spacing_open: 6,
		    spacing_closed: 6
		}
	});
	
    $( "#accordion" ).accordion({
        collapsible: true
    });
});

function showURL(url){
    document.getElementById('rFrame').src=url;
} 
</script>
</head>
<body>
<div class="ui-layout-center">
    <iframe width="100%" scrolling="no" height="100%" frameborder="no" src="site_info.jsp" marginheight="0" marginwidth="0" border="0" name="rFrame" id="rFrame"></iframe>
</div>
<div class="ui-layout-north">
	<div class="logo-text">网站管理</div>
</div>
<div class="ui-layout-south">
	<jsp:include page="copyright.jsp" />
</div>
<div class="ui-layout-west">
	<div id="accordion">
	    <h3>用户管理</h3>
	    <div>
	    	<a href="javascript:showURL('site.html')">企业用户管理</a><br/>
	    	<a href="javascript:showURL('site.html')">企业管理员信息</a><br/>
	    	<a href="javascript:showURL('site.html')">企业信息管理</a><br/>
	    </div>
	    <h3>站点配置</h3>
	    <div>
	    	<a href="javascript:showURL('notice.html')">邮件服务器配置</a><br/>
	    </div>
	    <h3>会议管理</h3>
	    <div>
	        <a href="javascript:showURL('meeting.html')">会议缺省值设置</a><br/>
	    </div>
	    <h3>信息查询</h3>
	    <div>
	        <a href="javascript:showURL('meeting.html')">会议信息</a><br/>
	        <a href="javascript:showURL('meeting.html')">企业管理员操作日志</a><br/>
	        <a href="javascript:showURL('meeting.html')">企业用户操作日志</a><br/>
	    </div>
	    <h3>公告管理</h3>
	    <div>
	        <a href="javascript:showURL('meeting.html')">公告信息</a><br/>
	    </div>
	    
	</div>
</div>
</body>
</html>