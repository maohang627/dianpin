package com.thestore.microstore.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thestore.microstore.util.Logger;
import com.thestore.microstore.util.Util;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.proxy.AreaProxy;
import com.thestore.microstore.vo.shoppingcart.CartItem;
import com.thestore.microstore.vo.shoppingcart.ShoppingCart;

public class ShoppingCarParser extends BaseParser<ShoppingCart> {
	
	private static final String TAG = "ShoppingCarParser";
	
	public ShoppingCart parseJSON(String paramString) throws JSONException {
//		//TODO:临时测试 begin
//		paramString = "{\"data\":{\"giftPromotionCount\":0,\"pricePromotionCount\":0,\"summary\":{\"amount\":{\"money\":42150,\"points\":0},\"badgeFreeDeliveryFee\":false,\"count\":4,\"deduction\":0,\"deliveryFee\":0,\"freeProgressPercent\":0,\"weight\":9.36,\"yhdDeliFreshOverWeight\":false,\"yhdDeliNormalOverWeight\":false},\"vdianBags\":[{\"itemGroups\":[{\"amount\":{\"money\":42150,\"points\":0},\"gifts\":[],\"items\":[{\"amount\":{\"money\":395,\"points\":0},\"checked\":true,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1172686_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837553595,\"name\":\"1.【永隆铸金】福虎如意（带玉石中号）千足绒沙金摆件\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":395,\"pic\":\"http://d12.yihaodianimg.com/t1/2010/1022/511/437/1272831.jpg\",\"pmId\":1172686,\"preSell\":false,\"price\":{\"money\":395,\"points\":0},\"productId\":1047996,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.5,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14280,\"warning\":true,\"weight\":0.5},{\"amount\":{\"money\":3356,\"points\":0},\"checked\":false,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"102957_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837566271,\"name\":\"2.飞利浦空气净化器AC4074\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":3356,\"pic\":\"http://d11.yihaodianimg.com/t1/2009/0918/315/188/96571.jpg\",\"pmId\":102957,\"preSell\":false,\"price\":{\"money\":3356,\"points\":0},\"productId\":35267,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":8,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14235,\"warning\":true,\"weight\":8},{\"amount\":{\"money\":29999,\"points\":0},\"checked\":false,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1248176_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837586601,\"name\":\"3.爱卡纯情爱恋1克拉美钻18K金女戒\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":29999,\"pic\":\"http://d13.yihaodianimg.com/t1/2011/0309/28/305/1466908.jpg\",\"pmId\":1248176,\"preSell\":false,\"price\":{\"money\":29999,\"points\":0},\"productId\":1092300,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.1,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14232,\"warning\":true,\"weight\":0.1},{\"amount\":{\"money\":8400,\"points\":0},\"checked\":false,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1158880_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837576704,\"name\":\"4.佳能canonEOS60DKIT（EF-S18-135IS）单反套机\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":8400,\"pic\":\"http://d12.yihaodianimg.com/t1/2010/1009/135/376/1241223.jpg\",\"pmId\":1158880,\"preSell\":false,\"price\":{\"money\":8400,\"points\":0},\"productId\":1039809,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.76,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14233,\"warning\":true,\"weight\":0.76},{\"amount\":{\"money\":395,\"points\":0},\"checked\":false,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1172686_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837553595,\"name\":\"5.【永隆铸金】福虎如意（带玉石中号）千足绒沙金摆件\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":395,\"pic\":\"http://d12.yihaodianimg.com/t1/2010/1022/511/437/1272831.jpg\",\"pmId\":1172686,\"preSell\":false,\"price\":{\"money\":395,\"points\":0},\"productId\":1047996,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.5,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14280,\"warning\":true,\"weight\":0.5},{\"amount\":{\"money\":3356,\"points\":0},\"checked\":false,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"102957_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837566271,\"name\":\"6.飞利浦空气净化器AC4074\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":3356,\"pic\":\"http://d11.yihaodianimg.com/t1/2009/0918/315/188/96571.jpg\",\"pmId\":102957,\"preSell\":false,\"price\":{\"money\":3356,\"points\":0},\"productId\":35267,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":8,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14235,\"warning\":true,\"weight\":8},{\"amount\":{\"money\":29999,\"points\":0},\"checked\":false,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1248176_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837586601,\"name\":\"7.爱卡纯情爱恋1克拉美钻18K金女戒\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":29999,\"pic\":\"http://d13.yihaodianimg.com/t1/2011/0309/28/305/1466908.jpg\",\"pmId\":1248176,\"preSell\":false,\"price\":{\"money\":29999,\"points\":0},\"productId\":1092300,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.1,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14232,\"warning\":true,\"weight\":0.1},{\"amount\":{\"money\":8400,\"points\":0},\"checked\":false,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1158880_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837576704,\"name\":\"8.佳能canonEOS60DKIT（EF-S18-135IS）单反套机\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":8400,\"pic\":\"http://d12.yihaodianimg.com/t1/2010/1009/135/376/1241223.jpg\",\"pmId\":1158880,\"preSell\":false,\"price\":{\"money\":8400,\"points\":0},\"productId\":1039809,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.76,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14233,\"warning\":false,\"weight\":0.76},{\"amount\":{\"money\":395,\"points\":0},\"checked\":true,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1172686_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837553595,\"name\":\"9.【永隆铸金】福虎如意（带玉石中号）千足绒沙金摆件\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":395,\"pic\":\"http://d12.yihaodianimg.com/t1/2010/1022/511/437/1272831.jpg\",\"pmId\":1172686,\"preSell\":false,\"price\":{\"money\":395,\"points\":0},\"productId\":1047996,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.5,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14280,\"warning\":false,\"weight\":0.5},{\"amount\":{\"money\":3356,\"points\":0},\"checked\":true,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"102957_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837566271,\"name\":\"10.飞利浦空气净化器AC4074\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":3356,\"pic\":\"http://d11.yihaodianimg.com/t1/2009/0918/315/188/96571.jpg\",\"pmId\":102957,\"preSell\":false,\"price\":{\"money\":3356,\"points\":0},\"productId\":35267,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":8,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14235,\"warning\":false,\"weight\":8},{\"amount\":{\"money\":29999,\"points\":0},\"checked\":true,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1248176_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837586601,\"name\":\"11.爱卡纯情爱恋1克拉美钻18K金女戒\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":29999,\"pic\":\"http://d13.yihaodianimg.com/t1/2011/0309/28/305/1466908.jpg\",\"pmId\":1248176,\"preSell\":false,\"price\":{\"money\":29999,\"points\":0},\"productId\":1092300,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.1,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14232,\"warning\":false,\"weight\":0.1},{\"amount\":{\"money\":8400,\"points\":0},\"checked\":true,\"commission\":false,\"discount\":false,\"epp\":false,\"flash\":false,\"fresh\":false,\"gather\":false,\"giftCard\":false,\"groupon\":false,\"id\":\"1158880_0-1\",\"itemSaleType\":1,\"itemType\":\"ITEM\",\"marketPrice\":0,\"merchantId\":1,\"milliTime\":1412837576704,\"name\":\"12.佳能canonEOS60DKIT（EF-S18-135IS）单反套机\",\"nestedItems\":[],\"num\":1,\"onSale\":false,\"originalPrice\":8400,\"pic\":\"http://d12.yihaodianimg.com/t1/2010/1009/135/376/1241223.jpg\",\"pmId\":1158880,\"preSell\":false,\"price\":{\"money\":8400,\"points\":0},\"productId\":1039809,\"productType\":0,\"sensitive\":false,\"shoppingCount\":0,\"totalWeight\":0.76,\"typeValue\":1,\"vip\":false,\"vmerchantId\":1079,\"vpmId\":14233,\"warning\":false,\"weight\":0.76}],\"milliTime\":0,\"pricePromotions\":[]}],\"summary\":{\"amount\":{\"money\":42150,\"points\":0},\"badgeFreeDeliveryFee\":false,\"count\":4,\"deduction\":0,\"deliveryFee\":0,\"freeProgressPercent\":0,\"weight\":9.36,\"yhdDeliFreshOverWeight\":false,\"yhdDeliNormalOverWeight\":false},\"vmerchantId\":1079,\"vmerchantName\":\"赵\"}]},\"rtn_code\":0,\"rtn_msg\":\"success\"}";
//		//TODO:临时测试 end
		
		ShoppingCart cart = new ShoppingCart();
		CheckResponse response = checkResponse(cart, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, TAG+" 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, TAG+" 调用接口出错.");

			return (ShoppingCart) response.getBaseEntity();
		}
		
		List<CartItem> cartItems = new ArrayList<CartItem>();//正常的商品
		List<CartItem> overDueCartItems = new ArrayList<CartItem>();//过期的商品
		double totalPrice = 0d;
		
		Logger.d(TAG, paramString);
		JSONObject jsonObject = new JSONObject(paramString);
		JSONObject jsonData = jsonObject.getJSONObject("data");
//		totalPrice = jsonData.getJSONObject("summary").getJSONObject("amount").getDouble("money");
		
		JSONArray jaVDianBags = jsonData.getJSONArray("vdianBags");
		for (int i = 0; i < jaVDianBags.length(); i++) {
			JSONObject joVDianBag = (JSONObject) jaVDianBags.get(i);
			JSONArray jaItemGroups = joVDianBag.getJSONArray("itemGroups");
			for (int j = 0; j < jaItemGroups.length(); j++) {
				JSONObject joItemGroup = (JSONObject) jaItemGroups.get(j);
				JSONArray jaItems = joItemGroup.getJSONArray("items");
				for (int k = 0; k < jaItems.length(); k++) {
					JSONObject joItem = (JSONObject) jaItems.get(k);
					boolean warning = joItem.getBoolean("warning");
					
					CartItem cartItem = new CartItem();
					cartItem.setId(joItem.getString("id"));
					if (!joItem.isNull("pic")) {
						cartItem.setImageUrl(Util.getImg40x40(joItem.getString("pic")));
					}
					cartItem.setDesc(joItem.getString("name"));
					cartItem.setColor("");
					cartItem.setSize("");
					cartItem.setCount(joItem.getInt("num") + "");
					double money = joItem.getJSONObject("amount").getDouble("money");
					cartItem.setPrice(money + "");
					cartItem.setvMerchantId(joItem.getInt("vmerchantId") + "");
					cartItem.setPmId(joItem.getInt("pmId") + "");	
					cartItem.setvPmId(joItem.getInt("vpmId") + "");
					boolean checked = joItem.getBoolean("checked");
					cartItem.setChecked(checked);
					
					if(warning){
						// 失效商品列表
						overDueCartItems.add(cartItem);
					}else{	
						// 正常商品列表
						cartItems.add(cartItem);
						// 对选择中的商品计算总价
						if(checked){
//							totalPrice += money; 
							totalPrice = new BigDecimal(Double.toString(totalPrice)).add(new BigDecimal(Double.toString(money))).doubleValue();
						}
					}
											
				}
				
			}
			
		}
		
		cart.setCartItems(cartItems);
		cart.setOverDueCartItems(overDueCartItems);
		cart.setTotalPrice(String.valueOf(totalPrice));	
		
		return cart;
	}
	
}
