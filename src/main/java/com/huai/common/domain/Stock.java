package com.huai.common.domain;

import java.util.HashMap;

public class Stock extends HashMap {

	private String stock_id;
	
	private String stock_name;
	
	private String abbr;
	
	private String type;
	
    private String unit;
	
	private String price = "0";
	
	private String storage_id;
	
	private String category_id;
	
	private String count = "0";
	
	private String alarm_count = "0";
	
	private String image = "";
	
	private String remark = "";
	
	public String getStock_id() {
		return stock_id;
	}

	public void setStock_id(String stock_id) {
		this.stock_id = stock_id;
		this.put("stock_id", stock_id);
	}

	public String getStock_name() {
		return stock_name;
	}

	public void setStock_name(String stock_name) {
		this.stock_name = stock_name;
		this.put("stock_name", stock_name);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		this.put("type", type);
	}

	public String getStorage_id() {
		return storage_id;
	}

	public void setStorage_id(String storage_id) {
		this.storage_id = storage_id;
		this.put("storage_id", storage_id);
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
		this.put("unit", unit);
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
		this.put("count", count);
	}

	public String getAlarm_count() {
		return alarm_count;
	}

	public void setAlarm_count(String alarm_count) {
		this.alarm_count = alarm_count;
		this.put("alarm_count", alarm_count);
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
		this.put("category_id", category_id);
	}
	
	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
		this.put("abbr", abbr);
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
		this.put("price", price);
	}
	
	public String getType_name(){
		String name=this.type;
		if(type.equals("1")){
			name = "直拨";
		}else if(type.equals("2")){
			name = "入库";
		}
		return name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
		this.put("image", image);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
		this.put("remark", remark);
	}
	
	
}
