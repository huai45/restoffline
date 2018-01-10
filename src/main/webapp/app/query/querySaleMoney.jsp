<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
String start_date = request.getParameter("start_date");
String end_date = request.getParameter("end_date");
System.out.println("start_date:"+start_date);
System.out.println("end_date:"+end_date);
String show_start_date = start_date;
String show_end_date = end_date;
String queryTag = "1";
if(start_date==null){
	queryTag = "0";
	start_date = "";
	show_start_date = ut.currentDate(-1);
}
if(end_date==null){
	end_date = "";
	show_end_date = ut.currentDate(-1);
}
%>
<!DOCTYPE>
<html>
<head>
<title>RestOn统计查询系统</title>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css"/>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css"/>
<link rel="stylesheet" href="/app/query/css/common.css"/>
<link rel="stylesheet" href="/app/query/css/index_head.css"/>
<style>
    .order_head {
        width:97%;float:left;margin-left:30px;height:30px;line-height:30px;font-size:14px;cursor:pointer;
    }
    .order_head div{
	    float:left;width:100px;text-align:left;
	}
    .order_tr{
	    width:97%;float:left;margin-left:10px;padding-left:20px;height:30px;line-height:30px;font-size:14px;color:#595959;cursor:pointer;
	}
	.order_tr div{
	    float:left;width:100px;text-align:left;
	}
	.order_tr_hover{
	    background:#E2E2E2;
	}
	.order_tr_select{
	    background:#80397B;color:#fff;
	}
	.bluefont{
	    color:#80397B;
	}
	.checkbox{
	    height:30px;width:40px;text-align:center;
	}
	.btn {
		 font-weight:normal;
		 cursor: pointer;
		 height:32px;
		 line-height:30px;
		 over-flow: hidden;
		 Width:100px;
		 font-size: 16px; 
		 background:#E0E0E0;
		 BORDER:  hidden;
		 color:#616161;
		 font-family:'Microsoft YaHei';
	}
	.btn:active {
		opacity:0.9;
		-khtml-opacity: .9;
		-moz-opacity: 0.9;
    }
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script src="/app/operation/js/func.js"></script>
<script type="text/javascript">
$(document).ready(function(){
     
     $("#salemoneypage").addClass("left_menu_sel");
     
     $(".order_tr").hover(
         function(){
             $(this).addClass("order_tr_hover");    //鼠标经过添加hover样式   
         },   
         function(){   
             $(this).removeClass("order_tr_hover");   //鼠标离开移除hover样式   
         }   
     );
	
    if($("#center-region-container").width()>1000){
        $(".audit_list").css("width", parseInt($("#center-region-container").width()*97/100));
        $(".fenge").css("width", parseInt($("#center-region-container").width()*90/100));
    }
    
	$(".order_tr").click(function(){
	    $("#allpick").removeAttr("checked");
        if($(".checkbox:checked").length>1){
            $(".checkbox:checked").removeAttr("checked");
            $(".order_tr_select").children().eq(1).addClass("bluefont");
			$(".order_tr_select").children().eq(2).addClass("bluefont");
            $(".order_tr_select").removeClass("order_tr_select");
            $(".bluefont1").addClass("bluefont");
            
            $(this).children().eq(0).children().eq(0).attr("checked",true);
            $(this).addClass("order_tr_select");
            $(this).children().eq(1).removeClass("bluefont");
			$(this).children().eq(2).removeClass("bluefont");
        }else if($(this).hasClass("order_tr_select")){
            $(this).removeClass("order_tr_select");
            $(".checkbox:checked").removeAttr("checked");
			$(this).children().eq(1).addClass("bluefont");
			$(this).children().eq(2).addClass("bluefont");
        }else{
            $(".checkbox:checked").removeAttr("checked");
            $(".order_tr_select").children().eq(1).addClass("bluefont");
			$(".order_tr_select").children().eq(2).addClass("bluefont");
            $(".order_tr_select").removeClass("order_tr_select");
            $(this).children().eq(0).children().eq(0).attr("checked",true);
            $(this).addClass("order_tr_select");
            $(this).children().eq(1).removeClass("bluefont");
			$(this).children().eq(2).removeClass("bluefont");
        }
	});
	
	if(<%= queryTag %>>0){
	
	    $("#msg").show();
	
	    $.post("/query/queryHistorySaleData.html", {
	        start_date : '<%= start_date %>',
	        end_date : '<%= end_date %>'
	        }, function (result) {
				var obj = $.parseJSON(result);
				$("#msg").hide();
				if (obj.success == "true") {
				    initTodayPage(obj);
				}
				return false;
		}).error(function(){
		    $("#msg").hide();
		    alert("查询数据异常！");
		});
	}
		
	$("#queryBtn").click(function(){
	    var start_date = $('#start_year').val()+"-"+$('#start_month').val()+"-"+$('#start_day').val();
	    var end_date = $('#end_year').val()+"-"+$('#end_month').val()+"-"+$('#end_day').val();
        document.location.href="/transPage?page=/query/querySaleMoney&start_date="+start_date+"&end_date="+end_date;
	});
	
	
	$("#end_year").val('<%= show_end_date.substring(0,4) %>');
	$("#end_month").val('<%= show_end_date.substring(5,7) %>');
	$("#end_day").val('<%= show_end_date.substring(8,10) %>');
	
	$("#start_year").val('<%= show_start_date.substring(0,4) %>');
	$("#start_month").val('<%= show_start_date.substring(5,7) %>');
	$("#start_day").val('<%= show_start_date.substring(8,10) %>');
	
});
</script>
</head>
<body id="body" style="">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
<%@ include file="/app/query/page/index_head.jsp" %>
<%@ include file="/app/query/page/index_west.jsp" %>
<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;">
    <div id="" style="height:50px;width:95%;padding-left:0px;line-height:50px;border-bottom:solid 0px #CCCCCC;font-size:20px;">
        <div id="" style="height:50px;width:100%;line-height:50px;border-bottom:solid 1px #CCCCCC;font-size:20px;">
            <div id="" style="height:50px;width:100%;line-height:50px;border-bottom:solid 1px #CCCCCC;font-size:20px;">
            <div id="" style="float:right;">
		        <div id="queryBtn" align="center" class="btn" style="float:left;background:#80397B;color:#FFFFFF;margin-top:8px;margin-right: 18px; " onselectstart='return false'> 查询 </div>
	        </div> 
	        <div style="float:right;height:50px;line-height:50px;margin-left:5px;margin-right:45px;">
	                日
	        </div>
	        <div style="float:right;height:50px;line-height:30px;">
	            <select id="end_day" style="float:left;margin-top:11px;border:solid 1px #CCCCCC;padding:0px;line-height:30px;width:50px; height:30px;font-size:18px; ">
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
					<option value="24">24</option>
					<option value="25">25</option>
					<option value="26">26</option>
					<option value="27">27</option>
					<option value="28">28</option>
					<option value="29">29</option>
					<option value="30">30</option>
					<option value="31" selected="selected" >31</option>
				</select>
	        </div>
	        
	        <div style="float:right;height:50px;line-height:50px;margin-left:5px;margin-right:5px;">
	                月
	        </div>
	        
	        <div style="float:right;height:50px;line-height:30px;">
	            <select id="end_month" style="float:left;margin-top:11px;border:solid 1px #CCCCCC;padding:0px;line-height:30px;width:50px; height:30px;font-size:18px; ">
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
				</select>
	        </div>
	        <div style="float:right;height:50px;line-height:50px;margin-left:5px;margin-right:5px;">
	                年
	        </div>
            <div style="float:right;height:50px;line-height:30px;">
	            <select id="end_year" style="float:left;margin-top:11px;border:solid 1px #CCCCCC;padding:0px;line-height:30px;width:80px; height:30px;font-size:18px; ">
					<option value="2016" >2016</option>
					<option value="2017" >2017</option>
					<option value="2018" >2018</option>
					<option value="2019" >2019</option>
					<option value="2020" >2020</option>
					<option value="2021" >2021</option>
					<option value="2022" >2022</option>
				</select>
	        </div>
	        <div style="float:right;height:50px;line-height:50px;margin-left:10px;margin-right:10px;">
	                至
	        </div>
	        
	        <div style="float:right;height:50px;line-height:50px;margin-left:5px;margin-right:5px;">
	                日
	        </div>
	        <div style="float:right;height:50px;line-height:30px;">
	            <select id="start_day" style="float:left;margin-top:11px;border:solid 1px #CCCCCC;padding:0px;line-height:30px;width:50px; height:30px;font-size:18px; ">
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
					<option value="24">24</option>
					<option value="25">25</option>
					<option value="26">26</option>
					<option value="27">27</option>
					<option value="28">28</option>
					<option value="29">29</option>
					<option value="30">30</option>
					<option value="31">31</option>
				</select>
	        </div>
	        
	        <div style="float:right;height:50px;line-height:50px;margin-left:5px;margin-right:5px;">
	                月
	        </div>
	        
	        <div style="float:right;height:50px;line-height:30px;">
	            <select id="start_month" style="float:left;margin-top:11px;border:solid 1px #CCCCCC;padding:0px;line-height:30px;width:50px; height:30px;font-size:18px; ">
					<option value="01">01</option>
					<option value="02">02</option>
					<option value="03">03</option>
					<option value="04">04</option>
					<option value="05">05</option>
					<option value="06">06</option>
					<option value="07">07</option>
					<option value="08">08</option>
					<option value="09">09</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
				</select>
	        </div>
	        <div style="float:right;height:50px;line-height:50px;margin-left:5px;margin-right:5px;">
	                年
	        </div>
            <div style="float:right;height:50px;line-height:30px;">
	            <select id="start_year" style="float:left;margin-top:11px;border:solid 1px #CCCCCC;padding:0px;line-height:30px;width:80px; height:30px;font-size:18px; ">
					<option value="2016" >2016</option>
					<option value="2017" >2017</option>
					<option value="2018" >2018</option>
					<option value="2019" >2019</option>
					<option value="2020" >2020</option>
					<option value="2021" >2021</option>
					<option value="2022" >2022</option>
				</select>
	        </div>
	        <div style="float:right;height:50px;line-height:50px;margin-left:5px;margin-right:5px;">
	                日期：
	        </div>
	        
        </div>
    </div>
    <div id="center">
	    
	    <div style="margin-left:10px;margin-top:20px;margin-bottom:0px;font-size:22px;">营业收入统计
	        <span style="font-size:16px;margin-left:15px;">(  <%= start_date %>  至  <%= end_date %> )</span>
	        <span id="msg" style="display:none;font-size:26px;margin-left:55px;">正在查询中 ... 请稍候</span>
	    </div>
	    <div style="margin-left:10px;margin-top:5px;">
            <div class="fenge" style="float:left;8px;border:solid 1px #80397B;border-top:solid 2px #80397B;width:95%;height:0px;line-height:0px;"></div>
        </div>
        
        <div id="" style="float:left;">
	        <div style="background:#fff;height:40px;line-height:40px;margin-top:10px;margin-left:0px;position:relative;">
		        <div style="height:35px;line-height:35px;width:240px;color:#00B2F0;
		        	  font-size:22px;border-bottom:solid 1px #CCC;">
		            收款明细
		        </div>
	        </div>
	        <div style="background:#fff;width:240px;margin-left:0px;position:relative;color:#000;font-size:16px;padding-top:10px;">
	            <table id="recv_table" width='100%' cellspacing="0" style="border:solid 1px #CCCCCC;background:#fff;">
	               
	            </table>   
	            
	        </div>
	        
	        <div style="background:#fff;width:240px;height:40px;line-height:40px;margin-top:10px;margin-left:0px;position:relative;">
		        <div style="height:35px;line-height:35px;width:240px;color:#EC6F2C;
		        	  font-size:22px;border-bottom:solid 1px #CCC;">
		            楼层统计
		        </div>
	        </div>
	        <div style="background:#fff;width:240px;height:490px;margin-left:0px;position:relative;color:#000;font-size:16px;padding-top:10px;">
	            <table id="floor_data_table" width='100%' cellspacing="0" style="border:solid 1px #CCCCCC;background:#fff;">
	               
	            </table>   
	        </div>
	        
	    </div>
	    
	    
	    <div id="" style="float:left;margin-left:30px;margin-top:0px;">
	        <div style="background:#fff;width:240px;height:40px;line-height:40px;margin-top:10px;margin-left:0px;position:relative;">
		        <div style="height:35px;line-height:35px;width:240px;color:#8B0195;
		        	  font-size:22px;border-bottom:solid 1px #CCC;">
		            档口统计
		        </div>
	        </div>
	        <div style="background:#fff;width:240px;height:490px;margin-left:0px;position:relative;color:#000;font-size:16px;padding-top:10px;">
	            <table id="category_table" width='100%' cellspacing="0" style="border:solid 1px #CCCCCC;background:#fff;">
	               
	            </table> 
	        </div>
	    </div>
	    
	    <div id="" style="float:left;margin-left:30px;margin-top:0px;">
	        <div style="background:#fff;width:240px;height:40px;line-height:40px;margin-top:10px;margin-left:0px;position:relative;">
		        <div style="height:35px;line-height:35px;width:240px;color:#094AB2;
		        	  font-size:22px;border-bottom:solid 1px #CCC;">
		            收入统计
		        </div>
	        </div>
	        <div style="background:#fff;width:240px;height:490px;margin-left:0px;position:relative;color:#000;font-size:16px;padding-top:10px;">
	            <table width='100%' cellspacing="0" style="border:solid 1px #CCCCCC;background:#fff;">
	               <tr height="30" >
	                   <td width='120'><span style="margin-left:30px;">抹零：</span></td>
	                   <td >￥<span id="moling_money">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='120'><span style="margin-left:30px;">打折：</span></td>
	                   <td >￥<span id="discount_money">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='120'><span style="margin-left:30px;">舍弃：</span></td>
	                   <td >￥<span id="lose_money">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='140'><span style="margin-left:30px;">已结账单：</span></td>
	                   <td ><span id="close_count">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='140'><span style="margin-left:30px;">已结金额：</span></td>
	                   <td >￥<span id="recv_total">0</span></td>
	               </tr>
	                <tr height="30" >
	                   <td width='140'><span style="margin-left:30px;">未结账单：</span></td>
	                   <td ><span id="open_count">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='140'><span style="margin-left:30px;">未结金额：</span></td>
	                   <td >￥<span id="unrecv_money">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='140'><span style="margin-left:30px;">营业收入：</span></td>
	                   <td >￥<span id="hope_total_recv">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='140'><span style="margin-left:30px;">就餐人数：</span></td>
	                   <td ><span id="total_person">0</span></td>
	               </tr>
	               <tr height="30" >
	                   <td width='140'><span style="margin-left:30px;">人均消费：</span></td>
	                   <td >￥<span id="average_money">0</span></td>
	               </tr>
	               <tr height="30" style="display:none;" >
	                   <td width='140'><span style="margin-left:30px;">套餐销量：</span></td>
	                   <td ><span id="package_count">0</span> 份</td>
	               </tr>
	               <tr height="30" style="display:none;" >
	                   <td width='140'><span style="margin-left:30px;">套餐收入：</span></td>
	                   <td >￥<span id="package_money">0</span></td>
	               </tr>
	            </table>   
	            
	        </div>
	    </div>
        
        
        
    </div>
    
</div>
        
</body>
</html>
