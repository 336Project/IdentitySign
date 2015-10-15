package com.ateam.identity.sign.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.team.hbase.utils.AppManager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {
    private static final String LOG_FILE_NAME = "AppErrorLog.log";
    private static final int LOG_MAX_SIZE = 1024 * 1024;//1M

    private static CrashHandler INSTANCE;
    private Context mContext;
    private UncaughtExceptionHandler mDefaultHandler;
    //设备信息
    private Map<String,String> mDeviceInfos=new HashMap<String,String>();

    private CrashHandler() {

    }

    public synchronized static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
            INSTANCE.mContext = context;
            INSTANCE.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
        }

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(thread, ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
        	try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
            //退出程序  
            AppManager.getInstance().ExitApp();
        }
    }

    private boolean handleException(Thread thread,final Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext,"很抱歉，程序出现错误，即将退出...",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        Log.e("CrashHandler.java",Log.getStackTraceString(ex));
        log(ex);
        return true;
    }
    
    private void log(Throwable ex){
    	collectDeviceInfo(mContext);
    	saveCrashInfoToFile(ex);
    }

    /**
     * 收集设备信息
     * @param context
     */
    private void collectDeviceInfo(Context context) {
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
            if(pi != null){
                String versionName = pi.versionName;
                String versionCode = String.valueOf(pi.versionCode);
                mDeviceInfos.put("versionName", versionName);
                mDeviceInfos.put("versionCode", versionCode);
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        //利用反射机制获取设备信息
        Field[] fields= Build.class.getFields();
        for (Field field:fields){
            try{
                field.setAccessible(true);
                mDeviceInfos.put(field.getName(), field.get(null).toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存异常信息到文件
     * @param ex
     * @return  返回文件所在完整名称,便于将文件传送到服务器
     */
    private String saveCrashInfoToFile(Throwable ex) {
        if(!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){//如果没有sdcard，则不存储
            return null;
        }
        Writer writer = null;
        PrintWriter printWriter = null;
        String stackTraceMsg = "";
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            stackTraceMsg = writer.toString();
        } catch (Exception e) {
        }finally{
            try {
                if(writer!=null) {
                    writer.close();
                }
                if(printWriter!=null){
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        String timestamp = sdf.format(new Date());
        sb.append(timestamp);//记录每个error的发生时间
        sb.append(System.getProperty("line.separator"));
        //打印设备信息
        for(Map.Entry<String,String> entry:mDeviceInfos.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            sb.append(key +"："+value);
            sb.append(System.getProperty("line.separator"));
        }
        //打印错误信息
        sb.append(stackTraceMsg);
        sb.append(System.getProperty("line.separator"));//每个error间隔2行
        sb.append(System.getProperty("line.separator"));
        FileOutputStream fos=null;
        try {
            String fileName="";
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                String dirPath= Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + mContext.getPackageName() + "/crash/";
                File dir = new File(dirPath);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                fileName=dirPath + LOG_FILE_NAME;
                File file = new File(fileName);
                if(!file.exists()){
                    file.createNewFile();
                }else if(file.length() > LOG_MAX_SIZE){//如果超过最大文件大小，则重新创建一个文件
                    file.delete();
                    file.createNewFile();
                }
                fos = new FileOutputStream(fileName,true);
                fos.write(sb.toString().getBytes());
                fos.flush();
            }
            return fileName;
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
