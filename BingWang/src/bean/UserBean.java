package bean;

/**
 * Created by Administrator on 2015/6/29 0029.
 */
public class UserBean {
    private String username;
    private String password;
    private String name;
    private String area;
    private String areacode;

    public String getUsername(){return username;}
    public void setUsername(String username){this.username=username;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}

    public String getName(){return name;}
    public void setName(String name){this.name=name;}

    public String getArea(){return area;}
    public void setArea(String area){this.area=area;}

    public String getAreacode(){return areacode;}
    public void setAreacode(String areacode){this.areacode=areacode;}
}
