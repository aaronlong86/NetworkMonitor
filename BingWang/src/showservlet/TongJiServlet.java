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

@WebServlet(name="TongJiServlet")
public class TongJiServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        ArrayList<TongJiBean> tjList = gettongji(45);
        request.setAttribute("tjList", tjList);
        ArrayList<OnLineBean> onLineBeanList = getonline();
        request.setAttribute("onLineBeanList", onLineBeanList);
        request.getRequestDispatcher("tongji.jsp").forward(request, response);
    }

    private ArrayList<TongJiBean> gettongji(int ipseg)
    {
        String str1 = "select t1.area,t1.areacode,t2.tasknum from organization t1,tasknumber t2 where (t1.areacode like '%00000000') and (t1.areacode not LIKE '%0000000000') and (t1.areacode=t2.areacode) and (t2.level=1)";

        String str2 = "SELECT COUNT(*),t2.area,t2.dishicode FROM (SELECT t1.area, SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1 WHERE t1.areacode LIKE '%00000000' AND t1.areacode NOT LIKE '%0000000000') t2, ipdiscovery t3 WHERE (SUBSTRING(t3.areacode,1,4) = t2.dishicode) AND (t3.ip LIKE \"" + Integer.toString(ipseg) + "%\")" + " GROUP BY t2.dishicode";

        ArrayList<TongJiBean> completetable = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try
        {
            ResultSet rs = mdb.sql.executeQuery(str1);
            while (rs.next())
            {
                TongJiBean tongJiBean = new TongJiBean();
                tongJiBean.setArea(rs.getString("t1.area"));
                tongJiBean.setAreacode(rs.getString("t1.areacode"));
                tongJiBean.setIpnum(0);
                tongJiBean.setTasknum(rs.getInt("t2.tasknum"));
                tongJiBean.setTaskrate(0);
                completetable.add(tongJiBean);
            }
            rs = mdb.sql.executeQuery(str2);
            while (rs.next()) {
                for (int i = 0; i < completetable.size(); i++) {
                    if (((TongJiBean)completetable.get(i)).getArea().equals(rs.getString("t2.area")))
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
                String s=tongJiBean.getArea().replace("广西","");
                s=s.replace("市公安局","");
                tongJiBean.setArea(s);
                tongJiBean.setRanking(i);
                total=tongJiBean.getIpnum()+total;
                tasknum=tongJiBean.getTasknum()+tasknum;
            }
            TongJiBean tongJiBean =new TongJiBean();
            tongJiBean.setArea("广西");
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

    private ArrayList<OnLineBean> getonline(){
        ArrayList<OnLineBean> onLineBeanList=new ArrayList<OnLineBean>();
        Mysqldb mdb = new Mysqldb();
        try
        {
            String str ="select area,areacode from organization where (areacode like '%00000000') and (areacode not LIKE '%0000000000') order by areacode";
            ResultSet rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                OnLineBean onLineBean=new OnLineBean();
                onLineBean.setArea(rs.getString("area"));
                onLineBean.setAreacode(rs.getString("areacode"));
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
            int total=0;
            int onlinenum=0;
            for (Iterator i$ = onLineBeanList.iterator(); i$.hasNext();)
            {
                OnLineBean onLineBean = (OnLineBean)i$.next();
                String s=onLineBean.getArea().replace("广西","");
                s=s.replace("市公安局","");
                onLineBean.setArea(s);
                total=total+onLineBean.getTotalnum();
                onlinenum=onlinenum+onLineBean.getOnlinenum();
            }
            OnLineBean onLineBean =new OnLineBean();
            onLineBean.setArea("广西");
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
