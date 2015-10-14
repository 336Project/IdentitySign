package com.ateam.identity.sign.access;

import android.content.Context;

import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.moduel.DataSync;

public class DataSyncAccess extends HBaseAccess<DataSync>{

	public DataSyncAccess(Context c, HRequestCallback<DataSync> requestCallback) {
		super(c, requestCallback);
	}
	
	public void execute(){
		execute(DATA_SYNC);
	}
}
