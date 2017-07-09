package com.huai.operation.dao;

import java.util.List;

import com.huai.common.domain.IData;

public interface QueryDao {

	IData queryCustById(IData param);

	IData queryCustByCardNo(IData param);

	List queryCustList(IData param);
	

	
}
