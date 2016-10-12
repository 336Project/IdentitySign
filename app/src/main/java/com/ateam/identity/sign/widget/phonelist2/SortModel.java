package com.ateam.identity.sign.widget.phonelist2;

public class SortModel {

	private String name;   //显示的数据
	private String sortLetters;  //显示数据拼音的首字母
	private Boolean ifSelect=false;//是否被选中
	
	public Boolean getIfSelect() {
		return ifSelect;
	}
	public void setIfSelect(Boolean ifSelect) {
		this.ifSelect = ifSelect;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
