package com.thestore.microstore.util;

import java.util.UUID;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.thestore.main.core.app.AppContext;
import com.thestore.main.core.app.MyApplication;
import com.thestore.microstore.data.Const;

/**
 * 保存在程序运行过程中很少发生变化的全局变量
 * 
 */
public class Config {

	public static final String TRADER_NAME = "androidSystem";
	public static final String TRADER_CLIENT_NAME = "Android";
	public static final String OS_TYPE = "10";
	
	
	private static MyApplication mApplication = null;
	private static boolean mIsPushing = true;
	private static String appVersion = "";
	public static final String APP_PACKAGE = "com.thestore.microstore";
	public static final String YHD_DOMAIN = "http://.yhd.com";
	public static final String VD_DOMAIN = "http://.vd.yhd.com";
	public static final String INTERFACE_VERSION = "1.3.6";
	public static final String SHA1 = "SHA1";
	public static final String MD5 = "MD5";
	public static final String SALT="7db105d8228804dd0dfb3a4ad563e562b37560df";
	// UT失效返回编码
	public static String RETURN_CODE_UT_FAIL = "055000000003";
	// VUT失效返回编码
	public static String RETURN_CODE_VUT_FAIL = "055000000016";	
	// http接口域名
	public static String DEFAULT_WAPSERVLET_IP = "http://vd.yhd.com";
	// 1号V店时间同步与秘钥获取
	public static final String SERVLET_URL_TIME = DEFAULT_WAPSERVLET_IP + "/service/time";
	// 1号V店APP数据跟踪
	public static final String SERVLET_URL_RECORDTRACK = DEFAULT_WAPSERVLET_IP + "/service/1/vdianAppTrackService/recordTrack";
	// 1号V店APP数据跟踪
	public static final String SERVLET_URL_RECORD_SHAREDATA = DEFAULT_WAPSERVLET_IP + "/service/1/vdianAppShareHedwService/recordShareData";
	// 1号V店APP用户注册
	public static final String SERVLET_URL_ADD_VUSER = DEFAULT_WAPSERVLET_IP + "/service/1/vdianAppPushService/addVUser";
	// 1号V店APP拉取消息
	public static final String SERVLET_URL_GET_PUSHINFORMATION = DEFAULT_WAPSERVLET_IP + "/service/1/vdianAppPushService/getPushInformation";
	// 验证VUT是否有效
	public static final String SERVLET_URL_ISLOGIN = DEFAULT_WAPSERVLET_IP + "/service/1/vdianUserService/isLogin";
	// 获取1号V店配置
	public static final String SERVLET_URL_GET_APPVDIANCONFIG_BYNAME = DEFAULT_WAPSERVLET_IP + "/service/1/vdianConfigService/getAppVdianConfigByName";
	// 检查APP更新
	public static final String SERVLET_URL_CHECK_APPVERSION = DEFAULT_WAPSERVLET_IP + "/service/1/vdianUserService/checkAppVersion";
	// 获取购物车商品
	public static final String SERVLET_URL_GETCART = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/getCart";
	// 编辑购物车商品数量
	public static final String SERVLET_URL_EDIT_NORMALPMINFO_NUM = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/editNormalPmInfoNum";
	// 删除购物车商品
	public static final String SERVLET_URL_DELETE_NORMALPMINFO = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/deleteNormalPmInfo";
	// 购物车切换省份
	public static final String SERVLET_URL_SWITCH_PROVINCE = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/switchProvince";
	// 查询所省市区
	public static final String SERVLET_URL_GET_ALLPROVINCEANDCITYANDCOUNTY = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/getAllProvinceAndCityAndCounty";
	// 查询所省份
	public static final String SERVLET_URL_GET_ALLPROVINCE = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/getAllProvince";
	// 根据省份ID获取对应的城市列表
	public static final String SERVLET_URL_GET_CITY_BYPROVINCEID = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/getCityByProvinceId";
	// 根据城市ID获取对应的区域列表
	public static final String SERVLET_URL_GET_COUNTY_BYCITYID = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/getCountyByCityId";
	// 保存或修改地址
	public static final String SERVLET_URL_SAVE_RECEIVERINFO = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/saveReceiverInfo";
	// 删除收货地址 -- TODO：暂时在AddressActivity中被注释掉了 
	public static final String SERVLET_URL_DEL_RECEIVERINFO = DEFAULT_WAPSERVLET_IP + "/service/1/vdianCartService/delReceiverInfo";
	// 保存配送方式
	public static final String SERVLET_URL_SAVE_DELIVERY = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/saveDelivery";
	// 保存发票
	public static final String SERVLET_URL_SAVE_INVOICEINFO = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/saveInvoiceInfo";
	// 保存支付方式
	public static final String SERVLET_URL_SAVE_PAYMENTINFO = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/savePaymentInfo";
	// 提交订单
	public static final String SERVLET_URL_SUBMIT_ORDERINFO = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/submitOrderInfo";
	// 订单创建完成后，信息查询
	public static final String SERVLET_URL_ORDERDETAIL_BYCODE = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/orderDetailByCode";
	// 初始化订单
	public static final String SERVLET_URL_INIT_CHECKOUT = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/initCheckout";
	// 银行列表
	public static final String SERVLET_URL_GET_BANKLIST = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/getBankList";
	// 买家订单详情页接口
	public static final String SERVLET_URL_FIND_ORDERDETAIL_BYCODE = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/findOrderDetailByCode";
	
