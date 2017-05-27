package com.huai.operation.dao;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface MonitorDao {

	List queryTodayBills(IData param);

	List queryTodayBillItems(IData param);
	
	
	
}
