<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="bean.IpBean" %>
<%@ page import="bean.OrganizationBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>广西公安视频专网</title>
  <style type="text/css">
    body { font-family:Verdana; font-size:14px; margin:0;}
    h1 {font-size:24px;color: black;font-weight:bold;}
    h2 {font-size:12px;color: black;font-weight:bold;}
    #container {margin:0 auto;border: solid;width:100%;}
    #header { height:80px;line-height: 80px; background:#9c6; margin-bottom:3px;text-align:center;}
    #menu1 {font-weight: bold;font-size:20px;height:30px; background:#693; margin-bottom:3px;}
    #menu2 {font-weight: bold;font-size:20px;height:30px; background:#693; margin-bottom:3px;}
    #mainContent { height:1200px; margin-bottom:3px;position:relative;vertical-align:top;}
    #sidebar {font-size:20px; float:left; width:200px; height:1200px;border: solid;vertical-align:top; background:#cf9;}
    #sidebar2 {float:right; width:450px; height:1200px;border: solid;vertical-align:top;background:#cf9;}
    #content {margin:0 455px 0 215px !important; margin:0 452px 0 202px;border: solid;vertical-align:top; height:1200px; background:#ffa;}
    #footer { height:60px;padding: 60px; background:#9c6;text-align:center;vertical-align:bottom;}
  </style>
 </head>
<body>
    <%
         String name=(String)session.getAttribute("name");
         String visitnum=(String)request.getAttribute("visitnum");
         if (name==null) {name="您";}
      %>

<body>
<div id="container">
  <div id="header">
      <h1>广西公安视频专网管理系统1.4</h1>
  </div>
    <div id="menu1" bgcolor="#006400">
        <table border="1" align="right">
            <tr>
                <th>欢迎<%=name %>，您是第<%=visitnum%>个用户！</th>
                <th>各地登录账号请与区厅科技信息化处邓明联系，电话：0771-2893683。</th>
            </tr>
            </table>
        </div>
  <div id="menu2">
      <table border="1">
          <tr>
              <th><a href="/bingwang/main">首页</a></th>
              <th><a href="/bingwang/manage/ipseg" target="_blank">IP段管理</a></th>
              <th><a href="/bingwang/manage/ip" target="_blank">设备管理</a></th>
              <th><a href="/bingwang/manage/ip?device=13" target="_blank">重点部位</a></th>
              <th><a href="/bingwang/manage/port" target="_blank">设备端口</a></th>
              <th><a href="/bingwang/manage/user" target="_blank">用户管理</a></th>
              <th><a href="http://172.1.2.12:8080/yy-yunwei" target="_blank">专网考核</a></th>
              <th>
                  <c:choose>
                      <c:when test="${name==null}">
                          <a href="/bingwang/login.jsp">登录</a>
                      </c:when>
                      <c:otherwise>
                          <a href="/bingwang/logout.jsp">注销</a>
                      </c:otherwise>
                  </c:choose>
              </th>
          </tr>
      </table>
  </div>
  <div id="mainContent">
    <div id="sidebar">
      <table border="1">
                    <tr>                      
                        <th><a href="/bingwang/main">单位(广西)</a></th>                                
                    </tr>
        <c:forEach items="${orgList}" var="org">
          <tr>
            <td><a href="/bingwang/dmain?area=${org.area}"> <c:out value="${org.area}"></c:out></a></td>
          </tr>
        </c:forEach>
      </table>
    </div>
    <div id="sidebar2">
        <iframe src= "/bingwang/offline"   width= "100% " height= "100% " scroll= "auto "
                frameborder= "1 " name= "content " style="position:absolute;">
        </iframe>
    </div>
      <div id="content">
              <iframe src= "/bingwang/tongji" width= "100% " height= "100% " scroll= "auto "
                      frameborder= "0" name= "content " style="position:absolute;">
              </iframe>
    </div>
  </div>
  <div id="footer">本软件为农忠海所开发，如有建议与意见请反馈，谢谢！
    <br>建议使用<a href="/bingwang/360se7.zip">最新版360浏览器（点击下载）</a>的极速模式或IE9.0以上非兼容模式
    <br><a href="/bingwang/putty_cn.zip">putty_cn（点击下载）</a>
  </div>
</div>
</body>
</html>
