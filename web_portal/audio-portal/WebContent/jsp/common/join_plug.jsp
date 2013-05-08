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
<%@ include file="/jsp/common/cookie_util.jsp"%>
<title>插件验证</title>
</head>
<body>

<!-- 未安装插件时，提示安装插件 -->

<%--

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

--%>



<!-- 未安装插件时，提示安装插件 -->
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="installPlugUpDiv" style="display:none">
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
            <p class="tit_b">您可以根据该页面的提示轻松安装您的会议插件。</p>
          </div>
          <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
          <div class="First_Steps_top" style=" background:#FFF"> </div>
          <div class="First_Steps_main_quick">
          <img class="top_ico" src="/static/images/top_ico.png" width="146" height="143" />
          <p class="qidong_top">您尚未安装会议插件，请跟据浏览器弹出的提示安装插件！</p>
            <p class="tishi01">1.选中浏览器上方的黄色栏并选择安装...或运行...</p>
			<p class="tishi02">2.当系统要求您执行操作时，选择是，继续，安装或运行。
			</p>
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


<!-- 未安装插件时，提示安装插件 -->

<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="installPlugDownDiv" style="display:none" >
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
          <div class="First_Steps_title_a"> <a  href="javascript:"  onclick="javascript:closeDialog();" ></a>
            <h3 class="tit_a">加入会议</h3>
            <p class="tit_b">您可以根据该页面的提示轻松安装您的会议插件。</p>
          </div>
          <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
          <div class="First_Steps_top" style=" background:#FFF"> </div>
          <div class="First_Steps_main_quick">
          <img class="down_ico" src="/static/images/down_ico.png" width="146" height="142" />
          <p class="qidong_top_down">您尚未安装会议插件，请跟据浏览器弹出的提示安装插件！</p>
            <p class="tishi01_down">1.选中浏览器上方的黄色栏并选择安装...或运行...</p>
			<p class="tishi02_down">2.当系统要求您执行操作时，选择是，继续，安装或运行。</p>
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
          <div class="First_Steps_title_a"> 
            <h3 class="tit_a">加入会议</h3>
            <p class="tit_b">进入该页面后请避免退出或刷新页面。</p>
          </div>
          <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
          <div class="First_Steps_top" style=" background:#FFF"> </div>
          <div class="First_Steps_main_quick">
            <p class="qidong_jiazai">正在加载插件，请稍等...</p>
            <img src="/static/images/loading.gif" width="32" height="32" style=" margin:0px auto; display:block; margin-bottom:30px;" /> </div>
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

        <div class="First_Steps_title_a"> <a href="javascript:"  onclick="closeDialog();" ></a>
          <h3 class="tit_a">加入会议</h3>
          <p class="tit_b">通过该页面您可以下载会议客户端进入会议。</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
        <div class="First_Steps_top" style=" background:#FFF"> </div>
        <div class="First_Steps_main_quick">
         <p class="qidong_top99">您尚未安装会议客户端，要进入会议，您需要下载，安装并运行会议系统</p>
         <a href="JavaScript:download();" class="go_down99" id="downLoadLink" >立即下载</a>
         <p class="qidong"><a href="/jsp/user/download_help.jsp" style=" text-decoration:underline;color:#4B92D1" target="_blank"><img src="/static/images/help_bg.png" width="16" height="15" align="absmiddle" style=" margin-right:5px; " />如果会议系统未启动，请点击查看此帮助!</a></p>
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
 <input name="rId" type="hidden" value="${rId}"/>
</form>
<iframe frameborder="0" width="0" height="0" scrolling="no" id="downFrame" name="downFrame"></iframe>

<object id="meetplugin" width="0" height="0" codebase="http://www.confcloud.cn/download/kip/mcieplgmeet.cab"  classid="clsid:DE97A9DE-33E1-4862-AFFD-0579C960B31E" >
</object>


