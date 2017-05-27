package com.huai.cust.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface CustService {

	Map addCreditUser(IData param);

	Map updateCreditUser(IData param);

	Map payfeeForCredit(IData param);
	
	
	
	Map queryVipCardList(IData param);

	Map addVipCard(IData param);
	
	Map updateVipCard(IData param);

	Map payfeeForVipCard(IData param);


	
}
