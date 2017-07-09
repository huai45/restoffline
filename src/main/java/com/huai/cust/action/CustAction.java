package com.huai.cust.action;

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
@RequestMapping("/cust")
public class CustAction extends BaseController {

	@Resource(name="custService")
	public CustService custService;
	
	@Resource(name="queryService")
	public QueryService queryService;
	
	private static final Logger log = Logger.getLogger(CustAction.class);
	
	@RequestMapping(value = "/index.html")   
    public ModelAndView loadingPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/index_cust");
    }
	
	@RequestMapping(value = "/vipcardquery.html")   
    public ModelAndView vipcardquery(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/vipcardquery");
    }
	
	@RequestMapping(value = "/creditquery.html")   
    public ModelAndView creditquery(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/creditquery");
    }
	
	@RequestMapping(value = "/addcreditpage.html")   
    public ModelAndView addcreditpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/addcredit");
    }
	
	@RequestMapping(value = "/addcredit.html")
	@ResponseBody
    public Object addcredit(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" addcredit  ");
		User user = this.getSessionUser(request);
		String custname = request.getParameter("custname");
		String phone = request.getParameter("phone");
		String credit = request.getParameter("credit");
		String remark = request.getParameter("remark");
		IData param = new IData();
		param.put("custname", custname );
		param.put("phone", phone );
		param.put("credit", credit );
		param.put("remark", remark==null?"":remark );
		param.put("user", user);
		Map result = custService.addCreditUser(param);
		return result;
    }
	
	@RequestMapping(value = "/editcreditpage.html")   
    public ModelAndView editcreditpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/editcredit");
    }
	
	@RequestMapping(value = "/editcredit.html")
	@ResponseBody
    public Object editcredit(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" editcredit  ");
		User user = this.getSessionUser(request);
		String user_id = request.getParameter("user_id");
		String custname = request.getParameter("custname");
		String phone = request.getParameter("phone");
		String credit = request.getParameter("credit");
		String remark = request.getParameter("remark");
		IData param = new IData();
		param.put("user_id", user_id );
		param.put("custname", custname );
		param.put("phone", phone );
		param.put("credit", credit );
		param.put("remark", remark==null?"":remark );
		param.put("user", user);
		Map result = custService.updateCreditUser(param);
		return result;
    }
	
	@RequestMapping(value = "/creditinfo.html")   
    public ModelAndView creditinfo(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
//		String user_id = request.getParameter("user_id");
//		modelMap.put("user_id", user_id);
    	return new ModelAndView("/cust/creditinfo");
    }
	
	@RequestMapping(value = "/payfeeforcreditpage.html")   
    public ModelAndView payfeeforcreditpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/payfeeforcredit");
    }
	
	@RequestMapping(value = "/payfeeForCredit.html")
	@ResponseBody
    public Object payfeeForCredit(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" payfeeForCredit  ");
		User user = this.getSessionUser(request);
		String user_id = request.getParameter("user_id");
		String recvfee = request.getParameter("recvfee");
		log.info(" recvfee = "+recvfee);
		IData param = new IData();
		param.put("user_id", user_id );
		param.put("recvfee", recvfee );
		param.put("user", user);
		Map result = custService.payfeeForCredit(param);
		return result;
    }
	
	@RequestMapping(value = "/queryVipCardList.html")
	@ResponseBody
    public Object queryVipCardList(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" queryVipCardList  ");
		User user = this.getSessionUser(request);
		String query_str = request.getParameter("query_str");
		log.info(" query_str = "+query_str);
		if(query_str==null){
			query_str="";
		}
		IData param = new IData();
		param.put("query_str", query_str.trim() );
		param.put("user", user);
		Map result = custService.queryVipCardList(param);
		return result;
    }
	
	@RequestMapping(value = "/addvipcardpage.html")   
    public ModelAndView addvipcardpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/addvipcard");
    }
	
	@RequestMapping(value = "/addvipcard.html")
	@ResponseBody
    public Object addvipcard(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" addvipcard  ");
		User user = this.getSessionUser(request);
		String card_no = request.getParameter("card_no");
		if(card_no==null) card_no="";
		String custname = request.getParameter("custname");
		if(custname==null) custname="";
		String phone = request.getParameter("phone");
		if(phone==null) phone="";
		String money = request.getParameter("money");
		if(money==null) money="0";
		String remark = request.getParameter("remark");
		if(remark==null) remark="";
		IData param = new IData();
		param.put("card_no", card_no );
		param.put("custname", custname );
		param.put("phone", phone );
		param.put("money", money );
		param.put("remark", remark );
		param.put("user", user);
		Map result = custService.addVipCard(param);
		return result;
    }
	
	@RequestMapping(value = "/editvipcardpage.html")   
    public ModelAndView editvipcardpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/editvipcard");
    }
	
	@RequestMapping(value = "/editvipcard.html")
	@ResponseBody
    public Object editvipcard(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" editvipcard  ");
		User user = this.getSessionUser(request);
		String user_id = request.getParameter("user_id");
		String custname = request.getParameter("custname");
		String phone = request.getParameter("phone");
		String credit = request.getParameter("credit");
		String remark = request.getParameter("remark");
		IData param = new IData();
		param.put("user_id", user_id );
		param.put("custname", custname );
		param.put("phone", phone );
		param.put("remark", remark==null?"":remark );
		param.put("user", user);
		Map result = custService.updateVipCard(param);
		return result;
    }
	
	@RequestMapping(value = "/vipcardinfo.html")   
    public ModelAndView vipcardinfo(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
//		String user_id = request.getParameter("user_id");
//		modelMap.put("user_id", user_id);
    	return new ModelAndView("/cust/vipcardinfo");
    }
	
	@RequestMapping(value = "/payfeeforvipcardpage.html")   
    public ModelAndView payfeeforvipcardpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/payfeeforvipcard");
    }
	
	@RequestMapping(value = "/payfeeForVipCard.html")
	@ResponseBody
    public Object payfeeForVipCard(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		log.info(" payfeeForVipCard  ");
		User user = this.getSessionUser(request);
		String user_id = request.getParameter("user_id");
		String recvfee = request.getParameter("recvfee");
		log.info(" recvfee = "+recvfee);
		IData param = new IData();
		param.put("user_id", user_id );
		param.put("recvfee", recvfee );
		param.put("user", user);
		Map result = custService.payfeeForVipCard(param);
		return result;
    }
	
	@RequestMapping(value = "/writevipcardpage.html")   
    public ModelAndView writevipcardpage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	return new ModelAndView("/cust/writevipcard");
    }
	
}
