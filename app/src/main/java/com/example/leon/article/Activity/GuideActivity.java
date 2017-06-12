package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.leon.article.R;

import cn.bingoogolapple.bgabanner.BGABanner;

public class GuideActivity extends AppCompatActivity {

    private BGABanner guideBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        initEvent();

        initDate();
    }

    private void initDate() {
        guideBanner.setData(R.drawable.qd1,R.drawable.qd2,R.drawable.qd3);
    }

    private void initEvent() {
        guideBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void initView() {
        guideBanner = (BGABanner) findViewById(R.id.banner_guide_foreground);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
