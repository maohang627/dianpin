package com.thestore.microstore.vo.coupon;



import com.thestore.microstore.vo.commom.BaseEntity;

/**
 * 抵用券返回结果对象
 * @author maohang
 *
 */
public class CouponResult extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private String msg;
	private String code;
	private String mobile;
	private ShoppingPaymentCoupon shoppingPaymentCoupon;
	
	private String paidByCoupon;//抵用券支付金额
	private String amountNeed2Pay;//共需支付
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public ShoppingPaymentCoupon getShoppingPaymentCoupon() {
		return shoppingPaymentCoupon;
	}
	public void setShoppingPaymentCoupon(ShoppingPaymentCoupon shoppingPaymentCoupon) {
		this.shoppingPaymentCoupon = shoppingPaymentCoupon;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPaidByCoupon() {
		return paidByCoupon;
	}
	public void setPaidByCoupon(String paidByCoupon) {
		this.paidByCoupon = paidByCoupon;
	}
	public String getAmountNeed2Pay() {
		return amountNeed2Pay;
	}
	public void setAmountNeed2Pay(String amountNeed2Pay) {
		this.amountNeed2Pay = amountNeed2Pay;
	}
	
	
}
