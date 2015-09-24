package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

public class OrderCreate extends BaseEntity {

	// 订单取消时间（分钟）
	private String cancelTime;

	// 剩余时间（秒）
	private String lefttime;

	// 订单编号
	private String orderCode;

	// 包裹数
	private String childNum;

	// 商品数
	private String productCount;

	// 金额
	private String orderAmount;

	// 支付方式）
	// 0：账户支付 1：网上支付 2：货到付款 3：邮局汇款 4：银行转账 5：pos机 6：万里通 7：分期付款 8：合同账期 9：货到转账 10：货到付支票, 目前微店只有1 2 5
	private String payServiceType;

	// 业务类型， 14判断订单为手机订单
	private String businessType;

	// 订单类型
	private String orderType;

	// 订单状态
	private String orderStatus;

	// 订单状态String
	private String orderStatusString;

	public String getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}

	public String getLefttime() {
		return lefttime;
	}

	public void setLefttime(String lefttime) {
		this.lefttime = lefttime;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getPayServiceType() {
		return payServiceType;
	}

	public void setPayServiceType(String payServiceType) {
		this.payServiceType = payServiceType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
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

}
