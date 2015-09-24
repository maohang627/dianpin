package com.thestore.microstore.util;

import org.afinal.simplecache.ACache;

import android.content.Context;
import android.text.TextUtils;

import com.thestore.microstore.data.Const;
import com.thestore.microstore.vo.proxy.Area;
import com.thestore.microstore.vo.proxy.AreaProxy;
import com.thestore.microstore.vo.proxy.City;
import com.thestore.microstore.vo.proxy.CityProxy;
import com.thestore.microstore.vo.proxy.Province;
import com.thestore.microstore.vo.proxy.ProvinceProxy;

public class ProvinceCityCountyManager {

	private static ProvinceCityCountyManager sInstance = null; 
	private static ProvinceProxy allProvinceCityCounty = null;
	
	public synchronized static ProvinceCityCountyManager getInstance(Context context) {  
	    if (sInstance == null) {  
	    	sInstance = new ProvinceCityCountyManager();
	    	ACache mCache = ACache.get(context);
	    	allProvinceCityCounty = (ProvinceProxy) mCache.getAsObject(Const.CACHE_ALLPROVINCECITYCOUNTY);
	    }  
	    
	    return sInstance;  
	}  
	
	public ProvinceProxy getProvinces(String provinceId){
		ProvinceProxy allProvince = allProvinceCityCounty;
		if(allProvinceCityCounty != null && !TextUtils.isEmpty(provinceId)){
			for (int i = 0; i < allProvinceCityCounty.getProvinceList().size(); i++) {
				Province province = allProvinceCityCounty.getProvinceList().get(i);
				if(province != null && provinceId.equals(province.getId())){
					allProvince.setPosition(i);
					break;
				}
			}			
		}
		
		return allProvince;
	}
	
	/**
	 * 获取指定省份下的所有城市
	 * @param provinceId
	 * @param cityId
	 * @return CityProxy
	 */
	public CityProxy getCitys(String provinceId, String cityId){
		CityProxy allCity = new CityProxy();
		if(allProvinceCityCounty != null && !TextUtils.isEmpty(provinceId)){
			for (int i = 0; i < allProvinceCityCounty.getProvinceList().size(); i++) {
				Province province = allProvinceCityCounty.getProvinceList().get(i);
				if(province != null && provinceId.equals(province.getId())){
					allCity.setCityList(province.getCityList());
					// 默认选中城市ID
					if(!TextUtils.isEmpty(cityId)){
						for (int j = 0; j < province.getCityList().size(); j++) {
							City city = province.getCityList().get(j);
							if(city != null && cityId.equals(city.getId())){
								allCity.setPosition(j);							
								break;
							} // end if cityId
						} // end for CityList	
					}

					break;
				} // end if provinceId
			} // end for ProvinceList			
		}
		
		return allCity;
	}
	
	/**
	 * 获取指定省份城市下的所有区域
	 * @param provinceId
	 * @param cityId
	 * @param areaId
	 * @return AreaProxy
	 */
	public AreaProxy getAreas(String provinceId, String cityId, String areaId){
		AreaProxy allArea = new AreaProxy();
		if(allProvinceCityCounty != null && !TextUtils.isEmpty(provinceId) && !TextUtils.isEmpty(cityId)){
			for (int i = 0; i < allProvinceCityCounty.getProvinceList().size(); i++) {
				Province province = allProvinceCityCounty.getProvinceList().get(i);
				if(province != null && provinceId.equals(province.getId())){
					for (int j = 0; j < province.getCityList().size(); j++) {
						City city = province.getCityList().get(j);
						if(city != null && cityId.equals(city.getId())){
							allArea.setAreaList(city.getAreaList());
							// 默认选中区域ID
							if(!TextUtils.isEmpty(areaId)){
								for (int k = 0; k < city.getAreaList().size(); k++) {
									Area area = city.getAreaList().get(k);
									if(area != null && areaId.equals(area.getId())){
										allArea.setPosition(k);
										break;
									} // end if areaId								
								} // end for AreaList
							}
														
							break;
						} // end if cityId
					} // end for CityList
					
					break;
				} // end if provinceId
			} // end for ProvinceList			
		}
		
		return allArea;
	}

}
