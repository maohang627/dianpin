package com.thestore.microstore;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import cn.sharesdk.framework.ShareSDK;

import com.thestore.microstore.data.Const;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.ThreadPoolManager;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.commom.BaseEntity;

/**
 * 
 * wapper 基类
 * 
 * 2014-9-4 上午8:57:21
 */
public abstract class BaseWapperActivity extends Activity implements OnClickListener {

	// 定义日志tag
	private static final String TAG = "BaseWapperActivity";
	
	protected String from;

	private Application application;

	private CustomClickListener customClickListener;

	/** 线程池管理 */
	private ThreadPoolManager threadPoolManager;

	protected Context context;

	/** content layout */
	private LinearLayout contentLayout;

	/** ContentView */
	private View inflate;

	/** 事务处理 */
	protected ProgressDialog progressDialog;

	/** head layout */
	private RelativeLayout headLayout;

	/** head 标题 */
	private TextView headTitle;

	/** head 左边的TextView */
	private TextView headLeftText;

	/** head用 右边的TextView */
	private TextView headRightText;

	/** 是否显示过程弹出层 */
	private boolean isShowProcessDialog = Boolean.TRUE;

	/**
	 * 基类初始化时 获取 线程池单例对象
	 */
	public BaseWapperActivity() {
		threadPoolManager = ThreadPoolManager.getInstance();
	}

	/**
	 * start 绘画
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {

			super.onCreate(savedInstanceState);
			application = getApplication();

			// -- 自定义标题
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

			// -- 设置主框架
			super.setContentView(R.layout.frame);

			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

			// -- 初始化属性
			contentLayout = (LinearLayout) super.findViewById(R.id.frame_content);
			headLayout = (RelativeLayout) super.findViewById(R.id.head_layout);
			headTitle = (TextView) super.findViewById(R.id.head_title);
			headLeftText = (TextView) super.findViewById(R.id.head_left);
			headRightText = (TextView) super.findViewById(R.id.head_right);

			customClickListener = new CustomClickListener();
			headLeftText.setOnClickListener(customClickListener);
			headRightText.setOnClickListener(customClickListener);

			context = getApplicationContext();

			// -- 初始化
			loadViewLayout();
			initAttrs();
			process();

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			persistentCookies();

			// 在Activity中停止ShareSDK
//			ShareSDK.stopSDK(this);
		} catch (Exception e) {
		}
	}

	private void persistentCookies() {
		CookieManager cookie = CookieManager.getInstance();
		String cookies = cookie.getCookie(Config.YHD_DOMAIN);
		Log.e("vdian-save", cookies);
		SharedPreferences sp = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("yhd-cookies", cookies);
		edit.commit();

		CookieManager cookieVD = CookieManager.getInstance();
		String cookiesVD = cookieVD.getCookie(Config.VD_DOMAIN);
		Log.e("vdian-save", cookiesVD);
		SharedPreferences spVD = getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
		Editor editVD = spVD.edit();
		editVD.putString("vdian-cookies", cookiesVD);
		editVD.commit();
	}

	/**
	 * 动态加载ViewLayout
	 */
	protected abstract void loadViewLayout();

	/**
	 * 起线程 读取远程数据
	 */
	protected abstract void process();

	/**
	 * 初始化一些常量
	 */
	protected abstract void initAttrs();

	/**
	 * 重新设置content部分的view
	 */
	@Override
	public void setContentView(int layoutResID) {
		inflate = getLayoutInflater().inflate(layoutResID, null);
		setContentView(inflate);
	}

	public void setContentView(View view) {
		contentLayout.removeAllViews();
		contentLayout.addView(inflate);
	}

	/**
	 * 获取View
	 */
	@Override
	public View findViewById(int id) {
		return inflate.findViewById(id);
	}

	/**
	 * head 左边按钮处理
	 * 
	 * @param v
	 */
	protected void onHeadLeftButton(View v) {
		finish();
	}

