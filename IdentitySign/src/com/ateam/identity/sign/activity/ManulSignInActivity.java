package com.ateam.identity.sign.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ateam.identity.sign.R;
import com.ateam.identity.sign.access.SignAccess;
import com.ateam.identity.sign.access.StudentAccess;
import com.ateam.identity.sign.access.I.HRequestCallback;
import com.ateam.identity.sign.moduel.HBaseObject;
import com.ateam.identity.sign.moduel.Student;
import com.ateam.identity.sign.moduel.StudentList;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SysUtil;
import com.ateam.identity.sign.widget.phonelist.IndexBarView;
import com.ateam.identity.sign.widget.phonelist.PinnedHeaderAdapter;
import com.ateam.identity.sign.widget.phonelist.PinnedHeaderListView;
import com.team.hbase.activity.HBaseActivity;
import com.team.hbase.utils.JSONParse;
import com.team.hbase.widget.dialog.CustomProgressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 手动签到界面
 * @author wtw
 * 2015-9-24下午3:43:03
 */
public class ManulSignInActivity extends HBaseActivity implements OnClickListener{
	
	// 显示数据
//	static final String[] ITEMS = new String[] { "阿木","魏天武","魏天文","魏阿","East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
//			"Eritrea", "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland", "Afghanistan",
//			"Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
//			"Antigua and Barbuda", "Argentina","小留","小魏","理他","周下款","李晓伟", "Armenia"};
//	private ArrayList<String> mItems=new ArrayList<String>();// 获取的数据
	private ArrayList<Integer> mListSectionPos;// 数据中含有的字母保存
	private ArrayList<String> mListItems;// 要进行显示的数组
	private PinnedHeaderListView mListView;// 显示数据的listview
	private PinnedHeaderAdapter mAdaptor;// listview适配器
	private ProgressBar mLoadingView;// loading view
	private TextView mEmptyView;// empty view
	private TextView mTvStudentName;//选中的学员名称
	private TextView mTvTime;//获取当前时间
	private TextView mTvShowDate;
	private ArrayList<Student> mListStudent;//获取的学员信息
	private CustomProgressDialog dialog;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_manul_sign_in);
		setActionBarTitle("手动签到");
		setupViews();
