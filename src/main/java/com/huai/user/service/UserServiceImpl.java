package com.huai.user.service;

import com.huai.user.dao.StaffRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.huai.common.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	@Autowired
	StaffRepository staffRepository;

	public User getMatchMember(String staff_id, String pwd) {
		User user = staffRepository.checkUserLogin(staff_id,pwd);
		log.info(" staffRepository     user = "+user);
//		List result = baseDao.jdbcTemplate.queryForList(" select * from td_staff where staff_id = ? and password = ? ",new Object[]{staff_id,pwd});
//		if(result.size()==0){
//			return null;
//		}
//		User user = Map2User((Map<String,String>)result.get(0));0
		return user;
	}

//	public User Map2User(Map<String,String> data){
//		User user = new User();
//		user.setStaff_id(data.get("staff_id"));
//		user.setRest_id(data.get("rest_id"));
//		user.setRole_id(data.get("role_id"));
//		user.setStaffname(data.get("staffname"));
//		user.setPhone(data.get("phone"));
//		user.setCtrlright(data.get("ctrlright"));
//		user.setDataright(data.get("dataright"));
//		user.setToken(data.get("token"));
//		return user;
//	}
	
	
}
