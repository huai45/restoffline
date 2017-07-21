package com.huai.print.service;

import java.util.Map;
import com.huai.common.domain.IData;

public interface PrintService {

	Map queryFoodPrintList(IData param);

	Map queryBillPrintList(IData param);

	Map queryPrintBillInfo(IData param);

	Map queryPrintBillInfoByTable(IData param);

	Map queryPrintBillInfoByBillId(IData param);

	Map printBill(String bill_id);

    Map printBillByBillId(String bill_id);

	Map printCategory();

	Map printTodayMoney();

}
