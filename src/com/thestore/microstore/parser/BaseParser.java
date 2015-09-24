package com.thestore.microstore.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.thestore.microstore.vo.commom.BaseEntity;
import com.thestore.microstore.vo.commom.CheckResponse;

public abstract class BaseParser<T> {

	public abstract T parseJSON(String paramString) throws JSONException;

	/**
	 * 检查返回的json数据
	 * @param baseEntity
	 * @param paramString
	 * @return
	 * @throws JSONException
	 */
	public CheckResponse checkResponse(BaseEntity baseEntity, String paramString) throws JSONException {
		CheckResponse result = new CheckResponse();
		if (paramString == null) {
			result.setResult(-1);
		} else {
			JSONObject jsonObject = new JSONObject(paramString);
			String rtnCode = jsonObject.getString("rtn_code");
			baseEntity.setRtnCode(rtnCode);
			baseEntity.setRtnMsg(jsonObject.getString("rtn_msg"));
			result.setBaseEntity(baseEntity);
			
			if("0".equals(rtnCode)) {
				result.setResult(0);
				
			} else {
				result.setResult(1);
			}
			
		}
		return result;
	}
}
