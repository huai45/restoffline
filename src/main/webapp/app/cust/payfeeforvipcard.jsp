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
ajax_flag=0;
socketurl = "<%= user.getParam().getString("PARAM3") %>";
default_printer = "<%= user.getParam().getString("PARAM4") %>";
$(document).ready(function(){
    
    $("#vipcardquerypage").addClass("left_menu_sel");
        
    $("#recv_fee_input")[0].focus();
        
    
    $("#cancelBtn").live("click",function(){
        document.location.href="/cust/vipcardinfo.html?user_id="+user_id+"&time="+new Date();
    }); 
    
    $("#submitBtn").live("click",function(){
        if(ajax_flag > 0){
	        return false;
	    }
	    $("#money_err").text('');
        var recvfee = $("#recv_fee_input").val();
        if (recvfee == "") {
            $("#money_err").text('*请输入充值金额');
			$("#recv_fee_input")[0].focus();
			return false;
		}
		if (isNaN(recvfee)) {
		    $("#money_err").text('*充值金额只能是数字');
			$("#recv_fee_input")[0].focus();
			return false;
		}
		if ( parseInt(recvfee)==0 ) {
			$("#money_err").text('*充值金额不能为0');
			$("#recv_fee_input")[0].focus();
			return false;
		}
		ajax_flag = 1;
		$.post("/cust/payfeeForVipCard.html", {
		        user_id : user_id,
		        recvfee : parseInt(recvfee)
		    }, function (result) {
				var obj = $.parseJSON(result);
				alert(obj.msg);
				if (obj.success == "true") {
					var data = {};
				    data.type="printvippay";
				    data.data = obj.info;
				    data.data.printer = default_printer;
				    var socket = new WebSocket(socketurl); 
					    // 打开Socket 
						socket.onopen = function(event) { 
							// 发送一个初始化消息
							socket.send(JSON.stringify(data)); 
							// 监听消息
							socket.onmessage = function(event) { 
							    socket.close();
							    document.location.href="/cust/vipcardinfo.html?user_id="+user_id+"&time="+new Date();
						}; 
						// 监听Socket的关闭
						socket.onclose = function(event) { 
						    //alert("Client notified socket has closed");
						};
						socket.onerror = function(event) { 
						    //alert(" onerror  ");
						    //socket.close();
						};
				    };
	                
				}else{
				    ajax_flag = 0;
				}
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
    });    
    
    $('#recv_fee_input').live("keyup",function(){
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
	    <div style="float:left;margin-left:20px;" >会员卡充值</div>
    </div>
    <div style="padding-left:20px;">
        <div style="line-height:30px;font-size:16px;margin-top:30px;">请输入充值金额：</div>
        <div style="line-height:50px;margin-top:20px;">
            <input id="recv_fee_input" type="text" class="cust_input"/>
            <span style="height:30px;line-height:30px;font-size:18px;margin-left:20px;">元</span>
            <span id="money_err" class="err_msg"></span>
        </div>
	    <div style="clear:both;"></div>
	    <div style="clear:both;width:800px;height:0px;line-height:0px;margin-top:30px;margin-left:0px;border-top:solid 1px #CCC;"></div>
        <div style="margin-top:40px;margin-left:0px;">
            <div id="submitBtn" class="btn_m" style="float:left;">提&nbsp;&nbsp;交</div>
            <div id="cancelBtn" class="btn_s" style="float:left;margin-left:30px;">取&nbsp;&nbsp;消</div>
	    </div>
    </div>
</div>
</div>
</body>
</html>