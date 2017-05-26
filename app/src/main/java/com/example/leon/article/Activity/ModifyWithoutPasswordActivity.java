package com.example.leon.article.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.api.ApiFactory;
import com.example.leon.article.api.BaseValueValidOperator;
import com.example.leon.article.api.bean.ArticleApiBean;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityModifyPasswordBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.SPUtil;
import com.example.leon.article.utils.Validator;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModifyWithoutPasswordActivity extends ToolBarBaseActivity<ActivityModifyPasswordBinding> {
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
        setTitle(CommonUtils.getString(R.string.withdraw_password_modify));
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
                    //旧密码输入正确
                    //新密码两次输入相同
                    if (newPwd.equals(retypePwd)) {
                        //请求接口更新密码
                        HashMap<String, String> hashMap = new HashMap<>(5);
                        hashMap.put("password", oldPwd);
                        hashMap.put("npassword", newPwd);
                        hashMap.put("renpassword", retypePwd);
                        hashMap.put("cookie", (String) SPUtil.get(Constant.Share_prf.COOKIE, ""));
                        hashMap.put("sid", (String) SPUtil.get(Constant.Share_prf.SID, ""));
                        ApiFactory.getApi().article(Constant.Api.EDIT_MONEY_PASSWORD, hashMap)
                                .lift(new BaseValueValidOperator<ArticleApiBean>())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ArticleApiBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(ModifyWithoutPasswordActivity.this, e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        EmptyTexts();
                                    }

                                    @Override
                                    public void onNext(ArticleApiBean apiBean) {
                                        Toast.makeText(ModifyWithoutPasswordActivity.this, apiBean.getMsg(),
                                                Toast.LENGTH_SHORT).show();
                                        EmptyTexts();
                                    }
                                });
                    } else {
                        Toast.makeText(ModifyWithoutPasswordActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validatorPwd(String... pwds) {

        for (String pwd : pwds) {
            boolean isPassword = Validator.isPassword(pwd);
            if (!isPassword) {
                Toast.makeText(ModifyWithoutPasswordActivity.this, "请输入正确的6~16位密码", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void EmptyTexts() {
        mOldPwd.setText("");
        mNewPwd.setText("");
        mRetypePwd.setText("");
    }
}
