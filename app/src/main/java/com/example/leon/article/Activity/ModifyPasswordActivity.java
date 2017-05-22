package com.example.leon.article.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityModifyPasswordBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.Validator;
import com.google.gson.Gson;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModifyPasswordActivity extends ToolBarBaseActivity<ActivityModifyPasswordBinding> {
    private TextInputEditText mOldPwd;
    private EditText mNewPwd;
    private EditText mRetypePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        initView();
        initEvent();
    }

    private void initView() {
        setTitle(CommonUtils.getString(R.string.password_modify));
        setNavigationView();
        hideHeaderInfo();
        mOldPwd = binding.etTypeOldPwd;
        mNewPwd = binding.etTypeNewPwd;
        mRetypePwd = binding.etRetypeNewPwd;
    }

    private void initEvent() {
        binding.btnConfirm.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String oldPwd = mOldPwd.getText().toString();
                String newPwd = mNewPwd.getText().toString();
                String retypePwd = mRetypePwd.getText().toString();

                boolean ispwds = validatorPwd(oldPwd, newPwd, retypePwd);

                if (ispwds) {
                    String oldpwdLocal = "a123456789";
                    //旧密码输入正确
                    //新密码两次输入相同
                    if (oldPwd.equals(oldpwdLocal) && newPwd.equals(retypePwd)) {
                        //请求接口更新密码
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("pwd", oldPwd);
                        hashMap.put("npwd", newPwd);
                        hashMap.put("renpwd", retypePwd);
                        hashMap.put("cookie", "90c393a671e233c67cabfe464bc99a6c");
                        hashMap.put("sid", "c5etakebn6grkst6csqk2a5o62");
                        ApiFactory.getApi().article(Constant.Api.EDIT_PASSWORD, hashMap)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ArticleApiBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(ModifyPasswordActivity.this, e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNext(ArticleApiBean apiBean) {
                                        Toast.makeText(ModifyPasswordActivity.this, new Gson().toJson(apiBean),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });
    }

    private boolean validatorPwd(String... pwds) {

        for (String pwd : pwds) {
            boolean isPassword = Validator.isPassword(pwd);
            if (!isPassword) {
                Toast.makeText(ModifyPasswordActivity.this, "请输入正确的6~16位密码", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
