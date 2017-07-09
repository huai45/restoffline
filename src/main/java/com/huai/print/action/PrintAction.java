package com.huai.print.action;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.huai.common.base.BaseController;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.util.ut;
import com.huai.print.service.PrintService;

@Controller
@RequestMapping("/localprint")
public class PrintAction extends BaseController {

	@Resource(name="printService")
	public PrintService printService;
	
	private static final Logger log = Logger.getLogger(PrintAction.class);
	
	@RequestMapping(value = "/index.html")   
    public ModelAndView loadingPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/print/index_print");
    }

	@RequestMapping(value = "/printBill")
	@ResponseBody
	public Object printBill(HttpServletRequest request, HttpServletResponse response)  {
		String table_id = request.getParameter("table_id");
		log.info(" printBill  table_id = "+table_id);
		Map result = printService.printBill(table_id);
		return result;
	}
	
	@RequestMapping(value = "/queryfoodprint.html")
	@ResponseBody
    public Object queryfoodprint(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryfoodprint  ");
		String appid = request.getParameter("appid");
		IData param = new IData();
		param.put("appid", appid );
		Map result = printService.queryFoodPrintList(param);
		return result;
    }
	
	@RequestMapping(value = "/querybillprint.html") 
	@ResponseBody
    public Object querybillprint(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String appid = request.getParameter("appid");
		log.info(" querybillprint   appid = "+appid);
		IData param = new IData();
		param.put("appid", appid );
		Map result = printService.queryBillPrintList(param);
		return result;
    }
	
	@RequestMapping(value = "/querybillinfo.html") 
	@ResponseBody
    public Object querybillinfo(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String appid = request.getParameter("appid");
		String bill_id = request.getParameter("bill_id");
		IData param = new IData();
		param.put("appid", appid );
		param.put("bill_id", bill_id );
		Map result = printService.queryPrintBillInfo(param);
		return result;
    }
	
	@RequestMapping(value = "/querybillinfobytable.html") 
	@ResponseBody
    public Object queryBillnfoByTable(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String table_id = request.getParameter("table_id");
		log.info(" localprint  queryBillnfoByTable   table_id = "+table_id);
		User user = this.getSessionUser(request);
		IData param = new IData();
		param.put("user", user);
		param.put("table_id", table_id );
		Map result = printService.queryPrintBillInfoByTable(param);
		return result;
    }
	
	@RequestMapping(value = "/querybillinfobybillid.html") 
	@ResponseBody
    public Object querybillinfobybillid(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String bill_id = request.getParameter("bill_id");
		log.info(" localprint  querybillinfobybillid   bill_id = "+bill_id);
		User user = this.getSessionUser(request);
		IData param = new IData();
		param.put("user", user);
		param.put("bill_id", bill_id );
		Map result = printService.queryPrintBillInfoByBillId(param);
		log.info(" result : "+result);
		return result;
    }
	
}
