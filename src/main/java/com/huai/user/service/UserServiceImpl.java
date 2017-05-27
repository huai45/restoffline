package com.huai.user.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.huai.common.dao.BaseDao;
import com.huai.common.domain.User;

@Component("userService")
public class UserServiceImpl implements UserService {

	@Resource(name="baseDao")
	public BaseDao baseDao;
	
	public User getMatchMember(String staff_id, String pwd) {
		List result = baseDao.jdbcTemplate.queryForList(" select * from td_staff where staff_id = ? and password = ? ",new Object[]{staff_id,pwd});
		if(result.size()==0){
			return null;
		}
		User user = Map2User((Map<String,String>)result.get(0));
		return user;
	}

	public User Map2User(Map<String,String> data){
		User user = new User();
		user.setStaff_id(data.get("staff_id"));
		user.setRest_id(data.get("rest_id"));
		user.setRole_id(data.get("role_id"));
		user.setStaffname(data.get("staffname"));
		user.setPhone(data.get("phone"));
		user.setCtrlright(data.get("ctrlright"));
		user.setDataright(data.get("dataright"));
		user.setToken(data.get("token"));
		return user;
	}
	
	
}
