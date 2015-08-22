package com.m2a.dto;

import java.util.ArrayList;
import java.util.List;

public class CommonDTO {
	
	private List<String> errMsg;

	public List<String> getErrMsg() {
		if(errMsg==null){
			errMsg = new ArrayList<String>();
		}
		return errMsg;
	}

	public void setErrMsg(List<String> errMsg) {
		this.errMsg = errMsg;
	}
	
	public void addErrMsg(String error){
		if(errMsg == null) {
			errMsg = new ArrayList<String>();
		}
		errMsg.add(error);
	}
	
}
