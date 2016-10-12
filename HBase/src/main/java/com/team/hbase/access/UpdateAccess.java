package com.team.hbase.access;

import android.content.Context;

import com.team.hbase.access.inter.HRequestCallback;
import com.team.hbase.model.UpdateInfo;
/**
 * 
 * @author 李晓伟
 * @Create_date 2015-3-25 上午10:09:32
 * @Version 
 * @TODO 检测更新接口
 */
public class UpdateAccess extends HBaseAccess<UpdateInfo> {

	public UpdateAccess(Context c, HRequestCallback<UpdateInfo> requestCallback) {
		super(c, requestCallback);
	}

	public void execute(String version){
		execute("http://www.baidu.com/");
	}
	
}
