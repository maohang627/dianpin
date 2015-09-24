package com.thestore.microstore.vo.proxy;

import com.thestore.microstore.vo.commom.BaseEntity;

/**
 * 
 * 银行
 * 
 * 2014-9-24 下午12:06:08
 */
public class Bank extends BaseEntity {

	// 银行编码
	private String code;

	// 银行类型
	private String type;

	// 支付网关
	private String gatewayId;

	// 银行名称
	private String name;

	// 备注
	private String remark;
	
	// 银行图片
	private String imgUrl;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
