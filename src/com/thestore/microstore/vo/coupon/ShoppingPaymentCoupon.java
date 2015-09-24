package com.thestore.microstore.vo.coupon;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;

public class ShoppingPaymentCoupon extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**
     * 可用抵用券数量 
     */
    private int useableCouponNum = 0;

    /**
     * 抵用券分组 
     */
    private List<ShoppingCouponGroup> couponGroups;

	public int getUseableCouponNum() {
		return useableCouponNum;
	}

	public void setUseableCouponNum(int useableCouponNum) {
		this.useableCouponNum = useableCouponNum;
	}

	public List<ShoppingCouponGroup> getCouponGroups() {
		return couponGroups;
	}

	public void setCouponGroups(List<ShoppingCouponGroup> couponGroups) {
		this.couponGroups = couponGroups;
	}
    
    
}
