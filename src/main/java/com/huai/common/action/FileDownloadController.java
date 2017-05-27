package com.huai.common.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huai.common.base.BaseController;
import com.huai.common.domain.User;
import com.huai.common.util.GetBean;
import com.huai.common.util.ut;


@Controller
@RequestMapping("/file")
public class FileDownloadController extends BaseController {
    
	@RequestMapping(value = "/exportFoodCategoryFile.html")   
    public void exportFoodCategoryFile(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	System.out.println("*************************  exportFoodCategoryFile  *************************");
		String sql_category = " select distinct category from td_food where rest_id = ? order by category desc ";
		User user = this.getSessionUser(request);
		JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
		List category_list = jdbcTemplate.queryForList(sql_category, new Object[]{ user.getRest_id() });
		try {
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			String fileName = "菜品类别表.txt";
		    fileName = URLEncoder.encode(fileName, "UTF-8");  
		    response.reset();
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
//		    response.addHeader("Content-Length", "" + data.length);  
		    response.setContentType("application/vnd.ms-excel;charset=UTF-8");  
			for(int i=0;i<category_list.size();i++){
				Map category = (Map)category_list.get(i);
				String index = i>9?""+(i+1):"0"+(i+1);
				String line = index+category.get("CATEGORY").toString()+"\r\n";
				byte b[]=line.getBytes();  
			    outputStream.write(b);  
			}
		    outputStream.flush();  
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	@RequestMapping(value = "/exportFoodFile")   
    public void exportFoodFile(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	System.out.println("*************************  exportFoodFile  *************************");
		String sql_category = " select distinct category from td_food where rest_id = ? order by category desc ";
		String sql_food = "select a.FOOD_ID,a.CATEGORY,a.FOOD_NAME,a.PRICE,a.UNIT ,a.ABBR " +
		    " from td_food a where a.rest_id = ?  order by a.FOOD_ID ";
		User user = this.getSessionUser(request);
		JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
		List category_list = jdbcTemplate.queryForList(sql_category, new Object[]{ user.getRest_id() });
		Map categoryMap = new HashMap();
		for(int i=0;i<category_list.size();i++){
			String index = i>9?""+(i+1):"0"+(i+1);
			Map category = (Map)category_list.get(i);
			categoryMap.put(category.get("CATEGORY").toString(),index);
		}
		ut.log("categoryMap:"+categoryMap);
		List food_list = jdbcTemplate.queryForList(sql_food, new Object[]{ user.getRest_id() });
		try {
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			String fileName = "菜品表.txt";
		    fileName = URLEncoder.encode(fileName, "UTF-8");  
		    response.reset();
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
//		    response.addHeader("Content-Length", "" + data.length);  
		    response.setContentType("application/vnd.ms-excel;charset=UTF-8");  
			for(int i=0;i<food_list.size();i++){
				Map food = (Map)food_list.get(i);
				String id = food.get("FOOD_ID").toString();
				String name = food.get("FOOD_NAME").toString();
				if(name.length()>9){
					name = name.substring(0,9);
				}
				String category = food.get("CATEGORY").toString();
				category = categoryMap.get(category).toString();
				String abbr = food.get("ABBR").toString();
				String price = food.get("PRICE").toString();
				price = ut.getDoubleString(Double.parseDouble(price));
				String unit = food.get("UNIT").toString();
				String line = ut.lpad(id, 5)+category+ut.rpad(name, 18)+ut.lpad(price, 8)+ut.lpad(price, 8)+ut.lpad(price, 8)+ut.lpad(price, 8);
				line = line + ut.lpad(unit, 4)+ut.lpad(unit, 4)+ut.lpad(unit, 4)+ut.lpad(unit, 4)+"0"+ut.lpad(" ", 75)+abbr+"\r\n";
				byte b[]=line.getBytes();  
			    outputStream.write(b);  
			}
		    outputStream.flush();  
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
    
	
	@RequestMapping(value = "/exportCategorySaleDetailFile")   
    public void exportCategorySaleDetailFile(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	System.out.println("*************************  exportCategorySaleDetailFile  *************************");
    	String path = GetBean.getContext().getServletContext().getRealPath("/")+"files\\";
    	JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
		User user = this.getSessionUser(request);
		String start_date = request.getParameter("start_date");
    	String end_date = request.getParameter("end_date");
    	Map param = new HashMap();
    	param.put("start_date", start_date);
    	param.put("end_date", end_date);
    	ut.log(" param :" +param);
    	String sql = " select  food_name , unit , groups category , sum(count) count , sum(count-back_count) real_count,sum(back_count) back_count ,sum(free_count) free_count "+ 
		        " from th_bill_item where 1 = 1 and rest_id = ? " ;
		if(param.containsKey("CATEGORY")){
		sql = sql +  " and CATEGORY = '"+param.get("CATEGORY")+"' ";
		}
		sql = sql +  " and OPER_TIME >= ? and OPER_TIME <= ? "+
		        " group by food_name ,unit , category order by sum(count) desc ";
		ut.log(" sql :" +sql);
		List datas = jdbcTemplate.queryForList(sql,new Object[]{user.getRest_id(),param.get("start_date")+" 00:00:00",param.get("end_date")+" 23:59:59"});
		Map categoryMap = new HashMap();
		for(int i=0;i<datas.size();i++){
			Map data = (Map)datas.get(i);
			if(categoryMap.containsKey(data.get("CATEGORY"))){
				List temp = (List)categoryMap.get(data.get("CATEGORY"));
				temp.add(data);
			}else{
				List temp = new ArrayList(); 
				temp.add(data);
				categoryMap.put(data.get("CATEGORY"), temp);
			}
		}
		ut.log(" datas :" +datas.size());
		HSSFWorkbook wb = new HSSFWorkbook();
		if(datas.size()==0){
			int rowNum = 0; 
            int colNum = 0;
            HSSFSheet sheet = wb.createSheet("查询结果");   
			HSSFRow row = sheet.createRow(rowNum++); 
            HSSFCell cell = row.createCell((short)colNum++); 
            cell.setCellValue(new HSSFRichTextString("未查询到相关的数据！"));
		}else{
			Set<String> keys = categoryMap.keySet();
	        for (Iterator it = keys.iterator(); it.hasNext();) {
	            String key = (String) it.next();
	            List data = (List)categoryMap.get(key);
	            HSSFSheet sheet = wb.createSheet(key);   
	            int rowNum = 0; 
	            int colNum = 0;
	            HSSFRow row = sheet.createRow(rowNum++); 
	            HSSFCell cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("名称"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("单位"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("发生总数量"));
	            cell = row.createCell((short)colNum++);
	            cell.setCellValue(new HSSFRichTextString("实际出库数量"));
	            cell = row.createCell((short)colNum++);
	            cell.setCellValue(new HSSFRichTextString("退菜数量"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("赠送数量"));
	            cell = row.createCell((short)colNum++); 
	            
	            for(int i=0;i<data.size();i++){   
	            	Map item = (Map)data.get(i);
	                row = sheet.createRow(rowNum++);   
	                colNum=0;   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("FOOD_NAME").toString()));   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("UNIT").toString()));   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("COUNT").toString()));
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("REAL_COUNT").toString()) ); 
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("BACK_COUNT").toString()) ); 
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("FREE_COUNT").toString()) ); 
	            }
	        }
		}
        String fileName = "档口销售明细_"+start_date+"-"+end_date+".xls";
        File file = new File(path+fileName);
        try {
        	OutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
        	fileName = URLEncoder.encode(fileName, "UTF-8");  
    	    response.reset();  
    	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
//    	    response.addHeader("Content-Length", "" + data.length);  
    	    response.setContentType("application/vnd.ms-excel;charset=UTF-8");  
        	OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        	InputStream is = new FileInputStream(file);
        	byte[] buffer = new byte[1024*1000];
        	int read = -1;
        	while((read=is.read(buffer))!=-1){
        		outputStream.write(buffer); 
        	}       
            outputStream.flush();  
		    outputStream.close();
		    is.close();
        } catch (Exception e) {
         e.printStackTrace();
        }
		
    }
	
	//@yuanyl@2014/8/9@档口楼层销售统计
	@RequestMapping(value = "/exportFloorSaleDetailFile")   
    public void exportFloorSaleDetailFile(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	System.out.println("*************************  exportFloorSaleDetailFile  *************************");
    	String path = GetBean.getContext().getServletContext().getRealPath("/")+"files\\";
    	JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
		User user = this.getSessionUser(request);
		String start_date = request.getParameter("start_date");
    	String end_date = request.getParameter("end_date");
    	Map param = new HashMap();
    	param.put("start_date", start_date);
    	param.put("end_date", end_date);
    	ut.log(" param :" +param);
    	String sql = "select tmp.groups,tmp.floor,tmp.money from ((select a.groups groups ,c.floor1 floor, FORMAT(sum( a.price* (a.count-a.back_count-a.free_count)*pay_rate/100 ),2) money "
    		+ "from th_bill_item a, th_bill b , td_table c  "
    		+ "where a.bill_id = b.bill_id  and a.rest_id = ? and b.rest_id = ? and c.rest_id = ? "
    		+ " and b.table_id = c.table_id "
    		+ " and a.oper_time >= ? and a.oper_time <= ? and b.open_date >= ? and b.open_date <= ?  "
    		+ " and a.groups in ('海鲜','鲍参翅','川菜','粤菜','本地菜','凉菜','烧腊','面点') "
    		+ " group by c.floor1 , a.groups order by a.groups ,c.floor1 ) "
    		+ "UNION ALL "
    		+ "(select a.category groups,c.floor1 floor, FORMAT(sum( a.price* (a.count-a.back_count-a.free_count)*pay_rate/100 ),2) money "
    		+ "from th_bill_item a, th_bill b , td_table c   "
    		+ "where a.bill_id = b.bill_id  and a.rest_id = ? and b.rest_id = ? and c.rest_id = ? "
    		+ " and b.table_id = c.table_id "
    		+ " and a.oper_time >= ? and a.oper_time <= ? and b.open_date >= ? and b.open_date <= ?   "
    		+ " and a.category in ('酒水') "
    		+ " group by c.floor1 , a.category order by a.category ,c.floor1  )) tmp "
    		+"  order by tmp.groups,tmp.floor ";
		ut.log(" sql :" +sql);
		List datas =jdbcTemplate.queryForList(sql,new Object[]{user.getRest_id(),user.getRest_id(),user.getRest_id(),start_date+" 00:00:00",end_date+" 23:59:59",start_date,end_date,user.getRest_id(),user.getRest_id(),user.getRest_id(),start_date+" 00:00:00",end_date+" 23:59:59",start_date,end_date});
		
		ut.log(" datas :" +datas.size());
		HSSFWorkbook wb = new HSSFWorkbook();
		if(datas.size()==0){
			int rowNum = 0; 
            int colNum = 0;
            HSSFSheet sheet = wb.createSheet("查询结果");   
			HSSFRow row = sheet.createRow(rowNum++); 
            HSSFCell cell = row.createCell((short)colNum++); 
            cell.setCellValue(new HSSFRichTextString("未查询到相关的数据！"));
		}else{
			    //创建第一分页 begin
	            HSSFSheet sheet = wb.createSheet("第一页");   
	            int rowNum = 0; 
	            int colNum = 0;
	            HSSFRow row = sheet.createRow(rowNum++); 
	            HSSFCell cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("类别"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("楼层"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("销售金额"));
	            
	            for(int i=0;i<datas.size();i++){   
	            	Map item = (Map)datas.get(i);
	                row = sheet.createRow(rowNum++);   
	                colNum=0;   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("GROUPS").toString()));   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("FLOOR").toString()));   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("MONEY").toString()));
	            }
	            
	           //创建第一分页 end
	          
	           String sql2 ="select tmp.groups, "
	          +" max(case tmp.floor when '一楼' then tmp.money else 0 end ) f1, " 
	          +" max(case tmp.floor when '二楼新区' then tmp.money else 0 end ) f2n, "
	          +" max(case tmp.floor when '二楼老区' then tmp.money else 0 end ) f2o, "
	          +" max(case tmp.floor when '三楼新区' then tmp.money else 0 end ) f3n, "
	          +"max(case tmp.floor when '三楼老区' then tmp.money else 0 end ) f3o, "
	          +" max(case tmp.floor when '外卖' then tmp.money else 0 end ) w "
	          +"from ((select a.groups  groups ,c.floor1 floor,  "
	          +"       FORMAT(sum( a.price* (a.count-a.back_count-a.free_count)*pay_rate/100 ),2) money "
	          +"  from th_bill_item a, th_bill b , td_table c   "
	          +"where a.bill_id = b.bill_id  and a.rest_id = ? and b.rest_id = ? and c.rest_id = ? "
	          +"    and b.table_id = c.table_id "
	          +"    and a.oper_time >= ? and a.oper_time <= ? and b.open_date >= ? and b.open_date <= ?   "
	          +"    and a.groups in ('海鲜','鲍参翅','川菜','粤菜','本地菜','凉菜','烧腊','面点') "
	          +"     group by c.floor1 , a.groups order by a.groups ,c.floor1  ) "
	          +"union all "
	          +" (select a.category groups,c.floor1 floor,  "
	          +"        FORMAT(sum( a.price* (a.count-a.back_count-a.free_count)*pay_rate/100 ),2) money "
	          +" from th_bill_item a, th_bill b , td_table c   "
	          +"where a.bill_id = b.bill_id  and a.rest_id = ? and b.rest_id = ? and c.rest_id = ? "
	          +"     and b.table_id = c.table_id "
	          +"     and a.oper_time >= ? and a.oper_time <= ? and b.open_date >= ? and b.open_date <= ?  "
	          +"     and a.category in ('酒水') "
	          +"    group by c.floor1 , a.category order by a.category ,c.floor1  ) )tmp "
	          +"group by tmp.groups "
	          +"order by tmp.groups ";
	            
	           ut.log(" sql :" +sql2);
	   		   List datas2 = jdbcTemplate.queryForList(sql2,new Object[]{user.getRest_id(),user.getRest_id(),user.getRest_id(),start_date+" 00:00:00",end_date+" 23:59:59",start_date,end_date,user.getRest_id(),user.getRest_id(),user.getRest_id(),start_date+" 00:00:00",end_date+" 23:59:59",start_date,end_date});
	   		   ut.log(" datas2 :" +datas2.size());
	            
	          //创建第二分页 begin
	            HSSFSheet sheet2 = wb.createSheet("第二页");   
	            int rowNum2 = 0; 
	            int colNum2 = 0;
	            HSSFRow row2 = sheet2.createRow(rowNum2++); 
	            HSSFCell cell2 = row2.createCell((short)colNum2++); 
	            cell2.setCellValue(new HSSFRichTextString(""));
	            cell2 = row2.createCell((short)colNum2++); 
	            cell2.setCellValue(new HSSFRichTextString("一楼"));
	            cell2 = row2.createCell((short)colNum2++); 
	            cell2.setCellValue(new HSSFRichTextString("二楼新区"));
	            cell2 = row2.createCell((short)colNum2++); 
	            cell2.setCellValue(new HSSFRichTextString("二楼老区"));
	            cell2 = row2.createCell((short)colNum2++); 
	            cell2.setCellValue(new HSSFRichTextString("三楼新区"));
	            cell2 = row2.createCell((short)colNum2++); 
	            cell2.setCellValue(new HSSFRichTextString("三楼老区"));
	            cell2 = row2.createCell((short)colNum2++); 
	            cell2.setCellValue(new HSSFRichTextString("外卖"));
	            
	            for(int i=0;i<datas2.size();i++){   
	            	Map item2 = (Map)datas2.get(i);
	                row2 = sheet2.createRow(rowNum2++);   
	                colNum2=0;   
	                cell2 = row2.createCell((short) colNum2++);   
	                cell2.setCellValue(new HSSFRichTextString(item2.get("GROUPS").toString().replaceAll(",", "")));   
	                cell2 = row2.createCell((short) colNum2++);   
	                cell2.setCellValue(new HSSFRichTextString(item2.get("F1").toString().replaceAll(",", "")));   
	                cell2 = row2.createCell((short) colNum2++);   
	                cell2.setCellValue(new HSSFRichTextString(item2.get("F2N").toString().replaceAll(",", "")));
	                cell2 = row2.createCell((short) colNum2++); 
	                cell2.setCellValue(new HSSFRichTextString(item2.get("F2O").toString().replaceAll(",", "")));   
	                cell2 = row2.createCell((short) colNum2++);   
	                cell2.setCellValue(new HSSFRichTextString(item2.get("F3N").toString().replaceAll(",", "")));   
	                cell2 = row2.createCell((short) colNum2++);   
	                cell2.setCellValue(new HSSFRichTextString(item2.get("F3O").toString().replaceAll(",", "")));
	                cell2 = row2.createCell((short) colNum2++);   
	                cell2.setCellValue(new HSSFRichTextString(item2.get("W").toString().replaceAll(",", "")));
	            }
	            
	           //创建第二分页 end
	            
		}
        String fileName = "档口楼层销售统计_"+start_date+"-"+end_date+".xls";
        File file = new File(path+fileName);
        try {
        	OutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
        	fileName = URLEncoder.encode(fileName, "UTF-8");  
    	    response.reset();  
    	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
//    	    response.addHeader("Content-Length", "" + data.length);  
    	    response.setContentType("application/vnd.ms-excel;charset=UTF-8");  
        	OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        	InputStream is = new FileInputStream(file);
        	byte[] buffer = new byte[1024*1000];
        	int read = -1;
        	while((read=is.read(buffer))!=-1){
        		outputStream.write(buffer); 
        	}       
            outputStream.flush();  
		    outputStream.close();
		    is.close();
        } catch (Exception e) {
         e.printStackTrace();
        }
		
    }
	//@yuanyl@2014/8/13@档口楼层菜品销售明细
	@RequestMapping(value = "/exportFloorGreensSaleDetailFile")   
    public void exportFloorGreensSaleDetailFile(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap)  {
    	System.out.println("*************************  exportFloorGreensSaleDetailFile  *************************");
    	String path = GetBean.getContext().getServletContext().getRealPath("/")+"files\\";
    	JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
		User user = this.getSessionUser(request);
		String start_date = request.getParameter("start_date");
    	String end_date = request.getParameter("end_date");
    	Map param = new HashMap();
    	param.put("start_date", start_date);
    	param.put("end_date", end_date);
    	ut.log(" param :" +param);
    	String sql = "select  * from "
                    +"((select c.floor1 floors,a.groups groups,a.FOOD_NAME fname, sum( a.price* (a.count-a.back_count-a.free_count)*pay_rate/100 ) money,sum(a.count-a.back_count-a.free_count) fcount , min(a.UNIT) unit "
                    +"from th_bill_item a, th_bill b , td_table c  "
                    +"where a.bill_id = b.bill_id  and a.rest_id = ? and b.rest_id = ? and c.rest_id = ?  "
                    +"  and b.table_id = c.table_id "
                    +"  and a.oper_time >= ? and a.oper_time <= ? and b.open_date >= ? and b.open_date <= ?  "
                    +"  and a.groups in ('海鲜','鲍参翅','川菜','粤菜','本地菜','凉菜','烧腊','面点') "
                    +"  group by c.floor1 , a.groups ,a.FOOD_NAME ) "
                    +"union all "
                    +"(select c.floor1 floors,a.category groups,a.FOOD_NAME fname,sum( a.price* (a.count-a.back_count-a.free_count)*pay_rate/100 ) money ,sum(a.count-a.back_count-a.free_count) fcount, min(a.UNIT) unit "
                    +" from th_bill_item a, th_bill b , td_table c  "
                    +" where a.bill_id = b.bill_id  and a.rest_id = ? and b.rest_id = ? and c.rest_id = ? "
                    +"   and b.table_id = c.table_id "
                    +"   and a.oper_time >= ? and a.oper_time <= ? and b.open_date >= ? and b.open_date <= ?  "
                    +"   and a.category in ('酒水') "
                    +"   group by c.floor1 , a.category ,a.FOOD_NAME )) tmp "
                    +"order by tmp.floors asc,tmp.groups asc, tmp.money desc,tmp.fcount desc,tmp.fname asc ";
		ut.log(" sql :" +sql);
		List datas =jdbcTemplate.queryForList(sql,new Object[]{user.getRest_id(),user.getRest_id(),user.getRest_id(),start_date+" 00:00:00",end_date+" 23:59:59",start_date,end_date,user.getRest_id(),user.getRest_id(),user.getRest_id(),start_date+" 00:00:00",end_date+" 23:59:59",start_date,end_date});
		
		Map floorsMap = new Hashtable();
		for(int i=0;i<datas.size();i++){
			Map data = (Map)datas.get(i);
			if(floorsMap.containsKey(data.get("FLOORS"))){
				List temp = (List)floorsMap.get(data.get("FLOORS"));
				temp.add(data);
			}else{
				List temp = new ArrayList(); 
				temp.add(data);
				floorsMap.put(data.get("FLOORS"), temp);
			}
		}
		ut.log(" datas :" +datas.size());
		HSSFWorkbook wb = new HSSFWorkbook();
		if(datas.size()==0){
			int rowNum = 0; 
            int colNum = 0;
            HSSFSheet sheet = wb.createSheet("查询结果");   
			HSSFRow row = sheet.createRow(rowNum++); 
            HSSFCell cell = row.createCell((short)colNum++); 
            cell.setCellValue(new HSSFRichTextString("未查询到相关的数据！"));
		}else{
			Set<String> keys = floorsMap.keySet();
		
	        for (Iterator it = keys.iterator(); it.hasNext();) {
	            String key = (String) it.next();
	            List data = (List)floorsMap.get(key);
	            HSSFSheet sheet = wb.createSheet(key);  
	            int rowNum = 0; 
	            int colNum = 0;
	            HSSFRow row = sheet.createRow(rowNum++); 
	            HSSFCell cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("楼层"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("分类"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("食物名称"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("数量"));
//	            cell = row.createCell((short)colNum++); 
//	            cell.setCellValue(new HSSFRichTextString("单位"));
	            cell = row.createCell((short)colNum++); 
	            cell.setCellValue(new HSSFRichTextString("销售金额(元)"));
	            for(int i=0;i<data.size();i++){   
	            	Map item = (Map)data.get(i);
	                row = sheet.createRow(rowNum++);   
	                colNum=0;   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("FLOORS").toString()));   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("GROUPS").toString()));   
	                cell = row.createCell((short) colNum++);   
	                cell.setCellValue(new HSSFRichTextString(item.get("FNAME").toString()));
	                cell = row.createCell((short)colNum++); 
		            cell.setCellValue(new HSSFRichTextString(item.get("FCOUNT").toString()));
//		            cell = row.createCell((short)colNum++); 
//		            cell.setCellValue(new HSSFRichTextString(item.get("UNIT").toString()));
	                cell = row.createCell((short)colNum++); 
		            cell.setCellValue(new HSSFRichTextString(item.get("MONEY").toString()));
		            
	            }
	            
	           //创建第一分页 end
	          
	        }
	            
		}
        String fileName = "档口楼层菜品销售明细_"+start_date+"-"+end_date+".xls";
        File file = new File(path+fileName);
        try {
        	OutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
        	fileName = URLEncoder.encode(fileName, "UTF-8");  
    	    response.reset();  
    	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
//    	    response.addHeader("Content-Length", "" + data.length);  
    	    response.setContentType("application/vnd.ms-excel;charset=UTF-8");  
        	OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        	InputStream is = new FileInputStream(file);
        	byte[] buffer = new byte[1024*1000];
        	int read = -1;
        	while((read=is.read(buffer))!=-1){
        		outputStream.write(buffer); 
        	}       
            outputStream.flush();  
		    outputStream.close();
		    is.close();
        } catch (Exception e) {
         e.printStackTrace();
        }
		
    }
}
