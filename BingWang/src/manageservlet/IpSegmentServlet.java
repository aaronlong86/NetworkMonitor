package manageservlet;

import bean.IpSegmentBean;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sys.Mysqldb;

@WebServlet(name="IpSegmentServlet")
public class IpSegmentServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String areacode = request.getParameter("areacode");
        String area = request.getParameter("area");
        String ipstart = request.getParameter("ipstart");
        String ipend = request.getParameter("ipend");
        String level = request.getParameter("level");
        String flag = request.getParameter("flag");
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("username");
        if ((areacode != null) || (area != null) || (ipstart != null) || (ipend != null) || (level != null)) {
            if (flag.equals("add")){addipsegment(area, areacode, ipstart, ipend, level, username);}
            if (flag.equals("delete")){deleteipsegment(area,areacode,ipstart,ipend,level,username);}
        }
        ArrayList<IpSegmentBean> ipsegList = getIpSegment();
        request.setAttribute("ipsegList", ipsegList);
        request.getRequestDispatcher("/manage/ipsegmentset.jsp").forward(request, response);
    }

    private ArrayList<IpSegmentBean> getIpSegment()
    {
        ArrayList<IpSegmentBean> ipSegmentBeanList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "select * from ipsegment where flag=1 order by areacode,ipstart";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next())
            {
                IpSegmentBean ipSegmentBean = new IpSegmentBean();
                ipSegmentBean.setId(i);
                ipSegmentBean.setIpstart(rs.getString("ipstart"));
                ipSegmentBean.setIpend(rs.getString("ipend"));
                ipSegmentBean.setArea(rs.getString("area"));
                ipSegmentBean.setAreacode(rs.getString("areacode"));
                ipSegmentBean.setLevel(rs.getInt("level"));
                ipSegmentBeanList.add(ipSegmentBean);
                i += 1;
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return ipSegmentBeanList;
        }
        return ipSegmentBeanList;
    }

    private boolean addipsegment(String area, String areacode, String ipstart, String ipend, String level, String username)
    {
        if ((!validate(ipstart)) || (!validate(ipend)))
        {
            System.err.println("不是有效的IP地址。" + ipstart);
            return false;
        }
        Mysqldb mdb = new Mysqldb();
        try
        {
            SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sqlstr = "insert into ipsegment (area,areacode,ipstart,ipend,level,recorder,recordtime) VALUES('" + area + "','" + areacode + "','" + ipstart + "','" + ipend + "'," + level + ",'" + username + "','" + Timestamp.valueOf(disctime.format(new Date())) + "')";

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

    private boolean deleteipsegment(String area, String areacode, String ipstart, String ipend, String level, String username)
    {
        if ((!validate(ipstart)) || (!validate(ipend)))
        {
            System.err.println("不是有效的IP地址。" + ipstart);
            return false;
        }
        Mysqldb mdb = new Mysqldb();
        try
        {
            SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sqlstr = "update ipsegment set flag=0,recordtime=\'"+
                    Timestamp.valueOf(disctime.format(new Date()))+
                    "\',recorder=\'"+username+"\' where areacode=\'"+
                    areacode+"\' and ipstart=\'"+ipstart+"\' and ipend=\'"+ipend+"\' and level ="+level;
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

    public static boolean validate(String ip){
        if(ip==null||ip.length()==0) return false;
        String[] array=ip.split("\\.");
        if(array.length!=4) return false;
        for(String str:array){
            try {
                int num=Integer.valueOf(str);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
