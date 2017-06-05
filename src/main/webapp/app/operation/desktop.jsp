<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.service.*"%>
<%@ page language="java" import="net.sf.json.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
String rest_id = user.getRestId();
String today = ut.currentDate();
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
List tables = jdbcTemplate.queryForList(" select * from td_table where rest_id = ? and use_tag= '1' order by 0+table_id ",new Object[]{rest_id});
ut.p("tables = "+tables.size());
JSONArray ja = JSONArray.fromObject(tables);

List foods = jdbcTemplate.queryForList(" select * from td_food where rest_id = ? and use_tag= '1' order by food_id ",new Object[]{rest_id});
ut.p("foods = "+foods.size());

List printers = jdbcTemplate.queryForList(" select * from td_printer where rest_id = ? and state= '1' order by printer ",new Object[]{rest_id});
ut.p("printers = "+printers.size());

%>
<!DOCTYPE html>
<html>
<head>
<title>RestOn收银系统</title>
<meta http-equiv="keywords" content="RestOn">
<meta http-equiv="description" content="RestOn">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/resource/ext4/resources/css/ext-all.css" />
<link rel="stylesheet" href="/resource/ext4/resources/css/ext-all-gray.css" />
<link rel="stylesheet" href="/app/operation/css/top_north.css">
<link rel="stylesheet" href="/app/operation/css/desktop.css">
<style>

</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/ext4/ext-all.js"></script>
<script>
var tables = <%= ja.toString() %>;
//alert(tables.length);
var floors = [];
height=0;
window_tag=0;
rest_id="<%= rest_id %>";
table_patrn=/^[0-9]{1,4}$/; 
food_patrn=/^[a-zA-Z]{1,20}$/;
ajax_flag = 0;

printtaskurl = "<%= user.getParam().getString("PARAM1") %>";
printtasktime = <%= user.getParam().getString("PARAM2") %>;
socketurl = "<%= user.getParam().getString("PARAM3") %>";
default_printer = "<%= user.getParam().getString("PARAM4") %>";

appid="<%= user.getInfo().getString("APPID") %>";
queryfoodurl="<%= user.getParam().getString("PARAM9") %>";
querybillurl="<%= user.getParam().getString("PARAM8") %>";
querybillinfourl="<%= user.getParam().getString("PARAM7") %>";
backurl="";
ftime=<%= user.getParam().getString("PARAM5") %>;
btime=<%= user.getParam().getString("PARAM6") %>;
querytabletime=<%= user.getParam().getString("PARAM10") %>;

cardsocketurl = "<%= user.getParam().getString("PARAM11") %>";
     
</script> 
<script src="/app/operation/js/start.js"></script>
<script src="/app/operation/js/func.js"></script>
<script src="/app/operation/js/layout.js"></script>
<script src="/app/operation/js/desktop.js"></script>
<script src="/app/operation/js/opentable.js"></script>
<script src="/app/operation/js/addfood.js"></script>

<script src="/app/operation/js/event.js"></script>
<script src="/app/operation/js/task.js"></script>
<script>
Ext.onReady(function(){
    
    //alert(11);
    
});
</script>  
</head>
<body id="body">

<div id="deskpage_north" class="" style="display:none;">
    <div id="logo" style="font-family:Microsoft Yahei;">收银台</div>
    <div  style="font-family:Microsoft Yahei;margin-left:50px;float:left;line-height:40px;color:#FFF;font-size:14px;"><%= today %></div>
    <div id="logoutBtn" class="titleBtn" style="font-family:Microsoft Yahei;">退出</div>
    <div id="username" style="font-family:Microsoft Yahei;"><%= user.getStaffname() %></div>
    <div id="settingBtn" class="titleBtn" style="font-family:Microsoft Yahei;">设置</div>
    <div id="waterBtn" class="titleBtn" style="font-family:Microsoft Yahei;">酒水</div>
    <div id="todayBtn" style="font-family:Microsoft Yahei;">今日</div>
</div>

<div id="deskcenter_north" class="" style="display:none;">
	<div style="margin:20px;margin-left:100px;" >
		<div style="position:relative;height:46px;width:500px;background:#FFF;">
			<input id="smart_str" type="text" placeholder="智能搜索" style="position:absolute;height:46px;width:500px;font-size:20px;padding-left:15px;top:0px;left:0px;" autocomplete="off" />
			<image id="smartBtn" src="/app/operation/image/wh.jpg" style="position:absolute;height:42px;width:42px;top:1px;right:2px;cursor:pointer;" />
		</div>
	</div>
</div>
<div id="tablepanelpage_center" class="" style="display:none;">
    <div id="floor_table" style="margin:0px;padding:0px;margin-left:60px;margin-right:20px;border:solid 0px #FFF;">&nbsp;
    
    </div>
</div>


