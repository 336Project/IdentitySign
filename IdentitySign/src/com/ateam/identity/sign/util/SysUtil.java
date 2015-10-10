package com.ateam.identity.sign.util;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.ateam.identity.sign.R;
import com.ateam.identity.sign.widget.datewheel.DateTimeDialog;
import com.ateam.identity.sign.widget.datewheel.DateTimeDialog.OnDateTimeChangeListener;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 小工具类
 * 
 * @author wtw 2015-9-24下午4:29:03
 */
public class SysUtil {

	// 获取当前时间
	public static String getNowTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

	// 中文转英文
	public static String converterToPinYin(String chinese) {
		String pinyinString = "";
		char[] charArray = chinese.toCharArray();
		// 根据需要定制输出格式，我用默认的即可
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		try {
			// 遍历数组，ASC码大于128进行转换
			for (int i = 0; i < charArray.length; i++) {
				if (charArray[i] > 128) {
					// charAt(0)取出首字母
					pinyinString += PinyinHelper.toHanyuPinyinStringArray(
							charArray[i], defaultFormat)[0].charAt(0);
				} else {
					pinyinString += charArray[i];
				}
			}
			return pinyinString;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 点击日历按钮后进行的监听操作
	 * 
	 * @param showDate1
	 *            :显示日历的按钮控件对象
	 * @param editText
	 *            :进行显示选中时间的 EditText对象
	 */
	public static void showDateListener(Context context,final View view) {
		Dialog dialog = new DateTimeDialog(
				context,
				R.style.my_dialog_theme,
				new OnDateTimeChangeListener() {

					@Override
					public void onConfirmDatetime(String datetime) {
						if(view instanceof TextView){
							((TextView)view).setText(datetime);
						}else if(view instanceof EditText){
							((EditText)view).setText(datetime);
						}
					}
				},true);
		dialog.show();
	}
	
	//密码加密
	public static String Encryption(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("md5");
		byte[] buf = md.digest(str.getBytes());
		return Base64EncoderByJINFA.encode(buf);
	}
	
	/**
	 * @return
	 * @TODO 判断网络连接是否正常
	 */
	public static boolean isNetworkConnected(final Context c){
		ConnectivityManager manager=(ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=manager.getActiveNetworkInfo();
		if(info!=null&&info.isAvailable()){
			return true;
		}
		return false;
	}
	
}
