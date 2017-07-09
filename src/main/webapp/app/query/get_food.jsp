<%@ page language="java" import="java.util.*,com.huai.common.domain.*,net.sf.json.JSONArray,net.sf.json.JSONObject,
org.springframework.jdbc.core.BatchPreparedStatementSetter,java.sql.*,
com.huai.common.util.*,com.mongodb.BasicDBObject,org.springframework.jdbc.core.JdbcTemplate,
javax.sql.DataSource,com.huai.common.domain.User" pageEncoding="UTF-8"%><%
try{
	User user = (User)session.getAttribute( CC.USER_CONTEXT );
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
	System.out.print(sb.toString()); 
}catch(Exception e){
	e.printStackTrace();
	out.print(ut.err("操作异常！"));
}
%>
