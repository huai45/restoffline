<%@ page language="java" import="java.util.*,com.huai.common.util.*,org.springframework.jdbc.core.JdbcTemplate" pageEncoding="UTF-8"%>
<%@ page import="com.huai.common.domain.IData" %>
<%
String table_name = "restinfo";
String sql = " select  *  from td_restaurant ";
JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
List result = jdbcTemplate.queryForList(sql,new Object[]{ });
Map info = new HashMap();
info.put("restname","");
info.put("address","");
info.put("printer","");
info.put("telephone","");
if(result.size()>0){
	info = (Map)result.get(0);
}
ut.p(info);

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

    $("#restinfopage").addClass("left_menu_sel");
     
    $("#cancelBtn").click( function(){
        document.location.href = "/app/setting/page/<%= table_name %>_mod.jsp";
    });
    $("#backBtn").click( function(){
        document.location.href = "/app/setting/page/<%= table_name %>_mod.jsp";
    });
	
	$("#submitBtn").click( function(){
	    if(ajax_flag > 0){
	        return false;
	    }
	    var restname= $.trim($("#restname").val());
	    var address= $.trim($("#address").val());
	    var telephone= $.trim($("#telephone").val());
        var printer= $.trim($("#printer").val());
	   
	    if(restname==''){
	        alert('请填写餐厅名称!');
			return false;
		}
		
	    if(address==''){
	        alert('请填写地址!');
			return false;
		}
	    if(telephone==''){
		    alert('请填写餐厅电话!');
			return false;
		}
        if(printer==''){
            alert('请填写结班信息打印机IP地址!');
            return false;
        }
		
		ajax_flag = 1;
		$.post("/app/setting/setting_service.jsp", {
		        TRADE_TYPE_CODE : '<%= table_name %>_mod',
                restname : restname,
                address : address,
                telephone : telephone,
		        printer : printer
		    }, function (result) {
		        ajax_flag = 0;
				var obj = $.parseJSON(result);
                alert(obj.msg);
				if(obj.success=='true'){
				    reloadPage();
				}else{

				}
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
	});	
	
	function reloadPage(){
	    document.location.href = "/app/setting/page/<%= table_name %>_mod.jsp";
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
        <div style="height:48px;width:100%;line-height:50px;">
            <div style="float:right;">
	            <div id="cancelBtn" align="center" class="btn" style="float:left;color:#595959;margin-top:8px;margin-right: 70px; " onselectstart='return false'> 刷新 </div>
	        </div>
            <span>编辑餐厅信息</span>
        </div>
        <div style="line-height:1px;height:1px;width:97%;border-bottom:solid 1px #CCCCCC;"></div>
    </div>
    <div style="">
        <div style="float:left;width:500px;background:#fff;margin-left:0px;margin-top:0px;">
	    
		    <div style="margin-left:100px;padding-top:5px;margin-bottom:0px;font-size:20px;font-weight:bold;">&nbsp;</div>
		    <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">餐厅名称</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="restname" type="text" value="<%= info.get("restname") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:500px; height:32px;font-size:20px; " />
	        </div>
		    <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">餐厅地址</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="address" type="text" value="<%= info.get("address") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:500px; height:32px;font-size:20px; " />
	        </div>
	        <div style="margin-left:100px;margin-bottom:5px;font-size:18px;">餐厅电话</div>
		    <div style="margin-left:100px;margin-bottom:15px;">
	            <input id="telephone" type="text" value="<%= info.get("telephone") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:500px; height:32px;font-size:20px; " />
	        </div>
			<div style="margin-left:100px;margin-bottom:5px;font-size:18px;">结班信息打印机IP地址</div>
			<div style="margin-left:100px;margin-bottom:15px;">
				<input id="printer" type="text" value="<%= info.get("printer") %>" style="border:solid 1px #CCCCCC;padding:0px;margin:0px;line-height:32px;width:300px; height:32px;font-size:20px; " />
			</div>
	        <div style="clear:both;"></div>
	        
	        <div id="" style="margin-top:35px;margin-left:120px;margin-bottom:65px;height:50px;width:500px;line-height:50px;">
	            <div id="submitBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#fff;margin-top: 8px;margin-right: 18px; " onselectstart='return false'> 保存 </div>
	        </div>
        
        </div>
    </div>
</div>
</body>
</html>
