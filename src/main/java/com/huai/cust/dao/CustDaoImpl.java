package com.huai.cust.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.util.*;

@Component("custDao")
public class CustDaoImpl extends BaseDao implements CustDao {

	public String payfeeForCredit(IData param) {
		User user = (User)param.get("user");
		IData cust = (IData)param.get("cust");
		//`TYPE` varchar(20) NOT NULL default '1' COMMENT '类型:0 - 充值 ; 1- 账单消费; 2 - 抵扣欠款 ; 3 - 挂账冲正 '
		String type = "3";
		String now = ut.currentTime();
		String remark = "于"+now+" 挂账冲正 : "+param.get("recvfee")+"元";
		//2. 更新账户余额
		jdbcTemplate.update(" update tf_user set money = ? where user_id = ? " ,
				new Object[]{cust.get("NEW_MONEY"),cust.get("USER_ID")} );
		//3. 记录存取款日志
		jdbcTemplate.update("insert into tf_user_fee (charge_id,user_id,type,fee,old_money,new_money,out_trade_id,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME,REMARK) " +
				" values (?,?,?,?,?,?,?,?,?,?,? ) " ,
				new Object[]{param.get("charge_id"),cust.get("USER_ID"),type,param.get("recvfee"),
				cust.get("MONEY"),cust.get("NEW_MONEY"),"",user.getStaffId(),user.getStaffname(),now,remark} );
		return ut.suc("交费成功");
	}

	public String addCreditUser(IData param) {
		User user = (User)param.get("user");
		String now = ut.currentTime();
		jdbcTemplate.update("insert into tf_user (user_id,user_type,card_no,level,custname,phone,money,credit,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME,REMARK) " +
				" values (?,?,?,?,?,?,?,?,?,?,?,? ) " ,
				new Object[]{param.get("user_id"),"G",param.get("user_id"),"0",param.get("custname"),
				param.get("phone"),param.get("credit"),param.get("credit"),user.getStaffId(),user.getStaffname(),now,param.get("remark")} );
		return ut.suc("开户成功");
	}

	public String updateCreditUser(IData param) {
		String now = ut.currentTime();
		jdbcTemplate.update(" update tf_user set custname = ? ,phone = ? , credit = ? ,REMARK = ? where user_id = ? and user_type = 'G' " ,
			new Object[]{ param.get("custname"), param.get("phone"),param.get("credit"), param.get("remark"),param.get("user_id")} );
		return ut.suc("更新成功");
	}

	public List queryCustByCardNo(IData param) {
		String sql = " select * from tf_user where card_no = ?  and user_type = ? order by user_id ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("card_no")  , param.getString("user_type") });
		return data;
	}

	
	public List queryCustByPhone(IData param) {
		User user = (User)param.get("user");
		String sql = " select * from tf_user where phone = ? and user_type = ? order by user_id ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("phone"),  param.getString("user_type")  });
		return data;
	}
	
	public List queryCustByImei(IData param) {
		String sql = " select a.imei,b.* from tf_card a , tf_user b where a.imei = ?  and a.CARD_NO = b.CARD_NO and b.user_type = 'C' ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("imei") });
		return data;
	}

	public String updateVipCard(IData param) {
		String now = ut.currentTime();
		jdbcTemplate.update(" update tf_user set custname = ? ,phone = ? , REMARK = ? where user_id = ? and user_type = 'C' " ,
			new Object[]{ param.get("custname"), param.get("phone"),param.get("remark"),param.get("user_id")} );
		return ut.suc("更新成功");
	}

	public String payfeeForVipCard(IData param) {
		User user = (User)param.get("user");
		IData cust = (IData)param.get("cust");
		//`TYPE` varchar(20) NOT NULL default '1' COMMENT '类型:0 - 充值 ; 1- 账单消费; 2 - 抵扣欠款 ; 3 - 挂账冲正 '
		String type = "3";
		String now = ut.currentTime();
		String remark = "于"+now+" 充值 : "+param.get("recvfee")+"元";
		//2. 更新账户余额
		jdbcTemplate.update(" update tf_user set money = ? where user_id = ? " ,
				new Object[]{cust.get("NEW_MONEY"),cust.get("USER_ID")} );
		//3. 记录存取款日志
		jdbcTemplate.update("insert into tf_user_fee (charge_id,user_id,type,fee,old_money,new_money,out_trade_id,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME,REMARK) " +
				" values (?,?,?,?,?,?,?,?,?,?,? ) " ,
				new Object[]{param.get("charge_id"),cust.get("USER_ID"),type,param.get("recvfee"),
				cust.get("MONEY"),cust.get("NEW_MONEY"),"",user.getStaffId(),user.getStaffname(),now,remark} );
		return ut.suc("充值成功");
	}

	public String addVipCard(IData param) {
		User user = (User)param.get("user");
		String now = ut.currentTime();
		jdbcTemplate.update("insert into tf_user (user_id,user_type,card_no,level,custname,phone,money,credit,OPER_STAFF_ID,OPER_STAFF_NAME,OPER_TIME,REMARK) " +
				" values (?,?,?,?,?,?,?,?,?,?,?,? ) " ,
				new Object[]{param.get("user_id"),"C",param.get("card_no"),"0",param.get("custname"),
				param.get("phone"),param.get("money"),"0",user.getStaffId(),user.getStaffname(),now,param.get("remark")} );
		return ut.suc("开卡成功");
	}

	public IData queryCardByCardNo(IData param) {
		String sql = " select * from tf_card where card_no = ? ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("card_no")  });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}
	
	public IData queryCardByImei(IData param) {
		String sql = " select * from tf_card where imei = ? ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("imei") });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}


}
