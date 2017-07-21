package com.huai.test;

import com.huai.common.domain.DemoVO;

/**
 * Created by zhonghuai.zhang on 2017/7/18.
 */
public class LongTest {

    public static void main(String[] args) {
        DemoVO DemoVO = new DemoVO();
        System.out.println(DemoVO.getId());
        System.out.println(DemoVO.getName());
        System.out.println(DemoVO.getAge());
    }
}
