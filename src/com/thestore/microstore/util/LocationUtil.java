package com.thestore.microstore.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.thestore.main.core.log.Lg;
import com.thestore.microstore.R;

/**
 * 定位公共类
 * 
 */
public class LocationUtil {

	private Context context;
	private ArrayList<CellIDInfo> cellID;

	private LocationManager mLocationManager;

	private LocationClient mLocationClient;
	private BDLocationListener myListener;

	private Handler mHandler;

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public LocationUtil(Context context) {
		this.context = context.getApplicationContext();

		mLocationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		mLocationClient = new LocationClient(this.context);
		myListener = new MyLocationListenner();

		LocationClientOption option = new LocationClientOption();
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先,
																// 默认是gps优先
		option.setOpenGps(true);// 不使用GPS定位,仅使用网络
		option.setAddrType("all");// 需要详细地址 "detail"
		option.setCoorType("gcj02");
		option.setScanSpan(5000);
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(false); // 是否需要POI的电话和地址等详细信息
		option.setServiceName("com.baidu.location.service_v2.9");
		mLocationClient.setLocOption(option);
	}

	/**
	 * 停止定位
	 */
	public void stopLocation() {
		if (mLocationClient != null) {
			if (myListener != null) {// 取消监听
				mLocationClient.unRegisterLocationListener(myListener);
			}
			if (mLocationClient.isStarted()) {
				mLocationClient.stop();
			}
			mLocationClient = null;
		}
	}

