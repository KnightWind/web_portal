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
        <h2 style=" margin:0; padding:0; border:0; font-size:20px; color:#333; border-bottom:#ccc 1px solid; padding-bottom:5px; margin:0px 40px; font-family:"微软雅黑";">主持人账号信息</h2>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#EB6C00; font-size:12px; margin:5px 40px 15px">尊敬的${user.trueName}，您好！</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px;">感谢您使用confcloud系统，现在可以享受confcloud系统给您带来的卓越服务。</p>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px;">您可以点击<a href="http://${siteaddress}" style=" margin:0; padding:0; border:0;">confcloud系统平台</a>登录系统。</p>
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
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">密码：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.loginPass}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">用户名：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.trueName}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">邮箱：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.userEmail}</span></td>
    </tr>
    <#if lics??>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">授权点数信息如下：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;"></span></td>
    </tr>
	<#list lics as lic>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">最大方数：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.licNum}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">生效时间：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.effeDate?string("yyyy-MM-dd")}</span></td>
    </tr>
      <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">到期时间：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.expireDate?string("yyyy-MM-dd")}</span></td>
    </tr>
	</#list>
	</#if>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff;">
    <tr height="20">
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px; margin-top:10px;  "> 您有任何问题，可以参考<a href="http://${userGuider!'www.confcloud.cn/help'}" style=" margin:0; padding:0; border:0; color:#EB6C00" >用户手册</a>或联系客服:400 082 6161</p>
      </td>
    </tr>
    <tr height="20"><td>&nbsp;</td></tr>
  </table>
  
  <!--英文-->
  <table cellpadding="0" cellspacing="0" border="0" width="700" style=" background:#fff;">
    <tr height="60" >
      <td>
        <h2 style=" margin:0; padding:0; border:0; font-size:20px; color:#333; border-bottom:#ccc 1px solid; padding-bottom:5px; margin:0px 40px; font-family:"微软雅黑";">Moderator Account Info</h2>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#EB6C00; font-size:12px; margin:5px 40px 15px">Dear ${user.trueName},</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px;">Thank you for choosing ConfCloud system where you will enjoy the excellent service provided by Confcloud.</p>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px;">Please click <a href="http://${siteaddress}" style=" margin:0; padding:0; border:0;">Confcloud System Platform</a> to log in.</p>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px 10px; font-family:Arial, Helvetica, sans-serif;">Below is your account info:</p>
      </td>
    </tr>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff; margin-left:50px;">
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Login Name：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.loginName}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Password：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.loginPass}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">User Name：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.trueName}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">Email：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${user.userEmail}</span></td>
    </tr>
    <#if lics??>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">Below is Authorized Points info：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;"></span></td>
    </tr>
	<#list lics as lic>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">Max Attendee Number：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.licNum}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">Effective Time：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.effeDate?string("yyyy-MM-dd")}</span></td>
    </tr>
      <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px;font-weight:bold; ">Due time：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666;line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${lic.expireDate?string("yyyy-MM-dd")}</span></td>
    </tr>
	</#list>
	</#if>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff;">
    <tr height="20">
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:65px;line-height:22px; margin-top:10px;  "> Any questions, please refer to<a href="http://${userGuider!'www.confcloud.cn/help'}" style=" margin:0; padding:0; border:0; color:#EB6C00" >User Guide</a>or contact Customer Service:400 082 6161</p>
      </td>
    </tr>
    <tr height="20"><td>&nbsp;</td></tr>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" width="700" style="background:#004D9E;">
    <tr height="40" align="center" >
      <td>
        <p style="margin:0; padding:0; border:0; font-size:12px; color:#fff; line-height:20px; font-family:Arial, Helvetica, sans-serif">Copyright © 2003-2013 BizConf Telecom Co., Ltd. All right Reserved. 沪ICP备 11034277号-5  &nbsp;&nbsp;</p>
      </td>
    </tr>
  </table>
</div>
</body>
</html>
