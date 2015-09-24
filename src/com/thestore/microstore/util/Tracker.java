package com.thestore.microstore.util;

import android.content.Context;
import android.text.TextUtils;

import com.thestore.main.core.app.AppContext;
import com.thestore.main.core.app.ClientInfo;
import com.thestore.main.core.app.DeviceInfo;
import com.thestore.main.core.datastorage.bean.Settings;
import com.thestore.main.core.datastorage.bean.UserInfo;
import com.thestore.main.core.log.Lg;
import com.thestore.main.core.util.Util;
import com.thestore.microstore.VDApplication;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Tracker {

	/**
	 * 测试样子
	 * 
	 * <Pre>
	 * 	http://tracker.yhd.com/tracker/newInfo.do?1=1
	 * &w_url=http://www.yhd.com/?type=2
	 * &s_iev=Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0
	 * &s_plt=Win32
	 * &s_rst=1366*768
	 * &w_rfu=http://www.yhd.com
	 * &bd={b_ai=[{1=[{7642607=1:2377}]}]|w_ck=,uname:jiadong588,unionKey:4734|u_cm=128973751|b_adt=91|b_pmi=8782318|w_pif=0.0.0.0.0.IAr`wC|b_lp=6328_9946460_2|b_pid=7642607|u_pid=1|b_tu=4734}
	 * </Pre>
	 */

	// 请求超时10秒钟
	private static final int REQUEST_TIMEOUT = 10 * 1000;
	// 响应超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000;

	// ThreadPool size 10
	private static ExecutorService exec = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());

	// tracker base URL (make it configurable)
	private static String TRACKERBASEURL = "http://tracker.yhd.com/tracker/newInfo.do?1=1";

	private static final String REFER_PAGE_TYPE = "track.refer_page_type";
	private static final String REFER_PAGE_VALUE = "track.refer_page_value";
	
	private static final String REFER_UN_ID="track.refer_un_id";

	// 这三个是打点的对应缓存 在需要打点的位置将相应的pageId放入 然后每一次统计的时候会从缓存里取数据 取完清空
	private static final String CACHED_PAGE_ID = "track.cached_page_id";
	private static final String CACHED_TPA = "track.cached_tpa";
	private static final String CACHED_TPI = "track.cached_tpi";

	private static Header[] headers = null;

	private static void track(final String url) {
		exec.execute(new Runnable() {
			@Override
			public void run() {
				trackTask(url);
			}
		});

	}

	private static void trackTask(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Lg.e("url===", URLDecoder.decode(url));
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		if (headers != null) {
			addCookie(headers, client);
		} else {
			addCookie(client);

		}
		client.getCookieSpecs().register("easy", new CookieSpecFactory() {
			@Override
			public CookieSpec newInstance(HttpParams arg0) {
				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie arg0, CookieOrigin arg1) throws MalformedCookieException {
						// do nothing
					}
				};
			}
		});
		// customer cookie policy, ignore cookie check
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "easy");
		try {
			HttpGet httpRequest = new HttpGet(url);
			HttpResponse response = client.execute(httpRequest);
			if (response != null) {
				StatusLine statusLine = response.getStatusLine();
				if (statusLine != null && statusLine.getStatusCode() == 200) {
					headers = response.getHeaders("Set-Cookie");
					Lg.d("统计成功", "success");
				} else {
					Lg.d("统计失败", "fail");
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	private static void addCookie(Header[] headers, DefaultHttpClient client) {
		try {
			for (Header header : headers) {
				String headerValue = header.getValue();
				String[] splitValue = headerValue.split("[;=]");
				String name = splitValue[0].trim();
				String value = splitValue[1].trim();
				BasicClientCookie basicClientCookie = new BasicClientCookie(name, value);
				basicClientCookie.setDomain("tracker.yhd.com");
				basicClientCookie.setPath("/");
				client.getCookieStore().addCookie(basicClientCookie);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void addCookie(DefaultHttpClient client) {
		BasicClientCookie basicClientCookie = new BasicClientCookie("tracker_msessionid", TrackerTool.getSessionValue());
		basicClientCookie.setDomain("tracker.yhd.com");
		basicClientCookie.setPath("/");

		BasicClientCookie basicClientCookie2 = new BasicClientCookie("global_user_sign", AppContext.getClientInfo()
				.getDeviceCode());
		basicClientCookie2.setDomain("tracker.yhd.com");
		basicClientCookie2.setPath("/");

		BasicClientCookie basicClientCookie3 = new BasicClientCookie("u_uid", "");
		basicClientCookie3.setDomain("tracker.yhd.com");
		basicClientCookie3.setPath("/");
		client.getCookieStore().addCookie(basicClientCookie);
		client.getCookieStore().addCookie(basicClientCookie2);
		client.getCookieStore().addCookie(basicClientCookie3);
	}

	public static void track(final String w_pt/* pageTypeId 页面id */, final String w_rpt/* 前一页面id */,
			final String w_pv/* pagevalue */, final String b_fi/*
															 * filter_info(上传键值对)
															 * （
															 * brand=海飞丝,price=0
															 * -100,…）
															 */, final String w_rpv/* referPageValue */,
			final String w_run, final String b_scp/* current_page_num */, final String b_pmi/*
																						 * pm_id
																						 * (
																						 * 组合商品传组合商品
																						 * ，
																						 * 系列商品的子商品
																						 * ,
																						 * 放到ext_field7中
																						 * )
																						 */, final String u_cm,
			final String b_rs /* 结果个数resultSum */, final String b_aci/*
																	 * <Pre>
																	 * activy_id
																	 * （
																	 * 促销活动id）(
																	 * 一个商品可能会对应多个活动ID
																	 * ,
																	 * 用‘，’分割)</
																	 * Pre>
																	 */, final String b_oc/*
																						 * order_code
																						 * （
																						 * 不要传成orderid
																						 * ）
																						 */, final String b_lci/*
																												 * list_category_id
																												 * （
																												 * 在搜索列表页选择的类目ID
																												 * ）
																												 */,
			final String b_pms/* pm_status_type_id（当前商品状态） */, final String b_pyi /*
																				 * position_type_id
																				 * (
																				 * 普通判断
																				 * ：
																				 * 1
																				 * 跳转2不跳转
																				 * ；
																				 * 加入购物车特例
																				 * ：
																				 * 3
																				 * 普通商品跳转加入购物车
																				 * ，
																				 * 4
																				 * 普通商品不跳转加入购物车
																				 * ，
																				 * 5
																				 * 一键购跳转加购物车
																				 * ，
																				 * 6
																				 * 一键购不跳转加入购物车
																				 * )
																				 */, final String w_tpa,
			final String w_tpi) {
		// String currentUrl;// 当前url
		// String previousUrl;// previousUrl

		String refer = w_rpt;// 缓存前一个页面的统计
		if (TextUtils.isEmpty(refer)) {// 如果refer为空 说明该refer是可变的
			refer = Storage.getString(REFER_PAGE_TYPE, "");
			Storage.put(REFER_PAGE_TYPE, w_pt);
		}
		// 如果refer不为空 说明是从固定页面过来的

		String referPv = w_rpv;// 缓存前一个页面的pageValue
		if (TextUtils.isEmpty(referPv)) {
			referPv = Storage.getString(REFER_PAGE_VALUE, null);
			Storage.put(REFER_PAGE_VALUE, w_pv);
		}

		String tempTpa = w_tpa;
		String tempTpi = w_tpi;
		if (TextUtils.isEmpty(tempTpa)) {
			tempTpa = Storage.getString(CACHED_TPA, "");
		}
		if (TextUtils.isEmpty(tempTpi)) {
			tempTpi = Storage.getString(CACHED_TPI, "");
		}
		String pageId = Storage.getString(CACHED_PAGE_ID, "");
		if (!TextUtils.isEmpty(pageId) && !TextUtils.isEmpty(refer)) {
			refer = pageId;
		}
		ClientInfo clientInfo = AppContext.getClientInfo();
		String unionKey = clientInfo.getUnionKey();
		// 留空
//		String thirdUnionKey = "";
		String thirdUnionKey = com.thestore.microstore.util.Util.getMetaData(VDApplication.getAppContext(), "Channel_Name");
		String w_un = TrackerTool.genUnid();
		String referUn = w_un;
		if (!TextUtils.isEmpty(referUn)) {
			referUn = Storage.getString(REFER_UN_ID, "");
			Storage.put(REFER_UN_ID, w_un);
		}
		// 留空
		String b_adt = "";
		// String b_adt =
		// String.valueOf(Storage.getLong(Const.HOME_FLOOR_PRECISE_USER_TYPE,
		// 0L));
		String userId = "";
		String guid = Config.getUUid(VDApplication.getAppContext());
		String sessionId = CookiesUtil.getVSessionId();
		String longitude = CookiesUtil.getCookieFromVD("longitude");
		String latitude = CookiesUtil.getCookieFromVD("latitude");
		String b_st = "10001"; // 微店

		final String bd = "{" + "u_pid=" + Settings.getProvinceId() + "|"
				+ (TextUtils.isEmpty(userId) ? "" : "u_uid=" + userId + "|")
				+ (TextUtils.isEmpty(latitude) ? "" : "u_lat=" + latitude + "|")
				+ (TextUtils.isEmpty(longitude) ? "" : "u_lon=" + longitude + "|")
				+ (TextUtils.isEmpty(b_adt) ? "" : "b_adt=" + b_adt + "|")
				+ (TextUtils.isEmpty(w_pt) ? "" : "w_pt=" + w_pt + "|")
				+ (TextUtils.isEmpty(w_un) ? "" : "w_un=" + w_un + "|")
				+ (TextUtils.isEmpty(referUn) ? "" : "w_run=" + referUn + "|")
				+ (TextUtils.isEmpty(refer) ? "" : "w_rpt=" + refer + "|")
				+ (TextUtils.isEmpty(w_pv) ? "" : "w_pv=" + w_pv + "|")
				+ (TextUtils.isEmpty(b_fi) ? "" : "b_fi=" + b_fi + "|")
				+ (TextUtils.isEmpty(referPv) ? "" : "w_rpv=" + referPv + "|")
				+ (TextUtils.isEmpty(b_scp) ? "" : "b_scp=" + b_scp + "|")
				+ (TextUtils.isEmpty(b_pmi) ? "" : "b_pmi=" + b_pmi + "|")
				+ (TextUtils.isEmpty(u_cm) ? "" : "u_cm=" + u_cm + "|")
				+ (TextUtils.isEmpty(b_rs) ? "" : "b_rs=" + b_rs + "|")
				+ (TextUtils.isEmpty(b_aci) ? "" : "b_aci=" + b_aci + "|")
				+ (TextUtils.isEmpty(b_oc) ? "" : "b_oc=" + b_oc + "|")
				+ (TextUtils.isEmpty(b_lci) ? "" : "b_lci=" + b_lci + "|")
				+ (TextUtils.isEmpty(b_pms) ? "" : "b_pms=" + b_pms + "|")
				+ (TextUtils.isEmpty(b_pyi) ? "" : "b_pyi=" + b_pyi + "|")
				+ (TextUtils.isEmpty(tempTpa) ? "" : "w_tpa=" + tempTpa + "|")
				+ (TextUtils.isEmpty(tempTpi) ? "" : "w_tpi=" + tempTpi + "|")
				+ (TextUtils.isEmpty(guid) ? "" : "guid=" + guid + "|")
				+ (TextUtils.isEmpty(sessionId) ? "" : "sessionId=" + sessionId + "|")
				+ (TextUtils.isEmpty(thirdUnionKey) ? "" : "b_tu=" + thirdUnionKey + "|")
				+ (TextUtils.isEmpty(unionKey) ? "" : "b_dtu=" + unionKey.toLowerCase() + "|") + "s_ct=yhdapp|s_ctv="
				+ Util.getClientAppVersion() + "|s_pv=" + android.os.Build.VERSION.RELEASE + "|s_pt=ardphone" + "|b_st=" + b_st + "}";

		try {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// 请求的值都是小写
					String appVersion = Util.getClientAppVersion();
					// String clientSystem = trader.getClientSystem();
					/**
					 * 测试样子
					 * 
					 * <Pre>
					 *  http://tracker.yhd.com/tracker/info.do?1=1
					 * &w_url=http://www.yhd.com/?type=2
					 * &s_iev=Mozilla/5.0 (Windows NT 6.1; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0
					 * &s_plt=Win32
					 * &s_rst=1366*768
					 * &w_rfu=http://www.yhd.com
					 * &bd={b_ai=[{1=[{7642607=1:2377}]}]|w_ck=,uname:jiadong588,unionKey:4734|u_cm=128973751|b_adt=91|b_pmi=8782318|w_pif=0.0.0.0.0.IAr`wC|b_lp=6328_9946460_2|b_pid=7642607|u_pid=1|b_tu=4734}
					 * </Pre>
					 */

					DeviceInfo deviceInfo = AppContext.getDeviceInfo();
					String uriAPI = TRACKERBASEURL
					/***
					 * 先加通用的参数
					 */
					+ (TextUtils.isEmpty(appVersion) ? "" : "&s_iev=" + appVersion.toLowerCase())
							+ ("&s_plt=androidSystem" + URLEncoder.encode(android.os.Build.VERSION.RELEASE))
							+ ("&s_rst=" + deviceInfo.widthPixels + "*" + deviceInfo.heightPixels) + "&bd="
							+ URLEncoder.encode(bd);
					track(uriAPI);
//					if (!TextUtils.isEmpty(UserInfo.getPmId())) {
//						UserInfo.setPmId("");
//					}
					clearCache();
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * 统计页面 仅有pageTypeId是参数
	 * 
	 * @param pageTypeId
	 */
	public static void trackPage(String pageTypeId) {
		track(pageTypeId/* w_pt */, null/* w_rpt */, null/* w_pv */, null/* b_fi */, null/* w_rpv */, null/* w_run */,
				null/* b_scp */, null /* b_pmi */, null/* u_cm */, null/* b_rs */, null /* b_aci */, null/* b_oc */,
				null/* b_lci */, null/* b_pms */, "1"/* b_pyi */, null/* w_tpa */, null/* w_tpi */);
	}

	protected static void putCachedValue(String pageId, String tpa, String tpi) {
		Storage.put(CACHED_PAGE_ID, pageId);
		Storage.put(CACHED_TPA, tpa);
		Storage.put(CACHED_TPI, tpi);
		Lg.d("newInfo", "putCachedValue", pageId, tpa, tpi);
	}

	protected static void clearCache() {
		Storage.remove(CACHED_PAGE_ID);
		Storage.remove(CACHED_TPA);
		Storage.remove(CACHED_TPI);
	}
}
