import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/15 0015.
 */
public class SnmpMac {
    public String switchIP;
 /*   public Map<Integer,String> getPortAFT(){
        Map<Integer,String> AFT=null;
        ArrayList<String> macSet=new ArrayList<String>();
        int maxRepetitions = 100;
        Snmp protocol=null;
        CommunityTarget target=null;
        Address targetAddress=null;
        TransportMapping transportProtocol=null;
        target=new CommunityTarget();
        targetAddress= GenericAddress.parse("udp:" + this.switchIP + "/161");
        target.setAddress(targetAddress);
        target.setCommunity(new OctetString("public"));
        target.setRetries(2);
        target.setTimeout(5000);
        target.setVersion(SnmpConstants.version2c);
        try {
            transportProtocol=new DefaultUdpTransportMapping();
            transportProtocol.listen();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        protocol=new Snmp(transportProtocol);
        try {
            protocol.listen();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PDUFactory pF =new DefaultPDUFactory(PDU.GETNEXT);
        TableUtils tableUtils =new TableUtils(protocol, pF);
        tableUtils.setMaxNumRowsPerPDU(maxRepetitions);
        OID[] columns =new OID[1];
        columns[0] =new OID("1.3.6.1.2.1.17.4.3.1.1" );

        NetTools tools=new NetTools();
        String MACAddress=getLastInterfaceMAC();
        String decimalMAC=tools.decimalMAC(MACAddress);

        OID lowerBoundIndex =  new OID(decimalMAC);
        OID upperBoundIndex =  null;
        List AFTList =  tableUtils.getTable(target, columns, lowerBoundIndex, upperBoundIndex);

        for ( int j = 0; j < AFTList.size()-1;j++){
            String containVbs=null;
            int indexVbs=AFTList.get(j).toString().indexOf("vbs");
            containVbs=AFTList.get(j).toString().substring(indexVbs);

            String containFirstEqualSign=null;
            int indexFirstEqualSign=containVbs.indexOf("=");
            containFirstEqualSign=containVbs.substring(indexFirstEqualSign+2);

            String mac=null;
            int  indexSecondEqualSign=containFirstEqualSign.indexOf("=");
            int indexOfEnd=containFirstEqualSign.indexOf("]");
            mac=containFirstEqualSign.substring(indexSecondEqualSign+2,indexOfEnd);
            System.out.println(mac);
            macSet.add(mac);


        }
        return AFT;
    }*/
}
