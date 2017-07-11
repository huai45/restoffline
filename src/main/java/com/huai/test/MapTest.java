package com.huai.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhonghuai.zhang on 2017/7/11.
 */
public class MapTest {


    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");
        map.put("4","4");
        map.put("1","5");
        System.out.println(map.size());
        Set keys = map.entrySet();
        Iterator<Integer> it = keys.iterator();
        while (it.hasNext())
        {
            System.out.print(it.next() + " ");
        }
    }
}
