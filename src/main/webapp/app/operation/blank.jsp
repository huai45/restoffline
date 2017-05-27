<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
%>
<!DOCTYPE html>
<html>
<head>
<style>

</style>
<script src="/resource/jquery-easyui-1.3.1/jquery-1.8.0.min.js"></script>
<script>
$(document).ready(function(){
    $(document).keydown(function(event){
        if(event.keyCode==8){
            return false;
        }
        if(event.keyCode==27){
            top.resetTablePage();
            top.Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
            top.$("#smart_str")[0].focus();
            //top.$("#mainFrame").attr("src","/app/operation/blank.jsp?time="+new Date());
        }
    });
});
</script> 
</head>
<body style="margin:0px;padding:0px;">

</body>
</html>