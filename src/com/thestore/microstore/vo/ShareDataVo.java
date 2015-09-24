package com.thestore.microstore.vo;

import java.io.Serializable;

public class ShareDataVo implements Serializable{

	private static final long serialVersionUID = -5685791496975363191L;
	/**
	 * 分享ID
	 */
	private String shareId;	
	/**
	 * 1：V店铺首页，2：V商品详情页
	 */
	private Integer contentType;
	/**
	 * 微店店铺id
	 */
	private Integer vdianId;
	/**
	 * 微店商品id
	 */
	private Integer pminfoVdianId;
	/**
	 * 分享url
	 */
	private String shareUrl;
	/**
	 * 1：微信朋友圈，2：微信好友，3：QQ空间，4：QQ
	 */
	private Integer sharePlatform;
	
	public String getShareId() {
		return shareId;
	}
	public void setShareId(String shareId) {
		this.shareId = shareId;
	}
	public Integer getContentType() {
		return contentType;
	}
	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}
	public Integer getVdianId() {
		return vdianId;
	}
	public void setVdianId(Integer vdianId) {
		this.vdianId = vdianId;
	}
	public Integer getPminfoVdianId() {
		return pminfoVdianId;
	}
	public void setPminfoVdianId(Integer pminfoVdianId) {
		this.pminfoVdianId = pminfoVdianId;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	public Integer getSharePlatform() {
		return sharePlatform;
	}
	public void setSharePlatform(Integer sharePlatform) {
		this.sharePlatform = sharePlatform;
	}
	
}
