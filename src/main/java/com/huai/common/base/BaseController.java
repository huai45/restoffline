package com.huai.common.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huai.common.domain.User;
import com.huai.common.util.CC;

public class BaseController {

	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(CC.USER_CONTEXT);
	}
   
	/**
	 * @param request
	 * @param user
	 */
	protected void setSessionUser(HttpServletRequest request,User user) {
		request.getSession().setAttribute(CC.USER_CONTEXT,user);
	}
	
}
