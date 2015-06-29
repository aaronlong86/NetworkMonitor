package showservlet;

import bean.OnLineBean;
import bean.TongJiBean;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sys.Init;
import sys.Mysqldb;

@WebServlet(name="DTongJiServlet")
public class DTongJiServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String area = request.getParameter("area");
        ArrayList<TongJiBean> tjList = gettongji(area);
        request.setAttribute("tjList", tjList);
        ArrayList<OnLineBean> onLineBeanList = dgetonline(area);
        request.setAttribute("onLineBeanList", onLineBeanList);
        request.getRequestDispatcher("tongji.jsp").forward(request, response);
    }

    private ArrayList<TongJiBean> gettongji(String area)
    {
        String str1 = "SELECT t4.area,t4.areacode,t5.tasknum FROM (SELECT  SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1 WHERE t1.area='" + area + "')" + " t2,organization t4,tasknumber t5 WHERE (SUBSTRING(t4.areacode,1,4) = t2.dishicode)" + " and (t5.areacode=t4.areacode) and (t5.level=2) and (t4.areacode LIKE '%000000')";

        String str2 = "SELECT COUNT(*),t4.area,SUBSTRING(t4.areacode, 1, 6) AS xiancode FROM (SELECT  SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1 WHERE t1.area='" + area + "')" + " t2, ipdiscovery t3,organization t4 WHERE (SUBSTRING(t3.areacode,1,4) = " + "t2.dishicode)  and (t4.areacode=t3.areacode) and (t3.areacode LIKE '%000000')" + "and (t3.ip like \"45%\") GROUP BY xiancode";

        ArrayList<TongJiBean> completetable = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            ResultSet rs = mdb.sql.executeQuery(str1);
            while (rs.next())
            {
                TongJiBean tongJiBean = new TongJiBean();
                tongJiBean.setArea(rs.getString("t4.area"));
                tongJiBean.setAreacode(rs.getString("t4.areacode"));
                tongJiBean.setIpnum(0);
                tongJiBean.setTasknum(rs.getInt("t5.tasknum"));
                tongJiBean.setTaskrate(0);
                completetable.add(tongJiBean);
            }
            rs = mdb.sql.executeQuery(str2);
            while (rs.next()) {
                for (int i = 0; i < completetable.size(); i++) {
                    if (((TongJiBean)completetable.get(i)).getArea().equals(rs.getString("t4.area")))
                    {
                        ((TongJiBean)completetable.get(i)).setIpnum(Integer.valueOf(rs.getString("COUNT(*)")).intValue());
                        ((TongJiBean)completetable.get(i)).setTaskrate((float)(((TongJiBean)completetable.get(i)).getIpnum()) / ((TongJiBean)completetable.get(i)).getTasknum());
                    }
                }
            }
            Collections.sort(completetable, new SortTongji());
            int i = 1;
            int total=0;
            int tasknum=0;
            for (Iterator i$ = completetable.iterator(); i$.hasNext(); i++)
            {
                TongJiBean tongJiBean = (TongJiBean)i$.next();
                tongJiBean.setRanking(i);
                String s=tongJiBean.getArea().replace("广西","");
                s=s.replace("市公安局","");
                s=s.replace("分局","");
                s=s.replace("公安局","");
                s=s.replace("壮族自治区","");
                tongJiBean.setArea(s);
                total=tongJiBean.getIpnum()+total;
                tasknum=tongJiBean.getTasknum()+tasknum;
            }
            TongJiBean tongJiBean =new TongJiBean();
            tongJiBean.setArea("全市");
            tongJiBean.setAreacode("450000000000");
            tongJiBean.setIpnum(total);
            tongJiBean.setTasknum(tasknum);
            tongJiBean.setTaskrate(((float) total) / tasknum);
            completetable.add(tongJiBean);
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

    private ArrayList<OnLineBean> dgetonline(String area){
        ArrayList<OnLineBean> onLineBeanList=new ArrayList<OnLineBean>();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String str ="SELECT t4.area,t4.areacode FROM (SELECT "+
                    " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                    " WHERE t1.area=\'"+area+"\')"
                    +" t2,organization t4 WHERE (SUBSTRING(t4.areacode,1,4) = t2.dishicode)"
                    +" and (t4.areacode LIKE '%000000')";
            ResultSet rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                OnLineBean onLineBean=new OnLineBean();
                onLineBean.setArea(rs.getString("t4.area"));
                onLineBean.setAreacode(rs.getString("t4.areacode"));
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
            int total=0;
            int onlinenum=0;
            for(OnLineBean onLineBean:onLineBeanList)
            {
                String s=onLineBean.getArea().replace("广西", "");
                s=s.replace("市公安局","");
                s=s.replace("分局","");
                s=s.replace("公安局","");
                s=s.replace("壮族自治区","");
                onLineBean.setArea(s);
                total=total+onLineBean.getTotalnum();
                onlinenum=onlinenum+onLineBean.getOnlinenum();
            }
            OnLineBean onLineBean =new OnLineBean();
            onLineBean.setArea("全市");
            onLineBean.setAreacode("450000000000");
            onLineBean.setOnlinenum(onlinenum);
            onLineBean.setTotalnum(total);
            onLineBean.setOnlinerate(((float)onlinenum)/total);
            onLineBeanList.add(onLineBean);
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
