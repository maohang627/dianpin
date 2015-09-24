package com.thestore.microstore.vo.order;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;

public class ProPackage extends BaseEntity {

	private String name;

	private String desc;

	private String priceDesc;
	
	private String expectReceiveDate;
	
	private Integer quantity;

	//配送方式
	private Delivery delivery;

	private List<OrderPro> proList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPriceDesc() {
		return priceDesc;
	}

	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}

	public List<OrderPro> getProList() {
		return proList;
	}

	public void setProList(List<OrderPro> proList) {
		this.proList = proList;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public String getExpectReceiveDate() {
		return expectReceiveDate;
	}

	public void setExpectReceiveDate(String expectReceiveDate) {
		this.expectReceiveDate = expectReceiveDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
