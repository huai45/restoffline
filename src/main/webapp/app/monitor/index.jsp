<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="net.sf.json.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
List tables = jdbcTemplate.queryForList(" select * from td_table where 1 = 1 ",new Object[]{});
List foods = jdbcTemplate.queryForList(" select * from td_food where 1 = 1 ",new Object[]{});
%>
<!DOCTYPE>
<html>
<head>
<title>RestOn传菜监控系统</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<link rel="stylesheet" href="/resource/ext4/resources/css/ext-all.css" />
<link rel="stylesheet" href="/resource/ext4/resources/css/ext-all-gray.css" />
<link  rel="stylesheet" type="text/css" href="/app/monitor/css/monitor.css" />
<style>

</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src='/resource/jquery/jquery.cookie.js'></script>
<script src="/resource/ext4/ext-all.js"></script>
<script type="text/javascript">
billList = {};
itemList = {};
ajax_flag = 0;
takeoutbills = new Array();
var alarmList1 = new Array();  // 超过30分钟
var alarmList2 = new Array();  // 超过20分钟
var alarmList3 = new Array();  // 超过15分钟
var alarmList4 = new Array();  // 正常
var alarmList5 = new Array();  // 已上菜
var alarmList6 = new Array();  // 未起菜
test="hello";
current_ban='<%= ut.currentDate() %>';
</script>
<script src="/app/monitor/js/monitor.js"></script>
</head>
<body id="body" style="">
<div id="main_north"  style="display:none;color:#B9B9B9;">
    <div style="height:32px;line-height:32px;background:#2D2D2D;font-size:14px;padding-left:10px;">
	    <div id="searchPageBtn" class="menu menu_select" target="blank" title="" onselectstart="return false">搜索</div>
	    <div id="monitorPageBtn" class="menu" target="blank" title="" onselectstart="return false">菜品监控</div>
	    <div id="takeOutPageBtn" class="menu" target="blank" title="" onselectstart="return false">外卖查询</div>
	    
	    <div id="exit" style="float:right;width:70px;height:32px;line-height:32px;text-align:center;cursor:pointer;" onselectstart="return false">注销</div>
	    <div style="float:right;height:14px;margin-top:10px;width:0px;border-left:solid 1px #B9B9B9;"></div>
	    <div id="username" style="float:right;padding-left:20px;padding-right:20px;height:32px;line-height:32px;
	        text-align:center;cursor:pointer;" onselectstart="return false">${user.staffname}</div>
    </div>
</div>
<div id="mainpage_north"  style="display:none;border:solid 0px #DDD;">
    <div style="height:68px;line-height:68px;background:#f1f1f1;">
    <form id="form" name = "form" method="post" action="/app/sendfood/search.jsp">
        <div style="float:left;height:68px;line-height:68px;width:125px;text-align:center;">传菜助手
        </div>
        <input id="search_str" tyle="text" style="float:left;height:27px;line-height:27px;width:575px;margin-top:19px;padding-left:5px;border:solid 1px #CCC;" />
        <div style="float:left;height:68px;line-height:68px;margin-left:10px;" onselectstart="return false">
            <div id="searchBtn" onselectstart="return false">
                &nbsp;
            </div>
        </div>
    </form>
    </div>
</div>
<div id="mainpage_west" style="display:none;">
	<div id="" style="float:left;margin-left:30px;margin-top:20px;width:270px;">
	    <div style="margin-left:20px;margin-top:0px;color:#fff;font-size:24px;">传菜部</div>
	</div>
    <div id="" style="float:left;margin-left:20px;margin-top:10px;width:270px;">
		<div class="app" style="position:relative;height:120px;width:250px;color:#fff;background:#199DDD;
		    margin-top:10px;margin-left:10px;cursor:pointer;float:left;" url="" >
		    <div style="position:absolute;top:10px;left:10px;width:220px;">
		        <span style="font-size:20px;">您好 ： ${USER_CONTEXT.username}</span>
		    </div>
		    <div style="position:absolute;top:85px;left:10px;width:220px;">
		        <span style="font-size:20px;"><%= "123" %></span>
		    </div>
		</div>
		<div id="monitorBtn" class="app" style="position:relative;height:120px;width:120px;color:#fff;background:#199DDD;
		    margin-top:10px;margin-left:10px;cursor:pointer;float:left;" >
		    <div style="position:absolute;top:88px;left:8px;">
                <span style="font-size:18px;">超时监控</span>    
            </div>
		</div>
		<div id="queryBillPageBtn" class="app" style="position:relative;height:120px;width:120px;color:#fff;background:#199DDD;
		    margin-top:10px;margin-left:10px;cursor:pointer;float:left;" url="" >
		    <div style="position:absolute;top:22px;left:39px;">
                <image src="/image/jisuanqi.jpg" style="height:56px;width:43x;" /> 
            </div>
		    <div style="position:absolute;top:88px;left:8px;">
                <span style="font-size:18px;">账单查询</span>    
            </div>
		</div>
		<div id="" class="app" style="position:relative;height:120px;width:120px;color:#fff;background:#199DDD;
		    margin-top:10px;margin-left:10px;cursor:pointer;float:left;" url="" >
		    <div style="position:absolute;top:22px;left:39px;">
                <image src="/image/jisuanqi.jpg" style="height:56px;width:43x;" /> 
            </div>
		    <div style="position:absolute;top:88px;left:8px;">
                <span style="font-size:18px;">外卖查询</span>    
            </div>
		</div>
	</div>