	// 是否可以取消订单
	public static final String SERVLET_URL_IS_CAN_CANCEL_ORDER = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/isCanCancelOrder";
	// 取消订单
	public static final String SERVLET_URL_CANCEL_ORDER = DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/cancelOrder";

	//添加抵用券
	public static final String SERVLET_URL_ADD_COUPON= DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/addCoupon";
	//删除抵用券
	public static final String SERVLET_URL_REMOVE_COUPON= DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/removeCoupon";
	//验证抵用券短信
	public static final String SERVLET_URL_VERIFY_COUPON_SMS= DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/verifyCouponSms";
	//发送抵用券短信
	public static final String SERVLET_URL_SEND_COUPON_SMS= DEFAULT_WAPSERVLET_IP + "/service/1/vdianOrderService/sendCouponSms";
	
	private Config() {
		mApplication = AppContext.APP;
	}

	public static void setApplication(MyApplication application) {
		mApplication = application;
	}

	public static MyApplication app() {
		return mApplication;
	}

	public static void setAppVersion(String version) {
		appVersion = version;
	}

	public static String getAppVersion() {
		return appVersion;
	}

	public static void setIsPushing(boolean isPushing) {
		mIsPushing = isPushing;
		// 要和sharedPreference同步
		mApplication
				.getSharedPreferences(Const.STORE_NAME, Context.MODE_PRIVATE)
				.edit().putBoolean(Const.STORE_IS_PUSHING, mIsPushing).commit();
	}

	public static void setPushTime(String start, String end) {
		mApplication
				.getSharedPreferences(Const.STORE_NAME, Context.MODE_PRIVATE)
				.edit().putString(Const.STORE_PUSH_START_TIME, start)
				.putString(Const.STORE_PUSH_END_TIME, end).commit();
	}

	public static void setLongitude(double longitude) {
		mApplication
				.getSharedPreferences(Const.STORE_NAME, Context.MODE_PRIVATE)
				.edit()
				.putString(Const.STORE_LONGTITUDE, String.valueOf(longitude))
				.commit();
	}

	public static String getLongitude() {
		return mApplication.getSharedPreferences(Const.STORE_NAME,
				Context.MODE_PRIVATE).getString(Const.STORE_LONGTITUDE, "");
	}

	public static void setLatitude(double latitude) {
		mApplication
				.getSharedPreferences(Const.STORE_NAME, Context.MODE_PRIVATE)
				.edit()
				.putString(Const.STORE_LATITUDE, String.valueOf(latitude))
				.commit();
	}

	public static String getLatitude() {
		return mApplication.getSharedPreferences(Const.STORE_NAME,
				Context.MODE_PRIVATE).getString(Const.STORE_LATITUDE, "");
	}

	/**
	 * 得到UUid
	 * 
	 * @return
	 */
//	public static String getUUid() {
//		final TelephonyManager tm = (TelephonyManager) mApplication
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		String tmDevice = "" + tm.getDeviceId();
//		String tmSerial = "" + tm.getSimSerialNumber();
//		String androidId = ""
//				+ Secure.getString(mApplication.getContentResolver(),
//						Secure.ANDROID_ID);
//		UUID deviceUuid = new UUID(androidId.hashCode(),
//				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//		String uuid = deviceUuid.toString();
//		return uuid;
//	}
	
    
	
	/**
	 * 获取deviceCode
	 * @return
	 */
    public static String getDeviceCode(Context mContext) {
    	TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    	return tm.getDeviceId();
    }
    
	/**
	 * 得到UUid
	 * 
	 * @return
	 */
	public static String getUUid(Context mContext) {
		final TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String tmDevice = "" + tm.getDeviceId();
		String tmSerial = "" + tm.getSimSerialNumber();
		String androidId = ""
				+ Secure.getString(mContext.getContentResolver(),
						Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uuid = deviceUuid.toString();
		return uuid;
	}

}
