package com.huai.test;

import java.util.*;

/**
 * Created by zhonghuai.zhang on 2017/7/11.
 */
public class SetTest {

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<Integer>();
        set.add(11);
        set.add(10);
        set.add(14);
        set.add(10);
        Iterator<Integer> it = set.iterator();
        while (it.hasNext())
        {
            System.out.print(it.next() + " ");
        }
        System.out.println(" ============================  ");
        List list = new ArrayList();
        list.add(11);
        list.add(10);
        list.add(14);
        list.add(10);
        it = list.iterator();
        while (it.hasNext())
        {
            System.out.print(it.next() + " ");
        }




    }

}
