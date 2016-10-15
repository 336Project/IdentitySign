package com.ateam.identity.sign.util;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.ParseException;
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static long time2Long(String date) {

        if(date.equals(""))
            return 0;

        try {
            String format = "yyyy/MM/dd HH:mm:ss";
            SimpleDateFormat sf = new SimpleDateFormat(format);
            return sf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
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
                    pinyinString += PinyinHelper.toHanyuPinyinStringArray(charArray[i], defaultFormat)[0].charAt(0);
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
     */
    @SuppressWarnings("deprecation")
    public static void showDateListener(Context context, final View view) {
        Dialog dialog = new DateTimeDialog(context, R.style.my_dialog_theme, new OnDateTimeChangeListener() {

            @Override
            public void onConfirmDatetime(String datetime) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(datetime);
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(datetime);
                }
            }
        }, true);
        dialog.show();
        WindowManager windowManager = dialog.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth() / 2); //设置宽度
        lp.height = (int) (display.getHeight());
        dialog.getWindow().setAttributes(lp);
    }

    //密码加密
    public static String Encryption(String str) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("md5");
            byte[] buf = md.digest(str.getBytes());
            return Base64EncoderByJINFA.encode(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @return
     * @TODO 判断网络连接是否正常
     */
    public static boolean isNetworkConnected(final Context c) {
        ConnectivityManager manager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        }
        return false;
    }

    public static String formate(String str) {
        try {
            str = new String(str.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
