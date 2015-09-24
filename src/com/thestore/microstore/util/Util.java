/**
 * 
 */
package com.thestore.microstore.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.thestore.microstore.VDApplication;
import com.thestore.microstore.parser.ResultParser;
import com.thestore.microstore.vo.RequestVo;
import com.thestore.microstore.vo.Result;

import junit.framework.Assert;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Util {
	private static final String TAG = "Util";
    private Activity      activity;

    private final Context mContext;
    
    private static Toast mToast;

    public Util(Activity activity) {
        this.activity = activity;
        this.mContext = activity;
    }

    public Util(Context context) {
        mContext = context;
    }

    /**
     * FunName: Util.java Description: 判断EditText是否为空 Author: tianjsh Create
     * Date: 2010/10/25 13:15:49
     * 
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().equals("")
            && editText.getText().toString().trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 用来判断服务是否后台运行
     * 
     * @param context
     * @param className
     *            判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public boolean isServiceRunning(Context mContext, String className) {
        boolean IsRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
            .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
            .getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                IsRunning = true;
                break;
            }
        }
        return IsRunning;
    }

    /**
     * 
     * FunName: Util.java Description: 当输入框为空时，设置其错误显示 Author: tianjsh Create
     * Date: 2010/10/25 13:40:55
     * 
     * @param editText
     * @param resID
     */
    public static void setEmptyError(EditText editText, int resID) {
        if (isEmpty(editText)) {
            setErrorMessage(editText, resID);
        }
    }

    public static void setErrorMessage(EditText editText, int resId) {
        editText.setError(editText.getContext().getString(resId));
    }

    public static String getEditString(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static void showInputMethod(View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.RESULT_SHOWN);

    }

    /**
     * 
     * FunName: Util.java Description: 隐藏键盘 Author: tianjsh Create Date:
     * 2010/12/16 13:43:56 update at 2012-09-24
     */
    public void hindInputMethod() {
        if (activity == null) {
            return;
        }
        View focus = activity.getCurrentFocus();
        if (focus != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(focus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static boolean isExistSdCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean isAuto() {
        return android.os.Build.BRAND.toLowerCase().equals("qcom");
    }

    public static boolean isG8() {
        return android.os.Build.BOARD.equals("buzz");
    }

    public static float getHeightPixels(Activity activity) {
        DisplayMetrics displayMetrics = activity.getApplicationContext().getResources()
            .getDisplayMetrics();
        float heightPixels = displayMetrics.heightPixels;
        return heightPixels;
    }

    /**
     * (时间格式化 由于服务器返回的都是CST时间,格式化将时间强制设置到-6区)已取消强制时区,接口返回正常
     * 
     * @param date
     *            时间
     * @param dateFormat
     *            格式字符串
     * @return
     */
    public static String getDateString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        // sdf.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));
        return sdf.format(date);
    }

    public static Date getLocaleDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date d = null;
        try {
            d = sdf.parse(getDateString(date, dateFormat));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

 
    /**
     * 小数点后面有两位除0之外的数字，保留两位小数， 否则保留一位小数
     * 
     * @param 价格
     * @return 价格
     */
    public static Double getDecimalPoint(Double d) {
        if (d == null) {
            return Double.valueOf(0.0);
        }
        String priceStr = d + "";
        if (priceStr.lastIndexOf(".") != -1) {
            // 有小数点
            priceStr = priceStr.substring(priceStr.lastIndexOf(".") + 1, priceStr.length());
            if (priceStr.length() > 1) {
                // 小数点后面有两位除0之外的数字，保留两位小数
                return getDecimalPoint(d, "0.00");
            }
        }
        return getDecimalPoint(d, "0.0");
    }

    public static Double getDecimalPoint(Double d, String format) {
        if (d == null) {
            return Double.valueOf(0.0);
        }
        return Double.parseDouble(new DecimalFormat(format).format(d));
    }

    public static Float getDecimalPoint(Float f, String format) {
        if (f == null) {
            return Float.valueOf(0.0f);
        }
        return Float.parseFloat(new DecimalFormat(format).format(f));
    }

    public static CharSequence getDoubleToString(Double d) {
        String price = d.toString();
        int pos = price.lastIndexOf(".");
        return price.subSequence(0, pos);
    }

    /**
     * 
     * @param d
     * @return
     */
    public static String getPriceString(Double price) {
        return "￥" + getDecimalPoint(price);
    }

    public static String getPriceString(Double price, String format) {
        return "￥" + getDecimalPoint(price, format);
    }

     public static String getNullString(String string) {
        if (string == null) {
            return "";
        } else {
            return string;
        }
    }

    /* ==========================阿拉伯数字转大写中文================== */
    private static String[] cnDigiStr = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八",
            "九"                      };

    private static String[] cnDiviStr = new String[] { "", "十", "百", "千", "万", "十", "百", "千", "亿",
            "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千", "万", "十", "百", "千" };

    /**
     * 阿拉伯数字转大写中文
     * 
     * @param intVal
     * @return
     */
    public static String positiveIntegerToCnStr(int intVal) {
        String numStr = intVal + "";
        String cnStr = "";
        boolean isLastZero = false;
        boolean hasValue = false;
        int len, n;
        len = numStr.length();
        if (len > 15)
            return "";
        for (int i = len - 1; i >= 0; i--) {
            if (numStr.charAt(len - i - 1) == ' ')
                continue;
            n = numStr.charAt(len - i - 1) - '0';
            if (n < 0 || n > 9)
                return "";

            if (n != 0) {
                if (isLastZero)
                    cnStr += cnDigiStr[0];
                if (!(n == 1 && (i % 4) == 1 && i == len - 1))
                    cnStr += cnDigiStr[n];
                cnStr += cnDiviStr[i];
                hasValue = true;
            } else {
                if ((i % 8) == 0 || ((i % 8) == 4 && hasValue))
                    cnStr += cnDiviStr[i];
            }
            if (i % 8 == 0)
                hasValue = false;
            isLastZero = (n == 0) && (i % 4 != 0);
        }

        if (cnStr.length() == 0)
            return cnDigiStr[0];
        return cnStr;
    }

    /***
     * 递归删除文件及文件夹
     * 
     * @param file
     */
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    /***
     * 递归删除文件及文件夹，保留根部文件夹
     * 
     * @param file
     */
    public static void deleteFromRoot(File file) {

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
        }
    }

    /**
     * 判断是否联网
     * 
     * @return true已联网 false未联网
     */
    public static boolean isConnectNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 判断是否连接wifi
     * 
     * @param context
     * @return
     */
    public static boolean isConnectWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到安卓系统版本号
     * 
     * @return clientVersion
     */
    public static String getAndroidSystemVersion() {
        return android.os.Build.MODEL + "," + android.os.Build.VERSION.SDK + ","
               + android.os.Build.VERSION.RELEASE;
    }
    
	/**
	 * 获取软件版本号名称
	 * @return versionName
	 */
	public String getVersionName()
	{
		String versionName = "";
		try
		{
			// 获取软件版本号名称，对应AndroidManifest.xml下android:versionName
			versionName = mContext.getPackageManager().getPackageInfo(Config.APP_PACKAGE, 0).versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionName;
	}

    /**
     * @return 0: SIM卡良好 1: 无SIM卡 2: SIM卡被锁定或未知的状态
     */
    public static int getSimState(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context
            .getSystemService(Context.TELEPHONY_SERVICE);

        if (telMgr.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return 0;
        } else if (telMgr.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 设置文本框不为空时显示清除按钮
     * 
     * @param editText
     * @param button
     */
    public static void cleanEditText(final EditText editText, final Button button) {
        if (!Util.isEmpty(editText)) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    editText.setText("");
                }
            });
        } else {
            button.setVisibility(View.GONE);
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Util.isEmpty(editText)) {
                    button.setVisibility(View.VISIBLE);
                    button.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            editText.setText("");
                        }
                    });
                } else {
                    button.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editText.addTextChangedListener(textWatcher);
    }

    private static Pattern pattern = Pattern.compile("^[A-Za-z]+$");

    // 获得汉语拼音首字母
    public static String getAlpha(String str) {
        if (str == null) {
            return "#";
        }

        if (str.trim().length() == 0) {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        String cStr = Character.toString(c);
        // 正则表达式，判断首字母是否是英文字母
        if (pattern.matcher(cStr).matches()) {
            return cStr.toUpperCase();
        } else {
            return "#";
        }
    }

    public String getConnectionType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            int type = networkInfo.getType();
            int subType = networkInfo.getSubtype();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return "wifi";
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                switch (subType) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return "2g"; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return "2g"; // ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return "2g"; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return "2g"; // ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return "2g"; // ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return "2g"; // ~ 100 kbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        return "3g"; // ~ 2-14 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        return "3g"; // ~ 700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        return "3g"; // ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return "3g"; // ~ 400-7000 kbps
                        // NOT AVAILABLE YET IN API LEVEL 7
                        // case TelephonyManager.NETWORK_TYPE_EHRPD:
                        // return true; // ~ 1-2 Mbps
                        // case Connectivity.NETWORK_TYPE_EVDO_B:
                        // return true; // ~ 5 Mbps
                        // case Connectivity.NETWORK_TYPE_HSPAP:
                        // return true; // ~ 10-20 Mbps
                        // case Connectivity.NETWORK_TYPE_IDEN:
                        // return false; // ~25 kbps
                        // case Connectivity.NETWORK_TYPE_LTE:
                        // return true; // ~ 10+ Mbps
                        // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        return "";
                    default:
                        return "";
                }
            } else {
                return "";
            }
        }
        return "";
    }

    public static String getImg160x160(String url) {
        if (url.endsWith(".jpg")) {
            url = url.replace(".jpg", "_160x160.jpg");
        }
        return url;
    }

    public static String getImg90x90(String url) {
        if (url.endsWith(".jpg")) {
            url = url.replace(".jpg", "_90x90.jpg");
        }
        return url;
    }
    
    public static String getImg40x40(String url) {
        if (url.endsWith(".jpg")) {
            url = url.replace(".jpg", "_40x40.jpg");
        }
        return url;
    }

    /**
     * 图片压缩
     */
    public static void imageCompress(String path, Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bos = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            bos = new BufferedOutputStream(fileOutputStream);
            if (null != bitmap) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bos) {
                    bos.flush();
                    bos.close();
                }
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 通过毫米数查询剩余时间
     * 
     * @param 毫秒数
     * @return xxx天xx小时
     */
    public static String getIntervalUpdateTime(long intervalTime) {
        StringBuilder result = new StringBuilder();
        long interval = intervalTime / 1000;
        final long day = 24 * 60 * 60;
        final long hour = 60 * 60;
        final long minute = 60;
        int detailDay = 0;
        int detailHour = 0;
        int detailMinute = 0;
        if (interval >= day) {
            detailDay = (int) (interval / day);
            interval = interval - detailDay * day;
        }
        if (interval >= hour) {
            detailHour = (int) (interval / hour);
            interval = interval - hour * detailHour;
        }
        if (interval >= minute) {
            detailMinute = (int) (interval / minute);
            interval = interval - detailMinute * minute;
        }
        result.setLength(0);
        if (detailDay > 0) {
            result.append(detailDay);
            result.append("天");
        }
        if (detailHour > 0) {
            result.append(detailHour);
            result.append("小时");
        }
        // if (detailMinute > 0) {
        // result.append(detailMinute);
        // result.append("分");
        // }
        // if (detailSecond > 0) {
        // result.append(detailSecond);
        // result.append("秒");
        // }
        return result.toString();
    }

    /**
     * 通过毫米数查询剩余时间 超过24小时，显示“仅XX天” 不足24小时，显示“仅XX小时” 不足1小时，显示“仅1小时”
     * 
     * @param 毫秒数
     * @return xxx天xx小时
     */
    public static String getIntervalUpdateTimeFlash(long intervalTime) {
        StringBuilder result = new StringBuilder();
        long interval = intervalTime / 1000;
        final long day = 24 * 60 * 60;
        final long hour = 60 * 60;
        final long minute = 60;
        int detailDay = 0;
        int detailHour = 0;
        int detailMinute = 0;
        if (interval >= day) {
            detailDay = (int) (interval / day);
            interval = interval - detailDay * day;
        }
        if (interval >= hour) {
            detailHour = (int) (interval / hour);
            interval = interval - hour * detailHour;
        }
        if (interval >= minute) {
            detailMinute = (int) (interval / minute);
            interval = interval - detailMinute * minute;
        }
        result.setLength(0);
        if (detailDay > 0) {
            result.append(detailDay);
            result.append("天");
        } else {
            if (detailHour >= 1) {
                result.append(detailHour);
                result.append("小时");
            } else {
                result.append("1");
                result.append("小时");
            }
        }
        return result.toString();
    }

    /**
     * 剩余时间大于1天时“仅”需要改为“仅剩：”，小于1天时“仅”需要改为“剩余：”
     * 
     */
    public static String getIntervalUpdateTimeBrand(long intervalTime) {
        StringBuilder result = new StringBuilder();
        long interval = intervalTime / 1000;
        final long day = 24 * 60 * 60;
        final long hour = 60 * 60;
        final long minute = 60;
        int detailDay = 0;
        int detailHour = 0;
        int detailMinute = 0;
        if (interval >= day) {
            detailDay = (int) (interval / day);
            interval = interval - detailDay * day;
        }
        if (interval >= hour) {
            detailHour = (int) (interval / hour);
            interval = interval - hour * detailHour;
        }
        if (interval >= minute) {
            detailMinute = (int) (interval / minute);
            interval = interval - detailMinute * minute;
        }
        result.setLength(0);
        if (detailDay > 0) {
            result.append("仅剩:");
            result.append(detailDay);
            result.append("天");
        } else {
            result.append("剩余:");
            if (detailHour >= 1) {
                result.append(detailHour);
                result.append("小时");
            } else {
                result.append("1");
                result.append("小时");
            }
        }
        return result.toString();
    }
    
    /**
     * 获取meta-data值
     * @param context
     * @param key
     * @return
     */
	public static String getMetaData(Context context, String key) {

		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * 获取屏幕分辨率（单位是像素）
	 * @return
	 */
	public static String getDisplayMetrics(Context context){		
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        
        return String.valueOf(metrics.widthPixels)+"*"+String.valueOf(metrics.heightPixels);
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
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
	
	public static byte[] getHtmlByteArray(final String url) {
		 URL htmlUrl = null;     
		 InputStream inStream = null;     
		 try {         
			 htmlUrl = new URL(url);         
			 URLConnection connection = htmlUrl.openConnection();         
			 HttpURLConnection httpConnection = (HttpURLConnection)connection;         
			 int responseCode = httpConnection.getResponseCode();         
			 if(responseCode == HttpURLConnection.HTTP_OK){             
				 inStream = httpConnection.getInputStream();         
			  }     
			 } catch (MalformedURLException e) {               
				 e.printStackTrace();     
			 } catch (IOException e) {              
				e.printStackTrace();    
		  } 
		byte[] data = inputStreamToByte(inStream);

		return data;
	}
	
	public static byte[] inputStreamToByte(InputStream is) {
		try{
			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			bytestream.close();
			return imgdata;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			Log.i(TAG, "readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

		if(offset <0){
			Log.e(TAG, "readFromFile invalid offset:" + offset);
			return null;
		}
		if(len <=0 ){
			Log.e(TAG, "readFromFile invalid len:" + len);
			return null;
		}
		if(offset + len > (int) file.length()){
			Log.e(TAG, "readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // 创建合适文件大小的数组
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}
	
	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}
	
	/**
	 * 打印消息并且用Toast显示消息
	 * 
	 * @param activity
	 * @param message
	 * @param logLevel
	 *            填d, w, e分别代表debug, warn, error; 默认是debug
	 */
	public static final void toastMessage(final Activity activity,
			final String message, String logLevel) {
		if ("w".equals(logLevel)) {
			Log.w("sdkDemo", message);
		} else if ("e".equals(logLevel)) {
			Log.e("sdkDemo", message);
		} else {
			Log.d("sdkDemo", message);
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast != null) {
					mToast.cancel();
					mToast = null;
				}
				mToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
				mToast.show();
			}
		});
	}

	/**
	 * 打印消息并且用Toast显示消息
	 * 
	 * @param activity
	 * @param message
	 * @param logLevel
	 *            填d, w, e分别代表debug, warn, error; 默认是debug
	 */
	public static final void toastMessage(final Activity activity,
			final String message) {
		toastMessage(activity, message, null);
	}
	
    /**
     * APP分享数据统计
     * @param shareId
     * @param shareUrl
     * @param sharePlatform
     */
	public void recordShareData(String shareId, String shareUrl, String sharePlatform){
    	if(mContext != null){
        	try {
    	    	RequestVo requestVo = new RequestVo();
    			requestVo.requestUrl = Config.SERVLET_URL_RECORD_SHAREDATA;
    			requestVo.context = mContext;
    			HashMap<String, String> postParamMap = new HashMap<String, String>();
    			postParamMap.put("sid", shareId);
    			postParamMap.put("surl", URLEncoder.encode(shareUrl, "utf-8"));
    			postParamMap.put("splotform", sharePlatform);
    			requestVo.requestDataMap = postParamMap;
    			requestVo.jsonParser = new ResultParser();
    			Result result = (Result) HttpUtil.post(requestVo);
    			if(result != null && "0".equals(result.getRtnCode())) {
    				VDApplication.SHARE_FLAG = false;
        			VDApplication.SHARE_ID = null;
        			VDApplication.SHARE_URL = null;
        			VDApplication.SHARE_PLATFORM = null;
    			}
    		} catch (Exception e) {
    			Logger.d(TAG, e.getMessage());
    		}
    	}
    }

}
