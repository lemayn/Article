package com.example.leon.article.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityModifyPasswordBinding;
import com.example.leon.article.utils.CommonUtils;
import com.example.leon.article.utils.PerfectClickListener;
import com.example.leon.article.utils.Validator;

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

                    //旧密码输入正确

                    //新密码两次输入相同

                    //请求接口更新密码

                    //回调成功，更新本地存储密码

                    //失败，提示用户
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
