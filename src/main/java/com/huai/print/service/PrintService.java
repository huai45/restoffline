package com.huai.print.service;

import java.util.List;
import java.util.Map;
import com.huai.common.domain.IData;

public interface PrintService {

	public Map queryFoodPrintList(IData param);

	public Map queryBillPrintList(IData param);

	public Map queryPrintBillInfo(IData param);

	public Map queryPrintBillInfoByTable(IData param);

	public Map queryPrintBillInfoByBillId(IData param);
	
}
