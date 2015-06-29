package showservlet;

import bean.OrganizationBean;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sys.Mysqldb;

@WebServlet(name="DmainServlet")
public class DmainServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String area = request.getParameter("area");
        ArrayList<OrganizationBean> orgList = getdishi();
        request.setAttribute("orgList", orgList);
        request.setAttribute("area", area);
        String visitnum=Integer.toString(getVisitnum());
        request.setAttribute("visitnum", visitnum);
        request.getRequestDispatcher("dishi.jsp").forward(request, response);
    }

    private ArrayList<OrganizationBean> getxianju(String area)
    {
        ArrayList<OrganizationBean> orgList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "select t3.area,t3.areacode from (SELECT t1.area,SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1 WHERE t1.area='" + area + "') t2,organization t3 where (SUBSTRING(t3.areacode, 1, 4)=dishicode) and" + " (t3.areacode like '%000000') and (t3.areacode not like '%00000000') order by t3.areacode";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                OrganizationBean organizationBean = new OrganizationBean();
                organizationBean.setArea(rs.getString("area"));
                orgList.add(organizationBean);
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return orgList;
        }
        return orgList;
    }

    private ArrayList<OrganizationBean> getdishi()
    {
        ArrayList<OrganizationBean> orgList = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "select area,areacode from organization where (areacode like '%00000000') and (areacode not like '%0000000000') order by areacode";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                OrganizationBean organizationBean = new OrganizationBean();
                organizationBean.setArea(rs.getString("area"));
                orgList.add(organizationBean);
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return orgList;
        }
        return orgList;
    }

    private int getVisitnum()
    {
        int num=0;
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "select count(*) from recordvisit where path=\'/main\'";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next()){num=Integer.valueOf(rs.getString("COUNT(*)"));}
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return num;
        }
        return num;
    }
}
