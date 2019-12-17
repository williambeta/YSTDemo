package com.example.ystdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.coolpush.ctbusinesslibcore.CTAdBaseLayout;
import com.coolpush.ctbusinesslibcore.callback.CTAdRenderCallback;

import java.util.Arrays;
import java.util.List;

public class EntranceActivity extends Activity {
    private static final String TAG = "EntranceActivity";

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, EntranceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        final CTAdBaseLayout ctAdBaseLayout = findViewById(R.id.ct_ad);
        ctAdBaseLayout.setCTAdRenderCallback(new CTAdRenderCallback() {
            @Override
            public void onAdRenderSucceed(boolean isShowRedDot) {
                Log.d(TAG, "onAdRenderSucceed() called with: isShowRedDot = [" + isShowRedDot +
                        "]");
                ctAdBaseLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdRenderFailed() {

            }
        });
        List<String> positionList = Arrays.asList("455442", "455443", "455444", "455445", "455446"
                , "455447", "455448", "455449");
        ctAdBaseLayout.refreshAd(positionList);
    }
}
