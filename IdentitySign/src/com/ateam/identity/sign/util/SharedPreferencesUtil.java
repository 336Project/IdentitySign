package com.ateam.identity.sign.util;

import android.content.Context;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class SharedPreferencesUtil {
	public static String SET = "user.set";
	
	public static void setUsername(Context context, String loginName) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SET,
				Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString("loginName", loginName);
		edit.commit();
	}

	public static String getUsername(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SET,
				Context.MODE_PRIVATE);
		String result = sharedPreferences.getString("loginName", "");
		return result;
	}
	public static void setPassword(Context context, String password) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SET,
				Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString("password", password);
		edit.commit();
	}
	public static String getPassword(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SET,
				Context.MODE_PRIVATE);
		String result = sharedPreferences.getString("password", "");
		return result;
	}
}

