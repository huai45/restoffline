package com.huai.operation.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.util.*;

@Component("operationDao")
public class OperationDaoImpl extends BaseDao implements OperationDao {

	private static final Logger log = Logger.getLogger(OperationDaoImpl.class);
	
	String add_sql = "insert into tf_bill_item (item_id,bill_id,rest_id,item_type,call_type," +
		" state,food_id,food_name,price,unit,category,groups,cook_tag,cook_time,count,oper_time," +
		" oper_staff_id,oper_staff_name,start_time,trade_id,barcode,note,package_id,package_name,printer) values " +
		" ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ";

	String print_sql = "insert into tf_print_log ( PRINT_ID,REST_ID,BARCODE,PRINTER,PRINT_COUNT,CALL_TYPE,STATE, " +
		" TABLE_ID,TABLE_NAME,BILL_ID,NOP,ITEM_ID, " +
		" FOOD_ID,FOOD_NAME,PRICE,COUNT,BACK_COUNT,UNIT,NOTE,OPER_TIME,OPER_STAFF_ID,OPER_STAFF_NAME,PRINTER_START,PRINTER_HURRY,PRINTER_BACK,PRINTER_SEC ) values " +
		" ( (select nextval('print_id')) ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ";

	public List queryTableBills(String rest_id, String table_id) {
		List bills = jdbcTemplate.queryForList("select * from tf_bill where table_id = ? and rest_id = ? order by bill_id desc ", new Object[]{ table_id,rest_id });
		return bills;
	}
	
	public IData queryBillByTable(String rest_id, String table_id) {
		List bills = jdbcTemplate.queryForList("select * from tf_bill where table_id = ? and rest_id = ? and pay_type = '0' order by bill_id desc ", new Object[]{ table_id,rest_id });
		if(bills.size()==0){
			return null;
		}
		IData bill = new IData((Map)bills.get(0));
		return bill;
	}
	
	public IData queryBillByBillId(String bill_id, String rest_id) {
		List bills = jdbcTemplate.queryForList("select * from tf_bill where bill_id = ? and rest_id = ? order by bill_id desc ", 
				new Object[]{ bill_id,rest_id });
		if(bills.size()==0){
			return null;
		}
		IData bill = new IData((Map)bills.get(0));
		return bill;
	}

	public List queryBillFeeByBillId(String bill_id, String rest_id) {
		List items = jdbcTemplate.queryForList("select * from tf_bill_fee where bill_id = ? and rest_id = ?  ", new Object[]{ bill_id,rest_id });
		return items;
	}

	public List queryBillItemByBillId(String bill_id, String rest_id) {
		List fees = jdbcTemplate.queryForList("select * from tf_bill_item where bill_id = ? and rest_id = ? order by item_id desc ", new Object[]{ bill_id,rest_id });
		return fees;
	}
	
