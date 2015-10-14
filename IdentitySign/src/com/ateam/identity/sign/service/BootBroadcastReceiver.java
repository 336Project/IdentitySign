package com.ateam.identity.sign.service;

import com.ateam.identity.sign.activity.LoginActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("BootBroadcastReceiver", "开机自启动.....");
		Intent loginIntent = new Intent(context, LoginActivity.class);
		context.startActivity(loginIntent);
	}

}
