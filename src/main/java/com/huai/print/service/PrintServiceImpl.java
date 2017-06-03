package com.huai.print.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.dao.CommonDao;
import com.huai.common.domain.IData;
import com.huai.operation.dao.OperationDao;
import com.huai.operation.dao.TableDao;
import com.huai.print.dao.PrintDao;
import com.huai.common.util.*;

@Component("printService")
public class PrintServiceImpl implements PrintService {

	private static final Logger log = Logger.getLogger(PrintServiceImpl.class);

	@Resource(name="printDao")
	public PrintDao printDao;
	
	@Resource(name="operationDao")
	public OperationDao operationDao;
	
	@Resource(name="commonDao")
	public CommonDao commonDao;
	
	@Resource(name="tableDao")
	public TableDao tableDao;

	public Map queryFoodPrintList(IData param) {
		IData restinfo = printDao.queryRestByAppId(param.getString("appid"));
		Map result = new HashMap();
		result.put("success", "true");
		if(restinfo==null){
			result.put("success", "false");
			result.put("msg", " 无餐厅信息 ");
			return result;
		}
		param.put("rest_id", restinfo.getString("REST_ID"));
		List foods = printDao.queryFoodPrintList(param);
		ut.p("foods.size()="+foods.size());
		result.put("foods", foods);
		return result;
	}

	public Map queryBillPrintList(IData param) {
		IData restinfo = printDao.queryRestByAppId(param.getString("appid"));
		Map result = new HashMap();
		result.put("success", "true");
		if(restinfo==null){
			result.put("success", "false");
			result.put("msg", " 无餐厅信息 ");
			return result;
		}
		param.put("rest_id", restinfo.getString("REST_ID"));
		List bills = printDao.queryBillPrintList(param);
		ut.p("bills.size()="+bills.size());
		result.put("bills", bills);
		return result;
	}

	public Map queryPrintBillInfo(IData param) {
		IData restinfo = printDao.queryRestByAppId(param.getString("appid"));
		Map result = new HashMap();
		result.put("success", "true");
		if(restinfo==null){
			result.put("success", "false");
			result.put("msg", " 无餐厅信息 ");
			return result;
		}
		param.put("rest_id", restinfo.getString("REST_ID"));
		IData bill = operationDao.queryBillByBillId(param.getString("bill_id"), restinfo.getString("REST_ID"));
		if(bill==null){
			result.put("success", "false");
			result.put("msg", " 无账单 ");
		}else{
			operationDao.queryBillInfo(bill);
			quqryPrintRestInfo(bill);
			bill.put("RESTNAME", restinfo.getString("RESTNAME"));
			bill.put("ADDRESS", restinfo.getString("ADDRESS"));
			bill.put("TELEPHONE", restinfo.getString("TELEPHONE"));
			result.put("bill", bill);
		}
		return result;
	}

	private IData quqryPrintRestInfo(IData bill) {
//		log.info("bill:"+bill);
		IData table = tableDao.queryTableById(bill.getString("REST_ID"),bill.getString("TABLE_ID"));
//		log.info("table:"+table);
		if(table!=null) {
        	bill.put("PRINTER", table.get("PRINTER"));
        }else{
        	bill.put("PRINTER", "");
        }
		return bill;
	}

	public Map queryPrintBillInfoByTable(IData param) {
		IData restinfo = commonDao.queryRestInfoById(param);
		Map result = new HashMap();
		result.put("success", "true");
		IData bill = operationDao.queryBillByTable(param.getString("rest_id"), param.getString("table_id"));
		if(bill==null){
			result.put("success", "false");
			result.put("msg", " 无账单 ");
		}else{
			operationDao.queryBillInfo(bill);
			quqryPrintRestInfo(bill);
			bill.put("RESTNAME", restinfo.getString("RESTNAME"));
			bill.put("ADDRESS", restinfo.getString("ADDRESS"));
			bill.put("TELEPHONE", restinfo.getString("TELEPHONE"));
			result.put("bill", bill);
		}
//		log.info("bill:"+bill);
		return result;
	}

	public Map queryPrintBillInfoByBillId(IData param) {
		IData restinfo = commonDao.queryRestInfoById(param);
		Map result = new HashMap();
		result.put("success", "true");
		IData bill = operationDao.queryBillByBillId(param.getString("bill_id"), param.getString("rest_id"));
		if(bill==null){
			result.put("success", "false");
			result.put("msg", " 无账单 ");
		}else{
			operationDao.queryBillInfo(bill);
			quqryPrintRestInfo(bill);
			bill.put("RESTNAME", restinfo.getString("RESTNAME"));
			bill.put("ADDRESS", restinfo.getString("ADDRESS"));
			bill.put("TELEPHONE", restinfo.getString("TELEPHONE"));
			result.put("bill", bill);
		}
//		log.info("bill:"+bill);
		return result;
	}

	@Override
	public Map printBill(String table_id) {
		Map result = new HashMap();
		result.put("success", "true");
		IData param = new IData();
		IData bill = operationDao.queryBillByTable(null,table_id);

		log.info(" printBill  bill = "+bill);

		param.put("table_id", bill.getString("table_id"));
		Map table = tableDao.queryTableById(null,table_id);

		log.info(" printBill  table = "+table);
		param.put("bill_id", bill.get("BILL_ID"));
		param.put("printer", table.get("PRINTER"));
		int n = printDao.addPrintBillLog(param);
		if(n == 0){
			result.put("success", "false");
			result.put("msg", "打印账单异常");
		}
		return result;
	}

	@Override
	public Map printBillByBillId(String bill_id) {
		Map result = new HashMap();
		result.put("success", "true");
		IData param = new IData();
		IData bill = operationDao.queryBillByBillId(bill_id,null);
		log.info(" printBill  bill = "+bill);

		param.put("table_id", bill.getString("TABLE_ID"));
		Map table = tableDao.queryTableById(null,bill.getString("TABLE_ID"));

		log.info(" printBill  table = "+table);
		param.put("bill_id", bill.get("BILL_ID"));
		param.put("printer", table.get("PRINTER"));
		int n = printDao.addPrintBillLog(param);
		if(n == 0){
			result.put("success", "false");
			result.put("msg", "打印账单异常");
		}
		return result;
	}


}