	public IData queryBillItemByItemId(String item_id, String rest_id) {
		List items = jdbcTemplate.queryForList("select * from tf_bill_item where item_id = ? and rest_id = ? order by item_id desc ", new Object[]{ item_id,rest_id });
		if(items.size()==0){
			return null;
		}
		IData item = new IData((Map)items.get(0));
		return item;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public IData createNewBill(IData param) {
		User user = (User)param.get("user");
		// 1. 插入账单表
		jdbcTemplate.update("insert into tf_bill (bill_id,rest_id,table_id,pay_type,nop,open_staff_id,open_staff_name,open_date,open_time) " +
				" values (?,?,?,?,?,?,?,?,?) " ,
				new Object[]{param.get("bill_id"),param.get("rest_id"),param.get("table_id"),"0",param.get("nop"),
				user.getStaff_id(),user.getStaffname(),ut.currentDate(),ut.currentTime()} );
		// 2. 置为占用状态  写入账单id
		jdbcTemplate.update("update td_table set state = '1'  where table_id = ? and rest_id = ? " ,
				new Object[]{param.getString("table_id"),param.get("rest_id")} );
		return queryBillByBillId(param.getString("bill_id"),param.getString("rest_id"));
	}
    
	public List queryTableList(IData param) {
//		ut.p(" queryTableList   rest_id:"+param.getString("rest_id"));
//		log.info(" queryTableList   rest_id:"+param.getString("rest_id"));
		List tables = jdbcTemplate.queryForList("select * from td_table where rest_id = ? and use_tag = '1' order by 0+table_id desc ", 
			new Object[]{ param.getString("rest_id") });
		return tables;
	}
    
	@Transactional(propagation=Propagation.REQUIRED)
	public String saveBillItems(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		String pay_type = jdbcTemplate.queryForObject("select pay_type from tf_bill where bill_id = ? ", new Object[]{bill.getString("BILL_ID")}, String.class);
		log.info(" saveBillItems  bill_id = "+bill.getString("BILL_ID")+" , pay_type = "+pay_type);
		if(pay_type.equals("1")){
			log.info(" 账单关闭，不能加菜  bill_id = "+bill.getString("BILL_ID")+" , pay_type = "+pay_type);
			return "";
		}
		jdbcTemplate.batchUpdate( add_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
				        return items.size();
				   }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					String item_id = getNewID("item_id");
					String barcode = getNewID("barcode");
				    IData item = (IData)items.get(i);
				    item.put("item_id", item_id);
				    item.put("barcode", barcode);
                    if(item.getString("CALL_TYPE").equals("1")){  //1 ： 即起     0 ： 叫起
                    	item.put("STATE", "1");
					}else {
						item.put("STATE", "0");
					}
				    pstmt.setString(1, item_id);
				    pstmt.setString(2, bill.getString("BILL_ID"));
				    pstmt.setString(3, bill.getString("REST_ID"));
				    if(item.containsKey("ITEM_TYPE")){
				    	pstmt.setString(4, item.getString("ITEM_TYPE") );
				    }else{
				    	pstmt.setString(4, "0" );
				    }
				    pstmt.setString(5, item.getString("CALL_TYPE"));
				    pstmt.setString(6, item.getString("STATE") );
				    pstmt.setString(7, item.getString("FOOD_ID"));
				    pstmt.setString(8, item.getString("FOOD_NAME"));
				    pstmt.setString(9, item.getString("PRICE"));
				    pstmt.setString(10, item.getString("UNIT"));
				    pstmt.setString(11, item.getString("CATEGORY"));
				    pstmt.setString(12, item.getString("GROUPS"));
				    pstmt.setString(13, item.getString("COOK_TAG"));
				    pstmt.setString(14, item.getString("COOK_TIME"));
				    pstmt.setString(15, item.getString("COUNT"));
				    pstmt.setString(16, now );
				    pstmt.setString(17, user.getStaff_id());
				    pstmt.setString(18, user.getStaffname());
				    pstmt.setString(19, item.getString("CALL_TYPE").equals("1")?now:"");
				    pstmt.setString(20, item.containsKey("TRADE_ID")?item.getString("TRADE_ID"):"");
				    pstmt.setString(21, item.getString("barcode"));
				    pstmt.setString(22, item.getString("NOTE").trim());
				    pstmt.setString(23, item.containsKey("PACKAGE_ID")?item.getString("PACKAGE_ID"):"");
				    pstmt.setString(24, item.containsKey("PACKAGE_NAME")?item.getString("PACKAGE_NAME"):"");
				    pstmt.setString(25, item.getString("PRINTER"));
				}
		});
		
		jdbcTemplate.batchUpdate( print_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
				        return items.size();
				   }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, bill.getString("REST_ID"));
				    pstmt.setString(2, item.getString("barcode"));
				    pstmt.setString(3, item.getString("PRINTER"));
				    pstmt.setString(4, item.getString("PRINT_COUNT"));
				    pstmt.setString(5, item.getString("CALL_TYPE"));
				    pstmt.setString(6, "0");
				    pstmt.setString(7, bill.getString("TABLE_ID"));
				    pstmt.setString(8, bill.getString("TABLE_ID"));
				    pstmt.setString(9, bill.getString("BILL_ID"));
				    pstmt.setString(10, bill.getString("NOP"));
				    pstmt.setString(11, item.getString("item_id"));
				    pstmt.setString(12, item.getString("FOOD_ID"));
				    pstmt.setString(13, item.getString("FOOD_NAME"));
				    pstmt.setString(14, item.getString("PRICE"));
				    pstmt.setString(15, item.getString("COUNT"));
				    pstmt.setString(16, "0");
				    pstmt.setString(17, item.getString("UNIT"));
				    pstmt.setString(18, item.getString("NOTE"));
				    pstmt.setString(19, now);
				    pstmt.setString(20, user.getStaff_id());
				    pstmt.setString(21, user.getStaffname());
				    pstmt.setString(22, item.getString("PRINTER_START"));
				    pstmt.setString(23, item.getString("PRINTER_HURRY"));
				    pstmt.setString(24, item.getString("PRINTER_BACK"));
				    pstmt.setString(25, item.getString("PRINTER_SEC"));
				}
		});
		return "";
	}

	public IData queryFoodById(IData param) {
		List foods = jdbcTemplate.queryForList("select * from td_food where food_id = ? and rest_id = ?  ", 
				new Object[]{ param.get("food_id"),param.get("rest_id") });
		if(foods.size()==0){
			return null;
		}
		IData food = new IData((Map)foods.get(0));
		return food;
	}
	
	public IData queryTempFood(IData param) {
		List foods = jdbcTemplate.queryForList("select * from td_food where food_id = '-1' and rest_id = 'ZZZZ'  ", 
				new Object[]{  });
		if(foods.size()==0){
			IData food = new IData();
			food.put("FOOD_ID", "-1");
			food.put("REST_ID", "ZZZZ");
			food.put("FOOD_NAME", "临时菜");
			food.put("ABBR", "LSC");
			food.put("PRICE", "0");
			food.put("UNIT", "份");
			food.put("CATEGORY", "临时菜");
			food.put("GROUPS", "临时菜");
			food.put("PRINTER", "不打印");
			food.put("PRINT_COUNT", "1");
			food.put("COOK_TAG", "N");
			food.put("COOK_TIME", "0");
			food.put("COUNT_TAG", "N");
			food.put("SHOW_TAG", "N");
			food.put("USE_TAG", "1");
			food.put("IMAGE", "");
			food.put("REMARK", "");
			food.put("PRINTER_START", "不打印");
			food.put("PRINTER_HURRY", "不打印");
			food.put("PRINTER_BACK", "不打印");
			food.put("PRINTER_SEC", "不打印");
			return food;
		}
		IData food = new IData((Map)foods.get(0));
		return food;
	}

	@Transactional(propagation=Propagation.REQUIRED) 
	public String cancelBillItems(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		ut.p(bill);
		String update_sql = " update tf_bill_item set back_count = ? where item_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return items.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, item.getString("BACK_COUNT"));
				    pstmt.setString(2, item.getString("ITEM_ID"));
				    pstmt.setString(3, bill.getString("REST_ID"));
				}
		});
		jdbcTemplate.batchUpdate( print_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
				        return items.size();
				   }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, bill.getString("REST_ID"));
				    pstmt.setString(2, item.getString("BARCODE"));
				    pstmt.setString(3, item.getString("PRINTER"));
				    pstmt.setString(4, "1");
