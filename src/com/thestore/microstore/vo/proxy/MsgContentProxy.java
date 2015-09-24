package com.thestore.microstore.vo.proxy;

import java.io.Serializable;

import com.thestore.microstore.vo.MsgContent;

public class MsgContentProxy implements Serializable {

	private static final long serialVersionUID = -8049169111734018227L;
	
	private MsgContent msgContent;

	public MsgContent getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(MsgContent msgContent) {
		this.msgContent = msgContent;
	}

}
