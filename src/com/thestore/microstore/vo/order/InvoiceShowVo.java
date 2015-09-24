package com.thestore.microstore.vo.order;

import com.thestore.microstore.vo.commom.BaseEntity;

public class InvoiceShowVo extends BaseEntity {

	private static final long serialVersionUID = 423513856421583959L;
	
	// 是否包含普通商品
	private String isContainComm;
	// 是否包含要强制开发票商品
	private String isContainMust;
	
	
	public String getIsContainComm() {
		return isContainComm;
	}
	public void setIsContainComm(String isContainComm) {
		this.isContainComm = isContainComm;
	}
	public String getIsContainMust() {
		return isContainMust;
	}
	public void setIsContainMust(String isContainMust) {
		this.isContainMust = isContainMust;
	}

}