	/**
	 * 修改head标题内容
	 */
	public void setTitle(CharSequence title) {
		headTitle.setText(title);
	}

	/**
	 * 修改head标题内容
	 */
	public void setTitle(int titleId) {
		headTitle.setText(titleId);
	}

	/**
	 * 显示左侧按钮 同时设置内容
	 * 
	 * @param text
	 */
	public void setLeftTextTo(CharSequence text, boolean isShow, Integer textColorId) {
		if (isShow) {
			headLeftText.setVisibility(View.VISIBLE);
		} else {
			headLeftText.setVisibility(View.INVISIBLE);
		}

		if (textColorId != null) {
			headLeftText.setTextColor(textColorId);
		}
		headLeftText.setText(text);
	}

	/**
	 * 显示右侧按钮 同时设置内容
	 * 
	 * @param text
	 */
	public void setRightTextTo(CharSequence text, boolean isShow, Integer textColorId) {
		if (isShow) {
			headRightText.setVisibility(View.VISIBLE);
		} else {
			headRightText.setVisibility(View.INVISIBLE);
		}

		if (textColorId != null) {
			headRightText.setTextColor(textColorId);
		}
		headRightText.setText(text);
	}

	/**
	 * head 右边按钮处理
	 * 
	 * @param v
	 */
	protected void onHeadRightButton(View v) {

	}

	abstract class DataCallback<T> {

		// 正确的逻辑
		public abstract void processData(T paramObject);

		// 错误的信息处理, 如果需要父类处理则返回true，如果子类有对错误编码做特殊处理，则返回false
		public boolean errorLogicAndCall(BaseEntity obj) {
			return Boolean.TRUE;
		}

	}

	/**
	 * 从服务器上获取数据，并回调处理
	 * 
	 * @param reqVo
	 * @param dataCallback
	 */
	protected void asynForLoadDataAndDraw(RequestVo reqVo, DataCallback callBack) {

		if (isShowProcessDialog) {
			showProgressDialog();
		}
		final BaseHandler handler = new BaseHandler(this, callBack, reqVo);
		BaseTask taskThread = new BaseTask(this, reqVo, handler);
		this.threadPoolManager.addTask(taskThread);
	}
	
	/**
	 * 从服务器上获取数据，并回调处理
	 * 
	 * @param reqVo
	 * @param dataCallback
	 */
	protected void asynForLoadDataAndDrawWithOutProgress(RequestVo reqVo, DataCallback callBack) {
		final BaseHandler handler = new BaseHandler(this, callBack, reqVo);
		BaseTask taskThread = new BaseTask(this, reqVo, handler);
		this.threadPoolManager.addTask(taskThread);
	}

	/**
	 * 显示提示框
	 */
	protected void showProgressDialog() {
		if ((!isFinishing()) && (this.progressDialog == null)) {
			this.progressDialog = new ProgressDialog(this);
		}
		this.progressDialog.setTitle(getString(R.string.load_title));
		this.progressDialog.setMessage(getString(R.string.load_content));
		progressDialog.setCanceledOnTouchOutside(false); // 点击外部不关闭层
		this.progressDialog.show();
	}

	/**
	 * 关闭提示框
	 */
	protected void closeProgressDialog() {
		if (this.progressDialog != null) {
			this.progressDialog.dismiss();
		}
	}

	/**
	 * 返回是否显示弹出层
	 * 
	 * @return
	 */
	public boolean isShowProcessDialog() {
		return isShowProcessDialog;
	}

	/**
	 * 设置是否显示过程弹出层
	 * 
	 * @param isShowProcessDialog
	 */
	public void setShowProcessDialog(boolean isShowProcessDialog) {
		this.isShowProcessDialog = isShowProcessDialog;
	}

	/**
	 * 此方法在 handler中 设置adapter 绘制页面
	 */
	// protected abstract void draw(Object obj);

	// handler
	class BaseHandler extends Handler {
		private Context context;
		private DataCallback callBack;
		private RequestVo reqVo;

