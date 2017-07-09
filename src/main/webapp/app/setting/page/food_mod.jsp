<%@ page language="java" import="java.util.*,
com.huai.common.util.*,com.mongodb.BasicDBObject,org.springframework.jdbc.core.JdbcTemplate,
javax.sql.DataSource,com.huai.common.domain.User" pageEncoding="UTF-8"%>
<%
String table_name = "food";
User user = (User) request.getSession().getAttribute(CC.USER_CONTEXT);
String id = request.getParameter("id");
String sql = " select * from td_food a  where    food_id = ?   ";
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
List orders = jdbcTemplate.queryForList(sql,new Object[]{ id });
Map item = new HashMap();
if(orders.size()>0){
	item = (Map)orders.get(0);
}

List categoryList = jdbcTemplate.queryForList("select * from td_food_category a where 1 = 1 and level = 1   ",new Object[]{ });
ut.log(" categoryList.size() :" +categoryList.size());

List groupsList = jdbcTemplate.queryForList("select * from td_food_category a where 1 = 1 and level = 2   ",new Object[]{ });
ut.log(" groupsList.size() :" +groupsList.size());

List printerList = jdbcTemplate.queryForList("select * from td_printer a  where 1 = 1 order by printer desc ",new Object[]{ });
ut.log(" printerList.size() :" +printerList.size());

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css"/>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css"/>
<link rel="stylesheet" href="/app/setting/css/common.css"/>
<link rel="stylesheet" href="/app/setting/css/index_head.css"/>
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
	    background:#094AB2;color:#fff;
	}
	.bluefont{
	    color:#094AB2;
	}
	.checkbox{
	    height:30px;width:40px;text-align:center;
	}
	.group_input {
        border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:18px; 
        margin-right:10px;
    }
    .group_label {
        margin-left:120px;margin-bottom:5px;font-size:14px;
    }
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script type="text/javascript">
ajax_flag = 0;
$(document).ready(function(){

    $("#foodmanagepage").addClass("left_menu_sel"); 
     
    $("#cancelBtn").click( function(){
        document.location.href = "/app/setting/page/<%= table_name %>_manage.jsp";
    });
    $("#backBtn").click( function(){
        document.location.href = "/app/setting/page/<%= table_name %>_manage.jsp";
    });
	
	$("#submitBtn").click( function(){
	    if(ajax_flag > 0){
	        return false;
	    }
	    var food_id= $.trim($("#food_id").val());
	    var food_name= $.trim($("#food_name").val());
	    var abbr= $.trim($("#abbr").val());
	    var price= $.trim($("#price").val());
	    var unit= $.trim($("#unit").val());
	    var category= $.trim($("#category").val());
	    var groups= $.trim($("#groups").val());
	    var remark= $.trim($("#remark").val());
	    
	    var printer= $.trim($("#printer").val());
	    var printer_sec= $.trim($("#printer_sec").val());
	    var printer_start= $.trim($("#printer_start").val());
	    var printer_hurry= $.trim($("#printer_hurry").val());
	    var printer_back= $.trim($("#printer_back").val());
	    
	    var print_count= $.trim($("#print_count").val());
	    var cook_tag= $.trim($("#cook_tag").val());
	    var cook_time= $.trim($("#cook_time").val());
	    var show_tag= $.trim($("#show_tag").val());
	    var use_tag= $.trim($("#use_tag").val());
	    
	    if(food_id==''){
	        alert('请填写菜品编号!');
			return false;
		}
	    if(food_name==''){
		    alert('请填写菜品名称!');
			return false;
		}
		if(abbr==''){
		    alert('请填写缩写码!');
			return false;
		}
		if(price==''){
		    alert('请填写单价!');
			return false;
		}
		if(unit==''){
		    alert('请填写单位!');
			return false;
		}
		
		ajax_flag = 1;
		$.post("/app/setting/setting_service.jsp", {
		        TRADE_TYPE_CODE : '<%= table_name %>_mod',
		        food_id : food_id,
			    food_name : food_name,
			    abbr : abbr,
			    price : price,
			    unit : unit ,
			    category : category ,
			    groups : groups ,
			    remark : remark ,
			    
			    printer : printer ,
			    printer_sec : printer_sec ,
			    printer_start : printer_start ,
			    printer_hurry : printer_hurry ,
			    printer_back : printer_back ,
			    
			    print_count : print_count ,
			    cook_tag : cook_tag ,
			    cook_time : cook_time ,
			    show_tag : show_tag ,
			    use_tag : use_tag
		    }, function (result) {
		        ajax_flag = 0;
				var obj = $.parseJSON(result);
				if(obj.success=='true'){
				    alert(obj.msg);
				    reloadPage();
				}else{
				    alert(obj.msg);
				}
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
	});	
	
	function reloadPage(){
	    document.location.href = "/app/setting/page/<%= table_name %>_manage.jsp";
	};
	
	$("#category").val('<%= item.get("category") %>');
    $("#groups").val('<%= item.get("groups") %>');
    
    $("#printer").val('<%= item.get("printer") %>');
    $("#printer_sec").val('<%= item.get("printer_sec") %>');
    $("#printer_start").val('<%= item.get("printer_start") %>');
    $("#printer_hurry").val('<%= item.get("printer_hurry") %>');
    $("#printer_back").val('<%= item.get("printer_back") %>');
    
});
</script>
</head>
<body id="body" style="">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
<%@ include file="/app/setting/page/index_head.jsp" %>
<%@ include file="/app/setting/page/index_west.jsp" %>
<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;">

    <div id="north" style="height:50px;padding-left:20px;line-height:50px;border-bottom:solid 0px #CCCCCC;font-size:20px;">
        <div id="" style="height:48px;width:100%;line-height:50px;">
            <div id="" style="float:right;">
	            <div id="cancelBtn" align="center" class="btn" style="float:left;color:#595959;margin-top:8px;margin-right: 70px; " onselectstart='return false'> 取消 </div>
	        </div>
            <span>编辑菜品信息</span><span style="margin-left:150px;font-size:16px;">当前菜品名称 ： <%= item.get("food_name") %></span>
        </div>
        <div id="" style="line-height:1px;height:1px;width:97%;border-bottom:solid 1px #CCCCCC;"></div>
    </div>
    <div id="center" style="">
        <div style="float:left;width:500px;background:#fff;margin-left:0px;margin-top:0px;">
	    
		    <div id="" style="margin-left:100px;padding-top:5px;margin-bottom:0px;font-size:20px;font-weight:bold;">&nbsp;</div>
		    <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">菜品编号</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="food_id" type="text" value="<%= item.get("food_id") %>" readonly style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
		    <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">菜品名称</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="food_name" type="text" value="<%= item.get("food_name") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">缩写码</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="abbr" type="text" value="<%= item.get("abbr") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">单价（元）</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="price" type="text" value="<%= item.get("price") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">单位</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="unit" type="text" value="<%= item.get("unit") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">大类</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
		        <select id="category" class="group_input" >
	                <option value="0" >选择...</option>
				    <% 
			            for(int i=0;i<categoryList.size();i++){
			            	Map category = (Map)categoryList.get(i);
			        %>
			            <option value="<%= category.get("name") %>"><%= category.get("name")  %></option>
			        <% 
			            }
			        %>
				</select>
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">分组</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
		        <select id="groups" class="group_input" >
	                <option value="0" >选择...</option>
				    <% 
			            for(int i=0;i<groupsList.size();i++){
			            	Map category = (Map)groupsList.get(i);
			        %>
			            <option value="<%= category.get("name") %>"><%= category.get("name")  %></option>
			        <% 
			            }
			        %>
				</select>
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">备注信息</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="remark" type="text" value="<%= item.get("remark") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        
	        <div style="clear:both;"></div>
	        
	        <div id="" style="margin-top:35px;margin-left:120px;margin-bottom:65px;height:50px;width:500px;line-height:50px;">
	            <div id="submitBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#fff;margin-top: 8px;margin-right: 18px; " onselectstart='return false'> 确定 </div>
	            <div id="backBtn" align="center" class="btn" style="float:left;background:#D5D5D5;color:#3D3D3D;margin-top: 8px;margin-right: 5px; " onselectstart='return false'> 取消 </div>
	        </div>
        
        </div>
        
        
        <div style="float:left;width:400px;background:#fff;height:600px;margin-left:50px;margin-top:30px;border-left:solid 1px #595959;">
		    <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">主打印机</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
		        <select id="printer" class="group_input" value = "<%= item.get("printer") %>">
	                <option value="0" >选择...</option>
				    <% 
			            for(int i=0;i<printerList.size();i++){
			            	Map category = (Map)printerList.get(i);
			        %>
			            <option value="<%= category.get("printer") %>"><%= category.get("printer")  %></option>
			        <% 
			            }
			        %>
				</select>
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">备打印机</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
		        <select id="printer_sec" class="group_input" value = "<%= item.get("printer_sec") %>">
	                <option value="0" >选择...</option>
				    <% 
			            for(int i=0;i<printerList.size();i++){
			            	Map category = (Map)printerList.get(i);
			        %>
			            <option value="<%= category.get("printer") %>"><%= category.get("printer")  %></option>
			        <% 
			            }
			        %>
				</select>
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">起菜打印机</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
		        <select id="printer_start" class="group_input">
	                <option value="0" >选择...</option>
				    <% 
			            for(int i=0;i<printerList.size();i++){
			            	Map category = (Map)printerList.get(i);
			        %>
			            <option value="<%= category.get("printer") %>"><%= category.get("printer")  %></option>
			        <% 
			            }
			        %>
				</select>
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">催菜打印机</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
		        <select id="printer_hurry" class="group_input">
	                <option value="0" >选择...</option>
				    <% 
			            for(int i=0;i<printerList.size();i++){
			            	Map category = (Map)printerList.get(i);
			        %>
			            <option value="<%= category.get("printer") %>"><%= category.get("printer")  %></option>
			        <% 
			            }
			        %>
				</select>
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">退菜打印机</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
		        <select id="printer_back" class="group_input">
	                <option value="0" >选择...</option>
				    <% 
			            for(int i=0;i<printerList.size();i++){
			            	Map category = (Map)printerList.get(i);
			        %>
			            <option value="<%= category.get("printer") %>"><%= category.get("printer")  %></option>
			        <% 
			            }
			        %>
				</select>
	        </div>
		    <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">打印数量</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="print_count" type="text" value="<%= item.get("print_count") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">属否制作</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="cook_tag" type="text" value="<%= item.get("cook_tag") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="display:none;margin-left:100px;margin-bottom:5px;font-size:18px;">制作时长(单位：分钟)</div>
		    <div style="display:none;margin-left:100px;margin-bottom:15px;">
	            <input id="cook_time" type="text" value="<%= item.get("needtime") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">是否默认显示</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="show_tag" type="text" value="<%= item.get("show_tag") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	        <div style="display:none;margin-left:100px;margin-bottom:5px;font-size:18px;">是否启用</div>
		    <div style="display:none;margin-left:100px;margin-bottom:15px;">
	            <input id="use_tag" type="text" value="<%= item.get("usetag") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
	        </div>
	    </div>
	        
    </div>
       
</body>
</html>
