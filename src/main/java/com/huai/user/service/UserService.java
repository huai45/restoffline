package com.huai.user.service;

import com.huai.common.domain.User;

public interface UserService {

	User getMatchMember(String staff_id,String pwd);
	
	
}
