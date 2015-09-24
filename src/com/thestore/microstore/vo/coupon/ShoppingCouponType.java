package com.thestore.microstore.vo.coupon;

public enum ShoppingCouponType {
	/**
	 * 以下类型的抵用券可以使用多张，相同活动编码的抵用券限用1张，不能与其他类型抵用券同时使用(免邮券除外)
	 */
	YHD_COUPON_TYPE_BRAND("11_brand","品牌券"),
	
	/**
	 * 以下类型的抵用券可以使用多张，相同活动编码的抵用券限用1张，不能与其他类型抵用券同时使用(免邮券除外)
	 */
	YHD_COUPON_TYPE_PRODUCT("12_product","单品券"),
	
	/**
	 * 以下类型的抵用券限用1张，不能与其他类型抵用券同时使用(免邮券除外)
	 */
	YHD_COUPON_TYPE_CATEGORY("13_category","品类券"),
	
	/**
	 * 以下类型的抵用券限用1张，可以与其他类型抵用券同时使用 
	 */
	YHD_COUPON_TYPE_DELIVERY_FEE("14_deliveryFee","免邮券"),
	
	/**
	 * 以下类型的抵用券限用1张，可以与其他类型抵用券同时使用 
	 */
	YHD_COUPON_TYPE_DELIVERY_ON_TIME_FEE("15_deliveryOnTimeFee","免准时达券"),
	
	/**
	 * 1MALL全场券，不显示说明
	 */
	YMALL_COUPON_TYPE_THRESHOLD("21_threshold", "1mall全场券"),
	
	/**
	 * 1mall商家券
	 */
	YMALL_COUPON_TYPE_MERCHANT("22_merchant", "1Mall商家券");
	
	private String type;
	private String desc;

	private ShoppingCouponType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
