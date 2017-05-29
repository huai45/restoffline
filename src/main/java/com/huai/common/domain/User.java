package com.huai.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class User implements Serializable {

	private String staffId;
	private String restId;
	private String roleId;
	private String password;
	private String staffname;
	private String phone;
	private String ctrlright;
	private String dataright;
	private String remark;
	private String token;
	
	private List<Node> nodeList = new ArrayList<Node>();
	private IData info = new IData();
	private IData param = new IData();


	
}
