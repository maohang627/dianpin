package com.thestore.microstore.vo.proxy;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;

/**
 * 
 * 银行
 * 
 * 2014-9-24 下午12:06:08
 */
public class BankProxy extends BaseEntity {

	private int position;

	private List<Bank> bankList;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Bank> getBankList() {
		return bankList;
	}

	public void setBankList(List<Bank> bankList) {
		this.bankList = bankList;
	}
}
