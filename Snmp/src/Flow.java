import java.io.IOException;
import java.net.InetAddress;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import org.snmp4j.AbstractTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.TableEvent;

//统计流量的类，取时间与流量
public class Flow {
    private enum FlowType {get,send};
    private String ip="192.168.2.128";
    public void multiThread(){

        for (FlowType flowType:FlowType.values())
        {
            new Thread(new GetFlow(ip,flowType)).start();
        }
/*
        GetFlow getFlow1 = new GetFlow(ip,FlowType.get);
        GetFlow getFlow2 = new GetFlow(ip,FlowType.send);
        Thread t1 = new Thread(getFlow1);
        Thread t2 = new Thread(getFlow2);
        t1.start();
        t2.start();*/
    }

    private class GetFlow implements Runnable{
        String getwalkFlowOid=".1.3.6.1.2.1.2.2.1.10";
        String sendwalkFlowOid=".1.3.6.1.2.1.2.2.1.16";
        private String walkFlowOid;
        private String ipAddress;
        GetFlow(String ipAddress,FlowType flowType){
            this.ipAddress=ipAddress;
            if (flowType==FlowType.get){this.walkFlowOid=getwalkFlowOid;}
            if (flowType==FlowType.send){this.walkFlowOid=sendwalkFlowOid;}
        }
      public void run() {
        List list = SnmpFunction.getSNMPList(ipAddress,walkFlowOid);
        for (int i = 0; i < list.size(); i++) {
            TableEvent te = (TableEvent) list.get(i);
            VariableBinding[] vb = te.getColumns();
            if (vb == null) {
                return;
            } else {
                for (int j = 0; j < vb.length; j++) {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String NowTime = sdf.format(c.getTime());// 当前时间
                    if (walkFlowOid==getwalkFlowOid) {
                        System.out.println(NowTime + "端口" + Integer.valueOf(i + 1).toString() + "下载网速:"
                                + vb[j].getOid().toString() + "=" + calc(ipAddress, vb[j].getOid().toString()));
                    }
                    if (walkFlowOid==sendwalkFlowOid) {
                        System.out.println(NowTime + "端口" + Integer.valueOf(i + 1).toString() + "上传网速:"
                                + vb[j].getOid().toString() + "=" + calc(ipAddress, vb[j].getOid().toString()));
                    }
                }
            }
        }
    }
    // 计算端口流量
    @SuppressWarnings("unchecked")
      private long calc(String IpAddress, String FlowOid) {
        long FlowValue = 0;
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();// 监听

            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("public"));// 设置共同体名
            Address targetAddress = GenericAddress.parse("udp:" + IpAddress
                    + "/161");
            target.setAddress(targetAddress);// 设置目标Agent地址
            target.setRetries(2);// 重试次数
            target.setTimeout(3000);// 超时设置
            target.setVersion(1);// 版本

            PDU request = new PDU();
            request.setType(PDU.GET);// 操作类型GET
            request.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.3.0")));// OID_sysUpTime
            request.add(new VariableBinding(new OID(FlowOid)));
            // 取两次数据，间隔10秒，算差值
            long[] time = new long[2];
            long[] flow = new long[2];
            for (int i = 0; i < 2; i++) {
                ResponseEvent respEvt = snmp.send(request, target);// 发送请求
                if (respEvt != null && respEvt.getResponse() != null) {
                    // 从目的设备取值，得到Vector
                    Vector<VariableBinding> revBindings = (Vector<VariableBinding>) respEvt.getResponse().
                            getVariableBindings();
                    String TimeTicks = revBindings.elementAt(0)
                            .getVariable().toString().trim();
                    String[] TimeString = TimeTicks.split(" ");// 得到时间字符串数组
                    // 取时间 186 days, 21:26:15.24，也有可能没有day，就是不到一天
                    if(TimeTicks.contains("day")){
                        time[i] = Long.parseLong(TimeString[0])
                                * 24
                                * 3600
                                + Long.parseLong(TimeString[2].split(":")[0])
                                * 3600
                                + Long.parseLong(TimeString[2].split(":")[1])
                                * 60
                                + Math.round(Double.parseDouble(TimeString[2]
                                .split(":")[2]));
                    }else{
                        time[i] = Long.parseLong(TimeString[0].split(":")[0])
                                * 3600
                                + Long.parseLong(TimeString[0].split(":")[1])
                                * 60
                                + Math.round(Double.parseDouble(TimeString[0]
                                .split(":")[2]));
                    }
                    // 取端口流量
                    flow[i] = Long.parseLong(revBindings.elementAt(1).getVariable().toString());
                }
                if (i == 0)
                    Thread.sleep(5000);// 延时5秒后，第二次取值
            }
            snmp.close();
            transport.close();
            // 计算并为时间和最终流量赋值
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String NowTime = sdf.format(c.getTime());// 当前时间
            long AllSubValue = 0;
            long sub = flow[1] - flow[0];
 /*
   * 端口流量值为无符号32位，超出后就归0，所以如果两次取值差值为负，
  * 必然出现一次归0的情况，由于单个端口的流量不可能超过每5秒1*2^32字节
  */
                if (sub < 0) {
                    // 因为端口流量为无符号32位，所以最大值是有符号32位的两倍
                    sub += 2L * Integer.MAX_VALUE;
                }
                AllSubValue += sub;

            if (time[1] - time[0] != 0) {
                // 字节换算成KB才是最终流量
                FlowValue = (long)((AllSubValue/1024.0)/ (time[1] - time[0]));
            } else {
                System.out.println("地址：" + IpAddress + "数据采集失败！");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return FlowValue;
    }
  }
}