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
		return mDb.findAll(Student.class,"id DESC");
	}
	
	//根据老师的身份证查找学生信息
	public List<Student> findByTeacherID(String teacherID,String classroom){
		System.out.println("tCardNum:"+teacherID+" classroom:"+classroom);
		return mDb.findAllByWhere(Student.class, "tCardNum = '"+teacherID+"'"+"and classroom = '"+classroom+"'");
	}
//	//根据老师的身份证查找未出席的学生信息
//	public List<Student> findAbsentStudentByTeacherID(String teacherID,String classroom,){
//		System.out.println("tCardNum:"+teacherID+" classroom:"+classroom);
//		return mDb.findAllByWhere(Student.class, "tCardNum = '"+teacherID+"'"+"and classroom = '"+classroom+"'");
//	}
	
	//根据老师的身份证查找学生信息
	public boolean findTeacher(String teacherID){
		if(mDb.findAllByWhere(Student.class, "tCardNum = '"+teacherID+"'").size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	//根据老师的身份证删除学生信息
	public void deleteByIDCard(String teacherID){
		mDb.deleteByWhere(Student.class, "tCardNum = '"+teacherID+"'");
	}
	
	public void deleteAll(){
		mDb.deleteAll(Student.class);
	}
	/**
	 * 是否可以签到
	 */
	public boolean isCanSign(String cardNum,String tCardNum,String classroom){
		return mDb.findAllByWhere(Student.class, "tCardNum = '"+tCardNum+"' and cardNum = '"+cardNum+"' and classroom='"+classroom+"'").size()>0;
	}
	
}
