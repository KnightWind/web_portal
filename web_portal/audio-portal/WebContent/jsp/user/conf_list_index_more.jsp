<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!--创建会议成功后，若该会议为7天内会议则插入会议列表，否则不插入-->
<cc:confList var="CONF_STATUS_OPENING"/>
<cc:confList var="CONF_STATUS_SUCCESS"/>
<cc:confList var="CONF_STATUS_FINISHED"/>
<c:forEach var="newConfBase" items="${confList}">
<div class="extras-container">
		<div class="extras-tr">
			<div class="date_part">
				<fmt:formatDate var="comingStartTime" value="${newConfBase.startTime}" pattern="yyyy-MM-dd HH:mm:ss" />
				<fmt:formatDate var="comingEndTime" value="${newConfBase.endTime}" pattern="yyyy-MM-dd HH:mm:ss" />
	          	<section class="timeago date-holder" title="${comingStartTime}" startTime="${comingStartTime}" endTime="${comingEndTime}" status="come"> 
	          		<span class="date">
	          			<fmt:formatDate value="${newConfBase.startTime}" pattern="yyyy-MM-dd" />
	          		</span> 
	          		<span class="time">
	          			<fmt:formatDate value="${newConfBase.startTime}" pattern="HH:mm"/>
	          		</span> 
	          	</section>
	        </div>
	        <div class="text_part" align="left">
	        	<section class="date-holder_01"> 
	        	
	        		<span class="title"><a href="javascript:;" onclick="viewConf(${newConfBase.id})">${newConfBase.confName}
	        			<c:forEach var="confCyc" items="${cycs}">
			        			<c:if test="${confCyc.id eq newConfBase.cycleId}">
			        				<c:choose>
			        					<c:when test="${confCyc.cycleType eq '1'}">
			        						<span style="color:#47ADE9;">(日会议)</span>
			        					</c:when>
			        					<c:when test="${confCyc.cycleType eq '2'}">
			        						<span style="color:#47ADE9;">(周会议)</span>
			        					</c:when>
			        					<c:when test="${confCyc.cycleType eq '3'}">
			        						<span style="color:#47ADE9;">(月会议)</span>
			        					</c:when>
			        				</c:choose>
			        			</c:if>
		        		</c:forEach>		
	        		</a></span> 
	        		<span class="content"><a href="#">${newConfBase.confDesc}</a></span>
	        	</section>
	        </div>
			<div class="action_part" style="float: right;">
				<table  border="0" align="right" cellpadding="0" cellspacing="0">
	          		<tr>
	          			<td class="" ><!-- 邀请（主持人专用） -->
	          				<c:if test="${user.id eq newConfBase.compereUser && newConfBase.confStatus==CONF_STATUS_SUCCESS}">
	          					<div class="k_f" onclick="inventContact(${newConfBase.id})"><img src="/static/images/yaoqing.png" width="21" height="16" align="absmiddle" style=" padding-right:5px;" />邀请</div>
          					</c:if>
          				</td>
	            		<td><div class="k02 viewtip durationTime" title="会议时长:${newConfBase.duration}分钟" duration="${newConfBase.duration}"><img src="${baseUrlStatic}/images/clockbtn.png" width="20" height="20" align="absmiddle" /><div class="k02_span durationMin">${newConfBase.duration}分钟</div></div></td>
	            		<td><div class="k02  k09 viewtip" title="主持人${newConfBase.compereName}"><img src="${baseUrlStatic}/images/zhuchuren.png" width="20" height="20" align="absmiddle" />${newConfBase.compereName}</div></td>
	            		<td><div onclick="editInventContact('${newConfBase.id}')" class="k04 attendee" title="尚未邀请人"><img src="${baseUrlStatic}/images/ico24.png" width="20" height="20" align="absmiddle" />0</div></td>
	            		<td><div title="查看会议选项" class="viewtip extras-default" onclick="toggleExtra(this)"><a href="javascript:;"><img src="${baseUrlStatic}/images/caozuo.png" width="20" height="20" /></a></div></td>
	            		<td>
	            			<div class="k06" onclick="javascript:joinMeeting(1,'${newConfBase.id}');">
	            				<c:if test="${newConfBase.confStatus==CONF_STATUS_FINISHED}">
	            					<c:if test="${user.id eq newConfBase.createUser}">
							              <a href="javascript:;" onclick="reCreateReservationConf(${newConfBase.id})">重新创建</a>
						            </c:if>
	            				</c:if>
	            				<c:if test="${newConfBase.confStatus!=CONF_STATUS_FINISHED}">
									<c:if test="${user.id != newConfBase.compereUser}">
				            			<a href="javascript:;">加入会议</a>
				            		</c:if>
				            		<c:if test="${user.id eq newConfBase.compereUser}">
				            			<a href="javascript:;">开始会议</a>
				            		</c:if>	            				
	            				</c:if>
		            		</div>
            			</td>
	          		</tr>
        		</table>
			</div>
			<span class="fader" onclick="faderClick(this)"></span>		
		</div>
   		<div class="extras-action">
				<div align="left" style="padding-top:10px">
					<c:if test="${newConfBase.confStatus!=CONF_STATUS_FINISHED}">
						<a class="email01" href="javascript:sendNoticeEmail(${newConfBase.id});"><img src="${baseUrlStatic}/images/email05.png" width="16" height="12" align="absmiddle" style=" padding-right:5px;"/>Outlook</a>
						<a class="email02" href="javascript:sendNoticeEmail(${newConfBase.id});" style="display: none;"> <img src="${baseUrlStatic}/images/ico002.png" width="16" height="11" align="absmiddle" style=" padding-right:5px;" />Gmail</a>
						<a class="email03" href="javascript:sendNoticeEmail(${newConfBase.id});" style="display: none;"><img src="${baseUrlStatic}/images/ico003.png" width="14" height="17" align="absmiddle" style=" padding-right:5px;" />Foxmail</a>
					</c:if>
				</div>
				<div align="right">
				<c:if test="${newConfBase.confStatus!=CONF_STATUS_FINISHED}">
					<c:if test="${user.id eq newConfBase.createUser}">
						<c:choose>
						 	<c:when test="${newConfBase.cycleId!=0}">
								<a class="email04" href="javascript:;" onclick="delAllConf(${newConfBase.cycleId},${newConfBase.id});">
									<img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;"/>取消全部</a>
								<a class="email04" href="javascript:;" onclick="delSignleConf(${newConfBase.cycleId});">
									<img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;"/>取消</a>
							 	<a class="email05" href="javascript:;" onclick="updateCycleBookMeetingInfo(${newConfBase.id})">
									<img src="${baseUrlStatic}/images/ico004.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;"/>修改全部</a>	
								<a class="email05" href="javascript:;" onclick="updateAllBookMeeting(${newConfBase.cycleId})">
									<img src="${baseUrlStatic}/images/ico004.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;"/>修改</a>
						 	</c:when>
						 	<c:otherwise>
						 		<a class="email04" href="javascript:;" onclick="delConf(${newConfBase.id});">
									<img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;"/>取消</a>
						 		<a class="email05" href="javascript:;" onclick="updateBookMeeting(${newConfBase.id})">
								<img src="${baseUrlStatic}/images/ico004.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;"/>修改</a>	
						 	</c:otherwise>
						</c:choose>
					</c:if>
				</c:if>
				<c:if test="${newConfBase.confStatus==CONF_STATUS_FINISHED}">
					<c:if test="${user.id eq newConfBase.createUser}">
	   					<a class="email04" href="#" onclick="delConf(${newConfBase.id});"><img src="${baseUrlStatic}/images/ico005.png" width="12" height="17" align="absmiddle" style=" padding-right:5px;" />删除</a>
	   				</c:if>
					<a class="email05" href="javascript:;" onclick="viewConf(${newConfBase.id});">
						<img src="${baseUrlStatic}/images/xiangq.png" width="16" height="16" align="absmiddle" style=" padding-right:5px;" />查看详情</a>
				</c:if>
				</div>
		</div>	
</div>
</c:forEach>