<div id="quicktablepage_center" class="" style="display:none;">
    <div  style="margin-top:30px;margin-left:100px;margin-bottom:30px;color:#EFEFEF;font-size:14px;">
        找到<span id="table_count" style="padding-left:5px;padding-right:5px;">0</span>个台位
    </div>
    
    <div id="smartTableList" style="margin-top:10px;margin-left:80px;float:left;width:260px;border:solid 0px #FFF;">
        <% for(int i=0;i<tables.size();i++){
		       Map table = (Map)tables.get(i);
		%>
		<div class="smart_table" 
		     table_id="<%=table.get("TABLE_ID").toString().trim()%>" 
		     floor="<%=table.get("FLOOR").toString().trim()%>" 
		     table_name="<%=table.get("TABLE_NAME").toString().trim()%>" 
		     state="<%=table.get("STATE").toString().trim()%>" 
		     size="<%=table.get("SIZE").toString().trim()%>" 
		     bill_id="<%=table.get("BILL_ID").toString().trim()%>" 
		     printer="<%=table.get("PRINTER").toString().trim()%>">
		     <div style="line-height:100px;font-size:20px;color:#FFF;">桌号</div>
		     <div style="line-height:100px;color:#FFF;"><%=table.get("TABLE_ID").toString().trim()%></div>
		</div>
		<% }%>
		&nbsp;
    </div>
    
    <div id="buttonList" style="display:none;float:left;margin:40px;margin-top:0px;">
        <div  style="margin-top:10px;margin-left:20px;color:#EFEFEF;font-size:14px;">常用功能<span style="padding-left:30px;">(按空格键切换)</span></div>
	    <div>
	        <div id="quickTableInfoBtn" class="bigbutton bigbutton_sel" index="1" >进入</div>
	        <div id="quickPayCashBtn" class="bigbutton" index="2" >收现金(shift)</div>
	        <div id="quickPayByCardBtn" class="bigbutton" index="3" >刷卡</div>
        </div>
        <div style="clear:both;margin-top:10px;">&nbsp;</div>
        <div  style="margin-top:10px;margin-left:20px;color:#EFEFEF;font-size:14px;">其他功能</div>
        <div id="quickPrintBillBtn" class="bigbutton" index="11" >打单(ctrl)</div>
        <div id="quickAddFoodBtn" class="bigbutton" index="12" >加菜(a~z)</div>
        <div id="quickQueryBillBtn" class="bigbutton" index="13" >今日账单</div>
        
    </div>
    <div style="clear:both;width:1000px;height:20px;margin-left:100px;border-bottom:solid 1px #999;">&nbsp;</div>
    <div style="clear:both;margin-bottom:40px;"></div>

</div>

<!-- 开台窗口    start  -->
<div id="openTablePage" style="display:none;">
    <div class="page_head" style="">
	</div>
	<div id="openTablePageCenter" style="width:400px;height:300px;background:#ECECEC;margin-left:auto;margin-right:auto;" onselectstart='return false'>
	    <div style="height:50px;line-height:50px;width:100%;background:#DDDDDD;text-align:center;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
	        桌号 ： <span id="open_table_name" style=""></span>
	    </div>
	    <div style="font-size:16px;margin-left:20px;margin-top:40px;">请输入就餐人数</div>
	    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:30px;
	        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
		    <form id="open_table_form" >
	        <input id="open_table_id" type="hidden" value=""  />
	        <input  type="submit" value="" style="position:absolute;top:-9999px;"  />
		    <input id="open_table_nopinput" type="text" value="" style="width:300px;height:40px;line-height:40px;
		          font-size:26px;margin:0px;margin-left:20px;border:solid 0px #C0C0C0;" autocomplete="off"/>
	        </form>
	    </div>
	    <div style="margin-top:74px;height:50px;line-height:50px;background:#3366FF;">
	        <div id="backBtnFromOpenTableBtn" style="float:left;height:50px;line-height:50px;width:199px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">取消</div>
	        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
	        <div id="openTableBtn" style="float:left;height:50px;line-height:50px;width:199px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">开台</div>
	    </div>	
	</div>

</div>
<!-- 开台窗口    end  -->

