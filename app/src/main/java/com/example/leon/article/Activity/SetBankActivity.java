package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivitySetbankBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.DataBindingUtil;

/**
 * 银行设置
 * Created by leonseven on 2017/5/18.
 */

public class SetBankActivity extends ToolBarBaseActivity<ActivitySetbankBinding> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setbank);
        bindViews();
    }

    private void bindViews() {

        setTitle(CommonUtils.getString(R.string.basic_info));
        setNavigationView();

//        binding.linLayout1

        //添加银行卡

        binding.btnAddbankcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetBankActivity.this, AddBankCardActivity.class));
            }
        });
    }
}
