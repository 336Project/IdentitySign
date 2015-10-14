package com.ateam.identity.sign.dao;

import java.util.List;

import android.content.Context;

import com.ateam.identity.sign.moduel.Student;
import com.team.hbase.dao.HBaseDao;

/**
 * 未提交数据操作
 * @author wtw
 * 2015-10-10上午10:08:32
 */
public class StudentDao extends HBaseDao {

	public StudentDao(Context context) {
		super(context);
	}
	
	public void save(Student pro){
		mDb.save(pro);
	}
	
	public List<Student> query(){
		return mDb.findAll(Student.class,"idcard DESC");
	}
	
	//根据老师的身份证查找学生信息
	public List<Student> findByTeacherID(String teacherID,String classroom){
		return mDb.findAllByWhere(Student.class, "teacherID = '"+teacherID+"'"+"and classroom = '"+classroom+"'");
	}
	
	//根据老师的身份证查找学生信息
	public boolean findTeacher(String teacherID){
		if(mDb.findAllByWhere(Student.class, "teacherID = '"+teacherID+"'").size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	//根据老师的身份证删除学生信息
	public void deleteByIDCard(String teacherID){
		mDb.deleteByWhere(Student.class, "teacherID = '"+teacherID+"'");
	}
	
	public void deleteAll(){
		mDb.deleteAll(Student.class);
	}
	
}
