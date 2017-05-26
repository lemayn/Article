package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leon.article.Http.Api;
import com.example.leon.article.Http.XHttpUtils;
import com.example.leon.article.R;
import com.example.leon.article.base.ToolBarBaseActivity;
import com.example.leon.article.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 *
 * loginActivity
 * Created by leonseven on 2017/5/9.
 */

public class LoginActivity extends ToolBarBaseActivity<ActivityLoginBinding> implements View.OnClickListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hideHeaderInfo();
        hideHeaderMoneyInfo();
        bindViews();
    }

    private void bindViews() {

        setTitle(getString(R.string.login));

        binding.btnLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.tvForgetpwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
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
        String pwd = binding.tvForgetpwd.getText().toString().trim();

        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("pwd", pwd)
                .build();
        XHttpUtils.getInstance().asyncPost(Api.baseurl + Api.Login,
                formBody, new XHttpUtils.HttpCallBack() {
                    @Override
                    public void onError(Request request, IOException e) {
                        Log.i("MyTest", "请求失败");
                    }

                    @Override
                    public void onSuccess(Request request, String result) {
                        Gson gson = new Gson();
                        Log.i("MyTest", "请求成功" + result.toString());
                    }
                });


        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
