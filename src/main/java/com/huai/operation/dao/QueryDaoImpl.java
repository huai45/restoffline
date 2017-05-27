package com.huai.operation.dao;

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

@Component("queryDao")
public class QueryDaoImpl extends BaseDao implements QueryDao {

	public IData queryCustById(IData param) {
		User user = (User)param.get("user");
		
		String rest_id = user.getRest_id();
		if(rest_id.equals("mt")){
			rest_id = "kh";
		}
		
		String sql = " select * from tf_user where user_id = ? and rest_id = ? ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("user_id") , rest_id });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}
	
	public IData queryCustByCardNo(IData param) {
		User user = (User)param.get("user");
		
		String rest_id = user.getRest_id();
		if(rest_id.equals("mt")){
			rest_id = "kh";
		}
		
		String sql = " select * from tf_user where card_no = ? and rest_id = ? and user_type = 'C'  ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("card_no") , rest_id });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}

	public List queryCustList(IData param) {
		User user = (User)param.get("user");
		
		String rest_id = user.getRest_id();
		if(rest_id.equals("mt")){
			rest_id = "kh";
		}
		
		List data = null;
		String sql = " select * from tf_user where rest_id = ? and user_type = ? order by  user_id ";
		if(param.has("user_type")){
			data = jdbcTemplate.queryForList(sql,new Object[]{ user.getRest_id(),param.get("user_type") });
		}else{
			data = jdbcTemplate.queryForList(" select * from tf_user where rest_id = ? order by  user_id",
				new Object[]{ rest_id });
		}
		return data;
	}


}
