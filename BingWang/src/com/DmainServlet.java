package com;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/8 0008.
 */
@WebServlet(name = "DmainServlet")
public class DmainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String area = (String)request.getParameter("area");
        response.setContentType("text/html;charset=UTF-8");
        RecordVisit recordVisit = new RecordVisit();
        recordVisit.insertrecord(request);
        ArrayList<OrganizationBean> orgList;
        orgList = getdishi();
        request.setAttribute("orgList", orgList);
        request.setAttribute("area",area);
        request.getRequestDispatcher("dishi.jsp").forward(request, response);
    }
    //获取县分局单位列表
    private ArrayList<OrganizationBean> getxianju(String area) {
        ArrayList<OrganizationBean> orgList=new ArrayList<OrganizationBean>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="select t3.area,t3.areacode from ("+
                "SELECT t1.area,SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1 WHERE t1.area=\'"
                +area+"\') t2,organization t3 where (SUBSTRING(t3.areacode, 1, 4)=dishicode) and"+
                " (t3.areacode like '%000000') and (t3.areacode not like '%00000000') order by t3.areacode";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                OrganizationBean organizationBean =new OrganizationBean();
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
    //获取市局单位列表
    private ArrayList<OrganizationBean> getdishi() {
        ArrayList<OrganizationBean> orgList=new ArrayList<OrganizationBean>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="select area,areacode from organization where (areacode like '%00000000') and (areacode not like '%0000000000') order by areacode";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                OrganizationBean organizationBean =new OrganizationBean();
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
}
