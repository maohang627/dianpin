package com.thestore.microstore.vo.proxy;

import java.io.Serializable;
import java.util.List;

public class MyyhdProvinceVoProxy implements Serializable{

	private static final long serialVersionUID = -5674086588917062982L;
	
	private List<MyyhdProvinceVo> provinceVoList;

	public List<MyyhdProvinceVo> getProvinceVoList() {
		return provinceVoList;
	}

	public void setProvinceVoList(List<MyyhdProvinceVo> provinceVoList) {
		this.provinceVoList = provinceVoList;
	}
	
}
