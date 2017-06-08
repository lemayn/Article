package com.example.leon.article.Activity;

import android.os.Bundle;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityMoreInfoBinding;


public class MoreInfoActivity extends ToolBarBaseActivity<ActivityMoreInfoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        setNavigationView();
        hideHeaderInfo();

        setTitle(R.string.app_intro);
    }
}