//					    0 ： 即起   1 ： 叫起    2：  起菜   3：  催菜  4：  退菜
				    pstmt.setString(5, "4");
				    pstmt.setString(6, "0");
				    pstmt.setString(7, bill.getString("TABLE_ID"));
				    pstmt.setString(8, bill.getString("TABLE_ID"));
				    pstmt.setString(9, bill.getString("BILL_ID"));
				    pstmt.setString(10, bill.getString("NOP"));
				    pstmt.setString(11, item.getString("ITEM_ID"));
				    pstmt.setString(12, item.getString("FOOD_ID"));
				    pstmt.setString(13, item.getString("FOOD_NAME"));
				    pstmt.setString(14, item.getString("PRICE"));
				    pstmt.setString(15, item.getString("COUNT"));
				    pstmt.setString(16, item.getString("BACK_COUNT"));
				    pstmt.setString(17, item.getString("UNIT"));
				    pstmt.setString(18, item.getString("NOTE"));
				    pstmt.setString(19, now);
				    pstmt.setString(20, user.getStaff_id());
				    pstmt.setString(21, user.getStaffname());
				    pstmt.setString(22, item.getString("PRINTER_START"));
				    pstmt.setString(23, item.getString("PRINTER_HURRY"));
				    pstmt.setString(24, item.getString("PRINTER_BACK"));
				    pstmt.setString(25, item.getString("PRINTER_SEC"));
				}
		});
		return "";
	}

	public String freeBillItems(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		String update_sql = " update tf_bill_item set free_count = ? where item_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return items.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, item.getString("FREE_COUNT"));
				    pstmt.setString(2, item.getString("ITEM_ID"));
				    pstmt.setString(3, bill.getString("REST_ID"));
				}
		});
		return "";
	}
	
	public String derateBillItems(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		String update_sql = " update tf_bill_item set pay_rate = ? where item_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return items.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, item.getString("PAY_RATE"));
				    pstmt.setString(2, item.getString("ITEM_ID"));
				    pstmt.setString(3, bill.getString("REST_ID"));
				}
		});
		return "";
	}

	public String changeTableForBillItems(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		String update_sql = " update tf_bill_item set bill_id = ? , remark = ? where item_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return items.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, item.getString("BILL_ID"));
					pstmt.setString(2, item.getString("REMARK"));
				    pstmt.setString(3, item.getString("ITEM_ID"));
				    pstmt.setString(4, bill.getString("REST_ID"));
				}
		});
		return "";
	}

    //  STATE   0 ： 未打单   1 ： 已打单但未上菜   2 ： 已上菜
	@Transactional(propagation=Propagation.REQUIRED) 
	public String startCook(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		jdbcTemplate.batchUpdate( print_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
				        return items.size();
				   }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, bill.getString("REST_ID"));
				    pstmt.setString(2, item.getString("BARCODE"));
				    pstmt.setString(3, item.getString("PRINTER"));
				    pstmt.setString(4, item.getString("PRINT_COUNT"));
