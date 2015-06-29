<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/6/29 0029
  Time: 11:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title></title>
</head>
<body>
<table border="1">
  <tr>
    <th>用户名</th>
    <th>密码</th>
    <th>姓名</th>
    <th>单位</th>
    <th>操作</th>                
  </tr>
  <c:forEach items="${userbeans}" var="users">
    <tr>
      <form name="input" action="/bingwang/manage/user" method="get">
        <td>${users.username}</td>
        <td><input type="password" name="password" value="${users.password}"></td>
        <td><input type="text" name="name" value="${users.name}"></td>
        <td>
          <select name="areacode">
            <option value="${users.areacode}">${users.area}</option>
            <c:forEach items="${organizationbeans}" var="orgs">
              <option value="${orgs.areacode}">${orgs.area}</option>
            </c:forEach>
          </select>
        </td>
        <input type="hidden" name="flag" value="modify">
        <input type="hidden" name="username" value="${users.username}">
        <td><input type="submit" value="提交修改" /></td>
      </form>
    </tr>
  </c:forEach>
  <tr>
    <form name="input" action="/bingwang/manage/user" method="get">
      <td><input type="text" name="username"></td>
      <td><input type="password" name="password"></td>
      <td><input type="text" name="name"></td>
      <td>
        <select name="areacode">
          <c:forEach items="${organizationbeans}" var="orgs">
            <option value="${orgs.areacode}">${orgs.area}</option>
          </c:forEach>
        </select>
      </td>
      <input type="hidden" name="flag" value="add">
      <td><input type="submit" value="提交增加" /></td>
    </form>
  </tr>
          </table>
</body>
</html>