<!-- 餐台信息页面    start  -->
<div id="tableinfo_head" style="display:none;">
	<div id="backBtnFromTableInfo" style="float:right;height:46px;line-height:46px;text-align:center;width:64px;cursor:pointer;color:#FFF;">关闭</div>
    
    <div  class="" style="float:right;height:20px;line-height:20px;margin-top:13px;margin-left:15px;margin-right:5px;width:0px;border-right:solid 1px #FFF;">
    </div>
    
    <div id="addTempFoodBtn" onselectstart='return false' style="float:right;height:46px;line-height:46px;text-align:center;width:74px;cursor:pointer;color:#FFF;">
    	临时菜
    </div>
    <div id="packageBtn" onselectstart='return false' style="display:none;float:right;height:46px;line-height:46px;text-align:center;width:64px;cursor:pointer;color:#FFF;">
    	套餐
    </div>
    <div id="addFoodBtn" onselectstart='return false' style="float:right;height:46px;line-height:46px;text-align:center;width:64px;cursor:pointer;color:#FFF;">
    	点菜
    </div>
    
    <div id="allpick" onselectstart='return false' style="float:right;height:46px;line-height:46px;text-align:center;width:64px;cursor:pointer;color:#FFF;">全选</div>
    
    <div  class="" style="float:right;height:20px;line-height:20px;margin-top:13px;margin-left:15px;margin-right:15px;width:0px;border-right:solid 1px #FFF;">
    </div>
    
    <div id="changeFoodTableBtn" class="bill_item_menu" onselectstart='return false'>
    	转台
    </div>
    <div id="hurryCookFoodBtn" class="bill_item_menu" onselectstart='return false'>
    	催菜
    </div>
    <div id="startCookFoodBtn" class="bill_item_menu" onselectstart='return false'>
    	起菜
    </div>
    <div id="derateFoodBtn" class="bill_item_menu" onselectstart='return false'>
    	打折
    </div>
    <div id="presentFoodBtn" class="bill_item_menu" onselectstart='return false'>
    	赠送
    </div>
    <div id="cancelFoodBtn" class="bill_item_menu" onselectstart='return false'>
    	退菜
    </div>
    
    <div style="display:none;float:right;height:46px;line-height:46px;margin-right:10px;
        color:#EEE;font-size:16Px;">
        已选定  <span id="select_item_count" ></span>  个菜品 , 请选择 ：
    </div>
    <div  class="bill_item_menu" style="float:right;height:20px;line-height:20px;margin-top:13px;margin-left:15px;margin-right:25px;width:0px;border-right:solid 1px #FFF;">
    </div>
</div>
<div id="tableinfo_east" style="display:none;">
    <div style="background-color:#FFF;height:330px;width:260px;margin:5px;border:solid 1px #CCC;font-family:'Microsoft YaHei';">
        <div class="billitem" style="">当前桌号  ：    <span id="tableinfo_table_id"></span></div>
        <div class="billitem" style="">账单流水  ：    <span id="tableinfo_bill_id"></span></div>
        <div class="billitem" style="display:none;">人数  ：    <span id="tableinfo_nop"></span></div>
        <div class="billitem" style="display:none;">班次  ：    <span id="tableinfo_ban"></span></div>
        <div class="billitem" style="">开台时间  ：    <span id="tableinfo_start_time"></span></div>
        <div class="billitem" style="display:none;">发票  ：    <span id="tableinfo_invoice"></span></div>
        <div class="billitem" style="">账单金额  ：    ￥<span id="tableinfo_bill_fee"></span></div>
        <div class="billitem" style="">打折金额  ：    ￥<span id="tableinfo_discount_fee"></span></div>
        <div class="billitem" style="">抹零金额  ：    ￥<span id="tableinfo_reducefee"></span></div>
        <div class="billitem" style="">应收金额  ：    ￥<span id="tableinfo_spay_fee"></span></div>
        <div class="billitem" style="">已收金额  ：    <span id="tableinfo_recvfeestr"></span></div>
    </div>
    <div class="tablebtn_group">
        <div id="paycashBtn" class="tablebtn">现金</div>
        <div class="tablebtn_split"></div>
        <div id="payByCardBtn" class="tablebtn">刷卡</div>
        <div class="tablebtn_split"></div>
        <div id="payByChequePageBtn" class="tablebtn">支票</div>
    </div>
    <div class="tablebtn_group">
        <div id="payByVipCardPageBtn" class="tablebtn">会员</div>
        <div class="tablebtn_split"></div>
        <div id="payByCreditUserPageBtn" class="tablebtn">挂账</div>
        <div class="tablebtn_split"></div>
        <div id="reducefeeBtn" class="tablebtn">抹零</div>
    </div>
    <div class="tablebtn_group">
        <div id="printQueryBillBtn" class="tablebtn">打印</div>
        <div class="tablebtn_split"></div>
        <div id="payByWechatBtn" class="tablebtn"><span style="">微信</span></div>
        <div class="tablebtn_split"></div>
        <div id="invoiceBtn" class="tablebtn"><span style="display:none;">开票</span></div>
    </div>
    
    <div id="closeBillBtn" style="background-color:#C42140;height:45px;line-height:45px;width:260px;font-size:16px;text-align:center;
        margin:20px 5px 0px 5px;border:solid 0px #CCC;color:#fff;border-radius:0px;cursor:pointer;font-family:'Microsoft YaHei';">封单（Del）
    </div>
    <input id="tableinfo_hidden_input" type="text" class="" value="" autocomplete="off" />
</div>
<div id="tableinfo_center" style="display:none;">
   <div id="billItemList" style="margin-top:10px;margin-left:20px;"></div>
   <div style="clear:both;margin-top:10px;">&nbsp;</div>
</div>
<div id="tableinfo_south">

</div>
<!-- 餐台信息页面    end  -->

