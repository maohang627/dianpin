package com.thestore.microstore.vo.proxy;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;




/**
 * 
 * 省份
 * 
 *
 * 2014-9-9 上午9:12:06
 */
public class Province extends BaseEntity {

	private String id;
	
	private String value;
	
	private List<City> cityList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<City> getCityList() {
		return cityList;
	}

	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
	
	@Override
	public String toString() {
		return value;
	}

	
}
