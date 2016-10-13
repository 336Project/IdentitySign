package com.ateam.identity.sign.access;

import android.content.Context;

import com.ateam.identity.sign.util.SharedPreferencesUtil;
import com.team.hbase.access.HBaseAccess;
import com.team.hbase.access.inter.HRequestCallback;

import java.io.File;
import java.util.Map;

/**
 * Created by 李晓伟 on 2016/10/13.
 *
 */

public class AbsAccess<T> extends HBaseAccess<T> {

    public AbsAccess(Context c, HRequestCallback requestCallback) {
        super(c, requestCallback);
    }

    @Override
    protected void execute(String url) {
        url = SharedPreferencesUtil.getURL(mContext) + url;
        super.execute(url);
    }

    @Override
    protected void execute(String url, Map<String, String> map) {
        url = SharedPreferencesUtil.getURL(mContext) + url;
        super.execute(url, map);
    }

    @Override
    protected void execute(String url, Map<String, String> map, Map<String, File> files) {
        url = SharedPreferencesUtil.getURL(mContext) + url;
        super.execute(url, map, files);
    }
}