</div>
<div id="scanpage_center" style="display:none;">
    <div style="clear:both;"></div>
    <div id="" style="margin-top:10px;margin-left:100px;color:#999;font-size:14px;">
        找到<span id="barcode_count" style="padding-left:5px;padding-right:5px;">0</span>个条码
    </div>
    <div id="barcodetable" style="float:left;margin-left:130px;margin-top:20px;">
        <div style="background:#fff;width:450px;height:40px;line-height:40px;margin-top:10px;margin-left:0px;position:relative;">
	        <div style="height:35px;line-height:35px;width:450px;color:#094AB2;
	        	  font-size:22px;border-bottom:solid 1px #CCC;">
	            条码信息
	        </div>
        </div>
        <div style="background:#fff;width:450px;height:400px;margin-left:0px;position:relative;color:#000;font-size:16px;padding-top:10px;">
            <table width='100%' cellspacing="0" style="border:solid 1px #CCCCCC;background:#fff;">
               <tr height="40" >
                   <td width='170'><span style="margin-left:30px;">条码编号：</span></td>
                   <td ><span id="food_barcode"></span></td>
               </tr>
               <tr height="40" >
                   <td width='170'><span style="margin-left:30px;">菜品名称：</span></td>
                   <td ><span id="food_name"></span></td>
               </tr>
               <tr height="40" >
                   <td width='170'><span style="margin-left:30px;">台号：</span></td>
                   <td ><span id="food_table_name"></span></td>
               </tr>
               <tr height="40" >
                   <td width='170'><span style="margin-left:30px;">单价：</span></td>
                   <td ><span id="food_price"></span></td>
               </tr>
               <tr height="40" >
                   <td width='190'><span style="margin-left:30px;">备注：</span></td>
                   <td ><span id="food_note">0</span></td>
               </tr>
                <tr height="40" >
                   <td width='190'><span style="margin-left:30px;">状态：</span></td>
                   <td ><span id="food_result"></span></td>
               </tr>
            </table>
            <div id="" style="height:40px;margin-top:20px;margin-left:0px;font-size:20px;">
                <div id="sendFoodBtn" style="height:45px;line-height:45px;width:240px;border-radius:3px;
                 text-align:center;background:#094AB2;color:#fff;cursor:pointer;" >上    菜</div>
            </div>
            
        </div>
    </div>
    <div id="" style="display:none;float:left;margin-left:130px;margin-top:20px;">
	    <div class="" style="height:40px;margin-top:20px;margin-left:130px;font-size:20px;">
	        <div id="showCookie" style="height:35px;line-height:35px;width:240px;border-radius:3px;
	            text-align:center;background:#3D3D3D;color:#fff;cursor:pointer;" >显示缓存</div>
	    </div>
	    
	    <div class="" style="height:40px;margin-top:20px;margin-left:130px;font-size:20px;">
	        <div id="clearCookie" style="height:35px;line-height:35px;width:240px;border-radius:3px;
	            text-align:center;background:#D0284C;color:#fff;cursor:pointer;" >清除缓存</div>
	    </div> 
    </div> 
</div>

