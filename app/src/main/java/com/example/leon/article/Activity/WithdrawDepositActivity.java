package com.example.leon.article.Activity;

import android.os.Bundle;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityWithdrawDepositBinding;
import com.example.leon.article.utils.CommonUtils;


public class WithdrawDepositActivity extends ToolBarBaseActivity<ActivityWithdrawDepositBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_deposit);

        initView();
    }

    private void initView() {
        setTitle(CommonUtils.getString(R.string.withdraw_deposit));
        setNavigationView();
    }
}
