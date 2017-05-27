package com.huai.operation.dao;

import java.util.List;
import java.util.Map;

import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface TableDao {
	
	public IData queryTableById(String rest_id,String table_id);
	
	
}
