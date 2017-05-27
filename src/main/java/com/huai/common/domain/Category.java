package com.huai.common.domain;

import java.util.ArrayList;
import java.util.List;

public class Category {

	private String id;
	
	private String name;
	
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

	
	public String toString(){
		return "---> id : "+this.id+" , name : "+name ;
	}
}
