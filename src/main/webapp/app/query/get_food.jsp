<%@ page language="java" import="java.util.*,com.huai.common.util.*,org.springframework.jdbc.core.JdbcTemplate" pageEncoding="UTF-8"%><%
try{
	JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
	List foods = jdbcTemplate.queryForList("select * from td_food where 1 = 1  " ,new Object[]{});
	StringBuffer sb = new StringBuffer();
	sb.append("[");
	for(int i=0;i<foods.size();i++){
		Map food = (Map)foods.get(i);
		sb.append("{").append("\"food_id\":\"").append(food.get("food_id")).append("\",\"food_name\":\"").append(food.get("food_name")).append("\"}");
		if(i<foods.size()-1){
			sb.append(",");
		}
	}
	sb.append("]");
	out.print(sb.toString()); 
	//System.out.print(sb.toString());
}catch(Exception e){
	e.printStackTrace();
	out.print(ut.err("操作异常！"));
}
%>
