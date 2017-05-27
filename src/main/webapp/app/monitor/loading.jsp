<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
String username = "admin";
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="/resource/MetroUICSS/min/metro-bootstrap.min.css">
<link rel="stylesheet" href="/resource/MetroUICSS/min/metro-bootstrap-responsive.min.css">
<link rel="stylesheet" href="/resource/MetroUICSS/min/iconFont.min.css">
<style>

</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/MetroUICSS/jquery/jquery.widget.min.js"></script>
<script src="/resource/MetroUICSS/min/metro.min.js"></script>
<script>
    
</script> 
</head>
<body class="metro" style="margin:0px;padding:0px;overflow:hidden;">
<div id="" style="margin:300px;margin-top:250px;margin-bottom:0px;height:200px;" >
    <div class="">
	   <h4>正在加载系统文件，请稍候</h4>
	</div>
	<div style="margin-top:20px;">
	   <div class="progress-bar small" data-role="progress-bar" id="pb1"></div>
	   <button class="button " id="reset_btn" style="margin-top:20px;">取消</button>
	   <script>
	       $(function(){
	           var pb = $('#pb1').progressbar();
	           var progress = 0;
	           
	           var interval = setInterval(
		           function(){
		               pb.progressbar('value', (++progress));
		               if (progress == 100) {
		                   progress = 0;
		                   //window.clearInterval(interval);
		               }
		           }, 100);
	                       
	           $("#pb_start_btn").on('click', function(){
	               var interval = setInterval(
	                       function(){
	                           pb.progressbar('value', (++progress));
	                           if (progress == 100) {
	                               progress = 0;
	                               //window.clearInterval(interval);
	                           }
	                       }, 100);
	           });
	
	           $("#reset_btn").on('click', function(){
	               window.close();
	           });
	       });
	   </script> 
	</div>
</div>
<link rel="stylesheet" href="/resource/ext4/resources/css/ext-all.css" />
<link rel="stylesheet" href="/resource/ext4/resources/css/ext-all-gray.css" />
<script src="/resource/ext4/ext-all.js"></script>
<script>
    document.location.href="/monitor/index.html";
</script>  
</body>
</html>