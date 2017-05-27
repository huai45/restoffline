<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String[] green = {"#108A03","#18930C"};
String[] purple = {"#68217A","#8C459F"};
String[] color = purple;
String product = "RestaurantOn";




%>
<!DOCTYPE html>
<html>
<head>
<title>RestaurantOn Login</title>
<link rel="stylesheet" href="/resource/MetroUICSS/min/metro-bootstrap.min.css">
<link rel="stylesheet" href="/resource/MetroUICSS/min/metro-bootstrap-responsive.min.css">
<link rel="stylesheet" href="/resource/MetroUICSS/min/iconFont.min.css">
<style>
body{
    margin:0px;padding:0px;
}
#submitBtn{
    margin-top:40px;margin-left:40px;width:85px;height:32px;line-height:32px;text-align:center;
    background-color:<%= color[0] %>;font-size:14px;color:#FFF;font-family:Microsoft Yahei;cursor:pointer;
}
#submitBtn:hover{
    background-color:<%= color[1] %>;
}
</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/MetroUICSS/jquery/jquery.widget.min.js"></script>
<script src="/resource/MetroUICSS/min/metro.min.js"></script>
<script>
$(document).ready(function(){

    var height=$(document).height();
    var width = $(document).width();
    //alert( $(document).height() + " " + $(document).width() );
    if(width>1000){
        $("body").css("padding-left",(width-1000)/2);
    }
    if(height>620){
        $("body").css("padding-top",(height-620)/2);
    }
    /*
    $('#form').submit(function(){
        document.location.href="/app/index.jsp";
        return false;
    });
    */
    $('#submitBtn').click(function(){
        $('#form').submit();
        return false;
    });
    
});
</script>  
</head>
<body class="metro" style="margin:0px;padding:0px;padding-top:20px;padding-left:140px;">
<div id="center" style="height:620px;width:1000px;border:solid 0px red;position:relative;">
<image src="/app/login/image/mn.jpg" style="position:absolute;height:340px;width:480px;top:10px;left:40px;"/>
<div style="position:absolute;height:150px;width:480px;top:350px;left:40px;background:<%= color[0] %>;color:#FFF;font-size:24px;padding:20px;font-family:Microsoft Yahei;">
    在线餐饮管理平台，运营变得如此轻松<br/><span style="font-weight:bold;font-size:16px;"></span>
</div>
<div style="position:absolute;height:520px;width:400px;top:20px;right:20px;border:solid 0px #CCC;">
<form id="form" name="form" method="post" action="/login.action">
    <div style="margin-top:30px;margin-left:40px;font-size:32px;color:#666666;font-family:Microsoft Yahei;"><%= product %>平台</div>
    <div style="margin-top:30px;margin-left:40px;font-size:12px;color:#000;font-family:Microsoft Yahei;"><%= product %>账户</div>
    <div style="margin-top:10px;margin-left:40px;">
        <div class="input-control text" data-role="input-control">
            <input id="username" name="username" type="text" placeholder="帐号" autofocus style="width:320px;">
        </div>
    </div>
    <div style="margin-top:0px;margin-left:40px;">
        <div class="input-control password" data-role="input-control">
            <input id="password" name="password" type="password" placeholder="密码" autofocus style="width:320px;">
        </div>
    </div>
    <input id="loginbtn" type="submit" style=" position:absolute;top:-1000px;"/>
    <div id="submitBtn" style="">
        登录
    </div>
    <div style="position:absolute;top:210px;right:200px;">
        <h6 style="color: red;">
			<c:out value="${err_info}" />
		</h6>
    </div>
    <div style="margin-top:40px;margin-left:40px;">
        <a href="#" style="font-size:12px;color:#68217A;">无法访问你的账户？</a>
    </div>
    <div style="margin-top:110px;margin-left:40px;font-size:12px;">
        没有<%= product %>账户？<a href="#" style="color:#68217A;">立即申请</a>
    </div>
 </form>   
</div>


<div style="position:absolute;height:70px;width:960px;bottom:0px;left:20px;border-top:solid 1px #CCC;">
    <div style="position:absolute;top:10px;right:20px;color:#737373;font-weight:bold;font-size:14px;">
    <%= product %>
    </div>
    <div style="position:absolute;top:40px;left:20px;color:#666;font-size:12px;">
       ©2014 <%= product %>		 
    </div>
    <div style="position:absolute;top:40px;right:20px;color:#666;font-size:12px;">
       <a href="#" style="color:#333;">反馈</a>		 
    </div>
    <div style="position:absolute;top:40px;right:60px;color:#666;font-size:12px;">
       <a href="#" style="color:#333;">帮助中心</a>		 
    </div>
</div>

</div>
</body>
</html>