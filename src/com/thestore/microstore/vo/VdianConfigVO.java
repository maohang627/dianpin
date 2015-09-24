package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

public class VdianConfigVO extends BaseEntity {

	private static final long serialVersionUID = 3237889352364932624L;
	/**
	 * App首页H5地址
	 */
	private String appIndexUrl;
	/**
	 * 1号店域名
	 */
	private String yhdDomain;
	/**
	 * 1号V店域名
	 */
	private String vdDomain;
	/**
	 * 订单明细H5地址
	 */
	private String vdianOrderDetailUrl;
    /**
     * 我的收入H5地址
     */
	private String incomeListUrl;
	/**
	 * 1号V店 Android App 下载地址
	 */
	private String apkDownLoadUrl;
	/**
	 * 我也要开店H5地址
	 */
	private String createStartUrl;
	/**
	 * 我的店铺H5地址
	 */
	private String sellerIndexUrl;
	/**
	 * 收货地址个数上限
	 */
	private int goodReceiverMaxNum;
	/**
	 * 我的微店已失效商品tab页H5地址
	 */
	private String soldOutUrl;
	/**
	 * 我的微店页面（商品无库存）
	 */
	private String noStockUrl;

	
	public String getAppIndexUrl() {
		return appIndexUrl;
	}
	public void setAppIndexUrl(String appIndexUrl) {
		this.appIndexUrl = appIndexUrl;
	}
	public String getYhdDomain() {
		return yhdDomain;
	}
	public void setYhdDomain(String yhdDomain) {
		this.yhdDomain = yhdDomain;
	}
	public String getVdDomain() {
		return vdDomain;
	}
	public void setVdDomain(String vdDomain) {
		this.vdDomain = vdDomain;
	}
	public String getVdianOrderDetailUrl() {
		return vdianOrderDetailUrl;
	}
	public void setVdianOrderDetailUrl(String vdianOrderDetailUrl) {
		this.vdianOrderDetailUrl = vdianOrderDetailUrl;
	}
	public String getIncomeListUrl() {
		return incomeListUrl;
	}
	public void setIncomeListUrl(String incomeListUrl) {
		this.incomeListUrl = incomeListUrl;
	}
	public String getApkDownLoadUrl() {
		return apkDownLoadUrl;
	}
	public void setApkDownLoadUrl(String apkDownLoadUrl) {
		this.apkDownLoadUrl = apkDownLoadUrl;
	}
	public String getCreateStartUrl() {
		return createStartUrl;
	}
	public void setCreateStartUrl(String createStartUrl) {
		this.createStartUrl = createStartUrl;
	}
	public String getSellerIndexUrl() {
		return sellerIndexUrl;
	}
	public void setSellerIndexUrl(String sellerIndexUrl) {
		this.sellerIndexUrl = sellerIndexUrl;
	}
	public int getGoodReceiverMaxNum() {
		return goodReceiverMaxNum;
	}
	public void setGoodReceiverMaxNum(int goodReceiverMaxNum) {
		this.goodReceiverMaxNum = goodReceiverMaxNum;
	}
	public String getSoldOutUrl() {
		return soldOutUrl;
	}
	public void setSoldOutUrl(String soldOutUrl) {
		this.soldOutUrl = soldOutUrl;
	}
	public String getNoStockUrl() {
		return noStockUrl;
	}
	public void setNoStockUrl(String noStockUrl) {
		this.noStockUrl = noStockUrl;
	}	
	
}
