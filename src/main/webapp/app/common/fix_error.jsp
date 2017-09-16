<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@ page language="java" import="com.huai.common.domain.*"%>
<%@ page language="java" import="com.huai.common.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
    int errorNum = 0;
    JdbcTemplate jdbcTemplate = (JdbcTemplate)GetBean.getBean("jdbcTemplate");
    List itemList = jdbcTemplate.queryForList(" select * from tf_bill_item where 1 = 1 ",new Object[]{ });
    ut.p("itemList = "+itemList.size());
    for(int i=0;i<itemList.size();i++){
        Map item = (Map)itemList.get(i);
        String item_id = item.get("item_id").toString();
        try{
            String count = item.get("count").toString();
            Double.parseDouble(count);
        }catch(Exception e){
            errorNum++;
            e.printStackTrace();
            jdbcTemplate.update(" update tf_bill_item set count = 0 where item_id = ?",new Object[]{ item_id });
        }
    }
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<style>

</style>
<script type="text/javascript">

</script>
</head>
<body id="body" style="">

    <div id="startpage" style="height:50px;width:95%;padding-left:20px;line-height:50px;border-bottom:solid 0px #CCCCCC;font-size:20px;">
        <div id="" style="height:50px;width:100%;line-height:50px;border-bottom:solid 1px #CCCCCC;font-size:20px;">
         处理结果：
        </div>
    </div>
    <div id="notice" style="padding-top:30px;font-size:20px;">
	    <div style="height:60px;line-height:30px;padding-left:60px;">
	    发现 <%= errorNum %> 个问题，已经修复...
	    </div>
    </div>
</body>
</html>
