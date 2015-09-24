package com.thestore.microstore.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.vo.Invoice;


/**
 * 
 * 
 * 发票parser
 *
 * 2014-9-18 下午2:07:08
 */
public class InvoiceParser  extends BaseParser<Invoice> {
	
	private static final String TAG = "vdian_InvoiceParser";

	@Override
	public Invoice parseJSON(String paramString) throws JSONException {
		
		if(paramString == null) {
			Log.i(TAG, "InvoiceParser, 返回结果为空.");
			return null;
		}
		
		Invoice result = new Invoice();
		JSONObject jsonData = new JSONObject(paramString);
		
		result.setOrderRundomString(jsonData.getJSONObject("data").getJSONObject("invoiceDTO").getString("orderRundomString"));
		
		return result;
	}

}
