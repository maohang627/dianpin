package com.thestore.microstore;

import android.content.Context;

import com.thestore.main.core.app.MyApplication;


/**
 * 全局application类
 */
public class VDApplication extends MyApplication {
	
	private static Context context;
	public static boolean SHARE_FLAG;
	public static String SHARE_ID;
	public static String SHARE_URL;
	public static String SHARE_PLATFORM;

	@Override
	public void onCreate() {

		super.onCreate();
		
		VDApplication.context = getApplicationContext();

		// Initialize UncaughtException 
		//针对异常的捕捉要进行全局监控整个项目，所以要将其在Application中注册(也就是初始化)：
		UncaughtException crashHandler = UncaughtException.getInstance();
		crashHandler.init();
	}
	
    public static Context getAppContext() { 
        return VDApplication.context; 
    } 

}
