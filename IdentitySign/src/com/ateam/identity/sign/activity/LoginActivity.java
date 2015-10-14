package com.ateam.identity.sign.activity;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.R;
import com.ateam.identity.sign.access.LoginAccess;
import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.dao.UserDao;
import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SharedPreferencesUtil;
import com.ateam.identity.sign.util.SysUtil;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.JSONParse;

public class LoginActivity extends HBaseActivity implements OnClickListener{
	private EditText et_username;
	private EditText et_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_login);
		setActionBarTitle("登陆页");
		getLeftIcon().setVisibility(View.GONE);
		getRightIcon().setVisibility(View.GONE);
		initView();
	}
	
	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		findViewById(R.id.br_login).setOnClickListener(this);
		
		et_username.setText(SharedPreferencesUtil.getUsername(LoginActivity.this));
		et_password.setText(SharedPreferencesUtil.getPassword(LoginActivity.this));
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.br_login:
			login();
			break;

		default:
			break;
		}
	}
	private UserDao userDao;
	private void login() {
		final String username = et_username.getText().toString();
		final String password = et_password.getText().toString();
		if("".equals(username)){
			MyToast.showShort(LoginActivity.this, "用户名不能为空！");
			return;
		}
		if("".equals(password)){
			MyToast.showShort(LoginActivity.this, "密码不能为空！");
			return;
		}
		
		SharedPreferencesUtil.setUsername(LoginActivity.this, username);
		SharedPreferencesUtil.setPassword(LoginActivity.this, password);
		userDao = new UserDao(LoginActivity.this);
		List<User> users = userDao.findByUserNameAndPassWord(username,SysUtil.Encryption(password));
		if(users!=null && users.size()>0){//先判断本地是否有缓存
			loginOffLine(username, password);
		}else{
			loginOnLine(username, password);
		}
	}
	/**
	 * 在线登录
	 */
	private void loginOnLine(final String username,final String password){
		HRequestCallback<User> request=new HRequestCallback<User>() {
			@Override
			public User parseJson(String jsonStr) {
				Type type = new com.google.gson.reflect.TypeToken<User>() {
				}.getType();
				return (User) JSONParse.jsonToObject(
						jsonStr, type);
			}
			@Override
			public void onSuccess(User result) {
				if(result.isSuccess()){
					Log.e("LoginActivity", "在线模式");
					MyToast.showShort(LoginActivity.this, "登录成功");
					/*userDao.deleteByUserName(username);
					result.setPassword(password);
					result.setUsername(username);
					userDao.save(result);*/
					MyApplication application = (MyApplication) getApplication();
					application.setUser(result);
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else{
					MyToast.showShort(LoginActivity.this, result.getMessage());
				}
			}
			@Override
			public void onFail(Context c, String errorMsg) {
				Log.e("!!!", "!!!");
				
				if(!SysUtil.isNetworkConnected(LoginActivity.this)){
					loginOffLine(username, password);
				}
				else{
					MyToast.showShort(c, errorMsg);
				}
			}
		};
		LoginAccess access=new LoginAccess(LoginActivity.this, request);
		access.login(username, SysUtil.Encryption(password));
	}
	
	private void loginOffLine(String username,String password){
		List<User> users = userDao.findByUserNameAndPassWord(username,SysUtil.Encryption(password));
		if(users !=null&&users.size()>0){
			Log.e("LoginActivity", "离线模式");
			MyToast.showShort(LoginActivity.this, "登录成功");
			MyApplication application = (MyApplication) getApplication();
			application.setUser(users.get(0));
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}else{
			MyToast.showShort(this, "用户名或密码错误!");
		}
	}
}
