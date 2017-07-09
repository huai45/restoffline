package com.huai.operation.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.huai.operation.dao.OperationDao;
import com.huai.operation.service.MonitorService;
import com.huai.operation.service.OperationService;
import com.huai.operation.service.QueryService;

@Controller
@RequestMapping("/monitor")
public class MonitorAction extends BaseController {

	@Resource(name="operationService")
	public OperationService operationService;
	
	@Resource(name="monitorService")
	public MonitorService monitorService;
	
	@Resource(name="queryService")
	public QueryService queryService;
	
	private static final Logger log = Logger.getLogger(MonitorAction.class);
	
	@RequestMapping(value = "/loading.html")   
    public ModelAndView loadingPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/monitor/loading");
    }
	
	@RequestMapping(value = "/index.html")   
    public ModelAndView testindexpage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)  {
		log.info(" /monitor/index  ");
		return new ModelAndView("/monitor/index", modelMap);
	}
	
	@RequestMapping(value = "/queryAllBill.html")
	@ResponseBody
    public Object queryAllBill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryAllBill  ");
		User user = this.getSessionUser(request);
		IData param = new IData();
		param.put("user", user);
		List bills = monitorService.queryAllBill(param);
		List items = monitorService.queryAllBillItem(param);
		Map result = new HashMap();
		result.put("success", "true");
		result.put("billList", bills);
		result.put("itemList", items);
		return result;
	}
	
	@RequestMapping(value = "/sendSubmit.html")
	@ResponseBody
    public Object sendSubmit(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" sendSubmit  ");
		User user = this.getSessionUser(request);
		String submit_str = request.getParameter("submit_str");
		ut.p("submit_str:"+submit_str);
		IData param = new IData();
		param.put("submit_str", submit_str);
		param.put("user", user);
		Map result = monitorService.sendSubmit(param);
		return result;
	}
	
	@RequestMapping(value = "/queryTableBill")
	@ResponseBody
    public Object queryTableBill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		User user = this.getSessionUser(request);
		String table_id = request.getParameter("table_id");
		log.info(" queryTableBill  table_id = "+table_id);
		IData param = new IData();
		param.put("table_id", table_id);
		param.put("user", user);
		Map result = monitorService.queryTableBill(param);
    	return result;
	}
	
	@RequestMapping(value = "/querybillformonitor")
    public Object querybillformonitor(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		log.info(" querybillformonitor  bill_id = "+bill_id);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("user", user);
		IData bill = null;
		if(!ut.isEmpty(bill_id)){
			bill = queryService.queryTodayBillById(param);
		}else{
			//ut.p("不用查询了  billid==null");
		}
		modelMap.put("bill", bill);
		modelMap.put("msg", "true");
    	return new ModelAndView("/monitor/querybillinfo");
	}
	
	
	
	
}
