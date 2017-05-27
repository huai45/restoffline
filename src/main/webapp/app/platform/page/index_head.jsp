<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script>
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
        document.location.href="/logout.action";
        return false;
    });
    
});
</script>  
<div id="north" data-options="region:'north',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="height:40px;background:#262626;">
    <div id="logo" style="font-family:Microsoft Yahei;">RestON</div>
    <div id="logoutBtn" class="titleBtn" style="font-family:Microsoft Yahei;">注销</div>
    <div id="username" style="font-family:Microsoft Yahei;">${user.staffname}</div>
    <div id="account" style="font-family:Microsoft Yahei;">我的账户</div>
    <div class="titleBtn" style="font-family:Microsoft Yahei;">商城</div>
    <div class="titleBtn" style="font-family:Microsoft Yahei;">功能</div>
</div>
