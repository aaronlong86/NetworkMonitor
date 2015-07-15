import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2015/7/15 0015.
 */
public class SnmpFunction {
    /**
     * 获取网络设备信息的公共函数
     * @param ip
     * @param stOID
     * @return list
     */
    public static List getSNMPList(String ip,String stOID) {
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setVersion(SnmpConstants.version2c);
        target.setAddress(new UdpAddress(ip + "/161"));
        target.setTimeout(5000);    //3s
        target.setRetries(1);
        List list = null;
        try {
            //Example for Sending an Asynchronous Message
            DefaultUdpTransportMapping udpTransportMap = new DefaultUdpTransportMapping();
            //udpTransportMap.listen();
            Snmp snmp = new Snmp(udpTransportMap);
            snmp.listen();

            PDUFactory pf = new DefaultPDUFactory(PDU.GET);
            TableUtils tu = new TableUtils(snmp, pf);
            OID[] columns = new OID[1];
            columns[0] = new VariableBinding(new OID(stOID)).getOid();
            list = tu.getTable(target, columns, null, null);
            snmp.close();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
