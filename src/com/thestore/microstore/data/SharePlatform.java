package com.thestore.microstore.data;

public enum SharePlatform {
	
	WECHATMOMENTS("1", "WechatMoments"),
	WECHAT("2", "Wechat"),
	QZONE("3", "QZone"),
	QQ("4", "QQ");	

  	public String id;
  	public String name;
  	
  	private SharePlatform(String id, String name) {
  		this.id = id;
  		this.name = name;
  	}

}
