package com.ateam.identity.sign.util;

import android.annotation.SuppressLint;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	@SuppressLint("SimpleDateFormat")
	public static String getNowTime() {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日 HH:mm:ss");
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
}