<!-- 点菜页面    start  -->
<div id="addfood_head" style="display:none;">
    <div id="backBtnFromAddFood" class="titleBackBtn">返回</div>
    <div  class="titleBackBtn" style="width:300px;margin-left:100px;color:#FFF;">当前桌号 ：<span id="addfood_table_name"></span></div>
    <div id="all_jiaoqi" class="top_nemu" style="float:right;padding-left:20px;padding-right:20px;height:45px;line-height:45px;
        text-align:center;cursor:pointer;color:#FFF;">全部叫起</div>
    <div id="all_jiqi" class="top_nemu" style="float:right;padding-left:20px;padding-right:20px;height:45px;line-height:45px;
        text-align:center;cursor:pointer;color:#FFF;">全部即起</div>
</div>
<div id="foodpanel_head" style="display:none;">
    <div style="background-color:#F3F3F3;height:60px;line-height:60px;margin-top:0px;border-bottom:solid 1px #999;">
        <span style="margin-left:20px;font-size:16px;">搜索</span><input id="query_food_str" name="query_food_str" type="text" autocomplete="off" style="padding-left:5px;border:solid 1px #DDD;width:240px;height:40px;line-height:40px;margin-top:9px;margin-left:20px;font-size:24px;font-weight:normal;color:#333;" value=""  autocomplete="off"/>
    </div>
</div>
<div id="addfoodwest_center">
	  <div id="foodlist" style="padding:10px;" onselectstart="return false" ></div>

</div>
<div id="selectlist" style="display:none;"></div>
<div id="hiddenlist" style="display:none;">
        <% for(int i=0;i<foods.size();i++){ 
		        Map food = (Map)foods.get(i);
		 %>
		<div id="food_<%= food.get("FOOD_ID") %>" class="food" food_id="<%= food.get("FOOD_ID") %>" food_name="<%= food.get("FOOD_NAME") %>" 
		     abbr="<%= food.get("ABBR") %>" unit="<%= food.get("UNIT") %>" show_tag="<%= food.get("SHOW_TAG") %>" printer="<%= food.get("PRINTER") %>"
		     price="<%= food.get("PRICE") %>" category="<%= food.get("CATEGORY") %>" groups="<%= food.get("GROUPS") %>" 
		     print_count="<%= food.get("PRINT_COUNT") %>" style="" >
		     <span class="foodname">
		          <%= food.get("FOOD_NAME") %>
		     </span>
		     <span class="foodprice">￥<%= food.get("PRICE") %>/<%= food.get("UNIT") %></span>
		</div>
		<% } %>
</div>
<div id="addfood_center" style="display:none;">
    <div style="background-color:#F3F3F3;height:60px;line-height:60px;margin-top:0px;border-bottom:solid 1px #999;">
        <span style="margin-left:20px;font-size:16px;">已点</span><span id="select_food_count" style="margin-left:10px;margin-right:10px;">0</span><span style="font-size:16px;">个菜品，</span><span style="margin-left:20px;font-size:16px;">合计：&nbsp;￥</span><span id="select_food_money">0</span>
        <div id="addFoodSubmitBtn" style="float:right;background:#007AFF;width:140px;height:40px;line-height:40px;cursor:pointer;
            margin-top:10px;font-size:16px;color:#FFF;text-align:center;margin-right:20px;">下单</div>
        <div id="add_note" style="float:right;background:#007AFF;width:140px;height:40px;line-height:40px;cursor:pointer;
            margin-top:10px;font-size:16px;color:#FFF;text-align:center;margin-right:50px;">加整单备注</div>
        <input id="addfood_total_note" type="hidden" value=""/>
    </div>
    <div id="title11" style="clear:both;height:50px;line-height:50px;min-width:900px;font-size:14px;color:#666;border-bottom:solid 1px #999;background-color:#F3F3F3;">
        <div class="addfoodname">名称</div>
        <div class="addfoodprice">单价</div>
        <div style="float:left;width:80px;height:50px;line-height:50px;color:#111;text-align:center;">数量</div>
        <div style="float:left;margin-left:40px;height:50px;line-height:50px;width:70px;color:#111;text-align:center;">方式</div>
        <div style="float:left;margin-left:40px;height:50px;line-height:50px;width:80px;color:#111;text-align:center;">操作</div>
    </div>	
	<div id="addfoodlist"></div>
	 
</div>
<div id="addfood_south" style="display:none;">
    <div id="addfood_bill_id" style="display:none;"></div>
</div>
<!-- 点菜页面    end  -->


