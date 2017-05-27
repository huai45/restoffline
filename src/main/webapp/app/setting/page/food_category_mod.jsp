<%@ page language="java" import="java.util.*,
com.huai.common.util.*,com.mongodb.BasicDBObject,org.springframework.jdbc.core.JdbcTemplate,
javax.sql.DataSource,com.huai.common.domain.User" pageEncoding="UTF-8"%>
<%
ut.log(" mod " );
String table_name = "food_category";
User user = (User) request.getSession().getAttribute(CC.USER_CONTEXT);
String id = request.getParameter("id");
//id = new String(id.getBytes("iso-8859-1"),"UTF-8");  
ut.log(" id :" +id);
String sql = " select * from td_food_category  where rest_id = ? and category_id = ?   ";
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
List orders = jdbcTemplate.queryForList(sql,new Object[]{user.getRest_id(),id});
Map item = new HashMap();
if(orders.size()>0){
	item = (Map)orders.get(0);
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
	#allpick{
	    height:30px;width:40px;text-align:center;
	}
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script type="text/javascript">
ajax_flag = 0;
$(document).ready(function(){

    $("#foodcategorypage").addClass("left_menu_sel"); 
     
    
    $("#cancelBtn").click( function(){
        document.location.href = "/transPage?page=/setting/page/food_category&time="+new Date();
    });
    $("#backBtn").click( function(){
        document.location.href = "/transPage?page=/setting/page/food_category&time="+new Date();
    });
	
	$("#submitBtn").click( function(){
	    if(ajax_flag > 0){
	        return false;
	    }
	    var id= $.trim($("#id").val());
	    var name= $.trim($("#name").val());
	    if(id==''){
	        alert('请填写类别编号');
			return false;
		}
		if(name==''){
		    alert('请填写类别名称!');
			return false;
		}
		ajax_flag = 1;
		$.post("/app/setting/setting_service.jsp", {
		        TRADE_TYPE_CODE : '<%= table_name %>_mod',
		        id : id,
		        name : name
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
	    document.location.href = "/transPage?page=/setting/page/food_category&time="+new Date();
	};
	
	
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
            <span>编辑菜品类别信息</span><span style="margin-left:150px;font-size:16px;">当前类别编号 ： <%= item.get("category_id") %></span>
            <input id="id" type="text" value="<%= item.get("category_id") %>" style="display:none;" />
        </div>
        <div id="" style="line-height:1px;height:1px;width:97%;border-bottom:solid 1px #CCCCCC;"></div>
    </div>
    <div id="" style="">
        <div style="float:right;width:400px;background:#fff;height:250px;margin-right:50px;margin-top:70px;border-left:solid 1px #595959;">
		  <div style="position:relative;top:-50px;">
		    
	      </div>
	    </div>
	    <div id="stock_name" style="margin-left:120px;padding-top:5px;margin-bottom:0px;font-size:20px;font-weight:bold;">&nbsp;</div>
	    
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">类别名称</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="name" type="text" value="<%= item.get("name") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        <div style="margin-left:120px;margin-bottom:5px;font-size:18px;">级别</div>
	    <div style="margin-left:120px;margin-bottom:15px;">
            <input id="level" type="text" value="<%= item.get("level") %>" readonly style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
        </div>
        
        
        <div style="clear:both;"></div>
        <div id="" style="margin-top:15px;margin-left:120px;margin-bottom:45px;height:50px;width:500px;line-height:50px;">
            <div id="submitBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#fff;margin-top: 8px;margin-right: 18px; " onselectstart='return false'> 确定 </div>
            <div id="backBtn" align="center" class="btn" style="float:left;background:#D5D5D5;color:#3D3D3D;margin-top: 8px;margin-right: 5px; " onselectstart='return false'> 取消 </div>
        </div>
    </div>
    
</body>
</html>
