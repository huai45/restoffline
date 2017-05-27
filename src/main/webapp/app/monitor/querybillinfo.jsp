<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%
User user = (User) request.getSession().getAttribute(CC.USER_CONTEXT);
IData info = user.getInfo();
IData bill = (IData)request.getAttribute("bill");
ut.p("bill:"+bill);
String result = (String)request.getAttribute("msg");
if(result.equals("false")){
    out.print("<div style='margin:250px 0px 0px 370px;font-size:20px;'>Sorry , 查询账单时好像出了点小问题</div>");
    return;
}
ut.log(bill);
if(bill==null){
    out.print("<div style='margin:250px 0px 0px 370px;font-size:20px;'>Sorry , 未查询到此账单信息</div>");
    return;
}
List packages = (List)bill.get("PACKAGELIST");
List items = (List)bill.get("ITEMLIST");
List recvfees = (List)bill.get("FEELIST");
String reopen_str="否";
if(bill.get("REOPEN")!=null&&bill.get("REOPEN").toString().equals("1")){
	reopen_str="是,激活时间："+bill.get("PARAM1").toString();
}
%>
<!DOCTYPE>
<html>
<head>
<title>账单明细信息</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css" />
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css" />
<style>
.bill_item{
    clear:both;height:36px;line-height:36px;font-size:14px;color:#444;border-bottom:solid 1px #999;background:#FFF;
}
.bill_item:hover{
    background:#F3F3F3;
}
</style>
<script type="text/javascript">
FinishBill = {};
printer = '<%= info.get("printer") %>';
//alert(printer);
rest_name='<%= info.get("rest_name") %>';
helloword='<%= info.get("helloword") %>';
address='<%= info.get("address") %>';
phone='<%= info.get("phone") %>';
</script>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script type="text/javascript">
flag = 0;
$(document).ready(function(){

    $(document).keydown(function(event){
        if(event.keyCode==8){
            return false;
        }
        if(event.keyCode==27){
            top.table_bill_window.hide();
        }
    });
    
    $('.start').click(function() {
	    var items = [];
	    var item = {};
	    item.item_id=$(this).attr("item_id");
	    items.push(item);
	    ajax_flag = 1;
	    $.post("/operation/startCook.html", { 
		        bill_id: $(this).attr("bill_id") ,
		        item_str : JSON.stringify(items)
	        }, function (result) {
				var obj = $.parseJSON(result);
				ajax_flag = 0;
				alert(obj.msg);
				if(obj.success=='true'){
			        $(".start[item_id="+item.item_id+"]").parent().html('已起菜');
				}
		}).error(function(){
		    alert("系统异常"); 
		});
	});
	
	$('.hurry').click(function() {
	    var items = [];
	    var item = {};
	    item.item_id=$(this).attr("item_id");
	    items.push(item);
	    ajax_flag = 1;
	    $.post("/operation/hurryCook.html", { 
		        bill_id: $(this).attr("bill_id") ,
		        item_str : JSON.stringify(items)
	        }, function (result) {
				var obj = $.parseJSON(result);
				ajax_flag = 0;
				alert(obj.msg);
				if(obj.success=='true'){
			        //alert(item.item_id);
			        //alert($(".hurry[item_id="+item.item_id+"]").parent().attr("style"));
			        $(".hurry[item_id="+item.item_id+"]").parent().html('已催菜');
				}
		}).error(function(){
		    alert("系统异常"); 
		});
	});
	
	$('.send').click(function() {
	    var items = [];
	    var item = {};
	    item.item_id=$(this).attr("item_id");
	    items.push(item);
	    ajax_flag = 1;
	    $.post("/operation/finishCook.html", { 
		        bill_id: $(this).attr("bill_id") ,
		        item_str : JSON.stringify(items)
	        }, function (result) {
				var obj = $.parseJSON(result);
				ajax_flag = 0;
				alert(obj.msg);
				if(obj.success=='true'){
				    $(".start[item_id="+item.item_id+"]").parent().html('&nbsp;');
				    $(".hurry[item_id="+item.item_id+"]").parent().html('&nbsp;');
			        $(".send[item_id="+item.item_id+"]").parent().html('已上菜');
				}
		}).error(function(){
		    alert("系统异常"); 
		});
	});
    
    	
});
</script>
</head>
<body style="">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
<div id="north" data-options="region:'north',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="height:87px;background-color:#FFF;position:relative;">
    <div style="height:36px;line-height:36px;width:100%;font-size:16x;color:#111;margin-top:5px;border-bottom:solid 1px #CCC;">
  	    <div style="float:left;margin-left:10px;font-size:18px;">账单明细信息</div>
    </div>
    <div style="margin-top:0px;border-top:solid 0px #ccc;border-bottom:solid 1px #ccc;background:#FFF;font-size:14px;">
        <table style="width:100%;" border="0" cellspacing="0" cellpadding="0" >
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:42px;line-height:42px;font-weight:bold;color:#444;">账单ID</span>
                </td>
                <td style="text-align:left;width:20%;">
                    <div id="" style="height:42px;line-height:42px;margin-left:0px;color:#666;" ><%= bill.get("BILL_ID") %></div>
                </td>
                <td style="text-align:right;">
                &nbsp;
                </td>
            </tr>
        </table>
    </div>
