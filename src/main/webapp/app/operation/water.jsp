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
.query_foodname{
    cursor:pointer;font-size:20px;
}
.query_foodname:hover{
    color:#F67824;
}
.food_info_item{
    font-size:14px;margin-top:2px;color:#333;
}
.food_info_item div{
    float:left;width:140px;
}
.food_info_logo_div {
    float:left;position:relative;height:32px;width:34px;text-align:center;margin-top:5px;margin-right:12px;font-size:20px;
}
.food_info_logo{
    position:absolute;top:0px;left:0px;height:34px;width:34px;
}
.leftdirection  
{  
    width:0;height:0;  
    line-height:0;  
    border-width:10px;  
    border-style:solid;  
    border-color: transparent #FFF transparent transparent;  
}
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
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
	
    $('#categoryList').html('');
    $.post("/query/queryCategoryData.html", {
	        category : '酒水'
	    }, function (result) {
			var obj = $.parseJSON(result);	   
			if (obj.success == "true") {
			    $("#today_water_count").text(obj.categoryData.length);
                   for(var i=0;i<obj.categoryData.length;i++){
		            var food = obj.categoryData[i];
		            var food_div = '<div style="height:65px;margin-left:20px;"  >'+
                       '  <div class="food_info_logo_div"><div>('+(i+1)+')</div></div>'+
                       '  <div>'+
                       '    <div><span class="query_foodname" >'+food.FOOD_NAME+'</span></div>'+
                       '    <div class="food_info_item">'+
                       '      <div>单价:￥'+food.PRICE+'</div>'+
                       '      <div style="color:#000;">出库数量  :  '+(food.count-food.back_count)+'</div>'+
                       '      <div style="color:#000;">点菜数量  :  '+food.count+'</div>'+
                       '      <div style="color:#'+(food.back_count>0?"38A2DB":"000")+';">退菜数量  :  '+food.back_count+'</div>'+
                       '      <div style="color:#'+(food.free_count>0?"F67824":"000")+';">赠送数量  :  '+food.free_count+'</div>'+
                       '    </div>'+
                       '  </div>'+
                       '</div>';
		            $("#categoryList").append(food_div);
		        }
			}else{
			    alert(obj.msg);
			}
	});
	
	
});
</script> 
</head>
<body style="">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
	<div id="west" data-options="region:'west',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:120px;background-color:#3A3A40;">
		<div style="width:100%;line-height:50px;font-size:18px;color:#CCC;text-align:center;margin-top:50px;">
		    <image class="backToDeskBtn" src="/app/operation/image/back.png" style="height:40px;width:40px;cursor:pointer;"/ >
		</div>
		<div style="width:100%;line-height:50px;font-size:18px;color:#CCC;text-align:center;margin-top:60px;">
		    酒水销售
		</div>
		<div  class="leftdirection" style="position:absolute;top:182px;right:0px;"></div> 
	</div>
	<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;">
	    <div id="querycategory_north" style="padding-left:50px;color:#fff;" >
		    <div style="height:40px;line-height:40px;width:750px;font-size:16x;color:#111;margin-top:5px;border-bottom:solid 1px #D3D3D3;">
		  	    <span style="margin-left:0px;font-size:18px;"><%= today %></span>
		    </div>
		</div>
		<div id="querycategory_center" style="padding-left:50px;">
			  <div style="height:24px;line-height:24px;font-size:16px;color:#111;margin-top:20px;">
		  	    <span style="margin-left:0px;">共计 - </span><span id="today_water_count" style="margin-left:0px;"></span>种
		    </div>
		    <div id="" style="color:#000;margin-top:10px;margin-bottom:20px;" >
				    <div id="categoryList" style="margin-top:0px;">
				        
				    </div>
				</div>
		
		</div>
	</div>
</div>
</body>
</html>