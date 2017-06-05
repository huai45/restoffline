package com.huai.common.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.huai.common.base.BaseController;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.service.CommonService;
import com.huai.common.util.CC;
import com.huai.common.util.ut;
import com.huai.user.service.UserService;

@Controller
public class LoginController extends BaseController {

	private static final Logger log = Logger.getLogger(LoginController.class);
	
	@Autowired
	public UserService userService;

	@Autowired
	public CommonService commonService;
	
    @RequestMapping(value = "/login.action")   
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	User user = (User) request.getSession().getAttribute(CC.USER_CONTEXT);
		Map<String, Object> model_return = new HashMap<String, Object>();
//		log.info(" login.action , user = "+user);
		setSessionUser(request, null);
		String nextPage = "";
		if (user == null) {
			String staff_id = request.getParameter("username");
			String pwd = request.getParameter("password");
			log.info("userName: " + staff_id + "\tPassword: " + pwd);
			if (null == staff_id || "".equals(staff_id) || null == pwd || "".equals(pwd)) {
				// 密码错误
				nextPage = "/login/login";
				model_return.put("err_info", "用户名或密码为空");
				log.info("用户名或密码为空");
				ut.p(" 1 1");
			} else {
				user = userService.getMatchMember(staff_id.trim(), pwd.trim());
				if (null == user) {
					nextPage = "/login/login";
					model_return.put("err_info", "用户名或密码错误");
					log.info("用户名或密码错误");
				} else {
					IData param = new IData();
					param.put("rest_id", user.getRestId());
					IData info = commonService.queryRestInfo(param);
					IData p = commonService.queryRestParam(param);
					user.setInfo(info);
					user.setParam(p);
					setSessionUser(request, user);
					log.info("登录成功");
					if(user.getRestId().equals("2")){
						return new ModelAndView(new RedirectView("/operation/loading.html"));
					}
					if(user.getRestId().equals("3")){
						return new ModelAndView(new RedirectView("/monitor/loading.html"));
					}
					if(user.getRestId().equals("4")){
						return new ModelAndView(new RedirectView("/query/index.html"));
					}
					return new ModelAndView(new RedirectView("/index.html"));
				}
			}
		} else {
			log.info("用户已经登录");
			if(user.getRestId().equals("2")){
				return new ModelAndView(new RedirectView("/operation/loading.html"));
			}
			if(user.getRestId().equals("3")){
				return new ModelAndView(new RedirectView("/monitor/loading.html"));
			}
			return new ModelAndView(new RedirectView("/index.html"));
		}
		ModelAndView mav = new ModelAndView(nextPage, model_return);
		return mav;
    }
    
    @RequestMapping(value = "/index.html")   
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	try{
    		User user = this.getSessionUser(request);
        	return new ModelAndView("/platform/index");
    	}catch(Exception e){
    		e.printStackTrace();
    		modelMap.put("err_info", "<font color=red>登陆异常!</font>");
            return new ModelAndView("/login/login", modelMap);
    	}
    }
    
    @RequestMapping(value = "/login.html")   
    public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	String msg = request.getParameter("error");
    	if(msg==null){
    		msg="";
    	}
//    	ut.p("msg"+msg);
    	try {
    		msg = new String(msg.getBytes("ISO-8859-1"),"utf-8");
//			ut.p("msg"+new String(msg.getBytes("utf-8"),"gbk"));
//			ut.p("msg"+new String(msg.getBytes("gbk"),"utf-8"));
//	    	ut.p("msg"+new String(msg.getBytes("ISO-8859-1"),"gbk"));
//	    	ut.p("msg"+new String(msg.getBytes("ISO-8859-1"),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	modelMap.put("err_info", msg);
        return new ModelAndView("/login/login", modelMap);
    }
    
    @RequestMapping(value = "/logout")   
    public String logout(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {  
    	setSessionUser(request, null);
    	modelMap.clear();
    	return "redirect:login.html?time="+System.currentTimeMillis();
    }   
    
    @RequestMapping(value = "/waiting")   
    public ModelAndView waitingPage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)  {
    	
    	log.info(" waiting ");
    	return new ModelAndView("/waiting", modelMap);
    }
    
    @RequestMapping(value = "/transPage")   
    public ModelAndView transPage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)  {
		String page = request.getParameter("page");
		ut.log(" *** transPage  page = "+page);
    	if(page==null||page.trim().equals("")){
    		return new ModelAndView("/waiting", modelMap);
    	}
    	return new ModelAndView(page, modelMap);
        	
    }
    
}
