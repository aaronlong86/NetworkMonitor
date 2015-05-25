package sys;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2015/5/22 0022.
 */
@WebFilter(filterName = "RecordVisitFilter")
public class RecordVisitFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        ((HttpServletResponse)resp).setContentType("text/html;charset=UTF-8");
        HttpServletRequest request =(HttpServletRequest)req;
        String agent = request.getHeader("User-Agent");
        BrowseTool b=new BrowseTool();
        String ie=b.checkBrowse(agent);
        String uri=request.getRequestURI();
        String path=request.getServletPath();
        String ip=request.getRemoteAddr();
        String hostname=request.getRemoteHost();
        insertdb(ip,hostname,ie,agent,uri,path);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

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
