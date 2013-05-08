<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>站点管理</title>
<style type="text/css" title="currentStyle">
@import "/static/js/jquery-ui-1.9.2.custom/css/smoothness/jquery-ui-1.9.2.custom.css";
@import "/static/js/DataTables-1.9.4/media/css/jquery.dataTables_themeroller.css";
</style>
<script type="text/javascript" language="javascript" src="/static/js/jquery-ui-1.9.2.custom/js/jquery-1.8.3.js"></script>
<script type="text/javascript" language="javascript" src="/static/js/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.js"></script>
<script type="text/javascript" language="javascript" src="/static/js/DataTables-1.9.4/media/js/jquery.dataTables.js"></script>
<script type="text/javascript" language="javascript" src="/static/js/jquery.stepy-1.1.0/js/jquery.stepy.js"></script>
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$('#site-info').dataTable({
			"bJQueryUI": true,
			"bPaginate" : true,
			"iDisplayLength": 5,
			"bProcessing": true,
	        "bServerSide": true,
	        "sAjaxDataProp": "result",
			"sPaginationType": "full_numbers",
			"sAjaxSource": '/system/site/siteList',
			"aoColumns": [
                { "mData": "siteName" },
                { "mData": "siteSign" },
                { "mData": "siteFlag" },
                { "mData": "license" },
                { "mData": "expireDate" },
                { "mData": "clientName" },
                { "mData": "lockFlag", "bSortable": false},
                { "mData": "id", "bSortable": false}
            ],
            "aoColumnDefs": [
				{
				    "mRender": function ( data, type, row ) {
				        return  row.expireDate.date;
				    },
				    "aTargets": [ 4 ]
				},
                {
                    "mRender": function ( data, type, row ) {
                        return  '<a href=\"/' + row.lockFlag + '\">正常</a>';
                    },
                    "aTargets": [ 6 ]
                },
                {
                    "mRender": function ( data, type, row ) {
                        return  '<a href=\"Details/' + row.id + '\">修改</a>';
                    },
                    "aTargets": [ 7 ]
                }
            ],
            "oLanguage": {  
                "sProcessing": "正在加载数据...",
                "sZeroRecords": "没有符合项件的数据...",  
                "sInfo": "共有 _TOTAL_ 项记录",  
                "sInfoEmpty": "显示 0 至 0 共 0 项",  
                "sInfoFiltered": "(_MAX_)",
                "oPaginate": {
                    "sFirst": "首页",
                    "sLast": "尾页",
                    "sPrevious": "上一页",
                    "sNext": "下一页"
                }
            },
		});

		$("#create-site").click(function() {
			window.parent.editSiteDialog();
		});

	} );
</script>
</head>
<body>
<div>
	<div style="float: left;"><input type="text"/>高级搜索</div>
	<div style="float: right;">
		<div><input id="create-site" type="button" value="创建站点"/></div>
	</div>
</div>
<table cellpadding="0" cellspacing="0" border="0" class="display" id="site-info">
	<thead>
		<tr>
			<th width="12%">企业名称</th>
			<th width="12%">企业标识</th>
			<th width="12%">站点类型</th>
			<th width="12%">最大参会方数</th>
			<th width="12%">到期时间</th>
			<th width="12%">用户名</th>
			<th width="10%">状态</th>
			<th width="10%">操作</th>
		</tr>
	</thead>
	<tbody>
		
	</tbody>

</table>
</body>
</html>