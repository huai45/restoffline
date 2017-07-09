package com.huai.operation.dao;

import java.util.List;
import com.huai.common.domain.IData;

public interface MonitorDao {

	List queryTodayBills(IData param);

	List queryTodayBillItems(IData param);

}
