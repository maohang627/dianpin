package com.thestore.microstore.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.proxy.Province;
import com.thestore.microstore.vo.proxy.ProvinceProxy;

/**
 * 
 * 省份处理
 * 
 * 2014-9-17 上午9:33:28
 */
public class ProvinceParser extends BaseParser<ProvinceProxy> {

	private static final String TAG = "vdian_ProvinceParser";

	private String defaultId;

	public ProvinceParser() {

	}

	public ProvinceParser(String defaultId) {
		this.defaultId = defaultId;
	}

	public ProvinceProxy parseJSON(String paramString) throws JSONException {

		ProvinceProxy result = new ProvinceProxy();

		CheckResponse response = checkResponse(result, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, "ProvinceParser 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, "ProvinceParser 调用接口出错.");

			return (ProvinceProxy) response.getBaseEntity();
		}

		List<Province> provinceList = new ArrayList<Province>();
		JSONArray jsonArrayData = new JSONObject(paramString).getJSONArray("data");

		int position = 0;

		for (int i = 0; i < jsonArrayData.length(); i++) {
			JSONObject remove = (JSONObject) jsonArrayData.get(i);

			Province province = new Province();
			province.setId(remove.getString("id"));

			if (defaultId != null && defaultId.equals(remove.getString("id"))) {
				position = i;
			}
			province.setValue(remove.getString("provinceName"));

			provinceList.add(province);
		}

		result.setPosition(position);
		result.setProvinceList(provinceList);

		return result;

	}

}
