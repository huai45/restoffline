package com.huai.common.dao;

import java.util.Map;

import com.huai.common.domain.IData;

public interface CommonDao {

	public IData queryRestInfoById(IData param);

	public IData queryCommonParam();
	
	public IData queryRestParam(IData p);
	
}
