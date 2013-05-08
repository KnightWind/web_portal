<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="head">
  <div class="header_left"> 
  	<c:if test="${!empty siteBase.siteLogo}">
  		<img style=" float:left" class="yh_logo png" onerror="this.src='${baseUrlStatic}/images/logo.png';" src="${siteBase.siteLogo}" height="48" />
  	</c:if>
  	<c:if test="${empty siteBase.siteLogo}">
  		<img style=" float:left" class="yh_logo png" onerror="this.src='${baseUrlStatic}/images/logo.png';" src="${baseUrlStatic}/images/logo.png" height="48" />
  	</c:if>
  	<h3 style=" float:left; padding-top:30px; padding-left:20px; color:#013A7E; font-size:24px; font-family:'微软雅黑';">${siteBase.siteName}</h3>

  </div>
      <div class="header_right" style="display: none;">
     	<div class="header_right01">
     		<c:if test="${!empty siteBase}">
				<form name="langForm" id="langForm">
			  <select id="lang" name="lang">
			    <option value="zh-cn">${LANG['website.lang.zh']}</option>
<%-- 				<option value="en-us">${LANG['website.lang.en']}</option> --%>
			  </select>
			</form>     		
     		</c:if>
     	</div>
     </div>
</div>