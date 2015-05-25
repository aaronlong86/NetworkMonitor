package com;

import java.util.Date;

/**
 * Created by Administrator on 2015/5/6 0006.
 */
public class IpBean {
    private int id;
    private String area;
    private String ip;
    private String lastdiscovery;
    private String status;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }

    public String getIp() { return ip; }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLastdiscovery() {
        return lastdiscovery;
    }
    public void setLastdiscovery(String lastdiscovery) {
        this.lastdiscovery = lastdiscovery;
    }

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
}
