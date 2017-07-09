<%@ page language="java" import="java.util.*,
com.huai.common.util.*,com.mongodb.BasicDBObject,org.springframework.jdbc.core.JdbcTemplate,
javax.sql.DataSource,com.huai.common.domain.User" pageEncoding="UTF-8"%>
<%
String table_name = "food_category";
User user = (User) request.getSession().getAttribute(CC.USER_CONTEXT);
String sql = " select * from td_food_category a where 1 = 1  order by category_id  ";
ut.log(" sql :" +sql);
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
List orders = jdbcTemplate.queryForList(sql,new Object[]{});
ut.log(" details1.size() :" +orders.size());
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
     
     $('#backBtn').click(function(event){
	 });
	
     $(".order_tr").hover(
         function(){
             $(this).addClass("order_tr_hover");    //鼠标经过添加hover样式   
         },   
         function(){   
             $(this).removeClass("order_tr_hover");   //鼠标离开移除hover样式   
         }   
     );
	
	function resetCheckBox(){
	    $(".checkbox").removeAttr("checked");
	    $("#allpick").removeAttr("checked");
	    $(".order_tr_select").removeClass("order_tr_select");
    }
    
    $(".checkbox").click(function(event){
        if($(this).parent().parent().hasClass("order_tr_select")){
            $(this).parent().parent().removeClass("order_tr_select");
			$(this).parent().parent().children().eq(1).addClass("bluefont");
			$(this).parent().parent().children().eq(2).addClass("bluefont");
        }else{
            $(this).parent().parent().addClass("order_tr_select");
            $(this).parent().parent().children().eq(1).removeClass("bluefont");
			$(this).parent().parent().children().eq(2).removeClass("bluefont");
        }
        event.stopPropagation();
	});
    
    $("#allpick").click(function(){
        if($(this).attr("checked")){
            $(".checkbox").attr("checked",true);
            $(".order_tr").addClass("order_tr_select");
            $(".bluefont").removeClass("bluefont");
        }else{
            $(".checkbox").removeAttr("checked");
            $(".order_tr").removeClass("order_tr_select");
            $(".bluefont1").addClass("bluefont");
        }
	});
	
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
	
    $("#selectType").click(function(){
        if($("#selectType").text()=='直拨'){
            $("#order_type").val(1);
        }else if($("#selectType").text()=='入库'){
            $("#order_type").val(2);
        }else{
            $("#order_type").val(0);
        }
        $(this).hide();
        $(this).next().show();
    });
    
    $("#order_type").change(function(){
        if($("#order_type").val()==0){
            $("#selectType").text("类型");
            $(".order_tr").appendTo("#show_list");
        }else{
            $("#selectType").text( $("#order_type").find("option:selected").text() );
            $("#show_list").children().appendTo("#hidden_list");
            $(".order_tr div[stock_type="+$("#order_type").val()+"]").parent().appendTo("#show_list");
        }
        $("#order_type").hide();
        $("#selectType").show();
        resetCheckBox();
    });
    $("#order_type").blur(function(){
        if($("#order_type").val()==0){
            $("#order_type").hide();
            $("#selectType").show();
        }
    });
    
    $("#addBtn").click( function(){
        document.location.href = "/app/setting/page/<%= table_name %>_add.jsp";
    });
    
    $("#refreshBtn").click( function(){
        document.location.href = "/transPage?page=/setting/page/food_category";
    });
    
    $("#modifyBtn").click( function(){
		if($("#show_list .order_tr_select").length!=1){
		    alert('请先选择一条需要编辑的记录!');
			return false;
		}
		var id = "";
		$("#show_list .order_tr_select").each(function(){
		    id =$(this).attr("id");
		});
		document.location.href = "/app/setting/page/<%= table_name %>_mod.jsp?id="+id;
	});	
	
	$("#cancelBtn").click( function(){
	    if(ajax_flag > 0){
	        return false;
	    }
	    if($("#show_list .order_tr_select").length==0){
	        alert('请先选择需要删除的记录!');
			return false;
		}
		var orders = [];
		$("#show_list .order_tr_select").each(function(){
		    var order = {};
		    order.id=$(this).attr("id");
		    orders.push(order);
		});
		if(confirm("确定要删除选定的"+orders.length+"条记录吗?")){
            ajax_flag = 1;
		    $.post("/app/setting/setting_service.jsp", {
			        TRADE_TYPE_CODE : '<%= table_name %>_delete',
			        jsonStr : JSON.stringify(orders)
			    }, function (result) {
			        ajax_flag = 0;
					var obj = $.parseJSON(result);
					if(obj.success=='true'){
					    $("#show_list .order_tr_select").remove();
                        resetCheckBox();
                        alert(obj.msg);
					}else{
					    alert(obj.msg);
					}
			}).error(function(){
			    ajax_flag = 0;
			    alert("系统异常"); 
			});
        }
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
                <div id="addBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#FFFFFF;margin-top:8px;margin-right: 70px; " onselectstart='return false'> 添加 </div>
		        <div id="modifyBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#FFFFFF;margin-top:8px;margin-right: 70px; " onselectstart='return false'> 编辑 </div>
		        <div id="cancelBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#FFFFFF;margin-top:8px;margin-right: 70px; " onselectstart='return false'> 删除 </div>
	            <div id="refreshBtn" align="center" class="btn" style="float:left;color:#595959;margin-top:8px;margin-right: 70px; " onselectstart='return false'> 刷新 </div>
	        </div> 
            <span>菜品类别管理</span><span style="margin-left:50px;">共 <span id="count"><%= orders.size() %></span> 条记录</span>
        </div>
        <div id="" style="line-height:1px;height:1px;width:97%;border-bottom:solid 1px #CCCCCC;"></div>
    </div>
    <div id="" style="">
        <div id="audit_list" style="height:470px;width:930px;overflow:auto;">
            <div class="order_head" style="">
	            <div style="float:left;padding-top:1px;width:40px;text-align:center;" >
	                <input type="checkbox" id="allpick" value="checkbox" ></input>
	            </div>
	            <div style="width:120px;" >编号</div>
	            <div style="width:300px;" >名称</div>
	            <div style="width:300px;" >级别</div>
	            
	        </div>
	        <div id="show_list">
            <% for(int i=0;i<orders.size();i++){
                   Map order = (Map)orders.get(i);
                   //ut.log(" category = "+order);
            %>
            <div class="order_tr" id="<%= order.get("category_id") %>" >
                <div style="float:left;padding-top:1px;width:40px;text-align:center;" >
	                <input type="checkbox" class="checkbox" value="checkbox" ></input>
	            </div>
	            <div class="bluefont1 bluefont" style="width:120px;" ><%= order.get("category_id") %></div>
	            <div class="bluefont1 bluefont" style="width:300px;" ><%= order.get("name") %></div>
	            <div class="" style="width:300px;" ><%= order.get("level") %></div>
	        </div>
	        <% } %>
	        </div>
        </div>
        
        <div style="clear:both;"></div>
        <div id="hidden_list" style="display:none;"></div>
    </div>
    
</body>
</html>
