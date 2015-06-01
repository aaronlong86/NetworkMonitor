<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/6 0006
  Time: 18:18
  To change this template use File | Settings | File Templates.
--%>
<style type="text/css">
    h1{text-align:center;font-weight: 900;font-size: 100%;background-color:lightgreen;color: black }
    h2{text-align:center;font-weight: 900;font-size: 100%;background-color:greenyellow;color: black }
    h3{text-align:center;font-weight: 900;font-size: 100%;background-color:darkseagreen;color: black }
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
                    <th>删除IP段</th>
                    <th>层级</th>
                    <th>首IP</th>
                    <th>至</th>
                    <th>尾IP</th>
                    <th>增加IP段</th>                    
                </tr>
    <c:forEach items="${ipsegList}" var="ips">
        <tr>
            <td><c:out value="${ips.id}"></c:out></td>
            <td><c:out value="${ips.area}"></c:out></td>
            <td><c:out value="${ips.ipstart}"></c:out>--<c:out value="${ips.ipend}"></c:out></td>
            <form name="delete" action="/bingwang/manage/ipseg" method="get">
                <input type="hidden" name="areacode" value="${ips.areacode}">
                <input type="hidden" name="level" value="${ips.level}">
                <input type="hidden" name="area" value="${ips.area}">
                <input type="hidden" name="ipstart" value="${ips.ipstart}">
                <input type="hidden" name="ipend" value="${ips.ipend}">
                <input type="hidden" name="flag" value="delete">
                <td><input type="submit" value="删除" /></td>
            </form>
            <td>
                <c:choose>
                    <c:when test="${ips.level==0}">
                        <h1>厅</h1>
                    </c:when>
                    <c:when test="${ips.level==1}">
                        <h2>全市(处)</h2>
                    </c:when>
                    <c:otherwise>
                        <h3>县(市属)</h3>
                    </c:otherwise>
                </c:choose>
            </td>
            <form name="input" action="/bingwang/manage/ipseg" method="get">
            <td><input type="text" name="ipstart" /></td>
            <td>--</td>
            <td><input type="text" name="ipend" /></td>
                <input type="hidden" name="areacode" value="${ips.areacode}">
                <input type="hidden" name="level" value="${ips.level}">
                <input type="hidden" name="area" value="${ips.area}">
                <input type="hidden" name="flag" value="add">
            <td><input type="submit" value="提交增加" /></td>
            </form>
        </tr>
    </c:forEach>
            </table>
               </body>
               </html>
