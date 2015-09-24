package com.thestore.microstore;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
//import cn.sharesdk.framework.ShareSDK;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thestore.main.core.app.AppContext;
import com.thestore.main.core.datastorage.bean.UserInfo;
import com.thestore.main.core.net.bean.ResultVO;
import com.thestore.main.core.net.request.ParamHelper;
import com.thestore.main.core.net.request.Request;
import com.thestore.main.core.net.request.UrlHelper;
import com.thestore.microstore.data.ApiConst;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.data.SharePlatform;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.service.MicroStoreService;
import com.thestore.microstore.util.CheckUpdateAsyncTask;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.ImageUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.UpdateAsyncTask;
import com.thestore.microstore.util.Util;
import com.thestore.microstore.vo.AppVersionVO;
import com.thestore.microstore.vo.MsgContent;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;
import com.thestore.microstore.vo.proxy.MsgContentProxy;
import com.thestore.microstore.webkit.BitmapUtil;
import com.thestore.microstore.webkit.IdGenerator;
import com.thestore.microstore.webkit.YHDJavaScriptInterface;
import com.yihaodian.mobile.vo.push.PushInformationVO;

public class MainActivity extends Activity implements OnClickListener {

	private static String TAG = "MainActivity";
	private static final int ADDVUSER = 1001;
	private static final int SETOPEN = 1002;

	private WebView mWebView;
	private ProgressBar mProgressBar;
	private String mUrl = "http://vd.yhd.com/myvdian/appindex.do";
    private String mYHDDomain = "http://.yhd.com";
    private String mVDDomain = "http://.vd.yhd.com";
    // 卖家订单详情H5页面
    private String mVDianOrderDetailUrl = "http://vd.yhd.com/order/vdianOrderDetail.do?orderCode=";
    // 我的收入H5页面
    private String mIncomeListUrl = "http://vd.yhd.com/income/incomeList.do";
    // 1号微店apk下载地址
    private AppVersionVO mAppVersion;
    private String mAPKDownLoadUrl = "http://vd.yhd.com/downloads/yihaovdian.apk";
    // 我也要开店H5页面
    private String mCreateStartUrl = "http://vd.yhd.com/myvdian/createstart.do";
    // 我的店铺H5页面
    private String mSellerIndexUrl = "http://vd.yhd.com/myvdian/sellerindex.do";
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
	private String mUpdateTitle = "更新提示";
	private Date mLastUpdateDate;
	private boolean isShowUpdateDialog = true;

	// 导航栏
	private RelativeLayout mWebviewController;
	private RelativeLayout mWebError;
	private YHDJavaScriptInterface jsInterface;

	private ImageButton mPreviousBtn;
	private ImageButton mNextBtn;
	private ImageButton mRefreshBtn;

	private Button mBackToHomeBtn;
	
	private String mLastUrl = mUrl;

	public static final int ERROR_PAGE = 0;
	public static final int TAKE_PHOTO_SQUARE = 1;
	public static final int TAKE_PHOTO_RECTANGLE = 2;
	public static final int GET_ALBUM_SQUARE = 3;
	public static final int GET_ALBUM_RECTANGLE = 4;
	public static final int PHOTO_REQUEST_CUT = 5;
	public static final int FILECHOOSER_RESULTCODE = 6;	
	
	private ValueCallback<Uri> mUploadMessage;
	
