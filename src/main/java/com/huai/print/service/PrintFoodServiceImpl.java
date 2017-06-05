package com.huai.print.service;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import com.huai.operation.dao.OperationDao;
import com.huai.print.dao.PrintDao;
import com.huai.print.util.PrinterUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.huai.common.domain.IData;
import com.huai.common.util.ut;
import org.springframework.stereotype.Service;

@Service("printFoodService")
public class PrintFoodServiceImpl implements PrintFoodService {

	private static final Logger log = Logger.getLogger(PrintFoodServiceImpl.class);

	@Autowired
	public PrintDao printDao;

	@Autowired
	public OperationDao operationDao;

	public boolean printOneFood() {
		//1. 查询一条打印记录
		IData param = new IData();
		param.put("count", "1");
		List data = printDao.queryFoodPrintList(param);
		if(data.size()==0){
			return false;
		}
		IData food = new IData((Map)data.get(0));
		log.info(" printOneFood    food = "+food);
		String printId = food.getString("PRINT_ID");
		food.put("REMARK", "");
		//2. 打印菜品
		boolean result = printFood(food);
		//3. 更新打印结果
		if(result){
			printDao.printFoodFinish(printId,"1");
		}else{
			printDao.printFoodFinish(printId,"9");
		}
		return result;
	}

