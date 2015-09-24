package com.thestore.microstore;

import java.util.HashMap;

import org.afinal.simplecache.ACache;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.AllProvinceCityCountyParser;
import com.thestore.microstore.parser.VdianConfigParser;
import com.thestore.microstore.parser.VdianTimeParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.VdianConfigVO;
import com.thestore.microstore.vo.VdianTimeVO;
import com.thestore.microstore.vo.proxy.ProvinceProxy;


/**
 * 启动界面
 * 
 * @author zhaojianjian
 * 
 */
public class LoadingActivity extends Activity{
	private static final String TAG = "LoadingActivity";
	// 版本，测试版：
	private String clientVersion="测试版：";
	private String mUrl = "http://vd.yhd.com/myvdian/appindex.do";
    private String mYHDDomain = "http://.yhd.com";
    private String mVDDomain = "http://.vd.yhd.com";
    // 卖家订单详情H5页面
    private String mVDianOrderDetailUrl = "http://vd.yhd.com/order/vdianOrderDetail.do?orderCode=";
    // 我的收入H5页面
    private String mIncomeListUrl = "http://vd.yhd.com/income/incomeList.do";
    // 1号微店apk下载地址
    private String mAPKDownLoadUrl = "http://vd.yhd.com/downloads/yihaovdian.apk";
    // 我也要开店H5页面
    private String mCreateStartUrl = "http://vd.yhd.com/myvdian/createstart.do";
    // 我的店铺H5页面
    private String mSellerIndexUrl = "http://vd.yhd.com/myvdian/sellerindex.do";
    // 收货地址个数上限
    private int mGoodReceiverMaxNum = 10;
    // 我的微店已失效商品tab页面
    private String mSoldOutUrl = "http://vd.yhd.com/myvdian/myvdian.do?tabType=soldOut";
    // 我的微店页面（商品无库存）
    private String mNoStockUrl = "http://vd.yhd.com/myvdian/myvdian.do";
    // 秘钥
    private String mKeyword = "";
    // 时间差
    private Long mT = 0L;    
	private LocationClient mLocationClient = null;
	private long mExitTime = 0;
	private String mClientAppVersion = "";
	private String mDeviceId = "";
	private ACache mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_activity);
		try {
			clientVersion += getClientVersion();
		} catch (NameNotFoundException e) {
		}
		
		
    	shortcut();
    	
    	mCache = ACache.get(this);
    	
		final IWXAPI api = WXAPIFactory.createWXAPI(this, null);
		// 将该app注册到微信
		api.registerApp(Const.APP_ID_WECHAT);
				
		loading();
	}	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 加载界面
	 * @param 
	 * @return
	 */
	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				try {
					
					// 微店服务端时间同步与秘钥获取
			    	getVdianTimeFromServer();
			    	// 获取微店服务端配置
			    	getVdianConfigFromServer();
			    	// 获取全部省市区
			    	getAllProvinceCityCounty();
			    	// 进入主页
			    	gotoHome();

				} catch (Exception e) {
					Logger.e(TAG, e.getMessage());
				}
			}
		}).start();
	}
	
    /**
     * 首次安装添加桌面快捷方式
     */
    private void shortcut() {
		String isFirst = "1";
        SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        if(sp.getString("isFirst", "") != null && !"".equals(sp.getString("isFirst", ""))){
        	isFirst = sp.getString("isFirst", "");
        }
    	if(!"0".equals(isFirst)){
            Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
            shortcutIntent.putExtra("duplicate", false);
            // Begin: fixbug 0103962
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            // End: fixbug 0103962
            intent
                .setComponent(new ComponentName(getPackageName(), LoadingActivity.class.getName()));
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
            shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));

            sendBroadcast(shortcutIntent);
            
            //保存第一次运行
            SharedPreferences spVD = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
            Editor editVD = spVD.edit();
            editVD.putString("isFirst", "0");
            editVD.commit();
    	}
    }

	/**
	 * 进入主页
	 */
	private void gotoHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 获取当前应用的版本号
	 * 
	 * @return
	 * @throws NameNotFoundException
	 */
	private String getClientVersion() throws NameNotFoundException {
		PackageManager packageManager = getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
		return packageInfo.versionName;
	}
	
    /**
     * 微店服务端时间同步与秘钥获取
     */
    private void getVdianTimeFromServer(){
    	RequestVo requestVo = new RequestVo();
		requestVo.requestUrl = Config.SERVLET_URL_TIME;
		requestVo.context = this;
		requestVo.jsonParser = new VdianTimeParser();
		VdianTimeVO result = (VdianTimeVO) HttpUtil.post(requestVo);
		if(result != null){
	        SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
	        Editor edit = sp.edit();
	        
	        mKeyword = result.getKeyword();
	        edit.putString("mKeyword", mKeyword);
	        Long localTime = System.currentTimeMillis();
	        if(result.getTimestamp() != null){
	        	mT = result.getTimestamp() - localTime;
	        }	        
	        edit.putLong("mT", mT);
	        
	        edit.commit();
		}		
    }
    
    /**
     * 获取微店服务端配置
     */
    private void getVdianConfigFromServer(){
    	RequestVo requestVo = new RequestVo();
		requestVo.requestUrl = Config.SERVLET_URL_GET_APPVDIANCONFIG_BYNAME;
		requestVo.context = this;
		HashMap<String, String> postParamMap = new HashMap<String, String>();
		postParamMap.put("configName", "vdian_app_android");
		requestVo.requestDataMap = postParamMap;
		requestVo.jsonParser = new VdianConfigParser();
		VdianConfigVO result = (VdianConfigVO) HttpUtil.post(requestVo);
		if(result != null){
	        SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
	        Editor edit = sp.edit();
	        
	        mUrl = result.getAppIndexUrl();
	        edit.putString("mUrl", mUrl);
	        mYHDDomain = result.getYhdDomain();
			edit.putString("mYHDDomain", mYHDDomain);
			mVDDomain = result.getVdDomain();
			edit.putString("mVDDomain", mVDDomain);
			mVDianOrderDetailUrl = result.getVdianOrderDetailUrl();
			edit.putString("mVDianOrderDetailUrl", mVDianOrderDetailUrl);
			mIncomeListUrl = result.getIncomeListUrl();
			edit.putString("mIncomeListUrl", mIncomeListUrl);
			mAPKDownLoadUrl = result.getApkDownLoadUrl();
			edit.putString("mAPKDownLoadUrl", mAPKDownLoadUrl);
			mCreateStartUrl = result.getCreateStartUrl();
			edit.putString("mCreateStartUrl", mCreateStartUrl);
			mSellerIndexUrl = result.getSellerIndexUrl();
			edit.putString("mSellerIndexUrl", mSellerIndexUrl);
			mSellerIndexUrl = result.getSellerIndexUrl();
			edit.putString("mSellerIndexUrl", mSellerIndexUrl);
			mGoodReceiverMaxNum = result.getGoodReceiverMaxNum();
			edit.putInt("mGoodReceiverMaxNum", mGoodReceiverMaxNum);
			mSoldOutUrl = result.getSoldOutUrl();
			edit.putString("mSoldOutUrl", mSoldOutUrl);
			mNoStockUrl = result.getNoStockUrl();
			edit.putString("mNoStockUrl", mNoStockUrl);
			
	        edit.commit();
		}

    }
    
    /**
     * 获取全部省市区
     */
    private void getAllProvinceCityCounty(){
    	RequestVo requestVo = new RequestVo();
		requestVo.requestUrl = Config.SERVLET_URL_GET_ALLPROVINCEANDCITYANDCOUNTY;
		requestVo.context = this;
		requestVo.jsonParser = new AllProvinceCityCountyParser();
		ProvinceProxy result = (ProvinceProxy) HttpUtil.post(requestVo);
		if(result != null && "0".equals(result.getRtnCode()) && result.getProvinceList().size() > 0){
			mCache.put(Const.CACHE_ALLPROVINCECITYCOUNTY, result);
		}
    }

}