	Handler appVersionHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what == Const.TRUE){
				mAppVersion = (AppVersionVO)msg.obj;
				if(mAppVersion != null){
					// TODO:后续直接从检查更新接口中获取apk下载地址
					mAppVersion.setApkDownLoadUrl(mAPKDownLoadUrl);
					if(mAppVersion.isForceUpgrade()){
						// 强制更新，显示软件更新对话框
						if(isShowUpdateDialog){
							showUpdateDialog(mAppVersion);
						}						
					}else{
						// 非强制更新，取消更新之后当天不再重复提醒
				    	String cancelUpdate = "";
				        SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
				        if(!TextUtils.isEmpty(sp.getString("cancelUpdate", ""))){
				        	cancelUpdate = sp.getString("cancelUpdate", "");
				        }
				        if(!cancelUpdate.equals(Util.getDateString(new Date(),"yyyyMMdd"))){
							// 显示软件更新对话框
				        	if(isShowUpdateDialog){
				        		showUpdateDialog(mAppVersion);
				        	}
				        }
					}
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_main);
		// 添加当前的Activity对象
		AppManager.getAppManager().addActivity(this);
		
		getVdianConfigFromStorage();
		// H5页面通过URL打开Activity
		Intent i_getvalue = getIntent();  
		String action = i_getvalue.getAction(); 		
		if(Intent.ACTION_VIEW.equals(action)){  
		    Uri uri = i_getvalue.getData();  
		    if(uri != null){
		    	if(uri.toString().contains("myvdian_createstart")){
		    		mUrl = mCreateStartUrl;
		    	}else if(uri.toString().contains("myvdian_sellerindex")){
		    		mUrl = mSellerIndexUrl;
		    	}
		    }
		}		
		initializeView();
		initLocation();
        //初始化ShareSDK
//        ShareSDK.initSDK(this);

        String token = CookiesUtil.getVUT();
//        Toast.makeText(this, token, Toast.LENGTH_LONG).show();
        if(token != null && !"".equals(token)){
        	UserInfo.setToken(token);
    		// setOpen();

//    		Request reqTask = AppContext.newRequest();
//    		HashMap<String, Object> param = ParamHelper.jsonParam(
//    				ApiConst.ADDVUSER, null);
//    		// 10.161.144.22 interface.m.yihaodian.com
//    		// 端口8080
//    		// http://ip:port/centralmobile/mobileservice/addVUser.do
//    		reqTask.applyParam(Request.POST, UrlHelper.getUrl(ApiConst.path), null,
//    				new TypeToken<ResultVO<String>>() {
//    				}.getType());
//    		Message callback = handler.obtainMessage(ADDVUSER);
//    		reqTask.setCallBack(callback);
//    		reqTask.execute();
        	
			// 1号V店APP用户注册
			new Thread(new Runnable() {
				public void run() {
					try {
						addVUser();
					} catch (Exception e) {
						Logger.d(TAG, e.getMessage());
					}
				}
			}).start();
			
        }

		Intent intent = new Intent();
		intent.setClass(this, MicroStoreService.class);
		intent.putExtra("mode", MicroStoreService.MODE_PUSH);
		startService(intent);

	}

    //Activity创建或者从后台重新回到前台时被调用  
    @Override  
    protected void onStart() {  
        super.onStart();
        
        // APP分享回调统计
        if(VDApplication.SHARE_FLAG && SharePlatform.WECHAT.id.equals(VDApplication.SHARE_PLATFORM)){
			new Thread(new Runnable() {
				public void run() {
					try {
				        Util util = new Util(VDApplication.getAppContext());
				        util.recordShareData(VDApplication.SHARE_ID, VDApplication.SHARE_URL, VDApplication.SHARE_PLATFORM);
					} catch (Exception e) {
						Logger.d(TAG, e.getMessage());
					}
				}
			}).start();
        }
        
		// 获取参数
		Intent intent = getIntent();
		String toH5Url = intent.getStringExtra("toH5Url");
		if (!TextUtils.isEmpty(toH5Url)) {
			Logger.d(TAG, "onStart loadUrl:"+toH5Url);
			mWebView.loadUrl(toH5Url);
		}
		mWebView.loadUrl("javascript:updateCartCountFromNative()");
		
		//启动异步任务检查更新，1秒内不重复检查更新
		if(mLastUpdateDate == null || (new Date().getTime() - mLastUpdateDate.getTime()) >= 1000L ){
	        CheckUpdateAsyncTask checkAsyncTask = new CheckUpdateAsyncTask(this, appVersionHandler, mVDDomain);  
	        checkAsyncTask.execute();
	        mLastUpdateDate = new Date();
		}

    } 
        
	 @Override  
	 protected void onNewIntent(Intent intent) {  
		super.onNewIntent(intent);
		if (intent != null) {
			PushInformationVO pushOV = (PushInformationVO) intent.getSerializableExtra("push_info_vo");
			if (pushOV != null) {
				String msgContentJson = pushOV.getMsgContent();				
				MsgContentProxy msgContentProxy = new Gson().fromJson(msgContentJson,
						new TypeToken<MsgContentProxy>() {
						}.getType());
				
				MsgContent msgContent = null;
				String pId = "";
				int pType = 0;				
				if(msgContentProxy != null){
					msgContent = msgContentProxy.getMsgContent();
					if(msgContent != null){
						pId = msgContent.getpId();
						pType = msgContent.getpType();
					}
				}
				// 注：微店消息需要跳转的H5页面有2个：
				// 1) pType：类型 0：不需要跳转，1：订单详情， 2：我的收入
				// 2) pId：参数（ptype = 1时，pId传入订单号）
				// 3) 根据ptype，App端拼接H5的URL：
				//  订单详情URL:
				// http://vd.yhd.com/order/vdianOrderDetail.do?orderCode=xxx
				//  我的收入URL: http://vd.yhd.com/income/incomeList.do
				switch (pType) {
				case 0:
					break;
				case 1:
					mWebView.loadUrl(mVDianOrderDetailUrl + pId);
					break;
				case 2:
					mWebView.loadUrl(mIncomeListUrl);
					break;
				case 3:
					mWebView.loadUrl(mSoldOutUrl);
					break;
				case 4:
					mWebView.loadUrl(mNoStockUrl);
					break;
				default:
					break;
				}
			}
			
			// 获取参数
			String toH5Url = intent.getStringExtra("toH5Url");
			if (!TextUtils.isEmpty(toH5Url)) {
				Logger.d(TAG, "onNewIntent loadUrl:"+toH5Url);
				mWebView.loadUrl(toH5Url);
			}
			mWebView.loadUrl("javascript:updateCartCountFromNative()");
		}
		
	 } 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
			if (mWebView.canGoBack()){
				mWebView.goBack();
			}else{
		        if((System.currentTimeMillis() - mExitTime) > 2000){  
		            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
		            mExitTime = System.currentTimeMillis();   
		        } else {
		        	this.onDestroy();
		            System.exit(0);
		        }
			}

	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}

	private void setOpen() {
		// TODO Auto-generated method stub
		Request reqTask = AppContext.newRequest();
		HashMap<String, Object> param = ParamHelper.jsonParam(ApiConst.SETOPEN,
				null);
		param.put("open", 1);
		// 10.161.144.22 interface.m.yihaodian.com
		// 端口8080
		// http://ip:port/centralmobile/mobileservice/addVUser.do
		reqTask.applyParam(Request.POST,
				UrlHelper.getUrl(ApiConst.setOpenPath), null,
				new TypeToken<ResultVO<String>>() {
				}.getType());
		Message callback = handler.obtainMessage(SETOPEN);
		reqTask.setCallBack(callback);
		reqTask.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ADDVUSER:

				break;
			case SETOPEN:
				break;
			default:
				break;
			}

		};
	};

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.web_pre_btn:
			if (mWebView.canGoBack()) {
				
				try {
					String nativeURI = CookiesUtil.getNativeURI();
					Logger.d(TAG, "nativeURI=" + nativeURI);
					if (nativeURI.startsWith("toNative-")) {
						String nativeURIs[] = nativeURI.split("[?]");
						String nativeName = "";
						if (nativeURIs.length > 0) {
							nativeName = nativeURIs[0].replace("toNative-", "");
							if (!TextUtils.isEmpty(nativeName)
									&& !"undefined".equals(nativeName)) {
								Class c = Class.forName("com.thestore.microstore." + nativeName);
								if (c != null) {
									Intent intent = new Intent();
									intent.setClass(this, c);
									
									//加载参数
									if(nativeURIs.length > 1){
										String parameters[] = nativeURIs[1].split("[&]");
										for(String parameter : parameters){
											String paraKeyValue[] = parameter.split("[=]");
											if(paraKeyValue.length == 2){
												intent.putExtra(paraKeyValue[0], paraKeyValue[1]);
											}
										}
									}

									((Activity) this).startActivity(intent);
									// 清空cookie中的nativeURI
									CookiesUtil.setNativeURI(this, "");
									
								}// end if
							}// end if
						}// end if

					}else{
						mWebView.goBack();

						setWebControllerBtnState();
					}
				} catch (Exception e) {
					Logger.e(TAG, e.getMessage());
				}

				break;
			}
		case R.id.web_next_btn:
			if (mWebView.canGoForward()) {
				mWebView.goForward();
			}
			setWebControllerBtnState();
			break;

		case R.id.web_refresh_btn:
			mWebView.reload();
			break;
		case R.id.web_home_btn:
			mWebView.loadUrl(mUrl);
			break;
		default:
			break;
		}
	}

	private void setWebControllerBtnState() {
		mPreviousBtn.setEnabled(mWebView.canGoBack());
		mNextBtn.setEnabled(mWebView.canGoForward());
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void initializeView() {
		Intent it = getIntent();
//		mUrl = "file:///android_asset/Noname1.html";
//		mUrl = "file:///android_asset/testimg.html";
		restoreCookies();
		setSystemParameter();
		mProgressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
		mWebView = (WebView) findViewById(R.id.web_view);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setBuiltInZoomControls(true); // 显示放大缩小
		webSettings.setSupportZoom(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setDomStorageEnabled(true);// 支持H5的local storage
		webSettings.setDatabasePath("/data/data/" + this.getPackageName() + "/databases/");
		webSettings.setSavePassword(false);// 不提示记住密码
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				mProgressBar.setProgress(progress);
				if (progress != 100) {
					mProgressBar.setVisibility(View.VISIBLE);
				} else {
					mProgressBar.setVisibility(View.GONE);
				}
			}
			public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
				mUploadMessage = uploadFile;
		        //uploadFile.onReceiveValue(null);
		        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
	            i.addCategory(Intent.CATEGORY_OPENABLE);
	            i.setType("*/*");
	            MainActivity.this.startActivityForResult(
	            	     Intent.createChooser(i, "请选择要上传的文件"),
	            	     MainActivity.FILECHOOSER_RESULTCODE);		           
		}
		});
		mWebView.setDownloadListener(new MyWebViewDownLoadListener());
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			
	        @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
	        	mWebView.setVisibility(View.GONE);
	        	mWebError.setVisibility(View.VISIBLE);
	        	
	        	super.onReceivedError(view, errorCode, description, failingUrl);
            } 

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				setWebControllerBtnState();
			}
		});

		jsInterface = new YHDJavaScriptInterface(this, mAPKDownLoadUrl, mVDDomain);
		mWebView.addJavascriptInterface(jsInterface,
				YHDJavaScriptInterface.YHD_JS_INTERFACE_NAME);

		mWebView.loadUrl(mUrl);
		mWebviewController = (RelativeLayout) findViewById(R.id.web_controller);
		mWebError = (RelativeLayout) findViewById(R.id.web_error);
		mWebError.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mWebError.setVisibility(View.GONE);
            	mWebView.reload();
            	mWebView.setVisibility(View.VISIBLE);
            }
        });
		mPreviousBtn = (ImageButton) findViewById(R.id.web_pre_btn);
		mPreviousBtn.setOnClickListener(this);
		mNextBtn = (ImageButton) findViewById(R.id.web_next_btn);
		mNextBtn.setOnClickListener(this);
		mRefreshBtn = (ImageButton) findViewById(R.id.web_refresh_btn);
		mRefreshBtn.setOnClickListener(this);
		mBackToHomeBtn = (Button) findViewById(R.id.web_home_btn);
		mBackToHomeBtn.setOnClickListener(this);

	}
	
	private class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			persistentCookies();
			
			mWebView.removeAllViews();
			((ViewGroup) mWebView.getParent()).removeView(mWebView);
			mWebView.destroy();
			
    		// 在Activity中停止ShareSDK
