package com.example.ystdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.coolpush.ctbusinesslibcore.CTGlobalAdConfig;
import com.coolpush.ctbusinesslibcore.utils.LocationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int REQUEST_CODE_PERMISSION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (needRequestPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }
        findViewById(R.id.tv_entrance).setOnClickListener(this);
        CTGlobalAdConfig.requestRealNetworkIp();
    }

    private boolean needRequestPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClick(View v) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationUtils.getInstance().initLocation(CTGlobalAdConfig.getApplication());
        switch (v.getId()) {
            case R.id.tv_entrance:
                EntranceActivity.launch(this);
                break;
        }
    }

}
