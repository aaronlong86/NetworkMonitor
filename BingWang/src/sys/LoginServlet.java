package sys;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name="LoginServlet")
public class LoginServlet
        extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String result = "";

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if ((username == "") || (username == null) || (username.length() > 20)) {
            try
            {
                result = "������������(��������20������)!";
                request.setAttribute("message", result);
                response.sendRedirect("login.jsp");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if ((password == "") || (password == null) || (password.length() > 20)) {
            try
            {
                result = "����������(��������20������)!";
                request.setAttribute("message", result);
                response.sendRedirect("login.jsp");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Mysqldb mdb = new Mysqldb();
        try
        {
            String sqlstr = "select t1.areacode,t2.area,t1.name from users t1,organization t2 where t1.username='" + username + "' and t1.password= '" + password + "' and t1.areacode=t2.areacode";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            HttpSession session = request.getSession();
            if (rs.next())
            {
                session.setAttribute("username", username);
                session.setAttribute("name", rs.getString("t1.name"));
                session.setAttribute("area", rs.getString("t2.area"));
                session.setAttribute("areacode", rs.getString("areacode"));
                response.sendRedirect("/bingwang/success.jsp");
            }
            else
            {
                session.setAttribute("message", "用户名或密码不匹配！");
                response.sendRedirect("/bingwang/login.jsp");
            }
            rs.close();
            mdb.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error : " + ex.toString());
            mdb.close();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {}
}
