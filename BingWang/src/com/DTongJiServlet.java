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
import java.util.Collections;

/**
 * Created by Administrator on 2015/5/8 0008.
 */
@WebServlet(name = "DTongJiServlet")
public class DTongJiServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String area = (String)request.getParameter("area");
        response.setContentType("text/html;charset=UTF-8");
        RecordVisit recordVisit = new RecordVisit();
        recordVisit.insertrecord(request);
        ArrayList<TongJiBean> tjList=gettongji(area);
        request.setAttribute("tjList", tjList);
        request.getRequestDispatcher("tongji.jsp").forward(request, response);
    }

    private ArrayList<TongJiBean> gettongji(String area){
        String str1="SELECT t4.area,t5.tasknum FROM (SELECT "+
                " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                " WHERE t1.area=\'"+area+"\')"
                +" t2,organization t4,tasknumber t5 WHERE (SUBSTRING(t4.areacode,1,4) = t2.dishicode)"
                +" and (t5.areacode=t4.areacode) and (t5.level=2) and (t4.areacode LIKE '%000000')";
        String str2="SELECT COUNT(*),t4.area,SUBSTRING(t4.areacode, 1, 6) AS xiancode FROM (SELECT "+
                " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1 WHERE t1.area=\'"+area+"\')"
                +" t2, ipdiscovery t3,organization t4 WHERE (SUBSTRING(t3.areacode,1,4) = "
                +"t2.dishicode)  and (t4.areacode=t3.areacode) and (t3.areacode LIKE '%000000')"
                +"and (t3.ip like \"45%\") GROUP BY xiancode";

        ArrayList<TongJiBean> completetable=new ArrayList<TongJiBean>();
        Mysqldb mdb = new Mysqldb();
        try
        { 	ResultSet rs = mdb.sql.executeQuery(str1);
            while (rs.next())
            {
                TongJiBean tongJiBean=new TongJiBean();
                tongJiBean.setArea(rs.getString("t4.area"));
                tongJiBean.setIpnum(0);
                tongJiBean.setTasknum(rs.getInt("t5.tasknum"));
                tongJiBean.setTaskrate(0);
                completetable.add(tongJiBean);
            }

            rs = mdb.sql.executeQuery(str2);
            while (rs.next())
            {
                for (int i=0;i<completetable.size();i++) {
                    if (completetable.get(i).getArea().equals(rs.getString("t4.area")))
                    {completetable.get(i).setIpnum(Integer.valueOf(rs.getString("COUNT(*)")));
                        completetable.get(i).setTaskrate(((float)completetable.get(i).getIpnum())/((float)completetable.get(i).getTasknum()));
                    }
                }
            }
            Collections.sort(completetable, new SortTongji());
            int i=1;
            for (TongJiBean tongJiBean:completetable){tongJiBean.setRanking(i);i++;}
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return completetable;
        }
        return completetable;
    }
}
