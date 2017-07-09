package com.huai.common.dao;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.huai.common.domain.IData;

@Component("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {

	private static final Logger log = Logger.getLogger(CommonDaoImpl.class);
	
	public IData queryRestInfoById(IData param) {
		List result = jdbcTemplate.queryForList("select * from td_restaurant where 1 = 1 ", new Object[]{  });
		if(result.size()==0){
			return null;
		}
		IData info = new IData((Map)result.get(0));
		return info;
	}

	public IData queryCommonParam() {
		IData param = new IData();
		List result = jdbcTemplate.queryForList("select * from td_param ");
		for(int i=0;i<result.size();i++){
			Map temp = (Map)result.get(i);
			param.put(temp.get("NAME").toString(), temp.get("VALUE").toString());
		}
		return param;
	}

	
	
}
