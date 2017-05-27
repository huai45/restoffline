package com.huai.operation.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.operation.dao.MonitorDao;
import com.huai.operation.dao.QueryDao;
import com.huai.common.util.*;

@Component("monitorService")
public class MonitorServiceImpl implements MonitorService {

	@Resource(name="baseDao")
	public BaseDao baseDao;

	@Resource(name="queryDao")
	public QueryDao queryDao;
	
	@Resource(name="monitorDao")
	public MonitorDao monitorDao;
	
	public Map sendSubmit(final IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		User user = (User)param.get("user");
		String submit_str = param.getString("submit_str");
		try {
			if(ut.isEmpty(submit_str)||submit_str.equals("null")||submit_str.equals("undefined")){
				ut.p(" ****************  sendSubmit   submit_str 为空 直接返回 ");
				return result;
			}
			String[] items =  submit_str.split("#END");
			final List itemList = new ArrayList();
			String now = ut.currentTime();
			for(int i=0;i<items.length;i++){
				if(ut.isEmpty(items[i])){
					continue;
				}
				String[] temp = items[i].split("#DOT");
				Map item = new HashMap();
				item.put("barcode", temp[0]);
				if(temp.length>1){
					item.put("time", temp[1]);
				}else{
					item.put("time", now);
				}
				itemList.add(item);
			}
			// state    0 ： 未打单   1 ： 已打单但未上菜   2 ： 已上菜
			baseDao.jdbcTemplate.batchUpdate( "update tf_bill_item set state = '2' , end_time = ? where barcode = ? and rest_id = ? and state not in ('0','2') " , 
					new BatchPreparedStatementSetter() {
					public int getBatchSize() {
					        return itemList.size();
					   }
					public void setValues(PreparedStatement pstmt, int i)
							throws SQLException {
						Map item = (Map)itemList.get(i);
//						ut.log(item);
					    pstmt.setString(1, ""+item.get("time"));
					    pstmt.setString(2, item.get("barcode").toString());
					    pstmt.setString(3, param.getString("rest_id"));
					}
			});
		} catch (Exception e) {
			result.put("success", "false");
			e.printStackTrace();
			ut.log(ut.err("提交失败!"));
			return result;
		}
		return result;
	}
	
	public List queryAllBill(IData param) {
		List bills = monitorDao.queryTodayBills(param);
		return bills;
	}

	public List queryAllBillItem(IData param) {
		List items = monitorDao.queryTodayBillItems(param);
		return items;
	}

	public Map queryTableBill(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		User user = (User)param.get("user");
		String table_id = param.getString("table_id");
		List billList = baseDao.jdbcTemplate.queryForList("select * from tf_bill where table_id = ? and rest_id = ? and pay_type = '0' ",
			new Object[]{ table_id,user.getRest_id() });
	    String bill_id = "";
		if(billList.size()>0){
			bill_id = ((Map)billList.get(0)).get("BILL_ID").toString();
	    }
		result.put("bill_id", bill_id);
		ut.p(result);
		return result;
	}

	

}


