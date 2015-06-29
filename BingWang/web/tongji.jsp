<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/7 0007<p>各市并网进度情况</p>
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="bean.TongJiBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style type="text/css">
  h1{text-align:center;font-weight: 900;font-size: 100%;background-color:#6BE61A;color: black }
  h2{text-align:center;font-weight: 900;font-size: 100%;background-color:red;color: black }
  h3{text-align:center;font-weight: 900;font-size: 100%;background-color:yellow;color: black }
  h4{text-align:center;font-weight: 900;font-size: 100%;background-color:#ADADAD;color: black }
</style>
<b>广西公安视频专网并网进度情况统计</b>
        <table border="1">
              <tr>                  
                  <th>排名</th>
                  <th style="width:90px">单位</th>
                  <th style="width:50px">并网任务数</th>                
                  <th style="width:55px"><a href="/bingwang/ip?areacode45=450000000000" target="_blank">已并网数(区厅)</a></th>                
                  <th>完成率</th>                
                  <th  style="width:100px">进度</th>                
              </tr>
  <c:forEach items="${tjList}" var="tj">
    <tr>
      <td><c:out value="${tj.ranking}"></c:out></td>
      <td  style="width:90px"><c:out value="${tj.area}"></c:out></td>
      <td><c:out value="${tj.tasknum}"></c:out></td>
      <td><a href="/bingwang/ip?areacode45=${tj.areacode}" target="_blank"><c:out value="${tj.ipnum}"></a></c:out></td>
      <td><fmt:formatNumber type="percent" value="${tj.taskrate}" /></td>
      <td style="width:100px"><progress value="${tj.taskrate}" max="1"> </progress></td>
    </tr>
  </c:forEach>
          </table>
<b>广西公安视频专网设备在线情况统计</b>
<table border="1">
              <tr>                  
                  <th  style="width:120px">单位</th>
                  <th style="width:55px"><a href="/bingwang/ip?areacode=450000000000" target="_blank">设备总数(区厅)</a></th>
                  <th>在线数</th>
  <th>在线率</th>
  <th>状态</th>                
              </tr>
  <c:forEach items="${onLineBeanList}" var="oll">
    <tr>
      <td  style="width:120px"><c:out value="${oll.area}"></c:out></td>
      <td>
        <c:choose>
          <c:when test="${oll.totalnum==0}">
            <h4>--</h4>
          </c:when>
          <c:otherwise>
            <a href="/bingwang/ip?areacode=${oll.areacode}" target="_blank">
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
            <a href="/bingwang/ip?areacode=${oll.areacode}&onlinerate=${oll.onlinerate}" target="_blank">
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
