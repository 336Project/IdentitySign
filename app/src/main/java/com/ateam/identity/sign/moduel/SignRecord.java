package com.ateam.identity.sign.moduel;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

import java.io.Serializable;

/**
 * 学生签到记录
 * Created by Administrator on 2016/10/14.
 */
@Table(name=SignRecord.TABLE_SIGN_RECORD)
public class SignRecord implements Serializable {
    @Transient
    public static final String TABLE_SIGN_RECORD="tb_sign_record";
    @Transient
    private static final long serialVersionUID = 1L;

    private int id;
    private String cardNum;         //学生身份证号
    private String attendanceDate;  //签到时间
    private String teacherCardNum;
    private String classroom;

    public SignRecord(String cardNum,String attendanceDate,String teacherCardNum,String classroom){
        this.cardNum = cardNum;
        this.attendanceDate = attendanceDate;
        this.teacherCardNum = teacherCardNum;
        this.classroom = classroom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
    public String getTeacherCardNum() {
        return teacherCardNum;
    }

    public void setTeacherCardNum(String teacherCardNum) {
        this.teacherCardNum = teacherCardNum;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

}
