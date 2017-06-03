package com.huai.operation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.huai.print.service.PrintService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huai.common.dao.BaseDao;
import com.huai.common.dao.CommonDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.operation.dao.OperationDao;
import com.huai.operation.dao.QueryDao;
import com.huai.operation.dao.TableDao;
import com.huai.common.util.*;

@Component("operationService")
public class OperationServiceImpl implements OperationService {
    
	private static final Logger log = Logger.getLogger(OperationServiceImpl.class);
	
	@Resource(name="baseDao")
	public BaseDao baseDao;
	
	@Resource(name="commonDao")
	public CommonDao commonDao;
	
	@Resource(name="operationDao")
	public OperationDao operationDao;
	
	@Resource(name="tableDao")
	public TableDao tableDao;
    
	@Resource(name="queryDao")
	public QueryDao queryDao;

	@Autowired
	public PrintService printService;
	
	
	public Map checkTableState(IData param) {
		IData table = tableDao.queryTableById(param.getString("rest_id"), param.getString("table_id"));
		Map result = new HashMap();
		if(table==null){
			result.put("success", "false");
			return result;
		}
		String state = table.getString("STATE");
		IData bill = operationDao.queryBillByTable(param.getString("rest_id"), param.getString("table_id"));
		if(bill!=null){
			state = "1";
			this.queryBillInfo(bill);
			result.put("bill", bill);
			if(!table.getString("STATE").equals("1")){
				baseDao.jdbcTemplate.update(" update td_table  set state = '1' where rest_id = ? and table_id = ? ",
					new Object[]{param.getString("rest_id"), param.getString("table_id")});
			}
		}else{
			state = "0";
			if(!table.getString("STATE").equals("0")){
				baseDao.jdbcTemplate.update(" update td_table  set state = '0' where rest_id = ? and table_id = ? ",
					new Object[]{param.getString("rest_id"), param.getString("table_id")});
			}
		}
		table.put("STATE", state);
		result.put("success", "true");
		result.put("state", state);
		result.put("table", table);
		return result;
	}

	public IData queryBillInfo(IData bill){
		bill = operationDao.queryBillInfo(bill);
		return bill;
	}
    
	public IData queryBillById(IData param) {
		IData bill = operationDao.queryBillByBillId(param.getString("BILL_ID"), param.getString("rest_id"));
		if(bill!=null){
			this.queryBillInfo(bill);
		}
		return bill;
	}
	
	public Map openTable(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByTable(param.getString("rest_id"), param.getString("table_id"));
		if(bill==null){
			String bill_id = baseDao.getNewID("bill_id");
			param.put("bill_id", bill_id);
			bill = operationDao.createNewBill(param);
		}
		this.queryBillInfo(bill);
		result.put("bill", bill);
		return result;
	}

	public Map queryAllTableState(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		List tables =  operationDao.queryTableList(param);
		result.put("tables", tables);
		return result;
	}
	
	public List queryTableList(IData param) {
		List tables =  operationDao.queryTableList(param);
		return tables;
	}

	public Map addBillItems(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,不能加菜！");
			return result;
		}
		JSONArray jsonArr = null;
		param.put("bill", bill);
		try {
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject item = jsonArr.getJSONObject(i); 
		        param.put("food_id", item.getString("food_id"));
		        IData food = operationDao.queryFoodById(param);
		        food.put("COUNT", item.getString("count"));
		        food.put("NOTE", item.getString("note"));
		        food.put("CALL_TYPE", item.getString("call_type"));
		        items.add(food);
			}
			param.put("items", items);
			String msg = operationDao.saveBillItems(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (Exception e) {
			result.put("success", "false");
			e.printStackTrace();
		}
		return result;
	}

	public Map addTempFood(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,不能加菜！");
			return result;
		}
		JSONObject item = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			item = JSONObject.fromObject(param.getString("item_str"));
			List items = new ArrayList();
	        param.put("food_id", "-1");
	        IData food = operationDao.queryTempFood(param);
	        food.put("FOOD_NAME", item.getString("food_name"));
	        food.put("PRICE", item.getString("price"));
	        food.put("COUNT", item.getString("count"));
	        food.put("NOTE", "");
	        food.put("CALL_TYPE", "1");
	        items.add(food);
			param.put("items", items);
			String msg = operationDao.saveBillItems(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (Exception e) {
			result.put("success", "false");
			e.printStackTrace();
		}
		return result;
	}

