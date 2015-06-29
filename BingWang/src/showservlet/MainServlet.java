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

@WebServlet(name="MainServlet")
public class MainServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        ArrayList<OrganizationBean> orgList = getdishi();
        request.setAttribute("orgList", orgList);
        String visitnum=Integer.toString(getVisitnum());
        request.setAttribute("visitnum", visitnum);
        request.getRequestDispatcher("main.jsp").forward(request, response);
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
