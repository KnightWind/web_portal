<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
#head {
    background: url("/static/images/new_header_bg.jpg") repeat-x scroll left center transparent;
    height: 100px;
    min-width: 1002px;
    overflow: hidden;
    position: relative;
    width: 100%;
}

.logo_img {
    float: left;
    margin: 20px 0 0 40px;
}

.header_left h2 {
    bottom: 36px;
    color: #FFFFFF;
    font-family: "${LANG['bizconf.jsp.header.res1']}";
    font-size: 18px;
    left: 180px;
    position: absolute;
}
.header_left h2 strong{
    color: #FFFFFF;
    font-family: "${LANG['bizconf.jsp.header.res1']}";
    font-size: 18px;
}
.header_left {
    background: url("/static/images/new_header_left.jpg") no-repeat scroll left center transparent;
    float: left;
    height: 100px;
    width: 100%;
    position: absolute;
    left: 0px;
}

.new_li {
    left: 245px;
    position: absolute;
    top: 65px;
}
.new_ico02 {
    left: 288px;
    position: absolute;
    top: 70px;
    z-index: 99;
    display: none;
}

.new_ico {
    left: 295px;
    position: absolute;
    top: -25px;
}

.new_right {
    position: absolute;
    right: 0;
    top: 0;
}

.titile {
    color: #DCDCDC;
    font-family: Arial,Helvetica,sans-serif;
    left: 347px;
    position: absolute;
    top: 72px;
    z-index: 120;
}

.header_left h2 strong {
    font-size: 28px;
}
</style>
<div id="head">
  <div class="header_left">
  	<c:if test="${!empty siteBase.siteLogo}">
  		<img class="logo_img png" onerror="this.src='${baseUrlStatic}/images/logo_bg.png';" src="${siteBase.siteLogo}" height="54" />
  	</c:if>
  	<c:if test="${empty siteBase.siteLogo}">
  		<img class="logo_img png" onerror="this.src='${baseUrlStatic}/images/logo_bg.png';" src="${baseUrlStatic}/images/logo_bg.png" height="54" />
  	</c:if>
  <h2 id="siteName" style="margin-left: 60px;"></h2>
  
  </div>
  <img class="new_li png" src="${baseUrlStatic}/images/new_header_li.png" width="413" height="3" />
  <img class="new_ico02" src="${baseUrlStatic}/images/new_ico.png" width="25" height="25" /> 
  <img class="new_ico png" src="${baseUrlStatic}/images/new_xingxing.png" width="102" height="104" />
  <img class="new_right" src="${baseUrlStatic}/images/new_header_right.jpg" width="183" height="100" />
  <h3 class="titile">${siteBase.enName}</h3>
</div>
<script type="text/javascript">
var str = "${siteBase.siteName}";
var word = "${LANG['bizconf.jsp.header.res2']}";
str = str.replace(new RegExp("(" + word + ")","ig"), "<strong>" + word + "</strong>");
$("#siteName").html(str);
</script>
<!-- 
<div id="head">
  <div class="header_left"> 
  	<c:if test="${!empty siteBase.siteLogo}">
  		<img style=" float:left" class="yh_logo png" onerror="this.src='${baseUrlStatic}/images/logo.png';" src="${siteBase.siteLogo}" height="48" />
  	</c:if>
  	<c:if test="${empty siteBase.siteLogo}">
  		<img style=" float:left" class="yh_logo png" onerror="this.src='${baseUrlStatic}/images/logo.png';" src="${baseUrlStatic}/images/logo.png" height="48" />
  	</c:if>
  	<h3 style=" float:left; padding-top:30px; padding-left:20px; color:#013A7E; font-size:24px; font-family:'${LANG['bizconf.jsp.header.res1']}';">${siteBase.siteName}</h3>

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
 -->
