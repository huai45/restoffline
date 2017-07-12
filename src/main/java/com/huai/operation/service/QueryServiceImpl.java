package com.huai.operation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.operation.dao.OperationDao;
import com.huai.operation.dao.QueryDao;
import com.huai.operation.dao.TableDao;
import com.huai.common.util.*;

@Component("queryService")
public class QueryServiceImpl implements QueryService {

	private static final Logger log = Logger.getLogger(QueryServiceImpl.class);
	
	
	@Resource(name="baseDao")
	public BaseDao baseDao;
	
	@Resource(name="operationService")
	public OperationService operationService;
	
	@Resource(name="queryDao")
	public QueryDao queryDao;
	
	@Resource(name="operationDao")
	public OperationDao operationDao;
	
	@Resource(name="tableDao")
	public TableDao tableDao;

	public IData queryCustById(IData param) {
		IData cust = queryDao.queryCustById(param);
		return cust;
	}
	
	public IData queryVipCardUserInfo(IData param) {
		IData cust = queryDao.queryCustByCardNo(param);
		return cust;
	}
	
	// 如果传入的card_no不为空  则只查询匹配卡号的这个会员信息返回 ， 否则查询所有会员信息
	public Map queryVipCardUserList(IData param) {
		Map result = new HashMap();
		User user = (User)param.get("user");
		result.put("success", "true");
		List data = new ArrayList();
		if(param.get("card_no")!=null&&!param.getString("card_no").trim().equals("")){
			IData cust = queryDao.queryCustByCardNo(param);
			if(cust!=null){
				data.add(cust);
			}
		}else{
			param.put("user_type", "C");
			data = queryDao.queryCustList(param);
		}
		result.put("data", data);
		return result;
	}
	
	public Map queryCreditUserList(IData param) {
		Map result = new HashMap();
		User user = (User)param.get("user");
		result.put("success", "true");
		param.put("user_type", "G");
		List data = queryDao.queryCustList(param);
		result.put("data", data);
		log.info(" queryCreditUserList   data.size() ： "+data.size());
		return result;
	}
	
	public Map queryTodayData(IData param) {
		Map result = new HashMap();
		User user = (User)param.get("user");
		log.info(" queryTodayData , user.getStaffId() = "+user.getStaffId());
		result.put("success", "true");
		String sql_recv = " select mode_id,mode_name , sum(fee) recv_fee from tf_bill_fee where 1 = 1 group by mode_id,mode_name order by  mode_id ";
		List data_recv = baseDao.jdbcTemplate.queryForList(sql_recv,new Object[]{  });
		result.put("recv_data", data_recv);
		log.info(" recv_data ： "+data_recv);
		String floor = " select a.floor, ifnull(b.money,0) money from " +
			" ( select distinct floor1 floor from td_table where 1 = 1 order by floor_order ) a " +
			" left join " +
			" ( select  b.floor1 floor, sum(a.recv_fee) money from tf_bill a, td_table b where a.table_id = b.table_id  group by b.floor1 ) b " +
			" on a.floor = b.floor ";
		List data_floor = baseDao.jdbcTemplate.queryForList(floor,new Object[]{   });
		result.put("floor_data", data_floor);
		
		String item_money = " select ifnull(sum(reduce_fee),0) moling_money , ifnull( (sum(bill_fee)-(sum(derate_fee)+sum(reduce_fee)+sum(recv_fee))) ,0) lose_money ," +
				" ifnull(sum(spay_fee),0) spay_fee, ifnull(sum(derate_fee),0) discount_money, ifnull(sum(recv_fee),0) recv_money, ifnull(sum(nop),0) total_person " +
				" from tf_bill  where 1 = 1  ";
		List data_item = baseDao.jdbcTemplate.queryForList(item_money,new Object[]{  });
		result.put("item_data", data_item);
		
		String bill_count = " select ifnull( sum(case when pay_type = '1' then 1 else 0 end) ,0)  close_count , " +
				" ifnull( sum(case when pay_type = '0' then 1 else 0 end),0) open_count  from tf_bill  where 1 = 1   ";
		List bill_count_data = baseDao.jdbcTemplate.queryForList(bill_count,new Object[]{ });
		result.put("bill_count_data", bill_count_data);
		
		String unrecv_money = " select ifnull(sum(a.fee),0) money from  "+
	        " ( select b.bill_id  ,sum(a.price*(a.count-a.back_count-a.free_count)*a.pay_rate/100) -b.reduce_fee fee " +
	        " from tf_bill_item a , tf_bill b "+
	        " where a.bill_id = b.bill_id  and b.pay_type = '0' group by a.bill_id  ) a ";
		List data_unrecv = baseDao.jdbcTemplate.queryForList(unrecv_money,new Object[]{  });
		result.put("unrecv_data",data_unrecv);
		
		String category_sql = " select groups category , sum(price*count*pay_rate/100) money ," +
				"  sum(price*(count-back_count-free_count)*pay_rate/100) real_money from tf_bill_item a where 1 = 1 group by groups " +
				"  union all select '合计',sum(price*count*pay_rate/100),sum(price*(count-back_count-free_count)*pay_rate/100) " +
				" from tf_bill_item a where 1 = 1 ";
		List data_category = baseDao.jdbcTemplate.queryForList(category_sql,new Object[]{  });
		result.put("category_data",data_category);

		List data_package = new ArrayList();
		result.put("package_data", data_package);
		return result;
	}
    
	
	public Map queryCategoryData(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		String category = param.getString("category");
		String sql = " select  food_name , price , sum(count) count, sum(back_count) back_count,sum(free_count) free_count " +
				" from tf_bill_item where  category = ? group by food_name , price order by price desc, count desc ";
		List list = baseDao.jdbcTemplate.queryForList(sql, new Object[]{  category });
		result.put("categoryData", list);
		return result;
	}

