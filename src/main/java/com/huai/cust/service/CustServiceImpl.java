package com.huai.cust.service;

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
import com.huai.operation.dao.QueryDao;
import com.huai.common.util.*;
import com.huai.cust.dao.CustDao;

@Component("custService")
public class CustServiceImpl implements CustService {

	@Resource(name="baseDao")
	public BaseDao baseDao;

	@Resource(name="queryDao")
	public QueryDao queryDao;
	
	@Resource(name="custDao")
	public CustDao custDao;

	public Map payfeeForCredit(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		User user = (User)param.get("user");
		try {
			int recvfee = Integer.parseInt(param.getString("recvfee"));
			if(recvfee==0){
				result.put("success", "false");
				result.put("msg", "交费金额为0");
				return result;
			}
			if(recvfee>999999||recvfee<-999999){
				result.put("success", "false");
				result.put("msg", "交费金额超限");
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
			cust.put("NEW_MONEY", ""+(money+recvfee) );
			param.put("cust", cust);
			param.put("recvfee", ""+recvfee);
			param.put("charge_id", ""+baseDao.getNewID("charge_id"));
			String msg = custDao.payfeeForCredit(param);
			result.put("msg", "交费冲正成功");
			
			Map print_info = new HashMap();
			print_info.put("CUSTNAME", cust.get("CUSTNAME"));
			print_info.put("RESTNAME", user.getInfo().getString("RESTNAME"));
			print_info.put("OLD_MONEY", ""+(credit-money));
			print_info.put("PAYFEE", ""+recvfee);
			print_info.put("NEW_MONEY", ""+(credit-money-recvfee));
			print_info.put("STAFF_NAME", user.getStaffname());
			print_info.put("REMARK", "交费冲正成功");
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

	public Map addCreditUser(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		try {
			int credit = Integer.parseInt(param.getString("credit"));
			if(credit <= 0){
				result.put("success", "false");
				result.put("msg", "信用额度必须为正数");
				return result;
			}
			if(ut.isEmpty(param.getString("custname"))){
				result.put("success", "false");
				result.put("msg", "请输入客户姓名");
				return result;
			}
			String user_id = baseDao.getNewID("user_id");
			param.put("user_id", user_id);
			String msg = custDao.addCreditUser(param);
			result.put("msg", "挂账用户创建成功");
			result.put("user_id", user_id);
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "信用额度不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map updateCreditUser(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		try {
			int credit = Integer.parseInt(param.getString("credit"));
			if(credit <= 0){
				result.put("success", "false");
				result.put("msg", "信用额度必须为正数");
				return result;
			}
			if(ut.isEmpty(param.getString("custname"))){
				result.put("success", "false");
				result.put("msg", "请输入客户姓名");
				return result;
			}
			String msg = custDao.updateCreditUser(param);
			result.put("msg", "更新成功");
		} catch (NumberFormatException e) {
			result.put("success", "false");
			result.put("msg", "信用额度不合法！");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map queryVipCardList(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		try {
			String query_str = param.getString("query_str");
			if(ut.isEmpty(query_str)){
				result.put("success", "false");
				result.put("msg", "请填写查询条件！");
				return result;
			}
			List custList = null;
			
			param.put("user_type", "C");
			if(query_str.trim().length()<11){
				param.put("card_no", query_str);
				custList = custDao.queryCustByCardNo(param);
			}else if(query_str.trim().length()==11){
				param.put("phone", query_str);
				custList = custDao.queryCustByPhone(param);
			}else{
				param.put("imei", query_str.trim());
				custList = custDao.queryCustByImei(param);
			}
			result.put("data", custList);
			result.put("msg", "查询成功");
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "查询异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map updateVipCard(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		try {
			if(ut.isEmpty(param.getString("custname"))){
				result.put("success", "false");
				result.put("msg", "请输入客户姓名");
				return result;
			}
			String msg = custDao.updateVipCard(param);
			result.put("msg", "更新成功");
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}

	public Map payfeeForVipCard(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		User user = (User)param.get("user");
		try {
			int recvfee = Integer.parseInt(param.getString("recvfee"));
			if(recvfee==0){
				result.put("success", "false");
				result.put("msg", "交费金额为0");
				return result;
			}
			if(recvfee>999999||recvfee<-999999){
				result.put("success", "false");
				result.put("msg", "交费金额超限");
				return result;
			}
			IData cust = queryDao.queryCustById(param);
			if(cust==null){
				result.put("success", "false");
				result.put("msg", "查询不到用户信息！");
				return result;
			}
			int money = Integer.parseInt(cust.getString("MONEY"));
			cust.put("NEW_MONEY", ""+(money+recvfee) );
			param.put("cust", cust);
			param.put("recvfee", ""+recvfee);
			param.put("charge_id", ""+baseDao.getNewID("charge_id"));
			String msg = custDao.payfeeForVipCard(param);
			result.put("msg", "充值成功");
			
			Map print_info = new HashMap();
			print_info.put("CARD_NO", cust.get("CARD_NO"));
			print_info.put("RESTNAME", user.getInfo().getString("RESTNAME"));
			print_info.put("OLD_MONEY", cust.getString("MONEY"));
			print_info.put("PAYFEE", ""+recvfee);
			print_info.put("NEW_MONEY", ""+(money+recvfee));
			print_info.put("STAFF_NAME", user.getStaffname());
			print_info.put("REMARK", "充值成功");
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

	public Map addVipCard(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		try {
			if(ut.isEmpty(param.getString("custname"))){
				result.put("success", "false");
				result.put("msg", "请输入客户姓名");
				return result;
			}
			if(ut.isEmpty(param.getString("card_no"))){
				result.put("success", "false");
				result.put("msg", "请输入卡号");
				return result;
			}
//			IData card = custDao.queryCardByCardNo(param);
//			if(card==null){
//				result.put("success", "false");
//				result.put("msg", "卡号不存在！请先制卡！");
//				return result;
//			}else if(card.getString("USE_TAG").equals("1")){
//				result.put("success", "false");
//				result.put("msg", "此卡已被使用！不能重复开户！");
//				return result;
//			}
			IData cust = queryDao.queryCustByCardNo(param);
			if(cust!=null){
				result.put("success", "false");
				result.put("msg", "卡号已存在客户资料！");
				return result;
			}
			String user_id = baseDao.getNewID("user_id");
			param.put("user_id", user_id);
			param.put("money", "0");
			String msg = custDao.addVipCard(param);
			result.put("msg", "会员卡创建成功");
			result.put("user_id", user_id);
		} catch (Exception e) {
			result.put("success", "false");
			result.put("msg", "程序异常！");
			e.printStackTrace();
		}
		return result;
	}
	

	

}


