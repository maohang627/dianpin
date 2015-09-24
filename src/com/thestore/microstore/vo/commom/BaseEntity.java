package com.thestore.microstore.vo.commom;

import java.io.Serializable;

/**
 * 
 * 基础VO
 * 
 * 2014-9-6 下午2:37:45
 */
public class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 返回的code
	 */
	protected String rtnCode;

	/**
	 * 返回的信息
	 */
	protected String rtnMsg;

	public String getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode;
	}

	public String getRtnMsg() {
		return rtnMsg;
	}

	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

}
