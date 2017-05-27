function test() {
    alert('alert');
}
function queryTable(table_id) {
	$(".smart_table").hide();
	$(".smart_table_sel").removeClass("smart_table_sel");
	var count = $(".smart_table[table_id=" + table_id + "]").show().addClass("smart_table_sel").length;
	$("#table_count").text(count);
	if (count == 1) {
		$(".bigbutton_sel").removeClass("bigbutton_sel");
		$(".bigbutton[index=1]").addClass("bigbutton_sel");
		$("#buttonList").show();
	} else {
		$("#buttonList").hide();
	}
	Ext.getCmp("center-deskcenterpage-container").getLayout().setActiveItem("quicktablepage");
}
function resetOpenTablePage() {
	$("#open_table_id").val("");
	$("#open_table_name").text("");
	$("#open_table_nopinput").val("");
}

// 计算账单
function colBillFee(bill) {
	var total_item_fee = 0;
	var total_recv_fee = 0;
    //alert(bill.REDUCE_FEE);
	var reduce_fee = Number(bill.REDUCE_FEE);
    //alert(bill.ITEMLIST.length);
	var payfeestr = "";
	var discount_fee = 0;
	for (var i = 0; i < bill.PACKAGELIST.length; i++) {
		total_item_fee = Number(bill.PACKAGELIST[i].PACKAGE_PRICE) + total_item_fee;
	}
	for (var i = 0; i < bill.ITEMLIST.length; i++) {
		if (bill.ITEMLIST[i].PACKAGE_ID != "") {
			continue;
		}
		var count = Number(bill.ITEMLIST[i].COUNT) - Number(bill.ITEMLIST[i].BACK_COUNT) - Number(bill.ITEMLIST[i].FREE_COUNT);
		var price = Number(bill.ITEMLIST[i].PRICE);
		var rate = Number(bill.ITEMLIST[i].PAY_RATE);
		var fee = (count * price).toFixed(2);
		var rate_fee = (count * price * rate / 100).toFixed(2);
		total_item_fee = Number(fee) + total_item_fee;
		discount_fee = discount_fee + Number(fee) - Number(rate_fee);
	}
	for (var i = 0; i < bill.FEELIST.length; i++) {
		var recv_fee = bill.FEELIST[i].FEE;
		var mode_id = bill.FEELIST[i].MODE_ID;
		payfeestr = payfeestr + bill.FEELIST[i].MODE_NAME + "\uffe5" + bill.FEELIST[i].FEE + ";";
		total_recv_fee = Number(recv_fee) + total_recv_fee;
	}
	var bill_fee = (total_item_fee).toFixed(2);
	bill.BILL_FEE = bill_fee;
    // toFixed(0) 方法会四舍五入    1.5变成2了    但是要求是1.5变成1  也就是取整  所以应该用parseInt方法
    //bill.SPAY_FEE = Number(bill_fee-total_recv_fee-reduce_fee-discount_fee).toFixed(0);
	bill.SPAY_FEE = parseInt(bill_fee - reduce_fee - discount_fee) - total_recv_fee;
	bill.RECVFEESTR = payfeestr;
	bill.BILL_RATE_FEE = discount_fee.toFixed(2);
	return bill;
}

