package com.ateam.identity.sign.activity;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.R;
import com.ateam.identity.sign.moduel.User;
import com.team.hbase.activity.HBaseActivity;

public class MainActivity extends HBaseActivity implements OnClickListener {
	private TextView mTextViewName;
	private TextView mTextViewCard;
	private TextView mTextViewSex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_main);
		setActionBarTitle("首页");
		getLeftIcon().setImageResource(R.drawable.icon_back);
		getRightIcon().setVisibility(View.GONE);
		initView();
	}

	private void initView() {
		
		MyApplication application = (MyApplication) getApplication();
		User user = application.getUser();
		
		mTextViewName = (TextView) findViewById(R.id.tv_name);
		mTextViewCard = (TextView) findViewById(R.id.tv_card);
		mTextViewSex = (TextView) findViewById(R.id.tv_sex);
		
		mTextViewName.setText(user.getName());
		mTextViewCard.setText(user.getCardNum());
		mTextViewSex.setText(user.getSex());
		
	}

	@Override
	public void onClick(View v) {

	}

}