<!-- 备注信息展示窗口    begin  -->
<div id="food_note_center" style="display:none;color:#111;" >
<form id="note_form" >
	    <div style="height:50px;line-height:50px;width:100%;background:#DDDDDD;text-align:center;
	    	  border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
	        添加备注
	    </div>
	    <div id="clearNoteInputBtn" style="position:absolute;top:48px;right:20px;height:30px;line-height:30px;width:100px;text-align:center;
	        	  margin:20px 0px 0px 20px;background:#CB4437;color:#FFF;cursor:pointer;font-size:16px;" onselectstart='return false'>清空备注</div>
	    <div style="font-size:16px;margin-left:20px;margin-top:20px;">请输入备注信息</div>
	    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:20px;
	        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
		      
	        <input id="pagefrom" type="hidden" value=""  />
	        <input  type="submit" value="" style="position:absolute;top:-9999px;"  />
		      <input id="note_input" type="text" value="" style="width:680px;height:40px;line-height:40px;
		          font-size:26px;margin:0px;margin-left:20px;border:solid 0px #C0C0C0;" />
	        
	    </div>
	    <div style="font-size:16px;margin-left:20px;margin-top:20px;">常用做法</div>
	    <div style="width:100%;height:200px;font-size:16px;margin:0px;margin-top:10px;border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
	        <div class="note_item">免辣</div><div class="note_item">免蒜</div>
	        <div class="note_item">免葱</div><div class="note_item">免姜</div><div class="note_item">免糖</div>
	        <div class="note_item">免味精</div><div class="note_item">免香菜</div>
	        <div class="note_item">微辣</div><div class="note_item">少盐</div>
	        <div class="note_item">白灼</div><div class="note_item">清炒</div><div class="note_item">辣炒</div>
	        <div class="note_item">清蒸</div><div class="note_item">红烧</div><div class="note_item">干烧</div>
	        <div class="note_item">家熬</div><div class="note_item">水煮</div><div class="note_item">加热</div>
	        <div class="note_item">常温</div><div class="note_item">清真</div><div class="note_item">不制作</div>
	        
	    </div>
	    
	    <div style="margin-top:30px;height:50px;line-height:50px;background:#3366FF;">
	        <div id="closeNoteWinBtn" style="float:left;height:50px;line-height:50px;width:359px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">取消</div>
	        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
	        <div id="submitNoteBtn" style="float:left;height:50px;line-height:50px;width:359px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">确定</div>
	    </div>
</form>
</div>
<!-- 备注信息展示窗口    end  -->

<!-- 现金收取页面    start  -->
<div id="paycash_north" style="display:none;">
   
</div>
<div id="paycash_center" style="display:none;">
<form id="paycashform" >
		<div id="paycashdiv" style="width:500px;height:400px;background:#ECECEC;margin-left:auto;margin-right:auto;" onselectstart='return false'>
		    <div style="height:50px;line-height:50px;width:100%;background:#DDDDDD;text-align:center;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
		        桌号 ： <span id="paycash_table_name" style=""></span>
		        <span id="mode_id" style="display:none;"></span>
		        <input id="paycash_bill_id" type="hidden" value=""  />
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:30px;">应收</div>
		    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;color:#555;">
			      <span style="font-size:20px;margin-left:20px;">￥</span>
			      <span id="paycash_owefee" style="width:300px;height:40px;line-height:40px;
			          font-size:22px;margin:0px;margin-left:5px;border:solid 0px #C0C0C0;" >
			      </span>
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:20px;"><span id="mode_name" style="font-size:24px;color:#000;"></span></div>
		    <div style="width:100%;height:58px;line-height:58px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
		        <div style="float:left;font-size:20px;margin-left:20px;height:56px;line-height:56px;">￥</div>
		        <input  type="submit" value="" style="position:absolute;top:-9999px;"  />
			      <input id="recvfeeinput" type="text" value="" autocomplete="off" style="float:left;width:420px;height:56px;line-height:56px;
			          font-size:38px;margin:0px;padding-left:10px;border:solid 0px #C0C0C0;" />
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:20px;">找零</div>
		    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;color:#C42140;">
			      <span style="font-size:20px;margin-left:20px;">￥</span>
			      <span id="paycash_backfee" style="width:300px;height:40px;line-height:40px;
			          font-size:22px;margin:0px;margin-left:5px;border:solid 0px #C0C0C0;" >0</span>
		    </div>
		    <div style="margin-top:40px;height:50px;line-height:50px;background:#3366FF;">
		        <div id="cancelRecvFeeBtn" onselectstart='return false'  style="float:left;height:50px;line-height:50px;width:249px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">取消</div>
		        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
		        <div id="recvfeeBtn" onselectstart='return false' style="float:left;height:50px;line-height:50px;width:249px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">收取</div>
		    </div>	
		</div>
	
</form>
</div>
<!-- 现金收取页面    end  -->

