package com.ateam.identity.sign.access;

import android.content.Context;

import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.SignObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.team.hbase.access.inter.HRequestCallback;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ateam.identity.sign.access.I.HURL.SIGN_IN;

public class SignAccess extends AbsAccess<HBaseObject> {

	public SignAccess(Context c, HRequestCallback<HBaseObject> requestCallback) {
		super(c, requestCallback);
	}
	/**
	 * 
	 * 2015-10-6 下午9:15:16
	 * @param signList
	 * @TODO batch sign
	 */
	public void sign(List<SignObject> signList){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data", signList);
		Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PUBLIC).create();
		Map<String,String> data = new HashMap<>();
		data.put("studentList",gson.toJson(map));
		execute(SIGN_IN, data);
	}

}
