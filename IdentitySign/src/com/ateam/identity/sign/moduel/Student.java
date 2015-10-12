package com.ateam.identity.sign.moduel;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

//学生对象
@Table(name=Student.TABLE_STUDENT)
public class Student implements Serializable{
	@Transient
	public static final String TABLE_STUDENT="tb_student";
	@Transient
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String teacherID;//老师的身份证号
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
		return "Student [id=" + id + ", teacherID=" + teacherID + ", cardNum="
				+ cardNum + ", name=" + name + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
