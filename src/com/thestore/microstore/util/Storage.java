package com.thestore.microstore.util;

import com.thestore.microstore.VDApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Storage {

	/**
	 * 持久化键值对
	 * 
	 * @param key
	 *            key值必须使用 模块名.字段名 的格式 例如home.picture;
	 * @param value
	 *            要存贮的值 String
	 * @return
	 */
	public static void put(String key, String value) {
        SharedPreferences spVD = VDApplication.getAppContext().getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        Editor editVD = spVD.edit();
        editVD.putString(key, value);
        editVD.commit();
	}

	public static String getString(String key, String defValue) {
		SharedPreferences sp = VDApplication.getAppContext().getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	public static void remove(String key) {
        SharedPreferences spVD = VDApplication.getAppContext().getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        Editor editVD = spVD.edit();
        editVD.remove(key);
        editVD.commit();
	}

}

