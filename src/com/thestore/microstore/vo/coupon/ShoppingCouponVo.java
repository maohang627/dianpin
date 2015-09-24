package com.thestore.microstore.vo.coupon;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCouponVo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int USED = 1;// 已经被使用
	public static final int UNUSED = 0;// 未被使用

	/** 抵用券号 */
	private String couponNumber;

	/** 抵扣金额 */
	private String amount;

	/** 有效开始时间（仅用于公有券和系列抵用券） */
	private String beginTime;

	/** 有效结束时间（仅用于公有券和系列抵用券） */
	private String expiredTime;

	/** 活动范围类型（仅用于公有券） 0:全场 1:产品 2:分类 3:品牌 */
	private Integer defineType;

	/** 抵用券前缀 */
	private String prefix;

	/** 是否已经使用 */
	private boolean used;
	/** 是否可用 */
	private boolean canUse;
	/**
	 * 抵用券类型： 1：互斥 2：多选
	 */
	private int type;
	/**
	 * 描述信息 ，分段提示信息，格式如下： [ "指定的", { "desc": "分类" "details"：["母婴营养品", "洗护清洁",
	 * "尿裤/湿巾"] }, "满￥199元可用；不支持团购、预售、礼品卡" ]
	 */
	private List<String> description = new ArrayList<String>();

	public String getCouponNumber() {
		return couponNumber;
	}

	public void setCouponNumber(String couponNumber) {
		this.couponNumber = couponNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public Integer getDefineType() {
		return defineType;
	}

	public void setDefineType(Integer defineType) {
		this.defineType = defineType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isCanUse() {
		return canUse;
	}

	public void setCanUse(boolean canUse) {
		this.canUse = canUse;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	

}
