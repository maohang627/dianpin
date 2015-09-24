package com.thestore.microstore.vo;

import java.util.HashMap;

import android.content.Context;

import com.thestore.microstore.parser.BaseParser;
import com.thestore.microstore.util.Config;
import com.thestore.microstore.util.CookiesUtil;

public class RequestVo {
	public String requestUrl;
	public Context context;
	public HashMap<String, String> requestDataMap;
	public BaseParser<?> jsonParser;
	public Object testObject;
	
	public String dufaultPosition;//省份 城市 区域 默认

	public RequestVo() {
	}

	public RequestVo(String requestUrl, Context context, HashMap<String, String> requestDataMap, BaseParser<?> jsonParser, Object testObject) {
		super();
		this.requestUrl = requestUrl;
		this.context = context;
		this.requestDataMap = requestDataMap;
		this.jsonParser = jsonParser;
		this.testObject = testObject;
		
	}
	
	/**
	 * 设置一些常用的参数
	 * @param context
	 * @return
	 */
	public static RequestVo setCommontParam(Context context) {
		RequestVo vo = new RequestVo();
		vo.context = context;
		HashMap<String, String> postParamMap = new HashMap<String, String>();
		postParamMap.put("provinceId", CookiesUtil.getProvinceId());
		postParamMap.put("interfaceVersion", Config.INTERFACE_VERSION);
		postParamMap.put("sessionId", Config.getUUid(context));
		postParamMap.put("userToken", CookiesUtil.getUT());
		vo.requestDataMap = postParamMap;
		return vo;
	}
}
