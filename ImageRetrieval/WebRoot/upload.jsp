<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<title>upload page</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/style.css"/>
<script type="text/javascript">
	function check(){
		  var strFileName=form1.image.value;
		  if (strFileName=="")
		  {
		    alert("请选择要上传的文件");
		    return false;
		  }
		  var strtype=strFileName.substring(strFileName.length-3,strFileName.length);
		  strtype=strtype.toLowerCase();
		  if (strtype=="jpg"||strtype=="gif" || strtype=="bmp"||strtype=="png")
		    return true;
		  else{
		    alert("这种文件类型不允许上传！\r\n只允许上传这几种文件：jpg、gif、bmp、png\r\n请选择别的文件并重新上传。");
			form1.image.focus();
			return false;
		  }
		return true;
	}
</script> 
</head>
<body>
	<form id="form1" action="/ImageRetrieval/servlet/UploadServlet" method="post" enctype='multipart/form-data' >
		<span class="logo"><img src="/ImageRetrieval/images/logo.GIF" />
			<span class="search">
				请选择要上传的文件<input type="file" name="image" id="image"  class="input" 请选择要上传的文件/>
			</span>
		</span>
		<span class="search">
				<input type='submit' value='提交' onClick="return check()">
		</span>
	</form>
</body>
</html>