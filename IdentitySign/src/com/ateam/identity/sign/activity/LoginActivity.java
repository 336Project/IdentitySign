package com.ateam.identity.sign.activity;

import java.lang.reflect.Type;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ateam.identity.sign.R;
import com.ateam.identity.sign.access.LoginAccess;
import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.util.MyToast;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.JSONParse;
import com.team.hbase.widget.dialog.CustomProgressDialog;

public class LoginActivity extends HBaseActivity implements OnClickListener{
	private CustomProgressDialog dialog;
	private EditText et_username;
	private EditText et_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_login);
		initView();
	}
	
	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		findViewById(R.id.br_login).setOnClickListener(this);
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
	private void login() {
		String username = et_username.getText().toString();
		String password = et_password.getText().toString();
//		if("".equals(username)){
//			Toast.makeText(context, resId, duration);
//			return;
//		}
		
		dialog=new CustomProgressDialog(this, "登录中...");
		dialog.show();
		HRequestCallback<User> request=new HRequestCallback<User>() {
			@SuppressWarnings("unchecked")
			@Override
			public User parseJson(String jsonStr) {
				// TODO Auto-generated method stub
				Type type = new com.google.gson.reflect.TypeToken<HBaseObject>() {
				}.getType();
				return (User) JSONParse.jsonToObject(
						jsonStr, type);
			}
			@Override
			public void onSuccess(User result) {
				
			}
			@Override
			public void onFail(Context c, String errorMsg) {
				// TODO Auto-generated method stub
				super.onFail(c, errorMsg);
				dialog.dismiss();
				MyToast.showShort(c, errorMsg);
			}
		};
		LoginAccess access=new LoginAccess(LoginActivity.this, request);
		access.login(username, password);
	}


}
