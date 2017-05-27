package com.huai.common.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.huai.common.base.BaseController;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.util.ut;
import com.huai.cust.service.CustService;
import com.huai.operation.service.QueryService;

@Controller
@RequestMapping("/setting")
public class SettingAction extends BaseController {

	@Resource(name="queryService")
	public QueryService queryService;
	
	private static final Logger log = Logger.getLogger(SettingAction.class);
	
	@RequestMapping(value = "/index.html")   
    public ModelAndView loadingPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/setting/index_setting");
    }
	
	@RequestMapping(value = "/bolidcbpage.html")   
    public ModelAndView bolidcbpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/setting/bolidcb");
    }
	
	
	
}
