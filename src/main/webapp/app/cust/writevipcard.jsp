<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.service.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
CommonService commonService = (CommonService)GetBean.getBean("commonService");
IData p = new IData();
IData param = commonService.queryRestParam(p);
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
var isReading = 0;
$(document).ready(function(){
    
    $("#writevipcardpage").addClass("left_menu_sel");
    
    $("#submitBtn").live("click",function(){
        if (isReading==0) {
            var tag = 0; 
		    $(".err_msg").text('');
		    var card_no = $.trim($("#card_no").val());
	        if (card_no.length!=4&&card_no.length!=5) {
	            $("#card_no").val('');
				$("#card_no_err").text('*请输入4-5位数字卡号');
				tag = 1;
			}
			if (isNaN(card_no)) {
	            $("#card_no").val('');
				$("#card_no_err").text('*请输入4-5位数字卡号');
				tag = 1;
			}
			if(tag>0){
			    return false;
			}
            isReading = 1;
		    var data = {};
		    data.type="writecard";
		    data.card_no=card_no;
		    // ws://localhost:9988/websocket
		    var socket = new WebSocket("<%= param.getString("PARAM11") %>"); 
			    // 打开Socket 
				socket.onopen = function(event) { 
					// 发送一个初始化消息
					socket.send(JSON.stringify(data)); 
					// 监听消息
					socket.onmessage = function(event) { 
					    socket.close();
					    isReading = 0;
				        alert( event.data );
				}; 
				// 监听Socket的关闭
				socket.onclose = function(event) { 
				    //alert("Client notified socket has closed");
				};
				socket.onerror = function(event) { 
				    //alert(" onerror  ");
				    socket.close();
				};
		    };
		}
    });   
    
    $('#card_no').live("keyup",function(){
        $("#card_no_err").text('');
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
	    <div style="float:left;margin-left:20px;" >初始化会员卡（制卡）</div>
    </div>
    <div style="font-size:18px;padding-left:20px;">
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">卡号</div>
        <div style="line-height:30px;"><input id="card_no" type="text" class="cust_input" /><span id="card_no_err" class="err_msg"></span></div>
        
        <div style="clear:both;"></div>
	    <div style="clear:both;width:800px;height:0px;line-height:0px;margin-top:30px;margin-left:0px;border-top:solid 1px #CCC;"></div>
        <div style="margin-top:20px;margin-left:0px;">
            <div id="submitBtn" class="btn_m" style="float:left;">写&nbsp;&nbsp;入</div>
	    </div>
	    
    </div>
    
</div>
</div>
</body>
</html>


