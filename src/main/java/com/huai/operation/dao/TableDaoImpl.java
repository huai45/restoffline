package com.huai.operation.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.util.*;

@Component("tableDao")
public class TableDaoImpl extends BaseDao implements TableDao {

	public IData queryTableById(String rest_id, String table_id) {
		List list = jdbcTemplate.queryForList("select * from td_table where table_id = ? and rest_id = ? ", new Object[]{ table_id,rest_id });
		IData table = null;
		if(list.size()>0) {
        	table = new IData((Map)list.get(0));
        }
		return table;
	}


}
