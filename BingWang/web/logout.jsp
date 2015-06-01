<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/22 0022
  Time: 23:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注销</title>
</head>
<body>
<%
  response.setHeader("refresh","2;url=/bingwang/main");//定时跳转
  session.invalidate();//注销
%>
<div align="center">
<h3>你好,你已经退出本系统,两秒后跳会首页</h3>
<h3>如没有跳转,请按<a href="/bingwang/main">这里</a></h3>
</div>
</body>
</html>
