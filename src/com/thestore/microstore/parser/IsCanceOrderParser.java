package com.thestore.microstore.parser;

import org.json.JSONException;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.order.CanceOrder;

public class IsCanceOrderParser  extends BaseParser<CanceOrder>{
	
	private static final String TAG = "IsCanceOrderParser";

	@Override
	public CanceOrder parseJSON(String paramString) throws JSONException {
		
		CanceOrder result =new CanceOrder();
		CheckResponse response = checkResponse(result, paramString);
		if(response.getResult() == -1) {
			Log.e(TAG, "IsCanceOrderParser 返回空.");
			
			return null;
		} else if(response.getResult() == 1) {
			Log.e(TAG, "IsCanceOrderParser 调用接口出错.");
			
			return (CanceOrder) response.getBaseEntity();
		}
		
		
		JSONObject root = JSON.parseObject(paramString);
		JSONObject jsonData = root.getJSONObject("data");
		result.setRtnCode(root.getString("rtn_code"));
		result.setRtnMsg(root.getString("rtn_msg"));
		result.setIsCancel(jsonData.getString("isCancel"));
		result.setReason(jsonData.getJSONObject("reason"));
		result.setCode(jsonData.getString("code"));
		result.setReasonIds(jsonData.getString("reasonIds"));
		return result;
	}

}
