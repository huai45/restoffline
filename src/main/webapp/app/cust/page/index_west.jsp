<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style>
.left_menu{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#000;cursor:pointer;border-left:solid 6px #FFF;
}
.left_menu_sel{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#D24726;cursor:pointer;border-left:solid 6px #D24726;
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
	<div style="height:41px;line-height:41px;width:100%;font-size:18px;color:#D24726;margin-top:0px;border-bottom:solid 1px #DDD;">
  	    <span style="margin-left:30px;">会员管理</span>
    </div>
    <div style="height:15px;line-height:15px;width:10px;font-size:12px;color:#999;">&nbsp;</div>
    <div id="indexpage" class="left_menu" url="/cust/index.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">首页</span>
    </div>
    <div id="vipcardquerypage" class="left_menu" url="/cust/vipcardquery.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">会员卡查询</span>
    </div>
    <div id="addvipcardpage" class="left_menu" url="/cust/addvipcardpage.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">新建会员</span>
    </div>
    <div id="vipcardlistpage" class="left_menu" url="/cust/vipcardlistpage.html" onselectstart='return false'>
        <span style="margin-left:20px;">会员卡管理</span>
    </div>
<%--    <div id="writevipcardpage" class="left_menu" url="/cust/writevipcardpage.html" onselectstart='return false'>--%>
<%--  	    <span style="margin-left:20px;">制作会员卡</span>--%>
<%--    </div>--%>
    <div id="creditquerypage" class="left_menu" url="/cust/creditquery.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">挂账用户查询</span>
    </div>
    <div id="addcreditpage" class="left_menu" url="/cust/addcreditpage.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">新建挂账用户</span>
    </div>
</div>  
</div>
