package com.huai.common.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;

public interface FoodService {

	IData queryFoodById(IData param);

	List queryFoodList(IData param);

	
}
