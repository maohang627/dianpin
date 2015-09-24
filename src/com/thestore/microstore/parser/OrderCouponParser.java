package com.thestore.microstore.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.coupon.CouponResult;
import com.thestore.microstore.vo.coupon.ShoppingCouponGroup;
import com.thestore.microstore.vo.coupon.ShoppingCouponVo;
import com.thestore.microstore.vo.coupon.ShoppingPaymentCoupon;

/**
 * 抵用券接口解析
 * 
 * @author maohang
 * 
 */
public class OrderCouponParser extends BaseParser<CouponResult> {

	private static final String TAG = "vdian_OrderCouponParser";

	@Override
	public CouponResult parseJSON(String paramString)
			throws JSONException {

		CouponResult result = new CouponResult();
		CheckResponse response = checkResponse(result, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, "OrderInitParser 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, "OrderInitParser 调用接口出错.");

			return (CouponResult) response.getBaseEntity();
		}

		JSONObject root = JSON.parseObject(paramString);
		JSONObject jsonData = root.getJSONObject("data");
		result.setCode(jsonData.getString("code"));
		result.setMsg(jsonData.getString("msg"));

		// 解析抵用券信息
		if (jsonData != null && !jsonData.isEmpty()) {

			JSONObject data = jsonData.getJSONObject("data");
			if(data!=null && !data.isEmpty()){
				if("3".equals(result.getCode())){
					result.setMobile(data.getString("mobile"));
				}
				JSONObject paymentCouponObj = data.getJSONObject("paymentCoupon");
				if (paymentCouponObj != null && !paymentCouponObj.isEmpty()) {
					ShoppingPaymentCoupon  shoppingPaymentCoupon =new ShoppingPaymentCoupon();
					shoppingPaymentCoupon.setUseableCouponNum(paymentCouponObj.getIntValue("useableCouponNum"));
					List<ShoppingCouponGroup> couponGroups = new ArrayList<ShoppingCouponGroup>();

					JSONArray groupsArray = paymentCouponObj
							.getJSONArray("couponGroups");
					if (groupsArray != null && !groupsArray.isEmpty()) {
						for (int i = 0; i < groupsArray.size(); i++) {
							JSONObject groupObj = (JSONObject) groupsArray.get(i);
							ShoppingCouponGroup group = new ShoppingCouponGroup();
							group.setCouponType(groupObj.getString("couponType"));
							group.setCouponTypeDesc(groupObj.getString("couponTypeDesc"));
							group.setMerchantId(groupObj.getLong("merchantId"));

							List<ShoppingCouponVo> mutexCouponList = new ArrayList<ShoppingCouponVo>();
							JSONArray mutexCouponArray = groupObj.getJSONArray("mutexCouponList");
							if (mutexCouponArray != null&& !mutexCouponArray.isEmpty()) {

								for (int j = 0; j < mutexCouponArray.size(); j++) {
									JSONObject couponObj = (JSONObject) mutexCouponArray.get(j);
									ShoppingCouponVo couponVo = new ShoppingCouponVo();
									couponVo.setAmount(couponObj.getString("amount"));
									couponVo.setBeginTime(couponObj.getString("beginTime"));
									couponVo.setCanUse(couponObj.getBooleanValue("canUse"));
									couponVo.setCouponNumber(couponObj.getString("couponNumber"));
									couponVo.setDefineType(couponObj.getInteger("defineType"));
									couponVo.setExpiredTime(couponObj.getString("expiredTime"));
									couponVo.setPrefix(couponObj.getString("prefix"));
									couponVo.setType(couponObj.getIntValue("type"));
									couponVo.setUsed(couponObj.getBooleanValue("used"));

									List<String> description = new ArrayList<String>();
									JSONArray descriptionArray = couponObj.getJSONArray("description");
									if (descriptionArray != null&& !descriptionArray.isEmpty()) {
										for (int k = 0; k < descriptionArray.size(); k++) {
											description.add(descriptionArray.get(k).toString());
										}
									}
									couponVo.setDescription(description);
									mutexCouponList.add(couponVo);
								}
							}
							group.setMutexCouponList(mutexCouponList);

							List<ShoppingCouponVo> multipleCouponList = new ArrayList<ShoppingCouponVo>();
							JSONArray multipleCouponArray = groupObj.getJSONArray("multipleCouponList");
							if (multipleCouponArray != null&& !multipleCouponArray.isEmpty()) {

								for (int j = 0; j < multipleCouponArray.size(); j++) {
									JSONObject couponObj = (JSONObject) multipleCouponArray.get(j);
									ShoppingCouponVo couponVo = new ShoppingCouponVo();
									couponVo.setAmount(couponObj.getString("amount"));
									couponVo.setBeginTime(couponObj.getString("beginTime"));
									couponVo.setCanUse(couponObj.getBooleanValue("canUse"));
									couponVo.setCouponNumber(couponObj.getString("couponNumber"));
									couponVo.setDefineType(couponObj.getInteger("defineType"));

									couponVo.setExpiredTime(couponObj.getString("expiredTime"));
									couponVo.setPrefix(couponObj.getString("prefix"));
									couponVo.setType(couponObj.getIntValue("type"));
									couponVo.setUsed(couponObj.getBooleanValue("used"));

									List<String> description = new ArrayList<String>();
									JSONArray descriptionArray = couponObj.getJSONArray("description");
									if (descriptionArray != null
											&& !descriptionArray.isEmpty()) {
										for (int k = 0; k < descriptionArray.size(); k++) {
											description.add(descriptionArray.get(k).toString());
										}
									}
									couponVo.setDescription(description);
									multipleCouponList.add(couponVo);
								}
							}
							group.setMultipleCouponList(multipleCouponList);
							couponGroups.add(group);
						}
					}

					shoppingPaymentCoupon.setCouponGroups(couponGroups);
					result.setShoppingPaymentCoupon(shoppingPaymentCoupon);
				}
				
				JSONObject selectedPaymentObj = data.getJSONObject("selectedPayment");
				if (selectedPaymentObj != null && !selectedPaymentObj.isEmpty()) {
					result.setPaidByCoupon(selectedPaymentObj.getString("paidByCoupon"));
					result.setAmountNeed2Pay(selectedPaymentObj.getString("amountNeed2Pay"));
				}				
			}
			
		}

		return result;
	}

}
