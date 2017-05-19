package com.example.leon.article.Activity;

import android.os.Bundle;
import android.view.View;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityAddbankcardBinding;
import com.example.leon.article.utils.CommonUtils;

/**
 * Created by leonseven on 2017/5/18.
 */

public class AddBankCardActivity extends ToolBarBaseActivity<ActivityAddbankcardBinding> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addbankcard);

        bindviews();
    }

    private void bindviews() {

        setTitle(CommonUtils.getString(R.string.add_bankcard));
        setNavigationView();

        binding.btnAddbankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
