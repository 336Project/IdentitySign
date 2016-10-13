package com.ateam.identity.sign.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.ateam.identity.sign.util.PermissionHelper;
import com.team.hbase.activity.HBaseActivity;

/**
 * Created by 李晓伟 on 2016/10/13.
 * 6.0动态权限管理
 */

public class PermissionActivity extends HBaseActivity implements PermissionHelper.OnApplyPermissionListener{
    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionHelper = new PermissionHelper(this);
        if(Build.VERSION.SDK_INT >=23){
            if(!mPermissionHelper.isAllRequestedPermissionGranted()){
                mPermissionHelper.applyPermissions();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAfterApplyAllPermission() {

    }
}
