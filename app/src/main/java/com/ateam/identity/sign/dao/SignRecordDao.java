package com.ateam.identity.sign.dao;

/**
 * Created by Administrator on 2016/10/14.
 */
import android.content.Context;

import com.ateam.identity.sign.moduel.SignRecord;
import com.team.hbase.dao.HBaseDao;

import java.util.List;

public class SignRecordDao extends HBaseDao {
    public SignRecordDao(Context context) {
        super(context);
    }

    public void save(SignRecord pro){
        mDb.save(pro);
    }
    //根据老师的身份证查找学生信息

    public List<SignRecord> findSignRecordByTeacherID(String teacherID, String classroom , long startDate , long endDate){
        return mDb.findAllByWhere(SignRecord.class, "teacherCardNum = '"+teacherID+"'"
                +"and classroom = '"+classroom+"'"
                +"and attendanceDate >= '"+startDate+"'"
                +"and attendanceDate <= '"+endDate+"'"
        );
    }
    public List<SignRecord> findSignRecordByTeacherID(String teacherID, String classroom ){
        return mDb.findAllByWhere(SignRecord.class, "teacherCardNum = '"+teacherID+"'"
                +"and classroom = '"+classroom+"'"
        );
    }


}
