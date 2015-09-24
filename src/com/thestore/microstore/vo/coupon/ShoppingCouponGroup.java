package com.thestore.microstore.vo.coupon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCouponGroup implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
     * 抵用券类型
     * @see CouponGroupEnum
     */
    private String couponType;
    /**
     * 抵用券类型说明
     */
    private String couponTypeDesc;

    /**
     * 互斥的抵用券列表
     */
    private List<ShoppingCouponVo> mutexCouponList = new ArrayList<ShoppingCouponVo>();

    /**
     * 同时选用的抵用券列表
     */
    private List<ShoppingCouponVo> multipleCouponList = new ArrayList<ShoppingCouponVo>();
    
    private Long merchantId;

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponTypeDesc() {
		return couponTypeDesc;
	}

	public void setCouponTypeDesc(String couponTypeDesc) {
		this.couponTypeDesc = couponTypeDesc;
	}

	public List<ShoppingCouponVo> getMutexCouponList() {
		return mutexCouponList;
	}

	public void setMutexCouponList(List<ShoppingCouponVo> mutexCouponList) {
		this.mutexCouponList = mutexCouponList;
	}

	public List<ShoppingCouponVo> getMultipleCouponList() {
		return multipleCouponList;
	}

	public void setMultipleCouponList(List<ShoppingCouponVo> multipleCouponList) {
		this.multipleCouponList = multipleCouponList;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
    
    
    

}
