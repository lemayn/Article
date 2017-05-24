package com.example.leon.article.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.bean.BankApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityWithdrawDepositBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.PerfectClickListener;
import com.google.gson.Gson;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class WithdrawDepositActivity extends ToolBarBaseActivity<ActivityWithdrawDepositBinding> {

    private BankApiBean bankApiBean;
    private int mDefaultChoice;
    private boolean hasData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_deposit);

        if (getIntent() != null) {
            bankApiBean = (BankApiBean) getIntent().getSerializableExtra(Constant.Intent_Extra.USER_BANK_INFO);
            mDefaultChoice = getIntent().getIntExtra(Constant.Intent_Extra.USER_BANK_HAS_CHOOSE, 0);
            hasData = bankApiBean != null && bankApiBean.getData() != null && bankApiBean.getData().size() > 0;
            if (hasData) {
                binding.setBean(bankApiBean.getData().get(mDefaultChoice));
            }
        }

        //打桩测试
        String json = "\uFEFF{\"code\":\"1\",\"msg\":\"\",\"data\":[{\"cid\":\"1\",\"bank\":\"中国银行\"," +
                "\"card\":\"6321\\t1231\\t1231\\t1231\\t1231\\t1231\",\"account_name\":\"老虎\"},{\"cid\":\"3\"," +
                "\"bank\":\"招商银行\",\"card\":\"6321\\t1231\\t1231\\t1231\\t1231\\t4567\",\"account_name\":\"老鹰\"}]}";
        bankApiBean = new Gson().fromJson(json, BankApiBean.class);
        hasData = bankApiBean != null && bankApiBean.getData() != null && bankApiBean.getData().size() > 0;
        if (hasData) {
            binding.setBean(bankApiBean.getData().get(mDefaultChoice));
        }

        initView();
        initEvent();
    }


    private void initView() {
        setTitle(CommonUtils.getString(R.string.withdraw_deposit));
        setNavigationView();
    }

    private void initEvent() {
        binding.tvBanks.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (hasData) {
                    String[] choices = new String[bankApiBean.getData().size()];
                    for (int i = 0; i < bankApiBean.getData().size(); i++) {
                        choices[i] = bankApiBean.getData().get(i).getBank();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawDepositActivity.this);
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setSingleChoiceItems(choices, mDefaultChoice, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDefaultChoice = which;
                            binding.setBean(bankApiBean.getData().get(which));
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        binding.btnConfirm.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                withdrawDeposit();
            }
        });
    }

    private void withdrawDeposit() {
        String money = binding.etWithdrawMoney.getText().toString().trim();
        String pwd = binding.etWithdrawPwd.getText().toString();
        if (TextUtils.isEmpty(money)) {
            Toast.makeText(WithdrawDepositActivity.this, "请输入提现金额", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(WithdrawDepositActivity.this, "请输入提现密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Integer.parseInt(money) < 100) {
            Toast.makeText(WithdrawDepositActivity.this, "最小提现金额为100", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hasData) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("cookie", "c1c5582ccdd4f5d16e37ae19c03f8dea");
            hashMap.put("sid", "c5etakebn6grkst6csqk2a5o62");
            hashMap.put("cid", bankApiBean.getData().get(mDefaultChoice).getCid());
            hashMap.put("account_name", bankApiBean.getData().get(mDefaultChoice).getAccount_name());
            hashMap.put("money", money);
            hashMap.put("password", pwd);
            ApiFactory.getApi().bank(Constant.Api.WITHDRAW_MONEY, hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BankApiBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BankApiBean apiBean) {
                            Toast.makeText(WithdrawDepositActivity.this, apiBean.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            if ("1".equals(apiBean.getCode())) {
                                finish();
                            }
                        }
                    });
        }
    }
}
