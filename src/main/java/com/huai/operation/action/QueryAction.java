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
import com.huai.operation.service.OperationService;
import com.huai.operation.service.QueryService;

@Controller
@RequestMapping("/query")
public class QueryAction extends BaseController {

	private static final Logger log = Logger.getLogger(QueryAction.class);
	
	@Resource(name="operationService")
	public OperationService operationService;
	
	@Resource(name="queryService")
	public QueryService queryService;
	
	@RequestMapping(value = "/index.html")   
    public ModelAndView loadingPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/query/index_query");
    }
	
	@RequestMapping(value = "/setting.html")   
    public ModelAndView settingPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/operation/setting");
    }
	
	@RequestMapping(value = "/water.html")   
    public ModelAndView waterPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/operation/water");
    }
	
	@RequestMapping(value = "/todaybill.html")   
    public ModelAndView todaybill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/operation/todaybill");
    }
	
	@RequestMapping(value = "/queryVipCardUserInfo")
	@ResponseBody
    public Object queryVipCardUserInfo(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryVipCardUserInfo  ");
		User user = this.getSessionUser(request);
		String card_no = request.getParameter("card_no");
		log.info(" card_no = "+card_no);
		IData param = new IData();
		param.put("card_no", card_no );
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		Map result = new HashMap();
		Map vipuser = queryService.queryVipCardUserInfo(param);
		if(vipuser!=null){
			result.put("success", "true");
			result.put("msg", "ok");
			result.put("data", vipuser);
		}else{
			result.put("success", "false");
			result.put("msg", "查询不到该卡的会员信息");
		}
		return result;
	}
	
	@RequestMapping(value = "/queryVipCardUserList")
	@ResponseBody
    public Object queryVipCardUserList(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryVipCardUserList  ");
		User user = this.getSessionUser(request);
		String card_no = request.getParameter("card_no");
		IData param = new IData();
		param.put("card_no", card_no );
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		Map result = queryService.queryVipCardUserList(param);
		return result;
	}
	
	@RequestMapping(value = "/queryCreditUserList")
	@ResponseBody
    public Object queryCreditUserList(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryCreditUserList  ");
		User user = this.getSessionUser(request);
		IData param = new IData();
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		Map result = queryService.queryCreditUserList(param);
		return result;
	}
	
	@RequestMapping(value = "/queryTodayData")
	@ResponseBody
    public Object queryTodayData(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryTodayData  ");
		User user = this.getSessionUser(request);
		IData param = new IData();
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		Map result = queryService.queryTodayData(param);
		log.info(" result : "+result);
		return result;
	}
	
	
	@RequestMapping(value = "/queryCategoryData")
	@ResponseBody
    public Object queryCategoryData(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryCategoryData  ");
		User user = this.getSessionUser(request);
		String category = request.getParameter("category");
		log.info(" category = "+category);
		IData param = new IData();
		param.put("category", category);
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		Map result = queryService.queryCategoryData(param);
		return result;
	}
	
	@RequestMapping(value = "/queryTodayBills")
	@ResponseBody
    public Object queryTodayBills(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryTodayBills  ");
		User user = this.getSessionUser(request);
		String table_id = request.getParameter("table_id");
		IData param = new IData();
		param.put("table_id", table_id);
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		Map result = queryService.queryTodayBills(param);
		return result;
	}
	
	@RequestMapping(value = "/querybill")
    public Object qrybill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" querybill  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		IData bill = queryService.queryTodayBillById(param);
		modelMap.put("msg", "true");
		modelMap.put("bill", bill);
    	return new ModelAndView("/query/querybillinfo");
	}
	
	
	@RequestMapping(value = "/queryHistorySaleData")
	@ResponseBody
    public Object queryHistorySaleData(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryHistorySaleData  ");
		User user = this.getSessionUser(request);
		IData param = new IData();
		String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		param.put("start_date", start_date);
		param.put("end_date", end_date);
		param.put("rest_id", user.getRestId());
		param.put("user", user);
		Map result = queryService.queryHistorySaleData(param);
		log.info(" result : "+result);
		return result;
	}
	
	
}
