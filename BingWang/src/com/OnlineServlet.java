package com;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/5/8 0008.
 */
@WebServlet(name = "OnlineServlet")
public class OnlineServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RecordVisit recordVisit = new RecordVisit();
        recordVisit.insertrecord(request);
        ArrayList<OnLineBean> onLineBeanList;
        onLineBeanList = getonline();
        request.setAttribute("onLineBeanList", onLineBeanList);
        request.getRequestDispatcher("online.jsp").forward(request, response);
    }
    private ArrayList<OnLineBean> getonline(){
        ArrayList<OnLineBean> onLineBeanList=new ArrayList<OnLineBean>();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String str ="select area from organization where (areacode like '%00000000') and (areacode not LIKE '%0000000000') order by areacode";
            ResultSet rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                OnLineBean onLineBean=new OnLineBean();
                onLineBean.setArea(rs.getString("area"));
                onLineBean.setTotalnum(0);
                onLineBean.setOnlinenum(0);
                onLineBean.setOnlinerate(0);
                onLineBeanList.add(onLineBean);
            }

            str="SELECT COUNT(*),t2.area,t2.dishicode FROM (SELECT t1.area,"+
                    " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                    " WHERE (t1.areacode LIKE \'%00000000\') and (t1.areacode not LIKE '%0000000000'))"
                    +" t2, ipdiscovery t3 WHERE     SUBSTRING(t3.areacode,1,4) = t2.dishicode "
                    +" GROUP BY t2.dishicode ORDER BY t2.dishicode";
            rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                for (int i=0;i<onLineBeanList.size();i++) {
                    if (rs.getString("t2.area").equals(onLineBeanList.get(i).getArea()))
                    {
                        onLineBeanList.get(i).setTotalnum(Integer.valueOf(rs.getString("COUNT(*)")));
                    }
                }
            }

            str="SELECT COUNT(*),t2.area,t2.dishicode FROM (SELECT t1.area,"+
                    " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                    " WHERE (t1.areacode LIKE \'%00000000\') and (t1.areacode not LIKE '%0000000000'))"
                    +" t2, ipdiscovery t3 WHERE  (SUBSTRING(t3.areacode,1,4) = t2.dishicode) "
                    +"and ((TIMESTAMPDIFF(MINUTE,t3.discoverylasttime,now()))<="+
                    Integer.toString(Init.scanIpinterval)+") GROUP BY t2.dishicode ORDER BY t2.dishicode";

             	rs = mdb.sql.executeQuery(str);
                while (rs.next())
                {  for (int i=0;i<onLineBeanList.size();i++) {
                    if (onLineBeanList.get(i).getArea().equals(rs.getString("t2.area")))
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
