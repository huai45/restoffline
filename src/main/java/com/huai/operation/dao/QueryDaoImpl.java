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
		
		String restId = user.getRestId();
		if(restId.equals("mt")){
			restId = "kh";
		}
		
		String sql = " select * from tf_user where user_id = ? and restId = ? ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("user_id") , restId });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}
	
	public IData queryCustByCardNo(IData param) {
		User user = (User)param.get("user");
		
		String restId = user.getRestId();
		if(restId.equals("mt")){
			restId = "kh";
		}
		
		String sql = " select * from tf_user where card_no = ? and restId = ? and user_type = 'C'  ";
		List data = jdbcTemplate.queryForList(sql,new Object[]{ param.getString("card_no") , restId });
		if(data.size()>0){
			return new IData((Map)data.get(0));
		}
		return null;
	}

	public List queryCustList(IData param) {
		User user = (User)param.get("user");
		
		String restId = user.getRestId();
		if(restId.equals("mt")){
			restId = "kh";
		}
		
		List data = null;
		String sql = " select * from tf_user where restId = ? and user_type = ? order by  user_id ";
		if(param.has("user_type")){
			data = jdbcTemplate.queryForList(sql,new Object[]{ user.getRestId(),param.get("user_type") });
		}else{
			data = jdbcTemplate.queryForList(" select * from tf_user where restId = ? order by  user_id",
				new Object[]{ restId });
		}
		return data;
	}


}
