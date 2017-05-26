package com.example.leon.article.Activity;

import android.os.Bundle;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityBasicinformationBinding;
import com.example.leon.article.utils.CommonUtils;

/**
 * Basic Information
 * Created by leonseven on 2017/5/16.
 */

public class BasicinformationActivity extends ToolBarBaseActivity<ActivityBasicinformationBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinformation);

        bindViews();

        initData();
    }

    private void initData() {
        loadUserData();
    }

    private void bindViews() {

        setTitle(CommonUtils.getString(R.string.basic_info));
        setNavigationView();

    }

}
