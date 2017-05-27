package com.huai.cust.dao;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface CustDao {

	public String addCreditUser(IData param);

	public String updateCreditUser(IData param);

	public String payfeeForCredit(IData param);

	
	
	public List queryCustByCardNo(IData param);

	public List queryCustByPhone(IData param);

	public List queryCustByImei(IData param);

	public String addVipCard(IData param);
	
	public String updateVipCard(IData param);

	public String payfeeForVipCard(IData param);

	public IData queryCardByCardNo(IData param);

	public IData queryCardByImei(IData param);
	
	
	
}
