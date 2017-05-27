Ext.onReady(function() {
    el = Ext.get("body");
    new Ext.KeyMap(el,{
        key:Ext.EventObject.BACKSPACE ,
        fn: function(e){
            if(flag){
                event.returnValue = false ;
            }
        },
        scope : el
    });
    
    var ban = $.cookie('ban');
    if(current_ban!=ban){
        $.cookie('submit_str', '' , { expires: 7 }); 
        $.cookie('ban', current_ban , { expires: 7 }); 
    }
	function initMainPage() {
	    $("#food_barcode").text('');
        $("#food_name").text('');
        $("#food_table_name").text('');
        $("#food_price").text('');
        $("#food_note").text('');
        $("#food_result").text(''); 
        
        $("#search_str").val(''); 
        
	}
	
	function initQueryBillPage() {
	    $("#billinfo_bill_list").html('');
	    for(var i=0;i<billList.length;i++){
            var bill = billList[i];
            var str = '<div class="querybill" bill_id = "'+bill.BILL_ID+'" table_id = "'+bill.TABLE_ID+'" table_name = "'+bill.TABLE_ID+'" '+
               ' nop = "'+bill.NOP+'" reduce_fee = "'+bill.REDUCE_FEE+'" discount_fee = "'+bill.DERATE_FEE+'" invoice_fee = "'+bill.INVOICE_FEE+'" '+
               ' bill_fee = "'+bill.BILL_FEE+'" spay_fee = "'+bill.SPAY_FEE+'" recv_fee = "'+bill.RECV_FEE+'" staff_id = "'+bill.OPEN_STAFF_ID+'" '+
               ' staff_name = "'+bill.OPEN_STAFF_NAME+'" start_time = "'+bill.OPEN_TIME+'" end_time = "'+bill.CLOSE_TIME+'" '+
               ' remark = "'+bill.REMARK+'"  >'+
              '<div class="bill_list_table_id">'+bill.TABLE_ID+'</div>'+
              '<div class="bill_list_recv_fee">￥'+bill.RECV_FEE+'</div>'+
              '<div class="bill_list_time">'+(""+bill.OPEN_TIME).substr(11,8)+' - '+(""+bill.CLOSE_TIME).substr(11,8)+'</div>'+
              '</div>';
            $("#billinfo_bill_list").append(str);
        }
        $('#billinfo_querybill_str').val('');
        $('#billinfo_querybill_str')[0].focus();
	}
	
	function initTakeOutPage() {
	    $("#billinfo_bill_list").html('');
	    for(var i=0;i<takeoutbills.length;i++){
            var bill = takeoutbills[i];
            var str = '<div class="querybill" bill_id = "'+bill.BILL_ID+'" table_id = "'+bill.TABLE_ID+'" table_name = "'+bill.TABLE_ID+'" '+
               ' nop = "'+bill.NOP+'" reduce_fee = "'+bill.REDUCE_FEE+'" discount_fee = "'+bill.DERATE_FEE+'" invoice_fee = "'+bill.INVOICE_FEE+'" '+
               ' bill_fee = "'+bill.BILL_FEE+'" spay_fee = "'+bill.SPAY_FEE+'" recv_fee = "'+bill.RECV_FEE+'" staff_id = "'+bill.OPEN_STAFF_ID+'" '+
               ' staff_name = "'+bill.OPEN_STAFF_NAME+'" start_time = "'+bill.OPEN_TIME+'" end_time = "'+bill.CLOSE_TIME+'" '+
               ' remark = "'+bill.REMARK+'"  >'+
              '<div class="bill_list_table_id">'+bill.TABLE_ID+'</div>'+
              '<div class="bill_list_recv_fee">￥'+bill.RECV_FEE+'</div>'+
              '<div class="bill_list_time">'+(""+bill.OPEN_TIME).substr(11,8)+' - '+(""+bill.CLOSE_TIME).substr(11,8)+'</div>'+
              '</div>';
            $("#billinfo_bill_list").append(str);
        }
        $('#billinfo_querybill_str').val('');
        $('#billinfo_querybill_str')[0].focus();
	}
	
	function init(data) {
	    //alert('initMainPage');
	    billList = data.billList;
	    itemList = data.itemList;
	    takeoutbills.length=0;
	    alarmList1.length=0;
	    alarmList2.length=0;
	    alarmList3.length=0;
	    alarmList4.length=0;
	    alarmList5.length=0;
	    alarmList6.length=0;
	    $("#overtime1").html('');
	    $("#overtime2").html('');
	    $("#overtime3").html('');
	    $(".count_tag").text('0');
	    
	    for(var i=0;i<itemList.length;i++){
	        var item = itemList[i];
	        var food_id = item.FOOD_ID;
	        var count_tag = parseInt($("#food_"+food_id).children(".count_tag").text());
	        //alert(count_tag);
	        $("#food_"+food_id).children(".count_tag").text(count_tag+1);
	        if(item.PAY_TYPE!='0'){
	            alarmList5.push(item);
	            continue;
	        }
	        if(item.CATEGORY=='酒水'||item.CATEGORY=='其他'){
	            alarmList5.push(item);
	            continue;
	        }
	        var hurry_tag = "";
	        if(item.HURRY_TIME!=''){
	            hurry_tag="(已催)";
	        }
	        var item_div = '<div class="alarm" item_id="'+item.ITEM_ID+'" bill_id="'+item.BILL_ID+'" class="item_'+item.STATE+'" clazz="item_'+item.STATE+'" count="'+item.COUNT+'" >'+
	                       '  <div class="alarm_food" item_id="'+item.ITEM_ID+'" bill_id="'+item.BILL_ID+'" >'+item.FOOD_NAME+'</div>'+
	                       '  <div>'+
	                       '    <div class="alarm_table">台号：'+item.TABLE_ID+'  '+hurry_tag+'</div>'+
	                       '    <div class="alarm_time">用时:'+item.USE_TIME+'分</div>'+
	                       '  </div>'+
	                       '  <div class="alarm_line"></div>'+
	                       '</div>';
	        $("#billItemList").append(item_div);
	        if(item.STATE=='1'){
	            if(item.USE_TIME>35){
	                $("#overtime1").append(item_div);
	                alarmList1.push(item);
		        }else if(item.USE_TIME>25){
		            $("#overtime2").append(item_div);
		            alarmList2.push(item);
		        }else if(item.USE_TIME>15){
		            $("#overtime3").append(item_div);
		            alarmList3.push(item);
		        }else{
		            alarmList4.push(item);
		        }
	        }else if(item.STATE=='2'){
	            alarmList5.push(item);
	        }else if(item.STATE=='0'){
	            alarmList6.push(item);
	        }
	    }
	    
	    $("#finish_count").text(alarmList5.length);
	    $("#cooking_count").text(alarmList1.length+alarmList2.length+alarmList3.length+alarmList4.length);
	    $("#alarm1_count").text(alarmList1.length);
	    $("#alarm2_count").text(alarmList2.length);
	    $("#alarm3_count").text(alarmList3.length);
	    $("#normal_count").text(alarmList4.length);
	    $("#waitstart_count").text(alarmList6.length);
	    $("#total_count").text(itemList.length);
	    //alert("billList.length = "+billList.length);
	    for(var i=0;i<billList.length;i++){
	        var bill = billList[i];
	        //alert(Ext.encode(bill));
	        //alert(bill.TABLE_ID+" , ");
	        if(bill.TABLE_ID.substr(0,1)=="9"){
	            takeoutbills.push(bill);
	        }
	    }
	}
	
	// 初始化菜品查询页面
    function initQueryFoodPage(){
        
    }
    
    // 重置菜品查询页面
    function resetQueryFoodPage(){
        $("#keyword").text('');
        $("#foodList").html('');
        
    }
    
    var blankpage = new Ext.Panel({   
		id: 'blankpage',   
		border:false,
        layout: 'fit',
	    autoScroll : true,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#FFF;margin:0px;padding:0px;',
        contentEl: 'blank_center',
        listeners:{"render":function(obj){
	        $("#blank_center").show();
	    }}
	});
	
	var scanpage = new Ext.Panel({   
		id: 'scanpage',   
		border:false,
        layout: 'fit',
	    autoScroll : true,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#FFF;margin:0px;padding:0px;',
        contentEl: 'scanpage_center',
        listeners:{"render":function(obj){
	        $("#scanpage_center").show();
	    }}
	});
	
	var tablepage = new Ext.Panel({   
		id: 'tablepage',   
		border:false,
        layout: 'fit',
	    autoScroll : true,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#FFF;margin:0px;padding:0px;',
        contentEl: 'tablepage_center',
        listeners:{"render":function(obj){
	        $("#tablepage_center").show();
	    }}
	});
	
	
	var takeout = new Ext.Panel({   
		id: 'takeout',   
		border:false,
        layout: 'border',
	    autoScroll : false,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#E1E1E1 url(/image/desk22.jpg) no-repeat;margin:0px;padding:0px;',
        items: [{
	          id: 'center-takeout-container',
	          region: 'center',
	          border:false,
		      autoScroll : true,
	          layout: 'fit',
	          border:false,
	          contentEl: 'takeout_center',
	          bodyStyle: 'background:#E1E1E1;margin:0px;padding:0px;',
	          paddings: '0 0 0 0',
	          margins: '0 0 0 0',
	          listeners:{"render":function(obj){
		          $("#takeout_center").show();
		      }}
	      },{
	          id: 'west-takeout-container',
	          region:'west',
	          xtype: 'panel',
	          border:false,
	          layout: 'fit',
	          autoScroll : true,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          width: 300,
	          height:1500,
	          collapsible: false,
	          bodyStyle: 'background:#3D3D3D;margin: 0px; padding:0px;',
	          contentEl: 'takeout_west',
	          listeners:{"render":function(obj){
		          $("#takeout_west").show();
		      }}
	      }]
	});
	
	var queryfood = new Ext.Panel({   
		id: 'queryfood',   
		border:false,
        layout: 'border',
	    autoScroll : false,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#E1E1E1 url(/image/desk22.jpg) no-repeat;margin:0px;padding:0px;',
        items: [{
	          id: 'north-queryfood-container',
	          region:'north',
	          xtype: 'panel',
	          border:false,
	          layout: 'fit',
	          autoScroll : false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          height: 0,
	          collapsible: false,
	          bodyStyle: 'background:#000;margin: 0px; padding:0px;color:#fff;',
	          contentEl: 'queryfood_north',
	          listeners:{"render":function(obj){
		          $("#queryfood_north").show();
		      }}
	      },{
	          id: 'center-queryfood-container',
	          region: 'center',
	          border:false,
		      autoScroll : true,
	          layout: 'fit',
	          border:false,
	          contentEl: 'queryfood_center',
	          bodyStyle: 'background:#FFF;margin:0px;padding:0px;',
	          paddings: '0 0 0 0',
	          margins: '0 0 0 0',
	          listeners:{"render":function(obj){
		          $("#queryfood_center").show();
		      }}
	      }]
	});
	
	var mainpage = new Ext.Panel({   
		id: 'mainpage',   
		border:false,
        layout: 'border',
        items: [{
	          id: 'north-mainpage-container',
	          region:'north',
	          xtype: 'panel',
	          border:false,
	          layout: 'fit',
	          autoScroll : false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          height: 90,
	          collapsible: false,
	          bodyStyle: 'background:#F1F1F1;margin:0px; padding:0px;border:solid 0px #DDD;border-bottom:solid 1px #DDD;',
	          contentEl: 'mainpage_north',
	          listeners:{"render":function(obj){
		          $("#mainpage_north").show();
		      }}
	      },{
	          id: 'center-mainpage-container',
	          region: 'center',
	          border:false,
		      layout: 'card',
		      autoScroll : false,
	          activeItem: 0,    
		      paddings: '0 0 0 0',
	          margins: '0 0 0 0',
		      items: [blankpage,scanpage,queryfood,tablepage]
	      },{
	          id: 'west-mainpage-container',
	          region:'west',
	          xtype: 'panel',
	          border:false,
	          layout: 'fit',
	          autoScroll : false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          width: 0,
	          height:1500,
	          collapsible: false,
	          bodyStyle: 'background:#3D3D3D;margin: 0px; padding:0px;',
	          contentEl: 'mainpage_west',
	          listeners:{"render":function(obj){
		          $("#mainpage_west").show();
		      }}
	      }]
	});

	var monitorpage = new Ext.Panel({   
		id: 'monitorpage',   
		border:false,
        layout: 'fit',
	    autoScroll : true,
        contentEl:'monitorpage_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2D2F2E url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;color:#fff;',
        listeners:{"show":function(obj){
		    $("#monitorpage_center").show()
		}}
	});
	var viewport = new Ext.Viewport({
        layout: 'border',
        border:false,
	    items: [{
	          id: 'north-main-container',
	          region:'north',
	          xtype: 'panel',
	          border:false,
	          layout: 'fit',
	          autoScroll : false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          height: 32,
	          collapsible: false,
	          bodyStyle: 'background:#D2D2D2;margin:0px; padding:0px;border-bottom:solid 1px #000;',
	          contentEl: 'main_north',
	          listeners:{"render":function(obj){
		          $("#main_north").show();
		      }}
	      },{
	          id: 'center-main-container',
	          region: 'center',
	          border:false,
		      layout: 'card',
		      autoScroll : false,
	          activeItem: 0,    
		      paddings: '0 0 0 0',
	          margins: '0 0 0 0',
		      items: [mainpage,monitorpage,takeout]
	    }]
	});
	
	table_bill_window = new Ext.Window({   
		id: 'table_bill_window',   
		border:false,
        bodyBorder :false,
		header: false,
        layout: 'fit',
        width:1150,  
        height:600,
        showtag:false,
        modal:true,
        plain:true,
        frame : true,
        baseCls :'mywin-panel',
        closable:false,
        closeAction: 'hide',
        constrain: true,
        //maximizable:true,
        resizable:false,
	    autoScroll : false,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#ECECEC url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;color:#fff;',
        html:"<iframe id='tableWinFrame' src='/transPage?page=/operation/blank' frameborder='0' width='100%' height='100%' ></iframe>",
        listeners:{"show":function(obj){
            table_bill_window.showtag=true;
		},"hide":function(obj){
		    $("#tableWinFrame").attr("src","/transPage?page=/operation/blank");
		    table_bill_window.showtag=false;
        }}
	});
	
	waitmask = new Ext.LoadMask("center-main-container", {
       msg : '操作进行中,请稍候...'
    });
    
	height = $("#center-main-container").height();
	width = $("#center-main-container").width();
	$("#barcodetable").css("margin-left",(width-450)/2);
	
	$("#overtime1").height(height-140);
	$("#overtime2").height(height-140);
	$("#overtime3").height(height-140);
	$("#overtime4").height(height-140);
	
    $('#monitorBtn').click(function(){
        Ext.getCmp('center-main-container').getLayout().setActiveItem('monitorpage');
        initMainPage();
	});
	
	$('#queryBillPageBtn').click(function(){
        Ext.getCmp('center-main-container').getLayout().setActiveItem('takeout');
        initQueryBillPage();
	});
	
	$('#takeOutPageBtn').click(function(){
	    $(".menu_select").removeClass("menu_select");
        $('#takeOutPageBtn').addClass("menu_select");
        Ext.getCmp('center-main-container').getLayout().setActiveItem('takeout');
        initTakeOutPage();
	});
    
    $('#queryFoodBtn').click(function(){
        var food_name = $("#search_str").val();
        //alert("food_name = "+food_name);
        $("#keyword").text(food_name);
        for(var i=0;i<itemList.length;i++){
            var item = itemList[i];
            if(item.PAY_TYPE=='0'&&item.STATE!='2'&&item.FOOD_NAME.indexOf(food_name)>=0){
                var food_div = '<div style="height:65px;"  >'+
                       '  <div><image src="/image/i.png" class="food_info_logo"/></div>'+
                       '  <div>'+
                       '    <div><span class="query_foodname" barcode="'+item.BARCODE+'" >'+item.FOOD_NAME+'</span></div>'+
                       '    <div class="food_info_item">'+
                       '      <div>条码:'+item.BARCODE+'</div>'+
                       '      <div>桌号:'+item.TABLE_ID+'</div>'+
                       '      <div>点菜员:'+item.OPER_STAFF_NAME+'</div>'+
                       '      <div>数量:￥'+item.PRICE+' '+item.UNIT+' X '+item.COUNT+'</div>'+
                       '      <div>起菜时间-'+(""+itemList[i].START_TIME).substr(11,8)+'</div>'+
                       '      <div>催菜时间-'+(""+itemList[i].HURRY_TIME).substr(11,8)+'</div>'+
                       '      <div>做法:'+item.NOTE+'</div>'+
                       '    </div>'+
                       '  </div>'+
                       '</div>';
                $("#foodList").append(food_div);
            }
        }
        Ext.getCmp('center-main-container').getLayout().setActiveItem('queryfood');
	});
    
    $('#searchBtn').click(function(){
        searchFormSubmit();
        return false;
	});
    
    $('#exit').click(function(){
        if(confirm("确定要退出系统吗?")){
            //window.close();
            document.location.href="/logout.action";
            return false;
        }
	});
    
    $('.alarm').live("click",function(){
        if(ajax_flag>0){
            return false;
        }
        var items = [];
	    var item = {};
	    item.item_id=$(this).attr("item_id");
	    items.push(item);
	    ajax_flag = 1;
	    $.post("/operation/hurryCook.html", { 
		        bill_id: $(this).attr("bill_id") ,
		        item_str : JSON.stringify(items)
	        }, function (result) {
				var obj = $.parseJSON(result);
				ajax_flag = 0;
				alert(obj.msg);
				if(obj.success=='true'){
				    
				}
		});
	});
    
    $('.query_foodname').live("click",function(){
        var barcode = $(this).attr("barcode");
        if(barcode.length>6){
            barcode = barcode.substr(0,6);
        }
        var flag = 0;
        $("#food_barcode").text(barcode);
        $("#food_name").text('');
        $("#food_table_name").text('');
        $("#food_price").text('');
        $("#food_note").text('');
        $("#food_result").text('未找到该菜品'); 
        for(var i=0;i<itemList.length;i++){
            var item = itemList[i];
            if(item.BARCODE==barcode){
                //alert(Ext.encode(item));
                $("#food_barcode").text(item.BARCODE);
                $("#food_name").text(item.FOOD_NAME);
                $("#food_table_name").text(item.TABLE_ID);
                $("#food_price").text('￥ '+item.PRICE);
                $("#food_note").text(item.NOTE);
                $("#food_result").text('扫单成功');
                flag = 1;
                if(item.STATE=='0'){
                    $("#food_result").text('该菜品还未起菜!');
                    flag = 0;
                }else{
                    item.STATE='2';
                }
                break; 
            }
        }
        
        $("#search_str").val('');
        if(flag==1){
            var hour = new Date().getHours();
			if(hour<10) hour = "0"+hour
			var minute = new Date().getMinutes();
			if(minute<10) minute = "0"+minute
			var second = new Date().getSeconds();
			if(second<10) second = "0"+second
            var submit_str = $.cookie('submit_str');
            if(submit_str==null||submit_str=='null'||submit_str==undefined) submit_str="";
            submit_str = submit_str+barcode+"#DOT"+hour+":"+minute+":"+second+"#END";
            $.cookie('submit_str', submit_str , { expires: 7 }); 
        }
        Ext.getCmp('center-main-container').getLayout().setActiveItem('mainpage');
        resetQueryFoodPage();
        return false;
	});
	
    // 重置账单查询页面
    function resetQueryBillPage(){
        $("#billinfo_bill_id").text('');
        $("#billinfo_table_id").text('');
        $("#billinfo_date").text('');
        $("#billinfo_ban").text('');
        $("#billinfo_nop").text('');
        $("#billinfo_bill_fee").text('');
        $("#billinfo_start_time").text('');
        $("#billinfo_end_time").text('');
        
        $(".billinfo_item").remove();
        $('#billinfo_querybill_str').val('');
        $('#billinfo_querybill_str')[0].blur();
        
        $('#billinfopart').hide();
        $('#billitempart').hide();
    }
    
    $("#billinfo_querybill_str").live("keyup",function(){
        if($("#billinfo_querybill_str").val()==''){
	        $(".querybill").show();
	        return false;
	    }
	    $(".querybill").hide();
        $(".querybill[table_id*="+$("#billinfo_querybill_str").val()+"]").show();
	});
    
    $(".querybill").live("click",function() {
        //alert($(this).attr("bill_id"));
        $("#billinfo_bill_id").text( $(this).attr("bill_id") );
        $("#billinfo_table_id").text( $(this).attr("table_id") );
        $("#billinfo_date").text( (""+$(this).attr("start_time")).substr(0,10) );
        $("#billinfo_ban").text( $(this).attr("ban") );
        $("#billinfo_nop").text( $(this).attr("nop") );
        $("#billinfo_start_time").text( (""+$(this).attr("start_time")).substr(11,8) );
        $("#billinfo_end_time").text( (""+$(this).attr("end_time")).substr(11,8) );
        var invoice_fee = Number($(this).attr("invoice_fee"));
        $("#billinfo_invoice_fee").text( "未开" );
        if(invoice_fee>0){
            $("#billinfo_invoice_fee").text( "已开" );
        }
        $('#billinfopart').show();
        $('#billitempart').show();
        
        $(".bill_info_item").remove();
        for(var i=0;i<itemList.length;i++){
            if(itemList[i].BILL_ID!=$(this).attr("bill_id")){
                continue;
            }
            var call_type="即起";
            if(itemList[i].CALL_TYPE==0){
                call_type=" 叫起";
            }
	        var item_div = '<tr class="bill_info_item" height="26">'+
	                       '<td>'+itemList[i].FOOD_NAME+'</td>'+
	                       '<td>￥'+itemList[i].PRICE+'/'+itemList[i].UNIT+' X '+itemList[i].COUNT+'</td>'+
	                       '<td>'+itemList[i].BACK_COUNT+'</td>'+
	                       '<td>'+itemList[i].FREE_COUNT+'</td>'+
	                       '<td>'+itemList[i].PAY_RATE+'%</td>'+
	                       '<td>'+call_type+'</td>'+
	                       '<td>'+(""+itemList[i].START_TIME).substr(11,8)+'</td>'+
	                       '<td>'+(""+itemList[i].HURRY_TIME).substr(11,8)+'</td>'+
	                       '<td>'+(""+itemList[i].END_TIME).substr(11,8)+'</td>'+
	                       '<td>'+itemList[i].NOTE+'</td>'+
	                       '<td>'+itemList[i].REMARK+'</td>'+
	                       '</tr>';
	        $("#billinfo_itemtable").append(item_div);
	    }
	    return false;
	});
	
	$("#billinfo_itemtable tr").live({
	    mouseenter:function(){
	        $(this).addClass("billitemtable_tr_hover");
	    },
	    mouseleave:function(){
	        $(this).removeClass("billitemtable_tr_hover");
	    }
	});
	
	function scanFood(){
	    var barcode = $("#search_str").val();
        if(barcode.length>6){
            barcode = barcode.substr(0,6);
        }
        var flag = 0;
        $("#food_barcode").text(barcode);
        $("#food_name").text('');
        $("#food_table_name").text('');
        $("#food_price").text('');
        $("#food_note").text('');
        $("#food_result").text('未找到该菜品');
        $("#barcode_count").text("0");
        for(var i=0;i<itemList.length;i++){
            var item = itemList[i];
            if(item.BARCODE==barcode){
                //alert(Ext.encode(item));
                $("#barcode_count").text("1");
                $("#food_barcode").text(item.BARCODE);
                $("#food_name").text(item.FOOD_NAME);
                $("#food_table_name").text(item.TABLE_ID);
                $("#food_price").text('￥ '+item.PRICE);
                $("#food_note").text(item.NOTE);
                $("#food_result").text('');
                flag = 1;
                if(item.STATE=='0'){
                    $("#food_result").text('该菜品还未起菜!');
                }if(item.STATE=='1'){
                    $("#food_result").text('该菜品制作中!');
                }else{
                    item.STATE='2';
                    $("#food_result").text('该菜品已上菜!');
                }
                break; 
            }
        }
        
	}
	
	function scanFoodSubmit(){
	    var barcode = $("#search_str").val();
        if(barcode.length>6){
            barcode = barcode.substr(0,6);
        }
        var flag = 0;
        $("#food_barcode").text(barcode);
        $("#food_name").text('');
        $("#food_table_name").text('');
        $("#food_price").text('');
        $("#food_note").text('');
        $("#food_result").text('未找到该菜品'); 
        for(var i=0;i<itemList.length;i++){
            var item = itemList[i];
            if(item.BARCODE==barcode){
                //alert(Ext.encode(item));
                $("#food_barcode").text(item.BARCODE);
                $("#food_name").text(item.FOOD_NAME);
                $("#food_table_name").text(item.TABLE_ID);
                $("#food_price").text('￥ '+item.PRICE);
                $("#food_note").text(item.NOTE);
                $("#food_result").text('扫单成功');
                flag = 1;
                if(item.STATE=='0'){
                    $("#food_result").text('该菜品还未起菜!');
                    flag = 0;
                }else{
                    item.STATE='2';
                }
                break; 
            }
        }
        $("#search_str").val('');
        if(flag==1){
            var hour = new Date().getHours();
			if(hour<10) hour = "0"+hour
			var minute = new Date().getMinutes();
			if(minute<10) minute = "0"+minute
			var second = new Date().getSeconds();
			if(second<10) second = "0"+second
            var submit_str = $.cookie('submit_str');
            if(submit_str==null||submit_str=='null'||submit_str==undefined) submit_str="";
            submit_str = submit_str+barcode+"#DOT"+hour+":"+minute+":"+second+"#END";
            $.cookie('submit_str', submit_str , { expires: 7 }); 
        }
        
	}
	
	$("#sendFoodBtn").click(function(){
	    var barcode = $("#food_barcode").text();
	    for(var i=0;i<itemList.length;i++){
            var item = itemList[i];
            if(item.BARCODE==barcode){
                //alert(Ext.encode(item));
                $("#food_result").text('扫单成功');
                item.STATE='2';
                $("#search_str").val('');
                var hour = new Date().getHours();
				if(hour<10) hour = "0"+hour
				var minute = new Date().getMinutes();
				if(minute<10) minute = "0"+minute
				var second = new Date().getSeconds();
				if(second<10) second = "0"+second
		        var submit_str = $.cookie('submit_str');
		        if(submit_str==null||submit_str=='null'||submit_str==undefined) submit_str="";
		        submit_str = submit_str+barcode+"#DOT"+hour+":"+minute+":"+second+"#END";
		        $.cookie('submit_str', submit_str , { expires: 7 }); 
                break; 
            }
        }
        
	});
	
	
    new Ext.KeyMap(el,{
        key:Ext.EventObject.ESC ,
        fn: function(e){
            if(table_bill_window.showtag){
                table_bill_window.hide();
                table_bill_window.showtag=false;
            }else if(Ext.getCmp('center-main-container').layout.activeItem.id=='mainpage'){
                $("#search_str").val('');
		        $("#search_str")[0].focus();
		        Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('blankpage');
                return false;
		    }else if(Ext.getCmp('center-main-container').layout.activeItem.id=='monitorpage'){
		        
		    }else if(Ext.getCmp('center-main-container').layout.activeItem.id=='takeout'){
		        
		    }else{
		        
		    }
        },
        scope : el
    });
    
    var focusTask = {
		run : function() {
		    if(!table_bill_window.showtag&&Ext.getCmp('center-main-container').layout.activeItem.id=='mainpage'){
		        $("#search_str")[0].focus();
		    }else{
		        $("#search_str")[0].blur();
		    }
		},
		interval : 500
	}
	
	Ext.TaskManager.start(focusTask);
	
	var queryTask = {
		run : function() {
		    Ext.Ajax.request({
		        url:'/monitor/queryAllBill.html',
		        method:'post',
			    params:{ },
			    success:function(response, opts){
			        var obj = Ext.decode(response.responseText);
			        if(obj.success=='true'){
			            init(obj);
				    }
			    }
			});
		},
		interval : 60000
	}
	Ext.TaskManager.start(queryTask);
	
	var submitTask = {
		run : function() {
		    var submit_str = $.cookie('submit_str');
            if(submit_str==null||submit_str=='null'||submit_str==undefined) submit_str="";
		    Ext.Ajax.request({
		        url:'/monitor/sendSubmit.html',
		        method:'post',
			    params:{submit_str : submit_str },
			    success:function(response, opts){
			        var obj = Ext.decode(response.responseText);
			        if(obj.success=='true'){
			            // 在情况cookie之前先要进行判断  如果字符串没有变化 则清空 ; 如果有变化 则不做操作,留待下次提交后解决
			            var new_submit_str = $.cookie('submit_str');
                        if(new_submit_str==null||new_submit_str=='null'||new_submit_str==undefined) new_submit_str="";
                        if(new_submit_str==submit_str){
                            $.cookie('submit_str', '' , { expires: 7 }); 
                        }
				    }
			    }
			});
		},
		interval : 40000
	}
	Ext.TaskManager.start(submitTask);
	
	$('#showCookie').click(function() {
	    var submit_str = $.cookie('submit_str');
		alert(" cookie : "+submit_str);
	});
	
	$('#clearCookie').click(function() {
	    $.cookie('submit_str', '' , { expires: 7 }); 
	    alert("清空成功");
	});
	
	$('#searchPageBtn').click(function() {
	    $(".menu_select").removeClass("menu_select");
		$('#searchPageBtn').addClass("menu_select");
		Ext.getCmp('center-main-container').getLayout().setActiveItem('mainpage');
        //initMainPage();
	});
	
	$('#monitorPageBtn').click(function() {
	    $(".menu_select").removeClass("menu_select");
		$('#monitorPageBtn').addClass("menu_select");
		Ext.getCmp('center-main-container').getLayout().setActiveItem('monitorpage');
        //initMainPage();
	});
	
	$(document).keydown(function(event){
	    if(event.keyCode==112){
            $(".menu_select").removeClass("menu_select");
			$('#searchPageBtn').addClass("menu_select");
			Ext.getCmp('center-main-container').getLayout().setActiveItem('mainpage');
			return false;
        }
        if(event.keyCode==113){
            $(".menu_select").removeClass("menu_select");
			$('#monitorPageBtn').addClass("menu_select");
			Ext.getCmp('center-main-container').getLayout().setActiveItem('monitorpage');
			return false;
        }
        if(event.keyCode==114){
            $(".menu_select").removeClass("menu_select");
			$('#takeOutPageBtn').addClass("menu_select");
            Ext.getCmp('center-main-container').getLayout().setActiveItem('takeout');
            initTakeOutPage();
			return false;
        }
    });
	
	function queryTable(table_id){
	    $(".table").hide();
	    $(".table_sel").removeClass("table_sel");
	    var count1 = $(".table[table_id="+table_id+"]").show().addClass("table_sel").length;;
	    var count2 = $(".table[table_id^="+table_id+"]").show().length;
	    if(count1>0){
	        count2 = count2 - count1;
	    }
	    $("#table_count").text(count1+count2);
	    Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('tablepage');
	}
	function resetTablePage(){
	    $(".table").hide();
	    Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('tablepage');
	    $("#search_str").val('');
	    $("#table_count").text($(".table:visible").length);
	    Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('blankpage');
	}
	function queryTableSubmit(table_id){
	    Ext.Ajax.request({
			url : '/monitor/queryTableBill.html',
			params : {
			    table_id : table_id
			},
			success : function(response, opts) {
				var obj = Ext.decode(response.responseText);
			    if(obj.success=='true'){
			        resetTablePage();
				    table_bill_window.show();
				    $("#tableWinFrame").attr("src","/monitor/querybillformonitor.html?bill_id="+obj.bill_id);
				}
			},
			failure : function(response, opts) {
			    alert("系统异常,操作失败");
			}
		});
	    
	}
	$('.table').click(function(){
	    var table_id = $(this).attr("table_id");
        queryTableSubmit(table_id);
	});
	function queryFood(str){
	    //alert("queryFood str = "+str);
	    var sel_tag =true;
	    $(".food").hide();
	    $(".food_sel").removeClass("food_sel");
	    var count1 = $(".food[abbr="+str.toUpperCase()+"]").length;
	    if(count1>0){
	        $(".food[abbr="+str.toUpperCase()+"]").show();
	        $(".food[abbr="+str.toUpperCase()+"]").eq(0).addClass("food_sel");
	        sel_tag = false;
	    }
	    var count2 = $(".food[abbr^="+str.toUpperCase()+"]").length;
	    if(count2>0){
	        $(".food[abbr^="+str.toUpperCase()+"]").show();
		    if(sel_tag){
		        $(".food[abbr^="+str.toUpperCase()+"]").eq(0).addClass("food_sel");
		        sel_tag = false;
		    }
	    }
	    var count3 = $(".food[food_name*="+str+"]").length;
	    if(count3>0){
	        $(".food[food_name*="+str+"]").show();
		    if(sel_tag){
		        $(".food[food_name*="+str+"]").eq(0).addClass("food_sel");
		        sel_tag = false;
		    }
	    }
	    var count = count1 + count2 + count3;
	    $("#food_count").text(count);
	    Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('queryfood');
	}
	function resetFoodPage(){
	    $(".food").hide();
	    Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('queryfood');
	    $("#search_str").val('');
	    $("#food_count").text($(".food:visible").length);
	    Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('blankpage');
	}
	function queryFoodSubmit(food_id){
	    //var count = parseInt($("#food_"+food_id).children(".count_tag").text());
	    resetFoodPage();
	    table_bill_window.show();
		$("#tableWinFrame").attr("src","/app/monitor/queryfood.jsp?id="+food_id+"&name="+$("#food_"+food_id).attr("food_name")+"&time="+new Date());
	}
	$('.food').click(function(){
	    var food_id = $(this).attr("food_id");
        queryFoodSubmit(food_id);
	});
	var table_patrn=/^[0-9]{1,4}$/; 
    var barcode_patrn=/^[0-9]{5,20}$/; 
    var food_patrn=/^[a-zA-Z0-9]{1,20}$/; 
	$("#search_str").live("keyup",function(event){
	    //alert(event.keyCode);
	    var str = $.trim( $("#search_str").val() );
	    if(str==""){
	        $("#search_str").val('');
	        //alert(event.keyCode);
	        if(event.keyCode==13){
	            return false;
	        }
	        Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('blankpage');
	        return false;
	    }
	    if(table_patrn.exec(str)){
	        //alert("查询座号");
	        queryTable(str);
	        return false;
	    }
	    if(barcode_patrn.exec(str)){
	        //alert("查询条形码");
	        scanFood();
	        Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('scanpage');
	        return false;
	    }
	    if(food_patrn.exec(str)){
	        //alert("查询菜品名称");
	        
	    }
	    queryFood(str);
        Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('queryfood');
        return false;
	});
	
	function searchFormSubmit(){
	    var str = $.trim( $("#search_str").val() );
	    if(str==""){
	        return false;
	    }
	    //alert('form  submit , str='+str);
	    if(table_patrn.exec(str)){
	        //alert("查询座号");
	        Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('tablepage');
	        if($(".table_sel").length==1){
	            //alert("打开 table_id="+str+" 的窗口");
	            queryTableSubmit(str);
	        }else{
	            resetTablePage();
	        }
	        return false;
	    }
	    if(barcode_patrn.exec(str)){
	        //alert("查询条形码");
	        scanFoodSubmit();
	        //Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('scanpage');
	        return false;
	    }
	    if(food_patrn.exec(str)){
	        //alert("查询缩略码");
	        //Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('queryfood');
	        //return false;
	    }
	    Ext.getCmp('center-mainpage-container').getLayout().setActiveItem('queryfood');
        if($(".food_sel").length==1){
            queryFoodSubmit($(".food_sel").attr("food_id"));
            //alert("打开 food_name="+$(".food_sel").attr("food_id")+" 的窗口");
        }
	    //alert("查询菜品名称");
        return false;
	}
	
	$('#form').submit(function(){
        //alert('form  submit');
        searchFormSubmit();
        return false;
	});
	
	/*
	var str = "a0";
	alert(str);
	var patrn=/^[0-9]{1,4}$/; 
    var rs=patrn.exec(str);
    if(rs){
        alert(rs);
    }else{
        alert("false;");
    }
    */
    
});

