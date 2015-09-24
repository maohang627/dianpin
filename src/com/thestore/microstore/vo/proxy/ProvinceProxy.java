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
public class ProvinceProxy extends BaseEntity {

	private int position;

	private List<Province> provinceList;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Province> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<Province> provinceList) {
		this.provinceList = provinceList;
	}

}
