package com.thestore.microstore.vo.order;

import com.thestore.microstore.vo.commom.BaseEntity;

public class InvoicesCommDisplay extends BaseEntity {

	private static final long serialVersionUID = 7370483673244701320L;
	
	// 发票类型
	private String titleType;
	// 发票抬头
	private String title;
	// 发票内容
	private String content;
	
	public String getTitleType() {
		return titleType;
	}
	public void setTitleType(String titleType) {
		this.titleType = titleType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
