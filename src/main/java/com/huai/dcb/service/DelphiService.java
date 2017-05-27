package com.huai.dcb.service;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface DelphiService {

	public Map getPhoneUser(String rest_id, String user_id);
	
	public Map getPhoneInfo(String rest_id, String phone);

	public String openTable(IData param); 
	
	public boolean checkTrade(String rest_id, String trade_id);

	public String printQueryBill(IData param);

	
	
}
