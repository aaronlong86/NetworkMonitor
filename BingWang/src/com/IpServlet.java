package com;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/5/6 0006.
 * @WebServlet(name = "IpServlet")
 */
@WebServlet(name = "IpServlet")
public class IpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String area = (String)request.getParameter("area");//从在线页面跳入
        RecordVisit recordVisit = new RecordVisit();
        recordVisit.insertrecord(request);
        String onlinerate=(String) request.getParameter("onlinerate");
        String area45 = (String)request.getParameter("area45");//从并网统计页面跳入
        response.setContentType("text/html;charset=UTF-8");
        ArrayList<IpBean> ipBeanList=new ArrayList<IpBean>();
        ArrayList<IpSegmentBean> ipsegList=new ArrayList<IpSegmentBean>();
        if (area==null)
        {
            ipBeanList = getDiscoveryIp(area45, 45);
            ipsegList=getIpSegment(area45);
        }
        if (area45==null) {
            if (onlinerate == null) {
                ipBeanList = getDiscoveryIp(area);
                ipsegList=getIpSegment(area);
            } else {
                ipBeanList = getDiscoveryIp(area, onlinerate);
                ipsegList=getIpSegment(area);
            }
        }
        Collections.sort(ipBeanList, new SortIp());
        request.setAttribute("ipBean", ipBeanList);
        request.setAttribute("ipsegList", ipsegList);
        request.getRequestDispatcher("ip.jsp").forward(request, response);
    }
    //获取已发现的所有IP
    private ArrayList<IpBean> getDiscoveryIp(String area) {
        ArrayList<IpBean> ipBeanList=new ArrayList<IpBean>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from ("+
                "SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area=\'"+area+
                "\') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"+
                " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i=1;
            while (rs.next())
            {
                IpBean ipBean =new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("ip"));
                ipBean.setArea(rs.getString("area"));
                ipBean.setLastdiscovery(rs.getString("discoverylasttime"));
                Date datenow=new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre=disctime.parse(rs.getString("discoverylasttime"));
                long l=(datenow.getTime()-datepre.getTime())/60000;
                String status="offline";
                if (l<Init.scanIpinterval){status="online";}
                else{if (l<Init.scanIpinterval*2){status="warning";}}
                ipBean.setStatus(status);
                ipBeanList.add(ipBean);
                i=i+1;
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return ipBeanList;
        }
        return ipBeanList;
    }
    //获取已发现的所有某段IP
    private ArrayList<IpBean> getDiscoveryIp(String area,int ipseg) {
        ArrayList<IpBean> ipBeanList=new ArrayList<IpBean>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from ("+
                "SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area=\'"+area+
                "\') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"+
                " and (t1.ip like \'"+Integer.toString(ipseg)+"%\')"+
                " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i=1;
            while (rs.next())
            {
                IpBean ipBean =new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("ip"));
                ipBean.setArea(rs.getString("area"));
                ipBean.setLastdiscovery(rs.getString("discoverylasttime"));
                Date datenow=new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre=disctime.parse(rs.getString("discoverylasttime"));
                long l=(datenow.getTime()-datepre.getTime())/60000;
                String status="offline";
                if (l<=Init.scanIpinterval){status="online";}
                else{if (l<=Init.scanIpinterval*2){status="warning";}}
                ipBean.setStatus(status);
                ipBeanList.add(ipBean);
                i=i+1;
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return ipBeanList;
        }
        return ipBeanList;
    }
    //获取离线的IP
    private ArrayList<IpBean> getDiscoveryIp(String area,String onlinerate) {
        ArrayList<IpBean> ipBeanList=new ArrayList<IpBean>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from ("+
                "SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area=\'"+area+
                "\') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"+
                " and ((TIMESTAMPDIFF(MINUTE,t1.discoverylasttime,now()))>="+
                Integer.toString(Init.scanIpinterval)+")"+
                " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i=1;
            while (rs.next())
            {
                IpBean ipBean =new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("ip"));
                ipBean.setArea(rs.getString("area"));
                ipBean.setLastdiscovery(rs.getString("discoverylasttime"));
                Date datenow=new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre=disctime.parse(rs.getString("discoverylasttime"));
                long l=(datenow.getTime()-datepre.getTime())/60000;
                String status="offline";
                if (l<=Init.scanIpinterval){status="online";}
                else{if (l<=Init.scanIpinterval*2){status="warning";}}
                ipBean.setStatus(status);
                ipBeanList.add(ipBean);
                i=i+1;
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return ipBeanList;
        }
        return ipBeanList;
    }

    //获取所有IP段
    private ArrayList<IpSegmentBean> getIpSegment(String area) {
        ArrayList<IpSegmentBean> IpSegmentBeanList=new ArrayList<IpSegmentBean>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="select t2.area,t1.ipstart,t1.ipend,t1.areacode from ("+
                "SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area=\'"+area+
                "\') t3,ipsegment t1,organization t2 where (t1.areacode=t2.areacode)"+
                " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ipstart";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i=1;
            while (rs.next())
            {
                IpSegmentBean IpSegmentBean =new IpSegmentBean();
                IpSegmentBean.setId(i);
                IpSegmentBean.setIpstart(rs.getString("ipstart"));
                IpSegmentBean.setIpend(rs.getString("ipend"));
                IpSegmentBean.setArea(rs.getString("area"));
                IpSegmentBeanList.add(IpSegmentBean);
                i=i+1;
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return IpSegmentBeanList;
        }
        return IpSegmentBeanList;
    }


}
