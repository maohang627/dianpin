package com.thestore.microstore.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.thestore.microstore.MainActivity;
import com.thestore.microstore.R;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.AppVersionParser;
import com.thestore.microstore.parser.VdianTimeParser;
import com.thestore.microstore.vo.AppVersionVO;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.VdianTimeVO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ProgressBar;

/**
 * 检查是否有更新
 * @author zhaojianjian
 *
 */
public class CheckUpdateAsyncTask extends AsyncTask<Integer, Integer, String> {  
	
	private static final String TAG = "CheckUpdateAsyncTask";
	private Context mContext;
	private Handler handler;
	private AppVersionVO appVersion;	
	private String mVDDomain = "";	
	private boolean mCanUpgrade = false;
	private boolean mForceUpgrade = false;
	private String mCurrentVersion = "";
	private String mUpdateInfo = "";
	
	public CheckUpdateAsyncTask(Context context, Handler handler, String vdDomain){
		this.mContext = context;
		this.handler = handler;
		this.mVDDomain = vdDomain;
	}
	
    @Override  
    protected String doInBackground(Integer... params) {  
    	RequestVo requestVo = new RequestVo();
		requestVo.requestUrl = Config.SERVLET_URL_CHECK_APPVERSION;
		requestVo.context = mContext;
		requestVo.jsonParser = new AppVersionParser();
		appVersion = (AppVersionVO) HttpUtil.post(requestVo);
		if(appVersion != null){
			mCanUpgrade = appVersion.isCanUpgrade();
			mForceUpgrade = appVersion.isForceUpgrade();
			mCurrentVersion = appVersion.getCurrentVersion();
			mUpdateInfo = appVersion.getUpgradeKeyword();			
			
			CookieSyncManager.createInstance(mContext);
			CookieManager cookieVD = CookieManager.getInstance();
			cookieVD.setAcceptCookie(true);

	        String canUpgrade = "canUpgrade="+(mCanUpgrade == true ? "1":"0")+";";
	        cookieVD.setCookie(mVDDomain, canUpgrade);
			String currentVersion = "currentVersion="+mCurrentVersion+";";
			cookieVD.setCookie(mVDDomain, currentVersion);
			
			CookieSyncManager.getInstance().sync();// 同步
		}        
    	
    	return String.valueOf(mCanUpgrade);
    }

	@Override
	protected void onPostExecute(String result) {
		if("true".equals(result)){
			Message msg = Message.obtain();
			msg.what = Const.TRUE;
			msg.obj = appVersion;
			handler.sendMessage(msg);
		}else{
			Message msg = Message.obtain();
			msg.what = Const.FALSE;
			msg.obj = appVersion;
			handler.sendMessage(msg);
		}
		
		super.onPostExecute(result);
	}

}
