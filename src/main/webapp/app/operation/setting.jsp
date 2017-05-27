<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String today = ut.currentDate();
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
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
flag = 0;
function initTopPage() {
    top.resetTablePage();
    top.Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
    top.$("#smart_str")[0].focus();
    top.$("#mainFrame").attr("src","/app/operation/blank.jsp?time="+new Date());
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
	
	$('#finishTodayBtn').click(function(){
	    if(flag>0){
            return false;
        }
	    if(confirm("确定要结束今日的营业工作吗?")){
            flag = 1;
            $.post("/operation/finishToday.html", { }, function (result) {
				var obj = $.parseJSON(result);
				flag = 0;  
				alert(obj.msg);
				if (obj.success == "true") {
				    top.document.location.reload();
				}
			});
        }
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
		    设置
		</div>
		<div  class="leftdirection" style="position:absolute;top:182px;right:0px;"></div> 
	</div>
	<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;font-size:14px;">
	    <div style="font-size:28px;margin-top:30px;margin-left:30px;">设置
	    </div>
	    <div style="margin-top:30px;margin-left:30px;">
	        <div style="float:left;margin-right:40px;font-size:12px;font-weight:bold;color:#000;">结束营业</div>
	        <div style="float:left;margin-right:40px;font-size:12px;font-weight:bold;color:#777;">常规</div>
	    </div>
	    <div style="clear:both;"></div>
	    <div style="margin-top:50px;margin-left:30px;">
            <div style="font-size:18px;margin-top:20px;">功能介绍</div>
            <div style="font-size:12px;margin-top:10px;">1、当结束一天的营业工作后，需要点击下面的“结束营业”按钮，将本日的所有账单数据备份，为第二天的营业做准备。</div>
            <div style="font-size:12px;margin-top:10px;">2、结束营业后，当日的所有统计数据不能再从收银前台查询，请去历史数据查询功能查询。</div>
	    </div>
	    <div style="margin-top:50px;margin-left:30px;">
            <div id="finishTodayBtn" class="btn_m" style="float:left;">结束营业</div>
	    </div>
	    <div style="clear:both;"></div>
	    <div style="clear:both;width:800px;height:0px;line-height:0px;margin-top:20px;margin-left:30px;border-top:solid 1px #CCC;"></div>
	</div>
</div>
</body>
</html>