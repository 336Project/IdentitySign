package com.ateam.identity.sign.moduel;

import com.ateam.identity.sign.util.Installation;

public class SignObject {
	private String idcard;
	private String ip;
	private String attendanceDate;//sign time
	
	public SignObject(){
		ip = Installation.getAppId();
	}
	
	public SignObject(String idcard,String attendanceDate){
		this.idcard = idcard;
		this.attendanceDate = attendanceDate;
		ip = Installation.getAppId();
	}
	
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getAttendanceDate() {
		return attendanceDate;
	}
	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}
	public String getIp() {
		return ip;
	}
}
