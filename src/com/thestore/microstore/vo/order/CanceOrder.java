package com.thestore.microstore.vo.order;

import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.vo.commom.BaseEntity;

public class CanceOrder extends BaseEntity{
	
	private String isCancel;//1可以取消
	
	private JSONObject reason;
	
	private String code;//1 取消订单成功
	
	private String reasonIds;//取消原因ids  1,2, 

	public String getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}

	public JSONObject getReason() {
		return reason;
	}

	public void setReason(JSONObject reason) {
		this.reason = reason;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReasonIds() {
		return reasonIds;
	}

	public void setReasonIds(String reasonIds) {
		this.reasonIds = reasonIds;
	}
	
	

}
