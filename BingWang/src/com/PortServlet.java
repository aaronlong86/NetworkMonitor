package com;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Administrator on 2015/5/9 0009.
 */
@WebServlet(name = "PortServlet")
public class PortServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String area = (String)request.getParameter("area");
        response.setContentType("text/html;charset=UTF-8");
        RecordVisit recordVisit = new RecordVisit();
        recordVisit.insertrecord(request);
        ArrayList<PortBean> portBeanList = getDiscoveryPort(area);
        ArrayList<IpSegmentBean> ipsegList=getIpSegment(area);
        Collections.sort(portBeanList,new SortPort());
        request.setAttribute("portBean", portBeanList);
        request.setAttribute("ipsegList", ipsegList);
        request.getRequestDispatcher("port.jsp").forward(request, response);
    }
    //获取已发现的所有端口
    private ArrayList<PortBean> getDiscoveryPort(String area) {
        ArrayList<PortBean> portBeanList=new ArrayList<PortBean>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="select t1.ip,t2.area,t3.port,t3.protocol,t3.description,t1.discoverylasttime,t1.areacode from ("
                +"SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area=\'"+area+
                "\') t3,portdiscovery t1,organization t2,commonport t3 where (t1.areacode=t2.areacode)"+
                " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) and (t3.port=t1.port) order by t1.areacode,t1.ip";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i=1;
            while (rs.next())
            {
                PortBean portBean =new PortBean();
                portBean.setIp(rs.getString("t1.ip"));
                portBean.setArea(rs.getString("t2.area"));
                portBean.setLastdiscovery(rs.getString("t1.discoverylasttime"));
                portBean.setPort(rs.getInt("t3.port"));
                portBean.setProtocol(rs.getString("t3.protocol"));
                portBean.setDescription(rs.getString("t3.description"));
                Date datenow=new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre=disctime.parse(rs.getString("discoverylasttime"));
                long l=(datenow.getTime()-datepre.getTime())/60000;
                String status="offline";
                if (l<=Init.scanPortinterval){status="online";}
                else{if (l<=Init.scanPortinterval*2){status="warning";}}
                portBean.setStatus(status);
                int j=portBeanList.size();
                portBean.setId(i);
                if (j>1) {
                    if (!(portBeanList.get(j-1).getIp().equals(portBean.getIp()))){
                        i=i+1;
                        portBean.setId(i);
                     }
                   }
                portBeanList.add(portBean);
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return portBeanList;
        }
        return portBeanList;
    }

    //获取已发现的所有IP段
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
