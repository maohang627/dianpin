package com.thestore.microstore.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.proxy.City;
import com.thestore.microstore.vo.proxy.CityProxy;

/**
 * 
 * 城市处理
 * 
 * 2014-9-17 上午9:33:28
 */
public class CityParser extends BaseParser<CityProxy> {

	private static final String TAG = "vdian_CityParser";

	private String defaultId;

	public CityParser() {

	}

	public CityParser(String defaultId) {
		this.defaultId = defaultId;
	}

	public CityProxy parseJSON(String paramString) throws JSONException {

		CityProxy result = new CityProxy();

		CheckResponse response = checkResponse(result, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, "CityParser 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, "CityParser 调用接口出错.");

			return (CityProxy) response.getBaseEntity();
		}

		int position = 0;

		List<City> cityList = new ArrayList<City>();
		JSONArray jsonArrayData = new JSONObject(paramString).getJSONArray("data");

		for (int i = 0; i < jsonArrayData.length(); i++) {
			JSONObject remove = (JSONObject) jsonArrayData.get(i);

			City city = new City();
			city.setId(remove.getString("id"));

			if (defaultId != null && remove.getString("id").equals(defaultId)) {
				position = i;
			}

			city.setValue(remove.getString("cityName"));

			cityList.add(city);
		}

		result.setPosition(position);
		result.setCityList(cityList);

		return result;

	}

}
