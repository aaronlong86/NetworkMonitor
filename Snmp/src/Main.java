/**
 * Created by Administrator on 2015/7/9 0009.
 */
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Main {
    public static void main(String[] args) throws IOException {
       Flow flow =new Flow();
        flow.multiThread();
/*
        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
        snmp.listen();
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setVersion(SnmpConstants.version2c);
        target.setAddress(new UdpAddress("192.168.2.128/161"));
        target.setTimeout(5000);    //3s
        target.setRetries(1);*/

  //      sendRequest(snmp, createGetPdu(), target);
 //       sendRequest(snmp, createGetNextPdu(), target);
  //      sendRequest(snmp, createGetBulkPdu(), target);
  //      snmpWalk(snmp, target);
 //       SnmpCpu snmpCpu =new SnmpCpu();
  //      System.out.println(snmpCpu.getCpuLoads("192.168.2.128")+"%");

  //      target.setCommunity(new OctetString("public"));
 //       sendRequest(snmp, createSetPdu(), target);
//广播查找可管理的设备
     /*    CommunityTarget broadcastTarget = new CommunityTarget();
        broadcastTarget.setCommunity(new OctetString("public"));
        broadcastTarget.setVersion(SnmpConstants.version2c);
        broadcastTarget.setAddress(new UdpAddress("45.255.0.255/161"));
        broadcastTarget.setTimeout(5000);   //5s
        sendAsyncRequest(snmp, createGetNextPdu(), broadcastTarget);
        try {
            Thread.sleep(6000); //main thread wait 6s for the completion of asynchronous request
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    private static PDU createGetPdu() {
        PDU pdu = new PDU();
        pdu.setType(PDU.GET);
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.10.1.3.1"))); //sysUpTime
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.10.1.3.2"))); //sysName
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.10.1.3.3")));   //mac
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.4.3.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.4.4.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.4.5.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.4.6.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.9.1.2.1")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.9.1.3.1")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.9.1.6.1")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.9.1.7.1")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.9.1.9.1")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.1.3.0")));
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.2.2.1.2.2")));
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.2.2.1.6.2")));
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.2.1.0")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.2.1.25.3.3")));
        pdu.add(new VariableBinding(new OID(".1.3.6.1.4.1.2021.11.11.0")));//空闲CPU百分比
        return pdu;
    }

    private static PDU createGetNextPdu() {
        PDU pdu = new PDU();
        pdu.setType(PDU.GETNEXT);
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.3")));   //sysUpTime
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5")));   //sysName
        return pdu;
    }

    private static PDU createGetBulkPdu() {
        PDU pdu = new PDU();
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(1000);  //must set it, default is 0
        pdu.setNonRepeaters(0);
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.17.4.3.1.1")));     //system
        return pdu;
    }

    private static PDU createSetPdu() {
        PDU pdu = new PDU();
        pdu.setType(PDU.SET);
        pdu.add(new VariableBinding(new OID("1.3.6.1.2.1.1.5.0"), new OctetString("sysname"))); //sysName
        return pdu;
    }

    private static void sendRequest(Snmp snmp, PDU pdu, CommunityTarget target)
            throws IOException {
        ResponseEvent responseEvent = snmp.send(pdu, target);
        PDU response = responseEvent.getResponse();

        if (response == null) {
            System.out.println("TimeOut...");
        } else {
            if (response.getErrorStatus() == PDU.noError) {
                Vector<? extends VariableBinding> vbs = response.getVariableBindings();
                int i=1;
                for (VariableBinding vb : vbs) {
                    System.out.println(Integer.valueOf(i).toString()+":"+vb + " ," + vb.getVariable().getSyntaxString());
                    i++;
                }
            } else {
                System.out.println("Error:" + response.getErrorStatusText());
            }
        }
    }

    private static void sendAsyncRequest(Snmp snmp, PDU pdu, CommunityTarget target)
            throws IOException {
        snmp.send(pdu, target, null, new ResponseListener(){

            @Override
            public void onResponse(ResponseEvent event) {
                PDU response = event.getResponse();
                System.out.println("Got response from " + event.getPeerAddress());
                if (response == null) {
                    System.out.println("TimeOut...");
                } else {
                    if (response.getErrorStatus() == PDU.noError) {
                        Vector<? extends VariableBinding> vbs = response.getVariableBindings();
                        for (VariableBinding vb : vbs) {
                            System.out.println(vb + " ," + vb.getVariable().getSyntaxString());
                        }
                    } else {
                        System.out.println("Error:" + response.getErrorStatusText());
                    }
                }
            }});
    }

    private static void snmpWalk(Snmp snmp, CommunityTarget target) {
        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));//GETNEXT or GETBULK
        utils.setMaxNumRowsPerPDU(5);   //only for GETBULK, set max-repetitions, default is 10
        OID[] columnOids = new OID[] {
                new OID(".1.3.6.1.4.1.2021.9.1.9"), //
        };
        // If not null, all returned rows have an index in a range (lowerBoundIndex, upperBoundIndex]
        List<TableEvent> l = utils.getTable(target, columnOids,null,null);
        for (TableEvent e : l) {
            System.out.println(e);
        }
    }
}
