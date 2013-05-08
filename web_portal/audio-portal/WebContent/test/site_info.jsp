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
		$('#example').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"sAjaxSource": '/static/data/test.json'
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
<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
	<thead>
		<tr>
			<th width="20%">Rendering engine</th>
			<th width="25%">Browser</th>
			<th width="25%">Platform(s)</th>
			<th width="15%">Engine version</th>
			<th width="15%">CSS grade</th>
		</tr>
	</thead>
	<tbody>
		
	</tbody>

</table>
</body>
</html>