	private boolean printFood(IData food) {
		log.info(" 正在打印菜单 food = "+food);
		String printer = "";
		try{
			String print_id = food.getString("PRINT_ID");
			String barcode = food.getString("BARCODE");
			printer = food.getString("IP");
			String table_id = food.getString("TABLE_ID");
			String food_name = food.getString("FOOD_NAME");
			String nop = food.getString("NOP");
			String call_type = food.getString("CALL_TYPE");
			String call_type_show = getCallTypeShow(call_type);
			String count = food.getString("COUNT");
			String back_count = food.getString("BACK_COUNT");
			String price = food.getString("PRICE");
			String unit = food.getString("UNIT");
			String note = food.getString("NOTE");
			String oper_time = food.getString("OPER_TIME");
			String staff_name = food.getString("OPER_STAFF_NAME");
			String remark = food.getString("REMARK");
			if(printer.endsWith(".257")||printer.endsWith(".258")||printer.endsWith(".289")){
				return true;
			}
			int printCount = Integer.parseInt(food.getString("PRINT_COUNT"));
			// call_type --  0 ： 叫起    1 ： 即起   2：  起菜   3：  催菜   4:   退菜
			if(call_type.equals("2")){
				printCount = 1;
				printer = food.getString("PRINTER_START");
			}
			if(call_type.equals("3")){
				printCount = 1;
				printer = food.getString("PRINTER_HURRY");
			}
			if(call_type.equals("4")){
				printCount = 1;
				printer = food.getString("PRINTER_BACK");
			}
			log.info(" 正在打印菜单 printer = "+printer);
			Socket client = new java.net.Socket();
			client.connect(new InetSocketAddress( printer , 9100),10*1000);
			PrintWriter socketWriter = new PrintWriter(client.getOutputStream());
			//设置左边距
			PrinterUtil.setLeftSpace( socketWriter , 0 );
			//设置行间距
			PrinterUtil.setLineSpace( socketWriter, 10 );
			for(int i=0;i<printCount;i++){
				log.info(" printFood , Food_name =  "+food_name+" ,第 "+i+" 次打印  开始 ！！！！ printCount = "+printCount);
				//  打印备注信息   如 :  打印机故障!请转送:烧腊
				if(!remark.trim().equals("")){
					PrinterUtil.setFont( socketWriter , 17);    //设置字体
					socketWriter.println(remark.trim());
					PrinterUtil.setFont( socketWriter , 0);    //设置字体
					socketWriter.println(" ");
				}
				PrinterUtil.setFont( socketWriter , 1);    //设置字体
				if(call_type.equals("0")||call_type.equals("1")){
					if(i == 0){
						socketWriter.print("  正  式");
					}else{
						socketWriter.print(" @ 备份"+i);
					}
				}else{
					socketWriter.print("       ");
					//i = 9999; //  当起菜或者催菜时  只打印一张出来  所以把i设成一个比较大的数值，下次循环就不会执行， printCount(打印张数)也就失效了
				}
				PrinterUtil.setFont( socketWriter , 18);    //设置字体
				socketWriter.println("       "+call_type_show);
				PrinterUtil.setFont( socketWriter , 0);    //设置字体
				socketWriter.println(" ");

				PrinterUtil.setFont( socketWriter , 17);    //设置字体
				socketWriter.println(" "+table_id+" 台    [ "+nop+" 人 ]");

				PrinterUtil.setFont( socketWriter , 0);    //设置字体
				socketWriter.println(" ");
				socketWriter.print(""+oper_time);
				socketWriter.println("     点菜员:"+staff_name);
				socketWriter.println(" ");

				PrinterUtil.setFont( socketWriter , 17);    //设置字体
				socketWriter.println(food_name);

				PrinterUtil.setFont( socketWriter , 0);    //设置字体
				socketWriter.println(" ");

				PrinterUtil.setFont( socketWriter , 17);    //设置字体
				if(call_type.equals("4")){
					socketWriter.println("点菜数量 ： "+count+" "+unit);
					socketWriter.println("总退菜量 ： "+back_count+" "+unit);
					double left_count = Double.parseDouble(count)-Double.parseDouble(back_count);
					socketWriter.println("剩余数量 ： "+ut.getDoubleString(left_count)+" "+unit);
				}else{
					if(Double.parseDouble(back_count)>0){
						socketWriter.println("点菜数量 ： "+count+" "+unit);
						socketWriter.println("退菜数量 ： "+back_count+" "+unit);
						double left_count = Double.parseDouble(count)-Double.parseDouble(back_count);
						socketWriter.println("制作数量 ： "+ut.getDoubleString(left_count)+" "+unit);
					}else{
						socketWriter.println("数量 ：    "+count+" "+unit);
					}
				}
				PrinterUtil.setFont( socketWriter , 0);    //设置字体
				socketWriter.println(" ");
				PrinterUtil.setFont( socketWriter , 1);    //设置字体
				if(!call_type.equals("4")){
					socketWriter.println("单价 ："+price+" 元        合计 ："+ut.getDoubleString(ut.doubled(price)*ut.doubled(count))+" 元");
				}
				if(note!=null && !note.trim().equals("") && !note.trim().equals("-")){
					PrinterUtil.setFont( socketWriter , 0);    //设置字体
					socketWriter.println(" ");
					PrinterUtil.setFont( socketWriter , 17);    //设置字体
					socketWriter.println("备注："+note.trim());
				}
				PrinterUtil.setFont( socketWriter , 1);    //设置字体
				socketWriter.println( "-----------------------------------------" );
				log.info(" call_type = "+call_type);
				// 只有即起和叫起的菜单打印条码，   起菜、催菜和退菜的都不打印条码,只打编号
				log.info(" barcode = "+barcode);
				socketWriter.println("No. "+ barcode );
				// call_type --  0 ： 叫起    1 ： 即起   2：  起菜   3：  催菜   4:   退菜
				if(call_type.equals("0")||call_type.equals("1")){

					//设置条码编码方式
					socketWriter.write(0x1d);
					socketWriter.write('k');
					socketWriter.write(70);  //设置条码编码方式为69  CODE39
					socketWriter.write(12);   //设置条码位数为6位
					char[] chars = barcode.toCharArray();
					for(int n = 0;n<chars.length;n++){
//						log.info(chars[n]+" : "+(int)chars[n]);
						socketWriter.write((int)chars[n]);
					}
					log.info(" end chars ....  ");
					socketWriter.println( barcode );
				}else{
					socketWriter.println(" ");
				}
				// 打印完毕自动走纸
				PrinterUtil.pushPaper(socketWriter);
				// 打印完毕自动切纸
				PrinterUtil.cutPaper(socketWriter);
				// 强制打印缓冲区
				socketWriter.flush();
				log.info(" printFood , Food_name =  "+food_name+" ,第 "+i+" 次打印 结束 ！！！！ printCount = "+printCount);
			}
			client.close();
			log.info(" -----  打印任务  end    ----  ");
		}catch (SocketTimeoutException e) {
			log.error("链接打印机失败 ip : " + printer+",printId = "+food.getString("PRINT_ID"),e);
			return false;
		}catch (Exception e) {
			log.error("打印菜品失败 ITEM_ID = "+food.getString("ITEM_ID")+",printId = "+food.getString("PRINT_ID"),e);
			return false;
		}
		return true;
	}

	// call_type --  0 ： 叫起    1 ： 即起   2：  起菜   3：  催菜   4:   退菜
	private String getCallTypeShow(String type){
		String str="";
		if(type.equals("0")){
			str="叫起";
		}else if(type.equals("1")){
			str="即起";
		}else if(type.equals("2")){
			str="起菜";
		}else if(type.equals("3")){
			str="催菜";
		}else if(type.equals("4")){
			str="退菜";
		}
		return str;
	}

}
