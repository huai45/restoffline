package com.huai.print.service;

import com.huai.common.util.ut;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by huai23 on 2017/5/30.
 */
@Service
public class PrintTask {

    private static final Logger log = Logger.getLogger(PrintTask.class);

    @Resource(name="printService")
    public PrintService printService;

    @Scheduled(cron = "* * * * * *")
    public void printBill(){
        System.out.println(" printBill  time = "+ ut.currentTime());
        // 1. 查询待打印账单

        // 2. 打印账单

        // 3. 更新账单打印状态


    }

    @Scheduled(cron = "* * * * * *")
    public void printFood(){
        System.out.println(" printFood  time = "+ ut.currentTime());
        // 1. 查询待打印菜品

        // 2. 打印菜品

        // 3. 更新菜品打印状态
    }

}
