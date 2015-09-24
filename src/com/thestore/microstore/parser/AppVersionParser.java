package com.thestore.microstore.parser;

import org.json.JSONException;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.vo.AppVersionVO;
import com.thestore.microstore.vo.commom.CheckResponse;

public class AppVersionParser extends BaseParser<AppVersionVO> {
	private static final String TAG = "AppVersionParser";
	
	@Override
	public AppVersionVO parseJSON(String paramString) throws JSONException {
		AppVersionVO appVersion = new AppVersionVO();
		CheckResponse response = checkResponse(appVersion, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, TAG+" 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, TAG+" 调用接口出错.");

			return (AppVersionVO) response.getBaseEntity();
		}
		
		JSONObject root = JSON.parseObject(paramString);
		String jsonData = root.getString("data");
		appVersion = JSON.parseObject(jsonData, AppVersionVO.class);
		appVersion.setRtnCode(root.getString("rtn_code"));
		appVersion.setRtnMsg(root.getString("rtn_msg"));
		
		return appVersion;		
	}

}
