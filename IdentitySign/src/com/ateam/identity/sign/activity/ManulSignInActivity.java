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
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.JSONParse;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
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
	
	private List<Student> SourceDateList;
	private List<Student> mListStudent;//获取的学员信息
	private MyApplication mAPP;
	private StudentDao studentDao;
	private TextView mTvTime;//获取当前时间
	private TextView mTvShowDate;//点击后显示日历对话框
	private ArrayList<Student> mCommitStudent;//要提交的学生的信息

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.reciving_adress);
		setActionBarTitle("手动签到");
		getLeftIcon().setImageResource(R.drawable.icon_back);
		getRightIcon().setVisibility(View.GONE);
		initViews();
	}

	private void initViews() {
		mAPP=(MyApplication) getApplication();
		studentDao=new StudentDao(this);
		mCommitStudent=new ArrayList<Student>();
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
				if(adapter == null){
					return;
				}
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
				ImageView mivSelect = (ImageView)view.findViewById(R.id.iv_select);
				if(mivSelect.getVisibility()==View.GONE){
					mCommitStudent.add((Student)adapter.getItem(position));
					mivSelect.setVisibility(View.VISIBLE);
					adapter.setItemSelect(position, true);
				}else{
					mCommitStudent.remove((Student)adapter.getItem(position));
					mivSelect.setVisibility(View.GONE);
					adapter.setItemSelect(position, false);
				}
//				MyToast.showShort(ManulSignInActivity.this,((Student) adapter.getItem(position)).getName());
			}
		});
		initData();
	}
	
	//获取数据
	private void initData(){
		mListStudent=studentDao.findByTeacherID(mAPP.getUser().getCardNum(),mAPP.getClassroom());
		//测试数据
//		mListStudent=new ArrayList<Student>();
//		for (int i = 0; i < 10; i++) {
//			Student student=new Student();
//			student.setCardNum("1021252455214"+i);
//			if(i==0){
//				student.setName("xiaoli"+i);
//			}else if(i==1){
//				student.setName("biaoli"+i);
//			}else{
//				student.setName("miaoli"+i);
//			}
//			mListStudent.add(student);
//		}
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
	private List<Student> filledData(List<Student> date) {
		List<Student> mSortList = new ArrayList<Student>();

		for (int i = 0; i < date.size(); i++) {
			Student sortModel = new Student();
			sortModel.setName(date.get(i).getName());
			sortModel.setCardNum(date.get(i).getCardNum());
			sortModel.setClassroom(date.get(i).getClassroom());
			sortModel.setSex(date.get(i).getSex());
			sortModel.settCardNum(date.get(i).gettCardNum());
			sortModel.setSex(date.get(i).getSex());
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
				MyToast.showShort(ManulSignInActivity.this, "签到成功");
				if(!result.isSuccess()){
					for (int i = 0; i < mCommitStudent.size(); i++) {
						SignObject sign=new SignObject(mCommitStudent.get(i).getCardNum(), mTvTime.getText().toString(),SignObject.TYPE_MANUL);
						UnCommitInfoDao dao=new UnCommitInfoDao(ManulSignInActivity.this);
						dao.save(sign);
					}
				}
			}
			
			@Override
			public void onFail(Context c, String errorMsg) {
				// TODO Auto-generated method stub
				//super.onFail(c, errorMsg);
				MyToast.showShort(ManulSignInActivity.this, "签到成功");
				for (int i = 0; i < mCommitStudent.size(); i++) {
					SignObject sign=new SignObject(mCommitStudent.get(i).getCardNum(), mTvTime.getText().toString(),SignObject.TYPE_MANUL);
					UnCommitInfoDao dao=new UnCommitInfoDao(ManulSignInActivity.this);
					dao.save(sign);
				}
			}
		};
		SignAccess access=new SignAccess(ManulSignInActivity.this, request);
		ArrayList<SignObject> data=new ArrayList<SignObject>();
		for (int i = 0; i < mCommitStudent.size(); i++) {
			SignObject sign=new SignObject(mCommitStudent.get(i).getCardNum(), mTvTime.getText().toString(),SignObject.TYPE_MANUL);
			data.add(sign);
		}
		//需要时间格式化：SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//by 天舞
		access.sign(data);
	}
		

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_showDate:
			SysUtil.showDateListener(ManulSignInActivity.this, mTvTime);
			break;
			
		case R.id.btn_signin:
			if(mCommitStudent.size()==0){
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
