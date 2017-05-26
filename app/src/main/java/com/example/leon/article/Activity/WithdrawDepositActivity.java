package com.example.leon.article.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.BankApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityWithdrawDepositBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.SPUtil;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class WithdrawDepositActivity extends ToolBarBaseActivity<ActivityWithdrawDepositBinding> {

    private BankApiBean mBankApiBean;
    private int mDefaultChoice;
    private boolean hasData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_deposit);

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
                    String[] choices = new String[mBankApiBean.getData().size()];
                    for (int i = 0; i < mBankApiBean.getData().size(); i++) {
                        choices[i] = mBankApiBean.getData().get(i).getBank();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawDepositActivity.this);
                    builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setSingleChoiceItems(choices, mDefaultChoice, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDefaultChoice = which;
                            binding.setBean(mBankApiBean.getData().get(which));
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
        loadUserData();
        getUserBankInfo();
    }

    private void getUserBankInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        ApiFactory.getApi().bank(Constant.Api.USER_BANK, hashMap)
                .lift(new BaseValueValidOperator<BankApiBean>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BankApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(BankApiBean bankApiBean) {
                        dismissProgressDialog();
                        if (bankApiBean != null && "1".equals(bankApiBean.getCode())) {
                            mBankApiBean = bankApiBean;
                            hasData = mBankApiBean.getData() != null && mBankApiBean.getData().size() > 0;
                            if (hasData) {
                                binding.setBean(mBankApiBean.getData().get(mDefaultChoice));
                            }
                        }
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
            hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
            hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
            hashMap.put("cid", mBankApiBean.getData().get(mDefaultChoice).getCid());
            hashMap.put("account_name", mBankApiBean.getData().get(mDefaultChoice).getAccount_name());
            hashMap.put("money", money);
            hashMap.put("password", pwd);
            ApiFactory.getApi().bank(Constant.Api.WITHDRAW_MONEY, hashMap)
                    .lift(new BaseValueValidOperator<BankApiBean>())
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
