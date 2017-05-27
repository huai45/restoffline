Ext.onReady(function(){

    $('#open_table_nopinput').live("keyup",function(e){
	    //alert($(this).val());
	    if($.trim($('#open_table_nopinput').val())==''){
	        $('#open_table_nopinput').val('');
            $('#open_table_nopinput')[0].focus();
        }
	    if( isNaN($(this).val())||$(this).val().length > 2 ){
	        $(this).val( $(this).val().substring(0,$(this).val().length-1) );
	    }else {
	    }
	});
	
	$('#backBtnFromOpenTableBtn').click(function(){
        resetTablePage();
        Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
        resetOpenTablePage();
	});
	
	function fn_tableinfo_hidden_input_task(){
	    $("#tableinfo_hidden_input").focus();
	}
	var tableinfo_hidden_input_task = new Ext.util.DelayedTask(fn_tableinfo_hidden_input_task);
	
	function openTable(table_id,nop) {
	    if(nop==undefined||nop=='null'||nop==''||nop==0||isNaN(nop)){
	        nop = 3;
	    }
		//alert(" openTable   table_id = "+table_id+",nop = "+nop);
		$.post("/operation/openTable.html",{
		        table_id : table_id ,
		        nop : nop
		    },function(result){
	            var obj = Ext.decode(result);	            
	            if(obj.success=='false'){
	                return false;
	            }
	            initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#table_icon_"+table_id).addClass("busy");
	            //$("#tableinfo_hidden_input")[0].focus();
	            tableinfo_hidden_input_task.delay(50);
		    }
		).error(function(){
		    alert("系统异常"); 
		});
		return false;
	}

    $("#open_table_form").submit(function(){
	    var table_id = $("#open_table_id").val();
	    var nop = $('#open_table_nopinput').val();
	    openTable(table_id,nop);
	    return false;
    });
	
	$("#openTableBtn").click(function(){
	    $("#open_table_form").submit();
	    return false;
    });
    
});