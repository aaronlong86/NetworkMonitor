package com;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/5/17 0017.
 */
public class SortPort implements Comparator {
    public int compare(Object o1, Object o2) {
        PortBean s1 = (PortBean) o1;
        PortBean s2 = (PortBean) o2;
        int[] i1 = {Integer.valueOf(s1.getIp().split("\\.")[0]), Integer.valueOf(s1.getIp().split("\\.")[1]),
                Integer.valueOf(s1.getIp().split("\\.")[2]), Integer.valueOf(s1.getIp().split("\\.")[3])};
        int[] i2 = {Integer.valueOf(s2.getIp().split("\\.")[0]), Integer.valueOf(s2.getIp().split("\\.")[1]),
                Integer.valueOf(s2.getIp().split("\\.")[2]), Integer.valueOf(s2.getIp().split("\\.")[3])};
        int p1=s1.getPort();
        int p2=s2.getPort();
        int i=0;
        while(i<4)
        {
            if(i1[i]<i2[i]){return -1;}
            if (i1[i]==i2[i]){i++;}else { return 1;}
        }
        if (p1<p2) {return -1;}
        if (p1>p2) {return 1;}
        return 0;
    }
}
