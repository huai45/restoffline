<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.operation.dao.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String today = ut.currentDate(-1);
%>
<!DOCTYPE html>
<html>
<head>
<title>RestOn会员管理系统</title>
<link rel="stylesheet" href="/resource/MetroUICSS/min/metro-bootstrap.min.css">
<link rel="stylesheet" href="/resource/MetroUICSS/min/metro-bootstrap-responsive.min.css">
<link rel="stylesheet" href="/resource/MetroUICSS/min/iconFont.min.css">
<link rel="stylesheet" href="/app/query/css/common.css"/>
<link rel="stylesheet" href="/app/query/css/index_head.css"/>
<style>
body{
    font-family:Microsoft Yahei;
}
.left_menu{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#000;cursor:pointer;border-left:solid 6px #FFF;
}
.left_menu_sel{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#80397B;cursor:pointer;border-left:solid 6px #80397B;
}
</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/MetroUICSS/jquery/jquery.widget.min.js"></script>
<script src="/resource/MetroUICSS/min/metro.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    
    $('#logo').click(function(){
        document.location.href="/index.html";
        return false;
    });
    
    $('#account').click(function(){
        document.location.href="/app/platform/account.jsp";
        return false;
    });
   
    $('#logoutBtn').click(function(){
        window.close();
        return false;
    });
    
    function gotoUrl(obj) {
        var url = obj.attr("url");
        var target = obj.attr("target");
	    if(url==undefined||url==''){
	        return false;
	    }
        if(url.indexOf("?")>=0){
            document.location.href=url+"&time="+new Date();
        }else{
            document.location.href=url+"?time="+new Date();
        }
    }
    $(".left_menu,.left_menu_sel").click(function(){
        //alert($(this).attr("url"));
        gotoUrl($(this));
	});
	
    
    $("#querytodaypage").addClass("left_menu_sel");
    
    // 重置今日统计页面
    function resetTodayPage(){
        
        
    }
    
	$("#queryBtn").click(function(){
	    var today = $('#year').val()+"-"+$('#month').val()+"-"+$('#day').val();
        Ext.Ajax.request({
			url : '/query/queryTodayHistoryData',
			params : {
			    start_date : today ,
			    end_date : today 
			},
			success : function(response, opts) {
				var obj = Ext.util.JSON.decode(response.responseText);
				top.mask.hide();
			    if(obj.success=='true'){
			        $("#todat_str").text(today);
				    initTodayPage(obj.data);
				}
			},
			failure : function(response, opts) {
			    top.mask.hide();
			    alert("系统异常");
			}
		});
	    return false;
	});
	
	$("#year").val('<%= today.substring(0,4) %>');
	$("#month").val('<%= today.substring(5,7) %>');
	$("#day").val('<%= today.substring(8,10) %>');
	
});
</script>
</head>
<body class="metro" style="font-family:Microsoft Yahei;">
<div id="north" style="height:40px;background:#262626;">
    <div id="logo" style="font-family:Microsoft Yahei;">RestON</div>
    <div id="logoutBtn" class="titleBtn" style="font-family:Microsoft Yahei;">退出</div>
    <div id="username" style="font-family:Microsoft Yahei;">${user.staffname}</div>
    <div id="account" style="font-family:Microsoft Yahei;">我的账户</div>
    <div class="titleBtn" style="font-family:Microsoft Yahei;">商城</div>
</div>
<div id="west" style="float:left;width:190px;">
	<div style="height:41px;line-height:41px;width:100%;font-size:18px;color:#80397B;margin-top:0px;border-bottom:solid 1px #DDD;">
  	    <span style="margin-left:30px;">营业查询</span>
    </div>
    <div style="height:15px;line-height:15px;width:10px;font-size:12px;color:#999;">&nbsp;</div>
    <div id="indexpage" class="left_menu" url="/query/index.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">首页</span>
    </div>
    <div id="querytodaypage" class="left_menu" url="/transPage?page=/query/queryTodayHistory" onselectstart='return false'>
  	    <span style="margin-left:20px;">今日查询</span>
    </div>
    <div id="addvipcardpage" class="left_menu" url="/transPage?page=/query/queryTodayHistory" onselectstart='return false'>
  	    <span style="margin-left:20px;">往日查询</span>
    </div>
    <div id="addvipcardpage" class="left_menu" url="/transPage" onselectstart='return false'>
  	    <span style="margin-left:20px;">档口统计</span>
    </div>
    <div id="addvipcardpage" class="left_menu" url="/transPage" onselectstart='return false'>
  	    <span style="margin-left:20px;">菜品销售明细</span>
    </div>
    <div id="creditquerypage" class="left_menu" url="/transPage" onselectstart='return false'>
  	    <span style="margin-left:20px;">酒水查询</span>
    </div>
    <div id="addcreditpage" class="left_menu" url="/transPage" onselectstart='return false'>
  	    <span style="margin-left:20px;">点菜员业绩</span>
    </div>
    <div id="addcreditpage" class="left_menu" url="/transPage" onselectstart='return false'>
  	    <span style="margin-left:20px;">台位销售统计</span>
    </div>
    <div id="addcreditpage" class="left_menu" url="/transPage" onselectstart='return false'>
  	    <span style="margin-left:20px;">历史账单查询</span>
    </div>
</div>
<div id="center" style="float:left;font-family:Microsoft Yahei;">
    <div id="center_north">
	    <div id="startpage" style="height:50px;width:95%;padding-left:20px;line-height:50px;border-bottom:solid 0px #CCCCCC;font-size:20px;">
	        <div id="" style="height:50px;width:100%;line-height:50px;border-bottom:solid 1px #CCCCCC;font-size:20px;">
	            <div id="" style="float:right;">
			        <div id="queryBtn" align="center" class="btn" style="float:left;background:#094AB2;color:#FFFFFF;margin-top:8px;margin-right: 18px; " onselectstart='return false'> 查询 </div>
		        </div> 
	            <div id="" style="float:right;">
		            <div class="input-control text" data-role="datepicker"
					    data-date="2013-01-01"
					    data-format="yyyy-mm-dd"
					    data-position="bottom"
					    data-locale='zhCN'
					    data-effect="none|slide|fade">
					    <input type="text">
					    <button class="btn-date"></button>
					</div>
		        </div>        
		        <div style="float:left;height:50px;line-height:50px;margin-left:15px;margin-right:5px;">
		               每日统计( <span id="todat_str"></span> )
		        </div>
	        </div>
	    </div>
    </div>
    
</div>

<!-- 今日统计页面    end  -->
</body>
</html>
