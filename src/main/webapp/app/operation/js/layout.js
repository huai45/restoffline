Ext.onReady(function(){
    
    var tablepanelpage = new Ext.Panel({   
        id: 'tablepanelpage',
        border:false,
	    autoScroll : true,
        layout: 'fit',
        bodyStyle: 'background:url(/app/platform/image/bg.png) no-repeat #3A3A40;background-position: -280px -80px;',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        contentEl: 'tablepanelpage_center',
        listeners:{"render":function(obj){
	        $("#tablepanelpage_center").show();
	    }}
    });
    
    
    var quicktablepage = new Ext.Panel({   
        id: 'quicktablepage',
        border:false,
	    autoScroll : true,
        layout: 'fit',
        bodyStyle: 'background:#3A3A40;color:#FFF;',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        contentEl: 'quicktablepage_center',
        listeners:{"render":function(obj){
	        $("#quicktablepage_center").show();
	    }}
    });
    
    var deskcenterpage = new Ext.Panel({   
		id: 'deskcenterpage',   
		region: 'center',
        border:false,
     	autoScroll : false,
        layout: 'border',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        items: [{id: 'north-deskcenterpage-container',
	          region:'north',
	          xtype: 'panel',
	          layout: 'fit',
	          autoScroll : false,
	          border:false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          height: 80,
	          collapsible: false, 
	          split: false,
	          bodyStyle: 'background:#3A3A40;',
	          contentEl: 'deskcenter_north',
	          listeners:{"render":function(obj){
		          $("#deskcenter_north").show();
		      }}
	        },{
	          id: 'center-deskcenterpage-container',
	          region: 'center',
	          border:false,
		      layout: 'card',
			  autoScroll : false,
			  paddings: '0 0 0 0',
			  margins: '0 0 0 0',
			  activeItem: 0,    
			  items: [tablepanelpage,quicktablepage]
	        }]
	});
    
    var deskpage = new Ext.Panel({   
		id: 'deskpage',   
        border:false,
     	autoScroll : false,
        layout: 'border',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        items: [{id: 'north-deskpage-container',
	          region:'north',
	          xtype: 'panel',
	          layout: 'fit',
	          autoScroll : false,
	          border:false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          height: 40,
	          collapsible: false, 
	          split: false,
	          bodyStyle: 'background:#262626;margin:0px;padding:0px;',
	          contentEl: 'deskpage_north',
	          listeners:{"render":function(obj){
		          $("#deskpage_north").show();
		      }}
	        } , deskcenterpage ]
	});

    
    var opentablepage = new Ext.Panel({   
		id: 'opentablepage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'openTablePage',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#openTablePage").show()
		}}
	});

    var tableinfopage = new Ext.Panel({   
		id: 'tableinfopage',   
		border:false,
        layout: 'border',
	    autoScroll : false,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
	    items: [{
	          id: 'north-tableinfopage-container',
	          region:'north',
	          xtype: 'panel',
	          layout: 'fit',
	          autoScroll : false,
	          border:true,
	          margins: '0 0 0 0',
	          height: 46,
	          collapsible: false, 
	          split: false,
	          bodyStyle: 'background:#000 url(/image/desk11.jpg) no-repeat;border:solid 0px #444;border-bottom:solid 1px #444;margin:0px;padding:0px;',
	          contentEl: 'tableinfo_head',
	          listeners:{"render":function(obj){
		          $("#tableinfo_head").show();
		      }}
	      },{
	          id: 'center-tableinfopage-container',
	          region: 'center',
	           border:false,
		      autoScroll : true,
	          layout: 'fit',
	          border:false,
	          contentEl: 'tableinfo_center',
	          bodyStyle: 'background:#323232 url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
	          paddings: '0 0 0 0',
	          margins: '0 0 0 0',
	          listeners:{"render":function(obj){
		          $("#tableinfo_center").show();
		      }}
	      },{
	          id: 'east-tableinfopage-container',
	          region:'west',
	          xtype: 'panel',
	          //border:false,
	          layout: 'fit',
	          autoScroll : true,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          width: 275,
	          height:2400,
	          collapsible: false,
	          bodyStyle: 'background:#E5E5E5;margin: 0px; padding:0px;border-right:solid 1px #999;',
	          contentEl: 'tableinfo_east',
	          listeners:{"render":function(obj){
		          $("#tableinfo_east").show();
		      }}
	      },{
	          id: 'south-tableinfopage-container',
	          region:'south',
	          xtype: 'panel',
	          border:true,
	          autoScroll : false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          height: 0,
	          collapsible: false,
	          bodyStyle: 'background:#000;margin: 0px; padding:0px;border:solid 0px #444;border-top:solid 1px #444;',
	          contentEl: 'tableinfo_south',
	          layout: 'fit',
	          listeners:{"render":function(obj){
		          $("#tableinfo_south").show();
		      }}
	      }]
	});
	
	var foodpanel = new Ext.Panel({ 
        layout: 'border',
        border:false,
	         items: [{
	          id: 'north-foodpanel-container',
	          region:'north',
	          xtype: 'panel',
	          layout: 'fit',
	          autoScroll : false,
	          margins: '0 0 0 0',
	          height: 60,
	          collapsible: false, 
	          split: false,
	          bodyStyle: 'background-color:#FFF;margin:0px;padding:0px;border:solid 0px #CCC;border-bottom:solid 1px #EDEDED;',
	          contentEl: 'foodpanel_head',
	          listeners:{"render":function(obj){
		          $("#foodpanel_head").show();
		      }}
	      },{
	          id: 'center-foodpanel-container',
	          region: 'center',     // center region is required, no width/height specified
	          border:false,
		      autoScroll : true,
	          layout: 'fit',
	          border:false,
	          bodyStyle: 'background-color:#FFF;margin:0px;padding:0px;',
	          paddings: '0 0 0 0',
	          margins: '0 0 0 0',
	          contentEl: 'addfoodwest_center',
	          listeners:{"render":function(obj){
		          $("#addfoodwest_center").show();
		      }}
	      }]
	});
	
	var addfoodpage = new Ext.Panel({   
		id: 'addfoodpage',   
		border:false,
        layout: 'border',
	    autoScroll : false,
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        items: [{
	          id: 'north-addfoodpage-container',
	          region:'north',
	          xtype: 'panel',
	          layout: 'fit',
	          autoScroll : false,
	          border:true,
	          margins: '0 0 0 0',
	          height: 46,
	          collapsible: false, 
	          split: false,
	          bodyStyle: 'background:#000;border:solid 0px #444;border-bottom:solid 1px #444;margin:0px;padding:0px;',
	          contentEl: 'addfood_head',
	          listeners:{"render":function(obj){
		          $("#addfood_head").show();
		      }}
	      },{
	          id: 'center-addfoodpage-container',
	          region: 'center',     // center region is required, no width/height specified
	           border:false,
		      autoScroll : true,
	          layout: 'fit',
	          border:false,
	          contentEl: 'addfood_center',
	          bodyStyle: 'background-color:#F3F3F3;margin:0px;padding:0px;',
	          paddings: '0 0 0 0',
	          margins: '0 0 0 0',
	          listeners:{"render":function(obj){
		          $("#addfood_center").show();
		      }}
	      },{
	          id: 'west-addfoodpage-container',
	          region:'west',
	          xtype: 'panel',
	          layout: 'fit',
	          autoScroll : true,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          width: 370,
	          collapsible: false,   // make collapsible
	          bodyStyle: 'border:solid 0px #999;border-right:solid 1px #999;background:#000;margin: 0px; padding:0px;',
	          items:[foodpanel]
	      },{
	          id: 'south-addfoodpage-container',
	          region:'south',
	          xtype: 'panel',
	          layout: 'fit',
	          border:false,
	          autoScroll : false,
	          margins: '0 0 0 0',
	          paddings: '0 0 0 0',
	          height: 4,
	          collapsible: false,   // make collapsible
	          bodyStyle: 'background:#000;margin: 0px; padding:0px;border:solid 0px #444;border-top:solid 1px #444;',
	          contentEl: 'addfood_south',
	          listeners:{"render":function(obj){
		          $("#addfood_south").show();
		      }}
	      }]
	});
	
	var paycashpage = new Ext.Panel({   
		id: 'paycashpage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'paycash_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#paycash_center").show()
		}}
	});
	
	var reducefeepage = new Ext.Panel({   
		id: 'reducefeepage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'reducefee_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#reducefee_center").show()
		}}
	});
	var addtempfoodpage = new Ext.Panel({   
		id: 'addtempfoodpage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'addtempfood_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#addtempfood_center").show()
		}}
	});
	
	var operatefoodpage = new Ext.Panel({   
		id: 'operatefoodpage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'operatefood_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#operatefood_center").show()
		}}
	});
	
	var selectvipcardpage = new Ext.Panel({   
		id: 'selectvipcardpage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'selectvip_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#selectvip_center").show()
		}}
	});
	
	var vipcardpaypage = new Ext.Panel({   
		id: 'vipcardpaypage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'vipcardpay_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#vipcardpay_center").show()
		}}
	});
	
	var selectcreditpage = new Ext.Panel({   
		id: 'selectcreditpage',   
		border:false,
        layout: 'fit',
	    autoScroll : true,  // 如果是false  挂账用户太多  没有滚动条 就显示不出来了   
        contentEl:'selectcredit_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#selectcredit_center").show()
		}}
	});
	
	var creditpaypage = new Ext.Panel({   
		id: 'creditpaypage',   
		border:false,
        layout: 'fit',
	    autoScroll : false,
        contentEl:'creditpay_center',
        paddings: '0 0 0 0',
        margins: '0 0 0 0',
        bodyStyle: 'background:#2A2A2A url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;',
        listeners:{"show":function(obj){
		    $("#creditpay_center").show()
		}}
	});
	
	var todaypage = new Ext.Panel({   
	    id: 'todaypage',   
	    border:false,
        layout: 'border',
	    autoScroll : false,
	    paddings: '0 0 0 0',
        margins: '0 0 0 0',
	    items: [{
	          id: 'west-todaypage-container',
	          region:'west',
	          xtype: 'panel',
	          layout: 'fit',
	          autoScroll : false,
	          margins: '0 0 0 0',
	          width: 120,
	          collapsible: false, 
	          split: false,
	          border:false,
	          bodyStyle: 'background-color:#3A3A40;margin:0px;padding:0px;',
	          contentEl: 'today_west',
	          listeners:{"render":function(obj){
		          $("#today_west").show();
		      }}
	      },{
	          id: 'center-todaypage-container',
	          region: 'center',     // center region is required, no width/height specified
	          border:false,
		      autoScroll : true,
	          layout: 'fit',
	          border:false,
	          paddings: '0 0 0 0',
	          margins: '0 0 0 0',
	          bodyStyle: 'background-color:#FFF;margin:0px;padding:0px;padding-top:10px;padding-left:50px;',
	          contentEl: 'today_center',
	          listeners:{"render":function(obj){
		          $("#today_center").show();
		      }}
	      }]
	});
	
	var apppage = new Ext.Panel({   
		id: 'apppage',   
		border:false,
		region:'center',
        //frame:true, 
        layout: 'fit',
	    autoScroll : false,
        html:"<iframe id='mainFrame' src='/transPage?page=/operation/blank' frameborder='0' width='100%' height='100%' ></iframe>",
        paddings: '0 0 0 0',
        margins: '0 0 0 0'
	});
	
	var viewport = new Ext.Viewport({
	    id:"viewport",
		border:false,
	    layout: 'card',
	    autoScroll : false,
	    paddings: '0 0 0 0',
	    margins: '0 0 0 0',
	    activeItem: 0,    
	    items: [deskpage,opentablepage,tableinfopage,addfoodpage,paycashpage,reducefeepage,addtempfoodpage,
	        operatefoodpage,selectvipcardpage,vipcardpaypage,selectcreditpage,creditpaypage,todaypage,apppage]
	});

    height = $("#deskpage").height();
    $("#openTablePageCenter").css("margin-top",(height-340)/2);
    $("#paycashdiv").css("margin-top",(height-450)/2);
    $("#reducefeediv").css("margin-top",(height-400)/2);
    $("#tempfooddiv").css("margin-top",(height-500)/2);
    $("#operatefooddiv").css("margin-top",(height-340)/2);
    $("#vipcardpaydiv").css("margin-top",(height-450)/2);
    $("#creditpaydiv").css("margin-top",(height-450)/2);
    
    
    floors = getFloors(tables);
    makeFloorDiv(floors,tables);
    
    
    new Ext.KeyMap(el,{
        key:Ext.EventObject.ESC,
        fn: function(e){
            if(window_tag==1){
                food_note_window.hide();
                window_tag=0;
                return false;
            }
            //alert(Ext.getCmp('center-main-container').layout.activeItem.id);
            if(Ext.getCmp('viewport').layout.activeItem.id=='deskpage'){
                resetTablePage();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='opentablepage'){
                resetTablePage();
                Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
                resetOpenTablePage();
                $("#smart_str")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='tableinfopage'){
                resetTablePage();
                Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
                $("#smart_str")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='addfoodpage'){
                resetAddFoodPage();
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='paycashpage'){
                resetPayCashPage();
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='reducefeepage'){
                resetReduceFeePage();
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='addtempfoodpage'){
                resetAddTempFoodPage();
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='operatefoodpage'){
                resetOpearteFoodPage();
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='selectvipcardpage'){
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
                $("#vipcard_list").html('');
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='vipcardpaypage'){
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='selectcreditpage'){
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
                $("#credit_list").html('');
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='creditpaypage'){
                Ext.getCmp('viewport').getLayout().setActiveItem('tableinfopage');
                $("#tableinfo_hidden_input")[0].focus();
                resetCreditPayPage();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='todaypage'){
                resetTablePage();
                Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
                $("#smart_str")[0].focus();
            }else if(Ext.getCmp('viewport').layout.activeItem.id=='apppage'){
                resetTablePage();
                Ext.getCmp('viewport').getLayout().setActiveItem('deskpage');
                $("#smart_str")[0].focus();
                $("#mainFrame").attr("src","/app/operation/blank.jsp?time="+new Date());
            }
        },
        scope : el
    });
    
    food_note_window = new Ext.Window({   
		id: 'food_note_window',   
		border:false,
        bodyBorder :false,
		header: false,
        layout: 'fit',
        width:720,  
        height:480,  
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
        contentEl:'food_note_center',
        bodyStyle: 'background:#ECECEC url(/image/desk1.jpg) no-repeat;margin:0px;padding:0px;color:#fff;',
        listeners:{"show":function(obj){
		    $("#food_note_center").show();
		    window_tag=1;
		},"hide":function(obj){
		    window_tag=0;
		    $("#note_input").val('');
		    $("#query_food_str")[0].focus();
        }}
	});
    
});