<!-- 抹零页面    start  -->
<div id="reducefee_center" style="display:none;">
    <div id="reducefeediv" style="width:500px;height:350px;background:#ECECEC;margin-left:auto;margin-right:auto;" onselectstart='return false'>
	    <div style="height:50px;line-height:50px;width:100%;background:#DDDDDD;text-align:center;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
	        桌号 ： <span id="reducefee_table_name" style=""></span>
	        <input id="reducefee_bill_id" type="hidden" value=""  />
	    </div>
	    <div style="font-size:16px;margin-left:20px;margin-top:30px;">应收</div>
	    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;
	        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;color:#555;">
		      <span style="font-size:20px;margin-left:20px;">￥</span>
		      <span id="reducefee_owefee" style="width:300px;height:40px;line-height:40px;
		          font-size:22px;margin:0px;margin-left:5px;border:solid 0px #C0C0C0;" >
		      </span>
	    </div>
	    <div style="font-size:16px;margin-left:20px;margin-top:20px;"><span style="font-size:24px;color:#000;">抹去</span></div>
	    <div style="width:100%;height:58px;line-height:58px;border:solid 0px #C0C0C0;margin-top:10px;
	        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
	        <div style="float:left;font-size:20px;margin-left:20px;height:56px;line-height:56px;">￥</div>
		      <input id="reducefeeinput" type="text" value="" autocomplete="off" style="float:left;width:420px;height:56px;line-height:56px;
		          font-size:38px;margin:0px;padding-left:10px;border:solid 0px #C0C0C0;" />
	    </div>
	    <div style="font-size:16px;margin-left:20px;margin-top:20px;"></div>
	    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;">
	    </div>
	    <div style="margin-top:0px;height:50px;line-height:50px;background:#836BC1;">
	        <div id="cancelReducefeeBtn" onselectstart='return false'  style="float:left;height:50px;line-height:50px;width:249px;color:#FFF;text-align:center;cursor:pointer;">取消</div>
	        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
	        <div id="addReducefeeBtn" onselectstart='return false' style="float:left;height:50px;line-height:50px;width:249px;color:#FFF;text-align:center;cursor:pointer;">抹零</div>
	    </div>	
	</div>
</div>
<!-- 抹零页面    end  -->
<!-- 添加临时菜页面    start  -->
<div id="addtempfood_center" style="display:none;">
<form id="addtempfoodform" >
   <div id="tempfooddiv" style="width:720px;height:480px;background:#ECECEC;margin-left:auto;margin-right:auto;">
      <input id="addtempfood_bill_id" type="hidden" value=""  />
      <input id="tempfoodhiddenbtn" type="submit" value="" style="position:absolute;top:-9999px;"  />
      <div style="height:45px;line-height:45px;width:100%;background:#DDDDDD;text-align:center;
	    	  border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
	        添加临时菜
	    </div>
	    <div style="font-size:16px;margin-left:15px;margin-top:20px;">请输入菜品名称</div>
	    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:15px;
	        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
		      <input id="temp_foodname_input" type="text" value="" style="width:680px;height:40px;line-height:40px;
		          font-size:26px;margin:0px;margin-left:20px;border:solid 0px #C0C0C0;" />
	    </div>
	    <div style="font-size:16px;margin-left:20px;margin-top:15px;">
	        <span style="">单价(元)</span>
	        <span style="margin-left:310px;">数量</span>
	    </div> 
	    <div>
	        <div style="float:left;width:357px;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:15px;
			        border-top:solid 1px #C0C0C0;border-right:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">  
				      <input id="temp_price_input" type="text" value="" autocomplete="off" style="width:310px;height:40px;line-height:40px;
				          font-size:26px;margin:0px;margin-left:20px;border:solid 0px #C0C0C0;" />
			    </div>
			    
			    <div style="float:right;width:357px;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:15px;
			        border-top:solid 1px #C0C0C0;border-left:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">  
				      <input id="temp_count_input" type="text" value="1" style="width:300px;height:40px;line-height:40px;
				          font-size:26px;margin:0px;margin-left:30px;border:solid 0px #C0C0C0;" />
			    </div>
	    </div>
	    <div style="clear:both;">&nbsp;</div>
	    <div style="font-size:16px;margin-left:20px;margin-top:0px;">请选择出单打印机</div>
	    <div style="width:100%;height:140px;font-size:16px;margin:0px;margin-top:15px;border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
	        <%  for(int i=0;i<printers.size();i++){ 
	                Map printer = (Map) printers.get(i);
	                //ut.log(printer);  
	        %>
		          <div class="printer sprinter" name="<%= printer.get("PRINTER") %>" ip="<%= printer.get("IP") %>"><%= printer.get("PRINTER") %></div>
		      <% }%>
	        
	    </div>
    
	    <div style="margin-top:20px;height:50px;line-height:50px;background:#3366FF;">
	        <div id="backBtnFromAddTempFoodBtn" style="float:left;height:50px;line-height:50px;width:359px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">取消</div>
	        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
	        <div id="addTempFoodSubmitBtn" style="float:left;height:50px;line-height:50px;width:359px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">下单</div>
	    </div>      
   </div>
</form>
</div>  
<!-- 添加临时菜页面    end  -->