</div>

<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;font-size:14px;">
    <div style="display:none;background-color:#F3F3F3;height:60px;line-height:60px;margin-top:0px;border-bottom:solid 1px #999;text-align:center;">
        <table style="width:100%;" border="0" cellspacing="0" cellpadding="0" >
            <tr>
                <td>
                    <div id="" style="float:right;background:#FFF;border:solid 1px #007AFF;width:240px;height:32px;line-height:32px;cursor:pointer;
	                    margin-top:14px;border-radius:4px 0px 0px 4px;font-size:16px;color:#007AFF;text-align:center;margin-right:0px;font-weight:bold;">账单信息</div>
                </td>
                <td>
                    <div id="" style="float:left;background:#007AFF;width:240px;height:32px;line-height:32px;cursor:pointer;font-weight:bold;
	            margin-top:14px;border-radius:0px 4px 4px 0px;font-size:16px;color:#FFF;text-align:center;margin-right:20px;">菜品信息</div>
                </td>
            </tr>
        </table>
    </div>
    <div style="margin-top:0px;border-top:solid 0px #ccc;border-bottom:solid 1px #ccc;background:#FFF;font-size:14px;">
        <table style="width:100%;background:#FFF;" border="0" cellspacing="0" cellpadding="0" >
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">桌号</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" ><%= bill.get("TABLE_ID") %></span>
                </td>
            </tr>
            <tr style="display:none;">
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">开台员工</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" ><%= bill.get("OPEN_STAFF_NAME") %></span>
                </td>
            </tr>
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">就餐时间</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" ><%= bill.get("OPEN_TIME") %> - <%= bill.get("CLOSE_TIME") %></span>
                </td>
            </tr>
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">消费金额</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" >￥<%= ut.getBillFee(packages,items) %></span>
                </td>
            </tr>
        </table>
    </div>
    
    <div style="">
    	<div style="clear:both;text-align:center;height:36px;line-height:36px;font-size:18px;font-weight:bold;color:#777;border-bottom:solid 1px #999;">共 <%= items.size() %> 个菜品</div>
        <% if(items.size()>0){ 
        %>
	    <div style="clear:both;height:36px;line-height:36px;font-size:14px;color:#666;border-bottom:solid 1px #999;background-color:#F3F3F3;">
	        <div style="float:left;width:260px;padding-left:20px;">名称</div>
	        <div style="float:left;width:90px;text-align:center;">单价</div>
	        <div style="float:left;width:60px;text-align:center;">数量</div>
	        <div style="float:left;width:60px;text-align:center;">退菜</div>
	        <div style="float:left;width:60px;text-align:center;">方式</div>
	        <div style="float:left;width:80px;text-align:center;">起菜时间</div>
	        <div style="float:left;width:80px;text-align:center;">催菜时间</div>
	        <div style="float:left;width:80px;text-align:center;">上菜时间</div>
	        <div style="float:left;width:80px;text-align:center;">点菜员</div>
	    </div>
	    <% }%>
	    <div style="margin-bottom:20px;">
	        <% for(int i=0;i<items.size();i++){ 
		           Map item = (Map)items.get(i);
	               ut.log( item);
	               String food_name = item.get("FOOD_NAME").toString();
	               if(!item.get("PACKAGE_ID").toString().trim().equals("")){
	            	   food_name = food_name+" [套] ";
	               }
	               if(!item.get("NOTE").toString().trim().equals("")){
	            	   food_name = food_name+" ("+item.get("NOTE")+")";
	               }
	               String start_time = ""+item.get("START_TIME");
	               System.out.println(" start_time = 1"+start_time+"1");
	               if(start_time.equals("")){
	            	   start_time="-";
	               }else if(start_time.length()>=19)  {
	            	   start_time = start_time.substring(10,19);
	               }
	               String hurry_time = ""+item.get("HURRY_TIME");
	               System.out.println(" hurry_time = 1"+hurry_time+"1");
	               if(hurry_time.equals("")){
	            	   hurry_time="-";
	               }else if(hurry_time.length()>=19) {
	            	   hurry_time = hurry_time.substring(10,19);
	               }
	               String end_time = ""+item.get("END_TIME");
	               System.out.println(" end_time = 1"+end_time+"1");
	               if(end_time.equals("")){
	            	   end_time="-";
	               }else if(end_time.length()>=19){
	            	   end_time = end_time.substring(10,19);
	               }
            %>
	        <div class="bill_item" style="">
		        <div style="float:left;width:260px;padding-left:20px;"><%= food_name %></div>
		        <div style="float:left;width:90px;text-align:center;">￥<%= item.get("PRICE") %>/<%= item.get("UNIT") %></div>
		        <div style="float:left;width:60px;text-align:center;"><%= item.get("COUNT") %></div>
		        <div style="float:left;width:60px;text-align:center;"><%= item.get("BACK_COUNT") %></div>
		        <div style="float:left;width:60px;text-align:center;"><%= ut.callType(item.get("CALL_TYPE")) %></div>
		        <div style="float:left;width:80px;text-align:center;"><%= start_time %>&nbsp;</div>
		        <div style="float:left;width:80px;text-align:center;"><%= hurry_time %>&nbsp;</div>
		        <div style="float:left;width:80px;text-align:center;"><%= end_time %>&nbsp;</div>
		        <div style="float:left;width:80px;text-align:center;"><%= item.get("OPER_STAFF_NAME") %></div>
		        <div style="float:left;width:80px;text-align:center;">
		        <% if(item.get("CALL_TYPE").toString().equals("0")&&bill.get("PAY_TYPE").equals("0")&&start_time.equals("-")&&end_time.equals("-")){ %>
		            <div class="start" item_id="<%= item.get("ITEM_ID") %>" bill_id="<%= item.get("BILL_ID") %>" style="background:#FFF;border:solid 1px #007AFF;width:60px;height:26px;line-height:26px;cursor:pointer;
                        margin:4px 0px 0px 9px;border-radius:4px;font-size:12px;color:#007AFF;text-align:center;" onselectstart='return false'>起菜</div>
		        <% }else {out.print("&nbsp;");} %>
		        </div>
	            <div style="float:left;width:80px;text-align:center;">
	            <% if(bill.get("PAY_TYPE").equals("0")&&!start_time.equals("-")&&end_time.equals("-")){ %>
		            <div class="hurry" item_id="<%= item.get("ITEM_ID") %>" bill_id="<%= item.get("BILL_ID") %>" style="background:#FFF;border:solid 1px #007AFF;width:60px;height:26px;line-height:26px;cursor:pointer;
                        margin:4px 0px 0px 9px;border-radius:4px;font-size:12px;color:#007AFF;text-align:center;" onselectstart='return false'>催菜</div>
		        <% }else {out.print("&nbsp;");} %>
	            </div>
	            <div style="float:left;width:80px;text-align:center;">
	            <% if(bill.get("PAY_TYPE").equals("0")&&end_time.equals("-")){ %>
		            <div class="send" item_id="<%= item.get("ITEM_ID") %>" bill_id="<%= item.get("BILL_ID") %>" style="background:#FFF;border:solid 1px #CC2B56;width:60px;height:26px;line-height:26px;cursor:pointer;
                        margin:4px 0px 0px 9px;border-radius:4px;font-size:12px;color:#CC2B56;text-align:center;" onselectstart='return false'>上菜</div>
		        <% }else {out.print("&nbsp;");} %>
	            </div>
		    </div>
		    <% }%>
	    </div>
    </div>
</div>
</body>
</html>
