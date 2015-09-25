package com.ateam.identity.sign.moduel;

import java.io.Serializable;

public class Respond<T> extends HBaseObject implements Serializable{
	private static final long serialVersionUID = 3321056538834150075L;
	private T datas;
	private boolean isSuccess;

	private int totalPages;
	private int totalCredit;//积分总额
	private String root_url;
	private String myuid;
	public String getRoot_url() {
		return root_url;
	}

	public void setRoot_url(String root_url) {
		this.root_url = root_url;
	}

	public T getDatas() {
		return datas;
	}

	public void setDatas(T datas) {
		this.datas = datas;
	}

	public boolean isSuccess() {
		if(getStatusCode().equals("200")){
			isSuccess=true;
		}else{
			isSuccess=false;
		}
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(int totalCredit) {
		this.totalCredit = totalCredit;
	}

	public String getMyuid() {
		return myuid;
	}

	public void setMyuid(String myuid) {
		this.myuid = myuid;
	}
}
