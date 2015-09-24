package com.thestore.microstore.vo.order;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;
import com.thestore.microstore.vo.proxy.Bank;


/**
 * 
 * 订单详情
 *
 * 2014-9-24 上午9:38:05
 */
public class OrderDetail extends BaseEntity {

	// 基本信息
	// 订单编号
	private String orderCode;
	
	// 下单时间
	private String orderTime;
	
	// 订单状态
	// 3 等待支付， 4 已支付，20 已发货， 24已发货，34 交易取消，35 交易完成 ，36 订单待审核， 37 已发货， 38 待发货
	private String orderStatus;
	
	private String orderStatusString;
	
	// 收货人姓名
	private String goodReceiverName;
	
	// 收货人省
	private String goodReceiverProvince;
	
	// 收货人市
	private String goodReceiverCity;
	
	// 收货人区
	private String goodReceiverCounty;
	
	// 收货人详细地址
	private String goodReceiverAddress;
	
	// 收货人手机号
	private String goodReceiverMobile;
	
	// 订单包裹数
	private String childNum;
	
	//订单产品数量
	private String productCount;
	
	// 支付方式
	// 0：账户支付 1：网上支付 2：货到付款 3：邮局汇款 4：银行转账 5：pos机 6：万里通 7：分期付款 8：合同账期 9：货到转账 10：货到付支票, 目前微店只有1 2 5
	private String payServiceType;
	
	private List<Bank> bankList;
	
	private String gateway;
	
	// 订单金额
	private String orderAmount;
	
	// 包裹及配送信息
	private List<ProPackage> proPackageList;
	
	// 还需支付金额
	private String payableAmount;

	// 运费
	private String orderDeliveryFee;
	
	// 微店卖家信息	
	// 店铺图标地址
	private String storeLogoUrl;
	
	// 店铺名称
	private String storeName;
	
	// 卖家微信号
	private String wechatId;
	
	// 卖家联系电话
	private String phone;	

	// 发票信息
	// 发票需要情况{0:不需要、1:旧版普通、2:新版普通、3:增值税发票}
	private String orderNeedInvoice;
	
	// 抬头类型 0个人 1单位
	private String invoiceType;
	
	// 发票抬头
	private String invoiceTitle;
	
	// 发票内容
	private String invoiceContent;
	
	// 抵用券支付金额
	private String orderPaidByCoupon;
	

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusString() {
		return orderStatusString;
	}

	public void setOrderStatusString(String orderStatusString) {
		this.orderStatusString = orderStatusString;
	}

	public String getGoodReceiverName() {
		return goodReceiverName;
	}

	public void setGoodReceiverName(String goodReceiverName) {
		this.goodReceiverName = goodReceiverName;
	}

	public String getGoodReceiverProvince() {
		return goodReceiverProvince;
	}

	public void setGoodReceiverProvince(String goodReceiverProvince) {
		this.goodReceiverProvince = goodReceiverProvince;
	}

	public String getGoodReceiverCity() {
		return goodReceiverCity;
	}

	public void setGoodReceiverCity(String goodReceiverCity) {
		this.goodReceiverCity = goodReceiverCity;
	}

	public String getGoodReceiverCounty() {
		return goodReceiverCounty;
	}

	public void setGoodReceiverCounty(String goodReceiverCounty) {
		this.goodReceiverCounty = goodReceiverCounty;
	}

	public String getGoodReceiverAddress() {
		return goodReceiverAddress;
	}

	public void setGoodReceiverAddress(String goodReceiverAddress) {
		this.goodReceiverAddress = goodReceiverAddress;
	}

	public String getGoodReceiverMobile() {
		return goodReceiverMobile;
	}

	public void setGoodReceiverMobile(String goodReceiverMobile) {
		this.goodReceiverMobile = goodReceiverMobile;
	}

	public String getChildNum() {
		return childNum;
	}

	public void setChildNum(String childNum) {
		this.childNum = childNum;
	}

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public String getPayServiceType() {
		return payServiceType;
	}

	public void setPayServiceType(String payServiceType) {
		this.payServiceType = payServiceType;
	}

	public List<Bank> getBankList() {
		return bankList;
	}

	public void setBankList(List<Bank> bankList) {
		this.bankList = bankList;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public List<ProPackage> getProPackageList() {
		return proPackageList;
	}

	public void setProPackageList(List<ProPackage> proPackageList) {
		this.proPackageList = proPackageList;
	}

	public String getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(String payableAmount) {
		this.payableAmount = payableAmount;
	}

	public String getOrderDeliveryFee() {
		return orderDeliveryFee;
	}

	public void setOrderDeliveryFee(String orderDeliveryFee) {
		this.orderDeliveryFee = orderDeliveryFee;
	}

	public String getStoreLogoUrl() {
		return storeLogoUrl;
	}

	public void setStoreLogoUrl(String storeLogoUrl) {
		this.storeLogoUrl = storeLogoUrl;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOrderNeedInvoice() {
		return orderNeedInvoice;
	}

	public void setOrderNeedInvoice(String orderNeedInvoice) {
		this.orderNeedInvoice = orderNeedInvoice;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getOrderPaidByCoupon() {
		return orderPaidByCoupon;
	}

	public void setOrderPaidByCoupon(String orderPaidByCoupon) {
		this.orderPaidByCoupon = orderPaidByCoupon;
	}
	
	
	
}
