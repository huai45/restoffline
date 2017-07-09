package com.huai.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.huai.common.util.GetBean;
import com.huai.common.util.ut;

public class ImportFoodUtil {

	/**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    public static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:{
            	DecimalFormat format = new DecimalFormat("#.00");
            	cellvalue = format.format(cell.getNumericCellValue());
            }
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    //cellvalue = String.valueOf(cell.getNumericCellValue());
                	//DecimalFormat format = new DecimalFormat("#.00");
                	DecimalFormat format = new DecimalFormat("#");
                	cellvalue = format.format(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
    
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		String table_name = "td_food_pre";
		String sql = " insert into "+table_name+" (FOOD_ID,FOOD_NAME,ABBR,PRICE,UNIT,CATEGORY,GROUPS,PRINTER,PRINT_COUNT,COOK_TAG," +
				" COOK_TIME,COUNT_TAG,SHOW_TAG,WARNING_TAG,USE_TAG,IMAGE,PRINTER_START,PRINTER_HURRY,PRINTER_BACK,PRINTER_SEC,REMARK) " +
				" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";
		
		// TODO Auto-generated method stub
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		JdbcTemplate jdbcTemplate = (JdbcTemplate)context.getBean("jdbcTemplate");
		ut.log(" jdbcTemplate = "+jdbcTemplate);
		List foods = jdbcTemplate.queryForList("select * from  "+table_name+" ");
		ut.log(" foods = "+foods.size());
		jdbcTemplate.update("delete from  "+table_name+" ");
		
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("d:/food.xls"));
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		int rows = sheet.getLastRowNum();
		ut.log(" rows = "+rows);
		for(int i=0;i<rows;i++){
			HSSFRow row = sheet.getRow(i);
			// 标题总列数
	        int colNum = row.getPhysicalNumberOfCells();
	        ut.log("colNum:" + colNum);
	        String food_id = getCellFormatValue(row.getCell((short)0)).trim();
	        try{
	        	int id = Integer.parseInt(food_id);
	        }catch(Exception e){
	        	ut.log(i+" , ID:"+getCellFormatValue(row.getCell((short) 0))+",food_name:"+getCellFormatValue(row.getCell((short) 1)));
	        	continue;
	        }
	        String food_name = getCellFormatValue(row.getCell((short)1)).trim();
	        if(ut.isEmpty(food_name)){
	        	ut.log(i+" , ID:"+getCellFormatValue(row.getCell((short) 0))+",food_name:"+getCellFormatValue(row.getCell((short) 1)));
	        	continue;
	        }
	        String abbr = getCellFormatValue(row.getCell((short)2)).trim().toUpperCase();
	        String price = getCellFormatValue(row.getCell((short)3)).trim();
	        String unit = getCellFormatValue(row.getCell((short)4)).trim();
	        String category = getCellFormatValue(row.getCell((short)5)).trim();
	        String groups = getCellFormatValue(row.getCell((short)6)).trim();
	        String printer = getCellFormatValue(row.getCell((short)7)).trim();
	        String printer_sec = getCellFormatValue(row.getCell((short)8)).trim();
	        String printer_start = getCellFormatValue(row.getCell((short)9)).trim();
	        String printer_hurry = getCellFormatValue(row.getCell((short)10)).trim();
	        String printer_back = getCellFormatValue(row.getCell((short)11)).trim();
	        String print_count = getCellFormatValue(row.getCell((short)12)).trim();
	        
	        String cook_tag = "1";
	        String cook_time = "15";
	        String count_tag = "1";
	        String use_tag = "1";
	        String show_tag = "N";
	        String warning_tag = "1";
	        String image = "";
	        String remark = "";
	        try{
	        	jdbcTemplate.update(sql,new Object[]{food_id,food_name,abbr,price,unit,category,groups,printer,print_count,
	    	        	cook_tag,cook_time,count_tag,show_tag,warning_tag,use_tag,image,printer_start,printer_hurry,printer_back,printer_sec,remark});
	        }catch(Exception e){
	        	e.printStackTrace();
	        	continue;
	        }
	        
		}
		
		
		
		
	}

}
