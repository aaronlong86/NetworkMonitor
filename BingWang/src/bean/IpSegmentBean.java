package bean;

public class IpSegmentBean
{
    private int id;
    private String area;
    private String ipstart;
    private String ipend;
    private String areacode;
    private int level;
    private int flag;

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getArea()
    {
        return this.area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getIpstart()
    {
        return this.ipstart;
    }

    public void setIpstart(String ipstart)
    {
        this.ipstart = ipstart;
    }

    public String getIpend()
    {
        return this.ipend;
    }

    public void setIpend(String ipend)
    {
        this.ipend = ipend;
    }

    public String getAreacode()
    {
        return this.areacode;
    }

    public void setAreacode(String areacode)
    {
        this.areacode = areacode;
    }

    public int getLevel()
    {
        return this.level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getFlag(){return flag;}
    public void setFlag(int flag){this.flag=flag;}
}
