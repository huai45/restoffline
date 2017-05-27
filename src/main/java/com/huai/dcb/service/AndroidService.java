package com.huai.dcb.service;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.User;

public interface AndroidService {

	public Map getPhoneUser(String rest_id, String user_id);
	
	public String printQueryBill(User user, Map param); 
	
}
