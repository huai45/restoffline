package com.huai.operation.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface MonitorService {

	List queryAllBill(IData param);

	List queryAllBillItem(IData param);
	
	Map sendSubmit(IData param);

	Map queryTableBill(IData param);

	
}
