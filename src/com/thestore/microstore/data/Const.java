package com.thestore.microstore.data;

public class Const {

	public static final String STORE_NAME = "com.microstore.version_preferences";// 记录文件名称
	public static final String STORE_IS_PUSHING = "STORE_IS_PUSHING";// 是否推送
	public static final String STORE_PUSH_START_TIME = "STORE_PUSH_START_TIME";// 推送开始时间
	public static final String STORE_PUSH_END_TIME = "STORE_PUSH_END_TIME";// 推送结束时间

	public static final String STORE_LATITUDE = "STORE_LATITUDE";

	public static final String STORE_LONGTITUDE = "STORE_LONGTITUDE";
	
	public static final String CACHE_ALLPROVINCECITYCOUNTY = "CACHE_ALLPROVINCECITYCOUNTY";//所有省市区数据缓存名称
	
	// APP_ID 微信appId
    public static final String APP_ID_WECHAT = "wxe8a29489ac244d29";
    public static class ShowMsgActivity {
		public static final String STitle = "showmsg_title";
		public static final String SMessage = "showmsg_message";
		public static final String BAThumbData = "showmsg_thumb_data";
	}
    // QQ appId
    public static final String APP_ID_QQ = "1103699819";
    // 分享平台
    public static final String SHARE_QQ = "QQ";
    public static final String SHARE_QZONE = "QZone";
    public static final String SHARE_WECHAT = "Wechat";
    public static final String SHARE_WECHATMOMENTS = "WechatMoments";


	public static final int SUCCESS = 0; // 成功
	public static final int FAILED = 1; // 失败
	public static final  int NET_FAILED = 2;
	public static final  int TIME_OUT = 3;
	public static final  int TRUE = 4; // 是
	public static final  int FALSE = 5; // 否
	
	public static final String ERROR_MSG_COMMON = "系统发生错误，请稍后重试。";
	public static final String ERROR_MSG_NOWECHAT = "尚未安装微信";
	
	// 限购商品数量错误
	public static final String ERROR_CODE_SHOPCART_NUM = "200019";

	public static final int ORDER_PRO_DEFAULT_PAGE_SIZE = 2; // 订单商品默认的显示数量

	// mobileSiteType 客户端site区分 1：一号店 2：1mall 3:合规后一号店 所有接口都必须传入
	public static final String MOBILE_SITE_TYPE_1 = "1";
	public static final String MOBILE_SITE_TYPE_2 = "2";
	public static final String MOBILE_SITE_TYPE_3 = "3";

	// 发票类型
	public static final String INVOICE_PERSION = "0";	//个人
	public static final String INVOICE_UNIT = "1";		//公司

	// 快递类型
	public static final int DELIVERY_SERVICE_TYPE_EXPRESS = 10001;// 配送服务为普通快递
	
	// 0：账户支付 1：网上支付 2：货到付款 3：邮局汇款 4：银行转账 5：pos机 6：万里通 7：分期付款 8：合同账期 9：货到转账 10：货到付支票, 目前微店只有1 2 5
	public static final String PAYMENT_TYPE_BY_NET = "1"; // 网上支付
	public static final String PAYMENT_TYPE_BY_CASH = "2"; // 货到付款
	public static final String PAYMENT_TYPE_BY_POS = "5"; // 货到刷卡
	// 待发货
	public static final String ORDER_STATUS_TO_BE_SHIPPED = "待发货";
    // 常用地址
	public static final String DEFAULT_ADDR = "常用地址";
    // 启动
	public static final String TRACK_STATUS_START = "1";
    // 异常
	public static final String TRACK_STATUS_ERROR = "3";
    // 公用提醒标题
	public static final String NOTIFICATION_COMMON_TITLE = "【提醒】";
    // 商品下架提醒标题
	public static final String NOTIFICATION_OFF_TITLE = "【商品下架提醒】";
    // 商品无库存提醒标题
	public static final String NOSTOCK_TITLE = "【商品无库存提醒】";
	
	// BI埋点PAGE_CODE
	public static final String PAGE_CODE_VDIAN_CARTINDEX = "8004"; // 微店购物车页
	public static final String PAGE_CODE_VDIAN_INITORDER = "8005"; // 微店结算页
	public static final String PAGE_CODE_VDIAN_ORDERDOFINISH = "8006"; // 微店订单完成页
	public static final String PAGE_CODE_VDIAN_ORDERDETAIL = "8009"; // 微店订单详情页
	public static final String PAGE_CODE_VDIAN_ORDERADDRESS = "8020"; // 微店购物流程编辑收货地址
		
}
