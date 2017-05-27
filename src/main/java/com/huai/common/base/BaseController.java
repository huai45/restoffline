package com.huai.common.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huai.common.domain.User;
import com.huai.common.util.CC;

public class BaseController {
	protected static final String ERROR_MSG_KEY = "errorMsg";

	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(
				CC.USER_CONTEXT);
	}
   
	/**
	 * @param request
	 * @param user
	 */
	protected void setSessionUser(HttpServletRequest request,User user) {
		request.getSession().setAttribute(CC.USER_CONTEXT,
				user);
	}

//	public final String getAppbaseUrl(HttpServletRequest request, String url) {
//		Assert.hasLength(url, "url����Ϊ��");
//		Assert.isTrue(url.startsWith("/"), "������/��ͷ");
//		return request.getContextPath() + url;
//	}
	
//	@RequestMapping(value="/throwException")
//	public String throwException(){
//		ut.log("***************");		
//		if(2>1){
//            throw new RuntimeException("dd");			
//		}
//		return "success";
//		
//	}
//	
//	@ExceptionHandler(RuntimeException.class)
//	public String handeException(RuntimeException e,HttpServletRequest request){
//        ut.log("***************");		
//		return "forward:/error.jsp";
//	}
	
}