		BaseHandler(BaseWapperActivity context, DataCallback callBack, RequestVo reqVo) {
			this.context = context;
			this.callBack = callBack;
			this.reqVo = reqVo;
		}

		@Override
		public void handleMessage(final Message msg) {
			Log.i(TAG, "BaseHandler处理逻辑.....");

			// 首先关闭弹出层
			if (isShowProcessDialog()) {
				closeProgressDialog();
			}
			if (msg.what == Const.SUCCESS) {
				BaseEntity obj = (BaseEntity) msg.obj;
				if (obj == null) {
					Log.e(TAG, "不可能出现的异常， 在BaseWapperActitivy中handleMessage方法, const fail: obj == null");
				} else {

					// 如果接口返回的code为0 则走正常处理逻辑， 否则 如果DataCallback实现类有对错误做特殊处理的优先走，然后再由子类决定其它错误是否交给父类处理
					if ("0".equals(obj.getRtnCode())) {
						callBack.processData(msg.obj);
					} else {
						if (callBack.errorLogicAndCall(obj)) {

							// 弹出公共异常
							Builder builder = new Builder(this.context);
							builder.setTitle("");
							String rtnMsg = Const.ERROR_MSG_COMMON;
							if(!TextUtils.isEmpty(obj.getRtnMsg())){
								rtnMsg = obj.getRtnMsg();
							}
							builder.setMessage(rtnMsg);
							builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							Dialog alertDialog = builder.create();
							alertDialog.setCancelable(false);
							alertDialog.setCanceledOnTouchOutside(false); // 点击外部不会消失
							alertDialog.show();

						} else {
							Log.e(TAG, "不可能出现的异常， 在BaseWapperActitivy中handleMessage方法");
						}
					}

				}
			} else if (msg.what == Const.FAILED) {
				Log.e(TAG, "const fail..");
			}

		}

	}

	/**
	 * 此方法在线程中,获取数据
	 */
	// protected abstract Object loadData(RequestVo reqVo);

	// task
	class BaseTask extends Thread {

		private BaseWapperActivity context;
		private RequestVo reqVo;
		private Handler handler;

		public BaseTask(BaseWapperActivity context, RequestVo reqVo, Handler handler) {
			this.context = context;
			this.reqVo = reqVo;
			this.handler = handler;
		}

		@Override
		public void run() {
			Object obj = null;
			Message msg = Message.obtain();
			try {
				if (reqVo.testObject != null) {
					// 测试数据
					obj = reqVo.testObject;
					msg.what = Const.SUCCESS;
					msg.obj = obj;
					handler.sendMessage(msg);
				} else {
					if (HttpUtil.hasNetwork(context)) {
						obj = HttpUtil.post(reqVo);
						msg.what = Const.SUCCESS;
						msg.obj = obj;
						handler.sendMessage(msg);
					} else {
						msg.what = Const.NET_FAILED;
						msg.obj = obj;
						handler.sendMessage(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 私有全局头部按钮监听里
	 */
	private class CustomClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.head_left:
				onHeadLeftButton(view);
				break;
			case R.id.head_right:
				onHeadRightButton(view);
				break;

			default:

				Log.e(TAG, "ButtonClickListener此处还未做处理...");
				break;
			}
		}

	}

	/**
	 * 跳转到H5登录页面
	 */
	public void toH5GoLogin(Context context, String returnUrl) {
		Intent intent = new Intent();
		intent.setClass(context, WebViewActivity.class);
		intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/myvdian/gologin.do?returnUrl="+Uri.encode(returnUrl));

		((Activity) context).startActivity(intent);
	}
	
	/**
	 * 公共弹出提示
	 * @param title
	 * @param message
	 * @param canceledOnTouchOutside false:点击外部不会消失
	 */
	public void showCommDialog(String title, String message, boolean canceledOnTouchOutside){
		Builder builder = new Builder(this.context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog alertDialog = builder.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside); // 点击外部不会消失
		alertDialog.show();
	}

}
