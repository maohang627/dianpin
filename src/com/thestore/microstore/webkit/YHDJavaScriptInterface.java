package com.thestore.microstore.webkit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.thestore.main.core.datastorage.bean.UserInfo;
import com.thestore.microstore.AddressActivity;
import com.thestore.microstore.AddressListActivity;
import com.thestore.microstore.MainActivity;
import com.thestore.microstore.OrderActivity;
import com.thestore.microstore.OrderCreateActivity;
import com.thestore.microstore.OrderDetailActivity;
import com.thestore.microstore.R;
import com.thestore.microstore.ShopCartActivity;
import com.thestore.microstore.ShopCartNoneActivity;
import com.thestore.microstore.VDApplication;
import com.thestore.microstore.WebViewActivity;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.data.SharePlatform;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.util.CheckUpdateAsyncTask;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.DES3;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.UpdateAsyncTask;
import com.thestore.microstore.util.Util;
import com.thestore.microstore.vo.AppVersionVO;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;


public class YHDJavaScriptInterface {

	private static final String TAG = "YHDJavaScriptInterface";

	public static final String YHD_JS_INTERFACE_NAME = "vdian";

	private Activity mContext = null;

	private SparseArray<String> imgArr = new SparseArray<String>();

	private AppVersionVO mAppVersion;
	private String mUpdateTitle = "更新提示";
	private String mAPKDownLoadUrl = "";
	private String mVDDomain = "";

	private static final int ADDVUSER = 1001;
	private static final int SETOPEN = 1002;
	
	private Tencent mTencent;

	public YHDJavaScriptInterface(Activity context, String apkDownLoadUrl, String vdDomain) {
		mContext = context;
		mAPKDownLoadUrl = apkDownLoadUrl;
		mVDDomain = vdDomain;
		mTencent = Tencent.createInstance(Const.APP_ID_QQ, context);
	}