	public Map cancelFood(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		JSONArray jsonArr = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject obj = jsonArr.getJSONObject(i); 
		        String item_id = obj.getString("item_id");
		        IData item = operationDao.queryBillItemByItemId(item_id, param.getString("rest_id"));
		        double left_count = Double.parseDouble(item.getString("COUNT"))-Double.parseDouble(item.getString("FREE_COUNT"));
		        if(Double.parseDouble(param.getString("count"))>left_count){
		        	result.put("success", "false");
					result.put("msg", "退菜失败：[ "+item.getString("FOOD_NAME")+" ] 剩余数量不足！");
					return result;
		        }
		        item.put("BACK_COUNT", param.getString("count"));
		        param.put("food_id", item.getString("FOOD_ID"));
		        IData food = operationDao.queryFoodById(param);
		        item.put("PRINT_COUNT", food.getString("PRINT_COUNT"));
		        item.put("PRINTER_START", food.getString("PRINTER_START"));
		        item.put("PRINTER_HURRY", food.getString("PRINTER_HURRY"));
		        item.put("PRINTER_BACK", food.getString("PRINTER_BACK"));
		        item.put("PRINTER_SEC", food.getString("PRINTER_SEC"));
		        items.add(item);
			}
			param.put("items", items);
			String msg = operationDao.cancelBillItems(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "输入数字不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map presentFood(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		JSONArray jsonArr = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject food = jsonArr.getJSONObject(i); 
		        String item_id = food.getString("item_id");
		        IData item = operationDao.queryBillItemByItemId(item_id, param.getString("rest_id"));
		        double left_count = Double.parseDouble(item.getString("COUNT"))-Double.parseDouble(item.getString("BACK_COUNT"));
		        if(Double.parseDouble(param.getString("count"))>left_count){
		        	result.put("success", "false");
					result.put("msg", "赠送失败：[ "+item.getString("FOOD_NAME")+" ] 剩余数量不足！");
					return result;
		        }
		        item.put("FREE_COUNT", param.getString("count"));
		        items.add(item);
			}
			param.put("items", items);
			String msg = operationDao.freeBillItems(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "输入数字不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}
	
	public Map derateFood(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		JSONArray jsonArr = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			int rate = Integer.parseInt(param.getString("count"));
			if(rate<0||rate>100){
				result.put("success", "false");
				result.put("msg", "打折百分比必须为0-100之间的整数");
				return result;
			}
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject food = jsonArr.getJSONObject(i); 
		        String item_id = food.getString("item_id");
		        IData item = new IData();
		        item.put("ITEM_ID", item_id);
//		        IData item = operationDao.queryBillItemByItemId(item_id, param.getString("rest_id"));
		        item.put("PAY_RATE", param.getString("count"));
		        items.add(item);
			}
			param.put("items", items);
			String msg = operationDao.derateBillItems(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "输入数字不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map changeTable(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		JSONArray jsonArr = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			if(bill.getString("TABLE_ID").equals(param.getString("table_id"))){
				result.put("success", "false");
				result.put("msg", "输入的目标台位[ "+param.getString("table_id")+" ]不能为当前台位！");
				return result;
			}
			IData table = tableDao.queryTableById(param.getString("rest_id"),param.getString("table_id"));
			if(table==null){
				result.put("success", "false");
				result.put("msg", "输入的目标台位[ "+param.getString("table_id")+" ]不存在！");
				return result;
			}
			IData target_bill = operationDao.queryBillByTable(param.getString("rest_id"), param.getString("table_id"));
			if(target_bill==null){
				String new_bill_id = baseDao.getNewID("bill_id");
				param.put("bill_id", new_bill_id);
				param.put("nop", "3");
				target_bill = operationDao.createNewBill(param);
			}
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject food = jsonArr.getJSONObject(i);
		        String item_id = food.getString("item_id");
		        IData item = new IData();
		        item.put("ITEM_ID", item_id);
		        item.put("BILL_ID", target_bill.getString("BILL_ID"));
		        item.put("REMARK", bill.get("TABLE_ID")+"->"+param.getString("table_id"));
		        items.add(item);
			}
			param.put("items", items);
			String msg = operationDao.changeTableForBillItems(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map startCook(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		JSONArray jsonArr = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject obj = jsonArr.getJSONObject(i); 
		        String item_id = obj.getString("item_id");
		        IData item = operationDao.queryBillItemByItemId(item_id, param.getString("rest_id"));
		        if( item.getString("CALL_TYPE").equals("0") && !item.getString("STATE").equals("2") ){
		        	param.put("food_id", item.getString("FOOD_ID"));
			        IData food = operationDao.queryFoodById(param);
			        item.put("PRINT_COUNT", food.getString("PRINT_COUNT"));
			        item.put("PRINTER_START", food.getString("PRINTER_START"));
			        item.put("PRINTER_HURRY", food.getString("PRINTER_HURRY"));
			        item.put("PRINTER_BACK", food.getString("PRINTER_BACK"));
			        item.put("PRINTER_SEC", food.getString("PRINTER_SEC"));
			        items.add(item);
		        }
			}
			param.put("items", items);
			String msg = operationDao.startCook(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
			result.put("msg", "起菜成功！");
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}
	
	
	public Map hurryCook(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		JSONArray jsonArr = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject obj = jsonArr.getJSONObject(i); 
		        String item_id = obj.getString("item_id");
		        IData item = operationDao.queryBillItemByItemId(item_id, param.getString("rest_id"));
		        param.put("food_id", item.getString("FOOD_ID"));
		        IData food = operationDao.queryFoodById(param);
		        item.put("PRINT_COUNT", food.getString("PRINT_COUNT"));
		        item.put("PRINTER_START", food.getString("PRINTER_START"));
		        item.put("PRINTER_HURRY", food.getString("PRINTER_HURRY"));
		        item.put("PRINTER_BACK", food.getString("PRINTER_BACK"));
		        item.put("PRINTER_SEC", food.getString("PRINTER_SEC"));
		        items.add(item);
			}
			param.put("items", items);
			String msg = operationDao.hurryCook(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
			result.put("msg", "催菜成功！");
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map finishCook(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		JSONArray jsonArr = null;
		String now = ut.currentTime();
		param.put("bill", bill);
		try {
			jsonArr = JSONArray.fromObject(param.getString("item_str"));
			List items = new ArrayList();
			for(int i=0;i<jsonArr.size();i++){ 
		        JSONObject food = jsonArr.getJSONObject(i); 
		        String item_id = food.getString("item_id");
		        IData item = operationDao.queryBillItemByItemId(item_id, param.getString("rest_id"));
		        items.add(item);
			}
			param.put("items", items);
			String msg = operationDao.finishCook(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
			result.put("msg", "上菜成功！");
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}
	
	public Map payFee(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		param.put("bill", bill);
		try {
			int recvfee = Integer.parseInt(param.getString("recvfee"));
			if(recvfee==0){
				this.queryBillInfo(bill);
				result.put("bill", bill);
				return result;
			}
			param.put("recvfee", ""+recvfee);
			param.put("charge_id", ""+baseDao.getNewID("charge_id"));
			param.put("mode_name", CC.PAY_MODE.get(param.get("mode_id")));
			String msg = operationDao.payFee(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "输入金额不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map reduceFee(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		try {
			int reducefee = Integer.parseInt(param.getString("reducefee"));
			param.put("reducefee", ""+reducefee);
			String msg = operationDao.reduceFee(param);
			bill.put("REDUCE_FEE", ""+reducefee);
			this.queryBillInfo(bill);
			result.put("bill", bill);
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "输入金额不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map closeBill(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		result.put("bill", "");
		try {
			IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
			if(bill.getString("PAY_TYPE").equals("1")){
				result.put("msg", "此单已经封单！请关闭！");
				return result;
			}
			this.queryBillInfo(bill);
			BillUtil.calculateBill(bill);
			if(bill.has("CLOSE_FLAG")&&bill.getString("CLOSE_FLAG").equals("1")){
				param.put("bill", bill);
				operationDao.closeBill(param);
				bill.put("PAY_TYPE", "1");
				IData restinfo = commonDao.queryRestInfoById(param);
				bill.put("RESTNAME", restinfo.getString("RESTNAME"));
				bill.put("ADDRESS", restinfo.getString("ADDRESS"));
				bill.put("TELEPHONE", restinfo.getString("TELEPHONE"));
				IData table = tableDao.queryTableById(bill.getString("REST_ID"),bill.getString("TABLE_ID"));
				if(table!=null) {
		        	bill.put("PRINTER", table.get("PRINTER"));
		        }else{
		        	bill.put("PRINTER", "");
		        }
				result.put("bill", bill);
				result.put("msg", "封单成功！");

				printService.printBillByBillId(bill.getString("BILL_ID"));

			}else{
				result.put("success", "false");
				result.put("msg", bill.get("REMARK"));
			}
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！封单失败！");
			e.printStackTrace();
		}
		return result;
	}

	public Map payByCreditUser(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		User user = (User)param.get("user");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		param.put("bill", bill);
		try {
			int recvfee = Integer.parseInt(param.getString("recvfee"));
			if(recvfee==0){
				this.queryBillInfo(bill);
				result.put("bill", bill);
				return result;
			}
			IData cust = queryDao.queryCustById(param);
			if(cust==null){
				result.put("success", "false");
				result.put("msg", "查询不到用户信息！");
				return result;
			}
			int money = Integer.parseInt(cust.getString("MONEY"));
			int credit = Integer.parseInt(cust.getString("CREDIT"));
			if(money<recvfee){
				result.put("success", "false");
				result.put("msg", "用户可用挂帐金额："+money+"元，不够交费！");
				return result;
			}
			cust.put("NEW_MONEY", ""+(money-recvfee) );
			param.put("cust", cust);
			param.put("recvfee", ""+recvfee);
			param.put("charge_id", ""+baseDao.getNewID("charge_id"));
			param.put("mode_name", CC.PAY_MODE.get(param.get("mode_id")));
			String msg = operationDao.payByCust(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
			
			Map print_info = new HashMap();
			print_info.put("CUSTNAME", cust.get("CUSTNAME"));
			print_info.put("RESTNAME", user.getInfo().getString("RESTNAME"));
			print_info.put("OLD_MONEY", ""+(credit-money));
			print_info.put("PAYFEE", ""+recvfee);
			print_info.put("NEW_MONEY", ""+(credit-money+recvfee));
			print_info.put("STAFF_NAME", user.getStaffname());
			print_info.put("REMARK", "");
			print_info.put("ADDRESS", user.getInfo().getString("ADDRESS"));
			print_info.put("TELEPHONE", user.getInfo().getString("TELEPHONE"));
			result.put("info", print_info);
			
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "输入金额不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map payByVipCard(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		User user = (User)param.get("user");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill.getString("PAY_TYPE").equals("1")){
			result.put("success", "false");
			result.put("msg", "账单已经关闭,操作无效！");
			return result;
		}
		param.put("bill", bill);
		try {
			int recvfee = Integer.parseInt(param.getString("recvfee"));
			if(recvfee==0){
				this.queryBillInfo(bill);
				result.put("bill", bill);
				return result;
			}
			IData cust = queryDao.queryCustById(param);
			if(cust==null){
				result.put("success", "false");
				result.put("msg", "查询不到用户信息！");
				return result;
			}
			int money = Integer.parseInt(cust.getString("MONEY"));
			if(money<recvfee){
				result.put("success", "false");
				result.put("msg", "卡内余额："+money+"元，不够交费！");
				return result;
			}
			cust.put("NEW_MONEY", ""+(money-recvfee) );
			param.put("cust", cust);
			param.put("recvfee", ""+recvfee);
			param.put("charge_id", ""+baseDao.getNewID("charge_id"));
			param.put("mode_name", CC.PAY_MODE.get(param.get("mode_id")));
			String msg = operationDao.payByCust(param);
			this.queryBillInfo(bill);
			result.put("bill", bill);
			Map print_info = new HashMap();
			print_info.put("CARD_NO", cust.get("CARD_NO"));
			print_info.put("RESTNAME", user.getInfo().getString("RESTNAME"));
			print_info.put("OLD_MONEY", cust.getString("MONEY"));
			print_info.put("PAYFEE", ""+recvfee);
			print_info.put("NEW_MONEY", ""+(money-recvfee));
			print_info.put("STAFF_NAME", user.getStaffname());
			print_info.put("REMARK", "");
			print_info.put("ADDRESS", user.getInfo().getString("ADDRESS"));
			print_info.put("TELEPHONE", user.getInfo().getString("TELEPHONE"));
			result.put("info", print_info);
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "输入金额不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRED) 
	public Map finishToday(IData param) {
		Map result = new HashMap();
		if(checkFinish(param)){
			try{
				// 1. 计算今日营收数据  入统计结果表
//				operationDao.calculateData(param);
				// 2. 备份账单数据，清空账单表内数据
				operationDao.backupTodayBill(param);
				// 3. 备份打印数据，清空打印日志表内数据
				operationDao.backupPrintLog(param);
				// 4. 备份用户数据 生成文件
				operationDao.backupUserMoney(param);
				// 5. 备份系统参数数据 生成文件
				operationDao.backupSysParam(param);
				result.put("success", "true");
				result.put("msg", "结束营业成功，辛苦了一天，终于下班啦！");
			}catch (Exception e) {
				result.put("success", "false");
				result.put("msg", "程序异常！结束营业失败！请联系管理员！");
				e.printStackTrace();
			}
		}else{
			result.put("success", "false");
			result.put("msg", "还有账单未结账！不能结束营业！");
		}
		return result;
	}

	public boolean checkFinish(IData param) {
		List bills = baseDao.jdbcTemplate.queryForList(" select * from tf_bill where rest_id = ? and pay_type in ('0') ", 
			new Object[]{param.get("rest_id")});
		boolean flag = true;
		log.info(" checkFinish  bills : "+bills.size());
		if(bills.size()>0){
            for(int i=0;i<bills.size();i++){
            	IData bill = new IData((Map)bills.get(i));
            	operationDao.queryBillInfo(bill);
//            	log.info(bill);
            	List packages = (List)bill.get("PACKAGELIST");
        		List items = (List)bill.get("ITEMLIST");
        		List fees = (List)bill.get("FEELIST");
        		if(items.size()>0||fees.size()>0){
        			flag = false;
        			break;
        		}
        		log.info("   bill  是空单  : "+bill.getString("BILL_ID"));
//        		BillUtil.calculateBill(bill);
//            	if(bill.getString("REDUCE_FEE").equals("0.00")||bill.getString("REDUCE_FEE").equals("0")){
//            		param.put("bill", bill);
//            		this.closeBill(param);
//            	}
            }			
		}
		return flag;
	}

	public Map reopenBill(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		IData bill =  operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		param.put("bill", bill);
		IData table = tableDao.queryTableById(param.getString("rest_id"), bill.getString("TABLE_ID"));
		if(table.getString("STATE").equals("0")){
			operationDao.reopenBill(param);
			result.put("msg", "账单已激活！");
		}else{
			result.put("success", "false");
			result.put("msg", "台位正在使用，无法激活账单！");
		}
		return result;
	}

	public Map queryTableById(IData param) {
		log.info(" queryTableById   param : "+param);
		IData table = tableDao.queryTableById(param.getString("rest_id"), param.getString("table_id"));
		log.info(" queryTableById   table : "+table);
		IData bill = operationDao.queryBillByTable(param.getString("rest_id"), param.getString("table_id"));
		log.info(" queryTableById   bill : "+bill);
		if(bill!=null){
			table.put("bill_id", bill.get("BILL_ID"));
			table.put("BILL_ID", bill.get("BILL_ID"));
		}else{
			
		}
		return table;
	}

	public Map queryBillItemByItemId(IData param) {
		IData item = operationDao.queryBillItemByItemId(param.getString("item_id"), param.getString("rest_id"));
		return item;
	}

    
}


