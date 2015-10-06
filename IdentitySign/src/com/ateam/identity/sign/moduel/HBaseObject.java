package com.ateam.identity.sign.moduel;

public class HBaseObject{
	private boolean isSuccess;//
	private String message;//状态信息
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
}