	/**
	 * 基站获取经纬度
	 * 
	 * @return
	 */
	private Location getGPRSLocation() {
		HttpClient client = HttpUtil.createHttpClient();
		HttpPost post = new HttpPost("http://www.google.com/loc/json");
		JSONObject holder = new JSONObject();
		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("home_mobile_country_code",
					cellID.get(0).mobileCountryCode);
			holder.put("home_mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			holder.put("radio_type", cellID.get(0).radioType);
			holder.put("request_address", true);
			if ("460".equals(cellID.get(0).mobileCountryCode)) {
				holder.put("address_language", "zh_CN");
			} else {
				holder.put("address_language", "en_US");
			}
			JSONObject data, current_data;
			JSONArray array = new JSONArray();
			current_data = new JSONObject();
			current_data.put("cell_id", cellID.get(0).cellId);
			current_data.put("location_area_code",
					cellID.get(0).locationAreaCode);
			current_data.put("mobile_country_code",
					cellID.get(0).mobileCountryCode);
			current_data.put("mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			current_data.put("age", 0);
			array.put(current_data);
			if (cellID.size() > 2) {
				for (int i = 1; i < cellID.size(); i++) {
					data = new JSONObject();
					data.put("cell_id", cellID.get(i).cellId);
					data.put("location_area_code",
							cellID.get(i).locationAreaCode);
					data.put("mobile_country_code",
							cellID.get(i).mobileCountryCode);
					data.put("mobile_network_code",
							cellID.get(i).mobileNetworkCode);
					data.put("age", 0);
					array.put(data);
				}
			}
			holder.put("cell_towers", array);
			StringEntity se = new StringEntity(holder.toString());
			post.setEntity(se);
			HttpResponse resp = client.execute(post);
			HttpEntity entity = resp.getEntity();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			StringBuffer sb = new StringBuffer();
			String result = br.readLine();
			while (result != null) {
				sb.append(result);
				result = br.readLine();
			}
			if (sb.length() <= 1) {
				return null;
			}
			data = new JSONObject(sb.toString());
			data = (JSONObject) data.get("location");

			Location loc = new Location(LocationManager.NETWORK_PROVIDER);
			loc.setLatitude((Double) data.get("latitude"));
			loc.setLongitude((Double) data.get("longitude"));
			loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
			loc.setTime(GetUTCTime());
			return loc;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * GPS获取经纬度
	 * 
	 * @return
	 */
	private Location getGPSLocation() {
		// 查找到服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

		String provider = mLocationManager.getBestProvider(criteria, true); // 获取GPS信息
		Location location = null;
		if (provider != null) {
			location = mLocationManager.getLastKnownLocation(provider); // 通过GPS获取位置
		}
		// updateToNewLocation(location);
		// // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
		// locationManager.requestLocationUpdates(provider, 100 * 1000, 500,
		// locationListener);
		return location;
	}

	/**
	 * 获取Location
	 * 
	 * @return
	 */
	public Location getLocation() {
		setCellID();
		Location loc = null;
		if (cellID.size() > 0) {
			loc = getGPRSLocation();
		} else {
			loc = getGPSLocation();
		}
		if (loc != null) {
			Config.setLatitude(loc.getLatitude());
			Lg.e("xxxxxxxxx", loc.getLatitude(), loc.getLongitude());
			Config.setLongitude(loc.getLongitude());
		}
		return loc;
	}

	/**
	 * 详细地址
	 * 
	 * @author xuhui3
	 * 
	 */
	public static final class AddressDetail {

		private String country = "";
		private String countryCode = "";
		private String region = "";
		private String city = "";
		private String street = "";
		private String streetNumber = "";

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getStreetNumber() {
			return streetNumber;
		}

		public void setStreetNumber(String streetNumber) {
			this.streetNumber = streetNumber;
		}

		@Override
		public String toString() {
			return country + countryCode + region + city + street
					+ streetNumber;
		};
	};

	/**
	 * 设置CellID
	 */
	private void setCellID() {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = tm.getNetworkType();
		cellID = new ArrayList<CellIDInfo>();
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A
				|| type == TelephonyManager.NETWORK_TYPE_CDMA
				|| type == TelephonyManager.NETWORK_TYPE_1xRTT) {
			CdmaCellLocation location = (CdmaCellLocation) tm.getCellLocation();
			int cellIDs = 0;
			int networkID = 0;
			StringBuilder nsb = new StringBuilder();
			CellIDInfo info = new CellIDInfo();
			if (location != null) {
				cellIDs = location.getBaseStationId();
				networkID = location.getNetworkId();
				nsb.append(location.getSystemId());
			}
			info.cellId = cellIDs;
			info.locationAreaCode = networkID; // ok
			info.mobileNetworkCode = nsb.toString();
			info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
			info.radioType = "cdma";
			cellID.add(info);
		}
		// 移动2G卡 + CMCC + 2
		// type = NETWORK_TYPE_EDGE
		else if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			int cellIDs = 0;
			int lac = 0;
			CellIDInfo info = new CellIDInfo();
			if (location != null) {
				cellIDs = location.getCid();
				lac = location.getLac();
			}
			info.cellId = cellIDs;
			info.locationAreaCode = lac;
			info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);
			info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
			info.radioType = "gsm";
			cellID.add(info);
		}
		// 联通的2G经过测试 China Unicom 1 NETWORK_TYPE_GPRS
		else if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			int cellIDs = 0;
			int lac = 0;
			CellIDInfo info = new CellIDInfo();
			if (location != null) {
				cellIDs = location.getCid();
				lac = location.getLac();
			}
			info.cellId = cellIDs;
			info.locationAreaCode = lac;
			// 经过测试，获取联通数据以下两行必须去掉，否则会出现错误，错误类型为JSON Parsing Error
			// info.mobileNetworkCode = tm.getNetworkOperator().substring(3, 5);
			// info.mobileCountryCode = tm.getNetworkOperator().substring(0, 3);
			info.radioType = "gsm";
			cellID.add(info);
		}
	}

	/**
	 * 获取UTC时间
	 * 
	 * @return
	 */
	private long GetUTCTime() {
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTimeInMillis();
	}

	/**
	 * CellIDInfo
	 * 
	 * @author xuhui3
	 * 
	 */
	public class CellIDInfo {

		public int cellId;
		public String mobileCountryCode;
		public String mobileNetworkCode;
		public int locationAreaCode;
		public String radioType;

		public CellIDInfo() {
		}
	}

	/**
	 * 根据地址返回经纬度
	 * 
	 * @param addr
	 * @return 返回经纬度数据, latLng[0]经度,latLng[1]维度
	 */
	public String[] getCoordinate(String addr) {
		String[] latLng = new String[2];
		String address = null;
		try {
			address = java.net.URLEncoder.encode(addr, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		;
		String output = "csv";
		// 密钥可以随便写一个key=abc
		String key = "abc";
		String url = "http://maps.google.com/maps/geo?q=" + address
				+ "&output=" + output + "&key=" + key;
		URL googleMapURL = null;
		URLConnection httpsConn = null;
		// 进行转码
		BufferedReader br = null;
		try {
			googleMapURL = new URL(url);
			httpsConn = (URLConnection) googleMapURL.openConnection();
			if (httpsConn != null) {
				InputStreamReader insr = new InputStreamReader(
						httpsConn.getInputStream(), "UTF-8");
				br = new BufferedReader(insr);
				String data = null;
				if ((data = br.readLine()) != null) {
					String[] retList = data.split(",");
					/*
					 * String latitude = retList[2]; String longitude =
					 * retList[3];
					 */
					if (retList.length > 2 && ("200".equals(retList[0]))) {
						latLng[0] = retList[3];
						latLng[1] = retList[2];
					}
				}
				insr.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
		return latLng;
	}

	public String[] reversAddressToCoordinate(String addr, String cty) {
		String[] coordinates = new String[2];
		try {
			String address = URLEncoder.encode(addr, "UTF-8");
			String city = URLEncoder.encode(cty, "UTF-8");
			String domain = "http://api.map.baidu.com/geocoder/v2/?";
			// TODO replace this key to company's api key.
			String ak = "0Fe5da3a85faf6ae1f6798267688ae93";// xz
			String output = "json";

			StringBuffer sb = new StringBuffer();
			sb.append(domain).append("ak=").append(ak).append("&")
					.append("output=").append(output).append("&")
					.append("address=").append(address).append("&")
					.append("city=").append(city);

			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(sb.toString());
			HttpResponse response = client.execute(get);
			if (response != null && response.getStatusLine() != null
					&& response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream content = entity.getContent();
					BufferedInputStream bis = new BufferedInputStream(content);
					StringBuffer line = new StringBuffer();
					byte[] buffer = new byte[1024];
					int count = 0;
					while ((count = bis.read(buffer)) != -1) {
						line.append(new String(buffer, 0, count));
					}
					JSONObject json = new JSONObject(line.toString());
					int status = json.getInt("status");
					if (status == 0) {
						JSONObject locObj = json.getJSONObject("location");
						double latitude = locObj.getDouble("lat");
						double longitude = locObj.getDouble("lng");
						coordinates[0] = "" + latitude;
						coordinates[1] = "" + longitude;
					}
				}
			}

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return coordinates;
	}

	/**
	 * 从百度定位的结果中获取地址信息
	 * 
	 * @param location
	 *            百度定位的Location
	 */
	private void getAddressDetailFromBDLocation(BDLocation location) {
		Config.setLongitude(location.getLongitude());
		Config.setLatitude(location.getLatitude());
		Lg.e("xxxxxxxxx", location.getLatitude(), location.getLongitude());
		// add by huangjun 2013/10/31 有个空指针错误
		stopLocation();
		// add end
		if (mHandler != null) { // 发消息
			Message msg = new Message();
			msg.what = R.id.baidu_location_callback;
			String locationStr = location.getProvince();

			Lg.d("location.mServerString", location.mServerString);
			Lg.d("location.getAddrStr", location.getAddrStr());
			Lg.d("location.getProvince", location.getProvince());
			Lg.d("location.getCity", location.getCity());
			Lg.d("location.getDistrict", location.getDistrict());
			Lg.d("location.getAltitude", location.getAltitude());
			Lg.d("location.getLatitude", location.getLatitude());
			Lg.d("location.getLongitude", location.getLongitude());
			Lg.d("location.getCityCode", location.getCityCode());
			Lg.d("location.getCoorType", location.getCoorType());
			Lg.d("location.getDerect", location.getDerect());
			Lg.d("location.getLocType", location.getLocType());
			Config.setLatitude(location.getLatitude());
			Config.setLongitude(location.getLongitude());
			Bundle data = new Bundle();
			data.putString("cityName", location.getCity());
			msg.setData(data);
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 百度定位监听类
	 * 
	 * @author xuhui3
	 * 
	 */
	private class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// Lg.d("onReceiveLocation");

			if (location == null) {
				return;
			}

			// 只监听网络定位的
			if (location.getLocType() == BDLocation.TypeNetWorkLocation
					|| location.getLocType() == BDLocation.TypeGpsLocation) {
				getAddressDetailFromBDLocation(location);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			// 这里不需要POI信息,所以暂不实现
			Lg.d("这里不需要POI信息,所以暂不实现", poiLocation);
		}
	}

}
