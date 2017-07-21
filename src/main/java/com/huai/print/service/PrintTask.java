package com.huai.print.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by huai23 on 2017/5/30.
 */
@Service
public class PrintTask {

    private static final Logger log = Logger.getLogger(PrintTask.class);

    @Autowired
    public PrintService printService;

    @Autowired
    public PrintFoodService printFoodService;

    @Autowired
    public PrintBillService printBillService;

    @Scheduled(cron = "* * * * * *")
    public void printBill(){
//        System.out.println(" printBill  time = "+ ut.currentTime());
        printBillService.printOneBill();
    }

    @Scheduled(cron = "* * * * * *")
    public void printFood(){
//        System.out.println(" printFood  time = "+ ut.currentTime());
        printFoodService.printOneFood();
    }

}
