package com;

/**
 * Created by Administrator on 2015/5/9 0009.
 */
public class PortBean {
    private int id;
    private String area;
    private String ip;
    private int port;
    private String protocol;
    private String description;
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
    public int getPort(){return port;}
    public void setPort(int port){this.port=port;}
    public String getProtocol(){return protocol;}
    public void setProtocol(String protocol){this.protocol=protocol;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
}
