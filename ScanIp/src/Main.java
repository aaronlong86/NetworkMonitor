import java.text.DecimalFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/5/3 0003.
 */
public class Main {
    public static void main(String[] args) {
      /* Timer timer = new Timer("zzh");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               //扫描已发现IP是否在线
                Date date1=new Date();
                 IpScanner ipScanner1 = new IpScanner();
                ipScanner1.scandiscoveryip();
                //扫描已发现端口是否在线
                PortScanner portScanner=new PortScanner();
                portScanner.scandiscoveryport();
                Date date2=new Date();
                long l=date2.getTime()-date1.getTime();
                DecimalFormat a = new DecimalFormat("0.0");
                l=(l/60000);
                String s=(a.format(l));
                System.out.println("Scan complete,spent "+s+" minute.");
                 }

        }, 3000, 1000*60*Init.scanPortinterval);
      //统计各市进度
     //   Statistics statistics=new Statistics();
     //   statistics.dishi();
         while(true) {
            IpScanner ipScanner1 = new IpScanner();
            //扫描地市已分配的IP
            ipScanner1.scandishidiv();
            //扫描已发现IP的常用端口
            PortScanner portScanner = new PortScanner();
            portScanner.scancommonport();
            //扫描区厅已分配的IP
            ipScanner1.scanqutingdiv();
        }*/
      //  IpScanner ipScanner1 = new IpScanner();
      //  ipScanner1.scan("172.52.0.0","172.52.255.255");
        //扫描已发现IP的常用端口
        Date date1=new Date();
       // PortScanner portScanner = new PortScanner();
       // portScanner.scancommonport();
          IpScanner ipScanner1 = new IpScanner();
          ipScanner1.scan("45.20.0.1","45.23.255.255");
        Date date2=new Date();
        long l=date2.getTime()-date1.getTime();
        DecimalFormat a = new DecimalFormat("0.0");
        l=(l/60000);
        String s=(a.format(l));
        System.out.println("Scan complete,spent "+s+" minute.");
        //test vcs
   }
}
