package com.huai.operation.action;

import java.util.HashMap;
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

@Controller
@RequestMapping("/operation")
public class OperationAction extends BaseController {

	@Resource(name="operationService")
	public OperationService operationService;
	
	private static final Logger log = Logger.getLogger(OperationAction.class);
	
	@RequestMapping(value = "/loading.html")   
    public ModelAndView loading(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" /operation/loading  ");
		return new ModelAndView("/operation/loading");
    }
	
	@RequestMapping(value = "/index.html")   
    public ModelAndView testindexpage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap)  {
		log.info(" /operation/desktop  ");
		return new ModelAndView("/operation/desktop", modelMap);
//		return new ModelAndView("/operation/loading", modelMap);
	}
	
	
	@RequestMapping(value = "/queryAllTableState.html")
	@ResponseBody
    public Object queryAllTableState(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		User user = this.getSessionUser(request);
//		log.info(" queryAllTableState  user = "+user);
		IData param = new IData();
		Map result = operationService.queryAllTableState(param);
		return result;
	}
	
	
	@RequestMapping(value = "/checkTableState.html")
	@ResponseBody
    public Object checkTableState(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" checkTableState  ");
		User user = this.getSessionUser(request);
		String table_id = request.getParameter("table_id");
		log.info(" table_id = "+table_id);
		IData param = new IData();
		param.put("table_id", table_id);
		Map result = operationService.checkTableState(param);
		return result;
	}
	
	@RequestMapping(value = "/queryBillById.html")
	@ResponseBody
    public Object queryBillById(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryBillById  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("user", user);
		IData bill = operationService.queryBillById(param);
		Map result = new HashMap();
		if(bill!=null){
			result.put("success", "true");
			result.put("bill", bill);
		}else{
			result.put("success", "false");
			result.put("msg", "查询不到该卡的会员信息");
		}
		return result;
	}
	
	
	@RequestMapping(value = "/openTable.html")
	@ResponseBody
    public Object openTable(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" openTable  ");
		User user = this.getSessionUser(request);
		String table_id = request.getParameter("table_id");
		String nop = request.getParameter("nop");
		log.info(" table_id = "+table_id+" , nop = "+nop);
		IData param = new IData();
		param.put("table_id", table_id);
		param.put("nop", nop);
		param.put("user", user);
		Map result = operationService.openTable(param);
		return result;
	}
	
	@RequestMapping(value = "/addFood.html")
	@ResponseBody
    public Object addFood(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" addFood  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.addBillItems(param);
		return result;
	}
	
	@RequestMapping(value = "/addTempFood.html")
	@ResponseBody
    public Object addTempFood(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" addTempFood  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.addTempFood(param);
		return result;
	}
	
	@RequestMapping(value = "/cancelFood.html")
	@ResponseBody
    public Object cancelFood(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" cancelFood  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String count = request.getParameter("count");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , count = "+count+" ,item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("count", count);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.cancelFood(param);
		ut.p(result);
		return result;
	}
	
	@RequestMapping(value = "/presentFood.html")
	@ResponseBody
    public Object presentFood(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" presentFood  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String count = request.getParameter("count");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , count = "+count+" ,item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("count", count);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.presentFood(param);
		return result;
	}
	
	@RequestMapping(value = "/derateFood.html")
	@ResponseBody
    public Object derateFood(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" derateFood  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String count = request.getParameter("count");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , count = "+count+" ,item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("count", count);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.derateFood(param);
		return result;
	}
	
	@RequestMapping(value = "/changeTable.html")
	@ResponseBody
    public Object changeTable(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" changeTable  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String count = request.getParameter("count");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("table_id", count);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.changeTable(param);
		return result;
	}
	
	@RequestMapping(value = "/startCook.html")
	@ResponseBody
    public Object startCook(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" startCook  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.startCook(param);
		return result;
	}
	
	@RequestMapping(value = "/hurryCook.html")
	@ResponseBody
    public Object hurryCook(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" hurryCook  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.hurryCook(param);
		return result;
	}
	
	@RequestMapping(value = "/finishCook.html")
	@ResponseBody
    public Object finishCook(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" finishCook  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String item_str = request.getParameter("item_str");
		log.info(" bill_id = "+bill_id+" , item_str = "+item_str);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("item_str", item_str);
		param.put("user", user);
		Map result = operationService.finishCook(param);
		return result;
	}
	
	@RequestMapping(value = "/payFee.html")
	@ResponseBody
    public Object payFee(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" payFee  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String mode_id = request.getParameter("mode_id");
		String recvfee = request.getParameter("recvfee");
		log.info(" bill_id = "+bill_id+" , mode_id = "+mode_id+" , recvfee = "+recvfee);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("mode_id", mode_id);
		param.put("recvfee", recvfee);
		param.put("user", user);
		Map result = operationService.payFee(param);
		return result;
	}
	
	@RequestMapping(value = "/reduceFee.html")
	@ResponseBody
    public Object reduceFee(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" reduceFee  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String reducefee = request.getParameter("reducefee");
		log.info(" bill_id = "+bill_id+" , reducefee = "+reducefee);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("reducefee", reducefee);
		param.put("user", user);
		Map result = operationService.reduceFee(param);
		return result;
	}
	
	@RequestMapping(value = "/closeBill.html")
	@ResponseBody
    public Object closeBill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" closeBill  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		log.info(" bill_id = "+bill_id);
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("user", user);
		Map result = operationService.closeBill(param);
		return result;
	}
	
	@RequestMapping(value = "/payByVipCard.html")
	@ResponseBody
    public Object payByVipCard(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" payByVipCard  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String user_id = request.getParameter("user_id");
		String card_no = request.getParameter("card_no");
		String recvfee = request.getParameter("recvfee");
		log.info(" bill_id = "+bill_id+" , user_id = "+user_id+" , card_no = "+card_no+" , recvfee = "+recvfee);
		IData param = new IData();
		param.put("mode_id", "vip");
		param.put("bill_id", bill_id);
		param.put("user_id", user_id);
		param.put("card_no", card_no);
		param.put("recvfee", recvfee);
		param.put("user", user);
		Map result = operationService.payByVipCard(param);
		return result;
	}
	
	@RequestMapping(value = "/payByCreditUser.html")
	@ResponseBody
    public Object payByCreditUser(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" payByCreditUser  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String user_id = request.getParameter("user_id");
		String recvfee = request.getParameter("recvfee");
		log.info(" bill_id = "+bill_id+" , user_id = "+user_id+" , recvfee = "+recvfee);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		IData param = new IData();
		param.put("mode_id", "gz");
		param.put("bill_id", bill_id);
		param.put("user_id", user_id);
		param.put("recvfee", recvfee);
		param.put("user", user);
		Map result = operationService.payByCreditUser(param);
		return result;
	}
	
	
	@RequestMapping(value = "/reopenBill.html")
	@ResponseBody
    public Object reopenBill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" reopenBill  ");
		User user = this.getSessionUser(request);
		String bill_id = request.getParameter("bill_id");
		String pwd = request.getParameter("pwd");
		IData param = new IData();
		param.put("bill_id", bill_id);
		param.put("pwd", pwd);
		param.put("user", user);
		Map result = operationService.reopenBill(param);
		return result;
	}
	
	@RequestMapping(value = "/finishToday.html")
	@ResponseBody
    public Object finishToday(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" finishToday  ");
		User user = this.getSessionUser(request);
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		IData param = new IData();
		param.put("user", user);
		Map result = operationService.finishToday(param);
		return result;
	}
	
	
	@RequestMapping(value = "/testjson")
	@ResponseBody
    public Object testjson(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" testjson  ");
        Map map = new HashMap();
        map.put("succes", true);
        map.put("msg", "hello");
		return map;
	}
}