//				    0 ： 即起   1 ： 叫起    2：  起菜   3：  催菜  4：  退菜
				    pstmt.setString(5, "2");
				    pstmt.setString(6, "0");
				    pstmt.setString(7, bill.getString("TABLE_ID"));
				    pstmt.setString(8, bill.getString("TABLE_ID"));
				    pstmt.setString(9, bill.getString("BILL_ID"));
				    pstmt.setString(10, bill.getString("NOP"));
				    pstmt.setString(11, item.getString("ITEM_ID"));
				    pstmt.setString(12, item.getString("FOOD_ID"));
				    pstmt.setString(13, item.getString("FOOD_NAME"));
				    pstmt.setString(14, item.getString("PRICE"));
				    pstmt.setString(15, item.getString("COUNT"));
				    pstmt.setString(16, item.getString("BACK_COUNT"));
				    pstmt.setString(17, item.getString("UNIT"));
				    pstmt.setString(18, item.getString("NOTE"));
				    pstmt.setString(19, now);
				    pstmt.setString(20, user.getStaff_id());
				    pstmt.setString(21, user.getStaffname());
				    pstmt.setString(22, item.getString("PRINTER_START"));
				    pstmt.setString(23, item.getString("PRINTER_HURRY"));
				    pstmt.setString(24, item.getString("PRINTER_BACK"));
				    pstmt.setString(25, item.getString("PRINTER_SEC"));
				}
		});
		String update_sql = " update tf_bill_item set state = '1' ,start_time = ? where item_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return items.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, now);
				    pstmt.setString(2, item.getString("ITEM_ID"));
				    pstmt.setString(3, bill.getString("REST_ID"));
				}
		});
		return "";
	}

    //  STATE   0 ： 未打单   1 ： 已打单但未上菜   2 ： 已上菜
	@Transactional(propagation=Propagation.REQUIRED) 
	public String hurryCook(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		jdbcTemplate.batchUpdate( print_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
				        return items.size();
				   }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, bill.getString("REST_ID"));
				    pstmt.setString(2, item.getString("BARCODE"));
				    pstmt.setString(3, item.getString("PRINTER"));
				    pstmt.setString(4, item.getString("PRINT_COUNT"));
