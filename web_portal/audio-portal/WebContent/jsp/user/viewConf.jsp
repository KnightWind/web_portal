<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>查看会议详情</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- Css -->	
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/popupbox.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/box.css?ver=${version}"/>

<!-- Javascript -->
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>

<script type="text/javascript">
	$(function() {
		var confType = "${confType}";
		if(confType == "") {
			$(".confTypeTR").hide();
		}	
		
		var confDesc = "${conf.confDesc}";
		if(confDesc == ""){
			$(".confDescTR").hide();
		}
		
	});


</script>


<script type="text/javascript">
function joinMeeting(joinType,cId){
	parent.joinMeeting(joinType,cId);
	closeDialog();
} 
function copyConfLink(meintext) {
	if (window.clipboardData){
		// the IE-manier
		window.clipboardData.setData("Text", meintext);
	} else {
		parent.errorDialog("您使用的浏览器不支持此复制功能，请使用Ctrl+C或鼠标右键");
	}

}
</script>

</head>
<body onload="loaded()">
<table class="overlay-panel" border="0" cellpadding="0" cellspacing="0" >
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
        <div class="First_Steps_quick_meeting02" style=" background:#FFF">
          <div class="First_Steps_title_meeting02"> <a href="javascript:closeDialog();"></a>
            <h3 class="tit_a_meeting">${LANG['system.menu.info.meeting']}</h3>
          </div>
          <div style=" background:#fff"><img class="toa_quick_meeting02" src="/static/images/min.jpg" width="620" height="1" /></div>
          <div class="mmm">
          <div class="First_Steps_main_quick_meeting">
            <table width="320" cellpadding="0" cellspacing="0" border="0" id="xx_meeting02" style=" float:left;">
              <tr>
                <td align="right" class="xx_07">${LANG['system.list.meeting.title']}：</td>
                <td align="left"><span class="xx_08">${conf.confName }</span></td>
              </tr>
            
             <c:if test="${!empty confCycle}">
               <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.mode.regular']}：</td>
                <td align="left"><span class="xx_08">${cycleMode }</span></td>
              </tr> 
             <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.duplicate']}：</td>
                <td align="left">
                <span class="xx_08">
                <fmt:formatDate value="${confCycle.beginDate}" pattern="yyyy-MM-dd" />----<fmt:formatDate value="${confCycle.endDate}" pattern="yyyy-MM-dd" />
                </span>
                </td>
              </tr> 
             </c:if> 
              
              
                <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.starttime']}：</td>
                <td align="left">
                	<span class="xx_08">
	                	<fmt:formatDate value="${conf.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
			            <c:if test="${!empty user }">
			            	(${user.timeZoneDesc })
			            </c:if>
			            <c:if test="${empty user }">
		                	(${site.timeZoneDesc })
			            </c:if>
		            </span>
                </td>
              </tr>
              <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.duration']}：</td>
                <td align="left"><span class="xx_08">${duration}</span></td>
              </tr>
             
              <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.invite.number']}：</td>
                <td align="left"><span class="xx_08">${inviteUserCount}</span></td>
              </tr>
              
            <c:if test="${!empty user}">   
              <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.participant.confid']}:</td>
                <td align="left"><span class="xx_08">${conf.userSecure }</span></td>
              </tr>
              <c:if test="${user.id eq conf.compereUser}">
	              <tr>
	                <td align="right" class="xx_07" valign="top">${LANG['user.menu.conf.host.confid']}:</td>
	                <td align="left" valign="top">
	                  <p class="xx_08">${conf.compereSecure }</p>
	                </td>
	              </tr>
              </c:if>
            </c:if>  
              
            </table>
          </div>
          <img src="/static/images/add_li_bg.png" width="5" height="200" style=" float:left;" />
          <div class="First_Steps_main_quick_meeting" style=" float:left">
            <table width="320" cellpadding="0" cellspacing="0" border="0" id="xx_meeting02">
              
               <tr class="confTypeTR">
                <td align="right" class="xx_07">${LANG['user.menu.conf.conftype']}：</td>
                <td align="left">
                	<span class="xx_08" title="${confType }">${confType}</span>
               	</td>
              </tr>
            
              <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.aheadtime']}：</td>
                <td align="left"><span class="xx_08">${ aheadTime}</span></td>
              </tr>
              <tr>
                <td align="right" class="xx_07">${LANG['user.menu.conf.func']}：</td>
                <td align="left"><span class="xx_08" title="${clientFunc}">${clientFunc} </span></td>
              </tr>
              <cc:confList var="CONF_STATUS_FINISHED"/>
              <c:if test="${conf.confStatus != CONF_STATUS_FINISHED }">
	              <c:if test="${!empty user}"> 
	              	<c:if test="${user.id eq conf.compereUser}">
		              <tr>
		                <td align="right" class="xx_07">${LANG['user.menu.conf.host.url']}：</td>
		                <td align="left"><span class="" style="margin-left: 5px;"><input readonly="readonly" onmouseover="this.select()" type="text" value="${hostUrl}" style="width: 150px;height:20px;line-height:20px;border:1px solid #666666"/></span></td>
		              </tr>
		            </c:if>
		              
		              <tr>
		                <td align="right" class="xx_07">${LANG['user.menu.conf.act.url']}：</td>
		                <td align="left"><span class="" style="margin-left: 5px;"><input readonly="readonly" onmouseover="this.select()" type="text" value="${userUrl }" style="width: 150px;height:20px;line-height:20px;border:1px solid #666666"/></span></td>
		              </tr>
	             </c:if>
	             
	             <c:if test="${empty user && conf.publicFlag == 1}">
	           		  <tr>
		                <td align="right" class="xx_07">${LANG['user.menu.conf.act.url']}：</td>
		                <td align="left"><span class="" style="margin-left: 5px;"><input readonly="readonly" onmouseover="this.select()" type="text" value="${userUrl }" style="width: 150px;height:20px;line-height:20px;border:1px solid #666666"/></span></td>
	              	  </tr>
	             </c:if>
             </c:if>
              <tr class="confDescTR">
                <td align="right" class="xx_07">${LANG['user.menu.conf.desc']}：</td>
                <td align="left">
                  <p class="xx_08">${conf.confDesc }</p>
                </td>
              </tr>
            </table>
           
          </div></div>
          
          
          
          
          <div class="bb" style="clear: both;">
              <cc:confList var="CONF_STATUS_OPENING"/>
		      <cc:confList var="CONF_STATUS_SUCCESS"/>
		      <cc:confList var="JOIN_TYPE_CONFID"/>
          <div class="btn_message01">
       		<span class="button_common">
          		<a href="javascript:closeDialog();"><img src="/static/images/back.png" width="17" height="14" align="absmiddle" style=" margin-right:5px; margin-left:5px" />${LANG['website.message.back']}</a>
       		</span>
       	  </div>
       	  <c:if test="${conf.confStatus == CONF_STATUS_SUCCESS ||  conf.confStatus == CONF_STATUS_OPENING}">
          <div class="btn_message02">
          	<span class="button_common">
          		<a href="javascript:;" onclick="javascript:joinMeeting(${JOIN_TYPE_CONFID},'${conf.id}');">
          			<img src="/static/images/bullet_go.png" width="16" height="16" align="absmiddle" style=" margin-right:5px; margin-left:5px" />
          				<c:if test="${user.id != conf.compereUser}">
		           			${LANG['user.menu.conf.join']}
		           		</c:if>
		           		<c:if test="${user.id eq conf.compereUser}">
		           			${LANG['user.menu.conf.begin']}
		           		</c:if>
       			</a>
       		</span>
   		  </div>
          </c:if>
       	</div>
        </div>
      </td>
      
      <!--弹出层主题内容区域开始========================================================================-->
      
      
      
      
