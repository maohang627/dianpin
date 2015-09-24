package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

/**
 * 返回结果信息
 *
 * 2014-9-17 下午1:41:48
 */
public class Result extends BaseEntity {
	
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Result [rtnCode=" + rtnCode + ", rtnMsg=" + rtnMsg + "]";
	}

}
