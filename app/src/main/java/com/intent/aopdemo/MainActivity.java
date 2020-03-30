package com.intent.aopdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.intent.aoplib.annotation.CheckNet;
import com.intent.aoplib.annotation.CheckPermission;
import com.intent.aoplib.utils.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @CheckNet(value = "12")
    public void toNext(View view) {
        Log.i(TAG, "toNext: 有网络跳转");

    }


    private static final int REQUEST_PERMISSION = 110;

    private static final String[] PERMISSION_STR = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public void requestPermission() {
        if (PermissionUtil.hasSelfPermissions(this, PERMISSION_STR)) {
            //1、已获取所有权限
            Log.i(TAG, "===1===");
            Log.i(TAG, "requestPermission: 已经全部授权");
        } else {
            //2、未获得权限
            Log.i(TAG, "===2===");
            ActivityCompat.requestPermissions(this, PERMISSION_STR, REQUEST_PERMISSION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (PermissionUtil.isGranted(grantResults)) {
                    //5、许可被授予
                    Log.i(TAG, "===3===");
                } else {
                    //6、许可被拒绝
                    Log.i(TAG, "===4===");
                    new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("建议您开启定位权限，就能看到更多的周边好点和惊喜优惠")
                            .setNegativeButton("暂不", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PermissionUtil.toAppDetail(MainActivity.this, 123);
                        }
                    }).show();
                }
                break;
        }
    }


    @CheckPermission(permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA})
    public void onRequestLocationPermissiont(View view) {
        Log.i(TAG, "onRequestLocationPermissiont: 权限申请成功");
    }
}
