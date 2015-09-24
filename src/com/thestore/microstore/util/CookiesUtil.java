package com.thestore.microstore.util;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class CookiesUtil {

	/**
	 * 从http://.yhd.com的cookie中读取ut
	 * 
	 * @return ut
	 */
	public static String getUT() {
		String ut = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.YHD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("ut=")) {
					ut = cookieVal.replace("ut=", "").trim();
					break;
				}
			}
		}
		Logger.d("ut=", ut);

		return ut;
	}
	
	/**
	 * 向http://.yhd.com的cookie中写入ut
	 * 
	 * @param ut
	 */
	public static void setUT(Context context, String ut) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.YHD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			cookieVD.setAcceptCookie(true);
			cookieVD.setCookie(Config.YHD_DOMAIN, "ut=" + ut);
			CookieSyncManager.getInstance().sync();// 同步
		}

		cookiesVD = cookieVD.getCookie(Config.YHD_DOMAIN);
		Logger.d("cookiesVD=", cookiesVD);
	}

	/**
	 * 从http://.vd.yhd.com的cookie中读取vut
	 * 
	 * @return vut
	 */
	public static String getVUT() {
		String vut = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("vut=")) {
					vut = cookieVal.replace("vut=", "").trim();
					break;
				}
			}
		}
		Logger.d("vut=", vut);

		return vut;
	}

	/**
	 * 从http://.vd.yhd.com的cookie中读取m_provinceId
	 * 
	 * @return m_provinceId
	 */
	public static String getProvinceId() {
		String provinceId = "1";// 默认上海
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("m_provinceId=")) {
					provinceId = cookieVal.replace("m_provinceId=", "").trim();
					break;
				}
			}
		}
		Logger.d("m_provinceId=", provinceId);

		return provinceId;
	}

	/**
	 * 向http://.vd.yhd.com的cookie中写入m_provinceId
	 * 
	 * @param provinceId
	 */
	public static void setProvinceId(Context context, String provinceId) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			cookieVD.setAcceptCookie(true);
			cookieVD.setCookie(Config.VD_DOMAIN, "m_provinceId=" + provinceId);
			CookieSyncManager.getInstance().sync();// 同步
		}

		cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		Logger.d("cookiesVD=", cookiesVD);
	}

	/**
	 * 从http://.vd.yhd.com的cookie中读取vsessionid
	 * 
	 * @return vSessionId
	 */
	public static String getVSessionId() {
		String vSessionId = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("vsessionid=")) {
					vSessionId = cookieVal.replace("vsessionid=", "").trim();
					break;
				}
			}
		}
		Logger.d("vsessionid=", vSessionId);

		return vSessionId;
	}
	
	/**
	 * 从cookie中获取登录手机号
	 * 
	 * @return
	 */
	public static String getLoginMobileNum() {
		String loginMobileNum = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("loginMobileNum=")) {
					loginMobileNum = cookieVal.replace("loginMobileNum=", "").trim();
					break;
				}
			}
		}
		Logger.d("loginMobileNum=", loginMobileNum);

		return loginMobileNum;
	}

	/**
	 * 从cookie中获取手机号
	 * 
	 * @return
	 */
	public static String getUserMobileNumFrom() {
		String userMobileNum = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("userMobileNum=")) {
					userMobileNum = cookieVal.replace("userMobileNum=", "").trim();
					break;
				}
			}
		}
		Logger.d("userMobileNum=", userMobileNum);

		return userMobileNum;
	}

	/**
	 * 向http://.vd.yhd.com的cookie中写入userMobileNum
	 * 
	 * @param userMobileNum
	 */
	public static void setUserMobileNum(Context context, String prevUserMobileNum) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			cookieVD.setAcceptCookie(true);
			cookieVD.setCookie(Config.VD_DOMAIN, "userMobileNum=" + prevUserMobileNum);
			CookieSyncManager.getInstance().sync();// 同步
		}

		cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		Logger.d("cookiesVD=", cookiesVD);

	}

	/**
	 * 
	 * 获取 当前省份上一个电话号码
	 * 
	 */
	public static String getPrevUserMobileNum() {
		String prevUserMobileNum = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.YHD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("prevUserMobileNum=")) {
					prevUserMobileNum = cookieVal.replace("prevUserMobileNum=", "").trim();
					break;
				}
			}
		}
		Logger.d("prevUserMobileNum=", prevUserMobileNum);

		return prevUserMobileNum;
	}

	/**
	 * 从http://.vd.yhd.com的cookie中读取paymethod
	 * 
	 * @return paymethod
	 */
	public static String getPaymethod() {
		String paymethod = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("paymethod=")) {
					paymethod = cookieVal.replace("paymethod=", "").trim();
					break;
				}
			}
		}
		Logger.d("paymethod=", paymethod);

		return paymethod;
	}

	/**
	 * 向http://.vd.yhd.com的cookie中写入paymethod
	 * 
	 * @param paymethod
	 */
	public static void setPayment(Context context, String paymethod) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			cookieVD.setAcceptCookie(true);
			cookieVD.setCookie(Config.VD_DOMAIN, "paymethod=" + paymethod);
			CookieSyncManager.getInstance().sync();// 同步
		}

		cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		Logger.d("cookiesVD=", cookiesVD);
	}
	
	/**
	 * 从http://.vd.yhd.com的cookie中读取nativeURI
	 * 
	 * @return nativeURI
	 */
	public static String getNativeURI() {
		String nativeURI = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			String[] cookiesVDArr = cookiesVD.split(";");
			for (String cookieVal : cookiesVDArr) {
				if (cookieVal.trim().startsWith("nativeURI=")) {
					nativeURI = cookieVal.replace("nativeURI=", "").trim();
					break;
				}
			}
		}
		Logger.d("nativeURI=", nativeURI);

		return nativeURI;
	}
	
	/**
	 * 向http://.vd.yhd.com的cookie中写入nativeURI
	 * 
	 * @param nativeURI
	 */
	public static void setNativeURI(Context context, String nativeURI) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		if (cookiesVD != null) {
			Logger.d("vdian-getCookie", cookiesVD);
			cookieVD.setAcceptCookie(true);
			cookieVD.setCookie(Config.VD_DOMAIN, "nativeURI=" + nativeURI);
			CookieSyncManager.getInstance().sync();// 同步
		}

		cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		Logger.d("cookiesVD=", cookiesVD);
	}
	
	/**
	 * 从http://.vd.yhd.com的cookie中读取字段
	 * @param cookieName
	 * @return cookieValue
	 */
	public static String getCookieFromVD(String cookieName) {
		String cookieValue = "";
		if(!TextUtils.isEmpty(cookieName)){
			CookieManager cookieVD = CookieManager.getInstance();
			String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
			if (cookiesVD != null) {
				Logger.d("vdian-getCookie", cookiesVD);
				String[] cookiesVDArr = cookiesVD.split(";");
				for (String cookieVal : cookiesVDArr) {
					if (cookieVal.trim().startsWith(cookieName+"=")) {
						cookieValue = cookieVal.replace(cookieName+"=", "").trim();
						break;
					}
				}
			}
			Logger.d(cookieName+"=", cookieValue);
		}

		return cookieValue;
	}

}
