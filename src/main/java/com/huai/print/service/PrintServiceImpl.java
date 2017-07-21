package com.huai.print.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.huai.operation.service.QueryService;
import com.huai.print.util.PrinterUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
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

	@Resource(name="queryService")
	public QueryService queryService;

	public Map queryFoodPrintList(IData param) {
		IData restinfo = printDao.queryRestByAppId(param.getString("appid"));
		Map result = new HashMap();
		result.put("success", "true");
		if(restinfo==null){
			result.put("success", "false");
			result.put("msg", " 无餐厅信息 ");
			return result;
		}
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
		IData bill = operationDao.queryBillByBillId(param.getString("bill_id") );
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
		IData table = tableDao.queryTableById( bill.getString("TABLE_ID"));
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
		IData bill = operationDao.queryBillByTable( param.getString("table_id"));
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
		IData bill = operationDao.queryBillByBillId(  param.getString("bill_id"));
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
		IData bill = operationDao.queryBillByTable(table_id);

		log.info(" printBill  bill = "+bill);

		param.put("table_id", bill.getString("table_id"));
		Map table = tableDao.queryTableById(table_id);

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
		IData bill = operationDao.queryBillByBillId(bill_id);
		log.info(" printBill  bill = "+bill);

		param.put("table_id", bill.getString("TABLE_ID"));
		Map table = tableDao.queryTableById(bill.getString("TABLE_ID"));

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
	public Map printCategory() {
		Map result = new HashMap();
		result.put("success", "true");
		IData param = new IData();
		IData data = new IData(queryService.queryTodayData(param));

		IData restinfo = commonDao.queryRestInfoById(param);

		data.put("printer", restinfo.getString("PRINTER"));

		PrinterUtil.printCategory(data);
		return result;
	}

	@Override
	public Map printTodayMoney() {
		Map result = new HashMap();
		result.put("success", "true");

		IData param = new IData();
		IData data = new IData(queryService.queryTodayData(param));

		IData restinfo = commonDao.queryRestInfoById(param);
		data.put("printer", restinfo.getString("PRINTER"));

		PrinterUtil.printTodayMoney(data);
		return result;
	}

}


