package com.ateam.identity.sign.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ateam.identity.sign.access.SignAccess;
import com.ateam.identity.sign.dao.UnCommitInfoDao;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.SignObject;
import com.ateam.identity.sign.util.SysUtil;
import com.team.hbase.access.inter.HRequestCallback;
import com.team.hbase.utils.JSONParse;

import java.util.List;
/**
 * 
 * @author 李晓伟
 * 2015-10-10 下午10:13:12
 * @TODO 后台服务补签
 */
public class UnSignService extends Service{
	public static final String TAG = UnSignService.class.getSimpleName();
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
					Log.e(TAG, "上传成功");
					dao.deleteList(datas);
				}else{
					Log.e(TAG, "上传失败");
				}
				stopSelf();
			}
			@Override
			public void onFail(Context c, String errorMsg) {
				stopSelf();
				Log.e(TAG, "上传失败");
			}
		});
		access.setIsShow(false);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!isDoing && SysUtil.isNetworkConnected(this)){
			Log.e(TAG, "开启上传签到缓存服务");
			isDoing = true;
			datas = dao.query();
			if(datas != null && !datas.isEmpty()){
				Log.e(TAG, "有签到缓存记录:"+datas.size());
				access.sign(datas);
			}else{
				Log.e(TAG, "没有签到缓存记录");
				stopSelf();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isDoing = false;
		Log.e(TAG, "关闭上传签到缓存服务");
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
