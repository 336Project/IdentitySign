package android_serialport_api;

import java.util.HashMap;
import java.util.Map;

import com.ateam.identity.sign.MyApplication;
import com.ateam.identity.sign.util.MyToast;
import com.team.hbase.widget.dialog.CustomProgressDialog;

import android.content.Context;
import android.os.AsyncTask;
import android_serialport_api.ParseSFZAPI.People;
import android_serialport_api.ParseSFZAPI.Result;

public class AsyncParseSFZ extends AsyncTask<AsyncParseSFZ.SFZ, Integer, Map<String, Object>>{
	private static final String DATA = "data";
	private static final String CODE = "code";
	private ParseSFZAPI parseAPI;
	
	private CustomProgressDialog progressDialog;
	private Context mContext;
	private OnReadSFZListener onReadSFZListener;
	public AsyncParseSFZ(Context context,OnReadSFZListener onReadSFZListener) {
		super();
		this.mContext = context;
		this.onReadSFZListener = onReadSFZListener;
		MyApplication application=(MyApplication)context.getApplicationContext();
		parseAPI = new ParseSFZAPI(application.getHandlerThread()
				.getLooper(), application.getRootPath(), context);
	}

	@Override
	protected void onPreExecute() {
		showProgressDialog("正在读取数据...");
	}
	
	@Override
	protected Map<String, Object> doInBackground(SFZ... params) {
		Map<String, Object> result = new HashMap<String, Object>();
		switch (params[0]) {
		case SECOND://读二代证
			Result resultSFZ = parseAPI.read(ParseSFZAPI.SECOND_GENERATION_CARD);
			result.put(DATA, resultSFZ.resultInfo);
			result.put(CODE, resultSFZ.confirmationCode);
			break;
		case THIRD://读三代证
			resultSFZ = parseAPI.read(ParseSFZAPI.THIRD_GENERATION_CARD);
			result.put(DATA, resultSFZ.resultInfo);
			result.put(CODE, resultSFZ.confirmationCode);
			break;
		default:
			break;
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(Map<String, Object> result) {
		cancleProgressDialog();
		try {
			if(onReadSFZListener == null) return;
			/**
			 * 确认码 1: 成功 2：失败 3: 超时 6：其它异常
			 */
			int status = Integer.parseInt(String.valueOf(result.get(CODE)));	
			switch(status){
				case Result.SUCCESS:
					onReadSFZListener.onReadSuccess((People)result.get(DATA));
					break;
				case Result.FIND_FAIL:
					if(isShowDialog){
						MyToast.showShort(mContext, "读卡失败，请换另外一种签到方式试试");
					}
					//MyToast.showShort(mContext, "读卡失败。");
					onReadSFZListener.onReadFail(status);
					break;
				case Result.TIME_OUT:
					if(isShowDialog){
						MyToast.showShort(mContext, "读卡失败，请换另外一种签到方式试试");
					}
					onReadSFZListener.onReadFail(status);
					//MyToast.showShort(mContext, "读卡超时，请重试。");
					break;
				case Result.OTHER_EXCEPTION:
					if(isShowDialog){
						MyToast.showShort(mContext, "读卡失败，请换另外一种签到方式试试");
					}
					onReadSFZListener.onReadFail(status);
					//MyToast.showShort(mContext, "读卡时发生异常。");
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(isShowDialog){
				MyToast.showShort(mContext, "读卡失败，请换另外一种签到方式试试");
			}
			onReadSFZListener.onReadFail(Result.OTHER_EXCEPTION);
		}
		
	}
	/**
	 * 
	 * 2015-9-30 下午9:25:56
	 * @param message
	 * @TODO 
	 */
	private boolean isShowDialog = true;
	private void showProgressDialog(String message) {
		if(isShowDialog){
			progressDialog = new CustomProgressDialog(mContext,message);
			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
		}
	}
	/**
	 * 
	 * 2015-9-30 下午9:26:09
	 * @TODO
	 */
	private void cancleProgressDialog() {
		if (isShowDialog && progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}
	
	public void setShowDialog(boolean isShowDialog){
		this.isShowDialog = isShowDialog;
	}
	/**
	 * 
	 * @author 李晓伟
	 * 2015-9-30 下午9:21:06
	 * @TODO IDCard type
	 */
	public enum SFZ{
		SECOND,//二代证
		THIRD//三代证
	}
	
	public interface OnReadSFZListener {
		void onReadSuccess(People people);
		void onReadFail(int code);
	}
}
