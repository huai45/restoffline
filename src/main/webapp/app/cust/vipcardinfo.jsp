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

</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
user_id="<%= user_id %>";
$(document).ready(function(){
    
    $("#vipcardquerypage").addClass("left_menu_sel");
        
    $("#payfeebtn").live("click",function(){
        document.location.href="/cust/payfeeforvipcardpage.html?user_id="+user_id+"&time="+new Date();
    });    
        
    $("#editbtn").live("click",function(){
        document.location.href="/cust/editvipcardpage.html?user_id="+user_id+"&time="+new Date();
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
	    <div style="float:left;margin-left:20px;" >会员卡信息</div>
	    <div id="editbtn" class="title_btn" style="" >
	        <image src="/resource/image/icon/black/edit.png" class="title_btn_image"/>
	         编辑
	    </div>
	    <div id="payfeebtn" class="title_btn" style="" >
	        <image src="/resource/image/icon/black/add.png" class="title_btn_image"/>
	         充值
	    </div>
    </div>
    <div style="font-size:18px;padding-left:20px;">
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:10px;">会员卡号</div>
        <div style="line-height:30px;"><%= cust.getString("CARD_NO") %></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:10px;">客户姓名</div>
        <div style="line-height:30px;"><%= cust.getString("CUSTNAME") %></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:10px;">联系电话</div>
        <div style="line-height:30px;"><%= cust.getString("PHONE") %></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:10px;">卡内余额</div>
        <div style="line-height:30px;"><%= cust.getString("MONEY") %> 元</div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:10px;">开户时间</div>
        <div style="line-height:30px;"><%= cust.getString("OPER_TIME") %></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:10px;">备注</div>
        <div style="line-height:30px;"><%= cust.getString("REMARK") %></div>
    </div>
    
</div>
</div>
</body>
</html>