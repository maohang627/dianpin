package com.thestore.microstore.vo;

import java.io.Serializable;

public class UserLevel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7084466796928414119L;

	private int last_level;
	private int level;

	private String msg = "";

	// "last_level": 0,
	// "level": 1,
	// "msg": "todo(预留，填空串)"

	public int getLast_level() {
		return last_level;
	}

	public void setLast_level(int last_level) {
		this.last_level = last_level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	//
}
