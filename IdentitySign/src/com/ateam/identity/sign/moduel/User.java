package com.ateam.identity.sign.moduel;

public class User extends HBaseObject{
	
	private String name; //姓名
	private String cardNum; //身份证
	private String sex; //性别
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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


}
