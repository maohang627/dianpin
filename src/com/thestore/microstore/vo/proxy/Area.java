package com.thestore.microstore.vo.proxy;

import com.thestore.microstore.vo.commom.BaseEntity;


/**
 * 
 * 城市
 * 
 * 2014-9-9 上午9:12:52
 */
public class Area extends BaseEntity {

	private String id;
	
	private String value;
	
	private String postCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	
}
