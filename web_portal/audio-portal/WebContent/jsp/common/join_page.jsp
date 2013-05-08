<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css"/>
<script type="text/javascript" src="${ctx}/static/js/jquery-1.8.3.js"></script> 
<title>插件验证</title>
</head>
<body>

<cc:confList var="JOIN_TYPE_CONFID"/>
<cc:confList var="JOIN_TYPE_SECURE_CODE"/>
<cc:confList var="JOIN_TYPE_EMAIL"/>
<cc:confList var="JOIN_TYPE_OURURL"/>
<c:if test="${joinType==JOIN_TYPE_SECURE_CODE}">
<!-- 通过安全会议号进入会议-->

<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="joinSecureDiv"  style="display:" >
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      
      <!--弹出层主题内容区域开始========================================================================-->
      
      <td class="overlay-content">
        <div class="First_Steps_quick_a" style=" background:#FFF;">
          <div class="First_Steps_title_a"> <a href="javascript:closeDialog();"></a>
            <h3 class="tit_a">加入会议</h3>
            <p class="tit_b">输入安全会议号，快速加入您的会议。</p>
          </div>
          <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
          <div class="First_Steps_top" style=" background:#FFF"> </div>
          <div class="First_Steps_main_quick">
            <table class="box_a_quick02">
             <form name="joinSecureForm" id="joinSecureForm" method="post" action="/join">
      <input type="hidden" name="joinType" id="joinType" value="${joinType}"/>
              <tr class="box01">
                <td align="right" class="left_text_a">姓名</td>
                <td align="left">
<%--                  <c:set var="userName" value="请输入用户名"/> --%>
              <c:if test="${currentUser!=null }">
              	<c:set var="userName" value="${currentUser.trueName }"/>
              </c:if>
              <input class="right_text_a" name="userName" id="userName" type="text"  value="${userName}"  watermark="请输入用户名"/>
                </td>
              </tr>
           
              <tr class="box01">
                <td align="right" class="left_text_a">安全会议号</td>
                <td align="left">
                  <input class="right_text_a" name="code" id="code"  type="text"  watermark="请输入安全会议号" />
                </td>
              </tr>
              <tr align="left">
            <td align="left" colspan="3">  
            	<div style="height: 50px;">
            		<div class="but150">
            			<span class="button_common">
            				<a  href="javascript:join();"><img src="/static/images/join.png" width="16" height="15" align="absmiddle" style=" margin-right:5px; margin-left:5px"/>加入会议</a>
            			</span>
            		</div>
            	</div>
            </td>
            </tr>
               </form>
            </table>
          </div>
          </div> 
      </td>
      
      <!--弹出层主题内容区域开始========================================================================-->
      
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>

</c:if>
<c:if test="${joinType==JOIN_TYPE_CONFID}">

<!-- 通过ID号进入公开会议 -->
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="joinPublicDiv" style="display:">
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      <td class="overlay-content">
      <!--弹出层主题内容区域开始========================================================================-->  
		<form name="joinConfIdForm" id="joinConfIdForm" method="post" action="/join">
      	<input type="hidden" name="cId" id="cId" value="${cId}"/>
      	<input type="hidden" name="joinType" id="joinType" value="${joinType}"/>
      	<div class="First_Steps_quick_a" style=" background:#FFF">
	        <div class="First_Steps_title_a"> <a href="javascript:"  onclick="javascript:closeDialog();" ></a>
	          <h3 class="tit_a">加入会议</h3>
	          <p class="tit_b">输入会议密码，加入您的会议。</p>
	        </div>
        <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
        <div class="First_Steps_top" style=" background:#FFF"> </div>
        <div class="First_Steps_main_quick">
          <table class="box_a_quick02">
            <tr class="box01">
              <td align="right" class="left_text_a">用户名</td>
              <td align="left">
<%--  				<c:set var="userName" value="请输入用户名"/> --%>
              	<c:if test="${currentUser!=null }">
              	<c:set var="userName" value="${currentUser.trueName }"/>
              	</c:if>
              	<input class="right_text_a" name="userName" id="userName" type="text" value="${userName}"/>
              </td>
            </tr>
            <c:if test="${passCheck==1}">
            <tr class="box01">
              <td align="right" class="left_text_a">会议密码</td>
              <td align="left">
              <input class="right_text_a" name="cPass" id="cPass"  type="password"   watermark="请输入会议密码" />
              </td>
            </tr>
            </c:if>
            <tr align="left">
            <td align="left" colspan="3">  
				<div style="height: 50px;">
	      		<div class="but150">
		          	<span class="button_common" style="margin-right: 5px;">
		          		<a href="javascript:join();">
		          			<img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-left:5px; margin-right:5px;"/>加入会议
		          		</a>	
		          	</span>
		          </div>  
	        </div>
            </td>
            </tr>
          </table>
        </div>
      </div>
      </form>
      <!--弹出层主题内容区域开始========================================================================-->      
      </td>
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>