<embed id="joinConf" name="joinConf"  width="0" height="0" type="application/x-vnd-eSpace Meeting-scriptableplugin" >
</embed>
<%--

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
          <p class="tit_b">请输入你的账号后进入该会议。</p>
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
 

<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" id="downClientDiv" style="display:none">
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
        <div class="First_Steps_title_a"> <a  href="javascript:"  onclick="javascript:closeDialog();" ></a>
          <h3 class="tit_a">加入会议</h3>
          <p class="tit_b">请输入你的账号后进入该会议。</p>
        </div>
        <div style=" background:#fff"><img class="toa_quick" src="/static/images/min.jpg" width="410" height="1" /></div>
        <div class="First_Steps_top" style=" background:#FFF"> </div>
        <div class="First_Steps_main_quick">
         <p class="qidong">如果客户端未启动，请<a  href="javascript:" onclick="download();" style=" color:#03F; text-decoration:underline;">点击下载</a>安装客户端。</p>
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
	          <p class="tit_b">请输入你的账号后进入该会议。</p>
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
 <form name="downForm" id="downForm" method="post" action="/join/download" target="downFrame">
 <input name="userName" type="hidden" value="${userName}"/>
 <input name="cId" type="hidden" value="${cId}"/>
</form>
<iframe frameborder="0" width="0" height="0" scrolling="no" id="downFrame" name="downFrame"></iframe>

<object id="meetplugin" codebase="http://www.confcloud.cn/download/kip/mcieplgmeet.cab" width=0
        height=0 classid="clsid:2BB4DED4-1CF2-4b5f-8345-95D73E4197A1"
        VIEWASTEXT></object>
        
        
        
         	<c:if test="${msgFlag!=null && msgFlag!=''}"> 
		function initPage(){
			//var msgFlag="${msgFlag}";
			<c:set var="msgName" value="website.conf.join.type.${msgFlag}"/>
			var msgFlag="${LANG[msgName]}";
			alert(msgFlag);
		}
		initPage();
		 	</c:if> 
		
--%>

</body>
</html>






