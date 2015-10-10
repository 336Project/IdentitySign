package com.ateam.identity.sign.service;

import java.util.List;

import com.ateam.identity.sign.access.SignAccess;
import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.dao.UnCommitInfoDao;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.SignObject;
import com.team.hbase.utils.JSONParse;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
/**
 * 
 * @author 李晓伟
 * 2015-10-10 下午10:13:12
 * @TODO 后台服务补签
 */
public class UnSignService extends Service{
	private UnCommitInfoDao dao;
	private SignAccess access;
	private List<SignObject> datas;
	private boolean isDoing = false;
	@Override
	public void onCreate() {
		super.onCreate();
		isDoing = false;
		dao = new UnCommitInfoDao(this);
		access = new SignAccess(this,new HRequestCallback<HBaseObject>() {
			
			@Override
			public HBaseObject parseJson(String jsonStr) {
				return (HBaseObject) JSONParse.jsonToBean(jsonStr, HBaseObject.class);
			}
			
			@Override
			public void onSuccess(HBaseObject result) {
				if(result.isSuccess()){
					dao.deleteList(datas);
				}
				stopSelf();
			}
			@Override
			public void onFail(Context c, String errorMsg) {
				stopSelf();
			}
		});
		access.setIsShow(false);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!isDoing){
			isDoing = true;
			datas = dao.query();
			if(datas != null && !datas.isEmpty()){
				access.sign(datas);
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isDoing = false;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
