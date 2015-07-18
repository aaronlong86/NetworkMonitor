import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/12 0012.
 */
public class SnmpCpu {
    /**
     * 获取CPU信息
     * @param ip
     * @return getvalue
     */
    @SuppressWarnings("unchecked")
    public List getSNMP_CPU(String ip) {
        List getvalue = new ArrayList();
        try {
            String CPU_OID = ".1.3.6.1.2.1.25.3.3";//查看CPU信息的OID值
            List list = SnmpFunction.getSNMPList(ip,CPU_OID);
            List SylLs = new ArrayList();//存放组集合
            List OidLs = new ArrayList();//存放OID的索引值对应值
            List tempLs = new ArrayList();//存放负载对应值
            for (int i = 0; i < list.size(); i++) {
                TableEvent te = (TableEvent) list.get(i);
                VariableBinding[] vb = te.getColumns();

                if(vb==null){
                    return null;
                }else{
                    for (int j = 0; j < vb.length; j++) {
                        String _sid = vb[j].getOid().toString().substring(21,22).trim();//获取键
                        String _oid = vb[j].getOid().toString().substring(23,29).trim();//获id
                        Variable _s = vb[j].getVariable();
                        String _sva = "";
                        //类型判断
                        if (_s instanceof OctetString) {
                            _sva = new String(((OctetString)_s).getValue(),"UTF-8");
                        }else{
                            _sva = _s.toString();
                        }
                        if(Integer.parseInt(_sid)==1){//OID的索引值
                            OidLs.add(_oid);
                            SylLs.add(_sva);
                        }
                        if(Integer.parseInt(_sid)==2){
                            tempLs.add(_sva);
                        }
                    }
                }
            }
            getvalue.add(OidLs);
            getvalue.add(SylLs);
            getvalue.add(tempLs);
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }catch (RuntimeException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return getvalue;
    }

    public String getCpuLoads(String ip){
        List list=getSNMP_CPU(ip);
        List tempLs = (List)list.get(2);
        float cpuloads=0;
        int j=0;
        for(int i=0;i<tempLs.size();i++){
            cpuloads=cpuloads+Integer.parseInt(tempLs.get(i).toString());
            j=i+1;
        }
        cpuloads =cpuloads/j;
        String s=Float.toString(cpuloads);
        return s;
    }
}