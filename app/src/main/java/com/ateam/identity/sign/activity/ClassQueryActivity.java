package com.ateam.identity.sign.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.R;
import com.ateam.identity.sign.dao.SignRecordDao;
import com.ateam.identity.sign.dao.StudentDao;
import com.ateam.identity.sign.moduel.SignRecord;
import com.ateam.identity.sign.moduel.Student;
import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SysUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClassQueryActivity extends PermissionActivity implements View.OnClickListener {

    private User user;
    private ListView mListView;
    private List<Student> mStudentList;
    private List<SignRecord> mSignRecordList;
    private MyApplication mApplication;
    private User mUser;
    private MyAdapter mAdapter;
    private TextView mTextViewStartTime;
    private TextView mTextViewEndTime;
    private Dialog mStartTimeDialog;
    private Dialog mEndTimeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_class_query);
        setActionBarTitle("未到人员查询");
        getLeftIcon().setImageResource(R.drawable.icon_back);
        getRightIcon().setVisibility(View.GONE);
        mApplication = (MyApplication) getApplication();
        mUser = mApplication.getUser();
        initView();
        initDate();
        initStartTimeDialog();
        initEndTimeDialog();
    }

    private void initDate() {
        if(mStudentList!=null){
            mStudentList.clear();
        }
        if(mSignRecordList!=null){
            mSignRecordList.clear();
        }
        StudentDao studentDao = new StudentDao(this);
        SignRecordDao signRecordDao = new SignRecordDao(this);

//        mStudentList.addAll(studentDao.findByTeacherID(mUser.getCardNum(), mApplication.getClassroom()));

        mStudentList = studentDao.findByTeacherID(mUser.getCardNum(), mApplication.getClassroom());
        mSignRecordList =  signRecordDao.findSignRecordByTeacherID(mUser.getCardNum(),mApplication.getClassroom(),
                SysUtil.time2Long(mTextViewStartTime.getText().toString()),SysUtil.time2Long(mTextViewEndTime.getText().toString()));
//        mSignRecordList.addAll(signRecordDao.findSignRecordByTeacherID(mUser.getCardNum(),mApplication.getClassroom(),
//                SysUtil.time2Long(mTextViewStartTime.getText().toString()),SysUtil.time2Long(mTextViewEndTime.getText().toString())));


        if(mStudentList.size()==0||mSignRecordList.size()==0){
            MyToast.showShort(ClassQueryActivity.this, "暂无未到人员");
            return;
        }
        if (mStudentList.size() > 0 && mSignRecordList.size() > 0) {
            for (int i = 0; i < mStudentList.size(); i++) {
                Student student = mStudentList.get(i);
                for (int j = 0; j < mSignRecordList.size(); j++) {
                    SignRecord signRecord = mSignRecordList.get(j);
                    if (student.getCardNum().equals(signRecord.getCardNum())) {
                        mStudentList.remove(i);
                        break;
                    }
                }

            }
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        mApplication = (MyApplication) getApplication();
        user = mApplication.getUser();
        TextView textViewClassName = (TextView) findViewById(R.id.tv_class_name);
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        mTextViewStartTime = (TextView) findViewById(R.id.tv_start_time);
        mTextViewEndTime = (TextView) findViewById(R.id.tv_end_time);
        mTextViewStartTime.setOnClickListener(this);
        mTextViewEndTime.setOnClickListener(this);
        textViewClassName.setText(mApplication.getClassroom());
        findViewById(R.id.btn_input).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start_time:
                mStartTimeDialog.show();
                break;
            case R.id.tv_end_time:
                mEndTimeDialog.show();
                break;
            case R.id.btn_input:
                if(mTextViewStartTime.getText().toString().equals("")||mTextViewEndTime.getText().toString().equals("")){
                    MyToast.showShort(ClassQueryActivity.this, "开始时间或者结束时间不能为空");
                    return;
                }
                if(SysUtil.time2Long(mTextViewStartTime.getText().toString().trim())>SysUtil.time2Long(mTextViewEndTime.getText().toString().trim())){
                    MyToast.showShort(ClassQueryActivity.this, "开始时间不能大于结束时间");
                    return;
                }
                initDate();
                break;
        }

    }

    public void initStartTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.date_time_dialog, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        datePicker.setCalendarViewShown(false);

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

        builder.setView(view);
        builder.setTitle("选取起始时间");
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d/%02d/%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                sb.append("  ");
                sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
                sb.append(":00");
                mTextViewStartTime.setText(sb);

                dialog.cancel();
            }
        });
        mStartTimeDialog = builder.create();
    }

    public void initEndTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.date_time_dialog, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        datePicker.setCalendarViewShown(false);

        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));

        builder.setView(view);
        builder.setTitle("选取结束时间");
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d/%02d/%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                sb.append("  ");
                sb.append(timePicker.getCurrentHour()).append(":").append(timePicker.getCurrentMinute());
                sb.append(":00");
                mTextViewEndTime.setText(sb);
                dialog.cancel();
            }
        });
        mEndTimeDialog = builder.create();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(mStudentList !=null){
                return mStudentList.size();
            }
            else{
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder viewholder = null;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Student item = mStudentList.get(position);
            if (convertView == null) {
                viewholder = new ViewHolder();
                view = inflater.inflate(R.layout.class_query_list_item, null);
                TextView textViewUserName = (TextView) view.findViewById(R.id.tv_username);
                TextView textViewSchool = (TextView) view.findViewById(R.id.tv_school);
                TextView textViewTel = (TextView) view.findViewById(R.id.tv_tel);
                TextView textViewSerial = (TextView) view.findViewById(R.id.tv_serial);
                viewholder.setTextViewSchool(textViewSchool);
                viewholder.setTextViewTel(textViewTel);
                viewholder.setTextViewUsername(textViewUserName);
                viewholder.setTextViewSerial(textViewSerial);
                view.setTag(viewholder);
            } else {
                view = convertView;
                viewholder = (ViewHolder) view.getTag();
            }
            viewholder.getTextViewUsername().setText(item.getName());
            viewholder.getTextViewSchool().setText(mUser.getSchool());
            viewholder.getTextViewTel().setText(item.getTelephone());
            StringBuffer sb = new StringBuffer();
            if(position+1<9){
                sb.append("0"+(position+1)+".");
            }
            else{
                sb.append((position+1)+".");
            }
            viewholder.getTextViewSerial().setText(sb.toString());
            return view;
        }

        class ViewHolder {
            private TextView textViewUsername;
            private TextView textViewSchool;
            private TextView textViewTel;
            private TextView textViewSerial;

            public TextView getTextViewSerial() {
                return textViewSerial;
            }

            public void setTextViewSerial(TextView textViewSerial) {
                this.textViewSerial = textViewSerial;
            }

            public TextView getTextViewTel() {
                return textViewTel;
            }

            public void setTextViewTel(TextView textViewTel) {
                this.textViewTel = textViewTel;
            }

            public TextView getTextViewUsername() {
                return textViewUsername;
            }

            public void setTextViewUsername(TextView textViewUsername) {
                this.textViewUsername = textViewUsername;
            }

            public TextView getTextViewSchool() {
                return textViewSchool;
            }

            public void setTextViewSchool(TextView textSchool) {
                this.textViewSchool = textSchool;
            }

        }
    }
}
