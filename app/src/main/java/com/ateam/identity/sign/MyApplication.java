package com.ateam.identity.sign;

import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.service.UnSignService;
import com.ateam.identity.sign.util.CrashHandler;
import com.ateam.identity.sign.util.Installation;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.HandlerThread;
import android.util.Log;

public class MyApplication extends Application{
	private User user;
	private String classroom;
	
	private String rootPath;
	
	public static final Logger logger = LoggerFactory.getLogger();

	private HandlerThread handlerThread;
	public String getRootPath() {
		return rootPath;
	}

	public HandlerThread getHandlerThread() {
		return handlerThread;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler.init(this);
		Installation.init(this);
		Log.e("", "ip"+Installation.getAppId());
		startService(new Intent(this, UnSignService.class));
		
		logger.debug("**********Enter Myapplication********");
		handlerThread = new HandlerThread("handlerThread",android.os.Process.THREAD_PRIORITY_BACKGROUND);
//		handlerThread = new HandlerThread("handlerThread");
		handlerThread.start();
		setRootPath();
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
	
	private void setRootPath() {
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			rootPath = info.applicationInfo.dataDir;
			Log.i("rootPath", "################rootPath=" + rootPath);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
