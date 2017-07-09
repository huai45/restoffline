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
$(document).ready(function(){
    
    $("#addvipcardpage").addClass("left_menu_sel");
        
    $("#custname")[0].focus();
    
    $("#resetBtn").live("click",function(){
        $("#card_no").val('');
        $("#custname").val('');
        $("#phone").val('');
        $("#credit").val('');
        $("#remark").val('');
        $("#custname")[0].focus();
    });  
    
    var isReading = 0;
    
    $("#readcardBtn").live("click",function(){
        //if (isReading==0) {
        if (true) {
            isReading = 1;
		    var data = {};
		    data.type="readcard";
		    // ws://localhost:9988/websocket
		    var socket = new WebSocket("<%= param.getString("PARAM11") %>"); 
			    // 打开Socket 
				socket.onopen = function(event) { 
					// 发送一个初始化消息
					socket.send(JSON.stringify(data)); 
					// 监听消息
					socket.onmessage = function(event) { 
					    var card_no = event.data;
					    socket.close();
					    isReading = 0;
					    if(card_no == 'RETRY'){
				            alert( '请重新将卡放在读卡器上读卡！' );
				            return false;
				        }else if(card_no.length==32){
				            card_no = card_no.substr((32-4) , 32);
				            $("#card_no").val(card_no);
				        }else{
				            alert( card_no );
				        }
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
    
    
    
    
    $("#submitBtn").live("click",function(){
        if(ajax_flag > 0){
	        return false;
	    }
	    var tag = 0; 
	    $(".err_msg").text('');
	    var card_no = $.trim($("#card_no").val());
        if (card_no == "") {
            $("#card_no").val('');
			$("#card_no_err").text('*请输入卡号');
			tag = 1;
		}
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
		var money = $.trim($("#money").val());
		if (money == "") {
		    $("#money").val('');
			$("#money_err").text('*请输入初始余额');
			tag = 1;
		}
		if (isNaN(money)) {
			$("#money_err").text('*初始余额只能是数字');
			tag = 1;
		}
		var remark = $.trim($("#remark").val());
		if(tag>0){
		    return false;
		}
		ajax_flag = 1;
		$.post("/cust/addvipcard.html", {
		        card_no : card_no,
		        custname : custname,
		        phone : phone,
		        money : money,
		        remark : remark
		    }, function (result) {
				var obj = $.parseJSON(result);
				alert(obj.msg);
				if (obj.success == "true") {
	                document.location.href="/cust/vipcardinfo.html?user_id="+obj.user_id+"&time="+new Date();
				}else{
				    ajax_flag = 0;
				}
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
    });    
        
    $('#card_no').live("keyup",function(){
        $("#card_no_err").text('');
    });    
    
    $('#custname').live("keyup",function(){
        $("#custname_err").text('');
    });  
    
    $('#phone').live("keyup",function(){
        $("#phone_err").text('');
    });    
    
    $('#money').live("keyup",function(){
        $("#money_err").text('');
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
	    <div style="float:left;margin-left:20px;" >新建会员卡</div>
    </div>
    <div style="font-size:18px;padding-left:20px;">
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">卡号</div>
        <div style="line-height:30px;"><input id="card_no" type="text" class="cust_input" readonly/><span id="card_no_err" class="err_msg"></span></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">客户姓名</div>
        <div style="line-height:30px;"><input id="custname" type="text" class="cust_input"/><span id="custname_err" class="err_msg"></span></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">联系电话</div>
        <div style="line-height:30px;"><input id="phone" type="text" class="cust_input"/><span id="phone_err" class="err_msg"></span></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">初始余额</div>
        <div style="line-height:30px;"><input id="money" type="text" class="cust_input" value="0" readonly/>&nbsp;&nbsp;&nbsp;元<span id="money_err" class="err_msg"></span></div>
        <div style="line-height:24px;font-size:12px;font-weight:bold;margin-top:15px;">备注</div>
        <div style="line-height:30px;"><input id="remark" type="text" class="cust_input"/></div>
        
        <div style="clear:both;"></div>
	    <div style="clear:both;width:800px;height:0px;line-height:0px;margin-top:30px;margin-left:0px;border-top:solid 1px #CCC;"></div>
        <div style="margin-top:20px;margin-left:0px;">
            <div id="submitBtn" class="btn_m" style="float:left;">提&nbsp;&nbsp;交</div>
            <div id="resetBtn" class="btn_s" style="float:left;margin-left:30px;">重&nbsp;&nbsp;置</div>
            <div id="readcardBtn" class="btn_s" style="float:left;margin-left:30px;">读&nbsp;&nbsp;卡</div>
	    </div>
	    
    </div>
    
</div>
</div>
</body>
</html>