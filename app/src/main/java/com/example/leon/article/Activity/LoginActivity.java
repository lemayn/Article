package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leon.article.R;

/**
 *
 * loginActivity
 * Created by leonseven on 2017/5/9.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEdittext_phone;
    private EditText mEdittext_pwd;
    private Button mBtn_login;
    private Button mBtn_register;
    private TextView tv_forgetpwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();
    }


    private void bindViews() {

        mEdittext_phone = (EditText) findViewById(R.id.edittext_phone);
        mEdittext_pwd = (EditText) findViewById(R.id.edittext_pwd);
        mBtn_login = (Button) findViewById(R.id.btn_login);mBtn_login.setOnClickListener(this);
        mBtn_register = (Button) findViewById(R.id.btn_register);mBtn_register.setOnClickListener(this);
        tv_forgetpwd = (TextView) findViewById(R.id.tv_forgetpwd);tv_forgetpwd.setOnClickListener(this);

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

        mEdittext_phone.getText().toString().trim();
        mEdittext_pwd.getText().toString().trim();

        startActivity(new Intent(LoginActivity.this, VIPCenterActivity.class));
    }
}
