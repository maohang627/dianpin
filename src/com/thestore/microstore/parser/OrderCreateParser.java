package com.thestore.microstore.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.vo.OrderCreate;

public class OrderCreateParser extends BaseParser<OrderCreate> {

	private static final String TAG = "vdian_ResultParser";

	@Override
	public OrderCreate parseJSON(String paramString) throws JSONException {

		if (paramString == null) {
			Log.i(TAG, "ResultParser, 返回结果为空.");
			return null;
		}

		OrderCreate result = new OrderCreate();
		JSONObject object = new JSONObject(paramString);
		JSONObject jsonData = object.getJSONObject("data");
		result.setRtnCode(object.getString("rtn_code"));
		result.setRtnMsg(object.getString("rtn_msg"));
		result.setCancelTime(jsonData.getString("cancelTime")); // 订单取消时间（分钟）
		result.setChildNum(jsonData.getString("childNum")+ "个"); // 包裹数
		result.setLefttime(jsonData.getString("lefttime")); // 剩余时间（分钟）
		result.setOrderAmount(jsonData.getString("orderAmount")); // 金额
		result.setOrderCode(jsonData.getString("orderCode")); // 订单编号
		result.setProductCount(jsonData.getString("productCount"));// 商品数
		result.setPayServiceType(jsonData.getString("payServiceType")); // 支付方式
		result.setOrderType(jsonData.getString("orderType")); // 订单类型
		result.setBusinessType(jsonData.getString("businessType")); // 业务类型
		result.setOrderStatus(jsonData.getString("orderStatus"));// 订单状态
		result.setOrderStatusString(jsonData.getString("orderStatusString"));// 订单状态描述

		return result;
	}

}