</c:if>

<c:if test="${joinType==JOIN_TYPE_EMAIL}">
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="joinFromEmailDiv" style="display:">
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      <td class="overlay-content">
      <!--弹出层主题内容区域开始========================================================================-->  
		<form name="joinEmailForm" id="joinEmailForm" method="post" action="/join">
      <input type="hidden" name="cId" id="cId" value="${cId}"/>
      <input type="hidden" name="uId" id="uId" value="${uId}"/>
      <input type="hidden" name="scode" id="scode" value="${scode}"/>
      <input type="hidden" name="joinType" id="joinType" value="${joinType}"/>
      <div class="First_Steps_quick_a" style=" background:#FFF">
        <div class="First_Steps_title_a"> <a href="javascript:"  onclick="javascript:closeDialog();" ></a>
          <h3 class="tit_a">加入会议</h3>
          <p class="tit_b">输入用户名，加入您的会议。</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
        <div class="First_Steps_top" style=" background:#FFF"> </div>
        <div class="First_Steps_main_quick">
          <table class="box_a_quick02">
            <tr class="box01">
              <td align="right" class="left_text_a">用户姓名</td>
              <td align="left">
 			  	<c:set var="userName" value=""/>
              	<c:if test="${currentUser!=null }"><c:set var="userName" value="${currentUser.trueName }"/></c:if>
                <input class="right_text_a" name="userName" id="userName" type="text" value="${userName}" watermark="请输入用户名"/>
              </td>
            </tr>
            <tr>
            	<td colspan="3">
            		<div style="height: 50px;">
            			<div class="but150">
				          <span class="button_common" style="margin-right: 5px;">
				          	<a href="javascript:join();"><img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-left:5px; margin-right:5px;"/>加入会议</a>
				          </span>
				        </div>
            		</div>
            	</td>
            </tr>
          </table>
        </div>
      </div>
      </form>
      <!--弹出层主题内容区域开始========================================================================-->      
      </td>
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>

</c:if>

<%--


<!-- 未安装插件时，提示安装插件 -->

<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0"  id="installPlugDiv" style="display:none">
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      
      <!--弹出层主题内容区域开始========================================================================-->
      
      <td class="overlay-content">
        <div class="First_Steps_quick_b" style=" background:#FFF">
          <div class="First_Steps_title_a"> <a href="javascript:"  onclick="javascript:closeDialog();" ></a>
            <h3 class="tit_a">加入会议</h3>
            <p class="tit_b">进入该页面后请避免退出或刷新页面。</p>
          </div>
          <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
          <div class="First_Steps_top" style=" background:#FFF"> </div>
          <div class="First_Steps_main_quick">
            <p class="qidong">您尚未安装会议插件，请跟据浏览器弹出的提示安装插件！</p>
          </div>
          <!-- <div class="First_Steps_bottom01">
          <div class="but120"><a href="#"><img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-right:8px;"/>提交</a></div>
          
        </div>--> 
        </div>
      </td>
      
      <!--弹出层主题内容区域开始========================================================================-->
      
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>






<!-- 加载插件 -->

<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="loadPlugDiv" style="display:none">
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      
      <!--弹出层主题内容区域开始========================================================================-->
      
      <td class="overlay-content">
        <div class="First_Steps_quick_b" style=" background:#FFF">
          <div class="First_Steps_title_a"> <a href="javascript:"  onclick="javascript:closeDialog();" ></a>
            <h3 class="tit_a">加入会议</h3>
            <p class="tit_b">进入该页面后请避免退出或刷新页面。</p>
          </div>
          <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
          <div class="First_Steps_top" style=" background:#FFF"> </div>
          <div class="First_Steps_main_quick">
            <p class="qidong_jiazai">正在加载插件，请稍等...</p>
            <img src="/static/images/loading.gif" width="32" height="32" style=" margin:0px auto; display:block; margin-bottom:30px;" /> </div>
          <!-- <div class="First_Steps_bottom01">
          <div class="but120"><a href="#"><img src="/static/images/right.png" width="16" height="14" align="absmiddle" style=" margin-right:8px;"/>提交</a></div>
          
        </div>--> 
        </div>
      </td>
      
      <!--弹出层主题内容区域开始========================================================================-->
      
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>





<!-- 启动Client -->