<!-- 退菜 赠送 打折  转台页面    start  -->
<div id="operatefood_center" style="display:none;">
		<div id="operatefooddiv" style="width:400px;height:300px;background:#ECECEC;margin-left:auto;margin-right:auto;" onselectstart='return false'>
		    <div style="height:50px;line-height:50px;width:100%;background:#DDDDDD;text-align:center;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
		        
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:40px;"><span id="operatefood_title"></span></div>
		    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:30px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
		        <input id="open_table_id" type="hidden" value=""  />
		        <input id="operate_type" type="hidden" value=""  />
			    <input id="operate_value" type="text" value="" autocomplete="off" style="width:300px;height:40px;line-height:40px;
			          font-size:26px;margin:0px;margin-left:20px;border:solid 0px #C0C0C0;" />
		    </div>
		    <div style="margin-top:74px;height:50px;line-height:50px;background:#3366FF;">
		        <div id="backBtnFromOperateFoodBtn" style="float:left;height:50px;line-height:50px;width:199px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">取消</div>
		        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
		        <div id="submitOperateFoodBtn" style="float:left;height:50px;line-height:50px;width:199px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">提交</div>
		    </div>	
		</div>

</div>  
<!-- 退菜 赠送 打折  转台页面  -->

<!-- 会员卡读卡页面    start  -->
<div id="selectvip_center" style="display:none;">
    <image id="backFromSelectVipBtn" src="/app/operation/image/back.png" style="position:absolute;top:30px;left:20px;height:40px;width:40px;cursor:pointer;" />
    <div style="line-height:60px;width:900px;color:#FFF;font-size:20px;border-bottom:solid #FFF 1px;margin:10px 40px 10px 100px;">
    请选择会员卡用户
    </div>
    <div id="vipcard_list" style="margin-left:100px;margin-top:10px;">
        
    </div>	
    	
</div>
<!-- 会员卡读卡页面    end  -->


<!-- 会员卡收取页面    start  -->
<div id="vipcardpay_center" style="display:none;">
	    <div id="vipcardpaydiv" style="width:500px;height:400px;background:#ECECEC;margin-left:auto;margin-right:auto;" onselectstart='return false'>
		    <div style="height:50px;line-height:50px;width:100%;background:#DDDDDD;text-align:center;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
		        桌号 ： <span id="vipcardpay_table_name" style=""></span>
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:30px;">应收</div>
		    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;color:#555;">
			      <span style="font-size:20px;margin-left:20px;">￥</span>
			      <span id="vipcardpay_owefee" style="width:300px;height:40px;line-height:40px;
			          font-size:22px;margin:0px;margin-left:5px;border:solid 0px #C0C0C0;" >
			      </span>
		    </div>
		    <div style="font-size:20px;margin-left:20px;margin-top:20px;">会员卡扣款</div>
		    <div style="width:100%;height:58px;line-height:58px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
		        <div style="float:left;font-size:20px;margin-left:20px;height:56px;line-height:56px;">￥</div>
			    <input id="vipcardpayfeeinput" type="text" value="" autocomplete="off" style="float:left;width:420px;height:56px;line-height:56px;
			          font-size:38px;margin:0px;padding-left:10px;border:solid 0px #C0C0C0;" />
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:20px;">卡号</div>
		    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
			      <span style="font-size:20px;margin-left:20px;"></span>
			      <input id="vipcardpay_user_id" type="hidden" value="" />
			      <span id="pay_card_no" style="width:300px;height:40px;line-height:40px;
			          font-size:22px;margin:0px;margin-left:5px;border:solid 0px #C0C0C0;" >0</span>
		    </div>
		    <div style="margin-top:40px;height:50px;line-height:50px;background:#3366FF;">
		        <div id="cancelPayByVipCardBtn" onselectstart='return false'  style="float:left;height:50px;line-height:50px;width:249px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">取消</div>
		        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
		        <div id="payByVipCardBtn" onselectstart='return false' style="float:left;height:50px;line-height:50px;width:249px;background:#3366FF;color:#FFF;text-align:center;cursor:pointer;">收取</div>
		    </div>	
		</div>
</div>
<!-- 会员卡收取页面    end  -->

<!-- 选择挂账用户页面    start  -->
<div id="selectcredit_center" style="display:none;">
    <image id="backFromSelectCreditBtn" src="/app/operation/image/back.png" style="position:absolute;top:30px;left:20px;height:40px;width:40px;cursor:pointer;" />
    <div style="line-height:60px;width:900px;color:#FFF;font-size:20px;border-bottom:solid #FFF 1px;margin:10px 40px 10px 100px;">
    请选择挂账用户
    </div>
    <div id="credit_list" style="margin-left:100px;margin-top:10px;">
        
    </div>	
</div>
<!-- 选择挂账用户页面    end  -->

