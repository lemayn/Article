package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.app;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityLoginBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.GsonUtil;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.utils.Validator;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * loginActivity
 * Created by leonseven on 2017/5/9.
 */

public class LoginActivity extends ToolBarBaseActivity<ActivityLoginBinding> implements View.OnClickListener {
    private boolean is_constraint_loin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        is_constraint_loin = getIntent().getBooleanExtra(Constant.Intent_Extra.IS_CONSTRAINT_LOIN, false);

        hideHeaderInfo();
        hideHeaderMoneyInfo();
        bindViews();

        boolean isLogin = !TextUtils.isEmpty((String) SPUtil.get(Constant.Share_prf.COOKIE, "")) ||
                !TextUtils.isEmpty((String) SPUtil.get(Constant.Share_prf.SID, ""));
        if (is_constraint_loin) {
            SPUtil.clear();
        } else {
            if (isLogin) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private void bindViews() {

        setTitle(getString(R.string.login));

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.tvForgetpwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                Login();
                break;

            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.tv_forgetpwd:
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                break;
        }

    }

    private void Login() {

        String name = binding.edittextPhone.getText().toString().trim();
        String pwd = binding.edittextPwd.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validator.isPassword(pwd)) {
            Toast.makeText(LoginActivity.this, "请输入正确的6~16位密码", Toast.LENGTH_SHORT).show();
            return;
        }

        final String finalPwd = CommonUtils.getMD5Str(pwd);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("pwd", finalPwd);
        ApiFactory.getApi().article(Constant.Api.LOGIN, hashMap)
                .lift(new BaseValueValidOperator<ArticleApiBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleApiBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArticleApiBean apiBean) {
                        SPUtil.put(Constant.Share_prf.LOGIN_RESPONSE, GsonUtil.GsonString(apiBean));
                        SPUtil.put(Constant.Share_prf.COOKIE, apiBean.getData().getCookie());
                        SPUtil.put(Constant.Share_prf.SID, apiBean.getData().getSid());
                        SPUtil.put(Constant.Share_prf.NAME, apiBean.getData().getMname());
                        SPUtil.put(Constant.Share_prf.PWD, finalPwd);
                        if (!is_constraint_loin) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        app.getInstance().exit();
    }
}