//				    0 ： 即起   1 ： 叫起    2：  起菜   3：  催菜  4：  退菜
				    pstmt.setString(5, "3");
				    pstmt.setString(6, "0");
				    pstmt.setString(7, bill.getString("TABLE_ID"));
				    pstmt.setString(8, bill.getString("TABLE_ID"));
				    pstmt.setString(9, bill.getString("BILL_ID"));
				    pstmt.setString(10, bill.getString("NOP"));
				    pstmt.setString(11, item.getString("ITEM_ID"));
				    pstmt.setString(12, item.getString("FOOD_ID"));
				    pstmt.setString(13, item.getString("FOOD_NAME"));
				    pstmt.setString(14, item.getString("PRICE"));
				    pstmt.setString(15, item.getString("COUNT"));
				    pstmt.setString(16, item.getString("BACK_COUNT"));
				    pstmt.setString(17, item.getString("UNIT"));
				    pstmt.setString(18, item.getString("NOTE"));
				    pstmt.setString(19, now);
				    pstmt.setString(20, user.getStaff_id());
				    pstmt.setString(21, user.getStaffname());
				    pstmt.setString(22, item.getString("PRINTER_START"));
				    pstmt.setString(23, item.getString("PRINTER_HURRY"));
				    pstmt.setString(24, item.getString("PRINTER_BACK"));
				    pstmt.setString(25, item.getString("PRINTER_SEC"));
				}
		});
		String update_sql = " update tf_bill_item set state = '1' , hurry_time = ? where item_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return items.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, now);
				    pstmt.setString(2, item.getString("ITEM_ID"));
				    pstmt.setString(3, bill.getString("REST_ID"));
				}
		});
		return "";
	}
	
	//  STATE   0 ： 未打单   1 ： 已打单但未上菜   2 ： 已上菜
	public String finishCook(IData param) {
		final IData bill =  (IData)param.get("bill");
		final List items = (List)param.get("items");
		final String now = ut.currentTime();
		String update_sql = " update tf_bill_item set state = '2' , end_time = ? where item_id = ? and rest_id = ? ";
		jdbcTemplate.batchUpdate( update_sql , 
			new BatchPreparedStatementSetter() {
				public int getBatchSize() {
			        return items.size();
			    }
				public void setValues(PreparedStatement pstmt, int i)
						throws SQLException {
					IData item = (IData)items.get(i);
					pstmt.setString(1, now);
				    pstmt.setString(2, item.getString("ITEM_ID"));
				    pstmt.setString(3, bill.getString("REST_ID"));
				}
		});
		return "";
	}

	public String payFee(IData param) {
		User user = (User)param.get("user");
		String now = ut.currentTime();
		jdbcTemplate.update("insert into tf_bill_fee (charge_id,bill_id,rest_id,mode_id,mode_name,fee,trade_id,user_id,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME) " +
				" values (?,?,?,?,?,?,?,?,?,?,? ) " ,
				new Object[]{param.get("charge_id"),param.get("bill_id"),param.get("rest_id"),param.get("mode_id"),param.get("mode_name"),param.get("recvfee"),"","",user.getStaff_id(),user.getStaffname(),now} );
		return ut.suc("交费成功");
	}

	public String reduceFee(IData param) {
		User user = (User)param.get("user");
		jdbcTemplate.update(" update tf_bill set reduce_fee = ? where bill_id = ? and rest_id = ?  " ,
				new Object[]{param.get("reducefee"),param.get("bill_id"),param.get("rest_id") } );
		return ut.suc("减免成功");
	}
    
	@Transactional(propagation=Propagation.REQUIRED) 
	public String closeBill(IData param) {
		final User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final String now = ut.currentTime();
		jdbcTemplate.update(" update tf_bill set PAY_TYPE = ? , BILL_FEE = ? , SPAY_FEE = ? , RECV_FEE = ? , DERATE_FEE = ? , CLOSE_DATE = ? , CLOSE_TIME = ? " +
				" where bill_id = ? and rest_id = ? " ,
				new Object[]{"1",bill.get("BILL_FEE"),bill.get("SPAY_FEE"),bill.get("RECV_FEE"),bill.get("DERATE_FEE"),ut.currentDate(),now,bill.get("BILL_ID"),bill.get("REST_ID")} );
		jdbcTemplate.update(" update td_table set STATE = '0' , BILL_ID = ''  where table_id = ? and rest_id = ? " ,
				new Object[]{bill.get("TABLE_ID"),bill.get("REST_ID")} );
		
		List items = this.queryBillItemByBillId(bill.get("BILL_ID").toString(),bill.get("REST_ID").toString());
		for(int i=0;i<items.size();i++){
			Map item = (Map)items.get(i);
			double cost_f = 0;
			List cost = jdbcTemplate.queryForList("select * from td_food_cost where rest_id = ? and food_id = ? ",
				new Object[]{user.getRest_id(),item.get("food_id")});
		    double total = 0;
			for(int j=0;j<cost.size();j++){
				Map rel = (Map)cost.get(j);
				Double count = Double.parseDouble(rel.get("count").toString());
				List materials = jdbcTemplate.queryForList("select * from td_material where material_id = ? and rest_id = ? ",
					new Object[]{rel.get("material_id"),user.getRest_id()});
			    if(materials.size()>0){
			    	Map material = (Map)materials.get(0);
			    	ut.log("material="+material);
			    	double price_m = 0;
			    	//ut.log(material);
			    	List cost2 = jdbcTemplate.queryForList("select * from td_material_cost where rest_id = ? and material_id = ? ",
			    		new Object[]{user.getRest_id(),material.get("material_id")});
					if(cost2.size()>0){
						Map rel2 = (Map)cost2.get(0);
						List stocks = jdbcTemplate.queryForList("select * from ts_stock where stock_id = ? ",new Object[]{rel2.get("stock_id")});
						int count1 = Integer.parseInt(rel2.get("count1").toString());
						int count2 = Integer.parseInt(rel2.get("count2").toString());
						if(cost.size()>0){
				        	Map stock = (Map)stocks.get(0);
				        	ut.log("stock="+stock);
				        	Double price = Double.parseDouble(stock.get("price").toString());
				        	price_m = price * count2 / count1;
				        }
					}
			    	ut.log("price_m = "+price_m);
			    	cost_f = cost_f + count*price_m;
			    }
			}
			ut.log("cost_f = "+cost_f);
			item.put("cost",ut.getDoubleString(cost_f));
			
			jdbcTemplate.update("update tf_bill_item set cost = ? , cost_str = ?  where item_id = ? and rest_id = ? ",
				new Object[]{item.get("cost"),"cost",item.get("item_id"),user.getRest_id()});
		}
		
		return "";
	}

