package com.thestore.microstore.vo.shoppingcart;

public class CartItem {
	
	private String id;
	
	private String imageUrl;
	
	private String desc;
	
	private String color;
	
	private String size;
	
	private String count;
	
	private String price;
	
	private String vMerchantId;
	
	private String pmId;
	
	private String vPmId;
	
	private boolean checked;
		

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
	
	public String getvMerchantId() {
		return vMerchantId;
	}

	public void setvMerchantId(String vMerchantId) {
		this.vMerchantId = vMerchantId;
	}

	public String getPmId() {
		return pmId;
	}

	public void setPmId(String pmId) {
		this.pmId = pmId;
	}

	public String getvPmId() {
		return vPmId;
	}

	public void setvPmId(String vPmId) {
		this.vPmId = vPmId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

}