<!--              弹出层主题内容区域开始======================================================================== -->
        
<!--         <td class="overlay-content">  -->
<!--   <div class="First_Steps_quick_meeting" style=" background:#FFF;height: 384px"> -->
<!--     <div class="First_Steps_title_meeting"> <a href="javascript:closeDialog();"></a>  -->
<!--      <h3 class="tit_a_meeting">会议信息</h3> -->
     
<!--     </div> -->
<!--     <div style=" background:#fff"><img class="toa_quick_meeting" src="/static/images/min.jpg" width="410" height="1" /></div> -->
   
<!--     <div class="First_Steps_main_quick_meeting"> -->
<!--       <table width="400" cellpadding="0" cellspacing="0" border="0" id="xx_meeting"> -->
<!--        	<tr> -->
<!--         	<td align="right" class="xx_01">会议主题：</td> -->
<%--             <td align="left"><span class="xx_02">${conf.confName }</span></td> --%>
<!--         </tr> -->
<!--         <tr> -->
<!--         	<td align="right" class="xx_01">开始时间：</td> -->
<!--             <td align="left"> -->
<%-- 	            <span class="xx_02"><fmt:formatDate value="${conf.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /> --%>
<%-- 	            <c:if test="${!empty user }"> --%>
<%-- 	            	(${user.timeZoneDesc }) --%>
<%-- 	            </c:if> --%>
<%-- 	            <c:if test="${empty user }"> --%>
<%--                 	(${site.timeZoneDesc }) --%>
<%-- 	            </c:if> --%>
<!-- 	            </span> -->
<!--             </td> -->
<!--         </tr> -->
<%--         <c:if test="${!empty confCycle}"> --%>
<!-- 	        <tr> -->
<!-- 	        	<td align="right" class="xx_01">周期时间：</td> -->
<!-- 	            <td align="left"> -->
<!-- 	            <span class="xx_02"> -->
<%-- 	            <fmt:formatDate value="${confCycle.beginDate}" pattern="yyyy-MM-dd" /> -- --%>
<%-- 	            <fmt:formatDate value="${confCycle.endDate}" pattern="yyyy-MM-dd" /> --%>
<!-- 	            </span></td> -->
	            
