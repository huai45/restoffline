package com.huai.dcb.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.huai.common.base.BaseController;
import com.huai.common.domain.IData;
import com.huai.common.domain.User;
import com.huai.common.service.FoodService;
import com.huai.common.util.ut;
import com.huai.dcb.service.AndroidService;
import com.huai.operation.dao.OperationDao;
import com.huai.operation.service.OperationService;
import com.huai.operation.service.OperationServiceImpl;
import com.huai.user.service.UserService;
import com.mongodb.BasicDBObject;

@Controller
@RequestMapping("/android")
public class AndroidAction extends BaseController {

	private static final Logger log = Logger.getLogger(AndroidAction.class);
	
	@Resource
	private UserService userService;
	
	@Resource(name="operationService")
	public OperationService operationService;
	
	@Resource(name="androidService")
	public AndroidService androidService;
	
	@Resource(name="foodService")
	public FoodService foodService;
	
	@Resource(name="operationDao")
	public OperationDao operationDao;
	
	@RequestMapping(value = "/login")   
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String user_id = request.getParameter("user_id");
		String password = request.getParameter("password");
		log.info(" ****************  AndroidController  login  ***********************  user_id : "+user_id+" , password : "+password);
		try{
			Map user = androidService.getPhoneUser(rest_id, user_id);
			if(user==null){
				modelMap.put("msg", ut.err("用户不存在！"));
				return new ModelAndView("/result", modelMap);
			}
			log.info(" user.getPassword() = "+user.get("password")+",");
			log.info("           password = "+password+",");
			log.info("           equal = "+(user.get("password").equals(password))+",");
			if(!user.get("password").equals(password)){
				modelMap.put("msg", ut.err("密码错误！"));
				return new ModelAndView("/result", modelMap);
			}
			User user_db = new User();
			user_db.setRest_id(rest_id);
			String ban = ut.currentDate();
			modelMap.put("msg", ut.suc("登录成功！","username",user.get("username").toString(),"ban",ban));
    	}catch(Exception e){
    		e.printStackTrace();
			modelMap.put("msg", ut.err("登录异常！"));
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/getData")   
    public ModelAndView getData(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String phone = request.getParameter("phone");
		log.info(" ****************  AndroidController  getData  ***********************  rest_id : "+rest_id+" , phone : "+phone);
		try{
			User user = new User();
			user.setRest_id(rest_id);
			
			IData param = new IData();
			param.put("rest_id", rest_id);
			
			List tables = operationService.queryTableList(param);
			List foods = foodService.queryFoodList(param);
			
			JSONArray jsonArr = new JSONArray();
			JSONObject j = new JSONObject(); 
			j.put("success", "true");
			j.put("msg", "同步数据完成！");
			j.put("tableList", JSONArray.fromObject(tables).toString());
			j.put("foodList", JSONArray.fromObject(foods).toString());
			
			jsonArr.add(j);
			modelMap.put("msg", jsonArr.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    		modelMap.put("msg", "4");
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/checkTableState")   
    public ModelAndView checkTableState(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String table_id = request.getParameter("table_id");
		log.info(" ****************  AndroidController  checkTableState  ***********************  rest_id : "+rest_id+" , table_id : "+table_id);
		try{
			User user = new User();
			user.setRest_id(rest_id);
			
			IData param = new IData();
			param.put("rest_id", rest_id);
			param.put("table_id", table_id);
			
			Map table = operationService.queryTableById(param);
			
			log.info(table);
			
			JSONArray jsonArr = new JSONArray();
			JSONObject j = new JSONObject(); 
			j.put("success", "true");
			j.put("state", table.get("STATE").toString());
			j.put("bill_id", table.get("BILL_ID").toString());
			j.put("msg", "查询完成！");
			
			jsonArr.add(j);
			modelMap.put("msg", jsonArr.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    		modelMap.put("msg", ut.err("查询异常！"));
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/openTable")   
    public ModelAndView openTable(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String table_id = request.getParameter("table_id");
		String staff = request.getParameter("staff");
		String nop = request.getParameter("nop");
		String user_id = request.getParameter("user_id");
		log.info(" ****************  AndroidController  openTable  ***********************  staff : "+staff+" , nop : "+nop);
		log.info(" ****************  AndroidController  openTable  ***********************  table_id : "+table_id+" , user_id : "+user_id);
		try{
			IData param = new IData();
			param.put("rest_id", rest_id);
			param.put("table_id", table_id);
			User user = new User();
			Map u =  androidService.getPhoneUser(rest_id, user_id);
			log.info("u="+u);
			user.setStaff_id(user_id);
			user.setStaffname(u.get("username").toString());
			param.put("user", user);
			
			Map table = operationService.queryTableById(param);
			if(table==null){
				modelMap.put("msg", ut.err("台位不存在！"));
				return new ModelAndView("/result", modelMap);
			}
			if(table.get("STATE").equals("1")){
				modelMap.put("msg", ut.suc(table.get("BILL_ID").toString()));
				return new ModelAndView("/result", modelMap);
			}
			param.put("nop", nop);
			operationService.openTable(param);
			table = operationService.queryTableById(param);
			modelMap.put("msg", ut.suc("开台成功！","bill_id",table.get("BILL_ID").toString()));
    	}catch(Exception e){
    		e.printStackTrace();
    		modelMap.put("msg", ut.err("开台异常！"));
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/saveBillItem")   
    public ModelAndView saveBillItem(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String user_id = request.getParameter("user_id");
		String table_id = request.getParameter("table_id");
		String trade_id = request.getParameter("trade_id");
		String totalnote = request.getParameter("totalnote");
		String param_str = request.getParameter("param_str");
		String staff = "";
		log.info(" ****************  AndroidController  saveBillItem  ***********************  param_str : "+param_str);
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
//		log.info("  param_str : "+param_str);
		try{
			IData param = new IData();
			param.put("rest_id", rest_id);
			param.put("table_id", table_id);
			
			User user = new User();
			Map u =  androidService.getPhoneUser(rest_id, user_id);
			log.info("u="+u);
			user.setStaff_id(user_id);
			user.setStaffname(u.get("username").toString());
			param.put("user", user);
			
			Map table = operationService.queryTableById(param);
			log.info(" table = "+table);
			if(table==null){
				modelMap.put("msg", ut.err("台位号错误！"));
				return new ModelAndView("/result", modelMap);
			}
			if(!table.get("STATE").equals("1")){
				modelMap.put("msg", ut.err("台位状态异常！"));
				return new ModelAndView("/result", modelMap);
			}
			Map staff_info = androidService.getPhoneUser(rest_id,user_id);
			staff = staff_info.get("username").toString();
			user.setStaffname(staff);
			param.put("table_id", table_id);
			param.put("table_name", table.get("table_name"));
			param.put("nop", table.get("nop"));
			param.put("start_time", ut.currentTime());
			param.put("bill_id", table.get("bill_id"));
			param.put("BILL_ID", table.get("bill_id"));
			List orders = new ArrayList();
			String[] items = param_str.split("END");
			
			log.info("  param =  "+param);
			Map bill = (Map)operationService.queryBillById(param);
			String now = ut.currentTime();
			log.info("  bill =  "+bill);
			log.info("  items.length =  "+items.length);
			for(int i=0;i<items.length;i++){
//				log.info("items["+i+"]"+items[i]);
				if(ut.isEmpty(items[i])){
					continue;
				}
		        String[] temp = items[i].split("#"); 
		        String call_type = "1";
		        if(!ut.isEmpty(temp[3])&&temp[3].equals("0")){
		        	call_type = "0";
		        }
		        String food_id = temp[0];
		        param.put("food_id", food_id);
		        Map food = foodService.queryFoodById(param);
		        log.info("food："+food);
		        if(food==null){
		        	continue;
		        }
		        JSONObject item = new JSONObject(); 
		        log.info(" ****************  item = "+item.toString());
		        item.put("table_id", bill.get("TABLE_ID"));
		        item.put("table_name", bill.get("TABLE_NAME"));
		        item.put("bill_id", bill.get("BILL_ID"));
		        item.put("food_id", food_id );
		        item.put("food_name", food.get("FOOD_NAME"));
		        item.put("price", food.get("PRICE"));
		        item.put("unit", food.get("UNIT"));
		        item.put("category", food.get("CATEGORY"));
		        item.put("groups", food.get("GROUPS"));
		        item.put("nop", bill.get("NOP"));
		        item.put("print_count", food.get("PRINT_COUNT"));
		        item.put("cook_tag", food.get("COOK_TAG"));
		        item.put("cook_time", food.get("COOK_TIME"));
		        item.put("time", now);
		        item.put("trade_id", trade_id);
		        item.put("note", (totalnote==null?"":totalnote+" ")+temp[2]);
		        item.put("count", temp[1]);
		        item.put("call_type", call_type);
		        item.put("package_id", "");
		        item.put("package_name", "");
		        item.put("package_price", "");
		        item.put("printer", food.get("PRINTER"));
		        item.put("printer_sec", food.get("PRINTER_SEC"));
		        item.put("printer_start", food.get("PRINTER_START"));
		        item.put("printer_hurry", food.get("PRINTER_HURRY"));
		        item.put("printer_back", food.get("PRINTER_BACK"));
		        
		        item.put("table_id".toUpperCase(), bill.get("TABLE_ID"));
		        item.put("table_name".toUpperCase(), bill.get("TABLE_NAME"));
		        item.put("bill_id".toUpperCase(), bill.get("BILL_ID"));
		        item.put("food_id".toUpperCase(), food_id );
		        item.put("food_name".toUpperCase(), food.get("FOOD_NAME"));
		        item.put("price".toUpperCase(), food.get("PRICE"));
		        item.put("unit".toUpperCase(), food.get("UNIT"));
		        item.put("category".toUpperCase(), food.get("CATEGORY"));
		        item.put("groups".toUpperCase(), food.get("GROUPS"));
		        item.put("nop".toUpperCase(), bill.get("NOP"));
		        item.put("print_count".toUpperCase(), food.get("PRINT_COUNT"));
		        item.put("cook_tag".toUpperCase(), food.get("COOK_TAG"));
		        item.put("cook_time".toUpperCase(), food.get("COOK_TIME"));
		        item.put("time".toUpperCase(), now);
		        item.put("trade_id".toUpperCase(), trade_id);
		        item.put("note".toUpperCase(), (totalnote==null?"":totalnote+" ")+temp[2]);
		        item.put("count".toUpperCase(), temp[1]);
		        item.put("call_type".toUpperCase(), call_type);
		        item.put("package_id".toUpperCase(), "");
		        item.put("package_name".toUpperCase(), "");
		        item.put("package_price".toUpperCase(), "");
		        item.put("printer".toUpperCase(), food.get("PRINTER"));
		        item.put("printer_sec".toUpperCase(), food.get("PRINTER_SEC"));
		        item.put("printer_start".toUpperCase(), food.get("PRINTER_START"));
		        item.put("printer_hurry".toUpperCase(), food.get("PRINTER_HURRY"));
		        item.put("printer_back".toUpperCase(), food.get("PRINTER_BACK"));
		        
		        IData item_temp = new IData(item);
		        orders.add(item_temp);
			}
			param.put("bill", bill);
			param.put("items", orders);
			String result = operationDao.saveBillItems(param);
			modelMap.put("msg", ut.suc("点菜成功！"));
			log.info("  点菜成功  "+result);
    	}catch(Exception e){
    		e.printStackTrace();
    		modelMap.put("msg", ut.err("点菜服务发生异常！"));
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/printBill")   
    public ModelAndView printBill(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String username = request.getParameter("username");
		String table_id = request.getParameter("table_id");
		log.info(" ****************  AndroidController  printBill  ***********************  table_id : "+table_id);
		log.info(" username : "+username);
		try{
			User user = new User();
			user.setRest_id(rest_id);
			IData param = new IData();
			param.put("rest_id", rest_id);
			param.put("table_id", table_id);
			
			Map table = operationService.queryTableById(param);
			log.info("table:"+table);
			if(table==null){
				modelMap.put("msg", ut.err("无此台位"));  // 无此台位
				return new ModelAndView("/result", modelMap);
			}
			if(!table.get("STATE").toString().equals("1")){
				modelMap.put("msg", ut.err("暂未开台!"));  // 暂未开台
				return new ModelAndView("/result", modelMap);
			}
			if(table.get("BILL_ID")==null||table.get("BILL_ID").toString().equals("")){
				modelMap.put("msg", ut.err("无账单可打印!"));  // 无账单可打印
				return new ModelAndView("/result", modelMap);
			}
			param.put("bill_id", table.get("BILL_ID"));
			param.put("table_name", table.get("TABLE_NAME"));
			param.put("printer", table.get("PRINTER"));
			param.put("phone", username);
			String result = androidService.printQueryBill(user,param);
			modelMap.put("msg", result);
			log.info("  printBill  打印成功");
    	}catch(Exception e){
    		modelMap.put("msg", ut.err("打印异常!"));  // 打印异常
    		e.printStackTrace();
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/startCook")   
    public ModelAndView startCook(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String username = request.getParameter("username");
		String table_id = request.getParameter("table_id");
		log.info(" ****************  AndroidController  startCook  ***********************  table_id : "+table_id);
		try{
//			User user = new User();
//			user.setRest_id(rest_id);
//			Map param = new HashMap();
//			param.put("table_id", table_id);
//			Map table = tableService.queryTableById(user, table_id);
//			if(table==null){
//				modelMap.put("msg", ut.err("无此台位"));  // 无此台位
//				return new ModelAndView("/result", modelMap);
//			}
//			if(!table.get("STATE").toString().equals("1")){
//				modelMap.put("msg", ut.err("暂未开台!"));  // 暂未开台
//				return new ModelAndView("/result", modelMap);
//			}
//			if(table.get("BILL_ID")==null||table.get("BILL_ID").toString().equals("")){
//				modelMap.put("msg", ut.err("台位无账单!"));  // 无账单可打印
//				return new ModelAndView("/result", modelMap);
//			}
//			param.put("bill_id", table.get("BILL_ID"));
//			param.put("table_name", table.get("TABLE_NAME"));
//			param.put("printer", table.get("PRINTER"));
//			param.put("phone", username);
//			String result = androidService.printQueryBill(user,param);
//			modelMap.put("msg", result);
			modelMap.put("msg", ut.suc("起菜功能暂未开放!"));
			log.info("  startCook  起菜完成");
    	}catch(Exception e){
    		modelMap.put("msg", ut.err("起菜异常!"));  // 打印异常
    		e.printStackTrace();
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/startSingleCook")   
    public ModelAndView startSingleCook(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String username = request.getParameter("username");
		String bill_id = request.getParameter("bill_id");
		String item_id = request.getParameter("item_id");
		log.info(" ****************  AndroidController  startSingleCook  ***********************  bill_id : "+bill_id+"  , item_id : "+item_id);
		try{
//			User user = new User();
//			user.setRest_id(rest_id);
//			Map param = new HashMap();
//			param.put("table_id", table_id);
//			Map table = tableService.queryTableById(user, table_id);
//			if(table==null){
//				modelMap.put("msg", ut.err("无此台位"));  // 无此台位
//				return new ModelAndView("/result", modelMap);
//			}
//			if(!table.get("STATE").toString().equals("1")){
//				modelMap.put("msg", ut.err("暂未开台!"));  // 暂未开台
//				return new ModelAndView("/result", modelMap);
//			}
//			if(table.get("BILL_ID")==null||table.get("BILL_ID").toString().equals("")){
//				modelMap.put("msg", ut.err("台位无账单!"));  // 无账单可打印
//				return new ModelAndView("/result", modelMap);
//			}
//			param.put("bill_id", table.get("BILL_ID"));
//			param.put("table_name", table.get("TABLE_NAME"));
//			param.put("printer", table.get("PRINTER"));
//			param.put("phone", username);
//			String result = androidService.printQueryBill(user,param);
//			modelMap.put("msg", result);
			modelMap.put("msg", ut.suc("起菜功能暂未开放!"));
			log.info("  startSingleCook  起菜完成");
    	}catch(Exception e){
    		modelMap.put("msg", ut.err("起菜异常!"));  // 打印异常
    		e.printStackTrace();
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/hurrySingleCook")   
    public ModelAndView hurrySingleCook(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String username = request.getParameter("username");
		String bill_id = request.getParameter("bill_id");
		String item_id = request.getParameter("item_id");
		log.info(" ****************  AndroidController  hurrySingleCook  ***********************  username : "+username+" ,bill_id : "+bill_id+"  , item_id : "+item_id);
		try{
			User user = new User();
			user.setStaff_id(username);
			user.setStaffname(username);
			user.setRest_id(rest_id);
			
			IData param = new IData();
			param.put("rest_id", rest_id);
			param.put("bill_id", bill_id);
			param.put("BILL_ID", bill_id);
			param.put("item_id", item_id);
			
			Map bill = (Map)operationService.queryBillById(param);
			Map item = (Map)operationService.queryBillItemByItemId(param);
			String food_id = item.get("FOOD_ID").toString();
			param.put("food_id", food_id);
			Map food = foodService.queryFoodById(param);
			item.putAll(food);
			item.put("TIME", ut.currentTime());
			item.put("TABLE_ID", bill.get("TABLE_ID"));
			item.put("TABLE_NAME", bill.get("TABLE_NAME"));
			item.put("NOP", bill.get("NOP"));
			item.put("OPER_STAFF_ID", username);
			item.put("OPER_STAFF_NAME", username);
			item.put("PRINT_COUNT", "1");
			log.info("item:"+item);
			
			List list = new ArrayList();
			list.add(item);
			param.put("user", user);
			param.put("bill", bill);
			param.put("items", list);
			String result = operationDao.hurryCook(param);
			modelMap.put("msg", ut.suc("催菜完成") );
			log.info("  hurrySingleCook  催菜完成");
    	}catch(Exception e){
    		modelMap.put("msg", ut.err("催菜异常!"));  // 打印异常
    		e.printStackTrace();
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/finishSingleCook")  
    public ModelAndView finishSingleCook(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String username = request.getParameter("username");
		String bill_id = request.getParameter("bill_id");
		String item_id = request.getParameter("item_id");
		ut.log(" ****************  AndroidController  finishSingleCook  ***********************  username : "+username+" ,bill_id : "+bill_id+"  , item_id : "+item_id);
		try{
			User user = new User();
			user.setStaff_id(username);
			user.setStaffname(username);
			user.setRest_id(rest_id);
			
			IData param = new IData();
			param.put("rest_id", rest_id);
			param.put("bill_id", bill_id);
			param.put("BILL_ID", bill_id);
			param.put("item_id", item_id);
			
			Map bill = (Map)operationService.queryBillById(param);
			Map item = (Map)operationService.queryBillItemByItemId(param);
			
			
			item.put("TIME", ut.currentTime());
			item.put("TABLE_ID", bill.get("TABLE_ID"));
			item.put("TABLE_NAME", bill.get("TABLE_NAME"));
			item.put("NOP", bill.get("NOP"));
			item.put("OPER_STAFF_ID", username);
			item.put("OPER_STAFF_NAME", username);
			List list = new ArrayList();
			list.add(item);
			
			param.put("bill", bill);
			param.put("items", list);
			String result = operationDao.finishCook(param);
			modelMap.put("msg", ut.suc("上菜完成") );
			ut.log("  finishSingleCook  上菜完成");
    	}catch(Exception e){
    		modelMap.put("msg", ut.err("上菜异常!"));  // 打印异常
    		e.printStackTrace();
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	@RequestMapping(value = "/queryTableInfo")   
    public ModelAndView queryTableInfo(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
		String rest_id = request.getParameter("rest_id");
		String table_id = request.getParameter("table_id");
		log.info(" ****************  AndroidController  queryTableInfo  ***********************  table_id : "+table_id);
		try{
			User user = new User();
			user.setRest_id(rest_id);
			IData param = new IData();
			param.put("rest_id", rest_id);
			param.put("table_id", table_id);
			Map table = operationService.queryTableById(param);
			JSONArray jsonArr = new JSONArray();
			JSONObject j = new JSONObject(); 
			j.put("success", "true");
			j.put("msg", "查询完成！");
			if(table==null){
				j.put("success", "false");
				j.put("msg", "查询无此台位！");
			}
			j.put("state", table.get("STATE").toString());
			j.put("table", JSONObject.fromObject(table).toString());
			if(table.get("STATE").toString().equals("1")){
				
				log.info("table1="+table);
				j.put("bill_id", table.get("bill_id").toString());
				param.put("bill_id", table.get("bill_id"));
				param.put("BILL_ID", table.get("bill_id"));
				log.info("param="+param);
				Map bill = (Map) operationService.queryBillById(param);
				log.info("bill = "+bill);
				bill.put("START_TIME", bill.get("OPEN_TIME"));
				List items = (List)bill.get("ITEMLIST");
				j.put("bill", JSONObject.fromObject(bill).toString());
				j.put("items", JSONArray.fromObject(items).toString());
			}
			jsonArr.add(j);
			modelMap.put("msg", jsonArr.toString());
    	}catch(Exception e){
    		JSONArray jsonArr = new JSONArray();
			JSONObject j = new JSONObject(); 
			j.put("success", "false");
			j.put("msg", "查询异常！");
			jsonArr.add(j);
			modelMap.put("msg", jsonArr.toString());
    		e.printStackTrace();
    	}
    	return new ModelAndView("/result", modelMap);
    }
	
	
	
}

