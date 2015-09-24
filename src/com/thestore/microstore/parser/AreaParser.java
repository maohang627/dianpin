package com.thestore.microstore.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.proxy.Area;
import com.thestore.microstore.vo.proxy.AreaProxy;

/**
 * 
 * 城市处理
 * 
 * 2014-9-17 上午9:33:28
 */
public class AreaParser extends BaseParser<AreaProxy> {

	private static final String TAG = "vdian_AreaParser";

	private String defaultId;

	public AreaParser() {

	}

	public AreaParser(String defaultId) {
		this.defaultId = defaultId;
	}

	public AreaProxy parseJSON(String paramString) throws JSONException {

		AreaProxy result = new AreaProxy();

		CheckResponse response = checkResponse(result, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, "AreaParser 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, "AreaParser 调用接口出错.");

			return (AreaProxy) response.getBaseEntity();
		}

		List<Area> areaList = new ArrayList<Area>();
		JSONArray jsonArrayData = new JSONObject(paramString).getJSONArray("data");

		int position = 0;

		for (int i = 0; i < jsonArrayData.length(); i++) {
			JSONObject remove = (JSONObject) jsonArrayData.get(i);

			Area area = new Area();
			area.setId(remove.getString("id"));

			if (defaultId != null && defaultId.equals(remove.getString("id"))) {
				position = i;
			}

			area.setValue(remove.getString("countyName"));
			area.setPostCode(remove.getString("postCode"));

			areaList.add(area);
		}

		result.setPosition(position);
		result.setAreaList(areaList);

		return result;

	}

}
