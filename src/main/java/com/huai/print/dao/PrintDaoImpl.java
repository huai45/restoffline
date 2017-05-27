package com.huai.print.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.util.*;

@Component("printDao")
public class PrintDaoImpl extends BaseDao implements PrintDao {

	public IData queryRestByAppId(String appid) {
		List result = jdbcTemplate.queryForList("select * from td_restaurant where appid = ? ", new Object[]{ appid });
		if(result.size()==0){
			return null;
		}
		IData info = new IData((Map)result.get(0));
		return info;
	}

	public List queryFoodPrintList(final IData param) {
		String sql = " select * , (select ip from td_printer b where b.rest_id = a.rest_id and b.printer = a.printer) IP ," +
				" (select ip from td_printer c where c.rest_id = a.rest_id and c.printer = a.printer_start) IP_START , " +
				" (select ip from td_printer d where d.rest_id = a.rest_id and d.printer = a.printer_hurry) IP_HURRY , " +
				" (select ip from td_printer e where e.rest_id = a.rest_id and e.printer = a.printer_back) IP_BACK , " +
				" (select ip from td_printer f where f.rest_id = a.rest_id and f.printer = a.printer_sec) IP_SEC  " +
				" from tf_print_log a where a.rest_id = ? and a.state in ('0') " +
				" order by a.print_id asc limit 0 , 100 ";
		final List result = jdbcTemplate.queryForList(sql, new Object[]{ param.getString("rest_id") });
		String update_sql = " update tf_print_log set state = ? where print_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql ,
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return result.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					Map item = (Map)result.get(i);
					item.put("PRINTER", item.get("IP"));
					item.put("PRINTER_START", item.get("IP_START"));
					item.put("PRINTER_HURRY", item.get("IP_HURRY"));
					item.put("PRINTER_BACK", item.get("IP_BACK"));
					item.put("PRINTER_SEC", item.get("IP_SEC"));
					pstmt.setString(1, "2" );
				    pstmt.setString(2, item.get("PRINT_ID").toString() );
				    pstmt.setString(3, param.getString("rest_id") );
				}
		});
		return result;
	}

	public List queryBillPrintList(final IData param) {
		String sql = " select * from tf_print_bill_log where rest_id = ? and state in ('0','9') order by print_id asc limit 0 , 100 ";
		final List result = jdbcTemplate.queryForList(sql, new Object[]{ param.getString("rest_id") });
		String update_sql = " update tf_print_bill_log set state = ? where print_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return result.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					Map item = (Map)result.get(i);
					pstmt.setString(1, "2" );
				    pstmt.setString(2, item.get("PRINT_ID").toString() );
				    pstmt.setString(3, param.getString("rest_id") );
				}
		});
		return result;
	}

	public IData quqryPrintRestInfo(IData bill) {
		List list = jdbcTemplate.queryForList("select * from td_table where table_id = ? and rest_id = ? ", new Object[]{ bill.getString("TABLE_ID"),bill.getString("RESTE_ID") });
		if(list.size()>0) {
        	Map table = (Map)list.get(0);
        	bill.put("PRINTER", table.get("PRINTER"));
        }
		
		
		return null;
	}

	
	
	

}
