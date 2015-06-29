<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/6 0006
  Time: 18:18
  To change this template use File | Settings | File Templates.
--%>
<style type="text/css">
    h1{text-align:center;font-weight: 900;font-size: 100%;background-color:#6BE61A;color: black }
    h2{text-align:center;font-weight: 900;font-size: 100%;background-color:red;color: black }
    h3{text-align:center;font-weight: 900;font-size: 100%;background-color:yellow;color: black }
</style>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>


<body>
<h1>   IP段列表   </h1>
        <table border="1">
                <tr>
                    <th>序号 </th>
                    <th>单位</th>
                    <th>IP段</th>                   
                </tr>
    <c:forEach items="${ipsegList}" var="ips">
        <tr>
            <td><c:out value="${ips.id}"></c:out></td>
            <td><c:out value="${ips.area}"></c:out></td>
            <td><c:out value="${ips.ipstart}"></c:out>--<c:out value="${ips.ipend}"></c:out></td>
        </tr>
    </c:forEach>
            </table>
 <h1>
             IP地址列表
          </h1>

<table border="1">
  <tr>
    <th>序号 </th>
    <th>单位</th>
    <th>IP</th>
      <th>状态</th>
      <th>操作</th>
    <th>设备类型</th>
    <th>负责人</th>
    <th>品牌</th>
      <th>位置</th>
      <th>用途</th>
      <th>操作</th>                
  </tr>
  <c:forEach items="${ipBeanList}" var="ipb">
    <tr>
        <form name="input" action="/bingwang/manage/ip" method="get">
        <td><c:out value="${ipb.id}"></c:out></td>
        <td><c:out value="${ipb.area}"></c:out></td>
        <td><c:out value="${ipb.ip}"></c:out></td>
        <td>
            <c:choose>
                <c:when test="${ipb.status=='online'}">
                    <h1>&#8730;</h1>
                </c:when>
                <c:when test="${ipb.status=='offline'}">
                    <h2>&#935;</h2>
                </c:when>
                <c:otherwise>
                    <h3>&#920;</h3>
                </c:otherwise>
            </c:choose>
        </td>
            <input type="hidden" name="ip" value="${ipb.ip}">
            <input type="hidden" name="flag" value="delete">
            <td><input type="submit" value="删除" /></td>
            </form>
        <form name="input" action="/bingwang/manage/ip" method="get">
        <td>
            <select name="devicetype">
                <option value="${ipb.devicetype}">${ipb.devicetype}</option>
                <c:forEach items="${devicetypeList}" var="devlist">
                  <option value="${devlist.devicetype}">${devlist.devicetype}</option>
                </c:forEach>
            </select>
        </td>
        <td><input type="text" name="manager" size="12" value="${ipb.manager}"></td>
        <td><input type="text" name="brand" size="12" value="${ipb.brand}"></td>
        <td><input type="text" name="location" value="${ipb.location}"></td>
        <td><input type="text" name="application" size="45" value="${ipb.application}"></td>
        <input type="hidden" name="ip" value="${ipb.ip}">
            <input type="hidden" name="flag" value="modify">
        <td><input type="submit" value="修改" /></td>
            </form>
    </tr>
                 </c:forEach>

                         </table>
               </body>
               </html>
