package showservlet;

import bean.IpBean;
import bean.IpSegmentBean;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sys.Mysqldb;

@WebServlet(name="IpServlet")
public class IpServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String area = request.getParameter("area");
        String onlinerate = request.getParameter("onlinerate");
        String area45 = request.getParameter("area45");
        ArrayList<IpBean> ipBeanList = new ArrayList();
        ArrayList<IpSegmentBean> ipsegList = new ArrayList();
        if (area == null)
        {
            ipBeanList = getDiscoveryIp(area45, 45);
            ipsegList = getIpSegment(area45);
        }
        if (area45 == null) {
            if (onlinerate == null)
            {
                ipBeanList = getDiscoveryIp(area);
                ipsegList = getIpSegment(area);
            }
            else
            {
                ipBeanList = getDiscoveryIp(area, onlinerate);
                ipsegList = getIpSegment(area);
            }
        }
        Collections.sort(ipBeanList, new SortIp());
        int i = 1;
        for (Iterator i$ = ipBeanList.iterator(); i$.hasNext(); i++)
        {
            IpBean ipb = (IpBean)i$.next();
            ipb.setId(i);
        }
        request.setAttribute("ipBeanList", ipBeanList);
        request.setAttribute("ipsegList", ipsegList);
        request.getRequestDispatcher("/ip.jsp").forward(request, response);
    }

    private ArrayList<IpBean> getDiscoveryIp(String area)
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (area='" + area + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area='" + area + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)" + " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            } else {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where area='" + area + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)" + " and (level=2) and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ip";
            }
            rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                IpBean ipBean = new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("ip"));
                ipBean.setArea(rs.getString("area"));
                ipBean.setLastdiscovery(rs.getString("discoverylasttime"));
                Date datenow = new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre = disctime.parse(rs.getString("discoverylasttime"));
                long l = (datenow.getTime() - datepre.getTime()) / 60000L;
                String status = "offline";
                if (l < 8L) {
                    status = "online";
                } else if (l < 16L) {
                    status = "warning";
                }
                ipBean.setStatus(status);
                ipBeanList.add(ipBean);
                i += 1;
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

    private ArrayList<IpBean> getDiscoveryIp(String area, int ipseg)
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (area='" + area + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area='" + area + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)" + " and (t1.ip like '" + Integer.toString(ipseg) + "%')" + " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            } else {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where area='" + area + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)" + " and (t2.level=2) and (t1.ip like '" + Integer.toString(ipseg) + "%')" + " and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ip";
            }
            rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                IpBean ipBean = new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("ip"));
                ipBean.setArea(rs.getString("area"));
                ipBean.setLastdiscovery(rs.getString("discoverylasttime"));
                Date datenow = new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre = disctime.parse(rs.getString("discoverylasttime"));
                long l = (datenow.getTime() - datepre.getTime()) / 60000L;
                String status = "offline";
                if (l <= 8L) {
                    status = "online";
                } else if (l <= 16L) {
                    status = "warning";
                }
                ipBean.setStatus(status);
                ipBeanList.add(ipBean);
                i += 1;
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

    private ArrayList<IpBean> getDiscoveryIp(String area, String onlinerate)
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (area='" + area + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area='" + area + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)" + " and ((TIMESTAMPDIFF(MINUTE,t1.discoverylasttime,now()))>=" + Integer.toString(8) + ")" + " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            } else {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where area='" + area + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)" + " and ((TIMESTAMPDIFF(MINUTE,t1.discoverylasttime,now()))>=" + Integer.toString(8) + ") and (t2.level=2)" + " and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ip";
            }
            rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                IpBean ipBean = new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("ip"));
                ipBean.setArea(rs.getString("area"));
                ipBean.setLastdiscovery(rs.getString("discoverylasttime"));
                Date datenow = new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre = disctime.parse(rs.getString("discoverylasttime"));
                long l = (datenow.getTime() - datepre.getTime()) / 60000L;
                String status = "offline";
                if (l <= 8L) {
                    status = "online";
                } else if (l <= 16L) {
                    status = "warning";
                }
                ipBean.setStatus(status);
                ipBeanList.add(ipBean);
                i += 1;
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

    private ArrayList<IpSegmentBean> getIpSegment(String area)
    {
        ArrayList<IpSegmentBean> IpSegmentBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (area='" + area + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t2.area,t1.ipstart,t1.ipend,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where area='" + area + "') t3,ipsegment t1,organization t2 where (t1.areacode=t2.areacode)" + " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ipstart";
            } else {
                sqlstr = "select t2.area,t1.ipstart,t1.ipend,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where area='" + area + "') t3,ipsegment t1,organization t2 where (t1.areacode=t2.areacode) and (t2.level=2)" + " and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ipstart";
            }
            rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                IpSegmentBean IpSegmentBean = new IpSegmentBean();
                IpSegmentBean.setId(i);
                IpSegmentBean.setIpstart(rs.getString("ipstart"));
                IpSegmentBean.setIpend(rs.getString("ipend"));
                IpSegmentBean.setArea(rs.getString("area"));
                IpSegmentBeanList.add(IpSegmentBean);
                i += 1;
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
