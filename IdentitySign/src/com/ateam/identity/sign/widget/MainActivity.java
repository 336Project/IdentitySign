package com.ateam.identity.sign.widget;

import com.ateam.identity.sign.R;
import com.ateam.identity.sign.R.layout;
import com.team.hbase.activity.HBaseActivity;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends HBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_main);
	}
}
