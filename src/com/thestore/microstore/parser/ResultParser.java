package com.thestore.microstore.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.vo.Result;

public class ResultParser extends BaseParser<Result> {
	
	private static final String TAG = "vdian_ResultParser";

	@Override
	public Result parseJSON(String paramString) throws JSONException {
		
		if(paramString == null) {
			Log.i(TAG, "ResultParser, 返回结果为空.");
			return null;
		}
		
		Result result = new Result();
		JSONObject jsonData = new JSONObject(paramString);
		
		result.setRtnCode(jsonData.getString("rtn_code"));
		result.setRtnMsg(jsonData.getString("rtn_msg"));
		if(!jsonData.isNull("data")){
			result.setData(jsonData.get("data"));
		}
		
		return result;
	}

}
