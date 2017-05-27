package com.huai.common.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.huai.common.domain.IData;

@Component("foodDao")
public class FoodDaoImpl extends BaseDao implements FoodDao {

	public IData queryFoodById(String rest_id, String food_id) {
		IData food = null;
		List foods = jdbcTemplate.queryForList("select * from td_food where food_id = ? and rest_id = ? order by food_id desc ", 
			new Object[]{ food_id,rest_id });
		if(foods.size()>0){
			food = new IData((Map)foods.get(0));
		}
		return food;
	}

	public List queryFoodList(IData param) {
		List foods = jdbcTemplate.queryForList("select * from td_food where rest_id = ? order by food_id asc ", 
			new Object[]{ param.get("rest_id") });
		return foods;		
	}

}