//	public String payByCreditUser(IData param) {
//		User user = (User)param.get("user");
//		IData cust = (IData)param.get("cust");
//		ut.p(cust);
//		String now = ut.currentTime();
//		jdbcTemplate.update("insert into tf_bill_fee (charge_id,bill_id,rest_id,mode_id,mode_name,fee,trade_id,user_id,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME) " +
//				" values (?,?,?,?,?,?,?,?,?,?,? ) " ,
//				new Object[]{param.get("charge_id"),param.get("bill_id"),param.get("rest_id"),param.get("mode_id"),param.get("mode_name"),param.get("recvfee"),"",param.get("user_id"),user.getStaff_id(),user.getStaffname(),now} );
//		return ut.suc("交费成功");
//	}
//
//	public String payByVipCard(IData param) {
//		User user = (User)param.get("user");
//		IData cust = (IData)param.get("cust");
//		ut.p(cust);
//		String now = ut.currentTime();
//		jdbcTemplate.update("insert into tf_bill_fee (charge_id,bill_id,rest_id,mode_id,mode_name,fee,trade_id,user_id,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME) " +
//				" values (?,?,?,?,?,?,?,?,?,?,? ) " ,
//				new Object[]{param.get("charge_id"),param.get("bill_id"),param.get("rest_id"),param.get("mode_id"),param.get("mode_name"),param.get("recvfee"),"",param.get("user_id"),user.getStaff_id(),user.getStaffname(),now} );
//		return ut.suc("交费成功");
//	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public String payByCust(IData param) {
		User user = (User)param.get("user");
		
		String rest_id = user.getRest_id();
		if(rest_id.equals("mt")){
			rest_id = "kh";
		}
		
		IData cust = (IData)param.get("cust");
		String type = "1";
		String now = ut.currentTime();
		String remark = "于"+now+"账单消费"+param.get("recvfee")+"元,账单ID："+param.get("bill_id");
		//1. 记录交费日志 
		jdbcTemplate.update("insert into tf_bill_fee (charge_id,bill_id,rest_id,mode_id,mode_name,fee,trade_id,user_id,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME) " +
				" values (?,?,?,?,?,?,?,?,?,?,? ) " ,
				new Object[]{param.get("charge_id"),param.get("bill_id"),param.get("rest_id"),param.get("mode_id"),param.get("mode_name"),
				    param.get("recvfee"),"",param.get("user_id"),user.getStaff_id(),user.getStaffname(),now} );
		//2. 更新账户余额
		jdbcTemplate.update(" update tf_user set money = ? where user_id = ? and rest_id = ? " ,
				new Object[]{cust.get("NEW_MONEY"),cust.get("USER_ID"),rest_id} );
		//3. 记录存取款日志
		jdbcTemplate.update("insert into tf_user_fee (charge_id,user_id,rest_id,type,fee,old_money,new_money,out_trade_id,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME,REMARK) " +
				" values (?,?,?,?,?,?,?,?,?,?,?,? ) " ,
				new Object[]{param.get("charge_id"),cust.get("USER_ID"),param.get("rest_id"),type,param.get("recvfee"),
				cust.get("MONEY"),cust.get("NEW_MONEY"),param.get("bill_id"),user.getStaff_id(),user.getStaffname(),now,remark} );
		return ut.suc("交费成功");
	}

	@Transactional(propagation=Propagation.REQUIRED) 
	public String reopenBill(IData param) {
		User user = (User)param.get("user");
		final IData bill =  (IData)param.get("bill");
		final String now = ut.currentTime();
		//1. 更新餐台状态
		jdbcTemplate.update(" update td_table set state = '1' where table_id = ? and rest_id = ? " ,
				new Object[]{bill.get("TABLE_ID"),user.getRest_id()} );
		//2. 更新账单状态
		jdbcTemplate.update(" update tf_bill set pay_type = '0' , reopen = '1' , param1 = ? where bill_id = ? and rest_id = ? " ,
				new Object[]{now,bill.get("BILL_ID"),user.getRest_id()} );
		return ut.suc("账单已激活！");
	}

	public IData queryBillInfo(IData bill) {
		if(bill!=null&&bill.containsKey("BILL_ID")&&bill.containsKey("REST_ID")){
			List items =  this.queryBillItemByBillId(bill.getString("BILL_ID"), bill.getString("REST_ID"));
			List fees =   this.queryBillFeeByBillId(bill.getString("BILL_ID"), bill.getString("REST_ID"));
			bill.put("FEELIST", fees);
			bill.put("ITEMLIST", items);
			bill.put("PACKAGELIST", new ArrayList());
		}
		return bill;
	}

	public String finishToday() {
		jdbcTemplate.update(" update td_staff set password = '' where STAFF_ID = '' ");
		return "";
	}

	public String finishToday1() {
		jdbcTemplate.update(" update td_staff set CTRL_RIGHT = 'haha' where STAFF_ID = 'kl01' ");
		return "";
	}

	// 此方法会抛出异常   用于测试事务
	public String finishToday2() {
		jdbcTemplate.update(" update td_staff set CTRL_RIGHT = 'hehe' where user_id = 'kl01' ");
		return "";
	}

	public String backupPrintLog(IData param) {
		User user = (User)param.get("user");
		long t1 = System.currentTimeMillis();
		jdbcTemplate.update(" insert into th_print_log select * from tf_print_log where rest_id = ? ",new Object[]{user.getRest_id()});
		jdbcTemplate.update(" insert into th_print_bill_log select * from tf_print_bill_log where rest_id = ? ",new Object[]{user.getRest_id()});
		long t2 = System.currentTimeMillis();
		jdbcTemplate.update(" delete from tf_print_log where rest_id = ? ",new Object[]{user.getRest_id()});
		jdbcTemplate.update(" delete from tf_print_bill_log where rest_id = ? ",new Object[]{user.getRest_id()});
		long t3 = System.currentTimeMillis();
		log.info(" backupPrintLog  time1 = "+(t2-t1));
		log.info(" backupPrintLog  time2 = "+(t3-t2));
		return "";
	}

	public String backupTodayBill(IData param) {
		User user = (User)param.get("user");
		long t1 = System.currentTimeMillis();
		jdbcTemplate.update(" insert into th_bill select * from tf_bill where rest_id = ? ",new Object[]{user.getRest_id()});
		jdbcTemplate.update(" insert into th_bill_fee select * from tf_bill_fee where rest_id = ? ",new Object[]{user.getRest_id()});
		jdbcTemplate.update(" insert into th_bill_item select * from tf_bill_item where rest_id = ? ",new Object[]{user.getRest_id()});
		long t2 = System.currentTimeMillis();
		jdbcTemplate.update(" delete from tf_bill where rest_id = ? ",new Object[]{user.getRest_id()});
		jdbcTemplate.update(" delete from tf_bill_fee where rest_id = ? ",new Object[]{user.getRest_id()});
		jdbcTemplate.update(" delete from tf_bill_item where rest_id = ? ",new Object[]{user.getRest_id()});
		long t3 = System.currentTimeMillis();
		jdbcTemplate.update(" update td_table set state = '0' ,bill_id = '' where rest_id = ? ",new Object[]{user.getRest_id()});
		log.info(" backupTodayBill  time1 = "+(t2-t1));
		log.info(" backupTodayBill  time2 = "+(t3-t2));
		return "";
	}

	public String backupUserMoney(IData param) throws Exception {
		User user = (User)param.get("user");
		String path = GetBean.getContext().getServletContext().getRealPath("/")+"backup\\"+user.getRest_id()+"\\";
		File restdoc = new File(path);
		if(!restdoc.exists()){
			restdoc.mkdirs();
		}
		String fileName = "custmoney_"+ut.currentFileTime()+".bak";
        File file = new File(path+fileName);
		List result = jdbcTemplate.queryForList("select * from tf_user where rest_id = ? order by user_type,(0+user_id) ", 
			new Object[]{ user.getRest_id() });
		FileWriter fileWriter=new FileWriter(file);
		for(int i=0;i<result.size();i++){
			Map cust = (Map)result.get(i);
			JSONObject obj = JSONObject.fromObject(cust);
			fileWriter.write(obj.toString()+"\r\n");
		}
		fileWriter.flush();
		fileWriter.close();
		return "";
	}

	public String calculateData(IData param) {
		User user = (User)param.get("user");
		List items = jdbcTemplate.queryForList("select * from tf_bill_item ", new Object[]{  });
		for(int i=0;i<items.size();i++){
			Map item = (Map)items.get(i);
			double cost_f = 0;
			List cost = jdbcTemplate.queryForList("select * from td_food_cost where rest_id = ? and food_id = ? ",
				new Object[]{user.getRest_id(),item.get("food_id")});
		    double total = 0;
			for(int j=0;j<cost.size();j++){
				Map rel = (Map)cost.get(j);
				Double count = Double.parseDouble(rel.get("count").toString());
				List materials = jdbcTemplate.queryForList("select * from td_material where material_id = ? and rest_id = ? ",
					new Object[]{rel.get("material_id"),user.getRest_id()});
			    if(materials.size()>0){
			    	Map material = (Map)materials.get(0);
			    	ut.log("material="+material);
			    	double price_m = 0;
			    	//ut.log(material);
			    	List cost2 = jdbcTemplate.queryForList("select * from td_material_cost where rest_id = ? and material_id = ? ",
			    		new Object[]{user.getRest_id(),material.get("material_id")});
					if(cost2.size()>0){
						Map rel2 = (Map)cost2.get(0);
						List stocks = jdbcTemplate.queryForList("select * from ts_stock where stock_id = ? ",new Object[]{rel2.get("stock_id")});
						int count1 = Integer.parseInt(rel2.get("count1").toString());
						int count2 = Integer.parseInt(rel2.get("count2").toString());
						if(cost.size()>0){
				        	Map stock = (Map)stocks.get(0);
				        	ut.log("stock="+stock);
				        	Double price = Double.parseDouble(stock.get("price").toString());
				        	price_m = price * count2 / count1;
				        }
					}
			    	ut.log("price_m = "+price_m);
			    	cost_f = cost_f + count*price_m;
			    }
			}
			ut.log("cost_f = "+cost_f);
			item.put("cost",ut.getDoubleString(cost_f));
			
			jdbcTemplate.update("update tf_bill_item set cost = ? , cost_str = ?  where item_id = ? and rest_id = ? ",
				new Object[]{item.get("cost"),"cost",item.get("item_id"),user.getRest_id()});
		}
		return "";
	}

	public String finishToday(IData param) {
		// TODO Auto-generated method stub
		return null;
	}

	public String backupSysParam(IData param) throws Exception {
		User user = (User)param.get("user");
		String path = GetBean.getContext().getServletContext().getRealPath("/")+"backup\\"+user.getRest_id()+"\\";
		File restdoc = new File(path);
		if(!restdoc.exists()){
			restdoc.mkdirs();
		}
		String fileName = "table_"+ut.currentFileTime()+".bak";
        File file = new File(path+fileName);
		List result = jdbcTemplate.queryForList("select * from td_table where rest_id = ? order by 0+table_id ", 
			new Object[]{ user.getRest_id() });
		FileWriter fileWriter=new FileWriter(file);
		for(int i=0;i<result.size();i++){
			Map cust = (Map)result.get(i);
			JSONObject obj = JSONObject.fromObject(cust);
			fileWriter.write(obj.toString()+"\r\n");
		}
		fileWriter.flush();
		fileWriter.close();
		
		fileName = "food_"+ut.currentFileTime()+".bak";
        file = new File(path+fileName);
		result = jdbcTemplate.queryForList("select * from td_food where rest_id = ? order by food_id ", 
			new Object[]{ user.getRest_id() });
		fileWriter=new FileWriter(file);
		for(int i=0;i<result.size();i++){
			Map food = (Map)result.get(i);
			JSONObject obj = JSONObject.fromObject(food);
			fileWriter.write(obj.toString()+"\r\n");
		}
		fileWriter.flush();
		fileWriter.close();
		
		fileName = "restinfo_"+ut.currentFileTime()+".bak";
        file = new File(path+fileName);
		result = jdbcTemplate.queryForList("select * from td_restaurant where rest_id = ? ", 
			new Object[]{ user.getRest_id() });
		fileWriter=new FileWriter(file);
		for(int i=0;i<result.size();i++){
			Map info = (Map)result.get(i);
			JSONObject obj = JSONObject.fromObject(info);
			fileWriter.write(obj.toString()+"\r\n");
		}
		fileWriter.flush();
		fileWriter.close();
		
		fileName = "restparam_"+ut.currentFileTime()+".bak";
        file = new File(path+fileName);
		result = jdbcTemplate.queryForList("select * from td_restaurant_para where rest_id = ? ", 
			new Object[]{ user.getRest_id() });
		fileWriter=new FileWriter(file);
		for(int i=0;i<result.size();i++){
			Map info = (Map)result.get(i);
			JSONObject obj = JSONObject.fromObject(info);
			fileWriter.write(obj.toString()+"\r\n");
		}
		fileWriter.flush();
		fileWriter.close();
		return "";
	}

	
}
