package com.thestore.microstore.vo.order;

import com.thestore.microstore.vo.commom.BaseEntity;


/**
 * 
 *	订单商品
 * 2014-9-14 下午10:37:14
 */
public class OrderPro extends BaseEntity {

	private String name;
	
	private String desc;

	private String color;

	private String size;

	private String count;

	private String price;

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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	
}
