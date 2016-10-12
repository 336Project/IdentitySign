package com.ateam.identity.sign.service;


import com.ateam.identity.sign.util.SysUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * 
 * @author 李晓伟
 * 2015-10-10 下午10:16:31
 * @TODO 监听网络变化
 */
public class NetworkBroadcastReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("NetworkBroadcastReceiver", "---网络发生了变化---");
		if(SysUtil.isNetworkConnected(context)){
			Log.e("NetworkBroadcastReceiver", "---有网络---");
			context.startService(new Intent(context,UnSignService.class));
		}else{
			Log.e("NetworkBroadcastReceiver", "---无网络---");
		}
	}

}
