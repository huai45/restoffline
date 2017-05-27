Ext.onReady(function(){

	$(".food").live({
	    mouseenter:function(){
	        $(this).addClass("food_hover");
	    },
	    mouseleave:function(){
	        $(this).removeClass("food_hover");
	    }
	});
	
	function countFoodPrice(){
        var total_price = 0;
        if($(".addfooditem").length==0){
            return 0;
        }
        $(".addfooditem").each(function(){
            //alert($(this).attr("price"));
            //alert($(this).children().eq(3).val());
            total_price = total_price + $(this).attr("price")*$(this).children().eq(2).val();      
        });
        return total_price.toFixed(2);
    }
    
    $('.food').click(function(){
        var note = $.trim($("#addfood_total_note").val());
        if(note!=''){
            note = "  ( "+note+" )";
        }
        var html = '<div class="addfooditem" food_id = "'+$(this).attr("food_id")+'" food_name = "'+$(this).attr("food_name")+'" price = "'+$(this).attr("price")+'" note="" >'+
            '<div class="addfoodname">'+$(this).attr("food_name")+note+'</div>'+
            '<div class="addfoodprice">￥'+$(this).attr("price")+'/'+$(this).attr("unit")+'</div>'+
            '<input class="addfoodcount" type="text" value="1"/>'+
            '<div class="calltype hurry" onselectstart="return false">即起</div>'+
            '<div class="addfoodnote" onselectstart="return false">加备注</div>'+
	        '<div class="deleteFood" onselectstart="return false">删除</div>'+
	        '</div>';
	    $('#addfoodlist').append(html);
        $(this).appendTo("#selectlist");
        $(this).removeClass("food_hover");
        $("#query_food_str").val('');
        $("#query_food_str")[0].focus();
        $('#select_food_count').text($(".addfooditem").length);
        $('#select_food_money').text(countFoodPrice());
	});
	
	$(".calltype").live("click",function(){
        if($(this).hasClass("hurry")){
            $(this).removeClass("hurry").addClass("wait").text('叫起');
        } else {
            $(this).removeClass("wait").addClass("hurry").text('即起');
        }
        return false;
    })
    
	$("#all_jiqi").click(function(){
        $("#addfoodlist .calltype").removeClass("wait").addClass("hurry").text('即起');
        return false;
    })
    
    $("#all_jiaoqi").click(function(){
        $("#addfoodlist .calltype").removeClass("hurry").addClass("wait").text('叫起');
        return false;
    })
    
    $(".addfoodcount").live("change",function(){
        $('#select_food_money').text(countFoodPrice());
        return false;
    })
    
    $(".deleteFood").live("click",function(){
        //alert( $("#title11").width() );
	    $("#food_"+$(this).parent().attr("food_id")).appendTo("#foodlist");
	    $(this).parent().remove();
	    $("#query_food_str")[0].focus();
	    $('#select_food_count').text($(".addfooditem").length);
	    $('#select_food_money').text(countFoodPrice());
	    return false;
	});
	
	function fn(){
	    $("#note_input").focus();
	}
	var task = new Ext.util.DelayedTask(fn);
	
	$("#add_note").click(function(){
        food_note_window.show();
        window_tag = 1;
        $("#pagefrom").val('all');
        $("#note_input").val($("#addfood_total_note").val());
	    task.delay(50);
    });
    
    $("#add_note_package").click(function(){
        food_note_window.show();
        window_tag = 1;
        $("#pagefrom").val('all_package');
        $("#note_input").val($("#selectpackage_total_note").val());
	    task.delay(50);
    });
    
	$(".addfoodnote").live("click",function(){
	    $("#pagefrom").val($(this).parent().attr("food_id"));
        food_note_window.show();
        window_tag = 1;
        var note = $(this).parent().attr("note");
        $("#note_input").val(note);
	    task.delay(50);
    });
    
    $(".note_item").click(function(){
        if( $.trim($("#note_input").val())==''){
            $("#note_input").val($.trim($(this).text()));
            $("#note_input").focus();
            return false;
        }
        $("#note_input").val($("#note_input").val()+""+$.trim($(this).text()));
        $("#note_input").focus();
    });
    
    $("#clearNoteInputBtn").live("click",function(){
        $("#note_input").val('');
        $("#note_input").focus();
    });
    
    function submitNoteForm(){
        if($("#pagefrom").val()=='all'){
            $("#addfood_total_note").val( $.trim($("#note_input").val()) );
            $("#addfoodlist .addfooditem").each(function(){
                var food_name = $(this).attr("food_name");
                var note = $("#addfood_total_note").val()+$(this).attr("note");
                if($.trim(note)==''){
                    $(this).children(".addfoodname").text(food_name);
                }else{
                    $(this).children(".addfoodname").text(food_name+"  ( "+note+" )");
                }
            });
        } else if($("#pagefrom").val()=='all_package'){
            $("#selectpackage_total_note").val( $.trim($("#note_input").val()) );
            $("#packageitemlist .addfooditem").each(function(){
                var food_name = $(this).attr("food_name");
                var note = $("#selectpackage_total_note").val()+$(this).attr("note");
                if($.trim(note)==''){
                    $(this).children(".addfoodname").text(food_name);
                }else{
                    $(this).children(".addfoodname").text(food_name+"  ( "+note+" )");
                }
            });
        } else{
            var food_id = $("#pagefrom").val();
            var divname = "#addfoodlist ";
            var note = $("#addfood_total_note").val()+$("#note_input").val();
            if($("#packageitemlist .addfooditem").length>0){
                divname = "#packageitemlist ";
                note = $("#selectpackage_total_note").val()+$("#note_input").val();
            }
            var food_name = $(divname+".addfooditem[food_id="+food_id+"]").attr("food_name");
            if($.trim(note)==''){
                $(divname+".addfooditem[food_id="+food_id+"]").children(".addfoodname").text(food_name);
            }else{
                $(divname+".addfooditem[food_id="+food_id+"]").children(".addfoodname").text(food_name+"  ( "+note+" )");
            }
            $(divname+".addfooditem[food_id="+food_id+"]").attr("note",$.trim($("#note_input").val()));
        }
        food_note_window.hide();
        window_tag = 0;
        $("#query_food_str")[0].focus();
    }
    
    $("#submitNoteBtn").click(function(){
        submitNoteForm();
        return false;
    });
    
    $("#note_form").submit(function(){
        submitNoteForm();
        return false;
    });
    
    $("#closeNoteWinBtn").click(function(){
        food_note_window.hide();
        window_tag = 0;
        $("#query_food_str")[0].focus();
    });
	
	
	$("#query_food_str").live("keyup",function(){
        if($("#query_food_str").val()==''){
            $(".food").appendTo("#hiddenlist");
	        $(".food[show_tag=Y]").appendTo("#foodlist");
	        return false;
	    }
        $("#foodlist").children().appendTo("#hiddenlist");
        $("#hiddenlist").children("div[abbr^="+$("#query_food_str").val().toUpperCase()+"]").appendTo("#foodlist");
        $("#hiddenlist").children("div[food_name*="+$("#query_food_str").val().toUpperCase()+"]").appendTo("#foodlist");
        $("#hiddenlist").children("div[food_id="+$("#query_food_str").val().toUpperCase()+"]").appendTo("#foodlist");
	});
	
	$('#backBtnFromAddFood').click(function(){
        Ext.getCmp('center-main-container').getLayout().setActiveItem('tableinfo');
        resetAddFoodPage();
        $("#tableinfo_hidden_input")[0].focus();
	});
	
    $("#addFoodSubmitBtn").live("click",function(){
        if(ajax_flag > 0){
	        return false;
	    }
	    if($("#addfoodlist .addfooditem").length==0){
	        alert('请先点菜再下单!');
	        return false;
	    }
	    ajax_flag = 1;
	    var foods = [];
        var total_note = "";
        if( $.trim($("#addfood_total_note").val())!='' ){
            total_note = $.trim($("#addfood_total_note").val());
        }
        $("#addfoodlist .addfooditem").each(function(){
		    var item = {};
		    var printer_name=$("#food_"+$(this).attr("food_id")).attr("printer");
		    item.food_id=$(this).attr("food_id");
		    item.count=$(this).children().eq(2).val();
		    if($(this).children().eq(3).hasClass("hurry")){
	            item.call_type="1";
	        } else {
	            item.call_type="0";
	        }
		    item.note=total_note+""+$.trim($(this).attr("note"));
		    foods.push(item);
		});
	    //alert(" addFood : "+Ext.encode(foods));
	    $.post("/operation/addFood.html",{
		        bill_id : $("#addfood_bill_id").text(),
		        item_str : Ext.encode(foods)
		    },function(result){
		        ajax_flag = 0;
	            var obj = Ext.decode(result);	            
	            if(obj.success=='true'){
	                initTableInfoPage(obj.bill)
		            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
		            $("#tableinfo_hidden_input")[0].focus();
	            }
	            else{
	                alert(obj.msg);
	            }
		    }
		).error(function(){
		    alert("系统异常"); 
		});
		return false;
	});
	
	
    
});