<!-- 菜品监控页面    begin  -->
<div id="monitorpage_center" style="display:none;">

    <div id="" style="float:left;margin-left:60px;margin-top:20px;">
        <div style="background:#D0284C;width:260px;height:70px;line-height:50px;margin-top:10px;margin-left:0px;position:relative;">
	        <div style="height:35px;line-height:35px;width:235px;position:absolute;top:16px;left:30px;font-size:22px;">
	            超时菜品(<span id="alarm1_count">0</span>)
	        </div>
        </div>
        <div id="overtime1" style="background:#fff;width:260px;height:500px;margin-left:0px;position:relative;
            color:#000;font-size:16px;padding-top:10px;OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
            <% for(int i=0;i<0;i++){%>
               <div class="alarm">
	               <div class="alarm_food">水煮酸菜鱼</div>
	               <div>
	                   <div class="alarm_table">台号：201</div>
	               	   <div class="alarm_time">已用时:45分</div>
	               </div>
	               <div class="alarm_line 1px #3D3D3D;"></div>
               </div>
            <% }%>   
        </div>
    </div>
    
    <div style="float:left;margin-left:40px;margin-top:20px;">
        <div style="background:#EC6F2C;width:260px;height:70px;line-height:50px;margin-top:10px;margin-left:0px;position:relative;">
	        <div style="height:35px;line-height:35px;width:235px;position:absolute;top:16px;left:30px;font-size:22px;">
	            告警菜品(<span id="alarm2_count">0</span>)
	        </div>
        </div>
        <div id="overtime2" style="background:#fff;width:260px;height:500px;margin-left:0px;position:relative;
            color:#000;font-size:16px;padding-top:10px;OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
        </div>
    </div>
    
    <div style="float:left;margin-left:40px;margin-top:20px;">
        <div style="background:#00B2F0;width:260px;height:70px;line-height:50px;margin-top:10px;margin-left:0px;position:relative;">
	        <div style="height:35px;line-height:35px;width:235px;position:absolute;top:16px;left:30px;font-size:22px;">
	            预警菜品(<span id="alarm3_count">0</span>)
	        </div>
        </div>
        <div id="overtime3" style="background:#fff;width:260px;height:500px;margin-left:0px;position:relative;
            color:#000;font-size:16px;padding-top:10px;OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
        </div>
    </div>
    
    <div style="float:left;margin-left:40px;margin-top:20px;">
        <div style="background:#878787;width:260px;height:70px;line-height:50px;margin-top:10px;margin-left:0px;position:relative;">
	        <div style="height:35px;line-height:35px;width:235px;position:absolute;top:16px;left:30px;font-size:22px;">
	            本班统计
	        </div>
        </div>
        <div id="overtime4" style="background:#fff;width:260px;height:500px;margin-left:0px;position:relative;color:#000;font-size:16px;padding-top:10px;">
            <table width='100%' cellspacing="0" style="border:solid 0px #CCCCCC;background:#fff;">
               <tr height="30" >
                   <td width='140'><span style="margin-left:30px;">已上菜品：</span></td>
                   <td ><span id="finish_count">0</span></td>
               </tr>
               <tr height="30" >
                   <td width='140'><span style="margin-left:30px;">正在制作：</span></td>
                   <td ><span id="cooking_count">0</span></td>
               </tr>
               <tr height="30" >
                   <td width='140'><span style="margin-left:30px;">正常制作：</span></td>
                   <td ><span id="normal_count">0</span></td>
               </tr>
               <tr height="30" >
                   <td width='140'><span style="margin-left:30px;">未起菜品：</span></td>
                   <td ><span id="waitstart_count">0</span></td>
               </tr>
               <tr height="30" >
                   <td width='140'><span style="margin-left:30px;">本班合计：</span></td>
                   <td ><span id="total_count"></span></td>
               </tr>
            </table>  
            
            <div class="refreshBtn" style="display:none;height:50px;margin-top:40px;margin-left:30px;font-size:20px;">
                <div id="" style="height:35px;line-height:35px;width:140px;
                 text-align:center;background:#878787;color:#fff;cursor:pointer;" >刷    新</div>
            </div>
             
        </div>
    </div>
    
    
<div>
<!-- 菜品监控页面    end  -->

<div id="takeout_west" style="color:#fff;" >
    <div style="height35px;line-height:35px;
	   margin-top:20px;margin-left:20px;color:#fff;font-size:20px;">台号</div>
    <div style="margin-top:10px;margin-left:20px;">
        <input id="billinfo_querybill_str" type="text" class="" style="line-height:34px;width:204px; height:34px;border:0px;font-size:20px; " />
    </div>
    <div id="billinfo_bill_list" style="margin-top:20px;">
	</div>
