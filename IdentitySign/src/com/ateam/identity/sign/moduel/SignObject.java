package com.ateam.identity.sign.moduel;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

import com.ateam.identity.sign.util.Installation;


@Table(name=SignObject.TABLE_SIGN_OBJECT)
public class SignObject {
	@Transient
	public static final String TABLE_SIGN_OBJECT="tb_sign_object";
	
	private int id; 
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

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
