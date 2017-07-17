package com.huai.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhonghuai.zhang on 2017/7/11.
 */
public class ListTest {

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            List list = new ArrayList(10);
            list.add("1");
            list.add("11");
            list.add("111");
            list.add("1111");
            list.add("2");
            list.add("1");
            list.add("11");
            list.add("111");
            list.add("1111");
            list.add("2");
            list.add("1");
            list.add("11");
            list.add("111");
            list.add("1111");
            list.add("2");
        }
        long t2 = System.currentTimeMillis();
        System.out.println("   use  time  :   "+ (t2-t1)+" ms");


        t1 = System.currentTimeMillis();
        List list2 = new ArrayList(10);
        for (int i = 0; i < 10000000; i++) {
            list2.add("1");
            list2.add("11");
            list2.add("111");
            list2.add("1111");
            list2.add("2");
            list2.add("1");
            list2.add("11");
            list2.add("111");
            list2.add("1111");
            list2.add("2");
            list2.add("1");
            list2.add("11");
            list2.add("111");
            list2.add("1111");
            list2.add("2");
            list2.clear();
        }
        t2 = System.currentTimeMillis();
        System.out.println("   use  time  :   "+ (t2-t1)+" ms");





    }
}
