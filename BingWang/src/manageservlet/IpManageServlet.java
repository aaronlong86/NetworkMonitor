package manageservlet;

import bean.DeviceTypeBean;
import bean.IpBean;
import bean.IpSegmentBean;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
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
import showservlet.SortIp;
import sys.Mysqldb;

@WebServlet(name="IpManageServlet")
public class IpManageServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String devicetype = request.getParameter("devicetype");
        String manager = request.getParameter("manager");
        String brand = request.getParameter("brand");
        String location = request.getParameter("location");
        String application = request.getParameter("application");
        String ip = request.getParameter("ip");
        String flag = request.getParameter("flag");
        HttpSession session = request.getSession();
        String areacode = (String)session.getAttribute("areacode");
        String username = (String)session.getAttribute("username");
        if (ip != null) {
            if (flag.equals("modify"))
            {
            updateinfo(devicetype, manager, brand, location, application, ip, username);}
            if (flag.equals("delete")){
                deleteinfo(ip,username);
            }
        }
        ArrayList<IpBean> ipBeanList;
        ArrayList<IpSegmentBean> ipsegList;
        if (username.equals("admin")){
            ipBeanList = getAllIp();
            ipsegList=getAllIpSegment();}
        else {
        ipBeanList = getDiscoveryIp(areacode);
        ipsegList=getIpSegment(areacode);}


        ArrayList<DeviceTypeBean> deviceTypeBeans = getDeviceType();
        Collections.sort(ipBeanList, new SortIp());
        int i = 1;
        for (Iterator i$ = ipBeanList.iterator(); i$.hasNext(); i++)
        {
            IpBean ipb = (IpBean)i$.next();
            ipb.setId(i);
        }
        request.setAttribute("ipBeanList", ipBeanList);
        request.setAttribute("ipsegList", ipsegList);
        request.setAttribute("devicetypeList", deviceTypeBeans);
        request.getRequestDispatcher("/manage/ip.jsp").forward(request, response);
    }

    private ArrayList<IpBean> getDiscoveryIp(String areacode)
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT t1.ip,t2.area,t1.devicetype,t1.manager,t1.brand,t1.location,t1.application,t1.discoverylasttime from ipdiscovery t1,organization t2 where (t1.areacode like '"
                    + areacode.substring(0, 4) + "%') and (t1.flag=1) and (t1.areacode=t2.areacode)";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                IpBean ipBean = new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("t1.ip"));
                ipBean.setArea(rs.getString("t2.area"));
                ipBean.setLastdiscovery(rs.getString("t1.discoverylasttime"));
                ipBean.setDevicetype(rs.getString("t1.devicetype"));
                ipBean.setManager(rs.getString("t1.manager"));
                ipBean.setBrand(rs.getString("t1.brand"));
                ipBean.setLocation(rs.getString("t1.location"));
                ipBean.setApplication(rs.getString("t1.application"));
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

    private ArrayList<IpBean> getAllIp()
    {
        ArrayList<IpBean> ipBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT t1.ip,t2.area,t1.devicetype,t1.manager,t1.brand,t1.location,t1.application,t1.discoverylasttime from ipdiscovery t1,organization t2 where (t1.areacode=t2.areacode) and (t1.flag=1) order by t1.ip";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                IpBean ipBean = new IpBean();
                ipBean.setId(i);
                ipBean.setIp(rs.getString("t1.ip"));
                ipBean.setArea(rs.getString("t2.area"));
                ipBean.setLastdiscovery(rs.getString("t1.discoverylasttime"));
                ipBean.setDevicetype(rs.getString("t1.devicetype"));
                ipBean.setManager(rs.getString("t1.manager"));
                ipBean.setBrand(rs.getString("t1.brand"));
                ipBean.setLocation(rs.getString("t1.location"));
                ipBean.setApplication(rs.getString("t1.application"));
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

    private ArrayList<IpSegmentBean> getIpSegment(String areacode)
    {
        ArrayList<IpSegmentBean> IpSegmentBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT * from ipsegment where (flag=1) and (areacode like '"
                    + areacode.substring(0, 4) + "%') order by ipstart,areacode";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
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

    private ArrayList<IpSegmentBean> getAllIpSegment()
    {
        ArrayList<IpSegmentBean> IpSegmentBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT * from ipsegment where flag=1 order by ipstart,areacode";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
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

    private ArrayList<DeviceTypeBean> getDeviceType()
    {
        ArrayList<DeviceTypeBean> deviceTypeBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT * from devicetype";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                DeviceTypeBean deviceTypeBean = new DeviceTypeBean();
                deviceTypeBean.setDevicetype(rs.getString("devicetype"));
                deviceTypeBeanList.add(deviceTypeBean);
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return deviceTypeBeanList;
        }
        return deviceTypeBeanList;
    }

    private void updateinfo(String devicetype, String manager, String brand, String location, String application, String ip, String username)
    {
        Mysqldb mdb = new Mysqldb();
        try
        {
            SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sqlstr = "UPDATE ipdiscovery set devicetype='" + devicetype + "',manager='" + manager + "',brand='" + brand +  "',location='" + location + "',application='" + application + "',recorder='" + username + "',recordtime='" + Timestamp.valueOf(disctime.format(new Date())) + "' where ip='" + ip + "'";

            mdb.sql.executeUpdate(sqlstr);
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
        }
    }

    private boolean deleteinfo(String ip,String username)
    {
        Mysqldb mdb = new Mysqldb();
        try
        {
            SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sqlstr = "update ipdiscovery set flag=0,recordtime=\'"+
                    Timestamp.valueOf(disctime.format(new Date()))+
                    "\',recorder=\'"+username+"\' where ip=\'"+
                    ip+"\'";
            mdb.sql.executeUpdate(sqlstr);
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return false;
        }
        return true;
    }
}
