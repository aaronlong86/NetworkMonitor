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
 * Created by Administrator on 2015/5/9 0009.
 */
@WebServlet(name = "DOnlineServlet")
public class DOnlineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String area = (String)request.getParameter("area");
        RecordVisit recordVisit = new RecordVisit();
        recordVisit.insertrecord(request);
        response.setContentType("text/html;charset=UTF-8");
        ArrayList<OnLineBean> onLineBeanList;
        onLineBeanList = dgetonline(area);
        request.setAttribute("onLineBeanList", onLineBeanList);
        request.getRequestDispatcher("online.jsp").forward(request, response);
    }
    private ArrayList<OnLineBean> dgetonline(String area){
        ArrayList<OnLineBean> onLineBeanList=new ArrayList<OnLineBean>();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String str ="SELECT t4.area FROM (SELECT "+
                    " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                    " WHERE t1.area=\'"+area+"\')"
                    +" t2,organization t4 WHERE (SUBSTRING(t4.areacode,1,4) = t2.dishicode)"
                    +" and (t4.areacode LIKE '%000000')";
            ResultSet rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                OnLineBean onLineBean=new OnLineBean();
                onLineBean.setArea(rs.getString("t4.area"));
                onLineBean.setTotalnum(0);
                onLineBean.setOnlinenum(0);
                onLineBean.setOnlinerate(0);
                onLineBeanList.add(onLineBean);
            }

            str="SELECT COUNT(*),t4.area,SUBSTRING(t4.areacode, 1, 6) AS xiancode FROM (SELECT "+
                    " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                    " WHERE t1.area=\'"+area+"\')"
                    +" t2, ipdiscovery t3,organization t4 WHERE (SUBSTRING(t3.areacode,1,4) = t2.dishicode)"
                    +" and (t4.areacode=t3.areacode)"+
                    " and (t3.areacode LIKE '%000000') GROUP BY xiancode ORDER BY xiancode";
            rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                for (int i=0;i<onLineBeanList.size();i++) {
                    if (rs.getString("t4.area").equals(onLineBeanList.get(i).getArea()))
                    {
                        onLineBeanList.get(i).setTotalnum(Integer.valueOf(rs.getString("COUNT(*)")));
                    }
                }
            }
            str="SELECT COUNT(*),t4.area,SUBSTRING(t4.areacode, 1, 6) AS xiancode FROM (SELECT "+
                    " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                    " WHERE t1.area=\'"+area+"\')"
                    +" t2, ipdiscovery t3,organization t4 WHERE (SUBSTRING(t3.areacode,1,4) = t2.dishicode)"+
                    " and (t4.areacode=t3.areacode)"+
                    " and (t3.areacode LIKE '%000000') AND (t3.areacode NOT LIKE '%00000000')"
                    +"and ((TIMESTAMPDIFF(MINUTE,t3.discoverylasttime,now()))<="
                    +Integer.toString(Init.scanIpinterval)+") GROUP BY xiancode ORDER BY xiancode";

            rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {  for (int i=0;i<onLineBeanList.size();i++) {
                if (onLineBeanList.get(i).getArea().equals(rs.getString("t4.area")))
                {
                    int t1=onLineBeanList.get(i).getTotalnum();
                    int t2=(Integer.valueOf(rs.getString("COUNT(*)")));
                    onLineBeanList.get(i).setOnlinenum(t2);
                    if (t1>0) {float d=(float)(t2)/(float)(t1);
                        onLineBeanList.get(i).setOnlinerate(d);}
                }
            }
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return onLineBeanList;
        }
        return onLineBeanList;
    }

}
