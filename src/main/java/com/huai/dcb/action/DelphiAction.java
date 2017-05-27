package com.huai.dcb.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.huai.common.base.BaseController;
import com.huai.common.dao.BaseDao;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.service.FoodService;
import com.huai.common.util.*;
import com.huai.dcb.service.DelphiService;
import com.huai.operation.dao.OperationDao;
import com.huai.operation.dao.TableDao;
import com.huai.operation.service.OperationService;

@Controller
@RequestMapping("/delphi")
public class DelphiAction extends BaseController {

	private static final Logger log = Logger.getLogger(DelphiAction.class);
	
	@Resource(name="operationService")
	public OperationService operationService;
	
	@Resource(name="delphiService")
	public DelphiService delphiService;
	
	@Resource(name="tableDao")
	public TableDao tableDao;
	
	@Resource(name="baseDao")
	public BaseDao baseDao;
	
	@Resource(name="foodService")
	public FoodService foodService;
	
	@Resource(name="operationDao")
	public OperationDao operationDao;
	
	// 返回值 ：  0 - 登陆成功   1 - 无此用户  2 - 密码错误 3 - 其他异常 
	@RequestMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String phone = request.getParameter("phone");
		String user_id = request.getParameter("staff");
		String password = request.getParameter("password");
		log.info(" ****************  DelphiController  login  *********************** phone="+phone+" ,  user_id : "+user_id+" , password : "+password);
		log.info(" ****************  DelphiController  login  rest_id : "+rest_id);
		try{
			Map user = delphiService.getPhoneUser(rest_id,user_id);
			if(user==null){
				modelMap.put("msg", "1");
			}else {
				if(user.get("PASSWORD").equals(password)){
					modelMap.put("msg", "0");
					baseDao.jdbcTemplate.update(" update td_phone set username = ? ,login_time = ? where rest_id = ? and phone = ? ",
						new Object[]{ user.get("USERNAME"), ut.currentTime(), rest_id, phone} );
				}else{
					modelMap.put("msg", "2");
				}
			}
    	}catch(Exception e){
    		modelMap.put("msg", "3");
    		e.printStackTrace();
    	}
    	return new ModelAndView("/common/json", modelMap);
    }
	
	// 返回值  ：   0 - 开台成功   1 - 无此员工  2 - 无此台位 3 - 正在使用中  4 - 其他异常
	@RequestMapping(value = "/openTable")   
    public ModelAndView openTable(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String table_id = request.getParameter("table_id");
		String staff = request.getParameter("staff");
		String nop = request.getParameter("nop");
		String phone = request.getParameter("phone");
		
		log.info(" ****************  DelphiController  openTable  rest_id : "+rest_id);
		log.info(" ****************  DelphiController  openTable  table_id : "+table_id);
		log.info(" ****************  DelphiController  openTable  staff : "+staff);
		log.info(" ****************  DelphiController  openTable  phone : "+phone);
		
		try{
			IData table = tableDao.queryTableById(rest_id, table_id);
			if(table==null){
				modelMap.put("msg", "2");
			}else if(!table.get("STATE").equals("0")){
				modelMap.put("msg", "3");
			}else{
				IData param = new IData();
				param.put("rest_id", rest_id);
				param.put("table_id", table_id);
				param.put("nop", nop);
				param.put("staff", staff);
				param.put("phone", phone);
				delphiService.openTable(param);
				modelMap.put("msg", "0");
			}
    	}catch(Exception e){
    		modelMap.put("msg", "4");
    		e.printStackTrace();
    	}
    	return new ModelAndView("/common/json", modelMap);
    }
	
	// 返回值 ：  0 - 点菜成功   1 - 无此台位  2 - 流水重复 3 - 餐台无账单  4 - 其他异常
	@RequestMapping(value = "/saveBillItem")   
    public ModelAndView saveBillItem(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String phone = request.getParameter("phone");
		String table_id = request.getParameter("table_id");
		String trade_id = request.getParameter("trade_id");
		String totalnote = request.getParameter("totalnote");
		String param_str = request.getParameter("param_str");
		String staff = "";
		log.info(" ****************  DelphiController  saveBillItem  ***********************  totalnote : "+totalnote);
		log.info(" ****************  phone : "+phone);
		log.info(" ****************  rest_id : "+rest_id);
		log.info(" ****************  table_id : "+table_id);
		log.info(" ****************  trade_id : "+trade_id);
		log.info(" ****************  param_str : "+param_str);
//		log.info("  rest_id : "+rest_id);
//		log.info("  phone : "+phone);
//		log.info("  table_id : "+table_id);
//		log.info("  trade_id : "+trade_id);
//		log.info("  totalnote : "+totalnote);
//		try {
//			log.info("  totalnote1 : "+new String(totalnote.getBytes("GBK"),"UTF-8"));
//			log.info("  totalnote3 : "+new String(totalnote.getBytes("GBK"),"ISO-8859-1"));
//			log.info("  totalnote2 : "+new String(totalnote.getBytes("UTF-8"),"GBK"));
//			log.info("  totalnote4 : "+new String(totalnote.getBytes("UTF-8"),"ISO-8859-1"));
//			log.info("  totalnote5 : "+new String(totalnote.getBytes("ISO-8859-1"),"GBK"));
//			log.info("  totalnote6 : "+new String(totalnote.getBytes("ISO-8859-1"),"UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		log.info("  param_str : "+param_str);
		// msg :  0 - 点菜成功   1 - 无此台位  2 - 流水重复 3 - 餐台无账单  4 - 其他异常
		try{
			IData table = tableDao.queryTableById(rest_id, table_id);
			if(table==null){
				modelMap.put("msg", "1");
				return new ModelAndView("/common/json", modelMap);
			}
			if(!table.get("STATE").equals("1")){
				modelMap.put("msg", "3");
				return new ModelAndView("/common/json", modelMap);
			}
			boolean flag = delphiService.checkTrade(rest_id, trade_id);
			if(flag){
				ut.p(" checkTrade  不通过  trade_id =  !"+trade_id+"! ,rest_id = "+rest_id);
				modelMap.put("msg", "2");
				return new ModelAndView("/common/json", modelMap);
			}
			Map staff_info = delphiService.getPhoneInfo(rest_id,phone);
			staff = staff_info.get("USERNAME").toString();
			IData param = new IData();
			User user = new User();
			user.setRest_id(rest_id);
			user.setStaff_id(phone);
			user.setStaffname(staff);
			param.put("rest_id", rest_id);
			param.put("user", user);
			
			List orders = new ArrayList();
			String[] items = param_str.split("END");
			IData bill = operationDao.queryBillByTable(rest_id, table_id);
//			log.info("  bill =  "+bill);
//			log.info("  items.length =  "+items.length);
			for(int i=0;i<items.length;i++){
//				log.info("items["+i+"]"+items[i]);
				if(ut.isEmpty(items[i])){
					continue;
				}
		        String[] temp = items[i].split("#"); 
		        String call_type = "1";
		        if(!ut.isEmpty(temp[3])&&temp[3].equals("1")){
		        	call_type = "0";
		        }
		        String food_id = temp[0];
		        param.put("food_id", food_id);
		        IData food = foodService.queryFoodById(param);
		        if(food==null){
		        	continue;
		        }
		        food.put("COUNT", temp[1]);
		        food.put("NOTE", ((totalnote==null||totalnote.trim().equals(""))?"":totalnote+" ")+temp[2]);
		        food.put("CALL_TYPE", call_type );
		        food.put("TRADE_ID", trade_id);
		        orders.add(food);
			}
			param.put("bill", bill);
			param.put("items", orders);
			log.info(" param :"+param);
			String msg = operationDao.saveBillItems(param);
			modelMap.put("msg", "0");
			log.info(" 点菜宝 点菜 成功   !");
    	}catch(Exception e){
    		modelMap.put("msg", "4");
    		e.printStackTrace();
    	}
    	return new ModelAndView("/common/json", modelMap);
    }
	
	// 返回值 ： 0 - 打印成功   1 - 无此台位  2 - 暂未开台 3 - 其他异常
	@RequestMapping(value = "/printBill")   
    public ModelAndView printBill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String table_id = request.getParameter("table_id");
		String phone = request.getParameter("phone");
		log.info(" ****************  DelphiController  printBill  ***********************  phone : "+phone+" , table_id : "+table_id);
		try{
			User user = new User();
			user.setRest_id(rest_id);
			IData param = new IData();
			param.put("user", user);
			param.put("table_id", table_id);
			param.put("rest_id", rest_id);
			param.put("phone", phone);
			IData table = tableDao.queryTableById(rest_id, table_id);
			if(table==null){
				modelMap.put("msg", "1");  // 无此台位
				return new ModelAndView("/common/json", modelMap);
			}
			IData bill = operationDao.queryBillByTable(rest_id, table_id);
			if(bill==null){
				modelMap.put("msg", "2");  // 无账单可打印
				return new ModelAndView("/common/json", modelMap);
			}
			param.put("table", table);
			param.put("bill", bill);
			String result = delphiService.printQueryBill(param);
			modelMap.put("msg", result);
			log.info("  printBill  打印成功");
    	}catch(Exception e){
    		modelMap.put("msg", "4");  // 打印异常
    		log.info("打印异常");
    		e.printStackTrace();
    	}
    	return new ModelAndView("/common/json", modelMap);
    }
	
	
	
	
	
	
}
