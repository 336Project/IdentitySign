package com.ateam.identity.sign.access;

import android.content.Context;

import com.ateam.identity.sign.moduel.DataSync;
import com.team.hbase.access.inter.HRequestCallback;

import static com.ateam.identity.sign.access.I.HURL.DATA_SYNC;

public class DataSyncAccess extends AbsAccess<DataSync> {

	public DataSyncAccess(Context c, HRequestCallback<DataSync> requestCallback) {
		super(c, requestCallback);
	}
	
	public void execute(){
		execute(DATA_SYNC);
	}
}
