package com.thestore.microstore.parser;

import org.json.JSONException;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.DES3;
import com.thestore.microstore.vo.VdianTimeVO;
import com.thestore.microstore.vo.commom.CheckResponse;

public class VdianTimeParser extends BaseParser<VdianTimeVO> {
	private static final String TAG = "VdianTimeParser";

	@Override
	public VdianTimeVO parseJSON(String paramString) throws JSONException {
		
		VdianTimeVO vdianTime = new VdianTimeVO();
		CheckResponse response = checkResponse(vdianTime, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, TAG+" 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, TAG+" 调用接口出错.");

			return (VdianTimeVO) response.getBaseEntity();
		}

		JSONObject root = JSON.parseObject(paramString);
		JSONObject jsonData = root.getJSONObject("data");
		vdianTime.setRtnCode(root.getString("rtn_code"));
		vdianTime.setRtnMsg(root.getString("rtn_msg"));
		try {
			String keyword = DES3.decode(jsonData.getString("keyword"));
			vdianTime.setKeyword(keyword);
			String timestamp = DES3.decode(jsonData.getString("timestamp"));
			vdianTime.setTimestamp(Long.parseLong(timestamp));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		return vdianTime;
	}
}
