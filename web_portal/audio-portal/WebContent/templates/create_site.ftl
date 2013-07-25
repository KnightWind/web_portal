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
        <h2 style=" margin:0; padding:0; border:0; font-size:20px; color:#333; border-bottom:#ccc 1px solid; padding-bottom:5px; margin:0px 40px; font-family:"微软雅黑";">企业站点信息</h2>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#EB6C00; font-size:12px; margin:5px 40px 15px">尊敬的${userBase.trueName}，您好！</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px; font-family:Arial, Helvetica, sans-serif;">感谢您使用confcloud企业管理平台，
          您可以点击<a href="http://${siteBase.siteSign}.confcloud.cn/admin" style=" margin:0; padding:0; border:0;font-family:Arial, Helvetica, sans-serif;">confcloud企业管理平台</a>登录系统。 </p>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px 10px; font-family:Arial, Helvetica, sans-serif;">您的企业站点相关信息如下：</p>
      </td>
    </tr>
  </table>
  
  <!--左部主题信息区域================================================================-->
  
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff; margin-left:50px;">
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">企业名称：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.siteName}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">站点时区：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${timezone}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">站点类型：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">
		 <#if siteBase.siteFlag==1>
             正式
         <#else>
            试用
         </#if>
	  </span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">租用模式：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">
		 <#if siteBase.hireMode==1>
            包月
         <#else>
            记时  
         </#if>
	  </span></td>
    </tr>
    
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">计费类型：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">
		<#if siteBase.chargeMode==1>
            name host
        <#elseif siteBase.chargeMode==2>
            active user
        <#elseif siteBase.chargeMode==3>
            seats
        <#else>
            time
        </#if>
	  </span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">最大点数：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.license}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">站点生效日期：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.effeDate?string("yyyy-MM-dd")}</span></td>
    </tr>
     <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">站点到期日期：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.expireDate?string("yyyy-MM-dd")}</span></td>
    </tr>
    <tr height="26">
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif;">您的账号信息如下：</p>
      </td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">登录名：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.loginName}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">密码：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.loginPass}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">用户名：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.trueName}</span></td>
    </tr>
     <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">邮箱：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.userEmail}</span></td>
    </tr>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff;">
    <tr height="20">
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:40px;line-height:22px; margin-top:10px;  "> 您有任何问题，可以参考<a href="http://${userGuider!'www.confcloud.cn/help'}" style=" margin:0; padding:0; border:0; color:#EB6C00" >用户手册</a>或联系客服:400 082 6161</p>
      </td>
    </tr>
    <tr height="20">
      <td>&nbsp;</td>
    </tr>
  </table>
  
  <!--英文-->
  <table cellpadding="0" cellspacing="0" border="0" width="700" style=" background:#fff;">
    <tr height="60" >
      <td>
        <h2 style=" margin:0; padding:0; border:0; font-size:20px; color:#333; border-bottom:#ccc 1px solid; padding-bottom:5px; margin:0px 40px; font-family:"微软雅黑";">Enterprise Site Info</h2>
      </td>
    </tr>
    <tr>
      <td>
        <h2 style="margin:0; padding:0; border:0; color:#EB6C00; font-size:12px; margin:5px 40px 15px">Dear ${userBase.trueName},</h2>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px; font-family:Arial, Helvetica, sans-serif;">Thank you for using ConfCloud Enterprise Management platform.
          please click <a href="http://${siteBase.siteSign}.confcloud.cn/admin" style=" margin:0; padding:0; border:0;font-family:Arial, Helvetica, sans-serif;">ConfCloud Enterprise Management platform</a> to log in. </p>
      </td>
    </tr>
    <tr>
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; margin:0px 40px 10px; font-family:Arial, Helvetica, sans-serif;">Below is your Enterprise Site Info：</p>
      </td>
    </tr>
  </table>
  
  <!--左部主题信息区域================================================================-->
  
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff; margin-left:50px;">
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Enterprise Name：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.siteName}</span></td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Site TimeZone：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${timezone}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Site Type：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">
		 <#if siteBase.siteFlag==1>
            Formal
         <#else>
            Trial
         </#if>
	  </span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Lease Mode：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">
		 <#if siteBase.hireMode==1>
            Monthly Payment
         <#else>
            Timing  
         </#if>
	  </span></td>
    </tr>
    
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Charge Type：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">
		<#if siteBase.chargeMode==1>
            name host
        <#elseif siteBase.chargeMode==2>
            active user
        <#elseif siteBase.chargeMode==3>
            seats
        <#else>
            time
        </#if>
	  </span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Max Attendees：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.license}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Effective Time：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.effeDate?string("yyyy-MM-dd")}</span></td>
    </tr>
     <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Due Time：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${siteBase.expireDate?string("yyyy-MM-dd")}</span></td>
    </tr>
    <tr height="26">
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif;">Below is your account info：</p>
      </td>
    </tr>
    <tr  height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px;line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Login Name：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px; font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.loginName}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Password：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.loginPass}</span></td>
    </tr>
    <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">User Name：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.trueName}</span></td>
    </tr>
     <tr height="22">
      <td align="right"><span  style="margin:0; padding:0; border:0; color:#333; font-size:12px; line-height:22px; margin-left:65px; margin-top:18px; font-weight:bold;">Email：</span></td>
      <td align="left"><span style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:22px;font-family:Arial, Helvetica, sans-serif; line-height:20px; margin-left:5px;line-height:24px;">${userBase.userEmail}</span></td>
    </tr>
  </table>
  <table cellpadding="0" cellspacing="0" border="0" style=" background:#fff;">
    <tr height="20">
      <td>
        <p style=" margin:0; padding:0; border:0; font-size:12px; color:#666666; line-height:20px; margin-left:40px;line-height:22px; margin-top:10px;  "> Any questions, please refer to<a href="http://${userGuider!'www.confcloud.cn/help'}" style=" margin:0; padding:0; border:0; color:#EB6C00" >User Guide</a>or contact Customer Service:400 082 6161</p>
      </td>
    </tr>
    <tr height="20">
      <td>&nbsp;</td>
    </tr>
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
