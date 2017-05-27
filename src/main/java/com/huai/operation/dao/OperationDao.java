package com.huai.operation.dao;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface OperationDao {

	public List queryTableBills(String rest_id, String table_id);
	
	public IData queryBillByTable(String rest_id, String table_id);
	
	public IData queryBillByBillId(String bill_id, String rest_id);
	
	public List queryBillItemByBillId(String bill_id, String rest_id);

	public IData queryBillItemByItemId(String item_id, String rest_id);
	
	public List queryBillFeeByBillId(String bill_id, String rest_id);
	
	public IData queryBillInfo(IData bill);

	public IData createNewBill(IData param);

	public List queryTableList(IData param);

	public String saveBillItems(IData param);

	public IData queryFoodById(IData param);

	public IData queryTempFood(IData param);

	public String cancelBillItems(IData param);
	
	public String freeBillItems(IData param);
	
	public String derateBillItems(IData param);

	public String changeTableForBillItems(IData param);

	public String hurryCook(IData param);

	public String startCook(IData param);

	public String finishCook(IData param);
	
	public String payFee(IData param);

	public String reduceFee(IData param);

	public String closeBill(IData param);

//	public String payByCreditUser(IData param);
//
//	public String payByVipCard(IData param);

	public String payByCust(IData param);

	public String reopenBill(IData param);

	public String finishToday(IData param);
	
	public String calculateData(IData param);

	public String backupTodayBill(IData param);

	public String backupPrintLog(IData param);

	public String backupUserMoney(IData param) throws Exception;

	public String backupSysParam(IData param) throws Exception;


}
