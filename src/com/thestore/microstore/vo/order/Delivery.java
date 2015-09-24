package com.thestore.microstore.vo.order;

import com.thestore.microstore.vo.commom.BaseEntity;

public class Delivery extends BaseEntity {

	//配送方式ID
	private String deliveryMethodId;
	
	//运费
	private String fee;
	
	/**
	 * 用于表示配送方式、包裹等在订单中的位置，保存配送方式时用到 
	 * orderMark = merchantId + "_" +deliveryGroupId + "_" +packageId \
	 * 例如：某配送方式的orderMark=1_1_2,表示这个配送方式在：第1个商家的第1个配送组第2个包裹
	 */
	private String orderMark;
	
	//订单页 用户输入的。 格式为 ：  "微信号|备注"
	private String remarks;

	public String getDeliveryMethodId() {
		return deliveryMethodId;
	}

	public void setDeliveryMethodId(String deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOrderMark() {
		return orderMark;
	}

	public void setOrderMark(String orderMark) {
		this.orderMark = orderMark;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
