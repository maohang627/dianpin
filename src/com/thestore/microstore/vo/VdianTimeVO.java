package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

public class VdianTimeVO extends BaseEntity {

	private static final long serialVersionUID = 4112466293646133749L;
	/**
	 * 秘钥
	 */
	private String keyword;
	/**
	 * 时间
	 */
	private Long timestamp;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
