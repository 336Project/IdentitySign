package com.ateam.identity.sign.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android_serialport_api.AsyncParseSFZ;
import android_serialport_api.AsyncParseSFZ.SFZ;
import android_serialport_api.SerialPortManager;
import android_serialport_api.AsyncParseSFZ.OnReadSFZListener;
import android_serialport_api.ParseSFZAPI.People;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.R;
import com.ateam.identity.sign.access.SignAccess;
import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.dao.UnCommitInfoDao;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.SignObject;
import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SysUtil;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.AppManager;
import com.team.hbase.utils.JSONParse;

public class MainActivity extends HBaseActivity implements OnClickListener {
	private TextView mTextViewName;
	private TextView mTextViewCard;
	private TextView mTextViewSex;
	
	private MediaPlayer mediaPlayer = null;
	private AsyncParseSFZ asyncSFZ ;
	private OnReadSFZListener onReadSFZListener;
	private SignAccess mAccess;
	private SignObject signObject;
	private boolean isSign = false;//是否正在签到
	
	private ImageView rightIcon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_main);
		setActionBarTitle("首页");
		rightIcon=getRightIcon();
		getLeftIcon().setImageResource(R.drawable.icon_back);
		rightIcon.setImageResource(R.drawable.icon_right);
		rightIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SetActivity.class);
				startActivity(intent );
			}
		});
		rightIcon.setVisibility(View.INVISIBLE);
		getTitleView().setLongClickable(true);
		getTitleView().setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				rightIcon.setVisibility(View.VISIBLE);
				return true;
			}
		});
		initView();
	}

	private void initView() {
		
		MyApplication application = (MyApplication) getApplication();
		User user = application.getUser();
		
		mTextViewName = (TextView) findViewById(R.id.tv_name);
		mTextViewCard = (TextView) findViewById(R.id.tv_card);
		mTextViewSex = (TextView) findViewById(R.id.tv_sex);
		mLinearLayoutClass = (LinearLayout) findViewById(R.id.linearLayout_class); //班级
		
		mTextViewName.setText(user.getName());
		mTextViewCard.setText(user.getCardNum());
		mTextViewSex.setText(user.getSex());
		String classroom = user.getClassroom();
		String[] split = classroom.split(",");
		if(split.length==0){
			mLinearLayoutClass.setVisibility(View.GONE);
		}
		else if(split.length==1){
			TextView textView = new TextView(this);
			textView.setText(split[0]);
			textView.setTextSize(18);
			mLinearLayoutClass.addView(textView);
		}
		else{
			RadioGroup radioGroup = new RadioGroup(MainActivity.this, null);
			for (int i = 0; i < split.length; i++) {
				RadioButton radioButton = new RadioButton(MainActivity.this);
				radioButton.setText(split[i]);
				radioButton.setTextSize(18);
				radioGroup.addView(radioButton);
			}
			mLinearLayoutClass.addView(radioGroup);
		}
		
		findViewById(R.id.btn_sign_second).setOnClickListener(this);
		findViewById(R.id.btn_sign_third).setOnClickListener(this);
		findViewById(R.id.btn_sign_manul).setOnClickListener(this);
		initSign();
	}
	public float px2sp(Context context, float pxVal) {
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}
	private void initSign() {
		mediaPlayer = MediaPlayer.create(this, R.raw.ok);
		mAccess = new SignAccess(this, new HRequestCallback<HBaseObject>() {
			
			@Override
			public HBaseObject parseJson(String jsonStr) {
				return (HBaseObject) JSONParse.jsonToBean(jsonStr, HBaseObject.class);
			}
			
			@Override
			public void onSuccess(HBaseObject result) {
				isSign = false;
				MyToast.showShort(MainActivity.this, "签到成功!");
				if(!result.isSuccess()){
					saveSign();
				}
			}
			
			@Override
			public void onFail(Context c, String errorMsg) {
				isSign = false;
				MyToast.showShort(MainActivity.this, "签到成功!");
				saveSign();
			}
		});
		onReadSFZListener = new AsyncParseSFZ.OnReadSFZListener() {
			
			@Override
			public void onReadSuccess(People people) {
				playRing();
				//签到
				List<SignObject> signList = new ArrayList<SignObject>();
				signObject = new SignObject(people.getPeopleIDCode(), SysUtil.getNowTime());
				signList.add(signObject);
				mAccess.sign(signList);
			}
			
			@Override
			public void onReadFail(int code) {
				isSign = false;
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sign_second:
			if(isSign){
				MyToast.showLong(this, "正在签到中，请稍等...");
				return;
			}
			isSign = true;
			asyncSFZ = new AsyncParseSFZ(this,onReadSFZListener);
			asyncSFZ.execute(SFZ.SECOND);
			break;
		case R.id.btn_sign_third:
			if(isSign){
				MyToast.showLong(this, "正在签到中，请稍等...");
				return;
			}
			isSign = true;
			asyncSFZ = new AsyncParseSFZ(this,onReadSFZListener);
			asyncSFZ.execute(SFZ.THIRD);
			break;
		case R.id.btn_sign_manul:
			startActivity(new Intent(this, ManulSignInActivity.class));
			break;

		default:
			break;
		}
	}
	/**
	 * 
	 * 2015-10-10 下午10:40:54
	 * @TODO 读卡成功响声
	 */
	private void playRing(){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.seekTo(0);
		}else{
			mediaPlayer.start();
		}
	}
	/**
	 * 
	 * 2015-10-10 下午10:41:10
	 * @TODO 签到失败时，保存签到对象
	 */
	private void saveSign(){
		if(signObject != null){
			UnCommitInfoDao dao=new UnCommitInfoDao(MainActivity.this);
			dao.save(signObject);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!SerialPortManager.getInstance().isOpen()){
			SerialPortManager.getInstance().openSerialPort();
		}
		Log.i("MainActivity", "onResume = "+SerialPortManager.getInstance().isOpen());
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("MainActivity", "onDestroy");
		mediaPlayer.release();
		mediaPlayer = null;
		SerialPortManager.getInstance().closeSerialPort();
	}
	
	private long currTime = 0;
	private LinearLayout mLinearLayoutClass;
	@Override
	public void onBackPressed() {
		if(System.currentTimeMillis()-currTime>2000){
			currTime = System.currentTimeMillis();
			MyToast.showShort(this, "再按一次退出程序");
		}else{
			AppManager.getInstance().ExitApp();
		}
	}

}
