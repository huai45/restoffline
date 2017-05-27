Ext.onReady(function(){

	var focusTask = {
		run : function() {
		    if(Ext.getCmp('viewport').layout.activeItem.id=='deskpage'){
		        $("input").blur();
		        $("#smart_str")[0].focus();
		    }else if(Ext.getCmp('viewport').layout.activeItem.id=='tableinfopage'){
		        $("input").blur();
		        $("#tableinfo_hidden_input")[0].focus();
		    }else if(Ext.getCmp('viewport').layout.activeItem.id=='paycashpage'){
		        $("input").blur();
		        $("#recvfeeinput")[0].focus();
		    }else if(Ext.getCmp('viewport').layout.activeItem.id=='addfoodpage'){
		        if(window_tag==0){
		            $("#query_food_str")[0].focus();
		        }else{
		            
		        }
		    }else if(Ext.getCmp('viewport').layout.activeItem.id=='operatefoodpage'){
		        $("input").blur();
		        $("#operate_value")[0].focus();
		    }else if(Ext.getCmp('viewport').layout.activeItem.id=='vipcardpaypage'){
		        $("input").blur();
		        $("#vipcardpayfeeinput")[0].focus();
		    }else if(Ext.getCmp('viewport').layout.activeItem.id=='creditpaypage'){
		        $("input").blur();
		        $("#creditpayfeeinput")[0].focus();
		    }
		},
		interval : 1000
	}
	Ext.TaskManager.start(focusTask);
    
    var queryTableStateTask = {
		run : function() {
		    if(Ext.getCmp('viewport').layout.activeItem.id=='deskpage'){
		        $.post("/operation/queryAllTableState.html",{
		            
				    },function(result){
			            var obj = Ext.decode(result);
			            if(obj.success=='true'){
			                for(var i=0;i<obj.tables.length;i++){
			                    //alert(obj.tables[i].table_id+" = "+obj.tables[i].state);
					            //$("#table_"+obj.tables[i].TABLE_ID).attr("state",obj.tables[i].STATE);
					            if(obj.tables[i].STATE=='0'){
					                $("#table_icon_"+obj.tables[i].TABLE_ID).removeClass("busy");
					            }else if(obj.tables[i].STATE=='1'){
					                $("#table_icon_"+obj.tables[i].TABLE_ID).addClass("busy");
					            }else if(obj.tables[i].STATE=='9'){
					                
					            }
							}
			            }
				    }
				).error(function(){
				});
		    }else{
		        
		    }
		},
		interval : querytabletime
	}
	Ext.TaskManager.start(queryTableStateTask);
	
	var printTask = {
		run : function() {
		     $.ajax({  
		         type: "Get",  
		         url: printtaskurl+"?appid="+appid+"&queryfoodurl="+queryfoodurl+"&querybillurl="+querybillurl+"&backurl="+backurl+"&ftime="+ftime+"&btime="+btime+"&querybillinfourl="+querybillinfourl,
		         cache: false,  
		         jsonp: "callback",  
		         dataType: "jsonp",  
		         success: function (result) {  
		             //alert(result.msg);   
		         }
		     });
		},
		interval : printtasktime
	}
	Ext.TaskManager.start(printTask);
	
});