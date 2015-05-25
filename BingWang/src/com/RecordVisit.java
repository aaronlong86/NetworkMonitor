package com;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.ResultSet;

/**
 * Created by Administrator on 2015/5/20 0020.
 */
public class RecordVisit {
    public void insertrecord(HttpServletRequest request)throws ServletException, IOException {
        String agent = request.getHeader("User-Agent");
        BrowseTool b=new BrowseTool();
        String ie=b.checkBrowse(agent);
        String uri=request.getRequestURI();//��÷��������ַ����Ŀͻ��˵�ַ
        String url=request.getRequestURL().toString();
        System.out.println(url);
        String path=request.getServletPath();//��ÿͻ���������Ľű��ļ����ļ�·��
        String ip=request.getRemoteAddr();//��ÿͻ��˵�IP��ַ
        String hostname=request.getRemoteHost();//��ÿͻ��˵��Ե����֣���ʧ�ܣ��򷵻ؿͻ��˵��Ե�IP��ַ
        insertdb(ip,hostname,ie,agent,uri,path);
    }
    private void insertdb(String ip,String hostname,String ie,String agent,String uri,String path){
        Mysqldb mdb =new Mysqldb();
        try
        { 	String sqlstr ="insert into recordvisit (ip,hostname,ie,agent,uri,path) VALUES(\'"
                +ip+"\',\'"+hostname+"\',\'"+ie+"\',\'"+agent+"\',\'"+uri+"\',\'"+path+"\')";
            mdb.sql.executeUpdate(sqlstr);
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
        }

    }
}

