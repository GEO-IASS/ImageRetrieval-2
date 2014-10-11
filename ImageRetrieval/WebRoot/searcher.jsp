<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "java.util.*, bean.*" %>
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>检索结果</title>


<script src="http://www.webdm.cn/themes/script/jquery.js"></script>
<script language="javascript" src="/ImageRetrieval/js/index.js">
</script>

<link href="/ImageRetrieval/css/index.css" rel="stylesheet" type="text/css" />
</head>

<body marginheight="0">
<%
	//String searchText=(String)request.getAttribute("searchText");
	List images=(List)request.getAttribute("images");
	int len=0;
	if(images!=null){
		len=images.size();
	}
	long timeCost=(Long)request.getAttribute("timeCost");
%>
<div class="containtor">
  <div class="chazhao">
  <table width="920" height="49" border="0" align="left" cellpadding="0" cellspacing="0">
  <tbody> 
    <tr>
		<td width="110" height="49"><img src="/ImageRetrieval/images/logo.png" width="50px" height="49px"/></td>
    		<form id="form" action="/ImageRetrieval/servlet/SearchServlet" method="post">		
   		<td width="600"><input type="text" name="searchText" id="searchText"  style="height:24px; width:600px" class="input" value="" /></td> 	
   		<td width="60"><a href="#"><img src="/ImageRetrieval/images/search.png" width="60" height="30" align="baseline"/></a></td>
			</form>  	
   		<td width="150"><a href="/ImageRetrieval/upload.jsp" >基于图像内容检索</a></td>	
   		
	 </tr>
  </tbody>

 	</table>
   		
  </div>
    
  <div class="result" align="center">
    <table width="1325" border="0" cellspacing="0" cellpadding="0" height="30px">
      <tr>
        <td width="825"><div align="left">&nbsp;&nbsp;相关搜索：&nbsp;&nbsp;${searchText}</div></td>
        <td width="500"><div align="right">共查询到&nbsp;<%=len %>&nbsp;张图片,查询共花费&nbsp;${timeCost}&nbsp;毫秒&nbsp;&nbsp;</div></td>
      </tr>
    </table>
    <hr align="left" width="100%"/>
  </div>
    
  <div class="picture">
	<div id="zoom">
	<%
		for(int i=0;i<images.size() && i<200;i++){
			ImageBean image=(ImageBean)images.get(i);
	%>
 	  <div id="pic"><a href="../servlet/CrossSearchServlet?imagename=<%=image.getImagename() %>&&searchText=${searchText}"><img class="img" src="../servlet/ImageServlet?fileName=<%=image.getImagename() %>" /></a></div>
	   <%} %>
 	</div>
  </div>
  <table width="90%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td>
    <div class="more"><span class="current">1</span>
    <a href="#?page=2">2</a>
    <a href="#?page=3">3</a>
    <a href="#?page=4">4</a>
    <a href="#?page=5">5</a>
    <a href="#?page=6">6</a>
    <a href="#?page=7">7</a>
    ...
    <a href="#?page=199">199</a>
    <a href="#?page=200">200</a>
    <a href="#?page=2">Next &gt; </a>
  </div>
    </td>
  </tr>
  <tr>
    <td>
    <div class="foot" align="center">
  	<hr />
  	&copy;版权所有
  </div>
    </td>
  </tr>
</table>

</div>

</body>
</html>
