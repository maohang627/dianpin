package com.thestore.microstore.vo.proxy;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;

public class CityProxy extends BaseEntity {

	private int position;

	private List<City> cityList;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<City> getCityList() {
		return cityList;
	}

	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}

}
