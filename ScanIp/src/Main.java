import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/5/3 0003.
 */
public class Main {
    public static void main(String[] args) {
       Timer timer = new Timer("zzh");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               //ɨ���ѷ���IP�Ƿ�����
                Date date1=new Date();
                 IpScanner ipScanner1 = new IpScanner();
                ipScanner1.scandiscoveryip();
                //ɨ���ѷ��ֶ˿��Ƿ�����
               // PortScanner portScanner=new PortScanner();
              //  portScanner.scandiscoveryport();
                Date date2=new Date();
                long l=date2.getTime()-date1.getTime();
                DecimalFormat a = new DecimalFormat("0.0");
                l=(l/60000);
                String s=(a.format(l));
                System.out.println("Scan complete,spent "+s+" minute.");
                 }
        }, 3000, 1000*60*Init.scanIpinterval);
      //ͳ�Ƹ��н���
     //   Statistics statistics=new Statistics();
     //   statistics.dishi();*/
     //   while(true) {
      //       IpScanner ipScanner1 = new IpScanner();
            //ɨ������ѷ����IP
            //ipScanner1.scandishidiv();
            //ɨ���ѷ���IP�ĳ��ö˿�
            //PortScanner portScanner = new PortScanner();
            //portScanner.scancommonport();
            //ɨ�������ѷ����IP
     //         ipScanner1.scanqutingdiv();
     //   }
     //   IpScanner ipScanner=new IpScanner();
     //   Ipalgorithm ipalgorithm =new Ipalgorithm();
    //    String areacode=ipalgorithm.getIpAreacode("45.27.23.1");
    //    System.out.println(areacode);
   //   ipScanner.scan("45.47.0.1","45.47.255.255");
      /*IpScanner ipScanner1 = new IpScanner();
        String ipstart=args[0];
        String ipend=args[1];
        ipScanner1.scan(ipstart,ipend);*/
        //ɨ���ѷ���IP�ĳ��ö˿�
       // PortScanner portScanner = new PortScanner();
       // portScanner.scancommonport();
   }
}
