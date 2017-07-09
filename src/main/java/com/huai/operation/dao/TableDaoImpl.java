package com.huai.operation.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;

@Component("tableDao")
public class TableDaoImpl extends BaseDao implements TableDao {

	public IData queryTableById( String table_id) {
		List list = jdbcTemplate.queryForList("select * from td_table where table_id = ?  ", new Object[]{ table_id  });
		IData table = null;
		if(list.size()>0) {
        	table = new IData((Map)list.get(0));
        }
		return table;
	}


}
