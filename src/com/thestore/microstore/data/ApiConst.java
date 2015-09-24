package com.thestore.microstore.data;

public class ApiConst {

	private static final String IP = "interface.m.yihaodian.com";

	// interface.m.yihaodian.com 线上
	// 10.161.144.22:8080 测试
	// 10.161.163.29:8080 yeliang

	public static final String path = "http://" + IP
			+ "/centralmobile/mobileservice/addVUser.do";

	public static final String getPushInformationPath = "http://" + IP
			+ "/centralmobile/mobileservice/getPushInformation.do";

	public static final String ADDVUSER = "addVUser";

	public static final String GETPUSHINFORMATION = "getPushInformation";

	public static final String SETOPEN = "setOpen";

	public static final String setOpenPath = "http://" + IP
			+ "/centralmobile/mobileservice/setOpen.do";

}
