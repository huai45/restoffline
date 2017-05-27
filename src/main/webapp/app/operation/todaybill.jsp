<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String table_id = request.getParameter("table_id");
ut.p("table_id:"+table_id);
if(table_id==null){
	table_id = "";
}
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css">
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css">
<style>
.leftdirection  
{  
    width:0;height:0;  
    line-height:0;  
    border-width:10px;  
    border-style:solid;  
    border-color: transparent #FFF transparent transparent;  
}
.btn_m{
   height:40px;line-height:40px;width:200px;background-color:#3365BA;color:#FFF;font-size:12px;font-weight:bold;
   text-align:center;cursor:pointer;
}
.bill{
    height:50px;line-height:50px;width:850px;border-left:solid 10px #3365BA;background:#E7E7E7;cursor:pointer;
    margin-top:20px;margin-bottom:20px;font-size:12px;
}
.bill:hover{
    background:#E7E7E7;
}
.bill_fee{
    float:left;width:120px;margin-left:20px;line-height:50px;font-size:18px;font-weight:bold;
}
.bill_table{
    float:left;width:100px;margin-left:10px;line-height:50px;
}
.pay_type{
    float:left;width:100px;margin-left:10px;line-height:50px;font-weight:bold;
}
.bill_time{
    float:left;width:230px;margin-left:10px;line-height:50px;
}
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
flag = 0;
table_id="<%=table_id%>";
function initTopPage() {
    top.resetTablePage();
    top.Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
    top.$("#smart_str")[0].focus();
    top.$("#mainFrame").attr("src","/app/operation/blank.jsp?time="+new Date());
}
function initBillPage(bills) {
    $("#bill_list").html('');
    $("#bill_count").html(bills.length);
    for (var i = 0; i < bills.length; i++) {
		var bill = bills[i];
		var state = "未结帐";
		if(bill.PAY_TYPE=='1'){
		    state = "已结账";
		}
	    var html = '<div class="bill" bill_id="' + bill.BILL_ID + '" >';
			html = html + '<div class="bill_fee">￥' + bill.BILL_FEE + '</div>';
			html = html + '<div class="bill_table">座位: ' + bill.TABLE_ID + '</div>';
			html = html + '<div class="pay_type">状态: ' + state + '</div>';
			html = html + '<div class="bill_time">开台时间： ' + bill.OPEN_TIME + '</div>';
			html = html + '<div class="bill_time">封单时间： ' + bill.CLOSE_TIME + '</div>';
			html = html + '</div>';
	    $("#bill_list").append(html);
	}
}
$(document).ready(function(){

    $(document).keydown(function(event){
        if(event.keyCode==8){
            return false;
        }
        if(event.keyCode==27){
            initTopPage();
        }
    });
    
    $('.backToDeskBtn').click(function(){
        initTopPage();
	});
	
	$.post("/query/queryTodayBills.html", { table_id:table_id }, function (result) {
		var obj = $.parseJSON(result);
		if (obj.success == "true") {
		    initBillPage(obj.bills);
		}else{
		    alert(obj.msg);
		}
	});
	
	$(".bill").live("click",function(){
	    var bill_id = $(this).attr("bill_id");
        window.open("/query/querybill.html?bill_id="+bill_id,'账单明细',"height=660,width=1050,top=0,left=0,toolbar=no,menubar=no,location=no,status=no ");
        return false;
	});
	
	
});
</script> 
</head>
<body style="">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
	<div id="west" data-options="region:'west',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:120px;background-color:#3A3A40;position:relative;">
		<div style="width:100%;line-height:50px;font-size:18px;color:#CCC;text-align:center;margin-top:50px;">
		    <image class="backToDeskBtn" src="/app/operation/image/back.png" style="height:40px;width:40px;cursor:pointer;"/ >
		</div>
		<div style="width:100%;line-height:50px;font-size:18px;color:#CCC;text-align:center;margin-top:60px;">
		    账单
		</div>
		<div  class="leftdirection" style="position:absolute;top:182px;right:0px;"></div> 
	</div>
	<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;font-size:14px;">
	    <div style="font-size:28px;margin-top:30px;margin-left:30px;">账单查询
	    </div>
	    <div style="margin-top:30px;margin-left:30px;">
	        <div style="float:left;margin-right:40px;font-size:12px;font-weight:bold;color:#000;">桌位： <%= table_id %></div>
	        <div style="float:left;margin-right:40px;font-size:12px;font-weight:bold;color:#777;">共 <span id="bill_count">-</span> 单</div>
	    </div>
	    <div style="clear:both;"></div>
	    <div style="clear:both;width:800px;height:0px;line-height:0px;margin-top:20px;margin-left:30px;border-top:solid 1px #CCC;"></div>
	    <div id="bill_list" style="margin-top:20px;margin-left:30px;">
	        
	    </div>
	    
	    
	    
	    
	</div>
	
	
</div>
</body>
</html>