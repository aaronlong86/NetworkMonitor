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
        String uri=request.getRequestURI();//获得发出请求字符串的客户端地址
        String url=request.getRequestURL().toString();
        System.out.println(url);
        String path=request.getServletPath();//获得客户端所请求的脚本文件的文件路径
        String ip=request.getRemoteAddr();//获得客户端的IP地址
        String hostname=request.getRemoteHost();//获得客户端电脑的名字，若失败，则返回客户端电脑的IP地址
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