<script type="text/javascript">

        function initPage(){
                startConf();
        }
        initPage();

        var startStatus=false;


        //控制显示Plugin层
        function showPlugDiv(){
                <c:if test="${reload==null}">
                installPlug();
                </c:if>
                <c:if test="${reload!=null}">
                loadPlug();
                window.setTimeout(startConf,3000);
                </c:if>
        }



        function clearReload(){
                var domain=getDomain();
                clearCookie("reload",domain);
        }

        //打开下载插件层
        function installPlug(){
                var version=$.browser.version;
        //      alert("version="+version);
                var isShowUp=true;
                if($.browser.msie) {
                        if(parseFloat(version,10)>=9){
                                isShowUp=false;
                        }
                }

                // if($.browser.mozilla) {
                         
                 //}
                //alert("isShowUp="+isShowUp);
                $("#installPlugUpDiv").hide();
                $("#installPlugDownDiv").hide();
                if(isShowUp){
                        $("#installPlugUpDiv").show();
                }else{
                        $("#installPlugDownDiv").show();
                }
                $("#loadPlugDiv").hide();
                $("#downClientDiv").hide();
                resetFrameHeight();
                var domain=getDomain();
                setCookie("reload","1,${cId},${userName},${joinType},${code},${cPass},${rId}",domain);
        }
        //打开加载层
        function loadPlug(){
                $("#installPlugUpDiv").hide();
                $("#installPlugDownDiv").hide();
                $("#loadPlugDiv").show();
                $("#downClientDiv").hide();
                resetFrameHeight();
                window.setTimeout(downClient,10000);
                
                
        }

        //打开下载层，
        function downClient(){
                $("#installPlugUpDiv").hide();
                $("#installPlugDownDiv").hide();
                $("#loadPlugDiv").hide();
                $("#downClientDiv").show();
                resetFrameHeight();
        }

        //调用下载的URL地址，去下载Client
        var downCount=0;
        function download(){
        	if(downCount>0){
        		 $("#downLoadLink").attr("clickCount",1);
                 alert("已经下载,请不要重复下载！");
                 return false;
        	}
        	clearReload();
        	$("#downForm").submit();
        	closeDialogForDelay();
        	downCount++;
        }


        //启动会议
        function startConf(){
                var time1=(new Date()).getTime();
                if($.browser.msie) {
                        startConfForIe();
                }else if($.browser.mozilla) {
                        startConfForFireFox();

                }else{
                	/*
                	var isChrome = navigator.userAgent.toLowerCase().match(/chrome/) != null;
                	if (isChrome) {
                		//var downLoadLinkObj=document.getElementById("downLoadLink");
                		//if(downLoadLinkObj){
                		//	downLoadLinkObj.
                		//}
                		$("#downLoadLink").attr('onclick', '');//.unbind('click').click( function () { changeToOpen(suitId, img); }); 
                		$("#downLoadLink").attr('href', 'JavaScript:download();');

                	}
                	*/
                        downClient();//alert("请使用 IE 或者 FireFox 浏览器进入会议");//downClient();//
                }
                var time2=(new Date()).getTime();
                //alert(time2-time1);
        }
        /******************************************************************************
                                IE 通过插件启动会议
        *******************************************************************************/
        //启动IE插件
        function startConfForIe(){

                //createObjectForIe();
                var mtgObj=document.getElementById("meetplugin");
                try{
                        if(mtgObj){
                                mtgObj.attachEvent("IePluginDownloadComplete", startSucceedForIe);
                                mtgObj.attachEvent("IePluginDownloadException",startFailedForIe);
                                mtgObj.StartConf("${preParam}");
                        }
                }catch(e){
                        showPlugDiv()
                        return null;
                }

        } 
        //创建IE插件
        function createObjectForIe(){
                var ieObject=document.createElement("object");
                if(ieObject){
                        ieObject.setAttribute("id","meetplugin");
                        ieObject.setAttribute("width","0px");
                        ieObject.setAttribute("height","0px");
                        ieObject.setAttribute("codebase","http://www.confcloud.cn/download/kip/mcieplgmeet.cab");
                        
                        ieObject.setAttribute("classid","clsid:DE97A9DE-33E1-4862-AFFD-0579C960B31E");
                       // ieObject.setAttribute("classid","clsid:2BB4DED4-1CF2-4b5f-8345-95D73E4197A1");
                        document.body.appendChild(ieObject);
                }
        }

        //IE启动成功需要处理的内容
        function startSucceedForIe(){
                //alert("IE启动成功");
                //$("#downClientDiv").hide();
                //$("#startClientDiv").show();
                loadPlug();
                startStatus=true;
                closeDialogForDelay();
        }

        //IE启动失败调用 的方法
        function startFailedForIe(){
                showPlugDiv()
        }



        /******************************************************************************
                                        FireFox通过插件启动会议
        *******************************************************************************/

        function startConfForFireFox(){
                //createObjectForFireFox();
                var plg=document.getElementById("joinConf");  //获取插件的对象
                if(plg){
                        try{
                                plg.StartConf("${preParam}");  //调用插件的接口方法，preparam为小参数
                        }catch(e){
                                downClient();
                                return null;
                        }
                        var startStaus=plg.LoadPlugin; 
                        if(startStaus){
                                startSucceedForFireFox();
                        }else{
                                startFailedForFireFox();
                        }
                }
        }

        function createObjectForFireFox(){
                var firefox=document.createElement("embed");
                if(firefox){
                        firefox.setAttribute("id","joinConf");
                        firefox.setAttribute("name","joinConf");
                        firefox.setAttribute("width","0");
                        firefox.setAttribute("height","0");
                        firefox.setAttribute("type","application/x-vnd-eSpace Meeting-scriptableplugin");
                        document.body.appendChild(firefox);
                }
        }

        function startSucceedForFireFox(){
                loadPlug();
                startStatus=true;
                closeDialogForDelay();

        }

        function startFailedForFireFox(){
                //showPlugDiv()
                downClient();
        }


        function closeDialogForDelay(){
                window.setTimeout(closeDialog,5000);
        }

        window.onload=  function() {
                var frame = parent.$("#joinMeeting");
                frame.trigger("loaded");
        }

        function closeDialog() {
                var topUrl=top.location+"";
	            if(topUrl.indexOf("?")>-1){
	            	var domain="${domain}";
	            	top.location="http://"+domain;
	            }else{
	            	//非邮件连接方式 进入 会议，刷新 Top页面
	            	 top.location=topUrl;
	            }
                clearReload();
                var dialog = parent.$("#joinMeeting");
                dialog.trigger("closeDialog");
              
        }

        function resetFrameHeight(){
            var pageHeight=document.body.scrollHeight;
            var pageHeight=pageHeight+5;
            parent.changeIframeSize("dialogFrame",0,pageHeight);
        }

