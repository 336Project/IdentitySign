package com.ateam.identity.sign.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.SignObject;
import com.team.hbase.utils.JSONParse;

public class SignAccess extends HBaseAccess<HBaseObject>{

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
		List<NameValuePair> nvps=new ArrayList<NameValuePair>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data", signList);
		nvps.add(new BasicNameValuePair("studentList", JSONParse.objectToJson(map)));
		execute(SIGN_IN, nvps);
	}

}
