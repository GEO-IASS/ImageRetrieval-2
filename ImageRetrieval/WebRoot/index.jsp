<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>图片检索</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/indexStyle.css"/> 
</head>

<body>

<div class="container">
  <div class="imc" align="center">
  <img src="images/123.jpg" height="200" width="534"/>
   <!-- end .imc --></div>
   
   
  <div class="content">
  <div class="wenben" align="center">
    <form id="form" action="servlet/SearchServlet" method="post">	
   	<span class="search">			
    <input type="text" name="searchText" id="searchText"  style="height:30px; width:600px" class="input" value="" />			
    </span>		

    </form>
    </div>
   <div class="sousuo" align="center">
	<a href="servlet/SearchServlet" class="g_a">
     <span class="g_b">
         <span class="g_c">&nbsp;</span>
         <span class="g_d">搜索一下</span>
     </span>
	</a>
   <!-- end .sousuo --></div>
    <!-- end .content --></div>
    
  <div class="foot" align="center">
  &copy;版权所有
    <!-- end .foot --></div>
  <!-- end .container --></div>
</body>
</html>
