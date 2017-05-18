package com.example.leon.article.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.leon.article.R;

/**
 * RegisterActivity
 * Created by leonseven on 2017/5/15.
 */

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout mLine1;
    private EditText mEdittext_name;
    private EditText mEdittext_pwd;
    private EditText mEdittext_pwd2;
    private EditText mEdittext_phone;
    private EditText mEdittext_qq;
    private Button mBtn_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();
    }


    private void bindViews() {

        mLine1 = (LinearLayout) findViewById(R.id.line1);
        mEdittext_name = (EditText) findViewById(R.id.edittext_name);
        mEdittext_pwd = (EditText) findViewById(R.id.edittext_pwd);
        mEdittext_pwd2 = (EditText) findViewById(R.id.edittext_pwd2);
        mEdittext_phone = (EditText) findViewById(R.id.edittext_phone);
        mEdittext_qq = (EditText) findViewById(R.id.edittext_qq);
        mBtn_register = (Button) findViewById(R.id.btn_register);

        mBtn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }


}
