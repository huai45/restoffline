<html>
<head>
<script src="/resource/jquery/jquery-1.7.min.js"></script>
<script>
$(function(){
    alert(123)

    function getSessionId(){
        var c_name = 'JSESSIONID';
        if(document.cookie.length>0){
            alert("document="+document);
            c_start=document.cookie.indexOf(c_name + "=")
            if(c_start!=-1){
                c_start=c_start + c_name.length+1
                c_end=document.cookie.indexOf(";",c_start)
                if(c_end==-1) c_end=document.cookie.length
                return unescape(document.cookie.substring(c_start,c_end));
            }
        }
    }
    /*
        //document.cookie="pin=test;domain=huai23.com;";
        $.ajax({
            url: "http://www.huai23.com:9999/test.jsp",
            type: "post",
            data: {name:"fdipzone",gender:"male"},
            dataType: "text",
            xhrFields: {
                withCredentials: true
            },
            crossDomain: true,
            success:function(result){
                alert(1);
            },
            error:function(){
            }
        });


 $.post("http://www.huai23.com:9999/test.jsp",{ data:{name:"fdipzone",gender:"male"},crossDomain:true, xhrFields: {  withCredentials: true  }})
            .done(function(data){
                alert("data="+data+",cookie="+document.cookie)
            });
*/
 document.cookie="pin=test;domain=huai23.com;";
    $.ajax({
        url:"http://www.huai23.com:9999/test.jsp",
        type: "POST",
        dataType : 'json',
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        success:function(result){
            alert("test");
        },
        error:function(XMLHttpRequest, textStatus, errorThrown) {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
        }
    });


})
</script>
</head>
<body>
welcome ${name}  to freemarker!1
</body>
</html>