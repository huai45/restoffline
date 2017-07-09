package com.huai.operation.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;

@Component("queryDao")
public class QueryDaoImpl extends BaseDao implements QueryDao {

	public IData queryCustById(IData param) {
		String sql = " select * from tf_user where user_id = ?  ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("user_id") });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}
	
	public IData queryCustByCardNo(IData param) {
		String sql = " select * from tf_user where card_no = ? and user_type = 'C'  ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("card_no") });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}

	public List queryCustList(IData param) {
		List data = null;
		String sql = " select * from tf_user where user_type = ? order by  user_id ";
		if(param.has("user_type")){
			data = jdbcTemplate.queryForList(sql,new Object[]{ param.get("user_type") });
		}else{
			data = jdbcTemplate.queryForList(" select * from tf_user where 1 = 1 order by  user_id",
				new Object[]{ });
		}
		return data;
	}


}
