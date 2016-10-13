package com.ateam.identity.sign.access;

import android.content.Context;

import com.ateam.identity.sign.moduel.StudentList;
import com.team.hbase.access.inter.HRequestCallback;

import java.util.HashMap;
import java.util.Map;

import static com.ateam.identity.sign.access.I.HURL.FIND_SUTDENT_LIST;

/**
 * 学生操作请求
 * @author wtw
 * 2015-9-24下午3:15:04
 */
@Deprecated
public class StudentAccess extends AbsAccess<StudentList> {

	
	public StudentAccess(Context c, HRequestCallback<StudentList> requestCallback) {
		super(c, requestCallback);
	}

	//获取对应的老师的学生列表
	public void findStudent(String teacherIDCard){
		Map<String,String> data = new HashMap<>();
		data.put("teacherCardNum", teacherIDCard);
		execute(FIND_SUTDENT_LIST, data);
	}
	
}
