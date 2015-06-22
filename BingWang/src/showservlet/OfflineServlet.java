package showservlet;

import bean.IpBean;
import sys.Init;
import sys.Mysqldb;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/19 0019.
 */
@WebServlet(name = "OfflineServlet")
public class OfflineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<IpBean> networkBeans =getOfflineNetwork();
        ArrayList<IpBean> platformBeans =getOfflinePlatform();
        request.setAttribute("network", networkBeans);
        request.setAttribute("platform", platformBeans);
        request.getRequestDispatcher("offline.jsp").forward(request, response);
    }

    private ArrayList<IpBean> getOfflineNetwork(){
        ArrayList<IpBean> ipBeans=new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT t1.ip,t2.area,t1.areacode FROM ipdiscovery t1,organization t2 WHERE (t1.devicetype = '交换机') and ((TIMESTAMPDIFF(MINUTE,t1.discoverylasttime,now()))>"
                    +Integer.toString(Init.scanIpinterval)+") and (t1.areacode=t2.areacode) order by t1.areacode";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                IpBean ipBean = new IpBean();
                ipBean.setIp(rs.getString("t1.ip"));
                ipBean.setArea(rs.getString("t2.area"));
                ipBean.setStatus("offline");
                ipBeans.add(ipBean);
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return ipBeans;
        }
        return ipBeans;
    }

    private ArrayList<IpBean> getOfflinePlatform(){
        ArrayList<IpBean> ipBeans=new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "SELECT t1.ip,t2.area,t1.areacode FROM ipdiscovery t1,organization t2 WHERE (t1.devicetype = '视频联网平台') and ((TIMESTAMPDIFF(MINUTE,t1.discoverylasttime,now()))>"
                    +Integer.toString(Init.scanIpinterval)+") and (t1.areacode=t2.areacode) order by t1.areacode";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                IpBean ipBean = new IpBean();
                ipBean.setIp(rs.getString("t1.ip"));
                ipBean.setArea(rs.getString("t2.area"));
                ipBean.setStatus("offline");
                ipBeans.add(ipBean);
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return ipBeans;
        }
        return ipBeans;
    }
}
