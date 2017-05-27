<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
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
$(document).ready(function(){
    
    function initPage(data){
	    $("#credit_list").html('');
	    for(var i=0;i<data.length;i++){
	        var temp = data[i];
	        var str = '<div class="credit" user_id="'+temp.USER_ID+'" custname="'+temp.CUSTNAME+'">'+
	          '<div class="credit_name">'+temp.CUSTNAME+'</div>'+
	          '<div class="credit_limit">信用：￥'+temp.CREDIT+'</div>'+
	          '<div class="credit_value">可用：￥'+temp.MONEY+'</div>'+
	          '</div>';
	        $("#credit_list").append(str);
	    }
	}

    $("#creditquerypage").addClass("left_menu_sel");
    
    $.post("/query/queryCreditUserList.html", {},function (result) {
		var obj = $.parseJSON(result);
		if (obj.success == "true") {
            initPage(obj.data);
		}else{
		    $("#err_msg").html(obj.msg);
		}
	}).error(function(){
	    alert("系统异常");
	});
    
    $(".credit").live("click",function(){
        var user_id = $(this).attr("user_id");
        document.location.href="/cust/creditinfo.html?user_id="+user_id+"&time="+new Date();
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
        <div style="float:left;margin-left:20px;" >请选择挂账用户</div>
    </div>
    <div id="credit_list" style="margin-left:20px;margin-top:10px;">
        <div id="err_msg" style="line-height:50px;text-align:center;margin-top:100px;font-size:20px;">正在查询挂账用户列表,请稍候......</div>
    </div>	
</div>
</div>
</body>
</html>