<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
        window.close();
        return false;
    });
    
});
</script>  
<div id="north" data-options="region:'north',collapsible:false,split:false,border:false,style:{borderWidth:0}" style="height:40px;background:#262626;">
    <div id="logo" style="font-family:Microsoft Yahei;">RestON</div>
    <div id="logoutBtn" class="titleBtn" style="font-family:Microsoft Yahei;">退出</div>
    <div id="username" style="font-family:Microsoft Yahei;">${user.staffname}</div>
    <div id="account" style="font-family:Microsoft Yahei;">我的账户</div>
    <div class="titleBtn" style="font-family:Microsoft Yahei;">商城</div>
</div>
