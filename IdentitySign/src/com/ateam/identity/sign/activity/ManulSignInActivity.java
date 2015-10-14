package com.ateam.identity.sign.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.R;
import com.ateam.identity.sign.access.SignAccess;
import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.dao.StudentDao;
import com.ateam.identity.sign.dao.UnCommitInfoDao;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.SignObject;
import com.ateam.identity.sign.moduel.Student;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SysUtil;
import com.ateam.identity.sign.widget.phonelist2.CharacterParser;
import com.ateam.identity.sign.widget.phonelist2.PinyinComparator;
import com.ateam.identity.sign.widget.phonelist2.SideBar;
import com.ateam.identity.sign.widget.phonelist2.SideBar.OnTouchingLetterChangedListener;
import com.ateam.identity.sign.widget.phonelist2.SortAdapter;
import com.ateam.identity.sign.widget.phonelist2.SortModel;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.JSONParse;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author wtw 2015-9-24下午3:43:03
 */
public class ManulSignInActivity extends HBaseActivity implements OnClickListener{
	private ListView sortListView;// 数据显示的listview
	private SideBar sideBar;// 字母滑动的bar
	private TextView dialog;// 显示当前选中的字母
	private SortAdapter adapter;// listviewadapter
	private CharacterParser characterParser;//汉字转换成拼音的类
	private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类
	
	private List<SortModel> SourceDateList;
	private ArrayList<Student> mListStudent;//获取的学员信息
	private MyApplication mAPP;
	private StudentDao studentDao;
	private TextView mTvTime;//获取当前时间
	private TextView mTvShowDate;//点击后显示日历对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.reciving_adress);
		initViews();
	}

	private void initViews() {
		mAPP=(MyApplication) getApplication();
		studentDao=new StudentDao(this);
		mTvTime=(TextView)findViewById(R.id.tv_time);
		mTvShowDate=(TextView)findViewById(R.id.tv_showDate);
		mTvShowDate.setOnClickListener(this);
		mTvShowDate.setClickable(true);
		findViewById(R.id.btn_signin).setOnClickListener(this);
		mTvTime.setText(SysUtil.getNowTime());
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				MyToast.showShort(ManulSignInActivity.this,((SortModel) adapter.getItem(position)).getName());
				finish();
			}
		});
		initData();
	}
	
	//获取数据
	private void initData(){
		mListStudent=(ArrayList<Student>) studentDao.findByTeacherID(mAPP.getUser().getCardNum());
		if(mListStudent!=null&&mListStudent.size()>0){
			SourceDateList = filledData(mListStudent);

			// 根据a-z进行排序源数据
			Collections.sort(SourceDateList, pinyinComparator);
			adapter = new SortAdapter(ManulSignInActivity.this, SourceDateList);
			sortListView.setAdapter(adapter);
		}else{
			MyToast.showShort(this, "暂没有学生信息");
			findViewById(R.id.btn_signin).setClickable(false);
		}
	}

	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(ArrayList<Student> date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date.get(i).getName());
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date.get(i).getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;

	}
	
	//签到
	private void signin(){
		HRequestCallback<HBaseObject> request=new HRequestCallback<HBaseObject>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public HBaseObject parseJson(String jsonStr) {
				// TODO Auto-generated method stub
				Type type = new com.google.gson.reflect.TypeToken<HBaseObject>() {
				}.getType();
				return (HBaseObject) JSONParse.jsonToObject(
						jsonStr, type);
			}
			
			@Override
			public void onSuccess(HBaseObject result) {
				MyToast.showShort(ManulSignInActivity.this, "已签到");
				if(!result.isSuccess()){
//					ArrayList<Integer> 
//					for (int i = 0; i < array.length; i++) {
//						
//					}
//					SignObject sign=new SignObject(mStudentCard, mTvTime.getText().toString());
//					UnCommitInfoDao dao=new UnCommitInfoDao(ManulSignInActivity.this);
//					dao.save(sign);
				}
			}
			
			@Override
			public void onFail(Context c, String errorMsg) {
				// TODO Auto-generated method stub
				//super.onFail(c, errorMsg);
				MyToast.showShort(ManulSignInActivity.this, "已签到");
//				SignObject sign=new SignObject(mStudentCard, mTvTime.getText().toString());
//				UnCommitInfoDao dao=new UnCommitInfoDao(ManulSignInActivity.this);
//				dao.save(sign);
			}
		};
		SignAccess access=new SignAccess(ManulSignInActivity.this, request);
		ArrayList<SignObject> data=new ArrayList<SignObject>();
		//需要时间格式化：SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//by 天舞
//		SignObject sign=new SignObject(mStudentCard, mTvTime.getText().toString());
//		data.add(sign);
//		access.sign(data);
	}
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_showDate:
			SysUtil.showDateListener(ManulSignInActivity.this, mTvTime);
			break;
			
		case R.id.btn_signin:
			if(adapter.getSelectItem().size()==0){
				MyToast.showShort(ManulSignInActivity.this, "您还未选择签到人员!");
				return;
			}
			signin();
			break;

		default:
			break;
		}
	}
}
