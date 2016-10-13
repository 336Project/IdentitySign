package com.ateam.identity.sign.access;

import android.content.Context;

import com.ateam.identity.sign.moduel.User;
import com.team.hbase.access.inter.HRequestCallback;

import java.util.HashMap;
import java.util.Map;

import static com.ateam.identity.sign.access.I.HURL.URL_USER_LOGIN;

public class LoginAccess extends AbsAccess<User> {

	public LoginAccess(Context c, HRequestCallback<User> requestCallback) {
		super(c, requestCallback);
	}

	// 获取对应的老师的学生列表
	public void login(String userName,String passWord) {
		Map<String,String> data = new HashMap<>();
		data.put("username", userName);
		data.put("password",passWord);
		execute(URL_USER_LOGIN, data);
	}
}
