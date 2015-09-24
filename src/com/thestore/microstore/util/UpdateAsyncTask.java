package com.thestore.microstore.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.thestore.microstore.R;
import com.thestore.microstore.vo.AppVersionVO;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateAsyncTask extends AsyncTask<Integer, Integer, String> {
	
	private static final String TAG = "UpdateAsyncTask";
	private Context mContext;
	private AppVersionVO mAppVersion;
//	private String mVDDomain = "";	
//	private boolean mCanUpgrade = false;
//	private String mCurrentVersion = "";
	private boolean mForceUpgrade = false;
	private String mUpdateTitle = "更新提示";
//	private String mUpdateInfo = "";
	private String mDownloadUrl = "http://vd.yhd.com/downloads/yihaovdian.apk";
	private String mAPKName = "yihaovdian.apk";
	private String mAPKPackage = "com.thestore.microstore";
	/* 下载中 */
//	private static final int DOWNLOAD = 1;
	/* 下载结束 */
//	private static final int DOWNLOAD_FINISH = 2;
	// 是否存在SD卡  
	private boolean sdExists = false;   
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
//	private int progress;
	/* 是否取消更新 */
	private boolean interceptFlag = false;	
	/* 更新进度条 */
	private Dialog mDownloadDialog;
	private TextView mProgressCount;
	private ProgressBar mProgress;
	
	public UpdateAsyncTask(Context context, AppVersionVO appVersion) {
		this.mContext = context;
		this.mAppVersion = appVersion;
		// 是否强制更新
		this.mForceUpgrade = appVersion.isForceUpgrade();
		// APK下载地址
		this.mDownloadUrl = appVersion.getApkDownLoadUrl();
	}
	
    @Override  
    protected void onPreExecute() {          
        if(checkSoftStage()){  
            showDownloadDialog();  
        }  
        super.onPreExecute();  
    } 
    
	@Override  
    protected String doInBackground(Integer... params) {  
		String result = ""; 
		if(!NetWorkUtil.checkURL(mDownloadUrl)){   //检查apk的下载地址是否可用  
            result = "netfail";  
        }else if(sdExists){  
            InputStream is = null;  
            FileOutputStream fos = null;  
            File file = new File(mSavePath);  
            if(!file.exists()){  
                file.mkdirs();  
            }  
            try {  
                URL url = new URL(mDownloadUrl);  
                URLConnection urlConn = url.openConnection();  
                is = urlConn.getInputStream();  
                int length = urlConn.getContentLength();   //文件大小  
                File apkFile = new File(mSavePath, mAPKName);
                fos = new FileOutputStream(apkFile);  
                  
                int count = 0,numread = 0;  
                byte buf[] = new byte[1024];  
                  
                while(!interceptFlag && (numread = is.read(buf))!=-1){  
                    count+=numread;  
                    int progressCount =(int)(((float)count / length) * 100);  
                    publishProgress(progressCount);  
                    fos.write(buf, 0, numread);  
                }  
                fos.flush();  
                result = "success";  
            } catch (Exception e) {  
                e.printStackTrace();  
                result = "fail";  
            }finally{  
                try {  
                    if(fos!=null)  
                        fos.close();  
                    if(is!=null)  
                        is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    result = "fail";  
                }  
            }  
        }  
        return result;  
    }  
	
    @Override  
    protected void onPostExecute(String result) {
		// 取消下载对话框显示
		mDownloadDialog.dismiss();
		
        if(!interceptFlag && "success".equals(result)){  
            installApk();  
        }else if("netfail".equals(result)){  
        	showExitDialog(mUpdateTitle, "连接服务器失败，请稍后重试！" , false);
        }
        
        super.onPostExecute(result); 
    }
    
    @Override  
    protected void onProgressUpdate(Integer... values) {  
        int count = values[0];  
        mProgress.setProgress(count);   //设置下载进度  
        mProgressCount.setText("进度："+count+"%");  
        
        super.onProgressUpdate(values);  
    }  
	
    /** 
     * 检测手机是否存在SD卡 
     */  
    private boolean checkSoftStage(){  
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //判断是否存在SD卡  
			// 获得存储卡的路径
			String sdpath = Environment.getExternalStorageDirectory() + "/";
			mSavePath = sdpath + "download";
        	File file = new File(mSavePath);  
            if(!file.exists()){  
                file.mkdir();  
            }  
            sdExists = true;
            return true;  
        }else{  
        	showExitDialog(mUpdateTitle, "检测到手机没有存储卡,请安装了内存卡后再升级！" , false);
            return false;  
        }  
    } 
	
	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgressCount = (TextView) v.findViewById(R.id.update_progress_count);
		mProgressCount.setText("进度：0");
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				interceptFlag = true;
				// 强制升级取消则退出App
	            if(mForceUpgrade){
	            	System.exit(0);
	            }
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
	}
	
	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mAPKName);
		if (!apkfile.exists())
		{
			showExitDialog(mUpdateTitle, "找不到下载的APK文件，请稍后重试！" , false);
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	
	/**
	 * 公共退出App提示
	 * @param title
	 * @param message
	 * @param canceledOnTouchOutside false:点击外部不会消失
	 */
	public void showExitDialog(String title, String message, boolean canceledOnTouchOutside){
		Builder builder = new Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
	        	// 强制升级失败则退出App
	            if(mForceUpgrade){
	            	System.exit(0);
	            }
			}
		});
		Dialog alertDialog = builder.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside); // 点击外部不会消失
		alertDialog.show();
	}
    
}
