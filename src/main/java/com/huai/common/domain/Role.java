package com.huai.common.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huai.common.util.ut;

public class Role {

	private String role_id = "";
	private String rolename = "";
	private String ctrlright = "";
	private String dataright = "";
	private String remark = "";
	
	private List<String> nodeList = new ArrayList();
	
	private Map<String,String> rightMap = new HashMap();
	
	public String getCtrlright() {
		if(ctrlright==null){
			return "";
		}
		return ctrlright;
	}
	public void setCtrlright(String ctrlright) {
		this.ctrlright = ctrlright;
	}
	public String getDataright() {
		if(dataright==null){
			return "";
		}
		return dataright;
	}
	public void setDataright(String dataright) {
		this.dataright = dataright;
		if(!ut.isEmpty(dataright)){
			String[] data = dataright.split("#");
			rightMap.clear();
			for(int i=0;i<data.length;i++){
				rightMap.put(data[i], "Y");
			}
		}
	}
	public String getRemark() {
		if(remark==null){
			return "";
		}
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List getNodeList() {
		return nodeList;
	}
	public void setNodeList(List nodeList) {
		this.nodeList = nodeList;
	}
	
	public String getNodeScript() {
		 StringBuffer str = new StringBuffer();
//		 for(int i=0;i<this.getNodeList().size();i++){
//			 Node node = (Node)this.getNodeList().get(i);  
//		     str.append(node.getScript());
//		     if(i<this.getNodeList().size()-1){
//				 str.append(",");
//			 }
//		 }
//		 System.out.println(" str = "+str.toString());
		 return str.toString();
	}
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getRolename() {
		if(rolename==null){
			return "��ȱ";
		}
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public Map getRightMap() {
		return rightMap;
	}
	public void setRightMap(Map rightMap) {
		this.rightMap = rightMap;
	}
}
