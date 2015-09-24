package com.thestore.microstore.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.vo.SummitOrderOutput;

public class SummitOrderOutputParser extends BaseParser<SummitOrderOutput> {
	
	private static final String TAG = "vdian_SummitOrderOutputParser";

	@Override
	public SummitOrderOutput parseJSON(String paramString) throws JSONException {
		
		if(paramString == null) {
			Log.i(TAG, "SummitOrderOutputParser, 返回结果为空.");
			return null;
		}
		
		SummitOrderOutput result = new SummitOrderOutput();
		JSONObject jsonData = new JSONObject(paramString);
		
		result.setOrderCode(jsonData.getJSONObject("data").getString("orderCode"));
		
		return result;
	}

}
