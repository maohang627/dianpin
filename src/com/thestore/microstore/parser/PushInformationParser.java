package com.thestore.microstore.parser;

import java.util.List;

import org.json.JSONException;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thestore.main.core.net.bean.ResultVO;
import com.thestore.microstore.util.Logger;
import com.yihaodian.mobile.vo.push.PushInformationVO;

public class PushInformationParser extends BaseParser<ResultVO<List<PushInformationVO>>> {
	
	private static final String TAG = "PushInformationParser";

	@Override
	public ResultVO<List<PushInformationVO>> parseJSON(String paramString) throws JSONException {
		
//		paramString = "{\"data\":[{\"id\":\"201501271811572271605482\",\"msgContent\":\"\",\"sendDate\":1422353508003}],\"rtn_code\":0,\"rtn_msg\":\"success\"}";
//		paramString = paramString.replace("{}", "{\"msgContent\":{\"pId\":\"\",\"pType\":2,\"pd\":\"yihaovdian_create\",\"userLevel\":{\"last_level\":0,\"level\":1,\"msg\":\"温馨提醒：您还没有绑定银行卡账号。马上登陆1号V店APP，点击“我的收入”即可添加银行卡，确保您可以及时提款。\"}}}");
		
		if(paramString == null) {
			Logger.d(TAG, "PushInformationParser, 返回结果为空.");
			return null;
		}
		
		ResultVO<List<PushInformationVO>> result = new Gson().fromJson(paramString,
				new TypeToken<ResultVO<List<PushInformationVO>>>() {
				}.getType());
		
		return result;
	}
	
}
