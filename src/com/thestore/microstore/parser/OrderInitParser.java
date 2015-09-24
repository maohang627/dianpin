package com.thestore.microstore.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.data.Const;
import com.thestore.microstore.vo.Address;
import com.thestore.microstore.vo.IdValue;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.coupon.ShoppingCouponGroup;
import com.thestore.microstore.vo.coupon.ShoppingCouponVo;
import com.thestore.microstore.vo.coupon.ShoppingPaymentCoupon;
import com.thestore.microstore.vo.order.Delivery;
import com.thestore.microstore.vo.order.InvoiceShowVo;
import com.thestore.microstore.vo.order.InvoicesCommDisplay;
import com.thestore.microstore.vo.order.Order;
import com.thestore.microstore.vo.order.OrderPro;
import com.thestore.microstore.vo.order.Payment;
import com.thestore.microstore.vo.order.ProPackage;

public class OrderInitParser extends BaseParser<Order> {

	private static final String TAG = "vdian_OrderInitParser";

	public Order parseJSON(String paramString) throws JSONException {
		

		Order result = new Order();
		CheckResponse response = checkResponse(result, paramString);
		if(response.getResult() == -1) {
			Log.e(TAG, "OrderInitParser 返回空.");
			
			return null;
		} else if(response.getResult() == 1) {
			Log.e(TAG, "OrderInitParser 调用接口出错.");
			
			return (Order) response.getBaseEntity();
		}

		JSONObject root = JSON.parseObject(paramString);
		JSONObject jsonData = root.getJSONObject("data");
		// 获取收获地址
		JSONObject receiverDTOList = jsonData.getJSONObject("receiverDTOList");
		List<Address> addressList = new ArrayList<Address>();

		// 获取收货地址 -- 被选中的收货地址
		JSONObject receiverDTO = receiverDTOList.getJSONObject("receiverDTO");
		Address defaultAddress = null;
		if (receiverDTO != null && !receiverDTO.isEmpty()) {
			defaultAddress = new Address();
			
			defaultAddress.setId(receiverDTO.getString("id"));
			defaultAddress.setName(receiverDTO.getString("name"));
			defaultAddress.setProvinceId(receiverDTO.getString("provinceId"));
			defaultAddress.setProvinceName(receiverDTO.getString("provinceName"));
			defaultAddress.setCityId(receiverDTO.getString("cityId"));
			defaultAddress.setCityName(receiverDTO.getString("cityName"));
			defaultAddress.setAreaId(receiverDTO.getString("countyId"));
			defaultAddress.setAreaName(receiverDTO.getString("countyName"));
			// 省 市 区
			String add = "";
			if(!TextUtils.isEmpty(receiverDTO.getString("provinceName"))){
				add += receiverDTO.getString("provinceName") + " ";
			}
			if(!TextUtils.isEmpty(receiverDTO.getString("cityName"))){
				add += receiverDTO.getString("cityName") + " ";
			}
			if(!TextUtils.isEmpty(receiverDTO.getString("countyName"))){
				add += receiverDTO.getString("countyName") + " ";
			}
			defaultAddress.setAddress(add);
			// 详情地址
			defaultAddress.setAddressDetail(receiverDTO.getString("address"));
			defaultAddress.setTel(receiverDTO.getString("mobileNum"));

			defaultAddress.setSelected(Boolean.TRUE);
			defaultAddress.setDefaultReceiver(receiverDTO.getString("defaultAddr"));
			result.setSelectedAddress(defaultAddress);
		}

		// 获取收获地址 -- 为用户的前20条地址记录 ,此处包含以选中的地址信息
		JSONArray receiverDTOs = receiverDTOList.getJSONArray("receiverDTOs");
		if (receiverDTOs != null && !receiverDTOs.isEmpty()) {
			for (int i = 0; i < receiverDTOs.size(); i++) {
				JSONObject receiverObj = (JSONObject) receiverDTOs.get(i);
				Address address = new Address();

				address.setId(receiverObj.getString("id"));
				if(receiverObj.getString("name") != null){
					address.setName(Html.fromHtml(receiverObj.getString("name")).toString());// （特殊字符转义后还原）
				}
				address.setProvinceId(receiverObj.getString("provinceId"));
				address.setProvinceName(receiverObj.getString("provinceName"));
				address.setCityId(receiverObj.getString("cityId"));
				address.setCityName(receiverObj.getString("cityName"));
				address.setAreaId(receiverObj.getString("countyId"));
				address.setAreaName(receiverObj.getString("countyName"));
				// 省 市 区
				String add = "";
				if(!TextUtils.isEmpty(receiverObj.getString("provinceName"))){
					add += receiverObj.getString("provinceName") + " ";
				}
				if(!TextUtils.isEmpty(receiverObj.getString("cityName"))){
					add += receiverObj.getString("cityName") + " ";
				}
				if(!TextUtils.isEmpty(receiverObj.getString("countyName"))){
					add += receiverObj.getString("countyName") + " ";
				}
				address.setAddress(add);
				// 详情地址
				if(receiverObj.getString("address") != null){
					address.setAddressDetail(Html.fromHtml(receiverObj.getString("address")).toString());// （特殊字符转义后还原）
				}
				if(receiverObj.getString("mobileNum") != null){
					address.setTel(Html.fromHtml(receiverObj.getString("mobileNum")).toString());// （特殊字符转义后还原）
				}
				if (defaultAddress != null && address.getId().equals(defaultAddress.getId())) { // 此处包含以选中的地址信息
					address.setSelected(Boolean.TRUE);
				}
				String defaultAddr = "0";
				if(Const.DEFAULT_ADDR.equals(receiverObj.getString("defaultAddr"))){
					defaultAddr = "1";
				}
				address.setDefaultReceiver(defaultAddr);
				addressList.add(address);
			}
			result.setAddressList(addressList);
		}

		// 获取支付方式
		List<Payment> paymentList = new ArrayList<Payment>();
		JSONObject paymentListObject = jsonData.getJSONObject("paymentList");
		if (paymentListObject != null && !paymentListObject.isEmpty()) {
			JSONArray payments = paymentListObject.getJSONArray("payments");
			if (payments != null && !payments.isEmpty()) {
				for (int i = 0; i < payments.size(); i++) {
					JSONObject pay = (JSONObject) payments.get(i);
					if (pay.getBoolean("isSupport")) {
						Payment payment = new Payment();
						payment.setId(pay.getString("id"));
						payment.setName(pay.getString("name"));
						paymentList.add(payment);
					}
				}
			}
		}

		result.setPaymentList(paymentList);
		
		
		//解析抵用券信息
		if (paymentListObject != null && !paymentListObject.isEmpty()) {
			ShoppingPaymentCoupon paymentCoupon = new ShoppingPaymentCoupon();
			JSONObject paymentCouponObj = paymentListObject.getJSONObject("paymentCoupon");
			if(paymentCouponObj!=null && !paymentCouponObj.isEmpty()){
				paymentCoupon.setUseableCouponNum(paymentCouponObj.getIntValue("useableCouponNum"));
				List<ShoppingCouponGroup> couponGroups =new ArrayList<ShoppingCouponGroup>();
				
				JSONArray groupsArray = paymentCouponObj.getJSONArray("couponGroups");
				if(groupsArray!=null && !groupsArray.isEmpty()){
					for (int i = 0; i < groupsArray.size(); i++) {
						JSONObject groupObj = (JSONObject) groupsArray.get(i);						
						ShoppingCouponGroup group =new  ShoppingCouponGroup();
						group.setCouponType(groupObj.getString("couponType"));
						group.setCouponTypeDesc(groupObj.getString("couponTypeDesc"));
						group.setMerchantId(groupObj.getLong("merchantId"));
																								
						List<ShoppingCouponVo> mutexCouponList =new ArrayList<ShoppingCouponVo>();
						JSONArray mutexCouponArray =groupObj.getJSONArray("mutexCouponList");
						if(mutexCouponArray!=null && !mutexCouponArray.isEmpty()){
							
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
								
								List<String> description =new  ArrayList<String>();
								JSONArray descriptionArray = couponObj.getJSONArray("description");
								if(descriptionArray!=null && !descriptionArray.isEmpty()){
									for (int k = 0; k < descriptionArray.size(); k++) {
										description.add(descriptionArray.get(k).toString());
									}
								}
								couponVo.setDescription(description);
								mutexCouponList.add(couponVo);
							}
						}
						group.setMutexCouponList(mutexCouponList);
						
						List<ShoppingCouponVo> multipleCouponList =new ArrayList<ShoppingCouponVo>();
						JSONArray multipleCouponArray =groupObj.getJSONArray("multipleCouponList");
						if(multipleCouponArray!=null && !multipleCouponArray.isEmpty()){
							
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
								
								List<String> description =new  ArrayList<String>();
								JSONArray descriptionArray = couponObj.getJSONArray("description");
								if(descriptionArray!=null && !descriptionArray.isEmpty()){
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
				
				paymentCoupon.setCouponGroups(couponGroups);
				
			}
			result.setPaymentCoupon(paymentCoupon);
		}		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		// 发票信息
		List<IdValue> invoiceTypeList = new ArrayList<IdValue>();
		IdValue persion = new IdValue();
		persion.setId(Const.INVOICE_PERSION);
		persion.setValue("个人");
		invoiceTypeList.add(persion);
		IdValue unit = new IdValue();
		unit.setId(Const.INVOICE_UNIT);
		unit.setValue("单位");
		invoiceTypeList.add(unit);
		result.setInvoiceTypeList(invoiceTypeList);

		JSONObject invoiceDTO = jsonData.getJSONObject("invoiceDTO");
		if (invoiceDTO != null) {
			String invoiceContentStr = invoiceDTO.getString("invoiceContentStr");
			if (!TextUtils.isEmpty(invoiceContentStr)) {
				List<String> invoiceContentList = new ArrayList<String>();
				String[] invoiceStrs = invoiceContentStr.split(",");
				for (String str : invoiceStrs) {
					invoiceContentList.add(str);
				}
				result.setInvoiceContentList(invoiceContentList);
			}
			
			JSONObject invoiceShowVo = invoiceDTO.getJSONObject("invoiceShowVo");
			if(invoiceShowVo != null){
				InvoiceShowVo invoiceShow = new InvoiceShowVo();
				invoiceShow.setIsContainComm(invoiceShowVo.getString("isContainComm"));
				invoiceShow.setIsContainMust(invoiceShowVo.getString("isContainMust"));
				
				result.setInvoiceShowVo(invoiceShow);
			}
			
			JSONObject invoicesCommDisplay = invoiceDTO.getJSONObject("invoicesCommDisplay");
			if(invoicesCommDisplay != null){
				InvoicesCommDisplay invoicesComm = new InvoicesCommDisplay();
				invoicesComm.setTitleType(invoicesCommDisplay.getString("titleType"));
				if(invoicesCommDisplay.getString("title") != null){
					invoicesComm.setTitle(Html.fromHtml(invoicesCommDisplay.getString("title")).toString());// （特殊字符转义后还原）
				}
				if(invoicesCommDisplay.getString("content") != null){
					invoicesComm.setContent(Html.fromHtml(invoicesCommDisplay.getString("content")).toString());// （特殊字符转义后还原）
				}
				
				result.setInvoicesCommDisplay(invoicesComm);
			}
			
		}

		// 商品列表
		List<ProPackage> proPackageList = new ArrayList<ProPackage>();
		JSONObject merchantList = jsonData.getJSONObject("merchantList");
		JSONObject productsMap = jsonData.getJSONObject("productsMap");
		if (receiverDTO == null || receiverDTO.isEmpty()) { // 如果收获地址为空
			int count = 0;
			int quantity = 0;
			BigDecimal price = new BigDecimal(0);
			List<OrderPro> proList = new ArrayList<OrderPro>();

			for (Object key : productsMap.keySet()) {
				JSONObject pro = (JSONObject) productsMap.get(key);
				count++;
				price = price.add(new BigDecimal(pro.getString("price")));

				OrderPro orderPro = new OrderPro();
				orderPro.setColor("");
				orderPro.setCount(pro.getString("quantity"));
				quantity += Integer.valueOf(pro.getString("quantity"));
				orderPro.setDesc("");
				orderPro.setName(pro.getString("name"));
				orderPro.setPrice(pro.getString("price"));
				orderPro.setSize("");

				proList.add(orderPro);
			}

			ProPackage proPackage = new ProPackage();
			proPackage.setName("包裹1");
			proPackage.setDesc(quantity + "件商品");
			proPackage.setPriceDesc("运费￥" + price.setScale(2, BigDecimal.ROUND_HALF_UP));
			proPackage.setProList(proList);
			proPackage.setQuantity(quantity);

			proPackageList.add(proPackage);

		} else {
			int packageCount = 1;
			JSONArray merchants = merchantList.getJSONArray("merchants");
			for (int i = 0; i < merchants.size(); i++) {
				JSONArray deliveryGroups = ((JSONObject) merchants.get(i)).getJSONArray("deliveryGroups");
				for (int j = 0; j < deliveryGroups.size(); j++) {
					JSONArray packages = ((JSONObject) deliveryGroups.get(j)).getJSONArray("packages");
					for (int m = 0; m < packages.size(); m++) {
						int quantity = 0;
						JSONObject pack = (JSONObject) packages.get(m);

						JSONArray products = pack.getJSONArray("products");

						// 设置包裹信息
						ProPackage proPackage = new ProPackage();
						proPackage.setName("包裹" + (packageCount++));

						// 设置包裹对应的配送方式
						JSONObject selectedDelivery = pack.getJSONObject("selectedDelivery");
						Delivery delivery = new Delivery();
						delivery.setDeliveryMethodId(selectedDelivery.getString("deliveryMethodId")); // 配送方式ID
						delivery.setFee(selectedDelivery.getString("fee")); // 运费
						delivery.setOrderMark(selectedDelivery.getString("orderMark")); //
						
						proPackage.setPriceDesc("￥" + selectedDelivery.getString("fee"));
						proPackage.setDelivery(delivery);

						List<OrderPro> proList = new ArrayList<OrderPro>();

						for (int n = 0; n < products.size(); n++) {
							String productId = (String) products.get(n);
							JSONObject pro = productsMap.getJSONObject(productId);

							OrderPro orderPro = new OrderPro();
							orderPro.setColor("");
							orderPro.setCount(pro.getString("quantity"));
							quantity += Integer.valueOf(pro.getString("quantity"));
							orderPro.setDesc("");
							orderPro.setName(pro.getString("name"));
							orderPro.setPrice(pro.getString("price"));
							orderPro.setSize("");

							proList.add(orderPro);
						}

						proPackage.setDesc(quantity + "件商品");
						proPackage.setProList(proList);
						proPackage.setQuantity(quantity);

						proPackageList.add(proPackage);
					}
				}
			}
		}

		// 添加商品列表
		result.setProPackageList(proPackageList);

		if (paymentListObject != null && !paymentListObject.isEmpty()) {
			JSONObject selectedPayment = paymentListObject.getJSONObject("selectedPayment");
			// 商品金额
			result.setAmt(selectedPayment.getString("productAmount"));

			// 运费
			result.setTranAmt(selectedPayment.getString("postage"));

			// 共需支付
			result.setNeedPay(selectedPayment.getString("amountNeed2Pay"));
		}

		return result;
	}
}
