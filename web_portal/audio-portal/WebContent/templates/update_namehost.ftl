<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style></style>
</head>
<body style=" margin:0; padding:0; border:0; background:#;font-family:Arial, Helvetica, sans-serif;">
<div style=" margin:0; padding:0; border:0; background:#FFF; width:700px; height:auto;font-family:Arial, Helvetica, sans-serif; "> 
  <table cellpadding="0" cellspacing="0" border="0" width="700" style=" background:#fff;">
    <tr height="60" >
      <td>
        <h2 style=" margin:0; padding:0; border:0; font-size:20px; color:#333; border-bottom:#ccc 1px solid; padding-bottom:5px; margin:0px 40px; font-family:"微软雅黑";">主持人账号信息修改</h2>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#EB6C00; font-size:12px; margin:5px 40px 15px">尊敬的${user.trueName}，您好！</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px; font-family:Arial, Helvetica, sans-serif;">感谢您使用confcloud系统,您要求修改的信息已成功更改。可以开始享受confcloud系统给您带来的卓越服务。<br/>您可以点击<a href="http://${siteaddress}" style=" margin:0; padding:0; border:0;font-family:Arial, Helvetica, sans-serif;">confcloud会议管理平台</a>登录系统。
        </p>
       
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px 10px; font-family:Arial, Helvetica, sans-serif;">您的帐号信息如下：</p>
      </td>
    </tr>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff; margin-left:50px;">
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">登录名：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.loginName}</span></td>
    </tr>
	<#if user.loginPass?? && user.loginPass!=''>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">密码：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.loginPass}</span></td>
    </tr>
	</#if>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">用户名：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.trueName}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">邮箱：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.userEmail}</span></td>
    </tr>
    
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">授权点数信息：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;"></span></td>
    </tr>
	<#list lics as lic>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">最大点数：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.licNum}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">生效时间：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.effeDate?string("yyyy-MM-dd")}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">到期时间：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.expireDate?string("yyyy-MM-dd")}</span></td>
    </tr>
	<tr height="22"><td align="right" ></td><td align="left"></td></tr>
   </#list> 
   
  </table>
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff;">
    <tr height="20">
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:40px;line-height:22px; margin-top:10px;  "> 您有任何问题，可以参考<a href="http://${userGuider!'www.bizconf.cn'}" style=" margin:0; padding:0; border:0; color:#EB6C00" >用户手册</a>或联系客服。</p>
      </td>
    </tr>
    <tr height="20"><td>&nbsp;</td></tr>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" width="700" style="background:#004D9E;">
    <tr height="40" align="center" >
      <td>
        <p style="margin:0; padding:0; border:0; font-size:12px; color:#fff; line-height:20px; font-family:Arial, Helvetica, sans-serif">Copyright © 2003-2013 BizConf Telecom Co., Ltd. All right Reserved. 沪ICP备 11034277号-5  </p>
      </td>
    </tr>
  </table>
</div>
</body>
</html>