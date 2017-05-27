Ext.onReady(function() {
    
    $('#logo').click(function(){
        document.location.href="/operation/index.html";
        return false;
    });
    
    $('#waterBtn').click(function(){
        $("#mainFrame").attr("src","/query/water.html?time="+new Date());
	    Ext.getCmp('viewport').getLayout().setActiveItem('apppage');
	});
	
	$('#settingBtn').click(function(){
	    $("#mainFrame").attr("src","/query/setting.html?time="+new Date());
	    Ext.getCmp('viewport').getLayout().setActiveItem('apppage');
	});
	
    $('#logoutBtn').click(function(){
        //window.close();
        document.location.href="/logout.action";
        return false;
    });
    
    $(".floor_name").toggle(
	    function(){
	        $(this).css("color","#999");
	        $("#floor_"+$(this).attr("floor_id")).children().hide();
	    },
	    function(){
	        $(this).css("color","#FFF");
	        $("#floor_"+$(this).attr("floor_id")).children().show();
	    }
	);
	
	$("#smartBtn").toggle(
	    function(){
	        //Ext.getCmp('center-deskcenterpage-container').getLayout().setActiveItem('quicktablepage');
	    },
	    function(){
	        //Ext.getCmp('center-deskcenterpage-container').getLayout().setActiveItem('tablepanelpage');
	    }
	);
	
	$("#smartBtn").click(function(){
        //Ext.getCmp('center-deskcenterpage-container').getLayout().setActiveItem('quicktablepage');
    });
	
	$("#smart_str").live("keydown",function(event){
	    //alert(event.keyCode);
	    //空格键 切换功能按钮
	    var str = $.trim( $("#smart_str").val() );
	    if($(".smart_table_sel").length==1){
	        if(event.keyCode==32 && $(".smart_table_sel").length==1){
		        changeSelTableButton();
		        return false;
		    }
	    }else{
	        if(event.keyCode==32){
		        return false;
		    }
	    }
	    
	});
	
	$("#smart_str").live("keyup",function(event){
	    //alert(event.keyCode);
	    if(event.keyCode==32){
	        return false;
	    }
	    var str = $.trim( $("#smart_str").val() );
	    //当有台位被选中的时候   快捷键功能优先适配
	    if($(".smart_table_sel").length==1){
	        if(event.keyCode==13&&table_patrn.exec(str)){
	            //alert("enter进入");
	            queryTableSubmit(str);
                return false;
	        }else if(event.keyCode==17){
	            //alert("打印账单");
	            printBill(str);
                return false;
	        }else if(event.keyCode==16){
	            quickPayCash(str)
                return false;
	        }else if(event.keyCode==18){
	            //alert("alt键暂未启用");
                return false;
	        }else if(event.keyCode>64&&event.keyCode<91){
	            //alert("快捷点菜, str = "+$(".smart_table_sel").attr("table_id"));
	            quickAddFood($(".smart_table_sel").attr("table_id"));
	            return false;
	        }
        }
	    if(str==""){
	        $("#smart_str").val('');
	        if(event.keyCode==13){
	            return false;
	        }
	        resetTablePage();
	        return false;
	    }
	    queryTable(str);
	    return false;
	});
	
	// 快捷按键1
	$('#quickTableInfoBtn').live("click",function(){
	    if($(".smart_table_sel").length==1){
	        var table_id = $(".smart_table_sel").attr("table_id");
	        showBill(table_id);
	        //resetTablePage();
	    }
    });
    
    // 快捷按键2
	$('#quickPayCashBtn').live("click",function(){
	    if($(".smart_table_sel").length==1){
	        var table_id = $(".smart_table_sel").attr("table_id");
	        quickPayCash(table_id);
            return false;
	    }
    });
    
    // 快捷按键3
	$('#quickPayByCardBtn').live("click",function(){
	    if($(".smart_table_sel").length==1){
	        var table_id = $(".smart_table_sel").attr("table_id");
	        quickPayByCard(table_id);
            return false;
	    }
    });
    
    // 快捷按键4
	$('#quickPrintBillBtn').live("click",function(){
	    if($(".smart_table_sel").length==1){
	        var table_id = $(".smart_table_sel").attr("table_id");
	        printBill(table_id);
            return false;
	    }
    });
    
    // 快捷按键5
	$('#quickAddFoodBtn').live("click",function(){
	    if($(".smart_table_sel").length==1){
	        var table_id = $(".smart_table_sel").attr("table_id");
	        quickAddFood(table_id);
            return false;
	    }
    });
    
    // 快捷按键6
	$('#quickQueryBillBtn').live("click",function(){
	    if($(".smart_table_sel").length==1){
	        var table_id = $(".smart_table_sel").attr("table_id");
	        $("#mainFrame").attr("src","/query/todaybill.html?table_id="+table_id+"&time="+new Date());
	        Ext.getCmp('viewport').getLayout().setActiveItem('apppage');
            return false;
	    }
    });
    
	$('.table').live("click",function(){
	    var table_id = $(this).attr("table_id");
	    showBill(table_id);
    });
	
	$('#backBtnFromTableInfo').live("click",function(){
	    resetTablePage();
        Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
        $("#smart_str")[0].focus();
    });
	
	$("#tableinfo_hidden_input").live("keyup",function(event){
	    //alert(event.keyCode);
	    if(Ext.getCmp('viewport').layout.activeItem.id=='tableinfopage'){
	        if(event.keyCode==16){
	            Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
		        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
		        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
		        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
		        $("#paycash_backfee").text('0');
		        $("#mode_id").text('xj');
		        $("#mode_name").text('现金');
		        $('#recvfeeinput')[0].focus();
	        }else if(event.keyCode==99999){
	            Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
		        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
		        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
		        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
		        $("#paycash_backfee").text('0');
		        $("#mode_id").text('sk');
                $("#mode_name").text('刷卡');
		        $('#recvfeeinput')[0].focus();
	        }else if(event.keyCode==17){
	            printBill( $("#tableinfo_table_id").text() );
	            return false;
	        }else  if(event.keyCode>64&&event.keyCode<91){
	            resetAddFoodPage();
	            $("#addfood_bill_id").text( $("#tableinfo_bill_id").text() );
		        Ext.getCmp('viewport').getLayout().setActiveItem('addfoodpage');
		        $("#query_food_str")[0].focus();
	        }else  if(event.keyCode == 46){
	            var bill_id = $("#tableinfo_bill_id").text();
			    var table_id = $("#tableinfo_table_id").text();
			    closeBill(bill_id,table_id);
	        }
	    }
	    $("#tableinfo_hidden_input").val('');
	    return false;
	});
	
	$('.item_0,.item_1,.item_2').live("click",function(){
	    if($(this).hasClass($(this).attr("clazz")+"_select")){
	        $(this).removeClass($(this).attr("clazz")+"_select");
	        $(this).children().eq(1).children().removeAttr("checked").hide();
	    }else {
	        $(this).addClass($(this).attr("clazz")+"_select");
            $(this).children().eq(1).children().attr("checked",true).show();
	    }
	    changeSelectCount();
	});
	
	$('#allpick').toggle(function(){
	    $(this).text('反选');
        $(".item_0,.item_1,.item_2").each(function(){
            if($(this).hasClass($(this).attr("clazz")+"_select")){
		    }else{
		        $(this).addClass($(this).attr("clazz")+"_select");
		    }
            $(this).children().eq(1).children().attr("checked",true).show();
            changeSelectCount();
		});
	},function(){
	    $(this).text('全选');
        $(".item_0,.item_1,.item_2").each(function(){
		    $(this).removeClass($(this).attr("clazz")+"_select");
	        $(this).children().eq(1).children().removeAttr("checked").hide();
	        changeSelectCount();
		});
	});
	
    $("#addFoodBtn").click(function(){
        resetAddFoodPage();
        $("#addfood_bill_id").text( $("#tableinfo_bill_id").text() );
        Ext.getCmp('viewport').getLayout().setActiveItem('addfoodpage');
        $("#query_food_str")[0].focus();
    });
    
    $("#paycashBtn").click(function(){
        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
        $("#paycash_backfee").text('0');
        $("#mode_id").text('xj');
        $("#mode_name").text('现金');
        Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
        $("#recvfeeinput")[0].focus();
    });
    
    $("#payByCardBtn").click(function(){
        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
        $("#paycash_backfee").text('0');
        $("#mode_id").text('sk');
        $("#mode_name").text('刷卡');
        Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
        $("#recvfeeinput")[0].focus();
    });
    
    $("#payByChequePageBtn").click(function(){
        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
        $("#paycash_backfee").text('0');
        $("#mode_id").text('zp');
        $("#mode_name").text('支票');
        Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
        $("#recvfeeinput")[0].focus();
    });
    
    $("#payByWechatBtn").click(function(){
        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
        $("#paycash_backfee").text('0');
        $("#mode_id").text('wx');
        $("#mode_name").text('微信');
        Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
        $("#recvfeeinput")[0].focus();
    });
    
    $('#recvfeeinput').live("keyup",function(){
	    var zhaoling = 0- ( Number($('#paycash_owefee').text()) - Number($('#recvfeeinput').val()) );
	    if(Number($('#recvfeeinput').val())==0){
	        $('#paycash_backfee').text('0');
	    }else if(isNaN(zhaoling)){
	        $('#paycash_backfee').text('0');
	    }else if(zhaoling==0){
	        $('#paycash_backfee').text('0');
	    }else if(zhaoling<0){
	        $('#paycash_backfee').text('0');
	    }else{
	        $('#paycash_backfee').text(zhaoling.toFixed(0));
	    }
	});
	
	$('#recvfeeBtn').click(function() {
        payCash();
	});
	
    $('#paycashform').submit(function() {
        payCash();
        return false;
	});
	
	$('#cancelRecvFeeBtn').click(function() {
	    resetPayCashPage();
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
	});
	
	var isReading = 0;
	
    $("#payByVipCardPageBtn").click(function(){
        //if (isReading==0) {
        if (true) {
            isReading = 1;
		    var data = {};
		    data.type="readcard";
		    var socket = new WebSocket(cardsocketurl); 
			    // 打开Socket 
				socket.onopen = function(event) { 
					// 发送一个初始化消息
					socket.send(JSON.stringify(data)); 
					// 监听消息
					socket.onmessage = function(event) { 
					    var card_no = event.data;
					    //socket.close();
					    isReading = 0;
					    if(card_no == 'RETRY'){
				            alert( '请重新将卡放在读卡器上读卡！' );
				            return false;
				        }else if(card_no.length==32){
				            card_no = card_no.substr((32-4) , 32);
				            if(ajax_flag > 0){
						        return false;
						    }
					        ajax_flag = 1;
					        $.post("/query/queryVipCardUserList.html", {
					                card_no : card_no 
					            },function (result) {
								var obj = Ext.decode(result);
								ajax_flag = 0;
								if (obj.success == "true") {
								    if (obj.data.length > 0) {
								        initSelectVipCardPage(obj.data);
						                Ext.getCmp('viewport').getLayout().setActiveItem('selectvipcardpage');
								    }else{
								        alert("查无此卡：卡号："+card_no);
								    }
								}else{
								    alert(obj.msg);
								}
							}).error(function(){
							    ajax_flag = 0;
							    alert("系统异常"); 
							});
				        }else{
				            alert( card_no );
				        }
				}; 
				// 监听Socket的关闭
				socket.onclose = function(event) { 
				    //alert("Client notified socket has closed");
				};
				socket.onerror = function(event) { 
				    //alert(" onerror  ");
				    //socket.close();
				};
		    };
		}
		return false;
        /*
        if(ajax_flag > 0){
	        return false;
	    }
        ajax_flag = 1;
        $.post("/query/queryVipCardUserList.html", {},function (result) {
			var obj = Ext.decode(result);
			ajax_flag = 0;
			if (obj.success == "true") {
                initSelectVipCardPage(obj.data);
	            Ext.getCmp('viewport').getLayout().setActiveItem('selectvipcardpage');
			}else{
			    alert(obj.msg);
			}
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
		*/
    });
    
    $("#backFromSelectVipBtn").click(function(){
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
        $("#vipcard_list").html('');
    });
    
    $(".vipcard").live("click",function(){
        var user_id = $(this).attr("user_id");
        var card_no = $(this).attr("card_no");
        
        $("#vipcardpay_user_id").val( user_id );
        $("#vipcardpay_table_name").text( $("#tableinfo_table_id").text() );
        
        $("#pay_card_no").text( card_no );
        $("#vipcardpay_owefee").text($("#tableinfo_spay_fee").text());
        $('#vipcardpayfeeinput').val($("#tableinfo_spay_fee").text());
        $('#vipcardpayfeeinput')[0].focus();
        
        Ext.getCmp('viewport').getLayout().setActiveItem('vipcardpaypage');
    });
    
    $("#cancelPayByVipCardBtn").click(function(){
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
        resetVipCardPayPage();
    });
    
    $("#vipcardpayfeeinput").live("keyup",function(event){
	    if(Ext.getCmp('viewport').layout.activeItem.id=='vipcardpaypage'){
	        if(event.keyCode==13){
	            payByVipCardUser();
		        event.stopPropagation();
		        return false;
	        }
	    }
	});
    
    $("#payByVipCardBtn").click(function(){
        payByVipCardUser();
        event.stopPropagation();
        return false;
    });
    
    
    $("#payByCreditUserPageBtn").click(function(){
        if(ajax_flag > 0){
	        return false;
	    }
        ajax_flag = 1;
        $.post("/query/queryCreditUserList.html", {},function (result) {
			var obj = Ext.decode(result);
			ajax_flag = 0;	   
			if (obj.success == "true") {
                initSelectCreditPage(obj.data);
	            Ext.getCmp('viewport').getLayout().setActiveItem('selectcreditpage');
			}else{
			    alert(obj.msg);
			}
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
        
    });
    
    $(".credit").live("click",function(){
        var user_id = $(this).attr("user_id");
        var custname = $(this).attr("custname");
        
        $("#creditpay_user_id").val( user_id );
        $("#creditpay_table_name").text( $("#tableinfo_table_id").text() );
        
        $("#pay_credit_user").text( custname );
        $("#creditpay_owefee").text($("#tableinfo_spay_fee").text());
        $('#creditpayfeeinput').val($("#tableinfo_spay_fee").text());
        $('#creditpayfeeinput')[0].focus();
        
        Ext.getCmp('viewport').getLayout().setActiveItem('creditpaypage');
    });
    
    
    $("#backFromSelectCreditBtn").click(function(){
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
        $("#credit_list").html('');
    });
    
    
    $("#cancelPayByCreditBtn").click(function(){
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
        resetCreditPayPage();
    });
    
    $("#creditpayfeeinput").live("keyup",function(event){
	    if(Ext.getCmp('viewport').layout.activeItem.id=='creditpaypage'){
	        if(event.keyCode==13){
	            payByCreditUser();
		        event.stopPropagation();
		        return false;
	        }
	    }
	});
    
    $("#payByCreditBtn").click(function(){
        payByCreditUser();
        event.stopPropagation();
        return false;
    });
    
    
    $("#reducefeeBtn").click(function(){
        Ext.getCmp('viewport').getLayout().setActiveItem('reducefeepage');
        $("#reducefee_bill_id").val( $("#tableinfo_bill_id").text() );
        $("#reducefee_table_name").text( $("#tableinfo_table_id").text() );
        $("#reducefee_owefee").text($("#tableinfo_spay_fee").text());
        $('#reducefeeinput').val( $("#tableinfo_reducefee").text() );
        $('#reducefeeinput')[0].focus();
    });
    
    $("#reducefeeinput").live("keyup",function(event){
	    if(Ext.getCmp('viewport').layout.activeItem.id=='reducefeepage'){
	        if(event.keyCode==13){
	            reduceFee();
		        event.stopPropagation();
		        return false;
	        }
	    }
	});
	
	$('#addReducefeeBtn').click(function() {
        reduceFee();
        return false;
	});
	
	$('#cancelReducefeeBtn').click(function() {
        resetReduceFeePage();
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
        return false;
	});
	
	
    $("#addTempFoodBtn").click(function(){
        resetAddTempFoodPage();
        Ext.getCmp('viewport').getLayout().setActiveItem('addtempfoodpage');
        $("#addtempfood_bill_id").val( $("#tableinfo_bill_id").text() );
        $("#temp_foodname_input")[0].focus();
    });
    
    $('#backBtnFromAddTempFoodBtn').click(function(){
        resetAddTempFoodPage();
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
	});
	
	$(".printer").click(function(){
	    if($(this).hasClass("printer_sel")){
	        return false;
	    }
        $(".printer_sel").removeClass("printer_sel").addClass("printer");
        $(this).removeClass("printer").addClass("printer_sel");
        $("#tempfoodhiddenbtn").focus();
        return false;
    });
    
    $("#addTempFoodSubmitBtn").click(function(){
        submitTempFoodForm();
        return false;
    });
    
    $("#addtempfoodform").submit(function(){
        submitTempFoodForm();
        return false;
    });
    
    $('#backBtnFromOperateFoodBtn').click(function(){
        resetOpearteFoodPage();
        Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
        $("#tableinfo_hidden_input")[0].focus();
	});
	
	
	$('#cancelFoodBtn').click(function(){
        if($(".checkfood:checkbox:checked").length==0){
            alert('请先选择菜品');
	    }else{
	        Ext.getCmp('viewport').getLayout().setActiveItem('operatefoodpage');
	        $("#operatefood_title").text('请输入退菜数量');
	        $("#operate_type").val('cancel');
	        $("#operate_value").val('1');
	        $("#operate_value")[0].focus();
	    }
	});
	
	$('#presentFoodBtn').click(function(){
        if($(".checkfood:checkbox:checked").length==0){
            alert('请先选择菜品');
	    }else{
	        Ext.getCmp('viewport').getLayout().setActiveItem('operatefoodpage');
	        $("#operatefood_title").text('请输入赠送数量');
	        $("#operate_type").val('present');
	        $("#operate_value").val('1');
	        $("#operate_value")[0].focus();
	    }
	});
	
    $("#derateFoodBtn").click(function(){
        if($(".checkfood:checkbox:checked").length==0){
            alert('请先选择菜品');
	    }else{
	        Ext.getCmp('viewport').getLayout().setActiveItem('operatefoodpage');
	        $("#operatefood_title").text('请输入打折百分比');
	        $("#operate_type").val('discount');
	        $("#operate_value").val('');
	        $("#operate_value")[0].focus();
	    }
        
    });

    $('#changeFoodTableBtn').click(function(){
        if($(".checkfood:checkbox:checked").length==0){
            alert('请先选择菜品');
	    }else{
	        Ext.getCmp('viewport').getLayout().setActiveItem('operatefoodpage');
	        $("#operatefood_title").text('请输入目标台号');
	        $("#operate_type").val('changetable');
	        $("#operate_value").val('');
	        $("#operate_value")[0].focus();
	    }
	});
	
	$('#submitOperateFoodBtn').click(function(event) {
        submitOperateFood();
        event.stopPropagation();
        return false;
	});
	
	$("#operate_value").live("keyup",function(event){
	    if(Ext.getCmp('viewport').layout.activeItem.id=='operatefoodpage'){
	        if(event.keyCode==13){
	            submitOperateFood();
		        event.stopPropagation();
		        return false;
	        }
	    }
	});
	
	$('#startCookFoodBtn').click(function(){
	    if($(".checkfood:checkbox:checked").length==0){
            alert('请先选择菜品');
            return false;
	    }
	    if(ajax_flag > 0){
	        return false;
	    }
	    var items = [];
		$(".checkfood:checkbox:checked").each(function(){
		    var item = {};
		    item.item_id=$(this).parent().parent().attr("item_id");
		    items.push(item);
		});
		ajax_flag = 1;
	    $.post("/operation/startCook.html", { 
		        bill_id:$("#tableinfo_bill_id").text() ,
		        item_str : Ext.encode(items)
	        }, function (result) {
				var obj = Ext.decode(result);
				ajax_flag = 0;
				alert(obj.msg);
				if (obj.success == "true") {
			        resetOpearteFoodPage();
	                initTableInfoPage(obj.bill)
		            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
		            $("#tableinfo_hidden_input")[0].focus();
				}
				return false;
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
	    return false;
	});
	
	$('#hurryCookFoodBtn').click(function(){
	    if($(".checkfood:checkbox:checked").length==0){
            alert('请先选择菜品');
            return false;
	    }
	    if(ajax_flag > 0){
	        return false;
	    }
	    var items = [];
		$(".checkfood:checkbox:checked").each(function(){
		    var item = {};
		    item.item_id=$(this).parent().parent().attr("item_id");
		    items.push(item);
		});
		ajax_flag = 1;
	    $.post("/operation/hurryCook.html", { 
		        bill_id:$("#tableinfo_bill_id").text() ,
		        item_str : Ext.encode(items)
	        }, function (result) {
				var obj = Ext.decode(result);
				ajax_flag = 0;
				alert(obj.msg);	   
				if (obj.success == "true") {
			        resetOpearteFoodPage();
	                initTableInfoPage(obj.bill)
		            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
		            $("#tableinfo_hidden_input")[0].focus();
				}
				return false;
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常"); 
		});
	    return false;
	
	});
	
	$('#printQueryBillBtn').click(function(){
	    var table_id = $("#tableinfo_table_id").text();
	    printBill(table_id);
	});
	
	$('#invoiceBtn').click(function(){
	    
	});
	
	$('#closeBillBtn').click(function(){
	    var bill_id = $("#tableinfo_bill_id").text();
	    var table_id = $("#tableinfo_table_id").text();
	    closeBill(bill_id,table_id);
	});
	
	$('#todayBtn').click(function(){
	    $.post("/query/queryTodayData.html", {
	            
	        }, function (result) {
				var obj = Ext.decode(result);
				if (obj.success == "true") {
				    initTodayPage(obj);
		            Ext.getCmp('viewport').getLayout().setActiveItem('todaypage');
				}
				return false;
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常");
		});
	});
	
	$('.backToDeskBtn').click(function(){
	    resetTablePage();
        Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
        $("#smart_str")[0].focus();
        //resetTodayPage();
	});
	
	$(".addfoodcount").live("focus",function(){
	    window_tag=1;
    });
    $(".addfoodcount").live("blur",function(){
	    window_tag=0;
		$("#query_food_str")[0].focus();
    });
    
    $('#printCategoryBtn').click(function(){
        $.post("/query/queryTodayData.html", {
	            
	        }, function (result) {
				var obj = Ext.decode(result);
				if (obj.success == "true") {
				    var data = {};
				    data.type="printcategory";
				    obj.printer = default_printer;
				    data.data = obj;
				    var socket = new WebSocket(socketurl); 
					    // 打开Socket 
						socket.onopen = function(event) { 
							// 发送一个初始化消息
							socket.send(JSON.stringify(data)); 
							// 监听消息
							socket.onmessage = function(event) { 
							    socket.close();
						}; 
						// 监听Socket的关闭
						socket.onclose = function(event) { 
						    //alert("Client notified socket has closed");
						};
						socket.onerror = function(event) { 
						    //alert(" onerror  ");
						    //socket.close();
						};
				    };
				}
				return false;
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常");
		});
	});
    
    
    $('#printTodayMoneyBtn').click(function(){
        $.post("/query/queryTodayData.html", {
	            
	        }, function (result) {
				var obj = Ext.decode(result);
				if (obj.success == "true") {
				    var data = {};
				    data.type="printtoday";
				    obj.printer = default_printer;
				    data.data = obj;
				    var socket = new WebSocket(socketurl); 
					    // 打开Socket 
						socket.onopen = function(event) { 
							// 发送一个初始化消息
							socket.send(JSON.stringify(data)); 
							// 监听消息
							socket.onmessage = function(event) { 
							    socket.close();
						}; 
						// 监听Socket的关闭
						socket.onclose = function(event) { 
						    //alert("Client notified socket has closed");
						};
						socket.onerror = function(event) { 
						    //alert(" onerror  ");
						    //socket.close();
						};
				    };
				}
				return false;
		}).error(function(){
		    ajax_flag = 0;
		    alert("系统异常");
		});
	});
    
    
    
    
    
    
});