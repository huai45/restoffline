package com.huai.operation.dao;

import java.util.List;

import com.huai.common.domain.IData;

public interface QueryDao {

	public IData queryCustById(IData param);

	public IData queryCustByCardNo(IData param);

	public List queryCustList(IData param);	
	

	
}
