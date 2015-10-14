package com.ateam.identity.sign.moduel;

import java.io.Serializable;

import com.ateam.identity.sign.util.SysUtil;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name=User.TABLE_USER)
public class User implements Serializable{
	
	@Transient
	private static final long serialVersionUID = 1L;

	@Transient
	public static final String TABLE_USER="tb_user";
	
	@Transient
	private boolean isSuccess;//
	
	@Transient
	private String message;//状态信息
	
	private int id;
	private String name; //姓名
	private String username; //账号
	private String password; //密码
	private String cardNum; //身份证
	private String sex; //性别
	private String classroom; //班级
	private String school;//学校
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = SysUtil.formate(name);
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = SysUtil.formate(classroom);
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = SysUtil.formate(school);
	}
}
