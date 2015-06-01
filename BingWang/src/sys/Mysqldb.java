package sys;

import java.sql.*;


public class Mysqldb {
	//private String url="jdbc:mysql://127.0.0.1:3306/test";
	public String url="jdbc:mysql://45.1.2.202:3306/bingwang?useUnicode=true&characterEncoding=utf8";
	private String user="root";
	//private String pwd="aaronlong";
	public String pwd="spjkyyzx2015";
	//建立到MySQL的连接
	private Connection conn = null;
	public Statement sql=null;

	public Mysqldb()  {
		try{
			//加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn =DriverManager.getConnection(url,user, pwd);
			sql = conn.createStatement();//创建语句对象，用以执行sql语言
		}  catch (Exception ex){System.out.println("数据库加载失败");}
	}

	public void close()
	{
		try {
			sql.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//获取某(单位)的所有IP段****以下代码暂未启用*****
  /*  public ArrayList<String> getIpSegment() {
        ArrayList<String> ipseg=new ArrayList<String>();
        try
        { 	ResultSet rs = sql.executeQuery("select ipstart,ipend,areacode from ipsegment order by areacode");
            while (rs.next())
            {
                ipseg.add(rs.getString("ipstart"));
                ipseg.add(rs.getString("ipend"));
                ipseg.add(rs.getString("areacode"));
            }
            rs.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            return ipseg;
        }
        return ipseg;
    }


	//获取某(单位)的所有IP段
	public ArrayList<String> getIpSegment(String areacode) {
		ArrayList<String> ip=new ArrayList<String>();
		try
		{ 	ResultSet rs = sql.executeQuery("select ipstart,ipend,areacode from ipsegment where areacode="+areacode+" order by ipstart");
			while (rs.next())
			{
				ip.add(rs.getString("ipstart"));
				ip.add(rs.getString("ipend"));
				ip.add(rs.getString("areacode"));
			}
			rs.close();
		}
		catch (Exception ex)
		{
			System.out.println("Error : " + ex.toString());
			return ip;
		}
		return ip;
	}
	//获取(某)IP段
	public ArrayList<String> getIpSegment(int ipseg) {
		ArrayList<String> ip=new ArrayList<String>();
		try
		{ 	ResultSet rs = sql.executeQuery("select ipstart,ipend,areacode from ipsegment where ipstart like "
					+"\""+Integer.toString(ipseg)+"%\""+" order by ipstart");
			while (rs.next())
			{
				ip.add(rs.getString("ipstart"));
				ip.add(rs.getString("ipend"));
				ip.add(rs.getString("areacode"));
			}
			rs.close();
		}
		catch (Exception ex)
		{
			System.out.println("Error : " + ex.toString());
			return ip;
		}
		return ip;
	}
	//获取某(单位)的（某）IP段
	public ArrayList<String> getIpSegment(String areacode,int ipseg) {
		ArrayList<String> ip=new ArrayList<String>();
		try
		{ 	ResultSet rs = sql.executeQuery("select ipstart,ipend,areacode from ipsegment where areacode="
			+areacode+" and ipstart like "+"\""+Integer.toString(ipseg)+"%\""+" order by ipstart");
			while (rs.next())
			{
				ip.add(rs.getString("ipstart"));
				ip.add(rs.getString("ipend"));
				ip.add(rs.getString("areacode"));
			}
			rs.close();
		}
		catch (Exception ex)
		{
			System.out.println("Error : " + ex.toString());
			return ip;
		}
		return ip;
	}
*/

}
  
  