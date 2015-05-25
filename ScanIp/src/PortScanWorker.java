import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/26 0026.
 */
public class PortScanWorker implements Runnable{
        private String ipStr;
        private int port;
        private String areacode;
        public PortScanWorker(String ipStr,int port,String areacode){
            this.ipStr=ipStr;
            this.port=port;
            this.areacode=areacode;
        }
        @Override
        public void run() {
            try {
                InetAddress inetAddress=InetAddress.getByName(ipStr);
                String ip = inetAddress.getHostAddress();
                Socket tcpsocket=new Socket(inetAddress,port);
                tcpsocket.close();
                System.out.println("DiscoverPort,Ip:" + ip + ",Port:" + port);
                InsertDiscoverPort(ip, port, areacode);
            } catch (UnknownHostException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

    public boolean InsertDiscoverPort(String ip,int port,String areacode)
    { boolean b=false;
        Mysqldb mdb = new Mysqldb();
        try
        {	ResultSet rs = mdb.sql.executeQuery("select idportdiscovery from portdiscovery where (ip=\'"
                +ip+"\') and (port="+Integer.toString(port)+");");
            SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String 	str="insert into portdiscovery (ip,port,discoveryfirsttime,areacode) values(\'"
                    + ip + "\'," + Integer.toString(port) + ",\'" + Timestamp.valueOf(disctime.format(new Date()))
                    + "\',\'" + areacode + "\')";
            if (rs.next()){
                str="UPDATE portdiscovery SET discoverylasttime=\'"
                        +Timestamp.valueOf(disctime.format(new Date()))+
                        "\',areacode=\'"+areacode+
                        "\' WHERE (ip=\'"+ip+"\') and (port="+Integer.toString(port)+");";
                System.out.println("port exist,update,ip:"+ip+",port:"+port+"was found discoverylasttime.");
            }else {
                System.out.println("new port,inserted,ip:" + ip +",port:"+port+ " into mydb.");}
            mdb.sql.executeUpdate(str);
            b=true;
            mdb.close();
        }catch (Exception ex)
        {System.out.println(ex.toString());mdb.close();}
        return b;
    }

    }


