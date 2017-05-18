package com.example.leon.article.Activity;

import android.os.Bundle;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityWithdrawDepositBinding;


public class WithdrawDepositActivity extends ToolBarBaseActivity<ActivityWithdrawDepositBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_deposit);

    }
}
