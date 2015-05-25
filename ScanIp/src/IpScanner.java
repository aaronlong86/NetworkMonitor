import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;

/**
 * User:superman
 * Date:2014/9/7
 * Time:11:52
 */
/**
 * @author Administrator
 *
 */
public class IpScanner {
    private int corePoolSize=50;
    private int maximumPoolSize=100;
    private long keepAliveTime=5000;
    private BlockingDeque<Runnable> workQueue=new LinkedBlockingDeque<Runnable>();
    private ExecutorService threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,
            TimeUnit.MILLISECONDS,workQueue);
    private enum AdministrativeLevels{quting,dishi}
    private void scan(String start,String end,String areacode){
        if(!Ipalgorithm.validate(start)||!Ipalgorithm.validate(end)){
            System.err.println("不是有效的IP地址。");
            return;
        }
        int[] first ={Integer.valueOf(start.split("\\.")[0]),Integer.valueOf(start.split("\\.")[1]),
                Integer.valueOf(start.split("\\.")[2]),Integer.valueOf(start.split("\\.")[3])};
        int[] last ={Integer.valueOf(end.split("\\.")[0]),Integer.valueOf(end.split("\\.")[1]),
                Integer.valueOf(end.split("\\.")[2]),Integer.valueOf(end.split("\\.")[3])};
        int i=0;
        while(i<3)
        {
            if(first[i]<last[i]){ break;}
            if (first[i]==last[i]){i++;}else {{System.err.println("IP段表达错误。"); return;}}
        }
        //遍历所有IP
        for (int A = first[0]; A <= last[0]; A++) {
            for (int B = (A == first[0] ? first[1] : 0); B <= (A == last[0] ? last[1]
                    : 255); B++) {
                for (int C = (B == first[1] ? first[2] : 0); C <= (B == last[1] ? last[2]
                        : 255); C++) {
                    for (int D = (C == first[2] ? first[3] : 0); D <= (C == last[2] ? last[3]
                            : 255); D++) {
                        String ipStr = new String(A + "." + B + "." + C + "." + D);
                        SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String s =Ipalgorithm.percentformat(Ipalgorithm.ipamount(start,ipStr),Ipalgorithm.ipamount(start, end));
                        System.out.println(workQueue.size()+"、Testing:"+ipStr+"  Time:"+disctime.format(new Date())+",complete:"+s);
                        while(workQueue.size()>maximumPoolSize){
                            try {
//                              System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        threadPoolExecutor.execute(new IpScanWorker(ipStr,areacode));
                    }
                }
            }
        }
        threadPoolExecutor.shutdown();
        while (!threadPoolExecutor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("scan:"+start+"---"+end+",is complete!");
    }
    //扫描本IP段所有IP，一般手工测试用
    public void scan(String start,String end){
        if(!Ipalgorithm.validate(start)||!Ipalgorithm.validate(end)){
            System.err.println("不是有效的IP地址。");
            return;
        }
        Ipalgorithm al =new Ipalgorithm();
        int[] first ={Integer.valueOf(start.split("\\.")[0]),Integer.valueOf(start.split("\\.")[1]),
                Integer.valueOf(start.split("\\.")[2]),Integer.valueOf(start.split("\\.")[3])};
        int[] last ={Integer.valueOf(end.split("\\.")[0]),Integer.valueOf(end.split("\\.")[1]),
                Integer.valueOf(end.split("\\.")[2]),Integer.valueOf(end.split("\\.")[3])};
        int i=0;
        while(i<3)
        {
            if(first[i]<last[i]){ break;}
            if (first[i]==last[i]){i++;}else {{System.err.println("IP段表达错误。"); return;}}
        }
        //遍历所有IP
        for (int A = first[0]; A <= last[0]; A++) {
            for (int B = (A == first[0] ? first[1] : 0); B <= (A == last[0] ? last[1]
                    : 255); B++) {
                for (int C = (B == first[1] ? first[2] : 0); C <= (B == last[1] ? last[2]
                        : 255); C++) {
                    for (int D = (C == first[2] ? first[3] : 0); D <= (C == last[2] ? last[3]
                            : 255); D++) {
                        String ipStr = new String(A + "." + B + "." + C + "." + D);
                        String areacode=al.getIpAreacode(ipStr);
                        SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String s =Ipalgorithm.percentformat(Ipalgorithm.ipamount(start,ipStr),Ipalgorithm.ipamount(start, end));
                        System.out.println("Testing:"+ipStr+"  Time:"+disctime.format(new Date())+",complete:"+s);
                        while(workQueue.size()>maximumPoolSize){
                            try {
//                              System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        threadPoolExecutor.execute(new IpScanWorker(ipStr,areacode));
                    }
                }
            }
        }
        threadPoolExecutor.shutdown();
        while (!threadPoolExecutor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("scan:"+start+"---"+end+",is complete!");
    }


    private void scan(ArrayList<String> iplist){
        //遍历所有IP
        for (int i=0;i<(iplist.size()-1);i++)
        {

            if (Ipalgorithm.validate(iplist.get(i)))
            {   String ipStr = iplist.get(i);
                String areacode=iplist.get(i+1);
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s =Ipalgorithm.percentformat(i+2,iplist.size());
                System.out.println(workQueue.size()+"、"+(i/2+1)+"、Testing:"+ipStr+"  Time:"+disctime.format(new Date())+",complete:"+s);
                while(workQueue.size()>maximumPoolSize){
             try {
//                System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                      Thread.sleep(500);
                  } catch (InterruptedException e) {
                  e.printStackTrace();
                   }
              }
            threadPoolExecutor.execute(new IpScanWorker(ipStr,areacode));
            i=i+1;
          }
        }
        threadPoolExecutor.shutdown();
        while (!threadPoolExecutor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("scan iplist complete!");
    }
    //扫描已发现的IP
    public void scandiscoveryip(){
        Mysqldb mdb = new Mysqldb();
        ArrayList<String> ipstr = mdb.getDiscoveryIp("");
        mdb.close();
        scan(ipstr);
    }
    public void scandishidiv(){
        //扫描地市已分配的ip段
        ArrayList<String> ipstr = getIpSegment(AdministrativeLevels.dishi);
        int amount = Ipalgorithm.totalipamount(ipstr);
        System.out.println("需扫描的IP总数：" + amount);
        int sum = 0;
        for (int i = 0; i < (ipstr.size() - 1); i++) {
            System.out.println("开始扫描IP:" + ipstr.get(i) + "---" + ipstr.get(i + 1));
            IpScanner ipScanner = new IpScanner();
            ipScanner.scan(ipstr.get(i), ipstr.get(i + 1), ipstr.get(i + 2));
            sum = sum + Ipalgorithm.ipamount(ipstr.get(i), ipstr.get(i + 1));
            System.out.println("已经扫描:" + Ipalgorithm.percentformat(sum, amount) + "," + sum + "/" + amount);
            i = i + 2;
        }
    }
    //扫描区厅已分配的ip段
    public void scanqutingdiv(){
        ArrayList<String> ipstr = getIpSegment(AdministrativeLevels.quting);
        int amount = Ipalgorithm.totalipamount(ipstr);
        System.out.println("需扫描的IP总数：" + amount);
        int sum = 0;
        for (int i = 0; i < (ipstr.size() - 1); i++) {
            System.out.println("开始扫描IP:" + ipstr.get(i) + "---" + ipstr.get(i + 1));
            IpScanner ipScanner2 = new IpScanner();
            ipScanner2.scan(ipstr.get(i), ipstr.get(i + 1), ipstr.get(i + 2));
            sum = sum + Ipalgorithm.ipamount(ipstr.get(i), ipstr.get(i + 1));
            System.out.println("已经扫描:" + Ipalgorithm.percentformat(sum, amount) + "," + sum + "/" + amount);
            i = i + 2;
        }
    }

    //获取（区厅或地市）分配的所有（某）IP段
    private ArrayList<String> getIpSegment(AdministrativeLevels adlevel) {
        ArrayList<String> iplist=new ArrayList<String>();
        Mysqldb mdb = new Mysqldb();
        try
        { 	String sqlstr = "";
            switch (adlevel){
                case quting:sqlstr="select ipstart,ipend,areacode from ipsegment where areacode like \"%00000000\" order by ipstart";
                    break;
                case dishi:sqlstr="select ipstart,ipend,areacode from ipsegment where areacode not like \"%00000000\" order by ipstart";
                    break;
                default:System.out.println("获取IP地址函数参数错误。");
            }

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            while (rs.next())
            {
                iplist.add(rs.getString("ipstart"));
                iplist.add(rs.getString("ipend"));
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

}