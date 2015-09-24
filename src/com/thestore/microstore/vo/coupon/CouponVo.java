package com.thestore.microstore.vo.coupon;

import java.util.ArrayList;
import java.util.List;

public class CouponVo {
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

	/**
	 * 抵用券类型说明
	 */
	private String couponTypeDesc;

	/**
	 * 拥有该商家抵用券数量
	 */
	private int cardNum;

	/** 是否显示头部 */
	private boolean isShowHead = false;

	/**
	 * 描述信息 ，分段提示信息，格式如下： [ "指定的", { "desc": "分类" "details"：["母婴营养品", "洗护清洁",
	 * "尿裤/湿巾"] }, "满￥199元可用；不支持团购、预售、礼品卡" ]
	 */
	public List<String> getDescription() {
		return description;
	}

	/**
	 * 描述信息 ，分段提示信息，格式如下： [ "指定的", { "desc": "分类" "details"：["母婴营养品", "洗护清洁",
	 * "尿裤/湿巾"] }, "满￥199元可用；不支持团购、预售、礼品卡" ]
	 */
	public void setDescription(List<String> description) {
		this.description = description;
	}

	/**
	 * 获取 是否已经使用
	 * 
	 * @return isUsed 是否已经使用
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * 设置 是否已经使用
	 * 
	 * @param isUsed
	 *            是否已经使用
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}

	/**
	 * 获取 是否可用
	 * 
	 * @return isCanUse 是否可用
	 */
	public boolean isCanUse() {
		return canUse;
	}

	/**
	 * 设置 是否可用
	 * 
	 * @param isCanUse
	 *            是否可用
	 */
	public void setCanUse(boolean canUse) {
		this.canUse = canUse;
	}

	/**
	 * 获取 抵用券号
	 * 
	 * @return couponNumber 抵用券号
	 */
	public String getCouponNumber() {
		return couponNumber;
	}

	/**
	 * 设置 抵用券号
	 * 
	 * @param couponNumber
	 *            抵用券号
	 */
	public void setCouponNumber(String couponNumber) {
		this.couponNumber = couponNumber;
	}

	/**
	 * 获取 抵扣金额
	 * 
	 * @return amount 抵扣金额
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * 设置 抵扣金额
	 * 
	 * @param amount
	 *            抵扣金额
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * 获取有效开始时间（仅用于公有券和系列抵用券）
	 * 
	 * @return beginTime 有效开始时间（仅用于公有券和系列抵用券）
	 */
	public String getBeginTime() {
		return beginTime;
	}

	/**
	 * 设置有效开始时间（仅用于公有券和系列抵用券）
	 * 
	 * @param beginTime
	 *            有效开始时间（仅用于公有券和系列抵用券）
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * 获取有效结束时间（仅用于公有券和系列抵用券）
	 * 
	 * @return expiredTime 有效结束时间（仅用于公有券和系列抵用券）
	 */
	public String getExpiredTime() {
		return expiredTime;
	}

	/**
	 * 设置有效结束时间（仅用于公有券和系列抵用券）
	 * 
	 * @param expiredTime
	 *            有效结束时间（仅用于公有券和系列抵用券）
	 */
	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	/**
	 * 获取 活动范围类型（仅用于公有券）0:全场1:产品2:分类3:品牌
	 * 
	 * @return defineType 活动范围类型（仅用于公有券）0:全场1:产品2:分类3:品牌
	 */
	public Integer getDefineType() {
		return defineType;
	}

	/**
	 * 设置 活动范围类型（仅用于公有券）0:全场1:产品2:分类3:品牌
	 * 
	 * @param defineType
	 *            活动范围类型（仅用于公有券）0:全场1:产品2:分类3:品牌
	 */
	public void setDefineType(Integer defineType) {
		this.defineType = defineType;
	}

	/**
	 * 获取 抵用券前缀
	 * 
	 * @return prefix 抵用券前缀
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 设置 抵用券前缀
	 * 
	 * @param prefix
	 *            抵用券前缀
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 获取 抵用券类型：1：互斥2：多选
	 * 
	 * @return type 抵用券类型：1：互斥2：多选
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置 抵用券类型：1：互斥2：多选
	 * 
	 * @param type
	 *            抵用券类型：1：互斥2：多选
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getCouponTypeDesc() {
		return couponTypeDesc;
	}

	public void setCouponTypeDesc(String couponTypeDesc) {
		this.couponTypeDesc = couponTypeDesc;
	}

	public int getCardNum() {
		return cardNum;
	}

	public void setCardNum(int cardNum) {
		this.cardNum = cardNum;
	}

	public boolean isShowHead() {
		return isShowHead;
	}

	public void setShowHead(boolean isShowHead) {
		this.isShowHead = isShowHead;
	}

}
