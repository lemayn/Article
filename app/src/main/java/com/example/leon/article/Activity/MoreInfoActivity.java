package com.example.leon.article.Activity;

import android.os.Bundle;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityMoreInfoBinding;
import com.example.leon.article.utils.Constant;


public class MoreInfoActivity extends ToolBarBaseActivity<ActivityMoreInfoBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        setNavigationView();
        hideHeaderInfo();

        setTitle(R.string.app_intro);

        loadExplain();
    }

    private void loadExplain() {
        binding.webView.loadUrl(Constant.Api.ABOOUT_IUS);
    }
}
