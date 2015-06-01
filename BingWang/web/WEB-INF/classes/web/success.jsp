<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/22 0022
  Time: 22:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>欢迎</title>
</head>
<body>
<%
  String name = (String)session.getAttribute("name") ;
%>
<div align="center">
  <%=name %>
  欢迎您，登陆成功！<br />
  <% response.setHeader("Refresh","2;URL=/bingwang/main");%>
  <a href="/bingwang/main">点击返回或两秒后自动返回</a>
</div>
</body>
</html>
