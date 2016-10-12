package com.ateam.identity.sign.moduel;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

import com.ateam.identity.sign.util.Installation;


@Table(name=SignObject.TABLE_SIGN_OBJECT)
public class SignObject {
	@Transient
	public static final String TABLE_SIGN_OBJECT="tb_sign_object";
	@Transient
	public static final String TYPE_AUTO = "1";
	@Transient
	public static final String TYPE_MANUL = "2";
	public int id;
	private String idcard;
	private String ip;
	private String attendanceDate;//sign time
	private String type;//1:刷卡签到2:手动签到
	
	public SignObject(){
		ip = Installation.getAppId();
	}
	
	public SignObject(String idcard,String attendanceDate,String type){
		this.idcard = idcard;
		this.attendanceDate = attendanceDate;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
