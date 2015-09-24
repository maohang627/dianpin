package com.thestore.microstore.parser;

import org.json.JSONException;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.vo.VdianConfigVO;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.shoppingcart.ShoppingCart;

public class VdianConfigParser extends BaseParser<VdianConfigVO> {

	private static final String TAG = "VdianConfigParser";

	@Override
	public VdianConfigVO parseJSON(String paramString) throws JSONException {
		
		VdianConfigVO vdianConfig = new VdianConfigVO();
		CheckResponse response = checkResponse(vdianConfig, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, TAG+" 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, TAG+" 调用接口出错.");

			return (VdianConfigVO) response.getBaseEntity();
		}

		JSONObject root = JSON.parseObject(paramString);
		JSONObject jsonData = root.getJSONObject("data");
		String configValue = jsonData.getString("configValue");
		if(!TextUtils.isEmpty(configValue)){
			vdianConfig = JSON.parseObject(configValue, VdianConfigVO.class);
			Logger.d(TAG, "解析configValue 成功");
		}
		if(vdianConfig != null){
			vdianConfig.setRtnCode(root.getString("rtn_code"));
			vdianConfig.setRtnMsg(root.getString("rtn_msg"));
		}
		
		return vdianConfig;
	}
}
