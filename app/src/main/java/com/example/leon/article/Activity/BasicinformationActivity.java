package com.example.leon.article.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityBasicinformationBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.GsonUtil;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.SPUtil;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Basic Information
 * Created by leonseven on 2017/5/16.
 */

public class BasicinformationActivity extends ToolBarBaseActivity<ActivityBasicinformationBinding> {

    private ArticleApiBean userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinformation);

        bindViews();

        initData();
    }


    private void bindViews() {
        setTitle(CommonUtils.getString(R.string.basic_info));
        setNavigationView();
    }

    private void initData() {
        loadUserData();

        String response = (String) SPUtil.get(Constant.Share_prf.USER_DATA, "");
        userData = GsonUtil.GsonToBean(response, ArticleApiBean.class);
        binding.setUserdata(userData);

        binding.btnRegister.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                boolean shouldEdit = TextUtils.isEmpty(userData.getData().getNickname()) ||
                        TextUtils.isEmpty(userData.getData().getWeixin()) ||
                        TextUtils.isEmpty(userData.getData().getEmail()) ||
                        TextUtils.isEmpty(userData.getData().getAlipay());
                if (shouldEdit) {
                    editUserData();
                } else {
                    Toast.makeText(BasicinformationActivity.this, "基本资料已完成！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnTellTest.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                requestVerifyFromServer();
            }
        });
    }

    private void requestVerifyFromServer() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        ApiFactory.getApi().article(Constant.Api.TELL_TEST, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(BasicinformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        final EditText editText = new EditText(BasicinformationActivity.this);
                        new AlertDialog.Builder(BasicinformationActivity.this)
                                .setTitle("验证")
                                .setMessage("请输入收到的验证码")
                                .setView(editText)
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        verifyTell(editText.getText().toString().trim());
                                    }
                                })
                                .setPositiveButton("取消", null)
                                .setCancelable(false)
                                .show();
                    }
                });

    }

    private void verifyTell(String verify) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        hashMap.put("verify", verify);
        ApiFactory.getApi().article(Constant.Api.TELL_RES, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(BasicinformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        binding.btnTellTest.setVisibility(View.GONE);
                        Toast.makeText(BasicinformationActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editUserData() {
        final String nickName = binding.editUseraccount.getText().toString().trim();
        String weiXin = binding.editWeixin.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        String alipay = binding.editAlipay.getText().toString().trim();

        if (TextUtils.isEmpty(nickName)) {
            binding.editUseraccount.setError("昵称不能为空");
            return;
        }
        if (TextUtils.isEmpty(weiXin)) {
            binding.editWeixin.setError("微信不能为空");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.editEmail.setError("邮箱不能为空");
            return;
        }
        if (TextUtils.isEmpty(alipay)) {
            binding.editAlipay.setError("支付宝不能为空");
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
        hashMap.put("nickname", nickName);
        hashMap.put("weixin", weiXin);
        hashMap.put("email", email);
        hashMap.put("alipay", alipay);
        ApiFactory.getApi().article(Constant.Api.V2_USER_INFO_EDIT, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showProgressDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        Toast.makeText(BasicinformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        loadBasicUserData();
                    }
                });
    }

    protected void loadBasicUserData() {
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
                        dismissProgressDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        baseBinding.setApibean(apiBean);
                        binding.setUserdata(apiBean);
                        userData = apiBean;
                        SPUtil.put(Constant.Share_prf.USER_DATA, GsonUtil.GsonString(apiBean));
                        Toast.makeText(BasicinformationActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
