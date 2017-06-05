package com.huai.common.util;

import java.util.HashMap;
import java.util.Map;

public class CC
{

	public static final String ROLE_ADMIN = "1";
	public static final String ROLE_CASHIER = "2";
	public static final String ROLE_MONITOR = "3";
	public static final String ROLE_QUERY = "4";


    public static final String USER_CONTEXT = "user";
    public static final int PAGE_SIZE = 3;
    public static final Map PAY_MODE = new HashMap();
    
    static{
    	PAY_MODE.put("xj", "现金");
    	PAY_MODE.put("sk", "刷卡");
    	PAY_MODE.put("gz", "挂账");
    	PAY_MODE.put("zp", "支票");
    	PAY_MODE.put("vip", "会员");
    	PAY_MODE.put("wx", "微信");
    	PAY_MODE.put("zfb", "支付宝");
    }
	
}
