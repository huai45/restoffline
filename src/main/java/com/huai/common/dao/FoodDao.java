package com.huai.common.dao;

import java.util.List;

import com.huai.common.domain.IData;

public interface FoodDao {

	IData queryFoodById(String rest_id, String food_id);

	List queryFoodList(IData param);

}
