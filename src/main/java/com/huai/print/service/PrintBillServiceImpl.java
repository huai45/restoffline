package com.huai.print.service;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huai.common.dao.CommonDao;
import com.huai.common.util.BillUtil;
import com.huai.operation.dao.OperationDao;
import com.huai.print.dao.PrintDao;
import com.huai.print.util.PrinterUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.huai.common.domain.IData;
import com.huai.common.util.GetBean;
import com.huai.common.util.ut;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("printBillService")
public class PrintBillServiceImpl implements PrintBillService {

	private static final Logger log = Logger.getLogger(PrintBillServiceImpl.class);

	@Autowired
	public PrintDao printDao;

	@Autowired
	public OperationDao operationDao;

	@Resource(name="commonDao")
	public CommonDao commonDao;

	public boolean printOneBill() {
		//1. 查询一条打印记录
		IData param = new IData();
		List bills = printDao.queryBillPrintList(param);
        if(bills.size()==0){
        	return false;
		}
		IData bill = new IData((Map)bills.get(0));
        String printId = bill.getString("PRINT_ID");
		String printer = bill.getString("PRINTER");
		bill = operationDao.queryBillByBillId(bill.getString("BILL_ID"));
		if(bill==null){
			printDao.printBillFinish(printId,"9");
			return false;
		}
		operationDao.queryBillInfo(bill);
		bill.put("PRINT_ID",printId);
		bill.put("PRINTER",printer);

		IData restinfo = commonDao.queryRestInfoById(param);
		bill.put("RESTNAME", restinfo.getString("RESTNAME"));
		bill.put("ADDRESS", restinfo.getString("ADDRESS"));
		bill.put("TELEPHONE", restinfo.getString("TELEPHONE"));

		//2. 打印账单
		boolean result = printBill(bill);
		//3. 更新打印结果
		if(result){
			log.info(" 打印账单成功 bill = "+bill);
			printDao.printBillFinish(printId,"1");
		}else{
			printDao.printBillFinish(printId,"9");
		}
		return result;
	}


	private boolean printBill(IData bill) {
		log.info(" 正在打印账单 bill = "+bill);
		boolean result = false;
		if(bill.getString("PAY_TYPE").equals("1")){
			result = printFinishBill(bill);
		}else{
			result = printQueryBill(bill);
		}
		return result;
	}

