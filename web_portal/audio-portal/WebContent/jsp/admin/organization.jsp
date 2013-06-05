<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${LANG['bizconf.jsp.admin.organization.res1']}</title>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/reset.css?ver=${version}"/>
<link rel="stylesheet" type="text/css" href="${baseUrlStatic}/css/user/index.css?ver=${version}"/>
<link rel="stylesheet" href="${baseUrlStatic}/css/user/zTreeStyle/zTreeStyle.css?ver=${version}" type="text/css">
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/jquery-1.8.3.js?ver=${version}"></SCRIPT>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.ztree.core-3.5.min.js?ver=${version}"></script>
<script type="text/javascript" src="${baseUrlStatic}/js/min/jquery.ztree.exedit-3.5.min.js?ver=${version}"></script>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/json2.js?ver=${version}"></SCRIPT>
<SCRIPT type="text/javascript" src="${baseUrlStatic}/js/util.js?ver=${version}"></SCRIPT>

<script type="text/javascript">
var zNodes =[];
</script>
<c:forEach var="org" items = "${pageModel.datas}">
<script type="text/javascript">
	var test = "${pageModel.datas}";
	var id = ${org.id};
	var pId = ${org.parentId};
	var name = "${org.orgName}";
	var node = {id: id, pId: pId, name:name, isParent:true};
	if(pId==0){
		node.open = true;
	}
	zNodes.push(node);
</script>	
</c:forEach>
<SCRIPT type="text/javascript">
	<!--
	var setting = {
		view: {
            dblClickExpand: dblClickExpand
        },
        edit: {
			enable: true,
			showRemoveBtn: false,
			showRenameBtn: false
		},
	    data: {
	    	keep: {
				parent:true,
				leaf:true
			},
	        simpleData: {
	            enable: true
	        }
	    },
	    callback: {
	        onClick: zTreeOnClick
	    }
	};
	
	function dblClickExpand(treeId, treeNode) {
        return treeNode.level > 0;
    }
	
	function zTreeOnClick(event, treeId, treeNode) {
		var url = "/admin/org/orgList/"+treeNode.id;
		showURL(url);
	}

	function showURL(url) {
		url+="?t="+ Math.random();
		$('#orgListForm').attr("src", url);
	}
	
	function createOrg(id, level) {
		if(level==4){
			parent.errorDialog("${LANG['bizconf.jsp.admin.organization.res2']}4${LANG['bizconf.jsp.admin.organization.res3']}");
		} else {
			parent.createOrg(id);	
		}
	}
	
	function addOrg(result) {
		var org = result.siteOrg[0];
		var zTree = $.fn.zTree.getZTreeObj("treeOrg"),
		nodes = zTree.getSelectedNodes(),
		treeNode = nodes[0];
		var node = {id: org.id, pId:org.parentId, isParent:true, name:org.orgName};
		zTree.addNodes(treeNode, node);
		var url = "/admin/org/orgList/"+org.parentId;
		showURL(url);
	}
	
	function updateOrg(result) {
		var org = result.siteOrg[0];
		var zTree = $.fn.zTree.getZTreeObj("treeOrg");
		var node = zTree.getNodeByParam("id", org.id, null);
		node.name = org.orgName;
		zTree.updateNode(node, false);
		var url = $("#orgListForm").attr("src");
		showURL(url);
	}
	
	function removeOrg(id) {
		var zTree = $.fn.zTree.getZTreeObj("treeOrg");
		var node = zTree.getNodeByParam("id", id, null);
		var parentNode = node.getParentNode();
		zTree.selectNode(parentNode, false);
		zTree.removeNode(node, false);	
		var url = "/admin/org/orgList/"+parentNode.id;
		showURL(url);
	}
	
	function assignUser() {
		var url = $("#orgListForm").attr("src");
		showURL(url);
	}
	
	function refreshIframe() {
		var url = $("#orgListForm").attr("src");
		showURL(url);
	}
	
	function refreshIHeight() {
		var height = $("#orgListForm").contents().find("body").height() + 50;
		$("#orgListForm").height(height);
		var pHeight = parent.$("#mainFrame").height();
	}
	$(document).ready(function(){
	    $.fn.zTree.init($("#treeOrg"), setting, zNodes);
	});
	//-->
	</SCRIPT>
	<style type="text/css">
		.ztree {margin-left: 20px;}
		.ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
		.ztree li ul.level0 {padding:0; background:none;}
		.ztree li span.button.ico_open, .ztree li span.button.ico_close{
			display: none;
		}
		.add_org {
		    background: url("/static/images/button01.jpg") no-repeat scroll 0 0 transparent;
		    cursor: pointer;
		    height: 24px;
		    line-height: 24px;
		    text-align: center;
		    width: 72px;
		    margin-left: 10px;
		    margin-top: 20px;
		    margin-bottom: 15px;
    	}		
	</style>	
</head>
<body>
<div style="width: 100%;height:50px;">
	<input class="add_org" id="addOrg" type="button" value="${LANG['bizconf.jsp.admin.index.res9']}" onclick="createOrg(0)"/>
</div>
<!-- <div style="min-height: 550px;overflow-y: auto;float: left;width:220px;"> -->
<!-- 	<div style="margin-left: 25px;">${LANG['bizconf.jsp.admin.organization.res4']}:</div> -->
<!-- 	<ul id="treeOrg" class="ztree"></ul> -->
<!-- </div> -->
<div class="main_right">
	<iframe width="100%" height="100%" frameborder="0" id="orgListForm" name="orgListForm" scrolling="auto" src="/admin/org/orgList"></iframe>
</div>
</body>
</html>
