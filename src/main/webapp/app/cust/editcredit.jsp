<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.operation.dao.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
String user_id = request.getParameter("user_id");
ut.p("user_id:"+user_id);
QueryDao queryDao = (QueryDao)GetBean.getBean("queryDao");
IData param = new IData();
param.put("user_id",user_id);
param.put("user",user);
IData cust = queryDao.queryCustById(param);

%>
<!DOCTYPE html>
<html>
<head>
<title>RestOn会员管理系统</title>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css">
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css">
<link rel="stylesheet" href="/app/cust/css/common.css">
<link rel="stylesheet" href="/app/cust/css/index_head.css">
<style>
.credit{
    float:left;height:90px;width:270px;margin:20px;margin-left:0px;background:#C42140;color:#FFF;position:relative;cursor:pointer;
    border:solid 2px #FFF;
}
.credit:hover{
    border:solid 2px #C42140;
}
.credit_name{
    position:absolute;top:10px;left:10px;font-size:16px;
}
.credit_limit{
    position:absolute;bottom:10px;left:10px;font-size:12px;
}
.credit_value{
    position:absolute;bottom:10px;right:10px;font-size:12px;
}
.err_msg{
    line-height:30px;margin-left:30px;color:red;font-size:14px;
}

</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
ajax_flag=0;
$(document).ready(function(){
    
    $("#creditquerypage").addClass("left_menu_sel");
        
    $("#custname")[0].focus();
    
    $("#cancelBtn").live("click",function(){
        document.location.href="/cust/creditinfo.html?user_id=<%= cust.getString("USER_ID") %>&time="+new Date();
    });   
    
    $("#submitBtn").live("click",function(){
        if(ajax_flag > 0){
	        return false;
	    }
	    var tag = 0; 
        var custname = $.trim($("#custname").val());
        if (custname == "") {
            $("#custname").val('');
			$("#custname_err").text('*请输入客户姓名');
			tag = 1;
		}
		var phone = $.trim($("#phone").val());
        if (phone == "") {
            $("#phone").val('');
			$("#phone_err").text('*客户电话为空');
			tag = 1;
		}
		var credit = $.trim($("#credit").val());
		if (credit == "") {
		    $("#credit").val('');
			$("#credit_err").text('*请输入信用额度');
			tag = 1;
		}
		if (isNaN(credit)) {
			$("#credit_err").text('*信用额度只能是数字');
			tag = 1;
		}
		var remark = $.trim($("#remark").val());
		if(tag>0){
		    return false;
		}
		ajax_flag = 1;
		$.post("/cust/editcredit.html", {
		        user_id : '<%= cust.getString("USER_ID") %>',
		        custname : custname,
		        phone : phone,
		        credit : credit,
		        remark : remark
		    }, function (result) {
				var obj = $.parseJSON(result);
				alert(obj.msg);
				if (obj.success == "true") {
	                document.location.href="/cust/creditinfo.html?user_id=<%= cust.getString("USER_ID") %>&time="+new Date();
				}else{
				    ajax_flag = 0;
				}
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
    });    
        
    $('#custname').live("keyup",function(){
        $("#custname_err").text('');
    });    
    
    $('#phone').live("keyup",function(){
        $("#phone_err").text('');
    });    
    
    $('#credit').live("keyup",function(){
        $("#credit_err").text('');
    });    
    
});
</script>  
</head>
<body class="metro">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
<%@ include file="/app/cust/page/index_head.jsp" %>
<%@ include file="/app/cust/page/index_west.jsp" %>
<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;">
    <div style="height:41px;line-height:41px;width:90%;font-size:16px;border-bottom:solid #FFF 1px;margin:0px 40px 10px 0px;border-bottom:solid 1px #DDD;">
	    <div style="float:left;margin-left:20px;" >编辑挂账用户信息</div>
    </div>
    <div style="font-size:18px;padding-left:20px;">
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">客户编号</div>
        <div style="line-height:30px;"><%= cust.getString("USER_ID") %></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">客户姓名</div>
        <div style="line-height:30px;"><input id="custname" type="text" value="<%= cust.getString("CUSTNAME") %>" class="cust_input"/><span id="custname_err" class="err_msg"></span></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">联系电话</div>
        <div style="line-height:30px;"><input id="phone" type="text" value="<%= cust.getString("PHONE") %>" class="cust_input"/><span id="phone_err" class="err_msg"></span></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">信用度</div>
        <div style="line-height:30px;"><input id="credit" type="text" value="<%= cust.getString("CREDIT") %>" class="cust_input"/>&nbsp;&nbsp;&nbsp;元<span id="credit_err" class="err_msg"></span></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">备注</div>
        <div style="line-height:30px;"><input id="remark" type="text" value="<%= cust.getString("REMARK") %>" class="cust_input"/></div>
        
        <div style="clear:both;"></div>
	    <div style="clear:both;width:800px;height:0px;line-height:0px;margin-top:30px;margin-left:0px;border-top:solid 1px #CCC;"></div>
        <div style="margin-top:20px;margin-left:0px;">
            <div id="submitBtn" class="btn_m" style="float:left;">保&nbsp;&nbsp;存</div>
            <div id="cancelBtn" class="btn_s" style="float:left;margin-left:30px;">取&nbsp;&nbsp;消</div>
	    </div>
	    
    </div>
    
</div>
</div>
</body>
</html>