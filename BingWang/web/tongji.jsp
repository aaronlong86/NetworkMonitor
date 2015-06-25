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
<b>广西公安视频专网并网进度情况统计</b>
        <table border="1">
              <tr>                  
                  <th>排名</th>
                  <th>单位</th>
                  <th style="width:50px">并网任务数</th>                
                  <th style="width:50px"><a href="/bingwang/ip?areacode45=450000000000" target="_blank">已并网数</a></th>                
                  <th>完成率</th>                
                  <th>进度</th>                
              </tr>
  <c:forEach items="${tjList}" var="tj">
    <tr>
      <td><c:out value="${tj.ranking}"></c:out></td>
      <td><c:out value="${tj.area}"></c:out></td>
      <td><c:out value="${tj.tasknum}"></c:out></td>
      <td><a href="/bingwang/ip?areacode45=${tj.areacode}" target="_blank"><c:out value="${tj.ipnum}"></a></c:out></td>
      <td><fmt:formatNumber type="percent" value="${tj.taskrate}" /></td>
      <td><progress value="${tj.taskrate}" max="1"> </progress></td>
    </tr>
  </c:forEach>
          </table>
