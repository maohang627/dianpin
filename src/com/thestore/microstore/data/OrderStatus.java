package com.thestore.microstore.data;

/**
 * 3 等待支付， 4 已支付，20 已发货， 24已发货，34 交易取消，35 交易完成 ，36 订单待审核， 37 已发货， 38 待发货
 * @author zhaojianjian
 *
 */
public enum OrderStatus {
	
	WAITING_FOR_PAYMENT("3", "等待支付"),
	PAID("4", "已支付"),
	TRANSACTION_CANCELED("34", "交易取消"),
	TRANSACTION_COMPLETION("35", "交易完成"),
	PENDING_ORDERS("36", "订单待审核"),
	TO_BE_SHIPPED("38", "待发货");	

  	public String code;
  	public String name;
  	
  	private OrderStatus(String code, String name) {
  		this.code = code;
  		this.name = name;
  	}

}
