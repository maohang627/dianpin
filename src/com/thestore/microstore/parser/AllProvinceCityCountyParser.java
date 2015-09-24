package com.thestore.microstore.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.thestore.microstore.util.Logger;
import com.thestore.microstore.vo.commom.CheckResponse;
import com.thestore.microstore.vo.proxy.Area;
import com.thestore.microstore.vo.proxy.City;
import com.thestore.microstore.vo.proxy.MyyhdCityVo;
import com.thestore.microstore.vo.proxy.MyyhdCountyVo;
import com.thestore.microstore.vo.proxy.MyyhdProvinceVo;
import com.thestore.microstore.vo.proxy.Province;
import com.thestore.microstore.vo.proxy.ProvinceProxy;

public class AllProvinceCityCountyParser extends BaseParser<ProvinceProxy> {
	private static final String TAG = "AllProvinceCityCountyParser";

	@Override
	public ProvinceProxy parseJSON(String paramString) throws JSONException {
		ProvinceProxy result = new ProvinceProxy();
		CheckResponse response = checkResponse(result, paramString);
		if (response.getResult() == -1) {
			Log.e(TAG, "AllProvinceCityCountyParser 返回空.");

			return null;
		} else if (response.getResult() == 1) {
			Log.e(TAG, "AllProvinceCityCountyParser 调用接口出错.");

			return (ProvinceProxy) response.getBaseEntity();
		}
		
		JSONObject root = JSON.parseObject(paramString);
		String dataString = root.getString("data");
		
		List<MyyhdProvinceVo> myyhdProvinceList = JSON.parseObject(dataString,
				new TypeToken<List<MyyhdProvinceVo>>() {
				}.getType());
		
		int position = 0;
		List<Province> provinceList = new ArrayList<Province>();
		for (int i = 0; i < myyhdProvinceList.size(); i++) {
			MyyhdProvinceVo provinceVo = myyhdProvinceList.get(i);

			Province province = new Province();
			province.setId(String.valueOf(provinceVo.getId()));
			province.setValue(provinceVo.getProvinceName());
			
			List<City> cityList = new ArrayList<City>();
			for(MyyhdCityVo myyhdCityVo : provinceVo.getCityVoList()){
				City city = new City();
				city.setId(String.valueOf(myyhdCityVo.getId()));
				city.setValue(myyhdCityVo.getCityName());
				
				List<Area> areaList = new ArrayList<Area>();
				for(MyyhdCountyVo myyhdCountyVo : myyhdCityVo.getCountyVoList()){
					Area area = new Area();
					area.setId(String.valueOf(myyhdCountyVo.getId()));
					area.setValue(myyhdCountyVo.getCountyName());
					areaList.add(area);
				}
				city.setAreaList(areaList);		
				
				cityList.add(city);
			}
			province.setCityList(cityList);
			
			provinceList.add(province);
		
		}
		
		result.setPosition(position);
		result.setProvinceList(provinceList);
		
		return result;
	}

}
