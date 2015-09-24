package com.thestore.microstore.vo.proxy;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;

public class City extends BaseEntity {

	private String id;
	
	private String value;
	
	private List<Area> areaList;

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

	public List<Area> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}

	@Override
	public String toString() {
		return value;
	}
	
	
	
}
