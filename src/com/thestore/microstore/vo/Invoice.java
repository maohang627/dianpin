package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

public class Invoice extends BaseEntity {

	//发票类型 个人 公司
	private String type;
	
	//发票抬头
	private String title;
	
	//发票内容
	private String content;
	
	//提交订单的随机码 
	private String orderRundomString;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOrderRundomString() {
		return orderRundomString;
	}

	public void setOrderRundomString(String orderRundomString) {
		this.orderRundomString = orderRundomString;
	} 
	
}