<!-- 挂账收取页面    start  -->
<div id="creditpay_center" style="display:none;">
	    <div id="creditpaydiv" style="width:500px;height:400px;background:#ECECEC;margin-left:auto;margin-right:auto;" onselectstart='return false'>
		    <div style="height:50px;line-height:50px;width:100%;background:#DDDDDD;text-align:center;border-bottom:solid 1px #C0C0C0;font-size:18px;font-weight:bold;">
		        桌号 ： <span id="creditpay_table_name" style=""></span>
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:30px;">应收</div>
		    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;color:#555;">
			      <span style="font-size:20px;margin-left:20px;">￥</span>
			      <span id="creditpay_owefee" style="width:300px;height:40px;line-height:40px;
			          font-size:22px;margin:0px;margin-left:5px;border:solid 0px #C0C0C0;" >
			      </span>
		    </div>
		    <div style="font-size:20px;margin-left:20px;margin-top:20px;">挂账金额</div>
		    <div style="width:100%;height:58px;line-height:58px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
		        <div style="float:left;font-size:20px;margin-left:20px;height:56px;line-height:56px;">￥</div>
			    <input id="creditpayfeeinput" type="text" value="" autocomplete="off" style="float:left;width:420px;height:56px;line-height:56px;
			          font-size:38px;margin:0px;padding-left:10px;border:solid 0px #C0C0C0;" />
		    </div>
		    <div style="font-size:16px;margin-left:20px;margin-top:20px;">挂账用户</div>
		    <div style="width:100%;height:42px;line-height:42px;border:solid 0px #C0C0C0;margin-top:10px;
		        border-top:solid 1px #C0C0C0;border-bottom:solid 1px #C0C0C0;background:#FFF;">
			      <span style="font-size:20px;margin-left:20px;"></span>
			      <input id="creditpay_user_id" type="hidden" value="" />
			      <span id="pay_credit_user" style="width:300px;height:40px;line-height:40px;
			          font-size:22px;margin:0px;margin-left:5px;border:solid 0px #C0C0C0;" ></span>
		    </div>
		    <div style="margin-top:40px;height:50px;line-height:50px;background:#C42140;">
		        <div id="cancelPayByCreditBtn" onselectstart='return false'  style="float:left;height:50px;line-height:50px;width:249px;background:#C42140;color:#FFF;text-align:center;cursor:pointer;">取消</div>
		        <div style="float:left;height:34px;line-height:34px;width:0px;margin-top:8px;border-left:solid 1px #FFF;"></div>
		        <div id="payByCreditBtn" onselectstart='return false' style="float:left;height:50px;line-height:50px;width:249px;background:#C42140;color:#FFF;text-align:center;cursor:pointer;">挂账</div>
		    </div>	
		</div>
</div>
<!-- 挂账收取页面    end  -->


<!-- 今日统计页面    start  -->
<div id="today_west" style="display:none;">
	<div style="width:100%;line-height:50px;font-size:18px;color:#CCC;text-align:center;margin-top:50px;">
	    <image class="backToDeskBtn" src="/app/operation/image/back.png" style="height:40px;width:40px;cursor:pointer;" />
	</div>
	<div style="width:100%;line-height:50px;font-size:18px;color:#CCC;text-align:center;margin-top:60px;">
	    今日统计
	</div>
	<div  class="leftdirection" style="position:absolute;top:182px;right:0px;"></div> 
</div>
<div id="today_center" style="display:none;">
	<div style="height:40px;line-height:40px;width:800px;font-size:16px;color:#111;margin-top:5px;border-bottom:solid 1px #D3D3D3;">
  	    <span id="todayPageTodayStr" style="margin-left:0px;font-size:18px;"><%= today %></span>
    </div>
    
    <div  style="float:left;margin-left:0px;margin-top:20px;">
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
    
    
    <div  style="float:left;margin-left:30px;margin-top:20px;">
        <div style="background:#fff;width:300px;height:40px;line-height:40px;margin-top:10px;margin-left:0px;position:relative;">
	        <div style="height:35px;line-height:35px;width:240px;color:#8B0195;
	        	  font-size:22px;border-bottom:solid 1px #CCC;">
	            档口统计
	        </div>
        </div>
        <div style="background:#fff;width:300px;height:490px;margin-left:0px;position:relative;color:#000;font-size:16px;padding-top:10px;">
            <table id="category_table" width='100%' cellspacing="0" style="border:solid 1px #CCCCCC;background:#fff;">
               
            </table> 
            
            <div id="printCategoryBtn" style="height:40px;margin-top:20px;margin-left:0px;font-size:20px;">
                <div  style="height:35px;line-height:35px;width:240px;
                 text-align:center;background:#8B0195;color:#fff;cursor:pointer;" >打    印</div>
            </div>
              
        </div>
    </div>
    
    <div  style="float:left;margin-left:30px;margin-top:20px;">
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
                   <td width='140'><span style="margin-left:30px;">预计收入：</span></td>
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
            <div  style="height:40px;margin-top:20px;margin-left:0px;font-size:20px;">
                <div id="printTodayMoneyBtn" style="height:35px;line-height:35px;width:240px;
                 text-align:center;background:#094AB2;color:#fff;cursor:pointer;" >打    印</div>
            </div>
            
        </div>
    </div>
    
    
<div>
<!-- 今日统计页面    end  -->

</body>
</html>