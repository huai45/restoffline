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

@Component("monitorDao")
public class MonitorDaoImpl extends BaseDao implements MonitorDao {


	public List queryTodayBills(IData param) {
		List bills = jdbcTemplate.queryForList("select * from tf_bill where 1 = 1 order by bill_id desc ", new Object[]{  });
		return bills;
	}

	public List queryTodayBillItems(IData param) {
		String sql = " select a.* , b.TABLE_ID , b.PAY_TYPE , TIMESTAMPDIFF(MINUTE, a.start_time ,now()) USE_TIME " +
			" from tf_bill_item a , tf_bill b  where  a.bill_id  = b.bill_id order by USE_TIME desc ";
		List items = jdbcTemplate.queryForList( sql , new Object[]{   });
		return items;
	}
	

}