<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="startClientDiv" style="display:none">
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      <td class="overlay-content">
      <!--弹出层主题内容区域开始========================================================================-->  
		<div class="First_Steps_quick_b" style=" background:#FFF">
        <div class="First_Steps_title_a"> <a   href="javascript:"  onclick="javascript:closeDialog();" ></a>
          <h3 class="tit_a">加入会议</h3>
          <p class="tit_b">输入您的会议安全号，快速加入您的会议。</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
        <div class="First_Steps_top" style=" background:#FFF"> </div>
        <div class="First_Steps_main_quick">
         <p class="qidong">请稍后，正在启动客户端...</p>
        </div>
      </div>
      <!--弹出层主题内容区域开始========================================================================-->      
      </td>
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>
 
 
 <!-- 下载Client -->
 <table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="downClientDiv" style="display:none">
    <tbody>
      <tr class="no-header">
        <td class="overlay-hdL"></td>
        <td class="overlay-hdC"></td>
        <td class="overlay-hdR"></td>
      </tr>
      <tr>
        <td class="overlay-bdL"></td>
        
        <!--弹出层主题内容区域开始========================================================================-->
        
        <td class="overlay-content"> 

        <div class="First_Steps_title_a"> <a href="javascript:"  onclick="javascript:closeDialog();" ></a>
          <h3 class="tit_a">加入会议</h3>
          <p class="tit_b">通过该页面您可以下载会议客户端进入会议。</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
        <div class="First_Steps_top" style=" background:#FFF"> </div>
        <div class="First_Steps_main_quick">
         <p class="qidong_top">您尚未安装会议客户端，要进入会议，您需要下载，安装并运行会议系统</p>
         <a href="javascript:" class="go_down" id="downLoadLink" onclick="">立即下载</a>
         <p class="qidong"><a href="#" style=" text-decoration:underline;color:#4B92D1"><img src="/static/images/help_bg.png" width="16" height="15" align="absmiddle" style=" margin-right:5px; " />如果会议系统未启动，请点击查看此帮助!</a></p>
        </div>
      
      </div>
    </td>
        
        <!--弹出层主题内容区域开始========================================================================-->
        
        <td class="overlay-bdR"></td>
      </tr>
      <tr>
        <td class="overlay-ftL"></td>
        <td class="overlay-ftC"></td>
        <td class="overlay-ftR"></td>
      </tr>
    </tbody>
  </table>
 
 <form name="downForm" id="downForm" method="post" action="/join/download" target="downFrame">
 <input name="userName" type="hidden" value="${userName}"/>
 <input name="cId" type="hidden" value="${cId}"/>
</form>
<iframe frameborder="0" width="0" height="0" scrolling="no" id="downFrame" name="downFrame"></iframe>




<!-- 错误的信息提示 -->
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="joinMtgErrorDiv" style="display:none">
  <tbody>
    <tr class="no-header">
      <td class="overlay-hdL"></td>
      <td class="overlay-hdC"></td>
      <td class="overlay-hdR"></td>
    </tr>
    <tr>
      <td class="overlay-bdL"></td>
      <td class="overlay-content">
      <!--弹出层主题内容区域开始========================================================================-->  
		<form name="joinForm" id="joinForm" method="post" action="/join">
	      	<input type="hidden" name="cId" id="cId" value="${cId}"/>
	      	<input type="hidden" name="joinType" id="joinType" value="${joinType}"/>
	      	<div class="First_Steps_quick_a" style=" background:#FFF">
		        <div class="First_Steps_title_a" style="height: 52px;"> 
		        	<a href="javascript:"  onclick="javascript:closeDialog();" ></a>
		          	<h3 class="tit_a">信息提示</h3>
		        </div>
	        	<div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
	        	<div class="First_Steps_top" style=" background:#FFF"> </div>
		        <div class="First_Steps_main_quick">
		          <table class="box_a_quick02" width="100%">
		            <tr class="box01">
		              <td align="center" colspan="2">
		              <c:set var="msgName" value="website.conf.join.type.${msgFlag}"/>
						<div style="height: 20px;line-height: 20px;">${LANG[msgName]}</div>
		              </td>
		           
		          </table>
		        </div>
		        <div class="First_Steps_bottom01"  style="height: 30px;">
		        	<div class="but150">
		        		<span class="button_common">
		        			<a href="javascript:" onclick="closeDialog();"><img src="/static/images/cross.png" width="16" height="15" align="absmiddle" style=" margin-right:5px; margin-left:5px"/>关闭</a>
		        		</span>
		        	</div>
		        </div>
	      </div>
      </form>
      <!--弹出层主题内容区域开始========================================================================-->      
      </td>
      <td class="overlay-bdR"></td>
    </tr>
    <tr>
      <td class="overlay-ftL"></td>
      <td class="overlay-ftC"></td>
      <td class="overlay-ftR"></td>
    </tr>
  </tbody>
</table>

--%>

</body>
</html>


