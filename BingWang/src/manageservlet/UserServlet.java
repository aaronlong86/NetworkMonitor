package manageservlet;

import bean.OrganizationBean;
import bean.PortBean;
import bean.UserBean;
import sys.Mysqldb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/29 0029.
 */
@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String areacode = request.getParameter("areacode");
        String flag = request.getParameter("flag");
        System.out.println(flag);
        if (username != null) {
            if (flag.equals("modify")){
                updateuser(username, password, name, areacode);
                System.out.println(flag);}
            if (flag.equals("add")){
                adduser(username, password, name, areacode);
                System.out.println(flag);}
        }
        ArrayList<UserBean> userBeans = getUser();
        ArrayList<OrganizationBean> organizationBeans = getorg();
        request.setAttribute("userbeans", userBeans);
        request.setAttribute("organizationbeans", organizationBeans);
        request.getRequestDispatcher("/manage/user.jsp").forward(request, response);
    }

    private ArrayList<UserBean> getUser() {
        ArrayList<UserBean> userBeans = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try {
            String sqlstr = "SELECT t1.username,t1.password,t1.name,t1.areacode,t2.area FROM users t1,organization t2 where t1.areacode=t2.areacode";
            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next()) {
                UserBean userBean = new UserBean();
                userBean.setUsername(rs.getString("username"));
                userBean.setPassword(rs.getString("password"));
                userBean.setName(rs.getString("name"));
                userBean.setArea(rs.getString("area"));
                userBean.setAreacode(rs.getString("areacode"));
                userBeans.add(userBean);
            }
            rs.close();
            mdb.close();
        } catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return userBeans;
        }
        return userBeans;
    }

    private ArrayList<OrganizationBean> getorg() {
        ArrayList<OrganizationBean> organizationBeans = new ArrayList();
        Mysqldb mdb = new Mysqldb();
        try {
            String sqlstr = "SELECT * FROM organization where areacode like \'%00000000\'";

            ResultSet rs = mdb.sql.executeQuery(sqlstr);
            int i = 1;
            while (rs.next()) {
                OrganizationBean organizationBean = new OrganizationBean();
                organizationBean.setArea(rs.getString("area"));
                organizationBean.setAreacode(rs.getString("areacode"));
                organizationBeans.add(organizationBean);
            }
            rs.close();
            mdb.close();
        } catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
            mdb.close();
            return organizationBeans;
        }
        return organizationBeans;
    }

    private void updateuser(String username, String password, String name, String areacode) {
        Mysqldb mdb = new Mysqldb();
        try {
            String sqlstr = "UPDATE users SET password='"+ password + "',name='" + name
                    + "',areacode='" + areacode + "' where username='"+username+"'";
            System.out.println(username);
            mdb.sql.executeUpdate(sqlstr);
            mdb.close();
        } catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
            mdb.close();
        }
    }

    private void adduser(String username, String password, String name, String areacode) {
        Mysqldb mdb = new Mysqldb();
        try {
            String sqlstr = "INSERT INTO users (username, password,name, areacode) VALUES ('"
            +username+ "','"+ password+ "','" + name+ "','"+  areacode+ "')";
            mdb.sql.executeUpdate(sqlstr);
            mdb.close();
        } catch (Exception ex) {
            System.out.println("Error : " + ex.toString());
            mdb.close();
        }
    }
}
