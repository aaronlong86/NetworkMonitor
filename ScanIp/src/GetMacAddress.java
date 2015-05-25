import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * ��ȡMAC��ַ
 * @author
 * 2011-12
 */
public class GetMacAddress {

    public static String callCmd(String[] cmd) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);
            while ((line = br.readLine ()) != null) {
                result += line;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    // ��Դ��ַ��http://yijianfengvip.blog.163.com/blog/static/175273432201212295830661/

    /**
     *
     * @param cmd  ��һ������
     * @param another �ڶ�������
     * @return   �ڶ��������ִ�н��
     */
    public static String callCmd(String[] cmd,String[] another) {
        String result = "";
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor();  //�Ѿ�ִ�����һ�����׼��ִ�еڶ�������
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);
            while ((line = br.readLine ()) != null) {
                result += line;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    /**
     *
     * @param ip  Ŀ��ip,һ���ھ�������
     * @param sourceString ������Ľ���ַ���
     * @param macSeparator mac�ָ�����
     * @return  mac��ַ��������ķָ����ű�ʾ
     */
    public static String filterMacAddress(final String ip, final String sourceString,final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while(matcher.find()){
            result = matcher.group(1);
            if(sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break;  //����ж��IP,ֻƥ�䱾IP��Ӧ��Mac.
            }
        }

        return result;
    }



    /**
     *
     * @param ip Ŀ��ip
     * @return   Mac Address
     *
     */
    public static String getMacInWindows(final String ip){
        String result = "";
        String[] cmd = {
                "cmd",
                "/c",
                "ping " +  ip
        };
        String[] another = {
                "cmd",
                "/c",
                "arp -a"
        };

        String cmdResult = callCmd(cmd,another);
        result = filterMacAddress(ip,cmdResult,"-");

        return result;
    }
    //     ��Դ���ͣ�http://yijianfengvip.blog.163.com/blog/static/175273432201212295830661/

    /**
     *
     * @param ip Ŀ��ip 
     * @return   Mac Address 
     *
     */
    public static String getMacInLinux(final String ip){
        String result = "";
        String[] cmd = {
                "/bin/sh",
                "-c",
                "ping " +  ip + " -c 2 && arp -a"
        };
        String cmdResult = callCmd(cmd);
        result = filterMacAddress(ip,cmdResult,":");

        return result;
    }

    /**
     * ��ȡMAC��ַ
     * @return ����MAC��ַ
     */
    public static String getMacAddress(String ip){
        String macAddress = "";
        macAddress = getMacInWindows(ip).trim();
        if(macAddress==null||"".equals(macAddress)){
            macAddress = getMacInLinux(ip).trim();
        }
        return macAddress;
    }
    /* ���²���nbtstat���ߣ����ڲ��ԣ�����δ����
    public String getMACAddressByIp(String ip){
        String str = "";
        String macAddress = "";
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                if (str != null) {
                    if (str.indexOf("MAC Address") > 1) {
                        macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return macAddress;
    }*/

}