<script type="text/javascript">
$(document).ready(function(){
	$(".right_text_a").keypress(function(e){
		if(e.keyCode == 13){
			join();
			return false;
		}
	});	
	
});

function join(){
	<c:if test="${joinType!=null}">
	var chkStatus=true;
	var isTip=false;
	<c:if test="${joinType==JOIN_TYPE_CONFID}">
	
	var userName=$("#userName").val();
	var cPass=$("#cPass").val();
	if(userName==null || userName==""  ){
		parent.errorDialog("请输入用户名");
		isTip=true;
		chkStatus=false;
	}
	<c:if test="${passCheck==1}">
	if(cPass==null || $.trim(cPass)==""  ){
		if(!isTip){
			parent.errorDialog("请输入会议密码");
		}
		isTip=true;
		chkStatus=false;
	}
	</c:if>
	if(chkStatus==true){
		$("#joinConfIdForm").submit();
	}
	</c:if>
	<c:if test="${joinType==JOIN_TYPE_SECURE_CODE}">
	
	var userName=$("#userName").val();
	var code=$("#code").val();
	if(userName==null || userName=="" ){
		parent.errorDialog("请输入用户名");
		isTip=true;
		chkStatus=false;
	}
	if(code==null || $.trim(code)=="" ){
		if(!isTip){
			parent.errorDialog("请输入安全会议号");
		}
		isTip=true;
		chkStatus=false;
	}
	if(chkStatus==true){
		$("#joinSecureForm").submit();
	}
//	$("#joinSecureForm").submit();
	</c:if>
	<c:if test="${joinType==JOIN_TYPE_EMAIL}">
	var userName=$("#userName").val();
	var code=$("#code").val();
	if(userName==null || userName==""){
		parent.errorDialog("请输入用户名");
		isTip=true;
		chkStatus=false;
	}
	if(chkStatus==true){
		$("#joinEmailForm").submit();
	}
//	$("#joinEmailForm").submit();
	//$("#joinSecureDiv").hide();
	//$("#joinFromEmailDiv").show();
	//$("#joinPublicDiv").hide();
	//$("#joinEmailForm").submit();
	</c:if>
	
	</c:if>
	
	
	
	
	
}

<%--	//显示错误信息
function showError(){
	$("#joinSecureDiv").hide();
	$("#joinPublicDiv").hide();
	
	$("#installPlugDiv").hide();
	$("#loadPlugDiv").hide();
	$("#startClientDiv").hide();
	$("#downClientDiv").hide();

	$("#joinMtgErrorDiv").show();
}
		--%>
			
	function resetFrameHeight(){
	    var pageHeight=document.body.scrollHeight;
	    var pageHeight=pageHeight+5;
	    parent.changeIframeSize("dialogFrame",0,pageHeight);
	}
	
	
	
	//系统加载
	window.onload=	function() {
		var frame = parent.$("#joinMeeting");
		frame.trigger("loaded");
		resetFrameHeight();
	}
	
	//关闭层
	function closeDialog() {
		var dialog = parent.$("#joinMeeting");
		dialog.trigger("closeDialog");
	}
	
	
	


	
</script>

<%--
function showJoinDiv(){
		
		resetFrameHeight();
	}
	function initPage(){
		showJoinDiv();
	}
	initPage();
		<c:if test="${joinType!=null}">
		$("#joinMtgErrorDiv").hide();
		<c:if test="${joinType==JOIN_TYPE_CONFID}">
		$("#joinSecureDiv").hide();
		$("#joinFromEmailDiv").hide();
		$("#joinPublicDiv").show();
		</c:if>
		<c:if test="${joinType==JOIN_TYPE_SECURE_CODE}">
		$("#joinSecureDiv").show();
		$("#joinFromEmailDiv").hide();
		$("#joinPublicDiv").hide();
		</c:if>
		<c:if test="${joinType==JOIN_TYPE_EMAIL}">
		$("#joinSecureDiv").hide();
		$("#joinFromEmailDiv").show();
		$("#joinPublicDiv").hide();
		</c:if>
		</c:if>
/*
	*  下载Client方法
	*/
	function download(){
		//调用下载的URL地址，去下载Client
		//$("#downClientDiv").show();
		//window.open("/join/download?cId="+cId);
		$("#downForm").submit();
		closeDialogForDelay();
		//alert("正在下载中，请稍候…………");
	}
	
	//延时关闭层
	function closeDialogForDelay(){
		window.setTimeout(closeDialog,2000);
	}
function initPage(){
			//var msgFlag="${msgFlag}";
			<c:set var="msgName" value="website.conf.joinouter.erroecode.${errorCode}"/>
			var msgFlag="${LANG[msgName]}";
			alert(msgFlag);
		}
		initPage();
 --%>
