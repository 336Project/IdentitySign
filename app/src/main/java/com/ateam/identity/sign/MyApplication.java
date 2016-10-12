package com.ateam.identity.sign;

import android.content.Intent;
import android.util.Log;

import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.service.UnSignService;
import com.ateam.identity.sign.util.CrashHandler;
import com.ateam.identity.sign.util.Installation;
import com.helen.andbase.application.HBaseApplication;

public class MyApplication extends HBaseApplication{
	private User user;
	private String classroom;
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.init(this);
		Installation.init(this);
		Log.e("", "ip"+Installation.getAppId());
		startService(new Intent(this, UnSignService.class));
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
}
