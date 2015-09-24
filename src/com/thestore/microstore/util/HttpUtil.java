package com.thestore.microstore.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.thestore.microstore.LoadingActivity;
import com.thestore.microstore.MainActivity;
import com.thestore.microstore.OrderDetailActivity;
import com.thestore.microstore.R;
import com.thestore.microstore.ShopCartActivity;
import com.thestore.microstore.ShopCartNoneActivity;
import com.thestore.microstore.WebViewActivity;
import com.thestore.microstore.net.TheStoreHttpClient;
import com.thestore.microstore.vo.ClientInfoVO;
import com.thestore.microstore.vo.RequestVo;

public class HttpUtil {
	
	private static final String TAG = "HttpUtil";

	public static HttpClient createHttpClient() {
		HttpClient client = new TheStoreHttpClient();
		// setProxy(Config.application(), client);
		return client;
	}
	
	/**
	 * 通过URL获取HttpPost请求
	 * 
	 * @param url
	 * @return HttpPost
	 */
	private static HttpPost getHttpPost(String url) {
		HttpPost httpPost = new HttpPost(url);
		return httpPost;
	}

	/**
	 * 通过HttpPost获取HttpPonse对象
	 * 
	 * @param HttpPost
	 * @return httpPost
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static HttpResponse getHttpResponse(HttpPost httpPost)
			throws ClientProtocolException, IOException {
		HttpResponse response = new DefaultHttpClient().execute(httpPost);
		return response;
	}

	/**
	 * 设置公共Http Header
	 * @param mContext
	 * @param method
	 * @param requestDataMap
	 */
	private static void setCommonHeader(Context mContext, HttpRequestBase method, HashMap<String, String> requestDataMap) {
		try {
			ClientInfoVO client = new ClientInfoVO();
			Util util = new Util(mContext);		
			String ut = "";
			String vut = "";
			String provinceId = "1"; // 默认上海
			String loginMobileNum = "";
			Double longitude = 0.0; // 经度
	        Double latitude = 0.0; // 纬度  
	
			if(!(mContext instanceof LoadingActivity)){
				ut = CookiesUtil.getUT();
				vut = CookiesUtil.getVUT();
				provinceId = CookiesUtil.getProvinceId();
				loginMobileNum = CookiesUtil.getLoginMobileNum();
				String longitudeStr = CookiesUtil.getCookieFromVD("longitude");
				if(!TextUtils.isEmpty(longitudeStr)){
					longitude = Double.valueOf(longitudeStr);
				}
				String latitudeStr = CookiesUtil.getCookieFromVD("latitude");
				if(!TextUtils.isEmpty(latitudeStr)){
					latitude = Double.valueOf(latitudeStr);
				}
			}
	
			client.setClientAppVersion(util.getVersionName());
			client.setClientSystem(Config.TRADER_CLIENT_NAME);
			client.setClientVersion(Util.getAndroidSystemVersion());
			client.setDeviceCode(Config.getUUid(mContext));
			client.setLongitude(longitude);
			client.setLatitude(latitude);
			client.setTraderName(Config.TRADER_NAME);
			client.setChl(Util.getMetaData(mContext, "Channel_Name"));// 渠道标识
			client.setNettype(NetWorkUtil.getNetTypeName(mContext));
			client.setIaddr("1");//多机房接口位置 1--上海， 0--北京
			client.setInterfaceVersion(Config.INTERFACE_VERSION);
			client.setClientip(NetWorkUtil.getLocalIpAddress());
			client.setRsl(Util.getDisplayMetrics(mContext));// 分辨率
			client.setMbl(loginMobileNum);
			
			method.addHeader("charset", HTTP.UTF_8);
			
			StringBuilder requestDataBuilder = new StringBuilder();
			SharedPreferences sp = mContext.getSharedPreferences("yhd-micro-store", Context.MODE_PRIVATE);
			String keyword= sp.getString("mKeyword", "");
			long t = sp.getLong("mT", 0L);
			long ts = System.currentTimeMillis() + t; 
			if (requestDataMap == null) {
				requestDataMap = new HashMap<String, String>();
			}
			requestDataMap.put("timestamp", String.valueOf(ts));
			
			List<Map.Entry<String, String>> requestDataList = new ArrayList<Map.Entry<String, String>>(requestDataMap.entrySet());
			//根据key排序
			Collections.sort(requestDataList, new Comparator<Map.Entry<String, String>>() {   
			    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {      
			        return (o1.getKey()).toString().compareTo(o2.getKey());
			    }
			}); 
			//排序后取小写的key
			for (int i = 0; i < requestDataList.size(); i++) {
				Map.Entry<String, String> data  = requestDataList.get(i);
				if(requestDataBuilder.length() > 0){
					requestDataBuilder.append("&"+data.getKey().toLowerCase()+"="+data.getValue());
				}else{
					requestDataBuilder.append(data.getKey().toLowerCase()+"="+data.getValue());
				}
			}
		
			method.addHeader("timestamp", String.valueOf(ts));
	//		String signature = EncoderHandler.encode(Config.SHA1, requestDataBuilder.toString()+keyword);
			String signature = VDMessageDigest.apiMessageDigest(requestDataBuilder.toString()+keyword);
			method.addHeader("signature", signature);
			
			method.addHeader("clientInfo", DBHelper.getGson().toJson(client));
	
			method.addHeader("userToken", ut);
			method.addHeader("vuserToken", vut);
			method.addHeader("provinceId", provinceId);
			// GZIP数据压缩
			method.addHeader("accept-encoding", "gzip,deflate");
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
		}
	}
	
