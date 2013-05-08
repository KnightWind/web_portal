<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style></style>
</head>
<body style=" margin:0; padding:0; border:0; background:#E6E6E6">
<div style=" margin:0; padding:0; border:0; background:#FFF; margin:0px auto; width:700px;"> 
  <table cellpadding="0" cellspacing="0" border="0" width="700" style=" background:#fff;">
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#EB6C00; font-size:12px; margin:50px 40px 16px">尊敬的${user.trueName}，您好！</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px;">您是<span href="#" style=" margin:0; padding:0; border:0; color:#EB6C00; font-weight:bold;" >"${conf.confName}"</span>的主持人,
         请<a href="http://${siteaddress}/?joinUrl=${joinUrl}" style=" margin:0; padding:0; border:0;" >点击加入</a>，可以开始您的会议！</p>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px;">以下是您的会议详细信息：</p>
      </td>
    </tr>
  </table>
  <table cellpadding="0" cellspacing="0" border="0">
  <tr>
  	<td>
  
  <table cellpadding="0" cellspacing="0" border="0" width="700" style=" background:#fff;">
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#333; font-size:12px; margin-left:65px; margin-top:18px; ">会议主题：</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px;  ">${conf.confName}</p>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#333; font-size:12px; margin-left:65px; margin-top:18px; ">会议主持人</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px;  ">${conf.compereName}</p>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#333; font-size:12px; margin-left:65px; margin-top:18px; ">会议开始时间：</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px;  ">${conf.startTime?string("yyyy-MM-dd HH:mm")}(${timezone})</p>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#333; font-size:12px; margin-left:65px; margin-top:18px; ">会议结束时间：</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px;  ">${conf.endTime?string("yyyy-MM-dd HH:mm")}(${timezone})</p>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#333; font-size:12px; margin-left:65px; margin-top:18px; ">会议安全号：</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px;  ">${conf.compereSecure}</p>
      </td>
    </tr>
    <#if conf.publicConfPass!=''>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#333; font-size:12px; margin-left:65px; margin-top:18px; ">会议密码：</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px;  ">${conf.publicConfPass!'无'}</p>
      </td>
    </tr>
    </#if>
    <!--<tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#333; font-size:12px; margin-left:65px; margin-top:18px; ">电话会议接入号：</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px; margin-bottom:25px;  ">华为项目会议</p>
      </td>
    </tr>-->
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px; margin-bottom:20px;  "> 您有任何问题，可以参考<a href="http://${userGuider!'www.bizconf.cn'}" style=" margin:0; padding:0; border:0; color:#EB6C00" >用户手册</a>或联系客服。</p>
      </td>
    </tr>
  </table>
  </td>
  </tr></table>
  <table cellpadding="0" cellspacing="0" border="0" width="700" style="background:#ccc;">
    <tr height="40" align="center" bgcolor="#004D9E">
      <td>
        <p style="margin:0; padding:0; border:0; font-size:12px; color:#fff; line-height:20px; font-family:Arial, Helvetica, sans-serif">Copyright © 2003-2013 BizConf Telecom Co., Ltd. All right Reserved. 沪ICP备 11034277号-5 </p>
      </td>
    </tr>
  </table>
</div>
</body>
</html>