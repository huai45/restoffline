package com.huai.operation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface OperationService {

	public Map checkTableState(IData param);

	public Map openTable(IData param);

	public Map queryAllTableState(IData param);

	public Map addBillItems(IData param);

	public Map addTempFood(IData param);

	public Map cancelFood(IData param);

	public Map presentFood(IData param);

	public Map derateFood(IData param);

	public Map changeTable(IData param);

	public Map startCook(IData param);

	public Map hurryCook(IData param);

	public Map finishCook(IData param);
	
	public Map payFee(IData param);

	public Map reduceFee(IData param);

	public Map closeBill(IData param);

	public Map payByCreditUser(IData param);

	public Map payByVipCard(IData param);

	public Map finishToday(IData param);
	
	public IData queryBillInfo(IData bill);

	public Map reopenBill(IData param);

	public IData queryBillById(IData param);

	public List queryTableList(IData IData);

	public Map queryTableById(IData param);

	public Map queryBillItemByItemId(IData param);

}
