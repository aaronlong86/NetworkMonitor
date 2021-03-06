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
import javax.servlet.http.HttpSession;

import sys.Init;
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
        String areacode = request.getParameter("areacode");
        String onlinerate = request.getParameter("onlinerate");
        String areacode45 = request.getParameter("areacode45");
        ArrayList<IpBean> ipBeanList = new ArrayList();
        ArrayList<IpSegmentBean> ipsegList = new ArrayList();
        if (areacode == null)
        {
            ipBeanList = getDiscoveryIp(areacode45, 45);
            ipsegList = getIpSegment(areacode45);
        }
        if (areacode45 == null) {
            if (onlinerate == null)
            {
                ipBeanList = getDiscoveryIp(areacode);
                ipsegList = getIpSegment(areacode);
            }
            else
            {
                ipBeanList = getDiscoveryIp(areacode, onlinerate);
                ipsegList = getIpSegment(areacode);
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

    private ArrayList<IpBean> getDiscoveryIp(String areacode)
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (areacode='"
                    + areacode + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where areacode='"
                        + areacode + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"
                        + "and (t1.flag=1) and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            } else {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where areacode='"
                        + areacode + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"
                        + "and (t1.flag=1) and (level=2) and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ip";
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
                int l = (int)((datenow.getTime() - datepre.getTime()) / 60000L);
                String status = "offline";
                if (l < Init.scanIpinterval) {
                    status = "online";
                } else if (l < (Init.scanIpinterval*2)) {
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

    private ArrayList<IpBean> getDiscoveryIp(String areacode, int ipseg)
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (areacode='" + areacode + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where areacode='"
                        + areacode + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"
                        + " and (t1.flag=1)  and (t1.ip like '" + Integer.toString(ipseg) + "%')" +
                        " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            } else {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where areacode='"
                        + areacode + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"
                        + " and (t1.flag=1)  and (t2.level=2) and (t1.ip like '" + Integer.toString(ipseg) + "%')" +
                        " and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ip";
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
                int l = (int)((datenow.getTime() - datepre.getTime()) / 60000L);
                String status = "offline";
                if (l <= Init.scanIpinterval) {
                    status = "online";
                } else if (l <= (Init.scanIpinterval*2)) {
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

    private ArrayList<IpBean> getDiscoveryIp(String areacode, String onlinerate)
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (areacode='" + areacode + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where areacode='"
                        + areacode + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"
                        + " and (t1.flag=1)  and ((TIMESTAMPDIFF(MINUTE,t1.discoverylasttime,now()))>=" +
                        Integer.toString(8) + ")" + " and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ip";
            } else {
                sqlstr = "select t1.ip,t2.area,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where areacode='"
                        + areacode + "') t3,ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode)"
                        + " and (t1.flag=1) and ((TIMESTAMPDIFF(MINUTE,t1.discoverylasttime,now()))>=" + Integer.toString(8)
                        + ") and (t2.level=2)" + " and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ip";
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
                int l = (int)((datenow.getTime() - datepre.getTime()) / 60000L);
                String status = "offline";
                if (l <= Init.scanIpinterval) {
                    status = "online";
                } else if (l <= (Init.scanIpinterval*2)) {
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

    private ArrayList<IpSegmentBean> getIpSegment(String areacode)
    {
        ArrayList<IpSegmentBean> IpSegmentBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT areacode from organization t4 where (areacode='" + areacode + "') and (areacode like '%00000000')";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            if (rs.next()) {
                sqlstr = "select t2.area,t1.ipstart,t1.ipend,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,4) as dishicode from organization t4 where areacode='"
                        + areacode + "') t3,ipsegment t1,organization t2 where (t1.areacode=t2.areacode)"
                        + " and (t1.flag=1) and (SUBSTRING(t2.areacode,1,4)=t3.dishicode) order by t1.areacode,t1.ipstart";
            } else {
                sqlstr = "select t2.area,t1.ipstart,t1.ipend,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where areacode='"
                        + areacode + "') t3,ipsegment t1,organization t2 where (t1.areacode=t2.areacode) and (t2.level=2)"
                        + " and (t1.flag=1) and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ipstart";
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
