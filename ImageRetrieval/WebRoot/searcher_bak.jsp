<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import= "java.util.*, bean.*" %>
<html>
<head>
<title>searcher page</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/style.css"/> 
	<script type="text/javascript">
	function check(){
		
	}
	</script>
</head>

<body>
<%
	//String searchText=(String)request.getAttribute("searchText");
	List images=(List)request.getAttribute("images");
	int len=0;
	if(images!=null){
		len=images.size();
	}
	long timeCost=(Long)request.getAttribute("timeCost");
%>
	<div id="header" align="center">
	<form id="form" action="/ImageRetrieval/servlet/SearchServlet" method="post" >
		<span class="logo"><img src="/ImageRetrieval/images/logo.GIF" />
			<span class="search">
			<input type="text" name="searchText" id="searchText"  class="input" value="${searchText}" />
			</span>
		</span>
		<span class="search">
			<input type="submit" name="button" class="button" value="search"/>
			<a href="/ImageRetrieval/upload.jsp" >基于图像内容检索</a>
		</span>
	</form>
	</div>
	<div id="cent">
		<p id="cent_head">
			<span id="head">
			<table border="0" style="width:100%">
				<tr>
					<td>相关搜索:&nbsp;&nbsp;${searchText}</td>
					<td><div align=right>共查询到&nbsp;<%=len %>&nbsp;张图片,查询共花费&nbsp;${timeCost}&nbsp;毫秒</div>	</td>
				</tr>
			</table>	
						
			</span>
		</p>
		<div align="center">
		<span id="picture">
		<table>
		<%
		//System.out.println(images);
		if(images!=null){
			for(int i=0;i<images.size() && i<=19;i=i+5){
				
		%>
			<tr>
				<%if(i<images.size()){
					ImageBean image=(ImageBean)images.get(i);
				%>
				<td>
					<a href="../servlet/CrossSearchServlet?imagename=<%=image.getImagename() %>&&searchText=${searchText}"><img class="img" src="../servlet/ImageServlet?fileName=<%=image.getImagename() %>" /></a>
				</td>
				<%}
				if(i+1<images.size()){
					ImageBean image=(ImageBean)images.get(i+1);
				%>
				<td>
					<a href="../servlet/CrossSearchServlet?imagename=<%=image.getImagename() %>&&searchText=${searchText}"><img class="img" src="../servlet/ImageServlet?fileName=<%=image.getImagename() %>" /></a>
				</td>
				<%}
				if(i+2<images.size()){
					ImageBean image=(ImageBean)images.get(i+2);
				%>
				<td>
					<a href="../servlet/CrossSearchServlet?imagename=<%=image.getImagename() %>&&searchText=${searchText}"><img class="img" src="../servlet/ImageServlet?fileName=<%=image.getImagename() %>" /></a>
				</td>
				<%}
				if(i+3<images.size()){
					ImageBean image=(ImageBean)images.get(i+3);
				%>
				<td>
					<a href="../servlet/CrossSearchServlet?imagename=<%=image.getImagename() %>&&searchText=${searchText}"><img class="img" src="../servlet/ImageServlet?fileName=<%=image.getImagename() %>" /></a>
				</td>
				<%}
				if(i+4<images.size()){
					ImageBean image=(ImageBean)images.get(i+4);
				%>
				<td>
					<a href="../servlet/CrossSearchServlet?imagename=<%=image.getImagename() %>&&searchText=${searchText}"><img class="img" src="../servlet/ImageServlet?fileName=<%=image.getImagename() %>" /></a>
				</td>
				<%} %>
			</tr>
		
		<%} }%>
	
			
		</table>
		</span>
		</div>
	</div>
</body>
</html>
