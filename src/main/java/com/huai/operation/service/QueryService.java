package com.huai.operation.service;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.IData;

public interface QueryService {

	Map queryCategoryData(IData param);

	Map queryTodayData(IData param);

	Map queryCreditUserList(IData param);
	
	Map queryVipCardUserList(IData param);

	IData queryVipCardUserInfo(IData param);

	IData queryCustById(IData param);

	Map queryTodayBills(IData param);

	IData queryTodayBillById(IData param);

	Map queryHistorySaleData(IData param);

}
