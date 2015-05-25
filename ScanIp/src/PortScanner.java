import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/5/1 0001.
 */
public class PortScanner {
    private int corePoolSize=50;
    private int maximumPoolSize=100;
    private long keepAliveTime=5000;
    private BlockingDeque<Runnable> workQueue=new LinkedBlockingDeque<Runnable>();
    private ExecutorService threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,
            TimeUnit.MILLISECONDS,workQueue);
    public PortScanner(){

    }
    //扫描已发现的所有端口
    public void scandiscoveryport(){
        ArrayList<String> discoveryportlist=getdiscoveryport();
        //遍历所有IP
        for (int i=0;i<(discoveryportlist.size()-1);i++)
        {   String ipStr = discoveryportlist.get(i);
            if (Ipalgorithm.validate(ipStr)) {
                int port =Integer.valueOf(discoveryportlist.get(i+1));
                String areacode = discoveryportlist.get(i + 2);
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s = Ipalgorithm.percentformat(i + 3, discoveryportlist.size());
                System.out.println((i/3 + 1) + "、Scaning port:" + ipStr + "  Time:" + disctime.format(new Date()) + ",complete:" + s);
                while (workQueue.size() > maximumPoolSize) {
                    try {
//                System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    threadPoolExecutor.execute(new PortScanWorker(ipStr,port,areacode));
                    System.out.println("port:"+port);
                i=i+2;
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
        System.out.println("scan port of discoveryportlist complete!");
    }
    //扫描已发现的所有IP的常用端口
    public void scancommonport(){
        Mysqldb mdb = new Mysqldb();
        ArrayList<String> iplist=mdb.getDiscoveryIp("");
        mdb.close();
        ArrayList<String> commonportlist=getcommonport();
        //遍历所有IP
        for (int i=0;i<(iplist.size()-1);i++)
        {
            if (Ipalgorithm.validate(iplist.get(i))) {
                String ipStr = iplist.get(i);
                String areacode = iplist.get(i + 1);
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s = Ipalgorithm.percentformat(i + 2, iplist.size());
                System.out.println((i / 2 + 1) + "、Scaning port:" + ipStr + "  Time:" + disctime.format(new Date()) + ",complete:" + s);
                for (int j = 0; j<(commonportlist.size()-1); j++) {
                    int port =Integer.valueOf(commonportlist.get(j));
                    while (workQueue.size() > maximumPoolSize) {
                        try {
//                System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    threadPoolExecutor.execute(new PortScanWorker(ipStr,port,areacode));
                    System.out.println(workQueue.size()+"、port:"+port);
                }
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
        System.out.println("scan port of commonportlist complete!");
    }
//扫描本IP段常用端口，一般手工测试用
    public void scancommonport(String start,String end){
        ArrayList<String> commonportlist=getcommonport();
        if(!Ipalgorithm.validate(start)||!Ipalgorithm.validate(end)){
            System.err.println("不是有效的IP地址。");
            return;
        }
        Ipalgorithm al=new Ipalgorithm();//这里加载数据库中的IP段
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
                        System.out.println("Scaning port:"+ipStr+"  Time:"+disctime.format(new Date())+",complete:"+s);
                        String areacode=al.getIpAreacode(ipStr);
                        for (int j = 0; j<(commonportlist.size()-1); j++) {
                            int port =Integer.valueOf(commonportlist.get(j));
                            while (workQueue.size() > maximumPoolSize) {
                                try {
//                System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            threadPoolExecutor.execute(new PortScanWorker(ipStr, port, areacode));
                            System.out.println("port:"+port);
                        }
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
        System.out.println("scan all port of:"+start+"---"+end+",is complete!");
    }
//扫描已发现的所有IP的所有端口
    public void scanallport(){
        Mysqldb mdb = new Mysqldb();
        ArrayList<String> iplist=mdb.getDiscoveryIp("");
        mdb.close();
        //遍历所有IP
        for (int i=0;i<(iplist.size()-1);i++)
        {
            if (Ipalgorithm.validate(iplist.get(i))) {
                String ipStr = iplist.get(i);
                String areacode = iplist.get(i + 1);
                SimpleDateFormat disctime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String s = Ipalgorithm.percentformat(i + 2, iplist.size());
                System.out.println((i / 2 + 1) + "、Scaning port:" + ipStr + "  Time:" + disctime.format(new Date()) + ",complete:" + s);
                for (int j = 0; j < 65536; j++) {
                    while (workQueue.size() > maximumPoolSize) {
                        try {
//                System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    threadPoolExecutor.execute(new PortScanWorker(ipStr,j,areacode));
                    System.out.println("port:"+j);
                }
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
        System.out.println("scan all port of iplist complete!");
    }
    //扫描本IP段所有端口，一般手工测试用
    public void scanallport(String start,String end){
        if(!Ipalgorithm.validate(start)||!Ipalgorithm.validate(end)){
               System.err.println("不是有效的IP地址。");
            return;
        }
        Ipalgorithm al=new Ipalgorithm();//这里加载数据库中的IP段
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
                        System.out.println("Scaning port:"+ipStr+"  Time:"+disctime.format(new Date())+",complete:"+s);
                        String areacode=al.getIpAreacode(ipStr);
                        for (int j = 0; j < 65536; j++) {
                            while(workQueue.size()>maximumPoolSize){
                                try {
//                              System.out.println("Thread queue is too long,sleep 500 milliseconds.");
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            threadPoolExecutor.execute(new PortScanWorker(ipStr,j,areacode));
                            System.out.println("port:" + j+"  ,Time:"+disctime.format(new Date()));
                        }
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
        System.out.println("scan all port of:"+start+"---"+end+",is complete!");
    }
    //获取已发现的端口列表
    private ArrayList<String> getdiscoveryport() {
        ArrayList<String> discoveryportlist=new ArrayList<String>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String str="select ip,port,areacode from portdiscovery order by ip";
            ResultSet rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                discoveryportlist.add(rs.getString("ip"));
                discoveryportlist.add(rs.getString("port"));
                discoveryportlist.add(rs.getString("areacode"));
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return discoveryportlist;
        }
        return discoveryportlist;
    }
    //获取常用的端口列表
    private ArrayList<String> getcommonport() {
        ArrayList<String> commonportlist=new ArrayList<String>();
        Mysqldb mdb =new Mysqldb();
        try
        { 	String str="select port from commonport order by port";
            ResultSet rs = mdb.sql.executeQuery(str);
            while (rs.next())
            {
                commonportlist.add(rs.getString("port"));
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return commonportlist;
        }
        return commonportlist;
    }

}
