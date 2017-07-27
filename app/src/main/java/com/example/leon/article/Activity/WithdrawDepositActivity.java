package com.example.leon.article.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.api.bean.BankApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityWithdrawDepositBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.GsonUtil;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.SPUtil;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class WithdrawDepositActivity extends ToolBarBaseActivity<ActivityWithdrawDepositBinding> {

    private BankApiBean mBankApiBean;
    private int mDefaultChoice;
    private boolean hasData;
    /**
     * 提现方式
     */
    private static final int TYPE_BANK = 0;
    private static final int TYPE_ALIBABA = 1;
    private static final int TYPE_TENCENT = 2;
    /**
     * 默认提现方式
     */
    private int mDefaultType = 0;
    private String[] types;

    /**
     * 线程安全的自增，2个接口请求完成隐藏progress
     */
    private AtomicInteger mProgressCount = new AtomicInteger(0);

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
        types = new String[]{"银行卡", "支付宝", "微信"};
        binding.tvType.setText(types[mDefaultType]);
        showAndHideByType();
        binding.tvType.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawDepositActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setSingleChoiceItems(types, mDefaultType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDefaultType = which;
                        binding.tvType.setText(types[mDefaultType]);
                        showAndHideByType();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
        loadWithdrawUserData();
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
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (mProgressCount.incrementAndGet() == 2) {
                            mProgressCount.set(0);
                            dismissProgressDialog();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BankApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BankApiBean bankApiBean) {
                        if (bankApiBean != null && "1".equals(bankApiBean.getCode())) {
                            mBankApiBean = bankApiBean;
                            hasData = mBankApiBean.getData() != null && mBankApiBean.getData().size() > 0;
                            if (hasData) {
                                binding.setBean(mBankApiBean.getData().get(mDefaultChoice));
                            } else {
                                Toast.makeText(WithdrawDepositActivity.this, "您还未绑定银行卡！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    protected void loadWithdrawUserData() {
        String userData = (String) SPUtil.get(Constant.Share_prf.USER_DATA, "");
        if (!TextUtils.isEmpty(userData)) {
            ArticleApiBean articleApiBean = GsonUtil.GsonToBean(userData, ArticleApiBean.class);
            baseBinding.setApibean(articleApiBean);
            binding.setUser(articleApiBean.getData());
            if (mProgressCount.incrementAndGet() == 2) {
                mProgressCount.set(0);
                dismissProgressDialog();
            }
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
            hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
            ApiFactory.getApi().article(Constant.Api.USER_DATA, hashMap)
                    .lift(new BaseValueValidOperator<ArticleApiBean>())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            showProgressDialog();
                        }
                    })
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            if (mProgressCount.incrementAndGet() == 2) {
                                mProgressCount.set(0);
                                dismissProgressDialog();
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArticleApiBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(WithdrawDepositActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(ArticleApiBean apiBean) {
                            baseBinding.setApibean(apiBean);
                            binding.setUser(apiBean.getData());
                            SPUtil.put(Constant.Share_prf.USER_DATA, GsonUtil.GsonString(apiBean));
                        }
                    });
        }
    }


    private void showAndHideByType() {
        switch (mDefaultType) {
            case TYPE_BANK:
                setBankVisible(View.VISIBLE);
                setAlibabaVisible(View.GONE);
                setTencentVisible(View.GONE);
                break;
            case TYPE_ALIBABA:
                setBankVisible(View.GONE);
                setAlibabaVisible(View.VISIBLE);
                setTencentVisible(View.GONE);
                break;
            case TYPE_TENCENT:
                setBankVisible(View.GONE);
                setAlibabaVisible(View.GONE);
                setTencentVisible(View.VISIBLE);
                break;
        }
    }

    private void setBankVisible(int state) {
        binding.bank.setVisibility(state);
        binding.bankDivide.setVisibility(state);
        binding.accountName.setVisibility(state);
        binding.accountDivide.setVisibility(state);
        binding.card.setVisibility(state);
        binding.cardDivide.setVisibility(state);
    }

    private void setTencentVisible(int state) {
        binding.weixin.setVisibility(state);
        binding.weixinDivide.setVisibility(state);
    }

    private void setAlibabaVisible(int state) {
        binding.alipay.setVisibility(state);
        binding.alipayDivide.setVisibility(state);
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
        if (Integer.parseInt(money) < 50) {
            Toast.makeText(WithdrawDepositActivity.this, "最小提现金额为50", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hasData) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
            hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
            hashMap.put("money", money);
            hashMap.put("password", pwd);
            if (mDefaultType == TYPE_BANK) {
                hashMap.put("cid", mBankApiBean.getData().get(mDefaultChoice).getCid());
                hashMap.put("account_name", mBankApiBean.getData().get(mDefaultChoice).getAccount_name());
            }
            hashMap.put("type", String.valueOf(mDefaultType));
            ApiFactory.getApi().bank(Constant.Api.V2_WITHDRAW_MONEY, hashMap)
                    .lift(new BaseValueValidOperator<BankApiBean>())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            SystemClock.sleep(500);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            showProgressDialog();
                        }
                    })
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            dismissProgressDialog();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BankApiBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(WithdrawDepositActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
