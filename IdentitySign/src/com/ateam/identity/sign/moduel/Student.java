package com.ateam.identity.sign.moduel;

import java.io.Serializable;

//学生对象
public class Student implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String teacherID;
	private String cardNum;//学生身份证号
	private String name;//学生名字
	
	
	public String getTeacherID() {
		return teacherID;
	}

	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student [cardNum=" + cardNum + ", name=" + name + "]";
	}
	
}