    private static int getShort(byte[] data) {
        return (int)((data[0]<<8) | data[1]&0xFF);
    }
	
	/**
	 * 将URL打包成HttpPost请求，发送，得到查询结果 网络异常 返回null
	 * @param vo
	 * @return obj
	 */
	public static Object post(RequestVo vo) {

		DefaultHttpClient client = new DefaultHttpClient();
		String url = vo.requestUrl;
		Logger.d(TAG, "Post " + url);
		HttpPost post = new HttpPost(url);
		setCommonHeader(vo.context, post, vo.requestDataMap);
		Object obj = null;
		try {
			if (vo.requestDataMap != null) {
				HashMap<String, String> map = vo.requestDataMap;
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
					pairList.add(pair);
				}
				HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
				post.setEntity(entity);
			}
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				
//				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		        //以下是解压缩的过程  
				InputStream is = response.getEntity().getContent();
				BufferedInputStream bis = new BufferedInputStream(is);
				bis.mark(2);
	            // 取前两个字节
	            byte[] header = new byte[2];
	            int rslt = bis.read(header);
	            // reset输入流到开始位置
	            bis.reset();
	            // 判断是否是GZIP格式
	            int headerData = getShort(header);
	            // Gzip 流 的前两个字节是 0x1f8b
	            if (rslt != -1 && headerData == 0x1f8b) {
	            	is = new GZIPInputStream(bis);
	            }else{
	            	is = bis;
	            }				
	            InputStreamReader reader = new InputStreamReader(is, "utf-8");
	            char[] data = new char[100];
	            int readSize;
	            StringBuffer sb = new StringBuffer();
	            while ((readSize = reader.read(data)) > 0) {
	                sb.append(data, 0, readSize);
	            }
	            String result = sb.toString();
	            bis.close();
	            reader.close();
				
				JSONObject jsonObject = new JSONObject(result);
				String rtnCode = jsonObject.getString("rtn_code");
				// token（UT）失效，除购物车其他界面要重新登录
				if (rtnCode != null && Config.RETURN_CODE_UT_FAIL.equals(rtnCode)) {
					if(vo.context instanceof ShopCartActivity){
						// 购物车列表界面
						// 清除无效token（UT），用sessionId读取本地购物车
						CookiesUtil.setUT(vo.context, "");
						Intent intent = new Intent();
						intent.setClass(vo.context, ShopCartActivity.class);
						((Activity) vo.context).startActivity(intent);
					}else if(vo.context instanceof OrderDetailActivity){
						// 订单详情界面
						String orderCode = "";
						if(vo.requestDataMap != null){
							orderCode = vo.requestDataMap.get("ordercode");
						}
						toH5GoLogin(vo.context, "toNative-OrderDetailActivity&orderCode="+orderCode);
					}else{
						// 购物流程相关界面
						toH5GoLogin(vo.context, "toNative-ShopCartActivity");
					}					
				}else{
//					StringBuffer buf = new StringBuffer(result);
//					
//					int i = 0;
//					while(true) {
//						
//						if(i + 50 >= buf.length()) {
//							
//							Log.i(TAG, buf.substring(i, buf.length()));
//							
//							break;
//						} else {
//							
//							Log.i(TAG, buf.substring(i, i + 50));
//						}
//						i = i + 50;
//					}
					
					Logger.d(TAG, result);
					try {
						obj = vo.jsonParser.parseJSON(result);
					} catch (JSONException e) {
						Logger.e(TAG, e.getLocalizedMessage(), e);
					}
					
					return obj;
				}
			} else {
				Logger.d(TAG, "状态CODE：" + response.getStatusLine().getStatusCode() + ", 请求地址：" + vo.requestUrl + ", 请求参数：" + vo.requestDataMap);
			}
		} catch (ClientProtocolException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		} catch (IOException e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			Logger.e(TAG, e.getLocalizedMessage(), e);
		}

 		return null;
	}
	
	/**
	 * 获得网络连接是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			Toast.makeText(context, R.string.net_error, Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	/**
	 * 跳转到H5登录页面
	 * @param context
	 * @param returnUrl 返回URL
	 */
	public static void toH5GoLogin(Context context, String returnUrl){
		Intent intent = new Intent();
		intent.setClass(context, WebViewActivity.class);
		intent.putExtra("toH5Url", Config.DEFAULT_WAPSERVLET_IP + "/myvdian/gologin.do?returnUrl="+Uri.encode(returnUrl));

		((Activity) context).startActivity(intent);
	}

}