package com.huai.common.util;

public class Dom {

	public static String AlertScript(){
		StringBuffer sb = new StringBuffer();
		sb.append("var alertWin = new Ext.Window({");
		sb.append("    id:'alertWin',layout: 'fit',width: 450,height: 230,modal:true, plain:true, frame : true,closable:false,");
		sb.append("    closeAction: 'hide',resizable:false,border:false,bodyStyle: \"background:#fff;margin: 0px; padding:0px;font-family:'Microsoft YaHei';\",");
		sb.append("    contentEl: 'alertwin',");
		sb.append("    listeners:{'show':function(obj){");
		sb.append("        $('#alertwin').show()");
		sb.append("    }}");
		sb.append("});");
		sb.append("$('#alertClose').click(function(){ ");
		sb.append("    $('#alertMsg').text('');  alertWin.hide(); eval($('#alertCallBack').text()); ");
		sb.append("});");
		return sb.toString();
	};
	
	public static String AlertDom(){
		return AlertDom("094AB2");
	};
	
	public static String AlertDom(String color){
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='alertwin' style='display:none;background:#fff;width:1024px;height:530px;'>");
		sb.append("  <div id='' style='height:140px;'>");
		sb.append("    <div id='alertTitle' style='margin-left:20px;padding-top:15px;margin-bottom:0px;font-size:26px;font-weight:normal;'>提示信息</div>");
		sb.append("    <div id='alertMsg' style='margin-left:20px;padding-top:20px;margin-bottom:15px;font-size:16px;font-weight:normal;'></div>");
		sb.append("    <div id='alertCallBack' style='display:none;'></div>");
		sb.append("  </div>");
		sb.append("  <div id='' >");
		sb.append("    <div id='' style='margin-left:20px;margin-top:15px;height:50px;width:400px;line-height:50px;border-top:solid 1px #595959;'>");
		sb.append("      <div id='alertClose' align='center' class='btn' style='float:left;background:#"+color+";color:#fff;margin-top:8px;margin-right: 18px; ' onselectstart='return false'> 关闭 </div>");
		sb.append("    </div>");
		sb.append("  </div> ");
		sb.append("</div>");
		return sb.toString();
	};
	
	public static String ComfirmScript(){
		StringBuffer sb = new StringBuffer();
		sb.append("flag = false;");
		sb.append("var confirmWin = new Ext.Window({");
		sb.append("    id:'confirmWin',layout: 'fit',width: 450,height: 230,modal:true, plain:true, frame : true,closable:false,");
		sb.append("    closeAction: 'hide',resizable:false,border:false,bodyStyle: \"background:#fff;margin: 0px; padding:0px;font-family:'Microsoft YaHei';\",");
		sb.append("    contentEl: 'confirmwin',");
		sb.append("    listeners:{'show':function(obj){");
		sb.append("        $('#confirmwin').show()");
		sb.append("    }}");
		sb.append("});");
		sb.append("$('#confirmOK').click(function(){ ");
		sb.append("    flag = true; $('#confirmMsg').text(''); confirmWin.hide();  eval($('#confirmCallBack').text());");
		sb.append("});");
		sb.append("$('#confirmCancel').click(function(){ ");
		sb.append("    flag = false; $('#confirmMsg').text(''); $('#confirmCallBack').text();  confirmWin.hide(); ");
		sb.append("});");
		return sb.toString();
	}
	
	public static String ComfirmDom(String color){
		StringBuffer sb = new StringBuffer();
		sb.append("<div id='confirmwin' style='display:none;background:#fff;width:1024px;height:530px;'>");
		sb.append("  <div id='' style='height:140px;'>");
		sb.append("    <div id='confirmTitle' style='margin-left:20px;padding-top:15px;margin-bottom:0px;font-size:26px;font-weight:normal;'>提示信息</div>");
		sb.append("    <div id='confirmMsg' style='margin-left:20px;padding-top:20px;margin-bottom:15px;font-size:16px;font-weight:normal;'></div>");
		sb.append("    <div id='confirmCallBack' style='display:none;'></div>");
		sb.append("  </div>");
		sb.append("  <div id='' >");
		sb.append("    <div id='' style='margin-left:20px;margin-top:15px;height:50px;width:400px;line-height:50px;border-top:solid 1px #595959;'>");
		sb.append("      <div id='confirmOK' align='center' class='btn' style='float:left;background:#"+color+";color:#fff;margin-top:8px;margin-right: 18px; ' onselectstart='return false'> 确定 </div>");
		sb.append("      <div id='confirmCancel' align='center' class='btn' style='float:left;background:#"+color+";color:#fff;margin-top:8px;margin-right: 18px; ' onselectstart='return false'> 取消 </div>");
		sb.append("    </div>");
		sb.append("  </div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	public static String ComfirmDom(){
		return ComfirmDom("094AB2");
	}
	
}
