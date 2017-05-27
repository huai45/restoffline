package com.huai.dcb.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.dao.FoodDao;
import com.huai.common.util.*;

@Component("delphiService")
public class DelphiServiceImpl implements DelphiService {

	@Resource(name="foodDao")
	public FoodDao foodDao;
	
	@Resource(name="baseDao")
	public BaseDao baseDao;


	public Map getPhoneUser(String rest_id, String user_id) {
		Map phone = baseDao.jdbcTemplate.queryForMap(" select * from td_phone_user  where user_id = ? and rest_id = ? ", 
			new Object[]{user_id,rest_id});
		return phone;
	}
	
	public Map getPhoneInfo(String rest_id, String phone) {
		Map phoneInfo = baseDao.jdbcTemplate.queryForMap(" select * from td_phone  where phone = ?  and rest_id = ? ", new Object[]{phone,rest_id});
		return phoneInfo;
	}

	public String phoneLogin(String rest_id, Map param) {
		DataSource ds = (DataSource)GetBean.getBean(rest_id);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
		jdbcTemplate.update("update td_phone set username = ? , LOGIN_TIME = ? where phone = ?  " ,
				new Object[]{param.get("username"), ut.currentTime() , param.get("phone")} );
		return ut.suc("登录成功");
	}

	public String openTable(IData param) {
		// TODO Auto-generated method stub
		String bill_id = baseDao.getNewID("bill_id");
		
		// 1. 插入账单表
		baseDao.jdbcTemplate.update("insert into tf_bill (bill_id,rest_id,table_id,pay_type,nop,open_staff_id,open_staff_name,open_date,open_time) " +
				" values (?,?,?,?,?,?,?,?,?) " ,
				new Object[]{bill_id,param.get("rest_id"),param.get("table_id"),"0",param.get("nop"),
				    param.get("phone"),param.get("staff"),ut.currentDate(),ut.currentTime()} );
		// 2. 置为占用状态  写入账单id
		baseDao.jdbcTemplate.update("update td_table set state = '1'  where table_id = ? and rest_id = ? " ,
				new Object[]{param.getString("table_id"),param.getString("rest_id")} );
		return "";
	}

	
	
	public boolean checkTrade(String rest_id, String trade_id) {
		List list = baseDao.jdbcTemplate.queryForList(" select item_id from tf_bill_item  where trade_id = ? and rest_id = ? ", new Object[]{trade_id,rest_id});
		if(list.size()>0){
			return true;
		}
		return false;
	}

	public String printQueryBill(IData param) {
		IData table = (IData)param.get("table");
		IData bill = (IData)param.get("bill");
		String print_id = baseDao.getNewID("print_id");
		String print_sql = "insert into tf_print_bill_log " +
			" (PRINT_ID,REST_ID,STATE,PRINTER,BILL_ID,OPER_TIME,OPER_STAFF_ID,OPER_STAFF_NAME,PRINT_TIME) " +
			" values ( ?,?,?,?,?,?,?,?,?) ";
		baseDao.jdbcTemplate.update(print_sql , new Object[]{print_id,param.get("rest_id"),"0",table.get("PRINTER"),bill.get("BILL_ID"),
				ut.currentTime(),param.get("phone"),param.get("phone"),""});
		return "0";
	}

	
}
