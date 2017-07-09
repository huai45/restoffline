package com.huai.operation.dao;

import com.huai.common.domain.IData;

public interface TableDao {
	
	IData queryTableById( String table_id );
	
	
}
