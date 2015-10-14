package com.ateam.identity.sign.access;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.moduel.StudentList;


import android.content.Context;

/**
 * 学生操作请求
 * @author wtw
 * 2015-9-24下午3:15:04
 * @param <T>
 */
@Deprecated
public class StudentAccess extends HBaseAccess<StudentList>{

	
	public StudentAccess(Context c, HRequestCallback<StudentList> requestCallback) {
		super(c, requestCallback);
	}

	//获取对应的老师的学生列表
	public void findStudent(String teacherIDCard){
		List<NameValuePair> nvps=new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("teacherCardNum", teacherIDCard));
		execute(FIND_SUTDENT_LIST, nvps);
	}
	
}
