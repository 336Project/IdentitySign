package com.ateam.identity.sign.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ateam.identity.sign.R;
import com.ateam.identity.sign.access.DataSyncAccess;
import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.dao.StudentDao;
import com.ateam.identity.sign.dao.UserDao;
import com.ateam.identity.sign.moduel.DataSync;
import com.ateam.identity.sign.moduel.Student;
import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SharedPreferencesUtil;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.JSONParse;
import com.team.hbase.widget.dialog.CustomProgressDialog;

public class SetActivity extends HBaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_set);
		setActionBarTitle("系统设置");
		getLeftIcon().setImageResource(R.drawable.icon_back);
		getRightIcon().setVisibility(View.GONE);
		findViewById(R.id.btn_setting_url).setOnClickListener(this);
		findViewById(R.id.btn_setting_sync).setOnClickListener(this);
	}
	/**
	 * url配置 
	 */
	public void urlSetting(){
		final View dialogView = View.inflate(this, R.layout.url_setting, null);
		final EditText input = (EditText) dialogView.findViewById(R.id.input);
		input.setText(SharedPreferencesUtil.getURL(this));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("URL配置").setIcon(android.R.drawable.ic_dialog_info).setView(dialogView)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               String url = input.getText().toString();
               changeUrl(url);
             }
        });
        builder.show();
	}
	
	private void changeUrl(String url) {
		if(!TextUtils.isEmpty(url)){
			SharedPreferencesUtil.setURL(this, url);
			MyToast.showShort(this, "配置成功");
		}else{
			MyToast.showShort(this, "请输入URL");
		}
	}
	/**
	 * 数据同步
	 */
	public void dataSync(){
		final CustomProgressDialog dialog= new CustomProgressDialog(this, "数据正在保存本地...");;
		DataSyncAccess access = new DataSyncAccess(this, new HRequestCallback<DataSync>() {
			
			@Override
			public DataSync parseJson(String jsonStr) {
				return (DataSync) JSONParse.jsonToBean(jsonStr, DataSync.class);
			}
			
			@Override
			public void onSuccess(DataSync result) {
				new AsyncTask<DataSync, Void, Void>() {
					protected void onPreExecute() {
						dialog.show();
					};

					@Override
					protected Void doInBackground(DataSync... params) {
						DataSync result = params[0];
						if(result != null){
							List<User> users = result.getTeachers();
							List<Student> students = result.getStudents();
							if(users != null){
								UserDao userDao = new UserDao(getApplicationContext());
								userDao.deleteAll();
								for (User user : users) {
									userDao.save(user);
								}
							}
							if(students != null){
								StudentDao studentDao = new StudentDao(getApplicationContext());
								studentDao.deleteAll();
								for (Student student : students) {
									studentDao.save(student);
								}
							}
						}
						return null;
					}
					
					protected void onPostExecute(Void result) {
						dialog.stopAndDismiss();
						MyToast.showShort(getApplicationContext(), "数据同步成功!");
					};
				}.execute(result);
			}
		});
		access.execute();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_setting_url:
			urlSetting();
			break;
		case R.id.btn_setting_sync:
			dataSync();
			break;
		default:
			break;
		}
	}
}
