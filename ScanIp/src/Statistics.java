import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/3 0003.
 */
public class Statistics {

    private ArrayList<String> getdishiipnumber(int ipseg){
        String str="SELECT COUNT(*),t2.area,t2.dishicode FROM (SELECT t1.area,"+
                " SUBSTRING(t1.areacode, 1, 4) AS dishicode FROM organization t1"+
                " WHERE t1.areacode LIKE \'%00000000\' AND t1.areacode NOT LIKE \'%0000000000\')"
                +" t2, ipdiscovery t3 WHERE     SUBSTRING(t3.areacode,1,4) = t2.dishicode "
                +"AND t3.ip LIKE "+"\""+Integer.toString(ipseg)+"%\""+" GROUP BY t2.dishicode ORDER BY t2.dishicode";
        ArrayList<String> completetable=new ArrayList<String>();
        Mysqldb mdb = new Mysqldb();
        try
        { 	ResultSet rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                completetable.add(rs.getString("COUNT(*)"));
                completetable.add(rs.getString("t2.area"));
                completetable.add(rs.getString("t2.dishicode"));
            }
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
    //统计发现的各市IP
   public void dishi(){
       ArrayList<String> ipstr = getdishiipnumber(45);
       for (int i = 0; i < (ipstr.size() - 1); i++) {
           System.out.println(i / 3 + ":" + ipstr.get(i + 1) + "," + ipstr.get(i) + "," + ipstr.get(i + 2));
           i = i + 2;
       }
       ipstr = getdishiipnumber(172);
       for (int i = 0; i < (ipstr.size() - 1); i++) {
           System.out.println(i / 3 + ":" + ipstr.get(i + 1) + "," + ipstr.get(i) + "," + ipstr.get(i + 2));
           i = i + 2;
       }
   }

}
