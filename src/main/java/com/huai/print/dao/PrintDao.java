package com.huai.print.dao;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface PrintDao {

	IData queryRestByAppId(String appid);

	List queryFoodPrintList(IData param);

	List queryBillPrintList(IData param);

	IData quqryPrintRestInfo(IData bill);
	
	
}
