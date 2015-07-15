import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Mysqldb {
	//private String url="jdbc:mysql://127.0.0.1:3306/test";
	public String url="jdbc:mysql://45.1.2.202:3306/bingwang?useUnicode=true&characterEncoding=utf8";
	private String user="root";
	//private String pwd="aaronlong";
	public String pwd="spjkyyzx2015";
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
	//��ȡĳ(��λ)�ѷ��ֵ�����IP
	public ArrayList<String> getDiscoveryIp(String areacode) {
		ArrayList<String> iplist=new ArrayList<String>();
		Mysqldb mdb = new Mysqldb();
		try
		{ 	String sqlstr ="";
			if (areacode==""){sqlstr="select ip,areacode from ipdiscovery where flag=1 order by ip";}
			else {sqlstr="select ip,areacode from ipdiscovery where (flag=1) and (areacode="
					+areacode+") order by ip";}
			ResultSet rs = mdb.sql.executeQuery(sqlstr);
			while (rs.next())
			{
				iplist.add(rs.getString("ip"));
				iplist.add(rs.getString("areacode"));
			}
			rs.close();
			mdb.close();
		}
		catch (Exception ex)
		{
			System.out.println("Error : " + ex.toString());
			mdb.close();
			return iplist;
		}
		return iplist;
	}
	//��ȡ����IP��
	public ArrayList<String> getIpSegment() {
		ArrayList<String> ipseg=new ArrayList<String>();
		try
		{ 	ResultSet rs = sql.executeQuery("select ipstart,ipend,areacode from ipsegment where flag=1 order by areacode");
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

	/*  ****���´�����δ����*****
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
  
  