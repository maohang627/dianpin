package com.thestore.microstore.vo;

import java.io.Serializable;

public class ClientInfoVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5495692061195868471L;

	//客户端程序版本号
	private String clientAppVersion;    
	
	//客户端系统类型 android/ios
	private String clientSystem;
	
	//客户端OS系统版本、机型
	private String clientVersion;    
	
	//客户端唯一标识
	private String deviceCode;
	
	//经度
	private Double longitude;
	
	//纬度
	private Double latitude;    
	
	//平台标识： 手机、平板、团购、闪购、
	private String traderName;
	
	//渠道标识
	private String chl;  
	
	//网络标识Wifi,3g,2g,4g
	private String nettype;
	
	//多机房接口位置 1--上海， 0--北京
	private String iaddr;
	
	private String interfaceVersion;
	
	private String clientip;
	
	//分辨率
	private String rsl;
	
	//登录手机号
	private String mbl;
	
	
	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public String getClientAppVersion() {
		return clientAppVersion;
	}
	
	public void setClientAppVersion(String clientAppVersion) {
		this.clientAppVersion = clientAppVersion;
	}
	
	public String getClientSystem() {
		return clientSystem;
	}
	
	public void setClientSystem(String clientSystem) {
		this.clientSystem = clientSystem;
	}
	
	public String getClientVersion() {
		return clientVersion;
	}
	
	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}
	
	public String getDeviceCode() {
		return deviceCode;
	}
	
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getTraderName() {
		return traderName;
	}
	
	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}	
	
	public String getChl() {
		return chl;
	}

	public void setChl(String chl) {
		this.chl = chl;
	}

	public String getNettype() {
		return nettype;
	}
	
	public void setNettype(String nettype) {
		this.nettype = nettype;
	}
	
	public String getIaddr() {
		return iaddr;
	}
	
	public void setIaddr(String iaddr) {
		this.iaddr = iaddr;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public String getRsl() {
		return rsl;
	}

	public void setRsl(String rsl) {
		this.rsl = rsl;
	}

	public String getMbl() {
		return mbl;
	}

	public void setMbl(String mbl) {
		this.mbl = mbl;
	} 
	
}