<!-- 	        </tr> -->
<%--         </c:if> --%>
<!--         <tr> -->
<!--         	<td align="right" class="xx_01">会议时长：</td> -->
<%--             <td align="left"><span class="xx_02">${duration}</span></td> --%>
<!--         </tr> -->
<%--         <c:if test="${conf.confType != 0}"> --%>
<!-- 	        <tr> -->
<!-- 	        	<td align="right" class="xx_01">会议功能：</td> -->
<%-- 	            <td align="left"><span class="xx_02">${confType}</span></td> --%>
<!-- 	        </tr> -->
<%--         </c:if> --%>
<!--         <tr> -->
<!--         	<td align="right" class="xx_01">参会方数：</td> -->
<%--             <td align="left"><span class="xx_02">${confUserNum}</span></td> --%>
<%--              <td align="left"><span class="xx_02">${inviteUserCount}</span></td> --%>
<!--         </tr> -->
<%--         <c:if test="${!empty user}"> --%>
<%--         	<c:if test="${user.id eq conf.compereUser}"> --%>
<!-- 			    <tr> -->
<!-- 		        	<td align="right" class="xx_01">主持人会议安全号：</td> -->
<%-- 		            <td align="left"><span class="xx_02">${conf.compereSecure }</span></td> --%>
<!-- 		        </tr> -->
<%--         	</c:if> --%>
<!-- 	        <tr> -->
<!-- 	        	<td align="right" class="xx_01">与会者安全会议号：</td> -->
<%-- 	            <td align="left"><span class="xx_02">${conf.userSecure }</span></td> --%>
<!-- 	        </tr> -->
<%-- 	        <c:if test="${conf.publicFlag == 1}"> --%>
<!-- 		        <tr> -->
<!-- 		        	<td align="right" class="xx_01">公开会议会议密码：</td> -->
<%-- 		            <td align="left"><span class="xx_02">${conf.publicConfPass }</span></td> --%>
<!-- 		        </tr> -->
<%-- 	        </c:if> --%>
<%--         </c:if> --%>
<!--         <tr> -->
<!--         	<td align="right" class="xx_01">会议描述：</td> -->
<%--             <td align="left"><p class="xx_02">${conf.confDesc }</p></td> --%>
<!--         </tr> -->
<!--       </table> -->
<!--     </div> -->
<!--     <div style="position: absolute;bottom: 5px;"> -->
<!--       <div class="btn09"> -->
<!--       	<span class="button_common"> -->
<!--       		<a href="javascript:closeDialog();"><img src="/static/images/back.png" width="16" height="14" align="absmiddle" style=" margin-right:8px; margin-left:5px"/>返回</a> -->
<!--       	</span> -->
<!--       </div> -->
<%--       <cc:confList var="CONF_STATUS_OPENING"/> --%>
<%--       <cc:confList var="CONF_STATUS_SUCCESS"/> --%>
<%--       <cc:confList var="JOIN_TYPE_CONFID"/> --%>
      
<%--      	<c:if test="${conf.confStatus == CONF_STATUS_SUCCESS ||  conf.confStatus == CONF_STATUS_OPENING}"> --%>
<!--      	<div class="btn08"> -->
<!--      		<span class="button_common"> -->
<%-- 	      <a href="javascript:;" onclick="javascript:joinMeeting(${JOIN_TYPE_CONFID},'${conf.id}');"> --%>
<!-- 	      <img src="/static/images/join.png" width="16" height="14" align="absmiddle" style=" margin-right:5px; margin-left:2px"/> -->
<%-- 	      		<c:if test="${user.id != conf.compereUser}"> --%>
<!--            			加入会议 -->
<%--            		</c:if> --%>
<%--            		<c:if test="${user.id eq conf.compereUser}"> --%>
<!--            			开始会议 -->
<%--            		</c:if> --%>
<!--    		  </a> -->
<!--    		  </span> -->
   		  
<!--       </div> -->
<%--       	</c:if>     --%>
<!--     </div> -->

      
    
<!-- </div> -->
<!--  </td> -->
        
<!--         弹出层主题内容区域开始======================================================================== -->
      
      
      
      
        
        
        
        <td class="overlay-bdR"></td>
      </tr>
      <tr>
        <td class="overlay-ftL"></td>
        <td class="overlay-ftC"></td>
        <td class="overlay-ftR"></td>
      </tr>
    </tbody>
  </table>
</body>
</html>
<script type="text/javascript">
function loaded() {
	var frame = parent.$("#viewMeeting");
	frame.trigger("loaded");
}
function closeDialog(){
	var frame = parent.$("#viewMeeting");
	frame.trigger("closeDialog");
}
</script>