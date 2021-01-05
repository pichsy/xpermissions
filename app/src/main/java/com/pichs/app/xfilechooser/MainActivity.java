package com.pichs.app.xfilechooser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pichs.permissions.PermissionUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtils.setPermissionInfoDialogEnable(true);
                PermissionUtils.setPermissionInfoDialogShowTwice(MainActivity.this, true);
                PermissionUtils.requestPermissionsWithDefaultDialog(MainActivity.this,
                        8912, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE
                        },
                        new PermissionUtils.OnPermissionCallback() {
                            @Override
                            public void onCallback(@Nullable String... deniedPermissions) {
                                if (deniedPermissions != null && deniedPermissions.length > 0) {
                                    Log.d("Permissions", "deniedPermissions:" + deniedPermissions.length);
                                    PermissionUtils.toAppSettingActivity(MainActivity.this);
                                }
                            }
                        }
                );
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtils.onActivityResult(this, requestCode, resultCode, data);
    }
}