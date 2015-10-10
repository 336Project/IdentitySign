package com.ateam.identity.sign.dao;

import java.util.List;

import android.content.Context;

import com.ateam.identity.sign.moduel.Student;
import com.ateam.identity.sign.moduel.User;
import com.team.hbase.dao.HBaseDao;

/**
 * 提交用户数据库
 * 
 * @author xiaokang 2015-10-10
 */

public class UserDao extends HBaseDao {

	public UserDao(Context context) {
		super(context);
	}

	public void save(User user) {
		mDb.save(user);
	}

	// 根据用户的姓名查找学生信息
	public List<User> findByUserName(String username) {
//		return mDb.findAll(User.class);
		return mDb.findAllByWhere(User.class, "username = '"+username+"'");
	}

	// 根据用户的姓名和密码查找学生信息
	public List<User> findByUserNameAndPassWord(String username, String password) {
		return mDb.findAllByWhere(User.class, "username = '" + username + "'"
				+ " and password= '" + password + "'");
	}

	// 根据用户的姓名删除用户信息
	public void deleteByUserName(String username) {
		mDb.deleteByWhere(User.class, "username = '" + username + "'");
	}

	public void deleteAll() {
		mDb.deleteAll(User.class);
	}

}
