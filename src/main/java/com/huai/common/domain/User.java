package com.huai.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

	private String staff_id;
	private String rest_id;
	private String role_id;
	
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

	public String getStaff_id() {
		return staff_id;
	}

	public void setStaff_id(String staff_id) {
		this.staff_id = staff_id;
	}

	public String getRest_id() {
		return rest_id;
	}

	public void setRest_id(String rest_id) {
		this.rest_id = rest_id;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStaffname() {
		return staffname;
	}

	public void setStaffname(String staffname) {
		this.staffname = staffname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCtrlright() {
		return ctrlright;
	}

	public void setCtrlright(String ctrlright) {
		this.ctrlright = ctrlright;
	}

	public String getDataright() {
		return dataright;
	}

	public void setDataright(String dataright) {
		this.dataright = dataright;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Node> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public IData getInfo() {
		return info;
	}

	public void setInfo(IData info) {
		this.info = info;
	}

	public IData getParam() {
		return param;
	}

	public void setParam(IData param) {
		this.param = param;
	}
	
	
}
