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
		String sql = " select * , (select ip from td_printer b where b.printer = a.printer) IP ," +
				" (select ip from td_printer c where c.printer = a.printer_start) IP_START , " +
				" (select ip from td_printer d where d.printer = a.printer_hurry) IP_HURRY , " +
				" (select ip from td_printer e where e.printer = a.printer_back) IP_BACK , " +
				" (select ip from td_printer f where f.printer = a.printer_sec) IP_SEC  " +
				" from tf_print_log a where 1 = 1 and a.state in ('0') " +
				" order by a.print_id limit 0 , 10 ";
		List result = jdbcTemplate.queryForList(sql, new Object[]{ });
		return result;
	}

	public boolean printFoodFinish(String printId ,String status) {
		String update_sql = " update tf_print_log set state = ? ,PRINT_TIME = ?  where print_id = ? ";
		int i = jdbcTemplate.update(update_sql,new Object[]{status,ut.currentTime(),printId});
		if(i==0){
			return false;
		}
		return true;
	}

	public List queryBillPrintList(final IData param) {
		String sql = " select * from tf_print_bill_log where 1=1 and state in ('0') order by print_id asc limit 0 , 10 ";
		List result = jdbcTemplate.queryForList(sql, new Object[]{ });
		return result;
	}


	public boolean printBillFinish(String printId ,String status) {
		String update_sql = " update tf_print_bill_log set state = ? ,PRINT_TIME = ?  where print_id = ? ";
		int i = jdbcTemplate.update(update_sql,new Object[]{status,ut.currentTime(),printId});
		if(i==0){
			return false;
		}
		return true;
	}

	public IData quqryPrintRestInfo(IData bill) {
		List list = jdbcTemplate.queryForList("select * from td_table where table_id = ?  ", new Object[]{ bill.getString("TABLE_ID") });
		if(list.size()>0) {
        	Map table = (Map)list.get(0);
        	bill.put("PRINTER", table.get("PRINTER"));
        }
		return null;
	}

	
	
	

}
