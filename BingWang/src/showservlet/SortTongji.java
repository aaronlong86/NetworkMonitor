package showservlet;

import bean.TongJiBean;
import java.util.Comparator;

public class SortTongji
        implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        TongJiBean s1 = (TongJiBean)o1;
        TongJiBean s2 = (TongJiBean)o2;
        if (s1.getTaskrate() > s2.getTaskrate()) {
            return -1;
        }
        if (s1.getTaskrate() == s2.getTaskrate()) {
            return 0;
        }
        return 1;
    }
}
