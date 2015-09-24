package com.thestore.microstore.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.util.Logger;
import com.thestore.microstore.vo.ShoppingCartBaseOutput;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.shoppingcart.ShoppingCart;

public class ShoppingCartBaseOutputParser extends BaseParser<ShoppingCartBaseOutput> {
	
	private static final String TAG = "ShoppingCartBaseOutputParser";
	
	public ShoppingCartBaseOutput parseJSON(String paramString) throws JSONException {
		ShoppingCartBaseOutput output = new ShoppingCartBaseOutput();
		
		CheckResponse response = checkResponse(output, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, TAG+" 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, TAG+" 调用接口出错.");

			return (ShoppingCartBaseOutput) response.getBaseEntity();
		}
		
		Logger.d(TAG, paramString);
		
		return output;

	}

}
