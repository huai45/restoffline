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
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css">
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css">
<link rel="stylesheet" href="/app/platform/css/common.css">
<link rel="stylesheet" href="/app/platform/css/index_head.css">
<link rel="stylesheet" href="/app/platform/css/index_foot.css">
<style>
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

</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script>
$(document).ready(function(){

    
});
</script>  
</head>
<body class="metro">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
	<%@ include file="/app/platform/page/index_head.jsp" %> 
	<div id="west" data-options="region:'west',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:0px;">
		
	</div>
	<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;color:#000;">
	    
	</div>
	<%@ include file="/app/platform/page/index_foot.jsp" %>
</div>
</body>
</html>