<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%
User user = (User) request.getSession().getAttribute(CC.USER_CONTEXT);
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
String pay_type = bill.get("PAY_TYPE").toString();
String recv_str = "";
for(int i=0;i<recvfees.size();i++){
	Map item = (Map)recvfees.get(i);
	recv_str = recv_str +item.get("MODE_NAME")+"：￥"+item.get("FEE")+"; ";
}
%>
<!DOCTYPE>
<html>
<head>
<title>账单明细信息</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css">
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css">
<style>
	
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script src="/app/operation/js/func.js"></script>
<script type="text/javascript">
ajax_flag = 0;
socketurl = "<%= user.getParam().getString("PARAM3") %>";
default_printer = "<%= user.getParam().getString("PARAM4") %>";
$(document).ready(function(){

    $(document).keydown(function(event){
        if(event.keyCode==8){
            return false;
        }
        if(event.keyCode==27){
            window.close();
        }
    });
    
	$('#openBillBtn').click(function() {
	    if(ajax_flag > 0){
	        return false;
	    }
	    ajax_flag = 1;
	    $.post("/operation/reopenBill.html", { bill_id : '<%= bill.get("BILL_ID") %>' }, function (result) {
			var obj = $.parseJSON(result);
			ajax_flag = 0;
			if (obj.success == "true" ) {
			    $('#openBillBtn').hide();
			    $('#printHistoryBillBtn').hide();
			}else{
                
			}
			alert(obj.msg);
		}).error(function(){
		    alert("系统异常"); 
		    ajax_flag = 0;
		});
	});
	
	$('#printHistoryBillBtn').click(function() {
        printBillByBillId("<%= bill.get("BILL_ID") %>");
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
                    <div id="printHistoryBillBtn"  style="float:right;background:#FFF;border:solid 1px #007AFF;width:110px;height:26px;line-height:26px;cursor:pointer;
                        margin-top:0px;border-radius:4px;font-size:12px;color:#007AFF;text-align:center;margin-right:30px;" onselectstart='return false'>补打账单</div>

                    <div id="openBillBtn" style="float:right;background:#FFF;border:solid 1px #CC2B56;width:110px;height:26px;line-height:26px;cursor:pointer;
                        margin-top:0px;border-radius:4px;font-size:12px;color:#CC2B56;text-align:center;margin-right:30px;" onselectstart='return false'>激活账单</div>
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
            <tr style="display:none;">
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">消费日期</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" ><%= bill.get("OPEN_DATE") %></span>
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
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">是否激活过</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" ><%= reopen_str %>    </span>
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
            <tr style="display:none;">
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">押金</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" >￥0</span>
                </td>
            </tr>
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">抹零</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" >￥<%= bill.get("REDUCE_FEE") %></span>
                </td>
            </tr>
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">打折金额</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" >￥<%= ut.getRateFee(items) %></span>
                </td>
            </tr>
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">已收金额</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" >￥<%= bill.get("RECV_FEE") %></span>
                </td>
            </tr>
            <tr>
                <td style="text-align:left;width:25%;">
                    <span style="margin-left:45px;height:36px;line-height:36px;font-weight:bold;color:#444;">收款明细</span>
                </td>
                <td style="text-align:left;">
                    <span id="" style="height:36px;line-height:36px;margin-left:0px;color:#666;" ><%= recv_str %></span>
                </td>
            </tr>
        </table>
    </div>
    
    <div style="">
    	<div style="clear:both;text-align:center;height:36px;line-height:36px;font-size:18px;font-weight:bold;color:#777;border-bottom:solid 1px #999;">共 <%= items.size() %> 个菜品</div>
        <% if(items.size()>0){ 
        %>
	    <div style="clear:both;height:36px;line-height:36px;font-size:14px;color:#666;border-bottom:solid 1px #999;background-color:#F3F3F3;">
	        <div style="float:left;width:400px;padding-left:20px;">名称</div>
	        <div style="float:left;width:100px;text-align:center;">单价</div>
	        <div style="float:left;width:80px;text-align:center;">数量</div>
	        <div style="float:left;width:80px;text-align:center;">退菜</div>
	        <div style="float:left;width:80px;text-align:center;">赠送</div>
	        <div style="float:left;width:80px;text-align:center;">打折</div>
	        <div style="float:left;width:80px;text-align:center;">方式</div>
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
            %>
	        <div style="clear:both;height:36px;line-height:36px;font-size:14px;color:#444;border-bottom:solid 1px #999;">
		        <div style="float:left;width:400px;padding-left:20px;"><%= food_name %></div>
		        <div style="float:left;width:100px;text-align:center;">￥<%= item.get("PRICE") %>/<%= item.get("UNIT") %></div>
		        <div style="float:left;width:80px;text-align:center;"><%= item.get("COUNT") %></div>
		        <div style="float:left;width:80px;text-align:center;"><%= item.get("BACK_COUNT") %></div>
		        <div style="float:left;width:80px;text-align:center;"><%= item.get("FREE_COUNT") %></div>
		        <div style="float:left;width:80px;text-align:center;"><%= item.get("PAY_RATE") %>%</div>
		        <div style="float:left;width:80px;text-align:center;"><%= ut.callType(item.get("CALL_TYPE")) %></div>
		        <div style="float:left;width:80px;text-align:center;"><%= item.get("OPER_STAFF_NAME") %></div>
		    </div>
		    <% }%>
	    </div>
    </div>
</div>
</body>
</html>
