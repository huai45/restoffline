<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
User user = (User)session.getAttribute( CC.USER_CONTEXT );

%>
<!DOCTYPE html>
<html>
<head>
<title>RestOn</title>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css">
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css">
<link rel="stylesheet" href="/app/platform/css/common.css">
<link rel="stylesheet" href="/app/platform/css/index_head.css">
<link rel="stylesheet" href="/app/platform/css/index_foot.css">
<style>
body{
    font-family:Microsoft Yahei;
}
#showBtn{
    height:36px;line-height:32px;width:150px;text-align:center;
    color:#BFBFBF;font-size:14px;font-family:Microsoft Yahei;border:solid 1px #BFBFBF;cursor:pointer;
}
#showBtn:hover{
    color:#FFF;border:solid 1px #FFF;
}
.app{
    float:left;width:120px;height:120px;position:relative;margin-bottom:5px;margin-left:5px;color:#FFF;
    background-color:#2B579A;border:solid 3px #3A3A40;cursor:pointer;font-size:12px;
}
.big{
    width:250px;
}
.app:hover{
    border:solid 3px #525252;
}
.appname{
    position:absolute;bottom:10px;left:10px;
}
.appicon{
    position:absolute;top:15px;left:24px;height:72px;width:72px;
}
#earth{
    margin-top:3px;margin-left:10px;height:32px;width:32px;
}

</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
$(document).ready(function(){
    function gotoUrl(obj) {
        var url = obj.attr("url");
        var target = obj.attr("target");
	    if(url==undefined||url==''){
	        return false;
	    }
        if(target=="blank"){
            if(url.indexOf("?")>=0){
                window.open(url+"&time="+new Date(),obj.attr("title"));
            }else{
                window.open(url+"?time="+new Date(),obj.attr("title"));
            }
	    }else {
	        if(url.indexOf("?")>=0){
	            document.location.href=url+"&time="+new Date();
            }else{
                document.location.href=url+"?time="+new Date();
            }
	    }
    }
    
    $(".app").click(function(){
        gotoUrl($(this));
	});
    
});
</script>  
</head>
<body class="metro">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
	<%@ include file="/app/platform/page/index_head.jsp" %>
	<div id="east" data-options="region:'east',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:0px;">
	
	</div>
	<div id="west" data-options="region:'west',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:0px;">
		
	</div>
	<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:url(/app/platform/image/bg.png) no-repeat #3A3A40;background-position: -280px -80px;">
	    <div style="float:left;width:540px;">
	        <div style="margin-top:80px;margin-left:150px;color:#FFF;font-size:50px;font-family:Microsoft Yahei;">使用 Restaurant Online 协作</div>
			<div style="margin-top:20px;margin-left:150px;margin-right:20px;color:#BFBFBF;font-size:20px;font-family:Microsoft Yahei;">
			在 RestaurantOn 中联机保存文档、电子表格和演示文稿。与他人共享和同时协同工作。立即开始，完全免费!
			</div>
			<div id="showBtn" style="margin-top:40px;margin-left:150px;">
			查看所有选项
			</div>
	    </div>
	    <div style="float:left;margin-top:90px;margin-left:50px;">
		    <div class="app big" url="/operation/loading.html" target="" title="收银" >
		        <image src="/app/platform/image/icon/word.jpg" class="appicon" style="left:86px;"/>
		        <div class="appname" style="font-family:Microsoft Yahei;">收银 Online</div>
		    </div>
		    <div class="app" style="background-color:#0072C6;display:none;">
		        <image src="/app/platform/image/icon/outlook.jpg" class="appicon" />
		        <div class="appname" style="font-family:Microsoft Yahei;">库存</div>
		    </div>
		    <div class="app" url="/cust/index.html" target="" title="会员管理" style="background-color:#D24726;">
		        <image src="/app/platform/image/icon/renmai.jpg" class="appicon" />
		        <div class="appname" style="font-family:Microsoft Yahei;">会员管理</div>
		    </div>
		    <div style="clear:both;"></div>
		   
		    <div class="app big" style="background-color:#D24726;display:none;">
		        <image src="/app/platform/image/icon/ppt.jpg" class="appicon" style="left:86px;"/>
		        <div class="appname" style="font-family:Microsoft Yahei;">预定</div>
		    </div>
		    <div class="app" style="background-color:#5133AB;display:none;">
		        <image src="/app/platform/image/icon/rili.jpg" class="appicon" style="left:22px;"/>
		        <div class="appname" style="font-family:Microsoft Yahei;">排位</div>
		    </div>
		    <div style="clear:both;"></div>
		    <div class="app big" url="/monitor/loading.html" target="" title="监控" style="background-color:#217346;">
		        <image src="/app/platform/image/icon/excel.jpg" class="appicon" style="left:86px;"/>
		        <div class="appname" style="font-family:Microsoft Yahei;">后厨监控</div>
		    </div>
		    <div class="app" url="setting/index.html" target="" title="系统设定" style="background-color:#094AB2;">
		        <image src="/app/platform/image/icon/onedrive.jpg" class="appicon" style="width:90px;left:14px;"/>
		        <div class="appname" style="font-family:Microsoft Yahei;">系统设定</div>
		    </div>
	    </div>
	</div>
	<%@ include file="/app/platform/page/index_foot.jsp" %>
</div>
</body>
</html>