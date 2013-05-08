<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>下载中心</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/common.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/index.css?ver=${version}"/>
</head>
<body>
<!--页面头部开始-->
<jsp:include page="header.jsp" />
<div id="head_bar">
  <div class="nav_profile">
  </div>
  <ul class="nav_help">
    <li class="bar01" align="absmiddle"><a  title="下载中心" href="/jsp/user/download_center.jsp" target="_blank">下载中心</a></li>
  </ul>
</div>

<!--使用帮助-->
<div class="main88">

  <h3>如何加入会议？</h3>
  <div class="fubiaoti">通过插件加入会议</div>
  <p>在点击开始会议/加入会议后，弹出插件安装提示（如下图）<span>注意：IE，火狐浏览器支持插件安装。</span></p>
  <p><img src="/static/images/down_help_img01.png" width="555" height="29" /></p>
  <p>点击安装后，可以自动启动会议。</p>
  <div class="fubiaoti">通过安装客户端加入会议</div>
  <p>点击下载，弹出下载工具。请您牢记迅雷等下载软件的下载路径。</p>
  <p><img src="/static/images/down_help_img02.png" width="459" height="352" /></p>
  <p><img src="/static/images/down_help_img03.png" width="265" height="72" /></p>
  <p>下载完成后，请您到目录文件夹找到安装文件，双击即可进行软件的安装进程。在安装前请您不要更改安装文件的名称。<span>注意：在安装过程中，如有防火墙或防御软件的阻止，请点击允许安装。</span></p>
  <p>点击安装后如下图显示：</p>
  <p><img src="/static/images/down_help_img04.png" width="549" height="487" /></p>
  <p>点击更多，允许程序所有操作，安装完成后，从新点击要加入的会议。</p>
  <p><img src="/static/images/down_help_img05.png" width="176" height="189" /></p>
  <p>客户端启动根据不同配置的电脑启动时间快慢不一样，因此可以等待几秒，当出现更新画面，表示安装成功。</p>
  <p><img src="/static/images/down_help_img06.png" width="380" height="212" /></p>
</div>

<!--页面下部-->
<div id="copy_close_downhelp">
<jsp:include page="footer.jsp" />
</div>
</body>
</html>