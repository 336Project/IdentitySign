package com.ateam.identity.sign;

import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.util.Installation;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application{
	private User user;
	@Override
	public void onCreate() {
		super.onCreate();
		Installation.init(this);
		Log.e("", "ip"+Installation.getAppId());
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
