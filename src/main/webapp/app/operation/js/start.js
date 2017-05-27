Ext.onReady(function(){

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
    
	Ext.getDoc().on("contextmenu", function(e){
        e.stopEvent();
    });
    




});