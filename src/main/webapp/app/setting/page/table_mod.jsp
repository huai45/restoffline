<%@ page language="java" import="java.util.*,
com.huai.common.util.*,org.springframework.jdbc.core.JdbcTemplate,
com.huai.common.domain.User" pageEncoding="UTF-8"%>
<%
String table_name = "table";
User user = (User) request.getSession().getAttribute(CC.USER_CONTEXT);
String id = request.getParameter("id");

ut.log(" id :" +id);

String sql = " select * from td_table where table_id = ?   ";
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
List tables = jdbcTemplate.queryForList(sql,new Object[]{id});
Map item = new HashMap();
if(tables.size()>0){
	item = (Map)tables.get(0);
}
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

    $("#tablemanagepage").addClass("left_menu_sel");

    $("#cancelBtn").click( function(){
        document.location.href = "/app/setting/page/<%= table_name %>_manage.jsp";
    });
    $("#backBtn").click( function(){
        document.location.href = "/app/setting/page/<%= table_name %>_manage.jsp";
    });

    function reloadPage(){
        document.location.href = "/app/setting/page/<%= table_name %>_manage.jsp";
    };
	
	$("#submitBtn").click( function(){
	    var table_id= $.trim($("#table_id").val());
	    var table_name= $.trim($("#table_name").val());
	    var floor_order= $.trim($("#floor_order").val());
	    var floor= $.trim($("#floor").val());
	    var size= $.trim($("#size").val());
	    var limit_money= $.trim($("#limit_money").val());
	    var printer= $.trim($("#printer").val());
	    var queue_tag= $.trim($("#queue_tag").val());
	    if(table_id==''){
		    alert('请填写楼餐台编号!');
		    $("#table_id").val('');
			return false;
		}
		if(table_name==''){
		    alert('请填写餐台名称!');
		    $("#table_name").val('');
			return false;
		}
		if(floor_order==''){
		    alert('请填写楼层排序!');
		    $("#floor_order").val('');
			return false;
		}
		if(floor==''){
		    alert('请填写所在楼层名称!');
		    $("#floor").val('');
			return false;
		}
		if(size==''){
		    alert('请填写可就餐人数!');
		    $("#size").val('');
			return false;
		}
		if(limit_money==''){
		    alert('请填写最低消费金额!');
		    $("#limit_money").val('');
			return false;
		}
		if(printer==''){
		    alert('请填写打印机IP!');
		    $("#printer").val('');
			return false;
		}
		if(queue_tag==''){
		    alert('请填写预定标志位!');
		    $("#queue_tag").val('');
			return false;
		}
        ajax_flag = 1;
        $.post("/app/setting/setting_service.jsp", {
			    TRADE_TYPE_CODE : '<%= table_name %>_mod',
		        table_id : table_id,
		        table_name : table_name,
		        floor_order : floor_order,
		        floor : floor,
		        size : size,
		        limit_money : limit_money,
		        printer : printer,
		        queue_tag : queue_tag
			},  function (result) {
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
            <span>编辑餐台信息</span><span style="margin-left:150px;font-size:16px;">当前餐台编号 ： <%= item.get("table_id") %></span>
            <input id="table_id" type="text" value="<%= item.get("table_id") %>" style="display:none;" />
        </div>
        <div id="" style="line-height:1px;height:1px;width:97%;border-bottom:solid 1px #CCCCCC;"></div>
    </div>
    <div id="center" style="">
        <div style="float:right;width:400px;background:#fff;height:450px;margin-right:50px;margin-top:70px;border-left:solid 1px #595959;">
		  <div style="position:relative;top:-50px;">
		    
	      </div>
	    </div>
	    <div id="stock_name" style="margin-left:120px;padding-top:5px;margin-bottom:0px;font-size:20px;font-weight:bold;">&nbsp;</div>
	    
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">餐台名称</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="table_name" type="text" value="<%= item.get("table_name") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">楼层排序</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="floor_order" type="text" value="<%= item.get("floor_order") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">楼层名称</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="floor" type="text" value="<%= item.get("floor") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">可容纳人数</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="size" type="text" value="<%= item.get("size") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">最低消费金额（元）</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="limit_money" type="text" value="<%= item.get("limit_money") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">打印机IP</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="printer" type="text" value="<%= item.get("printer") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">是否可预订</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="queue_tag" type="text" value="<%= item.get("queue_tag") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="clear:both;"></div>
        <div id="" style="margin-top:15px;margin-left:120px;margin-bottom:45px;height:50px;width:500px;line-height:50px;">
            <div id="submitBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#fff;margin-top: 8px;margin-right: 18px; " onselectstart='return false'> 确定 </div>
            <div id="backBtn" align="center" class="btn" style="float:left;background:#D5D5D5;color:#3D3D3D;margin-top: 8px;margin-right: 5px; " onselectstart='return false'> 取消 </div>
        </div>
    </div>
    
    <!-- 提示信息对话框    begin  -->
    <%= Dom.AlertDom() %>
    <!-- 提示信息对话框    end  -->
    
       
</body>
</html>
