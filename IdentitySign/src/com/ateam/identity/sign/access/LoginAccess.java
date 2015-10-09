package com.ateam.identity.sign.access;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.moduel.User;

public class LoginAccess extends HBaseAccess<User> {

	public LoginAccess(Context c, HRequestCallback<User> requestCallback) {
		super(c, requestCallback);
	}

	// 获取对应的老师的学生列表
	public void login(String userName,String passWord) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", userName));
		nvps.add(new BasicNameValuePair("password", passWord));
		execute(URL_USER_LOGIN, nvps);
	}
}
