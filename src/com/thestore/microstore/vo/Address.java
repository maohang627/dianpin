package com.thestore.microstore.vo;

import com.thestore.microstore.vo.commom.BaseEntity;

/**
 * 
 * 我的地址实体
 * 
 * 2014-9-13 下午12:45:59
 */
public class Address extends BaseEntity {

	private static final long serialVersionUID = -449001239289326558L;

	private String id;

	private String name;
	
	private String provinceId;
	
	private String provinceName;
	
	private String cityId;
	
	private String cityName;
	
	private String areaId;
	
	private String areaName;

	private String address;

	private String addressDetail;

	private String tel;
	
	private String defaultReceiver;
	
	private boolean selected = Boolean.FALSE;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDefaultReceiver() {
		return defaultReceiver;
	}

	public void setDefaultReceiver(String defaultReceiver) {
		this.defaultReceiver = defaultReceiver;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
