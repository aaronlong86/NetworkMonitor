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
                  <th>最近扫描发现时间</th>
                  <th>状态</th>                
              </tr>
  <c:forEach items="${ipBeanList}" var="ipb">
    <tr>
      <td><c:out value="${ipb.id}"></c:out></td>
      <td><c:out value="${ipb.area}"></c:out></td>
      <td><c:out value="${ipb.ip}"></c:out></td>
      <td><c:out value="${ipb.lastdiscovery}"></c:out></td>
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
                   </tr>
                 </c:forEach>

                         </table>
               </body>
               </html>
