package com.thestore.microstore.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkUtil {
	
	private static final String TAG = "NetWorkUtil";

	public static boolean isConnectFast() {
		String connectionType = NetWorkUtil.getConnectionTypeName();
		if ("TYPE_1xRTT".equals(connectionType)
				|| "NETWORK_TYPE_CDMA".equals(connectionType)
				|| "NETWORK_TYPE_EDGE".equals(connectionType)
				|| "TYPE_GPRS".equals(connectionType)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断网络类型
	 * 
	 * @return 网络类型
	 */
	public static String getConnectionTypeName() {
		String netType = "TYPE_UNKNOWN";

		ConnectivityManager connectivityManager = (ConnectivityManager) Config
				.app().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// networkInfo.getTypeName();
				netType = "TYPE_WIFI";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (networkInfo.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					netType = "TYPE_1xRTT";
					break; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					netType = "TYPE_CDMA";
					break; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					netType = "TYPE_EDGE";
					break; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					netType = "TYPE_EVDO_0";
					break; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					netType = "TYPE_EVDO_A";
					break; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					netType = "TYPE_GPRS";
					break; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					netType = "TYPE_HSDPA";
					break; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					netType = "TYPE_HSPA";
					break; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					netType = "TYPE_HSUPA";
					break; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					netType = "TYPE_UMTS";
					break; // ~ 400-7000 kbps
				// NOT AVAILABLE YET IN API LEVEL 7
				// case TelephonyManager.NETWORK_TYPE_EHRPD:
				// return true; // ~ 1-2 Mbps
				// case Connectivity.NETWORK_TYPE_EVDO_B:
				// return true; // ~ 5 Mbps
				// case Connectivity.NETWORK_TYPE_HSPAP:
				// return true; // ~ 10-20 Mbps
				// case Connectivity.NETWORK_TYPE_IDEN:
				// return false; // ~25 kbps
				// case Connectivity.NETWORK_TYPE_LTE:
				// return true; // ~ 10+ Mbps
				// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					netType = "TYPE_MOBILE";
					break;
				}
			}
		}
		return netType;
	}

	/**
	 * 判断网络类型
	 * 
	 * @return 网络类型
	 */
	public static String getNetTypeName() {
		String netType = "";

		ConnectivityManager connectivityManager = (ConnectivityManager) Config
				.app().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// networkInfo.getTypeName();
				netType = "wifi";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (networkInfo.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					netType = "2g";
					break; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					netType = "2g";
					break; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					netType = "2g";
					break; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					netType = "3g";
					break; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					netType = "3g";
					break; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					netType = "2g";
					break; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					netType = "3g";
					break; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					netType = "3g";
					break; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					netType = "3g";
					break; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					netType = "3g";
					break; // ~ 400-7000 kbps
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					netType = "";
					break;
				}
			}
		}
		return netType;
	}
	
	/**
	 * 判断网络类型
	 * 
	 * @return 网络类型
	 */
	public static String getNetTypeName(Context mContext) {
		String netType = "";

		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// networkInfo.getTypeName();
				netType = "wifi";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (networkInfo.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					netType = "2g";
					break; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					netType = "2g";
					break; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					netType = "2g";
					break; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					netType = "3g";
					break; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					netType = "3g";
					break; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					netType = "2g";
					break; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					netType = "3g";
					break; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					netType = "3g";
					break; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					netType = "3g";
					break; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					netType = "3g";
					break; // ~ 400-7000 kbps
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					netType = "";
					break;
				}
			}
		}
		return netType;
	}

	public static boolean is2G3G() {
		String connectName = getConnectionTypeName();
		return !("TYPE_WIFI".equals(connectName));
	}

    public static boolean isMobileNet(){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) Config
                    .app().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null&&networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                   return true;
            }
        }catch (Exception e){

        }
        return false;
    }
    
	public static boolean checkURL(String url) {
		boolean value = false;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url)
					.openConnection();
			int code = conn.getResponseCode();
			Logger.d(TAG, "checkURL getResponseCode:"+String.valueOf(code));
			if (code != 200) {
				value = false;
			} else {
				value = true;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
    
	public static String getLocalIpAddress() {  
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface  
                    .getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf  
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                    InetAddress inetAddress = enumIpAddr.nextElement();  
                    if (!inetAddress.isLoopbackAddress()) {  
                        return inetAddress.getHostAddress().toString();  
                    }  
                }  
            }  
        } catch (SocketException ex) {  

        }  
        return null;  
    } 
	
}
