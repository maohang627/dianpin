package com.thestore.microstore.vo.proxy;

import java.util.List;

import com.thestore.microstore.vo.commom.BaseEntity;


/**
 * 
 * 城市
 * 
 * 2014-9-9 上午9:12:52
 */
public class AreaProxy extends BaseEntity {

	private int position;
	
	List<Area> areaList;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public List<Area> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}


	
}
