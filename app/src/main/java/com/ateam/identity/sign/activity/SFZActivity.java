package com.ateam.identity.sign.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.R;
import com.ateam.identity.sign.access.SignAccess;
import com.ateam.identity.sign.dao.SignRecordDao;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.SignObject;
import com.ateam.identity.sign.moduel.SignRecord;
import com.ateam.identity.sign.moduel.User;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SysUtil;
import com.team.hbase.access.inter.HRequestCallback;
import com.team.hbase.utils.JSONParse;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.AsyncParseSFZ;
import android_serialport_api.AsyncParseSFZ.OnReadSFZListener;
import android_serialport_api.AsyncParseSFZ.SFZ;
import android_serialport_api.ParseSFZAPI.People;
import android_serialport_api.SerialPortManager;

@Deprecated
public class SFZActivity extends PermissionActivity implements OnClickListener {
	private TextView sfz_name;
	private TextView sfz_sex;
	private TextView sfz_nation;
	private TextView sfz_year;
	private TextView sfz_mouth;
	private TextView sfz_day;
	private TextView sfz_address;
	private TextView sfz_id;
	private ImageView sfz_photo;

	private Button read_button;
	private Button read_third_button;



	MediaPlayer mediaPlayer = null;
	private AsyncParseSFZ asyncSFZ ;
	private OnReadSFZListener onReadSFZListener;
	
	private SignAccess mAccess;

	private MyApplication mAPP;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sfz_activity);
		mAPP = (MyApplication) getApplication();
		initView();
		initData();
	}

	private void initView() {
		sfz_name = ((TextView) findViewById(R.id.sfz_name));
		sfz_nation = ((TextView) findViewById(R.id.sfz_nation));
		sfz_sex = ((TextView) findViewById(R.id.sfz_sex));
		sfz_year = ((TextView) findViewById(R.id.sfz_year));
		sfz_mouth = ((TextView) findViewById(R.id.sfz_mouth));
		sfz_day = ((TextView) findViewById(R.id.sfz_day));
		sfz_address = ((TextView) findViewById(R.id.sfz_address));
		sfz_id = ((TextView) findViewById(R.id.sfz_id));
		sfz_photo = ((ImageView) findViewById(R.id.sfz_photo));

		read_button = ((Button) findViewById(R.id.read_sfz));
		read_third_button = ((Button) findViewById(R.id.read_third_sfz));

		read_button.setOnClickListener(this);
		read_third_button.setOnClickListener(this);
	}

	private void initData() {
		mediaPlayer = MediaPlayer.create(this, R.raw.ok);
		final SignRecordDao signRecordDao = new SignRecordDao(this);
		final User user = ((MyApplication)getApplication()).getUser();
		mAccess = new SignAccess(this, new HRequestCallback<HBaseObject>() {
			
			@Override
			public HBaseObject parseJson(String jsonStr) {
				return (HBaseObject) JSONParse.jsonToBean(jsonStr, HBaseObject.class);
			}
			
			@Override
			public void onSuccess(HBaseObject result) {
				if(result.isSuccess()){
					MyToast.showShort(SFZActivity.this, "签到成功!");
				}else{
					MyToast.showShort(SFZActivity.this, result.getMessage());
				}
			}
		});
		onReadSFZListener = new AsyncParseSFZ.OnReadSFZListener() {
			
			@Override
			public void onReadSuccess(People people) {
				updateInfo(people);
				//签到
				List<SignObject> signList = new ArrayList<SignObject>();
				SignObject object = new SignObject(people.getPeopleIDCode(), SysUtil.getNowTime(),SignObject.TYPE_AUTO);
				signList.add(object);
//				SignRecord signRecord = new SignRecord(people.getPeopleIDCode(),SysUtil.time2Long(SysUtil.getNowTime()) ,user.getCardNum(),user.getClassroom());
				SignRecord signRecord = new SignRecord();
				signRecord.setTeacherCardNum(user.getCardNum());
				signRecord.setCardNum(people.getPeopleIDCode());
				signRecord.setAttendanceDate(SysUtil.time2Long(SysUtil.getNowTime()));
				signRecord.setClassroom(mAPP.getClassroom());
				signRecordDao.save(signRecord);
				mAccess.sign(signList);
			}
			
			@Override
			public void onReadFail(int code) {
				clear();
			}
		};
	}

	
	@Override
	public void onClick(View v) {
		boolean isExit = false;
		if (!SerialPortManager.getInstance().isOpen()
				&& !SerialPortManager.getInstance().openSerialPort()) {
			Toast.makeText(SFZActivity.this, "打开串口失败！",Toast.LENGTH_SHORT).show();
			isExit = true;
		}
		if(isExit){
			return;
		}
		int id = v.getId();
		switch (id) {
		case R.id.read_sfz:
			Log.i("whw", "read_sfz");
			asyncSFZ = new AsyncParseSFZ(this,onReadSFZListener);
			asyncSFZ.execute(SFZ.SECOND);
			break;
		case R.id.read_third_sfz:
			Log.i("whw", "read_third_sfz");
			asyncSFZ = new AsyncParseSFZ(this,onReadSFZListener);
			asyncSFZ.execute(SFZ.THIRD);
			break;
		default:
			break;
		}

	}



	@SuppressWarnings("deprecation")
	private void updateInfo(People people) {
		if(mediaPlayer.isPlaying()){
			mediaPlayer.seekTo(0);
		}else{
			mediaPlayer.start();
		}
		sfz_address.setText(people.getPeopleAddress());
		sfz_day.setText(people.getPeopleBirthday().substring(6));
		sfz_id.setText(people.getPeopleIDCode());
		sfz_mouth.setText(people.getPeopleBirthday().substring(4, 6));
		sfz_name.setText(people.getPeopleName());
		sfz_nation.setText(people.getPeopleNation());
		sfz_sex.setText(people.getPeopleSex());
		sfz_year.setText(people.getPeopleBirthday().substring(0, 4));
		if (people.getPhoto() != null) {
			Bitmap photo = BitmapFactory.decodeByteArray(people.getPhoto(), 0,
					people.getPhoto().length);
			sfz_photo.setBackgroundDrawable(new BitmapDrawable(photo));
		}
	}

	@Override
	protected void onDestroy() {
		mediaPlayer.release();
		mediaPlayer = null;
		Log.i("whw", "SFZActivity onDestroy");
		SerialPortManager.getInstance().closeSerialPort();
		super.onDestroy();
	}

	private void clear() {
		sfz_address.setText("");
		sfz_day.setText("");
		sfz_id.setText("");
		sfz_mouth.setText("");
		sfz_name.setText("");
		sfz_nation.setText("");
		sfz_sex.setText("");
		sfz_year.setText("");
		sfz_photo.setBackgroundColor(0);
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(!SerialPortManager.getInstance().isOpen()){
			SerialPortManager.getInstance().openSerialPort();
		}
		Log.i("whw", "onResume="+SerialPortManager.getInstance().isOpen());
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(SerialPortManager.getInstance().isOpen()){
			SerialPortManager.getInstance().closeSerialPort();
		}
	}
	

}
