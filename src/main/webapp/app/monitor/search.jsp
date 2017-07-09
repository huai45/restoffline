<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="net.sf.json.*"%><%
String trade_type_code = request.getParameter("TRADE_TYPE_CODE") ;
if(trade_type_code==null||trade_type_code.trim().equals("")){
	System.out.println(ut.err("无效操作类型"));
	out.print(ut.err("无效操作类型"));
	return;
}
try{
	if(trade_type_code.equals("query_table_bill")){
		out.print(queryTableBill(request));	
	}else{
		out.print(ut.err("无效操作类型！"));
	}

}catch(Exception e){
	e.printStackTrace();
	out.print(ut.err("操作异常！"));
}
%>
<%!
public String queryTableBill(HttpServletRequest request){
	User user = (User)request.getSession().getAttribute(CC.USER_CONTEXT);
	String table_id = request.getParameter("table_id");
	JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
	List billList = jdbcTemplate.queryForList("select * from tf_bill where table_id = ?  and pay_type = '0' ",
		new Object[]{ table_id });
    String bill_id = "";
	if(billList.size()>0){
		bill_id = ((Map)billList.get(0)).get("BILL_ID").toString();
    }
	return ut.suc("查询成功","bill_id",bill_id);	
}

public String getParam(HttpServletRequest request,String name){
	String param = "Exception";
	try{
		param = new String(request.getParameter(name).getBytes("iso-8859-1"),"UTF-8");
		/*
		ut.log(" name0 :" +new String(request.getParameter(name).getBytes("iso-8859-1"),"UTF-8"));
		ut.log(" name1 :" +new String(request.getParameter(name).getBytes("gbk"),"UTF-8"));
		ut.log(" name2 :" +new String(request.getParameter(name).getBytes("UTF-8"),"gbk"));
		ut.log(" name4 :" +new String(request.getParameter(name).getBytes("iso-8859-1"),"gbk"));
		ut.log(" name5 :" +request.getParameter(name));
		*/
	}catch( Exception e){
		
	}
    return param;	
}

;  
%>