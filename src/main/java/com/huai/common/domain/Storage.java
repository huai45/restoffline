package com.huai.common.domain;

import java.util.ArrayList;
import java.util.List;

public class Storage {

	private String id;
	
	private String rest_id;
	
	private String name;
	
	private List list = new ArrayList();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public String toString(){
		return "---> id : "+this.id+" , name : "+name +" , list.size : "+this.list.size();
	}

	public String getRest_id() {
		return rest_id;
	}

	public void setRest_id(String rest_id) {
		this.rest_id = rest_id;
	}
}
