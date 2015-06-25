package bean;

public class TongJiBean
{
    private String area;
    private String areacode;
    private int ipnum;
    private int tasknum;
    private float taskrate;
    private int ranking;

    public String getArea()
    {
        return this.area;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getAreacode(){return areacode;}
    public void setAreacode(String areacode){this.areacode=areacode;}

    public int getIpnum()
    {
        return this.ipnum;
    }

    public void setIpnum(int ipnum)
    {
        this.ipnum = ipnum;
    }

    public int getTasknum()
    {
        return this.tasknum;
    }

    public void setTasknum(int tasknum)
    {
        this.tasknum = tasknum;
    }

    public float getTaskrate()
    {
        return this.taskrate;
    }

    public void setTaskrate(float taskrate)
    {
        this.taskrate = taskrate;
    }

    public int getRanking()
    {
        return this.ranking;
    }

    public void setRanking(int ranking)
    {
        this.ranking = ranking;
    }
}
