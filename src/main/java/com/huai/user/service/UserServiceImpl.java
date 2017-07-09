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
		return user;
	}

	
}
