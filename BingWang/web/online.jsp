<%--
  Created by IntelliJ IDEA.
  User: Administrator<p> 各市设备在线情况  </p>
  Date: 2015/5/8 0008
  Time: 11:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="bean.OnLineBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style type="text/css">
  h1{text-align:center;font-weight: 900;font-size: 100%;background-color:#6BE61A;color: black }
  h2{text-align:center;font-weight: 900;font-size: 100%;background-color:red;color: black }
  h3{text-align:center;font-weight: 900;font-size: 100%;background-color:yellow;color: black }
  h4{text-align:center;font-weight: 900;font-size: 100%;background-color:#ADADAD;color: black }
</style>
<html>

<body>
<b>广西公安视频专网设备在线情况统计</b>
<table border="1">
              <tr>                  
                  <th>单位</th>
                  <th><a href="/bingwang/ip?area=广西公安厅" target="_blank">设备总数(区厅)</a></th>
                  <th>在线数</th>
  <th>在线率</th>
  <th>状态</th>                
              </tr>
  <c:forEach items="${onLineBeanList}" var="oll">
    <tr>
      <td><c:out value="${oll.area}"></c:out></td>
      <td>
        <c:choose>
        <c:when test="${oll.totalnum==0}">
          <h4>--</h4>
        </c:when>
        <c:otherwise>
        <a href="/bingwang/ip?area=${oll.area}" target="_blank">
          <c:out value="${oll.totalnum}"></c:out></a>
        </c:otherwise>
        </c:choose>

      </td>
      <td>
        <c:choose>
          <c:when test="${oll.totalnum==0}">
            <h4>--</h4>
          </c:when>
          <c:otherwise>
            <c:out value="${oll.onlinenum}"></c:out>
          </c:otherwise>
        </c:choose>
        </td>
      <td>
        <c:choose>
          <c:when test="${oll.totalnum==0}">
            <h4>--</h4>
          </c:when>
          <c:otherwise>
            <a href="/bingwang/ip?area=${oll.area}&onlinerate=${oll.onlinerate}" target="_blank">
              <fmt:formatNumber type="percent" value="${oll.onlinerate}" />
            </a>
          </c:otherwise>
        </c:choose>
        </td>
      <td>
        <c:choose>
          <c:when test="${oll.totalnum==0}">
            <h4>--</h4>
          </c:when>
          <c:when test="${oll.onlinerate>0.8}">
            <h1>&#8730;</h1>
          </c:when>
          <c:when test="${oll.onlinerate<0.3}">
            <h2>&#935;</h2>
          </c:when>
          <c:otherwise>
            <h3>&#920;</h3>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:forEach>
</table>
</body>
</html>
