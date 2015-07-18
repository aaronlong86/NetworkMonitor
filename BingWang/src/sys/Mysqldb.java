package sys;

import java.sql.*;


public class Mysqldb {
	private String url="jdbc:mysql://127.0.0.1:3306/bingwang";
	//public String url="jdbc:mysql://45.1.2.202:3306/bingwang?useUnicode=true&characterEncoding=utf8";
	private String user="root";
	private String pwd="aaronlong";
	//public String pwd="spjkyyzx2015";
	//������MySQL������
	private Connection conn = null;
	public Statement sql=null;

	public Mysqldb()  {
		try{
			//������������һ��Ҳ��дΪ��Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn =DriverManager.getConnection(url,user, pwd);
			sql = conn.createStatement();//��������������ִ��sql����
		}  catch (Exception ex){System.out.println("���ݿ����ʧ��");}
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

	//��ȡĳ(��λ)������IP��****���´�����δ����*****
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


	//��ȡĳ(��λ)������IP��
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
	//��ȡ(ĳ)IP��
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
	//��ȡĳ(��λ)�ģ�ĳ��IP��
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
  
  