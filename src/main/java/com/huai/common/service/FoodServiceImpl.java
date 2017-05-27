package com.huai.common.service;

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
import com.huai.common.dao.FoodDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.operation.dao.QueryDao;
import com.huai.common.util.*;
import com.huai.cust.dao.CustDao;

@Component("foodService")
public class FoodServiceImpl implements FoodService {

	@Resource(name="baseDao")
	public BaseDao baseDao;
	
	@Resource(name="foodDao")
	public FoodDao foodDao;

	public IData queryFoodById(IData param) {
		IData food = foodDao.queryFoodById(param.getString("rest_id"),param.getString("food_id"));
		return food;
	}

	public List queryFoodList(IData param) {
		List foods = foodDao.queryFoodList(param);
		return foods;
	}


	

}