//    		ShareSDK.stopSDK(this);			
		} catch (Exception e) {
		}
	}

	private void persistentCookies() {
        CookieManager cookie = CookieManager.getInstance();
        String cookies = cookie.getCookie(mYHDDomain);
        Log.e("vdian-save",cookies);
        SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        Editor edit = sp.edit();
        edit.putString("yhd-cookies", cookies);
        edit.commit();
        
        CookieManager cookieVD = CookieManager.getInstance();
        String cookiesVD = cookieVD.getCookie(mVDDomain);
        Log.e("vdian-save",cookiesVD);
        SharedPreferences spVD = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        Editor editVD = spVD.edit();
        editVD.putString("vdian-cookies", cookiesVD);
        editVD.commit();
	}

	private void restoreCookies() {
        SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        String cookies = sp.getString("yhd-cookies", "");
        Log.e("vdian-get",cookies);
        CookieManager cookie = CookieManager.getInstance();
        cookie.setAcceptCookie(true);
        cookie.setCookie(mYHDDomain, cookies);
        
        SharedPreferences spVD = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        String cookiesVD = spVD.getString("vdian-cookies", "");
        Log.e("vdian-get",cookies);
        CookieManager cookieVD = CookieManager.getInstance();
        cookieVD.setAcceptCookie(true);
        cookieVD.setCookie(mVDDomain, cookiesVD);
	}
	
	/**
	 * 从缓存文件中获取V店配置
	 */
	private void getVdianConfigFromStorage(){
        SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        if(!TextUtils.isEmpty(sp.getString("mUrl", ""))){
        	mUrl = sp.getString("mUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mYHDDomain", ""))){
        	mYHDDomain = sp.getString("mYHDDomain", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mVDDomain", ""))){
        	mVDDomain = sp.getString("mVDDomain", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mVDianOrderDetailUrl", ""))){
        	mVDianOrderDetailUrl = sp.getString("mVDianOrderDetailUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mIncomeListUrl", ""))){
        	mIncomeListUrl = sp.getString("mIncomeListUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mAPKDownLoadUrl", ""))){
        	mAPKDownLoadUrl = sp.getString("mAPKDownLoadUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mCreateStartUrl", ""))){
        	mCreateStartUrl = sp.getString("mCreateStartUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mSellerIndexUrl", ""))){
        	mSellerIndexUrl = sp.getString("mSellerIndexUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mSoldOutUrl", ""))){
        	mSoldOutUrl = sp.getString("mSoldOutUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mNoStockUrl", ""))){
        	mNoStockUrl = sp.getString("mNoStockUrl", "");
        }
        if(!TextUtils.isEmpty(sp.getString("mKeyword", ""))){
        	mKeyword = sp.getString("mKeyword", "");
        }
        mT = sp.getLong("mT", 0L);        
	}
	
	private void setSystemParameter(){
		CookieManager cookieVD = CookieManager.getInstance();
		cookieVD.setAcceptCookie(true);
		
        // os标记android
        String osType = "osType=10";
        cookieVD.setCookie(mVDDomain, osType);
        // 设备号
        String deviceCode = Config.getUUid(this);
        String dcode = "dcode="+deviceCode;
        cookieVD.setCookie(mVDDomain, dcode);
        // App版本号 
        Util util = new Util(this);
        String versionName = util.getVersionName();
        String appVersion = "appVersion="+versionName;
        cookieVD.setCookie(mVDDomain, appVersion);
        
        CookieSyncManager.getInstance().sync();// 同步
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case  ERROR_PAGE:
			if (resultCode == Activity.RESULT_OK) {
				mWebView.reload();
			}
			break;
		case TAKE_PHOTO_SQUARE:
			if (resultCode == Activity.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/vdianPhoto.jpg");
				crop(Uri.fromFile(temp),"0");
			}
			break;
		case TAKE_PHOTO_RECTANGLE:
			if (resultCode == Activity.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/vdianPhoto.jpg");
				crop(Uri.fromFile(temp),"1");
			}
			break;
		case GET_ALBUM_SQUARE:
			if (resultCode == Activity.RESULT_OK) {
				// 获得图片的uri
				Uri originalUri = data.getData();
				String path = ImageUtil.getPath(MainActivity.this, originalUri);
				ImageUtil.copySdcardFile(path, Environment.getExternalStorageDirectory() + "/vdianPhoto.jpg");
				File temp = new File(Environment.getExternalStorageDirectory() + "/vdianPhoto.jpg");
				crop(Uri.fromFile(temp),"0");

			}
			break;
		case GET_ALBUM_RECTANGLE:
			if (resultCode == Activity.RESULT_OK) {
				// 获得图片的uri
				Uri originalUri = data.getData();
				String path = ImageUtil.getPath(MainActivity.this, originalUri);
				ImageUtil.copySdcardFile(path, Environment.getExternalStorageDirectory() + "/vdianPhoto.jpg");
				File temp = new File(Environment.getExternalStorageDirectory() + "/vdianPhoto.jpg");
				crop(Uri.fromFile(temp),"1");
			}
			break;
		case PHOTO_REQUEST_CUT:
			if (resultCode == Activity.RESULT_OK) {
				// 从剪切图片返回的数据
				Bitmap bitmap = data.getParcelableExtra("data");
				byte[] bitmap2Bytes = BitmapUtil.bitmap2Bytes(bitmap);
				int id = IdGenerator.getInstance().nextId();
				jsInterface.putData(id,
						Base64.encodeToString(bitmap2Bytes, Base64.DEFAULT));
				mWebView.loadUrl("javascript:notifyData(" + id + ")");
			}
			break;
		case FILECHOOSER_RESULTCODE:
			if (null == mUploadMessage) return;
		    Uri result = data == null || resultCode != RESULT_OK ? null: data.getData();
		    mUploadMessage.onReceiveValue(result);
		    mUploadMessage = null;
		    break;
		default:
			break;

		}
	}
	

	private void crop(Uri uri, String type) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		if("1".equals(type)){
			// 裁剪框长方形的比例，2：1
			intent.putExtra("aspectX", 2);
			intent.putExtra("aspectY", 1);
			// 裁剪后输出图片的尺寸大小
			intent.putExtra("outputX", 500);
			intent.putExtra("outputY", 250);
		}else{
			// 裁剪框正方形的比例，1：1
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// 裁剪后输出图片的尺寸大小
			intent.putExtra("outputX", 250);
			intent.putExtra("outputY", 250);
		}

		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
    
	/**
	 * 初始化定位
	 */
	private void initLocation() {
		
		VDLocationListenner vdListener = new VDLocationListenner();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(vdListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		option.setPriority(LocationClientOption.NetWorkFirst);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	
	private class VDLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null){
				return;
			}	

			if (location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeGpsLocation) {
				saveLocationToVDCookie(mVDDomain,location.getLongitude(),location.getLatitude());
				mLocationClient.stop();
				
				// 获取到定位之后再做APP数据跟踪
				new Thread(new Runnable() {
					public void run() {
						try {
							recordTrack();
						} catch (Exception e) {
							Logger.d(TAG, e.getMessage());
						}
					}
				}).start();
				
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}
	}
	
	/** 
	 * 经纬度写入微店cookie
	 * @param vdDomain
	 * @param longitude 经度
	 * @param latitude 纬度
	 */
	private void saveLocationToVDCookie(String vdDomain, double longitude, double latitude){
		CookieSyncManager.createInstance(this);
        CookieManager cookieVD = CookieManager.getInstance();
        cookieVD.setAcceptCookie(true);
        String lng = "longitude="+longitude+";";
        cookieVD.setCookie(vdDomain, lng);
        String lat = "latitude="+latitude+";";
        cookieVD.setCookie(vdDomain, lat);
        CookieSyncManager.getInstance().sync();// 同步
	}
	
	/**
	 * 保存取消更新日期
	 */
	public void saveCancelUpdate(){
        SharedPreferences spVD = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        Editor editVD = spVD.edit();
        editVD.putString("cancelUpdate", Util.getDateString(new Date(),"yyyyMMdd"));
        editVD.commit();
	}
	
	/**
	 * 启动异步任务下载更新
	 */
	public void executeUpdateAsyncTask(){
		UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(this, mAppVersion);
		updateAsyncTask.execute();  
	}
	
	/**
	 * 检查WebView是否需要关闭
	 */
	public void checkWebView(){
		if (!mWebView.canGoBack()){
			this.finish();
		}
	}
	
	/**
	 * 显示更新对话框
	 */
	private void showUpdateDialog(AppVersionVO appVersion)
	{	
		isShowUpdateDialog = false;
		try {			
			// 构造对话框
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle(mUpdateTitle);
			builder.setMessage(appVersion.getUpgradeKeyword());
			// 以后再说
			if (!appVersion.isForceUpgrade()) {
				builder.setPositiveButton("以后再说", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//保存取消更新日期
						saveCancelUpdate();
						isShowUpdateDialog = true;
						dialog.dismiss();
					}
				});
			}
			// 现在升级
			builder.setNegativeButton("现在升级", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					isShowUpdateDialog = true;
					dialog.dismiss();
					executeUpdateAsyncTask();
				}
			});
			Dialog noticeDialog = builder.create();
			// 强制升级返回则退出App
			if (appVersion.isForceUpgrade()) {
				noticeDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {			
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if(keyCode == KeyEvent.KEYCODE_BACK){
							System.exit(0);					
						}
						return false;//false还会响应源代码，true只响应自己的代码
					}  
				});  
			}
			noticeDialog.setCanceledOnTouchOutside(false); //点击外部不会消失 
			noticeDialog.show();
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage());
		}
	}
	
    /**
     * APP数据跟踪
     */
    private void recordTrack(){
    	try {
	    	RequestVo requestVo = new RequestVo();
			requestVo.requestUrl = Config.SERVLET_URL_RECORDTRACK;
			requestVo.context = this;
			HashMap<String, String> postParamMap = new HashMap<String, String>();
			postParamMap.put("sts", Const.TRACK_STATUS_START);
			postParamMap.put("msg", "");
			requestVo.requestDataMap = postParamMap;
			requestVo.jsonParser = new ResultParser();
			Result result = (Result) HttpUtil.post(requestVo);
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
		}
    }
    
    /**
     * APP用户注册
     */
    private void addVUser(){
    	try {
	    	RequestVo requestVo = new RequestVo();
			requestVo.requestUrl = Config.SERVLET_URL_ADD_VUSER;
			requestVo.context = this;
			requestVo.jsonParser = new ResultParser();
			Result result = (Result) HttpUtil.post(requestVo);
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
		}
    }
    
    /**
     * APP拉取消息
     */
    private void getPushInformation(){
    	try {
	    	RequestVo requestVo = new RequestVo();
			requestVo.requestUrl = Config.SERVLET_URL_GET_PUSHINFORMATION;
			requestVo.context = this;
			requestVo.jsonParser = new ResultParser();
			Result result = (Result) HttpUtil.post(requestVo);
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
		}
    }

}
