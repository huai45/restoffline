<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%
//String msg = (String)request.getAttribute("msg");
//System.out.print(msg);
out.print(request.getAttribute("msg")==null?"":request.getAttribute("msg"));%>