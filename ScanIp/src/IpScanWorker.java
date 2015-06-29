import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/26 0026.
 */
public class IpScanWorker implements Runnable{

    private String ipStr;
    private String areacode;
    public IpScanWorker(String ipStr,String areacode){
        this.ipStr=ipStr;
        this.areacode=areacode;
    }
    @Override
    public void run() {
        try {
            InetAddress inetAddress=InetAddress.getByName(ipStr);
         //   GetMacAddress gmac = new GetMacAddress();
            if(inetAddress.isReachable(5000)) { // wait 5 seconds
                String ip = inetAddress.getHostAddress();
                String hostname = inetAddress.getHostName();
                String mac = GetMacAddress.getMacAddress(ipStr);
                //String mac=GetMacAddress.getMacInLinux(ipStr).trim();
                System.out.println("DiscoverIp:HostName:" + hostname + ",Ip:" + ip + ",Mac:" + mac);
                InsertDiscoverIp(ip, hostname, mac, areacode);
            }
        } catch (UnknownHostException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
    //如果是已发现的地市分配IP就更新机构代码和最后发现时间，如果是新发现的就插入
    private boolean InsertDiscoverIp(String ip,String hostname,String mac,String areacode)
    { boolean b=false;
        Mysqldb mdb = new Mysqldb();
        try
        {	ResultSet rs = mdb.sql.executeQuery("select idipdiscoverys from ipdiscovery where ip=\'"+ip+"\'");
            SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String 	str="UPDATE ipdiscovery SET flag=1,areacode=\'"+areacode+"\'"
                    +",discoverylasttime=\'"+ Timestamp.valueOf(disctime.format(new Date()))
                    +"\' WHERE ip=\'"+ip+"\'";
            if (rs.next()){
                if (areacode.substring(4).equals("00000000")){
                    str="UPDATE ipdiscovery SET flag=1,discoverylasttime=\'"
                    +Timestamp.valueOf(disctime.format(new Date()))+"\' WHERE ip=\'"+ip+"\'";}
                System.out.println("ip exist,update:"+ip+"was found discoverylasttime.");
            }else {
                str="insert into ipdiscovery (ip,mac,hostname,discoveryfirsttime,areacode) values(\'"
                        + ip + "\',\'" + mac + "\',\'" + hostname + "\',\'" + Timestamp.valueOf(disctime.format(new Date()))
                        + "\',\'" + areacode + "\')";
                System.out.println("new ip,inserted:" + ip + " into mydb.");			                          }
            mdb.sql.executeUpdate(str);
            b=true;
            mdb.close();
        }catch (Exception ex)
        {System.out.println(ex.toString());mdb.close();}
        return b;
    }
}