</div>
<div id="takeout_center" style="">
    <div style="height:60px;margin-left:80px;position:relative;" onselectstart='return false'>
     <div id="backBtnFromQueryBill"  onselectstart='return false'>
         <div class="paycash_title">
             <span style="color:#000;font-size:30px;">账单明细</span>
         </div>
     </div>
     
    </div>
    <div id="billinfopart" style="display:none;margin-left:30px;margin-right:30px;font-size:16px;color:#fff;background:#3985DA;">
        <table style="margin-left:20px;">
           <tr height='26'>
               <td style="width:80px;color:#E1E1E1">账单序号：</td><td id="billinfo_bill_id" style="width:130px;"></td>
               <td style="width:80px;color:#E1E1E1">餐台号：</td><td id="billinfo_table_id"style="width:130px;"></td>
               <td style="width:80px;color:#E1E1E1">日期：</td><td id="billinfo_date" style="width:130px;"></td>
               <td style="width:80px;color:#E1E1E1">班次：</td><td id="billinfo_ban" style="width:140px;"></td>
           </tr>
           <tr height='26'>
               <td style="width:80px;color:#E1E1E1">就餐人数：</td><td id="billinfo_nop" style="width:130px;"></td>
               <td style="width:80px;color:#E1E1E1">开台时间：</td><td id="billinfo_start_time" ></td>
               <td style="width:80px;color:#E1E1E1">封单时间：</td><td id="billinfo_end_time" ></td>
               <td style="width:80px;color:#E1E1E1">发票：</td><td id="billinfo_invoice_fee" ></td>
           </tr>
        </table>
    </div>
    <div id="billitempart" style="display:none;margin-left:30px;margin-right:30px;margin-bottom:20px;font-size:14px;color:#000;background:#F1F1F1">
        <table id="billinfo_itemtable" cellspacing="0"  style="">
            <tr height='26'>
               <td style="width:210px;padding-left:10px;background:#595959;color:#fff;">菜名</td>
               <td style="width:140px;background:#595959;color:#fff;">数量</td>
               <td style="background:#595959;color:#fff;width:80px;">退菜</td>
               <td style="background:#595959;color:#fff;width:80px;">赠送</td>
               <td style="background:#595959;color:#fff;width:80px;">折扣</td>
               <td style="background:#595959;color:#fff;width:80px;">类型</td>
               <td style="background:#595959;color:#fff;width:80px;">起菜时间</td>
               <td style="background:#595959;color:#fff;width:80px;">催菜时间</td>
               <td style="background:#595959;color:#fff;width:80px;">上菜时间</td>
               <td style="background:#595959;color:#fff;width:80px;">做法</td>
               <td style="background:#595959;color:#fff;width:80px;">备注</td>
           </tr>
        </table>
    </div>
</div>

<div id="queryfood_north" style="color:#fff;" >
    <div style="margin-top:20px;margin-left:20px;font-size:28px;" >以下是根据关键字 "<span id="keyword"></span>" 查询到的菜品  ,  单击菜名即可完成扫单</div>
</div>
<div id="queryfood_center" style="margin-left:40px;">
    <div id="" style="margin-top:10px;margin-left:60px;color:#999;font-size:14px;">
        找到<span id="food_count" style="padding-left:5px;padding-right:5px;">0</span>个菜品
    </div>
    <div id="foodList" style="margin:40px;margin-top:10px;">
        <% for(int i=0;i<foods.size();i++){
		       Map item = (Map)foods.get(i);
		%>
		<div class="food" style="position:relative;" id="food_<%=item.get("FOOD_ID")%>"
		     food_id="<%=item.get("FOOD_ID")%>" 
		     food_name="<%=item.get("FOOD_NAME")%>" 
		     abbr="<%=item.get("ABBR")%>" 
		     price="<%=item.get("PRICE")%>" 
		     unit="<%=item.get("UNIT")%>" 
		     category="<%=item.get("CATEGORY")%>" 
		     groups="<%=item.get("GROUPS")%>" 
		     printer="<%=item.get("PRINTER")%>" >
		     <div class="count_tag" style="position:absolute;bottom:-25px;right:10px;">0</div>
		     <%=item.get("FOOD_NAME")%>
		</div>
		<% }%>
    </div>
    <div style="clear:both;margin-bottom:40px;"></div>
</div>


<div id="tablepage_center" style="margin-left:40px;">
    <div id="" style="margin-top:10px;margin-left:60px;color:#999;font-size:14px;">
        找到<span id="table_count" style="padding-left:5px;padding-right:5px;">0</span>个台位
    </div>
    <div id="tableList" style="margin:40px;margin-top:10px;">
        <% for(int i=0;i<tables.size();i++){
		       Map table = (Map)tables.get(i);
		%>
		<div class="table" 
		     table_id="<%=table.get("TABLE_ID").toString().trim()%>" 
		     floor="<%=table.get("FLOOR").toString().trim()%>" 
		     table_name="<%=table.get("TABLE_NAME").toString().trim()%>" 
		     state="<%=table.get("STATE").toString().trim()%>" 
		     size="<%=table.get("SIZE").toString().trim()%>" 
		     bill_id="<%=table.get("BILL_ID").toString().trim()%>" 
		     printer="<%=table.get("PRINTER").toString().trim()%>">
		     <%=table.get("TABLE_ID").toString().trim()%>
		</div>
		<% }%>
    </div>
    <div style="clear:both;margin-bottom:40px;"></div>
</div>

<div id="tableHiddenList" style="display:none;">
	
</div>
<div id="blank_center" style="margin-left:40px;">

</div>
</body>
</html>