	public Map queryTodayBills(IData param) {
		Map result = new HashMap();
		result.put("success", "true");
		User user = (User)param.get("user");
		List list = null;
		if(param.getString("table_id").equals("")){
			list = new ArrayList();
			result.put("msg", "无效座位号,查询失败");
			result.put("success", "false");
		}else{
			list = operationDao.queryTableBills(param.getString("table_id"));
			result.put("msg", "ok");
		}
		result.put("bills", list);
		return result;
	}

	public IData queryTodayBillById(IData param) {
		IData bill = operationDao.queryBillByBillId(param.getString("bill_id"));
		if(bill!=null){
			bill = operationService.queryBillInfo(bill);
		}
		return bill;
	}

	public Map queryHistorySaleData(IData param) {
		long t1 = System.currentTimeMillis();
		Map result = new HashMap();
		User user = (User)param.get("user");
		result.put("success", "true");
		String sql_recv = " select mode_id,mode_name , sum(fee) recv_fee from th_bill_fee a,th_bill b " +
				" where  a.bill_id = b.bill_id and b.open_date >= ? and b.open_date <= ?  group by mode_id,mode_name order by  mode_id ";
		List data_recv = baseDao.jdbcTemplate.queryForList(sql_recv,new Object[]{  param.get("start_date"), param.get("end_date") });
		result.put("recv_data", data_recv);
		log.info(" data_recv  : " + data_recv);

		long t2 = System.currentTimeMillis();
		log.info(" data_recv  use : "+ (t2-t1)+" ms ");
		
		String floor = " select a.floor, ifnull(b.money,0) money from " +
			" ( select distinct floor1 floor from td_table where 1 = 1 order by floor_order ) a " +
			" left join " +
			" ( select  b.floor1 floor, sum(a.recv_fee) money from th_bill a, td_table b where a.table_id = b.table_id and  a.open_date >= ? and a.open_date <= ? group by b.floor1 ) b " +
			" on a.floor = b.floor ";
		List data_floor = baseDao.jdbcTemplate.queryForList(floor,new Object[]{ param.get("start_date"), param.get("end_date")});
		result.put("floor_data", data_floor);
		
		long t3 = System.currentTimeMillis();
		log.info(" data_floor  use : "+ (t3-t2)+" ms ");
		
		String item_money = " select ifnull(sum(reduce_fee),0) moling_money , ifnull( (sum(bill_fee)-(sum(derate_fee)+sum(reduce_fee)+sum(recv_fee))) ,0) lose_money ," +
				" ifnull(sum(spay_fee),0) spay_fee, ifnull(sum(derate_fee),0) discount_money, ifnull(sum(recv_fee),0) recv_money, ifnull(sum(nop),0) total_person " +
				" from th_bill  where  open_date >= ? and open_date <= ? ";
		List data_item = baseDao.jdbcTemplate.queryForList(item_money,new Object[]{  param.get("start_date"), param.get("end_date") });
		result.put("item_data", data_item);
		
		long t4 = System.currentTimeMillis();
		log.info(" item_money  use : "+ (t4-t3)+" ms ");
		
		String bill_count = " select ifnull( sum(case when pay_type = '1' then 1 else 0 end) ,0)  close_count , " +
				" ifnull( sum(case when pay_type = '0' then 1 else 0 end),0) open_count  from th_bill  where  open_date >= ? and open_date <= ?  ";
		List bill_count_data = baseDao.jdbcTemplate.queryForList(bill_count,new Object[]{ param.get("start_date"), param.get("end_date") });
		result.put("bill_count_data", bill_count_data);
		
		long t5 = System.currentTimeMillis();
		log.info(" bill_count_data  use : "+ (t5-t4)+" ms ");
		
		String unrecv_money = " select ifnull(sum(a.fee),0) money from  "+
	        " ( select b.bill_id  ,sum(a.price*(a.count-a.back_count-a.free_count)*a.pay_rate/100) -b.reduce_fee fee " +
	        " from th_bill_item a , th_bill b "+
	        " where a.bill_id = b.bill_id  and b.open_date >= ? and b.open_date <= ?   and b.pay_type = '0' group by a.bill_id  ) a ";
		List data_unrecv = baseDao.jdbcTemplate.queryForList(unrecv_money,new Object[]{ param.get("start_date"), param.get("end_date") });
		result.put("unrecv_data",data_unrecv);
		
		long t6 = System.currentTimeMillis();
		log.info(" unrecv_data  use : "+ (t6-t5)+" ms ");
		
		String category_sql = " select * from ( select groups category , sum(price*count*pay_rate/100) money ," +
				"  sum(price*(count-back_count-free_count)*pay_rate/100) real_money " +
				"  from th_bill_item a ,th_bill b where  a.bill_id = b.bill_id and b.open_date >= ? and b.open_date <= ? group by groups order by category, groups ) u  " +
				"  union all select '合计',sum(price*count*pay_rate/100),sum(price*(count-back_count-free_count)*pay_rate/100) " +
				" from th_bill_item a ,th_bill b where   a.bill_id = b.bill_id   and b.open_date >= ? and b.open_date <= ?  ";
		List data_category = baseDao.jdbcTemplate.queryForList(category_sql,new Object[]{  param.get("start_date"), param.get("end_date"), param.get("start_date"), param.get("end_date")  });
		result.put("category_data",data_category);
		
		long t7 = System.currentTimeMillis();
		log.info(" data_category  use : "+ (t7-t6)+" ms ");

		List data_package = new ArrayList();
		result.put("package_data", data_package);
		
		long t8 = System.currentTimeMillis();
		log.info(" data_package  use : "+ (t8-t7)+" ms ");
		
		return result;
	}


}