	public void toastOnAndroid(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 到达Native界面
	 * @param from
	 * @param params
	 */
	public void toNative(String from, String params) {
		Log.i(TAG, "from="+from);
		try {
			if(!TextUtils.isEmpty(params)){
				JSONObject jsonObject = new JSONObject(params);
				if (!jsonObject.isNull("nativeName")) {
					String nativeName = jsonObject.getString("nativeName");
					Log.i(TAG, "nativeName="+nativeName);
					
					if(!TextUtils.isEmpty(nativeName) && !"undefined".equals(nativeName)){
						Class c = Class.forName("com.thestore.microstore."+nativeName);
						if(c != null){
							Intent intent = new Intent();
							intent.setClass(mContext, c);
							
							if(!TextUtils.isEmpty("from")){
								intent.putExtra("from", from);
							}
							
							if (!jsonObject.isNull("shoppingBizType")) {
								String shoppingBizType = jsonObject.getString("shoppingBizType");
								if("FAST_VDIAN_MOBILE".equals(shoppingBizType)){
									intent.putExtra("resource", "ShopCartActivity");
									intent.putExtra("shoppingBizType", "FAST_VDIAN_MOBILE");
								}
							}
							
							// 订单已生成，订单详情
							if("OrderCreateActivity".equals(nativeName) || "OrderDetailActivity".equals(nativeName)){
								if (!jsonObject.isNull("orderCode")) {
									String orderCode = jsonObject.getString("orderCode");
									intent.putExtra("orderCode", orderCode);
								}
							}
							
							((Activity) mContext).startActivity(intent);
//							((Activity) mContext).finish();
						}
					}

				}
			}

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 到达购物车页面
	 * 
	 * @param phone
	 */
	public void shopingCart(String str) {
		Log.i(TAG, "到达购物车页面");

		try {
			Intent intent = new Intent();
			intent.setClass(mContext, ShopCartActivity.class);
			((Activity) mContext).startActivity(intent);
//			((Activity) mContext).finish();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 到达空购物车页面
	 */
	public void shopingCartNone() {
		Log.i(TAG, "到达空购物车页面");

		try {
			Intent intent = new Intent();
			intent.setClass(mContext, ShopCartNoneActivity.class);
			((Activity) mContext).startActivity(intent);
//			((Activity) mContext).finish();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 到达我的地址 -- TODO
	 * 
	 * @param phone
	 */
	public void address() {
		Log.i(TAG, "到达我的地址页面");

		try {
			Intent intent = new Intent();
			intent.setClass(mContext, AddressActivity.class);
			((Activity) mContext).startActivity(intent);
//			((Activity) mContext).finish();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 到达我的地址列表页面
	 */
	public void addressList() {
		Log.i(TAG, "到达我的地址列表页面");

		try {
			Intent intent = new Intent();
			intent.setClass(mContext, AddressListActivity.class);
			((Activity) mContext).startActivity(intent);
//			((Activity) mContext).finish();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 到达订单结算页面
	 */
	public void order() {
		Log.i(TAG, "到达订单结算页面");

		try {
			Intent intent = new Intent();
			intent.setClass(mContext, OrderActivity.class);
			((Activity) mContext).startActivity(intent);
//			((Activity) mContext).finish();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 到达订单详情页面
	 */
	public void orderdetail() {
		Log.i(TAG, "到达订单详情页面");

		try {
			Intent intent = new Intent();
			intent.setClass(mContext, OrderDetailActivity.class);
			((Activity) mContext).startActivity(intent);
//			((Activity) mContext).finish();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 到达订单创建完成页面
	 */
	public void orderCreate() {
		Log.i(TAG, "到达订单创建完成页面");

		try {
			Intent intent = new Intent();
			intent.setClass(mContext, OrderCreateActivity.class);
			((Activity) mContext).startActivity(intent);
//			((Activity) mContext).finish();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * 拍正方形照片
	 */
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "vdianPhoto.jpg")));
		((Activity) mContext).startActivityForResult(intent, MainActivity.TAKE_PHOTO_SQUARE);
	}

	/**
	 * 拍长方形照片
	 */
	public void takeRectanglePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "vdianPhoto.jpg")));
		((Activity) mContext).startActivityForResult(intent, MainActivity.TAKE_PHOTO_RECTANGLE);
	}

	/**
	 * 获取正方形照片
	 */
	public void getAlbum() {
		final String IMAGE_TYPE = "image/*";
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(IMAGE_TYPE);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		((Activity) mContext).startActivityForResult(intent, MainActivity.GET_ALBUM_SQUARE);
	}

	/**
	 * 获取长方形照片
	 */
	public void getRectangleAlbum() {
		final String IMAGE_TYPE = "image/*";
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(IMAGE_TYPE);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		((Activity) mContext).startActivityForResult(intent, MainActivity.GET_ALBUM_RECTANGLE);
	}

	public String getData(int id) {
		String base64Img = imgArr.get(id);
		return base64Img;
	}

	public void putData(int id, String data) {
		imgArr.put(id, data);
	}

	public String getDeviceInfo() {
		TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = tm.getDeviceId();
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("deviceId", deviceId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj.toString();
	}

	public void shareContent(String msg) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT, msg);
		((Activity) mContext).startActivity(Intent.createChooser(intent, "分享"));
	}

	/**
	 * 分享内容
	 * 
	 * @param params
	 */
	public void shareContent(String platformID, String params) {
		if ("Clipboard".equals(platformID)) {
			// 复制文本到剪贴板
			copyContent(params, mContext);
		} else {
			// 调用SDK分享内容
			shareContentBySDK(platformID, params);
		}
	}

	/**
	 * 复制文本到剪贴板
	 * 
	 * @param params
	 * @param context
	 */
	public static void copyContent(String params, Context context) {
		if (params != null && params != "") {
			try {
				String content = "";
				JSONObject jsonObject = new JSONObject(params);
				if (!jsonObject.isNull("title")) {
					content += jsonObject.getString("title") + "，";
				}
				if (!jsonObject.isNull("text")) {
					content += jsonObject.getString("text") + "，";
				}
				if (!jsonObject.isNull("titleUrl")) {
					content += jsonObject.getString("titleUrl") + "，";
				}
				if (!"".equals(content)) {
					content = content.substring(0, content.length() - 1);
				}

				// 得到剪贴板管理器
				ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				// API level >= 11 
				 cmb.setText(content.trim());
//				cmb.setPrimaryClip(ClipData.newPlainText(null, content.trim()));

				Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
				// 失败
				Toast.makeText(context, "复制失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 调用ShareSDK分享内容
	 * 
	 * @param platformID
	 * @param params
	 */
//	private void shareContentByShareSDK(String platformID, String params) {
//		try {
//			if (platformID != null && platformID != "" && params != null && params != "") {
//				JSONObject jsonObject = new JSONObject(params);
//				// 在MainActivity onCreate时初始化ShareSDK
//				// ShareSDK.initSDK(mContext);
//				ShareParams sp = new ShareParams();
//				// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//				if (!jsonObject.isNull("title")) {
//					sp.setTitle(jsonObject.getString("title"));
//				}
//				// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//				if (!jsonObject.isNull("titleUrl")) {
//					sp.setTitleUrl(jsonObject.getString("titleUrl"));
//				}
//				// text是分享文本，所有平台都需要这个字段
//				if (!jsonObject.isNull("text")) {
//					sp.setText(jsonObject.getString("text"));
//				}
//				if (!jsonObject.isNull("imageUrl") && !"".equals(jsonObject.getString("imageUrl"))) {
//					// sp.setImageUrl("http://d12.yihaodianimg.com/t1/2011/0223/506/271/1449978_142x142.jpg");
//					sp.setImageUrl(jsonObject.getString("imageUrl"));
//				}
//				// url仅在微信（包括好友和朋友圈）中使用
//				if (!jsonObject.isNull("url")) {
//					sp.setUrl(jsonObject.getString("url"));
//				}
//				// site是分享此内容的网站名称，仅在QQ空间使用
//				if (!jsonObject.isNull("site")) {
//					sp.setSite(jsonObject.getString("site"));
//				}
//				// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//				if (!jsonObject.isNull("siteUrl")) {
//					sp.setSiteUrl(jsonObject.getString("siteUrl"));
//				}
//				sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
//
//		        //初始化ShareSDK
//		        ShareSDK.initSDK(mContext);
//				Platform platForm = ShareSDK.getPlatform(mContext, platformID);
//				// 设置分享事件回调
//				platForm.setPlatformActionListener(new PlatformActionListener() {
//					@Override
//					public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
//						// TODO Auto-generated method stub
//						Message msg = new Message();
//						msg.arg1 = 1;
//						msg.arg2 = action;
//						msg.obj = platform;
//						handler_share.sendMessage(msg);
//					}
//
//					@Override
//					public void onError(Platform platform, int action, Throwable t) {
//						// TODO Auto-generated method stub
//						Message msg = new Message();
//						msg.arg1 = 2;
//						msg.arg2 = action;
//						msg.obj = platform;
//						handler_share.sendMessage(msg);
//					}
//
//					@Override
//					public void onCancel(Platform platform, int action) {
//						// TODO Auto-generated method stub
//						Message msg = new Message();
//						msg.arg1 = 3;
//						msg.arg2 = action;
//						msg.obj = platform;
//						handler_share.sendMessage(msg);
//					}
//				});
//				// 执行图文分享
//				platForm.share(sp);
//			}
//		} catch (Exception e) {
//			// 失败
//			Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
//		}
//	}

	/**
	 * 分享事件回调
	 */
//	final Handler handler_share = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.arg1) {
//			case 1: { // 成功
//				Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
//			}
//				break;
//			case 2: { // 失败
//				Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
//			}
//				break;
//			case 3: { // 取消
//				Toast.makeText(mContext, "分享取消", Toast.LENGTH_SHORT).show();
//			}
//				break;
//			}
//		}
//	};

	/**
	 * 从cookie中读取vut
	 * 
	 * @param vdDomain
	 * @return token
	 */
	private String getVUT(String vdDomain) {
		String token = "";
		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(vdDomain);
		Log.e("vdian-get", cookiesVD);
		String[] cookiesVDArr = cookiesVD.split(";");
		for (String cookieVal : cookiesVDArr) {
			if (cookieVal.trim().startsWith("vut=")) {
				token = cookieVal.replace("vut=", "").trim();
				break;
			}
		}
		Log.e("token=", token);

		return token;
	}

	/**
	 * 向移动消息服务器注册token
	 */
	public void setToken() {
		SharedPreferences sp = mContext.getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
		if (!TextUtils.isEmpty(sp.getString("mVDDomain", ""))) {
			mVDDomain = sp.getString("mVDDomain", "");
		}
			
		String token = getVUT(mVDDomain);
		// Toast.makeText(mContext, token, Toast.LENGTH_LONG).show();
		if (token != null && !"".equals(token)) {
			UserInfo.setToken(token);
//			Request reqTask = AppContext.newRequest();
//			HashMap<String, Object> param = ParamHelper.jsonParam(ApiConst.ADDVUSER, null);
//			// 10.161.144.22 interface.m.yihaodian.com
//			// 端口8080
//			// http://ip:port/centralmobile/mobileservice/addVUser.do
//			reqTask.applyParam(Request.POST, UrlHelper.getUrl(ApiConst.path), null, new TypeToken<ResultVO<String>>() {
//			}.getType());
//			Message callback = handler.obtainMessage(ADDVUSER);
//			reqTask.setCallBack(callback);
//			reqTask.execute();
			
			// 1号V店APP用户注册
			new Thread(new Runnable() {
				public void run() {
					try {
						addVUser();
					} catch (Exception e) {
						Logger.e(TAG, e.getMessage());
					}
				}
			}).start();
			
		}
	}
	
    /**
     * APP用户注册
     */
    private void addVUser(){
    	try {
	    	RequestVo requestVo = new RequestVo();
			requestVo.requestUrl = Config.SERVLET_URL_ADD_VUSER;
			requestVo.context = mContext;
			requestVo.jsonParser = new ResultParser();
			Result result = (Result) HttpUtil.post(requestVo);
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage());
		}
    }

//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case ADDVUSER:
//
//				break;
//			case SETOPEN:
//				break;
//			default:
//				break;
//			}
//
//		};
//	};

	/**
	 * 拨打电话
	 * 
	 * @param phone
	 */
	public void tel(String phone) {
		if (phone != null && !"".equals(phone)) {
			try {
				mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel:" + phone)));
			} catch (Exception e) {
				Logger.e(TAG, e.getMessage());
			}
		}
	}
	
	Handler appVersionHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what == Const.TRUE){
				mAppVersion = (AppVersionVO)msg.obj;
				if(mAppVersion != null){
					// TODO:后续直接从检查更新接口中获取apk下载地址
					mAppVersion.setApkDownLoadUrl(mAPKDownLoadUrl);
					// 显示软件更新对话框
					showUpdateDialog(mAppVersion);
				}
			}else{
				String appName = (String)mContext.getResources().getText(R.string.app_name);
				Toast.makeText(mContext, "您当前使用的已经是最新版的"+appName+"，感谢您对"+appName+"的支持！", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	/**
	 * 显示更新对话框
	 */
	private void showUpdateDialog(AppVersionVO appVersion)
	{
		try {			
			// 构造对话框
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle(mUpdateTitle);
			builder.setMessage(appVersion.getUpgradeKeyword());
			// 以后再说
			if (!appVersion.isForceUpgrade()) {
				builder.setPositiveButton("以后再说", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//保存取消更新日期
						saveCancelUpdate();
						dialog.dismiss();
					}
				});
			}
			// 现在升级
			builder.setNegativeButton("现在升级", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
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
	 * 保存取消更新日期
	 */
	public void saveCancelUpdate(){
        SharedPreferences spVD = mContext.getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
        Editor editVD = spVD.edit();
        editVD.putString("cancelUpdate", Util.getDateString(new Date(),"yyyyMMdd"));
        editVD.commit();
	}
	
	/**
	 * 启动异步任务下载更新
	 */
	public void executeUpdateAsyncTask(){
		UpdateAsyncTask updateAsyncTask = new UpdateAsyncTask(mContext, mAppVersion);
		updateAsyncTask.execute();  
	}

	/**
	 * 检查升级
	 */
	public void checkUpdate() {
		try {
			SharedPreferences sp = mContext.getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
			if (!TextUtils.isEmpty(sp.getString("mVDDomain", ""))) {
				mVDDomain = sp.getString("mVDDomain", "");
			}
			if (!TextUtils.isEmpty(sp.getString("mAPKDownLoadUrl", ""))) {
				mAPKDownLoadUrl = sp.getString("mAPKDownLoadUrl", "");
			}

			//启动异步任务检查更新  
	        CheckUpdateAsyncTask checkAsyncTask = new CheckUpdateAsyncTask(mContext, appVersionHandler, mVDDomain);  
	        checkAsyncTask.execute();  
		} catch (Exception e) {
			Toast.makeText(mContext, "检查升级失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * H5返回
	 */
	public void gobackFromJSNavigation(){
		try {
			if(mContext instanceof WebViewActivity){
				((WebViewActivity)mContext).checkWebView();
			}else{
				((MainActivity)mContext).checkWebView();
			}			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	/**
	 * 去应用市场评价
	 */
	public void goToMarket(){
		try {
		    Uri uri = Uri.parse("market://details?id="+mContext.getPackageName());  
		    Intent intent = new Intent(Intent.ACTION_VIEW,uri);  
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		    mContext.startActivity(intent); 
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	/**
	 * 设置APP是否接收消息
	 * @param isPushing true:接收，false:不接收
	 */
	public void setIsPushing(boolean isPushing) {
		SharedPreferences sp = mContext.getSharedPreferences(Const.STORE_NAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(Const.STORE_IS_PUSHING, isPushing);
		edit.commit();
	}
	
	/**
	 * 设置APP接收消息时段
	 * @param startTime 开始时点
	 * @param endTime 结束时点
	 */
	public void setPushStartEndTime(String startTime, String endTime) {
		SharedPreferences sp = mContext.getSharedPreferences(Const.STORE_NAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(Const.STORE_PUSH_START_TIME, startTime);
		edit.putString(Const.STORE_PUSH_END_TIME, endTime);
		edit.commit();
	}
	
	/**
	 * 调用SDK分享内容
	 * 
	 * @param platformID
	 * @param params
	 */
	private void shareContentBySDK(String platformID, String params) {
		try {
			if (!TextUtils.isEmpty(platformID) && !TextUtils.isEmpty(params)) {
				JSONObject jsonObject = new JSONObject(params);
				if(Const.SHARE_QQ.equals(platformID)){
					shareToQQ(jsonObject);
				}else if(Const.SHARE_QZONE.equals(platformID)){
					shareToQzone(jsonObject);
				}else if(Const.SHARE_WECHAT.equals(platformID)){
					shareToWechat(jsonObject, 0);
				}else if(Const.SHARE_WECHATMOMENTS.equals(platformID)){
					shareToWechat(jsonObject, 1);
				}
			}

		} catch (Exception e) {
			// 失败
			Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	Bundle shareParams = null;

	private void shareToQQ(JSONObject jsonObject) throws JSONException {
		Bundle shareQQParams = getShareQQBundle(jsonObject);
		if(shareQQParams != null){
			shareParams = shareQQParams;
			Thread thread = new Thread(shareThread);
			thread.start();
		}
	}

	private Bundle getShareQQBundle(JSONObject jsonObject) throws JSONException{
		Bundle shareQQParams = new Bundle();
		String uuid = UUID.randomUUID().toString();
		try {
			uuid = DES3.toUrlStr(DES3.encode(uuid));
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage());
		}
		final String shareId = uuid;
		String targetUrl = "";
		
		 if (!jsonObject.isNull("title")) {
			 shareQQParams.putString(QQShare.SHARE_TO_QQ_TITLE, jsonObject.getString("title"));
		 }
		 if (!jsonObject.isNull("imageUrl") && !"".equals(jsonObject.getString("imageUrl"))) {
			 shareQQParams.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, jsonObject.getString("imageUrl"));
		 }
		 if (!jsonObject.isNull("url")) {
			 targetUrl = jsonObject.getString("url");
			 if(targetUrl.indexOf("?") > -1){
				 targetUrl += "&shareId="+shareId;
			 }else{
				 targetUrl += "?shareId="+shareId;
			 }
			 shareQQParams.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		 }
		 if (!jsonObject.isNull("text")) {
			 shareQQParams.putString(QQShare.SHARE_TO_QQ_SUMMARY, jsonObject.getString("text"));
		 }
		 shareQQParams.putString(QQShare.SHARE_TO_QQ_SITE,  (String)mContext.getResources().getText(R.string.app_name));
		 shareQQParams.putString(QQShare.SHARE_TO_QQ_APP_NAME, (String)mContext.getResources().getText(R.string.app_name));
		 
		 VDApplication.SHARE_FLAG = true;
	     VDApplication.SHARE_PLATFORM = SharePlatform.QQ.id;
         VDApplication.SHARE_ID = shareId;
         VDApplication.SHARE_URL = targetUrl;
		 
         return shareQQParams;
	}

	// 线程类，该类使用匿名内部类的方式进行声明
	Runnable shareThread = new Runnable() {

		public void run() {
			doShareToQQ(shareParams);
		}
	};

	private void doShareToQQ(Bundle params) {
		mTencent.shareToQQ(mContext, params, new IUiListener() {

            @Override
            public void onCancel() {
            	Util.toastMessage(mContext, mContext.getString(R.string.errcode_cancel));
            }

            @Override
            public void onComplete(Object response) {
                // APP分享数据统计
                if(VDApplication.SHARE_FLAG){
        			new Thread(new Runnable() {
        				public void run() {
        					try {
        				        Util util = new Util(VDApplication.getAppContext());
        				        util.recordShareData(VDApplication.SHARE_ID, VDApplication.SHARE_URL, VDApplication.SHARE_PLATFORM);
        					} catch (Exception e) {
        						Logger.e(TAG, e.getMessage());
        					}
        				}
        			}).start();
                }
                
                Util.toastMessage(mContext, mContext.getString(R.string.errcode_success));
            }

            @Override
            public void onError(UiError e) {
                Util.toastMessage(mContext, mContext.getString(R.string.errcode_failure));
            }

        });
	}
	
	private void shareToQzone(JSONObject jsonObject) throws JSONException {
		Bundle shareQzoneParams = getShareQzoneBundle(jsonObject);
		if(shareQzoneParams != null){
			shareParams = shareQzoneParams;
			Thread thread = new Thread(shareQzoneThread);
			thread.start();
		}
	}
	
	private Bundle getShareQzoneBundle(JSONObject jsonObject) throws JSONException{
		Bundle shareQzoneParams = new Bundle();
		String uuid = UUID.randomUUID().toString();
		try {
			uuid = DES3.toUrlStr(DES3.encode(uuid));
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage());
		}
		final String shareId = uuid;
		String targetUrl = "";
		
	     shareQzoneParams.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
	     if (!jsonObject.isNull("title")) {
	    	 shareQzoneParams.putString(QzoneShare.SHARE_TO_QQ_TITLE, jsonObject.getString("title"));
	     }
	     if (!jsonObject.isNull("text")) {
	    	 shareQzoneParams.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, jsonObject.getString("text"));
	     }
	     if (!jsonObject.isNull("url")) {
			 targetUrl = jsonObject.getString("url");
			 if(targetUrl.indexOf("?") > -1){
				 targetUrl += "&shareId="+shareId;
			 }else{
				 targetUrl += "?shareId="+shareId;
			 }			 
	    	 shareQzoneParams.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
	     }
	     if (!jsonObject.isNull("imageUrl") && !"".equals(jsonObject.getString("imageUrl"))) {
		     ArrayList<String> imageUrls = new ArrayList<String>();
		     imageUrls.add(jsonObject.getString("imageUrl"));
		     shareQzoneParams.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
	     }
	     shareQzoneParams.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
	     
	     VDApplication.SHARE_FLAG = true;
	     VDApplication.SHARE_PLATFORM = SharePlatform.QZONE.id;
         VDApplication.SHARE_ID = shareId;
         VDApplication.SHARE_URL = targetUrl;
         
	     return shareQzoneParams;
	}
	
	// 线程类，该类使用匿名内部类的方式进行声明
	Runnable shareQzoneThread = new Runnable() {

		public void run() {
			doShareToQzone(shareParams);
		}
	};
	
	 private void doShareToQzone(Bundle params) {
	      mTencent.shareToQzone(mContext, params, new IUiListener() {

              @Override
              public void onCancel() {
                  Util.toastMessage(mContext, mContext.getString(R.string.errcode_cancel));
              }

              @Override
              public void onError(UiError e) {
                  Util.toastMessage(mContext, mContext.getString(R.string.errcode_failure));
              }

			  @Override
			  public void onComplete(Object response) {
	                // APP分享数据统计
	                if(VDApplication.SHARE_FLAG){
	        			new Thread(new Runnable() {
	        				public void run() {
	        					try {
	        				        Util util = new Util(VDApplication.getAppContext());
	        				        util.recordShareData(VDApplication.SHARE_ID, VDApplication.SHARE_URL, VDApplication.SHARE_PLATFORM);
	        					} catch (Exception e) {
	        						Logger.e(TAG, e.getMessage());
	        					}
	        				}
	        			}).start();
	                }
	                
				  Util.toastMessage(mContext, mContext.getString(R.string.errcode_success));
			  }

          });
	   }
	
	/**
	 * 分享到微信
	 * @param jsonObject
	 * @param scene
	 */
	public void shareToWechat(final JSONObject jsonObject, int scene) {
		try{
			IWXAPI api = WXAPIFactory.createWXAPI(mContext, Const.APP_ID_WECHAT);
			boolean isWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
			String uuid = UUID.randomUUID().toString();
			try {
				uuid = DES3.toUrlStr(DES3.encode(uuid));
			} catch (Exception e) {
				Logger.e(TAG, e.getMessage());
			}
			final String shareId = uuid;
			String targetUrl = "";
			
			if (!isWXAppInstalledAndSupported) {
				// 尚未安装微信
				Util.toastMessage(mContext, Const.ERROR_MSG_NOWECHAT);
			}else{
				WXWebpageObject webpage = new WXWebpageObject();
				if (!jsonObject.isNull("url")) {
					targetUrl = jsonObject.getString("url");
					if (targetUrl.indexOf("?") > -1) {
						targetUrl += "&shareId=" + shareId;
					} else {
						targetUrl += "?shareId=" + shareId;
					}
					webpage.webpageUrl = targetUrl;
				}
				WXMediaMessage msg = new WXMediaMessage(webpage);
				if (!jsonObject.isNull("title")) {
					msg.title = jsonObject.getString("title");
				}
				if (!jsonObject.isNull("text")) {
					msg.description = jsonObject.getString("text");
				}
				if (!jsonObject.isNull("imageUrl") && !"".equals(jsonObject.getString("imageUrl"))) {
					String url = jsonObject.getString("imageUrl");
					Bitmap bmp = null;
					try {
						bmp = BitmapFactory.decodeStream(new URL(url).openStream());
					} catch (Exception e) {
						Logger.e(TAG, e.getMessage());
					}
					if(bmp != null){
						// 缩略图 限制内容大小不超过32KB，压缩到10KB
						msg.thumbData = Util.bmpToByteArray(imageZoom(bmp, 10d), true);
					}
				}
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("webpage");
				req.message = msg;
	            if (scene == 0) {
	                // 分享到微信会话
	                req.scene = SendMessageToWX.Req.WXSceneSession;
		            VDApplication.SHARE_PLATFORM = SharePlatform.WECHAT.id;
	            } else {
	                // 分享到微信朋友圈
	                req.scene = SendMessageToWX.Req.WXSceneTimeline;
		            VDApplication.SHARE_PLATFORM = SharePlatform.WECHATMOMENTS.id;
	            }
	            api.sendReq(req);
	            
	            VDApplication.SHARE_FLAG = true;
	            VDApplication.SHARE_ID = shareId;
	            VDApplication.SHARE_URL = targetUrl;	            
			}
			
		} catch(Exception e) {
			Logger.e(TAG, e.getMessage());
		}
	}
	
    /**
     * 压缩的办法
     *
     * @param bmp
     * @param maxSize
     * @return
     */
    private static Bitmap imageZoom(Bitmap bmp, double maxSize) {
        // 图片允许最大空间 单位：KB
        // double maxSize =400.00;
        // 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        Bitmap bitMap = bmp;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        // 将字节换成KB
        double mid = b.length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
        while (mid > maxSize) {
            // 获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            // 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
            // （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i), bitMap.getHeight() / Math.sqrt(i));
            mid = bmpToByteArray(bitMap, false).length / 1024;
        }
        return bitMap;
    }
    
    /***
     * 图片的缩放方法
     *
     * @param bgimage：源图片资源
     * @param newWidth：缩放后宽度
     * @param newHeight：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 把bitmap转字节数组
     *
     * @param bmp
     * @param needRecycle
     * @return
     */
    static byte[] bmpToByteArray(final Bitmap bmp, boolean needRecycle) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bmp.compress(CompressFormat.PNG, 100, output);
            if(needRecycle) {
                    bmp.recycle();
            }

            byte[] result = output.toByteArray();
            try {
                    output.close();
            } catch (Exception e) {
                    e.printStackTrace();
            }
            return result;
    }
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	/**
	 * 在新窗口中打开H5页面
	 * @param targetUrl
	 */
	public void openInNewWindow(String targetUrl){
		try {
			if(!TextUtils.isEmpty(targetUrl)){
				// 跳转到H5页面
				Intent intent = new Intent();
				intent.setClass(mContext, WebViewActivity.class);
				intent.putExtra("toH5Url", targetUrl);							
				mContext.startActivity(intent);	
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

}
