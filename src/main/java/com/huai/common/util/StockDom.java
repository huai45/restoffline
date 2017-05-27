package com.huai.common.util;

import com.huai.common.domain.Stock;

public class StockDom {

	public static String SelfDom(Stock stock){
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='"+stock.getStock_id()+"' ");
		sb.append("     stock_name='"+stock.getStock_name()+"' ");
		sb.append("     abbr='"+stock.getAbbr()+"' ");
		sb.append("     type='"+stock.getType()+"' ");
		if(stock.getType().equals("1")){
			sb.append("     type_name='直拨' ");
		}else if(stock.getType().equals("2")){
			sb.append("     type_name='入库' ");
		}else{
			sb.append("     type_name='-' ");
		}
		sb.append("     unit='"+stock.getUnit()+"' ");
		sb.append("     price='"+stock.getPrice()+"' ");
		sb.append("     count='"+stock.getCount()+"' ");
		sb.append("     storage_id='"+stock.getStorage_id()+"' ");
		sb.append("     category_id='"+stock.getCategory_id()+"' ");
		sb.append("     alarm_count='"+stock.getAlarm_count()+"' ");
		sb.append("     class='stock' >");
		sb.append("  <span>"+stock.getStock_name()+"</span>");
		sb.append("</div>");
		return sb.toString();
	};
	
	public static String SelfDom2(Stock stock){
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='"+stock.getStock_id()+"' ");
		sb.append("     stock_name='"+stock.getStock_name()+"' ");
		sb.append("     abbr='"+stock.getAbbr()+"' ");
		sb.append("     type='"+stock.getType()+"' ");
		sb.append("     unit='"+stock.getUnit()+"' ");
		sb.append("     price='"+stock.getPrice()+"' ");
		sb.append("     count='"+stock.getCount()+"' ");
		sb.append("     storage_id='"+stock.getStorage_id()+"' ");
		sb.append("     category_id='"+stock.getCategory_id()+"' ");
		sb.append("     alarm_count='"+stock.getAlarm_count()+"' ");
		sb.append("     class='stock_tr' >");
		sb.append("  <div style='float:left;padding-top:1px;width:40px;text-align:center;' ></div>");
		sb.append("  <div style='float:left;' ></div>");
		sb.append("  <div class='bluefont1 bluefont stock_name' style='width:300px;' >"+stock.getStock_name()+"</div>");
//		sb.append("  <div style='width:100px;' >"+stock.getCategory_id()+"</div>");
		sb.append("  <div style='width:100px;' >"+stock.getPrice()+"</div>");
		sb.append("  <div style='width:100px;' >"+stock.getCount()+"</div>");
		sb.append("  <div style='width:70px;' >"+stock.getUnit()+"</div>");
		sb.append("  <div style='width:100px;' >-</div>");
		sb.append("</div>");
		return sb.toString();
	};
	
}
