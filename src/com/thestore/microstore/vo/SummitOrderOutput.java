package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

public class SummitOrderOutput extends BaseEntity {

	// 订单code 唯一标识一个订单
	private String orderCode;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}
