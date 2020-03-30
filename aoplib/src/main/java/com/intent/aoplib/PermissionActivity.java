package com.intent.aoplib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.intent.aoplib.utils.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

/**
 * 类描述:
 * 作者:xues
 * 时间:2020年03月29日
 */
public class PermissionActivity extends Activity {
    private static final String PERMISSIONS = "permissions";
    private static final String CALL_BACK = "call_back";
    private static final int REQUEST_PERMISSION_CODE = 110;
    private String permissions[];
    private static IPermission mCallBack;
    private static final String TAG = "PermissionActivity";

    public static void requestPermission(Context context, String[] permissions, IPermission callBack) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(PERMISSIONS, permissions);
        mCallBack = callBack;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        permissions = bundle.getStringArray(PERMISSIONS);
        for (String permission : permissions) {
            Log.i(TAG, "onCreate: " + permission);
        }
        requestPermissions();
    }


    public void requestPermissions() {
        if (PermissionUtil.hasSelfPermissions(this, permissions)) {
            finish();
            mCallBack.onSuccess();
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (PermissionUtil.isGranted(grantResults)) {
                    finish();
                    mCallBack.onSuccess();
                } else {
                    //美团：建议您开启定位权限，就能看到更多的周边好点和惊喜优惠
                    new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("建议您开启相关权限,否则将会影响您的使用")
                            .setCancelable(false)
                            .setNegativeButton("暂不", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PermissionUtil.toAppDetail(PermissionActivity.this, 123);
                            finish();
                        }
                    }).show();
                }
                break;
        }
        mCallBack = null;
    }
}
