package com.ateam.identity.sign;

import com.ateam.identity.sign.util.Installation;

import android.app.Application;

public class MyApplication extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		Installation.init(this);
	}

}
