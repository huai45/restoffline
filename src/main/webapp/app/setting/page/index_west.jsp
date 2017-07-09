<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style>
.left_menu{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#000;cursor:pointer;border-left:solid 6px #FFF;
}
.left_menu_sel{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#094AB2;cursor:pointer;border-left:solid 6px #094AB2;
}
</style> 
<script>
$(document).ready(function(){
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
	
});
</script>  
<div id="west" data-options="region:'west',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="width:200px;margin:0px;padding:0px;">
<div style="width:190px;">
	<div style="height:41px;line-height:41px;width:100%;font-size:18px;color:#094AB2;margin-top:0px;border-bottom:solid 1px #DDD;">
  	    <span style="margin-left:30px;">系统设定</span>
    </div>
    <div style="height:15px;line-height:15px;width:10px;font-size:12px;color:#999;">&nbsp;</div>
    <div id="indexpage" class="left_menu" url="/setting/index.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">首页</span>
    </div>
    <div id="foodmanagepage" class="left_menu" url="/transPage?page=/setting/page/food_manage" onselectstart='return false'>
  	    <span style="margin-left:20px;">菜品管理</span>
    </div>
    <div id="foodcategorypage" class="left_menu" url="/transPage?page=/setting/page/food_category" onselectstart='return false'>
  	    <span style="margin-left:20px;">菜品分类管理</span>
    </div>
    <div id="phoneUsermanagepage" class="left_menu" url="/transPage?page=/setting/page/android_user_manage" onselectstart='return false'>
  	    <span style="margin-left:20px;">手机点菜宝管理</span>
    </div>
    
</div>  
</div>
