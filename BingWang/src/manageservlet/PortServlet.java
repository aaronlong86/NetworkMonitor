package manageservlet;

import bean.IpSegmentBean;
import bean.PortBean;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import showservlet.SortPort;
import sys.Mysqldb;

@WebServlet(name="PortServlet")
public class PortServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String area = (String)session.getAttribute("area");
        ArrayList<PortBean> portBeanList = getDiscoveryPort(area);
        ArrayList<IpSegmentBean> ipsegList = getIpSegment(area);
        Collections.sort(portBeanList, new SortPort());
        int i = 0;
        int p = 0;
        for (PortBean pb : portBeanList)
        {
            if (p != pb.getPort()) {
                i++;
            }
            p = pb.getPort();
            pb.setId(i);
        }
        request.setAttribute("portBean", portBeanList);
        request.setAttribute("ipsegList", ipsegList);
        request.getRequestDispatcher("port.jsp").forward(request, response);
    }

    private ArrayList<PortBean> getDiscoveryPort(String area)
    {
        ArrayList<PortBean> portBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "select t1.ip,t2.area,t3.port,t3.protocol,t3.description,t1.discoverylasttime,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where area='" + area + "') t3,portdiscovery t1,organization t2,commonport t3 where (t1.areacode=t2.areacode)" + " and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) and (t3.port=t1.port) order by t1.areacode,t1.ip";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                PortBean portBean = new PortBean();
                portBean.setIp(rs.getString("t1.ip"));
                portBean.setArea(rs.getString("t2.area"));
                portBean.setLastdiscovery(rs.getString("t1.discoverylasttime"));
                portBean.setPort(rs.getInt("t3.port"));
                portBean.setProtocol(rs.getString("t3.protocol"));
                portBean.setDescription(rs.getString("t3.description"));
                Date datenow = new Date();
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date datepre = disctime.parse(rs.getString("discoverylasttime"));
                long l = (datenow.getTime() - datepre.getTime()) / 60000L;
                String status = "offline";
                if (l <= 240L) {
                    status = "online";
                } else if (l <= 480L) {
                    status = "warning";
                }
                portBean.setStatus(status);
                int j = portBeanList.size();
                portBean.setId(i);
                if ((j > 1) &&
                        (!((PortBean)portBeanList.get(j - 1)).getIp().equals(portBean.getIp())))
                {
                    i += 1;
                    portBean.setId(i);
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

    private ArrayList<IpSegmentBean> getIpSegment(String area)
    {
        ArrayList<IpSegmentBean> IpSegmentBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "select t2.area,t1.ipstart,t1.ipend,t1.areacode from (SELECT SUBSTRING(t4.areacode,1,6) as xiancode from organization t4 where area='" + area + "') t3,ipsegment t1,organization t2 where (t1.areacode=t2.areacode)" + " and (SUBSTRING(t2.areacode,1,6)=t3.xiancode) order by t1.areacode,t1.ipstart";

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
}
