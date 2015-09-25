package com.ateam.identity.sign.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.ateam.identity.sign.R;
import com.ateam.identity.sign.util.MyToast;
import com.ateam.identity.sign.util.SysUtil;
import com.ateam.identity.sign.widget.phonelist.IndexBarView;
import com.ateam.identity.sign.widget.phonelist.PinnedHeaderAdapter;
import com.ateam.identity.sign.widget.phonelist.PinnedHeaderListView;
import com.team.hbase.activity.HBaseActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 手动签到界面
 * @author wtw
 * 2015-9-24下午3:43:03
 */
public class ManulSignInActivity extends HBaseActivity {
	
	// 显示数据
	static final String[] ITEMS = new String[] { "阿木","魏天武","魏天文","魏阿","East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
			"Eritrea", "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland", "Afghanistan",
			"Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
			"Antigua and Barbuda", "Argentina","小留","小魏","理他","周下款","李晓伟", "Armenia"};
	private ArrayList<String> mItems;// 获取的数据
	private ArrayList<Integer> mListSectionPos;// 数据中含有的字母保存
	private ArrayList<String> mListItems;// 要进行显示的数组
	private PinnedHeaderListView mListView;// 显示数据的listview
	private PinnedHeaderAdapter mAdaptor;// listview适配器
	private ProgressBar mLoadingView;// loading view
	private TextView mEmptyView;// empty view
	private TextView mTvStudentName;//选中的学员名称
	private TextView mTvTime;//获取当前时间

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.activity_manul_sign_in);
		setActionBarTitle("手动签到");
		setupViews();
		// for handling configuration change
		if (savedInstanceState != null) {
			mListItems = savedInstanceState.getStringArrayList("mListItems");
			mListSectionPos = savedInstanceState.getIntegerArrayList("mListSectionPos");
			if (mListItems != null && mListItems.size() > 0 && mListSectionPos != null && mListSectionPos.size() > 0) {
				setListAdaptor();
			}
			String constraint = savedInstanceState.getString("constraint");
			if (constraint != null && constraint.length() > 0) {
				setIndexBarViewVisibility(constraint);
			}
		} else {
			new Poplulate().execute(mItems);
		}
	}
	
	private void setupViews() {
		mTvStudentName=(TextView)findViewById(R.id.tv_studentName);
		mTvTime=(TextView)findViewById(R.id.tv_time);
		mTvTime.setText(SysUtil.getNowTime());
		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		mEmptyView = (TextView) findViewById(R.id.empty_view);
		mItems = new ArrayList<String>(Arrays.asList(ITEMS));
		mListSectionPos = new ArrayList<Integer>();
		mListItems = new ArrayList<String>();
	}
	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
	
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
					for (String item : mItems) {
						if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
							filterItems.add(item);
						}
					}
					result.count = filterItems.size();
					result.values = filterItems;
				}
			} else {
				synchronized (this) {
					result.count = mItems.size();
					result.values = mItems;
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<String> filtered = (ArrayList<String>) results.values;
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

	// sort array and extract sections in background Thread here we use
	// AsyncTask
	private class Poplulate extends AsyncTask<ArrayList<String>, Void, Void> {

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
		protected Void doInBackground(ArrayList<String>... params) {
			mListItems.clear();
			mListSectionPos.clear();
			ArrayList<String> items = params[0];
			if (mItems.size() > 0) {
				Collections.sort(items, new SortIgnoreCase());
				String prev_section = "";
				for (String current_item : items) {
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
	public class SortIgnoreCase implements Comparator<String> {
		public int compare(String s1, String s2) {
			// 用来判断是否有中文字符，有的话进行转换为拼音添加
				Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
				Matcher ms1 = p.matcher(s1);
				Matcher ms2 = p.matcher(s2);
				if (ms1.find()&&ms2.find()) {
					return SysUtil.converterToPinYin(s1).compareToIgnoreCase(SysUtil.converterToPinYin(s2));
				}else if(ms1.find()&&!ms2.find()){
					return SysUtil.converterToPinYin(s1).compareToIgnoreCase(s2);
				}else if(!ms1.find()&&ms2.find()){
					return s1.compareToIgnoreCase(SysUtil.converterToPinYin(s2));
				}else{
					return s1.compareToIgnoreCase(s2);
				}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mListItems != null && mListItems.size() > 0) {
			outState.putStringArrayList("mListItems", mListItems);
		}
		if (mListSectionPos != null && mListSectionPos.size() > 0) {
			outState.putIntegerArrayList("mListSectionPos", mListSectionPos);
		}
		super.onSaveInstanceState(outState);
	}

}
