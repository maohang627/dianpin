package com.thestore.microstore.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.proxy.Bank;
import com.thestore.microstore.vo.proxy.BankProxy;

/**
 * 
 * 银行信息 信息获取
 * 
 * 2014-9-24 下午2:02:16
 */
public class BankParser extends BaseParser<BankProxy> {

	private static final String TAG = "vdian_ResultParser";

	private String bankCode = "";

	public BankParser() {

	}

	public BankParser(String bankCode) {
		this.bankCode = bankCode;
	}

	@Override
	public BankProxy parseJSON(String paramString) throws JSONException {

		BankProxy result = new BankProxy();

		CheckResponse response = checkResponse(result, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, "BankParser 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, "BankParser 调用接口出错.");

			return (BankProxy) response.getBaseEntity();
		}

		List<Bank> bankList = new ArrayList<Bank>();
		JSONArray dataList = JSON.parseObject(paramString).getJSONArray("data");

		int position = 0;
		for (int i = 0; i < dataList.size(); i++) {
			JSONObject data = (JSONObject) dataList.get(i);

			Bank bank = new Bank();
			String curBankCode = data.getString("bankCode");
			if (TextUtils.isEmpty(bankCode)) {
				if ("alipay".equals(curBankCode)) {
					position = i;
				}
			} else {

				if (bankCode.equals(curBankCode)) {
					position = i;
				}
			}

			bank.setCode(curBankCode);
			bank.setGatewayId(data.getString("gatewayId"));
			bank.setImgUrl(data.getString("imgUrl"));
			bank.setName(data.getString("name"));
			bank.setRemark(data.getString("remark"));
			bank.setType(data.getString("bankType"));

			bankList.add(bank);
		}

		result.setPosition(position); // position 始终不为空
		result.setBankList(bankList);

		return result;
	}

}
