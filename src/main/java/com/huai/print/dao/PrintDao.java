package com.huai.print.dao;

import java.util.List;
import com.huai.common.domain.IData;

public interface PrintDao {

	IData queryRestByAppId(String appid);

	List queryFoodPrintList(IData param);

	List queryBillPrintList(IData param);

	IData quqryPrintRestInfo(IData bill);

	IData queryPrintRestInfo(IData bill);

	boolean printBillFinish(String printId ,String status);

	boolean printFoodFinish(String printId ,String status);

    int addPrintBillLog(IData param);
}
