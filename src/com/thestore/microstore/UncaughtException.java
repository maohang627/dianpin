package com.thestore.microstore;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.thestore.microstore.data.Const;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.HttpUtil;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;


/**
 * 捕获全局异常,因为有的异常我们捕获不到
 * 
 */
public class UncaughtException implements UncaughtExceptionHandler {
	private final static String TAG = "UncaughtException";
	/**
	 * 保存的异常信息的路径
	 */
	private static String path = Environment.getExternalStorageDirectory() + "/yihaovdian/crash/";

	private static UncaughtException mUncaughtException;
	private Context context;
	private StringBuffer sb;
	/**
	 * 取得当前时间
	 */
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	/**
	 * 声明当前年时间
	 */
	private String time;

	public Context getContext() {
		return context;
	}

	/**
	 * 初始化context 添加到需要检测的类下面
	 * 
	 * @param context
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	private UncaughtException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取UncaughtException实例 ,单例模式 同步方法，保证只有一个CrashHandler实例 ,以免单例多线程环境下出现异常
	 * 
	 * @return
	 */
	public synchronized static UncaughtException getInstance() {
		if (mUncaughtException == null) {
			mUncaughtException = new UncaughtException();
		}
		return mUncaughtException;
	}

	/**
	 * 
	 * 获取系统默认的UncaughtException处理器, 设置该mUncaughtException为程序的默认处理器
	 * 初始化，把当前对象设置成UncaughtExceptionHandler处理异常
	 */
	public void init() {
		Thread.setDefaultUncaughtExceptionHandler(mUncaughtException);
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 获得当前的Activity对象
		context = AppManager.getAppManager().currentActivity();
		// 发送错误报告到服务器
		 sendCrashReportsToServer(ex);
	}

	/**
	 * 错误信息传送到服务器
	 * 
	 * @param ex
	 */
	private void sendCrashReportsToServer(Throwable ex) {

		sb = new StringBuffer();

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		// 把异常信息加入字符串sb中
		sb.append(result.substring(0, 1900));
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					recordErrorTrack();
				} catch (Exception e) {
					Logger.d(TAG, e.getMessage());
				} finally{
					Looper.prepare();						
					showExitDialog("", Const.ERROR_MSG_COMMON, false);						
					Looper.loop();
				}
			}
		}).start();

	}
	
	/**
	 * 发送BUG信息到服务器
	 */
	private void recordErrorTrack() {	
		try {
	    	RequestVo requestVo = new RequestVo();
			requestVo.requestUrl = Config.SERVLET_URL_RECORDTRACK;
			requestVo.context = context;
			HashMap<String, String> postParamMap = new HashMap<String, String>();
			postParamMap.put("sts", Const.TRACK_STATUS_ERROR);
			postParamMap.put("msg", Uri.encode(sb.toString()));
			requestVo.requestDataMap = postParamMap;
			requestVo.jsonParser = new ResultParser();
			Result result = (Result) HttpUtil.post(requestVo);
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
		}
	}
	
	/**
	 * 公共退出App提示
	 * @param title
	 * @param message
	 * @param canceledOnTouchOutside false:点击外部不会消失
	 */
	public void showExitDialog(String title, String message, boolean canceledOnTouchOutside){
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
	        	// 退出App
				System.exit(0);
			}
		});
		Dialog alertDialog = builder.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(canceledOnTouchOutside); // 点击外部不会消失
		alertDialog.show();
	}
	
}
