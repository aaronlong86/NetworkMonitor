package showservlet;

import bean.IpBean;
import java.util.Comparator;

public class SortIp
        implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        IpBean s1 = (IpBean)o1;
        IpBean s2 = (IpBean)o2;
        int[] i1 = { Integer.valueOf(s1.getIp().split("\\.")[0]).intValue(), Integer.valueOf(s1.getIp().split("\\.")[1]).intValue(), Integer.valueOf(s1.getIp().split("\\.")[2]).intValue(), Integer.valueOf(s1.getIp().split("\\.")[3]).intValue() };

        int[] i2 = { Integer.valueOf(s2.getIp().split("\\.")[0]).intValue(), Integer.valueOf(s2.getIp().split("\\.")[1]).intValue(), Integer.valueOf(s2.getIp().split("\\.")[2]).intValue(), Integer.valueOf(s2.getIp().split("\\.")[3]).intValue() };

        int i = 0;
        while (i < 4)
        {
            if (i1[i] < i2[i]) {
                return -1;
            }
            if (i1[i] == i2[i]) {
                i++;
            } else {
                return 1;
            }
        }
        return 0;
    }
}
