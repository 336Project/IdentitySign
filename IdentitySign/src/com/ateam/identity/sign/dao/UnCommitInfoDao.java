package com.ateam.identity.sign.dao;

import java.util.List;

import android.content.Context;

import com.ateam.identity.sign.moduel.SignObject;
import com.team.hbase.dao.HBaseDao;

/**
 * 未提交数据操作
 * @author wtw
 * 2015-10-10上午10:08:32
 */
public class UnCommitInfoDao extends HBaseDao {
	private static final int LIMIT = 10;
	public UnCommitInfoDao(Context context) {
		super(context);
	}
	
	public void save(SignObject pro){
		mDb.save(pro);
	}
	
	public List<SignObject> query(){
		return mDb.findAll(SignObject.class,"id DESC limit "+LIMIT);
	}
	
	public void deleteByIDCard(int idcard){
		mDb.deleteByWhere(SignObject.class, "idcard = '"+idcard+"'");
	}
	
	public void deleteList(List<SignObject> objects){
		if(objects != null && !objects.isEmpty()){
			for (SignObject signObject : objects) {
				mDb.delete(signObject);
			}
		}
	}
	
	public void deleteAll(){
		mDb.deleteAll(SignObject.class);
	}
	
}
