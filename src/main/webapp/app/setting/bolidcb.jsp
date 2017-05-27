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
<title>RestOn会员管理系统</title>
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/icon.css">
<link rel="stylesheet" href="/resource/jquery-easyui-1.3.1/themes/metro/easyui.css">
<link rel="stylesheet" href="/app/cust/css/common.css">
<link rel="stylesheet" href="/app/cust/css/index_head.css">
<style>

</style>    
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script src="/resource/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
<script src="/resource/jQuery.jPlayer.2.6.0/jquery.jplayer.min.js"></script>
<script>
ajax_flag = 0;
$(document).ready(function(){
    
    $("#bolidcbpage").addClass("left_menu_sel");
    
    $('#categoryfileBtn').click(function(){
        var form=$("<form>");//定义一个form表单
		form.attr("style","display:none");
		form.attr("target","");
		form.attr("method","post");
		form.attr("action","/file/exportFoodCategoryFile.html");
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name","exportData");
		input1.attr("value",(new Date()).getMilliseconds());
		$("body").append(form);//将表单放置在web中
		form.append(input1);
		form.submit();//表单提交
	});
	
	$('#foodfileBtn').click(function(){
        var form=$("<form>");//定义一个form表单
		form.attr("style","display:none");
		form.attr("target","");
		form.attr("method","post");
		form.attr("action","/file/exportFoodFile.html");
		var input1=$("<input>");
		input1.attr("type","hidden");
		input1.attr("name","exportData");
		input1.attr("value",(new Date()).getMilliseconds());
		$("body").append(form);//将表单放置在web中
		form.append(input1);
		form.submit();//表单提交
	});
	
	var url = "http://translate.google.com/translate_tts?tl=zh&q=请12号到餐座就餐";
	var text = "请12号到餐座就餐";
	//window.open(url);
	
	$('#speakBtn').click(function(){
	    var text = "请12号到餐座就餐";
	    $("#mainFrame").attr("src",url+text);
	    //document.mp3.play();
	    //var audio = document.createElement("audio");
		//audio.src = url+text;
		//audio.play();
		//$("#container").html("<embed align=center src=dewplayer.swf?mp3="+url+text+"&autostart=1&autoreplay=0&volume=100  width=1 height=1 type=application/x-shockwave-flash wmode='transparent' quality='high' ;><param name='wmode' value='transparent'> </embed>");
	});
	
	
});
</script>  
</head>
<body class="metro">
<div id="cc" class="easyui-layout" data-options="fit:true,border:false" style="">
<%@ include file="/app/setting/page/index_head.jsp" %>
<%@ include file="/app/setting/page/index_west.jsp" %>
<div id="center" data-options="region:'center',border:false,style:{borderWidth:0}" style="padding:0px;background:#FFF;">
    <div style="height:41px;line-height:41px;width:90%;font-size:16px;border-bottom:solid #FFF 1px;margin:0px 40px 10px 0px;border-bottom:solid 1px #DDD;">
	    <div style="float:left;margin-left:20px;" >博立点菜宝更新文件生成</div>
    </div>
    <div id="" style="padding-left:20px;">
	    <div style="clear:both;"></div>
        <div style="margin-top:40px;margin-left:0px;">
            <div id="categoryfileBtn" class="btn_m" style="float:left;background:#094AB2;border:solid 1px #094AB2;">导出菜品类别表.TXT</div>
            <div id="foodfileBtn" class="btn_s" style="float:left;margin-left:30px;color:#094AB2;">导出菜品表.TXT</div>
	    </div>
	    <div style="clear:both;"></div>
	    <div style="display:none;margin-top:40px;margin-left:0px;">
            <div id="speakBtn" class="btn_m" style="float:left;background:#094AB2;border:solid 1px #094AB2;">语音播报</div>
	    </div>
    </div>
    
    <div id="container"></div>
    
    <div id="" style="display:none;">
        <iframe id='mainFrame' src='/transPage?page=/operation/blank' frameborder='0' style="height:100px;width:200px;display:none;"></iframe>
    </div>
</div>
</div>
</body>
</html>