// 初始化餐台信息页面
function initTableInfoPage(bill) {
	bill = colBillFee(bill);
	$("#tableinfo_table_id").text(bill.TABLE_ID);
	$("#tableinfo_bill_id").text(bill.BILL_ID);
	$("#tableinfo_nop").text(bill.NOP);
	$("#tableinfo_ban").text(bill.BAN);
	$("#tableinfo_start_time").text(bill.OPEN_TIME);
	$("#tableinfo_bill_fee").text(bill.BILL_FEE);
	$("#tableinfo_reducefee").text(bill.REDUCE_FEE);
	$("#tableinfo_spay_fee").text(bill.SPAY_FEE);
	$("#tableinfo_deposit").text(bill.DEPOSIT);
	if (Number(bill.INVOICE_FEE) == 0) {
		$("#tableinfo_invoice").text("\u672a\u5f00");
	} else {
		$("#tableinfo_invoice").text("\u5df2\u5f00");
	}
	if (bill.RECVFEESTR == undefined || bill.RECVFEESTR == "") {
		$("#tableinfo_recvfeestr").text("\uffe50");
	} else {
		$("#tableinfo_recvfeestr").text(bill.RECVFEESTR);
	}
	$("#billItemList").html("");
	for (var i = 0; i < bill.ITEMLIST.length; i++) {
		var note = "";
		if (Number(bill.ITEMLIST[i].BACK_COUNT) > 0) {
			note = note + "\u9000\uff1a" + bill.ITEMLIST[i].BACK_COUNT + ";";
		}
		if (Number(bill.ITEMLIST[i].FREE_COUNT) > 0) {
			note = note + "\u8d60\uff1a" + bill.ITEMLIST[i].FREE_COUNT + ";";
		}
		if (Number(bill.ITEMLIST[i].PAY_RATE) < 100) {
			note = note + "\u6298\uff1a" + bill.ITEMLIST[i].PAY_RATE + "%;";
		}
		var name = bill.ITEMLIST[i].FOOD_NAME;
		if (bill.ITEMLIST[i].PACKAGE_ID != "") {
			name = name + "[\u5957]";
		}
		if (bill.ITEMLIST[i].NOTE != "" && bill.ITEMLIST[i].NOTE != "undefined") {
			name = name + "<span style='font-size:14px;'>(" + bill.ITEMLIST[i].NOTE + ")</span>";
		}
		var item_div = "<div item_id=\"" + bill.ITEMLIST[i].ITEM_ID + "\" class=\"item_" + bill.ITEMLIST[i].STATE + "\" clazz=\"item_" + bill.ITEMLIST[i].STATE + "\" count=\"" + bill.ITEMLIST[i].COUNT + "\" >" + "<span class=\"food_name\">" + name + "</span>" + "<div><input class=\"checkfood\" type=\"checkbox\" style=\"display:none;position:absolute;top:3px;right:3px;\"/></div>" + "<span class=\"item_note\">" + note + "</span>" + "<span class=\"item_info\">\uffe5" + bill.ITEMLIST[i].PRICE + "/" + bill.ITEMLIST[i].UNIT + " X " + bill.ITEMLIST[i].COUNT + "</span>";
		$("#billItemList").append(item_div);
	}
	$("#tableinfo_discount_fee").text(bill.BILL_RATE_FEE);
	$("#select_item_count").text("0");
	$("#select_item_count").parent().hide();
	//$("#tableinfo_hidden_input")[0].focus();
	$(".bill_item_menu").hide();
}
function showBill(table_id) {
    if(ajax_flag > 0){
        return false;
    }
    ajax_flag = 1;
	$.post("/operation/checkTableState.html", {table_id:table_id}, function (result) {
		var obj = $.parseJSON(result);
	        //alert(result);
		if (obj.success == "false") {
			return false;
		}
		ajax_flag = 0;
		if (obj.state == "0") {
			resetOpenTablePage();
			Ext.getCmp("viewport").getLayout().setActiveItem("opentablepage");
			$("#open_table_id").val(obj.table.TABLE_ID);
			$("#open_table_name").text(obj.table.TABLE_NAME);
			$("#open_table_nopinput")[0].focus();
		} else {
			if (obj.state == "1") {
				bill = obj.bill;
				initTableInfoPage(obj.bill);
				Ext.getCmp("viewport").getLayout().setActiveItem("tableinfopage");
				$("#tableinfo_hidden_input")[0].focus();
			}
		}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}

function quickAddFood(table_id) {
    $.post("/operation/checkTableState.html", {table_id:table_id}, function (result) {
		//alert(result); 
		var obj = $.parseJSON(result);	   
		if (obj.success == "true") {
			if (obj.state == "0") {
				resetOpenTablePage();
				Ext.getCmp("viewport").getLayout().setActiveItem("opentablepage");
				$("#open_table_id").val(obj.table.TABLE_ID);
				$("#open_table_name").text(obj.table.TABLE_NAME);
				$("#open_table_nopinput")[0].focus();
			} else if (obj.state == "1") {
				bill = obj.bill;
				initTableInfoPage(obj.bill);
				resetAddFoodPage();
	            $("#addfood_bill_id").text( $("#tableinfo_bill_id").text() );
		        Ext.getCmp('viewport').getLayout().setActiveItem('addfoodpage');
		        $("#query_food_str")[0].focus();
			}
		}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}


function queryTableSubmit(table_id) {
	var index = $(".bigbutton_sel").attr("index");
	if (index == 1) {
		showBill(table_id);
		return false;
	} else  if (index == 2) {
	    quickPayCash(table_id)
        return false;
	} else if (index == 3) {
		quickPayByCard(table_id);
		return false;
	} else {
	    
	}
}
function resetTablePage() {
	Ext.getCmp("center-deskcenterpage-container").getLayout().setActiveItem("tablepanelpage");
	$(".smart_table").hide();
	$(".smart_table_sel").removeClass("smart_table_sel");
	$("#buttonList").hide();
	$("#smart_str").val("");
	$("#table_count").text(0);
	$(".bigbutton_sel").removeClass("bigbutton_sel");
	$(".bigbutton[index=1]").addClass("bigbutton_sel");
}

//获取楼层
function getFloors(table_data) {
	var floors = [];
	var provisionalTable = {};
	for (var i = 0; i < table_data.length; i++) {
		var table = table_data[i];
		if (!provisionalTable[table.FLOOR]) {
			floors.push(table.FLOOR);
			provisionalTable[table.FLOOR] = true;
		}
	}
	return floors;
}
function makeFloorDiv(floor_data, table_data) {
	var height = $("#center-deskcenterpage-container").height() - 50;
	var count = parseInt((height - 50) / 50);
    //alert(count);
	var width = 0;
	for (var i = 0; i < floor_data.length; i++) {
		var floor = floor_data[i];
		var id = "floor_" + floor;
		var html = "<div style=\"float:left;height:" + height + "px;border:solid 0px #FFF;margin:0px;margin-left:30px;padding:0px;\" >";
		html = html + "<div class=\"floor_name\" floor_id = \"" + floor + "\">" + floor + "</div>";
		html = html + "<div id=\"" + id + "\" \"></div>";
		html = html + "</div>";
		$("#floor_table").append(html);
		width = width + 220;
		var index = 0;
		var n = count;
		var sub_id = "";
		for (var j = 0; j < table_data.length; j++) {
            //alert("n="+n);
			var table = table_data[j];
			if (floor == table.FLOOR) {
				if (n == count) {
					index++;
					sub_id = id + "_" + index;
					var html = "<div id=\"" + sub_id + "\" style=\"float:left;height:" + (height - 60) + "px;border:solid 0px #FFF;\" >";
					html = html + "</div>";
					$("#" + id).append(html);
					if (index > 1) {
						width = width + 180;
					}
				}
				var color_str = "";
				if (table.STATE == "1") {
					color_str = " busy";
				}
				var table_html = "<div id=\"table_" + table.TABLE_ID + "\" table_id=\"" + table.TABLE_ID + "\" floor=\"" + table.FLOOR + "\" class=\"table\" >";
				table_html = table_html + "<div id=\"table_icon_" + table.TABLE_ID + "\" class=\"table_icon" + color_str + "\"></div>";
				table_html = table_html + "<div class=\"table_name\">" + table.TABLE_NAME + "</div>";
				table_html = table_html + "</div>";
				$("#" + sub_id).append(table_html);
				n--;
				if (n <= 0) {
					n = count;
				}
			}
		}
	}
    //alert(width);
	$("#floor_table").width(width);
}
function changeSelTableButton() {
	var index = $(".bigbutton_sel").attr("index");
	$(".bigbutton_sel").removeClass("bigbutton_sel");
	index = parseInt(index) + 1;
	if (index == 4) {
		index = 1;
	}
	$(".bigbutton[index=" + index + "]").addClass("bigbutton_sel");
}

function changeSelectCount(){
    if($(".checkfood:checkbox:checked").length>0){
        $("#select_item_count").text($(".checkfood:checkbox:checked").length);
        $("#select_item_count").parent().show();
        $(".bill_item_menu").show();
    }else{
        $("#select_item_count").text('0');
        $("#select_item_count").parent().hide();
        $(".bill_item_menu").hide();
    }
}
	
	
// 重置点菜页面
function resetAddFoodPage() {
	$("#addfood_total_note").val("");
	$("#query_food_str").val("");
	$(".order_tr").remove().empty();
	$(".addfooditem").remove();
	$("#select_food_count").text("0");
	$("#select_food_money").text("0");
	$("#foodlist").children().appendTo("#hiddenlist");
	$("#selectlist").children().appendTo("#hiddenlist");
	$(".food[show_tag=Y]").appendTo("#foodlist");
	$("#query_food_str")[0].focus();
	$("#addfood_table_name").text($("#tableinfo_table_id").text());
}
function resetPayCashPage() {
	$("#paycash_table_name").text("");
	$("#paycash_owefee").text("");
	$("#paycash_backfee").text("0");
	$("#recvfeeinput").val("");
	$("#paycash_bill_id").val("");
	$("#recvfeeinput")[0].blur();
}

function quickPayCash(table_id) {
    $.post("/operation/checkTableState.html", {table_id:table_id}, function (result) {
		var obj = $.parseJSON(result);	   
	        //alert(result);         
		if (obj.success == "false") {
			return false;
		}
		if (obj.state == "0") {
			resetOpenTablePage();
			Ext.getCmp("viewport").getLayout().setActiveItem("opentablepage");
			$("#open_table_id").val(obj.table.TABLE_ID);
			$("#open_table_name").text(obj.table.TABLE_NAME);
			$("#open_table_nopinput")[0].focus();
		} else {
			if (obj.state == "1") {
				bill = obj.bill;
				initTableInfoPage(obj.bill);
				Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
		        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
		        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
		        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
		        $("#paycash_backfee").text('0');
		        $("#mode_id").text('xj');
		        $("#mode_name").text('现金');
		        $('#recvfeeinput')[0].focus();
			}
		}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}

function quickPayByCard(table_id) {
    $.post("/operation/checkTableState.html", {table_id:table_id}, function (result) {
		var obj = $.parseJSON(result);	   
	    //alert(result);         
		if (obj.success == "true") {
		    if (obj.state == "0") {
				resetOpenTablePage();
				Ext.getCmp("viewport").getLayout().setActiveItem("opentablepage");
				$("#open_table_id").val(obj.table.TABLE_ID);
				$("#open_table_name").text(obj.table.TABLE_NAME);
				$("#open_table_nopinput")[0].focus();
			} else if (obj.state == "1") {
				bill = obj.bill;
				initTableInfoPage(obj.bill);
				Ext.getCmp('viewport').getLayout().setActiveItem('paycashpage');
		        $("#paycash_bill_id").val( $("#tableinfo_bill_id").text() );
		        $("#paycash_table_name").text( $("#tableinfo_table_id").text() );
		        $("#paycash_owefee").text($("#tableinfo_spay_fee").text());
		        $("#paycash_backfee").text('0');
		        $("#mode_id").text('sk');
		        $("#mode_name").text('刷卡');
		        $('#recvfeeinput')[0].focus();
			}
		}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}

function payCash() {
    if(ajax_flag > 0){
        return false;
    }
	if ($("#recvfeeinput").val() == "") {
		$("#recvfeeinput")[0].focus();
		return false;
	}
	if (isNaN($("#recvfeeinput").val())) {
		alert("收款金额不合法");
		$("#recvfeeinput")[0].focus();
		return false;
	}
	var zhaoling = Number($("#paycash_owefee").text()) - Number($("#recvfeeinput").val());
	var recvfee = $("#recvfeeinput").val();
	if (Number($("#paycash_owefee").text()) < Number($("#recvfeeinput").val())) {
		recvfee = $("#paycash_owefee").text();
	}
	if (Number(recvfee) == 0) {
		return false;
	}
	ajax_flag = 1;
	//alert(" jiaofei , paycash_bill_id = "+$("#paycash_bill_id").val()+" , fee = "+recvfee);
	$.post("/operation/payFee.html", {
	        bill_id : $("#paycash_bill_id").val(),
	        mode_id : $("#mode_id").text(),
	        recvfee : recvfee
	    }, function (result) {
			var obj = $.parseJSON(result);	   
			if (obj.success == "true") {
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
	            resetPayCashPage();
			}else{
			    alert(obj.msg);
			}
			ajax_flag = 0;
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}

function resetReduceFeePage() {
	$("#reducefee_table_name").text('');
    $("#reducefee_owefee").text('');
    $("#reducefeeinput").val('');
    $("#reducefeeinput")[0].blur();
}

function reduceFee(){
    if(ajax_flag > 0){
        return false;
    }
    if($("#reducefeeinput").val()==''){
        $('#reducefeeinput')[0].focus();
        return false;
    }
    if( isNaN( $("#reducefeeinput").val() ) ){
        alert("金额不合法");
        $('#reducefeeinput')[0].focus();
        return false;
    }
    var reducefee = $("#reducefeeinput").val();
    if(Number(reducefee)<0){
        alert("抹零金额只能为正整数！");
        $('#reducefeeinput')[0].focus();
        return false;
    }
    if(Number($('#reducefee_owefee').text()) < Number(reducefee)){
        alert("抹零金额不能大于账单应收金额！");
        $('#reducefeeinput')[0].focus();
        return false;
    }
    ajax_flag = 1;
    $.post("/operation/reduceFee.html", {
	        bill_id:$("#tableinfo_bill_id").text() ,
	        reducefee : reducefee
	    }, function (result) {
			var obj = $.parseJSON(result);	   
			if (obj.success == "true") {
			    var obj = $.parseJSON(result);	            
	            if(obj.success=='true'){
	                initTableInfoPage(obj.bill)
		            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
		            $("#tableinfo_hidden_input")[0].focus();
		            resetReduceFeePage();
	            }
			}else{
			    alert(obj.msg);
			}
			ajax_flag = 0;
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}
    
    
function submitTempFoodForm(){
    if(ajax_flag > 0){
        return false;
    }
    var foodname = $.trim($("#temp_foodname_input").val());
    var price = $.trim($("#temp_price_input").val());
    var count = $.trim($("#temp_count_input").val());
    if(foodname==''){
        alert("请输入临时菜品名称");
        $("#temp_foodname_input").focus();
        return false;
    }
    if(price==''){
        alert("请输入临时菜品单价");
        $("#temp_price_input").focus();
        return false;
    }
    if(isNaN(price)){
        alert("菜品单价格式错误,请输入数字");
        $("#temp_price_input").focus();
        return false;
    }
    if(count==''){
        alert("请输入临时菜品数量");
        $("#temp_count_input").focus();
        return false;
    }
    if(isNaN(count)){
        alert("菜品数量格式错误,请输入数字");
        $("#temp_count_input").focus();
        return false;
    }
    if($(".printer_sel").length!=1){
        alert("请选择出单打印机");
        return false;
    }
    ajax_flag = 1;
    //var printer = $.trim($(".printer_sel").text());
    var printer = $.trim($(".printer_sel").attr("name"));
    var item = {};
    item.bill_id=$("#addtempfood_bill_id").val();
    item.food_name=foodname;
    item.price=price;
    item.count=count;
    item.printer=printer;
    $.post("/operation/addTempFood.html", {bill_id:$("#addtempfood_bill_id").val() ,item_str : Ext.encode(item)}, function (result) {
		ajax_flag = 0;
		var obj = $.parseJSON(result);	   
		if (obj.success == "true") {
		    var obj = $.parseJSON(result);	            
            if(obj.success=='true'){
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
            }else{
                alert(obj.msg);
            }
		}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}
	
// 重置添加临时菜页面
function resetAddTempFoodPage(){
    $("#addtempfood_bill_id").val('');
    $("#temp_foodname_input").val('');
    $("#temp_price_input").val('');
    $("#temp_count_input").val('1');
    $("#tempfoodhiddenbtn").blur();
}
    
// 重置退菜 赠送  打折 转台菜品页面
function resetOpearteFoodPage(){
    $("#operatefood_title").text('');
    $("#operate_type").val('');
    $("#operate_value").val('');
    $("#operate_value")[0].blur();
}

function cancelFood(){
    if($("#operate_value").val()==''){
        $('#operate_value')[0].focus();
        return false;
    }
    if( isNaN( $("#operate_value").val() ) ){
        alert("只能输入数字");
        $('#operate_value')[0].focus();
        return false;
    }
    var value = Number($('#operate_value').val());
    if( value == 0 ){
        alert("退菜数量不能为0");
        $('#operate_value')[0].focus();
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
    $.post("/operation/cancelFood.html", { 
	        bill_id:$("#tableinfo_bill_id").text() ,
	        item_str : Ext.encode(items),
	        count : $("#operate_value").val()
        }, function (result) {
			var obj = $.parseJSON(result);	   
			if (obj.success == "true") {
		        resetOpearteFoodPage();
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
			}else{
                alert(obj.msg);
                $('#operate_value')[0].focus();
            }
            ajax_flag = 0;
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
    return false;
}

function presentFood(){
    if($("#operate_value").val()==''){
        $('#operate_value')[0].focus();
        return false;
    }
    if( isNaN( $("#operate_value").val() ) ){
        alert("只能输入数字");
        $('#operate_value')[0].focus();
        return false;
    }
    var value = Number($('#operate_value').val());
    /*
    if( value == 0 ){
        alert("赠送数量不能为0");
        $('#operate_value')[0].focus();
        return false;
    }
    */
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
    $.post("/operation/presentFood.html", { 
	        bill_id:$("#tableinfo_bill_id").text() ,
	        item_str : Ext.encode(items),
	        count : $("#operate_value").val()
        }, function (result) {
			var obj = $.parseJSON(result);	   
			if (obj.success == "true") {
		        resetOpearteFoodPage();
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
			}else{
                alert(obj.msg);
                $('#operate_value')[0].focus();
            }
            ajax_flag = 0;
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
    return false;
}

function derateFood(){
    if($("#operate_value").val()==''){
        $('#operate_value')[0].focus();
        return false;
    }
    if( isNaN( $("#operate_value").val() ) ){
        alert("只能输入数字");
        $('#operate_value')[0].focus();
        return false;
    }
    var value = Number($('#operate_value').val());
    if(  value < 0 || value >100 ){
        alert("打折百分比必须为0-100之间的整数");
        $('#operate_value')[0].focus();
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
    $.post("/operation/derateFood.html", { 
	        bill_id:$("#tableinfo_bill_id").text() ,
	        item_str : Ext.encode(items),
	        count : $("#operate_value").val()
        }, function (result) {
            ajax_flag = 0;
			var obj = $.parseJSON(result);	   
			if (obj.success == "true") {
		        resetOpearteFoodPage();
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
			}else{
                alert(obj.msg);
                $('#operate_value')[0].focus();
            }
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
    return false;
}

function changeFoodTable(){
    if($("#operate_value").val()==''){
        $('#operate_value')[0].focus();
        return false;
    }
    if( isNaN( $("#operate_value").val() ) ){
        alert("目标台位号只能输入数字");
        $('#operate_value')[0].focus();
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
    $.post("/operation/changeTable.html", { 
	        bill_id:$("#tableinfo_bill_id").text() ,
	        item_str : Ext.encode(items),
	        count : $("#operate_value").val()
        }, function (result) {
            ajax_flag = 0;
			var obj = $.parseJSON(result);	   
			if (obj.success == "true") {
		        resetOpearteFoodPage();
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
			}else{
                alert(obj.msg);
                $('#operate_value')[0].focus();
            }
            
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
    return false;
}

function submitOperateFood(){
    if($("#operate_type").val()=='cancel'){
        cancelFood();
    } else if($("#operate_type").val()=='present'){
        presentFood();
    } else if($("#operate_type").val()=='discount'){
        derateFood();
    } else if($("#operate_type").val()=='changetable'){
        changeFoodTable();
    }
    return false;
}

function closeBill(bill_id,table_id) {
    if(ajax_flag > 0){
        return false;
    }
    ajax_flag = 1;
	$.post("/operation/closeBill.html", { 
	        bill_id: bill_id
        }, function (result) {
			var obj = $.parseJSON(result);
			ajax_flag = 0;
			if (obj.success == "true") {
			    alert(obj.msg);	
		        resetTablePage();
                Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
                $("#smart_str")[0].focus();
                $("#table_icon_"+table_id).removeClass("busy");
                if(obj.bill==""){
                    return false;
                }
                var data = {};
			    data.type="printbill";
			    data.bill = obj.bill;
			    data.bill.PRINTER = default_printer;
			    var socket = new WebSocket(socketurl); 
				    // 打开Socket 
					socket.onopen = function(event) { 
						// 发送一个初始化消息
						socket.send(JSON.stringify(data)); 
						// 监听消息
						socket.onmessage = function(event) { 
						    var card_no = event.data;
						    socket.close();
					}; 
					// 监听Socket的关闭
					socket.onclose = function(event) { 
					    //alert("Client notified socket has closed");
					};
					socket.onerror = function(event) { 
					    //alert(" onerror  ");
					    socket.close();
					};
			    };
			    
			}else{
			    alert(obj.msg);	
			}
			return false;
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
    return false;
}

// 初始化今日统计页面
function initTodayPage(data){
    var recv_total = 0;
    $("#recv_table").html('');
    for(var i=0;i<data.recv_data.length;i++){
        var temp = data.recv_data[i];
        recv_total = recv_total + Number(temp.recv_fee);
        var str = '<tr height="30" >'+
          '<td width="120"><span style="margin-left:30px;">'+temp.mode_name+'：</span></td>'+
          '<td ><span id="">￥'+temp.recv_fee+'</span></td>'+
          '</tr>';
        $("#recv_table").append(str);
    }
    var str = '<tr height="30" >'+
      '<td width="120"><span style="margin-left:30px;">合计：</span></td>'+
      '<td ><span id="">￥'+recv_total+'</span></td>'+
      '</tr>';
    $("#recv_table").append(str);
        
    $("#floor_data_table").html('');
    var floor_total = 0;
    for(var i=0;i<data.floor_data.length;i++){
        var temp = data.floor_data[i];
        floor_total = floor_total + Number(temp.money);
        var str = '<tr height="30" >'+
          '<td width="120"><span style="margin-left:30px;">'+temp.floor+'：</span></td>'+
          '<td ><span id="">￥'+temp.money+'</span></td>'+
          '</tr>';
        $("#floor_data_table").append(str);
    }
    var str = '<tr height="30" >'+
      '<td width="120"><span style="margin-left:30px;">合计：</span></td>'+
      '<td ><span id="">￥'+floor_total+'</span></td>'+
      '</tr>';
    $("#floor_data_table").append(str);
    
    
    $("#moling_money").text(data.item_data[0].moling_money);
    $("#discount_money").text(Number(data.item_data[0].discount_money).toFixed(2));
    $("#lose_money").text(Number(data.item_data[0].lose_money).toFixed(2));
    $("#total_person").text(data.item_data[0].total_person);
    
    $("#recv_total").text(recv_total);
    $("#close_count").text(data.bill_count_data[0].close_count);
    $("#open_count").text(data.bill_count_data[0].open_count);
    $("#unrecv_money").text(Number(data.unrecv_data[0].money).toFixed(2));
    
    $("#hope_total_recv").html( Number(  ( Number( $("#recv_total").text()) + Number($("#unrecv_money").text()) )  ).toFixed(2)  );
    
    //alert(data.item_data[0].total_person);
    
    $("#average_money").html( (Number($("#hope_total_recv").text())/Number(data.item_data[0].total_person)).toFixed(2) );
    
    $("#category_table").html('');
    for(var i=0;i<data.category_data.length;i++){
        var temp = data.category_data[i];
        var str = '<tr height="30" >'+
          '<td width="120"><span style="margin-left:30px;">'+temp.category+'：</span></td>'+
          '<td style="display:none;">￥'+Number(temp.money).toFixed(2)+'</td>'+
          '<td>￥'+Number(temp.real_money).toFixed(2)+'</td>'+
          '</tr>';
        $("#category_table").append(str);
    }
    
    
    var package_total = 0;
    for(var i=0;i<data.package_data.length;i++){
        var temp = data.package_data[i];
        package_total = package_total + Number(temp.PACKAGE_PRICE);
    }
    $("#package_count").text(data.package_data.length);
    $("#package_money").text(package_total);
}


function initSelectVipCardPage(data){
    $("#vipcard_list").html('');
    for(var i=0;i<data.length;i++){
        var temp = data[i];
        var str = '<div class="vipcard" user_id="'+temp.USER_ID+'" custname="'+temp.CUSTNAME+'" card_no="'+temp.CARD_NO+'">'+
          '<div class="credit_name">卡号 ： '+temp.CARD_NO+'</div>'+
          '<div class="credit_value">余额：￥'+temp.MONEY+'</div>'+
          '</div>';
        $("#vipcard_list").append(str);
    }
}

function resetVipCardPayPage(){
    $("#vipcard_list").html('');
    $("#vipcardpay_user_id").val('');
    $("#vipcardpay_table_name").text('');
    $("#pay_card_no").text('');
    $("#vipcardpay_owefee").text('');
    $('#vipcardpayfeeinput').val('');
}

function payByVipCardUser(){
    if(ajax_flag > 0){
        return false;
    }
    if ($("#vipcardpayfeeinput").val() == "") {
		$("#vipcardpayfeeinput")[0].focus();
		return false;
	}
	if (isNaN($("#vipcardpayfeeinput").val())) {
		alert("收费金额不合法");
		$("#vipcardpayfeeinput")[0].focus();
		return false;
	}
    ajax_flag = 1;
    var bill_id = $("#tableinfo_bill_id").text();
    var user_id = $('#vipcardpay_user_id').val();
    var card_no = $("#pay_card_no").text();
    var fee = $('#vipcardpayfeeinput').val();
    $.post("/operation/payByVipCard.html", {
	        bill_id : bill_id,
	        user_id : user_id,
	        card_no : card_no,
	        recvfee : fee
	    }, function (result) {
			var obj = $.parseJSON(result);
			ajax_flag = 0;
			if (obj.success == "true") {
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
	            resetVipCardPayPage();
	            
	            var data = {};
			    data.type="printvipbill";
			    data.data = obj.info;
			    data.data.printer = default_printer;
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
			    
	            
			}else{
			    alert(obj.msg);
			}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}




function initSelectCreditPage(data){
    $("#credit_list").html('');
    for(var i=0;i<data.length;i++){
        var temp = data[i];
        var str = '<div class="credit" user_id="'+temp.USER_ID+'" custname="'+temp.CUSTNAME+'">'+
          '<div class="credit_name">'+temp.CUSTNAME+'</div>'+
          '<div class="credit_limit">信用：￥'+temp.CREDIT+'</div>'+
          '<div class="credit_value">可用：￥'+temp.MONEY+'</div>'+
          '</div>';
        $("#credit_list").append(str);
    }
}

function resetCreditPayPage(){
    $("#credit_list").html('');
    $("#creditpay_user_id").val('');
    $("#creditpay_table_name").text('');
    $("#pay_credit_user").text('');
    $("#creditpay_owefee").text('');
    $('#creditpayfeeinput').val('');
}

function payByCreditUser(){
    if(ajax_flag > 0){
        return false;
    }
    if ($("#creditpayfeeinput").val() == "") {
		$("#creditpayfeeinput")[0].focus();
		return false;
	}
	if (isNaN($("#creditpayfeeinput").val())) {
		alert("挂账金额不合法");
		$("#creditpayfeeinput")[0].focus();
		return false;
	}
    ajax_flag = 1;
    var bill_id = $("#tableinfo_bill_id").text();
    var user_id = $('#creditpay_user_id').val();
    var fee = $('#creditpayfeeinput').val();
    $.post("/operation/payByCreditUser.html", {
	        bill_id : bill_id,
	        user_id : user_id,
	        recvfee : fee
	    }, function (result) {
			var obj = $.parseJSON(result);
			ajax_flag = 0;   
			if (obj.success == "true") {
                initTableInfoPage(obj.bill)
	            Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
	            $("#tableinfo_hidden_input")[0].focus();
	            resetCreditPayPage();
	            
	            var data = {};
			    data.type="printcreditbill";
			    data.data = obj.info;
			    data.data.printer = default_printer;
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
			    
			}else{
			    alert(obj.msg);
			}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});
}



function printBill(table_id){
    if(ajax_flag > 0){
        return false;
    }
    ajax_flag = 1;
    $.post("/localprint/querybillinfobytable.html", {
	        table_id : table_id
	    }, function (result) {
			var obj = $.parseJSON(result);
			ajax_flag = 0;   
			if (obj.success == "true") {
			    var data = {};
			    data.type="printbill";
			    data.bill = obj.bill;
			    data.bill.PRINTER = default_printer;
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
			}else{
			    alert(obj.msg);
			}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});


}

function printBillByBillId(bill_id){
    if(ajax_flag > 0){
        return false;
    }
    ajax_flag = 1;
    $.post("/localprint/querybillinfobybillid.html", {
	        bill_id : bill_id
	    }, function (result) {
			var obj = $.parseJSON(result);
			ajax_flag = 0;   
			if (obj.success == "true") {
			    var data = {};
			    data.type="printbill";
			    data.bill = obj.bill;
			    data.bill.PRINTER = default_printer;
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
			}else{
			    alert(obj.msg);
			}
	}).error(function(){
	    ajax_flag = 0;
	    alert("系统异常"); 
	});


}


