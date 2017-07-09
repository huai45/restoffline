package com.huai.operation.dao;

import java.util.List;

import com.huai.common.domain.IData;

public interface OperationDao {

	 List queryTableBills(String table_id );
	
	 IData queryBillByTable(String table_id );
	
	 IData queryBillByBillId(String bill_id );
	
	 List queryBillItemByBillId(String bill_id );

	 IData queryBillItemByItemId(String item_id );
	
	 List queryBillFeeByBillId(String bill_id );
	
	 IData queryBillInfo(IData bill);

	 IData createNewBill(IData param);

	 List queryTableList(IData param);

	 String saveBillItems(IData param);

	 IData queryFoodById(IData param);

	 IData queryTempFood(IData param);

	 String cancelBillItems(IData param);
	
	 String freeBillItems(IData param);
	
	 String derateBillItems(IData param);

	 String changeTableForBillItems(IData param);

	 String hurryCook(IData param);

	 String startCook(IData param);

	 String finishCook(IData param);
	
	 String payFee(IData param);

	 String reduceFee(IData param);

	 String closeBill(IData param);

	 String payByCust(IData param);

	 String reopenBill(IData param);

	 String finishToday(IData param);
	
	 String calculateData(IData param);

	 String backupTodayBill(IData param) throws Exception;

	 String backupPrintLog(IData param);

	 String backupUserMoney(IData param) throws Exception;

	 String backupSysParam(IData param) throws Exception;


}
