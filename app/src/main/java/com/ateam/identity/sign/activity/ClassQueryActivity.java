package com.ateam.identity.sign.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.R;
import com.ateam.identity.sign.dao.SignRecordDao;
import com.ateam.identity.sign.dao.StudentDao;
import com.ateam.identity.sign.moduel.SignRecord;
import com.ateam.identity.sign.moduel.Student;
import com.ateam.identity.sign.moduel.User;

import java.util.ArrayList;
import java.util.List;

public class ClassQueryActivity extends PermissionActivity {

    private User user;
    private ListView mListView;
    private List<Student> mStudentList = new ArrayList<Student>();
    private List<SignRecord> mSignRecordList = new ArrayList<SignRecord>();
    private MyApplication mApplication;
    private User mUser;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentView(R.layout.activity_class_query);
        setActionBarTitle("未到人员查询");
        getLeftIcon().setImageResource(R.drawable.icon_back);
        getRightIcon().setVisibility(View.GONE);
        mApplication = (MyApplication) getApplication();
        mUser = mApplication.getUser();
        initDate();
        initView();
    }

    private void initDate() {
        mStudentList.clear();
        mSignRecordList.clear();
        StudentDao studentDao = new StudentDao(this);
        SignRecordDao signRecordDao = new SignRecordDao(this);
        mStudentList.addAll(studentDao.findByTeacherID(mUser.getCardNum(),mUser.getClassroom()));
//        mSignRecordList.addAll(signRecordDao.findSignRecordByTeacherID(mUser.getCardNum(),mUser.getClassroom(),"",""));
        mSignRecordList.addAll(signRecordDao.findSignRecordByTeacherID(mUser.getCardNum(),mUser.getClassroom()));
        Log.e("sss","sss:"+mSignRecordList.size());
        if(mStudentList.size()>0&&mSignRecordList.size()>0){
            for(int i  = 0;i<mStudentList.size();i++){
                Student student = mStudentList.get(i);
                for(int j = 0 ; j<mSignRecordList.size();j++){
                    SignRecord signRecord = mSignRecordList.get(i);
                    if(student.getCardNum().equals(signRecord.getCardNum())){
                        mStudentList.remove(i);
                        break;
                    }
                }

            }
        }
        if(mAdapter !=null)
        {
            mAdapter.notifyDataSetChanged();
        }
    }
    private void initView() {
        mApplication = (MyApplication) getApplication();
        user =  mApplication.getUser();
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mStudentList.size();
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
            Student item =  mStudentList.get(position);
            if(convertView == null){
                viewholder = new ViewHolder();
                view = inflater.inflate(R.layout.class_query_list_item,null);
                TextView textViewUserName = (TextView) view.findViewById(R.id.tv_username);
                TextView textViewSchool = (TextView) view.findViewById(R.id.tv_school);
                TextView textViewTel = (TextView)   view.findViewById(R.id.tv_tel);
                viewholder.setTextViewSchool(textViewSchool);
                viewholder.setTextViewTel(textViewTel);
                viewholder.setTextViewUsername(textViewUserName);
                view.setTag(viewholder);
            }
            else{
                view = convertView;
                viewholder = (ViewHolder) view.getTag();
            }
            viewholder.getTextViewUsername().setText(item.getName());
            viewholder.getTextViewSchool().setText(mUser.getSchool());
            return null;
        }
        class ViewHolder{
            private TextView textViewUsername;
            private TextView textViewSchool;
            private TextView textViewTel;

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
