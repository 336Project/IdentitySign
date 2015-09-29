package com.ateam.identity.sign;



import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.AsyncParseSFZ;
import android_serialport_api.ParseSFZAPI;
import android_serialport_api.ParseSFZAPI.People;
import android_serialport_api.SerialPortManager;


public class SFZActivity extends BaseActivity implements OnClickListener {
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
	private Button clear_button;
	private Button module_button;
	private Button id_button;

	private Button sequential_read;
	private Button stop;
	private TextView resultInfo;

	private TextView moduleView;

	private ProgressDialog progressDialog;


	private AsyncParseSFZ asyncParseSFZ;

	private int readTime = 0;
	private int readFailTime = 0;
	private int readTimeout = 0;
	private int readSuccessTime = 0;
	/**
	 * 是否是连续读取
	 */
	private boolean isSequentialRead = false;

	private Handler mHandler = new Handler();

	MediaPlayer mediaPlayer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sfz_activity);
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
		clear_button = ((Button) findViewById(R.id.clear_sfz));
		module_button = ((Button) findViewById(R.id.read_module));
		id_button = ((Button) findViewById(R.id.read_id));
		moduleView = ((TextView) findViewById(R.id.module));
		sequential_read = ((Button) findViewById(R.id.sequential_read));
		stop = ((Button) findViewById(R.id.stop));
		resultInfo = ((TextView) findViewById(R.id.resultInfo));

		read_button.setOnClickListener(this);
		read_third_button.setOnClickListener(this);
		clear_button.setOnClickListener(this);
		module_button.setOnClickListener(this);
		id_button.setOnClickListener(this);
		sequential_read.setOnClickListener(this);
		stop.setOnClickListener(this);
	}

	private void initData() {
		mediaPlayer = MediaPlayer.create(this, R.raw.ok);
		asyncParseSFZ = new AsyncParseSFZ(application.getHandlerThread().getLooper(),application.getRootPath());
		asyncParseSFZ.setOnReadSFZListener(new AsyncParseSFZ.OnReadSFZListener() {
			@Override
			public void onReadSuccess(People people) {
				cancleProgressDialog();
				updateInfo(people);
				readSuccessTime++;
				refresh(isSequentialRead);
				endTime = System.currentTimeMillis();
				Log.i("whw", "@@@@@@@@@@@@@cost time="+(endTime-startTime));
			}

			@Override
			public void onReadFail(int confirmationCode) {
				cancleProgressDialog();
				if(confirmationCode == ParseSFZAPI.Result.FIND_FAIL){
					Toast.makeText(SFZActivity.this, "未寻到卡,有返回数据",Toast.LENGTH_SHORT).show();
				}else if(confirmationCode == ParseSFZAPI.Result.TIME_OUT){
					Toast.makeText(SFZActivity.this, "未寻到卡,无返回数据，超时！！",Toast.LENGTH_SHORT).show();
					readTimeout++;
				}else if(confirmationCode == ParseSFZAPI.Result.OTHER_EXCEPTION){
					Toast.makeText(SFZActivity.this, "可能是串口打开失败或其他异常",Toast.LENGTH_SHORT).show();
				}
				
				readFailTime++;
				clear();
				refresh(isSequentialRead);
			}
		});

		asyncParseSFZ.setOnReadModuleListener(new AsyncParseSFZ.OnReadModuleListener() {

			@Override
			public void onReadSuccess(String module) {
				moduleView.setText(module);
			}

			@Override
			public void onReadFail(int confirmationCode) {
//				ToastUtil.showToast(SFZActivity.this, "读模块号失败！");
				if(confirmationCode == ParseSFZAPI.Result.FIND_FAIL){
					Toast.makeText(SFZActivity.this, "读模块号失败,有返回数据",Toast.LENGTH_SHORT).show();
				}else if(confirmationCode == ParseSFZAPI.Result.TIME_OUT){
					Toast.makeText(SFZActivity.this, "读模块号失败,无返回数据，超时！！",Toast.LENGTH_SHORT).show();
				}else if(confirmationCode == ParseSFZAPI.Result.OTHER_EXCEPTION){
					Toast.makeText(SFZActivity.this, "可能是串口打开失败或其他异常",Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		asyncParseSFZ.setOnReadCardIDListener(new AsyncParseSFZ.OnReadCardIDListener() {
			
			@Override
			public void onReadSuccess(String id) {
				moduleView.setText(id);
			}
			
			@Override
			public void onReadFail() {
				Toast.makeText(SFZActivity.this, "读取卡号失败",Toast.LENGTH_SHORT).show();
			}
		});
	}

	private long startTime = 0;
	private long endTime = 0;
	
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
			startTime = System.currentTimeMillis();
			resultInfo.setText("");
			isSequentialRead = false;
			showProgressDialog("正在读取数据...");
			asyncParseSFZ.readSFZ(ParseSFZAPI.SECOND_GENERATION_CARD);
			Log.i("whw", "read_sfz");
			break;
		case R.id.read_third_sfz:
			resultInfo.setText("");
			isSequentialRead = false;
			showProgressDialog("正在读取数据...");
			asyncParseSFZ.readSFZ(ParseSFZAPI.THIRD_GENERATION_CARD);
			Log.i("whw", "read_third_sfz");
			break;
		case R.id.clear_sfz:
			clear();
			break;
		case R.id.read_module:
			moduleView.setText("");
			asyncParseSFZ.readModuleNum();
			break;
		case R.id.read_id:
			moduleView.setText("");
			asyncParseSFZ.readCardID();
			break;
		case R.id.sequential_read:
			isSequentialRead = true;
			readTime = 0;
			readFailTime = 0;
			readTimeout=0;
			readSuccessTime = 0;
			mHandler.post(task);
			break;
		case R.id.stop:
			mHandler.removeCallbacks(task);
		default:
			break;
		}

	}

	private Runnable task = new Runnable() {
		@Override
		public void run() {
			readTime++;
			showProgressDialog("正在读取数据...");
			asyncParseSFZ.readSFZ(ParseSFZAPI.SECOND_GENERATION_CARD);
		}
	};

	private void refresh(boolean isSequentialRead) {
		if (!isSequentialRead) {
			return;
		}
		mHandler.postDelayed(task, 2000);
		String result = "总共：" + readTime + "  成功：" + readSuccessTime + "  失败："
				+ readFailTime+"\n 超时次数："+readTimeout;
		Log.i("whw", "result=" + result);
		resultInfo.setText(result);
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
		mHandler.removeCallbacks(task);
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

		moduleView.setText("");
	}

	private void showProgressDialog(String message) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	private void cancleProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	

}
