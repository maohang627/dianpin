package com.thestore.microstore.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.text.Html;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.data.OrderStatus;
import com.thestore.microstore.vo.order.OrderDetail;
import com.thestore.microstore.vo.order.OrderPro;
import com.thestore.microstore.vo.order.ProPackage;
import com.thestore.microstore.vo.proxy.Bank;

public class OrderDetailParser extends BaseParser<OrderDetail> {
	private static final String TAG = "OrderDetailParser";

	@Override
	public OrderDetail parseJSON(String paramString) throws JSONException {

		if (paramString == null) {
			Log.i(TAG, "ResultParser, 返回结果为空.");
			return null;
		}

		OrderDetail result = new OrderDetail();
		JSONObject root = JSON.parseObject(paramString);
		JSONObject jsonData = root.getJSONObject("data");
		result.setRtnCode(root.getString("rtn_code"));
		result.setRtnMsg(root.getString("rtn_msg"));
		result.setOrderCode(jsonData.getString("orderCode")); // 订单编号
		result.setOrderTime(jsonData.getString("orderTime"));// 下单时间
		String payServiceType = jsonData.getString("payServiceType");
		result.setPayServiceType(payServiceType);// 支付方式
		result.setGateway(jsonData.getString("gateway"));// 网关
		String orderStatus = jsonData.getString("orderStatus");
		result.setOrderStatus(orderStatus);// 订单状态
		String orderStatusString = jsonData.getString("orderStatusString");
		// payServiceType=2：货到付款 5：pos机，orderStatus=3：等待支付，orderStatusString=待发货
		if ((Const.PAYMENT_TYPE_BY_CASH.equals(payServiceType) || Const.PAYMENT_TYPE_BY_POS
				.equals(payServiceType))
				&& OrderStatus.WAITING_FOR_PAYMENT.code.equals(orderStatus)) {
			orderStatusString = Const.ORDER_STATUS_TO_BE_SHIPPED;
		}
		result.setOrderStatusString(orderStatusString);
		result.setGoodReceiverName(jsonData.getString("goodReceiverName"));
		result.setGoodReceiverProvince(jsonData.getString("goodReceiverProvince"));
		result.setGoodReceiverCity(jsonData.getString("goodReceiverCity"));
		result.setGoodReceiverCounty(jsonData.getString("goodReceiverCounty"));
		result.setGoodReceiverAddress(jsonData.getString("goodReceiverAddress"));
		result.setGoodReceiverMobile(jsonData.getString("goodReceiverMobile"));
		result.setChildNum(jsonData.getString("childNum"));// 包裹数
		result.setProductCount(jsonData.getString("productCount"));// 订单产品数量
		JSONArray paymend = jsonData.getJSONArray("paymend");
		List<Bank> bankList = new ArrayList<Bank>();
		if(paymend != null){
			for (int i = 0; i < paymend.size(); i++) {
				JSONObject pay = (JSONObject)paymend.get(i);
				Bank bank = new Bank();
				bank.setCode(pay.getString("bankCode"));
				bank.setType(pay.getString("bankType"));
				bank.setGatewayId(pay.getString("gatewayId"));
				bank.setImgUrl(pay.getString("imgUrl"));
				bank.setName(pay.getString("name"));
				
				bankList.add(bank);
			}
		}
		result.setBankList(bankList);
		result.setPayableAmount(jsonData.getString("payableAmount"));// 还需支付金额
		
		result.setStoreLogoUrl(jsonData.getString("storeLogoUrl"));// 店铺图标地址
		if(jsonData.getString("storeName") != null){
			result.setStoreName(Html.fromHtml(jsonData.getString("storeName")).toString());// 店铺名称（特殊字符转义后还原）
		}
		if(jsonData.getString("wechatId") != null){
			result.setWechatId(Html.fromHtml(jsonData.getString("wechatId")).toString());// 卖家微信号（特殊字符转义后还原）
		}
		if(jsonData.getString("phone") != null){
			result.setPhone(Html.fromHtml(jsonData.getString("phone")).toString());// 卖家联系电话（特殊字符转义后还原）
		}
		result.setOrderNeedInvoice(jsonData.getString("orderNeedInvoice"));// 发票需要情况{0:不需要、1:旧版普通、2:新版普通、3:增值税发票}
		result.setInvoiceType(jsonData.getString("invoiceType"));// 抬头类型 0个人 1单位
		if(jsonData.getString("invoiceTitle") != null){
			result.setInvoiceTitle(Html.fromHtml(jsonData.getString("invoiceTitle")).toString());// 发票抬头（特殊字符转义后还原）
		}
		if(jsonData.getString("invoiceContent") != null){
			result.setInvoiceContent(Html.fromHtml(jsonData.getString("invoiceContent")).toString());// 发票内容（特殊字符转义后还原）
		}
		// 包裹及配送信息
		// 商品列表
		List<ProPackage> proPackageList = new ArrayList<ProPackage>();
		if(jsonData.containsKey("soItemList")){
			// 没有拆单
			// 设置包裹信息
			ProPackage proPackage = new ProPackage();
			proPackage.setName("包裹1");			

			JSONArray soItemList = jsonData.getJSONArray("soItemList");
			int quantity = 0;			
			List<OrderPro> proList = new ArrayList<OrderPro>();

			for (int j = 0; j < soItemList.size(); j++) {
				JSONObject pro = (JSONObject)soItemList.get(j);

				OrderPro orderPro = new OrderPro();
				orderPro.setColor("");
				orderPro.setCount(pro.getString("orderItemNum"));
				quantity += Integer.valueOf(pro.getString("orderItemNum"));
				orderPro.setDesc("");
				orderPro.setName(pro.getString("productCname"));
				orderPro.setPrice(pro.getString("orderItemPrice"));
				orderPro.setSize("");

				proList.add(orderPro);
			}
			
			proPackage.setDesc(quantity + "件商品");
			proPackage.setExpectReceiveDate(jsonData.getString("expectReceiveDateString"));
			proPackage.setProList(proList);
			proPackage.setQuantity(quantity);

			proPackageList.add(proPackage);	
		}else{
			// 有拆单
			JSONArray packageList = jsonData.getJSONArray("packageList");

			for (int i = 0; i < packageList.size(); i++) {
				JSONObject pack = (JSONObject)packageList.get(i);
				JSONArray soItemList = pack.getJSONArray("soItemList");
				int quantity = 0;
				
				// 设置包裹信息
				ProPackage proPackage = new ProPackage();
				proPackage.setName("包裹" + (i + 1));
				
				List<OrderPro> proList = new ArrayList<OrderPro>();

				for (int j = 0; j < soItemList.size(); j++) {
					JSONObject pro = (JSONObject)soItemList.get(j);

					OrderPro orderPro = new OrderPro();
					orderPro.setColor("");
					orderPro.setCount(pro.getString("orderItemNum"));
					quantity += Integer.valueOf(pro.getString("orderItemNum"));
					orderPro.setDesc("");
					orderPro.setName(pro.getString("productCname"));
					orderPro.setPrice(pro.getString("orderItemPrice"));
					orderPro.setSize("");

					proList.add(orderPro);
				}
				
				proPackage.setDesc(quantity + "件商品");
				proPackage.setExpectReceiveDate(pack.getString("expectReceiveDateString"));
				proPackage.setProList(proList);
				proPackage.setQuantity(quantity);

				proPackageList.add(proPackage);			
			}
		}
		
		// 添加商品列表
		result.setProPackageList(proPackageList);
		result.setOrderAmount(jsonData.getString("productAmount"));// 商品总金额
		result.setPayableAmount(jsonData.getString("payableAmount"));// 还需支付金额
		result.setOrderDeliveryFee(jsonData.getString("orderDeliveryFee"));// 运费
		result.setOrderPaidByCoupon(jsonData.getString("orderPaidByCoupon"));// 抵用券支付金额
		
		return result;
	}
}
