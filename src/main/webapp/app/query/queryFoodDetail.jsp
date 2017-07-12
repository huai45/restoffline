<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");

String food_id = request.getParameter("food_id");
if(food_id==null){
	food_id="";
}

String food_name = request.getParameter("food_name");
if(food_name==null){
	food_name="";
}
String start_date = request.getParameter("start_date");
String end_date = request.getParameter("end_date");
String show_start_date = start_date;
String show_end_date = end_date;
if(start_date==null){
	start_date = ut.currentDate(-1);
	show_start_date = ut.currentDate(-1);
}
if(end_date==null){
	end_date = ut.currentDate(-1);
	show_end_date = ut.currentDate(-1);
}


String sql = " select oper_staff_name , price , ROUND(sum(count),2) count, ROUND(sum(back_count),2) back, ROUND(sum(count-back_count),2) real_count, ROUND(sum(price*(count-back_count-free_count)*pay_rate/100),2) real_total "+ 
  " from th_bill_item a where   food_id = ?   and oper_time >= ? and oper_time <= ? group by oper_staff_name ,price  order by count DESC ; ";
ut.log(" sql :" +sql);
List details = new ArrayList();
if(!start_date.equals("")&&!end_date.equals("")&&!food_id.equals("")){
	details = jdbcTemplate.queryForList(sql,new Object[]{food_id,start_date+" 00:00:00",end_date+" 23:59:59"});
}
List staffList = jdbcTemplate.queryForList(" select username user_id,username from td_android_user where 1 = 1 ",
    new Object[]{} );

%>
<!DOCTYPE>
<html>
<head>
<title>个人销售查询</title>
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
<script src="/resource/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
$(document).ready(function(){
     
     $("#queryFoodDetail").addClass("left_menu_sel");
     
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
	
	$("#queryBtn").click(function(){
	    var food_id = $('#food_id').combobox("getValue");
	    var food_name = $("#food_id").combobox("getText");
	    var start_date = $('#start_date').val();
	    var end_date = $('#end_date').val();
        document.location.href="/transPage?page=/query/queryFoodDetail&start_date="+start_date+"&end_date="+end_date+"&food_id="+food_id+"&food_name="+food_name;
	});
	
	$('#food_id').combobox({
		filter: function(q, row){
			var opts = $(this).combobox('options');
			return row[opts.textField].indexOf(q) == 0;
		}
	});
	
	$('#food_id').combobox("setValue","<%= food_id %>");
	
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

            <div style="float:left;height:50px;line-height:50px;margin-left:10px;margin-right:15px;">
	                菜品
	        </div>
            <div style="float:left;height:50px;line-height:30px;margin-right:15px;margin-top:15px;">
	            <input id="food_id" class="easyui-combobox" name="food_id" style="width:200px;"
                    data-options="valueField:'food_id',textField:'food_name',url:'/app/query/get_food.jsp'">
	        </div>
            <div style="float:left;height:50px;line-height:50px;margin-left:5px;margin-right:5px;">
	                日期：
	        </div>
            <div style="float:left;height:50px;line-height:30px;margin-top:15px;">
	            <input type="text" id="start_date" size= "10"  onClick="WdatePicker()" readonly="readonly" value="<%=start_date%>" onchange="" />
	        </div>
	        <div style="float:left;height:50px;line-height:50px;margin-left:10px;margin-right:10px;">
	                至
	        </div>
	        <div style="float:left;height:50px;line-height:30px;margin-top:15px;">
	            <input type="text" id="end_date" size= "10"  onClick="WdatePicker()" readonly="readonly" value="<%=end_date%>" onchange="" />
	        </div>
            <div id="" style="float:left;">
		        <div id="queryBtn" align="center" class="btn" style="float:left;background:#80397B;color:#FFFFFF;margin-top:8px;margin-left: 20px; " onselectstart='return false'> 查询 </div>
	        </div>

        </div>
    </div>
    
    <div id="center">
	    
	    <div style="margin-left:10px;margin-top:20px;margin-bottom:0px;font-size:22px;"><%= "菜品("+food_name+")销售查询" %>
	        <span style="font-size:16px;margin-left:15px;">(  <%= start_date %>  至  <%= end_date %> )</span>
	    </div>
	    <div style="margin-left:10px;margin-top:5px;">
            <div class="fenge" style="float:left;8px;border:solid 1px #80397B;border-top:solid 2px #80397B;width:1000px;height:0px;line-height:0px;"></div>
        </div>
        <div class="audit_list" style="width:930px;">
            <div class="order_head" style="">
	            <div style="float:left;padding-top:1px;width:60px;text-align:center;" >
	                  序号
	            </div>
	            <div style="width:200px;" >销售人员</div>
	            <div style="width:100px;" >单价(元)</div>
	            <div style="width:100px;" >销售数量</div>
	            <div style="width:100px;" >退菜数量</div>
	            <div style="width:100px;" >实际销售数量</div>
	            <div style="width:240px;" >实际销售额 (元) : 去掉赠送和折扣</div>
	        </div>
	        <div id="show_list">
            <% 
                double total1 = 0;
                double total2 = 0;
                for(int i=0;i<details.size();i++){
                   Map staffData = (Map)details.get(i);
                   total2 = total2 + Double.parseDouble(staffData.get("real_total").toString());
            %>
            <div class="order_tr" id="<%= staffData.get("oper_date") %>" >
                <div style="float:left;padding-top:1px;width:60px;text-align:center;" >
	                <%= i+1 %>
	            </div>
	            <div class="bluefont1 bluefont" style="width:200px;" ><%= staffData.get("oper_staff_name") %></div>
	            <div style="width:100px;" ><%= staffData.get("price") %></div>
	            <div style="width:100px;" ><%= staffData.get("count") %></div>
	            <div style="width:100px;" ><%= staffData.get("back") %></div>
	            <div style="width:100px;" ><%= staffData.get("real_count") %></div>
	            <div style="width:240px;" ><%= staffData.get("real_total") %>&nbsp;</div>
	        </div>
	        <% } %>
	        
	        <div class="order_tr" >
                <div style="float:left;padding-top:1px;width:60px;text-align:center;" >
	                &nbsp;
	            </div>
	            <div class="bluefont1" style="width:200px;color:#000;font-weight:bold;" ><%= "合计" %></div>
	            <div style="width:100px;" >&nbsp;</div>
	            <div style="width:100px;" >&nbsp;</div>
	            <div style="width:100px;" >&nbsp;</div>
	            <div style="color:#000;" >&nbsp;</div>
	            <div style="width:240px;color:#000;" ><%= ut.getDoubleString(total2) %>&nbsp;</div>
	        </div>
	        
	        </div>
        </div>
        <div style="clear:both;"></div>
        <div style="height:30px;">&nbsp;</div>
    </div>
    
</div>
        
</body>
</html>