	/**
	 *  打印餐台查询单
	 * @param
	 * @return
	 *     add time ：2011-10-07
	 *  modify time ：2011-10-07
	 */
	public boolean printQueryBill(IData bill) {
		log.info(" *********  printQueryBill   开始  *********   bill_Id = "+bill.getString("BILL_ID"));
		String printer = bill.getString("PRINTER");
		log.info(" printQueryBill  printer : "+printer);
		bill.put("STATE", "0");
		Socket client = new java.net.Socket();
		try {
			client.connect(new InetSocketAddress( printer , 9100),10*1000);
			PrintWriter socketWriter = new PrintWriter(client.getOutputStream());
			// 计算账单金额
			BillUtil.calculateBill(bill);
			List items = (List)bill.get("ITEMLIST");
			List recvDetail = (List)bill.get("FEELIST");
			List tuicai = new ArrayList();
			List miancai = new ArrayList();
			List zengsong = new ArrayList();
			//设置左边距
			PrinterUtil.setLeftSpace( socketWriter , 0 );
			//设置行间距
			PrinterUtil.setLineSpace( socketWriter, 10 );
			PrinterUtil.setFont( socketWriter , 17);   //设置字体
			socketWriter.println(" *** 查询单 ***");
			PrinterUtil.setFont( socketWriter , 0);   //设置字体
			socketWriter.println(" ");
			PrinterUtil.setFont( socketWriter , 17);   //设置字体
			String nop = bill.getString("NOP");
			socketWriter.println(" "+bill.getString("TABLE_ID")+" 台    [ "+(int)Double.parseDouble(nop)+" 人 ]");
			//设置行间距
			PrinterUtil.setLineSpace( socketWriter, 64 );
			PrinterUtil.setFont( socketWriter , 0);   //设置字体
			socketWriter.println(" ");
			socketWriter.println("  开台时间  ： "+bill.getString("OPEN_TIME"));
			socketWriter.println("  开台员工  ： "+bill.getString("OPEN_STAFF_NAME"));
			socketWriter.println("  账单流水  ： "+bill.getString("BILL_ID"));
			socketWriter.println("  打印时间  ： "+ut.currentTime());
			socketWriter.println(" ----------------------------------------—--");
			socketWriter.println("    品名              数量 单位 金额  点菜员");
//			log.info(" FOOD_ITEM_STR = "+bill.getString("FOOD_ITEM_STR").toString());
			// 一个汉字2个空格   一个汉字 = 2个数字或2个空格   每一行  1个空格  18个空格长度的名字
			for(int i=0;i<items.size();i++){
				Map item = (Map)items.get(i);
//				log.info(" item = "+item);
				try{
					String name = item.get("FOOD_NAME").toString();
					// state     0 ： 未打单   1 ： 已打单但未上菜   2 ： 已上菜
					String state = item.get("STATE").toString();
					if(state.equals("0")){   // 未起菜
						item.put("FOOD_NAME", "#"+name);
					}
					if(state.equals("2")){  // 已上菜
						item.put("FOOD_NAME", "*"+name);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				if(Double.parseDouble(item.get("BACK_COUNT").toString())>0){
					tuicai.add(item);
				}
				if(Double.parseDouble(item.get("FREE_COUNT").toString())>0){
					zengsong.add(item);
				}
				int width = 0;
				String str= "";
				if(Double.parseDouble(item.get("PAY_RATE").toString())<100){
					width = 3;
					str= "("+item.get("PAY_RATE")+"%) ";
				}
				//            log.info(" ************      str="+str);
				item.put("FOOD_NAME",str+item.get("FOOD_NAME"));
				int food_name_length = item.get("FOOD_NAME").toString().length();
				socketWriter.print(" ");
				if(food_name_length>(9+width)){
					socketWriter.println(item.get("FOOD_NAME").toString().trim());
					socketWriter.print("                   ");
					socketWriter.print("   ");
					String count = item.get("COUNT").toString();
					socketWriter.print(count);
					for(int j=count.length();j<4;j++){
						socketWriter.print(" ");
					}
					if(item.get("UNIT").toString().length()==1){
						socketWriter.print(" "+item.get("UNIT")+" ");
					}else{
						socketWriter.print(item.get("UNIT"));
					}
					double total = ut.doubled(item.get("COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
					socketWriter.print("  "+ut.getDoubleString(total/100));
					socketWriter.println("  "+item.get("OPER_STAFF_NAME"));
				}else{
					socketWriter.print(item.get("FOOD_NAME"));
					for(int j=0;j<(9+width-item.get("FOOD_NAME").toString().length());j++){
						socketWriter.print("  ");
					}
					socketWriter.print("   ");
					String count = item.get("COUNT").toString();
					socketWriter.print(count.toString());
					for(int j=count.length();j<4;j++){
						socketWriter.print(" ");
					}
					if(item.get("UNIT").toString().length()==1){
						socketWriter.print(" "+item.get("UNIT").toString()+" ");
					}else{
						socketWriter.print(item.get("UNIT").toString());
					}
					double total = ut.doubled(item.get("COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
					socketWriter.print("  "+ut.getDoubleString(total/100));
					socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
				}
				if(!ut.isEmpty((String)item.get("NOTE"))){
					socketWriter.println("   附加项:"+item.get("NOTE").toString().trim());
				}
			}
			socketWriter.println(" ---------------------------------------—---");
			log.info(" tuicai = "+tuicai.size());
			if(tuicai.size()>0){
				socketWriter.println("   已退菜品          数量 单位 金额  点菜员");
				for(int i=0;i<tuicai.size();i++){
					Map item = (Map)tuicai.get(i);
					int width = 0;
					if(Double.parseDouble(item.get("PAY_RATE").toString())<100){
						width = 3;
					}
					int food_name_length = item.get("FOOD_NAME").toString().length();
					socketWriter.print(" ");
					if(food_name_length>(9+width)){
						socketWriter.println(item.get("FOOD_NAME").toString().trim());
						socketWriter.print("                   ");
						socketWriter.print("   ");
						String back_count = item.get("BACK_COUNT").toString();
						socketWriter.print(back_count);
						for(int j=back_count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("BACK_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}else{
						socketWriter.print(item.get("FOOD_NAME").toString());
						for(int j=0;j<(9+width-item.get("FOOD_NAME").toString().length());j++){
							socketWriter.print("  ");
						}
						socketWriter.print("   ");
						String count = item.get("BACK_COUNT").toString();
						socketWriter.print(count);
						for(int j=count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("BACK_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}
					if(!ut.isEmpty((String)item.get("NOTE"))){
						socketWriter.println("   附加项:"+item.get("NOTE").toString().trim());
					}
				}
				socketWriter.println(" ---------------------------------------—---");
			}
			log.info(" zengsong = "+zengsong.size());
			if(zengsong.size()>0){
				socketWriter.println("   赠送菜品          数量 单位 金额  点菜员");
				for(int i=0;i<zengsong.size();i++){
					Map item = (Map)zengsong.get(i);
					int width = 0;
					if(Double.parseDouble(item.get("PAY_RATE").toString())<100){
						width = 3;
					}
					int food_name_length = item.get("FOOD_NAME").toString().length();
					socketWriter.print(" ");
					if(food_name_length>(9+width)){
						socketWriter.println(item.get("FOOD_NAME").toString().trim());
						socketWriter.print("                   ");
						socketWriter.print("   ");
						String back_count = item.get("FREE_COUNT").toString();
						socketWriter.print(back_count);
						for(int j=back_count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("FREE_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}else{
						socketWriter.print(item.get("FOOD_NAME").toString());
						for(int j=0;j<(9+width-item.get("FOOD_NAME").toString().length());j++){
							socketWriter.print("  ");
						}
						socketWriter.print("   ");
						String count = item.get("FREE_COUNT").toString();
						socketWriter.print(count);
						for(int j=count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("FREE_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}
					if(!ut.isEmpty((String)item.get("NOTE"))){
						socketWriter.println("   附加项:"+item.get("NOTE").toString().trim());
					}
				}
				socketWriter.println(" ---------------------------------------—---");
			}
//			if(ut.doubled(bill.getString("DISCOUNT_FEE").toString())>0){
//				socketWriter.println("  打折金额 :  "+ut.getDoubleString(ut.doubled(bill.getString("DISCOUNT_FEE").toString()))+" 元");
//				socketWriter.println(" ----------------------------------------—--");
//			}
			socketWriter.println("  账单总金额   :    "+bill.getString("BILL_FEE").toString()+" 元");
			socketWriter.println(" ----------------------------------------—--");
			if(ut.doubled(bill.getString("REDUCE_FEE").toString())>0){
				socketWriter.println("  抹去金额 :  -"+ut.getDoubleString(ut.doubled(bill.getString("REDUCE_FEE").toString()))+" 元");
				socketWriter.println(" ----------------------------------------—--");
			}
			if(recvDetail.size()>0){
				for(int i=0;i<recvDetail.size();i++){
					Map map = (Map)recvDetail.get(i);
					socketWriter.println("   "+map.get("MODE_NAME")+"  :  "+map.get("FEE").toString()+" 元");
				}
				socketWriter.println(" ----------------------------------------—--");
			}
			socketWriter.println(" 已 收 ： "+bill.getString("RECV_FEE").toString()+" 元");
			socketWriter.println(" ----------------------------------------—--");
			PrinterUtil.setFont(socketWriter,17);    // 设置字体
			socketWriter.println(" 应 收 ： "+bill.getString("SPAY_FEE").toString()+" 元");

			// 打印饭店信息
			PrinterUtil.setFont( socketWriter , 0);
			socketWriter.println(" ----------------------------------------—--");
			PrinterUtil.setFont( socketWriter , 1);
			socketWriter.println("          "+bill.get("RESTNAME") +"欢迎您的光临");
			PrinterUtil.setFont( socketWriter , 0);
			socketWriter.println(" ");
			socketWriter.println("  地址 :  "+bill.get("ADDRESS"));
			socketWriter.println("  电话 :  "+bill.get("TELEPHONE"));
			socketWriter.println(" ----------------------------------------—--");
			// 打印完毕自动走纸
			PrinterUtil.pushPaper(socketWriter);
			// 打印完毕自动切纸
			PrinterUtil.cutPaper(socketWriter);
			socketWriter.flush();
			client.close();
			log.info("  **************   打印查询单完毕  ************** bill_id = "+bill.getString("BILL_ID"));
			bill.put("STATE", "2");
		} catch (Exception e) {
			log.error("打印账单失败 billId = "+bill.getString("BILL_ID")+",printId = "+bill.getString("PRINT_ID"),e);
			return false;
		}
		return true;
	}


	/**
	 *  打印餐台结账单
	 * @param
	 * @return
	 *     add time ：2011-10-07
	 *  modify time ：2011-10-07
	 */
	public boolean printFinishBill(IData bill) {
		log.info(" *********  printQueryBill   开始  *********    ");
		String printer = bill.getString("PRINTER");
		log.info(" printFinishBill  printer : "+printer);
		bill.put("STATE", "0");
		Socket client = new java.net.Socket();
		try {
			client.connect(new InetSocketAddress( printer , 9100),10*1000);
			PrintWriter socketWriter = new PrintWriter(client.getOutputStream());
			List items = (List)bill.get("ITEMLIST");
			List recvDetail = (List)bill.get("FEELIST");
			List tuicai = new ArrayList();
			List miancai = new ArrayList();
			List zengsong = new ArrayList();
			//设置左边距
			PrinterUtil.setLeftSpace( socketWriter , 0 );
			//设置行间距
			PrinterUtil.setLineSpace( socketWriter, 10 );
			PrinterUtil.setFont( socketWriter , 17);   //设置字体
			socketWriter.println(" *** 结账单 ***");
			PrinterUtil.setFont( socketWriter , 0);   //设置字体
			socketWriter.println(" ");
			PrinterUtil.setFont( socketWriter , 17);   //设置字体
			String nop = bill.getString("NOP");
			socketWriter.println(" "+bill.getString("TABLE_ID")+" 台    [ "+(int)Double.parseDouble(nop)+" 人 ]");
			//设置行间距
			PrinterUtil.setLineSpace( socketWriter, 64 );
			PrinterUtil.setFont( socketWriter , 0);   //设置字体
			socketWriter.println(" ");
			socketWriter.println("  开台时间  ： "+bill.getString("OPEN_TIME"));
			socketWriter.println("  开台员工  ： "+bill.getString("OPEN_STAFF_NAME"));
			socketWriter.println("  账单流水  ： "+bill.getString("BILL_ID"));
			socketWriter.println("  打印时间  ： "+ut.currentTime());
			socketWriter.println(" ----------------------------------------—--");
			socketWriter.println("    品名              数量 单位 金额  点菜员");
			// 一个汉字2个空格   一个汉字 = 2个数字或2个空格   每一行  1个空格  18个空格长度的名字
			for(int i=0;i<items.size();i++){
				Map item = (Map)items.get(i);
				try{
					String name = item.get("FOOD_NAME").toString();
					String state = item.get("STATE").toString();
					if(state.equals("0")){   // 未起菜
						item.put("FOOD_NAME", "#"+name);
					}
					if(state.equals("2")){  // 已上菜
						item.put("FOOD_NAME", "*"+name);
					}
				}catch(Exception e){
					e.printStackTrace();
				}

				if(Double.parseDouble(item.get("BACK_COUNT").toString())>0){
					tuicai.add(item);
				}
				if(Double.parseDouble(item.get("FREE_COUNT").toString())>0){
					zengsong.add(item);
				}
				int width = 0;
				String str= "";
				if(Double.parseDouble(item.get("PAY_RATE").toString())<100){
					width = 3;
					str= "("+item.get("PAY_RATE")+"%) ";
				}
				item.put("FOOD_NAME",str+item.get("FOOD_NAME"));
				int food_name_length = item.get("FOOD_NAME").toString().length();
				socketWriter.print(" ");
				if(food_name_length>(9+width)){
					socketWriter.println(item.get("FOOD_NAME").toString().trim());
					socketWriter.print("                   ");
					socketWriter.print("   ");
					String count = item.get("COUNT").toString();
					socketWriter.print(count);
					for(int j=count.length();j<4;j++){
						socketWriter.print(" ");
					}
					if(item.get("UNIT").toString().length()==1){
						socketWriter.print(" "+item.get("UNIT")+" ");
					}else{
						socketWriter.print(item.get("UNIT"));
					}
					double total = ut.doubled(item.get("COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
					socketWriter.print("  "+ut.getDoubleString(total/100));
					socketWriter.println("  "+item.get("OPER_STAFF_NAME"));
				}else{
					socketWriter.print(item.get("FOOD_NAME"));
					for(int j=0;j<(9+width-item.get("FOOD_NAME").toString().length());j++){
						socketWriter.print("  ");
					}
					socketWriter.print("   ");
					String count = item.get("COUNT").toString();
					socketWriter.print(count.toString());
					for(int j=count.length();j<4;j++){
						socketWriter.print(" ");
					}
					if(item.get("UNIT").toString().length()==1){
						socketWriter.print(" "+item.get("UNIT").toString()+" ");
					}else{
						socketWriter.print(item.get("UNIT").toString());
					}
					double total = ut.doubled(item.get("COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
					socketWriter.print("  "+ut.getDoubleString(total/100));
					socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
				}
				if(!ut.isEmpty((String)item.get("NOTE"))){
					socketWriter.println("   附加项:"+item.get("NOTE").toString().trim());
				}
			}
			socketWriter.println(" ---------------------------------------—---");
			if(tuicai.size()>0){
				socketWriter.println("   已退菜品          数量 单位 金额  点菜员");
				for(int i=0;i<tuicai.size();i++){
					Map item = (Map)tuicai.get(i);
					int width = 0;
					if(Double.parseDouble(item.get("PAY_RATE").toString())<100){
						width = 3;
					}
					int food_name_length = item.get("FOOD_NAME").toString().length();
					socketWriter.print(" ");
					if(food_name_length>(9+width)){
						socketWriter.println(item.get("FOOD_NAME").toString().trim());
						socketWriter.print("                   ");
						socketWriter.print("   ");
						String back_count = item.get("BACK_COUNT").toString();
						socketWriter.print(back_count);
						for(int j=back_count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("BACK_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}else{
						socketWriter.print(item.get("FOOD_NAME").toString());
						for(int j=0;j<(9+width-item.get("FOOD_NAME").toString().length());j++){
							socketWriter.print("  ");
						}
						socketWriter.print("   ");
						String count = item.get("BACK_COUNT").toString();
						socketWriter.print(count);
						for(int j=count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("BACK_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}
					if(!ut.isEmpty((String)item.get("NOTE"))){
						socketWriter.println("   附加项:"+item.get("NOTE").toString().trim());
					}
				}
				socketWriter.println(" ---------------------------------------—---");
			}
			if(zengsong.size()>0){
				socketWriter.println("   赠送菜品          数量 单位 金额  点菜员");
				for(int i=0;i<zengsong.size();i++){
					Map item = (Map)zengsong.get(i);
					int width = 0;
					if(Double.parseDouble(item.get("PAY_RATE").toString())<100){
						width = 3;
					}
					int food_name_length = item.get("FOOD_NAME").toString().length();
					socketWriter.print(" ");
					if(food_name_length>(9+width)){
						socketWriter.println(item.get("FOOD_NAME").toString().trim());
						socketWriter.print("                   ");
						socketWriter.print("   ");
						String back_count = item.get("FREE_COUNT").toString();
						socketWriter.print(back_count);
						for(int j=back_count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("FREE_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}else{
						socketWriter.print(item.get("FOOD_NAME").toString());
						for(int j=0;j<(9+width-item.get("FOOD_NAME").toString().length());j++){
							socketWriter.print("  ");
						}
						socketWriter.print("   ");
						String count = item.get("BACK_COUNT").toString();
						socketWriter.print(count);
						for(int j=count.length();j<4;j++){
							socketWriter.print(" ");
						}
						if(item.get("UNIT").toString().length()==1){
							socketWriter.print(" "+item.get("UNIT").toString()+" ");
						}else{
							socketWriter.print(item.get("UNIT").toString());
						}
						double total = ut.doubled(item.get("FREE_COUNT").toString())*ut.doubled(item.get("PRICE").toString())*ut.doubled(item.get("PAY_RATE").toString());
						socketWriter.print("  "+ut.getDoubleString(total/100));
						socketWriter.println("  "+item.get("OPER_STAFF_NAME").toString().trim());
					}
					if(!ut.isEmpty((String)item.get("NOTE"))){
						socketWriter.println("   附加项:"+item.get("NOTE").toString().trim());
					}
				}
				socketWriter.println(" ---------------------------------------—---");
			}
//			if(ut.doubled(PrintBill.getMember("DISCOUNT_FEE").toString())>0){
//				socketWriter.println("  打折金额 :  "+ut.getDoubleString(ut.doubled(PrintBill.getMember("DISCOUNT_FEE").toString()))+" 元");
//				socketWriter.println(" ----------------------------------------—--");
//			}
			socketWriter.println("  账单总金额   :    "+bill.getString("BILL_FEE")+" 元");
			socketWriter.println(" ----------------------------------------—--");
			if(ut.doubled(bill.getString("REDUCE_FEE"))>0){
				socketWriter.println("  抹去金额 :  -"+ut.getDoubleString(ut.doubled(bill.getString("REDUCE_FEE")))+" 元");
				socketWriter.println(" ----------------------------------------—--");
			}
			if(recvDetail.size()>0){
				for(int i=0;i<recvDetail.size();i++){
					Map map = (Map)recvDetail.get(i);
					socketWriter.println("   "+map.get("MODE_NAME")+"  :  "+map.get("FEE").toString()+" 元");
				}
				socketWriter.println(" ----------------------------------------—--");
			}
			PrinterUtil.setFont(socketWriter,17);    // 设置字体
			socketWriter.println(" 已 收 ： "+bill.getString("RECV_FEE")+" 元");

			// 打印饭店信息
			PrinterUtil.setFont( socketWriter , 0);
			socketWriter.println(" ----------------------------------------—--");
			PrinterUtil.setFont( socketWriter , 1);
			socketWriter.println("          "+bill.get("RESTNAME") +"欢迎您的光临");
			PrinterUtil.setFont( socketWriter , 0);
			socketWriter.println(" ");
			socketWriter.println("  地址 :  "+bill.get("ADDRESS"));
			socketWriter.println("  电话 :  "+bill.get("TELEPHONE"));
			socketWriter.println(" ----------------------------------------—--");
			// 打印完毕自动走纸
			PrinterUtil.pushPaper(socketWriter);
			// 打印完毕自动切纸
			PrinterUtil.cutPaper(socketWriter);
			socketWriter.flush();
			client.close();
			log.info("  **************   打印完毕  ************** bill = "+bill.getString("BILL_ID"));
			bill.put("STATE", "2");
		} catch (Exception e) {
			log.error("打印账单失败 billId = "+bill.getString("BILL_ID")+",printId = "+bill.getString("PRINT_ID"),e);
			return false;
		}
		return true;
	}


}
