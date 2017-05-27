<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.service.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
String rest_id = user.getRest_id();
CommonService commonService = (CommonService)GetBean.getBean("commonService");
IData p = new IData();
p.put("rest_id",rest_id);
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

.vipcard{
    float:left;height:90px;width:270px;margin:20px;margin-left:0px;background:#3366FF;color:#FFF;position:relative;cursor:pointer;
    border:solid 2px #FFF;
}
.vipcard:hover{
    border:solid 2px #3366FF;
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
ajax_flag = 0;
$(document).ready(function(){
    
    function initPage(data){
	    $("#card_list").html('');
	    for(var i=0;i<data.length;i++){
	        var temp = data[i];
	        var str = '<div class="vipcard" user_id="'+temp.USER_ID+'" custname="'+temp.CUSTNAME+'" card_no="'+temp.CARD_NO+'">'+
	          '<div class="credit_name">卡号 ： '+temp.CARD_NO+'</div>'+
	          '<div class="credit_value">余额：￥'+temp.MONEY+'</div>'+
	          '</div>';
	        $("#card_list").append(str);
	    }
	    if(data.length>0){
	        $("#err_msg").hide();
	    }else{
	        $("#err_msg").html('未查询到相关会员卡信息');
	    }
	}
	
    $("#vipcardquerypage").addClass("left_menu_sel");
    
    $("#query_input")[0].focus();
    
    $("#queryBtn").live("click",function(){
        if(ajax_flag>0){
            return false;
        }
        if( $.trim($("#query_input").val())==''){
            $("#query_input").val('');
            $("#query_input")[0].focus();
            return false;
        }
        ajax_flag = 1;
        $("#card_list").html('');
        $("#querytable").hide();
        $("#err_msg").show();
        $("#err_msg").html("正在查询,请稍候......");
        $("#resulttable").show();
        $.post("/cust/queryVipCardList.html", {
            query_str:$("#query_input").val()
            },function (result) {
                ajax_flag = 0;
				var obj = $.parseJSON(result);
				if (obj.success == "true") {
		            initPage(obj.data);
				}else{
				    $("#err_msg").html(obj.msg);
				}
		}).error(function(){
		    $("#err_msg").html("系统异常！");
		});
    }); 
    
    $(".vipcard").live("click",function(){
        var user_id = $(this).attr("user_id");
        document.location.href="/cust/vipcardinfo.html?user_id="+user_id+"&time="+new Date();
    });
    
    $("#backBtn").live("click",function(){
        $("#resulttable").hide();
        $("#querytable").show();
        $("#query_input")[0].focus();
        $("#err_msg").html("正在查询,请稍候......");
        $("#card_list").html('');
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
							    //socket.close();
							    isReading = 0;
							    if(card_no == 'RETRY'){
					            alert( '请重新将卡放在读卡器上' );
					            //return false;
					        }else if(card_no.length==32){
					            card_no = card_no.substr((32-4) , 32);
					            $("#query_input").val(card_no);
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
    
});
</script>  
</head>
<body class="metro">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
<%@ include file="/app/cust/page/index_head.jsp" %>
<%@ include file="/app/cust/page/index_west.jsp" %>
<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;">
    <div style="height:41px;line-height:41px;width:90%;font-size:16px;border-bottom:solid #FFF 1px;margin:0px 40px 10px 0px;border-bottom:solid 1px #DDD;">
	    <div style="float:left;margin-left:20px;" >会员卡查询</div>
    </div>
    <div id="querytable" style="padding-left:20px;">
        <div style="line-height:30px;font-size:16px;margin-top:20px;">请输入会员卡号 （ 或手机号码 ） ：</div>
        <div style="line-height:50px;margin-top:20px;">
            <input id="query_input" type="text" class="cust_input"/>
            <span style="height:30px;line-height:30px;font-size:18px;margin-left:20px;"></span>
        </div>
	    <div style="clear:both;"></div>
	    <div style="clear:both;width:800px;height:0px;line-height:0px;margin-top:30px;margin-left:0px;border-top:solid 1px #CCC;"></div>
        <div style="margin-top:40px;margin-left:0px;">
            <div id="queryBtn" class="btn_m" style="float:left;">查&nbsp;&nbsp;询</div>
            <div id="readcardBtn" class="btn_s" style="float:left;margin-left:30px;">读&nbsp;&nbsp;卡</div>
	    </div>
    </div>
    
    <div id="resulttable" style="display:none;padding-left:20px;padding-right:20px;">
        <div id="card_list" style="margin-left:20px;margin-top:10px;">
	    </div>
	    <div id="" style="margin-left:20px;margin-top:10px;">
	        <div id="err_msg" style="line-height:50px;text-align:center;margin-top:100px;font-size:30px;">正在查询,请稍候......</div>
	    </div>
	    <div style="clear:both;"></div>
	    <div style="width:90%;border-bottom:solid 1px #CCC;">
	    </div>
        <div style="margin-top:10px;margin-left:0px;">
            <div id="backBtn" class="btn_s" style="margin:auto;">返&nbsp;&nbsp;回</div>
	    </div>	
    </div>
    
</div>
</div>
</body>
</html>