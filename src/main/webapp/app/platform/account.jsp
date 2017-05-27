<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css">
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css">
<style>
body{
    
}
#logo{
    float:left;height:32px;line-height:30px;margin-left:10px;padding-left:10px;padding-right:20px;
    background-color:#505050;color:#FFF;cursor:pointer;font-size:14px;
}
#logo:hover{
    background-color:#444;
}
.titleBtn{
    float:right;height:32px;line-height:30px;padding-left:15px;padding-right:15px;color:#666;background-color:#EAECEE;
    cursor:pointer;font-size:12px;
}
.titleBtn:hover{
    color:#000;
}
#username{
    float:right;height:32px;line-height:30px;padding-left:20px;padding-right:15px;color:#666;background-color:#EAECEE;
    cursor:pointer;font-size:12px;
}
#username:hover{
    color:#000;
}
.topmenu{
    float:left;height:40px;line-height:38px;padding-left:12px;padding-right:12px;background-color:#FFF;color:#000;
    cursor:pointer;font-size:14px;
}
.topmenu:hover{
    color:#999;
}
.footBtn{
    float:left;height:40px;line-height:38px;padding-left:12px;padding-right:12px;background-color:#FFF;color:#666;
    cursor:pointer;font-size:12px;
}
.footBtn:hover{
    color:#999;
}
#showBtn{
    width:150px;height:32px;line-height:32px;text-align:center;
    background-color:#108A03;font-size:16px;color:#FFF;cursor:pointer;
}
#showBtn:hover{
    background-color:#707070;
}

</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
$(document).ready(function(){

    $('#logo,#mainPage').click(function(){
        document.location.href="/index.html";
        return false;
    });
    
   
});
</script>  
</head>
<body class="metro">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
	<div id="north" data-options="region:'north',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="height:32px;background:#505050;">
	    <div id="logo" style="font-family:Microsoft Yahei;">RestON</div>
	    <div id="logoutBtn" class="titleBtn" style="font-family:Microsoft Yahei;">注销</div>
	    <div style="float:right;width:7px;height:32px;background-color:#EAECEE;">
	        <image src="/app/platform/image/splite.jpg" style="width:7px;height:20px;margin-top:6px;"/>
	    </div>
	    <div id="username" style="font-family:Microsoft Yahei;">管理员</div>
	</div>
	<div id="east" data-options="region:'east',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:0px;">
	
	</div>
	<div id="west" data-options="region:'west',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:0px;">
		
	</div>
	<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;">
	    <div style="height:40px;margin-top:5px;margin-left:10px;">
	        <div id="mainPage" class="topmenu">主页</div>
	        <div class="topmenu">产品</div>
	        <div class="topmenu">支持</div>
	        <div class="topmenu" style="color:#DE3901;">我的账户</div>
	        <div class="topmenu">功能导航</div>
	    </div>
	    
	    <div style="line-height:40px;margin-top:0px;margin-left:20px;color:#666;font-size:20px;">
	        我的账户
	    </div>
	    
	    <div style="line-height:40px;margin-top:0px;margin-left:20px;width:940px;color:#000;font-size:22px;background:#EDEDED;padding-left:10px;">
	        Restaurant Online产品
	    </div>
	    
	    <div style="line-height:40px;margin-top:40px;margin-left:20px;width:950px;color:#000;font-size:22px;">
	        <div id="showBtn" style="margin-left:800px;">访问应用商店</div>
	    </div>
	    
	</div>
	<div id="south" data-options="region:'south',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="height:41px;background:#FFF;border-top:solid 1px #707070;">
	    <div class="footBtn" style="font-family:Microsoft Yahei;margin-left:10px;">网站目录</div>
	    <div class="footBtn" style="font-family:Microsoft Yahei;margin-right:0px;">与我们联系</div>
	    <div class="footBtn" style="font-family:Microsoft Yahei;margin-right:0px;">法律条款</div>
	    <div class="footBtn" style="font-family:Microsoft Yahei;margin-right:0px;">商标</div>
	    <div class="footBtn" style="font-family:Microsoft Yahei;margin-right:0px;">隐私声明和 Cookie</div>
	    
	    <div class="footBtn" style="float:right;font-family:Microsoft Yahei;margin-right:0px;">© 2014 YiYun Corporation。保留所有权利。</div>
	</div>
</div>
</body>
</html>