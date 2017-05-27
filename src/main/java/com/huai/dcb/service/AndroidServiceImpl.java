package com.huai.dcb.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.User;
import com.huai.common.dao.FoodDao;
import com.huai.common.util.*;
import com.huai.dcb.action.AndroidAction;

@Component("androidService")
public class AndroidServiceImpl implements AndroidService {

	private static final Logger log = Logger.getLogger(AndroidServiceImpl.class);
	
	@Resource(name="foodDao")
	public FoodDao foodDao;
	
	@Resource(name="baseDao")
	public BaseDao baseDao;


	public Map getPhoneUser(String rest_id, String user_id) {
		Map phoneInfo = null;
		List list = baseDao.jdbcTemplate.queryForList(" select rest_id,user_id,username,password,remark from td_android_user  where rest_id = ? and user_id = ?  ", new Object[]{rest_id,user_id});
		if(list.size()>0){
			phoneInfo = (Map)list.get(0);
		}
		return phoneInfo;
	}
	

	public String printQueryBill(User user, Map param) {
		String print_sql = "insert into tf_print_bill_log " +
			" (PRINT_ID,REST_ID,STATE,PRINTER,BILL_ID,OPER_TIME,OPER_STAFF_ID,OPER_STAFF_NAME,PRINT_TIME) " +
			" values ( ?,?,'0',?,?,?,?,?,'') ";
		String print_id = baseDao.getNewID("print_id");
		baseDao.jdbcTemplate.update(print_sql , new Object[]{print_id,param.get("rest_id"),param.get("printer"),
				param.get("bill_id"),param.get("phone"),param.get("phone"),ut.currentTime()});
		return ut.suc("打印任务提交成功！");
	}

	
}
