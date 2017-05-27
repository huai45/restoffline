<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style>
.left_menu{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#000;cursor:pointer;border-left:solid 6px #FFF;
}
.left_menu_sel{
    height:32px;line-height:32px;width:100%;font-size:14px;color:#80397B;cursor:pointer;border-left:solid 6px #80397B;
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
	<div style="height:41px;line-height:41px;width:100%;font-size:18px;color:#80397B;margin-top:0px;border-bottom:solid 1px #DDD;">
  	    <span style="margin-left:30px;">统计查询</span>
    </div>
    <div style="height:15px;line-height:15px;width:10px;font-size:12px;color:#999;">&nbsp;</div>
    <div id="indexpage" class="left_menu" url="/query/index.html" onselectstart='return false'>
  	    <span style="margin-left:20px;">首页</span>
    </div>
    <div id="salemoneypage" class="left_menu" url="/transPage?page=/query/querySaleMoney" onselectstart='return false'>
  	    <span style="margin-left:20px;">营业收入统计</span>
    </div>
    
    <div id="waterquerypage" class="left_menu" url="/transPage?page=/query/queryWaterSale" onselectstart='return false'>
  	    <span style="margin-left:20px;">酒水查询</span>
    </div>
    
    <div id="categoryquerypage" class="left_menu" url="/transPage?page=/query/queryCategoryHistory" onselectstart='return false'>
  	    <span style="margin-left:20px;">档口统计</span>
    </div>
    <div id="categorydetailpage" class="left_menu" url="/transPage?page=/query/queryCategoryDetail" onselectstart='return false'>
  	    <span style="margin-left:20px;">档口销售明细</span>
    </div>
    
    <!-- @yuanyl@2014/08/13  begin-->
    <div id="floorgreensaildetailpage" class="left_menu" url="/transPage?page=/query/queryFloorGreensSaleDetail" onselectstart='return false'>
  	    <span style="margin-left:20px;">档口楼层菜品销售明细</span>
    </div>
    <!-- @yuanyl@2014/08/13  end-->
    
    
     <!-- @yuanyl@2014/08/09  begin-->
    <div id="floorsaildetailpage" class="left_menu" url="/transPage?page=/query/queryFloorSaleDetail" onselectstart='return false'>
  	    <span style="margin-left:20px;">档口楼层销售统计</span>
    </div>
    <!-- @yuanyl@2014/08/09  end-->
    
    <div id="queryStaffMoney" class="left_menu" url="/transPage?page=/query/queryStaffMoney" onselectstart='return false'>
  	    <span style="margin-left:20px;">员工业绩</span>
    </div>
    
    <div id="queryStaffMoneyByDay" class="left_menu" url="/transPage?page=/query/queryStaffMoneyByDay" onselectstart='return false'>
  	    <span style="margin-left:20px;">员工业绩(个人)</span>
    </div>
    
    <div id="queryStaffMoneyByFood" class="left_menu" url="/transPage?page=/query/queryStaffMoneyByFood" onselectstart='return false'>
  	    <span style="margin-left:20px;">员工业绩(菜品)</span>
    </div>

    <div id="queryStaffDetail" class="left_menu" url="/transPage?page=/query/queryStaffDetail" onselectstart='return false'>
  	    <span style="margin-left:20px;">个人销售查询</span>
    </div>
    
    <div id="queryFoodDetail" class="left_menu" url="/transPage?page=/query/queryFoodDetail" onselectstart='return false'>
  	    <span style="margin-left:20px;">菜品销售查询</span>
    </div>
    
</div>  
</div>
