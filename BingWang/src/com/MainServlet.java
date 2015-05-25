package com;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2015/5/9 0009.
 */
@WebServlet(name = "MainServlet")
public class MainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RecordVisit recordVisit = new RecordVisit();
        recordVisit.insertrecord(request);
        ArrayList<OrganizationBean> orgList;
        orgList = getdishi();
        request.setAttribute("orgList", orgList);
        request.getRequestDispatcher("main.jsp").forward(request, response);
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
