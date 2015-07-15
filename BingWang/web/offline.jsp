<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/6/19 0019
  Time: 7:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style type="text/css">
h2{text-align:center;font-weight: 900;font-size: 100%;background-color:red;color: black }
</style>
<html>
<body>
<b>网络设备离线情况列表</b>
 <table border="1">
  <tr>
  <th>单位</th>
  <th style="width:50px">网络设备</th>
  <th style="width:50px">品牌</th>
  <th>状态</th>                           
  <th>最近上线时间</th>                           
  </tr>
  <c:forEach items="${network}" var="nw">
    <tr>
      <td width="60"><c:out value="${nw.area}"></c:out></td>
      <td><c:out value="${nw.ip}"></c:out></td>
      <td><c:out value="${nw.brand}"></c:out></td>
      <td><h2>&#935;</h2></td>
      <td><c:out value="${nw.lastdiscovery}"></c:out></td>
    </tr>
  </c:forEach>
          </table>

<b>视频联网平台离线情况列表</b>
 <table border="1">
  <tr>
    <th>单位</th>
    <th style="width:50px">视频联网平台</th>
    <th style="width:50px">品牌</th>
    <th>状态</th>                           
    <th>最近上线时间</th>                           
  </tr>
  <c:forEach items="${platform}" var="pf">
    <tr>
      <td width="60"><c:out value="${pf.area}"></c:out></td>
      <td><c:out value="${pf.ip}"></c:out></td>
      <td><c:out value="${pf.brand}"></c:out></td>
      <td><h2>&#935;</h2></td>
      <td><c:out value="${pf.lastdiscovery}"></c:out></td>
    </tr>
  </c:forEach>
          </table>
</body>
</html>
