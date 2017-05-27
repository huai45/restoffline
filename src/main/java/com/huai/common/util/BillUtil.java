package com.huai.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.huai.common.domain.IData;

public class BillUtil {

	public static IData calculateBill(IData bill){
		List packages = (List)bill.get("PACKAGELIST");
		List items = (List)bill.get("ITEMLIST");
		List fees = (List)bill.get("FEELIST");
		List blank_list = new ArrayList();
//		if(items==null||fees==null||packages==null){
//			return bill;
//		}
		if(items==null){
			items = blank_list;
		}
		if(fees==null){
			fees = blank_list;
		}
		if(packages==null){
			packages = blank_list;
		}
		double bill_fee = 0;
		double derate_fee = 0;
		int recv_fee = 0;
		int reduce_fee =  Integer.parseInt(bill.getString("REDUCE_FEE"));
//		for(int i=0;i<packages.size();i++){
//			Map pack = (Map)packages.get(i);
//			double price = ut.doubled(pack.get("PACKAGE_PRICE").toString());
//			bill_fee = bill_fee + price;
//		}
		
		for(int i=0;i<items.size();i++){
			Map item = (Map)items.get(i);
			if(item.get("PACKAGE_ID")!=null&&!item.get("PACKAGE_ID").toString().equals("")){
				continue;
			}
			int price = Integer.parseInt(item.get("price").toString());
			double count = ut.doubled(item.get("COUNT").toString());
			double back_count = ut.doubled(item.get("BACK_COUNT").toString());
			double free_count = ut.doubled(item.get("FREE_COUNT").toString());
			int payrate = Integer.parseInt(item.get("PAY_RATE").toString());
			double fee = price*(count-back_count-free_count);
			bill_fee = bill_fee + fee;
			derate_fee = derate_fee + fee*(100-payrate)/100;
		}
        for(int i=0;i<fees.size();i++){
        	Map fee = (Map)fees.get(i);
        	int payfee = Integer.parseInt(fee.get("FEE").toString());
        	recv_fee = recv_fee + payfee;
		}
        int spay_fee = (int)(bill_fee- (recv_fee+reduce_fee+derate_fee));
        bill.put("BILL_FEE", ut.getDoubleString(bill_fee));
        bill.put("DERATE_FEE", ut.getDoubleString(derate_fee));
        bill.put("SPAY_FEE", ""+spay_fee);
        bill.put("RECV_FEE", ""+recv_fee);
        if( spay_fee == 0 ){
        	bill.put("CLOSE_FLAG", "1");
        	bill.put("REMARK", "账单无欠费，可以封单");
        }else{
        	bill.put("CLOSE_FLAG", "0");
        	bill.put("REMARK", "账单有欠费,不能封单");
        }
//        ut.p(bill);
		return bill;
	}
	
	
	
}
