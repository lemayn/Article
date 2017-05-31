package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityRegisterBinding;
import com.example.leon.article.utils.CommonUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * RegisterActivity
 * Created by leonseven on 2017/5/15.
 */

public class RegisterActivity extends ToolBarBaseActivity<ActivityRegisterBinding> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        hideHeaderInfo();
        hideHeaderMoneyInfo();
        bindViews();
    }


    private void bindViews() {
        setTitle(getString(R.string.register));

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
            }

        });

    }

    private void register() {

        String account = binding.edittextName.getText().toString().trim();
        String pwd = binding.edittextPwd.getText().toString().trim();
        String pwd2 = binding.edittextPwd2.getText().toString().trim();
        String phone = binding.edittextPhone.getText().toString().trim();
        String qq =  binding.edittextQq.getText().toString().trim();

        Log.i("MyTest", account + pwd +" = " + pwd2 + phone + qq);

        if (account == null || account.equals("")){
            binding.edittextName.setError("账号不能为空");
        }
        if (account.length() < 6 || account.length() > 10){
            binding.edittextName.setError("账号不正确");
        }
        else if (pwd == null || pwd.equals("")){
            binding.edittextPwd.setError("密码不能为空");
        }
        if (pwd.length() < 6 || pwd.length() > 16){
            binding.edittextPwd.setError("密码长度不正确");
        }
        else if (pwd2 == null || pwd2.equals("")){
            binding.edittextPwd2.setError("重复密码不能为空");
        }
        else if (!pwd.equals(pwd2)){
            binding.edittextPwd2.setError("两次密码不一致");
        }
        else if (phone == null || phone.equals("")){
            binding.edittextPhone.setError("手机号不能为空");
        }
        else if (qq == null || qq.equals("")){
            binding.edittextQq.setError("QQ账号不能为空");
        }

        FormBody formBody = new FormBody.Builder()
                .add("name", account)
                .add("pwd", CommonUtils.getMD5Str(pwd))
                .add("repwd", CommonUtils.getMD5Str(pwd2))
                .add("tell", phone)
                .add("qq", qq)
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl + Api.Reg,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        Gson gson = new Gson();
                        Log.i("MyTest", "请求成功" + result.toString());

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                });

    }


}