//		new Poplulate().execute(mItems);
		getStudentList();
	}
	
	private void setupViews() {
		dialog=new CustomProgressDialog(this,"学生列表获取中...");
		mTvStudentName=(TextView)findViewById(R.id.tv_studentName);
		mTvTime=(TextView)findViewById(R.id.tv_time);
		mTvShowDate=(TextView)findViewById(R.id.tv_showDate);
		mTvShowDate.setOnClickListener(this);
		mTvShowDate.setClickable(true);
		findViewById(R.id.btn_signin).setOnClickListener(this);
		mTvTime.setText(SysUtil.getNowTime());
		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		mEmptyView = (TextView) findViewById(R.id.empty_view);
//		mItems = new ArrayList<String>(Arrays.asList(ITEMS));
		mListSectionPos = new ArrayList<Integer>();
		mListItems = new ArrayList<String>();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_showDate:
			SysUtil.showDateListener(ManulSignInActivity.this, mTvTime);
			break;
			
		case R.id.btn_signin:
			signin();
			break;

		default:
			break;
		}
	}
	
	//获取学生信息
	private void getStudentList(){
		dialog.show();
		HRequestCallback<StudentList> request=new HRequestCallback<StudentList>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public StudentList parseJson(String jsonStr) {
				// TODO Auto-generated method stub
				Type type = new com.google.gson.reflect.TypeToken<StudentList>() {
				}.getType();
				return (StudentList) JSONParse.jsonToObject(
						jsonStr, type);
			}
			
			@Override
			public void onSuccess(StudentList result) {
				// TODO Auto-generated method stub
				mListStudent=(ArrayList<Student>) result.studentList;
//				for (int i = 0; i < mListStudent.size(); i++) {
////					mItems.add(mListStudent.get(i).getName()+" "+mListStudent.get(i).getCardNum());
//				}
				new Poplulate().execute(mListStudent);
			}
			
			@Override
			public void onFail(Context c, String errorMsg) {
				// TODO Auto-generated method stub
				super.onFail(c, errorMsg);
				dialog.dismiss();
				MyToast.showShort(c, errorMsg);
			}
		};
		StudentAccess access=new StudentAccess(ManulSignInActivity.this, request);
		access.findStudent("350722200906120029");
	}
	
	//签到
	private void signin(){
		dialog=new CustomProgressDialog(this, "签到中...");
		dialog.show();
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
				
			}
			
			@Override
			public void onFail(Context c, String errorMsg) {
				// TODO Auto-generated method stub
				super.onFail(c, errorMsg);
				dialog.dismiss();
				MyToast.showShort(c, errorMsg);
			}
		};
		SignAccess access=new SignAccess(ManulSignInActivity.this, request);
		//access.sign(signList);
	}
	
	//设置旁边滚动条，头部
	private void setListAdaptor() {
		// create instance of PinnedHeaderAdapter and set adapter to list view
		mAdaptor = new PinnedHeaderAdapter(this, mListItems, mListSectionPos);
		mListView.setAdapter(mAdaptor);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// set header view
		View pinnedHeaderView = inflater.inflate(R.layout.section_row_view, mListView, false);
		mListView.setPinnedHeaderView(pinnedHeaderView);

		// set index bar view
		IndexBarView indexBarView = (IndexBarView) inflater.inflate(R.layout.index_bar_view, mListView, false);
		indexBarView.setData(mListView, mListItems, mListSectionPos);
		mListView.setIndexBarView(indexBarView);

		// set preview text view
		View previewTextView = inflater.inflate(R.layout.preview_view, mListView, false);
		mListView.setPreviewView(previewTextView);

		// for configure pinned header view on scroll change
		mListView.setOnScrollListener(mAdaptor);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				MyToast.showShort(ManulSignInActivity.this, ""+mListItems.get(arg2));
				mTvStudentName.setText(""+mListItems.get(arg2));
			}
		});
	}


	// 处理数据
	private class Poplulate extends AsyncTask<ArrayList<Student>, Void, Void> {

		private void showLoading(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
			emptyView.setVisibility(View.GONE);
		}

		private void showContent(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.VISIBLE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
		}

		private void showEmptyText(View contentView, View loadingView, View emptyView) {
			contentView.setVisibility(View.GONE);
			loadingView.setVisibility(View.GONE);
			emptyView.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPreExecute() {
			// show loading indicator
			showLoading(mListView, mLoadingView, mEmptyView);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(ArrayList<Student>... params) {
			mListItems.clear();
			mListSectionPos.clear();
//			ArrayList<String> items = params[0];
			if (mListStudent.size() > 0) {
				Collections.sort(mListStudent, new SortIgnoreCase());
				String prev_section = "";
				for (Student student : mListStudent) {
					String current_item=student.getName();
					Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
					Matcher ms1 = p.matcher(current_item);
					String current_section;
					if(ms1.find()){
						current_section = SysUtil.converterToPinYin(current_item).substring(0, 1).toUpperCase(Locale.getDefault());
					}else{
						current_section = current_item.substring(0, 1).toUpperCase(Locale.getDefault());
					}
					if (!prev_section.equals(current_section)) {
						mListItems.add(current_section);
						mListItems.add(current_item);
						// array list of section positions
						mListSectionPos.add(mListItems.indexOf(current_section));
						prev_section = current_section;
					} else {
						mListItems.add(current_item);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			if (!isCancelled()) {
				if (mListItems.size() <= 0) {
					showEmptyText(mListView, mLoadingView, mEmptyView);
				} else {
					setListAdaptor();
					showContent(mListView, mLoadingView, mEmptyView);
				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 对数组进行排序
	 * @author wtw
	 * 2015-9-24下午1:38:32
	 */
	public class SortIgnoreCase implements Comparator<Student> {
		public int compare(Student s1, Student s2) {
			// 用来判断是否有中文字符，有的话进行转换为拼音添加
				Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
				Matcher ms1 = p.matcher(s1.getName());
				Matcher ms2 = p.matcher(s2.getName());
				if (ms1.find()&&ms2.find()) {
					return SysUtil.converterToPinYin(s1.getName()).compareToIgnoreCase(SysUtil.converterToPinYin(s2.getName()));
				}else if(ms1.find()&&!ms2.find()){
					return SysUtil.converterToPinYin(s1.getName()).compareToIgnoreCase(s2.getName());
				}else if(!ms1.find()&&ms2.find()){
					return s1.getName().compareToIgnoreCase(SysUtil.converterToPinYin(s2.getName()));
				}else{
					return s1.getName().compareToIgnoreCase(s2.getName());
				}
		}
	}
	
	public class ListFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread,
			// and
			// not the UI thread.
			String constraintStr = constraint.toString().toLowerCase(Locale.getDefault());
			FilterResults result = new FilterResults();

			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<String> filterItems = new ArrayList<String>();

				synchronized (this) {
					for (Student student : mListStudent) {
						String item=student.getName();
						if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
							filterItems.add(item);
						}
					}
					result.count = filterItems.size();
					result.values = filterItems;
				}
			} else {
				synchronized (this) {
					result.count = mListStudent.size();
					result.values = mListStudent;
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<Student> filtered = (ArrayList<Student>) results.values;
			setIndexBarViewVisibility(constraint.toString());
			// sort array and extract sections in background Thread
			new Poplulate().execute(filtered);
		}

	}

	private void setIndexBarViewVisibility(String constraint) {
		// hide index bar for search results
		if (constraint != null && constraint.length() > 0) {
			mListView.setIndexBarVisibility(false);
		} else {
			mListView.setIndexBarVisibility(true);
		}
	}

}
