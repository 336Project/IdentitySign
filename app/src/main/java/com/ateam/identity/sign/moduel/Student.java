package com.ateam.identity.sign.moduel;

import java.io.Serializable;
import com.ateam.identity.sign.util.SysUtil;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

//学生对象
@Table(name=Student.TABLE_STUDENT)
public class Student implements Serializable{
	@Transient
	public static final String TABLE_STUDENT="tb_student";
	@Transient
	private static final long serialVersionUID = 1L;
	@Transient
	private String sortLetters;  //显示数据拼音的首字母
	@Transient
	private Boolean ifSelect=false;//是否被选中
	
	private int id;
	private String name;//学生名字
	private String tCardNum;//老师的身份证号
	private String cardNum;//学生身份证号
	private String classroom;//所在班级
	private String sex;//性别
	private String telephone;//手机号

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTCardNum() {
		return tCardNum;
	}

	public void setTCardNum(String tCardNum) {
		this.tCardNum = tCardNum;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public Boolean getIfSelect() {
		return ifSelect;
	}

	public void setIfSelect(Boolean ifSelect) {
		this.ifSelect = ifSelect;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = SysUtil.formate(classroom);
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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
		this.name = SysUtil.formate(name);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", tCardNum=" + tCardNum + ", cardNum="
				+ cardNum + ", name=" + name + ", classroom=" + classroom
				+ ", sex=" + sex + "]";
	}
	
}
