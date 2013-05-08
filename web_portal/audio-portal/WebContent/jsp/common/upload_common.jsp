<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
function fileUpload(value) {
	//判断上传文件类型...
	var typeAllow = {jpg:true,bmp:true,png:true,gif:true,JPG:true,BMP:true,PNG:true,GIF:true};
	suffix = value.substring(value.lastIndexOf(".")+1);
	if(typeAllow[suffix]){
		parent.document.getElementById("previewLoadImg").style.display="block";
		document.getElementById("uploadForm").submit();
	}else{
		parent.parent.errorDialog("请上传正确的文件类型！");
	}
}
</script>
<title></title>
</head>
<body style="margin:0px;">
<form id="uploadForm" action="/common/upload/common" method="post"
						enctype="multipart/form-data">
<input type="file" name="commonFile" onchange="fileUpload(this.value);"/><%--<input type="submit" value="上传"> --%>
</form>
</body>
</html>