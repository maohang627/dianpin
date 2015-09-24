package com.thestore.microstore.vo;

import java.io.Serializable;

public class MsgContent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2530587044166030046L;
	private UserLevel userLevel;
	private int pType;
	private String pd = "";
	private String pId = "";

	public UserLevel getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(UserLevel userLevel) {
		this.userLevel = userLevel;
	}

	public int getpType() {
		return pType;
	}

	public void setpType(int pType) {
		this.pType = pType;
	}

	public String getPd() {
		return pd;
	}

	public void setPd(String pd) {
		this.pd = pd;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

}
