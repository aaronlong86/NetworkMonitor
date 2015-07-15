import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/4/20 0020.
 */
public class Ipalgorithm {
    private ArrayList<String> allipseg=new ArrayList<String>();
    public Ipalgorithm(){
        Mysqldb mdb =new Mysqldb();
        try
        { 	ResultSet rs = mdb.sql.executeQuery("select ipstart,ipend,areacode from ipsegment WHERE flag=1 order by areacode");
            while (rs.next()) {
                this.allipseg.add(rs.getString("ipstart"));
                this.allipseg.add(rs.getString("ipend"));
                this.allipseg.add(rs.getString("areacode"));
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
        }
    };
    public static int ipamount(String start, String end) {
        int sum = 0;
        if(!validate(start)||!validate(end)){
            System.err.println("不是有效的IP地址。"+start);
            return sum;
        }
        int[] first = {Integer.valueOf(start.split("\\.")[0]), Integer.valueOf(start.split("\\.")[1]),
                Integer.valueOf(start.split("\\.")[2]), Integer.valueOf(start.split("\\.")[3])};
        int[] last = {Integer.valueOf(end.split("\\.")[0]), Integer.valueOf(end.split("\\.")[1]),
                Integer.valueOf(end.split("\\.")[2]), Integer.valueOf(end.split("\\.")[3])};
        int i=0;
        while(i<3)
        {
            if(first[i]<last[i]){ break;}
            if (first[i]==last[i]){i++;}else {{System.err.println("IP段表达错误。"+end); return sum;}}
        }
        sum=(last[0]-first[0])*255*255*255+(last[1]-first[1])*255*255+(last[2]-first[2])*255+(last[3]-first[3])+1;
        return  sum;
    }
    public static int totalipamount(ArrayList<String> ipstr)
    {  int sum=0;
        for (int i=0;i<(ipstr.size()-1);i++)
    {  sum=ipamount(ipstr.get(i),ipstr.get(i+1))+sum;
        i=i+2;
    }
        return sum;
    }
    public static boolean validate(String ip){
        if(ip==null||ip.length()==0) return false;
        String[] array=ip.split("\\.");
        if(array.length!=4) return false;
        for(String str:array){
            try {
                int num=Integer.valueOf(str);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
    public static boolean ipIsValid(String ipSection, String ip) {
        /*if (ipIsValid("45.1.1.1-45.61.1.100", "45.36.1.54")) {
            System.out.println("ip属于该网段");
        } else
            System.out.println("ip不属于该网段"); */
        if (ipSection == null)
            throw new NullPointerException("IP段不能为空!");
        if (ip == null)
            throw new NullPointerException("IP不能为空！");
        ipSection = ipSection.trim();
        ip = ip.trim();
        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
        if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP))
            return false;
        int idx = ipSection.indexOf('-');
        String[] sips = ipSection.substring(0, idx).split("\\.");
        String[] sipe = ipSection.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }

    public static String percentformat(int numerator,int denominator){
        double perc=(double)numerator/denominator;
        DecimalFormat a = new DecimalFormat("#%");
        String s=a.format(perc);
        return s;
    }
    //获取某IP所属的单位编码
    public String getIpAreacode(String ip) {
        String areacode="450000000000";
        if(!Ipalgorithm.validate(ip)){
            System.err.println("不是有效的IP地址。");
            return areacode;
        }
        for (int i=0;i<(this.allipseg.size()-1);i++)
        {
            if (ipIsValid(this.allipseg.get(i)+"-"+this.allipseg.get(i+1),ip)){
                areacode=this.allipseg.get(i+2);
            }
            i=i+2;
        }
        return areacode;
    }

}
