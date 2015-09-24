package com.thestore.microstore.util;

import java.util.Calendar;

import android.app.AlarmManager;

import com.thestore.main.core.app.AppContext;
import com.thestore.main.core.datastorage.bean.TrackerSession;
import com.thestore.main.core.util.Util;

public class TrackerTool {

	public static String getSessionValue() {

		long time = TrackerSession.getCreatedTime();
		if (time + AlarmManager.INTERVAL_DAY < AppContext.getSystemTime()) {
			// 过期（24小时）,重新生成一个
			TrackerSession.setCreatedTime(AppContext.getSystemTime());
			TrackerSession.setSessionId(Util.generateMixed(32));
		}
		return TrackerSession.getSessionId();
	}

	public static void resetSession() {
		TrackerSession.setCreatedTime(AppContext.getSystemTime());
		TrackerSession.setSessionId(Util.generateMixed(32));
	}

	public static String genUnid() {
		Calendar calendar = Calendar.getInstance();
		long timeInMillis = calendar.getTimeInMillis();
		// 　进制表
		String i64Bit = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ`^abcdefghijklmnopqrstuvwxyz";
		// 当前时间的2进制字符串
		String timeInBinaryString = Long.toBinaryString(timeInMillis);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < timeInBinaryString.length(); i += 6) {
			if (i + 6 < timeInBinaryString.length()) {
				// 2进制转64进制
				sb.append(i64Bit.charAt(Integer.parseInt(timeInBinaryString.substring(i, i + 6), 2)));
			} else {
				sb.append(i64Bit.charAt(Integer.parseInt(timeInBinaryString.substring(i, timeInBinaryString.length()),
						2)));
			}
		}
		return sb.toString();
	}

}