</script>
<%--

<script type="text/javascript">
	function initPage(){
		startConf();
	}
	initPage();
	
	var interDelay=2;
	var closeDelay=5;
	var plugDelay=20;
	var plugInter;
	
	//控制显示Plugin层
	function showPlugDiv(){
		<c:if test="${reload==null}">
		installPlug();
		</c:if>
		<c:if test="${reload!=null}">
		loadPlug();
		</c:if>
	}
	
	
	
	function clearReload(){
		var domain=getDomain();
		clearCookie("reload",domain);
	}
	
	//打开下载插件层
	function installPlug(){
		var version=$.browser.version;
	//	alert("version="+version);
		var isShowUp=true;
		if($.browser.msie) {
			if(parseFloat(version,10)>=9){
				isShowUp=false;
			}
		}

		// if($.browser.mozilla) {
			 
		 //}
		//alert("isShowUp="+isShowUp);
		$("#installPlugUpDiv").hide();
		$("#installPlugDownDiv").hide();
		if(isShowUp){
			$("#installPlugUpDiv").show();
		}else{
			$("#installPlugDownDiv").show();
		}
		$("#loadPlugDiv").hide();
		$("#downClientDiv").hide();
		resetFrameHeight();
		var domain=getDomain();
		setCookie("reload","1,${cId},${userName},${joinType},${code},${cPass},${rId}",domain);
	}
	//打开加载层
	function loadPlug(){
		$("#installPlugUpDiv").hide();
		$("#installPlugDownDiv").hide();
		$("#loadPlugDiv").show();
		$("#downClientDiv").hide();
		resetFrameHeight();
		window.setTimeout(downClient,plugDelay*1000);
		//plugInter=window.setInterval("startNoCallBackForIe()",interDelay*1000);
	}
	
	
	
	//打开下载层，
	function downClient(){
		$("#installPlugUpDiv").hide();
		$("#installPlugDownDiv").hide();
		$("#loadPlugDiv").hide();
		$("#downClientDiv").show();
		resetFrameHeight();
		plugInter=window.clearInterval(plugInter);
	}
	
	//调用下载的URL地址，去下载Client
	function download(){
		var clickCount=$("#downLoadLink").attr("clickCount");
		if(clickCount==null || clickCount==""){
			clearReload();
			$("#downForm").submit();
		}else{
			$("#downLoadLink").attr("clickCount",1);
			alert("已经下载,请不要重复下载！");
		}
		closeDialogForDelay();
	}
	/*
	var ii=0;
	function startNoCallBackForIe(){
		if($.browser.msie) {
			var mtgObj=document.getElementById("meetplugin");
			try{
				alert("AAAAA"+(ii++));
				if(mtgObj){
					mtgObj.attachEvent("IePluginDownloadComplete", plugInterClearStart);
					mtgObj.attachEvent("IePluginDownloadException",plugInterStart);
					mtgObj.StartConf("${preParam}");
				}
			}catch(e){
				return null;
			}
		}
	}
	function plugInterClearStart(){
		plugInter=window.clearInterval(plugInter);
	}

	function plugInterStart(){
		plugInter=window.setInterval("startNoCallBackForIe()",interDelay*1000);
	}
	
	*/
	//启动会议
	function startConf(){
		var time1=(new Date()).getTime();
		if($.browser.msie) {
			startConfForIe();
		}else if($.browser.mozilla) {
			startConfForFireFox();
			
		}else{
			downClient();//alert("请使用 IE 或者 FireFox 浏览器进入会议");//downClient();//
		}
		var time2=(new Date()).getTime();
		//alert(time2-time1);
	}
	/******************************************************************************
	       			IE 通过插件启动会议
	*******************************************************************************/
	//启动IE插件
	function startConfForIe(){
		
		//createObjectForIe();
		var mtgObj=document.getElementById("meetplugin");
		try{
			if(mtgObj){
				mtgObj.attachEvent("IePluginDownloadComplete", startSucceedForIe);
				mtgObj.attachEvent("IePluginDownloadException",startFailedForIe);
				mtgObj.StartConf("${preParam}");
			}
		}catch(e){
			showPlugDiv()
			return null;
		}
	
	} 
	//创建IE插件
	function createObjectForIe(){
		var ieObject=document.createElement("object");
		if(ieObject){
			ieObject.setAttribute("id","meetplugin");
			ieObject.setAttribute("width","0px");
			ieObject.setAttribute("height","0px");
			ieObject.setAttribute("codebase","http://www.confcloud.cn/download/kip/mcieplgmeet.cab");	
			ieObject.setAttribute("classid","clsid:2BB4DED4-1CF2-4b5f-8345-95D73E4197A1");
			document.body.appendChild(ieObject);
		}	
	}
	
	//IE启动成功需要处理的内容
	function startSucceedForIe(){
		//alert("IE启动成功");
		//$("#downClientDiv").hide();
		//$("#startClientDiv").show();
		loadPlug();
		closeDialogForDelay();
	}
	
	//IE启动失败调用 的方法
	function startFailedForIe(){
		showPlugDiv()
	}
		
		
		
	/******************************************************************************
					FireFox通过插件启动会议
	*******************************************************************************/
	
	function startConfForFireFox(){
		//createObjectForFireFox();
		var plg=document.getElementById("joinConf");  //获取插件的对象
		if(plg){
			try{
				plg.StartConf("${preParam}");  //调用插件的接口方法，preparam为小参数
			}catch(e){
				downClient();
				return null;
			}
			var startStaus=plg.LoadPlugin; 
			if(startStaus){
				startSucceedForFireFox();
			}else{
				startFailedForFireFox();
			}
		}
	}
	
	function createObjectForFireFox(){
		var firefox=document.createElement("embed");
		if(firefox){
			firefox.setAttribute("id","joinConf");
			firefox.setAttribute("name","joinConf");
			firefox.setAttribute("width","0");
			firefox.setAttribute("height","0");
			firefox.setAttribute("type","application/x-vnd-eSpace Meeting-scriptableplugin");
			document.body.appendChild(firefox);
		}
	}
	
	function startSucceedForFireFox(){
		loadPlug();
		closeDialogForDelay();
		
	}
	
	function startFailedForFireFox(){
		//showPlugDiv()
		downClient();
	}
		
		
	function closeDialogForDelay(){
		window.setTimeout(closeDelay,5000);
	}
	
	window.onload=	function() {
		var frame = parent.$("#joinMeeting");
		frame.trigger("loaded");
	}
	
	function closeDialog() {
		clearReload();
		var dialog = parent.$("#joinMeeting");
		dialog.trigger("closeDialog");
	}

	function resetFrameHeight(){
	    var pageHeight=document.body.scrollHeight;
	    var pageHeight=pageHeight+5;
	    parent.changeIframeSize("dialogFrame",0,pageHeight);
	}
	
</script>
--%>

