package com.ateam.identity.sign.moduel;

public class HBaseObject{
	private String statusCode;//状态代码
	private String message;//状态信息
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		if(message!=null){
			message=new String(message.getBytes());
		}
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
