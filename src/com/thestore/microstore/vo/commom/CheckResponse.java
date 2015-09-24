package com.thestore.microstore.vo.commom;


public class CheckResponse {

	
	// 0 正常 1 和 -1 都需要返回
	private int result;
	
	private BaseEntity baseEntity;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public BaseEntity getBaseEntity() {
		return baseEntity;
	}

	public void setBaseEntity(BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
	}
}
